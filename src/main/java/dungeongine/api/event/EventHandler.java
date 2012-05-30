package dungeongine.api.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method to be called when an event is fired. The signature for the method must be a single argument of the
 * type given for {@link #type()}. Return values are ignored.
 *
 * @see EventListener
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
	/** The type of events the annotated function handles. Subclasses do not fire the handlers for their superclasses. */
	Class<? extends Event> type();

	/**
	 * The priority of this EventHandler, where lower numbers are executed before higher numbers. This may be any valid
	 * integer.
	 */
	int priority() default 0;
}
