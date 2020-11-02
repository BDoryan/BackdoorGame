package isotopestudio.backdoor.game.applications.settings;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.component.event.button.ButtonWidthChangeEvent;
import org.liquidengine.legui.component.misc.listener.button.UpdateButtonStyleWidthListener;
import org.liquidengine.legui.component.optional.align.HorizontalAlign;
import org.liquidengine.legui.config.Configuration;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.event.MouseClickEvent.MouseClickAction;
import org.liquidengine.legui.listener.MouseClickEventListener;
import org.liquidengine.legui.style.Style.DisplayType;

import doryanbessiere.isotopestudio.commons.lang.Lang;
import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.components.desktop.Button;
import isotopestudio.backdoor.engine.components.desktop.Label;
import isotopestudio.backdoor.engine.components.desktop.SelectBox;
import isotopestudio.backdoor.engine.components.desktop.TextField;
import isotopestudio.backdoor.engine.components.desktop.ToggleButton;
import isotopestudio.backdoor.engine.components.desktop.Tooltip;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.settings.GameSettings;
import isotopestudio.backdoor.game.settings.VideoSettings;

/**
 * @author BESSIERE
 * @github https://www.github.com/DoryanBessiere/
 */
public class DisplaysPanel extends Panel implements IComponent {

	private Label fps_limit = new Label();
	private SelectBox<Integer> fps_limit_selector = new SelectBox<Integer>(); 
	
	private Label fps_limiter = new Label();
	private ToggleButton fps_limiter_togglebutton = new ToggleButton(); 
	
	private Label antialiasing = new Label();
	private ToggleButton antialiasing_togglebutton = new ToggleButton(); 
	
	private Label fullscreen = new Label();
	private ToggleButton fullscreen_togglebutton = new ToggleButton(); 

	private Button save_settings_button = new Button();
	
	private int[] fps_list = new int[] {60, 75, 144, 240, 280, 300};

	public DisplaysPanel(SettingsApplication app) {
		setFocusable(false);
		
		getStyle().setDisplay(DisplayType.FLEX);
		getStyle().getBackground().setColor(0, 0, 0, 0);
		getStyle().setBorderRadius(0f);

		fps_limit.setVariable(app.getComponentVariable() + "_fps_limit");
		fps_limit.getStyle().setTop(10);
		fps_limit.getStyle().setLeft(30);
		fps_limit.getStyle().setFontSize(15f);

		fps_limit_selector.setVariable(app.getComponentVariable() + "_fps_limit_selector");
		fps_limit_selector.setVisibleCount(20);
		fps_limit_selector.getStyle().setTop(30);
		fps_limit_selector.getStyle().setLeft(30);
		fps_limit_selector.getStyle().setWidth(50);
		fps_limit_selector.getStyle().setHeight(30);

		for(int fps_limit : fps_list) {
			fps_limit_selector.addElement(fps_limit);
		}

		VideoSettings settings = VideoSettings.getSettings();
		
		if(!fps_limit_selector.getElements().contains(settings.frames_limit)) {			
			fps_limit_selector.addElement(settings.frames_limit);
		}
		fps_limit_selector.setSelected((Integer)settings.frames_limit, true);

		fps_limiter.setVariable(app.getComponentVariable() + "_fps_limiter");
		fps_limiter.getStyle().setTop(70);
		fps_limiter.getStyle().setLeft(30);
		fps_limiter.getStyle().setFontSize(15f);

		fps_limiter_togglebutton.setVariable(app.getComponentVariable() + "_fps_limiter_togglebutton");
		fps_limiter_togglebutton.getStyle().setTop(90);
		fps_limiter_togglebutton.getStyle().setHeight(20);
		fps_limiter_togglebutton.getStyle().setWidth(80);
		fps_limiter_togglebutton.getStyle().setLeft(30);

		fps_limiter_togglebutton.setToggled(settings.frames_limiter);

		antialiasing.setVariable(app.getComponentVariable() + "_antialiasing");
		antialiasing.getStyle().setTop(120);
		antialiasing.getStyle().setLeft(30);
		antialiasing.getStyle().setFontSize(15f);

		antialiasing_togglebutton.setVariable(app.getComponentVariable() + "_antialiasing_togglebutton");
		antialiasing_togglebutton.getStyle().setTop(140);
		antialiasing_togglebutton.getStyle().setHeight(20);
		antialiasing_togglebutton.getStyle().setWidth(80);
		antialiasing_togglebutton.getStyle().setLeft(30);
		antialiasing_togglebutton.setToggled(settings.antialiasing);

		fullscreen.setVariable(app.getComponentVariable() + "_fullscreen");
		fullscreen.getStyle().setTop(165);
		fullscreen.getStyle().setLeft(30);
		fullscreen.getStyle().setFontSize(15f);

		fullscreen_togglebutton.setVariable(app.getComponentVariable() + "_fullscreen_togglebutton");
		fullscreen_togglebutton.getStyle().setTop(185);
		fullscreen_togglebutton.getStyle().setHeight(20);
		fullscreen_togglebutton.getStyle().setWidth(80);
		fullscreen_togglebutton.getStyle().setLeft(30);
		fullscreen_togglebutton.setToggled(settings.fullscreen);

		save_settings_button.setVariable(getComponentVariable() + "_save_settings_button");
		save_settings_button.getStyle().setFontSize(16f);
		save_settings_button.getStyle().setBottom(10f);
		save_settings_button.getStyle().setRight(10f);
		save_settings_button.getStyle().setHeight(40f);
		save_settings_button.getStyle().setShadow(null);
		save_settings_button.getListenerMap().addListener(ButtonWidthChangeEvent.class, new UpdateButtonStyleWidthListener() {
			@Override
			public void process(ButtonWidthChangeEvent event) {
				super.process(event);
				save_settings_button.getStyle().setWidth(event.getWidth()+20);
			}
		});
		save_settings_button.getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
			@Override
			public void process(MouseClickEvent event) {
				if(event.getAction() != MouseClickAction.RELEASE)return;

				VideoSettings settings = VideoSettings.getSettings();
				settings.frames_limit = fps_limit_selector.getSelection(); 
				settings.frames_limiter = fps_limiter_togglebutton.isToggled();
				settings.antialiasing = antialiasing_togglebutton.isToggled();
				settings.save();
				
				if(settings.fullscreen != fullscreen_togglebutton.isToggled()) {
					BackdoorGame.getGameWindow().toggleFullscreen();
				}
				saveMessage();
			}
		});
		
		add(save_settings_button);

		add(fps_limit);
		add(fps_limit_selector);
		add(fps_limiter);
		add(fps_limiter_togglebutton);
		add(antialiasing);
		add(antialiasing_togglebutton);
		add(fullscreen);
		add(fullscreen_togglebutton);
		
		load();
	}
	
	private void saveMessage() {
		Label save_message = new Label(Lang.get("settings_window_settings_saved"));
		save_message.load();

		save_message.getStyle().setLeft(10f);
		save_message.getStyle().setBottom(10f);

		add(save_message);
		
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				remove(save_message);
			}
		}, 200);
	}

	@Override
	public void update() {
	}

	@Override
	public void load() {
		fps_limit.load();
		fps_limit.getTextState().setText(Lang.get("settings_display_fps_limit"));
		/*fps_limit.setTooltip(new Tooltip(Lang.get("settings_display_fps_limit_tooltip")));
		((Tooltip)fps_limit.getTooltip()).load();*/

		fps_limiter.load();
		fps_limiter.getTextState().setText(Lang.get("settings_display_fps_limiter"));
		
		antialiasing.load();
		antialiasing.getTextState().setText(Lang.get("settings_display_antialiasing"));
		
		fullscreen.load();
		fullscreen.getTextState().setText(Lang.get("settings_display_fullscreen"));

		save_settings_button.load();
		save_settings_button.getTextState().setText(Lang.get("settings_window_save_settings"));

		fps_limiter_togglebutton.load();
		antialiasing_togglebutton.load();
		fullscreen_togglebutton.load();
		
		fps_limit_selector.load();
	}

	@Override
	public String getComponentName() {
		return null;
	}

	@Override
	public String getComponentVariable() {
		return "displays_settings";
	}
}
