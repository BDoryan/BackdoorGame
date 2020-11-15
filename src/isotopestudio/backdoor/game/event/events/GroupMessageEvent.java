package isotopestudio.backdoor.game.event.events;

import isotopestudio.backdoor.game.event.Event;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public class GroupMessageEvent extends Event {
	
	private String type;
	private String message;
	
	public GroupMessageEvent(String type, String message) {
		super();
		this.type = type;
		this.message = message;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
}
