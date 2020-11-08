package isotopestudio.backdoor.game.event;

/**
 * @author BESSIERE
 * @github https://www.github.com/DoryanBessiere/
 */
public interface EventListener<E extends Event> {

	public abstract void process(E event);
	
}
