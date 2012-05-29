package dungeongine.api;

import com.google.common.collect.Maps;
import dungeongine.api.event.Event;
import dungeongine.api.event.EventHandler;
import dungeongine.api.event.EventListener;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Events {
	private static final Map<Class<? extends Event>, SortedSet<RegisteredHandler>> handlers = Maps.newIdentityHashMap();

	/**
	 * Register an event listener. The methods that handle events must be annotated with {@link
	 * dungeongine.api.event.EventHandler}.
	 */
	public static synchronized void register(EventListener listener) {
		for (Method method : listener.getClass().getDeclaredMethods()) {
			EventHandler annotation = method.getAnnotation(EventHandler.class);
			if (annotation != null) {
				RegisteredHandler handler = new RegisteredHandler();
				handler.object = listener;
				handler.method = method;
				handler.priority = annotation.priority();
				getHandlerSet(annotation.type()).add(handler);
			}
		}
	}

	/** Unregister an event listener that was previously registered using {@link #register(dungeongine.api.event.EventListener)}. */
	public static synchronized void unregister(EventListener listener) {
		for (SortedSet<RegisteredHandler> registeredHandlers : handlers.values()) {
			for (Iterator<RegisteredHandler> it = registeredHandlers.iterator(); it.hasNext(); ) {
				if (it.next().object == listener)
					it.remove();
			}
		}
	}

	/** Dispatch an event to the registered listeners. */
	public static synchronized void dispatch(Event event) {
		Logger.getLogger(Events.class.getName()).log(Level.FINE, "Dispatching event: " + event);
		for (RegisteredHandler handler : getHandlerSet((Class<? extends Event>) event.getClass().getInterfaces()[0])) {
			try {
				handler.method.invoke(handler.object, event);
			} catch (Exception ex) {
				Logger.getLogger(Events.class.getName()).log(Level.SEVERE, "Exception in event handler " + handler, ex);
			}
		}
	}

	private static synchronized SortedSet<RegisteredHandler> getHandlerSet(Class<? extends Event> type) {
		if (!handlers.containsKey(type)) {
			SortedSet<RegisteredHandler> set = new TreeSet<>();
			handlers.put(type, set);
			return set;
		}
		return handlers.get(type);
	}

	private static class RegisteredHandler implements Comparable<RegisteredHandler> {
		EventListener object;
		Method method;
		int priority;

		@Override
		public int compareTo(RegisteredHandler o) {
			return priority - o.priority;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			RegisteredHandler that = (RegisteredHandler) o;

			return method == that.method && object == that.object;
		}

		@Override
		public int hashCode() {
			int result = object != null ? object.hashCode() : 0;
			result = 31 * result + (method != null ? method.hashCode() : 0);
			return result;
		}

		@Override
		public String toString() {
			return String.format("EventHandler {object = %s, method = %s, priority = %d}", object, method, priority);
		}
	}
}
