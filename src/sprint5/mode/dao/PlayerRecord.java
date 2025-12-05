package sprint5.mode.dao;

import java.time.*;
import java.util.*;

public class PlayerRecord {
	private Integer playerId;
	private final String userName;
	private final String name;
	private final String playerType;
	private final LocalDateTime createdAt;
	private LocalDateTime lastPlayed;
	//Constructor
	public PlayerRecord(String username, String name, String playerType) {
        this(null, username, name, playerType, null, null);
    }
//load database
	 public PlayerRecord(Integer playerId, String userName, String name, 
             String playerType, LocalDateTime createdAt, LocalDateTime lastPlayed) {
validateUsername(userName);
validateName(name);
validatePlayerType(playerType);

this.playerId = playerId;
this.userName = userName;
this.name = name;
this.playerType = playerType != null ? playerType : "HUMAN";
this.createdAt = createdAt;
this.lastPlayed = lastPlayed;
}
 private void validateUsername(String username) {
	        if (username == null || username.trim().isEmpty()) {
	            throw new IllegalArgumentException("Username cannot be null or empty");
	        }
	        if (username.length() > 255) {
	            throw new IllegalArgumentException("Username cannot exceed 255 characters");
	        }
	    }
 private void validateName(String name) {
     if (name == null || name.trim().isEmpty()) {
         throw new IllegalArgumentException("Display name cannot be null or empty");
     }
     if (name.length() > 255) {
         throw new IllegalArgumentException("Display name cannot exceed 255 characters");
     }
 }
 private void validatePlayerType(String playerType) {
     if (playerType != null && 
         !playerType.equals("HUMAN") && !playerType.equals("COMPUTER")) {
         throw new IllegalArgumentException(
             "Player type must be 'HUMAN' or 'COMPUTER', got: " + playerType);
     }
 }
 //getters
 public Integer getPlayerId() { return playerId; }
 public String getUserName() { return userName; }
 public String getName() { return name; }
 public String getPlayerType() { return playerType; }
 public LocalDateTime getCreatedAt() { return createdAt; }
 public LocalDateTime getLastPlayed() { return lastPlayed; }

//setter
public void setPlayerId(Integer playerId) {
    if (this.playerId != null) {
        throw new IllegalStateException("Player ID already set");
    }
    this.playerId = playerId;
}

public void setLastPlayed(LocalDateTime lastPlayed) {
    this.lastPlayed = lastPlayed;
}

//Helper methods
public boolean isHuman() { return "HUMAN".equals(playerType); }
public boolean isComputer() { return "COMPUTER".equals(playerType); }

@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PlayerRecord)) return false;
    PlayerRecord that = (PlayerRecord) o;
    return Objects.equals(playerId, that.playerId) &&
           Objects.equals(userName, that.userName);
}
@Override
public int hashCode() {
    return Objects.hash(playerId, userName);
}

@Override
public String toString() {
    return String.format("PlayerRecord[id=%d, username='%s', type=%s]",
        playerId, userName, playerType);
}
}
