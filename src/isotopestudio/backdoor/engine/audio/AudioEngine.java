package isotopestudio.backdoor.engine.audio;

import java.io.IOException;
import java.io.InputStream;

import org.bytedeco.opencv.opencv_core.float16_t;

import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.settings.AudioSettings;
import isotopestudio.backdoor.utils.OggClip;

/**
 * @author BESSIERE
 * @github https://www.github.com/DoryanBessiere/
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
