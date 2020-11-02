package isotopestudio.backdoor.engine.components;

import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.event.MouseClickEvent;
import org.liquidengine.legui.listener.MouseClickEventListener;

import isotopestudio.backdoor.engine.components.desktop.SelectBox;
import isotopestudio.backdoor.engine.datapack.DataParameters;
import isotopestudio.backdoor.game.BackdoorGame;

public interface IComponent {

	public abstract void load();
	public abstract void update();
	
	public abstract String getComponentName();
	public abstract String getComponentVariable();
	
	public default void initIComponent() {
		MouseClickEventListener mouseClick = new MouseClickEventListener() {
			@Override
			public void process(MouseClickEvent e) {
				BackdoorGame.target_component_variable = getComponentVariable();	
			}
		};
		if(this instanceof Component) {
			((Component) this).getListenerMap().addListener(MouseClickEvent.class, mouseClick);	
		}
		if(this instanceof SelectBox<?>) {
			((SelectBox<?>) this).getExpandButton().getListenerMap().addListener(MouseClickEvent.class, mouseClick);	
			((SelectBox<?>) this).getSelectionButton().getListenerMap().addListener(MouseClickEvent.class, mouseClick);	
		}
	}

    public default DataParameters getDataParameters(Component component){
    	if(component instanceof IComponent) {
    		IComponent iComponent = (IComponent) component;

    		if(iComponent.getComponentVariable() != null) {
    			DataParameters data = BackdoorGame.getDatapack().getData(iComponent.getComponentVariable());
    			if(data != null)
    			return BackdoorGame.getDatapack().getData(iComponent.getComponentVariable());
    		}
    	}

    	DataParameters data;
    	if((data = getParentDataParameters(component.getParent())) != null) {
    		return data;
    	}
    	if(component instanceof IComponent) {
    		IComponent iComponent = (IComponent) component;
    		return BackdoorGame.getDatapack().getData(iComponent.getComponentName());	
    	}
    	return null;
    }
    
    public default DataParameters getParentDataParameters(Component component) {
    	if(component == null)return null;
    	if(component instanceof IComponent) {
    		IComponent iComponent = (IComponent) component;

    		if(iComponent.getComponentVariable() != null) {
    			DataParameters data = BackdoorGame.getDatapack().getData(iComponent.getComponentVariable());
    			if(data!=null) 
    			return BackdoorGame.getDatapack().getData(iComponent.getComponentVariable());
    		}
		}
		if(component.getParent() != null) {
			return getParentDataParameters(component.getParent());	
		}
		return null;
    }
}