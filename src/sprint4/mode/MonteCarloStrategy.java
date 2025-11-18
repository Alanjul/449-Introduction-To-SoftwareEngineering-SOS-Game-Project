package sprint4.mode;

public class MonteCarloStrategy implements ComputerStrategy {
	
	@Override
	public Move findBestMove(Board board, Game4 game, Player computerPlayer, Player opponent,
			ComputerPlayer.LevelsOfDifficulty difficultyLevel) {
		MonteCarlosTreeSearch mcts = new MonteCarlosTreeSearch(computerPlayer, opponent,difficultyLevel, game);
		return mcts.findBestMove(board, game);
		
	}

@Override		
public String getStrategyName()
{
 return "Monte Carlo Tree search (MTCS)";	
}

}
