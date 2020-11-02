package isotopestudio.backdoor.game.command.commands;

import org.lwjgl.Version;
import org.lwjgl.opengl.GL11;

import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.command.ICommand;
import isotopestudio.backdoor.game.settings.GameSettings;
import isotopestudio.backdoor.game.settings.VideoSettings;

public class InfoCommand implements ICommand {

	@Override
	public String getCommand() {
		return "info";
	}
	
	@Override
	public String getDescription() {
		return "Gives you information about the game";
	}
	
	@Override
	public void handle(String[] args) {
		System.out.println("  ");
		System.out.println("Backdoor Game, information:");
		System.out.println("  Game directory: "+BackdoorGame.localDirectory().getPath());
		System.out.println("  ");
		System.out.println("  Game Settings:");
		System.out.println("   - Datapack: "+GameSettings.getSettings().datapack);
		System.out.println("   - Frame limiter: "+VideoSettings.getSettings().frames_limiter);
		System.out.println("   - Frames Caps: "+VideoSettings.getSettings().frames_limit);
		System.out.println("   - Langage: "+GameSettings.getSettings().lang);
		System.out.println("  ");
		System.out.println("  Version:");
		System.out.println("   - Game: "+BackdoorGame.GAME_VERSION);
		System.out.println("   - Legui: "+BackdoorGame.LEGUI_VERSION);
		System.out.println("   - JOML: "+BackdoorGame.JOML_VERSION);
	    System.out.println("   - LWJGL " + Version.getVersion());
	    System.out.println("   - OpenGL " + GL11.glGetString(GL11.GL_VERSION));
		System.out.println("  ");
	}
}
