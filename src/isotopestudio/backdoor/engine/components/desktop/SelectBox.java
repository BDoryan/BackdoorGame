package isotopestudio.backdoor.engine.components.desktop;

import org.liquidengine.legui.style.Style;

import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.datapack.DataParameters;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class SelectBox<T> extends org.liquidengine.legui.component.SelectBox<T> implements IComponent {

	public SelectBox() {
		super();
		
		initIComponent();
	}

	@Override
	public void load() {
		// Background colors
		DataParameters.applyBackgroundColor(this, "selectbox_background_color", getStyle());
		DataParameters.applyBackgroundColor(this, "selectbox_background_color_focused", getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "selectbox_background_color_hovered", getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "selectbox_background_color_pressed", getPressedStyle());

		// Border colors
		DataParameters.applyBorderColor(this, "selectbox_border_color", getStyle());
		DataParameters.applyBorderColor(this, "selectbox_border_color_focused", getFocusedStyle());
		DataParameters.applyBorderColor(this, "selectbox_border_color_hovered", getHoveredStyle());
		DataParameters.applyBorderColor(this, "selectbox_border_color_pressed", getPressedStyle());

		// Expand Button Background colors
		DataParameters.applyBackgroundColor(this, "selectbox_background_color", this.getExpandButton().getStyle());
		DataParameters.applyBackgroundColor(this, "selectbox_background_color_focused", this.getExpandButton().getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "selectbox_background_color_hovered", this.getExpandButton().getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "selectbox_background_color_pressed", this.getExpandButton().getPressedStyle());

		// Expand Button Border colors
		DataParameters.applyBorderColor(this, "selectbox_border_color", this.getExpandButton().getStyle());
		DataParameters.applyBorderColor(this, "selectbox_border_color_focused", this.getExpandButton().getFocusedStyle());
		DataParameters.applyBorderColor(this, "selectbox_border_color_hovered", this.getExpandButton().getHoveredStyle());
		DataParameters.applyBorderColor(this, "selectbox_border_color_pressed", this.getExpandButton().getPressedStyle());

		// Expand Button Title color
		DataParameters.applyTextColor(this, "selectbox_text_color", this.getExpandButton().getStyle());
		DataParameters.applyTextColor(this, "selectbox_text_color_focused", this.getExpandButton().getFocusedStyle());
		DataParameters.applyTextColor(this, "selectbox_text_color_hovered",this.getExpandButton(). getHoveredStyle());
		DataParameters.applyTextColor(this, "selectbox_text_color_pressed",this.getExpandButton(). getPressedStyle());

		// Expand Button Title font
		DataParameters.applyTextFont(this, "selectbox_text_font", this.getExpandButton().getStyle());
		DataParameters.applyTextFont(this, "selectbox_text_font_focused", this.getExpandButton().getFocusedStyle());
		DataParameters.applyTextFont(this, "selectbox_text_font_hovered", this.getExpandButton().getHoveredStyle());
		DataParameters.applyTextFont(this, "selectbox_text_font_pressed",this.getExpandButton(). getPressedStyle());

		// Selection Button Background colors
		DataParameters.applyBackgroundColor(this, "selectbox_background_color", this.getSelectionButton().getStyle());
		DataParameters.applyBackgroundColor(this, "selectbox_background_color_focused", this.getSelectionButton().getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "selectbox_background_color_hovered", this.getSelectionButton().getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "selectbox_background_color_pressed", this.getSelectionButton().getPressedStyle());

		// Selection Button Border colors
		DataParameters.applyBorderColor(this, "selectbox_border_color", this.getSelectionButton().getStyle());
		DataParameters.applyBorderColor(this, "selectbox_border_color_focused", this.getSelectionButton().getFocusedStyle());
		DataParameters.applyBorderColor(this, "selectbox_border_color_hovered", this.getSelectionButton().getHoveredStyle());
		DataParameters.applyBorderColor(this, "selectbox_border_color_pressed", this.getSelectionButton().getPressedStyle());
		
		// Selection Button Title color
		DataParameters.applyTextColor(this, "selectbox_text_color", this.getSelectionButton().getStyle());
		DataParameters.applyTextColor(this, "selectbox_text_color_focused", this.getSelectionButton().getFocusedStyle());
		DataParameters.applyTextColor(this, "selectbox_text_color_hovered",this.getSelectionButton(). getHoveredStyle());
		DataParameters.applyTextColor(this, "selectbox_text_color_pressed",this.getSelectionButton(). getPressedStyle());

		// Selection Button Title font
		DataParameters.applyTextFont(this, "selectbox_text_font", this.getSelectionButton().getStyle());
		DataParameters.applyTextFont(this, "selectbox_text_font_focused", this.getSelectionButton().getFocusedStyle());
		DataParameters.applyTextFont(this, "selectbox_text_font_hovered", this.getSelectionButton().getHoveredStyle());
		DataParameters.applyTextFont(this, "selectbox_text_font_pressed",this.getSelectionButton(). getPressedStyle());

		// Selection List panel Background colors
		DataParameters.applyBackgroundColor(this, "selectbox_background_color", this.getSelectionListPanel().getStyle());
		DataParameters.applyBackgroundColor(this, "selectbox_background_color_focused", this.getSelectionListPanel().getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "selectbox_background_color_hovered", this.getSelectionListPanel().getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "selectbox_background_color_pressed", this.getSelectionListPanel().getPressedStyle());

		// Selection List panel Border colors
		DataParameters.applyBorderColor(this, "selectbox_border_color", this.getSelectionListPanel().getStyle());
		DataParameters.applyBorderColor(this, "selectbox_border_color_focused", this.getSelectionListPanel().getFocusedStyle());
		DataParameters.applyBorderColor(this, "selectbox_border_color_hovered", this.getSelectionListPanel().getHoveredStyle());
		DataParameters.applyBorderColor(this, "selectbox_border_color_pressed", this.getSelectionListPanel().getPressedStyle());

		/*
		 * SCROLLBAR SECTION 
		 */
		
		// Background scrollbar colors
		DataParameters.applyBackgroundColor(this, "selectbox_scrollbar_background_color", this.getSelectionListPanel().getVerticalScrollBar().getStyle());
		DataParameters.applyBackgroundColor(this, "selectbox_scrollbar_background_color_focused",  this.getSelectionListPanel().getVerticalScrollBar().getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "selectbox_scrollbar_background_color_hovered",  this.getSelectionListPanel().getVerticalScrollBar().getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "selectbox_scrollbar_background_color_pressed",  this.getSelectionListPanel().getVerticalScrollBar().getPressedStyle());

		// Border scrollbar colors
		DataParameters.applyBorderColor(this, "selectbox_scrollbar_border_color",  this.getSelectionListPanel().getVerticalScrollBar().getStyle());
		DataParameters.applyBorderColor(this, "selectbox_scrollbar_border_color_focused",  this.getSelectionListPanel().getVerticalScrollBar().getFocusedStyle());
		DataParameters.applyBorderColor(this, "selectbox_scrollbar_border_color_hovered",  this.getSelectionListPanel().getVerticalScrollBar().getHoveredStyle());
		DataParameters.applyBorderColor(this, "selectbox_scrollbar_border_color_pressed",  this.getSelectionListPanel().getVerticalScrollBar().getPressedStyle());

		// Background scrollbar colors
		DataParameters.applyBackgroundColor(this, "selectbox_scrollbar_background_color", this.getSelectionListPanel().getHorizontalScrollBar().getStyle());
		DataParameters.applyBackgroundColor(this, "selectbox_scrollbar_background_color_focused",  this.getSelectionListPanel().getHorizontalScrollBar().getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "selectbox_scrollbar_background_color_hovered",  this.getSelectionListPanel().getHorizontalScrollBar().getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "selectbox_scrollbar_background_color_pressed",  this.getSelectionListPanel().getHorizontalScrollBar().getPressedStyle());

		// Border scrollbar colors
		DataParameters.applyBorderColor(this, "selectbox_scrollbar_border_color",  this.getSelectionListPanel().getHorizontalScrollBar().getStyle());
		DataParameters.applyBorderColor(this, "selectbox_scrollbar_border_color_focused",  this.getSelectionListPanel().getHorizontalScrollBar().getFocusedStyle());
		DataParameters.applyBorderColor(this, "selectbox_scrollbar_border_color_hovered",  this.getSelectionListPanel().getHorizontalScrollBar().getHoveredStyle());
		DataParameters.applyBorderColor(this, "selectbox_scrollbar_border_color_pressed",  this.getSelectionListPanel().getHorizontalScrollBar().getPressedStyle());
		
		if (DataParameters.has(this, "selectbox_border_radius")) {
			getStyle().setBorderRadius(DataParameters.getFloat(this, "selectbox_border_radius"));
		}

		this.getExpandButton().getStyle().setBorderRadius(0f);
		this.getSelectionButton().getStyle().setBorderRadius(0f);
		
		this.getSelectionListPanel().getContainer().getStyle().setBorderRadius(0f);
		this.getSelectionListPanel().getStyle().setBorderRadius(0f);
		this.getSelectionListPanel().getViewport().getStyle().setBorderRadius(0f);

		this.getStyle().setShadow(null);
		this.getStyle().setFocusedStrokeColor(0, 0, 0, 0);

		this.getExpandButton().getStyle().setShadow(null);
		this.getExpandButton().getStyle().setFocusedStrokeColor(0, 0, 0, 0);
		
		loadChildrens();
	}
	
	@Override
	public void addElement(T element) {
		super.addElement(element);
		loadChildrens();
	}
	
	public void loadChildrens() {
		for(org.liquidengine.legui.component.SelectBox<T>.SelectBoxElement<T> element : this.getSelectBoxElements()) {
			// Element Background colors
			DataParameters.applyBackgroundColor(this, "selectbox_background_color", element.getStyle());
			DataParameters.applyBackgroundColor(this, "selectbox_background_color_focused", element.getFocusedStyle());
			DataParameters.applyBackgroundColor(this, "selectbox_background_color_hovered", element.getHoveredStyle());
			DataParameters.applyBackgroundColor(this, "selectbox_background_color_pressed", element.getPressedStyle());

			// Element Border colors
			DataParameters.applyBorderColor(this, "selectbox_border_color", element.getStyle());
			DataParameters.applyBorderColor(this, "selectbox_border_color_focused", element.getFocusedStyle());
			DataParameters.applyBorderColor(this, "selectbox_border_color_hovered", element.getHoveredStyle());
			DataParameters.applyBorderColor(this, "selectbox_border_color_pressed", element.getPressedStyle());
			
			// Element Title color
			DataParameters.applyTextColor(this, "selectbox_text_color", element.getStyle());
			DataParameters.applyTextColor(this, "selectbox_text_color_focused", element.getFocusedStyle());
			DataParameters.applyTextColor(this, "selectbox_text_color_hovered",element. getHoveredStyle());
			DataParameters.applyTextColor(this, "selectbox_text_color_pressed",element. getPressedStyle());

			// Element Title font
			DataParameters.applyTextFont(this, "selectbox_text_font", element.getStyle());
			DataParameters.applyTextFont(this, "selectbox_text_font_focused", element.getFocusedStyle());
			DataParameters.applyTextFont(this, "selectbox_text_font_hovered", element.getHoveredStyle());
			DataParameters.applyTextFont(this, "selectbox_text_font_pressed",element. getPressedStyle());
			
			element.getStyle().setBorderRadius(0f);
		}
	}

	@Override
	public void update() {
	}

	@Override
	public String getComponentName() {
		return "selectbox";
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

	/**
	 * 
	 */
	public void clearElements() {
		if(getElements().size() == 0)return;
		for(int i = getElements().size() - 1; i >= 0; i--) {
			removeElement(i);
		}
	}
}
