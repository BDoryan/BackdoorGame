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
import org.liquidengine.legui.component.optional.TextState;
import org.liquidengine.legui.component.optional.align.HorizontalAlign;
import org.liquidengine.legui.component.optional.align.VerticalAlign;
import org.liquidengine.legui.style.Style;
import org.liquidengine.legui.style.font.FontRegistry;
import org.liquidengine.legui.system.context.Context;
import org.liquidengine.legui.system.renderer.nvg.component.NvgDefaultComponentRenderer;
import org.liquidengine.legui.system.renderer.nvg.util.NvgText;

import isotopestudio.backdoor.engine.components.desktop.Text;

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
}
