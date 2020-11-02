package isotopestudio.backdoor.engine.components.desktop.window;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.joml.Vector2f;
import org.liquidengine.legui.component.Button;
import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.component.ImageView;
import org.liquidengine.legui.component.Widget;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.event.MouseClickEvent.MouseClickAction;
import org.liquidengine.legui.intersection.RectangleIntersector;
import org.liquidengine.legui.listener.MouseClickEventListener;
import org.liquidengine.legui.style.Style.DisplayType;

import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.datapack.DataParameters;
import isotopestudio.backdoor.game.BackdoorGame;

public class Window extends Widget implements IComponent {

	private UUID uuid = UUID.randomUUID();

	private ImageView icon;

	private Button hide_button;
	private Button close_button;
	private Button maximize_button;

	public Window(String title, float x, float y, float width, float height) {
		super(title, x, y, width, height);
		this.icon = new ImageView();
		this.icon.setFocusable(false);
		this.icon.setTabFocusable(false);
		this.icon.setIntersector(new RectangleIntersector());

		this.getContainer().getStyle().setShadow(null);
		this.getContainer().getStyle().setFocusedStrokeColor(0, 0, 0, 0);

		this.icon.getStyle().getBackground().getColor().set(0, 0, 0, 0);
		this.icon.getStyle().getBorder().setEnabled(false);
		this.icon.getStyle().setBorderRadius(0F);
		this.icon.getStyle().setShadow(null);
		this.icon.getStyle().setLeft(2f);
		this.icon.getStyle().setTop(2f);
		this.icon.getStyle().setWidth((float) getTitleContainer().getStyle().getHeight().get() - 4);
		this.icon.getStyle().setHeight((float) getTitleContainer().getStyle().getHeight().get() - 4);

		this.close_button = new Button("");
		this.close_button.getStyle().setTop(4);
		this.close_button.getStyle().setRight(2);
		this.close_button.getStyle().setWidth(20 - 8);
		this.close_button.getStyle().setHeight(20 - 8);
		this.close_button.getStyle().getBorder().setEnabled(false);
		this.close_button.getStyle().setBorderRadius(16F);
		this.close_button.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
			if (event.getAction() == MouseClickAction.RELEASE) {
				closeWindow();
			}
		});

		this.maximize_button = new Button("");
		this.maximize_button.getStyle().setTop(4);
		this.maximize_button.getStyle().setRight(20);
		this.maximize_button.getStyle().setWidth(20 - 8);
		this.maximize_button.getStyle().setHeight(20 - 8);
		this.maximize_button.getStyle().getBorder().setEnabled(false);
		this.maximize_button.getStyle().setShadow(null);
		this.maximize_button.getStyle().setBorderRadius(16F);
		this.maximize_button.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
			if (event.getAction() == MouseClickAction.RELEASE) {
				maximizeWindow();
			}
		});

		this.hide_button = new Button("");
		this.hide_button.getStyle().setTop(4);
		this.hide_button.getStyle().setRight(38);
		this.hide_button.getStyle().setWidth(20 - 8);
		this.hide_button.getStyle().setHeight(20 - 8);
		this.hide_button.getStyle().getBorder().setEnabled(false);
		this.hide_button.getStyle().setBorderRadius(16F);
		this.hide_button.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) event -> {
			if (event.getAction() == MouseClickAction.RELEASE) {
				hideWindow();
			}
		});

		this.getContainer().getStyle().setShadow(null);

		this.getTitleContainer().add(icon);

		this.getTitleContainer().add(maximize_button);
		this.getTitleContainer().add(hide_button);
		this.getTitleContainer().add(close_button);

		this.getTitleContainer().remove(getCloseButton());
		this.getTitleContainer().remove(getMinimizeButton());
		initIComponent();
	}
	
	public void closeWindow() {
		BackdoorGame.getDesktop().removeWindow(Window.this);
	}

	public void minimizeWindow() {
		if (origin_position != null && origin_size != null) {
			this.setPosition(origin_position);
			this.setSize(origin_size);
		}
	}

	private Vector2f origin_position;
	private Vector2f origin_size;

	public void maximizeWindow() {
		if (!isResizable())
			return;
		boolean in_maximized_mode = getSize().equals(BackdoorGame.getDesktop().getSize().x,
				BackdoorGame.getDesktop().getSize().y - BackdoorGame.getDesktop().getTaskBar().getSize().y)
				&& getPosition().equals(0, BackdoorGame.getDesktop().getTaskBar().getSize().y);
		if (in_maximized_mode) {
			minimizeWindow();
			return;
		} else {
			origin_position = new Vector2f(this.getPosition().x, this.getPosition().y);
			origin_size = new Vector2f(this.getSize().x, this.getSize().y);

			this.setPosition(0, BackdoorGame.getDesktop().getTaskBar().getSize().y);
			this.setSize(BackdoorGame.getDesktop().getSize().x,
					BackdoorGame.getDesktop().getSize().y - BackdoorGame.getDesktop().getTaskBar().getSize().y);
		}
	}

	private DisplayType hidden_display_value;

	public void hideWindow() {
		hidden_display_value = getStyle().getDisplay();
		getStyle().setDisplay(DisplayType.NONE);
	}

	public void showWindow(DisplayType display_type) {
		Window.this.getStyle().setDisplay(display_type);
		BackdoorGame.getDesktop().remove(this);
		BackdoorGame.getDesktop().add(this);
	}

	public Button getWindowCloseButton() {
		return close_button;
	}

	/**
	 * <pre>
	 * Warning: DisplayType will have the value it had before it was hidden.
	 * </pre>
	 */
	public void showWindow() {
		if (hidden_display_value == null) {
			BackdoorGame.getDesktop().remove(this);
			BackdoorGame.getDesktop().add(this);
		} else {
			showWindow(hidden_display_value);
			hidden_display_value = null;
		}
	}

	@Override
	public void load() {
		if (hasIcon()) {
			this.getTitle().getStyle().setLeft((float) getTitleContainer().getStyle().getHeight().get());
		} else {
			this.getTitle().getStyle().setLeft(0f);
		}

		// Background colors
		DataParameters.applyBackgroundColor(this, "window_background_color", getStyle());
		DataParameters.applyBackgroundColor(this, "window_background_color", getContainer().getStyle());
		DataParameters.applyBackgroundColor(this, "window_background_color_focused", getContainer().getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "window_background_color_focused", getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "window_background_color_hovered", getContainer().getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "window_background_color_hovered", getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "window_background_color_pressed", getContainer().getPressedStyle());
		DataParameters.applyBackgroundColor(this, "window_background_color_pressed", getPressedStyle());

		// Border colors
		DataParameters.applyBorderColor(this, "window_border_color", getStyle());
		DataParameters.applyBorderColor(this, "window_border_color", getContainer().getStyle());
		DataParameters.applyBorderColor(this, "window_border_color_focused", getContainer().getFocusedStyle());
		DataParameters.applyBorderColor(this, "window_border_color_focused", getFocusedStyle());
		DataParameters.applyBorderColor(this, "window_border_color_hovered", getContainer().getHoveredStyle());
		DataParameters.applyBorderColor(this, "window_border_color_hovered", getHoveredStyle());
		DataParameters.applyBorderColor(this, "window_border_color_pressed", getContainer().getPressedStyle());
		DataParameters.applyBorderColor(this, "window_border_color_pressed", getPressedStyle());

		// Titlebar color
		DataParameters.applyBackgroundColor(this, "window_title_background_color", getTitleContainer().getStyle());
		DataParameters.applyBackgroundColor(this, "window_title_background_color_focused",
				getTitleContainer().getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "window_title_background_color_hovered",
				getTitleContainer().getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "window_title_background_color_pressed",
				getTitleContainer().getPressedStyle());

		// Title color
		DataParameters.applyTextColor(this, "window_title_color", getTitle().getStyle());
		DataParameters.applyTextColor(this, "window_title_color_focused", getTitle().getFocusedStyle());
		DataParameters.applyTextColor(this, "window_title_color_hovered", getTitle().getHoveredStyle());
		DataParameters.applyTextColor(this, "window_title_color_pressed", getTitle().getPressedStyle());

		// Title font
		DataParameters.applyTextFont(this, "window_title_font", getTitle().getStyle());
		DataParameters.applyTextFont(this, "window_title_font_focused", getTitle().getFocusedStyle());
		DataParameters.applyTextFont(this, "window_title_font_hovered", getTitle().getHoveredStyle());
		DataParameters.applyTextFont(this, "window_title_font_pressed", getTitle().getPressedStyle());

		// Button close background color
		DataParameters.applyBackgroundColor(this, "window_close_button_background_color", close_button.getStyle());
		DataParameters.applyBackgroundColor(this, "window_close_button_background_color_focused",
				close_button.getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "window_close_button_background_color_hovered",
				close_button.getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "window_close_button_background_color_pressed",
				close_button.getPressedStyle());

		// Button hide background color
		DataParameters.applyBackgroundColor(this, "window_hide_button_background_color", hide_button.getStyle());
		DataParameters.applyBackgroundColor(this, "window_hide_button_background_color_focused",
				hide_button.getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "window_hide_button_background_color_hovered",
				hide_button.getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "window_hide_button_background_color_pressed",
				hide_button.getPressedStyle());

		// Button maximize background color
		DataParameters.applyBackgroundColor(this, "window_maximize_button_background_color",
				maximize_button.getStyle());
		DataParameters.applyBackgroundColor(this, "window_maximize_button_background_color_focused",
				maximize_button.getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "window_maximize_button_background_color_hovered",
				maximize_button.getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "window_maximize_button_background_color_pressed",
				maximize_button.getPressedStyle());

		// Border length
		if (DataParameters.has(this, "window_border_radius")) {
			float border = DataParameters.getFloat(this, "window_border_radius");
			this.getStyle().setBorderRadius(border);
			this.getTitleContainer().getStyle().setBorderRadius(border);
			this.getContainer().getStyle().setBorderRadius(border);
		}

		BackdoorGame.getDesktop().getTaskBar().updateIcon(this);
	}

	@Override
	public void update() {
	}

	public boolean isWindowTargeted() {
		List<Window> windows = new ArrayList<>();
		for (Component child : getParent().getChildComponents()) {
			if (child instanceof Window) {
				windows.add((Window) child);
			}
		}

		if (windows.size() <= 0)
			return false;
		if (windows.size() == 1)
			return true;
		if (windows.get(windows.size() - 1) == this) {
			return true;
		}
		return false;
	}

	public boolean hasIcon() {
		return this.icon.getImage() != null;
	}

	public ImageView getIcon() {
		return icon;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void centerLocation() {
		Component parent = getParent();
		if (parent != null) {
			setPosition(parent.getSize().x / 2 - getSize().x / 2, parent.getSize().y / 2 - getSize().y / 2);
		}
	}

	@Override
	public String getComponentName() {
		return "window";
	}

	private String variable = null;

	/**
	 * <pre>
	 * Warning: execute <code>load();</code> to update the textures
	 * </pre>
	 * 
	 * @param variable
	 */
	public void setVariable(String variable) {
		this.variable = variable;
	}

	@Override
	public String getComponentVariable() {
		return variable;
	}
}