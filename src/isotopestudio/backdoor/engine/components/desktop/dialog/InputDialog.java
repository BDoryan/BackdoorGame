package isotopestudio.backdoor.engine.components.desktop.dialog;

import org.liquidengine.legui.component.event.button.ButtonWidthChangeEvent;
import org.liquidengine.legui.component.event.label.LabelContentChangeEvent;
import org.liquidengine.legui.component.event.label.LabelContentChangeEventListener;
import org.liquidengine.legui.component.event.textarea.TextAreaFieldContentChangeEvent;
import org.liquidengine.legui.component.event.textarea.TextAreaFieldContentChangeEventListener;
import org.liquidengine.legui.component.misc.listener.button.UpdateButtonStyleWidthListener;
import org.liquidengine.legui.component.misc.listener.button.UpdateButtonWidthListener;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.event.MouseClickEvent.MouseClickAction;
import org.liquidengine.legui.listener.MouseClickEventListener;
import org.liquidengine.legui.style.Style.DisplayType;

import doryanbessiere.isotopestudio.commons.lang.Lang;
import isotopestudio.backdoor.engine.components.desktop.Button;
import isotopestudio.backdoor.engine.components.desktop.Label;
import isotopestudio.backdoor.engine.components.desktop.TextField;

/**
 * @author BESSIERE
 * @github https://www.github.com/DoryanBessiere/
 */
public class InputDialog extends Dialog {

	public TextField responseTextField = new TextField();
	public Label questionLabel = new Label();
	public Button ok_button = new Button();
	
	/**
	 * @param title
	 * @param width
	 * @param height
	 */
	public InputDialog(String title, String question, String button_name, DialogCallback callback, float width, float height) {
		super(title, width, height);
		setVariable("input_dialog");

		responseTextField.setVariable("input_dialog_textfield");
		responseTextField.getStyle().setTop(30f);
		responseTextField.getStyle().setRight(10f);
		responseTextField.getStyle().setLeft(10f);
		responseTextField.getStyle().setHeight(20f);
		responseTextField.getStyle().setShadow(null);

		questionLabel.getTextState().setText(question);
		questionLabel.getStyle().setLeft(10f);
		questionLabel.getStyle().setTop(10f);
		questionLabel.getStyle().setWidth(200);
		questionLabel.getStyle().setHeight(20);
		questionLabel.getListenerMap().addListener(LabelContentChangeEvent.class,
				e -> questionLabel.getStyle().setMinWidth(questionLabel.getTextState().getTextWidth() + 20f));
		questionLabel.getListenerMap().addListener(LabelContentChangeEvent.class,
				new LabelContentChangeEventListener() {
					@Override
					public void process(LabelContentChangeEvent event) {
						setSize(questionLabel.getTextState().getTextWidth() + 20, getSize().y);
						centerLocation();
					}
				});

		ok_button.getTextState().setText(button_name == null ? Lang.get("message_ok") : button_name);
		ok_button.getListenerMap().addListener(MouseClickEvent.class, (MouseClickEventListener) e -> {
			if (e.getAction() == MouseClickAction.CLICK && ok_button.isEnabled()) {
				close();
				
				callback.result(responseTextField.getTextState().getText());
			}
		});

		getContainer().getStyle().setDisplay(DisplayType.FLEX);

		ok_button.getStyle().setLeft(10f);
		ok_button.getStyle().setBottom(10f);
		ok_button.getStyle().setWidth(50);
		ok_button.getStyle().setHeight(20);
		ok_button.getListenerMap().addListener(ButtonWidthChangeEvent.class, new UpdateButtonStyleWidthListener() {
			@Override
			public void process(ButtonWidthChangeEvent event) {
				event.getTargetComponent().getStyle().setWidth(event.getWidth() + 20);
			}
		});
		ok_button.getListenerMap().addListener(ButtonWidthChangeEvent.class, new UpdateButtonWidthListener() {
			@Override
			public void process(ButtonWidthChangeEvent event) {
				event.getTargetComponent().getSize().x = event.getWidth() + 20;
			}
		});

		getContainer().add(questionLabel);
		getContainer().add(ok_button);
		getContainer().add(responseTextField);

		load();
		questionLabel.load();
		ok_button.load();
		responseTextField.load();

		ok_button.setEnabled(false);
	}
}
