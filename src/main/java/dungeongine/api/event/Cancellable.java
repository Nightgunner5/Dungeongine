package dungeongine.api.event;

/**
 * Represents an event that can be cancelled.
 */
public interface Cancellable {
	/**
	 * Set the cancelled status of this event.
	 */
	void setCancelled(boolean cancelled);

	/**
	 * Get the cancelled status of this event.
	 */
	boolean isCancelled();
}
