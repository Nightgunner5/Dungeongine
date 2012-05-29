package dungeongine.server;

import dungeongine.api.Events;
import dungeongine.api.entity.Player;
import dungeongine.api.event.player.PlayerChatEvent;
import dungeongine.apiimpl.event.player.PlayerChatEventImpl;
import dungeongine.net.Connection;
import dungeongine.net.packet.Packet;
import dungeongine.net.packet.Packet02Chat;
import dungeongine.net.packet.PacketListener;

public class ServerChatListener implements PacketListener {
	@Override
	public boolean accept(Packet packet, Connection connection) {
		if (packet instanceof Packet02Chat) {
			Packet02Chat chat = (Packet02Chat) packet;
			if (chat.getMessage().trim().isEmpty())
				return true;
			PlayerChatEvent event = new PlayerChatEventImpl(connection, "<b>" + connection.getVar("name") + ":</b> " + chat.getMessage().replace("<", "&lt;").trim());
			Events.dispatch(event);
			if (!event.isCancelled() && !event.getMessage().isEmpty()) {
				for (Player recipient : event.getRecipients()) {
					recipient.sendChat(event.getMessage());
				}
			}
			return true;
		}
		return false;
	}
}
