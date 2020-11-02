package isotopestudio.backdoor.engine.components.desktop.dialog;

import org.liquidengine.legui.component.Layer;

/**
 * Dialog layer used to hold dialog window.
 */
public class DialogLayer extends Layer {

    /**
     * Default constructor of dialog layer.
     */
    public DialogLayer() {
        setEventPassable(false);
        setEventReceivable(true);

        getStyle().getBackground().setColor(0, 0, 0, 0.2f);
    }
}