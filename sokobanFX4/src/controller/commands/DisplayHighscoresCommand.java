package controller.commands;

import model.Model;
import view.View;

public class DisplayHighscoresCommand extends Command{

	View v;
	Model m;
	
	public DisplayHighscoresCommand(View v, Model m) {
		this.v = v;
		this.m = m;
	}
	
	@Override
	public void execute() throws Exception {
		String order = this.params.remove(0);
		v.displayHighscores(m.getHighscores(order,this.params));
	}

}
