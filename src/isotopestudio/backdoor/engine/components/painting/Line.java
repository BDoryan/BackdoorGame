package isotopestudio.backdoor.engine.components.painting;

import org.joml.Vector4f;
import org.liquidengine.legui.component.Component;

import isotopestudio.backdoor.engine.components.IComponent;

public class Line extends Component implements IComponent {

	private float thickness = 1;

	private float x;
	private float y;
	private float x2;
	private float y2;

	public Line(int thickness, float x3, float y3, float x4, float y4) {
		this.updateLocation(x3, y3, x4, y4);
		this.thickness = thickness;
		initIComponent();
	}

	private void updateLocation(float x, float y, float x2, float y2) {
		this.x = x;
		this.y = y;
		this.x2 = x2;
		this.y2 = y2;

		/*
		if (x2 < x) {
			throw new IllegalStateException("x can't be bigger than x2");
		}
		if (y2 < y) {
			throw new IllegalStateException("y can't be bigger than y2");
		}*/

		getStyle().setTop(y);
		getStyle().setLeft(x);
		
		setPosition(x, y);
	}	

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getX2() {
		return x2;
	}

	public float getY2() {
		return y2;
	}

	@Override
	public void load() {
	}

	@Override
	public void update() {
	}

	@Override
	public String getComponentName() {
		return "line";
	}

	@Override
	public String getComponentVariable() {
		return null;
	}

	public float getThickness() {
		return thickness;
	}

	private Vector4f color = new Vector4f(1,1,1,1);

	public void setColor(Vector4f color) {
		this.color = color;
	}

	public Vector4f getColor() {
		return color;
	}

	public void setThinkness(int thinkness) {
		this.thickness = thinkness;
	}
}
