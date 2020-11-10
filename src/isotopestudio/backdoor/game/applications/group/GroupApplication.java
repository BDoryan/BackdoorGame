package isotopestudio.backdoor.game.applications.group;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.component.event.button.ButtonWidthChangeEvent;
import org.liquidengine.legui.component.event.checkbox.CheckBoxChangeValueEvent;
import org.liquidengine.legui.component.event.checkbox.CheckBoxChangeValueEventListener;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.event.MouseClickEvent.MouseClickAction;
import org.liquidengine.legui.listener.MouseClickEventListener;
import org.liquidengine.legui.style.Style.DisplayType;

import doryanbessiere.isotopestudio.api.profile.Profile;
import doryanbessiere.isotopestudio.commons.ColorConvertor;
import doryanbessiere.isotopestudio.commons.lang.Lang;
import isotopestudio.backdoor.core.gamemode.GameMode;
import isotopestudio.backdoor.core.versus.Versus;
import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.components.desktop.Button;
import isotopestudio.backdoor.engine.components.desktop.SelectBox;
import isotopestudio.backdoor.engine.components.desktop.checkbox.CheckBox;
import isotopestudio.backdoor.engine.components.desktop.scrollablepanel.ScrollablePanel;
import isotopestudio.backdoor.engine.components.desktop.window.Window;
import isotopestudio.backdoor.engine.components.events.CheckBoxSizeEvent;
import isotopestudio.backdoor.engine.datapack.DataParameters;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.event.EventListener;
import isotopestudio.backdoor.game.event.EventManager;
import isotopestudio.backdoor.game.event.events.GroupUpdateEvent;
import isotopestudio.backdoor.game.manager.GroupManager;
import isotopestudio.backdoor.gateway.group.GroupObject;
import isotopestudio.backdoor.gateway.packet.packets.group.PacketGroupChangePrivate;
import isotopestudio.backdoor.gateway.packet.packets.group.PacketGroupCreate;
import isotopestudio.backdoor.utils.ColorConvert;

public class GroupApplication extends Window implements IComponent {

	public static GroupApplication main;

	/**
	 * @return
	 */
	public static boolean isOpen() {
		return main != null;
	}

	public static void showApplication() {
		if (main == null) {
			main = new GroupApplication();
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

	private EventListener<GroupUpdateEvent> listener = new EventListener<GroupUpdateEvent>() {
		@Override
		public void process(GroupUpdateEvent event) {
			if (event.getGroupObject() == null) {
				closeWindow();
				return;
			}

			if (!(event.getGroupObject() != null && GroupManager.getGroup() != null)
					|| (event.getGroupObject() != null && GroupManager.getGroup() != null && !event.getGroupObject()
							.getGroupUUID().toString().equals(GroupManager.getGroup().getGroupUUID()))) {
				initGroup(event.getGroupObject());
			} else {
				updateGroup(event.getGroupObject());
			}
		}
	};

	private SelectBox<String> gamemodes = new SelectBox<>();
	private SelectBox<String> versus = new SelectBox<>();

	private Button leave_button = new Button();
	private Button start_button = new Button();

	private CheckBox private_checkbox = new CheckBox();

	private Panel top_panel = new Panel();
	private Panel bottom_panel = new Panel();

	private ScrollablePanel players_panel = new ScrollablePanel();

	int players_list_off_y = 0;

	public GroupApplication() {
		super("Group", 0, 0, 400, 500);
		setVariable("group_window");

		EventManager.addListener(GroupUpdateEvent.class, listener);

		for (GameMode gameMode : GameMode.values())
			gamemodes.addElement(
					gameMode.toString().substring(0, 1).toUpperCase() + gameMode.toString().substring(1).toLowerCase());

		for (Versus vs : Versus.values())
			versus.addElement(vs.getText());

		getContainer().getStyle().setDisplay(DisplayType.FLEX);

		top_panel.getStyle().setTop(0f);
		top_panel.getStyle().setRight(0f);
		top_panel.getStyle().setLeft(0f);
		top_panel.getStyle().setHeight(70);

		bottom_panel.getStyle().setBottom(0f);
		bottom_panel.getStyle().setRight(0f);
		bottom_panel.getStyle().setLeft(0f);
		bottom_panel.getStyle().setHeight(70);

		players_panel.getStyle().setRight(0);
		players_panel.getStyle().setLeft(0);
		players_panel.getStyle().setBottom((float) bottom_panel.getStyle().getHeight().get());
		players_panel.getStyle().setTop((float) top_panel.getStyle().getHeight().get());

		players_panel.getContainer().getStyle().setDisplay(DisplayType.FLEX);
		players_panel.getContainer().getStyle().setLeft(0f);
		players_panel.getContainer().getStyle().setRight(0f);
		players_panel.getContainer().getStyle().setBorderRadius(0f);
		players_panel.getContainer().getStyle().setBottom((float) bottom_panel.getStyle().getHeight().get());
		players_panel.getContainer().getStyle().setTop((float) top_panel.getStyle().getHeight().get());

		players_panel.getViewport().getStyle().setBorderRadius(0f);

		players_panel.setAutoResize(true);

		players_panel.setHorizontalScrollBarVisible(false);

		players_panel.getViewport().getStyle().setShadow(null);
		players_panel.getContainer().getStyle().setShadow(null);
		players_panel.getStyle().setShadow(null);

		players_panel.getContainer().setFocusable(false);
		players_panel.setFocusable(false);
		players_panel.getViewport().setFocusable(false);

		for (Panel panel : new Panel[] { top_panel, bottom_panel }) {
			panel.getStyle().setBorderRadius(0f);
			panel.getStyle().setBorder(null);
			panel.getStyle().setShadow(null);
			panel.setFocusable(false);
			panel.getStyle().setDisplay(DisplayType.FLEX);
		}

		gamemodes.setVariable(getComponentVariable() + "_gamemodes_selectbox");
		gamemodes.getStyle().setTop(10);
		gamemodes.getStyle().setLeft(10);
		gamemodes.getStyle().setWidth(100);
		gamemodes.getStyle().setHeight(20);

		versus.setVariable(getComponentVariable() + "_versus_selectbox");
		versus.getStyle().setTop(10);
		versus.getStyle().setRight(10);
		versus.getStyle().setWidth(100);
		versus.getStyle().setHeight(20);

		leave_button.setVariable(getComponentVariable() + "_leave_button");
		leave_button.getStyle().setFontSize(16f);
		leave_button.getStyle().setBottom(10f);
		leave_button.getStyle().setLeft(10f);
		leave_button.getStyle().setHeight(20f);
		leave_button.getStyle().setShadow(null);
		leave_button.getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
			@Override
			public void process(MouseClickEvent event) {
				if(event.getAction() != MouseClickAction.RELEASE)return;
				
				boolean youAreOwner = GroupManager.getGroup().getOwner().getUuidString().equals(BackdoorGame.getUser().getUUIDString());
				if(youAreOwner) {
					GroupManager.delete();
				} else {
					GroupManager.leave();
				}
			}
		});

		private_checkbox.setVariable(getComponentVariable() + "_private_checkbox");
		private_checkbox.getStyle().setTop(40f);
		private_checkbox.getStyle().setLeft(10f);
		private_checkbox.getStyle().setRight(10f);
		private_checkbox.getStyle().setFontSize(20f);
		private_checkbox.getStyle().setFocusedStrokeColor(0, 0, 0, 0);
		private_checkbox.getIconChecked().setSize(new Vector2f(20, 20));
		private_checkbox.getIconUnchecked().setSize(new Vector2f(20, 20));
		private_checkbox.getListenerMap().addListener(CheckBoxChangeValueEvent.class,
				new CheckBoxChangeValueEventListener() {
					@Override
					public void process(CheckBoxChangeValueEvent event) {
						BackdoorGame.getGateway().sendPacket(new PacketGroupChangePrivate(event.getNewValue()));
					}
				});

		private_checkbox.getListenerMap().addListener(CheckBoxSizeEvent.class,
				new org.liquidengine.legui.listener.EventListener<CheckBoxSizeEvent>() {
					@Override
					public void process(CheckBoxSizeEvent event) {
						private_checkbox.getStyle().setWidth(event.getWidth());
						private_checkbox.getStyle().setHeight(event.getHeight());
					}
				});

		start_button.setVariable(getComponentVariable() + "_start_button");
		start_button.getStyle().setFontSize(16f);
		start_button.getStyle().setBottom(10f);
		start_button.getStyle().setRight(10f);
		start_button.getStyle().setHeight(20f);
		start_button.getStyle().setShadow(null);
		start_button.getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
			@Override
			public void process(MouseClickEvent event) {
				if(event.getAction() != MouseClickAction.RELEASE)return;
				boolean youAreOwner = GroupManager.getGroup().getOwner().getUuidString()
						.equals(BackdoorGame.getUser().getUUIDString());
				if (youAreOwner) {
					GroupManager.start(Versus.fromString(versus.getSelection()), GameMode.fromString(gamemodes.getSelection()));
				} else {
					if (GroupManager.getGroup().isReady(BackdoorGame.getUser().getUUIDString())) {
						GroupManager.unready();
					} else {
						GroupManager.ready();
					}
				}
			}
		});

		top_panel.add(gamemodes);
		top_panel.add(versus);
		top_panel.add(private_checkbox);

		getContainer().add(top_panel);

		getContainer().add(players_panel);

		for (Button button : new Button[] { start_button, leave_button }) {
			button.getListenerMap().addListener(ButtonWidthChangeEvent.class,
					(event) -> button.getStyle().setWidth(event.getWidth() + 20));
		}

		bottom_panel.add(leave_button);
		bottom_panel.add(start_button);

		getContainer().add(bottom_panel);

		if (GroupManager.getGroup() == null) {
			GroupManager.create();
		} else {
			initGroup(GroupManager.getGroup());
		}

		load();
	}

	private void initGroup(GroupObject group) {
		boolean youAreOwner = group.getOwner().getUuidString().equals(BackdoorGame.getUser().getUUIDString());
		private_checkbox.setEnabled(youAreOwner);
		if (youAreOwner) {
			leave_button.getTextState().setText(Lang.get("group_delete_group"));
		} else {
			leave_button.getTextState().setText(Lang.get("group_leave_group"));

			top_panel.remove(gamemodes);
			top_panel.remove(versus);
			top_panel.remove(private_checkbox);
			
			setReadyButton();
		}

		initPlayersList(group);
	}

	private void setReadyButton() {
		start_button.setVariable(getComponentVariable()+"_ready_button");
		start_button.getTextState().setText(Lang.get("group_ready"));
		start_button.load();
	}

	private void setUnreadyButton() {
		start_button.setVariable(getComponentVariable()+"_unready_button");
		start_button.getTextState().setText(Lang.get("group_unready"));
		start_button.load();
	}
	
	private void updateGroup(GroupObject group) {
		if (private_checkbox.isChecked() == GroupManager.privateParty) {
			bottom_panel.remove(start_button);
			if (group.getOwner().getUuidString().equals(BackdoorGame.getUser().getUUIDString())) {
				if (GroupManager.privateParty) {
					start_button.getTextState().setText(Lang.get("group_start_a_party"));
				} else {
					start_button.getTextState().setText(Lang.get("group_search_a_party"));
				}
			} else {
				if (group.isReady(BackdoorGame.getUser().getUUIDString())) {
					setUnreadyButton();
				} else {
					setReadyButton();
				}
			}
			bottom_panel.add(start_button);
		}
		
		updatePlayersList(group);
		
		if(group.getPlayersReady().size() == group.getPlayers().size() - 1) {
			start_button.setEnabled(true);
		} else {
			start_button.setEnabled(false);
		}
		
		if (!group.getOwner().getUuidString().equals(BackdoorGame.getUser().getUUIDString())) {
			if (group.isReady(BackdoorGame.getUser().getUUIDString())) {
				setUnreadyButton();
			} else {
				setReadyButton();
			}
			start_button.load();
		}

		if (group.getPlayers().size() == GroupManager.getGroup().getPlayers().size()) {
			return;
		}
		initPlayersList(group);
	}
	
	private void updatePlayersList(GroupObject group) {
		for(Component childrenComponent : players_panel.getContainer().getChildComponents()) {
			System.out.println(childrenComponent.getClass().getSimpleName()+" - "+(childrenComponent instanceof GroupPlayerComponent));
			if(childrenComponent instanceof GroupPlayerComponent) {
				GroupPlayerComponent groupPlayer = (GroupPlayerComponent) childrenComponent;
				groupPlayer.getStatus().setColor(
						group.getOwner().getUuidString().equals(groupPlayer.getProfile().getUuidString())
						? DataParameters.convertColor(DataParameters.getColor(this, "player_status_leader"))
						: DataParameters.convertColor(DataParameters.getColor(this, "player_status_"+
						(group.isReady(groupPlayer.getProfile().getUuidString()) ? "ready" : "unready"))));
			}
		}
	}

	private void initPlayersList(GroupObject group) {
		players_panel.getContainer().clearChildComponents();
		players_list_off_y = 0;
		for (Profile player : group.getPlayers()) {
			GroupPlayerComponent groupPlayer = new GroupPlayerComponent(this, player);

			players_panel.getContainer().add(groupPlayer);
			groupPlayer.getStyle().setTop(10 + players_list_off_y * (float) groupPlayer.getStyle().getHeight().get());
			players_list_off_y++;

			groupPlayer.getStatus().setColor(
					group.getOwner().getUuidString().equals(player.getUuidString())
					? DataParameters.convertColor(DataParameters.getColor(this, "player_status_leader"))
					: DataParameters.convertColor(DataParameters.getColor(this, "player_status_"+
					(group.isReady(player.getUuidString()) ? "ready" : "unready"))));
		}
	}

	@Override
	public void closeWindow() {
		EventManager.removeListener(GroupUpdateEvent.class, listener);
		super.closeWindow();
	}

	@Override
	public void load() {
		for (SelectBox<String> selectBox : new SelectBox[] { versus, gamemodes }) {
			selectBox.load();
		}
		for (Button button : new Button[] { leave_button, start_button }) {
			button.load();
		}

		players_panel.load();
		
		if (GroupManager.privateParty) {
			start_button.getTextState().setText(Lang.get("group_start_a_party"));
		} else {
			start_button.getTextState().setText(Lang.get("group_search_a_party"));
		}

		private_checkbox.getTextState().setText(Lang.get("group_private_party"));
		private_checkbox.load();

		getIcon().setImage(BackdoorGame.getDatapack().getImage("window_icon_multiplayer"));

		float[] color = DataParameters.getColor(this, "top_panel_background_color");
		top_panel.getStyle().getBackground().setColor(new Vector4f(color[0], color[1], color[2], color[3]));
		top_panel.getHoveredStyle().getBackground().setColor(new Vector4f(color[0], color[1], color[2], color[3]));
		top_panel.getFocusedStyle().getBackground().setColor(new Vector4f(color[0], color[1], color[2], color[3]));
		top_panel.getPressedStyle().getBackground().setColor(new Vector4f(color[0], color[1], color[2], color[3]));

		color = DataParameters.getColor(this, "bottom_panel_background_color");
		bottom_panel.getStyle().getBackground().setColor(new Vector4f(color[0], color[1], color[2], color[3]));
		bottom_panel.getHoveredStyle().getBackground().setColor(new Vector4f(color[0], color[1], color[2], color[3]));
		bottom_panel.getFocusedStyle().getBackground().setColor(new Vector4f(color[0], color[1], color[2], color[3]));
		bottom_panel.getPressedStyle().getBackground().setColor(new Vector4f(color[0], color[1], color[2], color[3]));

		super.load();
	}
}
