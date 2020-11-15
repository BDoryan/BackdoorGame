package isotopestudio.backdoor.game.command.commands;

import java.util.Collection;

import isotopestudio.backdoor.addons.AddonData;
import isotopestudio.backdoor.addons.BackdoorAddon;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.command.ICommand;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class PluginsCommand implements ICommand {

	@Override
	public void handle(String[] args) {
		if(args.length == 0) {
			System.out.println("");
			System.out.println("Description > " + getDescription());
			System.out.println("");
			System.out.println("Commands:");
			System.out.println("- " + getCommand() + " list");
		} else if (args.length == 1) {
			if(args[0].equalsIgnoreCase("list")) {
				Collection<BackdoorAddon> plugins = BackdoorGame.getPluginLoader().getPlugins().values();
				System.out.println("Plugins ("+plugins.size()+"):");
				for(BackdoorAddon plugin : plugins) {
					AddonData addonData = plugin.getPluginData();
					StringBuilder authors = new StringBuilder();
					if(addonData.getAuthor().length == 1) {
						authors.append(addonData.getAuthor()[0]);
					} else if (addonData.getAuthor().length > 1){
						for(int i = 0; i < addonData.getAuthor().length; i++) {
							String author = addonData.getAuthor()[i];
							if(i == 0) {
								authors.append(author);	
							} else {
								authors.append(", "+author);
							}
						}
					}
					
					System.out.println("  - "+addonData.getName());
					System.out.println("    Authors: "+authors.toString());
					System.out.println("    Description: "+addonData.getDescription());
					System.out.println("    Version: "+addonData.getVersion());
				}
			}
		}
	}

	@Override
	public String getCommand() {
		return "plugins";
	}

	@Override
	public String getDescription() {
		return "Allows you to manage plugins";
	}
}
