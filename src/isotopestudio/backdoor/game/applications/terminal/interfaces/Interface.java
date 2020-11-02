package isotopestudio.backdoor.game.applications.terminal.interfaces;

import java.util.HashMap;

import isotopestudio.backdoor.game.applications.terminal.TerminalApplication;

/**
 * @author BESSIERE Doryan
 * @github https://www.github.com/DoryanBessiere/
 */
public abstract class Interface {

	protected HashMap<String, Runnable> actions = new HashMap<>();
	
	/**
	 * @return the actions
	 */
	public HashMap<String, Runnable> getActions() {
		return actions;
	}
	
	public abstract String content(TerminalApplication terminalApplication);

}
