package sprint4.mode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sprint2.GameMode;
import sprint4.Util.BoardSearcher;

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
			ComputerPlayer.LevelsOfDifficulty difficulty) {
		this.computerPlayer = computerPlayer;
		this.opponentPlayer = opponentPlayer;
		this.iterations = getIteration(difficulty);
		this.random = new Random();
		this.totalSimulations = 0;
	}

	/* Get the iteration based on difficulty level */
	private int getIteration(ComputerPlayer.LevelsOfDifficulty difficulty) {
		return switch (difficulty) {
		case RANDOM -> 50;
		case EASY -> 150;
		case MEDIUM -> 400;
		case HARD -> 600;
		case EXPERT -> 800;
		};
	}

	/* findBestMove used to find the best move to make */
	public Move findBestMove(Board board, Game4 game) {
		totalSimulations = 0;
		// get all moves
		List<Move> availableMoves = getAvailableMoves(board);
		//no move available
		if(availableMoves.isEmpty())
		{
			return  null;
		}
		//if it is one move return immediately
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
			
			//expand to child if possible
			MCTSNode expansion =  expand(selectPromisingNode);
			
			//simulate: Play random game with this node
			double simulationResult = simulate(expansion, game);
			
			//BacKPropagation:  update the root with statistics
			backPropagate(expansion, simulationResult);
			
		}
		

		return getBestChildrenMove(root);//return the most promising node
	}

	private List<Move> getAvailableMoves(Board board) {
		List<Move> moves = new ArrayList<>();
		// get empty cells
		List<Board.Cell> emptyCells = board.getEmptyCells();
		// loop through empty cells to look where to put S or O
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
		List<Board4.Cell> emptyCells = board.getEmptyCells();// retrieve empty board
		// loop through the empty board as you place S or O
		for (Board4.Cell cell : emptyCells) {
			moves.add(new Move(cell.row(), cell.col(), 'S'));
			moves.add(new Move(cell.row(), cell.col(), 'O'));
		}
		return moves;
	}

	private MCTSNode select(MCTSNode node) {
		// move down while fully expanded and there are children and no terminal
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

		char nextPlayer = (node.playerToMove == node.computerLetter) ? node.opponentLetter : node.computerLetter;
		
		//create a new child node
		MCTSNode newNode = new MCTSNode(mv, nextState, nextPlayer, node.computerLetter, node.opponentLetter, node);
		
		// add the new node to parent
		node.children.add(newNode);
		return newNode;
	}

	// Simulation method
	private double simulate(MCTSNode node,  Game4 game) {
		Board simBoard = node.state.copy();
		char currentPlayer = node.playerToMove; // tracks whose turn it is

		int computerScore = 0;
		int oppositeScore = 0;
		
		//Play random moves until board is full or max depth is reached
		int maxDepth = simBoard.getEmptyCells().size();
		for (int i = 0; i < maxDepth; i++) {
			List<Board.Cell> empty = simBoard.getEmptyCells();
			
			//no move or game is over
			if (empty.isEmpty())
				break;

			Board.Cell cell = empty.get(random.nextInt(empty.size()));
			char letter = random.nextBoolean() ? 'S': 'O'; //choose between S and O
			simBoard.makeMove(cell.row(), cell.col(), letter);
			
			//check how many SOS formed 
			int sosFormed = BoardSearcher.findSOSPatterns(simBoard, cell.row(), cell.col(), null).size();
			
			//award points
			if (currentPlayer == node.computerLetter) {
				computerScore += sosFormed ;

			} else {
				oppositeScore += sosFormed;
			}
			// if sos is Formed in general mode, player continues no turn
			boolean isGeneral = (game.getMode() == GameMode4.GENERAL);
			if(!isGeneral || sosFormed == 0)
			{
				currentPlayer = (currentPlayer == node.computerLetter)? node.opponentLetter : 
					node.computerLetter;
			}
		}
		if (computerScore > oppositeScore) {
			return 1; // Computer wins
		}
		else if (computerScore < oppositeScore)
		{
			return -1; //computer loss
		}else
		{
		return 0; //draw
		}
	}

	private void backPropagate(MCTSNode node, double value) {
		
       while(node != null)
       {
    	   node.visits ++;
    	   node.score += value;
    	   
    	   //Move to parent
    	   MCTSNode parent = node.parent;
    	   
    	   //assign to opponent (Zero sum) and negate if a player actually changed
    	   if(parent != null)
    		   if (node.playerToMove != parent.playerToMove)
    			   value = -value;
    	   node = parent;
       }
	}
	
	private Move getBestChildrenMove(MCTSNode root)
	{
		MCTSNode bestChild = null;
		int maxVisits = -1;
		for (MCTSNode child : root.children)
		{
			if(child.visits > maxVisits)
			{
				maxVisits = child.visits;
				bestChild = child;
			}
		}
		return bestChild != null ? bestChild.move : null;
		
	}
	//Getting the number of simulation
	public int getTotalSimulations() {
		return totalSimulations;
	}

}
