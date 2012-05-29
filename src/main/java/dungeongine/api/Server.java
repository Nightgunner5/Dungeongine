package dungeongine.api;

import dungeongine.api.entity.Player;
import dungeongine.api.map.World;

public interface Server {
	Player[] getOnlinePlayers();

	void broadcastChatMessage(String message);

	World getWorld(String name);

	void reloadPlugins();

	boolean isPluginAvailable(String name);

	boolean isPluginLoaded(String name);

	void loadPlugin(String name);

	void unloadPlugin(String name);

	Plugin getPlugin(String name);
}
