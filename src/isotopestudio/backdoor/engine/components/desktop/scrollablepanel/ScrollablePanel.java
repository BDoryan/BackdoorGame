package isotopestudio.backdoor.engine.components.desktop.scrollablepanel;

import java.util.Random;

import org.joml.Vector2f;
import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.component.event.slider.SliderChangeValueEvent;
import org.liquidengine.legui.component.event.slider.SliderChangeValueEventListener;
import org.liquidengine.legui.style.Style.DisplayType;

import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.components.desktop.window.Window;
import isotopestudio.backdoor.engine.datapack.DataParameters;
import isotopestudio.backdoor.game.command.ICommand;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class ScrollablePanel extends org.liquidengine.legui.component.ScrollablePanel implements IComponent {

	public ScrollablePanel() {
		super();
	}

	public ScrollablePanel(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	public ScrollablePanel(Vector2f position, Vector2f size) {
		super(position, size);
	}

	@Override
	public void load() {
		// Background colors
		DataParameters.applyBackgroundColor(this, "scrollablepanel_background_color", getContainer().getStyle());
		DataParameters.applyBackgroundColor(this, "scrollablepanel_background_color_focused", getContainer().getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "scrollablepanel_background_color_hovered", getContainer().getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "scrollablepanel_background_color_pressed", getContainer().getPressedStyle());

		// Background  colors
		DataParameters.applyBackgroundColor(this, "scrollablepanel_background_color", getViewport().getStyle());
		DataParameters.applyBackgroundColor(this, "scrollablepanel_background_color_focused", getViewport().getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "scrollablepanel_background_color_hovered", getViewport().getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "scrollablepanel_background_color_pressed", getViewport().getPressedStyle());

		// Background  colors
		DataParameters.applyBackgroundColor(this, "scrollablepanel_background_color", getStyle());
		DataParameters.applyBackgroundColor(this, "scrollablepanel_background_color_focused", getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "scrollablepanel_background_color_hovered", getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "scrollablepanel_background_color_pressed", getPressedStyle());

		// Border colors
		DataParameters.applyBorderColor(this, "scrollablepanel_border_color", getStyle());
		DataParameters.applyBorderColor(this, "scrollablepanel_border_color_focused", getFocusedStyle());
		DataParameters.applyBorderColor(this, "scrollablepanel_border_color_hovered", getHoveredStyle());
		DataParameters.applyBorderColor(this, "scrollablepanel_border_color_pressed", getPressedStyle());

		// Background scrollbar colors
		DataParameters.applyBackgroundColor(this, "scrollablepanel_scrollbar_background_color", getHorizontalScrollBar().getStyle());
		DataParameters.applyBackgroundColor(this, "scrollablepanel_scrollbar_background_color_focused", getHorizontalScrollBar().getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "scrollablepanel_scrollbar_background_color_hovered", getHorizontalScrollBar().getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "scrollablepanel_scrollbar_background_color_pressed", getHorizontalScrollBar().getPressedStyle());

		// Border scrollbar colors
		DataParameters.applyBorderColor(this, "scrollablepanel_scrollbar_border_color", getHorizontalScrollBar().getStyle());
		DataParameters.applyBorderColor(this, "scrollablepanel_scrollbar_border_color_focused", getHorizontalScrollBar().getFocusedStyle());
		DataParameters.applyBorderColor(this, "scrollablepanel_scrollbar_border_color_hovered", getHorizontalScrollBar().getHoveredStyle());
		DataParameters.applyBorderColor(this, "scrollablepanel_scrollbar_border_color_pressed", getHorizontalScrollBar().getPressedStyle());

		// Background scrollbar colors
		DataParameters.applyBackgroundColor(this, "scrollablepanel_scrollbar_background_color", getVerticalScrollBar().getStyle());
		DataParameters.applyBackgroundColor(this, "scrollablepanel_scrollbar_background_color_focused", getVerticalScrollBar().getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "scrollablepanel_scrollbar_background_color_hovered", getVerticalScrollBar().getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "scrollablepanel_scrollbar_background_color_pressed", getVerticalScrollBar().getPressedStyle());

		// Border scrollbar colors
		DataParameters.applyBorderColor(this, "scrollablepanel_scrollbar_border_color", getVerticalScrollBar().getStyle());
		DataParameters.applyBorderColor(this, "scrollablepanel_scrollbar_border_color_focused", getVerticalScrollBar().getFocusedStyle());
		DataParameters.applyBorderColor(this, "scrollablepanel_scrollbar_border_color_hovered", getVerticalScrollBar().getHoveredStyle());
		DataParameters.applyBorderColor(this, "scrollablepanel_scrollbar_border_color_pressed", getVerticalScrollBar().getPressedStyle());

		if (DataParameters.has(this, "scrollablepanel_border_radius")) {
			getStyle().setBorderRadius(DataParameters.getFloat(this, "scrollablepanel_border_radius"));
		}
		
		if (DataParameters.has(this, "scrollablepanel_border_radius")) {
			getViewport().getStyle().setBorderRadius(DataParameters.getFloat(this, "scrollablepanel_border_radius"));
		}
	}

	@Override
	public void update() {
	}

	@Override
	public String getComponentName() {
		return "scrollablepanel";
	}

	private String variable;

	/**
	 * @param variable the variable to set
	 */
	public void setVariable(String variable) {
		this.variable = variable;
	}

	@Override
	public String getComponentVariable() {
		return variable;
	}

	public static Window test() {
		Window window = new Window("Testing ScrollablePanel", 0, 0, 500, 500);
		window.getContainer().getStyle().setDisplay(DisplayType.FLEX);

		ScrollablePanel scrollablePanel = new ScrollablePanel(10, 10, 100, 100);

		scrollablePanel.getStyle().setLeft(10f);
		scrollablePanel.getStyle().setRight(10f);
		scrollablePanel.getStyle().setTop(10f);
		scrollablePanel.getStyle().setBottom(10f);
		
		scrollablePanel.getContainer().getStyle().setShadow(null);
		scrollablePanel.getContainer().getStyle().setBorder(null);
		scrollablePanel.getContainer().getStyle().setBorderRadius(0f);
		scrollablePanel.getContainer().getStyle().setDisplay(DisplayType.FLEX);
		scrollablePanel.getContainer().getStyle().setLeft(0f);
		scrollablePanel.getContainer().getStyle().setRight(0f);
		scrollablePanel.getContainer().getStyle().setTop(0f);

		scrollablePanel.getViewport().getStyle().setShadow(null);
		scrollablePanel.getViewport().getStyle().setBorder(null);
		scrollablePanel.getViewport().getStyle().setBorderRadius(0f);
		
		scrollablePanel.setAutoResize(true);

		for (int i = 0; i < 200; i++) {
			Panel component = new Panel(0, i * 40, 100, 40);
			component.getStyle().getBackground().setColor((float) new Random().nextInt(255) / 255,
					(float) new Random().nextInt(255) / 255, (float) new Random().nextInt(255) / 255, 1);
			component.getStyle().setShadow(null);
			component.getStyle().setBorder(null);
			component.getStyle().setBorderRadius(0f);

			component.getStyle().setTop(i * 40f);
			component.getStyle().setRight(0f);
			component.getStyle().setLeft(0f);
			component.getStyle().setHeight(40f);
			
			/*

			scrollablePanel.getContainer().getStyle().setHeight((float) i * 40);
			scrollablePanel.getContainer().setSize(0, (float) i * 40);*/

			scrollablePanel.getContainer().add(component);
		}

		window.load();
		window.getContainer().add(scrollablePanel);
		return window;
	}
}
