package isotopestudio.backdoor.game.applications;

import java.util.ArrayList;

import org.joml.Vector4f;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.style.Style.DisplayType;
import org.liquidengine.legui.style.border.SimpleLineBorder;

import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.components.desktop.window.Window;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.game.component.GameElementComponent;

public class MatchmakingApplication extends Window implements IComponent {
	
	public static MatchmakingApplication main;

	public static void showApplication() {
		if(main == null) {
			main = new MatchmakingApplication();
			BackdoorGame.getDesktop().addWindow(main);
			main.centerLocation();
			main.load();
		} else {
			if (BackdoorGame.getDesktop().containsWindow(main)) {
				main.load();
				main.showWindow();	
			} else {
				main = null;
				showApplication();
			}
		}
	}

	public MatchmakingApplication() {
		super("Multiplayer", 0, 0, 800, 400);

		getContainer().getStyle().setDisplay(DisplayType.FLEX);
		
		load();
	}

	@Override
	public void load() {
		getIcon().setImage(BackdoorGame.getDatapack().getImage("window_icon_multiplayer"));
		super.load();
	}
}
