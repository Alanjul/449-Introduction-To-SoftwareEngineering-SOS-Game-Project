package sprint4.mode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sprint4.util.BoardSearcher;

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
		this.iterations = getIteration(difficulty);
		this.random = new Random();
		this.totalSimulations = 0;
		this.game = game;
	}

	/* Get the iteration based on difficulty level */
	private int getIteration(ComputerPlayer.LevelsOfDifficulty difficulty) {
		return switch (difficulty) {
		case RANDOM -> 50;
		case EASY -> 300;
		case MEDIUM -> 8000;
		case HARD -> 12000;
		case EXPERT -> 25000;
		};
	}

	/* findBestMove used to find the best move to make */
	public Move findBestMove(Board board, Game4 game) {
		totalSimulations = 0;
		List<Move> availableMoves = getAvailableMoves(board);
		if(availableMoves.isEmpty())
		{
			return  null;
		}
		if(availableMoves.size() == 1)
		{
			return availableMoves.get(0);
		}
		// initializing the root node
		char computerLetter = computerPlayer.getSymbol();
		char opponentLetter = opponentPlayer.getSymbol();
		MCTSNode root = new MCTSNode(null, board.copy(), computerLetter,computerLetter, opponentLetter, null);
		
		//run for specified period of simulation
		for(int i = 0; i < iterations; i++)
		{
			totalSimulations++;
			//select the promising node to explore
			MCTSNode selectPromisingNode = select(root);
			MCTSNode expansion =  expand(selectPromisingNode);
			double simulationResult = simulate(expansion, game);
			backPropagate(expansion, simulationResult);
			
		}
		Move bestMove = getBestChildrenMove(root);

		return bestMove;//return the most promising node
	}

	private List<Move> getAvailableMoves(Board board) {
		List<Move> moves = new ArrayList<>();
		
		List<Board.Cell> emptyCells = board.getEmptyCells();
		for (Board.Cell cell : emptyCells) {
			moves.add(new Move(cell.row(), cell.col(), 'S')); // place s
			moves.add(new Move(cell.row(), cell.col(), 'O')); // places o
		}
		return moves;
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
		int visits = 0;
		double score = 0.0;

		MCTSNode(Move move, Board state, char playerToMove, char computerLetter, char opponentLetter, MCTSNode parent) {
			this.move = move;
			this.state = state;
			this.playerToMove = playerToMove;
			this.computerLetter = computerLetter;
			this.opponentLetter = opponentLetter;
			this.children = new ArrayList<>();
			this.parent = parent;
			this.notTriedMoves = generateMove(state);//generate only legal moves
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

	// Generate all moves 
	private static List<Move> generateMove(Board board) {
		List<Move> moves = new ArrayList<>();
		List<Board.Cell> emptyCells = board.getEmptyCells();// retrieve empty board
		for (Board.Cell cell : emptyCells) {
			moves.add(new Move(cell.row(), cell.col(), 'S'));
			moves.add(new Move(cell.row(), cell.col(), 'O'));
		}
		return moves;
	}

	private MCTSNode select(MCTSNode node) {
		while (node.isFullyExpanded() && !node.children.isEmpty() && !node.isTerminal())  {
			node = bestUCT(node);
		}
		return node;
	}

	private MCTSNode bestUCT(MCTSNode node) {
		MCTSNode best = null; // initialize the best node to null
		double bestValue = Double.NEGATIVE_INFINITY;// track the maximum value stored so far
		// iterate through all the child node
		for (MCTSNode child : node.children) {
			double upperConfidenceBound;
			//prioritize unvisited nodes
			if(child.visits == 0)
			{
				upperConfidenceBound = Double.MAX_VALUE;
			}else {
			//exploitation average the score
			double bestExploit = child.score / (child.visits);
			double explore = Math.sqrt(Math.log(node.visits) / child.visits );
			
			//UCB formula
			 upperConfidenceBound = bestExploit + UCB_CONSTANT * explore;
			}
			if (upperConfidenceBound > bestValue) {
				bestValue = upperConfidenceBound;
				best = child;
			}
		}
		return best;
	}

	private MCTSNode expand(MCTSNode node) {
		//if no expansion possible return node
		if (node.isTerminal() || node.notTriedMoves.isEmpty())
			return node; // no expansion
		
		Move mv = node.notTriedMoves.remove(node.notTriedMoves.size() - 1);// get the last move
		//apply the move to create a move
		Board nextState = node.state.copy();// make a copy of the board
		nextState.makeMove(mv.getRow(), mv.getCol(), mv.getLetter());

		int sosFormed = BoardSearcher.findSOSPatterns(nextState, mv.getRow(),mv.getCol(), null).size();
		
		char nextPlayer;
		if(game.getMode() == GameMode4.GENERAL && sosFormed > 0)
		{
			nextPlayer = node.playerToMove;
		}else {
			nextPlayer = (node.playerToMove == node.computerLetter) 
		            ? node.opponentLetter 
		            : node.computerLetter;
		
		}
		// add the new node to parent
		MCTSNode newNode = new MCTSNode(mv, nextState, nextPlayer, 
		        node.computerLetter, node.opponentLetter, node);
		    node.children.add(newNode);
		return newNode;
	}

	// Simulation method
	private double simulate(MCTSNode node,  Game4 game) {
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
			if (empty.isEmpty())
				break;

			
			//Try to form SOS 
			Move move = null;
	        if (random.nextDouble() < 1) {
	            move = findScoringMove(simBoard);
	        }
	        //Other play random
	        if(move == null) {
			Board.Cell cell = empty.get(random.nextInt(empty.size()));
			char letter = random.nextBoolean() ? 'S': 'O'; //choose between S and O
			move = new Move(cell.row(), cell.col(), letter);
	        }
			simBoard.makeMove(move.getRow(), move.getCol(), move.getLetter());
			//check how many SOS formed 
			int sosFormed = BoardSearcher.findSOSPatterns(simBoard, move.getRow(), move.getCol(), null).size();
			
			//award points
			if (currentPlayer == node.computerLetter) {
				computerScore += sosFormed ;

			} else {
				opponentScore += sosFormed;
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
		return Integer.compare(computerScore, opponentScore);
	}

	private void backPropagate(MCTSNode node, double value) {
		
       while(node != null)
       {
    	   node.visits ++;
    	  
    	   node.score += value;
    	
    	   if (node.parent != null && node.playerToMove != node.parent.playerToMove) {
               value = -value;
           }
    	   
    	   node = node.parent;
       }
	}
	
	private Move getBestChildrenMove(MCTSNode root)
	{
		MCTSNode bestChild = null;
		double bestAverage = Double.NEGATIVE_INFINITY;
		List<MCTSNode>qualified = new ArrayList<>();
		for (MCTSNode child : root.children)
		{
			
			if(child.visits >= 3)
			{
				qualified.add(child);
				
			}
		}
		//Sort the visits
		qualified.sort((a, b) -> Double.compare(b.score / b.visits, a.score / a.visits));

		// Get the top 5
		int printCount = Math.min(5, qualified.size());
		for (int i = 0; i < printCount; i++) {
			MCTSNode child = qualified.get(i);
			double avg = child.score / child.visits;

			if (avg > bestAverage) {
				bestAverage = avg;
				bestChild = child;
			}
		}
		
	
	//fall back to most visited if no more has visited
	if(bestChild == null)
	{
		int maxVisits = -1;
		for (MCTSNode child : root.children)
		{
			if(child.visits > maxVisits)
			{
				maxVisits = child.visits;
				bestChild = child;
			}
		}
	}
	return bestChild != null ? bestChild.move : null;
}
	//Getting the number of simulation
	public int getTotalSimulations() {
		return totalSimulations;
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

}
