package isotopestudio.backdoor.engine.components.desktop.tooltip;

import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.components.events.TooltipSizeEvent;
import isotopestudio.backdoor.engine.components.listeners.TooltipAutoSize;
import isotopestudio.backdoor.engine.datapack.DataParameters;

/**
 * @author BESSIERE
 * @github https://www.github.com/DoryanBessiere/
 */
public class Tooltip extends org.liquidengine.legui.component.Tooltip implements IComponent {

	public Tooltip() {
		super();
		getListenerMap().addListener(TooltipSizeEvent.class, new TooltipAutoSize());
		initIComponent();
		load();
	}

	public Tooltip(String tooltip) {
		super(tooltip);
		getListenerMap().addListener(TooltipSizeEvent.class, new TooltipAutoSize());
		
		initIComponent();
		setSize(300,2);
		load();
	}

	@Override
	public void load() {
		// Background colors
		DataParameters.applyBackgroundColor(this, "tooltip_background_color", getStyle());
		DataParameters.applyBackgroundColor(this, "tooltip_background_color_focused", getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "tooltip_background_color_hovered", getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "tooltip_background_color_pressed", getPressedStyle());

		// Border colors
		DataParameters.applyBorderColor(this, "tooltip_border_color", getStyle());
		DataParameters.applyBorderColor(this, "tooltip_border_color_focused", getFocusedStyle());
		DataParameters.applyBorderColor(this, "tooltip_border_color_hovered", getHoveredStyle());
		DataParameters.applyBorderColor(this, "tooltip_border_color_pressed", getPressedStyle());

		// Title color
		DataParameters.applyTextColor(this, "tooltip_text_color", getStyle());
		DataParameters.applyTextColor(this, "tooltip_text_color_focused", getFocusedStyle());
		DataParameters.applyTextColor(this, "tooltip_text_color_hovered", getHoveredStyle());
		DataParameters.applyTextColor(this, "tooltip_text_color_pressed", getPressedStyle());

		// Title font
		DataParameters.applyTextFont(this, "tooltip_text_font", getStyle());
		DataParameters.applyTextFont(this, "tooltip_text_font_focused", getFocusedStyle());
		DataParameters.applyTextFont(this, "tooltip_text_font_hovered", getHoveredStyle());
		DataParameters.applyTextFont(this, "tooltip_text_font_pressed", getPressedStyle());

		if (DataParameters.has(this, "tooltip_border_radius")) {
			getStyle().setBorderRadius(DataParameters.getFloat(this, "tooltip_border_radius"));
		}
	}

	@Override
	public void update() {
		
	}

	@Override
	public String getComponentName() {
		return "tooltip";
	}

	private String variable;
	
	/**
	 * @param variable the variable to set
	 */
	public void setVariable(String variable) {
		this.variable = variable;
	}
	
	@Override
	public String getComponentVariable() {
		return variable;
	}
}