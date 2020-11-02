package isotopestudio.backdoor.game.command.commands;

import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.command.ICommand;
import isotopestudio.backdoor.network.packet.Packet;
import isotopestudio.backdoor.network.packet.PacketListener;

/**
 * @author BESSIERE Doryan
 * @github https://www.github.com/DoryanBessiere/
 */
public class ListenPacketsCommand implements ICommand {

	private static boolean status = false;
	private static PacketListener packetListener = new PacketListener() {
		@Override
		public void sended(Packet packet) {
			System.out.println("[Packet Listening] sended -> "+packet.getClass().getSimpleName());
		}
		
		@Override
		public void received(Packet packet) {
			System.out.println("[Packet Listening] received -> "+packet.getClass().getSimpleName());
		}
	};
	
	@Override
	public void handle(String[] args) {
		if(status && packetListener != null) {
			System.out.println("Listening to the packets is now disabled!");
			if(BackdoorGame.getGameClient() == null) {
				System.err.println("You are not connected on a game server!");
				return;
			}
			BackdoorGame.getGameClient().getPacketListeners().remove(packetListener);
		} else {
			if(BackdoorGame.getGameClient() == null) {
				System.err.println("You are not connected on a game server!");
				return;
			}
			BackdoorGame.getGameClient().getPacketListeners().add(packetListener);
			System.out.println("Listening to the packets is now activated!");
		}
	}

	@Override
	public String getCommand() {
		return "listenpackets";
	}

	@Override
	public String getDescription() {
		return "Allows you to listen the packets way";
	}
}
