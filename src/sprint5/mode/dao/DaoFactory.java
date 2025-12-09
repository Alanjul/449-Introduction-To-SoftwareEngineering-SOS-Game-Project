package sprint5.mode.dao;

public class DaoFactory {

	private static boolean useMockDAOs = false;

	private DaoFactory() {

	}

	// Create PlayerRecordDao instances
	public static PlayerRecordDAO createPlayerDao() {
		if (useMockDAOs) {
			return createMockPlayerDao();
		}
		return new PlayerRecordImplementation();
	}

	// Create GameRecordDao
	public static GameRecordDao createGameDao() {
		if (useMockDAOs) {
			return createMockGameDao();
		}
		return new GameRecordImplementation();
	}

	// Create a StatsRecordDao instance
	// Create PlayerRecordDao instances
	public static StatsRecordDao createStatsDao() {
		if (useMockDAOs) {
			return createMockStatsDao();
		}
		return new StatsRecordImplementation();
	}

	public static void setUseMockDAOs(boolean useMocks) {
		useMockDAOs = useMocks;
	}

	// Check if Mock DAOS
	public static boolean isUsingMockDAOs() {
		return useMockDAOs;
	}

	public static PlayerRecordDAO createMockPlayerDao() {
		return new PlayerRecordImplementation();
	}

	public static GameRecordDao createMockGameDao() {
		return new GameRecordImplementation();
	}

	public static StatsRecordDao createMockStatsDao() {
		return new StatsRecordImplementation();
	}

	// Create all Daos as bundle
	public static DAOBundle createAllDAOs() {
		return new DAOBundle(createPlayerDao(), createGameDao(), createStatsDao());
	}

	// Inner class
	public static class DAOBundle {
		private final PlayerRecordDAO playerDAO;
		private final GameRecordDao gameDAO;
		private final StatsRecordDao statsDAO;

		public DAOBundle(PlayerRecordDAO playerDAO, GameRecordDao gameDAO, StatsRecordDao statsDAO) {
			this.playerDAO = playerDAO;
			this.gameDAO = gameDAO;
			this.statsDAO = statsDAO;
		}

		public PlayerRecordDAO getPlayerDAO() {
			return playerDAO;
		}

		public GameRecordDao getGameDAO() {
			return gameDAO;
		}

		public StatsRecordDao getStatsDAO() {
			return statsDAO;
		}
	}
}
