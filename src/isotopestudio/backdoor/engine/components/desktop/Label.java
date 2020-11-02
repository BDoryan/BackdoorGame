package isotopestudio.backdoor.engine.components.desktop;

import org.joml.Vector2f;
import org.liquidengine.legui.component.event.label.LabelContentChangeEvent;
import org.liquidengine.legui.component.event.label.LabelContentChangeEventListener;
import org.liquidengine.legui.component.event.label.LabelWidthChangeEvent;
import org.liquidengine.legui.component.event.label.LabelWidthChangeEventListener;
import org.liquidengine.legui.component.misc.listener.label.UpdateLabelStyleWidthListener;
import org.liquidengine.legui.component.misc.listener.label.UpdateLabelWidthListener;
import org.liquidengine.legui.style.Style;

import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.components.events.LabelSizeEvent;
import isotopestudio.backdoor.engine.datapack.DataParameters;

public class Label extends org.liquidengine.legui.component.Label implements IComponent {

	public Label(LabelSizeEvent event) {
		super();
		initLabel(event);
	}

	public Label() {
		super();
		initLabel(null);
	}

	public Label(float x, float y, float width, float height,LabelSizeEvent event) {
		super(x, y, width, height);
		initLabel(event);
	}

	public Label(float x, float y, float width, float height) {
		super(x, y, width, height);
		initLabel(null);
	}

	public Label(String text, float x, float y, float width, float height,LabelSizeEvent event) {
		super(text, x, y, width, height);
		initLabel(event);
	}

	public Label(String text, float x, float y, float width, float height) {
		super(text, x, y, width, height);
		initLabel(null);
	}

	public Label(String text, Vector2f position, Vector2f size,LabelSizeEvent event) {
		super(text, position, size);
		initLabel(event);
	}

	public Label(String text,LabelSizeEvent event) {
		super(text);
		initLabel(event);
	}

	public Label(String text) {
		super(text);
		initLabel(null);
	}

	public Label(Vector2f position, Vector2f size,LabelSizeEvent event) {
		super(position, size);
		initLabel(event);
	}
	
	private void initLabel(LabelSizeEvent event_) {
		initIComponent();

		Style style = getStyle();
		style.setHeight(style.getFontSize());
		setSize(getSize().x, style.getFontSize());

		getListenerMap().addListener(LabelContentChangeEvent.class, new LabelContentChangeEventListener() {
			@Override
			public void process(LabelContentChangeEvent event) {
				style.setHeight(style.getFontSize());
				setSize(getSize().x, style.getFontSize());
			}
		});

		getListenerMap().addListener(LabelWidthChangeEvent.class, new LabelWidthChangeEventListener() {
			@Override
			public void process(LabelWidthChangeEvent event) {
				style.setWidth(event.getWidth()); 
				setSize(event.getWidth(), getSize().y);
				
				if(event_ != null) {
					if(getStyle().getFontSize() != null)
					event_.process(event.getWidth(), getStyle().getFontSize());
				}
			}
		});
	}

	@Override
	public void load() {
		// Background colors
		DataParameters.applyBackgroundColor(this, "label_background_color", getStyle());
		DataParameters.applyBackgroundColor(this, "label_background_color_focused", getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "label_background_color_hovered", getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "label_background_color_pressed", getPressedStyle());

		// Border colors
		DataParameters.applyBorderColor(this, "label_border_color", getStyle());
		DataParameters.applyBorderColor(this, "label_border_color_focused", getFocusedStyle());
		DataParameters.applyBorderColor(this, "label_border_color_hovered", getHoveredStyle());
		DataParameters.applyBorderColor(this, "label_border_color_pressed", getPressedStyle());
		
		// Title color
		DataParameters.applyTextColor(this, "label_text_color", getStyle());
		DataParameters.applyTextColor(this, "label_text_color_focused", getFocusedStyle());
		DataParameters.applyTextColor(this, "label_text_color_hovered", getHoveredStyle());
		DataParameters.applyTextColor(this, "label_text_color_pressed", getPressedStyle());

		// Title font
		DataParameters.applyTextFont(this, "label_text_font", getStyle());
		DataParameters.applyTextFont(this, "label_text_font_focused", getFocusedStyle());
		DataParameters.applyTextFont(this, "label_text_font_hovered", getHoveredStyle());
		DataParameters.applyTextFont(this, "label_text_font_pressed", getPressedStyle());

		if (DataParameters.has(this, "label_border_radius")) {
			getStyle().setBorderRadius(DataParameters.getFloat(this, "label_border_radius"));
		}
	}

	@Override
	public void update() {
	}

	@Override
	public String getComponentName() {
		return "label";
	}

	private String variable;

	public void setVariable(String variable) {
		this.variable = variable;
	}

	@Override
	public String getComponentVariable() {
		return variable;
	}

	public void autoSizeWidth() {
		getListenerMap().addListener(LabelWidthChangeEvent.class, new UpdateLabelWidthListener() {
			@Override
			public void process(LabelWidthChangeEvent event) {
				super.process(event);
			}
		});
		getListenerMap().addListener(LabelWidthChangeEvent.class, new UpdateLabelStyleWidthListener() {
			@Override
			public void process(LabelWidthChangeEvent event) {
				super.process(event);
			}
		});
	}
}
