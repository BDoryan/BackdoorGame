package isotopestudio.backdoor.game.applications.settings;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.liquidengine.legui.component.Panel;
import org.liquidengine.legui.component.event.button.ButtonWidthChangeEvent;
import org.liquidengine.legui.component.event.label.LabelWidthChangeEvent;
import org.liquidengine.legui.component.misc.listener.button.UpdateButtonStyleWidthListener;
import org.liquidengine.legui.component.misc.listener.label.UpdateLabelStyleWidthListener;
import org.liquidengine.legui.component.optional.align.HorizontalAlign;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.event.MouseClickEvent.MouseClickAction;
import org.liquidengine.legui.listener.MouseClickEventListener;
import org.liquidengine.legui.style.Style.DisplayType;
import org.lwjgl.Version;
import org.lwjgl.opengl.GL11;

import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.components.desktop.Button;
import isotopestudio.backdoor.engine.components.desktop.Label;
import isotopestudio.backdoor.game.BackdoorGame;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class InformationsPanel extends Panel implements IComponent {

	public InformationsPanel() {
		setFocusable(false);

		getStyle().setDisplay(DisplayType.FLEX);
		getStyle().getBackground().setColor(0, 0, 0, 0);
		getStyle().setBorderRadius(0f);

		LinkedHashMap<String, MouseClickEventListener> labels = new LinkedHashMap<>();
		labels.put("Backdoor Game, information:", null);
		labels.put("    Game directory: " + BackdoorGame.localDirectory().getPath(), new MouseClickEventListener() {
			@Override
			public void process(MouseClickEvent event) {
				if(event.getAction() != MouseClickAction.RELEASE)return;
				
				try {
					Desktop.getDesktop().open(BackdoorGame.localDirectory());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		labels.put("", null);
		labels.put("", null);
		labels.put("Version:", null);
		labels.put("    Game: " + BackdoorGame.GAME_VERSION, null);
		labels.put("    Legui: " + BackdoorGame.LEGUI_VERSION, null);
		labels.put("    JOML: " + BackdoorGame.JOML_VERSION, null);
		labels.put("    LWJGL: " + Version.getVersion(), null);
		labels.put("    OpenGL: " + GL11.glGetString(GL11.GL_VERSION), null);
		labels.put("", null);

		float font_size = 18f;

		float top = 10;

		for (Entry<String, MouseClickEventListener> entry : labels.entrySet()) {
			Label label = new Label(entry.getKey());

			label.getStyle().setFontSize(font_size);
			label.getStyle().setFont("main");
			label.getStyle().setLeft(10f);
			label.getStyle().setRight(10f);
			label.getStyle().setTop(top);
			label.getListenerMap().addListener(LabelWidthChangeEvent.class, new UpdateLabelStyleWidthListener());
			if(entry.getValue() != null) {
				label.getListenerMap().addListener(MouseClickEvent.class, entry.getValue());
			}

			top += label.getSize().y ;

			label.load();
			add(label);
		}

		Label copyright = new Label("All rights reserved to ISOTOPE (https://isotope-studio.fr/).");
		copyright.getStyle().setFontSize(18f);
		copyright.getStyle().setRight(10);
		copyright.getStyle().setLeft(10);
		copyright.getStyle().setBottom(0);
		copyright.getStyle().setHeight(20);
		copyright.getStyle().setHorizontalAlign(HorizontalAlign.CENTER);
		copyright.load();
		
		Button debugButton = new Button("Debug");
		debugButton.getStyle().setFontSize(20f);
		debugButton.getStyle().setBottom(30);
		debugButton.getStyle().setLeft(10);
		debugButton.getStyle().setHeight(40);
		debugButton.getListenerMap().addListener(ButtonWidthChangeEvent.class, new UpdateButtonStyleWidthListener() {
			@Override
			public void process(ButtonWidthChangeEvent event) {
				debugButton.getStyle().setWidth(event.getWidth() + 20);
			}
		});
		debugButton.getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
			@Override
			public void process(MouseClickEvent event) {
				if(event.getAction() != MouseClickAction.RELEASE)return;

				BackdoorGame.debugWindow();
			}
		});
		debugButton.load();
		
		copyright.getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
			@Override
			public void process(MouseClickEvent event) {
				if(event.getAction() != MouseClickAction.RELEASE)return;

				try {
					Desktop.getDesktop().browse(new URI("https://isotope-studio.fr/"));
				} catch (IOException | URISyntaxException e) {
					e.printStackTrace();
				}
			}
		});

		add(debugButton);
		add(copyright);

		load();
	}

	@Override
	public void update() {
	}

	@Override
	public void load() {
	}

	@Override
	public String getComponentName() {
		return null;
	}

	@Override
	public String getComponentVariable() {
		return null;
	}
}
