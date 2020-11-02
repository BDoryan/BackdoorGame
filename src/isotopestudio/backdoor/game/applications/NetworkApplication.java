package isotopestudio.backdoor.game.applications;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.joml.Vector4f;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.event.MouseClickEvent.MouseClickAction;
import org.liquidengine.legui.listener.MouseClickEventListener;
import org.liquidengine.legui.style.color.ColorConstants;

import doryanbessiere.isotopestudio.commons.ColorConvertor;
import doryanbessiere.isotopestudio.commons.GsonInstance;
import isotopestudio.backdoor.core.elements.GameElement;
import isotopestudio.backdoor.core.elements.GameElementLink;
import isotopestudio.backdoor.core.elements.GameElementType;
import isotopestudio.backdoor.core.team.Team;
import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.components.desktop.window.Window;
import isotopestudio.backdoor.engine.components.painting.Line;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.BackdoorGame.IUpdate;
import isotopestudio.backdoor.game.game.GameParty;
import isotopestudio.backdoor.game.game.component.GameElementComponent;
import isotopestudio.backdoor.game.game.component.NodeGameElementComponent;
import isotopestudio.backdoor.game.game.component.ServerGameElementComponent;
import isotopestudio.backdoor.utils.ColorConvert;

public class NetworkApplication extends Window implements IComponent {

	public static NetworkApplication main;

	public static NetworkApplication show(GameParty gameParty) {
		if (main == null) {
			NetworkApplication map_information = new NetworkApplication(gameParty);
			BackdoorGame.getDesktop().addWindow(map_information);
			main = map_information;
			map_information.centerLocation();
			map_information.load();
			return map_information;
		} else {
			if (BackdoorGame.getDesktop().containsWindow(main)) {
				main.load();
				main.showWindow();
				return main;
			} else {
				NetworkApplication map_information = new NetworkApplication(gameParty);
				BackdoorGame.getDesktop().addWindow(map_information);
				map_information.centerLocation();
				main = map_information;
				map_information.load();
				return map_information;
			}
		}
	}

	public HashMap<String, GameElementComponent> entities = new HashMap<String, GameElementComponent>();

	public static Vector4f grey_color = ColorConvert.convertToVector4f(ColorConvertor.hex("#808B96"));
	public static Vector4f red_color = ColorConvert.convertToVector4f(ColorConvertor.get("#E74C3C"));
	public static Vector4f blue_color = ColorConvert.convertToVector4f(ColorConvertor.get("#2980B9"));
	public static Vector4f white_color = ColorConvert.convertToVector4f(ColorConvertor.get("#FFFFFF"));
	public static Vector4f alert_color = ColorConvert.convertToVector4f(ColorConvertor.get("#f4e04d"));
	public static Vector4f hover_color = ColorConvert.convertToVector4f(new float[] { 0, 0, 0, 0.2f });

	private GameParty gameParty;

	public ServerGameElementComponent red_server;
	public ServerGameElementComponent blue_server;

	public static GameElement game_element_targeted;
	
	public static GameElement getGameElementTargeted() {
		return game_element_targeted;
	}

	public NetworkApplication(GameParty gameParty) {
		super("Network", 0, 0, 650, 320);

		this.gameParty = gameParty;

		System.out.println("Map Data: " + GsonInstance.instance().toJson(gameParty.getMapData()));

		int top = 20;
		
		red_server = createServer("red_server", Team.RED, 50, top+ 80);
		red_server.getBackgroundColor().set(red_color);

		blue_server = createServer("blue_server", Team.BLUE, 550, top+80);
		blue_server.getBackgroundColor().set(blue_color);

		createNode("node1", 50, top + 20);
		createNode("node2", 50, (int)( top + red_server.getPosition().y + red_server.getSize().y + 10));

		createNode("node3", 250, top + 20);
		createNode("node4", 260, top + 95);
		createNode("node5", 250, top + 165);

		createNode("node6", 350, top + 20);
		createNode("node7", 340, top + 95);
		createNode("node8", 350, top + 165);

		createNode("node9", 550 + 30, top + 20);
		createNode("node10", 550 + 30, (int)( top + blue_server.getPosition().y + blue_server.getSize().y + 10));

		drawLines(red_server);

		BackdoorGame.updates.add(new IUpdate() {
			long start = System.currentTimeMillis();

			boolean b2 = false;
			boolean b = false;

			@Override
			public void update() {
				if (BackdoorGame.getGameParty() == null)
					return;
				if (System.currentTimeMillis() - start > 200) {
					b = !b;
					if (b) {
						b2 = !b2;
					}
					for (Entry<String, GameElementComponent> entry : entities.entrySet()) {
						GameElementComponent entity_component = entry.getValue();

						// NodeEntity entity = ((NodeComponentEntity) entity_component).getNode();
						GameElement gameElement = entity_component.getGameElement();
						if (gameElement != getGameParty().getMapData().getElements().get(gameElement.getName())) {
							entity_component.setGameElement(
									getGameParty().getMapData().getElements().get(gameElement.getName()));
							gameElement = entity_component.getGameElement();
						}
						if (gameElement.getTeam() == Team.RED) {
							entity_component.getBackgroundColor().set(red_color);
						} else if (gameElement.getTeam() == Team.BLUE) {
							entity_component.getBackgroundColor().set(blue_color);
						} else if (gameElement.getTeam() == null) {
							entity_component.getBackgroundColor().set(grey_color);
						}

						if (gameElement.getType() == GameElementType.NODE) {
							if (gameElement.isTeamConnected(Team.BLUE) && gameElement.isTeamConnected(Team.RED)) {
								if (b2) {
									entity_component.getLineColor().set(blue_color);
								} else {
									entity_component.getLineColor().set(red_color);
								}
							} else {
								if (b) {
									if (gameElement.isTeamConnected(Team.BLUE)
											&& !gameElement.isTeamConnected(Team.RED)) {
										entity_component.getLineColor().set(blue_color);
									} else if (gameElement.isTeamConnected(Team.RED)
											&& !gameElement.isTeamConnected(Team.BLUE)) {
										entity_component.getLineColor().set(red_color);
									} else {
											entity_component.getLineColor().set(white_color);	
									}
								} else {
									if(entity_component.getGameElement().getTeam() != null && !entity_component.getGameElement().isLinked()) {
										entity_component.getLineColor().set(alert_color);	
									} else {
										entity_component.getLineColor().set(white_color);	
									}
								}
							}
						} else if (gameElement.getType() == GameElementType.SERVER) {
							if (b) {
								if (gameElement.getTeam() == Team.RED && gameElement.isTeamConnected(Team.BLUE)) {
									entity_component.getLineColor().set(blue_color);
								} else if (gameElement.getTeam() == Team.BLUE && gameElement.isTeamConnected(Team.RED)) {
									entity_component.getLineColor().set(red_color);
								} else {
									entity_component.getLineColor().set(white_color);
								}
							} else {
								entity_component.getLineColor().set(white_color);
							}
						}
					}
					start = System.currentTimeMillis();
				}
			}
		});
	}

	public HashMap<String, GameElementComponent> getEntities() {
		return entities;
	}

	public GameParty getGameParty() {
		return gameParty;
	}

	@Override
	public void load() {
		getIcon().setImage(BackdoorGame.getDatapack().getImage("window_icon_network"));
		super.load();

		for (GameElementComponent node : entities.values()) {
			node.load();
		}
	}

	private ArrayList<Line> lines = new ArrayList<>();

	public void drawLines(GameElementComponent from) {
		drawLinesComponent(from);
	}

	private ArrayList<GameElementLink> alreadyScanned = new ArrayList<GameElementLink>();

	private void drawLinesComponent(GameElementComponent to) {
		for (GameElementLink link : to.getGameElement().getLinks()) {
			if (alreadyScanned.contains(link)) {
				// Allows to prevent the previous entity from going to the previous entity
				continue;
			}
			GameElementComponent entity = getEntityComponent(link.getTo());
			GameElementComponent from_ = getEntityComponent(link.getFrom());
			alreadyScanned.add(link);
			Line line = new Line(1, (int) (entity.getPosition().x + (entity.getSize().x / 2)),
					(int) (entity.getPosition().y + (entity.getSize().y / 2)),
					(int) (from_.getPosition().x + (from_.getSize().x / 2)),
					(int) (from_.getPosition().y + (from_.getSize().y / 2)));
			line.setColor(ColorConstants.white());
			line.setThinkness(2);
			lines.add(line);

			// simple add line
			getContainer().add(line);

			/* to display the elements over the line */
			getContainer().remove(from_);
			getContainer().remove(entity);

			getContainer().add(from_);
			getContainer().add(entity);
			/* and we draw for the others */
			drawLinesComponent(entity);
		}
	}

	public GameElementComponent getEntityComponent(String uuid) {
		return entitiesUUID.get(uuid);
	}

	private HashMap<String, GameElementComponent> entitiesUUID = new HashMap<String, GameElementComponent>();

	private NodeGameElementComponent createNode(String name, int x, int y) {
		System.out.println(
				name + " -> " + GsonInstance.instance().toJson(this.gameParty.getMapData().getElements().get(name)));
		NodeGameElementComponent node = new NodeGameElementComponent(x, y, 30, 2, 5,
				this.gameParty.getMapData().getElements().get(name));
		node.getBackgroundColor().set(grey_color);
		node.getLineColor().set(white_color);
		node.getBackgroundColorHovered().set(hover_color);
		node.load();

		node.getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
			@Override
			public void process(MouseClickEvent event) {
				if (event.getAction() == MouseClickAction.RELEASE) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					game_element_targeted = node.getGameElement();
					DashboardApplication.showApplication(node.getGameElement());
				}
			}
		});

		getContainer().add(node);

		this.entities.put(name, node);
		this.entitiesUUID.put(node.getGameElement().getUUID().toString(), node);
		return node;
	}

	private ServerGameElementComponent createServer(String name, Team team, int x, int y) {
		System.out.println(
				name + " -> " + GsonInstance.instance().toJson(this.gameParty.getMapData().getElements().get(name)));
		ServerGameElementComponent server = new ServerGameElementComponent(x, y, 60, 2, 10,
				this.gameParty.getMapData().getElements().get(name));
		server.getBackgroundColor().set(grey_color);
		server.getLineColor().set(white_color);
		server.getBackgroundColorHovered().set(hover_color);
		server.load();

		server.getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
			@Override
			public void process(MouseClickEvent event) {
				if (event.getAction() == MouseClickAction.RELEASE) {
					game_element_targeted = server.getGameElement();
					DashboardApplication.showApplication(server.getGameElement());
				}
			}
		});

		getContainer().add(server);
		this.entities.put(name, server);
		this.entitiesUUID.put(server.getGameElement().getUUID().toString(), server);
		return server;
	}
}
