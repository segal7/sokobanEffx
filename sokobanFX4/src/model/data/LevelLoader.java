package model.data;

import java.io.InputStream;

import common.Level;

public interface LevelLoader {
	public Level loadLevel(InputStream input) throws Exception;
}
