package model.data;

import java.io.Serializable;

import common.Point;


public abstract class GameObject implements Serializable{
	private Point _location;
	// the host is the game object "beneath" this object such as when a box is going over a target or the player is
	private GameObject _host = null;

	public GameObject(){}
	
	public GameObject get_host(){
		return _host;
	}
	public void set_host(GameObject host){
		_host = host;
	}
	
	public Point get_location() {
		return _location;
	}

	public void set_location(Point position) {
		this._location = position;
	}	
}
