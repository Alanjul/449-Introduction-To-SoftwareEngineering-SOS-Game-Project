package sprint5.util;

import sprint5.mode.player.*;

public class GameStatistics {
	private PlayerStats blueStats;
    private PlayerStats redStats;
    private Player bluePlayer;
    private Player redPlayer;
    
    //Initialize statistics for a new game session
    public void initializeSession(Player bluePlayer, Player redPlayer) {
        this.bluePlayer = bluePlayer;
        this.redPlayer = redPlayer;
        
        //Create fresh stats
        this.blueStats = new PlayerStats(getPlayerName(bluePlayer));
        this.redStats = new PlayerStats(getPlayerName(redPlayer));
        
    }
    //Record SOS
    public void recordSOS(Player player, int sosCount, int points) {
        PlayerStats stats = getStats(player);
        if (stats != null) {
            stats.recordSOSFormed(sosCount);
            stats.recordPointsScored(points);
        }
    }
    //Record moves
    public void recordMove(Player player) {
    
    }
    
    //Record Game results
    public void recordGameResult(Winner winner) {
    	if(winner == null)
    	{
    		return ;
    	}
        switch (winner) {
            case BLUE:
                blueStats.recordWin();;
                redStats.recordLoss();;
                break;
                
            case RED:
                redStats.recordWin();
                blueStats.recordLoss();
                break;
                
            case DRAW:
                blueStats.recordDraw();
                redStats.recordDraw();
                break;
                
            case NONE:
            default:
                // No winner yet
                break;
        }
    }
    //Get stats
    public PlayerStats getStats(Player player) {
        if (player == null) {
            return null;
        }
        
        if (player.equals(bluePlayer)) {
            return blueStats;
        } else if (player.equals(redPlayer)) {
            return redStats;
        }
        
        return null;
    }
    //Blue player stats
    public PlayerStats getBlueStats() {
        return blueStats;
    }
    //red stats
    public PlayerStats getRedStats() {
        return redStats;
    }
    
    //Total SOS formed
    public int getTotalSOSFormed() {
        return blueStats.getTotalSOSFormed() + redStats.getTotalSOSFormed();
    }
    
    //Points scored
    public int getTotalPoints() {
        return blueStats.getTotalPointsScored() + redStats.getTotalPointsScored();
    }
    
    //Get total moves made in the game
    public int getTotalMoves() {
        return 0;
    }
    //Summary
    public String getStatsSummary() {
        return String.format(
            "Game Statistics:\n" +
            "Blue Player: %d SOS, %d points\n" +
            "Red Player: %d SOS, %d points\n" +
            "Total Moves: %d",
            blueStats.getTotalSOSFormed(),
            blueStats.getTotalPointsScored(),
            redStats.getTotalSOSFormed(),
            redStats.getTotalPointsScored(),
            getTotalMoves()
        );
    }
    //check if statistics is initialized
    public boolean isInitialized() {
        return blueStats != null && redStats != null;
    }
    //Reset
    public void reset() {
        if (bluePlayer != null) {
            blueStats = new PlayerStats(getPlayerName(bluePlayer));
        }
        if (redPlayer != null) {
            redStats = new PlayerStats(getPlayerName(redPlayer));
        }
    }
    
    private String getPlayerName(Player player) {
        if (player == null) {
            return "Unknown";
        }
        String name = player.getName();
        return (name != null && !name.isEmpty()) ? name : "Player " + player.getSymbol();
    }
    



}
