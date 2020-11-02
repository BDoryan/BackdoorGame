package isotopestudio.backdoor.game.command.commands;

import isotopestudio.backdoor.game.command.ICommand;

public class CreditCommand implements ICommand {

	@Override
	public void handle(String[] args) {
		System.out.println("");
		System.out.println(" ╔══════════════════╦═══════════════════╗");
		System.out.println(" ║ > DoryanBessiere ║ > Arthur Forgeard ║");
		System.out.println(" ║ -> Developper    ║ -> Designer       ║");
		System.out.println(" ╚══════════════════╩═══════════════════╝");
		System.out.println("");
		System.out.println(" ╔══════════════════════════════════════╗");
		System.out.println(" ║ > Library front (LEGUI)              ║");
		System.out.println(" ║ -> https://github.com/SpinyOwl/legui ║");
		System.out.println(" ╚══════════════════════════════════════╝");
		System.out.println("");
		System.out.println(" ╔════════════════════════════════════════════════════════════════════╗");
		System.out.println(" ║ Font FiraCode created by Nikita Prokopov (prokopov@gmail.com):     ║");
		System.out.println(" ║ -> https://github.com/tonsky/FiraCode/                             ║");
		System.out.println(" ║                                                                    ║");
		System.out.println(" ║ Licence:                                                           ║");
		System.out.println(" ║ -> https://github.com/tonsky/FiraCode/blob/master/LICENSE          ║");
		System.out.println(" ╚════════════════════════════════════════════════════════════════════╝");
		System.out.println("");
		System.out.println(" ╔════════════════════════════════════════════════════╗");
		System.out.println(" ║ > Icons by Alexandru Stoica                        ║");
		System.out.println(" ║ -> https://dribbble.com/alexandrustoica/tags/icons ║");
		System.out.println(" ╚════════════════════════════════════════════════════╝");
		System.out.println("");

	}

	@Override
	public String getCommand() {
		return "credit";
	}

	@Override
	public String getDescription() {
		return "Allows you to see the game credits";
	}
}
