package sprint5.mode.dao;

import java.util.List;
import java.util.Optional;

/*
 * @param <T> the entity type
 * @param <ID> the id type*/
public interface PlayerRecordDAO{

	//Create Player
	Integer create(PlayerRecord player);
	
	//Find the player by id
	Optional<PlayerRecord>findById(Integer id);
	
	//find player by userName
	 Optional<PlayerRecord> findByUsername(String userName);

	 //Get all Players
	 List<PlayerRecord> findAll();
	//Retrieve all human player
	 List<PlayerRecord> findAllHumans();
	//Search player by pattern
	 List<PlayerRecord> searchByPattern(String pattern);
	 //Get all Computer players
	 List<PlayerRecord> findAllComputers();
	 //Update player
	 boolean update(PlayerRecord player);
	 //Delete the player
	 boolean delete(Integer playerId);
	 //Create player
	 PlayerRecord getOrCreate(String username, String displayname, String playertype);
	 //Get statistics
	 StatsRecord getStats(Integer playerId);
	//Update last time played
	 void updateLastPlayed(Integer playerId);
	//check if player exists
	 boolean exists(Integer playerId);
}
