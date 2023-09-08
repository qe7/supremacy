package gay.nns.client.util.player;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;

public class MovementUtil {

	public static double getBaseMoveSpeed() {
		double baseSpeed = 0.2875D;
		if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
			baseSpeed *= 1.0D + 0.2D * (double) (Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
		}
		return baseSpeed;
	}

	public static double getBaseJumpHeight() {
		return Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.jump) ?
				0.419999986886978 + 0.1 * (double) (Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) :
				0.419999986886978;
	}

	public static double getBPS() {
		return Math.hypot(Minecraft.getMinecraft().thePlayer.posX - Minecraft.getMinecraft().thePlayer.prevPosX, Minecraft.getMinecraft().thePlayer.posZ - Minecraft.getMinecraft().thePlayer.prevPosZ) * Minecraft.getMinecraft().timer.timerSpeed * 20.0;
	}

	public static void setSpeed(double moveSpeed) {
		setSpeed(moveSpeed, Minecraft.getMinecraft().thePlayer.rotationYaw, Minecraft.getMinecraft().thePlayer.moveStrafing, Minecraft.getMinecraft().thePlayer.moveForward);
	}

	public static void setSpeed(double moveSpeed, float yaw, double strafe, double forward) {
		if (forward != 0.0D) {
			if (strafe > 0.0D) {
				yaw += ((forward > 0.0D) ? -45 : 45);
			} else if (strafe < 0.0D) {
				yaw += ((forward > 0.0D) ? 45 : -45);
			}
			strafe = 0.0D;
			if (forward > 0.0D) {
				forward = 1.0D;
			} else if (forward < 0.0D) {
				forward = -1.0D;
			}
		}
		if (strafe > 0.0D) {
			strafe = 1.0D;
		} else if (strafe < 0.0D) {
			strafe = -1.0D;
		}
		double motionX = Math.cos(Math.toRadians((yaw + 90.0F)));
		double motionZ = Math.sin(Math.toRadians((yaw + 90.0F)));
		Minecraft.getMinecraft().thePlayer.motionX = forward * moveSpeed * motionX + strafe * moveSpeed * motionZ;
		Minecraft.getMinecraft().thePlayer.motionZ = forward * moveSpeed * motionZ - strafe * moveSpeed * motionX;
	}

}
