package isotopestudio.backdoor.game.command.commands;

import org.liquidengine.legui.config.Configuration;

import isotopestudio.backdoor.engine.keyboard.KeyboardLoader;
import isotopestudio.backdoor.game.command.ICommand;
import isotopestudio.backdoor.game.settings.GameSettings;

public class KeyboardCommand implements ICommand {

	@Override
	public void handle(String[] args) {
		if (args.length == 0) {
			System.out.println("");
			System.out.println("Description > " + getDescription());
			System.out.println("");
			System.out.println("Commands:");
			System.out.println("  > " + getCommand() + " get");
			System.out.println("  > " + getCommand() + " set [keyboard layout]");
			System.out.println("  > " + getCommand() + " list");
		} else if (args.length == 1) {
			String arg1 = args[0];
			if (arg1.equalsIgnoreCase("get")) {
				System.out.println("Your keyboard layout now is: " + Configuration.getInstance().getKeyboardLayout());
				return;
			} else if (arg1.equalsIgnoreCase("list")) {
				System.out.println("");
				System.out.println("Keyboard Layout available:");
				for (String name : Configuration.getInstance().getKeyboardLayouts().keySet()) {
					System.out.println("  > " + name);
				}
				System.out.println("");
				return;
			}
		} else if (args.length == 2) {
			String arg1 = args[0];
			String arg2 = args[1];
			if (arg1.equalsIgnoreCase("set")) {
				if(Configuration.getInstance().getKeyboardLayouts().containsKey(arg1)) {
					System.out.println("Failed to load '"+arg1+"', not exist keyboard layout!");
					return;
				}
				System.out.println("Load '" + arg2 + "' keyboard layout, ");
				if (KeyboardLoader.load(arg2)) {
					GameSettings settings = GameSettings.getSettings();
					settings.keyboard_layout = arg2;
					settings.save();

					System.out.println("Loading success.");
				} else {
					System.err.println("Loading failed!");
				}
				return;
			}
		} else {
			System.err.println("Command invalid, type 'keyboard' for help!");
		}
	}

	@Override
	public String getCommand() {
		return "keyboard";
	}

	@Override
	public String getDescription() {
		return "Allows you to manage your keyboard settings in game";
	}
}
