package dungeongine.api.event.player;

import dungeongine.api.event.Cancellable;

import java.net.InetAddress;

/**
 * Fired when a {@link dungeongine.api.entity.Player} joins the server. This event can be cancelled to kick the player
 * from the server immediately.
 */
public interface PlayerJoinEvent extends PlayerEvent, Cancellable {
	/**
	 * Sets the message to be shown to all players if this join is successful. An empty string or null will cause the
	 * message to be suppressed.
	 */
	void setMessage(String message);

	/** Gets the message to be shown to all players if this join is successful. */
	String getMessage();

	/** Gets the IP address of the player attempting to connect. */
	InetAddress getAddress();
}
