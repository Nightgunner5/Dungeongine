package dungeongine.server;

import dungeongine.api.event.EventHandler;
import dungeongine.api.event.EventListener;
import dungeongine.api.event.player.PlayerChatEvent;
import dungeongine.api.event.player.PlayerJoinEvent;
import dungeongine.api.event.player.PlayerLeaveEvent;

import java.util.logging.Logger;

public class ServerLogger implements EventListener {
	@EventHandler(type = PlayerChatEvent.class, priority = Integer.MAX_VALUE)
	public void onPlayerChat(PlayerChatEvent event) {
		if (event.isCancelled())
			return;
		Logger.getLogger(ServerLogger.class.getName()).info(String.format("[Chat] %s: %s", event.getPlayer().getName(), event.getMessage()));
	}

	@EventHandler(type = PlayerJoinEvent.class, priority = Integer.MAX_VALUE)
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (event.isCancelled()) {
			Logger.getLogger(ServerLogger.class.getName()).info(String.format("[Denied Join] %s", event.getPlayer().getName()));
			return;
		}
		Logger.getLogger(ServerLogger.class.getName()).info(String.format("[Join] %s", event.getPlayer().getName()));
	}

	@EventHandler(type = PlayerLeaveEvent.class, priority = Integer.MAX_VALUE)
	public void onPlayerLeave(PlayerLeaveEvent event) {
		Logger.getLogger(ServerLogger.class.getName()).info(String.format("[Leave] %s", event.getPlayer().getName()));
	}
}
