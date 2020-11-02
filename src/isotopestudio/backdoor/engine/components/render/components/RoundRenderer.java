package isotopestudio.backdoor.engine.components.render.components;

import static org.liquidengine.legui.system.renderer.nvg.util.NvgRenderUtils.createScissor;
import static org.liquidengine.legui.system.renderer.nvg.util.NvgRenderUtils.getBorderRadius;
import static org.liquidengine.legui.system.renderer.nvg.util.NvgRenderUtils.resetScissor;
import static org.lwjgl.nanovg.NanoVG.nvgRestore;
import static org.lwjgl.nanovg.NanoVG.nvgSave;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.liquidengine.legui.icon.Icon;
import org.liquidengine.legui.style.Style;
import org.liquidengine.legui.system.context.Context;
import org.liquidengine.legui.system.renderer.nvg.component.NvgDefaultComponentRenderer;
import org.liquidengine.legui.system.renderer.nvg.util.NvgShapes;

import isotopestudio.backdoor.engine.components.painting.Round;

public class RoundRenderer extends NvgDefaultComponentRenderer<Round> {

	@Override
	protected void renderComponent(Round component, Context context, long nanovg) {
		createScissor(nanovg, component);
		{
			Style style = component.getStyle();
			Style currStyle = component.getStyle();

			nvgSave(nanovg);
			NvgShapes.drawRect(nanovg, component.getAbsolutePosition(), new Vector2f(component.getRadius(), component.getRadius()), component.getBackground(), component.getRadius());
			nvgRestore(nanovg);

		}
		resetScissor(nanovg);
	}
}
