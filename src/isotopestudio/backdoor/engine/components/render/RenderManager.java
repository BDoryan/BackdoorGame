package isotopestudio.backdoor.engine.components.render;

import org.liquidengine.legui.system.renderer.nvg.NvgRendererProvider;

import isotopestudio.backdoor.engine.components.desktop.Text;
import isotopestudio.backdoor.engine.components.desktop.checkbox.CheckBox;
import isotopestudio.backdoor.engine.components.desktop.tooltip.Tooltip;
import isotopestudio.backdoor.engine.components.painting.Circle;
import isotopestudio.backdoor.engine.components.painting.Line;
import isotopestudio.backdoor.engine.components.painting.Round;
import isotopestudio.backdoor.engine.components.render.components.CircleRenderer;
import isotopestudio.backdoor.engine.components.render.components.EntityComponentRenderer;
import isotopestudio.backdoor.engine.components.render.components.LineRenderer;
import isotopestudio.backdoor.engine.components.render.components.RoundRenderer;
import isotopestudio.backdoor.engine.components.render.components.TextRenderer;
import isotopestudio.backdoor.engine.components.render.custom.NvgCheckBoxRenderer;
import isotopestudio.backdoor.engine.components.render.custom.NvgTooltipRenderer;
import isotopestudio.backdoor.game.game.component.GameElementComponent;

public class RenderManager {

	public static void init() {
		NvgRendererProvider.getInstance().putComponentRenderer(Circle.class, new CircleRenderer());
		NvgRendererProvider.getInstance().putComponentRenderer(Round.class, new RoundRenderer());
		NvgRendererProvider.getInstance().putComponentRenderer(Line.class, new LineRenderer());
		NvgRendererProvider.getInstance().putComponentRenderer(GameElementComponent.class, new EntityComponentRenderer());
		NvgRendererProvider.getInstance().putComponentRenderer(Text.class, new TextRenderer());
		NvgRendererProvider.getInstance().putComponentRenderer(Tooltip.class, new NvgTooltipRenderer());
		NvgRendererProvider.getInstance().putComponentRenderer(CheckBox.class, new NvgCheckBoxRenderer());
		System.out.println("RenderManager.init();");
	}
}
