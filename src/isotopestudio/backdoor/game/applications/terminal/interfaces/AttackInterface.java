package isotopestudio.backdoor.game.applications.terminal.interfaces;

import java.security.SecureRandom;

import org.joml.Vector4i;

import doryanbessiere.isotopestudio.commons.lang.Lang;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.applications.terminal.TerminalApplication;
import isotopestudio.backdoor.game.applications.terminal.command.CommandTyped;
import isotopestudio.backdoor.network.packet.packets.PacketPlayerAttackElement;
import isotopestudio.backdoor.utils.ConsoleWriter;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class AttackInterface extends Interface {
	
	@Override
	public String content(TerminalApplication terminalApplication) {
		actions.clear();
		terminalApplication.commandListeners.clear();
		
		String key = BackdoorGame.getGameClient().getCommand();

		actions.put(".", new Runnable() {
			@Override
			public void run() {
				terminalApplication.load(InterfaceType.MAIN);
			}
		});

		terminalApplication.commandListeners.add(new CommandTyped() {
			@Override
			public boolean typed(String command) {
				if (command.equals(key)) {
					BackdoorGame.getGameClient().sendPacket(new PacketPlayerAttackElement(command));
					return true;
				}
				return false;
			}
		});

		ConsoleWriter writer = new ConsoleWriter();
		writer.addContainer(terminalApplication.title(), new Vector4i(1, 1, 1, 1));
		writer.backToLine();
		writer.backToLine();

		final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
		final String NUMBER = "0123456789";

		final String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_LOWER.toUpperCase() + NUMBER;
		final SecureRandom random = new SecureRandom();

		writer.addLine("                    Master key: ");
		String content = "";
		for (int line = 0; line < 9; line++) {
			if (line == 2 || line == 7) {
				content += "\r\n";
				continue;
			}
			String line_string = "";
			for (int x = 0; x < 16; x++) {
				StringBuilder sb = new StringBuilder(2);
				for (int i = 0; i < 2; i++) {

					// 0-62 (exclusive), random returns 0-61
					int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
					char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);

					sb.append(rndChar);
				}

				line_string += (line_string.equals("") ? sb.toString() : " " + sb.toString());
			}
			content += line_string + "\r\n";
		}
		writer.addContainer(content, new Vector4i(0, 0, 0, 0));
		writer.backToLine();
		writer.addTitle(ConsoleWriter.POSITION_CENTER, Lang.get("terminal_your_key", "%key%", key),
				new Vector4i(8, 1, 8, 0));
		writer.backToLine();
		writer.addLine(" [!] " + Lang.get("terminal_type_the_key"));
		writer.addLine(" [.] " + Lang.get("terminal_leave"));
		writer.backToLine();

		return writer.build();
	}
}
