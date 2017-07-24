package controller.server;

import java.io.IOException;

public interface Server {
	public void runServer() throws IOException;
	public void stop();
	public void sendMsgToClient(String msg);
	public ClientHandler getHandler();
	public void disconnectUser();
	
}
