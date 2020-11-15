package isotopestudio.backdoor.engine.components.listeners;

import org.liquidengine.legui.listener.EventListener;

import isotopestudio.backdoor.engine.components.events.TooltipSizeEvent;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class TooltipAutoSize implements EventListener<TooltipSizeEvent>{

	@Override
	public void process(TooltipSizeEvent event) {
		event.getTargetComponent().setSize(event.getWidth(), event.getHeight());
	}
}
