package isotopestudio.backdoor.engine.components.render.components;

import static org.liquidengine.legui.system.renderer.nvg.util.NvgRenderUtils.*;
import static org.lwjgl.nanovg.NanoVG.*;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.liquidengine.legui.icon.Icon;
import org.liquidengine.legui.style.Style;
import org.liquidengine.legui.system.context.Context;
import org.liquidengine.legui.system.renderer.nvg.component.NvgDefaultComponentRenderer;
import org.liquidengine.legui.system.renderer.nvg.util.NvgShapes;

import isotopestudio.backdoor.engine.components.painting.Circle;

public class CircleRenderer extends NvgDefaultComponentRenderer<Circle> {

	@Override
	protected void renderComponent(Circle component, Context context, long nanovg) {
		createScissor(nanovg, component);
		{
			nvgSave(nanovg);
			if(component.getBackground() != null)
				NvgShapes.drawRect(nanovg, component.getAbsolutePosition(), new Vector2f(component.getRadius(), component.getRadius()), component.getBackground(), component.getRadius());
			NvgShapes.drawRectStroke(nanovg, component.getAbsolutePosition(), new Vector2f(component.getRadius(), component.getRadius()), component.getLineColor(), (float) component.getThickness(), component.getRadius());
			nvgRestore(nanovg);

		}
		resetScissor(nanovg);
	}
}
