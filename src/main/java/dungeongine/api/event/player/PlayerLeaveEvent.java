package dungeongine.api.event.player;

/** Fired when a {@link dungeongine.api.entity.Player} leaves the server. */
public interface PlayerLeaveEvent extends PlayerEvent {
	/** Sets the message to be shown to all players. An empty string or null will cause the message to be suppressed. */
	void setMessage(String message);

	/** Gets the message to be shown to all players. */
	String getMessage();
}
