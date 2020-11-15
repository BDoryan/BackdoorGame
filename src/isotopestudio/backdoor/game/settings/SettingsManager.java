package isotopestudio.backdoor.game.settings;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
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
