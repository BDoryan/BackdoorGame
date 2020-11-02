package isotopestudio.backdoor.game.applications.terminal.interfaces;

import org.joml.Vector4i;

import doryanbessiere.isotopestudio.commons.lang.Lang;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.applications.terminal.TerminalApplication;
import isotopestudio.backdoor.utils.ConsoleWriter;

/**
 * @author BESSIERE Doryan
 * @github https://www.github.com/DoryanBessiere/
 */
public class MainInterface extends Interface {

	@Override
	public String content(TerminalApplication terminalApplication) {
		actions.clear();
		terminalApplication.commandListeners.clear();

		actions.put("1", new Runnable() {
			@Override
			public void run() {
				if (BackdoorGame.getGameParty() == null)
					return;
				if (BackdoorGame.getGameClient().getTargetAddress() == null) {
					terminalApplication.log(Lang.get("terminal_your_are_not_connected_on_a_server"));
					return;
				}
				terminalApplication.load(InterfaceType.ATTACK);
			}
		});

		actions.put("2", new Runnable() {
			@Override
			public void run() {
				terminalApplication.load(InterfaceType.INVENTORY);
			}
		});
		actions.put(".", new Runnable() {
			@Override
			public void run() {
				terminalApplication.log("You have been disconnected from the current server!");
				BackdoorGame.getGameParty().disconnect();
			}
		});

		String[] list = new String[] { "[1] " + Lang.get("terminal_use_a_command"),
				"[2] " + Lang.get("terminal_use_a_script"), "", "[.] " + Lang.get("terminal_leave") };

		String address = BackdoorGame.getGameClient().getTargetAddress();
		if(address == null) {
			ConsoleWriter writer = new ConsoleWriter();
			writer.addContainer("Information cannot be recovered!", new Vector4i(1, 1, 1, 1));
			writer.backToLine();

			return writer.build();
		}

		ConsoleWriter writer = new ConsoleWriter();
		writer.addContainer(terminalApplication.title(), new Vector4i(1, 1, 1, 1));
		writer.backToLine();
		writer.backToLine();
		writer.addLine("[!] " + Lang.get("terminal_you_are_connected_to_the_server", "%address%", address));
		writer.addList("[!] " + Lang.get("terminal_select_a_attack_type"), list);
		writer.backToLine();

		return writer.build();
	}
}
