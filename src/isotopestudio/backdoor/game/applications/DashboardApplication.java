package isotopestudio.backdoor.game.applications;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.component.event.label.LabelWidthChangeEvent;
import org.liquidengine.legui.component.event.label.LabelWidthChangeEventListener;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.event.MouseClickEvent.MouseClickAction;
import org.liquidengine.legui.listener.MouseClickEventListener;
import org.liquidengine.legui.style.Style.DisplayType;
import org.liquidengine.legui.style.border.SimpleLineBorder;

import doryanbessiere.isotopestudio.commons.lang.Lang;
import isotopestudio.backdoor.core.elements.GameElement;
import isotopestudio.backdoor.core.elements.GameElementType;
import isotopestudio.backdoor.core.player.Player;
import isotopestudio.backdoor.core.team.Team;
import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.components.desktop.Button;
import isotopestudio.backdoor.engine.components.desktop.Label;
import isotopestudio.backdoor.engine.components.desktop.ProgressBar;
import isotopestudio.backdoor.engine.components.desktop.window.Window;
import isotopestudio.backdoor.engine.components.painting.MultiLine;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.BackdoorGame.IUpdate;
import isotopestudio.backdoor.game.applications.terminal.TerminalApplication;
import isotopestudio.backdoor.game.game.GameParty;
import isotopestudio.backdoor.game.game.component.GameElementComponent;
import isotopestudio.backdoor.game.game.component.NodeGameElementComponent;
import isotopestudio.backdoor.game.game.component.ServerGameElementComponent;

public class DashboardApplication extends Window implements IComponent {

	public static DashboardApplication main;

	public static void showApplication(GameElement gameElement) {
		if (main == null) {
			main = new DashboardApplication(gameElement);
			BackdoorGame.getDesktop().addWindow(main);
			main.centerLocation();
			main.load();
		} else {
			if (BackdoorGame.getDesktop().containsWindow(main)) {
				if (main.gameElement == null || gameElement == null
						|| !main.gameElement.getUUID().toString().equalsIgnoreCase(gameElement.getUUID().toString())) {
					BackdoorGame.getDesktop().removeWindow(main);
					showApplication(gameElement);
					return;
				}
				main.showWindow();
				main.load();
			} else {
				main.deleteOldUpdater();
				main = null;
				showApplication(gameElement);
			}
		}
	}

	private GameElement gameElement;
	private GameElementComponent gameElementComponent;

	public DashboardApplication(GameElement gameElement) {
		super("Dashboard", 0, 0, 560, 300);

		this.gameElement = gameElement;
		initDashboard();
	}

	/**
	 * @return the gameElement
	 */
	public GameElement getGameElement() {
		return gameElement;
	}

	/**
	 * @return the gameElementComponent
	 */
	public GameElementComponent getGameElementComponent() {
		return gameElementComponent;
	}

	private void initDashboard() {
		setVariable("dashboard_window");

		getContainer().getStyle().setDisplay(DisplayType.FLEX);

		initBottom();
		initTopLeft();
		load();
	}

	private Label ip_label = new Label();
	private Label country_label = new Label();
	private Label team_label = new Label();
	private Label status_label = new Label();

	private Panel top_left_content = new Panel();
	private Panel info_container = new Panel();

	private void initTopLeft() {
		top_left_content.getStyle().setDisplay(DisplayType.FLEX);
		top_left_content.getStyle().setLeft(1f);
		top_left_content.getStyle().setBottom((float) bottom_content.getStyle().getHeight().get() + 2f);
		top_left_content.getStyle().setTop(1f);
		top_left_content.getStyle().setRight(150f);
		top_left_content.getStyle().getBackground().setColor(0, 0, 0, 0);
		top_left_content.getStyle().setBorder(new SimpleLineBorder(new Vector4f(1, 1, 1, 1), 1));
		top_left_content.getStyle().setBorderRadius(0F);
		top_left_content.getStyle().setShadow(null);

		info_container.getStyle().setTop(35f);
		info_container.getStyle().setLeft(70f);
		info_container.getStyle().setRight(0f);
		info_container.getStyle().setBottom(0);
		info_container.getStyle().getBackground().setColor(0, 0, 0, 0);
		info_container.getStyle().setBorder(new SimpleLineBorder(new Vector4f(1, 1, 1, 0), 1));
		info_container.getStyle().setBorderRadius(0F);
		info_container.getStyle().setShadow(null);

		if (gameElement != null) {
			if (gameElement.getType() == GameElementType.SERVER) {
				this.gameElementComponent = new ServerGameElementComponent(10, 10, 60, 2, 10,
						BackdoorGame.getGameParty().getMapData().getElements().get(gameElement.getName()));
			} else {
				this.gameElementComponent = new NodeGameElementComponent(10, 10, 60, 2, 10,
						BackdoorGame.getGameParty().getMapData().getElements().get(gameElement.getName()));
			}
			this.gameElementComponent.getStyle().setTop(80);
			this.gameElementComponent.getStyle().setLeft(10);

			top_left_content.add(this.gameElementComponent);
		}

		country_label.setVariable(getComponentVariable() + "_country_label");
		country_label.setPosition(110f, 12.5f);
		country_label.getStyle().setFontSize(16F);

		ip_label.setVariable(getComponentVariable() + "_ip_label");
		ip_label.setPosition(110f, 47.5f);
		ip_label.getStyle().setFontSize(16F);

		team_label.setVariable(getComponentVariable() + "_team_label");
		team_label.setPosition(110f, 87.5f);
		team_label.getStyle().setFontSize(16F);

		status_label.setVariable(getComponentVariable() + "_status_label");
		status_label.setPosition(110f, 122.5f);
		status_label.getStyle().setFontSize(16F);

		info_container.add(country_label);
		info_container.add(ip_label);
		info_container.add(team_label);
		info_container.add(status_label);

		drawLines();

		top_left_content.add(info_container);
		getContainer().add(top_left_content);

		runAutoUpdateGameElement();
	}

	private MultiLine[] lines = new MultiLine[4];

	private void drawLines() {
		lines[0] = new MultiLine(3, new Vector2f(0, 75), new Vector2f(25, 75), new Vector2f(75, 20),
				new Vector2f(100, 20));
		lines[1] = new MultiLine(3, new Vector2f(0, 75), new Vector2f(25, 75), new Vector2f(75, 55),
				new Vector2f(100, 55));
		lines[2] = new MultiLine(3, new Vector2f(0, 75), new Vector2f(25, 75), new Vector2f(75, 95),
				new Vector2f(100, 95));
		lines[3] = new MultiLine(3, new Vector2f(0, 75), new Vector2f(25, 75), new Vector2f(75, 130),
				new Vector2f(100, 130));

		for (MultiLine multiLine : lines) {
			multiLine.addLines(info_container);
		}
	}

	private Panel firewall_content = new Panel();
	private Panel teams_content = new Panel();

	private Panel left_bottom_content = new Panel();
	private Panel right_bottom_content = new Panel();

	private Panel bottom_content = new Panel();

	private Label firewall_label = new Label();
	private Label red_label = new Label();
	private Label blue_label = new Label();

	private ProgressBar firewall_progressbar = new ProgressBar();
	private ProgressBar red_progressbar = new ProgressBar();
	private ProgressBar blue_progressbar = new ProgressBar();

	private Label firewall_points_label = new Label();
	private Label red_points_label = new Label();
	private Label blue_points_label = new Label();

	private Button connection_button;

	private void initBottom() {
		bottom_content.getStyle().setDisplay(DisplayType.FLEX);
		bottom_content.getStyle().setRight(1f);
		bottom_content.getStyle().setLeft(1f);
		bottom_content.getStyle().setBottom(1f);
		bottom_content.getStyle().setHeight(60f);
		bottom_content.getStyle().setBorder(null);
		bottom_content.getStyle().getBackground().setColor(0, 0, 0, 0);
		bottom_content.getStyle().setBorder(new SimpleLineBorder(new Vector4f(1, 1, 1, 1), 1));
		bottom_content.getStyle().setBorderRadius(0F);
		bottom_content.getStyle().setShadow(null);

		left_bottom_content.getStyle().setDisplay(DisplayType.FLEX);
		left_bottom_content.getStyle().setLeft(1f);
		left_bottom_content.getStyle().setBottom(0f);
		left_bottom_content.getStyle().setTop(0f);
		left_bottom_content.getStyle().setRight(150f);
		left_bottom_content.getStyle().getBackground().setColor(0, 0, 0, 0);
		left_bottom_content.getStyle().setBorder(new SimpleLineBorder(new Vector4f(1, 1, 1, 0), 1));
		left_bottom_content.getStyle().setBorderRadius(0F);
		left_bottom_content.getStyle().setShadow(null);

		right_bottom_content.getStyle().setDisplay(DisplayType.FLEX);
		right_bottom_content.getStyle().setWidth(150f);
		right_bottom_content.getStyle().setBottom(0f);
		right_bottom_content.getStyle().setTop(0f);
		right_bottom_content.getStyle().setRight(0f);
		right_bottom_content.getStyle().getBackground().setColor(0, 0, 0, 0);
		right_bottom_content.getStyle().setBorder(new SimpleLineBorder(new Vector4f(1, 1, 1, 0), 1));
		right_bottom_content.getStyle().setBorderRadius(0F);
		right_bottom_content.getStyle().setShadow(null);

		firewall_content.getStyle().setDisplay(DisplayType.FLEX);
		firewall_content.getStyle().setLeft(0f);
		firewall_content.getStyle().setTop(5f);
		firewall_content.getStyle().setRight(0f);
		firewall_content.getStyle().setHeight(20f);
		firewall_content.getStyle().getBackground().setColor(0, 0, 0, 0);
		firewall_content.getStyle().setBorder(new SimpleLineBorder(new Vector4f(1, 1, 1, 0), 1));
		firewall_content.getStyle().setBorderRadius(0F);
		firewall_content.getStyle().setShadow(null);

		teams_content.getStyle().setDisplay(DisplayType.FLEX);
		teams_content.getStyle().setLeft(0f);
		teams_content.getStyle().setRight(0f);
		teams_content.getStyle().setBottom(0f);
		teams_content.getStyle().setTop((float) firewall_content.getStyle().getHeight().get());
		teams_content.getStyle().getBackground().setColor(0, 0, 0, 0);
		teams_content.getStyle().setBorder(new SimpleLineBorder(new Vector4f(1, 1, 1, 0), 1));
		teams_content.getStyle().setBorderRadius(0F);
		teams_content.getStyle().setShadow(null);

		firewall_label.setVariable(getComponentVariable() + "_firewall_label");
		firewall_label.getStyle().setLeft(5f);
		firewall_label.getStyle().setTop(2F);
		firewall_label.getStyle().setFontSize(16F);
		firewall_label.getListenerMap().addListener(LabelWidthChangeEvent.class, new LabelWidthChangeEventListener() {
			@Override
			public void process(LabelWidthChangeEvent event) {
				float width = event.getWidth();
				firewall_label.getStyle().setMinWidth(width);
				firewall_progressbar.getStyle().setLeft(width + 10f);
			}
		});

		red_label.setVariable(getComponentVariable() + "_red_label");
		red_label.getStyle().setLeft(5f);
		red_label.getStyle().setFontSize(16F);
		red_label.getStyle().setBottom(2F);
		red_label.getListenerMap().addListener(LabelWidthChangeEvent.class, new LabelWidthChangeEventListener() {
			@Override
			public void process(LabelWidthChangeEvent event) {
				float width = event.getWidth();
				red_label.getStyle().setMinWidth(width);
				red_progressbar.getStyle().setLeft(width + 10f);
			}
		});

		blue_label.setVariable(getComponentVariable() + "_blue_label");
		blue_label.getStyle().setLeft(5f);
		blue_label.getStyle().setFontSize(16F);
		blue_label.getStyle().setBottom(red_label.getStyle().getFontSize() + 2f);
		blue_label.getListenerMap().addListener(LabelWidthChangeEvent.class, new LabelWidthChangeEventListener() {
			@Override
			public void process(LabelWidthChangeEvent event) {
				float width = event.getWidth();
				blue_label.getStyle().setMinWidth(width);
				blue_progressbar.getStyle().setLeft(width + 10f);
			}
		});

		firewall_progressbar = new ProgressBar();
		firewall_progressbar.setVariable(getComponentVariable() + "_firewall_progressbar");
		firewall_progressbar.getStyle().setTop(7f);
		firewall_progressbar.getStyle().setBottom(7f);
		firewall_progressbar.getStyle().setRight(5f);
		firewall_progressbar.setValue(50f);

		red_progressbar = new ProgressBar();
		red_progressbar.setVariable(getComponentVariable() + "_red_progressbar");
		red_progressbar.getStyle().setBottom(5);
		red_progressbar.getStyle().setHeight(blue_label.getStyle().getFontSize() - 7f);
		red_progressbar.getStyle().setRight(5f);
		red_progressbar.setValue(50f);

		blue_progressbar = new ProgressBar();
		blue_progressbar.setVariable(getComponentVariable() + "_blue_progressbar");
		blue_progressbar.getStyle().setBottom(4f + blue_label.getStyle().getFontSize());
		blue_progressbar.getStyle().setHeight((float) red_progressbar.getStyle().getHeight().get());
		blue_progressbar.getStyle().setRight(5f);
		blue_progressbar.setValue(50f);

		firewall_points_label.setVariable(getComponentVariable() + "_firewall_points_label");
		firewall_points_label.getStyle().setRight(5f);
		firewall_points_label.getStyle().setTop(2F);
		firewall_points_label.getStyle().setFontSize(16F);
		firewall_points_label.getListenerMap().addListener(LabelWidthChangeEvent.class,
				new LabelWidthChangeEventListener() {
					@Override
					public void process(LabelWidthChangeEvent event) {
						float width = event.getWidth();
						firewall_points_label.getStyle().setMinWidth(width);
						firewall_progressbar.getStyle().setRight(width + 10f);
					}
				});

		red_points_label.setVariable(getComponentVariable() + "_red_points_label");
		red_points_label.getStyle().setRight(5f);
		red_points_label.getStyle().setFontSize(16F);
		red_points_label.getStyle().setBottom(2f);
		red_points_label.getListenerMap().addListener(LabelWidthChangeEvent.class, new LabelWidthChangeEventListener() {
			@Override
			public void process(LabelWidthChangeEvent event) {
				float width = event.getWidth();
				red_points_label.getStyle().setMinWidth(width);
				red_progressbar.getStyle().setRight(width + 10f);
			}
		});

		blue_points_label.setVariable(getComponentVariable() + "_blue_points_label");
		blue_points_label.getStyle().setRight(5f);
		blue_points_label.getStyle().setFontSize(16F);
		blue_points_label.getStyle().setBottom(red_label.getStyle().getFontSize() + 2f);
		blue_points_label.getListenerMap().addListener(LabelWidthChangeEvent.class,
				new LabelWidthChangeEventListener() {
					@Override
					public void process(LabelWidthChangeEvent event) {
						float width = event.getWidth();
						blue_points_label.getStyle().setMinWidth(width);
						blue_progressbar.getStyle().setRight(width + 10f);
					}
				});

		connection_button = new Button();
		connection_button.setVariable(getComponentVariable() + "_connection_button");
		connection_button.getStyle().setFontSize(16f);
		connection_button.getStyle().setTop(10f);
		connection_button.getStyle().setLeft(10f);
		connection_button.getStyle().setRight(10f);
		connection_button.getStyle().setBottom(10f);
		connection_button.getStyle().setShadow(null);
		connection_button.getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
			@Override
			public void process(MouseClickEvent event) {
				if (event.getAction() != MouseClickAction.RELEASE)
					return;
				GameParty party = BackdoorGame.getGameParty();
				if ((BackdoorGame.getGameClient().getTargetAddress() != null)) {
					TerminalApplication.closeApplication();
					party.disconnect();
					BackdoorGame.getGameClient().setTargetAddress(null);
				}
				party.connect(gameElement.getAddress());
			}
		});
		
		
		firewall_content.add(firewall_progressbar);
		firewall_content.add(firewall_label);
		firewall_content.add(firewall_points_label);

		teams_content.add(red_label);
		teams_content.add(red_progressbar);
		teams_content.add(red_points_label);

		teams_content.add(blue_label);
		teams_content.add(blue_progressbar);
		teams_content.add(blue_points_label);

		left_bottom_content.add(firewall_content);
		left_bottom_content.add(teams_content);

		right_bottom_content.add(connection_button);

		bottom_content.add(left_bottom_content);
		bottom_content.add(right_bottom_content);

		getContainer().add(bottom_content);
	}

	@Override
	public void load() {
		getIcon().setImage(BackdoorGame.getDatapack().getImage("window_icon_dashboard"));

		firewall_label.getTextState().setText(Lang.get("dashboard_firewall"));
		red_label.getTextState().setText(Lang.get("dashboard_red"));
		blue_label.getTextState().setText(Lang.get("dashboard_blue"));

		firewall_points_label.getTextState().setText("20/20");
		red_points_label.getTextState().setText("20/20");
		blue_points_label.getTextState().setText("20/20");

		connection_button.getTextState().setText(Lang.get("dashboard_connection_button"));

		firewall_label.load();
		red_label.load();
		blue_label.load();

		firewall_progressbar.load();
		red_progressbar.load();
		blue_progressbar.load();

		ip_label.load();
		status_label.load();
		country_label.load();
		team_label.load();

		firewall_points_label.load();
		red_points_label.load();
		blue_points_label.load();

		connection_button.load();
		if (gameElementComponent != null)
			gameElementComponent.load();

		super.load();
	}

	private void deleteOldUpdater() {
		BackdoorGame.updates.remove(updater);
	}
	
	private IUpdate updater;

	private void runAutoUpdateGameElement() {
		updater = new IUpdate() {
			long start = System.currentTimeMillis();

			boolean b2 = false;
			boolean b = false;

			@Override
			public void update() {
				if (BackdoorGame.getGameParty() == null)
					return;
				if (gameElement == null)
					return;
				if (System.currentTimeMillis() - start > 200) {
					team_label.getTextState()
							.setText(Lang.get("dashboard_team", "%value%",
									Lang.get(gameElement.getTeam() == null ? "dashboard_any_team_set"
											: gameElement.getTeam().getPath())));
					ip_label.getTextState().setText(Lang.get("dashboard_ip", "%value%", gameElement.getAddress()));
					country_label.getTextState().setText(Lang.get("dashboard_country", "%value%",
							"[" + gameElement.getCountry().getCode() + "] " + gameElement.getCountry().getName()));
					status_label.getTextState().setText(
							Lang.get("dashboard_status", "%value%", Lang.get(gameElement.getStatus().getLangPath())));

					blue_points_label.getTextState()
							.setText(gameElement.getTeamPoint(Team.BLUE) + "/" + gameElement.getMaxPoint());
					red_points_label.getTextState()
							.setText(gameElement.getTeamPoint(Team.RED) + "/" + gameElement.getMaxPoint());
					firewall_points_label.getTextState()
							.setText(gameElement.getFirewall() + "/" + gameElement.getFirewallMax());

					int blue_pourcentage = gameElement.getTeamPoint(Team.BLUE) != 0
							? ((gameElement.getTeamPoint(Team.BLUE) * 100) / gameElement.getMaxPoint())
							: 0;

					int red_pourcentage = gameElement.getTeamPoint(Team.RED) != 0
							? ((gameElement.getTeamPoint(Team.RED) * 100) / gameElement.getMaxPoint())
							: 0;

					int firewall_pourcentage = gameElement.getFirewall() != 0
							? ((gameElement.getFirewall() * 100) / gameElement.getFirewallMax())
							: 0;
							
					Player player = BackdoorGame.getGameClient();
							
					if(gameElement.getConnectPrice() > player.getMoney() || gameElement.isOffline() || gameElement.getTeam() == player.getTeam() && !gameElement.isLinked() || player.getTeam() != gameElement.getTeam() && gameElement.isProtected())
						connection_button.setEnabled(false);
					else
						connection_button.setEnabled(true); 
					
					if (blue_progressbar.getValue() != blue_pourcentage)
						blue_progressbar.setValue(blue_pourcentage);

					if (red_progressbar.getValue() != red_pourcentage)
						red_progressbar.setValue(red_pourcentage);

					if (firewall_progressbar.getValue() != firewall_pourcentage)
						firewall_progressbar.setValue(firewall_pourcentage);

					red_progressbar.setProgressColor(NetworkApplication.red_color);
					blue_progressbar.setProgressColor(NetworkApplication.blue_color);

					b = !b;
					if (b) {
						b2 = !b2;
					}

					// NodeEntity entity = ((NodeComponentEntity) gameElementComponent).getNode();
					if (gameElement != BackdoorGame.getGameParty().getMapData().getElements()
							.get(gameElement.getName())) {
						gameElementComponent.setGameElement(
								BackdoorGame.getGameParty().getMapData().getElements().get(gameElement.getName()));
						gameElement = gameElementComponent.getGameElement();
					}
					if (gameElement.getTeam() == Team.RED) {
						gameElementComponent.getBackgroundColor().set(NetworkApplication.red_color);
					} else if (gameElement.getTeam() == Team.BLUE) {
						gameElementComponent.getBackgroundColor().set(NetworkApplication.blue_color);
					} else if (gameElement.getTeam() == null) {
						gameElementComponent.getBackgroundColor().set(NetworkApplication.grey_color);
					}

					if (gameElement.getType() == GameElementType.NODE) {
						if (gameElement.isTeamConnected(Team.BLUE) && gameElement.isTeamConnected(Team.RED)) {
							if (b2) {
								gameElementComponent.getLineColor().set(NetworkApplication.blue_color);
							} else {
								gameElementComponent.getLineColor().set(NetworkApplication.red_color);
							}
						} else {
							if (b) {
								if (gameElement.isTeamConnected(Team.BLUE) && !gameElement.isTeamConnected(Team.RED)) {
									gameElementComponent.getLineColor().set(NetworkApplication.blue_color);
								} else if (gameElement.isTeamConnected(Team.RED)
										&& !gameElement.isTeamConnected(Team.BLUE)) {
									gameElementComponent.getLineColor().set(NetworkApplication.red_color);
								} else {
									gameElementComponent.getLineColor().set(NetworkApplication.white_color);
								}
							} else {
								if (gameElementComponent.getGameElement().getTeam() != null
										&& !gameElementComponent.getGameElement().isLinked()) {
									gameElementComponent.getLineColor().set(NetworkApplication.alert_color);
								} else {
									gameElementComponent.getLineColor().set(NetworkApplication.white_color);
								}
							}
						}
					} else if (gameElement.getType() == GameElementType.SERVER) {
						if (b) {
							if (gameElement.getTeam() == Team.RED && gameElement.isTeamConnected(Team.BLUE)) {
								gameElementComponent.getLineColor().set(NetworkApplication.blue_color);
							} else if (gameElement.getTeam() == Team.BLUE && gameElement.isTeamConnected(Team.RED)) {
								gameElementComponent.getLineColor().set(NetworkApplication.red_color);
							} else {
								gameElementComponent.getLineColor().set(NetworkApplication.white_color);
							}
						} else {
							gameElementComponent.getLineColor().set(NetworkApplication.white_color);
						}
					}
					start = System.currentTimeMillis();
				}
			}
		};
		BackdoorGame.updates.add(updater);
	}

}
