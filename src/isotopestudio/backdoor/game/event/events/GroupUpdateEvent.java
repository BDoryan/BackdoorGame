package isotopestudio.backdoor.game.event.events;

import isotopestudio.backdoor.game.event.Event;
import isotopestudio.backdoor.gateway.group.GroupObject;

/**
 * @author BESSIERE
 * @github https://www.github.com/DoryanBessiere/
 */
public class GroupUpdateEvent extends Event {
	
	private GroupObject groupObject;
	
	public GroupUpdateEvent(GroupObject groupObject) {
		super();
		this.groupObject = groupObject;
	}

	/**
	 * @return the groupObject
	 */
	public GroupObject getGroupObject() {
		return groupObject;
	}
	
	/**
	 * @param groupObject the groupObject to set
	 */
	public void setGroupObject(GroupObject groupObject) {
		this.groupObject = groupObject;
	}
}
