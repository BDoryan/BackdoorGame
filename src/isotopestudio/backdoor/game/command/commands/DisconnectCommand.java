package isotopestudio.backdoor.game.command.commands;

import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.command.ICommand;
import isotopestudio.backdoor.network.packet.packets.PacketPlayerDisconnect;

public class DisconnectCommand implements ICommand {

	@Override
	public void handle(String[] args) {
		if(BackdoorGame.game_client != null && BackdoorGame.game_client.isConnected()) {
			BackdoorGame.game_client.disconnection("logout");
			BackdoorGame.game_client = null;
			BackdoorGame.getGameParty().stop();
			BackdoorGame.game_party = null;
			
			System.err.println("You have logout the server");
		} else {
			System.err.println("You are not connected to a server!");
		}
	}

	@Override
	public String getCommand() {
		return "disconnect";
	}

	@Override
	public String getDescription() {
		return "Allows you to disconnect from the server";
	}
}
