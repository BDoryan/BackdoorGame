package isotopestudio.backdoor.game.command.commands;

import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.command.ICommand;

public class HelpCommand implements ICommand {

	@Override
	public String getCommand() {
		return "help";
	}
	
	@Override
	public void handle(String[] args) {
		System.out.println("List of commands:");
		for(ICommand command : ICommand.commands) {
			System.out.println(" ");
			System.out.println("  - "+command.getCommand());
			System.out.println("  > "+command.getDescription());
		}
		System.out.println(" ");
	}

	@Override
	public String getDescription() {
		return "Helps you java terminal";
	}
}
