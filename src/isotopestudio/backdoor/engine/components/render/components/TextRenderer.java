package isotopestudio.backdoor.engine.components.render.components;

import static org.liquidengine.legui.style.util.StyleUtilities.getInnerContentRectangle;
import static org.liquidengine.legui.style.util.StyleUtilities.getPadding;
import static org.liquidengine.legui.style.util.StyleUtilities.getStyle;
import static org.liquidengine.legui.system.renderer.nvg.util.NvgRenderUtils.calculateTextBoundsRect;
import static org.liquidengine.legui.system.renderer.nvg.util.NvgRenderUtils.createScissor;
import static org.liquidengine.legui.system.renderer.nvg.util.NvgRenderUtils.resetScissor;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.liquidengine.legui.component.event.label.LabelWidthChangeEvent;
import org.liquidengine.legui.component.optional.TextState;
import org.liquidengine.legui.component.optional.align.HorizontalAlign;
import org.liquidengine.legui.component.optional.align.VerticalAlign;
import org.liquidengine.legui.listener.processor.EventProcessorProvider;
import org.liquidengine.legui.style.Style;
import org.liquidengine.legui.style.font.FontRegistry;
import org.liquidengine.legui.system.context.Context;
import org.liquidengine.legui.system.renderer.nvg.component.NvgDefaultComponentRenderer;
import org.liquidengine.legui.system.renderer.nvg.util.NvgRenderUtils;
import org.liquidengine.legui.system.renderer.nvg.util.NvgShapes;
import org.liquidengine.legui.system.renderer.nvg.util.NvgText;

import isotopestudio.backdoor.engine.components.desktop.Text;
import isotopestudio.backdoor.engine.components.events.TextDynamicSizeChangeEvent;

/**
 * 
 * @author BESSIERE Doryan
 * @github https://www.github.com/BDoryan/
 */
public class TextRenderer extends NvgDefaultComponentRenderer<Text> {

	@Override
	protected void renderComponent(Text text, Context context, long nanovg) {
		createScissor(nanovg, text);
		{
			Style style = text.getStyle();
			Vector2f pos = text.getAbsolutePosition();
			Vector2f size = text.getSize();

			renderBackground(text, context, nanovg);

			TextState textState = text.getTextState();
			Vector4f padding = getPadding(text, style);
			Vector4f rect = getInnerContentRectangle(pos, size, padding);
			Float fontSize = getStyle(text, Style::getFontSize, 16F);
			VerticalAlign verticalAlign = getStyle(text, Style::getVerticalAlign, VerticalAlign.MIDDLE);
			HorizontalAlign horizontalAlign = getStyle(text, Style::getHorizontalAlign, HorizontalAlign.LEFT);

			String[] words = textState.getText().split(" ");

			int line_index = 0;
			int word_index = 0;
			while (true) {
				if(word_index > words.length) {
					break;
				}

				String line = null;
				String final_line = null;
				while (word_index < words.length) {
					String word = words[word_index];
					word_index++;
					
					if(word.equals("\n"))
						break;

					line = (line == null ? word : line + " " + word);
					
					float[] line_bounds = calculateTextBoundsRect(nanovg, rect, line, horizontalAlign, verticalAlign,
							fontSize);
					if (line_bounds[2] > size.x) {
						word_index--;
						break;
					}
					final_line = line;
				}
				Vector4f line_rect = getInnerContentRectangle(new Vector2f(pos.x, pos.y + fontSize * line_index), size,
						padding);
				
				line_index++;

				if (final_line == null)
					break;
				
				NvgText.drawTextLineToRect(nanovg, line_rect, false, horizontalAlign, verticalAlign, fontSize,
						getStyle(text, Style::getFont, FontRegistry.getDefaultFont()), final_line,
						getStyle(text, Style::getTextColor), text.getTextDirection());
			}
			
	            EventProcessorProvider.getInstance().pushEvent(new TextDynamicSizeChangeEvent(text, context, text.getFrame(), size.x, fontSize * line_index));	
			
			/*
			String[] lines = new String[(int) (textBounds[2] / size.x)];
			String[] words = textState.getText().split(" ");
			
			int word_index = 0;

			for (int index_line = 0; index_line < lines.length; index_line++) {
				String line = null;
				while (word_index < words.length) {
					String word = words[word_index];
					word_index++;

					line = (line == null ? word : line + " " + word);

					float[] line_bounds = calculateTextBoundsRect(nanovg, rect, line, horizontalAlign, verticalAlign,
							fontSize);
					if (line_bounds[2] > size.x) {
						word_index--;
						break;
					}
					lines[index_line] = line;
				}
				Vector4f line_rect = getInnerContentRectangle(new Vector2f(pos.x, pos.y + fontSize * index_line), size,
						padding);

				if (lines[index_line] == null)
					continue;
				
				NvgText.drawTextLineToRect(nanovg, line_rect, false, horizontalAlign, verticalAlign, fontSize,
						getStyle(text, Style::getFont, FontRegistry.getDefaultFont()), lines[index_line],
						getStyle(text, Style::getTextColor), text.getTextDirection());
			}
			*/
		}
		resetScissor(nanovg);
	}
	
	private void renderBorder(Long nanovg, Vector2f size, Vector2f absolutePosition) {
		 float thickness = 1;
         Vector4f borderColor = new Vector4f(1,0,0,1) ;
         if (thickness <= 0 || borderColor.w == 0) {
             return;
         }

//         float cornerRadius = component.getBorderRadius();

         Vector2f bSize = new Vector2f(size);
         bSize.add(thickness, thickness);
         Vector2f bPos = new Vector2f(absolutePosition).sub(thickness / 2f, thickness / 2f);

         NvgShapes.drawRectStroke(nanovg, bPos, bSize, borderColor, thickness, 0);

	}
}
