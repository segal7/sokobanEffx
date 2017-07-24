package model.data;

import java.util.HashMap;

import common.Level;
import common.Point;
import model.data.Box;
import model.data.GameObject;
import model.data.Sokoban;
import model.data.Target;
import model.data.Wall;

public class TextLevelDisplayer {

	HashMap<String,Character> _dictionary;
	
	public TextLevelDisplayer() {
		_dictionary = new HashMap<String,Character>();
		_dictionary.put(new Wall().getClass().toString(), '#');
		_dictionary.put(new Sokoban().getClass().toString(), 'A');
		_dictionary.put(null, ' ');
		_dictionary.put(new Box().getClass().toString(), '@');
		_dictionary.put(new Target().getClass().toString(), 'o');
	}
	
	public String display(Level l) {
		String res = "\n";
		
		if(l!= null)
		{
			HashMap<Point, GameObject> layout= l.get_layout();
	
			for(int j = 0; j < l.getYEdge(); j++)
			{
				for(int i = 0; i < l.getXEdge(); i++)
				{
					if(layout.get(new Point(i,j))!=null)
						res+=(_dictionary.get(layout.get(new Point(i,j)).getClass().toString())); 
					else
						res+=" ";
				}	
				res+="\n";
			}
		}
		
		return res;
	}
}
