package dungeongine.api;

import dungeongine.api.entity.Player;
import dungeongine.api.map.World;

public interface Server {
	Player[] getOnlinePlayers();

	void broadcastChatMessage(String message);

	World getWorld(String name);
}
