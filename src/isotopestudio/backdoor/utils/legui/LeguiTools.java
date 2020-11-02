package isotopestudio.backdoor.utils.legui;

import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.style.color.ColorConstants;

/**
 * @author BESSIERE
 * @github https://www.github.com/DoryanBessiere/
 */
public class LeguiTools {

	public static void setInvisible(Component component) {
		component.getStyle().getBackground().setColor(ColorConstants.transparent());
		component.getHoveredStyle().getBackground().setColor(ColorConstants.transparent());
		component.getPressedStyle().getBackground().setColor(ColorConstants.transparent());
		component.getFocusedStyle().getBackground().setColor(ColorConstants.transparent());

		component.getStyle().getBorder().setEnabled(false);
		component.getHoveredStyle().getBorder().setEnabled(false);
		component.getPressedStyle().getBorder().setEnabled(false);
		component.getFocusedStyle().getBorder().setEnabled(false);

		component.getStyle().setShadow(null);
		component.getHoveredStyle().setShadow(null);
		component.getPressedStyle().setShadow(null);
		component.getFocusedStyle().setShadow(null);
	}
}
