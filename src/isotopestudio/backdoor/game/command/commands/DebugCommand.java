package isotopestudio.backdoor.game.command.commands;

import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.command.ICommand;

public class DebugCommand implements ICommand {

	@Override
	public String getCommand() {
		return "debug";
	}
	
	@Override
	public String getDescription() {
		return "Gives you debug information";
	}
	
	@Override
	public void handle(String[] args) {
		System.out.println("Opening the debug window");
		BackdoorGame.debugWindow();
	}
}
