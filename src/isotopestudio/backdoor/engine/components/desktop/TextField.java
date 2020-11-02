package isotopestudio.backdoor.engine.components.desktop;

import org.joml.Vector2f;
import org.liquidengine.legui.component.TextInput;

import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.datapack.DataParameters;

public class TextField extends TextInput implements IComponent {

	public TextField() {
		super();
		initIComponent();
	}

	public TextField(float x, float y, float width, float height) {
		super(x, y, width, height);
		initIComponent();
	}

	public TextField(String text, float x, float y, float width, float height) {
		super(text, x, y, width, height);
		initIComponent();
	}

	public TextField(String text, Vector2f position, Vector2f size) {
		super(text, position, size);
		initIComponent();
	}

	public TextField(String text) {
		super(text);
		initIComponent();
	}

	public TextField(Vector2f position, Vector2f size) {
		super(position, size);
		initIComponent();
	}
	
	@Override
	public void initIComponent() {
		getStyle().setFocusedStrokeColor(0, 0, 0, 0);
		IComponent.super.initIComponent();
	}

	@Override
	public void load() {
		
		// Background colors
		DataParameters.applyBackgroundColor(this, "textfield_background_color", getStyle());
		DataParameters.applyBackgroundColor(this, "textfield_background_color_focused", getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "textfield_background_color_hovered", getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "textfield_background_color_pressed", getPressedStyle());

		// Border colors
		DataParameters.applyBorderColor(this, "textfield_border_color", getStyle());
		DataParameters.applyBorderColor(this, "textfield_border_color_focused", getFocusedStyle());
		DataParameters.applyBorderColor(this, "textfield_border_color_hovered", getHoveredStyle());

		// Text color
		DataParameters.applyTextColor(this, "textfield_text_color", getStyle());
		DataParameters.applyTextColor(this, "textfield_text_color_focused", getFocusedStyle());
		DataParameters.applyTextColor(this, "textfield_text_color_hovered", getHoveredStyle());
		DataParameters.applyTextColor(this, "textfield_text_color_pressed", getPressedStyle());

		// Text font
		DataParameters.applyTextFont(this, "textfield_text_font", getStyle());
		DataParameters.applyTextFont(this, "textfield_text_font_focused", getFocusedStyle());
		DataParameters.applyTextFont(this, "textfield_text_font_hovered", getHoveredStyle());
		DataParameters.applyTextFont(this, "textfield_text_font_pressed", getPressedStyle());

		if (DataParameters.has(this, "textfield_border_radius")) {
			getStyle().setBorderRadius(DataParameters.getFloat(this, "textfield_border_radius"));
		}
	}

	@Override
	public void update() {
	}

	@Override
	public String getComponentName() {
		return "textfield";
	}
	
	private String variable;
	
	public void setVariable(String variable) {
		this.variable = variable;
	}

	@Override
	public String getComponentVariable() {
		return variable;
	}

}
