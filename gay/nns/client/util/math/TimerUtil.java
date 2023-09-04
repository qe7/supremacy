package gay.nns.client.util.math;

public class TimerUtil {

	private long lastMS = 0L;

	public boolean hasTimeElapsed(long time, boolean reset) {
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastMS > time) {
			if (reset) {
				reset();
			}
			return true;
		}
		return false;
	}

	public boolean hasTimeElapsed(long time) {
		return System.currentTimeMillis() - lastMS > time;
	}

	public void reset() {
		lastMS = System.currentTimeMillis();
	}
}
