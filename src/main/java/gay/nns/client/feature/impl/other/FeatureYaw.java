package gay.nns.client.feature.impl.other;

import gay.nns.client.event.api.interfaces.Subscribe;
import gay.nns.client.feature.api.types.Feature;
import gay.nns.client.feature.api.enums.FeatureCategory;
import gay.nns.client.feature.api.interfaces.SerializeFeature;
import gay.nns.client.event.impl.player.EventUpdate;

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
