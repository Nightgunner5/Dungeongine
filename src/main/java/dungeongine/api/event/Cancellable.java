package dungeongine.api.event;

/** Represents an event that can be cancelled. */
public interface Cancellable {
	/** Sets the cancelled status of this event. */
	void setCancelled(boolean cancelled);

	/** Gets the cancelled status of this event. */
	boolean isCancelled();
}
