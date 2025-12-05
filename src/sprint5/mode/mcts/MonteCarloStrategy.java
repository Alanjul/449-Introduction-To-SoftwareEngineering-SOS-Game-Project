package sprint5.mode.mcts;

import sprint5.mode.GameMode.Game4;
import sprint5.mode.board.Board;
import sprint5.mode.computerPlayer.ComputerPlayer;
import sprint5.mode.computerPlayer.ComputerStrategy;
import sprint5.mode.move.Move;
import sprint5.mode.player.Player;

public class MonteCarloStrategy implements ComputerStrategy {

	@Override
	public Move findBestMove(Board board, Game4 game, Player computerPlayer, Player opponent,
			ComputerPlayer.LevelsOfDifficulty difficultyLevel) {
		MonteCarlosTreeSearch mcts = new MonteCarlosTreeSearch(computerPlayer, opponent,difficultyLevel, game);
		return mcts.findBestMove(board);

	}

@Override
public String getStrategyName()
{
 return "Monte Carlo Tree search (MTCS)";
}

}
