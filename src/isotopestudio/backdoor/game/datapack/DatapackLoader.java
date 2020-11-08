package isotopestudio.backdoor.game.datapack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.swing.JOptionPane;

import org.joml.Vector4i;
import org.liquidengine.legui.image.StbBackedLoadableImage;
import org.liquidengine.legui.style.font.FontRegistry;

import isotopestudio.backdoor.engine.datapack.DataParameters;
import isotopestudio.backdoor.engine.datapack.DatapackObject;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.utils.ConsoleWriter;

public class DatapackLoader {
	
	public static DatapackLoader DEFAULT = null;

	private File datapack_directory;

	protected HashMap<String, StbBackedLoadableImage> images = new HashMap<String, StbBackedLoadableImage>();
	protected HashMap<String, DataParameters> datas = new HashMap<String, DataParameters>();
	protected HashMap<String, File> audios = new HashMap<String, File>();
	
	public DatapackObject datapackObject;

	public void loadDirectory(File datapack_directory) {
		this.datapack_directory = datapack_directory;
		if (datapack_directory != null && datapack_directory.exists() && datapack_directory.isDirectory()) {
			load();
		} else {
			JOptionPane.showMessageDialog(null, "The datapack is not valid to be used (either does not exist or is not a file)", "Fatal error",
					JOptionPane.ERROR_MESSAGE);
			BackdoorGame.crash();
		}
	}

	public void load() {
		try {
			this.images.clear();
			this.datas.clear();
			
			System.out.println("[DatapackLoader] Loading images...");
			this.images.put("desktop_background", loadImage("desktop_background"));
			this.images.put("icon_taskbar", loadImage("icons/taskbar"));
			this.images.put("window_icon_terminal", loadImage("icons/terminal"));
			this.images.put("window_icon_dashboard", loadImage("icons/dashboard"));
			this.images.put("window_icon_network", loadImage("icons/network"));
			this.images.put("window_icon_debug", loadImage("icons/debug"));
			this.images.put("window_icon_market", loadImage("icons/market"));
			this.images.put("window_icon_default", loadImage("icons/window"));
			this.images.put("window_icon_map_creator", loadImage("icons/mapcreator"));
			this.images.put("window_icon_multiplayer", loadImage("icons/multiplayer"));
			this.images.put("window_icon_settings", loadImage("icons/settings"));
			this.images.put("backbot_happy", loadImage("icons/backbot_happy"));

			this.images.put("error", loadImage("icons/error"));
			this.images.put("warning", loadImage("icons/warning"));
			this.images.put("success", loadImage("icons/success"));
			
			this.images.put("group_invitation", loadImage("icons/group_invitation"));
			this.images.put("group_deleted", loadImage("icons/group_deleted"));
			this.images.put("group_kick", loadImage("icons/group_kick"));
			this.images.put("friend_invitation", loadImage("icons/friend_invitation"));
			
			this.images.put("icon_script", loadImage("icons/script"));
			this.images.put("icon_server", loadImage("icons/server"));
			this.images.put("icon_node", loadImage("icons/node"));
			this.images.put("icon_profile", loadImage("icons/profile"));
			this.images.put("icon_settings", loadImage("icons/settings"));
			this.images.put("icon_shutdown", loadImage("icons/shutdown"));
			this.images.put("icon_restart", loadImage("icons/restart"));
			this.images.put("icon_friends", loadImage("icons/friends"));
			this.images.put("icon_hosting", loadImage("icons/hosting"));
			this.images.put("icon_messaging", loadImage("icons/messaging"));
			this.images.put("icon_multiplayer", loadImage("icons/multiplayer"));
			this.images.put("icon_multiplayer", loadImage("icons/multiplayer"));
			this.images.put("window_icon_friends", loadImage("icons/friends"));
			this.images.put("window_icon_hosting", loadImage("icons/hosting"));
			this.images.put("window_icon_messaging", loadImage("icons/messaging"));
			
			File datas_directorys = new File(this.datapack_directory, "datas");
			System.out.println("[DatapackLoader] Loading datas...");
			loadData(datas_directorys);

			System.out.println("[DatapackLoader] Loading fonts...");
			File font_directory = new File(datapack_directory, "fonts/");
			loadFonts(font_directory);

			System.out.println("[DatapackLoader] Loading audios...");
			File audios_directory = new File(datapack_directory, "audios/");
			loadAudio(audios_directory);
			
			System.out.println("[DatapackLoader] Datapack successfully loaded.");

			File datapack_object_file = new File(this.datapack_directory, "datapack.json");
			if(!datapack_object_file.exists()) {
				System.err.println("The datapack is not complete, 'datapack.json' is missing.");
				BackdoorGame.crash();
			} else {
				try {
					datapackObject = DatapackObject.fromJson(new FileReader(datapack_object_file));	
					
					ConsoleWriter writer = new ConsoleWriter();
					writer.addTitleContainer(ConsoleWriter.POSITION_CENTER, datapackObject.getName(), "Author: "+datapackObject.getAuthor()+"\nVersion: "+datapackObject.getVersion(), new Vector4i(5, 1,5, 1));
					writer.write();
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("The 'datapack.json' file is incorrect!");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error loading datapack, missing file!", "Fatal error",
					JOptionPane.ERROR_MESSAGE);
			BackdoorGame.crash();
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error loading datapack, probably a missing file!", "Fatal error",
					JOptionPane.ERROR_MESSAGE);
			BackdoorGame.crash();
		}
	}
	
	/**
	 * @return the datapackObject
	 */
	public DatapackObject getDatapackObject() {
		return datapackObject;
	}
	
	public DataParameters getData(String variable) {
		return this.datas.get(variable);
	}
	
	public StbBackedLoadableImage getImage(String variable) {
		return this.images.get(variable);
	}
	
	private void loadData(File file) throws FileNotFoundException {
		if(file == null || !file.exists()) {
			System.err.println("'custom' directory not found!");
			return;
		}
		if(file.isFile()) {
			String key = file.getName().substring(0, file.getName().lastIndexOf(".data"));
			this.datas.put( key, DataParameters.load(file));
			System.out.println("[DatapackLoader] Data loaded, file -> [key="+key+"] "+file.getPath());
		} else if (file.isDirectory()) {
			for(File file_ : file.listFiles()) {
				loadData(file_);
			}	
		}		
	}
	
	private void loadAudio(File file) throws FileNotFoundException {
		if(file == null || !file.exists()) {
			System.err.println(file.getPath()+" directory not found!");
			return;
		}
		if(file.isFile()) {
			String key = file.getName().substring(0, file.getName().lastIndexOf(".ogg"));
			this.audios.put(key, file);
			System.out.println("[DatapackLoader] Audio loaded, file -> [key="+key+"] "+file.getPath());
		} else if (file.isDirectory()) {
			for(File file_ : file.listFiles()) {
				loadAudio(file_);
			}	
		}		
	}
	
	private void loadFonts(File file) throws FileNotFoundException {
		if(file == null || !file.exists()) {
			System.err.println("'custom' directory not found!");
			return;
		}
		if(file.isFile()) {
			String name = file.getName().substring(0, file.getName().lastIndexOf(".ttf"));
			String path = file.getPath();
			System.out.println("[DatapackLoader] Font loaded "+name+"="+path);
			FontRegistry.registerFont(name, path);
		} else if (file.isDirectory()) {
			if(file.exists()) {
				for(File target : file.listFiles()) {	
					loadFonts(target);
				}
			}
		}		
	}
	
	public static StbBackedLoadableImage getDatapackIcon(File datapack_directory) throws IOException, FileNotFoundException {
		File file = new File(datapack_directory, "icon.png");
		if(file.exists()) {
			return new StbBackedLoadableImage(file.getPath());
		}
		throw new FileNotFoundException();
	}

	private StbBackedLoadableImage getIcon() throws IOException, FileNotFoundException {
		File file = new File(datapack_directory, "icon.png");
		if(file.exists()) {
			return new StbBackedLoadableImage(file.getPath());
		}
		throw new FileNotFoundException();
	}

	private StbBackedLoadableImage loadImage(String name) throws IOException, FileNotFoundException {
		File file = new File(datapack_directory, "images/" + name + ".png");
		if(file.exists()) {
			return new StbBackedLoadableImage(file.getPath());
		}
		throw new FileNotFoundException();
	}
	
	private File getFont(String name) throws IOException, FileNotFoundException {
		File file = new File(datapack_directory, "fonts/" + name + ".ttf");
		if(file.exists()) {
			return file;
		}
		throw new FileNotFoundException();
	}
	
	public File getDatapackDirectory() {
		return datapack_directory;
	}
	
	public static DatapackLoader getDefaultDatapack() {
		return DEFAULT;
	}

	public InputStream getAudio(String path) {
		if(!(audios.containsKey(path)))
			return null;
		try {
			return new FileInputStream(audios.get(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}