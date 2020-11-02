package isotopestudio.backdoor.game.command.commands;

import org.bytedeco.librealsense.intrinsics;

import isotopestudio.backdoor.game.command.ICommand;
import isotopestudio.backdoor.game.server.ServerMaker;

/**
 * @author BESSIERE
 * @github https://www.github.com/DoryanBessiere/
 */
public class GameServerCommand implements ICommand {
	
	public static ServerMaker server; 

	@Override
	public void handle(String[] args) {
		if(args.length == 0) {
			System.out.println("");
			System.out.println("Description > "+getDescription());
			System.out.println("");
			System.out.println("Commands:");
			System.out.println(" > gameserver <start> <port:set 'null' if you want use the default port> <password:set 'null' if is public server> | For start the game server (Party)");
			System.out.println(" > gameserver <stop/close> | For close the game server");
		} else if (args.length == 3) {
			if(args[0].equalsIgnoreCase("start")) {
				int port = 66;
				try {
					String portString = args[1];
					if(!portString.equals("null")) 
						port = Integer.valueOf(portString);
				} catch (Exception e) {
				}
				
				String password = args[2];
				if(password.equals("null"))
					password = null;
				
				server = new ServerMaker(port, password);
				server.start();
			} else if (args[0].equalsIgnoreCase("stop") || args[0].equalsIgnoreCase("close")) {
				if(server != null)
				{
					server.destroy();
					System.out.println("Server process killed!");
				}
			}
		} else {
			
		}
	}

	@Override
	public String getCommand() {
		return "gameserver";
	}

	@Override
	public String getDescription() {
		return "Allows you to manage your game server";
	}
}
