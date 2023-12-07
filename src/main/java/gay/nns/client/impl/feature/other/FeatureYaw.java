package gay.nns.client.impl.feature.other;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.SerializeFeature;
import gay.nns.client.impl.event.player.EventUpdate;

@SerializeFeature(name = "Yaw", description = "Sets the Yaw of the local player.", category = FeatureCategory.OTHER)
public class FeatureYaw extends Feature {

	public FeatureYaw() {
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
		if (mc.thePlayer.prevRotationYaw == mc.thePlayer.rotationYaw) {
			// set the yaw to the closest 45 degree angle
			mc.thePlayer.rotationYaw = (float) (Math.round(mc.thePlayer.rotationYaw / 45.0F) * 45.0F);
		}
	}

}