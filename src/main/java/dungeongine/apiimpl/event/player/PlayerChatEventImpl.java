package dungeongine.apiimpl.event.player;

import dungeongine.api.entity.Player;
import dungeongine.api.event.player.PlayerChatEvent;
import dungeongine.apiimpl.entity.PlayerImpl;
import dungeongine.net.Connection;
import dungeongine.server.Server;

import java.util.LinkedHashSet;
import java.util.Set;

public class PlayerChatEventImpl extends PlayerEventImpl implements PlayerChatEvent {
	private String message;
	private String rawMessage;
	private final Set<Player> recipients;

	public PlayerChatEventImpl(Connection sender, String rawMessage) {
		super(sender);
		this.rawMessage = rawMessage;
		message = String.format("<b>%s:</b> %s", sender.getVar("name"), rawMessage);
		recipients = new LinkedHashSet<>();
		for (Connection connection : Server.clientMap.values()) {
			recipients.add(PlayerImpl.get(connection));
		}
	}

	@Override
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public String getRawMessage() {
		return rawMessage;
	}

	@Override
	public Set<Player> getRecipients() {
		return recipients;
	}

	@Override
	public String toString() {
		return String.format("PlayerChatEvent {message = '%s', sender = %s, recipients = %s}", message, getPlayer(), recipients);
	}
}
