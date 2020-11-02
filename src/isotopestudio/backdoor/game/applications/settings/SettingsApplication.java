package isotopestudio.backdoor.game.applications.settings;

import org.joml.Vector4f;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.event.MouseClickEvent.MouseClickAction;
import org.liquidengine.legui.listener.MouseClickEventListener;
import org.liquidengine.legui.style.Style.DisplayType;

import doryanbessiere.isotopestudio.commons.lang.Lang;
import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.components.desktop.Button;
import isotopestudio.backdoor.engine.components.desktop.window.Window;
import isotopestudio.backdoor.engine.datapack.DataParameters;
import isotopestudio.backdoor.game.BackdoorGame;

public class SettingsApplication extends Window implements IComponent {

	public static SettingsApplication main;

	public static void showApplication() {
		if(main == null) {
			main = new SettingsApplication();
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

	private Panel buttons_panel = new Panel();
	private Panel content_panel = new Panel();

	private Button generals_button = new Button();
	private Button themes_button = new Button();
	private Button displays_button = new Button();
	private Button audios_button = new Button();
	private Button informations_button = new Button();

	public SettingsApplication() {
		super(Lang.get("settings_window_title"), 0, 0, 800, 500);
		setVariable("settings_window");

		buttons_panel.getStyle().setDisplay(DisplayType.FLEX);
		content_panel.getStyle().setDisplay(DisplayType.FLEX);
		getContainer().getStyle().setDisplay(DisplayType.FLEX);
		
		buttons_panel.getStyle().setTop(0F);
		buttons_panel.getStyle().setBottom(0f);
		buttons_panel.getStyle().setWidth(250f);
		buttons_panel.getStyle().setBorder(null);
		buttons_panel.getStyle().setBorderRadius(0f);

		buttons_panel.getStyle().setTop(0F);
		buttons_panel.getStyle().setBottom(0F);
		buttons_panel.getStyle().setLeft(0f);
		buttons_panel.getStyle().setRight(0f);
		buttons_panel.getStyle().setBorder(null);
		buttons_panel.getStyle().setBorderRadius(0f);
		buttons_panel.setFocusable(false);
		
		getContainer().add(buttons_panel);
		
		generals_button.setVariable(getComponentVariable() + "_generals_button");
		generals_button.getStyle().setFontSize(16f);
		generals_button.getStyle().setTop(10f);
		generals_button.getStyle().setLeft(10f);
		generals_button.getStyle().setRight(10f);
		generals_button.getStyle().setHeight(40f);
		generals_button.getStyle().setShadow(null);
		generals_button.getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
			@Override
			public void process(MouseClickEvent event) {
				if(event.getAction() != MouseClickAction.RELEASE)return;

				loadPanel(new GeneralsPanel(SettingsApplication.this));
			}
		});
		
		themes_button.setVariable(getComponentVariable() + "_themes_button");
		themes_button.getStyle().setFontSize(16f);
		themes_button.getStyle().setTop(60f);
		themes_button.getStyle().setLeft(10f);
		themes_button.getStyle().setRight(10f);
		themes_button.getStyle().setHeight(40f);
		themes_button.getStyle().setShadow(null);
		themes_button.getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
			@Override
			public void process(MouseClickEvent event) {
				if(event.getAction() != MouseClickAction.RELEASE)return;

				loadPanel(new ThemesPanel(SettingsApplication.this));				
			}
		});
		
		informations_button.setVariable(getComponentVariable() + "_informations_button");
		informations_button.getStyle().setFontSize(16f);
		informations_button.getStyle().setBottom(10f);
		informations_button.getStyle().setLeft(10f);
		informations_button.getStyle().setRight(10f);
		informations_button.getStyle().setHeight(40f);
		informations_button.getStyle().setShadow(null);
		informations_button.getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
			@Override
			public void process(MouseClickEvent event) {
				if(event.getAction() != MouseClickAction.RELEASE)return;

				loadPanel(new InformationsPanel());
			}
		});
		
		displays_button.setVariable(getComponentVariable() + "_displays_button");
		displays_button.getStyle().setFontSize(16f);
		displays_button.getStyle().setTop(110f);
		displays_button.getStyle().setLeft(10f);
		displays_button.getStyle().setRight(10f);
		displays_button.getStyle().setHeight(40f);
		displays_button.getStyle().setShadow(null);
		displays_button.getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
			@Override
			public void process(MouseClickEvent event) {
				if(event.getAction() != MouseClickAction.RELEASE)return;

				loadPanel(new DisplaysPanel(SettingsApplication.this));
			}
		});
		
		audios_button.setVariable(getComponentVariable() + "_audios_button");
		audios_button.getStyle().setFontSize(16f);
		audios_button.getStyle().setTop(160f);
		audios_button.getStyle().setLeft(10f);
		audios_button.getStyle().setRight(10f);
		audios_button.getStyle().setHeight(40f);
		audios_button.getStyle().setShadow(null);

		buttons_panel.add(generals_button);
		buttons_panel.add(themes_button);
		buttons_panel.add(displays_button);
		buttons_panel.add(audios_button);
		buttons_panel.add(informations_button);

		content_panel.getStyle().setTop(0f);
		content_panel.getStyle().setRight(0f);
		content_panel.getStyle().setLeft((float)buttons_panel.getStyle().getWidth().get());
		content_panel.getStyle().setBottom(0f);
		content_panel.getStyle().getBackground().setColor(0,0,0,0);
		content_panel.getStyle().setBorder(null);
		content_panel.getStyle().setBorderRadius(0f);
		content_panel.setFocusable(false);
  
		getContainer().add(content_panel);
		
		load();
	}
	
	@Override
	public void update() {
		super.update();
	}
	
	private Panel current_panel;
	
	public void loadPanel(Panel panel) {
		this.content_panel.clearChildComponents();

		panel.getStyle().setTop(0f);
		panel.getStyle().setRight(0f);
		panel.getStyle().setLeft(0f);
		panel.getStyle().setBottom(0f);
		
		this.content_panel.add(panel);
	}
	
	/**
	 * @return the content_panel
	 */
	public Panel getContentPanel() {
		return content_panel;
	}

	@Override
	public void load() {
		getIcon().setImage(BackdoorGame.getDatapack().getImage("window_icon_settings"));

		generals_button.getTextState().setText(Lang.get("settings_window_generals"));
		themes_button.getTextState().setText(Lang.get("settings_window_themes"));
		displays_button.getTextState().setText(Lang.get("settings_window_displays"));
		audios_button.getTextState().setText(Lang.get("settings_window_audios"));
		informations_button.getTextState().setText(Lang.get("settings_window_informations"));

		generals_button.load();
		themes_button.load();
		displays_button.load();
		audios_button.load();
		informations_button.load();

		float[] color = DataParameters.getColor(this, "buttons_panel_background_color");
		buttons_panel.getStyle().getBackground().setColor(new Vector4f(color[0], color[1], color[2], color[3]));
		buttons_panel.getHoveredStyle().getBackground().setColor(new Vector4f(color[0], color[1], color[2], color[3]));
		buttons_panel.getFocusedStyle().getBackground().setColor(new Vector4f(color[0], color[1], color[2], color[3]));
		buttons_panel.getPressedStyle().getBackground().setColor(new Vector4f(color[0], color[1], color[2], color[3]));
		
		super.load();
	}
}
