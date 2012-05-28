package dungeongine.net.packet;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Packet03SpellCast implements Packet {
	private String spell;
	private long x;
	private long y;

	@Override
	public void read(DataInput input) throws IOException {
		spell = input.readUTF();
		x = input.readLong();
		y = input.readLong();
	}

	@Override
	public void write(DataOutput output) throws IOException {
		output.writeUTF(spell);
		output.writeLong(x);
		output.writeLong(y);
	}
}
