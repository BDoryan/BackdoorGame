package isotopestudio.backdoor.game.manager;

import java.util.ArrayList;

import org.liquidengine.legui.event.Event;

import isotopestudio.backdoor.game.event.EventManager;
import isotopestudio.backdoor.game.event.events.GroupUpdateEvent;
import isotopestudio.backdoor.gateway.group.GroupObject;

/**
 * @author BESSIERE
 * @github https://www.github.com/DoryanBessiere/
 */
public class GroupManager {
	
	private static GroupObject group;

	public static void kick() {
	}

	public static void start() {
	}

	public static void ready() {
	}

	public static void unready() {
	}

	public static void search() {
	}

	public static void unsearch() {
	}
	
	/**
	 * @return the group
	 */
	public static GroupObject getGroup() {
		return group;
	}
	
	/**
	 * @param group the group to set
	 */
	public static void setGroup(GroupObject group) {
		EventManager.handle(new GroupUpdateEvent(group));
		GroupManager.group = group;
	}
}
