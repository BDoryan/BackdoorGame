package isotopestudio.backdoor.engine.components.desktop;

import java.io.IOException;

import org.joml.Vector2f;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.event.MouseClickEvent.MouseClickAction;
import org.liquidengine.legui.listener.MouseClickEventListener;

import isotopestudio.backdoor.engine.audio.AudioEngine;
import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.datapack.DataParameters;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.utils.OggClip;

public class Button extends org.liquidengine.legui.component.Button implements IComponent {

	public Button() {
		super();
		initIComponent();
	}

	public Button(float x, float y, float width, float height) {
		super(x, y, width, height);
		initIComponent();
	}

	public Button(String text, float x, float y, float width, float height) {
		super(text, x, y, width, height);
		initIComponent();
	}

	public Button(String text, Vector2f position, Vector2f size) {
		super(text, position, size);
		initIComponent();
	}

	public Button(String text) {
		super(text);
		initIComponent();
	}

	public Button(Vector2f position, Vector2f size) {
		super(position, size);
		initIComponent();
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if(enabled) {
			// Background colors
			DataParameters.applyBackgroundColor(this, "button_background_color", getStyle());
			DataParameters.applyBackgroundColor(this, "button_background_color_focused", getFocusedStyle());
			DataParameters.applyBackgroundColor(this, "button_background_color_hovered", getHoveredStyle());
			DataParameters.applyBackgroundColor(this, "button_background_color_pressed", getPressedStyle());

			// Border colors
			DataParameters.applyBorderColor(this, "button_border_color", getStyle());
			DataParameters.applyBorderColor(this, "button_border_color_focused", getFocusedStyle());
			DataParameters.applyBorderColor(this, "button_border_color_hovered", getHoveredStyle());
			DataParameters.applyBorderColor(this, "button_border_color_pressed", getPressedStyle());
		} else {
			DataParameters.applyBackgroundColor(this, "button_background_color_disabled", getStyle());
			DataParameters.applyBackgroundColor(this, "button_background_color_disabled", getFocusedStyle());
			DataParameters.applyBackgroundColor(this, "button_background_color_disabled", getHoveredStyle());
			DataParameters.applyBackgroundColor(this, "button_background_color_disabled", getPressedStyle());

			DataParameters.applyBorderColor(this, "button_border_color_disabled", getStyle());
			DataParameters.applyBorderColor(this, "button_border_color_disabled", getFocusedStyle());
			DataParameters.applyBorderColor(this, "button_border_color_disabled", getHoveredStyle());
			DataParameters.applyBorderColor(this, "button_border_color_disabled", getPressedStyle());
		}
		
		// remove blue line
		getStyle().setFocusedStrokeColor(0, 0, 0, 0);
	}
	
	private boolean sound_enable = true;
	
	@Override
	public void initIComponent() {
		getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
			@Override
			public void process(MouseClickEvent event) {
				if(event.getAction() != MouseClickAction.PRESS || !isEnabled())return;
				if(!sound_enable)return;
					AudioEngine.playSound(BackdoorGame.getDatapack().getAudio("button_click"));
			}
		});
		IComponent.super.initIComponent();
	}

	@Override
	public void load() {
		// Background colors
		DataParameters.applyBackgroundColor(this, "button_background_color", getStyle());
		DataParameters.applyBackgroundColor(this, "button_background_color_focused", getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "button_background_color_hovered", getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "button_background_color_pressed", getPressedStyle());

		// Border colors
		DataParameters.applyBorderColor(this, "button_border_color", getStyle());
		DataParameters.applyBorderColor(this, "button_border_color_focused", getFocusedStyle());
		DataParameters.applyBorderColor(this, "button_border_color_hovered", getHoveredStyle());
		DataParameters.applyBorderColor(this, "button_border_color_pressed", getPressedStyle());

		// Title color
		DataParameters.applyTextColor(this, "button_text_color", getStyle());
		DataParameters.applyTextColor(this, "button_text_color_focused", getFocusedStyle());
		DataParameters.applyTextColor(this, "button_text_color_hovered", getHoveredStyle());
		DataParameters.applyTextColor(this, "button_text_color_pressed", getPressedStyle());

		// Title font
		DataParameters.applyTextFont(this, "button_text_font", getStyle());
		DataParameters.applyTextFont(this, "button_text_font_focused", getFocusedStyle());
		DataParameters.applyTextFont(this, "button_text_font_hovered", getHoveredStyle());
		DataParameters.applyTextFont(this, "button_text_font_pressed", getPressedStyle());

		if (DataParameters.has(this, "button_border_radius")) {
			getStyle().setBorderRadius(DataParameters.getFloat(this, "button_border_radius"));
		}
	}

	@Override
	public void update() {
	}

	@Override
	public String getComponentName() {
		return "button";
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
