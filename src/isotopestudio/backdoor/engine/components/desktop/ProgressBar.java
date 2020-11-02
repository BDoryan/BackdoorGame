package isotopestudio.backdoor.engine.components.desktop;

import org.joml.Vector2f;
import org.joml.Vector4f;

import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.datapack.DataParameters;

public class ProgressBar extends org.liquidengine.legui.component.ProgressBar implements IComponent {

	public ProgressBar() {
		super();
		initIComponent();
	}

	public ProgressBar(float x, float y, float width, float height) {
		super(x, y, width, height);
		initIComponent();
	}

	public ProgressBar(Vector2f position, Vector2f size) {
		super(position, size);
		initIComponent();
	}

	@Override
	public void load() {
		// Background colors
		DataParameters.applyBackgroundColor(this, "progressbar_background_color", getStyle());
		DataParameters.applyBackgroundColor(this, "progressbar_background_color_focused", getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "progressbar_background_color_hovered", getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "progressbar_background_color_pressed", getPressedStyle());

		// Border colors
		DataParameters.applyBorderColor(this, "progressbar_border_color", getStyle());
		DataParameters.applyBorderColor(this, "progressbar_border_color_focused", getFocusedStyle());
		DataParameters.applyBorderColor(this, "progressbar_border_color_hovered", getHoveredStyle());
		DataParameters.applyBorderColor(this, "progressbar_border_color_pressed", getPressedStyle());

		// Background progress colors
		float[] color = DataParameters.getColor(this, "progressbar_progress_color");
		setProgressColor(new Vector4f(color[0], color[1], color[2], color[3]));
		
		if (DataParameters.has(this, "button_border_radius")) {
			getStyle().setBorderRadius(DataParameters.getFloat(this, "button_border_radius"));
		}
	}

	@Override
	public void update() {
	}

	@Override
	public String getComponentName() {
		return "progressbar";
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
