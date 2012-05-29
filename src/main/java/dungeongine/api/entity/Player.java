package dungeongine.api.entity;

public interface Player extends Entity {
	/**
	 * Get the name the player used when they logged in.
	 */
	String getName();

	/**
	 * Send a chat message to this player. This may contain basic HTML formatting.
	 */
	void sendChat(String message);
}
