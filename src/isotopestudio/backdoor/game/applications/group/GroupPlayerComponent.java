package isotopestudio.backdoor.game.applications.group;

import org.joml.Vector4f;
import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.component.ImageView;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.event.MouseClickEvent.MouseClickAction;
import org.liquidengine.legui.input.Mouse.MouseButton;
import org.liquidengine.legui.intersection.RectangleIntersector;
import org.liquidengine.legui.listener.MouseClickEventListener;
import org.liquidengine.legui.style.Background;
import org.liquidengine.legui.style.Style.DisplayType;
import org.liquidengine.legui.style.border.SimpleLineBorder;
import org.liquidengine.legui.style.color.ColorConstants;

import doryanbessiere.isotopestudio.api.profile.Profile;
import doryanbessiere.isotopestudio.commons.lang.Lang;
import isotopestudio.backdoor.engine.components.desktop.Label;
import isotopestudio.backdoor.engine.components.desktop.popup.PopupMenu;
import isotopestudio.backdoor.engine.components.events.LabelSizeEvent;
import isotopestudio.backdoor.engine.components.painting.Line;
import isotopestudio.backdoor.engine.datapack.DataParameters;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.manager.GroupManager;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class GroupPlayerComponent extends Panel {

	private Profile profile;

	private ImageView profile_img = new ImageView();
	private Label profile_name = new Label();

	private Line status;

	public GroupPlayerComponent(GroupApplication groupApplication, Profile profile) {
		this.profile = profile;

		getStyle().setRight(10f);
		getStyle().setLeft(10f);
		getStyle().setHeight(60);
		getStyle().setBorderRadius(0f);
		getStyle().setBorder(null);
		getStyle().setShadow(null);
		getStyle().setDisplay(DisplayType.FLEX);
		getStyle().setBorderRadius(0f);
		getStyle().setBorder(null);
		getStyle().setShadow(null);

		float[] normal = DataParameters.getColor(groupApplication, "player_panel_background_color");
		getStyle().getBackground().setColor(new Vector4f(normal[0], normal[1], normal[2], normal[3]));

		float[] hovered = DataParameters.getColor(groupApplication, "player_panel_background_color_hovered");
		getHoveredStyle().setBackground(new Background());
		getHoveredStyle().getBackground().setColor(new Vector4f(hovered[0], hovered[1], hovered[2], hovered[3]));

		float[] focused = DataParameters.getColor(groupApplication, "player_panel_background_color_focused");
		getFocusedStyle().setBackground(new Background());
		getFocusedStyle().getBackground().setColor(new Vector4f(focused[0], focused[1], focused[2], focused[3]));

		float[] pressed = DataParameters.getColor(groupApplication, "player_panel_background_color_pressed");
		getPressedStyle().setBackground(new Background());
		getPressedStyle().getBackground().setColor(new Vector4f(pressed[0], pressed[1], pressed[2], pressed[3]));

		profile_img.getStyle().setLeft(10);
		profile_img.getStyle().setTop(10);
		profile_img.getStyle().setWidth(40);
		profile_img.getStyle().setHeight(40);
		profile_img.getStyle().setBorder(new SimpleLineBorder(new Vector4f(1, 1, 1, 1), 2));
		profile_img.getStyle().setFocusedStrokeColor(ColorConstants.transparent());
		profile_img.getStyle().getBackground().setColor(new Vector4f(0, 0, 0, 1f));
		profile_img.getStyle().setBorderRadius(groupApplication.getSize().x);
		profile_img.getStyle().setShadow(null);
		profile_img.setIntersector(new RectangleIntersector());
		profile_img.setImage(BackdoorGame.loadImageURL(profile.getProfilePictureURL()));

		// ready = 59CE27
		// unready = c02739
		// unready = CC1E1E

		profile_name.setVariable(groupApplication.getComponentVariable() + "_username_label");
		profile_name.getStyle().setTop(10);
		profile_name.getStyle().setLeft(60);
		profile_name.autoSizeWidth();

		status = new Line(6, 0, 0, 0, 60);

		add(profile_img);
		add(profile_name);
		add(status);

		update();

		for (Component childrenComponent : getChildComponents()) {
			childrenComponent.setFocusable(false);
		}

		getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
			@Override
			public void process(MouseClickEvent event) {
				if (event.getAction() == MouseClickAction.RELEASE && event.getButton() == MouseButton.MOUSE_BUTTON_2) {

					if (!GroupManager.getGroup().getOwner().getUuidString()
							.equals(BackdoorGame.getUser().getUUIDString()))
						return;
					PopupMenu popupmenu = new PopupMenu((int) event.getAbsolutePosition().x,
							(int) event.getAbsolutePosition().y);
					popupmenu.setVariable("group_popupmenu");
					popupmenu.setSize(50, 10);
					popupmenu.getStyle().setBorderRadius(0f);

					Label profile = new Label(Lang.get("group_window_profile"), new LabelSizeEvent() {
						@Override
						public void process(float width, float height) {
							popupmenu.initSize();
						}
					});
					profile.getStyle().setPadding(10f);

					MouseClickEventListener profile_click = new MouseClickEventListener() {
						@Override
						public void process(MouseClickEvent event) {
							if (event.getAction() != MouseClickAction.RELEASE)
								return;

						}
					};
					profile.getListenerMap().addListener(MouseClickEvent.class, profile_click);

					Label kick = new Label(Lang.get("group_window_kick"), new LabelSizeEvent() {
						@Override
						public void process(float width, float height) {
							popupmenu.initSize();
						}
					});
					kick.getStyle().setPadding(10f);
					MouseClickEventListener kick_click = new MouseClickEventListener() {
						@Override
						public void process(MouseClickEvent event) {
							if (event.getAction() != MouseClickAction.RELEASE)
								return;

							if (GroupManager.getGroup() == null)
								GroupApplication.showApplication();
							GroupManager.kick(getProfile().getUuidString());

						}
					};
					kick.getListenerMap().addListener(MouseClickEvent.class, kick_click);

					popupmenu.add(profile);
					popupmenu.add(kick);
					popupmenu.load();

					popupmenu.setFocused(true);

					BackdoorGame.getDesktop().add(popupmenu);
				}
			}
		});
	}

	/**
	 * @return the status
	 */
	public Line getStatus() {
		return status;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public Profile getProfile() {
		return profile;
	}

	private void update() {
		profile_name.getTextState().setText(profile.getUsername());
		profile_name.load();
	}
}
