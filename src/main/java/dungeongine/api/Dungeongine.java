package dungeongine.api;

import dungeongine.api.map.World;
import dungeongine.apiimpl.ServerImpl;

public final class Dungeongine {
	private Dungeongine() {
	}

	private static final Server server = new ServerImpl();

	public static Server getServer() {
		return server;
	}

	public static World getWorld(String name) {
		return server.getWorld(name);
	}
}
