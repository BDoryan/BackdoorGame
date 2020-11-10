package isotopestudio.backdoor.game.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import isotopestudio.backdoor.game.event.events.GroupUpdateEvent;

/**
 * @author BESSIERE
 * @github https://www.github.com/DoryanBessiere/
 */
public class EventManager {
	
    private static Map<Class<?>, ArrayList<? extends EventListener>> listeners = new ConcurrentHashMap<>();
	
	static {
		listeners.put(GroupUpdateEvent.class, new ArrayList<>());
	}

	public static <E extends Event> void addListener(Class<E> event, EventListener<E> listener) {
		getListeners(event).add(listener);
	}

	public static <E extends Event> void removeListener(Class<E> event, EventListener<E> listener) {
		getListeners(event).remove(listener);
	}
	
    public static <E extends Event> List<EventListener<E>> getListeners(Class<E> eventClass) {
        List<EventListener<E>> eventListeners = (List<EventListener<E>>) listeners.get(eventClass);
        if (eventListeners == null) {
        	return null;
        }
        return eventListeners;
    }
	
	public static void handle(Event event) {
        List<Object> listeners = new ArrayList<>();
        listeners.addAll(getListeners(event.getClass()));
		listeners.forEach((listener) -> ((EventListener) listener).process(event));
	}
}
