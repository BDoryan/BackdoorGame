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
public class AudioSettings extends Settings {

	private static final String NAME = "audio.settings";
	
	public float volume_global = 1f;
	
	public static AudioSettings CACHE;

	public AudioSettings() {
		super(new File(LocalDirectory.toFile(AudioSettings.class), NAME));
	}

	public static boolean load() {
		try {
			CACHE = (AudioSettings) new AudioSettings().getSettings(AudioSettings.class);
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

	@Override
	public AudioSettings clone() {
		return new AudioSettings();
	}
	
	public boolean sameSettings(AudioSettings audioSettings) {
		return true;
	}

	public static AudioSettings getSettings() {
		if (CACHE == null)
			load();
		return CACHE;
	}
}
