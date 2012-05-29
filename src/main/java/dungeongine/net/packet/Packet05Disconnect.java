package dungeongine.net.packet;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Packet05Disconnect implements Packet {
	private String uuid;

	public Packet05Disconnect() {
	}

	public Packet05Disconnect(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public void read(DataInput input) throws IOException {
		uuid = input.readUTF();
	}

	@Override
	public void write(DataOutput output) throws IOException {
		output.writeUTF(uuid);
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
