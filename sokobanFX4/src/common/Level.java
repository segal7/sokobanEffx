package common;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import model.data.Box;
import model.data.GameObject;
import model.data.Sokoban;
import model.data.Target;
import model.data.TextLevelDisplayer;
import model.data.Wall;
import view.LevelDisplayer;

@Entity( name= "Levels")
public class Level implements Serializable {
	//data members:
	@Id
	private String level_name;
	@Transient
	private int _score;
	@Transient
	private HashMap<Point,GameObject> _layout;
	@Transient
	private int XEdge = 0;
	@Transient
	private int YEdge = 0;

	@Transient
	private ArrayList<Box> _boxes;
	@Transient
	private ArrayList<Target> _targets;
	@Transient
	private ArrayList<Sokoban> _sokobans;
	@Transient
	private ArrayList<Wall> _walls;
	//Default contractor
	public Level()
	{
		_layout = new HashMap<Point,GameObject>();
		_boxes = new ArrayList<Box>();
		_targets = new ArrayList<Target>();
		_sokobans = new ArrayList<Sokoban>();
		_walls = new ArrayList<Wall>();
		level_name=null;
	}
	public Level(String name){
		_layout = new HashMap<Point,GameObject>();
		_boxes = new ArrayList<Box>();
		_targets = new ArrayList<Target>();
		_sokobans = new ArrayList<Sokoban>();
		_walls = new ArrayList<Wall>();
		level_name=name;
	}
	
	public Level(Level other) {
		this.level_name = other.level_name;
		this._score = other._score;
		this.XEdge = other.XEdge;
		this.YEdge = other.YEdge;
		
		_layout = new HashMap<Point, GameObject>();
		_boxes = new ArrayList<Box>();
		_targets = new ArrayList<Target>();
		_sokobans = new ArrayList<Sokoban>();
		_walls = new ArrayList<Wall>();

		for (Target t : other.get_targets())
			this.placeObject(new Target(), t.get_location());
		for (Box b : other.get_boxes())
			this.placeObject(new Box(), b.get_location());
		for (Sokoban s : other.get_sokobans())
			this.placeObject(new Sokoban(), s.get_location());
		for (Wall w : other.get_walls())
			this.placeObject(new Wall(), w.get_location());
		

	}
	//methods:
	//setters and getters

	public void updateMaxEdges(){
		int maxX = 0,maxY = 0;
		for(Point p: _layout.keySet())
		{
			if(_layout.get(p) != null)
			{
				if(maxX < p.x)
					maxX = p.x;
				if(maxY < p.y)
					maxY = p.y;
			}
		}
		XEdge = maxX+1;
		YEdge = maxY+1;
	}


	public void placeObject(GameObject obj,Point point){

		if(obj == null)
		{
			_layout.put(point, null);
			return;
		}
		//////////add the object host/////////////////
		obj.set_host(_layout.get(point));
		_layout.put(point, obj);
		obj.set_location(point);
		//////////////////////////////////////////////

		//////////add to appropriate list/////////////
		switch(obj.getClass().getSimpleName()){
		case "Box":
			if(!_boxes.contains(obj))
				_boxes.add((Box) obj);
			break;
		case "Target":
			if(!_targets.contains(obj))
				_targets.add((Target)obj);
			break;
		case "Sokoban":
			if(!_sokobans.contains(obj))
				_sokobans.add((Sokoban)obj);
			break;
		case "Wall":
			if(!_walls.contains(obj))
				_walls.add((Wall)obj);
			break;
		}
		//////////////////////////////////////////////
		/////////////update edges if neccesary////////
		if(point.x >= XEdge)
			XEdge = point.x + 1;
		if(point.y >= YEdge)
			YEdge = point.y + 1;
		//////////////////////////////////////////////
	}
	public void removeObject(Point position){
		GameObject obj = _layout.get(position);
		if(obj == null)
			return;

		GameObject host = obj.get_host();
		_layout.remove(position);
		_layout.put(position, host); //put back the host of the object

		if( obj instanceof Box)
			_boxes.remove(obj);
		if( obj instanceof Target)
			_targets.remove(obj);
		if( obj instanceof Sokoban)
			_sokobans.remove(obj);
		if( obj instanceof Wall)
			_walls.remove(obj);

		if(position.x == XEdge-1 || position.y == YEdge-1)
			updateMaxEdges();

	}

	public int boxesInPlace(){
		int count = 0;
		for(Box b:_boxes){
			for(Target t:_targets)
				if(t.get_location().equals(b.get_location()))
					count++;
		}

		return count;
	}
	public int boxesNotInPlace(){
		return _boxes.size() - boxesInPlace();
	}



	public int get_score() {
		return _score;
	}
	public int getXEdge() {
		return XEdge;
	}
	public int getYEdge() {
		return YEdge;
	}
	public void setXEdge(int xEdge) {
		XEdge = xEdge;
	}
	public void setYEdge(int yEdge) {
		YEdge = yEdge;
	}

	public String getLevel_name() {
		return level_name;
	}
	public void setLevel_name(String level_name) {
		this.level_name = level_name;
	}
	public void set_score(int score) {
		this._score = score;
	}
	public HashMap<Point, GameObject> get_layout() {
		return _layout;
	}
	public void set_layout(HashMap<Point, GameObject> _layout) {
		this._layout = _layout;
	}
	public ArrayList<Box> get_boxes() {
		return _boxes;
	}
	public void set_boxes(ArrayList<Box> _boxes) {
		this._boxes = _boxes;
	}
	public ArrayList<Target> get_targets() {
		return _targets;
	}
	public void set_targets(ArrayList<Target> _targets) {
		this._targets = _targets;
	}
	public ArrayList<Sokoban> get_sokobans() {
		return _sokobans;
	}
	public void set_sokobans(ArrayList<Sokoban> _sokobans) {
		this._sokobans = _sokobans;
	}
	public ArrayList<Wall> get_walls() {
		return _walls;
	}
	public void set_walls(ArrayList<Wall> _walls) {
		this._walls = _walls;
	}
	@Override
	public String toString() {
		return (new TextLevelDisplayer()).display(this);
	}

}
