package isotopestudio.backdoor.addons;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.jar.JarFile;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class AddonLoader {

	private File[] files;
	private HashMap<File, BackdoorAddon> plugins = new HashMap<>();

	public AddonLoader(File[] files) {
		super();
		this.files = files;
	}

	public void startAddons() {
		for (BackdoorAddon plugin : plugins.values()) {
			plugin.start();
			System.out.println(plugin.getPluginData().getName() + " is started.");
		}
	}

	public void stopAddons() {
		for (BackdoorAddon plugin : plugins.values()) {
			plugin.stop();
			System.out.println(plugin.getPluginData().getName() + " is stopped.");
		}
	}

	public void loadAddons() throws MalformedURLException {
		if (this.files.length == 0) {
			System.out.println("[PluginLoader] No plugin was found.");
			return;
		}

		System.out.println("[PluginLoader] " + files.length + " plugins have been detected.");

		URLClassLoader loader;
		String tmp = "";
		Enumeration enumeration;
		Class tmpClass = null;

		for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {
			File file = files[fileIndex];

			if (!file.exists()) {
				System.err.println("[PluginLoader] " + file.getPath() + " this file does not exist!");
				return;
			}

			URL u = file.toURL();
			loader = new URLClassLoader(new URL[] { u });

			try {
				JarFile jar = new JarFile(file.getAbsolutePath());
				enumeration = jar.entries();

				while (enumeration.hasMoreElements()) {

					tmp = enumeration.nextElement().toString();

					if (tmp.endsWith(".class")) {
						tmp = tmp.substring(0, tmp.length() - 6);
						tmp = tmp.replaceAll("/", ".");

						tmpClass = Class.forName(tmp, true, loader);

						for (int i = 0; i < tmpClass.getInterfaces().length; i++) {
							if (tmpClass.getInterfaces()[i].getName().toString()
									.equals("isotopestudio.backdoor.plugins.BackdoorPlugin")) {
								try {
									BackdoorAddon plugin = (BackdoorAddon) ((Class) tmpClass).newInstance();
									plugins.put(file, plugin);
									plugin.load();
									System.out.println(plugin.getPluginData().getName() + " is loaded.");
								} catch (InstantiationException e) {
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("[PluginLoader] Unable to load the jar file (file=" + file.getPath() + ")");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				System.err.println(
						"[PluginLoader] Impossible to find the class of the plugin (file=" + file.getPath() + ")");
			}
		}
	}

	/**
	 * @return the files
	 */
	public File[] getFiles() {
		return files;
	}

	/**
	 * @return the plugins
	 */
	public HashMap<File, BackdoorAddon> getPlugins() {
		return plugins;
	}
}
