package dungeongine.apiimpl;

import dungeongine.api.Server;
import dungeongine.api.entity.Player;
import dungeongine.api.map.World;
import dungeongine.apiimpl.entity.PlayerImpl;
import dungeongine.net.Connection;
import dungeongine.net.packet.Packet02Chat;

public class ServerImpl implements Server {
	@Override
	public Player[] getOnlinePlayers() {
		Player[] players = new Player[dungeongine.server.Server.clientMap.size()];
		int i = 0;
		for (Connection connection : dungeongine.server.Server.clientMap.values()) {
			players[i++] = PlayerImpl.get(connection);
		}
		return players;
	}

	@Override
	public void broadcastChatMessage(String message) {
		dungeongine.server.Server.broadcast(new Packet02Chat(message));
	}

	@Override
	public World getWorld(String name) {
		return null;
	}
}
