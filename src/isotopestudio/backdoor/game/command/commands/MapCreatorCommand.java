package isotopestudio.backdoor.game.command.commands;

import isotopestudio.backdoor.game.applications.MapCreatorApplication;
import isotopestudio.backdoor.game.command.ICommand;

public class MapCreatorCommand implements ICommand {

	@Override
	public void handle(String[] args) {
		MapCreatorApplication.showApplication();
	}

	@Override
	public String getCommand() {
		return "mapcreator";
	}

	@Override
	public String getDescription() {
		return "Allows you to run application mapcreator.";
	}

}
