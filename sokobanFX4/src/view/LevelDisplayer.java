package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import common.Level;
import common.Point;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import model.data.Box;
import model.data.Sokoban;
import model.data.Target;
import model.data.Wall;

public class LevelDisplayer extends Canvas {

	Level level;
	private StringProperty Up_Key;
	private StringProperty Down_Key;
	private StringProperty Left_Key;
	private StringProperty Right_Key;

	private	HashMap<String,Image> _dictionary;

	private StringProperty wallpic;
	private StringProperty sokobanpic;
	private StringProperty floorpic;
	private StringProperty boxpic;
	private StringProperty targetpic;


	public LevelDisplayer(){
		wallpic= new SimpleStringProperty();
		sokobanpic= new SimpleStringProperty();
		floorpic= new SimpleStringProperty();
		boxpic= new SimpleStringProperty();
		targetpic= new SimpleStringProperty();
		wallpic.set("./resources/graphics/wall.jpg");
		sokobanpic.set("./resources/graphics/wall.jpg");
		floorpic.set("./resources/graphics/wall.jpg");
		boxpic.set("./resources/graphics/wall.jpg");
		targetpic.set("./resources/graphics/wall.jpg");

		_dictionary = new HashMap<String,Image>();
		Up_Key= new SimpleStringProperty();
		Down_Key= new SimpleStringProperty();
		Left_Key= new SimpleStringProperty();
		Right_Key= new SimpleStringProperty();
	}

	public String getTargetpic() {
		return targetpic.get();
	}

	public void setTargetpic(String targetpic) {
		this.targetpic.set(targetpic);;
	}
	public String getWallpic() {
	return wallpic.get();
	}

	public void setWallpic(String wallpic) {
	this.wallpic.set(wallpic);
	}

	public String getSokobanpic() {
	return sokobanpic.get();
	}

	public void setSokobanpic(String sokobanpic) {
	this.sokobanpic.set(sokobanpic);;
	}

	public String getFloorpic() {
	return floorpic.get();
	}

	public void setFloorpic(String floorpic) {
	this.floorpic.set(floorpic);;
	}

	public String getBoxpic() {
	return boxpic.get();
	}

	public void setBoxpic(String boxpic) {
	this.boxpic.set(boxpic);
	}

	public void setLevel(Level level) {
		this.level = level;
		redraw();
	}

	public String getUp_Key() {
		return Up_Key.get();
	}

	public void setUp_Key(String up_Key) {
		Up_Key.set(up_Key);
	}

	public String getDown_Key() {
		return Down_Key.get();
	}

	public void setDown_Key(String down_Key) {
		Down_Key.set(down_Key);
	}

	public String getLeft_Key() {
		return Left_Key.get();
	}

	public void setLeft_Key(String left_Key) {
		Left_Key.set(left_Key);
	}

	public String getRight_Key() {
		return Right_Key.get();
	}

	public void setRight_Key(String right_Key) {
		Right_Key.set(right_Key);
	}
	private Image getImage(String className){
		return _dictionary.get(className);
	}

	public Level getLevel(){ return this.level; }

	public void redraw(){
		try {
			_dictionary.put(new Wall().getClass().toString(), new Image(new FileInputStream(wallpic.get())));
			_dictionary.put(new Sokoban().getClass().toString(), new Image(new FileInputStream(sokobanpic.get())));
			_dictionary.put(new Box().getClass().toString(), new Image(new FileInputStream(boxpic.get())));
			_dictionary.put(null, new Image(new FileInputStream(floorpic.get())));
			_dictionary.put(new Target().getClass().toString(), new Image(new FileInputStream(targetpic.get())));


		} catch (FileNotFoundException e) { e.printStackTrace(); }
		if(level != null){
			double W = getWidth();
			double H = getHeight();

			double w = W/(level.getXEdge());
			double h = H/(level.getYEdge());

			GraphicsContext gc = getGraphicsContext2D();
			gc.clearRect(0, 0, W, H);

			for(int i = 0; i < level.getXEdge(); i++)
				for(int j = 0; j < level.getYEdge(); j++){
					if(level.get_layout().get(new Point(i,j)) != null)
						gc.drawImage(getImage(level.get_layout().get(new Point(i,j)).getClass().toString()), i*w, j*h,w,h);
					else
						gc.drawImage(getImage(null), i*w, j*h,w,h);
				}

		}

	}
}
