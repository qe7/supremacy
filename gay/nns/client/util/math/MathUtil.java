package gay.nns.client.util.math;

public class MathUtil {

	public static int getRandom(int min, int max) {
		return (int) ((Math.random() * (max - min)) + min);
	}

	public static double getRandom(double min, double max) {
		return (Math.random() * (max - min)) + min;
	}

	public static float getRandom(float min, float max) {
		return (float) ((Math.random() * (max - min)) + min);
	}

}
