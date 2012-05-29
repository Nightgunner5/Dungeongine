package dungeongine.api.entity;

public interface Entity {
	/** Get the unique identifier for this entity. The identifier is safe for storage, but the Entity object itself is not. */
	String getId();
}
