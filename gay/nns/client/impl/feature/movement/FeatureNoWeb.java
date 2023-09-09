package gay.nns.client.impl.feature.movement;

import gay.nns.client.api.setting.annotations.Serialize;
import gay.nns.client.api.setting.annotations.SettingSlider;
import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.impl.event.player.EventUpdate;
import gay.nns.client.util.player.MovementUtil;

@FeatureInfo(name = "NoWeb", description = "Prevents you from getting slowed down by webs", category = FeatureCategory.MOVEMENT)
public class FeatureNoWeb extends Feature {

	@Serialize(name = "Speed")
	@SettingSlider(min = 0.0D, max = .5D, increment = 0.01D)
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
	public void onUpdate(final EventUpdate updateEvent) {
		if (mc.thePlayer.isInWeb) {
			mc.thePlayer.motionY = 0.0D;
			if (mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0)
				MovementUtil.setSpeed(speed);
		}
	}

}
