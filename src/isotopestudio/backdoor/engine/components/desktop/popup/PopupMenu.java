package isotopestudio.backdoor.engine.components.desktop.popup;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.bytedeco.opencv.opencv_core.float16_t;
import org.joml.Vector2f;
import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.style.Style.DisplayType;

import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.datapack.DataParameters;

/**
 * @author BESSIERE Doryan
 * @github https://www.github.com/DoryanBessiere/
 */
public class PopupMenu extends Panel implements IComponent {

	public PopupMenu(int x, int y) {
		setPosition(new Vector2f(x, y));

		getStyle().setLeft(x);
		getStyle().setTop(y);

		setFocusable(true);
	}

	@Override
	public boolean add(Component component) {
		boolean result = super.add(component);
		if(component instanceof IComponent)
			((IComponent) component).load();
		return result;
	}

	@Override
	public boolean remove(Component component) {
		boolean result = super.remove(component);
		return result;
	}

	@Override
	public void setFocused(boolean focused) {
		super.setFocused(focused);
		if (!focused) {
			if(getParent() != null)
			getParent().remove(this);
		}
	}
	
	public void initSize() {
		float top = 0f;
		float space = 5f;
		for(Component component : getChildComponents()) {
			if(getStyle().getDisplay() == DisplayType.FLEX) {
				/*if(getStyle().getWidth().get() == null || (getStyle().getWidth().get() != null && (float) popupmenu.getStyle().getWidth().get() < width)) {
					getStyle().setWidth(width);
				}
				if(getStyle().getHeight() != null) {
					getStyle().setHeight((float) getStyle().getHeight().get() + height);	
				} else {
					getStyle().setHeight(height);	
				}*/
			} else {
				float width = (component.getStyle().getDisplay() == DisplayType.FLEX ? (component.getStyle().getWidth() != null ? (float) component.getStyle().getWidth().get() : 0) : component.getSize().x) + (component.getStyle().getPaddingLeft() != null ?(float) component.getStyle().getPaddingLeft().get() : 0) + (component.getStyle().getPaddingRight() != null ? (float)component.getStyle().getPaddingRight().get() : 0);
				float height = component.getStyle().getDisplay() == DisplayType.FLEX ? (component.getStyle().getHeight() != null ? (float) component.getStyle().getHeight().get() : 0) : component.getSize().y;
				
				float widthSize = getSize().x;
				if(getSize().x < width) {
					widthSize = width;
				}
				
				component.setPosition(component.getPosition().x, top);
				top += space + height;

				setSize(widthSize, getSize().y + space + height);
			}	
			setSize(getSize().x, top);
		}
	}

	@Override
	public void load() {
		// Background colors
		DataParameters.applyBackgroundColor(this, "popupmenu_background_color", getStyle());
		DataParameters.applyBackgroundColor(this, "popupmenu_background_color_focused", getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "popupmenu_background_color_hovered", getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "popupmenu_background_color_pressed", getPressedStyle());

		// Border colors
		DataParameters.applyBorderColor(this, "popupmenu_border_color", getStyle());
		DataParameters.applyBorderColor(this, "popupmenu_border_color_focused", getFocusedStyle());
		DataParameters.applyBorderColor(this, "popupmenu_border_color_hovered", getHoveredStyle());
		DataParameters.applyBorderColor(this, "popupmenu_border_color_pressed", getPressedStyle());
	}

	@Override
	public void update() {
	}

	@Override
	public String getComponentName() {
		return "popupmenu";
	}

	private String variable;

	/**
	 * @param variable the variable to set
	 */
	public void setVariable(String variable) {
		this.variable = variable;
	}

	@Override
	public String getComponentVariable() {
		return this.variable;
	}
}
