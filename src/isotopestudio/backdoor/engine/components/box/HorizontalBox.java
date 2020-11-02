package isotopestudio.backdoor.engine.components.box;

import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.component.optional.align.HorizontalAlign;
import org.liquidengine.legui.style.Style.DisplayType;

public class HorizontalBox extends Panel {

	private int spacing;

	public HorizontalBox(int spacing) {
		this.spacing = spacing;
		
		getStyle().setDisplay(DisplayType.FLEX);
	}

	public int getSpacing() {
		return spacing;
	}

	public boolean addComponent(Component component) {
		boolean result = add(component);
		if (result)
			updateComponentsLocation();
		return result;
	}

	public boolean removeComponent(Component component) {
		boolean result = remove(component);
		if (result)
			updateComponentsLocation();
		return result;
	}
	
	private HorizontalAlign align = HorizontalAlign.LEFT;
	
	public void setHorizontalAlign(HorizontalAlign align) {
		this.align = align;
		updateComponentsLocation();
	}

	public void updateComponentsLocation() {
		float location = 0;
		if(getChildComponents().size() == 0)return;
		for(Component component : getChildComponents()) {
			component.getStyle().setTop(0f);
			component.getStyle().setBottom(0f);
			if(align == HorizontalAlign.LEFT) {
				component.getStyle().setLeft(location);
				//component.getStyle().setRight(null);
			} else if(align == HorizontalAlign.RIGHT) {
				component.getStyle().setRight(location);
				//component.getStyle().setLeft(null);
			}
			location += spacing + (component.getStyle().getWidth() == null ? component.getSize().x : (float) component.getStyle().getWidth().get());
		}
	}
}
