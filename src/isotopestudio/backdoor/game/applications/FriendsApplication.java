package isotopestudio.backdoor.game.applications;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.ClientProtocolException;
import org.joml.Vector4f;
import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.component.ImageView;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.component.event.label.LabelWidthChangeEvent;
import org.liquidengine.legui.component.event.label.LabelWidthChangeEventListener;
import org.liquidengine.legui.component.event.textinput.TextInputContentChangeEvent;
import org.liquidengine.legui.component.event.textinput.TextInputContentChangeEventListener;
import org.liquidengine.legui.event.KeyEvent;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.event.MouseClickEvent.MouseClickAction;
import org.liquidengine.legui.input.KeyCode;
import org.liquidengine.legui.input.Keyboard;
import org.liquidengine.legui.input.Mouse.MouseButton;
import org.liquidengine.legui.intersection.RectangleIntersector;
import org.liquidengine.legui.listener.KeyEventListener;
import org.liquidengine.legui.listener.MouseClickEventListener;
import org.liquidengine.legui.style.Background;
import org.liquidengine.legui.style.Style.DisplayType;
import org.liquidengine.legui.style.border.SimpleLineBorder;
import org.liquidengine.legui.style.color.ColorConstants;
import org.lwjgl.glfw.GLFW;

import doryanbessiere.isotopestudio.api.friends.FriendsAPI;
import doryanbessiere.isotopestudio.api.profile.Profile;
import doryanbessiere.isotopestudio.commons.lang.Lang;
import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.components.desktop.Label;
import isotopestudio.backdoor.engine.components.desktop.TextField;
import isotopestudio.backdoor.engine.components.desktop.popup.PopupMenu;
import isotopestudio.backdoor.engine.components.desktop.scrollablepanel.ScrollablePanel;
import isotopestudio.backdoor.engine.components.desktop.window.Window;
import isotopestudio.backdoor.engine.components.events.LabelSizeEvent;
import isotopestudio.backdoor.engine.datapack.DataParameters;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.applications.group.GroupApplication;
import isotopestudio.backdoor.game.manager.GroupManager;
import isotopestudio.backdoor.gateway.packet.packets.group.PacketGroupAccept;
import isotopestudio.backdoor.gateway.packet.packets.group.PacketGroupInvite;

public class FriendsApplication extends Window implements IComponent {

	public static FriendsApplication main;

	public static void showApplication() {
		if (main == null) {
			main = new FriendsApplication();
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

	private Panel top_panel = new Panel();
	private ImageView user_profile = new ImageView();
	private Label username = new Label();
	private Label level = new Label();
	private Label status = new Label();

	private ScrollablePanel friends_panel = new ScrollablePanel();

	private TextField search_textfield = new TextField();

	public FriendsApplication() {
		super(Lang.get("friends_window_title"), 0, 0, 340, 500);
		setVariable("friends_window");

		getContainer().getStyle().setDisplay(DisplayType.FLEX);

		top_panel.getStyle().setTop(0f);
		top_panel.getStyle().setRight(0f);
		top_panel.getStyle().setLeft(0f);
		top_panel.getStyle().setHeight(110);
		top_panel.getStyle().setBorderRadius(0f);
		top_panel.getStyle().setBorder(null);
		top_panel.getStyle().setShadow(null);
		top_panel.setFocusable(false);
		top_panel.getStyle().setDisplay(DisplayType.FLEX);

		user_profile.getStyle().setLeft(15);
		user_profile.getStyle().setTop(15);
		user_profile.getStyle().setWidth(50);
		user_profile.getStyle().setHeight(50);
		user_profile.getStyle().setBorder(new SimpleLineBorder(new Vector4f(1, 1, 1, 1), 3));
		user_profile.getStyle().setFocusedStrokeColor(ColorConstants.transparent());
		user_profile.getStyle().getBackground().setColor(new Vector4f(0, 0, 0, 1f));
		user_profile.getStyle().setBorderRadius(this.getSize().x);
		user_profile.getStyle().setShadow(null);
		user_profile.setIntersector(new RectangleIntersector());

		username.setVariable(getComponentVariable() + "_username_label");
		username.getStyle().setTop(15);
		username.getStyle().setLeft(80);
		username.autoSizeWidth();

		level.setVariable(getComponentVariable() + "_level_label");
		level.getStyle().setTop(35);
		level.autoSizeWidth();

		status.setVariable(getComponentVariable() + "_status_online_label");
		status.getStyle().setTop(35);
		status.getStyle().setLeft(80);
		status.getListenerMap().addListener(LabelWidthChangeEvent.class, new LabelWidthChangeEventListener() {
			@Override
			public void process(LabelWidthChangeEvent event) {
				level.getStyle().setLeft(80 + event.getWidth() + 10);
			}
		});

		search_textfield.getStyle().setRight(10);
		search_textfield.getStyle().setLeft(10);
		search_textfield.getStyle().setHeight(20);
		search_textfield.getStyle().setBottom(10);
		search_textfield.getStyle().setShadow(null);
		search_textfield.getListenerMap().addListener(KeyEvent.class, new KeyEventListener() {
			@Override
			public void process(KeyEvent event) {
				if (!(event.getAction() == GLFW.GLFW_RELEASE))
					return;
				if (event.getKey() == Keyboard.getNativeCode(KeyCode.ENTER)
						|| event.getKey() == Keyboard.getNativeCode(KeyCode.NUMPAD_ENTER)) {
					searchPlayer(search_textfield.getTextState().getText());
				}
			}
		});
		search_textfield.getListenerMap().addListener(TextInputContentChangeEvent.class,
				new TextInputContentChangeEventListener() {
					long last_type = 0l;

					@Override
					public void process(TextInputContentChangeEvent event) {
						if (event.getOldValue().equals(event.getNewValue()))
							return;
						if (event.getNewValue().contains(" ")) {
							int caret_position = search_textfield.getCaretPosition();
							search_textfield.getTextState().setText(event.getOldValue());
							search_textfield.setCaretPosition(
									caret_position > event.getOldValue().length() + 1 ? event.getOldValue().length()
											: caret_position);
							return;
						}
						if (event.getNewValue().equals("") && searching) {
							loadFriendsList();
							searching = false;
							return;
						}

						/*
						 * if(event.getNewValue().equals("")) { loadFriendsList(); searching = false;
						 * return; }
						 * 
						 * last_type = System.currentTimeMillis(); new Timer().schedule(new TimerTask()
						 * {
						 * 
						 * @Override public void run() { System.out.println((System.currentTimeMillis()
						 * - last_type)); if((System.currentTimeMillis() - last_type) >= 200) {
						 * if(event.getNewValue().equals(""))return; searchPlayer(event.getNewValue());
						 * } } }, 200);
						 */
					}
				});
		search_textfield.setVariable(getComponentVariable() + "_search_textfield");

		friends_panel.getStyle().setRight(0);
		friends_panel.getStyle().setLeft(0);
		friends_panel.getStyle().setBottom(0);
		friends_panel.getStyle().setTop((float) top_panel.getStyle().getHeight().get());

		friends_panel.getContainer().getStyle().setDisplay(DisplayType.FLEX);
		friends_panel.getContainer().getStyle().setLeft(0f);
		friends_panel.getContainer().getStyle().setRight(0f);
		friends_panel.getContainer().getStyle().setTop(0f);
		friends_panel.getContainer().getStyle().setBorderRadius(0f);

		friends_panel.getViewport().getStyle().setBorderRadius(0f);

		friends_panel.setAutoResize(true);

		friends_panel.setHorizontalScrollBarVisible(false);

		friends_panel.getViewport().getStyle().setShadow(null);
		friends_panel.getContainer().getStyle().setShadow(null);
		friends_panel.getStyle().setShadow(null);

		friends_panel.getContainer().setFocusable(false);
		friends_panel.setFocusable(false);
		friends_panel.getViewport().setFocusable(false);

		top_panel.add(user_profile);
		top_panel.add(username);
		top_panel.add(level);
		top_panel.add(status);
		top_panel.add(search_textfield);

		getContainer().add(top_panel);
		getContainer().add(friends_panel);

		loadFriendsList();

		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				try {
					if (searching)
						return;

					ArrayList<Profile> friends_ = new FriendsAPI().getListRequest(BackdoorGame.getUser().getEmail(),
							BackdoorGame.getUser().getToken());
					for (FriendComponent friend : friends) {
						for (Profile profile : friends_) {
							if (friend.getProfile().getUuidString().equals(profile.getUuidString())) {
								friend.setProfile(profile);
								friend.update();
								break;
							}
						}
					}

					/*
					 * // refresh friends list ArrayList<FriendComponent> friends_ = new
					 * ArrayList<>(); if(friends.size() > 0) { friends_.addAll(friends); for
					 * (FriendComponent friend : friends_) { friend.setProfile(new
					 * ProfileAPI().getProfile(BackdoorGame.getUser().getEmail(),
					 * BackdoorGame.getUser().getToken(), friend.getProfile().getUuidString()));
					 * friend.update(); } }
					 * 
					 * // refresh request friends list if(friendsRequest.size() < 0 )return;
					 * ArrayList<FriendRequestComponent> friendsRequest_ = new ArrayList<>();
					 * friends_.addAll(friends); for (FriendRequestComponent friend :
					 * friendsRequest_) { friend.setProfile(new
					 * ProfileAPI().getProfile(BackdoorGame.getUser().getEmail(),
					 * BackdoorGame.getUser().getToken(), friend.getProfile().getUuidString()));
					 * friend.update(); }
					 */
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 0, 1000);

		load();
	}

	private boolean searching = false;

	private int friends_off_y = 0;

	private void searchPlayer(String target) {
		searching = true;
		friends_off_y = 0;
		friends_panel.getContainer().clearChildComponents();
		try {
			for (Profile friend : new FriendsAPI().getSearchRequest(BackdoorGame.getUser().getEmail(),
					BackdoorGame.getUser().getToken(), target)) {
				if (friend.getUuidString().equals(BackdoorGame.getUser().getUUIDString()))
					continue;

				if (new FriendsAPI().isYourFriend(BackdoorGame.getUser().getEmail(), BackdoorGame.getUser().getToken(),
						friend.getUuidString())) {
					FriendComponent friend_panel = createFriendPanel(friend);

					friends_panel.getContainer().add(friend_panel);
					friend_panel.getStyle()
							.setTop(10 + friends_off_y * (float) friend_panel.getStyle().getHeight().get());
					friends.add(friend_panel);
				} else {
					FriendSearchComponenn friend_panel = createFriendSearchPanel(friend);
					friends_panel.getContainer().add(friend_panel);
					friend_panel.getStyle()
							.setTop(10 + friends_off_y * (float) friend_panel.getStyle().getHeight().get());
				}
				friends_off_y++;
			}
		} catch (Exception e) {
			BackdoorGame.getDesktop().dialog(
					Lang.get("dialog_exception_title", "%exception%", e.getClass().getSimpleName()),
					Lang.get("dialog_exception_message"));
			e.printStackTrace();
		}
	}

	private void loadFriendsList() {
		friends_off_y = 0;
		friends_panel.getContainer().clearChildComponents();
		try {
			loadFriends(
					new FriendsAPI().getFriends(BackdoorGame.getUser().getEmail(), BackdoorGame.getUser().getToken()));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			loadFriendsRequest(new FriendsAPI().getListRequest(BackdoorGame.getUser().getEmail(),
					BackdoorGame.getUser().getToken()));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadFriends(ArrayList<Profile> profiles) {
		for (Profile friend : profiles) {
			FriendComponent friend_panel = createFriendPanel(friend);
			friends_panel.getContainer().add(friend_panel);
			friend_panel.getStyle().setTop(10 + friends_off_y * (float) friend_panel.getStyle().getHeight().get());
			friends.add(friend_panel);
			friends_off_y++;
		}
	}

	private void loadFriendsRequest(ArrayList<Profile> profiles) {
		if (friends_off_y > 0) {
			friends_off_y += 1;
		}

		for (Profile friend : profiles) {
			FriendRequestComponent friend_panel = createFriendRequestPanel(friend);
			friends_panel.getContainer().add(friend_panel);
			friend_panel.getStyle().setTop(10 + friends_off_y * (float) friend_panel.getStyle().getHeight().get());
			friendsRequest.add(friend_panel);
			friends_off_y++;
		}
	}

	public ArrayList<FriendComponent> friends = new ArrayList<>();
	public ArrayList<FriendRequestComponent> friendsRequest = new ArrayList<>();

	private class FriendComponent extends Panel {

		private Profile profile;

		private ImageView profile_img = new ImageView();
		private Label profile_name = new Label();
		private Label profile_level = new Label("Level: 999999To");
		private Label profile_status = new Label();

		public FriendComponent(Profile profile) {
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

			float[] normal = DataParameters.getColor(FriendsApplication.this, "friend_panel_background_color");
			getStyle().getBackground().setColor(new Vector4f(normal[0], normal[1], normal[2], normal[3]));

			float[] hovered = DataParameters.getColor(FriendsApplication.this, "friend_panel_background_color_hovered");
			getHoveredStyle().setBackground(new Background());
			getHoveredStyle().getBackground().setColor(new Vector4f(hovered[0], hovered[1], hovered[2], hovered[3]));

			float[] focused = DataParameters.getColor(FriendsApplication.this, "friend_panel_background_color_focused");
			getFocusedStyle().setBackground(new Background());
			getFocusedStyle().getBackground().setColor(new Vector4f(focused[0], focused[1], focused[2], focused[3]));

			float[] pressed = DataParameters.getColor(FriendsApplication.this, "friend_panel_background_color_pressed");
			getPressedStyle().setBackground(new Background());
			getPressedStyle().getBackground().setColor(new Vector4f(pressed[0], pressed[1], pressed[2], pressed[3]));

			profile_img.getStyle().setLeft(10);
			profile_img.getStyle().setTop(10);
			profile_img.getStyle().setWidth(40);
			profile_img.getStyle().setHeight(40);
			profile_img.getStyle().setBorder(new SimpleLineBorder(new Vector4f(1, 1, 1, 1), 3));
			profile_img.getStyle().setFocusedStrokeColor(ColorConstants.transparent());
			profile_img.getStyle().getBackground().setColor(new Vector4f(0, 0, 0, 1f));
			profile_img.getStyle().setBorderRadius(FriendsApplication.this.getSize().x);
			profile_img.getStyle().setShadow(null);
			profile_img.setIntersector(new RectangleIntersector());
			profile_img.setImage(BackdoorGame.loadImageURL(profile.getProfilePictureURL()));

			profile_name.setVariable(getComponentVariable() + "_username_label");
			profile_name.getStyle().setTop(10);
			profile_name.getStyle().setLeft(60);
			profile_name.autoSizeWidth();

			profile_level.setVariable(getComponentVariable() + "_level_label");
			profile_level.getStyle().setTop(30);
			profile_level.autoSizeWidth();

			profile_status.setVariable(getComponentVariable() + "_status_label");
			profile_status.getStyle().setTop(30);
			profile_status.getStyle().setLeft(60);
			profile_status.getListenerMap().addListener(LabelWidthChangeEvent.class,
					new LabelWidthChangeEventListener() {
						@Override
						public void process(LabelWidthChangeEvent event) {
							profile_level.getStyle().setLeft(60 + event.getWidth() + 10);
						}
					});

			add(profile_img);
			add(profile_name);
			add(profile_level);
			add(profile_status);

			update();

			for (Component childrenComponent : getChildComponents()) {
				childrenComponent.setFocusable(false);
			}

			getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
				@Override
				public void process(MouseClickEvent event) {
					if (event.getAction() == MouseClickAction.RELEASE
							&& event.getButton() == MouseButton.MOUSE_BUTTON_2) {
						PopupMenu popupmenu = new PopupMenu((int) event.getAbsolutePosition().x,
								(int) event.getAbsolutePosition().y);
						popupmenu.setVariable("friend_popupmenu");
						popupmenu.setSize(50, 10);
						popupmenu.getStyle().setBorderRadius(0f);

						Label profile = new Label(Lang.get("friends_window_profile"), new LabelSizeEvent() {
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

						Label invite = new Label(Lang.get("friends_window_invite"), new LabelSizeEvent() {
							@Override
							public void process(float width, float height) {
								popupmenu.initSize();
							}
						});
						invite.getStyle().setPadding(10f);
						MouseClickEventListener invite_click = new MouseClickEventListener() {
							@Override
							public void process(MouseClickEvent event) {
								if (event.getAction() != MouseClickAction.RELEASE)
									return;

								if(GroupManager.getGroup() == null)
									GroupApplication.showApplication();
								GroupManager.invite(getProfile());

							}
						};
						invite.getListenerMap().addListener(MouseClickEvent.class, invite_click);

						Label message = new Label(Lang.get("friends_window_message"), new LabelSizeEvent() {
							@Override
							public void process(float width, float height) {
								popupmenu.initSize();
							}
						});
						message.getStyle().setPadding(10f);
						MouseClickEventListener message_click = new MouseClickEventListener() {
							@Override
							public void process(MouseClickEvent event) {
								if (event.getAction() != MouseClickAction.RELEASE)
									return;

							}
						};
						message.getListenerMap().addListener(MouseClickEvent.class, message_click);

						Label join = new Label(Lang.get("friends_window_join"), new LabelSizeEvent() {
							@Override
							public void process(float width, float height) {
								popupmenu.initSize();
							}
						});
						join.getStyle().setPadding(10f);
						MouseClickEventListener join_click = new MouseClickEventListener() {
							@Override
							public void process(MouseClickEvent event) {
								if (event.getAction() != MouseClickAction.RELEASE)
									return;

								BackdoorGame.getGateway().sendPacket(new PacketGroupAccept(getProfile().getUuidString()));
							}
						};
						join.getListenerMap().addListener(MouseClickEvent.class, join_click);

						Label delete = new Label(Lang.get("friends_window_delete"), new LabelSizeEvent() {
							@Override
							public void process(float width, float height) {
								popupmenu.initSize();
							}
						});
						delete.getStyle().setPadding(10f);
						MouseClickEventListener delete_click = new MouseClickEventListener() {
							@Override
							public void process(MouseClickEvent event) {
								if (event.getAction() != MouseClickAction.RELEASE)
									return;

								BackdoorGame.getDesktop().confirmDialog(
										Lang.get("dialog_friend_do_you_really_want_to_delete_title"),
										Lang.get("dialog_friend_do_you_really_want_to_delete_message", "%username%", FriendComponent.this.profile.getUsername()), new Runnable() {
											@Override
											public void run() {

											}
										}, new Runnable() {
											@Override
											public void run() {
												try {
													new FriendsAPI().removeRequest(BackdoorGame.getUser().getEmail(),
															BackdoorGame.getUser().getToken(),
															FriendComponent.this.profile.getUuidString());
													loadFriendsList();
												} catch (ClientProtocolException e) {
													e.printStackTrace();
												} catch (IOException e) {
													e.printStackTrace();
												}
											}
										});
							}
						};
						delete.getListenerMap().addListener(MouseClickEvent.class, delete_click);

						profile.setVariable("friends_popupmenu_profile");
						invite.setVariable("friends_popupmenu_invite");
						invite.setVariable("friends_popupmenu_invite");
						join.setVariable("friends_popupmenu_join");
						delete.setVariable("friends_popupmenu_delete");

						popupmenu.add(profile);
						if(BackdoorGame.group_invitations.contains(getProfile().getUuidString())) {
							popupmenu.add(join);
						}
						if(FriendComponent.this.profile.isOnline()) {
							popupmenu.add(invite);
							popupmenu.add(message);	
						}
						popupmenu.add(delete);
						popupmenu.load();

						popupmenu.setFocused(true);

						BackdoorGame.getDesktop().add(popupmenu);
					}
				}
			});

			profile_name.load();
			profile_level.load();
			profile_status.load();
		}

		public void setProfile(Profile profile) {
			this.profile = profile;
		}

		public Profile getProfile() {
			return profile;
		}

		private void update() {
			profile_name.getTextState().setText(profile.getUsername());

			if (profile.isOnline())
				profile_status.setVariable(getComponentVariable() + "_status_online_label");
			else
				profile_status.setVariable(getComponentVariable() + "_status_offline_label");

			profile_status.getTextState().setText(profile.isOnline() ? Lang.get("online") : Lang.get("offline"));

			profile_name.load();
			profile_status.load();
		}
	}

	private class FriendRequestComponent extends Panel {

		private Profile profile;

		private ImageView profile_img = new ImageView();
		private Label profile_name = new Label();

		private Label accept_label = new Label();
		private Label delete_label = new Label();

		public FriendRequestComponent(Profile profile) {
			this.profile = profile;
			setVariable("friends_window");

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
			setFocusable(false);

			profile_img.getStyle().setLeft(10);
			profile_img.getStyle().setTop(10);
			profile_img.getStyle().setWidth(40);
			profile_img.getStyle().setHeight(40);
			profile_img.getStyle().setBorder(new SimpleLineBorder(new Vector4f(1, 1, 1, 1), 3));
			profile_img.getStyle().setFocusedStrokeColor(ColorConstants.transparent());
			profile_img.getStyle().getBackground().setColor(new Vector4f(0, 0, 0, 1f));
			profile_img.getStyle().setBorderRadius(FriendsApplication.this.getSize().x);
			profile_img.getStyle().setShadow(null);
			profile_img.setIntersector(new RectangleIntersector());
			profile_img.setImage(BackdoorGame.loadImageURL(profile.getProfilePictureURL()));

			profile_name.setVariable(getComponentVariable() + "_username_label");
			profile_name.getStyle().setTop(10);
			profile_name.getStyle().setLeft(60);
			profile_name.autoSizeWidth();

			delete_label.setVariable(getComponentVariable() + "_delete_label");
			delete_label.getStyle().setTop(30);
			delete_label.autoSizeWidth();
			delete_label.getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
				@Override
				public void process(MouseClickEvent event) {
					if (event.getAction() == MouseClickAction.RELEASE) {
						try {
							new FriendsAPI().declineRequest(BackdoorGame.getUser().getEmail(),
									BackdoorGame.getUser().getToken(), profile.getUuidString());
							loadFriendsList();
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			});

			accept_label.setVariable(getComponentVariable() + "_accept_label");
			accept_label.getStyle().setTop(30);
			accept_label.getStyle().setLeft(60);
			accept_label.getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
				@Override
				public void process(MouseClickEvent event) {
					if (event.getAction() == MouseClickAction.RELEASE) {
						try {
							new FriendsAPI().acceptRequest(BackdoorGame.getUser().getEmail(),
									BackdoorGame.getUser().getToken(), profile.getUuidString());
							loadFriendsList();
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			});
			accept_label.getListenerMap().addListener(LabelWidthChangeEvent.class, new LabelWidthChangeEventListener() {
				@Override
				public void process(LabelWidthChangeEvent event) {
					delete_label.getStyle().setLeft(delete_label.getTextState().getTextWidth() + event.getWidth() + 10);
				}
			});

			float[] normal = DataParameters.getColor(FriendsApplication.this, "friend_panel_background_color");
			getStyle().getBackground().setColor(new Vector4f(normal[0], normal[1], normal[2], normal[3]));

			float[] hovered = DataParameters.getColor(FriendsApplication.this, "friend_panel_background_color_hovered");
			getHoveredStyle().setBackground(new Background());
			getHoveredStyle().getBackground().setColor(new Vector4f(hovered[0], hovered[1], hovered[2], hovered[3]));

			float[] focused = DataParameters.getColor(FriendsApplication.this, "friend_panel_background_color_focused");
			getFocusedStyle().setBackground(new Background());
			getFocusedStyle().getBackground().setColor(new Vector4f(focused[0], focused[1], focused[2], focused[3]));

			float[] pressed = DataParameters.getColor(FriendsApplication.this, "friend_panel_background_color_pressed");
			getPressedStyle().setBackground(new Background());
			getPressedStyle().getBackground().setColor(new Vector4f(pressed[0], pressed[1], pressed[2], pressed[3]));

			add(profile_img);
			add(profile_name);
			add(accept_label);
			add(delete_label);

			update();

			profile_name.load();
			accept_label.load();
			delete_label.load();
		}

		public void setProfile(Profile profile) {
			this.profile = profile;
		}

		public Profile getProfile() {
			return profile;
		}

		private void update() {
			profile_name.getTextState().setText(profile.getUsername());
			delete_label.getTextState().setText(Lang.get("delete"));
			accept_label.getTextState().setText(Lang.get("accept"));
		}
	}

	private class FriendSearchComponenn extends Panel {

		private Profile profile;

		private ImageView profile_img = new ImageView();
		private Label profile_name = new Label();

		private Label add_label = new Label();

		public FriendSearchComponenn(Profile profile) {
			this.profile = profile;
			setVariable("friends_window");

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

			float[] normal = DataParameters.getColor(FriendsApplication.this, "friend_panel_background_color");
			getStyle().getBackground().setColor(new Vector4f(normal[0], normal[1], normal[2], normal[3]));

			float[] hovered = DataParameters.getColor(FriendsApplication.this, "friend_panel_background_color_hovered");
			getHoveredStyle().setBackground(new Background());
			getHoveredStyle().getBackground().setColor(new Vector4f(hovered[0], hovered[1], hovered[2], hovered[3]));

			float[] focused = DataParameters.getColor(FriendsApplication.this, "friend_panel_background_color_focused");
			getFocusedStyle().setBackground(new Background());
			getFocusedStyle().getBackground().setColor(new Vector4f(focused[0], focused[1], focused[2], focused[3]));

			float[] pressed = DataParameters.getColor(FriendsApplication.this, "friend_panel_background_color_pressed");
			getPressedStyle().setBackground(new Background());
			getPressedStyle().getBackground().setColor(new Vector4f(pressed[0], pressed[1], pressed[2], pressed[3]));

			profile_img.getStyle().setLeft(10);
			profile_img.getStyle().setTop(10);
			profile_img.getStyle().setWidth(40);
			profile_img.getStyle().setHeight(40);
			profile_img.getStyle().setBorder(new SimpleLineBorder(new Vector4f(1, 1, 1, 1), 3));
			profile_img.getStyle().setFocusedStrokeColor(ColorConstants.transparent());
			profile_img.getStyle().getBackground().setColor(new Vector4f(0, 0, 0, 1f));
			profile_img.getStyle().setBorderRadius(FriendsApplication.this.getSize().x);
			profile_img.getStyle().setShadow(null);
			profile_img.setIntersector(new RectangleIntersector());
			profile_img.setImage(BackdoorGame.loadImageURL(profile.getProfilePictureURL()));
			profile_img.setFocusable(false);

			profile_name.setVariable(getComponentVariable() + "_username_label");
			profile_name.getStyle().setTop(10);
			profile_name.getStyle().setLeft(60);
			profile_name.autoSizeWidth();

			add_label.setVariable(getComponentVariable() + "_add_label");
			add_label.getStyle().setTop(30);
			add_label.getStyle().setLeft(60);
			add_label.getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
				@Override
				public void process(MouseClickEvent event) {
					if (event.getAction() == MouseClickAction.RELEASE) {
						try {
							new FriendsAPI().sentRequest(BackdoorGame.getUser().getEmail(),
									BackdoorGame.getUser().getToken(), profile.getUuidString());
							search_textfield.getTextState().setText("");
							searching = false;
							loadFriendsList();
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			});

			add(profile_img);
			add(profile_name);
			add(add_label);

			update();

			profile_name.load();
			add_label.load();
		}

		public void setProfile(Profile profile) {
			this.profile = profile;
		}

		public Profile getProfile() {
			return profile;
		}

		private void update() {
			profile_name.getTextState().setText(profile.getUsername());
			add_label.getTextState().setText(Lang.get("add"));
		}
	}

	private FriendComponent createFriendPanel(Profile profile) {
		return new FriendComponent(profile);
	}

	private FriendSearchComponenn createFriendSearchPanel(Profile profile) {
		return new FriendSearchComponenn(profile);
	}

	private FriendRequestComponent createFriendRequestPanel(Profile profile) {
		return new FriendRequestComponent(profile);
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public void load() {
		getIcon().setImage(BackdoorGame.getDatapack().getImage("window_icon_friends"));

		user_profile.setImage(BackdoorGame.loadImageURL(BackdoorGame.getUserProfile().getProfilePictureURL()));

		username.load();
		username.getTextState().setText(BackdoorGame.getUserProfile().getUsername());

		level.load();
		level.getTextState().setText("Level: 999999To");

		status.load();
		status.getTextState().setText(Lang.get("online"));

		friends_panel.load();
		search_textfield.load();

		float[] color = DataParameters.getColor(this, "top_panel_background_color");
		top_panel.getStyle().getBackground().setColor(new Vector4f(color[0], color[1], color[2], color[3]));
		top_panel.getHoveredStyle().getBackground().setColor(new Vector4f(color[0], color[1], color[2], color[3]));
		top_panel.getFocusedStyle().getBackground().setColor(new Vector4f(color[0], color[1], color[2], color[3]));
		top_panel.getPressedStyle().getBackground().setColor(new Vector4f(color[0], color[1], color[2], color[3]));

		super.load();
	}
}
