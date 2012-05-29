package dungeongine.api.event;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.RUNTIME)
public @interface EventHandler {
	Class<? extends Event> type();

	/**
	 * The priority of this EventHandler, where lower numbers are executed before higher numbers. This may be any valid
	 * integer.
	 */
	int priority() default 0;
}
