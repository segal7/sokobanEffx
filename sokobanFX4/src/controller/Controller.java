package controller;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import controller.commands.Command;

public class Controller {

	private BlockingQueue<Command> queue;
	private boolean stop = false;
	
	public Controller() {
		//queue = new PriorityBlockingQueue<>()
		queue = new ArrayBlockingQueue<Command>(10);		
	}
	
	public void insertCommand(Command cmd) {
		try {
			queue.put(cmd);
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
	}
	
	public void start() {
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (!stop) {
					try {
						Command cmd = queue.take();
						if (cmd != null)
							cmd.execute();						
					} catch (InterruptedException e) { e.printStackTrace();} 
					  catch (Exception e) { e.printStackTrace();}
				}
				
			}
		});
		thread.start();
	}
	
	public void stop() {
		System.out.println("closing command queue.");
		stop = true;
	}
}
