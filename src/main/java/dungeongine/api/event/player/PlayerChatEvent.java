package dungeongine.api.event.player;

import dungeongine.api.entity.Player;
import dungeongine.api.event.Cancellable;

import java.util.Set;

/** Fired when a player uses the chatbox to send a message. */
public interface PlayerChatEvent extends PlayerEvent, Cancellable {
	/**
	 * Sets the message to be displayed in the chat area of all {@link #getRecipients() recipients}. Basic HTML formatting
	 * can be used.
	 */
	void setMessage(String message);

	/** Gets the message as it will be displayed. */
	String getMessage();

	/** Gets the message in its original form with no formatting. */
	String getRawMessage();

	/** Gets a set containing the recipients of the chat message. {@link Player} objects may be removed from this set. */
	Set<Player> getRecipients();
}
