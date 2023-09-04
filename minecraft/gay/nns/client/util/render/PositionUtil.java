package gay.nns.client.util.render;

public class PositionUtil {

	private final AnimateUtil posX;
	private final AnimateUtil posY;

	public PositionUtil(double initialPosX, double initialPosY, float speed) {
		this.posX = new AnimateUtil(initialPosX, speed);
		this.posY = new AnimateUtil(initialPosY, speed);
	}

	public void interpolate(float targetX, float targetY) {
		posX.interpolate(targetX);
		posY.interpolate(targetY);
	}

	public double getX() {
		return posX.getValue();
	}

	public double getY() {
		return posY.getValue();
	}
}
