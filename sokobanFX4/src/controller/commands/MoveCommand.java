package controller.commands;

import model.Model;
import view.View;

public class MoveCommand extends Command{

	Model _model;
	View _view;
	public MoveCommand(Model m, View v) {
		this._model=m;
		this._view=v;

	}

	@Override
	public void execute() throws Exception {
		_model.movePlayer(params.get(0));
		_view.setLevel(_model.get_Level());
		_view.setMoves(_model.get_numOfMoves());
		if(_model.hasWon())
			_view.win();
	}
}
