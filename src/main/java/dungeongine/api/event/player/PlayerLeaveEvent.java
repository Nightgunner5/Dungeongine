package dungeongine.api.event.player;

public interface PlayerLeaveEvent extends PlayerEvent {
	void setMessage(String message);
	String getMessage();
}
