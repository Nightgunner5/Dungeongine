package dungeongine.api.event;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.RUNTIME)
public @interface EventHandler {
	Class<? extends Event> type();
	int priority() default 0;
}
