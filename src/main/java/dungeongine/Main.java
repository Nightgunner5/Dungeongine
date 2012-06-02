package dungeongine;

import dungeongine.apiimpl.StorageImpl;
import dungeongine.gui.GUI;
import dungeongine.gui.ItemEditorGUI;

public final class Main {
	public static final int PORT = 0xD61F;
	public static final int DB_PORT = 0xD61E;

	private Main() {
	}

	public static void main(String[] args) {
		StorageImpl.init();
		if (args.length > 0) {
			switch (args[0]) {
				case "items":
					ItemEditorGUI.start();
			}
		}
		GUI.start();
	}
}
