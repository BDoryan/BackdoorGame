package isotopestudio.backdoor.game.applications;

import java.text.SimpleDateFormat;

import org.liquidengine.legui.component.event.textarea.TextAreaFieldUpdateEvent;
import org.liquidengine.legui.component.optional.align.HorizontalAlign;
import org.liquidengine.legui.component.optional.align.VerticalAlign;
import org.liquidengine.legui.event.KeyEvent;
import org.liquidengine.legui.listener.KeyEventListener;
import org.liquidengine.legui.listener.processor.EventProcessorProvider;
import org.liquidengine.legui.style.Style.DisplayType;
import org.lwjgl.glfw.GLFW;

import doryanbessiere.isotopestudio.api.profile.Profile;
import doryanbessiere.isotopestudio.commons.lang.Lang;
import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.components.desktop.TextArea;
import isotopestudio.backdoor.engine.components.desktop.TextField;
import isotopestudio.backdoor.engine.components.desktop.window.Window;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.gateway.listeners.ChatListener;
import isotopestudio.backdoor.gateway.packet.packets.PacketClientChatMessage;
import isotopestudio.backdoor.gateway.packet.packets.PacketClientChatMessages;

public class ChatApplication extends Window implements IComponent {
	
	public static ChatApplication main;
	
	public static void showApplication() {
		if(main == null) {
			main = new ChatApplication();
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

	public ChatApplication() {
		super("", 0, 0, 600, 400);

		getStyle().setMinWidth(getSize().x);
		getStyle().setMinHeight(getSize().y);

		this.getContainer().getStyle().setDisplay(DisplayType.FLEX);

		textarea = new TextArea();
		textarea.setEditable(false);

		textarea.getStyle().setBorderRadius(0F);
		textarea.getViewport().getStyle().setBorderRadius(0F);

		textarea.getTextAreaField().setSize(800, 600);

		textarea.getStyle().setShadow(null);
		textarea.getStyle().setTop(0f);
		textarea.getStyle().setBottom(20f);
		textarea.getStyle().setRight(0f);
		textarea.getStyle().setLeft(0f);

		textarea.getTextAreaField().getStyle().setFontSize(14f);
		textarea.getTextAreaField().getStyle().setHorizontalAlign(HorizontalAlign.LEFT);
		textarea.getTextAreaField().getStyle().setVerticalAlign(VerticalAlign.TOP);
		textarea.getTextAreaField().setStickToAlignment(false);

		textfield = new TextField("");
		textfield.getStyle().setFontSize(14f);
		textfield.getStyle().setHeight(20);
		textfield.getStyle().setBottom(0f);
		textfield.getStyle().setRight(15f);
		textfield.getStyle().setLeft(0f);

		textfield.getListenerMap().addListener(KeyEvent.class, (KeyEventListener) event -> {
			if (event.getAction() == GLFW.GLFW_RELEASE) {
				int key = event.getKey();

				if (key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_KP_ENTER) {
					BackdoorGame.getGateway().sendPacket(new PacketClientChatMessage(textfield.getTextState().getText()));
					textfield.getTextState().setText("");
				}
			}
		});

		this.getContainer().add(textfield);
		this.getContainer().add(textarea);

		load();
		listenChat();
	}
	
	@Override
	public void closeWindow() {
		unlistenChat();
		super.closeWindow();
	}
	
	private ChatListener chatListener;

	public void listenChat() {
		BackdoorGame.getGateway().getChatListeners().add(chatListener = new ChatListener() {
			@Override
			public void message(Profile profile, long time, String textMessage) {
				String message = "[" + new SimpleDateFormat("HH:mm:ss").format(time) + "] ["+profile.getUsername()+"] > "+textMessage; 
				
				textarea.getTextState().setText(textarea.getTextAreaField().getTextState().getText() + message+"\n");
				textarea.getTextAreaField()
						.setCaretPosition(textarea.getTextAreaField().getTextState().getText().length());
				EventProcessorProvider.getInstance().pushEvent(
						(new TextAreaFieldUpdateEvent(textarea.getTextAreaField(), null, BackdoorGame.getFrame())));
			}
		});
		BackdoorGame.getGateway().sendPacket(new PacketClientChatMessages());
	}

	public void unlistenChat() {
		BackdoorGame.getGateway().getChatListeners().remove(chatListener);
	}
	
	private TextArea textarea;
	private TextField textfield;

	@Override
	public void load() {
		getIcon().setImage(BackdoorGame.getDatapack().getImage("window_icon_messaging"));
		super.load();

		if (textarea != null) {
			textarea.load();
		}

		if (textfield != null) {
			textfield.load();
		}

		getTitle().getTextState().setText(Lang.get("chat_application_title"));
	}
}
