package isotopestudio.backdoor.engine.components.render;

import static org.lwjgl.nanovg.NanoVG.*;

import org.joml.Vector4f;
import org.liquidengine.legui.system.renderer.nvg.util.NvgColorUtil;
import org.lwjgl.nanovg.NVGColor;

public class MyNvgShapes {

	public static void drawLine(long context, Vector4f positions, float thinkness, Vector4f color) {
		try (NVGColor colorA = NVGColor.calloc()) {
			NvgColorUtil.fillNvgColorWithRGBA(color, colorA);
			nvgLineCap(context, NVG_ROUND);
			nvgLineJoin(context, NVG_ROUND);
	        nvgShapeAntiAlias(context, true);
			nvgBeginPath(context);
			nvgMoveTo(context, positions.x, positions.y);
			nvgLineTo(context, positions.z, positions.w);
			nvgStrokeWidth(context, thinkness);
			nvgStrokeColor(context, colorA);
			nvgStroke(context);
		}
	}
}
