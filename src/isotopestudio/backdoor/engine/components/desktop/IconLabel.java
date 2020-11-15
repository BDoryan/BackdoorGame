package isotopestudio.backdoor.engine.components.desktop;

import org.joml.Vector4f;
import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.component.ImageView;
import org.liquidengine.legui.component.event.label.LabelWidthChangeEvent;
import org.liquidengine.legui.component.misc.listener.label.UpdateLabelWidthListener;
import org.liquidengine.legui.image.Image;
import org.liquidengine.legui.style.Style.DisplayType;

import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.components.events.LabelSizeEvent;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class IconLabel extends Component implements IComponent {

	public static final int ICON_LEFT_POSITION = 0x1;
	public static final int ICON_RIGHT_POSITION = 0x2;

	private Label label;
	private ImageView icon_view;

	private int icon_position;
	private int padding = 10;

	public IconLabel(String text, Image icon, LabelSizeEvent sizeEvent) {
		super();
		this.label = new Label(text) {
			@Override
			public String getComponentName() {
				return IconLabel.this.getComponentName();
			}
		};
		this.label.getStyle().setFontSize(20f);
		this.label.getListenerMap().addListener(LabelWidthChangeEvent.class, new UpdateLabelWidthListener() {
			@Override
			public void process(LabelWidthChangeEvent event) {
				super.process(event);

				float width = label.getStyle().getFontSize() + padding + event.getWidth() + padding;
				float height = label.getStyle().getFontSize();

				getStyle().setWidth(width);
				getStyle().setHeight(height);

				setSize(width, height);
				sizeEvent.process(width, height);
			}
		});

		this.getStyle().getBackground().setColor(new Vector4f(1, 1, 1, 0f));
		this.getStyle().setShadow(null);
		this.getStyle().getBorder().setEnabled(false);
		this.getStyle().setBorderRadius(0F);

		this.icon_view = new ImageView(icon);
		this.icon_view.getStyle().getBackground().setColor(new Vector4f(0, 0, 0, 0));
		this.icon_view.getStyle().setShadow(null);
		this.icon_view.getStyle().getBorder().setEnabled(false);
		this.icon_view.getStyle().setBorderRadius(0F);
		this.icon_view.getStyle().setWidth(label.getStyle().getFontSize());
		this.icon_view.getStyle().setHeight(label.getStyle().getFontSize());

		this.add(icon_view);
		this.add(label);

		this.getStyle().setDisplay(DisplayType.FLEX);
		
		setSize(this.getStyle().getFontSize() + padding + 20, padding+this.getStyle().getFontSize());

		this.setIconPosition(ICON_LEFT_POSITION);
		
		initIComponent();
	}

	/**
	 * @return the label
	 */
	public Label getLabel() {
		return label;
	}

	/**
	 * @return the icon_view
	 */
	public ImageView getIconView() {
		return icon_view;
	}

	/**
	 * @param icon_position the icon_position to set
	 */
	public void setIconPosition(int icon_position) {
		this.icon_position = icon_position;

		if (icon_position == ICON_LEFT_POSITION) {
			this.icon_view.getStyle().setRight(null);
			this.icon_view.getStyle().setLeft(0f);

			this.label.getStyle().setLeft(this.label.getStyle().getFontSize() + padding);
			this.label.getStyle().setRight(0);
		} else {
			this.icon_view.getStyle().setRight(0f);
			this.icon_view.getStyle().setLeft(null);

			this.label.getStyle().setLeft(0f);
			this.label.getStyle().setRight(null);
		}
	}

	/**
	 * @return the icon_position
	 */
	public int getIconPosition() {
		return icon_position;
	}

	@Override
	public void load() {
		label.load();
	}

	@Override
	public void update() {
	}

	@Override
	public String getComponentName() {
		return "iconlabel";
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
}
