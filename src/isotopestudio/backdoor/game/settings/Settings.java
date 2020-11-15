package isotopestudio.backdoor.game.settings;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import doryanbessiere.isotopestudio.commons.GsonInstance;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class Settings {

	private File file;

	public Settings(File file) {
		this.file = file;
	}

	public Object getSettings(Class<?> settingsClass) throws JsonIOException, JsonSyntaxException, IOException {
		if (!file.exists()) {
			save();
		}
		Gson instance = GsonInstance.instance();
		FileReader reader = new FileReader(file);
		Settings settings = instance.fromJson(instance.newJsonReader(reader), settingsClass);
		reader.close();
		settings.file = this.file;
		settings.save();
		return settings;
	}

	public boolean save() {
		if (file.exists()) {
			if (!file.delete()) {
				System.err.println("File settings ("+getFile().getPath()+") cannot be deleted!");
				return false;
			}
		}

		try {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			GsonInstance.instance().toJson(this, writer);
			writer.close();

			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}
}
