package gay.nns.client.feature.impl.ghost;

import gay.nns.client.event.api.interfaces.Subscribe;
import gay.nns.client.feature.api.types.Feature;
import gay.nns.client.feature.api.enums.FeatureCategory;
import gay.nns.client.feature.api.interfaces.SerializeFeature;
import gay.nns.client.event.impl.player.EventUpdate;

@SerializeFeature(name = "NoClickDelay", category = FeatureCategory.GHOST, description = "Removes the click delay")
public class FeatureNoClickDelay extends Feature {

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
