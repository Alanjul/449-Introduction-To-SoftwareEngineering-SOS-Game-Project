package sprint4.controller;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import sprint2.GameMode;
import sprint4.mode.Board;
import sprint4.mode.Board4;
import sprint4.mode.ComputerPlayer;
import sprint4.mode.ComputerPlayerFactory;
import sprint4.mode.ComputerStrategy;
import sprint4.mode.Game4;
import sprint4.mode.GameInterface;
import sprint4.mode.GameMode4;
import sprint4.mode.GameResult;
import sprint4.mode.GeneralGame4;
import sprint4.mode.GeneralGame4Factory;
import sprint4.mode.HumanPlayer;
import sprint4.mode.HumanPlayerFactory;
import sprint4.mode.Move;
import sprint4.mode.MoveResult;
import sprint4.mode.Player;
import sprint4.mode.PlayerInterface;
import sprint4.mode.SimpleGame4;
import sprint4.mode.SimpleGame4Factory;
import sprint4.view.GameFrame;
import sprint4.view.MainPanel;

public class SOSGameController {

	private GameFrame frame;
	private MainPanel panel;
	private Game4 game;
	private Player bluePlayer;
	private Player redPlayer;
	private boolean isInProgress;
	private Timer computerMoveTimer;

	//abstractions
	private final Map<GameMode4, GameInterface>gameInterfaces;
	private final Map<Boolean, PlayerInterface> playerInterfaces;
	
	public SOSGameController(GameFrame frame, ComputerStrategy strategy)
	{
		this.frame = frame;
		this.isInProgress = false;
		this.gameInterfaces = createGameInterface();
		this.playerInterfaces = createPlayerInterface(strategy);
		
	}
	
	/***Constructor with dependency injection*/
	public SOSGameController(GameFrame frame, Map<GameMode4, GameInterface>gameInterfaces,
			Map<Boolean, PlayerInterface> playerInterfaces) {
		this.frame = frame;
		this.isInProgress = false;
		
		//initialize the interfaces
	 this.gameInterfaces = gameInterfaces;
	 this.playerInterfaces = playerInterfaces;
	}

	/**Create game factories*/
	private Map<GameMode4, GameInterface>createGameInterface()
	{
		Map<GameMode4, GameInterface>interfaces = new HashMap<>();
		interfaces.put(GameMode4.SIMPLE, new SimpleGame4Factory());
		interfaces.put(GameMode4.GENERAL, new GeneralGame4Factory());
		
		return interfaces;
		
	}
	
	/**Create player interfaces*/
	private Map< Boolean, PlayerInterface>createPlayerInterface(ComputerStrategy strategy)
	{
		Map<Boolean, PlayerInterface>interfaces = new HashMap<>();
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
			Board board = new Board4(boardSize);
			//Create players
			bluePlayer = createPlayer('B', panel.getBluePlayerPanel().isHuman(),
					panel.getBluePlayerPanel().getDifficultyEnum(),
					panel.getBluePlayerPanel().getSelectedLetter());
			
			redPlayer = createPlayer('R', panel.getRedPlayerPanel().isHuman(),
					panel.getRedPlayerPanel().getDifficultyEnum(),
					panel.getRedPlayerPanel().getSelectedLetter());
			
			//Create Game using interfaces
			GameInterface gameInterface = gameInterfaces.get(gameMode);
			if(gameInterface == null) {
				throw new IllegalStateException("No interface for game mode"+ gameMode);
			}
			game = gameInterface.createGame(board, bluePlayer, redPlayer);
			
			 if (bluePlayer instanceof ComputerPlayer) {
	               ((ComputerPlayer) bluePlayer).setCurrentGame(game);
				   
	            }
	            if (redPlayer instanceof ComputerPlayer) {
	                ((ComputerPlayer) redPlayer).setCurrentGame(game);
	            }
	            
	            //Update GUI
	            panel.resetPanels(boardSize);
	            panel.getBoardPanel().updateBoard(board);
	            panel.getBoardPanel().updateSosLines(game.getLineFormed());
	            
	            // Disable settings during game
	            panel.getTopPanel().setSettingsEnabled(false);
	            panel.getBluePlayerPanel().setPlayerSettings(false);
	            panel.getRedPlayerPanel().setPlayerSettings(false);
	            
	            isInProgress = true;
	            
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
		PlayerInterface factory = playerInterfaces.get(isHuman);
		if (factory == null) {
			throw new IllegalStateException("No factory for player type: "+
		(isHuman ? "Human": "Computer"));
		} 
			return factory.createPlayer(symbol, preferredLetter, difficulty);
		
	}

	public void handleCellClick(int row, int col, JButton button) {
		// Check if game is not in progress or game has not yet started
		if (!isInProgress || game == null) {
			frame.showInfo("Start game before you continue");
			return;
		}
		// check if the game is over
		if (game.isGameOver()) {
			frame.showInfo("Game is over!, Click New Game button to restart");
			return;
		}
		Player currentPlayer = game.getCurrentPlayer();// get the current player
		// Check if it not human
		if (!(currentPlayer instanceof HumanPlayer)) {
			frame.showInfo("It is computer turn, Wait");
			return;
		}
		// Check if the button is filled already
		if (!(button.getText().equals(""))) {
			updateStatusLabel("Cell can't be over written");
			return;
		}
		// Get letter for human
		char letter = ((HumanPlayer) currentPlayer).chooseLetter();// choose letter
		makeMove(row, col, letter);
	}

	/** MakeMove methods makes a move on board */
	private void makeMove(int row, int col, char letter) {
		Move move = new Move(row, col, letter);
		// Create a MoveResult object
		MoveResult result = game.makeMove(move);
		if (!(result.isSuccess())) {
			frame.showError(result.getMessage());
			return;
		}
		Color playerColor = (game.getCurrentTurn() == 'B') ? Color.BLUE : Color.RED;

		// Switch to opposite
		playerColor = (playerColor == Color.BLUE) ? Color.RED : Color.BLUE;

		// update the cell
		panel.getBoardPanel().updateCell(row, col, letter, playerColor);
		// Update lines
		panel.getBoardPanel().updateSosLines(game.getLineFormed());

		// check if the game is over
		if (game.isGameOver()) {
			handleGameOver();
			return;
		}
		// Update status
		updateStatus();

		// schedule computer
		if (game.getCurrentPlayer() instanceof ComputerPlayer) {
			scheduleComputerMove();
		}
	}

	// Scheduling computer move after delay
	private void scheduleComputerMove() {
		// Stop timer if running
		if (computerMoveTimer != null && computerMoveTimer.isRunning()) {
			computerMoveTimer.stop();
		}

		// Create time
		computerMoveTimer = new Timer(1000, e -> {
			if (isInProgress && !game.isGameOver()) {
				computerMove();
			}
		});
		// Run the time once
		computerMoveTimer.setRepeats(false);
		computerMoveTimer.start();
	}

	// Make computer Moving using Monte carlos Tree search Algorithm
	public void computerMove() {
		ComputerPlayer player = (ComputerPlayer) game.getCurrentPlayer();
		Player opponent = game.getOpponent();
		updateStatusLabel("Computer is thinking! Wait ------");

		// create a new SWing work for background processing
		SwingWorker<Move, Void> worker = new SwingWorker<>() {

			@Override
			protected Move doInBackground() {
				return player.chooseMove(game.getBoard(), opponent, game);
			}

			@Override
			protected void done() {
				try {
					// Get result from the background
					Move move = get();
					makeMove(move.getRow(), move.getCol(), move.getLetter());
				} catch (Exception e) {
					frame.showError("Computer move issue" + e.getMessage());
					e.printStackTrace();
				}
			}
		};
		worker.execute();
	}

	// Handle game over
	private void handleGameOver() {
		isInProgress = false;
		// Enable the settings
		panel.getTopPanel().setSettingsEnabled(true);
		panel.getBluePlayerPanel().setPlayerSettings(true);
		panel.getRedPlayerPanel().setPlayerSettings(true);

		GameResult result = game.getResult();
		String message = result.getMessage();
		updateStatusLabel(message);

		// Show dialog message
		boolean playAgain = frame.showGameDialog(message);
		if (playAgain) {
			startNewGame();
		}
	}

	private void updateStatus() {
		if (game == null)
			return;
		if (game.isGameOver()) {
			updateStatusLabel(game.getResult().getMessage());// get current state of the game
			return;
		}
		Player current = game.getCurrentPlayer();
		String playerName = (current.getSymbol() == 'B') ? "Blue Player" : "Red Player";

		// Build string message
		StringBuilder builder = new StringBuilder();
		builder.append("Current Turn: ").append(playerName);
		if (current instanceof ComputerPlayer) {
			ComputerPlayer player = (ComputerPlayer) current; // use Computer class method
			builder.append(" Level: (").append(player.getDifficulty().getName()).append(") ");
		}
		if (game.getMode() == GameMode4.GENERAL) {
			builder.append("Blue Score: ").append(game.getBlueScore());
			builder.append("- Red Score: ").append(game.getRedScore());
		}

		// update the panel
		panel.getBottomPanel().updateCurrentTurn(builder.toString());// convert message to String
	}

	private void updateStatusLabel(String message) {
		panel.getBottomPanel().updateCurrentTurn(message);
	}

	/**
	 * @return the game
	 */
	public Game4 getGame() {
		return game;
	}

}
