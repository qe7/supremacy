package gay.nns.client.impl.feature.movement;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.CheckBox;
import gay.nns.client.api.setting.annotations.Mode;
import gay.nns.client.api.setting.annotations.Serialize;
import gay.nns.client.api.setting.annotations.Slider;
import gay.nns.client.impl.event.player.PreMotionEvent;
import gay.nns.client.impl.event.player.UpdateEvent;
import gay.nns.client.impl.event.render.Render2DEvent;
import gay.nns.client.util.player.MovementUtil;
import net.minecraft.potion.Potion;
import org.lwjgl.input.Keyboard;

@FeatureInfo(name = "Speed", description = "Speeds you up.", category = FeatureCategory.MOVEMENT)
public class FeatureSpeed extends AbstractFeature {

	@Serialize(name = "Mode")
	@Mode(modes = {"Vanilla", "Vanilla-Hop", "Hypixel-Hop", "NCP", "NCP-Hop"})
	public String mode = "Vanilla";

	@Serialize(name = "Speed")
	@Slider(min = 0.0D, max = 1.0D, increment = 0.01D)
	public double speed = 0.3D;

	@Serialize(name = "Water_Check")
	@CheckBox
	public boolean waterCheck = true;

	private int count = 0;
	private double moveSpeed, lastDist;

	public FeatureSpeed() {
		super();
	}

	@Override
	protected void onEnable() {
		super.onEnable();

		count = 0;
		lastDist = 0;
		moveSpeed = 0;
	}

	@Override
	protected void onDisable() {
		super.onDisable();

		mc.timer.timerSpeed = 1.F;

		if (mode.equalsIgnoreCase("hypixel")) {
			mc.gameSettings.keyBindJump.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode());
		}
	}

	@Subscribe
	public void onRender(final Render2DEvent render2DEvent) {
		this.setSuffix(mode);
	}

	@Subscribe
	public void onUpdate(final UpdateEvent updateEvent) {
		final boolean tick = mc.thePlayer.ticksExisted % 2 == 0;
		lastDist = Math.sqrt(((mc.thePlayer.posX - mc.thePlayer.prevPosX) * (mc.thePlayer.posX - mc.thePlayer.prevPosX)) + ((mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * (mc.thePlayer.posZ - mc.thePlayer.prevPosZ)));
		if (lastDist > 5) lastDist = 0.0D;
	}

	@Subscribe
	public void onMotion(final PreMotionEvent motionEvent) {
		if (mc.theWorld == null) return;
		if (mc.thePlayer == null) return;
		if (mc.thePlayer.isInWater() && waterCheck) return;

		switch (mode.toLowerCase()) {
			case "vanilla" -> {
				if (mc.thePlayer.moveForward != 0.f || mc.thePlayer.moveStrafing != 0.f) MovementUtil.setSpeed(speed);
			}
			case "vanilla-hop" -> {
				if (mc.thePlayer.moveForward != 0.f || mc.thePlayer.moveStrafing != 0.f) {
					MovementUtil.setSpeed(speed);
					if (mc.thePlayer.onGround) mc.thePlayer.jump();
				}
			}
			case "hypixel-hop" -> {
				// jumps with friction, me love this, we need this. - shae
				mc.gameSettings.keyBindJump.pressed = mc.thePlayer.onGround;

				if (mc.thePlayer.onGround) {
					MovementUtil.setSpeed(MovementUtil.getBaseMoveSpeed() + 0.02f);
				}
			}
			case "ncp" -> {
				switch (count) {
					case 0 -> {
						++count;
						lastDist = 0.0D;
					}
					case 2 -> {
						double motionY = 0.4025;
						if ((mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F) && mc.thePlayer.onGround) {
							if (mc.thePlayer.isPotionActive(Potion.jump))
								motionY += ((mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
							motionEvent.setY(mc.thePlayer.motionY = motionY);
							moveSpeed *= 2;
						}
					}
					case 3 -> moveSpeed = lastDist - (0.7 * (lastDist - MovementUtil.getBaseMoveSpeed()));
					default -> {
						if ((!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, mc.thePlayer.motionY, 0.0D)).isEmpty() || mc.thePlayer.isCollidedVertically) && count > 0) {
							count = mc.thePlayer.moveForward == 0.0F && mc.thePlayer.moveStrafing == 0.0F ? 0 : 1;
						}
						moveSpeed = lastDist - lastDist / 159.0D;
					}
				}
				moveSpeed = Math.max(moveSpeed, MovementUtil.getBaseMoveSpeed());
				MovementUtil.setSpeed(moveSpeed);
				++count;
			}
			case "ncp-hop" -> {
				if (mc.thePlayer.moveForward != 0.f || mc.thePlayer.moveStrafing != 0.f) {
					if (mc.thePlayer.onGround) {
						mc.thePlayer.jump();
						MovementUtil.setSpeed(MovementUtil.getBaseMoveSpeed() + 0.2f);
						mc.timer.timerSpeed = 1.5f;
					} else if (mc.thePlayer.fallDistance > 0.25f) {
						mc.thePlayer.motionY = -63d;
						mc.thePlayer.moveStrafing *= 2f;
						MovementUtil.setSpeed(MovementUtil.getBaseMoveSpeed() * 0.4f);
						mc.timer.timerSpeed = 0.9f;
					} else {
						MovementUtil.setSpeed(MovementUtil.getBaseMoveSpeed());
						mc.timer.timerSpeed = 1.f;
					}
				}
			}
		}
	}

}
