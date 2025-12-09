-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: sos_game_db
-- ------------------------------------------------------
-- Server version	9.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `game_moves`
--

DROP TABLE IF EXISTS `game_moves`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `game_moves` (
  `move_id` int NOT NULL AUTO_INCREMENT,
  `game_id` int NOT NULL,
  `move_number` int NOT NULL,
  `player_id` int NOT NULL,
  `row_position` int NOT NULL,
  `col_position` int NOT NULL,
  `letter` enum('S','O') COLLATE utf8mb4_unicode_ci NOT NULL,
  `sos_formed` int DEFAULT '0',
  `points_scored` int DEFAULT '0',
  `move_timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`move_id`),
  UNIQUE KEY `unique_game_move` (`game_id`,`move_number`),
  KEY `player_id` (`player_id`),
  KEY `idx_game_moves` (`game_id`,`move_number`),
  KEY `idx_move_timestamp` (`move_timestamp`),
  CONSTRAINT `game_moves_ibfk_1` FOREIGN KEY (`game_id`) REFERENCES `games` (`game_id`) ON DELETE CASCADE,
  CONSTRAINT `game_moves_ibfk_2` FOREIGN KEY (`player_id`) REFERENCES `players` (`player_id`) ON DELETE CASCADE,
  CONSTRAINT `chk_non_negative_points` CHECK (((`sos_formed` >= 0) and (`points_scored` >= 0))),
  CONSTRAINT `chk_valid_position` CHECK (((`row_position` >= 0) and (`col_position` >= 0)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game_moves`
--

LOCK TABLES `game_moves` WRITE;
/*!40000 ALTER TABLE `game_moves` DISABLE KEYS */;
/*!40000 ALTER TABLE `game_moves` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `games`
--

DROP TABLE IF EXISTS `games`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `games` (
  `game_id` int NOT NULL AUTO_INCREMENT,
  `blue_player_id` int NOT NULL,
  `red_player_id` int NOT NULL,
  `game_mode` enum('SIMPLE','GENERAL') COLLATE utf8mb4_unicode_ci NOT NULL,
  `board_size` int NOT NULL,
  `blue_score` int DEFAULT '0',
  `red_score` int DEFAULT '0',
  `winner_id` int DEFAULT NULL,
  `game_status` enum('IN_PROGRESS','COMPLETED','ABANDONED') COLLATE utf8mb4_unicode_ci DEFAULT 'COMPLETED',
  `total_moves` int DEFAULT '0',
  `started_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `completed_at` timestamp NULL DEFAULT NULL,
  `duration_seconds` int DEFAULT NULL,
  PRIMARY KEY (`game_id`),
  KEY `idx_game_mode` (`game_mode`),
  KEY `idx_completed_at` (`completed_at`),
  KEY `idx_winner` (`winner_id`),
  KEY `idx_blue_player` (`blue_player_id`),
  KEY `idx_red_player` (`red_player_id`),
  CONSTRAINT `games_ibfk_1` FOREIGN KEY (`blue_player_id`) REFERENCES `players` (`player_id`) ON DELETE CASCADE,
  CONSTRAINT `games_ibfk_2` FOREIGN KEY (`red_player_id`) REFERENCES `players` (`player_id`) ON DELETE CASCADE,
  CONSTRAINT `games_ibfk_3` FOREIGN KEY (`winner_id`) REFERENCES `players` (`player_id`) ON DELETE SET NULL,
  CONSTRAINT `chk_board_size` CHECK ((`board_size` between 3 and 15)),
  CONSTRAINT `chk_different_players` CHECK ((`blue_player_id` <> `red_player_id`)),
  CONSTRAINT `chk_non_negative_scores` CHECK (((`blue_score` >= 0) and (`red_score` >= 0)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `games`
--

LOCK TABLES `games` WRITE;
/*!40000 ALTER TABLE `games` DISABLE KEYS */;
/*!40000 ALTER TABLE `games` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `player_stats`
--

DROP TABLE IF EXISTS `player_stats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `player_stats` (
  `stats_id` int NOT NULL AUTO_INCREMENT,
  `player_id` int NOT NULL,
  `games_played` int DEFAULT '0',
  `games_won` int DEFAULT '0',
  `games_lost` int DEFAULT '0',
  `games_drawn` int DEFAULT '0',
  `total_sos_formed` int DEFAULT '0',
  `total_points_scored` int DEFAULT '0',
  `last_updated` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`stats_id`),
  UNIQUE KEY `unique_player_stats` (`player_id`),
  CONSTRAINT `player_stats_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `players` (`player_id`) ON DELETE CASCADE,
  CONSTRAINT `chk_games_consistency` CHECK ((`games_played` = ((`games_won` + `games_lost`) + `games_drawn`))),
  CONSTRAINT `chk_non_negative_stats` CHECK (((`games_played` >= 0) and (`games_won` >= 0) and (`games_lost` >= 0) and (`games_drawn` >= 0) and (`total_sos_formed` >= 0) and (`total_points_scored` >= 0)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `player_stats`
--

LOCK TABLES `player_stats` WRITE;
/*!40000 ALTER TABLE `player_stats` DISABLE KEYS */;
/*!40000 ALTER TABLE `player_stats` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `players`
--

DROP TABLE IF EXISTS `players`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `players` (
  `player_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `displayname` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `playertype` enum('HUMAN','COMPUTER') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'HUMAN',
  `Created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `last_played` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`player_id`),
  UNIQUE KEY `username` (`username`),
  KEY `idusername` (`username`),
  KEY `idplayertype` (`playertype`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `players`
--

LOCK TABLES `players` WRITE;
/*!40000 ALTER TABLE `players` DISABLE KEYS */;
/*!40000 ALTER TABLE `players` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `vw_player_rankings`
--

DROP TABLE IF EXISTS `vw_player_rankings`;
/*!50001 DROP VIEW IF EXISTS `vw_player_rankings`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `vw_player_rankings` AS SELECT 
 1 AS `player_id`,
 1 AS `username`,
 1 AS `displayname`,
 1 AS `games_played`,
 1 AS `games_won`,
 1 AS `games_lost`,
 1 AS `games_drawn`,
 1 AS `total_sos_formed`,
 1 AS `total_points_scored`,
 1 AS `win_percentage`,
 1 AS `avg_sos_per_game`,
 1 AS `avg_points_per_game`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `vw_recent_games`
--

DROP TABLE IF EXISTS `vw_recent_games`;
/*!50001 DROP VIEW IF EXISTS `vw_recent_games`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `vw_recent_games` AS SELECT 
 1 AS `game_id`,
 1 AS `game_mode`,
 1 AS `board_size`,
 1 AS `blue_player`,
 1 AS `red_player`,
 1 AS `blue_score`,
 1 AS `red_score`,
 1 AS `winner`,
 1 AS `total_moves`,
 1 AS `duration_seconds`,
 1 AS `completed_at`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `vw_player_rankings`
--

/*!50001 DROP VIEW IF EXISTS `vw_player_rankings`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `vw_player_rankings` AS select `p`.`player_id` AS `player_id`,`p`.`username` AS `username`,`p`.`displayname` AS `displayname`,`ps`.`games_played` AS `games_played`,`ps`.`games_won` AS `games_won`,`ps`.`games_lost` AS `games_lost`,`ps`.`games_drawn` AS `games_drawn`,`ps`.`total_sos_formed` AS `total_sos_formed`,`ps`.`total_points_scored` AS `total_points_scored`,(case when (`ps`.`games_played` > 0) then round(((`ps`.`games_won` * 100.0) / `ps`.`games_played`),2) else 0 end) AS `win_percentage`,(case when (`ps`.`games_played` > 0) then round(((`ps`.`total_sos_formed` * 1.0) / `ps`.`games_played`),2) else 0 end) AS `avg_sos_per_game`,(case when (`ps`.`games_played` > 0) then round(((`ps`.`total_points_scored` * 1.0) / `ps`.`games_played`),2) else 0 end) AS `avg_points_per_game` from (`players` `p` left join `player_stats` `ps` on((`p`.`player_id` = `ps`.`player_id`))) order by (case when (`ps`.`games_played` > 0) then round(((`ps`.`games_won` * 100.0) / `ps`.`games_played`),2) else 0 end) desc,`ps`.`games_won` desc */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `vw_recent_games`
--

/*!50001 DROP VIEW IF EXISTS `vw_recent_games`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `vw_recent_games` AS select `g`.`game_id` AS `game_id`,`g`.`game_mode` AS `game_mode`,`g`.`board_size` AS `board_size`,`bp`.`displayname` AS `blue_player`,`rp`.`displayname` AS `red_player`,`g`.`blue_score` AS `blue_score`,`g`.`red_score` AS `red_score`,(case when (`g`.`winner_id` is null) then 'Draw' when (`g`.`winner_id` = `g`.`blue_player_id`) then `bp`.`displayname` else `rp`.`displayname` end) AS `winner`,`g`.`total_moves` AS `total_moves`,`g`.`duration_seconds` AS `duration_seconds`,`g`.`completed_at` AS `completed_at` from ((`games` `g` join `players` `bp` on((`g`.`blue_player_id` = `bp`.`player_id`))) join `players` `rp` on((`g`.`red_player_id` = `rp`.`player_id`))) where (`g`.`game_status` = 'COMPLETED') order by `g`.`completed_at` desc */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-29 16:52:07
