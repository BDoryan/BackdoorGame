package isotopestudio.backdoor.engine.components.desktop;

import java.util.ArrayList;
import java.util.List;

import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.component.event.label.LabelContentChangeEvent;
import org.liquidengine.legui.component.event.label.LabelWidthChangeEvent;
import org.liquidengine.legui.component.event.label.LabelWidthChangeEventListener;
import org.liquidengine.legui.component.optional.TextState;
import org.liquidengine.legui.style.font.TextDirection;

import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.datapack.DataParameters;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class Text extends Component implements IComponent {

	private TextState text;
    private TextDirection textDirection = TextDirection.HORIZONTAL;
    
	public Text(String text) {
		this.text = new TextState(text);

		getStyle().getBackground().setColor(0, 0, 0, 0f);
		getStyle().setShadow(null);
	}

	public TextState getTextState() {
		return text;
	}
	
	public TextDirection getTextDirection() {
		return textDirection;
	}
	
	public void setTextDirection(TextDirection textDirection) {
		this.textDirection = textDirection;
	}

	@Override
	public void load() {
		// Title color
		DataParameters.applyTextColor(this, "text_color", getStyle());
		DataParameters.applyTextColor(this, "text_color_focused", getFocusedStyle());
		DataParameters.applyTextColor(this, "text_color_hovered", getHoveredStyle());
		DataParameters.applyTextColor(this, "text_color_pressed", getPressedStyle());

		// Title font
		DataParameters.applyTextFont(this, "text_font", getStyle());
		DataParameters.applyTextFont(this, "text_font_focused", getFocusedStyle());
		DataParameters.applyTextFont(this, "text_font_hovered", getHoveredStyle());
		DataParameters.applyTextFont(this, "text_font_pressed", getPressedStyle());

		if (DataParameters.has(this, "border_radius")) {
			getStyle().setBorderRadius(DataParameters.getFloat(this, "border_radius"));
		}
	}

	@Override
	public String getComponentName() {
		return "text";
	}

	@Override
	public void update() {
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