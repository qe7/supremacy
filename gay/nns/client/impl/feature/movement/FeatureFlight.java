package gay.nns.client.impl.feature.movement;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.Mode;
import gay.nns.client.api.setting.annotations.Serialize;
import gay.nns.client.api.setting.annotations.Slider;
import gay.nns.client.impl.event.player.UpdateEvent;
import gay.nns.client.impl.event.render.Render2DEvent;
import gay.nns.client.util.IMinecraft;
import gay.nns.client.util.player.MovementUtil;
import org.lwjgl.input.Keyboard;

@FeatureInfo(name = "Flight", description = "Allows you to fly.", category = FeatureCategory.MOVEMENT)
public class FeatureFlight extends AbstractFeature {

	@Serialize(name = "Mode")
	@Mode(modes = {"Vanilla", "Creative"})
	public String mode = "Vanilla";

	@Serialize(name = "Speed")
	@Slider(min = 0.0D, max = 1.0D, increment = 0.01D)
	public double speed = 0.3D;

	private boolean savedFlyingCapabilityState;

	public FeatureFlight() {
		super();
	}

	@Override
	protected void onEnable() {
		super.onEnable();

		savedFlyingCapabilityState = IMinecraft.mc.thePlayer.capabilities.allowFlying;
	}

	@Override
	protected void onDisable() {
		super.onDisable();

		IMinecraft.mc.thePlayer.capabilities.allowFlying = savedFlyingCapabilityState;
	}

	@Subscribe
	public void onRender(final Render2DEvent render2DEvent) {
		this.setSuffix(mode);
	}

	@Subscribe
	public void onUpdate(final UpdateEvent updateEvent) {
		switch (mode.toLowerCase()) {
			case "vanilla" -> {
				IMinecraft.mc.thePlayer.motionY = 0.0D;
				if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
					IMinecraft.mc.thePlayer.motionY += (speed / 2);
				if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
					IMinecraft.mc.thePlayer.motionY -= (speed / 2);
				if (IMinecraft.mc.thePlayer.moveForward != 0.F || IMinecraft.mc.thePlayer.moveStrafing != 0.F)
					MovementUtil.setSpeed(speed);
				else
					MovementUtil.setSpeed(0.0D);
			}
			case "creative" -> {
				IMinecraft.mc.thePlayer.capabilities.allowFlying = true;
				IMinecraft.mc.thePlayer.capabilities.isFlying = true;
			}
		}
	}

}
