package model;

import java.util.List;

import common.Level;
import model.db.Player_solved_level;
import model.policy.Policy;

public interface Model {
	void movePlayer(String direction) throws Exception;
	Level get_Level();
	Policy get_policy();
	void LoadLevel(String path) throws Exception;
	void SaveLevel(String path) throws Exception;
	int get_numOfMoves();
	boolean hasWon();
	void saveScoreToDB(Player_solved_level psl);
	String getSolutionToLevel();
	String getHistory();
	List<Player_solved_level> getHighscores(String order,List<String> specifics);
}
