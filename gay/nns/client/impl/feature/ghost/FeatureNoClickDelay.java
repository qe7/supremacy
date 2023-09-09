package gay.nns.client.impl.feature.ghost;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.impl.event.player.EventUpdate;

@FeatureInfo(name = "NoClickDelay", category = FeatureCategory.GHOST, description = "Removes the click delay")
public class FeatureNoClickDelay extends AbstractFeature {

	public FeatureNoClickDelay() {
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
	public void onUpdate(final EventUpdate event) {
		if (mc.theWorld == null) return;
		if (mc.thePlayer == null) return;

		if (mc.leftClickCounter != 0)
			mc.leftClickCounter = 0;
	}

}
