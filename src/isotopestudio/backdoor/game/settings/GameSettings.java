package isotopestudio.backdoor.game.settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import doryanbessiere.isotopestudio.commons.LocalDirectory;

/**
 * @author BESSIERE Doryan
 * @github https://www.github.com/DoryanBessiere/
 */
public class GameSettings extends Settings {

	private static final String NAME = "game.settings";

	public String datapack = "default";
	public String lang = "french";
	public String keyboard_layout = "azerty";
	public int refresh_rate = 300;

	public static GameSettings CACHE;

	public GameSettings() {
		super(new File(LocalDirectory.toFile(GameSettings.class), NAME));
	}

	@Override
	public GameSettings clone() {
		GameSettings gameSettings = new GameSettings();
		gameSettings.datapack = this.datapack;
		gameSettings.lang = this.lang;
		gameSettings.keyboard_layout = this.keyboard_layout;
		return gameSettings;
	}

	public boolean sameSettings(GameSettings gameSettings) {
		return gameSettings.datapack.equals(this.datapack) && gameSettings.lang.equals(this.lang) && gameSettings.keyboard_layout.equals(this.keyboard_layout);
	}

	public static boolean load() {
		try {
			CACHE = (GameSettings) new GameSettings().getSettings(GameSettings.class);
			return true;
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static GameSettings getSettings() {
		if (CACHE == null)
			load();
		return CACHE;
	}
}
