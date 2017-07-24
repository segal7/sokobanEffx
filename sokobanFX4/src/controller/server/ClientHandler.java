package controller.server;

import java.io.InputStream;
import java.io.OutputStream;

public interface ClientHandler {
	void handleClient(InputStream inFromClient, OutputStream outToClient);
	public void msgToClient(String msg) throws InterruptedException;
	public void disconnectUser();
}
