package dungeongine.api.event.player;

import dungeongine.api.event.Cancellable;

import java.net.InetAddress;

public interface PlayerJoinEvent extends PlayerEvent, Cancellable {
	void setMessage(String message);

	String getMessage();

	InetAddress getAddress();
}
