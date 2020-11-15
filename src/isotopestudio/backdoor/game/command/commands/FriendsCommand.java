package isotopestudio.backdoor.game.command.commands;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;

import doryanbessiere.isotopestudio.api.friends.FriendsAPI;
import doryanbessiere.isotopestudio.api.profile.Profile;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.command.ICommand;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class FriendsCommand implements ICommand {

	@Override
	public void handle(String[] args) {
		if (args.length == 0) {
			System.out.println("");
			System.out.println("Description > " + getDescription());
			System.out.println("");
			System.out.println("Commands:");
			System.out.println("- " + getCommand() + " search <username> | For search a user");
			System.out.println("- " + getCommand() + " sent <uuid> | For sent a friend request");
			System.out.println("- " + getCommand() + " accept <uuid> | For accept a friend request");
			System.out.println("- " + getCommand() + " remove <uuid> | For remove a friend");
			System.out.println("- " + getCommand() + " request | Friends request list");
			System.out.println("- " + getCommand() + " friends | Friends list");
			return;
		} else {
			if (args.length == 1) {
				String arg1 = args[0];

				if (arg1.equalsIgnoreCase("request")) {

					return;
				} else if (arg1.equalsIgnoreCase("list")) {
					System.out.println("");
					System.out.println("Your friends list");
					try {
						ArrayList<Profile> friends = new FriendsAPI().getFriends(BackdoorGame.getUser().getEmail(), BackdoorGame.getUser().getToken());
						if(friends.size() <= 0) {
							System.out.println("  *You don't have a friend*");
						} else {
							for(Profile friend : friends) {
								System.out.println("  - "+friend.getUsername()+" [uuid="+friend.getUuidString()+"]");
							}
						}
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println("");
					return;
				}
			} else if (args.length == 2) {
				String arg1 = args[0];
				String arg2 = args[2];

				if (arg1.equalsIgnoreCase("search")) {
					return;
				} else if (arg1.equalsIgnoreCase("sent")) {
					return;
				} else if (arg1.equalsIgnoreCase("accept")) {
					return;
				} else if (arg1.equalsIgnoreCase("remove")) {
					return;
				}
			}
		}
		System.err.println("Command invalid, type '" + getCommand() + "' for help!");
	}

	@Override
	public String getCommand() {
		return "friends";
	}

	@Override
	public String getDescription() {
		return "Allows you to manage your friends.";
	}
}
