package isotopestudio.backdoor.engine.components.desktop.notification;

import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.component.ImageView;
import org.liquidengine.legui.component.optional.align.HorizontalAlign;
import org.liquidengine.legui.component.optional.align.VerticalAlign;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.image.Image;
import org.liquidengine.legui.style.Style.DisplayType;

import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.components.desktop.Label;
import isotopestudio.backdoor.engine.components.desktop.Text;
import isotopestudio.backdoor.engine.datapack.DataParameters;

/**
 * @author BESSIERE Doryan
 * @github https://www.github.com/DoryanBessiere/
 */
public class Notification extends Component implements IComponent {

	private Label label_title;
	private Text text;
	private ImageView image_view;

	private double time_left_in_second = 0D;
	
	public Notification(Image image, String title, String text) {
		this(image, title, text, 5D);
	}

	public Notification(Image image, String title, String text, double time_left_in_second) {
		super();
		this.time_left_in_second = time_left_in_second;

		this.getStyle().setDisplay(DisplayType.FLEX);
		this.getStyle().setBorderRadius(0f);
		this.getStyle().setShadow(null);
		this.setSize(400, 110);

		this.label_title = new Label(title);
		this.label_title.getStyle().setFontSize(18f);
		this.label_title.getStyle().setLeft(10f);
		this.label_title.getStyle().setRight(10f);
		this.label_title.getStyle().setTop(10f);

		this.text = new Text(text);
		this.text.getStyle().setVerticalAlign(VerticalAlign.TOP);
		this.text.getStyle().setFontSize(14f);
		this.text.getStyle().setLeft(10f);
		this.text.getStyle().setRight(10f);
		this.text.getStyle().setBottom(10f);
		this.text.getStyle().setTop(36f);

		this.text.setFocusable(false);
		this.text.setTabFocusable(false);

		this.label_title.setFocusable(false);
		this.label_title.setTabFocusable(false);
		
		float image_width = this.getSize().y - 20;
		if (image != null) {
			this.image_view = new ImageView(image);
			this.image_view.getStyle().setVerticalAlign(VerticalAlign.MIDDLE);
			this.image_view.getStyle().setHorizontalAlign(HorizontalAlign.CENTER);
			this.image_view.getStyle().setBorderRadius(0f);
			this.image_view.getStyle().setBorder(null);
			this.image_view.getStyle().setShadow(null);
			this.image_view.getStyle().getBackground().setColor(0, 0, 0, 0);

			this.image_view.getStyle().setTop(10 + (image_width / 2 - (image.getHeight() / 2)));
			this.image_view.getStyle().setLeft(10+ (image_width / 2 - (image.getWidth() / 2)));
			this.image_view.getStyle().setHeight(image.getHeight());
			this.image_view.getStyle().setWidth(image.getWidth());

			this.image_view.setFocusable(false);
			this.image_view.setTabFocusable(false);

			this.text.getStyle().setLeft(106);
			this.label_title.getStyle().setLeft(106f);
			
			this.setSize(400 + image_width + 20, this.getSize().y);

			this.add(this.image_view);
		} else {
			this.setSize(400, 106);
		}

		this.add(this.label_title);
		this.add(this.text);
	}
	
	/**
	 * @return the time_left_in_second
	 */
	public double getTimeLeftInSecond() {
		return time_left_in_second;
	}

	public void setAction(MouseClickEvent<Notification> action) {
		this.setAction(action);
	}

	@Override
	public void load() {
		// Background colors
		DataParameters.applyBackgroundColor(this, "notification_background_color", getStyle());
		DataParameters.applyBackgroundColor(this, "notification_background_color_focused", getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "notification_background_color_hovered", getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "notification_background_color_pressed", getPressedStyle());

		// Border colors
		DataParameters.applyBorderColor(this, "notification_border_color", getStyle());
		DataParameters.applyBorderColor(this, "notification_border_color_focused", getFocusedStyle());
		DataParameters.applyBorderColor(this, "notification_border_color_hovered", getHoveredStyle());
		DataParameters.applyBorderColor(this, "notification_border_color_pressed", getPressedStyle());

		this.text.setVariable("notification_text");
		this.label_title.setVariable("notification_title");

		this.text.load();
		this.label_title.load();

		if (DataParameters.has(this, "notification_border_radius")) {
			getStyle().setBorderRadius(DataParameters.getFloat(this, "notification_border_radius"));
		}
	}

	@Override
	public void update() {
	}

	@Override
	public String getComponentName() {
		return "notification";
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
		return this.variable;
	}
}
