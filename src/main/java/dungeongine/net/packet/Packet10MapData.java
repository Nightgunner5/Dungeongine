package dungeongine.net.packet;

import dungeongine.api.map.Tile;
import dungeongine.net.NetworkUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Packet10MapData implements Packet {
	private Tile tile;

	public Tile getTile() {
		return tile;
	}

	public void setTile(Tile tile) {
		this.tile = tile;
	}

	@Override
	public void read(DataInput input) throws IOException {
		tile = NetworkUtils.readTile(input);
	}

	@Override
	public void write(DataOutput output) throws IOException {
		NetworkUtils.write(output, tile);
	}
}
