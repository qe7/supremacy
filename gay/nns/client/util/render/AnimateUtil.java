package gay.nns.client.util.render;

public class AnimateUtil {

	private double value;
	private float speed;
	private long lastRenderTime;

	public AnimateUtil(double value, float speed) {
		this.value = value;
		this.speed = speed;
	}

	public void interpolate(double target) {
		long currentTime = System.currentTimeMillis();
		long delta = currentTime - this.lastRenderTime;
		this.lastRenderTime = currentTime;
		this.value = moveTowards(target, value, delta, speed);
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	private double moveTowards(double target, double current, long delta, float speed) {
		if (delta < 1) {
			delta = 1;
		}

		double diff = target - current;
		boolean isIncreasing = target > current;

		current += (diff / 50) * (delta * speed);

		if (isIncreasing) {
			if (current > target) {
				current = target;
			}
		} else {
			if (current < target) {
				current = target;
			}
		}

		return current;
	}
}
