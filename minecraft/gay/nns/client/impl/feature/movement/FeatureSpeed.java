package gay.nns.client.impl.feature.movement;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.CheckBox;
import gay.nns.client.api.setting.annotations.Mode;
import gay.nns.client.api.setting.annotations.Serialize;
import gay.nns.client.api.setting.annotations.Slider;
import gay.nns.client.impl.event.player.MotionEvent;
import gay.nns.client.impl.event.player.UpdateEvent;
import gay.nns.client.impl.event.render.Render2DEvent;
import gay.nns.client.util.IMinecraft;
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

		IMinecraft.mc.timer.timerSpeed = 1.F;

		if (mode.equalsIgnoreCase("hypixel")) {
			IMinecraft.mc.gameSettings.keyBindJump.pressed = Keyboard.isKeyDown(IMinecraft.mc.gameSettings.keyBindJump.getKeyCode());
		}
	}

	@Subscribe
	public void onRender(final Render2DEvent render2DEvent) {
		this.setSuffix(mode);
	}

	@Subscribe
	public void onUpdate(final UpdateEvent updateEvent) {
		final boolean tick = IMinecraft.mc.thePlayer.ticksExisted % 2 == 0;
		lastDist = Math.sqrt(((IMinecraft.mc.thePlayer.posX - IMinecraft.mc.thePlayer.prevPosX) * (IMinecraft.mc.thePlayer.posX - IMinecraft.mc.thePlayer.prevPosX)) + ((IMinecraft.mc.thePlayer.posZ - IMinecraft.mc.thePlayer.prevPosZ) * (IMinecraft.mc.thePlayer.posZ - IMinecraft.mc.thePlayer.prevPosZ)));
		if (lastDist > 5) lastDist = 0.0D;
	}

	@Subscribe
	public void onMotion(final MotionEvent motionEvent) {
		if (IMinecraft.mc.theWorld == null) return;
		if (IMinecraft.mc.thePlayer == null) return;
		if (IMinecraft.mc.thePlayer.isInWater() && waterCheck) return;

		switch (mode.toLowerCase()) {
			case "vanilla" -> {
				if (IMinecraft.mc.thePlayer.moveForward != 0.f || IMinecraft.mc.thePlayer.moveStrafing != 0.f) MovementUtil.setSpeed(speed);
			}
			case "vanilla-hop" -> {
				if (IMinecraft.mc.thePlayer.moveForward != 0.f || IMinecraft.mc.thePlayer.moveStrafing != 0.f) {
					MovementUtil.setSpeed(speed);
					if (IMinecraft.mc.thePlayer.onGround) IMinecraft.mc.thePlayer.jump();
				}
			}
			case "hypixel-hop" -> {
				// jumps with friction, me love this, we need this. - shae
				IMinecraft.mc.gameSettings.keyBindJump.pressed = IMinecraft.mc.thePlayer.onGround;

				if (IMinecraft.mc.thePlayer.onGround) {
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
						if ((IMinecraft.mc.thePlayer.moveForward != 0.0F || IMinecraft.mc.thePlayer.moveStrafing != 0.0F) && IMinecraft.mc.thePlayer.onGround) {
							if (IMinecraft.mc.thePlayer.isPotionActive(Potion.jump))
								motionY += ((IMinecraft.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
							motionEvent.setY(IMinecraft.mc.thePlayer.motionY = motionY);
							moveSpeed *= 2;
						}
					}
					case 3 -> moveSpeed = lastDist - (0.7 * (lastDist - MovementUtil.getBaseMoveSpeed()));
					default -> {
						if ((!IMinecraft.mc.theWorld.getCollidingBoundingBoxes(IMinecraft.mc.thePlayer, IMinecraft.mc.thePlayer.getEntityBoundingBox().offset(0.0D, IMinecraft.mc.thePlayer.motionY, 0.0D)).isEmpty() || IMinecraft.mc.thePlayer.isCollidedVertically) && count > 0) {
							count = IMinecraft.mc.thePlayer.moveForward == 0.0F && IMinecraft.mc.thePlayer.moveStrafing == 0.0F ? 0 : 1;
						}
						moveSpeed = lastDist - lastDist / 159.0D;
					}
				}
				moveSpeed = Math.max(moveSpeed, MovementUtil.getBaseMoveSpeed());
				MovementUtil.setSpeed(moveSpeed);
				++count;
			}
			case "ncp-hop" -> {
				if (IMinecraft.mc.thePlayer.moveForward != 0.f || IMinecraft.mc.thePlayer.moveStrafing != 0.f) {
					if (IMinecraft.mc.thePlayer.onGround) {
						IMinecraft.mc.thePlayer.jump();
						MovementUtil.setSpeed(MovementUtil.getBaseMoveSpeed() + 0.2f);
						IMinecraft.mc.timer.timerSpeed = 1.5f;
					} else if (IMinecraft.mc.thePlayer.fallDistance > 0.25f) {
						IMinecraft.mc.thePlayer.motionY = -63d;
						IMinecraft.mc.thePlayer.moveStrafing *= 2f;
						MovementUtil.setSpeed(MovementUtil.getBaseMoveSpeed() * 0.4f);
						IMinecraft.mc.timer.timerSpeed = 0.9f;
					} else {
						MovementUtil.setSpeed(MovementUtil.getBaseMoveSpeed());
						IMinecraft.mc.timer.timerSpeed = 1.f;
					}
				}
			}
		}
	}

}
