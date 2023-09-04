package gay.nns.client.impl.feature.other;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.impl.event.player.UpdateEvent;
import gay.nns.client.util.IMinecraft;

@FeatureInfo(name = "Yaw", description = "Sets the Yaw of the local player.", category = FeatureCategory.OTHER)
public class FeatureYaw extends AbstractFeature {

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
	public void onUpdate(final UpdateEvent updateEvent) {
		if (IMinecraft.mc.thePlayer.rotationYaw == IMinecraft.mc.thePlayer.prevRotationYaw) {
			// set the yaw to the closest 45 degree angle
			IMinecraft.mc.thePlayer.rotationYaw = (float) (Math.round(IMinecraft.mc.thePlayer.rotationYaw / 45.0F) * 45.0F);
		}
	}

}
