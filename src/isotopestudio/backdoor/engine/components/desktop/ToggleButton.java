package isotopestudio.backdoor.engine.components.desktop;

import org.joml.Vector2f;
import org.liquidengine.legui.animation.Animation;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.listener.MouseClickEventListener;
import org.liquidengine.legui.style.Style.DisplayType;

import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.datapack.DataParameters;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class ToggleButton extends org.liquidengine.legui.component.Component implements IComponent {

	private static final float SPACE = 20f;

	private Button button;
	private boolean toggled = false;

	public ToggleButton() {
		super();
		initIComponent();
	}

	public ToggleButton(float x, float y, float width, float height) {
		super(x, y, width, height);
		initIComponent();
	}

	public ToggleButton(Vector2f position, Vector2f size) {
		super(position, size);
		initIComponent();
	}

	@Override
	public void initIComponent() {
		IComponent.super.initIComponent();

		getStyle().setDisplay(DisplayType.FLEX);
		getStyle().setFocusedStrokeColor(0, 0, 0, 0);

		getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
			@Override
			public void process(MouseClickEvent event) {
				setToggled(!toggled);
			}
		});

		button = new Button("") {
			@Override
			public void load() {
				if (toggled) {
					// Background colors
					DataParameters.applyBackgroundColor(ToggleButton.this, "togglebutton_toggled_background_color",
							button.getStyle());
					DataParameters.applyBackgroundColor(ToggleButton.this,
							"togglebutton_toggled_background_color_focused", button.getFocusedStyle());
					DataParameters.applyBackgroundColor(ToggleButton.this,
							"togglebutton_toggled_background_color_hovered", button.getHoveredStyle());
					DataParameters.applyBackgroundColor(ToggleButton.this,
							"togglebutton_toggled_background_color_pressed", button.getPressedStyle());

					// Border colors
					DataParameters.applyBorderColor(ToggleButton.this, "togglebutton_toggled_border_color",
							button.getStyle());
					DataParameters.applyBorderColor(ToggleButton.this, "togglebutton_toggled_border_color_focused",
							button.getFocusedStyle());
					DataParameters.applyBorderColor(ToggleButton.this, "togglebutton_toggled_border_color_hovered",
							button.getHoveredStyle());
					DataParameters.applyBorderColor(ToggleButton.this, "togglebutton_toggled_border_color_pressed",
							button.getPressedStyle());
				} else {
					// Background colors
					DataParameters.applyBackgroundColor(ToggleButton.this, "togglebutton_untoggled_background_color",
							button.getStyle());
					DataParameters.applyBackgroundColor(ToggleButton.this,
							"togglebutton_untoggled_background_color_focused", button.getFocusedStyle());
					DataParameters.applyBackgroundColor(ToggleButton.this,
							"togglebutton_untoggled_background_color_hovered", button.getHoveredStyle());
					DataParameters.applyBackgroundColor(ToggleButton.this,
							"togglebutton_untoggled_background_color_pressed", button.getPressedStyle());

					// Border colors
					DataParameters.applyBorderColor(ToggleButton.this, "togglebutton_untoggled_border_color",
							button.getStyle());
					DataParameters.applyBorderColor(ToggleButton.this, "togglebutton_untoggled_border_color_focused",
							button.getFocusedStyle());
					DataParameters.applyBorderColor(ToggleButton.this, "togglebutton_untoggled_border_color_hovered",
							button.getHoveredStyle());
					DataParameters.applyBorderColor(ToggleButton.this, "togglebutton_untoggled_border_color_pressed",
							button.getPressedStyle());
				}
			}
		};
		button.setFocusable(false);
		button.getStyle().setBorderRadius(0f);
		button.getStyle().getBorder().setEnabled(false);
		button.getStyle().setShadow(null);

		button.getStyle().setTop(0f);
		button.getStyle().setLeft(0f);
		button.getStyle().setRight(SPACE);
		button.getStyle().setBottom(0f);

		add(button);
	}

	/**
	 * @return the toggled
	 */
	public boolean isToggled() {
		return toggled;
	}

	Animation animation = null;

	public void setToggled(boolean toggled) {
		boolean useAnimation = this.toggled != toggled;

		if (!toggled) {
			if (useAnimation) {
				animation = new Animation() {
					private float spacing = 0f;
					
					@Override
					protected boolean animate(double delta) {
						if(spacing >= SPACE)return true;
						float diff = 1.2f;
						
						spacing+= diff;

						button.getStyle().setLeft((float) button.getStyle().getLeft().get() - diff);
						button.getStyle().setRight((float) button.getStyle().getRight().get() + diff);
						
						return false;
					}
				};
				animation.startAnimation();
			} else {
				button.getStyle().setTop(0f);
				button.getStyle().setLeft(0f);
				button.getStyle().setRight(SPACE);
				button.getStyle().setBottom(0f);
			}
		} else {
			if (useAnimation) {
				animation = new Animation() {
					private float spacing = 0f;
					
					@Override
					protected boolean animate(double delta) {
						if(spacing >= SPACE)return true;
						float diff = 1.2f;
						
						spacing += diff;
						
						button.getStyle().setLeft((float) button.getStyle().getLeft().get() + diff);
						button.getStyle().setRight((float) button.getStyle().getRight().get() - diff);

						return false;
					}
				};
				animation.startAnimation();
			} else {
				button.getStyle().setTop(0f);
				button.getStyle().setLeft(SPACE);
				button.getStyle().setRight(0f);
				button.getStyle().setBottom(0f);
			}
		}

		this.toggled = toggled;
		button.load();
	}

	@Override
	public void load() {
		// Background colors
		DataParameters.applyBackgroundColor(this, "togglebutton_background_color", getStyle());
		DataParameters.applyBackgroundColor(this, "togglebutton_background_color_focused", getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "togglebutton_background_color_hovered", getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "togglebutton_background_color_pressed", getPressedStyle());

		// Border colors
		DataParameters.applyBorderColor(this, "togglebutton_border_color", getStyle());
		DataParameters.applyBorderColor(this, "togglebutton_border_color_focused", getFocusedStyle());
		DataParameters.applyBorderColor(this, "togglebutton_border_color_hovered", getHoveredStyle());
		DataParameters.applyBorderColor(this, "togglebutton_border_color_pressed", getPressedStyle());

		// Title color
		DataParameters.applyTextColor(this, "togglebutton_text_color", getStyle());
		DataParameters.applyTextColor(this, "togglebutton_text_color_focused", getFocusedStyle());
		DataParameters.applyTextColor(this, "togglebutton_text_color_hovered", getHoveredStyle());
		DataParameters.applyTextColor(this, "togglebutton_text_color_pressed", getPressedStyle());

		// Title font
		DataParameters.applyTextFont(this, "togglebutton_text_font", getStyle());
		DataParameters.applyTextFont(this, "togglebutton_text_font_focused", getFocusedStyle());
		DataParameters.applyTextFont(this, "togglebutton_text_font_hovered", getHoveredStyle());
		DataParameters.applyTextFont(this, "togglebutton_text_font_pressed", getPressedStyle());

		if (DataParameters.has(this, "togglebutton_border_radius")) {
			getStyle().setBorderRadius(DataParameters.getFloat(this, "togglebutton_border_radius"));
		}

		button.load();
	}

	@Override
	public void update() {
	}

	@Override
	public String getComponentName() {
		return "togglebutton";
	}

	private String variable;

	public void setVariable(String variable) {
		this.variable = variable;
	}

	@Override
	public String getComponentVariable() {
		return variable;
	}
}
