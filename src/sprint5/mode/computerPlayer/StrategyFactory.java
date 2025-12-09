package sprint5.mode.computerPlayer;

import sprint5.mode.mcts.MonteCarloStrategy;

public class StrategyFactory {
	// create strategy for difficulty level
	public static ComputerStrategy createStrategy(ComputerPlayer.LevelsOfDifficulty difficulty) {
		return switch (difficulty) {
		case RANDOM -> new RandomStrategy();
		case EASY -> new EasyStrategy();
		case MEDIUM, HARD, EXPERT -> new MonteCarloStrategy();
		};
	}
}
