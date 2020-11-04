	package isotopestudio.backdoor.engine.components.desktop.desktop;

import static org.liquidengine.legui.event.MouseClickEvent.MouseClickAction.CLICK;

import java.util.ArrayList;

import org.joml.Vector4f;
import org.liquidengine.legui.animation.Animation;
import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.component.ImageView;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.component.event.button.ButtonWidthChangeEvent;
import org.liquidengine.legui.component.misc.listener.button.UpdateButtonStyleWidthListener;
import org.liquidengine.legui.component.misc.listener.button.UpdateButtonWidthListener;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.event.WindowSizeEvent;
import org.liquidengine.legui.intersection.RectangleIntersector;
import org.liquidengine.legui.listener.EventListener;
import org.liquidengine.legui.listener.MouseClickEventListener;
import org.liquidengine.legui.listener.WindowSizeEventListener;
import org.liquidengine.legui.style.Background;
import org.liquidengine.legui.style.Style.DisplayType;

import doryanbessiere.isotopestudio.commons.lang.Lang;
import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.components.desktop.Button;
import isotopestudio.backdoor.engine.components.desktop.Text;
import isotopestudio.backdoor.engine.components.desktop.desktop.taskbar.TaskBar;
import isotopestudio.backdoor.engine.components.desktop.dialog.Dialog;
import isotopestudio.backdoor.engine.components.desktop.dialog.DialogCallback;
import isotopestudio.backdoor.engine.components.desktop.dialog.InputDialog;
import isotopestudio.backdoor.engine.components.desktop.notification.Notification;
import isotopestudio.backdoor.engine.components.desktop.window.Window;
import isotopestudio.backdoor.engine.components.events.TextDynamicSizeChangeEvent;
import isotopestudio.backdoor.engine.components.utils.Responsive;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.applications.ChatApplication;
import isotopestudio.backdoor.game.applications.DashboardApplication;
import isotopestudio.backdoor.game.applications.MarketApplication;
import isotopestudio.backdoor.game.applications.MatchmakingApplication;
import isotopestudio.backdoor.game.applications.NetworkApplication;
import isotopestudio.backdoor.game.applications.terminal.TerminalApplication;

public class Desktop extends Panel implements IComponent {

	private ImageView background;
	private TaskBar task_bar;

	public ArrayList<WindowSizeEventListener> windowSizeEventListeners = new ArrayList<WindowSizeEventListener>();

	public Desktop(int width, int height) {
		super(0, 0, width, height);

		getStyle().setTop(0f);
		getStyle().setRight(0f);
		getStyle().setLeft(0f);
		getStyle().setBottom(0f);
		getStyle().setBorderRadius(0f);
	}

	public ImageView getBackground() {
		return background;
	}

	public TaskBar getTaskBar() {
		return task_bar;
	}

	public void init() {
		getListenerMap().addListener(WindowSizeEvent.class, (WindowSizeEventListener) event -> {
			setSize(event.getWidth(), event.getHeight());
			if (!windowSizeEventListeners.isEmpty()) {
				for (WindowSizeEventListener listener : windowSizeEventListeners) {
					listener.process(event);
				}
			}
		});

		Background b = new Background();
		b.setColor(new Vector4f(0f, 0f, 0f, 1f));
		getStyle().setBackground(b);

		setBackground(new ImageView());
		setTaskBar(new TaskBar());

		add(new DesktopIcon(10, 40, BackdoorGame.getDatapack().getImage("window_icon_terminal"), "Terminal",
				new Runnable() {
					@Override
					public void run() {
						if (BackdoorGame.getGameParty() != null) {
							TerminalApplication.showApplication();
						} else {
							dialog(Lang.get("application_cannot_launch_title"),
									Lang.get("application_cannot_launch_any_party"));
						}
					}
				}));

		add(new DesktopIcon(10, 140, BackdoorGame.getDatapack().getImage("window_icon_network"), "Network",
				new Runnable() {
					@Override
					public void run() {
						if (BackdoorGame.getGameParty() != null) {
							NetworkApplication.show(BackdoorGame.getGameParty());
						} else {
							dialog(Lang.get("application_cannot_launch_title"),
									Lang.get("application_cannot_launch_any_party"));
						}
					}
				}));

		add(new DesktopIcon(10, 240, BackdoorGame.getDatapack().getImage("window_icon_dashboard"), "Dashboard",
				new Runnable() {
					@Override
					public void run() {
						if (BackdoorGame.getGameParty() != null) {
							if (NetworkApplication.game_element_targeted == null)
								return;
							DashboardApplication.showApplication(NetworkApplication.game_element_targeted);
						} else {
							dialog(Lang.get("application_cannot_launch_title"),
									Lang.get("application_cannot_launch_any_party"));
						}
					}
				}));

		add(new DesktopIcon(10, 340, BackdoorGame.getDatapack().getImage("window_icon_market"), "Market",
				new Runnable() {
					@Override
					public void run() {
						if (BackdoorGame.getGameParty() != null) {
							MarketApplication.showApplication();
						} else {
							dialog(Lang.get("application_cannot_launch_title"),
									Lang.get("application_cannot_launch_any_party"));
						}
					}
				}));

		add(new DesktopIcon(10, 440, BackdoorGame.getDatapack().getImage("window_icon_multiplayer"), "Multiplayer",
				new Runnable() {
					@Override
					public void run() {
						MatchmakingApplication.showApplication();
					}
				}));

		add(new DesktopIcon(110, 40, BackdoorGame.getDatapack().getImage("window_icon_messaging"), "Messaging",
				new Runnable() {
					@Override
					public void run() {
						ChatApplication.showApplication();
					}
				}));

		load();
	}

	private ArrayList<Notification> notificationQueue = new ArrayList<>();

	public void spawnNotification(Notification notification) {
		notification.load();
		notificationQueue.add(notification);
	}

	private boolean notification_displayed = false;

	public void update() {
		if (notification_displayed)
			return;
		if (notificationQueue.size() > 0) {
			Notification notification = notificationQueue.get(notificationQueue.size() - 1);
			notificationQueue.remove(notification);
			Responsive responsive = new Responsive(notification, null, null, 10f, null);
			notification.getStyle().setRight(10f);
			notification_displayed = true;
			Animation animation = new Animation() {

				@Override
				protected void beforeAnimation() {
					super.beforeAnimation();
					Desktop.this.add(notification);
					notification.setPosition(notification.getPosition().x,
							notification.getParent().getSize().y - notification.getSize().y);
					responsive.applyResponsive();
				}

				private double time_left = -1D;
				private double time = 0D;

				@Override
				protected boolean animate(double delta) {
					time += delta;

					float bottom = (float) ((double) (time * 48D) * 2);
					if (bottom > (10 + notification.getSize().y)) {
						bottom = (10 + notification.getSize().y);
					}

					if (time_left == -1D)
						time_left += time + notification.getTimeLeftInSecond();
					else if (time > time_left)
						return true;
					notification.setPosition(notification.getPosition().x,
							notification.getParent().getSize().y - bottom);
					responsive.applyResponsive();

					return false;
				}

				@Override
				protected void afterAnimation() {
					super.afterAnimation();
					notification_displayed = false;
					Desktop.this.remove(notification);
				}
			};
			notification.getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
				@Override
				public void process(MouseClickEvent event) {
					animation.stopAnimation();
				}
			});
			animation.startAnimation();
		}
	}

	@Override
	public void load() {
		this.background.setImage(BackdoorGame.datapack.getImage("desktop_background"));
	}

	public void confirmDialog(String title, String message, Runnable no, Runnable yes) {
		int width = 600;
		Dialog dialog = new Dialog(title, getSize().x < width ? getSize().x - 20 : width, 50);
		dialog.getStyle().setMinWidth(dialog.getSize().x);
		dialog.getStyle().setMinHeight(dialog.getSize().y);

		Text questionLabel = new Text(message);

		questionLabel.getStyle().setLeft(10f);
		questionLabel.getStyle().setTop(10f);
		questionLabel.getStyle().setRight(10f);
		questionLabel.getListenerMap().addListener(TextDynamicSizeChangeEvent.class, new EventListener<TextDynamicSizeChangeEvent>() {
			@Override
			public void process(TextDynamicSizeChangeEvent event) {
				dialog.getSize().set(dialog.getSize().x, event.getHeight() + 50);
			}
		});

		Button yesButton = new Button(Lang.get("message_yes"));
		Button noButton = new Button(Lang.get("message_no"));
		yesButton.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) e -> {
			if (CLICK == e.getAction()) {
				dialog.close();
				yes.run();
			}
		});
		noButton.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) e -> {
			if (CLICK == e.getAction()) {
				dialog.close();
				no.run();
			}
		});

		dialog.getContainer().getStyle().setDisplay(DisplayType.FLEX);

		yesButton.getStyle().setLeft(10f);
		yesButton.getStyle().setBottom(10f);
		yesButton.getStyle().setWidth(50);
		yesButton.getStyle().setHeight(20);
		yesButton.getListenerMap().addListener(ButtonWidthChangeEvent.class, new UpdateButtonStyleWidthListener() {
			@Override
			public void process(ButtonWidthChangeEvent event) {
				event.getTargetComponent().getStyle().setWidth(event.getWidth() + 20);
			}
		});
		yesButton.getListenerMap().addListener(ButtonWidthChangeEvent.class, new UpdateButtonWidthListener() {
			@Override
			public void process(ButtonWidthChangeEvent event) {
				event.getTargetComponent().getSize().x = event.getWidth() + 20;
			}
		});

		noButton.getStyle().setLeft(20f + (float) yesButton.getStyle().getWidth().get());
		noButton.getStyle().setBottom(10f);
		noButton.getStyle().setWidth(50);
		noButton.getStyle().setHeight(20);
		noButton.getListenerMap().addListener(ButtonWidthChangeEvent.class, new UpdateButtonStyleWidthListener() {
			@Override
			public void process(ButtonWidthChangeEvent event) {
				event.getTargetComponent().getStyle().setWidth(event.getWidth() + 20);
			}
		});
		noButton.getListenerMap().addListener(ButtonWidthChangeEvent.class, new UpdateButtonWidthListener() {
			@Override
			public void process(ButtonWidthChangeEvent event) {
				event.getTargetComponent().getSize().x = event.getWidth() + 20;
			}
		});

		dialog.getContainer().add(questionLabel);
		dialog.getContainer().add(yesButton);
		dialog.getContainer().add(noButton);

		dialog.load();
		questionLabel.load();
		yesButton.load();
		noButton.load();

		dialog.show(BackdoorGame.frame);
	}

	public InputDialog inputDialog(String title, String message, String button_name, DialogCallback callback) {
		InputDialog dialog = new InputDialog(title, message, button_name, callback, 300, 110);
		dialog.getStyle().setMinWidth(dialog.getSize().x);
		dialog.getStyle().setMinHeight(dialog.getSize().y);

		dialog.show(BackdoorGame.frame);

		return dialog;
	}

	public void dialog(String title, String message) {
		int width = 600;
		Dialog dialog = new Dialog(title, getSize().x < width ? getSize().x - 20 : width, 50);
		dialog.getStyle().setMinWidth(dialog.getSize().x);
		dialog.getStyle().setMinHeight(dialog.getSize().y);
		
		dialog.getContainer().getStyle().setDisplay(DisplayType.FLEX);

		Text questionLabel = new Text(message);
		questionLabel.getStyle().setLeft(10f);
		questionLabel.getStyle().setTop(10f);
		questionLabel.getStyle().setRight(10f);
		questionLabel.getListenerMap().addListener(TextDynamicSizeChangeEvent.class, new EventListener<TextDynamicSizeChangeEvent>() {
			@Override
			public void process(TextDynamicSizeChangeEvent event) {
				dialog.getSize().set(dialog.getSize().x, event.getHeight() + 50);
			}
		});

		Button okButton = new Button(Lang.get("message_ok"));
		okButton.getListenerMap().addListener(ButtonWidthChangeEvent.class, new UpdateButtonStyleWidthListener() {
			@Override
			public void process(ButtonWidthChangeEvent event) {
				okButton.getStyle().setWidth(event.getWidth() + 20);
			}
		});

		okButton.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) e -> {
			if (CLICK == e.getAction()) {
				dialog.close();
			}
		});

		okButton.getStyle().setLeft(10f);
		okButton.getStyle().setBottom(10f);
		okButton.getStyle().setHeight(20);

		dialog.getContainer().add(questionLabel);
		dialog.getContainer().add(okButton);

		dialog.load();
		questionLabel.load();
		okButton.load();

		dialog.show(BackdoorGame.frame);
	}

	public void setTaskBar(TaskBar task_bar) {
		if (task_bar == null) {
			if (this.task_bar != null) {
				this.remove(this.task_bar);
			}
		} else {
			task_bar.setSize(getSize().x, getSize().y);
			task_bar.getStyle().getBorder().setEnabled(false);
			task_bar.getStyle().setBorderRadius(0F);
			task_bar.setIntersector(new RectangleIntersector());

			this.add(task_bar);
			task_bar.init();

			windowSizeEventListeners.add(new WindowSizeEventListener() {
				@Override
				public void process(WindowSizeEvent event) {
					task_bar.getResponsive().applyResponsive();
				}
			});
		}

		this.task_bar = task_bar;
	}

	public void setBackground(ImageView background) {
		if (background == null) {
			if (this.background != null) {
				this.remove(this.background);
			}
		} else {
			background.setSize(getSize().x, getSize().y);
			background.getStyle().getBorder().setEnabled(false);
			background.getStyle().setBorderRadius(0F);
			background.setIntersector(new RectangleIntersector());

			background.getStyle().setRight(0f);
			background.getStyle().setLeft(0f);
			background.getStyle().setTop(0f);
			background.getStyle().setBottom(0f);

			this.add(background);

			Responsive responsive_background = new Responsive(background, 0f, 0f, 0f, 0f);
			responsive_background.applyResponsive();

			windowSizeEventListeners.add(new WindowSizeEventListener() {
				@Override
				public void process(WindowSizeEvent event) {
					responsive_background.applyResponsive();
				}
			});
		}

		this.background = background;
	}

	private ArrayList<Window> windows = new ArrayList<Window>();

	public void addWindow(Window window) {
		task_bar.addWindowIcon(window);
		this.add(window);
		windows.add(window);
	}

	public void removeWindow(Window window) {
		task_bar.removeWindowIcon(window);
		this.remove(window);
		windows.remove(window);
	}

	public boolean containsWindow(Window window) {
		for (Window window_ : windows) {
			if (window_.toString().equals(window.toString())) {
				return true;
			}
		}
		return false;
	}

	public void reload() {
		BackdoorGame.getDatapack().load();
		System.out.println("reload(){");
		reload(this);
		System.out.println("};");
	}

	private void reload(Component component) {
		System.out.println("-> " + component.getClass().getSimpleName());
		for (Component children : component.getChildComponents()) {
			if (!children.getChildComponents().isEmpty()) {
				reload(children);
			}
			if (children instanceof IComponent) {
				((IComponent) children).load();
				System.out.println("   -> " + children.getClass().getSimpleName());
			}
		}
		if (component instanceof IComponent) {
			((IComponent) component).load();
		}
	}

	public void update(Component component) {
		for (Component children : component.getChildComponents()) {
			if (!children.getChildComponents().isEmpty()) {
				update(children);
			}
			if (children instanceof IComponent) {
				((IComponent) children).update();
			}
		}
		if (component instanceof IComponent) {
			((IComponent) component).update();
		}
	}

	@Override
	public String getComponentName() {
		return "desktop";
	}

	@Override
	public String getComponentVariable() {
		return null;
	}

	public void ready() {
		spawnNotification(new Notification(BackdoorGame.getDatapack().getImage("backbot_happy"),
				Lang.get("backbot_notification_ready_title"), Lang.get("backbot_notification_ready_text"), 15d));
	}
}
