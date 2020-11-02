package isotopestudio.backdoor.game.command.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import doryanbessiere.isotopestudio.commons.changelogs.ChangeLogsObject;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.command.ICommand;

public class ChangelogsCommand implements ICommand {

	@Override
	public void handle(String[] args) {
		System.out.println("");
		File changelogs_file = new File(BackdoorGame.localDirectory(), "changelogs.log");
		if (changelogs_file.exists()) {
			try {
				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
				ChangeLogsObject changelogs = gson.fromJson(new FileReader(changelogs_file), ChangeLogsObject.class);
				System.out.println(changelogs.getTitle());
				System.out.println("  *"+changelogs.getSubtitle());
				System.out.println("");
				System.out.println(""+BackdoorGame.GAME_VERSION);
				System.out.println(""+new SimpleDateFormat("dd/MM/yyyy HH:mm").format(changelogs.getDate()));
				System.out.println("");
				for(Entry<String, ArrayList<String>> content : changelogs.getContents().entrySet()) {
					System.out.println("  "+content.getKey()+":");
					for(String logs : content.getValue()) {
						System.out.println("    - "+logs);
					}
					System.out.println("");
				}
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
			} catch (JsonIOException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			System.err.println(changelogs_file.getParent() + " not found");
		}
		System.out.println(" ");
	}

	@Override
	public String getCommand() {
		return "changelogs";
	}

	@Override
	public String getDescription() {
		return "You can find here all the new features of the update";
	}
}
