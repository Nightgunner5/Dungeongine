package dungeongine.api.event.player;

import dungeongine.api.entity.Player;
import dungeongine.api.event.Cancellable;

import java.util.Set;

public interface PlayerChatEvent extends PlayerEvent, Cancellable {
	void setMessage(String message);

	String getMessage();

	String getRawMessage();

	Set<Player> getRecipients();
}
