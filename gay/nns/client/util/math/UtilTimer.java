package gay.nns.client.util.math;

public class UtilTimer {

	private long lastMS = 0L;

	public boolean hasTimeElapsed(long milliSeconds, boolean reset) {
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastMS > milliSeconds) {
			if (reset) {
				reset();
			}
			return true;
		}
		return false;
	}

	public boolean hasTimeElapsed(long milliSeconds) {
		return System.currentTimeMillis() - lastMS > milliSeconds;
	}

	public void reset() {
		lastMS = System.currentTimeMillis();
	}

	public long getTime() {
		return System.currentTimeMillis() - lastMS;
	}

}
