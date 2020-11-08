package isotopestudio.backdoor.game.applications.group;

import org.joml.Vector4f;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.component.event.button.ButtonWidthChangeEvent;
import org.liquidengine.legui.component.misc.listener.button.UpdateButtonStyleWidthListener;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.listener.MouseClickEventListener;
import org.liquidengine.legui.style.Style.DisplayType;

import doryanbessiere.isotopestudio.commons.lang.Lang;
import isotopestudio.backdoor.core.gamemode.GameMode;
import isotopestudio.backdoor.core.versus.Versus;
import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.components.desktop.Button;
import isotopestudio.backdoor.engine.components.desktop.SelectBox;
import isotopestudio.backdoor.engine.components.desktop.window.Window;
import isotopestudio.backdoor.engine.datapack.DataParameters;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.event.EventListener;
import isotopestudio.backdoor.game.event.EventManager;
import isotopestudio.backdoor.game.event.events.GroupUpdateEvent;
import isotopestudio.backdoor.game.manager.GroupManager;
import isotopestudio.backdoor.gateway.group.GroupObject;
import isotopestudio.backdoor.gateway.packet.packets.group.PacketGroupCreate;

public class GroupApplication extends Window implements IComponent {

	public static GroupApplication main;

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
				updateGroup();
			}
		}
	};

	private SelectBox<String> gamemodes = new SelectBox<>();
	private SelectBox<String> versus = new SelectBox<>();

	private Button leave_button = new Button();
	private Button start_button = new Button();

	private Panel top_panel = new Panel();
	private Panel bottom_panel = new Panel();

	public GroupApplication() {
		super("Group", 0, 0, 300, 500);
		setVariable("group_window");

		EventManager.addListener(GroupUpdateEvent.class, listener);

		if (GroupManager.getGroup() == null) {
			BackdoorGame.getGateway().sendPacket(new PacketGroupCreate());
		}

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
		bottom_panel.getStyle().setHeight(80);

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
		versus.getStyle().setTop(40);
		versus.getStyle().setLeft(10);
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
				if (GroupManager.getGroup().getOwner().getUuidString().equals(BackdoorGame.getUser().getUUIDString())) {
				} else {
				}
			}
		});

		top_panel.add(gamemodes);
		top_panel.add(versus);

		getContainer().add(top_panel);

		for (Button button : new Button[] { start_button, leave_button }) {
			button.getListenerMap().addListener(ButtonWidthChangeEvent.class,
					(event) -> button.getStyle().setWidth(event.getWidth() + 20));
		}

		bottom_panel.add(leave_button);
		bottom_panel.add(start_button);

		getContainer().add(bottom_panel);

		load();
	}

	private void initGroup(GroupObject group) {
		if (group.getOwner().getUuidString().equals(BackdoorGame.getUser().getUUIDString())) {
			leave_button.getTextState().setText(Lang.get("group_delete_group"));
		} else {
			leave_button.getTextState().setText(Lang.get("group_leave_group"));
		}
	}

	private void updateGroup() {

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
