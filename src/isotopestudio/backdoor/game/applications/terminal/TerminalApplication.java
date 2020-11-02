package isotopestudio.backdoor.game.applications.terminal;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;

import org.joml.Vector4i;
import org.liquidengine.legui.component.TextAreaField;
import org.liquidengine.legui.component.event.textarea.TextAreaFieldContentChangeEvent;
import org.liquidengine.legui.component.optional.TextState;
import org.liquidengine.legui.component.optional.align.HorizontalAlign;
import org.liquidengine.legui.component.optional.align.VerticalAlign;
import org.liquidengine.legui.event.CharEvent;
import org.liquidengine.legui.event.KeyEvent;
import org.liquidengine.legui.event.KeyboardEvent;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.event.MouseDragEvent;
import org.liquidengine.legui.input.Mouse.MouseButton;
import org.liquidengine.legui.listener.CharEventListener;
import org.liquidengine.legui.listener.processor.EventProcessorProvider;
import org.liquidengine.legui.style.Style.DisplayType;
import org.liquidengine.legui.util.TextUtil;

import doryanbessiere.isotopestudio.commons.lang.Lang;
import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.components.desktop.TextArea;
import isotopestudio.backdoor.engine.components.desktop.window.Window;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.applications.terminal.command.CommandTyped;
import isotopestudio.backdoor.game.applications.terminal.event.TextAreaFieldKeyEventListener;
import isotopestudio.backdoor.game.applications.terminal.interfaces.InterfaceType;
import isotopestudio.backdoor.game.command.ICommand;
import isotopestudio.backdoor.network.packet.packets.PacketPlayerAttackElement;
import isotopestudio.backdoor.utils.ConsoleWriter;

public class TerminalApplication extends Window implements IComponent, CommandTyped {

	public static TerminalApplication main;
	public static TimerTask caret_timer;

	public static void showApplication() {
		if (main == null) {
			main = new TerminalApplication();
			BackdoorGame.getDesktop().addWindow(main);
			main.centerLocation();
			main.load();
		} else {
			if (BackdoorGame.getDesktop().containsWindow(main)) {
				main.showWindow();
				main.load();
			} else {
				main = null;
				showApplication();
			}
		}
	}

	public static void closeApplication() {
		if (main != null) {
			BackdoorGame.getDesktop().removeWindow(main);
			main = null;
		}
	}

	private String command = "";

	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @param command the command to set
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	public TerminalApplication() {
		super("", 0, 0, 540, 500);
		setVariable("terminal_window");

		getStyle().setMinWidth(getSize().x);
		getStyle().setMinHeight(getSize().y);

		this.getContainer().getStyle().setDisplay(DisplayType.FLEX);

		textarea = new TextArea();

		textarea.getTextAreaField().setSize(500, 500);

		textarea.getStyle().setShadow(null);
		textarea.getStyle().setTop(0f);
		textarea.getStyle().setBottom(20f);
		textarea.getStyle().setRight(0f);
		textarea.getStyle().setLeft(0f);

		textarea.getStyle().setBorderRadius(0F);
		textarea.getViewport().getStyle().setBorderRadius(0F);

		textarea.getTextAreaField().getListenerMap().getListeners(MouseClickEvent.class).clear();
		textarea.getTextAreaField().getListenerMap().getListeners(MouseDragEvent.class).clear();
		textarea.getTextAreaField().getListenerMap().getListeners(KeyEvent.class).clear();
		textarea.getTextAreaField().getListenerMap().getListeners(KeyboardEvent.class).clear();
		textarea.getTextAreaField().getListenerMap().addListener(KeyboardEvent.class,
				new TextAreaFieldKeyEventListener(this));
		textarea.getTextAreaField().getListenerMap().getListeners(CharEvent.class).clear();
		textarea.getTextAreaField().getListenerMap().addListener(CharEvent.class, new CharEventListener() {
			@Override
			public void process(CharEvent event) {
				TextAreaField textInput = (TextAreaField) event.getTargetComponent();
				if (textInput.isFocused() && textInput.isEditable() && !MouseButton.MOUSE_BUTTON_LEFT.isPressed()) {
					String str = TextUtil.cpToStr(event.getCodepoint());
					TextState textState = textInput.getTextState();
					String oldText = textState.getText();
					int start = textInput.getStartSelectionIndex();
					int end = textInput.getEndSelectionIndex();
					if (start > end) {
						start = textInput.getEndSelectionIndex();
						end = textInput.getStartSelectionIndex();
					}
					if (start != end) {
						StringBuilder t = new StringBuilder(textState.getText());
						t.delete(start, end);
						textState.setText(t.toString());
						textInput.setCaretPosition(start);
						textInput.setStartSelectionIndex(start);
						textInput.setEndSelectionIndex(start);
					}
					int caretPosition = textInput.getCaretPosition();
					StringBuilder t = new StringBuilder(textState.getText());
					t.insert(caretPosition, str);
					command += str;

					textState.setText(t.toString());
					int newCaretPosition = caretPosition + str.length();
					textInput.setCaretPosition(newCaretPosition);
					textInput.setEndSelectionIndex(newCaretPosition);
					textInput.setStartSelectionIndex(newCaretPosition);
					String newText = textState.getText();

					EventProcessorProvider.getInstance().pushEvent(new TextAreaFieldContentChangeEvent(textInput,
							event.getContext(), event.getFrame(), oldText, newText));
				}
			}
		});

		textarea.getTextAreaField().getStyle().setFont("main");
		textarea.getTextAreaField().getStyle().setFontSize(14F);
		textarea.getTextAreaField().getStyle().setHorizontalAlign(HorizontalAlign.LEFT);
		textarea.getTextAreaField().getStyle().setVerticalAlign(VerticalAlign.TOP);
		textarea.getTextAreaField().setStickToAlignment(false);
		textarea.setVerticalScrollBarVisible(false);
		textarea.setHorizontalScrollBarVisible(false);
		textarea.setVariable("terminal_window_textarea");

		this.getContainer().add(textarea);

		load(InterfaceType.MAIN);

		load();
	}

	public ArrayList<CommandTyped> commandListeners = new ArrayList<>();

	private ICommand[] commands = new ICommand[] {};

	public void handleCommand() {
		if (!typed(this.command))
			log("This command is unknown");
		this.command = "";
	}

	@Override
	public boolean typed(String command) {
		log(command);

		for (CommandTyped commandListener : commandListeners) {
			if (commandListener.typed(command))
				return true;
		}

		if(getInterface() != null) {
			if (getInterface().getInterface().getActions().containsKey(command)) {
				getInterface().getInterface().getActions().get(command).run();
				this.command = "";
				return true;
			}	
		}

		String[] args = command.split(" ");
		String target = args[0];

		for (ICommand icommand : commands) {
			if (icommand.getCommand().equalsIgnoreCase(target)) {
				String[] arguments = new String[args.length - 1];
				if (args.length > 1) {
					for (int i = 1; i < args.length; i++) {
						arguments[i - 1] = args[i];
					}
				}
				icommand.handle(arguments);
				this.command = "";
				return true;
			}
		}
		return false;
	}

	public void log(String message) {
		removeTypeLine();
		textarea.getTextState().setText(textarea.getTextState().getText() + "" + message + "\n");
		addTypeLine();
	}

	private InterfaceType interface_;
	
	public void load(InterfaceType interface_) {
		this.interface_ = interface_;
		setText(interface_.getInterface().content(this));
		addTypeLine();
	}
	
	/**
	 * @return the interface_
	 */
	public InterfaceType getInterface() {
		return interface_;
	}

	public void removeTypeLine() {
		String[] lines = textarea.getTextState().getText().split("\n");
		int i = 0;
		int max = lines.length - 1;
		String text = "";
		for (String line : lines) {
			if (i < max) {
				text += line + "\n";
				i++;
			}
		}
		setText(text);
	}

	public String textContent() {
		return textarea.getTextState().getText();
	}
	
	public void addTypeLine() {
		setText(textarea.getTextState().getText() + " > ");
		textarea.setCaretPosition(textarea.getTextState().getText().length());
	}

	public String title() {
		return " _____         _     _ \r\n" + "| __  |___ ___| |_ _| |___ ___ ___  \r\n"
				+ "| __ -| .'|  _| '_| . | . | . |  _|    Version\r\n" + "|_____|__,|___|_,_|___|___|___|_|      "
				+ BackdoorGame.GAME_VERSION + "\r\n";
	}

	public void clearText() {
		textarea.getTextState().setText("");
	}

	public void setText(String text) {
		textarea.getTextState().setText(text);
	}

	public void addText(String text) {
		textarea.getTextState().setText(textarea.getTextState().getText() + "\r\n" + text);
	}

	private TextArea textarea;

	@Override
	public void load() {
		getIcon().setImage(BackdoorGame.getDatapack().getImage("window_icon_terminal"));
		super.load();

		if (textarea != null) {
			textarea.load();
		}

		getTitle().getTextState().setText(Lang.get("terminal_title"));
	}
}
