package dungeongine.bootstrap.net.packet;

import dungeongine.bootstrap.net.Connection;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.UUID;

public class Packet01Handshake implements Packet {
	private long version;
	private String uuid;
	private String playerName;

	public Packet01Handshake() {
	}

	public Packet01Handshake(String playerName) {
		this(playerName, UUID.randomUUID().toString());
	}

	public Packet01Handshake(String playerName, String uuid) {
		this.version = Connection.NETWORK_VERSION;
		this.uuid = uuid;
		this.playerName = playerName;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	@Override
	public void read(DataInput input) throws IOException {
		version = input.readLong();
		uuid = input.readUTF();
		playerName = input.readUTF();
	}

	@Override
	public void write(DataOutput output) throws IOException {
		output.writeLong(version);
		output.writeUTF(uuid);
		output.writeUTF(playerName);
	}
}
