package isotopestudio.backdoor.game.applications;

import org.liquidengine.legui.component.event.textarea.TextAreaFieldUpdateEvent;
import org.liquidengine.legui.component.optional.align.HorizontalAlign;
import org.liquidengine.legui.component.optional.align.VerticalAlign;
import org.liquidengine.legui.event.KeyEvent;
import org.liquidengine.legui.listener.KeyEventListener;
import org.liquidengine.legui.listener.processor.EventProcessorProvider;
import org.liquidengine.legui.style.Style.DisplayType;
import org.lwjgl.glfw.GLFW;

import doryanbessiere.isotopestudio.commons.lang.Lang;
import doryanbessiere.isotopestudio.commons.logger.listeners.LoggerListener;
import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.components.desktop.TextArea;
import isotopestudio.backdoor.engine.components.desktop.TextField;
import isotopestudio.backdoor.engine.components.desktop.window.Window;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.command.ICommand;

public class JavaTerminalApplication extends Window implements IComponent {

	// icon_window_terminal
	public JavaTerminalApplication() {
		super("", 0, 0, 800, 600);

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
					ICommand.command(textfield.getTextState().getText());
					textfield.getTextState().setText("");
				}
			}
		});

		this.getContainer().add(textfield);
		this.getContainer().add(textarea);

		load();
		listenLogger();
	}
	
	public void listenLogger() {
		textarea.getTextAreaField().getTextState().setText(BackdoorGame.getLogger().toString()+"\n");
		BackdoorGame.getLogger().getListeners().add(new LoggerListener() {
			@Override
			public void log(String message) {
				writeTerminal(message);
			}
		});
	}

	private TextArea textarea;
	private TextField textfield;

	private void writeTerminal(String message) {
		textarea.getTextState().setText(textarea.getTextAreaField().getTextState().getText() + message);
		textarea.getTextAreaField().setCaretPosition(textarea.getTextAreaField().getTextState().getText().length());
		EventProcessorProvider.getInstance()
		.pushEvent((new TextAreaFieldUpdateEvent(textarea.getTextAreaField(), null, BackdoorGame.getFrame())));
	}

	@Override
	public void load() {
		getIcon().setImage(BackdoorGame.getDatapack().getImage("window_icon_terminal"));
		super.load();

		if(textarea!=null) {
			textarea.load();	
		}

		if(textfield!=null) {
			textfield.load();	
		}
		
		getTitle().getTextState().setText(Lang.get("java_terminal_title"));
	}
}
