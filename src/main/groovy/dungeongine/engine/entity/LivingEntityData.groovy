package dungeongine.engine.entity

/**
 * EntityData for an entity with health.
 */
class LivingEntityData extends EntityData {
	/**
	 * The current health of the entity.
	 */
	int health
	/**
	 * The maximum health of the entity.
	 */
	int maxHealth

	/**
	 * Increases the entity's current health, taking into account maximum health.
	 */
	void heal(int amount) {
		normalize()
		health = Math.min(health + amount, maxHealth)
	}

	/**
	 * Decreases the entity's current health, preventing negative health values.
	 */
	void hurt(int amount) {
		normalize()
		health = Math.max(0, health - amount)
	}

	/**
	 * True if the entity has a maximum health of 0.
	 */
	boolean isInvincible() {
		normalize()
		maxHealth == 0
	}

	/**
	 * True if the entity is not invincible and has a current health of 0.
	 */
	boolean isDead() {
		normalize()
		!invincible && health == 0
	}

	private void normalize() {
		maxHealth = Math.max(0, maxHealth)
		health = Math.min(Math.max(health, 0), maxHealth)
	}
}
