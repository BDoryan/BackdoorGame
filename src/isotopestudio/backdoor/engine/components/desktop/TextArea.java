package isotopestudio.backdoor.engine.components.desktop;

import org.joml.Vector2f;

import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.datapack.DataParameters;

public class TextArea extends org.liquidengine.legui.component.TextArea implements IComponent {

	public TextArea() {
		super();
		initIComponent();
	}

	public TextArea(float x, float y, float width, float height) {
		super(x, y, width, height);
		initIComponent();
	}

	public TextArea(Vector2f position, Vector2f size) {
		super(position, size);
		initIComponent();
	}

	@Override
	public void load() {
		getHorizontalScrollBar().getStyle().getBackground().setColor(0, 0, 0, 1f);
		
		// Background colors
		DataParameters.applyBackgroundColor(this, "textarea_background_color", getTextAreaField().getStyle());
		DataParameters.applyBackgroundColor(this, "textarea_background_color_focused", getTextAreaField().getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "textarea_background_color_hovered", getTextAreaField().getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "textarea_background_color_pressed", getTextAreaField().getPressedStyle());

		// Background  colors
		DataParameters.applyBackgroundColor(this, "textarea_background_color", getViewport().getStyle());
		DataParameters.applyBackgroundColor(this, "textarea_background_color_focused", getViewport().getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "textarea_background_color_hovered", getViewport().getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "textarea_background_color_pressed", getViewport().getPressedStyle());

		// Background  colors
		DataParameters.applyBackgroundColor(this, "textarea_background_color", getStyle());
		DataParameters.applyBackgroundColor(this, "textarea_background_color_focused", getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "textarea_background_color_hovered", getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "textarea_background_color_pressed", getPressedStyle());

		// Border colors
		DataParameters.applyBorderColor(this, "textarea_border_color", getStyle());
		DataParameters.applyBorderColor(this, "textarea_border_color_focused", getFocusedStyle());
		DataParameters.applyBorderColor(this, "textarea_border_color_hovered", getHoveredStyle());
		DataParameters.applyBorderColor(this, "textarea_border_color_pressed", getPressedStyle());

		// Background scrollbar colors
		DataParameters.applyBackgroundColor(this, "textarea_scrollbar_background_color", getHorizontalScrollBar().getStyle());
		DataParameters.applyBackgroundColor(this, "textarea_scrollbar_background_color_focused", getHorizontalScrollBar().getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "textarea_scrollbar_background_color_hovered", getHorizontalScrollBar().getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "textarea_scrollbar_background_color_pressed", getHorizontalScrollBar().getPressedStyle());

		// Border scrollbar colors
		DataParameters.applyBorderColor(this, "textarea_scrollbar_border_color", getHorizontalScrollBar().getStyle());
		DataParameters.applyBorderColor(this, "textarea_scrollbar_border_color_focused", getHorizontalScrollBar().getFocusedStyle());
		DataParameters.applyBorderColor(this, "textarea_scrollbar_border_color_hovered", getHorizontalScrollBar().getHoveredStyle());
		DataParameters.applyBorderColor(this, "textarea_scrollbar_border_color_pressed", getHorizontalScrollBar().getPressedStyle());

		// Background scrollbar colors
		DataParameters.applyBackgroundColor(this, "textarea_scrollbar_background_color", getVerticalScrollBar().getStyle());
		DataParameters.applyBackgroundColor(this, "textarea_scrollbar_background_color_focused", getVerticalScrollBar().getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "textarea_scrollbar_background_color_hovered", getVerticalScrollBar().getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "textarea_scrollbar_background_color_pressed", getVerticalScrollBar().getPressedStyle());

		// Border scrollbar colors
		DataParameters.applyBorderColor(this, "textarea_scrollbar_border_color", getVerticalScrollBar().getStyle());
		DataParameters.applyBorderColor(this, "textarea_scrollbar_border_color_focused", getVerticalScrollBar().getFocusedStyle());
		DataParameters.applyBorderColor(this, "textarea_scrollbar_border_color_hovered", getVerticalScrollBar().getHoveredStyle());
		DataParameters.applyBorderColor(this, "textarea_scrollbar_border_color_pressed", getVerticalScrollBar().getPressedStyle());

		// Text color
		DataParameters.applyTextColor(this, "textarea_text_color", getTextAreaField().getStyle());
		DataParameters.applyTextColor(this, "textarea_text_color_focused", getTextAreaField().getFocusedStyle());
		DataParameters.applyTextColor(this, "textarea_text_color_hovered", getTextAreaField().getHoveredStyle());
		DataParameters.applyTextColor(this, "textarea_text_color_pressed", getTextAreaField().getPressedStyle());

		// Text font
		DataParameters.applyTextFont(this, "textarea_text_font", getTextAreaField().getStyle());
		DataParameters.applyTextFont(this, "textarea_text_font_focused", getTextAreaField().getFocusedStyle());
		DataParameters.applyTextFont(this, "textarea_text_font_hovered", getTextAreaField().getHoveredStyle());
		DataParameters.applyTextFont(this, "textarea_text_font_pressed", getTextAreaField().getPressedStyle());

		if (DataParameters.has(this, "textarea_border_radius")) {
			getStyle().setBorderRadius(DataParameters.getFloat(this, "textarea_border_radius"));
		}
		
		// Background colors
		DataParameters.applyBackgroundColor(this, "textarea_background_color", getTextAreaField().getStyle());
		DataParameters.applyBackgroundColor(this, "textarea_background_color_focused", getTextAreaField().getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "textarea_background_color_hovered", getTextAreaField().getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "textarea_background_color_pressed", getTextAreaField().getPressedStyle());

		// Background  colors
		DataParameters.applyBackgroundColor(this, "textarea_background_color", getViewport().getStyle());
		DataParameters.applyBackgroundColor(this, "textarea_background_color_focused", getViewport().getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "textarea_background_color_hovered", getViewport().getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "textarea_background_color_pressed", getViewport().getPressedStyle());

		// Background  colors
		DataParameters.applyBackgroundColor(this, "textarea_background_color", getStyle());
		DataParameters.applyBackgroundColor(this, "textarea_background_color_focused", getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "textarea_background_color_hovered", getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "textarea_background_color_pressed", getPressedStyle());

		// Border colors
		DataParameters.applyBorderColor(this, "textarea_border_color", getStyle());
		DataParameters.applyBorderColor(this, "textarea_border_color_focused", getFocusedStyle());
		DataParameters.applyBorderColor(this, "textarea_border_color_hovered", getHoveredStyle());
		DataParameters.applyBorderColor(this, "textarea_border_color_pressed", getPressedStyle());

		// Background scrollbar colors
		DataParameters.applyBackgroundColor(this, "textarea_scrollbar_background_color", getHorizontalScrollBar().getStyle());
		DataParameters.applyBackgroundColor(this, "textarea_scrollbar_background_color_focused", getHorizontalScrollBar().getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "textarea_scrollbar_background_color_hovered", getHorizontalScrollBar().getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "textarea_scrollbar_background_color_pressed", getHorizontalScrollBar().getPressedStyle());

		// Border scrollbar colors
		DataParameters.applyBorderColor(this, "textarea_scrollbar_border_color", getHorizontalScrollBar().getStyle());
		DataParameters.applyBorderColor(this, "textarea_scrollbar_border_color_focused", getHorizontalScrollBar().getFocusedStyle());
		DataParameters.applyBorderColor(this, "textarea_scrollbar_border_color_hovered", getHorizontalScrollBar().getHoveredStyle());
		DataParameters.applyBorderColor(this, "textarea_scrollbar_border_color_pressed", getHorizontalScrollBar().getPressedStyle());

		// Background scrollbar colors
		DataParameters.applyBackgroundColor(this, "textarea_scrollbar_background_color", getVerticalScrollBar().getStyle());
		DataParameters.applyBackgroundColor(this, "textarea_scrollbar_background_color_focused", getVerticalScrollBar().getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "textarea_scrollbar_background_color_hovered", getVerticalScrollBar().getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "textarea_scrollbar_background_color_pressed", getVerticalScrollBar().getPressedStyle());

		// Border scrollbar colors
		DataParameters.applyBorderColor(this, "textarea_scrollbar_border_color", getVerticalScrollBar().getStyle());
		DataParameters.applyBorderColor(this, "textarea_scrollbar_border_color_focused", getVerticalScrollBar().getFocusedStyle());
		DataParameters.applyBorderColor(this, "textarea_scrollbar_border_color_hovered", getVerticalScrollBar().getHoveredStyle());
		DataParameters.applyBorderColor(this, "textarea_scrollbar_border_color_pressed", getVerticalScrollBar().getPressedStyle());

		// Text color
		DataParameters.applyTextColor(this, "textarea_text_color", getTextAreaField().getStyle());
		DataParameters.applyTextColor(this, "textarea_text_color_focused", getTextAreaField().getFocusedStyle());
		DataParameters.applyTextColor(this, "textarea_text_color_hovered", getTextAreaField().getHoveredStyle());
		DataParameters.applyTextColor(this, "textarea_text_color_pressed", getTextAreaField().getPressedStyle());

		// Text font
		DataParameters.applyTextFont(this, "textarea_text_font", getTextAreaField().getStyle());
		DataParameters.applyTextFont(this, "textarea_text_font_focused", getTextAreaField().getFocusedStyle());
		DataParameters.applyTextFont(this, "textarea_text_font_hovered", getTextAreaField().getHoveredStyle());
		DataParameters.applyTextFont(this, "textarea_text_font_pressed", getTextAreaField().getPressedStyle());

		if (DataParameters.has(this, "textarea_border_radius")) {
			getStyle().setBorderRadius(DataParameters.getFloat(this, "textarea_border_radius"));
		}
	}

	@Override
	public void update() {
	}

	@Override
	public String getComponentName() {
		return "textarea";
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
