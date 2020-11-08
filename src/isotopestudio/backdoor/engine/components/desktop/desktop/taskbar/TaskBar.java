package isotopestudio.backdoor.engine.components.desktop.desktop.taskbar;

import java.util.HashMap;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.liquidengine.legui.component.ImageView;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.event.MouseClickEvent.MouseClickAction;
import org.liquidengine.legui.intersection.RectangleIntersector;
import org.liquidengine.legui.listener.MouseClickEventListener;
import org.liquidengine.legui.style.Style.DisplayType;
import org.liquidengine.legui.style.border.SimpleLineBorder;
import org.liquidengine.legui.style.color.ColorConstants;

import doryanbessiere.isotopestudio.commons.lang.Lang;
import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.components.box.HorizontalBox;
import isotopestudio.backdoor.engine.components.desktop.IconLabel;
import isotopestudio.backdoor.engine.components.desktop.popup.PopupMenu;
import isotopestudio.backdoor.engine.components.desktop.window.Window;
import isotopestudio.backdoor.engine.components.events.LabelSizeEvent;
import isotopestudio.backdoor.engine.components.utils.Responsive;
import isotopestudio.backdoor.engine.datapack.DataParameters;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.applications.FriendsApplication;
import isotopestudio.backdoor.game.applications.settings.SettingsApplication;
import isotopestudio.backdoor.utils.legui.LeguiTools;

public class TaskBar extends Panel implements IComponent {

	public static final int POSITION_BOTTOM = 101;
	public static final int POSITION_TOP = 102;
	public static final int POSITION_RIGHT = 001;
	public static final int POSITION_LEFT = 002;

	private int position;

	private int height = 30;
	private int width = 30;

	public void setPosition(int position) {
		this.position = position;

		if (this.position != POSITION_TOP) {
			System.err.println("The taskbar cannot be placed in this position!");
			return;
		}
		if (position == 101) {
			this.responsive.getComponent().setSize(new Vector2f(0.0F, this.height));
			this.responsive.setTop(null);
			this.responsive.setBottom(0f);
			this.responsive.setRight(0f);
			this.responsive.setLeft(0f);
		}
		if (position == 102) {
			this.responsive.getComponent().setSize(new Vector2f(0.0F, this.height));
			this.responsive.setTop(0f);
			this.responsive.setBottom(null);
			this.responsive.setRight(0f);
			this.responsive.setLeft(0f);
		}
		if (position == 2) {
			this.responsive.getComponent().setSize(new Vector2f(this.width, 0.0F));
			this.responsive.setTop(0f);
			this.responsive.setBottom(0f);
			this.responsive.setRight(0f);
			this.responsive.setLeft(null);
		}
		if (position == 3) {
			this.responsive.getComponent().setSize(new Vector2f(this.width, 0.0F));
			this.responsive.setTop(0f);
			this.responsive.setBottom(0f);
			this.responsive.setRight(null);
			this.responsive.setLeft(0f);
		}
		this.responsive.applyResponsive();
	}

	public boolean isHorizontal() {
		return this.position > 100;
	}

	public boolean isVertical() {
		return !isHorizontal();
	}

	public int getTaskbarPosition() {
		return position;
	}

	private Responsive responsive;

	private ImageView menu_button;
	private Object windows_box;

	public void init() {
		this.responsive = new Responsive(this, null, null, null, null);
		setPosition(BackdoorGame.getDatapack().getData("taskbar").getInteger("task_bar_position"));

		this.getStyle().setDisplay(DisplayType.FLEX);

		if (isHorizontal()) {
			menu_button = new ImageView();

			menu_button.setSize(height + 6, height + 6);
			menu_button.setPosition(12, -2);
			menu_button.getStyle().setBorder(new SimpleLineBorder(new Vector4f(1, 1, 1, 1), 3));
			menu_button.getStyle().setFocusedStrokeColor(ColorConstants.transparent());
			menu_button.getStyle().getBackground().setColor(new Vector4f(0, 0, 0, 1f));
			menu_button.getStyle().setBorderRadius(this.getSize().x);
			menu_button.getStyle().setShadow(null);
			menu_button.setIntersector(new RectangleIntersector());

			/*
			 * menu_button.getStyle().setTop(-2); menu_button.getStyle().setLeft(4);
			 * menu_button.getStyle().setWidth(height + 6);
			 * menu_button.getStyle().setHeight(height + 6);
			 */

			menu_button.getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
				@Override
				public void process(MouseClickEvent event) {
					if (event.getAction() != MouseClickAction.RELEASE)
						return;

					PopupMenu popupmenu = new PopupMenu(0, height);
					popupmenu.setSize(50, 10);
					popupmenu.getStyle().setBorderRadius(0f);

					Panel top_margin = new Panel();
					top_margin.setFocusable(false);
					LeguiTools.setInvisible(top_margin);
					top_margin.setSize(10, 10);
					popupmenu.add(top_margin);
					popupmenu.initSize();

					IconLabel settings = new IconLabel(Lang.get("taskbar_settings"),
							BackdoorGame.getDatapack().getImage("icon_settings"), new LabelSizeEvent() {
								@Override
								public void process(float width, float height) {
									popupmenu.initSize();
								}
							});
					MouseClickEventListener settings_click = new MouseClickEventListener() {
						@Override
						public void process(MouseClickEvent event) {
							if (event.getAction() != MouseClickAction.RELEASE)
								return;

							SettingsApplication.showApplication();
						}
					};
					settings.getLabel().getListenerMap().addListener(MouseClickEvent.class, settings_click);
					settings.getIconView().getListenerMap().addListener(MouseClickEvent.class, settings_click);
					settings.getListenerMap().addListener(MouseClickEvent.class, settings_click);

					IconLabel profile = new IconLabel(Lang.get("taskbar_profile"),
							BackdoorGame.getDatapack().getImage("icon_profile"), new LabelSizeEvent() {
								@Override
								public void process(float width, float height) {
									popupmenu.initSize();
								}
							});
					MouseClickEventListener profile_click = new MouseClickEventListener() {
						@Override
						public void process(MouseClickEvent event) {
							if (event.getAction() != MouseClickAction.RELEASE)
								return;
						}
					};
					profile.getLabel().getListenerMap().addListener(MouseClickEvent.class, profile_click);
					profile.getIconView().getListenerMap().addListener(MouseClickEvent.class, profile_click);
					profile.getListenerMap().addListener(MouseClickEvent.class, profile_click);

					IconLabel shutdown = new IconLabel(Lang.get("taskbar_shutdown"),
							BackdoorGame.getDatapack().getImage("icon_shutdown"), new LabelSizeEvent() {
								@Override
								public void process(float width, float height) {
									popupmenu.initSize();
								}
							});
					MouseClickEventListener shutdown_click = new MouseClickEventListener() {
						@Override
						public void process(MouseClickEvent event) {
							if (event.getAction() != MouseClickAction.RELEASE)
								return;

							BackdoorGame.stop();
						}
					};
					shutdown.getLabel().getListenerMap().addListener(MouseClickEvent.class, shutdown_click);
					shutdown.getIconView().getListenerMap().addListener(MouseClickEvent.class, shutdown_click);
					shutdown.getListenerMap().addListener(MouseClickEvent.class, shutdown_click);

					IconLabel restart = new IconLabel(Lang.get("taskbar_restart"),
							BackdoorGame.getDatapack().getImage("icon_restart"), new LabelSizeEvent() {
								@Override
								public void process(float width, float height) {
									popupmenu.initSize();
								}
							});
					MouseClickEventListener restart_click = new MouseClickEventListener() {
						@Override
						public void process(MouseClickEvent event) {
							if (event.getAction() != MouseClickAction.RELEASE)
								return;

							BackdoorGame.restart();
						}
					};
					restart.getLabel().getListenerMap().addListener(MouseClickEvent.class, restart_click);
					restart.getIconView().getListenerMap().addListener(MouseClickEvent.class, restart_click);
					restart.getListenerMap().addListener(MouseClickEvent.class, restart_click);

					popupmenu.add(profile);
					popupmenu.add(settings);
					popupmenu.add(shutdown);
					popupmenu.add(restart);
					popupmenu.load();

					popupmenu.setFocused(true);

					getParent().remove(menu_button);
					BackdoorGame.getDesktop().add(popupmenu);
					getParent().add(menu_button);
				}
			});

			this.windows_box = new HorizontalBox(8);

			HorizontalBox box = getHorizontalWindowsBox();
			box.getStyle().setTop(4);
			box.getStyle()
					.setLeft(25 + (menu_button.getStyle().getDisplay() == DisplayType.FLEX
							? (float) menu_button.getStyle().getWidth().get()
							: menu_button.getSize().x));
			box.getStyle().setRight(0);
			box.getStyle().setBottom(2);
			box.getStyle().getBackground().getColor().set(0, 0, 0, 0);
			box.getStyle().getBorder().setEnabled(false);
			box.getStyle().setBorderRadius(0F);
			box.getStyle().setShadow(null);
			box.setIntersector(new RectangleIntersector());

			ImageView friends_icon = new ImageView(BackdoorGame.getDatapack().getImage("icon_friends"));
			friends_icon.setSize(height - 12, height - 12);
			friends_icon.getStyle().setBorder(null);
			friends_icon.getStyle().setFocusedStrokeColor(ColorConstants.transparent());
			friends_icon.getStyle().getBackground().setColor(ColorConstants.transparent());
			friends_icon.getStyle().setBorderRadius(0f);
			friends_icon.getStyle().setShadow(null);
			friends_icon.setIntersector(new RectangleIntersector());
			friends_icon.getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
				@Override
				public void process(MouseClickEvent event) {
					FriendsApplication.showApplication();
				}
			});

			friendResponsive = new Responsive(friends_icon, (float) 8, null, (float) 8, null);
			this.getParent().add(friends_icon);
			friendResponsive.applyResponsive();

			this.getParent().add(menu_button);
			this.add(getHorizontalWindowsBox());
		}
		load();
		((IComponent) this).initIComponent();
	}

	public Responsive friendResponsive;

	public HorizontalBox getHorizontalWindowsBox() {
		return (HorizontalBox) windows_box;
	}

	@Override
	public void load() {
		setPosition(BackdoorGame.getDatapack().getData("taskbar").getInteger("task_bar_position"));

		DataParameters data = getDataParameters(this);

		data.applyBackgroundColor("task_bar_background_color", getStyle());
		data.applyBackgroundColor("task_bar_background_color_focused", getFocusedStyle());
		data.applyBackgroundColor("task_bar_background_color_hovered", getHoveredStyle());
		data.applyBackgroundColor("task_bar_background_color_pressed", getPressedStyle());

		this.menu_button.setImage(BackdoorGame.loadImageURL(BackdoorGame.getUserProfile().getProfilePictureURL()));
	}

	@Override
	public void update() {
	}

	public Responsive getResponsive() {
		return responsive;
	}

	public HashMap<String, TaskBarWindowIcon> windows_icons = new HashMap<>();

	public void addWindowIcon(Window window) {
		if (windows_icons.containsKey(window.getUuid().toString()))
			return;

		TaskBarWindowIcon window_icon = new TaskBarWindowIcon(window, window.hasIcon() ? window.getIcon().getImage()
				: BackdoorGame.getDatapack().getImage("window_icon_default"), width, width);
		windows_icons.put(window.getUuid().toString(), window_icon);

		window_icon.load();
		getHorizontalWindowsBox().addComponent(window_icon);
	}

	public void removeWindowIcon(Window window) {
		if (!windows_icons.containsKey(window.getUuid().toString()))
			return;

		getHorizontalWindowsBox().removeComponent(windows_icons.get(window.getUuid().toString()));
		windows_icons.remove(window.getUuid().toString());
	}

	public void updateIcon(Window window) {
		if (windows_icons.containsKey(window.getUuid().toString())) {
			windows_icons.get(window.getUuid().toString()).setIcon(window.hasIcon() ? window.getIcon().getImage()
					: BackdoorGame.getDatapack().getImage("window_icon_default"));
			return;
		} else {
			addWindowIcon(window);
		}
	}

	@Override
	public String getComponentName() {
		return "taskbar";
	}

	@Override
	public String getComponentVariable() {
		return null;
	}
}
