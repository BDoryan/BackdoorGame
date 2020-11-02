package isotopestudio.backdoor.game.command.commands;

import doryanbessiere.isotopestudio.commons.Toolkit;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.command.ICommand;
import isotopestudio.backdoor.game.settings.GameSettings;
import isotopestudio.backdoor.game.settings.VideoSettings;

/**
 * @author BESSIERE Doryan
 * @github https://www.github.com/DoryanBessiere/
 */
public class SettingsCommand implements ICommand {

	@Override
	public void handle(String[] args) {
		if (args.length == 0) {
			System.out.println("");
			System.out.println("Description > " + getDescription());
			System.out.println("");
			System.out.println("Commands:");
			System.out.println("  > " + getCommand() + " set <setting> <value>");
			System.out.println("  > " + getCommand() + " get");
			return;
		} else if (args.length == 3) {
			String arg1 = args[0];
			String arg2 = args[1];
			String arg3 = args[2];

			if (arg1.equalsIgnoreCase("set")) {
				if (arg2.equalsIgnoreCase("datapack")) {
					if (BackdoorGame.loadDatapack(arg3)) {
						GameSettings gameSettings = GameSettings.getSettings();
						gameSettings.datapack = arg3;
						gameSettings.save();

						System.out.println("Now you using '" + arg3 + "' datapack !");
					} else {
						System.err.println("This datapack cannot be set!");
					}
					return;
				} else if (arg2.equalsIgnoreCase("lang")) {
					if (BackdoorGame.loadLang(arg3)) {
						GameSettings gameSettings = GameSettings.getSettings();
						gameSettings.lang = arg3;
						gameSettings.save();
						
						BackdoorGame.loadLang(arg3);

						System.out.println("Now you using '" + arg3 + "' for the game language !");
					} else {
						System.err.println("This language cannot be set!");
					}
					return;
				} else if (arg2.equalsIgnoreCase("framelimiter")) {
					if (Toolkit.isBoolean(arg3)) {
						boolean value = Boolean.valueOf(arg3);

						VideoSettings video_settings = VideoSettings.getSettings();
						video_settings.frames_limiter = value;
						video_settings.save();

						if (value) {
							System.out.println("Now you have frame limiter!");
						} else {
							System.out.println("Now you don't have frame limiter!");
						}
						return;
					} else {
						System.err.println("This value cannot be set!");
					}
					return;
				} else if (arg2.equalsIgnoreCase("framelimit")) {
					if (Toolkit.isInteger(arg3)) {
						Integer value = Integer.valueOf(arg3);

						VideoSettings video_settings = VideoSettings.getSettings();
						video_settings.frames_limit = value;
						video_settings.save();

						System.out.println("Now your fps are limited to " + value + "fps.");
						return;
					}
					return;
				} else if (arg2.equalsIgnoreCase("keyboard")) {
					System.out.println("If you want to change the keyboard values ​​use this 'keyboard' command");
					return;
				}
			}
		} else if (args.length == 1) {
			String arg1 = args[0];

			if (arg1.equalsIgnoreCase("get")) {
				GameSettings game_settings = GameSettings.getSettings();
				VideoSettings video_settings = VideoSettings.getSettings();
				System.out.println("");
				System.out.println("Your game settings");
				System.out.println("");
				System.out.println("['datapack'] > " + game_settings.datapack);
				System.out.println("['lang'] > " + game_settings.lang);
				System.out.println("['keyboard'] > " + game_settings.keyboard_layout);
				System.out.println("['framelimiter'] > " + video_settings.frames_limiter);
				System.out.println("['framelimit'] > " + video_settings.frames_limit);
				System.out.println("");
				return;
			}
		}

		System.err.println("Command invalid, type '"+getCommand()+"' for help!");
	}

	@Override
	public String getCommand() {
		return "settings";
	}

	@Override
	public String getDescription() {
		return "Allows you to change your game settings";
	}
}
