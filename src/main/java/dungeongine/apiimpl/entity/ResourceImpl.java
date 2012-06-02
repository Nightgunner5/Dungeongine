package dungeongine.apiimpl.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import dungeongine.api.Dungeongine;
import dungeongine.api.entity.Resource;
import dungeongine.api.item.Item;
import dungeongine.api.item.Items;
import dungeongine.apiimpl.item.ItemReference;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ResourceImpl extends EntityImpl implements Resource {
	private long respawnTime;
	private long timeRemaining;
	private ItemReference[] drops;

	public ResourceImpl(String id) {
		super(id);
	}

	@Override
	public void setRespawnTime(long ticks) {
		Preconditions.checkArgument(ticks >= 0);
		dataChanged("respawnTime", respawnTime, ticks);
		respawnTime = ticks;
	}

	@Override
	public long getRespawnTime() {
		return respawnTime;
	}

	@Override
	public void setTimeRemaining(long ticks) {
		dataChanged("timeRemaining", timeRemaining, ticks);
		respawnTime = Math.max(0, ticks);
	}

	@Override
	public long getTimeRemaining() {
		return timeRemaining;
	}

	@Override
	public boolean isSpawned() {
		return timeRemaining <= 0;
	}

	@Override
	public void despawn() {
		setTimeRemaining(getRespawnTime());
	}

	@Override
	public void forceRespawn() {
		setTimeRemaining(0);
	}

	@Override
	public Item[] getDrops() {
		return ItemReference.get(drops);
	}

	@Override
	public void setDrops(Item... drops) {
		dataChanged("drops", getDrops(), drops);
		this.drops = ItemReference.get(drops);
		Arrays.sort(this.drops);
	}

	@Override
	protected void load(Map<String, Object> data) {
		super.load(data);
		respawnTime = (Long) data.get("respawnTime");
		timeRemaining = (Long) data.get("timeRemaining");
		if (!isSpawned())
			Dungeongine.Ticker.scheduleTick(new ResourceTick(this), 20);

		List<Long> drops = (List<Long>) data.get("drops");
		this.drops = new ItemReference[drops.size()];
		int i = 0;
		for (Long drop : drops) {
			this.drops[i++] = new ItemReference(Items.get(drop));
		}
	}

	@Override
	protected Map<String, Object> getDefault() {
		Map<String, Object> data = super.getDefault();
		data.put("respawnTime", 600); // 30 seconds
		data.put("timeRemaining", 0); // Already spawned
		data.put("drops", Collections.emptyList());
		return data;
	}

	@Override
	protected void serialize(Map<String, Object> data) {
		super.serialize(data);
		data.put("respawnTime", respawnTime);
		data.put("timeRemaining", timeRemaining);

		List<Long> drops = Lists.newArrayList();
		for (ItemReference drop : this.drops) {
			drops.add(drop.getID());
		}
		data.put("drops", drops);
	}

	private static class ResourceTick implements Runnable {
		private final WeakReference<ResourceImpl> resource;

		public ResourceTick(ResourceImpl resource) {
			this.resource = new WeakReference<>(resource);
		}

		@Override
		public void run() {
			ResourceImpl impl = resource.get();
			if (impl == null)
				return;
			impl.setTimeRemaining(impl.getTimeRemaining() - 20);
			if (!impl.isSpawned())
				Dungeongine.Ticker.scheduleTick(new ResourceTick(impl), Math.min(impl.getTimeRemaining(), 20));
		}
	}
}
