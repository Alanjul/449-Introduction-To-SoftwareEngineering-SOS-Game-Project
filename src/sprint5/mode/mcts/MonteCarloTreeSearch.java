package sprint5.mode.mcts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import sprint5.mode.GameMode.Game4;
import sprint5.mode.GameMode.GameMode4;
import sprint5.mode.board.Board;
import sprint5.mode.computerPlayer.*;
import sprint5.mode.move.Move;
import sprint5.mode.player.Player;
import sprint5.util.BoardSearcher;

/**
 * This is the implementation of Monte carlo Tree Search for my board game to
 * provide better exploration and use promising paths Performs selection,
 * expansion, simulation, and backPropagation
 */
public class MonteCarloTreeSearch {
	private static final double UCB_CONSTANT = Math.sqrt(2);
	private static final double K_RAVE = 400.0;
	private static final int MAX_SIMULATION_DEPTH = 100;
	private static final double STRATEGIC_SIMULATION_PROBABILITY = 0.80;
	private static final double PROGRESSIVE_WEIGHT = 0.3;
	private final Player computerPlayer;
	private final Player opponentPlayer;
	private Game4 game;
	private final int iterations; // iteration the algorithm will use
	private final Random random;
	private int totalSimulations;

	/*
	 * Constructor for the MCTS class
	 *
	 * @param computerPlayer
	 *
	 * @param opponentPlayer
	 *
	 * @param difficulty level
	 */
	public MonteCarloTreeSearch(Player computerPlayer, Player opponentPlayer,
			ComputerPlayer.LevelsOfDifficulty difficulty, Game4 game) {
		this.computerPlayer = computerPlayer;
		this.opponentPlayer = opponentPlayer;
		this.iterations = determineIteration(difficulty);
		this.random = new Random();
		this.totalSimulations = 0;
		this.game = game;
	}

	/* Get the iteration based on difficulty level */
	private int determineIteration(ComputerPlayer.LevelsOfDifficulty difficulty) {
		int baseInteration = switch (difficulty) {
		case MEDIUM -> 10000;
		case HARD -> 40000;
		case EXPERT -> 100000;
		default -> throw new IllegalArgumentException("Invalid difficulty");
		};
		return baseInteration;
	}

	/* findBestMove used to find the best move to make */
	public Move findBestMove(Board board) {

		List<Move> availableMoves = getAvailableMoves(board);
		if (availableMoves.isEmpty()) {
			return null;
		}
		if (availableMoves.size() == 1) {
			return availableMoves.get(0);
		}
		totalSimulations = 0;
		// initializing the root node
		MCTSNode root = createRootNode(board);

		// run for specified period of simulation
		for (int i = 0; i < iterations; i++) {
			totalSimulations++;
			// select the promising node to explore
			MCTSNode selected = selectPromisingNode(root);
			if (!selected.isTerminal() && !selected.notTriedMoves.isEmpty()) {
				selected = expandNode(selected);
			}
			List<Move> simMoves = new ArrayList<>();
			double simulationResult = simulateRandomPlayout(selected, simMoves);
			backPropagateResult(selected, simulationResult, simMoves);

		}

		return selectBestMove(root).move;
	}

	// Generate all available movs for the current board
	private List<Move> getAvailableMoves(Board board) {
		List<Move> moves = new ArrayList<>();
		List<Board.Cell> emptyCells = board.getEmptyCells();

		for (Board.Cell cell : emptyCells) {
			moves.add(new Move(cell.row(), cell.col(), 'S'));
			moves.add(new Move(cell.row(), cell.col(), 'O'));
		}

		return moves;
	}

	// Create the root node for mcts
	private MCTSNode createRootNode(Board board) {
		char computerLetter = computerPlayer.getSymbol();
		char opponentLetter = opponentPlayer.getSymbol();

		return new MCTSNode(null, board.copy(), computerLetter, computerLetter, opponentLetter, null // no parent for
																										// the root
		);

	}

	private MCTSNode selectPromisingNode(MCTSNode node) {
		while (node.isFullyExpanded() && !node.children.isEmpty() && !node.isTerminal()) {
			node = selectBestChildUCB(node);
		}
		return node;
	}

	private MCTSNode selectBestChildUCB(MCTSNode node) {
		MCTSNode best = null; // initialize the best node to null
		double bestValue = Double.NEGATIVE_INFINITY;// track the maximum value stored so far

		for (MCTSNode child : node.children) {
			double exploitation = child.visits > 0 ? child.score / child.visits : 0;
			double raveExploitation = child.raveVisits > 0 ? child.raveScore / child.raveVisits : 0;

			double beta = Math.sqrt(K_RAVE / (3.0 * node.visits + K_RAVE));
			double combinedValue = (1 - beta) * exploitation + beta * raveExploitation;

			// combinedValue = ALPHA * combinedValue + (1 - ALPHA) * exploitation;
			double ucb = UCB_CONSTANT * Math.sqrt(Math.log(node.visits + 1) / (child.visits + 1e-6));
			double progressive = PROGRESSIVE_WEIGHT * child.heuristicValue;

			double nodeValue = combinedValue + ucb + progressive;

			if (nodeValue > bestValue) {
				bestValue = nodeValue;
				best = child;

			}
		}
		return best;
	}

	private MCTSNode expandNode(MCTSNode node) {
		// if no expansion possible return node
		if (node.isTerminal() || node.notTriedMoves.isEmpty()) {
			return node; // no expansion
		}

		Move move = node.notTriedMoves.remove(random.nextInt(node.notTriedMoves.size()));

		Board newState = node.state.copy();// make a copy of the board
		newState.makeMove(move.getRow(), move.getCol(), move.getLetter());

		boolean formedSOS = BoardSearcher.findSOSPatterns(newState, move.getRow(), move.getCol(), null).size() > 0;

		char nextPlayer = determineNextPlayer(node.playerToMove, formedSOS);

		MCTSNode childNode = new MCTSNode(move, newState, nextPlayer, node.computerLetter, node.opponentLetter, node);
		childNode.heuristicValue = heuristic(newState, move); // * WEIGHT;
		node.children.add(childNode);
		return childNode;
	}

	// Determine the next player to player based on game mode and SOS formations
	private char determineNextPlayer(char current, boolean scored) {
		if (game.getMode() == GameMode4.GENERAL) {
			return scored ? current : opponentOf(current);
		}
		// SIMPLE mode
		return opponentOf(current);
	}

	private char opponentOf(char p) {
		return (p == computerPlayer.getSymbol()) ? opponentPlayer.getSymbol() : computerPlayer.getSymbol();
	}

	// Simulation method
	private double simulateRandomPlayout(MCTSNode node, List<Move> simMoves) {
		Board simBoard = node.state.copy();
		char currentPlayer = node.playerToMove; // tracks whose turn it is
		GameMode4 mode = game.getMode();

		int computerScore = 0;
		int opponentScore = 0;
		int depth = 0;

		// Play random moves until board is full or max depth is reached
		while (!simBoard.getEmptyCells().isEmpty() && depth < MAX_SIMULATION_DEPTH) {
			depth++;
			Move move = selectSimulationMove(simBoard);
			simMoves.add(move);

			boolean success = simBoard.makeMove(move.getRow(), move.getCol(), move.getLetter());
			if (!success) {
				break;
			}
			// check how many SOS formed
			int sosFormed = BoardSearcher.findSOSPatterns(simBoard, move.getRow(), move.getCol(), null).size();

			// award points
			if (currentPlayer == node.computerLetter) {
				computerScore += sosFormed;

			} else {
				opponentScore += sosFormed;
			}
			if (mode == GameMode4.SIMPLE && sosFormed > 0) {
				break;
			}

			if (mode == GameMode4.GENERAL) {
				if (sosFormed == 0) {
					currentPlayer = opponentOf(currentPlayer);
				}
			} else {
				currentPlayer = opponentOf(currentPlayer);
			}
		}

		if (computerScore > opponentScore)
			return +1;
		if (computerScore < opponentScore)
			return -1;
		return 0.0;

	}

	private Move selectSimulationMove(Board board) {
		if (random.nextDouble() < STRATEGIC_SIMULATION_PROBABILITY) {
			Move scoringMove = findScoringMove(board);
			if (scoringMove != null) {
				return scoringMove;
			}
		}
		List<Board.Cell> empty = board.getEmptyCells();
		Board.Cell c = empty.get(random.nextInt(empty.size()));
		char letter = random.nextBoolean() ? 'S' : 'O';
		return new Move(c.row(), c.col(), letter);

	}// end of the method

	private void backPropagateResult(MCTSNode node, double value, List<Move> simMoves) {

		MCTSNode current = node;
		while (current != null) {
			current.visits++;
			boolean isComputerNode = (current.playerToMove == current.computerLetter);
			if (isComputerNode) {
				current.score += value;
			} else {
				current.score -= value;
			}
			for (Move m : simMoves) {
				MCTSNode child = current.findChildByMove(m);
				if (child != null) {
					child.raveVisits++;
					boolean isChildComputerNode = (child.playerToMove == child.computerLetter);
					if (isChildComputerNode)
						child.raveScore += value;
					else
						child.raveScore -= value;
				}
			}
			current = current.parent;
		}
	}

	// Helper method for for finding a move that scores
	private Move findScoringMove(Board board) {
		List<Board.Cell> empty = board.getEmptyCells();
		List<Board.Cell> shuffled = new ArrayList<>(empty);
		Collections.shuffle(shuffled, random);
		for (Board.Cell cell : shuffled) {
			// check s
			if (BoardSearcher.countSOS(board, cell.row(), cell.col(), 'S') > 0) {
				return new Move(cell.row(), cell.col(), 'S');
			}
			// check O
			if (BoardSearcher.countSOS(board, cell.row(), cell.col(), 'O') > 0) {
				return new Move(cell.row(), cell.col(), 'O');
			}
		}
		return null;

	}

	// Select the best move from the root's children
	private MCTSNode selectBestMove(MCTSNode root) {
		MCTSNode bestChild = null;
		double bestWinRate = Double.NEGATIVE_INFINITY;
		// double bestWinRate = Double.NEGATIVE_INFINITY;
		for (MCTSNode child : root.children) {
			if (child.visits == 0)
				continue;
			double winRate = child.score / child.visits;

			if (winRate > bestWinRate) {
				bestWinRate = winRate;
				bestChild = child;

			}
		}

		return bestChild != null ? bestChild : root.children.get(0);
	}

	// Getting the number of simulation
	public int getTotalSimulations() {
		return totalSimulations;
	}

	// Heuristics to find the score with best chance of forming SOS
	private double heuristic(Board state, Move move) {
		return BoardSearcher.countPotentialSOS(state, move.getRow(), move.getCol()) * 0.1;
	}

	private static class MCTSNode {
		Move move; // stores the current move made
		Board state; // state at the move
		char playerToMove; // player move
		char computerLetter;
		char opponentLetter;
		MCTSNode parent;
		List<MCTSNode> children;
		List<Move> notTriedMoves;
		int visits = 0;
		double score = 0;
		int raveVisits = 0;
		double raveScore = 0;
		double heuristicValue = 0.0;

		MCTSNode(Move move, Board state, char playerToMove, char computerLetter, char opponentLetter, MCTSNode parent) {
			this.move = move;
			this.state = state;
			this.playerToMove = playerToMove;
			this.computerLetter = computerLetter;
			this.opponentLetter = opponentLetter;
			this.children = new ArrayList<>();
			this.parent = parent;
			this.notTriedMoves = generatePossibleMove(state);// generate only legal moves

		}

		// Generate all moves
		private static List<Move> generatePossibleMove(Board board) {
			List<Move> moves = new ArrayList<>();
			List<Board.Cell> emptyCells = board.getEmptyCells();// retrieve empty board
			for (Board.Cell cell : emptyCells) {
				moves.add(new Move(cell.row(), cell.col(), 'S'));
				moves.add(new Move(cell.row(), cell.col(), 'O'));
			}
			return moves;
		}

		// True terminal if no empty cell left
		boolean isTerminal() {
			return state.getEmptyCells().isEmpty();
		}

		// fully expand when no untried moves remain
		boolean isFullyExpanded() {
			return notTriedMoves.isEmpty();
		}

		MCTSNode findChildByMove(Move m) {
			for (MCTSNode c : children) {
				if (c.move != null && c.move.equals(m))
					return c;

			}
			return null;
		}
	}

}
