package dungeongine.api.entity;

public interface Player extends Entity {
	/**
	 * Get the name the player used to log in with.
	 * @return The player's name
	 */
	String getName();

	/**
	 * Send a chat message to this player.
	 * @param message The message
	 */
	void sendChat(String message);
}
