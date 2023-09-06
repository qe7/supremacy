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
import gay.nns.client.util.math.MathUtil;
import gay.nns.client.util.player.MovementUtil;
import org.lwjgl.input.Keyboard;

@FeatureInfo(name = "Speed", description = "Speeds you up.", category = FeatureCategory.MOVEMENT)
public class FeatureSpeed extends AbstractFeature {

	@Serialize(name = "Mode")
	@Mode(modes = {"Vanilla", "Vanilla-Hop", "Hypixel-Hop", "NCP-Hop"})
	public String mode = "Vanilla";

	@Serialize(name = "Speed")
	@Slider(min = 0.0D, max = 1.0D, increment = 0.01D)
	public double speed = 0.3D;

	@Serialize(name = "Water_Check")
	@CheckBox
	public boolean waterCheck = true;

	private int stage = 0;

	public FeatureSpeed() {
		super();
	}

	@Override
	protected void onEnable() {
		super.onEnable();

		stage = 0;
	}

	@Override
	protected void onDisable() {
		super.onDisable();

		mc.timer.timerSpeed = 1.F;

		stage = 0;

		if (mode.equalsIgnoreCase("hypixel-hop")) {
			mc.gameSettings.keyBindJump.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode());
		}
	}

	@Subscribe
	public void onRender(final Render2DEvent render2DEvent) {
		this.setSuffix(mode);
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
				if (mc.thePlayer.moveForward != 0.f || mc.thePlayer.moveStrafing != 0.f) {
					// jumps with friction, me love this, we need this. - shae
					mc.gameSettings.keyBindJump.pressed = mc.thePlayer.onGround;

					if (mc.thePlayer.onGround) {
						if (mc.thePlayer.hurtTime > 2)
							MovementUtil.setSpeed(MovementUtil.getBaseMoveSpeed() + 0.02f);
						else
							MovementUtil.setSpeed(MovementUtil.getBaseMoveSpeed() - 0.04f);
					}
				}
			}
			case "ncp-hop" -> {
				if (mc.thePlayer.moveForward != 0.f || mc.thePlayer.moveStrafing != 0.f) {
					if (mc.thePlayer.onGround) {
						stage = 0;
						mc.thePlayer.jump();
						if (mc.thePlayer.hurtTime > 2)
							MovementUtil.setSpeed(MovementUtil.getBaseMoveSpeed() + 0.06f);
						else
							MovementUtil.setSpeed(MovementUtil.getBaseMoveSpeed() - 0.04f);
					} else {
						stage++;
						switch (stage) {
							case 1 -> MovementUtil.setSpeed(MovementUtil.getBaseMoveSpeed() + MathUtil.getRandom(0.f, 0.02f));
							case 2 -> MovementUtil.setSpeed(MovementUtil.getBaseMoveSpeed() + MathUtil.getRandom(-0.02f, 0.f));
							case 3 -> MovementUtil.setSpeed(MovementUtil.getBaseMoveSpeed() + MathUtil.getRandom(-0.04f, -0.02f));
						}
						if (stage > 3) {
							MovementUtil.setSpeed(MovementUtil.getBaseMoveSpeed() + MathUtil.getRandom(-0.04f, -0.02f));
						}
					}
				}
			}
		}
	}

}
