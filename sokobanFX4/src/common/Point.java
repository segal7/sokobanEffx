package common;

import java.io.Serializable;

public class Point implements Serializable{
	public int x , y;
	
	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Point(){ x = 0; y = 0; }
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "("+x+","+y+")";
	}
	@Override
	public boolean equals(Object obj) {
		if(obj == this)
			return true;
		if(!(obj instanceof Point))
			return false;
		
		Point p = (Point)obj;
		if(p.x == this.x && p.y == this.y)
			return true;
		else
			return false;
	}
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
}
