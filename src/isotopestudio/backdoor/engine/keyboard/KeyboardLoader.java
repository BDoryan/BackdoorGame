package isotopestudio.backdoor.engine.keyboard;

import org.liquidengine.legui.config.Configuration;
import org.liquidengine.legui.input.Keyboard;

public class KeyboardLoader {

	public static boolean load(String keyboard_layout) {
		try {
		    Keyboard.updateMapping(Configuration.getInstance().getKeyboardLayouts().get(keyboard_layout));
			Configuration.getInstance().setKeyboardLayout(keyboard_layout);

			System.out.println("[KeyboardLoader] Keyboard layout load with success.");
		    return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.err.println("[KeyboardLoader] Failed to load '"+keyboard_layout+"'");
		return false;
	}
}
