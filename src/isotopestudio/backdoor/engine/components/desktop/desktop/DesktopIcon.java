package isotopestudio.backdoor.engine.components.desktop.desktop;

import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.component.ImageView;
import org.liquidengine.legui.component.event.label.LabelContentChangeEvent;
import org.liquidengine.legui.component.event.label.LabelWidthChangeEvent;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.event.MouseClickEvent.MouseClickAction;
import org.liquidengine.legui.image.Image;
import org.liquidengine.legui.listener.MouseClickEventListener;
import org.liquidengine.legui.style.Style.DisplayType;
import org.liquidengine.legui.style.Style.PositionType;
import org.liquidengine.legui.style.color.ColorConstants;
import org.liquidengine.legui.style.length.Auto;

import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.components.desktop.Label;
import isotopestudio.backdoor.game.BackdoorGame;
import isotopestudio.backdoor.game.BackdoorGame.IUpdate;

public class DesktopIcon extends Component implements IComponent {

	private ImageView icon;
	private Label name;

	public DesktopIcon(int x, int y, Image icon, String name, Runnable click) {
		this.icon = new ImageView();
		this.name = new Label(name);

		add(this.icon);
		add(this.name);

		this.getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
			long time = 0L;

			@Override
			public void process(MouseClickEvent event) {
				if (event.getAction() != MouseClickAction.RELEASE) return;
				if(time == 0L) {
					time = System.currentTimeMillis();	
				} else {
					if(System.currentTimeMillis() - time < 800) {
						click.run();
					}
					time = 0L;
				}
			}
		});

		this.getStyle().setDisplay(DisplayType.FLEX);
		this.getStyle().setLeft(x);
		this.getStyle().setTop(y);
		this.getStyle().getBorder().setEnabled(false);
		this.getStyle().setBorderRadius(0f);

		this.setPosition(x, y);
		this.setSize(80, 80);

		this.icon.setImage(icon);
		this.icon.getStyle().setPosition(PositionType.ABSOLUTE);
		this.icon.getStyle().setWidth(40);
		this.icon.getStyle().setHeight(40);
		this.icon.getStyle().setTop(10);
		this.icon.getStyle().setLeft(20);
		this.icon.getStyle().getBorder().setEnabled(false);
		this.icon.getStyle().setShadow(null);
		this.icon.setFocusable(false);
		this.icon.getStyle().getBackground().getColor().set(0f, 0f, 0f, 0f);

		this.name.getStyle().setTextColor(ColorConstants.white());
		this.name.getStyle().getBackground().getColor().set(ColorConstants.transparent());
		this.name.getStyle().setFont("main");
		this.name.getStyle().setFontSize(13F);
		this.name.getStyle().setPosition(PositionType.RELATIVE);
		this.name.getStyle().setMarginLeft(Auto.AUTO);
		this.name.getStyle().setMarginRight(Auto.AUTO);
		this.name.getStyle().setHeight(15f);
		this.name.getStyle().setTop(60f);
		this.name.setFocusable(false);
		this.name.getListenerMap().addListener(LabelWidthChangeEvent.class,
				e -> this.name.getStyle().setMinWidth(this.name.getTextState().getTextWidth()));
		this.name.getTextState().setText(name);

		initIComponent();
	}

	@Override
	public void update() {
		if (isFocused()) {
			getStyle().getBackground().getColor().set(1f, 1f, 1f, 0.35f);
		} else if (isHovered()) {
			getStyle().getBackground().getColor().set(1f, 1f, 1f, 0.25f);
		} else {
			getStyle().getBackground().getColor().set(1f, 1f, 1f, 0f);
		}
		getStyle().setBorder(null);
		getStyle().setShadow(null);
		getStyle().setBorderRadius(0f);
	}

	@Override
	public void load() {
	}

	@Override
	public String getComponentName() {
		return "desktopicon";
	}

	@Override
	public String getComponentVariable() {
		return null;
	}
}
