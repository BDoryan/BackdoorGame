package isotopestudio.backdoor.game.command.commands;

import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.command.ICommand;

public class ExitCommand implements ICommand {

	@Override
	public String getCommand() {
		return "exit";
	}
	
	@Override
	public String getDescription() {
		return "Allows you to close the game";
	}
	
	@Override
	public void handle(String[] args) {
		System.out.println("Game closing, good bye :D");
		BackdoorGame.stop();
	}
}
