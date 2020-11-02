package isotopestudio.backdoor.engine.components.render.components;

import static org.liquidengine.legui.system.renderer.nvg.util.NvgRenderUtils.createScissor;
import static org.liquidengine.legui.system.renderer.nvg.util.NvgRenderUtils.resetScissor;
import static org.lwjgl.nanovg.NanoVG.nvgRestore;
import static org.lwjgl.nanovg.NanoVG.nvgSave;

import org.joml.Vector4f;
import org.liquidengine.legui.system.context.Context;
import org.liquidengine.legui.system.renderer.nvg.component.NvgDefaultComponentRenderer;
import org.liquidengine.legui.system.renderer.nvg.util.NvgShapes;
import org.lwjgl.nanovg.NanoVG;

import isotopestudio.backdoor.engine.components.painting.Line;

public class LineRenderer extends NvgDefaultComponentRenderer<Line> {

	@Override
	protected void renderComponent(Line component, Context context, long nanovg) {
		createScissor(nanovg, component);
		{
			nvgSave(nanovg);
			float thinkness = component.getThickness();
			Vector4f color = component.getColor();
			
			NvgShapes.drawLine(nanovg, thinkness, color, NanoVG.NVG_ROUND, component.getAbsolutePosition().x, component.getAbsolutePosition().y, component.getAbsolutePosition().x + (component.getX2() - component.getX()),
					component.getAbsolutePosition().y + (component.getY2() - component.getY()));
			
			/*
			MyNvgShapes.drawLine(nanovg,
					new Vector4f(component.getAbsolutePosition().x, component.getAbsolutePosition().y,
							component.getAbsolutePosition().x + (component.getX2() - component.getX()),
							component.getAbsolutePosition().y + (component.getY2() - component.getY())
							), component.getThickness(), component.getColor());
							*/
			nvgRestore(nanovg);

		}
		resetScissor(nanovg);
	}
}
