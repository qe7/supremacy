package gay.nns.client.util.render;

public class UtilPosition {

	private final UtilAnimate posX;
	private final UtilAnimate posY;

	public UtilPosition(double initialPosX, double initialPosY, float speed) {
		this.posX = new UtilAnimate(initialPosX, speed);
		this.posY = new UtilAnimate(initialPosY, speed);
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
