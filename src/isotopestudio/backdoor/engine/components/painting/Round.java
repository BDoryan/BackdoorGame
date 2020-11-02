package isotopestudio.backdoor.engine.components.painting;

import org.joml.Vector4f;
import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.style.Style.DisplayType;

import isotopestudio.backdoor.engine.components.IComponent;

public class Round extends Component implements IComponent {

	private int radius;

	private Vector4f background = new Vector4f(0f, 0f, 0f, 1f);

	public Round(int x, int y, int radius) {
		this.radius = radius;
		getStyle().getBackground().setColor(new Vector4f(0, 0, 0, 1f));
		getStyle().setTop(x);
		getStyle().setLeft(y);
		getStyle().setWidth(radius);
		getStyle().setHeight(radius);
		setPosition(x, y);
		setSize(radius, radius);
		initIComponent();
	}

	@Override
	public void load() {
	}

	@Override
	public void update() {
	}

	@Override
	public String getComponentName() {
		return "round";
	}

	public void setBackground(Vector4f background) {
		this.background = background;
	}

	public Vector4f getBackground() {
		return background;
	}

	private String variable;

	public void setVariable(String variable) {
		this.variable = variable;
	}

	@Override
	public String getComponentVariable() {
		return variable;
	}

	public int getRadius() {
		return radius;
	}

}
