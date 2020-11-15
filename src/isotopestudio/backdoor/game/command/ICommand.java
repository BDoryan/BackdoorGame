package isotopestudio.backdoor.game.command;

import java.util.Scanner;

import isotopestudio.backdoor.game.command.commands.ChangelogsCommand;
import isotopestudio.backdoor.game.command.commands.ConnectCommand;
import isotopestudio.backdoor.game.command.commands.CreditCommand;
import isotopestudio.backdoor.game.command.commands.DebugCommand;
import isotopestudio.backdoor.game.command.commands.DisconnectCommand;
import isotopestudio.backdoor.game.command.commands.ExitCommand;
import isotopestudio.backdoor.game.command.commands.FriendsCommand;
import isotopestudio.backdoor.game.command.commands.GameServerCommand;
import isotopestudio.backdoor.game.command.commands.HelpCommand;
import isotopestudio.backdoor.game.command.commands.InfoCommand;
import isotopestudio.backdoor.game.command.commands.KeyboardCommand;
import isotopestudio.backdoor.game.command.commands.ListenPacketsCommand;
import isotopestudio.backdoor.game.command.commands.MapCreatorCommand;
import isotopestudio.backdoor.game.command.commands.MatchmakingCommand;
import isotopestudio.backdoor.game.command.commands.PluginsCommand;
import isotopestudio.backdoor.game.command.commands.RunIntroCommand;
import isotopestudio.backdoor.game.command.commands.SettingsCommand;

public interface ICommand {

	public static ICommand[] commands = new ICommand[] { 
			new ExitCommand(), 
			new InfoCommand(), 
			new DebugCommand(),
			new HelpCommand(), 
			new ChangelogsCommand(),
			new ConnectCommand(),
			new DisconnectCommand(),
			new CreditCommand(),
			new MapCreatorCommand(),
			new MatchmakingCommand(),
			new KeyboardCommand(),
			new SettingsCommand(),
			new ListenPacketsCommand(),
			new FriendsCommand(),
			new GameServerCommand(),
			new RunIntroCommand(), 
			new PluginsCommand()
	};

	public abstract void handle(String[] args);

	public abstract String getCommand();

	public abstract String getDescription();

	public static void command(String line) {
		String[] args = line.split(" ");
		String target = args[0];

		System.out.println(line);

		for (ICommand command : commands) {
			if (command.getCommand().equalsIgnoreCase(target)) {
				String[] arguments = new String[args.length - 1];
				if (args.length > 1) {
					for (int i = 1; i < args.length; i++) {
						arguments[i - 1] = args[i];
					}
				}
				command.handle(arguments);
				return;
			}
		}
		System.err.println("This command is unknown");
	}

	public static Thread listenJavaConsole() {
		return new Thread(new Runnable() {
			@Override
			public void run() {
				Scanner java_console_scanner = new Scanner(System.in);
				String line = null;
				while ((line = java_console_scanner.nextLine()) != null) {
					command(line);
				}
				java_console_scanner.close();
			}
		});
	}
}
