package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import javafx.scene.image.Image;
import model.data.*;

public class ObjectImageDictionary {
	HashMap<String,Image> _dictionary;
	
	public ObjectImageDictionary(){
		_dictionary = new HashMap<String,Image>();
		try {
			_dictionary.put(new Wall().getClass().toString(), new Image(new FileInputStream("./resources/graphics/wall.jpg")));
			_dictionary.put(new Sokoban().getClass().toString(), new Image(new FileInputStream("./resources/graphics/levelplayer.jpg")));
			_dictionary.put(new Box().getClass().toString(), new Image(new FileInputStream("./resources/graphics/levelwoodenbox.jpg")));
			_dictionary.put(null, new Image(new FileInputStream("./resources/graphics/levelfloor.jpg")));
			_dictionary.put(new Target().getClass().toString(), new Image(new FileInputStream("./resources/graphics/leveltarget.jpg")));
			
			
		} catch (FileNotFoundException e) { e.printStackTrace(); }
	}
	
	public Image getImage(String className){
		return _dictionary.get(className);
	}
}
