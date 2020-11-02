package isotopestudio.backdoor.game.command.commands;

import java.io.IOException;
import java.net.UnknownHostException;

import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.command.ICommand;
import isotopestudio.backdoor.network.client.GameClient;

public class ConnectCommand implements ICommand {

	@Override
	public void handle(String[] args) {
		if(args.length == 1) {
			if(BackdoorGame.game_client != null && BackdoorGame.game_client.isConnected()) {
				System.err.println("You already have a customer logged in!");
				return;
			}
			if(BackdoorGame.user == null) {
				System.err.println("You are not authentified");
				return;
			}
			String address = args[0];
			int port = 66;
			if(address.contains(":")) {
				port = Integer.valueOf(address.split(":")[1]);
				address = address.split(":")[0];	
			}
			
			BackdoorGame.connect(address, port, null);
		} else 
			if(args.length == 2) {
				if(BackdoorGame.game_client != null && BackdoorGame.game_client.isConnected()) {
					System.err.println("You already have a customer logged in!");
					return;
				}
				if(BackdoorGame.user == null) {
					System.err.println("You are not authentified");
					return;
				}
				String address = args[0];
				int port = 66;
				if(address.contains(":")) {
					port = Integer.valueOf(address.split(":")[1]);
					address = address.split(":")[0];	
				}
				
				String password = args[1];
				
				BackdoorGame.connect(address, port, password);
		} else {
			System.out.println("Error, try: connect <adress> <username> <password:if is a private server>");
		}
	}
	
	@Override
	public String getCommand() {
		return "connect";
	}

	@Override
	public String getDescription() {
		return "Allows you to connect to a game server";
	}
}
