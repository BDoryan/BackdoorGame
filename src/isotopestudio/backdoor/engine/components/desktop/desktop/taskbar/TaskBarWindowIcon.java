package isotopestudio.backdoor.engine.components.desktop.desktop.taskbar;

import org.joml.Vector4f;
import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.component.ImageView;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.event.MouseClickEvent.MouseClickAction;
import org.liquidengine.legui.image.Image;
import org.liquidengine.legui.listener.MouseClickEventListener;
import org.liquidengine.legui.style.Style.DisplayType;
import org.liquidengine.legui.style.color.ColorUtil;

import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.components.desktop.window.Window;
import isotopestudio.backdoor.engine.components.painting.Line;
import isotopestudio.backdoor.engine.datapack.DataParameters;
import isotopestudio.backdoor.game.BackdoorGame;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class TaskBarWindowIcon extends Component implements IComponent {

	private Image icon;
	private Line status_line;
	
	private Window window;

	private ImageView iconView;
	
	public TaskBarWindowIcon(Window window, Image icon, float width, float height) {
		this.window = window;

		getStyle().setDisplay(DisplayType.NONE);
		getStyle().setBorder(null);
		getStyle().setShadow(null);
		getStyle().getBackground().setColor(0,0,0,0);
		getStyle().setWidth(width);
		getStyle().setHeight(width);
		
		setIcon(icon);
		getStyle().setDisplay(DisplayType.FLEX);
		
		this.status_line = new Line(4, 0, width - 6, width, width - 6);
		this.add(status_line);
	}
	
	/**
	 * @return the icon
	 */
	public Image getIcon() {
		return icon;
	}
	
	/**
	 * @param icon the icon to set
	 */
	public void setIcon(Image icon) {
		this.icon = icon;

		if(this.iconView != null) {
			remove(this.iconView);
		}
		
		if(icon == null) {
			this.iconView = null;
			return;
		}

		this.iconView = new ImageView(icon);
		iconView.getStyle().setWidth((float)getStyle().getWidth().get() -  12);
		iconView.getStyle().setHeight((float)getStyle().getWidth().get() - 12);
		iconView.getStyle().setLeft(6);
		iconView.getStyle().setBottom(12);
		iconView.getStyle().getBackground().setColor(new Vector4f(0,0,0,0));
		iconView.getStyle().setShadow(null);
		iconView.getStyle().getBorder().setEnabled(false);
		iconView.getStyle().setBorderRadius(0F);
		iconView.setFocusable(false);
		
		getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
			@Override
			public void process(MouseClickEvent event) {
				if(event.getAction() == MouseClickAction.RELEASE)
				window.showWindow();
			}
		});
		
		add(this.iconView);
	}
	
	@Override
	public void load() {
		DataParameters.applyBackgroundColor(this, "taskbarwindowicon_background_color", getStyle());
		DataParameters.applyBackgroundColor(this, "taskbarwindowicon_background_color_focused", getFocusedStyle());
		DataParameters.applyBackgroundColor(this, "taskbarwindowicon_background_color_hovered", getHoveredStyle());
		DataParameters.applyBackgroundColor(this, "taskbarwindowicon_background_color_pressed", getPressedStyle());

		// Border colors
		DataParameters.applyBorderColor(this, "taskbarwindowicon_border_color", getStyle());
		DataParameters.applyBorderColor(this, "taskbarwindowicon_border_color_focused", getFocusedStyle());
		DataParameters.applyBorderColor(this, "taskbarwindowicon_border_color_hovered", getHoveredStyle());
		DataParameters.applyBorderColor(this, "taskbarwindowicon_border_color_pressed", getPressedStyle());

		if (DataParameters.has(this, "taskbarwindowicon_border_radius")) {
			getStyle().setBorderRadius(DataParameters.getFloat(this, "taskbarwindowicon_border_radius"));
		}
	}

	@Override
	public void update() {
		if(window.isWindowTargeted()) {
			float[] color = DataParameters.getColor(this, "taskbarwindowicon_bar_color_when_window_is_targeted");
			status_line.getColor().set(color[0], color[1], color[2], color[3]);
		} else {
			float[] color = DataParameters.getColor(this, "taskbarwindowicon_bar_color_when_window_is_not_target");
			status_line.getColor().set(color[0], color[1], color[2], color[3]);
		}
	}

	@Override
	public String getComponentName() {
		return "taskbarwindowicon";
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
