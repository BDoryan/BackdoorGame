package isotopestudio.backdoor.gateway.listeners;

import doryanbessiere.isotopestudio.api.profile.Profile;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public abstract class ChatListener {

	public abstract void message(Profile profile, long time, String message);
	
}
