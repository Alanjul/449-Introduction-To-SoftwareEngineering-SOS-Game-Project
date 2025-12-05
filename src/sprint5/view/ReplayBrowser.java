package sprint5.view;

import java.time.*;
import java.time.format.*;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import sprint5.mode.GameMode.*;

public class ReplayBrowser extends JDialog {
	private static final DateTimeFormatter DATE_FORMATTER = 
	        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	private final GameReplayService replayService;
    private GameReplay selectedReplay;
    private JTextField searchField;
    private JComboBox<String> filterCombo;
    private JTable gamesTable;
    private DefaultTableModel tableModel;
    private JButton openButton;
    private JButton cancelButton;
    private JButton statisticsButton;
    
    //Table column header
    private static final String[] COLUMN_NAMES = {
            "Game ID", "Date", "Blue Player", "Red Player", 
            "Mode", "Board", "Score", "Winner", "Moves"
        };
    
    //Constructor
    public ReplayBrowser(Frame owner) {
        super(owner, "Game Replay Browser", true);
        this.replayService = new GameReplayService();
        this.selectedReplay = null;
        
        initializeComponents();
        layoutComponents();
        setupListeners();
        loadGames();
        
        pack();
        setLocationRelativeTo(owner);
    }
    private void initializeComponents() {
        // Search field
        searchField = new JTextField(20);
        searchField.setToolTipText("Search by player name");
        
        // Filter combo box
        String[] filters = {
            "All Games",
            "Recent (10)",
            "Recent (50)",
            "Simple Mode Only",
            "General Mode Only",
            "This Week",
            "This Month"
        };
        filterCombo = new JComboBox<>(filters);
        
        // Games table
        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; //read only
            }
        };
        gamesTable = new JTable(tableModel);
        gamesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        gamesTable.setAutoCreateRowSorter(true);
        gamesTable.setRowHeight(25);
        
        // Buttons
        openButton = new JButton("Open Replay");
        openButton.setEnabled(false);
        
        cancelButton = new JButton("Cancel");
        
        statisticsButton = new JButton("Statistics");
        statisticsButton.setEnabled(false);
    }
    //Layout components
    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Top panel 
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
        
        // Center panel - Table
        JScrollPane scrollPane = new JScrollPane(gamesTable);
        scrollPane.setPreferredSize(new Dimension(900, 400));
        add(scrollPane, BorderLayout.CENTER);
        
        // Bottom panel - Buttons
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Add padding
        ((JPanel) getContentPane()).setBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
    //Load games
    private void loadGames() {
        try {
            List<GameReplay> replays = replayService.getAllReplays();
            populateTable(replays);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error loading games: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    //Create top panel
    private JPanel createTopPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        // Search row
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchGames());
        searchPanel.add(searchButton);
        
        // Filter row
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filter:"));
        filterPanel.add(filterCombo);
        JButton applyFilterButton = new JButton("Apply");
        applyFilterButton.addActionListener(e -> applyFilter());
        filterPanel.add(applyFilterButton);
        
        panel.add(searchPanel);
        panel.add(filterPanel);
        
        return panel;
    }
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        panel.add(statisticsButton);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(openButton);
        panel.add(cancelButton);
        
        return panel;
    }
    
    //Listeners
    private void setupListeners() {
       //Table listener
        gamesTable.getSelectionModel().addListSelectionListener(e -> {
            boolean hasSelection = gamesTable.getSelectedRow() != -1;
            openButton.setEnabled(hasSelection);
            statisticsButton.setEnabled(hasSelection);
        });
        
        // Double-click to open
        gamesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && gamesTable.getSelectedRow() != -1) {
                    openSelectedReplay();
                }
            }
        });
        
        // Button listeners
        openButton.addActionListener(e -> openSelectedReplay());
        cancelButton.addActionListener(e -> {
            selectedReplay = null;
            dispose();
        });
        statisticsButton.addActionListener(e -> showStatistics());
        
        //search
        searchField.addActionListener(e -> searchGames());
    }
    
    //Apply filter
    private void applyFilter() {
        try {
            String filter = (String) filterCombo.getSelectedItem();
            List<GameReplay> replays;
            
            switch (filter) {
                case "All Games":
                    replays = replayService.getAllReplays();
                    break;
                    
                case "Recent (10)":
                    replays = replayService.getRecentReplays(10);
                    break;
                    
                case "Recent (50)":
                    replays = replayService.getRecentReplays(50);
                    break;
                    
                case "Simple Mode Only":
                    replays = replayService.getReplaysByMode("SIMPLE");
                    break;
                    
                case "General Mode Only":
                    replays = replayService.getReplaysByMode("GENERAL");
                    break;
                    
                case "This Week":
                    LocalDateTime weekAgo = LocalDateTime.now().minusWeeks(1);
                    replays = replayService.getReplaysByDateRange(weekAgo, LocalDateTime.now());
                    break;
                    
                case "This Month":
                    LocalDateTime monthAgo = LocalDateTime.now().minusMonths(1);
                    replays = replayService.getReplaysByDateRange(monthAgo, LocalDateTime.now());
                    break;
                    
                default:
                    replays = replayService.getAllReplays();
            }
            
            populateTable(replays);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error applying filter: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //Search game
    private void searchGames() {
        String searchTerm = searchField.getText().trim();
        
        if (searchTerm.isEmpty()) {
            loadGames();
            return;
        }
        
        try {
            List<GameReplay> replays = replayService.searchReplaysByPlayer(searchTerm);
            populateTable(replays);
            
            if (replays.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No games found for player: " + searchTerm,
                    "No Results",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error searching: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //Populate table
    private void populateTable(List<GameReplay> replays) {
        // Clear existing rows
        tableModel.setRowCount(0);
        
        // Add replay rows
        for (GameReplay replay : replays) {
        	
        	String blueType = replay.isBlueHuman() ? "Human" : "Computer";
            String redType = replay.isRedHuman() ? "Human" : "Computer";
            
            
            String winner;
            if (replay.isDraw()) {
                winner = "Draw";
            } else if (replay.didBlueWin()) {
                winner = "Blue";
            } else if (replay.didRedWin()) {
                winner = "Red";
            } else if (!replay.isCompleted()) {
                winner = "In Progress";
            } else {
                winner = "Unknown";
            }
            Object[] row = {
                replay.getGameId(),
                replay.getCreatedAt().format(DATE_FORMATTER),
                blueType,
                redType,
                replay.getGameMode(),
                replay.getBoardSize() + "x" + replay.getBoardSize(),
                replay.getScoreDisplay(),
                winner,
                replay.getTotalMoves()
            };
            tableModel.addRow(row);
        }
        
        // Update label
        setTitle(String.format("Game Replay Browser (%d games)", replays.size()));
    }
    
    private void openSelectedReplay() {
        int selectedRow = gamesTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        
        try {
            // Get game ID from table
            int gameId = (Integer) gamesTable.getValueAt(selectedRow, 0);
            
            // Load replay
            selectedReplay = replayService.loadReplay(gameId).orElse(null);
            
            if (selectedReplay == null) {
                JOptionPane.showMessageDialog(this,
                    "Could not load replay for game #" + gameId,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Open replay window
            openReplayWindow(selectedReplay);
            
            // Close browser
            dispose();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error opening replay: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //Replay window
    private void openReplayWindow(GameReplay replay) {
        // Create replay window
        ReplayPanel replayPanel = new ReplayPanel(replay);
        
        JDialog replayDialog = new JDialog((Frame) getOwner(), 
            "Replay: Game #" + replay.getGameId(), false);
        replayDialog.add(replayPanel);
        replayDialog.pack();
        replayDialog.setLocationRelativeTo(this);
        replayDialog.setVisible(true);
    }
    //Show statistics
    private void showStatistics() {
        int selectedRow = gamesTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        
        try {
            // Get game ID from table
            int gameId = (Integer) gamesTable.getValueAt(selectedRow, 0);
            
            // Load replay
            GameReplay replay = replayService.loadReplay(gameId).orElse(null);
            
            if (replay == null) {
                JOptionPane.showMessageDialog(this,
                    "Could not load replay for game #" + gameId,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Show statistics
            String stats = replay.getDetailedInfo();
            
            JOptionPane.showMessageDialog(this,
                stats,
                "Game Statistics",
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error loading statistics: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
  
    

    //Show browser dialog
    public static GameReplay showBrowser(Frame owner) {
        ReplayBrowser browser = new ReplayBrowser(owner);
        browser.setVisible(true);
        return browser.selectedReplay;
    }
    
    //Get the selected replay
    public GameReplay getSelectedReplay() {
        return selectedReplay;
    }
    
    
}
