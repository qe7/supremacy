package gay.nns.client.impl.feature.movement;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.SettingMode;
import gay.nns.client.api.setting.annotations.Serialize;
import gay.nns.client.api.setting.annotations.SettingSlider;
import gay.nns.client.impl.event.player.EventUpdate;
import gay.nns.client.impl.event.render.EventRender2D;
import gay.nns.client.util.player.UtilMovement;
import org.lwjgl.input.Keyboard;

@FeatureInfo(name = "Flight", description = "Allows you to fly.", category = FeatureCategory.MOVEMENT)
public class FeatureFlight extends Feature {

	@Serialize(name = "Mode")
	@SettingMode(modes = {"Vanilla", "Creative"})
	public String mode = "Vanilla";

	@Serialize(name = "Speed")
	@SettingSlider(min = 0.0D, max = 1.0D, increment = 0.01D)
	public double speed = 0.3D;

	private boolean savedFlyingCapabilityState;

	public FeatureFlight() {
		super();
	}

	@Override
	protected void onEnable() {
		super.onEnable();

		savedFlyingCapabilityState = mc.thePlayer.capabilities.allowFlying;
	}

	@Override
	protected void onDisable() {
		super.onDisable();

		mc.thePlayer.capabilities.allowFlying = savedFlyingCapabilityState;
	}

	@Subscribe
	public void onRender(final EventRender2D render2DEvent) {
		this.setSuffix(mode);
	}

	@Subscribe
	public void onUpdate(final EventUpdate updateEvent) {
		switch (mode.toLowerCase()) {
			case "vanilla" -> {
				mc.thePlayer.motionY = 0.0D;
				if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
					mc.thePlayer.motionY += (speed / 2);
				if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
					mc.thePlayer.motionY -= (speed / 2);
				if (mc.thePlayer.moveForward != 0.F || mc.thePlayer.moveStrafing != 0.F)
					UtilMovement.setSpeed(speed);
				else
					UtilMovement.setSpeed(0.0D);
			}
			case "creative" -> {
				mc.thePlayer.capabilities.allowFlying = true;
				mc.thePlayer.capabilities.isFlying = true;
			}
		}
	}

}
