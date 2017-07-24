package model;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import common.Level;
import common.Point;
import model.data.GameObject;
import model.data.LevelLoader;
import model.data.LevelSaver;
import model.data.MyObjLevelLoader;
import model.data.MyObjLevelSaver;
import model.data.MyTextLevelLoader;
import model.data.MyTextLevelSaver;
import model.data.MyXMLLevelLoader;
import model.data.MyXMLLevelSaver;
import model.data.Sokoban;
import model.db.Player_solved_level;
import model.policy.Policy;

public class MyModel extends Observable implements Model{
	private Level initialLevel;
	private Level _loadedLevel;
	private Policy _policy;
	private int _numOfMoves = 0;

	private HashMap<String,LevelLoader> _loaders;
	private HashMap<String,LevelSaver> _savers;

	private String history;
	private String solverServerIp;
	private int solverServerPort; 
	
	public String getSolverServerIp() {
		return solverServerIp;
	}

	public void setSolverServerIp(String solverServerIp) {
		this.solverServerIp = solverServerIp;
	}

	public int getSolverServerPort() {
		return solverServerPort;
	}

	public void setSolverServerPort(int solverServerPort) {
		this.solverServerPort = solverServerPort;
	}

	public MyModel(Policy pol,String server_ip, int port){
		this._policy = pol;
		_numOfMoves = 0;
		//loader dictionary
		_loaders = new HashMap<String,LevelLoader>();
		_loaders.put("txt",new MyTextLevelLoader());
		_loaders.put("xml",new MyXMLLevelLoader());
		_loaders.put("obj", new MyObjLevelLoader());
		//saver dictionary
		_savers = new HashMap<String,LevelSaver>();
		_savers.put("txt", new MyTextLevelSaver());
		_savers.put("obj", new MyObjLevelSaver());
		_savers.put("xml", new MyXMLLevelSaver());
		
		history = "";
		solverServerIp = server_ip;
		solverServerPort = port;
	}


	public int get_numOfMoves() {
		return _numOfMoves;
	}

	public void set_numOfMoves(int _numOfMoves) {
		this._numOfMoves = _numOfMoves;
	}

	public String getHistory() {
		return history;
	}
	
	public void setPolicy(Policy poc){
		this._policy = poc;
	}

	@Override
	public void movePlayer(String direction) throws Exception {

		if(_loadedLevel == null)
			throw new NullPointerException("there is no level to move in");
		Sokoban sokob = _loadedLevel.get_sokobans().get(0);
		if(sokob == null)
			throw new NullPointerException("there are no sokobans in this level to move");
		move(sokob,direction);
		
		_numOfMoves++;
		if(direction.equals("Up"))
			history += "u";
		if(direction.equals("Down"))
			history += "d";
		if(direction.equals("Right"))
			history += "r";
		if(direction.equals("Left"))
			history += "l";
	}

	@Override
	public Level get_Level() {
		return _loadedLevel;
	}

	@Override
	public void LoadLevel(String path) throws Exception {
		String filetype = "";
		int dotidx = path.length();
		if(path.indexOf(".")==-1)
			throw new FileNotFoundException("this file name is invalid");
		while(path.charAt(--dotidx)!='.');//find the index of the "." indicating the file type
		filetype = path.substring(dotidx+1, path.length());
		_loadedLevel = _loaders.get(filetype).loadLevel(new BufferedInputStream(new FileInputStream(path)));
		int slashidx = path.length()-1;
		while(path.charAt(slashidx--) != '\\');
		_loadedLevel.setLevel_name(path.substring(slashidx+2,dotidx));
		_numOfMoves = 0;
		history = "";
		initialLevel = new Level(_loadedLevel);
	}

	@Override
	public void SaveLevel(String path) throws Exception {
		if(_loadedLevel == null)
			throw new NullPointerException("no lvl to save");
		String filetype = "";
		int dotidx = path.length();
		if(path.indexOf(".")==-1)
			throw new FileNotFoundException("invalid file name");
		while(path.charAt(--dotidx)!='.');//find the index of the "." indicating the file type
		filetype = path.substring(dotidx+1, path.length());
		_savers.get(filetype).saveLevel(_loadedLevel, path.substring(0, dotidx));

	}



	private boolean move(GameObject obj,String direction) throws Exception{
		boolean willmove = false;
		Point currentLocation = obj.get_location();
		Point nextLocation = nextPoint(currentLocation,direction);
		GameObject nextObj = _loadedLevel.get_layout().get(nextLocation);

		if(_policy.canStepOn(obj, nextObj))
			willmove = true;
		else if(_policy.canPush(obj, nextObj))
			if(move(nextObj,direction))
				willmove = true;

		if(willmove)
		{
			GameObject host = obj.get_host();
			_loadedLevel.placeObject(obj,nextLocation);
			_loadedLevel.placeObject(host, currentLocation);
		}

		return willmove;
	}

	private Point nextPoint(Point p,String toDirection) throws Exception{
		Point res = new Point(p.x,p.y);
		switch (toDirection.toLowerCase())
		 {

		  case ("up") :
			  res.x = p.x; res.y = p.y-1;
			  break;
		  case ("down"):
			  res.x = p.x; res.y = p.y+1;
		       break;
		  case ("left"):
			  res.x = p.x-1; res.y = p.y;
		  	   break;
		  case ("right"):
			  res.x = p.x+1; res.y = p.y;
		  	   break;
		  default:
			  throw new Exception("no such direction");
		 }
		return res;
	}

	@Override
	public Policy get_policy() {
		return _policy;
	}

	@Override
	public boolean hasWon() {
		if(_loadedLevel.boxesNotInPlace() == 0)
			return true;
		return false;
	}



	@Override
	public void saveScoreToDB(Player_solved_level psl) {
		try {
			Socket connectSocket = new Socket(solverServerIp, solverServerPort);
			BufferedWriter output = new BufferedWriter(new OutputStreamWriter(connectSocket.getOutputStream()));
			BufferedReader b = new BufferedReader(new InputStreamReader(connectSocket.getInputStream()));
			
			output.write("store\n");
			output.write(psl.get_level_name() + "\n");
			output.write(psl.get_Player_name() + "\n");
			output.write(""+psl.get_steps() + "\n");
			output.write(""+psl.get_seconds() + "\n");
			output.write(psl.get_solution() + "\n");
			output.flush();
			
			connectSocket.close();
			output.close();
			b.close();
		} catch (ConnectException e) {
			System.out.println("can't connect to server");
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	private String compressLevelToString(Level l){
		return l.toString().replaceAll("\n", "k") + "\n";
	}

	@Override
	public String getSolutionToLevel() {
		String res = "";
		compressLevelToString(initialLevel);
		try {
			Socket connectSocket = new Socket(solverServerIp, solverServerPort);
			BufferedWriter output = new BufferedWriter(new OutputStreamWriter(connectSocket.getOutputStream()));
			BufferedReader b = new BufferedReader(new InputStreamReader(connectSocket.getInputStream()));
			
			output.write("solve\n");
			output.write(compressLevelToString(this.initialLevel));
			output.flush();
			
			res = b.readLine();
			
			connectSocket.close();
			output.close();
			b.close();
		} catch (ConnectException e) {
			System.out.println("can't connect to server");
			return null;
		} catch (IOException e){
			e.printStackTrace();
		}
		return res;
	}

	
	@Override
	public List<Player_solved_level> getHighscores(String order,List<String> specifics) {
		
		List<Player_solved_level> res = new LinkedList<Player_solved_level>();
		
		try {
			Socket connectSocket = new Socket(solverServerIp, solverServerPort);
			BufferedWriter output = new BufferedWriter(new OutputStreamWriter(connectSocket.getOutputStream()));
			BufferedReader b = new BufferedReader(new InputStreamReader(connectSocket.getInputStream()));
			
			output.write("get_scores\n");
			output.write(order+"\n");
			System.out.println(specifics);
			for(String s : specifics)
				output.write(s+"\n");
			output.write("stop\n");
			output.flush();
			
			String stringed_psl = b.readLine();
			while(!stringed_psl.equals("stop")){
				if(turn_string_to_psl(stringed_psl) != null)
					res.add(turn_string_to_psl(stringed_psl));
				System.out.println(stringed_psl);
				stringed_psl = b.readLine();
			}
			
			connectSocket.close();
			output.close();
			b.close();
		} catch (ConnectException e) {
			System.out.println("can't connect to server");
			return null;
		} catch (IOException e){
			e.printStackTrace();
		}
		
		return res;
	}


	
	private Player_solved_level turn_string_to_psl(String rep){
		rep = rep.substring(7, rep.length()-1);
		String[] arr = rep.split(",");
		return new Player_solved_level(Integer.parseInt(arr[2]), Integer.parseInt(arr[2]), arr[0],arr[1], arr[4]);
	}
	
}
