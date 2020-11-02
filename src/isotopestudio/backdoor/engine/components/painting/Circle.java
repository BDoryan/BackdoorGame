package isotopestudio.backdoor.engine.components.painting;

import org.joml.Vector4f;
import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.style.Style.DisplayType;

import isotopestudio.backdoor.engine.components.IComponent;

public class Circle extends Component implements IComponent {

	private int radius;
	private int thickness = 1;

	private Vector4f line_color = new Vector4f(0f, 0f, 0f, 1f);
	private Vector4f background;

	public Circle(int x, int y, int radius, int thickness) {
		this(x, y, radius);
		this.thickness = thickness;
	}

	public Circle(int x, int y, int radius) {
		this.radius = radius;
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

	public void setLineColor(Vector4f line_color) {
		this.line_color = line_color;
	}

	public Vector4f getLineColor() {
		return line_color;
	}

	public void setBackground(Vector4f background) {
		this.background = background;
	}

	public Vector4f getBackground() {
		return background;
	}

	@Override
	public String getComponentName() {
		return "circle";
	}

	@Override
	public String getComponentVariable() {
		return null;
	}

	public int getRadius() {
		return radius;
	}

	public int getThickness() {
		return thickness;
	}
}
