package gay.nns.client.impl.feature.movement;

import gay.nns.client.api.setting.annotations.Serialize;
import gay.nns.client.api.setting.annotations.Slider;
import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.impl.event.player.UpdateEvent;
import gay.nns.client.util.player.MovementUtil;

@FeatureInfo(name = "NoWeb", description = "Prevents you from getting slowed down by webs", category = FeatureCategory.MOVEMENT)
public class FeatureNoWeb extends AbstractFeature {

	@Serialize(name = "Speed")
	@Slider(min = 0.0D, max = .5D, increment = 0.01D)
	public double speed = 0.2D;

	public FeatureNoWeb() {
		super();
	}

	@Override
	protected void onEnable() {
		super.onEnable();
	}

	@Override
	protected void onDisable() {
		super.onDisable();
	}

	@Subscribe
	public void onUpdate(final UpdateEvent updateEvent) {
		if (mc.thePlayer.isInWeb) {
			mc.thePlayer.motionY = 0.0D;
			if (mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0)
				MovementUtil.setSpeed(speed);
		}
	}

}
