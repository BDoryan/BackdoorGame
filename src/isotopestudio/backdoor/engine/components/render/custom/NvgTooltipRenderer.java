package isotopestudio.backdoor.engine.components.render.custom;

import static org.liquidengine.legui.style.util.StyleUtilities.getInnerContentRectangle;
import static org.liquidengine.legui.style.util.StyleUtilities.getPadding;
import static org.liquidengine.legui.style.util.StyleUtilities.getStyle;
import static org.liquidengine.legui.system.renderer.nvg.util.NvgRenderUtils.createScissor;
import static org.liquidengine.legui.system.renderer.nvg.util.NvgRenderUtils.resetScissor;
import static org.lwjgl.nanovg.NanoVG.nvgFontFace;
import static org.lwjgl.nanovg.NanoVG.nvgFontSize;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.liquidengine.legui.component.optional.TextState;
import org.liquidengine.legui.component.optional.align.HorizontalAlign;
import org.liquidengine.legui.component.optional.align.VerticalAlign;
import org.liquidengine.legui.style.Style;
import org.liquidengine.legui.style.font.FontRegistry;
import org.liquidengine.legui.system.context.Context;
import org.liquidengine.legui.system.renderer.nvg.component.NvgDefaultComponentRenderer;

import isotopestudio.backdoor.engine.components.desktop.tooltip.Tooltip;

/**
 * Created by ShchAlexander on 13.02.2017.
 */
public class NvgTooltipRenderer extends NvgDefaultComponentRenderer<Tooltip> {

    @Override
    public void renderSelf(Tooltip component, Context context, long nanovg) {
        createScissor(nanovg, component);
        {
            Style style = component.getStyle();
            TextState textState = component.getTextState();
            Vector2f pos = component.getAbsolutePosition();
            Vector2f size = component.getSize();
            float fontSize = getStyle(component, Style::getFontSize, 16F);
            String font = getStyle(component, Style::getFont, FontRegistry.getDefaultFont());
            String text = textState.getText();
            HorizontalAlign horizontalAlign = getStyle(component, Style::getHorizontalAlign, HorizontalAlign.LEFT);
            VerticalAlign verticalAlign = getStyle(component, Style::getVerticalAlign, VerticalAlign.MIDDLE);
            Vector4f padding = getPadding(component, style);
			Vector4f rect = getInnerContentRectangle(pos, size, padding);

            renderBackground(component, context, nanovg);

            nvgFontSize(nanovg, fontSize);
            nvgFontFace(nanovg, font);
            
			String[] words = text.split(" ");
        }	
        resetScissor(nanovg);
    }
}
