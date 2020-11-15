package isotopestudio.backdoor.engine.components.desktop.checkbox;

import org.joml.Vector2f;
import org.liquidengine.legui.icon.CharIcon;

import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.datapack.DataParameters;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class CheckBox extends org.liquidengine.legui.component.CheckBox implements IComponent {

	public CheckBox() {
		super();
		initIComponent();
	}

	public CheckBox(float x, float y, float width, float height) {
		super(x, y, width, height);
		initIComponent();
	}

	public CheckBox(String text, float x, float y, float width, float height) {
		super(text, x, y, width, height);
		initIComponent();
	}

	public CheckBox(String text, Vector2f position, Vector2f size) {
		super(text, position, size);
		initIComponent();
	}

	public CheckBox(String text) {
		super(text);
		initIComponent();
	}

	public CheckBox(Vector2f position, Vector2f size) {
		super(position, size);
		initIComponent();
	}

	@Override
	public void load() {
		// Background colors
		DataParameters.applyBackgroundColor(this, "checkbox_background_color", getStyle());
		DataParameters.applyBackgroundColor(this, "checkbox_background_color_focused", getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "checkbox_background_color_hovered", getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "checkbox_background_color_pressed", getPressedStyle());

		// Border colors
		DataParameters.applyBorderColor(this, "checkbox_border_color", getStyle());
		DataParameters.applyBorderColor(this, "checkbox_border_color_focused", getFocusedStyle());
		DataParameters.applyBorderColor(this, "checkbox_border_color_hovered", getHoveredStyle());
		DataParameters.applyBorderColor(this, "checkbox_border_color_pressed", getPressedStyle());

		((CharIcon)this.getIconUnchecked()).setColor(DataParameters.convertColor(DataParameters.getColor(this, "checkbox_uncheck_color")));
		((CharIcon)this.getIconChecked()).setColor(DataParameters.convertColor(DataParameters.getColor(this, "checkbox_check_color")));
		
		// Title color
		DataParameters.applyTextColor(this, "checkbox_text_color", getStyle());
		DataParameters.applyTextColor(this, "checkbox_text_color_focused", getFocusedStyle());
		DataParameters.applyTextColor(this, "checkbox_text_color_hovered", getHoveredStyle());
		DataParameters.applyTextColor(this, "checkbox_text_color_pressed", getPressedStyle());

		// Title font
		DataParameters.applyTextFont(this, "checkbox_text_font", getStyle());
		DataParameters.applyTextFont(this, "checkbox_text_font_focused", getFocusedStyle());
		DataParameters.applyTextFont(this, "checkbox_text_font_hovered", getHoveredStyle());
		DataParameters.applyTextFont(this, "checkbox_text_font_pressed", getPressedStyle());

		if (DataParameters.has(this, "checkbox_border_radius")) {
			getStyle().setBorderRadius(DataParameters.getFloat(this, "checkbox_border_radius"));
		}
	}

	@Override
	public void update() {
		
	}

	@Override
	public String getComponentName() {
		return "checkbox";
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
