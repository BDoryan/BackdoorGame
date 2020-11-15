package isotopestudio.backdoor.engine.components.render.components;

import static org.liquidengine.legui.style.util.StyleUtilities.getInnerContentRectangle;
import static org.liquidengine.legui.style.util.StyleUtilities.getPadding;
import static org.liquidengine.legui.style.util.StyleUtilities.getStyle;
import static org.liquidengine.legui.system.renderer.nvg.util.NvgRenderUtils.calculateTextBoundsRect;
import static org.liquidengine.legui.system.renderer.nvg.util.NvgRenderUtils.createScissor;
import static org.liquidengine.legui.system.renderer.nvg.util.NvgRenderUtils.resetScissor;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.liquidengine.legui.component.optional.TextState;
import org.liquidengine.legui.component.optional.align.HorizontalAlign;
import org.liquidengine.legui.component.optional.align.VerticalAlign;
import org.liquidengine.legui.listener.processor.EventProcessorProvider;
import org.liquidengine.legui.style.Style;
import org.liquidengine.legui.style.font.FontRegistry;
import org.liquidengine.legui.system.context.Context;
import org.liquidengine.legui.system.renderer.nvg.component.NvgDefaultComponentRenderer;
import org.liquidengine.legui.system.renderer.nvg.util.NvgShapes;
import org.liquidengine.legui.system.renderer.nvg.util.NvgText;

import isotopestudio.backdoor.engine.components.desktop.Text;
import isotopestudio.backdoor.engine.components.events.TextDynamicSizeChangeEvent;

/**
 * 
 * @author BDoryan
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
			//HashMap<Vector4f, String> lines = new HashMap<>();

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
				//lines.put(line_rect, final_line);
			}	
			
			EventProcessorProvider.getInstance().pushEvent(new TextDynamicSizeChangeEvent(text, context, text.getFrame(), size.x, fontSize * line_index));
		}
		resetScissor(nanovg);
	}
}
