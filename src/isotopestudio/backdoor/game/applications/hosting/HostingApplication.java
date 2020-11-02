package isotopestudio.backdoor.game.applications.hosting;

import doryanbessiere.isotopestudio.commons.lang.Lang;
import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.components.desktop.Label;
import isotopestudio.backdoor.engine.components.desktop.SelectBox;
import isotopestudio.backdoor.engine.components.desktop.Text;
import isotopestudio.backdoor.engine.components.desktop.TextField;
import isotopestudio.backdoor.engine.components.desktop.window.Window;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.applications.FriendsApplication;

/**
 * @author BESSIERE
 * @github https://www.github.com/DoryanBessiere/
 */
public class HostingApplication extends Window implements IComponent {
	
	public static HostingApplication main;

	public static void showApplication() {
		if (main == null) {
			main = new HostingApplication();
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

	private Text addressText;
	private TextField addressField;

	private Text portText;
	private TextField portField;

	private Text passwordText;
	private TextField passwordField;
	
	private SelectBox<String> gamemodeSelectBox;
	
	private void createTextFieldWithTitle(String title) {
		
	}
	
	public HostingApplication() {
		super("", 0, 0, 400, 350);
	}
	
	@Override
	public void update() {
		super.update();
	}

	@Override
	public void load() {
		getIcon().setImage(BackdoorGame.getDatapack().getImage("window_icon_hosting"));
		super.load();

		getTitle().getTextState().setText(Lang.get("debug_hosting"));
	}
}
