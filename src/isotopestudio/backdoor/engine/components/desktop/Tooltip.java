package isotopestudio.backdoor.engine.components.desktop;

import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.datapack.DataParameters;

/**
 * @author BESSIERE
 * @github https://www.github.com/DoryanBessiere/
 */
public class Tooltip extends org.liquidengine.legui.component.Tooltip implements IComponent {

	private boolean autoSize = true;
	
	public Tooltip() {
		super();
	}

	public Tooltip(String tooltip) {
		super(tooltip);
	}
	
	/**
	 * @param autoSize the autoSize to set
	 */
	public void setAutoSize(boolean autoSize) {
		this.autoSize = autoSize;
	}
	
	/**
	 * @return the autoSize
	 */
	public boolean hasAutoSize() {
		return autoSize;
	}

	@Override
	public void load() {// Background colors
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
