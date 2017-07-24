package view;

import java.util.LinkedList;

import controller.MyController;
import controller.server.CLIClientHandler;
import controller.server.MyServer;
import javafx.application.Application;
import javafx.stage.Stage;
import model.MyModel;
import model.policy.MySokobanPolicy;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {

	private static String[] arguments;
	private MyController cont;
	private MainWindowLogic mwl;

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader fxl = new FXMLLoader();
			BorderPane root = fxl.load(getClass().getResource("MainWindow.fxml").openStream());

			primaryStage.setTitle("Simpsoban");

			String server_ip = "localhost";
			int port = 2138;
			
			mwl = fxl.getController();
			MyModel m = new MyModel(new MySokobanPolicy(),server_ip,port);
			MyServer server = null;
			CLIClientHandler ch = new CLIClientHandler();

			if(arguments != null){
				if(arguments.length == 2){
					if(arguments[0].equals("-server"))
						server = new MyServer(Integer.parseInt(arguments[1]), ch);
					else
						System.out.println("invalid arguments, starting without server");
				}
			}
			//TODO add an error message when there is an invalid command sent to the server
			cont = new MyController(mwl, m, server);


			mwl.addObserver(cont);
			m.addObserver(cont);
			if(server!=null)
			{
				server.addObserver(cont);
				ch.addObserver(server);
			}
			cont.start();

			Scene scene = new Scene(root,600,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		arguments = args;
		launch(args);
	}

	@Override
	public void stop(){
		//System.out.println("bye bye ,calling stop");
		//cont.stop();
		LinkedList<String> params = new LinkedList<String>();
		params.add("Exit");
		cont.update(null, params);
	}
}
