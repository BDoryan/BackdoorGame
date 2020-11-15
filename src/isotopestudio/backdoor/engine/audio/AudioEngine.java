package isotopestudio.backdoor.engine.audio;

import java.io.IOException;
import java.io.InputStream;

import isotopestudio.backdoor.game.settings.AudioSettings;
import isotopestudio.backdoor.utils.OggClip;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class AudioEngine {
	
	public static void playSound(InputStream audio) {
		AudioSettings audio_settings = AudioSettings.getSettings();
		float volume = audio_settings.volume_global;
		OggClip ogg;
		try {
			ogg = new OggClip(audio);
			ogg.setGain(volume);
			ogg.resume();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
