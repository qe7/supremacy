package gay.nns.client.util.player;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

import javax.vecmath.Vector2f;

public class UtilRotation {

	public static Vector2f applyGCD(Vector2f vec) {
		final Vector2f previousRotation = Minecraft.getMinecraft().thePlayer.getPreviousRotation();
		final float mouseSensitivity = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6F + 0.2F;
		final double multiplier = mouseSensitivity * mouseSensitivity * mouseSensitivity * 8.0F * 0.15D;

		final float yaw = previousRotation.x + (float) (Math.round((vec.x - previousRotation.x) / multiplier) * multiplier);
		final float pitch = MathHelper.clamp_float(previousRotation.y + (float) (Math.round((vec.y - previousRotation.y) / multiplier) * multiplier), -90, 90);

		return new Vector2f(yaw, pitch);
	}

	public static Vector2f applySanity(Vector2f vec) {
		float yaw = vec.x;
		float pitch = MathHelper.clamp_float(vec.y, -90, 90);

		return new Vector2f(yaw, pitch);
	}

	public static Vector2f getRotations(Entity entity) {
		double deltaX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
		double deltaY = entity.posY + (entity.getEyeHeight() * 0.7) - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
		double deltaZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;

		double distance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);

		float yaw = (float) Math.toDegrees(-Math.atan(deltaX / deltaZ));
		float pitch = (float) -Math.toDegrees(Math.atan(deltaY / distance));

		double degrees = Math.toDegrees(Math.atan(deltaZ / deltaX));
		if (deltaX < 0 && deltaZ < 0) {
			yaw = (float) (90 + degrees);
		} else if (deltaX > 0 && deltaZ < 0) {
			yaw = (float) (-90 + degrees);
		}

		return new Vector2f(yaw, pitch);
	}

	public static Vector2f getSmoothRotations(Vector2f lastRotation, Vector2f targetRotation, double speed) {
		float yaw = targetRotation.x;
		float pitch = targetRotation.y;
		float lastYaw = lastRotation.x;
		float lastPitch = lastRotation.y;

		if (speed != 0) {
			float rotationSpeed = (float) speed;

			double deltaYaw = MathHelper.wrapAngleTo180_float(targetRotation.x - lastRotation.x);
			double deltaPitch = pitch - lastPitch;

			double distance = Math.sqrt(deltaYaw * deltaYaw + deltaPitch * deltaPitch);
			double distributionYaw = Math.abs(deltaYaw / distance);
			double distributionPitch = Math.abs(deltaPitch / distance);

			double maxYaw = rotationSpeed * distributionYaw;
			double maxPitch = rotationSpeed * distributionPitch;

			float moveYaw = (float) Math.max(Math.min(deltaYaw, maxYaw), -maxYaw);
			float movePitch = (float) Math.max(Math.min(deltaPitch, maxPitch), -maxPitch);

			yaw = lastYaw + moveYaw;
			pitch = lastPitch + movePitch;

			int numIterations = (int) (Minecraft.getDebugFPS() / 20f + Math.random() * 10);

			for (int i = 1; i <= numIterations; ++i) {
				if (Math.abs(moveYaw) + Math.abs(movePitch) > 1) {
					yaw += (float) ((Math.random() - 0.5) / 1000);
					pitch -= (float) (Math.random() / 200);
				}
			}
		}

		return new Vector2f(yaw, pitch);
	}


}