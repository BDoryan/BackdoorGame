package isotopestudio.backdoor.game.game.component;

import org.joml.Vector4f;
import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.image.Image;

import isotopestudio.backdoor.core.elements.GameElement;
import isotopestudio.backdoor.engine.components.IComponent;

public abstract class GameElementComponent extends Component implements IComponent {

	private int radius;
	private int thickness;

	private Vector4f line_color = new Vector4f(0,0,0,0);
	private Vector4f background_color = new Vector4f(0,0,0,0);
	private Vector4f background_color_hovered = new Vector4f(0,0,0,0);

	private Image image;

	private int margin;
	
	private GameElement gameElement;

	public GameElementComponent(int x, int y, int radius, int thickness, int margin, GameElement gameElement) {
		this.gameElement = gameElement;
		this.radius = radius;
		this.thickness = thickness;

		this.margin = margin;

		getStyle().setLeft(x);
		getStyle().setTop(y);
		getStyle().setWidth(radius);
		getStyle().setHeight(radius);
		
		setPosition(x, y);
		setSize(radius, radius);
	}
	
	public GameElement getGameElement() {
		return gameElement;
	}
	
	public void setGameElement(GameElement gameElement) {
		this.gameElement = gameElement;
	}
	
	public void setImage(Image image) {
		this.image = image;
	}
	
	public int getMargin() {
		return margin;
	}
	
	public Vector4f getBackgroundColorHovered() {
		return background_color_hovered;
	}
	
	public Vector4f getBackgroundColor() {
		return background_color;
	}
	
	public Vector4f getLineColor() {
		return line_color;
	}
	
	public int getRadius() {
		return radius;
	}
	
	public int getThickness() {
		return thickness;
	}

	public abstract void load();
	
	public Image getImage() {
		return image;
	}

	@Override
	public String getComponentName() {
		return "node";
	}

	@Override
	public String getComponentVariable() {
		return null;
	}
}
