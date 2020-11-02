package isotopestudio.backdoor.engine.components.utils;

import org.joml.Vector2f;
import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.component.event.component.ChangeSizeEvent;
import org.liquidengine.legui.component.event.component.ChangeSizeEventListener;

public class Responsive {

	private Component component;
	private Float top, bottom, right, left;

	public Responsive(Component component, Float top, Float bottom, Float right, Float left) {
		this.component = component;
		this.top = top;
		this.bottom = bottom;
		this.right = right;
		this.left = left;
	}

	public void applyResponsive() {
		Vector2f position = component.getPosition();
		Vector2f size = component.getSize();

		if (component != null) {
			float width = -1;
			float height = -1;
			float x = -1;
			float y = -1;

			if (left != null) {
				x = left;

				if (right != null) {
					width = component.getParent().getSize().x - x - right;
				} else {
					width = size.x;
				}
			} else {
				width = size.x;

				if (right != null) {
					x = component.getParent().getSize().x - width - right;
				} else {
					x = position.x;
				}
			}

			if (top != null) {
				y = top;

				if (bottom != null) {
					height = component.getParent().getSize().y - y - bottom;
				} else {
					height = size.y;
				}
			} else {
				height = size.y;

				if (bottom != null) {
					y = component.getParent().getSize().y - height - bottom;
				} else {
					y = position.y;
				}
			}

			component.setPosition(new Vector2f(x, y));
			component.setSize(new Vector2f(width, height));
		}
		
		if(!component.getChildComponents().isEmpty()) {
		}
	}

	public Component getComponent() {
		return component;
	}

	public Float getTop() {
		return top;
	}

	public void setTop(Float top) {
		this.top = top;
	}

	public Float getBottom() {
		return bottom;
	}

	public void setBottom(Float bottom) {
		this.bottom = bottom;
	}

	public Float getRight() {
		return right;
	}

	public void setRight(Float right) {
		this.right = right;
	}

	public Float getLeft() {
		return left;
	}

	public void setLeft(Float left) {
		this.left = left;
	}
}
