package view;

import java.util.List;

import common.Level;
import model.db.Player_solved_level;

public interface View {
	public void setLevel(Level l);
	public Level getLevel();
	public void setMoves(int moves);
	public void startTimer();
	public void start();
	public void close();
	public void Load();
	public void Save();
	public void win();
	public void solveLevel();
	public void setSolution(String solution); 
	public void showSolution();
	public void displayHighscores(List<Player_solved_level> highscores);
	public void showHint();
}
