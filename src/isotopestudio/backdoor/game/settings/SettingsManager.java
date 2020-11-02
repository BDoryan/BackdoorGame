package isotopestudio.backdoor.game.settings;

/**
 * @author BESSIERE Doryan
 * @github https://www.github.com/DoryanBessiere/
 */
public class SettingsManager {

	public static void load() {
		if(!GameSettings.load())
			System.err.println("Loading game settings failed!");
		if(!VideoSettings.load())
			System.err.println("Loading video settings failed!");
		if(!AudioSettings.load())
			System.err.println("Loading audio settings failed!");
	}
}
