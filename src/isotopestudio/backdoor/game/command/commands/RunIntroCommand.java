package isotopestudio.backdoor.game.command.commands;

import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.command.ICommand;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class RunIntroCommand implements ICommand {

	@Override
	public void handle(String[] args) {
		BackdoorGame.runIntro();
	}

	@Override
	public String getCommand() {
		return "runintro";
	}

	@Override
	public String getDescription() {
		return null;
	}
}
