package dungeongine.net.packet;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Packet02Chat implements Packet {
	private String message;

	public Packet02Chat() {
	}

	public Packet02Chat(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public void read(DataInput input) throws IOException {
		message = input.readUTF();
	}

	@Override
	public void write(DataOutput output) throws IOException {
		output.writeUTF(message);
	}
}
