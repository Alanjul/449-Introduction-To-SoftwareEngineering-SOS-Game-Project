package sprint5.controller;

import java.awt.*;
import java.util.*;


import javax.swing.*;
import javax.swing.Timer;

import sprint5.mode.GameMode.*;

import sprint5.mode.board.*;
import sprint5.mode.computerPlayer.*;
import sprint5.mode.dao.*;
import sprint5.mode.humanplayer.*;
import sprint5.mode.move.*;
import sprint5.mode.player.*;
import sprint5.util.*;
import sprint5.util.GamePersistenceService.GameSession;
import sprint5.view.*;

public class SOSGameController {

	private GameFrame frame;
	private MainPanel panel;
	private Game4 game;
	private Player bluePlayer;
	private Player redPlayer;
	private boolean isInProgress;
	private Timer computerMoveTimer;

	//abstractions
	private final Map<GameMode4, GameFactory>gameInterfaces;
	private final Map<Boolean, PlayerFactory> playerInterfaces;
	
	private final GameStatistics statistics;
	private final GamePersistenceService persistence;
	
	private GameSession currentSession;
	private int moveCounter;
	private PlayerStats blueSessionStats;
	private PlayerStats redSessionStats;

	public SOSGameController(GameFrame frame, ComputerStrategy strategy)
	{
		this.frame = frame;
		this.isInProgress = false;
		this.gameInterfaces = createGameInterface();
		this.playerInterfaces = createPlayerInterface(strategy);
		
		//Create services
		DaoFactory.DAOBundle daos = DaoFactory.createAllDAOs();
		this.persistence = new GamePersistenceService(
			daos.getPlayerDAO(),
			daos.getGameDAO(),
			daos.getStatsDAO()
		);
		this.statistics = new GameStatistics();

	}

	/***Constructor with dependency injection*/
	public SOSGameController(GameFrame frame, Map<GameMode4, GameFactory>gameInterfaces,
			Map<Boolean, PlayerFactory> playerInterfaces,
			 GamePersistenceService persistence,GameStatistics statistics)
	{
			this.frame = frame;
			this.isInProgress = false;
			this.gameInterfaces = gameInterfaces;
			this.playerInterfaces = playerInterfaces;
			this.persistence = persistence;
			this.statistics = statistics;
	}

	/**Create game factories*/
	private Map<GameMode4, GameFactory>createGameInterface()
	{
		Map<GameMode4, GameFactory>interfaces = new HashMap<>();
		interfaces.put(GameMode4.SIMPLE, new SimpleGameFactory());
		interfaces.put(GameMode4.GENERAL, new GeneralGameFactory());

		return interfaces;

	}

	/**Create player interfaces*/
	private Map< Boolean, PlayerFactory>createPlayerInterface(ComputerStrategy strategy)
	{
		Map<Boolean, PlayerFactory>interfaces = new HashMap<>();
		interfaces.put(true, new HumanPlayerFactory());
		interfaces.put(false, new ComputerPlayerFactory(strategy));

		return interfaces;

	}
	//set the mainPanel
	public void setMainPanel(MainPanel panel) {

		this.panel = panel;
		setUpListeners();

	}

	// Helper method to setUp listeners
	private void setUpListeners() {
		panel.getBottomPanel().setNewGameListener(e ->
		{
			startNewGame();});

		panel.getBottomPanel().setReplayGameListener(e -> openReplayBrowser());
		// Board size spinner
		panel.getTopPanel().getBoardSpinner().addChangeListener(e -> {
			if (!isInProgress) {
				int size = panel.getTopPanel().getBoardSize();
				panel.getBoardPanel().createBoard(size);
				frame.resetFrame();
			}
		});
	}

	private void startNewGame()
	{

		try
		{
			int boardSize = panel.getTopPanel().getBoardSize();//get board size from top panel class
			GameMode4 gameMode  = panel.getTopPanel().getSimpleMode() ? GameMode4.SIMPLE: GameMode4.GENERAL;
			Board board = new GameBoard5(boardSize);
			//Create players
			bluePlayer = createPlayer('B', panel.getBluePlayerPanel().isHuman(),
					panel.getBluePlayerPanel().getDifficultyEnum(),
					panel.getBluePlayerPanel().getSelectedLetter());

			redPlayer = createPlayer('R', panel.getRedPlayerPanel().isHuman(),
					panel.getRedPlayerPanel().getDifficultyEnum(),
					panel.getRedPlayerPanel().getSelectedLetter());

			//Create Game using interfaces
			GameFactory gameInterface = gameInterfaces.get(gameMode);
			if(gameInterface == null) {
				throw new IllegalStateException("No interface for game mode"+ gameMode);
			}
			game = gameInterface.createGame(board, bluePlayer, redPlayer);

			statistics.initializeSession(bluePlayer, redPlayer);
	            //Update GUI
	           if(panel.getBottomPanel().isRecordGameEnabled())
	           {
	        	   String mode = gameMode == GameMode4.SIMPLE ? "SIMPLE" : "GENERAL";
	        	   currentSession = persistence.startGameSession(
	        			   bluePlayer.getName(),
	        			   redPlayer.getName(),
	        			   mode,
	        			   boardSize,
	        			   bluePlayer.isHumanPlayer(),
	        			   redPlayer.isHumanPlayer());
	        	   
	           }else
	           {
	        	   currentSession = null;
	           }

	            //Update GUI
	           panel.resetPanels(boardSize);
				panel.getBoardPanel().updateBoard(board);
				panel.getBoardPanel().updateSosLines(game.getLinesFormed());
				
				//Disable settings during play
				panel.getTopPanel().setSettingsEnabled(false);
				panel.getBluePlayerPanel().setPlayerSettings(false);
				panel.getRedPlayerPanel().setPlayerSettings(false);
				
	            isInProgress = true;
	            moveCounter = 0;
	            // Update status
	            updateStatusLabel("New " + gameMode.getName()+ " game started! Current turn: Blue");

	            // If computer starts, make its move
	            if (game.getCurrentPlayer() instanceof ComputerPlayer) {
	                scheduleComputerMove();
	            }
		} catch(Exception e)
		{
			frame.showError("Error Starting game: " + e.getMessage());
			e.printStackTrace();
		}

	}

	private Player createPlayer(char symbol, boolean isHuman, ComputerPlayer.LevelsOfDifficulty difficulty,
			char preferredLetter) {
		PlayerFactory factory = playerInterfaces.get(isHuman);
		if (factory == null) {
			throw new IllegalStateException("No factory for player type: "+
		(isHuman ? "Human": "Computer"));
		}
			return factory.createPlayer(symbol, preferredLetter, difficulty);

	}

	public void handleCellClick(int row, int col, JButton button) {
		//Only human player can click
		if (!isInProgress || !(game.getCurrentPlayer() instanceof HumanPlayer)) {
			return;
		}
		Player currentPlayer = game.getCurrentPlayer();
		
		//Get letter
		char letter;
		if(currentPlayer.getSymbol() == 'B')
		{
			letter = panel.getBluePlayerPanel().getSelectedLetter();
		}else
		{
			letter = panel.getRedPlayerPanel().getSelectedLetter();
		}
		
		
		makeMove(row, col, letter);
	}

	/** MakeMove methods makes a move on board */
	private void makeMove(int row, int col, char letter) {
		try {
			Player currentPlayer = game.getCurrentPlayer();
			Move move = new Move(row, col, letter);
			MoveResult result = game.makeMove(move);
		if(!result.isSuccess())
		{
			frame.showError(result.getMessage());
			return;
		}
		
		updateUIAfterMove(row, col, letter, currentPlayer);

		moveCounter++;
		
		statistics.recordMove(currentPlayer);
		if(result.getSosCount() > 0)
		{
			statistics.recordSOS(currentPlayer,
					result.getSosCount(),   
			        result.getSosCount());
		}
		
		if (currentSession != null) {
			Integer playerId = (currentPlayer.getSymbol() == 'B') 
			                  ? currentSession.getBluePlayerId()
			                  : currentSession.getRedPlayerId();
			
			persistence.recordMove(
				currentSession.getGameId(),
				moveCounter,  
				playerId,
				row,
				col,
				letter,
				result.getSosCount(),
				result.getSosCount()  
			);
		}
		
		//Check game over
		if (game.isGameOver()) {
			handleGameOver();
		}else
		{
			updateStatusLabel("Current turn: " + 
	                (game.getCurrentPlayer().getSymbol() == 'B' ? "Blue" : "Red"));
			if (game.getCurrentPlayer() instanceof ComputerPlayer) {
				scheduleComputerMove();
			}
		}

	} catch (Exception e) {
		frame.showError("Error making move: " + e.getMessage());
		e.printStackTrace();
	}
	}

	

	// Handle game over
	private void handleGameOver() {
		isInProgress = false;
		GameResult result = game.getResult();
		
		//Record game result
		statistics.recordGameResult(result.getWinner());
		
		if (currentSession != null) {
			try {
				persistence.completeGame(
					currentSession,
					result.getWinner(),
					game.getBluePlayer().getScore(),
					game.getRedPlayer().getScore(),
					statistics.getBlueStats(),
					statistics.getRedStats()
				);
		
				//enable replay
				panel.getBottomPanel().setReplayButton(true);
				
			} catch (Exception e) {
				frame.showError("Warning: Game saved but statistics update failed");
				e.printStackTrace();
			}
		}
		
		//Show game over dialog
		String message = result.message();
		boolean playAgain = frame.showGameDialog(message);
		panel.getTopPanel().setSettingsEnabled(true);
		panel.getBluePlayerPanel().setPlayerSettings(true);
		panel.getRedPlayerPanel().setPlayerSettings(true);
		if (playAgain) {
			startNewGame();
		}
	}

	//Schedule computer move
	private void scheduleComputerMove() {
	if(computerMoveTimer != null && computerMoveTimer.isRunning())
	{
		return;
	}
	computerMoveTimer = new Timer(500, e -> {
		computerMoveTimer.stop();
		executeComputerMove();
	});
	computerMoveTimer.setRepeats(false);
	computerMoveTimer.start();
}
	
	private void executeComputerMove() {
		SwingWorker<Move, Void> worker = new SwingWorker<>() {
			@Override
			protected Move doInBackground() {
				ComputerPlayer computer = (ComputerPlayer) game.getCurrentPlayer();
				Player opponent = game.getOpponent(computer);
				Board board = game.getBoard();
				
				// Use your actual method signature: chooseMove(board, opponent, game)
				return computer.chooseMove(board, opponent, game);
			}

			@Override
			protected void done() {
				try {
					Move move = get();
					if (move != null) {
						makeMove(move.getRow(), move.getCol(), move.getLetter());
					}
				} catch (Exception e) {
					frame.showError("Computer move error: " + e.getMessage());
				}
			}
		};
		worker.execute();
	}

	//Update UI after move
	private void updateUIAfterMove(int row, int col, char letter, Player currentPlayer) {
		Color color = currentPlayer.getSymbol() == 'B' ? Color.BLUE : Color.RED;
		panel.getBoardPanel().updateCell(row, col, letter, color);
		panel.getBoardPanel().updateSosLines(game.getLinesFormed());
	}
	private void updateStatusLabel(String message) {
		panel.getBottomPanel().updateCurrentTurn(message);
	}

	//Open replay browser
	private void openReplayBrowser()
	{
		ReplayBrowser.showBrowser(frame);
	}
	
	//Getters
	public Game4 getGame() {
		return game;
	}
	public Player getBluePlayer() { return bluePlayer; }
	public Player getRedPlayer() { return redPlayer; }
	public boolean isInProgress() { return isInProgress; }
	public GameStatistics getStatisticsManager() { return statistics; }
	public GamePersistenceService getPersistenceService() { return persistence; }


}
