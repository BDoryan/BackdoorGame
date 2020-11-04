package isotopestudio.backdoor.engine.components.events;

import org.liquidengine.legui.component.Frame;
import org.liquidengine.legui.event.Event;
import org.liquidengine.legui.system.context.Context;

import isotopestudio.backdoor.engine.components.desktop.Text;

/**
 * @author BESSIERE
 * @github https://www.github.com/DoryanBessiere/
 */
public class TextDynamicSizeChangeEvent extends Event<Text>{

	private float width;
	private float height;
	
	/**
	 * @param targetComponent
	 * @param context
	 * @param frame
	 */
	public TextDynamicSizeChangeEvent(Text targetComponent, Context context, Frame frame, float width, float height) {
		super(targetComponent, context, frame);
		this.width = width;
		this.height = height;
	}
	
	/**
	 * @return the width
	 */
	public float getWidth() {
		return width;
	}
	
	/**
	 * @return the height
	 */
	public float getHeight() {
		return height;
	}
}
