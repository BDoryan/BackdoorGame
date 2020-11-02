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
public class VideoSettings extends Settings {
	
	private static final String NAME = "video.settings";

	public boolean frames_limiter = false;
	public boolean fullscreen = false;
	public int frames_limit = 60;

	public boolean antialiasing = true;
	
	public static VideoSettings CACHE;

	public VideoSettings() {
		super(new File(LocalDirectory.toFile(VideoSettings.class), NAME));
	}
	
	@Override
	public VideoSettings clone() {
		VideoSettings videoSettings = new VideoSettings();
		videoSettings.frames_limiter = this.frames_limiter;
		videoSettings.fullscreen = this.fullscreen;
		videoSettings.frames_limit = this.frames_limit;
		return videoSettings;
	}
	
	public boolean sameSettings(VideoSettings videoSettings) {
		return videoSettings.frames_limiter == (this.frames_limiter) &&
			   videoSettings.fullscreen == (this.fullscreen) && 
			   videoSettings.frames_limit == (this.frames_limit);
	}
	
	public static boolean load() {
		try {
			CACHE = (VideoSettings) new VideoSettings().getSettings(VideoSettings.class);
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
	
	public static VideoSettings getSettings() {
		if(CACHE == null)
			load();
		return CACHE;
	}
}
