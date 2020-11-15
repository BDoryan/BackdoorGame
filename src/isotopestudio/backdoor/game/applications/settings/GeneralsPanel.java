package isotopestudio.backdoor.game.applications.settings;

import java.util.Timer;
import java.util.TimerTask;

import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.component.event.button.ButtonWidthChangeEvent;
import org.liquidengine.legui.component.misc.listener.button.UpdateButtonStyleWidthListener;
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
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.settings.GameSettings;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class GeneralsPanel extends Panel implements IComponent {

	private Label keyboard_selection_label = new Label();
	private SelectBox<Object> keyboards = new SelectBox<>();

	private Label lang_selection_label = new Label();
	private SelectBox<Object> langs = new SelectBox<>();

	private Label refresh_rate_label = new Label();
	private SelectBox<Integer> refresh_rates = new SelectBox<>();

	private Button save_settings_button = new Button();

	public GeneralsPanel(SettingsApplication app) {
		setFocusable(false);
		
		getStyle().setDisplay(DisplayType.FLEX);
		getStyle().getBackground().setColor(0, 0, 0, 0);
		getStyle().setBorderRadius(0f);

		keyboard_selection_label.setVariable(app.getComponentVariable() + "_keyboard_selection_label");
		keyboard_selection_label.getStyle().setTop(10);
		keyboard_selection_label.getStyle().setLeft(30);
		keyboard_selection_label.getStyle().setFontSize(15f);

		keyboards.setVariable(app.getComponentVariable() + "_keyboards_selectbox");
		keyboards.getStyle().setTop(30);
		keyboards.getStyle().setLeft(30);
		keyboards.getStyle().setWidth(200);
		keyboards.getStyle().setHeight(30);

		lang_selection_label.setVariable(app.getComponentVariable() + "_lang_selection_label");
		lang_selection_label.getStyle().setTop(70);
		lang_selection_label.getStyle().setLeft(30);
		lang_selection_label.getStyle().setFontSize(15f);

		langs.setVariable(app.getComponentVariable() + "_langs_selectbox");
		langs.getStyle().setTop(90);
		langs.getStyle().setLeft(30);
		langs.getStyle().setWidth(200);
		langs.getStyle().setHeight(30);

		refresh_rate_label.setVariable(app.getComponentVariable() + "_refresh_rate_label");
		refresh_rate_label.getStyle().setTop(130);
		refresh_rate_label.getStyle().setLeft(30);
		refresh_rate_label.getStyle().setFontSize(15f);
		
		refresh_rates.setVariable(app.getComponentVariable() + "_refresh_rates");
		refresh_rates.getStyle().setTop(150);
		refresh_rates.getStyle().setLeft(30);
		refresh_rates.getStyle().setWidth(200);
		refresh_rates.getStyle().setHeight(30);
		
		for(String lang : BackdoorGame.getLangs()) {
			langs.addElement(lang);
		}
		
		for(String keyboard : Configuration.getInstance().getKeyboardLayouts().keySet()) {
			keyboards.addElement(keyboard);
		}

		refresh_rates.addElement(15);
		refresh_rates.addElement(30);
		refresh_rates.addElement(60);
		refresh_rates.addElement(100);
		refresh_rates.addElement(150);
		refresh_rates.addElement(200);
		for(int i = 250; i <= 10000; i+=250) {
			refresh_rates.addElement(i);
		}
		
		refresh_rates.setVisibleCount(15);

		keyboards.setSelected(GameSettings.getSettings().keyboard_layout, true);
		langs.setSelected(GameSettings.getSettings().lang, true);
		
		if(!refresh_rates.getElements().contains(GameSettings.getSettings().refresh_rate)) {
			refresh_rates.addElement(GameSettings.getSettings().refresh_rate);
		}
		refresh_rates.setSelected((Integer)GameSettings.getSettings().refresh_rate, true);

		add(keyboard_selection_label);
		add(keyboards);

		add(lang_selection_label);
		add(langs);

		add(refresh_rate_label);
		add(refresh_rates);
		
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
				
				GameSettings gameSettings = GameSettings.getSettings();
				gameSettings.keyboard_layout = (String) keyboards.getSelection();
				if(!gameSettings.lang.equals((String)keyboards.getSelection())) {
					gameSettings.lang = (String) langs.getSelection();
					
					BackdoorGame.loadLang((String)langs.getSelection());
				}
				gameSettings.refresh_rate = refresh_rates.getSelection();
				gameSettings.save();
				saveMessage();
			}
		});
		
		add(save_settings_button);
		
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
		keyboard_selection_label.getTextState().setText(Lang.get("window_settings_generals_keyboard_selection"));
		lang_selection_label.getTextState().setText(Lang.get("window_settings_generals_lang_selection"));
		refresh_rate_label.getTextState().setText(Lang.get("window_settings_generals_refresh_rate_selection"));

		save_settings_button.getTextState().setText(Lang.get("settings_window_save_settings"));
		save_settings_button.load();

		keyboard_selection_label.load();
		keyboards.load();

		refresh_rate_label.load();
		refresh_rates.load();
		
		lang_selection_label.load();
		langs.load();
	}

	@Override
	public String getComponentName() {
		return null;
	}

	@Override
	public String getComponentVariable() {
		return "generals_settings";
	}
}
