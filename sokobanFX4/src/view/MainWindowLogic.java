package view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import common.Level;
import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.db.Player_solved_level;

public class MainWindowLogic extends Observable implements Initializable, View, Observer {

	Level l = null;
	/*
	 * @FXML LevelDisplayer displayer;
	 */

	@FXML
	Label time;
	@FXML
	Label steps;
	@FXML
	Label msg;
	@FXML
	LevelDisplayer displayer;
	@FXML
	Label lab;
	@FXML
	Button boop;
	@FXML
	TextField name;
	@FXML
	Button solve;
	@FXML
	Button hint;
	@FXML
	TextArea hintFIeld;

	@FXML
	TWLogic tablewindow;

	private boolean up;
	private boolean down;
	private boolean left;
	private boolean right;

	
	private boolean is_gameover = false;
	private boolean music_plays;
	MediaPlayer mpplayer;
	private Timer timer = null;
	private long timemilis = 0;
	private long finishtimemilis = 0;
	private boolean stop = false;
	private String solution;
	
	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	public MainWindowLogic() {
		up = false;
		down = false;
		left = false;
		right = false;

		is_gameover = false;
		// timer = new Timer();
		displayer = new LevelDisplayer();
		String path;
		path = "./resources/music/The Simpsons 8 bit theme.mp3";
		Media media = new Media(new File(path).toURI().toString());
		mpplayer = new MediaPlayer(media);
		msg = new Label();
		music_plays = false;

		tablewindow = new TWLogic(this);
		tablewindow.addObserver(this);
	}

	public void startTimer() {

		timemilis = System.currentTimeMillis();
		if (timer == null) {
			Timer timer = new Timer();

			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							if (!stop) {
								time.setText(
										"time: " + (int) ((System.currentTimeMillis() - timemilis) / 1000) + "sec");
							}
						}
					});
				}
			}, 0, 1000);
		}
	}

	@Override
	public void start() {
		System.out.println("start");
	}

	@Override
	public void Load() {
		LinkedList<String> args = new LinkedList<String>();
		FileChooser f = new FileChooser();
		f.setTitle("Load");
		f.setInitialDirectory(new File("./resources/levels"));
		File chosen = f.showOpenDialog(null);
		if (chosen != null) {
			String path;
			path = chosen.getPath();
			args.add("Load");
			args.add(path);
			setChanged();
			notifyObservers(args);

			// fx stuff that have to do with:win function and rgistration
			boop.setVisible(false);
			name.setVisible(false);
			boop.setDisable(true);
			lab.setVisible(false);
			stop = false;
			is_gameover = false;

		}
	}

	public void UpChanger() {
		up = true;
	}

	public void DownChanger() {
		down = true;
	}

	public void LeftChanger() {
		left = true;
	}

	public void RightChanger() {
		right = true;
	}

	@Override
	public void Save() {
		LinkedList<String> args = new LinkedList<String>();
		FileChooser f = new FileChooser();
		f.setTitle("save");
		f.setInitialDirectory(new File("./resources/levels/"));
		File chosen = f.showSaveDialog(null);
		if (chosen != null) {
			args.add("Save");
			args.add(chosen.getPath());
			setChanged();
			notifyObservers(args);
		}
	}

	@Override
	public void close() { // TODO exit the window as well
		if (timer != null)
			timer.cancel();
		LinkedList<String> args = new LinkedList<String>();
		args.add("Exit");
		setChanged();
		notifyObservers(args);

	}

	// music
	public void Start_music() {
		music_plays = !music_plays;
		if (music_plays) {
			String path;
			path = "./resources/music/The Simpsons 8 bit theme.mp3";
			Media media = new Media(new File(path).toURI().toString());
			mpplayer = new MediaPlayer(media);
			mpplayer.setAutoPlay(true);
			MediaView mv = new MediaView(mpplayer);
			mpplayer.setCycleCount(Animation.INDEFINITE);
		} else
			mpplayer.stop();
	}

	public void stop_music() {
		Platform.runLater(new Runnable() {
			public void run() {
				Alert alert;
				alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("Really?Why???? . press start music again to stop :(");
				alert.setTitle("NOOOOOOOOOOOOOO");
				alert.setHeaderText(null);
				alert.showAndWait();

			}
		});

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		displayer.setLevel(this.l);

		displayer.addEventFilter(MouseEvent.MOUSE_CLICKED, (e) -> displayer.requestFocus());

		displayer.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (!is_gameover) {
					LinkedList<String> args = new LinkedList<String>();
					args.add("Move");

					if (event.getCode().toString().toLowerCase().equals(displayer.getUp_Key().toLowerCase())) {
						args.add("Up");
						setChanged();
						notifyObservers(args);

					} else if (event.getCode().toString().toLowerCase().equals(displayer.getDown_Key().toLowerCase())) {
						args.add("Down");
						setChanged();
						notifyObservers(args);

					} else if (event.getCode().toString().toLowerCase()
							.equals(displayer.getRight_Key().toLowerCase())) {
						args.add("Right");
						setChanged();
						notifyObservers(args);

					} else if (event.getCode().toString().toLowerCase().equals(displayer.getLeft_Key().toLowerCase())) {
						args.add("Left");
						setChanged();
						notifyObservers(args);

					}

					if (up) {
						displayer.setUp_Key(event.getCode().toString());
						up = false;
					}
					if (down) {
						displayer.setDown_Key(event.getCode().toString());
						down = false;
					}
					if (left) {
						displayer.setLeft_Key(event.getCode().toString());
						left = false;
					}
					if (right) {
						displayer.setRight_Key(event.getCode().toString());
						right = false;
					}

				}

			}
		});
	}

	public void setMoves(int moves) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				steps.setText("steps: " + moves);
			}
		});
	}

	@Override
	public void setLevel(Level l) {
		this.l = l;
		displayer.setLevel(this.l);
		displayer.redraw();
	}

	public void Table_open(ActionEvent eve) {
		if(this.l == null)
			return;
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TableWindow.fxml"));
			fxmlLoader.setController(tablewindow);
			Parent root1;
			root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void register_player_score() {
		boop.setVisible(false);
		boop.setDisable(true);
		name.setVisible(false);

		LinkedList<String> args = new LinkedList<String>();
		args.add("AddScoreToDB");
		args.add(name.getText());
		args.add(String.valueOf(finishtimemilis / 1000));// seconds
		String step_count = steps.getText().substring(7);
		System.out.println(step_count);
		args.add(step_count);// steps

		setChanged();
		notifyObservers(args);
	}

	@Override
	public void win() {
		boop.setVisible(true);
		boop.setDisable(false);
		;
		name.setVisible(true);
		lab.setVisible(true);
		is_gameover = true;
		stop = true;
		finishtimemilis = System.currentTimeMillis() - timemilis;

	}

	@Override
	public Level getLevel() {
		return l;
	}

	@Override
	public void solveLevel() {
		if(this.l == null){
			hintFIeld.setText("no\nlevel\n");
			return;
		}
		LinkedList<String> args = new LinkedList<String>();
		args.add("Solve");
		setChanged();
		notifyObservers(args);
	}
	public void HintLevel() {
		if(this.l == null){
			hintFIeld.setText("no\nlevel\n");
			return;
		}
		LinkedList<String> args = new LinkedList<>();
		args.add("Solve");
		args.add("hint");
		setChanged();
		notifyObservers(args);
	}
	@Override
	public void showSolution() {
		if(this.l == null){
			hintFIeld.setText("no\nlevel\n");
			return;
		}
		if(solution == null){
			hintFIeld.setText("can't\n reach\n solving\n server\n");
			return;
		}
		if(solution.equals("can't find a solution")){
			hintFIeld.setText("faild\n to find\n a solution\n");
			return;
		}
		String res = solution.replaceAll("u", "up\n").replaceAll("d","down\n").replaceAll("l", "left\n").replaceAll("r", "right\n");
		hintFIeld.setText("solution:\n"+res);
	}
	@Override
	public void showHint() {
		if(this.l == null){
			hintFIeld.setText("no\nlevel\n");
			return;
		}
		if(solution == null){
			hintFIeld.setText("can't\n reach\n solving\n server\n");
			return;
		}
		if(solution.equals("can't find a solution")){
			hintFIeld.setText("faild\n to find\n a solution\n");
			return;
		}
		String res = solution.substring(0, solution.length()/2);
		if(res.equals(""))
			hintFIeld.setText("really?");
		else{
			res = res.replaceAll("u", "up\n").replaceAll("d","down\n").replaceAll("l", "left\n").replaceAll("r", "right\n");
			hintFIeld.setText("hint:\n" + res);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		setChanged();
		notifyObservers(arg);
	}

	@Override
	public void displayHighscores(List<Player_solved_level> highscores) {
		this.tablewindow.setScoresData(highscores);
	}

}
