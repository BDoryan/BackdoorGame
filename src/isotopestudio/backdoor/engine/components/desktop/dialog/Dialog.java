package isotopestudio.backdoor.engine.components.desktop.dialog;

import org.joml.Vector2f;
import org.liquidengine.legui.component.Frame;
import org.liquidengine.legui.component.misc.listener.dialog.DialogCloseEventListener;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.event.MouseClickEvent.MouseClickAction;
import org.liquidengine.legui.listener.MouseClickEventListener;

import isotopestudio.backdoor.engine.components.IComponent;
import isotopestudio.backdoor.engine.components.desktop.window.Window;
import isotopestudio.backdoor.game.BackdoorGame;

public class Dialog extends Window implements IComponent {

    /**
     * Default dialog title.
     */
    public static final String DEFAULT_DIALOG_TITLE = "Dialog";

    /**
     * Used to hold dialog layer with dialog.
     */
    private Frame frame;
    /**
     * Used to hold dialog.
     */
    private DialogLayer dialogLayer = new DialogLayer();

    public Dialog(String title, float width, float height) {
    	super(title, 0,0,width,height);
        initialize(new Vector2f(width, height), title);
    }

    /**
     * Used to initialize dialog with title and size
     *
     * @param size  size of component.
     * @param title dialog text.
     */
    private void initialize(Vector2f size, String title) {
        getTitleTextState().setText(title);
        if (size != null) {
            setSize(size);
        }
        
        getWindowCloseButton().getListenerMap().addListener(MouseClickEvent.class, new MouseClickEventListener() {
			@Override
			public void process(MouseClickEvent event) {
				if(event.getAction() == MouseClickAction.RELEASE) {
					close();
				}
			}
		});

        this.setMinimizable(false);
    }

    /**
     * Used to show title on specified frame.
     *
     * @param frame frame to show dialog.
     */
    public void show(Frame frame) {
        this.frame = frame;
        if (this.frame != null) {
            Vector2f dialogLayerSize = new Vector2f(frame.getContainer().getSize());
            this.setPosition((dialogLayerSize.x - this.getSize().x) / 2f, (dialogLayerSize.y - this.getSize().y) / 2f);

            this.frame.addLayer(dialogLayer);
            dialogLayer.setSize(dialogLayerSize);
            dialogLayer.add(this);
        }
    }
    
    @Override
    public void hideWindow() {
    	return;
    }

    public DialogLayer getDialogLayer() {
        return dialogLayer;
    }

    /**
     * Used to close dialog.
     */
    public void close() {
		BackdoorGame.getDesktop().removeWindow(Dialog.this);
        if (frame != null) {
            frame.removeLayer(dialogLayer);
        }
    }

	@Override
	public void load() {
		super.load();
	}

	@Override
	public String getComponentName() {
		return "window";
	}
	
	private String variable;
	
	public void setVariable(String variable) {
		this.variable = variable;
	}

	@Override
	public String getComponentVariable() {
		return variable;
	}
}
