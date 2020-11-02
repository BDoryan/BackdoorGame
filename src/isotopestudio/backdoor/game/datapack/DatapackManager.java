package isotopestudio.backdoor.game.datapack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;

import doryanbessiere.isotopestudio.commons.GsonInstance;
import isotopestudio.backdoor.engine.datapack.DatapackObject;
import isotopestudio.backdoor.game.BackdoorGame;

/**
 * @author BESSIERE
 * @github https://www.github.com/DoryanBessiere/
 */
public class DatapackManager {

	public static boolean deleteDatapack(String name) {
		File datapack_directory = new File(BackdoorGame.getDatapackDirectory(), name);
		if (!datapack_directory.exists()) {
			System.err.println("The datapack directory indicated ['target' variable] does not exist.  (path="+datapack_directory.getPath()+").");
			BackdoorGame.crash();
			return false;
		}

		try {
			FileUtils.deleteDirectory(datapack_directory);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean renameDatapack(String name, String new_name) {
		BackdoorGame.getLogger().debug("Renaming of the datapack '"+name+"' by "+new_name);
		try {
			File datapack_directory = new File(BackdoorGame.getDatapackDirectory(), name);
			if (!datapack_directory.exists()) {
				System.err.println("The datapack directory indicated ['target' variable] does not exist.  (path="+datapack_directory.getPath()+").");
				BackdoorGame.crash();
				return false;
			}

			BackdoorGame.getLogger().debug("Applying the modification to the datapack file");
			
			File datapackJson = new File(datapack_directory, "datapack.json");

			FileReader reader = new FileReader(datapackJson);
			DatapackObject datapackObject = DatapackObject.fromJson(reader);
			datapackObject.setName(new_name);
			reader.close();

			if (!datapackJson.exists()) {
				System.err.println("The datapack does not have a file 'datapack.json' (path="+datapack_directory.getPath()+").");
				BackdoorGame.crash();
				return false;
			}

			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(datapackJson),
					StandardCharsets.UTF_8);
			GsonInstance.instance().toJson(datapackObject, writer);
			writer.close();
			

			BackdoorGame.getLogger().debug("Applying the change to the datapack folder name");
			
			if(!datapack_directory.renameTo(new File(datapack_directory.getParent(), new_name))) {
				System.err.println("It is impossible to rename the datapack directory. (path="
						+ datapackJson.getPath() + ")");
				BackdoorGame.crash();
				return false;
			}

			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean createDatapack(String name) {
		BackdoorGame.getLogger().debug("Creating the new datapack: " + name);
		File default_datapack = DatapackLoader.getDefaultDatapack().getDatapackDirectory();
		if (!default_datapack.exists()) {
			System.err.println("You need the default datapack in order to create a datapack.");
			BackdoorGame.crash();
			return false;
		}

		try {
			File datapack_directory = new File(BackdoorGame.getDatapackDirectory(), name);
			datapack_directory.mkdirs();

			BackdoorGame.getLogger().debug("Datapack directory created.");

			System.out.println("Copy of the default datapack files.");
			FileUtils.copyDirectory(default_datapack, datapack_directory);

			File datapackJson = new File(datapack_directory, "datapack.json");

			FileReader reader = new FileReader(datapackJson);
			DatapackObject datapackObject = DatapackObject.fromJson(reader);
			datapackObject.setName(name);
			datapackObject.setAuthor(BackdoorGame.getUser().getUsername());
			datapackObject.setVersion("0.0.1");
			reader.close();

			if (datapackJson.exists()) {
				if (!datapackJson.delete()) {
					System.err.println("It is impossible to delete the file 'datapack.json'. (path="
							+ datapackJson.getPath() + ")");
					BackdoorGame.crash();
					return false;
				}
			}
			datapackJson.createNewFile();

			BackdoorGame.getLogger().debug("Modification of the 'datapack.json' file");

			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(datapackJson),
					StandardCharsets.UTF_8);
			GsonInstance.instance().toJson(datapackObject, writer);
			writer.close();

			BackdoorGame.getLogger().debug("Creation finished reloading datapack list.");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
