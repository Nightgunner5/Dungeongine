package dungeongine.engine.entity

class LivingEntityData extends EntityData {
	int health
	int maxHealth

	void heal(int amount) {
		normalize()
		health = Math.min(health + amount, maxHealth)
	}

	void hurt(int amount) {
		normalize()
		health = Math.max(0, health - amount)
	}

	boolean isInvincible() {
		normalize()
		maxHealth == 0
	}

	boolean isDead() {
		normalize()
		!invincible && health == 0
	}

	void normalize() {
		maxHealth = Math.max(0, maxHealth)
		health = Math.min(Math.max(health, 0), maxHealth)
	}
}
