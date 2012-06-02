package dungeongine.api.item;

public interface Craftable extends Item {
	Item[] getReagents();
	void setReagents(Item... reagents);
}
