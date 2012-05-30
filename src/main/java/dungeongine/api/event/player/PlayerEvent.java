package dungeongine.api.event.player;

import dungeongine.api.entity.Player;
import dungeongine.api.event.Event;

/** An {@link Event} relating to (usually caused by) a {@link Player}. */
public interface PlayerEvent extends Event {
	/** Gets the {@link Player} related to (usually causing) this event. */
	Player getPlayer();
}
