package dungeongine.api.event;

public interface Cancellable {
	void setCancelled(boolean cancelled);
	boolean isCancelled();
}
