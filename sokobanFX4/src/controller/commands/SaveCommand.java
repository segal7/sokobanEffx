package controller.commands;

import model.Model;
import view.View;

public class SaveCommand extends Command{

	Model _model;
	View _view;
	
	public SaveCommand(Model m, View v) {
		this._model = m;
		this._view = v;
	}
	
	@Override
	public void execute() throws Exception {
		_model.SaveLevel(params.get(0));
		
	}

}
