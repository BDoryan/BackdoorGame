package isotopestudio.backdoor.game.applications.settings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.component.ImageView;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.event.CharEvent;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.event.MouseClickEvent.MouseClickAction;
import org.liquidengine.legui.listener.CharEventListener;
import org.liquidengine.legui.listener.MouseClickEventListener;
import org.liquidengine.legui.style.Style.DisplayType;

import doryanbessiere.isotopestudio.commons.lang.Lang;
import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.components.desktop.Button;
import isotopestudio.backdoor.engine.components.desktop.Label;
import isotopestudio.backdoor.engine.components.desktop.dialog.DialogCallback;
import isotopestudio.backdoor.engine.components.desktop.dialog.InputDialog;
import isotopestudio.backdoor.engine.components.desktop.scrollablepanel.ScrollablePanel;
import isotopestudio.backdoor.engine.datapack.DataParameters;
import isotopestudio.backdoor.engine.datapack.DatapackObject;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.datapack.DatapackLoader;
import isotopestudio.backdoor.game.datapack.DatapackManager;
import isotopestudio.backdoor.game.settings.GameSettings;

/**
 * @author BESSIERE
 * @github https://www.github.com/DoryanBessiere/
 */
public class ThemesPanel extends Panel implements IComponent {

	private ScrollablePanel datapacks_panel = new ScrollablePanel();

	private Button apply_datapack_button = new Button();
	private Button delete_datapack_button = new Button();
	private Button rename_datapack_button = new Button();
	private Button create_datapack_button = new Button();

	public ThemesPanel(SettingsApplication app) {
		super();
		setVariable(app.getComponentVariable() + "_themes");
		setFocusable(false);

		getStyle().setDisplay(DisplayType.FLEX);
		getStyle().getBackground().setColor(0, 0, 0, 0);
		getStyle().setBorderRadius(0f);

		datapacks_panel.setVariable(getComponentVariable() + "_datapacks_panel");
		datapacks_panel.getStyle().setLeft(20f);
		datapacks_panel.getStyle().setRight(200f);
		datapacks_panel.getStyle().setTop(10f);
		datapacks_panel.getStyle().setBottom(10f);
		datapacks_panel.getStyle().setShadow(null);
		datapacks_panel.getStyle().setBorder(null);
		datapacks_panel.getStyle().setBorderRadius(0f);

		datapacks_panel.getContainer().getStyle().setShadow(null);
		datapacks_panel.getContainer().getStyle().setBorder(null);
		datapacks_panel.getContainer().getStyle().setBorderRadius(0f);
		datapacks_panel.getContainer().getStyle().setDisplay(DisplayType.FLEX);
		datapacks_panel.getContainer().getStyle().setLeft(0f);
		datapacks_panel.getContainer().getStyle().setRight(0f);
		datapacks_panel.getContainer().getStyle().setTop(0f);

		datapacks_panel.getViewport().getStyle().setShadow(null);
		datapacks_panel.getViewport().getStyle().setBorder(null);
		datapacks_panel.getViewport().getStyle().setBorderRadius(0f);

		datapacks_panel.setAutoResize(true);
		datapacks_panel.setHorizontalScrollBarVisible(false);

		add(datapacks_panel);

		apply_datapack_button.setVariable(getComponentVariable() + "_apply_datapack_button");
		apply_datapack_button.getStyle().setFontSize(16f);
		apply_datapack_button.getStyle().setTop(10f);
		apply_datapack_button.getStyle().setRight(10f);
		apply_datapack_button.getStyle().setHeight(40f);
		apply_datapack_button.getStyle().setWidth((float) datapacks_panel.getStyle().getRight().get() - 20f);
		apply_datapack_button.getStyle().setShadow(null);
		apply_datapack_button.getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
			@Override
			public void process(MouseClickEvent event) {
				if (event.getAction() != MouseClickAction.RELEASE)
					return;
				if (!event.getTargetComponent().isEnabled())
					return;

				if (datapack_selected != null) {
					if (!BackdoorGame.loadDatapack(datapack_selected.getName())) {
						BackdoorGame.getDesktop().dialog(
								Lang.get("settings_themes_dialog_failed_datpack_loading_title"),
								Lang.get("settings_themes_dialog_failed_datpack_loading_message"));
					} else {
						GameSettings settings = GameSettings.getSettings();
						settings.datapack = datapack_selected.getName();
						settings.save();
					}
				}
			}
		});

		create_datapack_button.setVariable(getComponentVariable() + "_create_datapack_button");
		create_datapack_button.getStyle().setFontSize(16f);
		create_datapack_button.getStyle().setTop(60f);
		create_datapack_button.getStyle().setRight(10f);
		create_datapack_button.getStyle().setWidth((float) datapacks_panel.getStyle().getRight().get() - 20f);
		create_datapack_button.getStyle().setHeight(40f);
		create_datapack_button.getStyle().setShadow(null);
		create_datapack_button.getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
			@Override
			public void process(MouseClickEvent event) {
				if (event.getAction() != MouseClickAction.RELEASE)
					return;
				if (!event.getTargetComponent().isEnabled())
					return;

				InputDialog inputDialog = BackdoorGame.getDesktop().inputDialog(
						Lang.get("settings_themes_creation_of_a_datapack"),
						Lang.get("settings_themes_please_enter_the_name_of_the_datapack"),
						Lang.get("settings_themes_create_the_datapack"), new DialogCallback() {
							@Override
							public void result(Object object) {
								String name = object + ""; // Cast the object to a string

								if (DatapackManager.createDatapack(name)) {
									loadDatapacks();
								}
							}
						});
				inputDialog.responseTextField.getListenerMap().addListener(CharEvent.class, new CharEventListener() {
					@Override
					public void process(CharEvent event) {
						if (inputDialog.responseTextField.getTextState().getText().equals("")) {
							inputDialog.ok_button.setEnabled(false);
						} else {
							inputDialog.ok_button.setEnabled(true);
						}
						boolean exist = false;
						for (DatapackObject datapackObject : datapacksHashMap.keySet()) {
							if (datapackObject.getName()
									.equals(inputDialog.responseTextField.getTextState().getText())) {
								exist = true;
							}
						}
						if (exist) {
							inputDialog.ok_button.setEnabled(false);
						} else {
							inputDialog.ok_button.setEnabled(true);
						}
					}
				});
			}
		});

		rename_datapack_button.setVariable(getComponentVariable() + "_rename_datapack_button");
		rename_datapack_button.getStyle().setFontSize(16f);
		rename_datapack_button.getStyle().setTop(110f);
		rename_datapack_button.getStyle().setRight(10f);
		rename_datapack_button.getStyle().setWidth((float) datapacks_panel.getStyle().getRight().get() - 20f);
		rename_datapack_button.getStyle().setHeight(40f);
		rename_datapack_button.getStyle().setShadow(null);
		rename_datapack_button.getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
			@Override
			public void process(MouseClickEvent event) {
				if (event.getAction() != MouseClickAction.RELEASE)
					return;
				if (!event.getTargetComponent().isEnabled())
					return;
				
				if (datapack_selected == null) return;

				InputDialog inputDialog = BackdoorGame.getDesktop().inputDialog(
						Lang.get("settings_themes_rename_a_datapack"),
						Lang.get("settings_themes_please_enter_the_new_name_of_the_datapack"),
						Lang.get("settings_themes_rename_the_datapack"), new DialogCallback() {
							@Override
							public void result(Object object) {
								String name = object + ""; // Cast the object to a string

								if (datapack_selected != null) {
									if (DatapackManager.renameDatapack(datapack_selected.getName(), name)) {
										loadDatapacks();
									}
								}
							}
						});
				inputDialog.responseTextField.getTextState().setText(datapack_selected.getName());
				inputDialog.responseTextField.getListenerMap().addListener(CharEvent.class, new CharEventListener() {
					@Override
					public void process(CharEvent event) {
						if (inputDialog.responseTextField.getTextState().getText().equals("")) {
							inputDialog.ok_button.setEnabled(false);
						} else {
							inputDialog.ok_button.setEnabled(true);
						}
						boolean exist = false;
						for (DatapackObject datapackObject : datapacksHashMap.keySet()) {
							if (datapackObject.getName()
									.equals(inputDialog.responseTextField.getTextState().getText())) {
								exist = true;
							}
						}
						if (exist) {
							inputDialog.ok_button.setEnabled(false);
						} else {
							inputDialog.ok_button.setEnabled(true);
						}
					}
				});
			}
		});

		delete_datapack_button.setVariable(getComponentVariable() + "_delete_datapack_button");
		delete_datapack_button.getStyle().setFontSize(16f);
		delete_datapack_button.getStyle().setTop(160f);
		delete_datapack_button.getStyle().setRight(10f);
		delete_datapack_button.getStyle().setWidth((float) datapacks_panel.getStyle().getRight().get() - 20f);
		delete_datapack_button.getStyle().setHeight(40f);
		delete_datapack_button.getStyle().setShadow(null);
		delete_datapack_button.getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
			@Override
			public void process(MouseClickEvent event) {
				if (event.getAction() != MouseClickAction.RELEASE)
					return;
				if (!event.getTargetComponent().isEnabled())
					return;

				if (datapack_selected == null) return;

				BackdoorGame.getDesktop().confirmDialog(Lang.get("settings_themes_delete_a_datapack"), Lang.get("settings_themes_delete_a_datapack_question"), new Runnable() {
					@Override
					public void run() {
					}
				}, new Runnable() {
					@Override
					public void run() {
						if (datapack_selected == null) return;
						
						if(DatapackManager.deleteDatapack(datapack_selected.getName())) {
							loadDatapacks();
						}
					}
				});
			}
		});

		add(apply_datapack_button);
		add(create_datapack_button);
		add(rename_datapack_button);
		add(delete_datapack_button);

		load();

		apply_datapack_button.setEnabled(false);
		delete_datapack_button.setEnabled(false);
		rename_datapack_button.setEnabled(false);
	}

	private void loadDatapacks() {
		datapacks_panel.getContainer().clearChildComponents();
		datapacksHashMap.clear();
		int i = 0;
		for (Entry<DatapackObject, File> datapack : BackdoorGame.getDatapacks().entrySet()) {
			DatapackObject datapackObject = datapack.getKey();
			File datapackDirectory = datapack.getValue();

			try {
				Component datapack_component = createDatapackComponent(datapackObject, datapackDirectory);
				datapack_component.getStyle().setTop(i * (float) datapack_component.getStyle().getHeight().get());

				datapacks_panel.getContainer().add(datapack_component);
				datapacksHashMap.put(datapackObject, datapack_component);
				i++;

				if (datapack_selected != null && datapack_selected.getName().equals(datapackObject.getName())) {
					datapack_component.setFocused(true);
					initButtons(datapackObject);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private DatapackObject datapack_selected;

	private HashMap<DatapackObject, Component> datapacksHashMap = new HashMap<>();

	private Panel createDatapackComponent(DatapackObject datapackObject, File datapack_directory)
			throws FileNotFoundException, IOException {
		Panel datapack_panel = new Panel();
		datapack_panel.getStyle().setDisplay(DisplayType.FLEX);
		datapack_panel.getStyle().getBackground().setColor(0, 0, 0, 0);
		datapack_panel.getStyle().setBorder(null);
		datapack_panel.getStyle().setShadow(null);
		datapack_panel.getStyle().setBorderRadius(0f);

		DataParameters.applyBackgroundColor(this, "datapack_component_background_color", datapack_panel.getStyle());
		DataParameters.applyBackgroundColor(this, "datapack_component_background_color_focused",
				datapack_panel.getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "datapack_component_background_color_hovered",
				datapack_panel.getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "datapack_component_background_color_pressed",
				datapack_panel.getPressedStyle());

		// Border colors
		DataParameters.applyBorderColor(this, "datapack_component_border_color", datapack_panel.getStyle());
		DataParameters.applyBorderColor(this, "datapack_component_border_color_focused",
				datapack_panel.getFocusedStyle());
		DataParameters.applyBorderColor(this, "datapack_component_border_color_hovered",
				datapack_panel.getHoveredStyle());
		DataParameters.applyBorderColor(this, "datapack_component_border_color_pressed",
				datapack_panel.getPressedStyle());

		datapack_panel.getStyle().setRight(2);
		datapack_panel.getStyle().setLeft(0);
		datapack_panel.getStyle().setHeight(75);
		datapack_panel.setSize(datapack_panel.getSize().x, 75);

		Label name = new Label(datapackObject.getName());
		name.setVariable("datapack_component_name");
		name.getStyle().setFontSize(15f);
		name.getStyle().setTop(10);
		name.getStyle().setLeft(80);
		name.setFocusable(false);

		Label author = new Label(datapackObject.getAuthor());
		author.setVariable("datapack_component_author");
		author.getStyle().setFontSize(15f);
		author.getStyle().setTop(30);
		author.getStyle().setLeft(80);
		author.setFocusable(false);

		Label version = new Label(datapackObject.getVersion());
		version.setVariable("datapack_component_version");
		version.getStyle().setFontSize(15f);
		version.getStyle().setTop(50f);
		version.getStyle().setLeft(80);
		version.setFocusable(false);

		ImageView icon = new ImageView();
		icon.setImage(DatapackLoader.getDatapackIcon(datapack_directory));
		icon.getStyle().setBorderRadius(0f);
		icon.getStyle().setBorder(null);
		icon.getStyle().setShadow(null);
		icon.getStyle().getBackground().setColor(0, 0, 0, 0);
		icon.getStyle().setTop(10f);
		icon.getStyle().setLeft(10f);
		icon.getStyle().setWidth(52.5f);
		icon.getStyle().setHeight(52.5f);
		icon.setFocusable(false);

		name.load();
		author.load();
		version.load();

		datapack_panel.add(icon);
		datapack_panel.add(name);
		datapack_panel.add(author);
		datapack_panel.add(version);

		datapack_panel.getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
			@Override
			public void process(MouseClickEvent event) {
				if (event.getAction() != MouseClickAction.RELEASE)
					return;

				selectDatapack(datapackObject);
			}
		});

		return datapack_panel;
	}

	private void selectDatapack(DatapackObject datapackObject) {
		if (datapack_selected != null && datapacksHashMap.containsKey(datapack_selected)) {
			datapacksHashMap.get(datapack_selected).setFocused(false);
		}
		initButtons(datapackObject);
	}

	private void initButtons(DatapackObject datapackObject) {
		datapack_selected = datapackObject;

		create_datapack_button.setEnabled(true);

		delete_datapack_button.setEnabled(true);
		rename_datapack_button.setEnabled(true);

		if (datapackObject.getName().equalsIgnoreCase("default")) {
			delete_datapack_button.setEnabled(false);
			rename_datapack_button.setEnabled(false);
		}

		if (BackdoorGame.getDatapack().getDatapackObject().getName().equals(datapackObject.getName())) {
			apply_datapack_button.setEnabled(false);

			delete_datapack_button.setEnabled(false);
			rename_datapack_button.setEnabled(false);
		} else {
			apply_datapack_button.setEnabled(true);
		}
	}

	@Override
	public void update() {
		if (datapack_selected != null && datapacksHashMap.containsKey(datapack_selected)) {
			datapacksHashMap.get(datapack_selected).setFocused(true);
		}
	}

	@Override
	public void load() {
		create_datapack_button.getTextState().setText(Lang.get("settings_themes_create_datapack"));
		rename_datapack_button.getTextState().setText(Lang.get("settings_themes_rename_datapack"));
		delete_datapack_button.getTextState().setText(Lang.get("settings_themes_delete_datapack"));
		apply_datapack_button.getTextState().setText(Lang.get("settings_themes_apply_datapack"));

		create_datapack_button.load();
		rename_datapack_button.load();
		delete_datapack_button.load();
		apply_datapack_button.load();

		datapacks_panel.load();
		loadDatapacks();
	}

	@Override
	public String getComponentName() {
		return null;
	}

	private String variable;

	/**
	 * @param variable the variable to set
	 */
	public void setVariable(String variable) {
		this.variable = variable;
	}

	@Override
	public String getComponentVariable() {
		return variable;
	}
}
