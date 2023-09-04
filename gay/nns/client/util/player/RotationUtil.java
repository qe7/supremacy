package gay.nns.client.util.player;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

import javax.vecmath.Vector2f;

public class RotationUtil {

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
}
