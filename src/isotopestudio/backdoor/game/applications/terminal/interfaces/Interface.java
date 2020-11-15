package isotopestudio.backdoor.game.applications.terminal.interfaces;

import java.util.HashMap;

import isotopestudio.backdoor.game.applications.terminal.TerminalApplication;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
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
