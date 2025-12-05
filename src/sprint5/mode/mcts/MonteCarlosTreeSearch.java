package sprint5.mode.mcts;

import java.util.ArrayList;
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
public class MonteCarlosTreeSearch {
	private static final double UCB_CONSTANT = Math.sqrt(2);
	private final Player computerPlayer;
	private final Player opponentPlayer;
	private Game4 game;
	private final int iterations; // iteration the algorithm will use
	private  final Random  random;
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
	public MonteCarlosTreeSearch(Player computerPlayer, Player opponentPlayer,
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
		return switch (difficulty) {
		case MEDIUM -> 1600;
		case HARD -> 8000;
		case EXPERT -> 25000;
		case RANDOM, EASY -> throw new IllegalArgumentException(
	            "MCTS should only be used for MEDIUM, HARD, and EXPERT. " +
	            "Got: " + difficulty + ". " +
	            "Check StrategyFactory - RANDOM should use RandomStrategy, " +
	            "EASY should use EasyStrategy."
	        );
		};
	}

	/* findBestMove used to find the best move to make */
	public Move findBestMove(Board board) {
		
		List<Move> availableMoves = getAvailableMoves(board);
		if(availableMoves.isEmpty())
		{
			return  null;
		}
		if(availableMoves.size() == 1)
		{
			return availableMoves.get(0);
		}
		totalSimulations = 0;
		// initializing the root node
		MCTSNode root = createRootNode(board);
		
		//run for specified period of simulation
		for(int i = 0; i < iterations; i++)
		{
			totalSimulations++;
			//select the promising node to explore
			MCTSNode selectPromisingNode = selectPromisingNode(root);
			MCTSNode expansion =  expandNode(selectPromisingNode);
			double simulationResult = simulateRandomPlayout(expansion);
			backPropagateResult(expansion, simulationResult);

		}
		

		return selectBestMove(root);//return the most promising node
	}

	//Generate all available movs for the current board
	private List<Move>getAvailableMoves(Board board)
	{
		List<Move> moves = new ArrayList<>();
        List<Board.Cell> emptyCells = board.getEmptyCells();
        
        for (Board.Cell cell : emptyCells) {
            moves.add(new Move(cell.row(), cell.col(), 'S'));
            moves.add(new Move(cell.row(), cell.col(), 'O'));
        }
        
        return moves;
	}
	//Create the root node for mcts
	private MCTSNode createRootNode(Board board)
	{
		char computerLetter = computerPlayer.getSymbol();
		char opponentLetter = opponentPlayer.getSymbol();
		
		return  new MCTSNode(null, board.copy(), 
				computerLetter,computerLetter, opponentLetter,
				null //no parent for the root
				 );

	}
	

	
	private MCTSNode selectPromisingNode(MCTSNode node) {
		while (node.isFullyExpanded() && !node.children.isEmpty() &&
				!node.isTerminal())  {
			node = selectBestChildUCB(node);
		}
		return node;
	}

	private MCTSNode selectBestChildUCB(MCTSNode node) {
		MCTSNode best = null; // initialize the best node to null
		double bestValue = Double.NEGATIVE_INFINITY;// track the maximum value stored so far
		// iterate through all the child node
		for (MCTSNode child : node.children) {
			double upperConfidenceBoundValue = calculateUCB(node, child);
			
			if (upperConfidenceBoundValue > bestValue) {
				bestValue = upperConfidenceBoundValue;
				best = child;
			}
		}
		return best;
	}

	//private calculate thbe UCB1 for a node
	private double calculateUCB(MCTSNode parent, MCTSNode child)
	{
		//prioritize unvisited nodes
		if(child.visits == 0)
		{
			return Double.MAX_VALUE;
		}
		
		//Exploitation: average score
		double exploitation = child.score /child.visits;
		
		double exploration = UCB_CONSTANT * Math.sqrt(Math.log(parent.visits )/ child.visits);
		
		return exploitation + exploration;
	}
	private MCTSNode expandNode(MCTSNode node) {
		//if no expansion possible return node
		if (node.isTerminal() || node.notTriedMoves.isEmpty()) {
			return node; // no expansion
		}

		Move move = node.notTriedMoves.remove(random.nextInt(node.notTriedMoves.size()));
		
		Board newState = node.state.copy();// make a copy of the board
		newState.makeMove(move.getRow(), move.getCol(), move.getLetter());

		boolean formedSOS =
	            BoardSearcher.findSOSPatterns(newState, move.getRow(), move.getCol(), null).size() > 0;

	    char nextPlayer = determineNextPlayer(node.playerToMove, formedSOS);
		// add the new node to parent
		MCTSNode childNode = new MCTSNode(move, newState, nextPlayer,
		        node.computerLetter, node.opponentLetter, node);
		    node.children.add(childNode);
		return childNode;
	}

	//Determine the next player to player based on game mode and SOS formations
	private char determineNextPlayer(char current, boolean scored)
	{
		 if (game.getMode() == GameMode4.GENERAL) {
	            return scored ? current : opponentOf(current);
	        }
	        // SIMPLE mode
	        return opponentOf(current);
	}
	private char opponentOf(char p) {
        return (p == computerPlayer.getSymbol())
                ? opponentPlayer.getSymbol()
                : computerPlayer.getSymbol();
    }
	
	// Simulation method
	private double simulateRandomPlayout(MCTSNode node) {
		Board simBoard = node.state.copy();
		char currentPlayer = node.playerToMove; // tracks whose turn it is
		GameMode4 mode = game.getMode();

		int computerScore = 0;
		int opponentScore = 0;

		//Play random moves until board is full or max depth is reached
		int maxDepth = simBoard.getEmptyCells().size();
		for (int i = 0; i < maxDepth; i++) {
			List<Board.Cell> empty = simBoard.getEmptyCells();

			//no move or game is over
			if (empty.isEmpty()) {
				break;
			}


			//Try to form SOS
			Move move = selectSimulationMove(simBoard);
	        
	       
			simBoard.makeMove(move.getRow(), move.getCol(), move.getLetter());
			//check how many SOS formed
			int sosFormed = BoardSearcher.findSOSPatterns(simBoard, move.getRow(), move.getCol(), null).size();

			//award points
			if (currentPlayer == node.computerLetter) {
				computerScore += sosFormed ;

			} else {
				opponentScore += sosFormed;
			}
			
			if (mode == GameMode4.SIMPLE && sosFormed > 0) {
			    break;
			}

			if (mode == GameMode4.GENERAL) {
                if (sosFormed == 0) {
                    currentPlayer = (currentPlayer == node.computerLetter)
                            ? node.opponentLetter
                            : node.computerLetter;
                }
            } else {
                // SIMPLE MODE: turn always switches
                currentPlayer = (currentPlayer == node.computerLetter)
                        ? node.opponentLetter
                        : node.computerLetter;
            }
        }
		return normalizeScore(computerScore, opponentScore);
	}

	//Prefer moves that form SOS, otherwise random
	private Move selectSimulationMove(Board board)
	{
		List<Board.Cell> empty = board.getEmptyCells();
        Board.Cell c = empty.get(random.nextInt(empty.size()));
        char letter = random.nextBoolean() ? 'S' : 'O';
        return new Move(c.row(), c.col(), letter);
	  
	  
	}//end of the method
	
	//normalize score between -1 and 1
	private double normalizeScore(int computerScore, int opponentScore)
	{
		 if (computerScore > opponentScore) {
		        return 1.0;  // Computer winning
		    } else if (computerScore < opponentScore) {
		        return -1.0;  // Opponent winning
		    } else {
		        return 0.0;  // Draw
		    }
	}
	private void backPropagateResult(MCTSNode node, double value) {

		MCTSNode current = node;
       while(current != null)
       {
    	   current.visits ++;
    	   if(current.playerToMove != current.computerLetter) {
				current.score += -value;
			} else {
				current.score += value;
			}
    	   current = current.parent;
       }
	}
	

	//Helper method for for finding a move that scores
	private Move findScoringMove(Board board) {
	    List<Board.Cell> empty = board.getEmptyCells();
	    for (Board.Cell cell : empty) {
	       //check s
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
	
	//Select the best move from the root's children
	private Move selectBestMove(MCTSNode root)
	{
		//filter children with sufficient visits
		List<MCTSNode>qualified = new ArrayList<>();
		for (MCTSNode child : root.children)
		{

			if(child.visits >= 3)
			{
				qualified.add(child);

			}
		}
		
		if (qualified.isEmpty()) {
			MCTSNode fallback = getMostVisitedChild(root);
			return (fallback != null) ? fallback.move : null;
		}
		//sort scores
		qualified.sort((a, b) -> 
		Double.compare(b.score / b.visits, a.score / a.visits));
		return qualified.get(0).move;
	}
	//Get the most visited child of the nod
	private MCTSNode getMostVisitedChild(MCTSNode node)
	{
		if (node.children.isEmpty()) {
	        return null;  
	    }
		MCTSNode mostVisited = null;
		int maxVisits = -1;
		for (MCTSNode child : node.children)
		{
			if(child.visits > maxVisits)
			{
				maxVisits = child.visits;
				mostVisited = child;
			}
		}
	return mostVisited;
	}
	
	//Getting the number of simulation
	public int getTotalSimulations() {
		return totalSimulations;
	}
	
	
	private static class MCTSNode {
		Move move; // stores the current move made
		Board state; // state at the move
		char playerToMove; // player move
		char computerLetter;
		char opponentLetter;
		MCTSNode parent; // used for backpropagation
		List<MCTSNode> children;
		List<Move> notTriedMoves;
		int visits;
		double score;

		MCTSNode(Move move, Board state, char playerToMove, char computerLetter, char opponentLetter, MCTSNode parent) {
			this.move = move;
			this.state = state;
			this.playerToMove = playerToMove;
			this.computerLetter = computerLetter;
			this.opponentLetter = opponentLetter;
			this.children = new ArrayList<>();
			this.parent = parent;
			this.notTriedMoves = generatePossibleMove(state);//generate only legal moves
			this.visits = 0;
			this.score = 0.0;
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
		//fully expand when no untried moves remain
		boolean isFullyExpanded()
		{
			return notTriedMoves.isEmpty();
		}
	}

}
