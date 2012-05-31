package dungeongine;

import dungeongine.apiimpl.StorageImpl;
import dungeongine.gui.GUI;

public final class Main {
	public static final int PORT = 0xD61F;
	public static final int DB_PORT = 0xD61E;

	private Main() {
	}

	public static void main(String[] args) {
		StorageImpl.init();
		GUI.start();
	}
}
