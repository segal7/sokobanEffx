package controller.commands;

import model.Model;
import view.View;

public class LoadCommand extends Command{

	Model _model;
	View _view;
	
	public LoadCommand(Model m, View v) {
		this._model = m;
		this._view = v;
	}
	
	@Override
	public void execute() throws Exception {
		String path = params.get(0);
		_model.LoadLevel(path);
		_view.setLevel(_model.get_Level());
		_view.setMoves(_model.get_numOfMoves());
		_view.startTimer();
	}

}
