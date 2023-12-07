package gay.nns.client.impl.ui.snakegui;

import gay.nns.client.api.core.SupremacyCore;
import gay.nns.client.impl.feature.other.FeatureSnake;
import gay.nns.client.util.math.UtilMath;
import gay.nns.client.util.math.UtilTimer;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;

public class GuiSnake extends GuiScreen {

	private final UtilTimer utilTimer = new UtilTimer();

	private final int WIDTH_AND_HEIGHT = 290;

	private Direction direction = Direction.RIGHT;

	private int snakeX = 0;
	private int snakeY = 0;

	private int appleX = UtilMath.getRandom(0, WIDTH_AND_HEIGHT / 10);
	private int appleY = UtilMath.getRandom(0, WIDTH_AND_HEIGHT / 10);

	private int snakeLength = 3;
	private int highScore = 0;

	private final int[] snakeXs = new int[100];
	private final int[] snakeYs = new int[100];

	private boolean ended = false;
	private boolean paused = false;

	public GuiSnake() {
		super();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);

		drawRect((float) width / 2 - 150 - 1, (float) height / 2 - 150 - 1, (float) width / 2 + 150 + 1, (float) height / 2 + 150 + 1, new Color(0, 0, 0, 255).getRGB());
		drawRect((float) width / 2 - 150 - 0.5f, (float) height / 2 - 150 - 0.5f, (float) width / 2 + 150 + 0.5f, (float) height / 2 + 150 + 0.5f, new Color(48, 48, 49, 255).getRGB());
		drawRect((float) width / 2 - 150, (float) height / 2 - 150, (float) width / 2 + 150, (float) height / 2 + 150, new Color(25, 25, 24, 255).getRGB());
		drawRect((float) width / 2 - 150 + 0.5f, (float) height / 2 - 150 + 0.5f, (float) width / 2 + 150 - 0.5f, (float) height / 2 + 150 - 0.5f, new Color(19, 19, 18, 255).getRGB());

		drawCenteredString(mc.fontRendererObj, "Score: " + snakeLength, (float) width / 2, 10, -1);

		drawCenteredString(mc.fontRendererObj, "High score: " + highScore, (float) width / 2, 20, -1);

		drawCenteredString(mc.fontRendererObj, "Press ESC to exit", (float) width / 2, 30, -1);

		drawCenteredString(mc.fontRendererObj, "Press WASD or arrow keys to move", (float) width / 2, 40, -1);

		drawCenteredString(mc.fontRendererObj, "Press R to restart", (float) width / 2, 50, -1);

		drawCenteredString(mc.fontRendererObj, "Press P to pause", (float) width / 2, 60, -1);

		if (ended) {
			if (snakeLength >= highScore) {
				drawCenteredString(mc.fontRendererObj, "You reached " + highScore + ", this is now your highest score!", (float) width / 2, (float) height / 2 - 60, -1);
				highScore = snakeLength;
			}
			drawCenteredString(mc.fontRendererObj, "Game Over!", (float) width / 2, (float) height / 2 - 50, -1);
			drawCenteredString(mc.fontRendererObj, "Press R to restart", (float) width / 2, (float) height / 2 - 40, -1);
			return;
		}

		if (paused) {
			drawCenteredString(mc.fontRendererObj, "Paused", (float) width / 2, (float) height / 2 - 50, -1);
			drawCenteredString(mc.fontRendererObj, "Press P to unpause", (float) width / 2, (float) height / 2 - 40, -1);
			return;
		}

		int snakeSpeed = 8;
		if (utilTimer.hasTimeElapsed(1000 / snakeSpeed)) {
			moveSnake(direction);
			utilTimer.reset();
		}

		drawRect((float) width / 2 - 150 + snakeX * 10, (float) height / 2 - 150 + snakeY * 10, (float) width / 2 - 150 + snakeX * 10 + 10, (float) height / 2 - 150 + snakeY * 10 + 10, new Color(0, 255, 0, 255).getRGB());

		drawRect((float) width / 2 - 150 + appleX * 10, (float) height / 2 - 150 + appleY * 10, (float) width / 2 - 150 + appleX * 10 + 10, (float) height / 2 - 150 + appleY * 10 + 10, new Color(255, 0, 0, 255).getRGB());

		for (int i = 0; i < snakeLength; i++) {
			drawRect((float) width / 2 - 150 + snakeXs[i] * 10, (float) height / 2 - 150 + snakeYs[i] * 10, (float) width / 2 - 150 + snakeXs[i] * 10 + 10, (float) height / 2 - 150 + snakeYs[i] * 10 + 10, new Color(0, 255, 0, 255).getRGB());
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);

		if (keyCode == Keyboard.KEY_R) {
			resetGame();
		} else if (keyCode == Keyboard.KEY_P) {
			paused = !paused;
		} else if (keyCode == Keyboard.KEY_UP || keyCode == Keyboard.KEY_W) {
			if (direction != Direction.DOWN) {
				direction = Direction.UP;
			}
		} else if (keyCode == Keyboard.KEY_DOWN || keyCode == Keyboard.KEY_S) {
			if (direction != Direction.UP) {
				direction = Direction.DOWN;
			}
		} else if (keyCode == Keyboard.KEY_RIGHT || keyCode == Keyboard.KEY_D) {
			if (direction != Direction.LEFT) {
				direction = Direction.RIGHT;
			}
		} else if (keyCode == Keyboard.KEY_LEFT || keyCode == Keyboard.KEY_A) {
			if (direction != Direction.RIGHT) {
				direction = Direction.LEFT;
			}
		}
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	private void moveSnake(Direction direction) {
		if (direction == Direction.UP) {
			snakeY--;
		} else if (direction == Direction.DOWN) {
			snakeY++;
		} else if (direction == Direction.LEFT) {
			snakeX--;
		} else if (direction == Direction.RIGHT) {
			snakeX++;
		}

		if (snakeX == appleX && snakeY == appleY) {
			snakeLength++;
			appleX = UtilMath.getRandom(0, WIDTH_AND_HEIGHT / 10);
			appleY = UtilMath.getRandom(0, WIDTH_AND_HEIGHT / 10);
		}

		for (int i = snakeLength - 1; i > 0; i--) {
			snakeXs[i] = snakeXs[i - 1];
			snakeYs[i] = snakeYs[i - 1];
		}

		// check if snake is out of bounds
		if (snakeX < 0 || snakeX > WIDTH_AND_HEIGHT / 10 || snakeY < 0 || snakeY > WIDTH_AND_HEIGHT / 10) {
			ended = true;
			return;
		}

		// check if snake hits itself
		for (int i = 1; i < snakeLength; i++) {
			if (snakeX == snakeXs[i] && snakeY == snakeYs[i]) {
				ended = true;
				return;
			}
		}

		snakeXs[0] = snakeX;
		snakeYs[0] = snakeY;
	}

	private void resetGame() {
		ended = false;

		snakeX = 0;
		snakeY = 0;

		appleX = UtilMath.getRandom(0, WIDTH_AND_HEIGHT / 10);
		appleY = UtilMath.getRandom(0, WIDTH_AND_HEIGHT / 10);

		snakeLength = 3;

		direction = Direction.RIGHT;

		for (int i = 0; i < snakeLength; i++) {
			snakeXs[i] = 0;
			snakeYs[i] = 0;
		}
	}

	private enum Direction {
		UP, DOWN, LEFT, RIGHT
	}

}
