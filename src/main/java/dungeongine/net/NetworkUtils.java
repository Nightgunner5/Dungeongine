package dungeongine.net;

import com.google.common.net.InetAddresses;
import dungeongine.api.map.Location;
import dungeongine.api.map.Tile;
import dungeongine.apiimpl.map.TileImpl;
import dungeongine.client.Client;
import dungeongine.client.ClientChatListener;
import dungeongine.net.packet.Packet;
import dungeongine.server.Server;
import dungeongine.server.ServerChatListener;
import dungeongine.server.ServerHandshakeListener;
import dungeongine.server.ServerPacketListener;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class NetworkUtils {
	private NetworkUtils() {}

	public static String toString(Packet packet) {
		StringBuilder sb = new StringBuilder(packet.getClass().getSimpleName());
		sb.append(" {");
		boolean first = true;
		for (Field field : packet.getClass().getDeclaredFields()) {
			if (first) {
				first = false;
			} else {
				sb.append(", ");
			}
			field.setAccessible(true);
			try {
				sb.append(field.getName()).append(" = '").append(field.get(packet)).append('\'');
			} catch (IllegalAccessException ex) {
				Logger.getLogger(NetworkUtils.class.getName()).log(Level.SEVERE, "", ex);
			}
		}
		return sb.append('}').toString();
	}

	public static String toString(Connection connection) {
		return new StringBuilder(InetAddresses.toAddrString(connection.socket.getInetAddress()))
				.append(':').append(connection.socket.getPort())
				.append(" {name = '")
				.append(connection.getVar("name"))
				.append("', uuid = '")
				.append(connection.getVar("uuid"))
				.append("'}").toString();
	}

	public static void write(DataOutput output, Location location) throws IOException {
		output.writeUTF(location.getWorldName());
		output.writeLong(location.getX());
		output.writeLong(location.getY());
	}

	public static Location readLocation(DataInput input) throws IOException {
		return new Location(input.readUTF(), input.readLong(),  input.readLong());
	}

	public static void write(DataOutput output, Tile tile) throws IOException {
		write(output, tile.getLocation());
		output.writeBoolean(tile.isPassable());
	}

	public static Tile readTile(DataInput input) throws IOException {
		return new TileImpl(readLocation(input), input.readBoolean());
	}

	public static void registerServerListeners() {
		Server.registerListener(new KeepAliveListener());
		Server.registerListener(new ServerHandshakeListener());
		// All the remaining listeners must be wrapped in ServerPacketListener
		Server.registerListener(new ServerPacketListener(new ServerChatListener()));
	}

	public static void registerClientListeners() {
		Client.addListener(new KeepAliveListener());
		Client.addListener(new ClientChatListener());
	}
}
