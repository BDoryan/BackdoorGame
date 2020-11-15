package isotopestudio.backdoor.game.event;

/**
 * @author BDoryan
 * @github https://www.github.com/BDoryan/
 */
public interface EventListener<E extends Event> {

	public abstract void process(E event);
	
}
