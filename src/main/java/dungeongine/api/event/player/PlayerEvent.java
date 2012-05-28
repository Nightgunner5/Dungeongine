package dungeongine.api.event.player;

import dungeongine.api.entity.Player;
import dungeongine.api.event.Event;

public interface PlayerEvent extends Event {
	Player getPlayer();
}
