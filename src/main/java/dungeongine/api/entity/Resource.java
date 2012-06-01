package dungeongine.api.entity;

import dungeongine.api.item.Item;

public interface Resource extends Entity {
	void setRespawnTime(long ticks);
	long getRespawnTime();

	void setTimeRemaining(long ticks);
	long getTimeRemaining();
	boolean isSpawned();
	void despawn();
	void forceRespawn();

	Item[] getDrops();
	void setDrops(Item[] drops);
}
