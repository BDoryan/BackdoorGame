package isotopestudio.backdoor.game.applications.terminal.interfaces;

import java.util.HashMap;
import java.util.Map.Entry;

import org.joml.Vector4i;

import doryanbessiere.isotopestudio.commons.lang.Lang;
import isotopestudio.backdoor.core.gamescript.GameScript;
import isotopestudio.backdoor.core.gamescript.GameScript.GameScripts;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.applications.terminal.TerminalApplication;
import isotopestudio.backdoor.game.applications.terminal.command.CommandTyped;
import isotopestudio.backdoor.network.packet.packets.PacketPlayerExecScript;
import isotopestudio.backdoor.utils.ConsoleWriter;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class InventoryInterface extends Interface {

	@Override
	public String content(TerminalApplication terminalApplication) {
		actions.clear();
		terminalApplication.commandListeners.clear();

		actions.put(".", new Runnable() {
			@Override
			public void run() {
				terminalApplication.load(InterfaceType.MAIN);
			}
		});

		actions.put("1", new Runnable() {
			@Override
			public void run() {
				terminalApplication.load(InterfaceType.INVENTORY);
			}
		});
		
		terminalApplication.commandListeners.add(new CommandTyped() {
			@Override
			public boolean typed(String command) {
				String[] args = command.split(" ");
				if(args.length == 2 && args[0].startsWith("sh")) {
					String script = args[1];
					if(BackdoorGame.getGameClient().containsScript(script)) {
						BackdoorGame.getGameClient().sendPacket(new PacketPlayerExecScript(GameScripts.fromName(script).getGameScript()));
						terminalApplication.log(Lang.get("terminal_the_script_has_been_executed"));
					} else {
						terminalApplication.log(Lang.get("terminal_you_dont_have_this_script"));
					}
					return true;
				}
				return false;
			}
		});

		ConsoleWriter writer = new ConsoleWriter();
		writer.addContainer(terminalApplication.title(), new Vector4i(1, 1, 1, 1));
		writer.backToLine();
		writer.backToLine();
		String content = "";
		HashMap<String, Integer> scripts = new HashMap<>();
		if(BackdoorGame.getGameClient().getScripts().size() >= 1) {
			for(GameScript gameScript : BackdoorGame.getGameClient().getScripts()) {
				if(!scripts.containsKey(gameScript.getName())) {
					scripts.put(gameScript.getName(), 1);
				} else {
					scripts.put(gameScript.getName(), scripts.get(gameScript.getName()) + 1);
				}
			}
			for(Entry<String, Integer> entries : scripts.entrySet()) {
				String string =  entries.getValue()+"x "+entries.getKey();
				content += content.equals("") ? string : "\n"+string;
			}
		} else {
			content = "No thing";
		}
		writer.addTitleContainer(ConsoleWriter.POSITION_CENTER, Lang.get("terminal_inventory"), 3, content,
				new Vector4i(1, 1, 1, 1));
		writer.backToLine();
		writer.addLine(" [.] " + Lang.get("terminal_leave"));
		writer.addLine(" [1] " + Lang.get("terminal_refresh_scripts_list"));
		writer.backToLine();

		return writer.build();
	}
}
