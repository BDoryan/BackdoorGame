package isotopestudio.backdoor.game.applications;

import java.util.ArrayList;

import org.joml.Vector4f;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.style.Style.DisplayType;
import org.liquidengine.legui.style.border.SimpleLineBorder;

import isotopestudio.backdoor.core.elements.GameElement;
import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.components.desktop.window.Window;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.game.component.GameElementComponent;

public class MapCreatorApplication extends Window implements IComponent {
	
	public static MapCreatorApplication main;
	
	public static void showApplication() {
		if(main == null) {
			main = new MapCreatorApplication();
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

	public MapCreatorApplication() {
		super("Map Creator", 0, 0, 800, 400);

		getContainer().getStyle().setDisplay(DisplayType.FLEX);
		
		initComponentsPane();
		
		load();
	}
	
	private void initComponentsPane() {
		Panel components_pane = new Panel();
		components_pane.getStyle().setBorder(new SimpleLineBorder(new Vector4f(1,1,1,1), 1));
		
		components_pane.getStyle().setHeight(100);
		components_pane.getStyle().setRight(0);
		components_pane.getStyle().setBottom(0);
		components_pane.getStyle().setLeft(0);
		
		ArrayList<GameElementComponent> components = new ArrayList<GameElementComponent>();
		
		getContainer().add(components_pane);
	}

	@Override
	public void load() {
		getIcon().setImage(BackdoorGame.getDatapack().getImage("window_icon_map_creator"));
		super.load();
	}
}
