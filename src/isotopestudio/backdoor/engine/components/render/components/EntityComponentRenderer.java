package isotopestudio.backdoor.engine.components.render.components;

import static org.liquidengine.legui.system.renderer.ImageRenderer.C_RADIUS;
import static org.liquidengine.legui.system.renderer.nvg.util.NvgRenderUtils.createScissor;
import static org.liquidengine.legui.system.renderer.nvg.util.NvgRenderUtils.resetScissor;
import static org.lwjgl.nanovg.NanoVG.nvgRestore;
import static org.lwjgl.nanovg.NanoVG.nvgSave;

import java.util.HashMap;
import java.util.Map;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector4f;
import org.liquidengine.legui.image.Image;
import org.liquidengine.legui.system.context.Context;
import org.liquidengine.legui.system.renderer.RendererProvider;
import org.liquidengine.legui.system.renderer.nvg.component.NvgDefaultComponentRenderer;
import org.liquidengine.legui.system.renderer.nvg.util.NvgShapes;

import isotopestudio.backdoor.game.game.component.GameElementComponent;

public class EntityComponentRenderer extends NvgDefaultComponentRenderer<GameElementComponent> {

	@Override
	protected void renderComponent(GameElementComponent component, Context context, long nanovg) {
		createScissor(nanovg, component);
		{
			nvgSave(nanovg);
			if(component.getBackgroundColor() != null)
			NvgShapes.drawRect(nanovg, component.getAbsolutePosition(), new Vector2f(component.getRadius(), component.getRadius()), component.getBackgroundColor(), component.getRadius());
			NvgShapes.drawRectStroke(nanovg, component.getAbsolutePosition(), new Vector2f(component.getRadius(), component.getRadius()), component.getLineColor(), (float) component.getThickness(), component.getRadius());
			if(component.getImage() != null) {
	            HashMap<String, Object> p = new HashMap<>();
	            p.put(C_RADIUS, new Vector4f());
	            Vector2f position = new Vector2f(component.getAbsolutePosition().x + component.getMargin(), component.getAbsolutePosition().y +  component.getMargin());
	            Vector2f size = new Vector2f(component.getRadius() - ( component.getMargin() * 2), component.getRadius()- ( component.getMargin() * 2));

	            renderImage(component.getImage(), position, size, p, context);
			}
			
			if(component.isHovered()) {
				if(component.getBackgroundColorHovered() != null)
					NvgShapes.drawRect(nanovg, new Vector2f(component.getAbsolutePosition().x + component.getThickness(), component.getAbsolutePosition().y + component.getThickness()), new Vector2f(component.getRadius() - (component.getThickness() * 2), component.getRadius() - (component.getThickness() * 2)), component.getBackgroundColorHovered(), component.getRadius());
			}
			nvgRestore(nanovg);

		}
		resetScissor(nanovg);
	}
	
    private void renderImage(Image image, Vector2fc position, Vector2fc size, Map<String, Object> properties, Context context) {
        if (image != null) {
            RendererProvider.getInstance().getImageRenderer(image.getClass()).render(image, position, size, properties, context);
        }
    }
}
