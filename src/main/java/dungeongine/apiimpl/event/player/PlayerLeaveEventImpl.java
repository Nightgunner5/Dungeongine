package dungeongine.apiimpl.event.player;

import dungeongine.api.event.player.PlayerLeaveEvent;
import dungeongine.net.Connection;

public class PlayerLeaveEventImpl extends PlayerEventImpl implements PlayerLeaveEvent {
	private String message;

	public PlayerLeaveEventImpl(Connection connection) {
		super(connection);
		message = String.format("<b>%s</b> disconnected.", connection.getVar("name"));
	}

	@Override
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
