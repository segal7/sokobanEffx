package model.data;

import common.Level;

public interface LevelSaver {

	public void saveLevel(Level lvl,String filename) throws Exception;
}
