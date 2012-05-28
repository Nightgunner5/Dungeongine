package dungeongine.net;

import dungeongine.net.packet.Packet;

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
}
