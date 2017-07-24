package model.data;



import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import common.Level;
import common.Point;


public class MyTextLevelSaver implements LevelSaver{

	HashMap<String,Character> _dictionary;
	
	public MyTextLevelSaver()
	{
		_dictionary = new HashMap<String,Character>();
		_dictionary.put(new Wall().getClass().toString(), '#');
		_dictionary.put(new Sokoban().getClass().toString(), 'A');
		_dictionary.put(null, ' ');
		_dictionary.put(new Box().getClass().toString(), '@');
		_dictionary.put(new Target().getClass().toString(), 'o');
	}
	
	@Override
	public void saveLevel(Level lvl,String filename) throws IOException
	{
		HashMap<Point, GameObject> layout = lvl.get_layout();
		
		PrintWriter out = new PrintWriter( new FileWriter(filename+".txt"));

		for(int j = 0; j < lvl.getYEdge(); j++)
		{
			for(int i = 0; i < lvl.getXEdge(); i++)
				if(layout.get(new Point(i,j))!=null)
					out.print(_dictionary.get(layout.get(new Point(i,j)).getClass().toString()));
				else
					out.print(_dictionary.get(null));
			out.print("\r\n");
		}
				
		
		out.close();
	}
}
