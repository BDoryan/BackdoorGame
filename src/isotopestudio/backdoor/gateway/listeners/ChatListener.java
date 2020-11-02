package isotopestudio.backdoor.gateway.listeners;

import doryanbessiere.isotopestudio.api.profile.Profile;

/**
 * @author BESSIERE
 * @github https://www.github.com/DoryanBessiere/
 */
public abstract class ChatListener {

	public abstract void message(Profile profile, long time, String message);
	
}
