package isotopestudio.backdoor.game.command.commands;

import isotopestudio.backdoor.core.matchmaking.MatchmakingClient;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.command.ICommand;

public class MatchmakingCommand implements ICommand {
	
	@Override
	public void handle(String[] args) {
		new Thread(new Runnable() {
			private boolean already_in_matchmaking = false;
			
			@Override
			public void run() {
				/*
				if(already_in_matchmaking)return;
				already_in_matchmaking = true;
				System.out.println("Waiting other players...");
				String result = MatchmakingClient.connect(BackdoorGame.getUser().getEmail(), BackdoorGame.getUser().getToken(), BackdoorGame.GAME_VERSION);
				System.out.println("Matchmaking > "+result);
				if(result.startsWith("connection")) {
					String address = result.split("=")[1];
					int port = Integer.valueOf(address.split(":")[1]);
					address = address.split(":")[0];
					
					BackdoorGame.connect(address, port);
				}
				already_in_matchmaking = false;*/
			}
		}).start();
	}

	@Override
	public String getCommand() {
		return "matchmaking";
	}

	@Override
	public String getDescription() {
		return "Allows you to join the matchmaking queue";
	}
}
