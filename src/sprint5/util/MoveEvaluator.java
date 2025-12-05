package sprint5.util;

import java.util.ArrayList;
import java.util.List;

import sprint5.mode.board.Board;
import sprint5.mode.move.Move;

public class MoveEvaluator {
	
//find the best move for placing letter
	public static Move findBestMove(Board board, char letter)
	{
		List<Board.Cell>emptyCells = board.getEmptyCells();
		if(emptyCells.isEmpty())
		{
			return null;
		}
		
		Move bestMove = null;
		int maxSOSCount = -1;
		
		for (Board.Cell cell : emptyCells)
		{
			int sosCount = BoardSearcher.countSOS(board, cell.row(), cell.col(), letter);
			
			if(sosCount > maxSOSCount)
			{
				maxSOSCount = sosCount;
				bestMove = new Move(cell.row(), cell.col(), letter);
			}
		}
		
		//fall back
		if(bestMove == null)
		{
			Board.Cell first = emptyCells.get(0);
			bestMove = new Move(first.row(), first.col(), letter);
		}
		return bestMove;
	}
	//find the best move for both S and O
	//@param board the game board
	//@return the best move
	public static Move findBestMoveAnyLetter(Board board) {
        Move bestMoveS = findBestMove(board, 'S');
        Move bestMoveO = findBestMove(board, 'O');
        
        if (bestMoveS == null) return bestMoveO;
        if (bestMoveO == null) return bestMoveS;
        
        int scoreS = evaluateMove(board, bestMoveS);
        int scoreO = evaluateMove(board, bestMoveO);
        
        return (scoreS >= scoreO) ? bestMoveS : bestMoveO;
    }
	/**Evaluate how many SOS patterns a move would create
	 * @param board the game board
	 * @param move the move to evaluate
	 * @return number of SOS patterns*/
	public static int evaluateMove(Board board, Move move)
	{
		if(move == null) return 0;
		
		return BoardSearcher.countSOS(board,
				move.getRow(),
				move.getCol(),
				move.getLetter());
	}
	
	//check if the move would form sos
	public static boolean isSOSFormed(Board board, Move move)
	{
		return evaluateMove(board, move) > 0;
	}
	
	/*Find all moves the would form SOS
	 * @param board the game board
	 * @param letter to place
	 * @return list of all scoring moves
	 */
	public static List<Move>findAllSOSFormed(Board board, char letter)
	{
		List<Move>scoringMoves = new ArrayList<>();
		List<Board.Cell>emptyCells = board.getEmptyCells();
		for(Board.Cell cell : emptyCells) {
			if(BoardSearcher.formSOS(board, cell.row(),cell.col(), letter))
			{
				scoringMoves.add(new Move(cell.row(), cell.col(), letter));
			}
		}
		return scoringMoves;
	}
	//Find all possible moves of given letter
	public static List<Move>findAllPossibleMoves(Board board, char letter)
	{
		List<Move>moves = new ArrayList<>();
		List<Board.Cell>emptyCells = board.getEmptyCells();
		for(Board.Cell cell: emptyCells)
		{
			moves.add(new Move(cell.row(), cell.col(), letter));
		}
		return moves;
	}
	

}
