package gay.nns.client.impl.feature.ghost;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.impl.event.player.PreMotionEvent;

@FeatureInfo(name = "AimAssist", description = "Automatically aims at the nearest entity", category = FeatureCategory.GHOST)
public class FeatureAimAssist extends AbstractFeature {

	public FeatureAimAssist() {
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
	public void onMotion(final PreMotionEvent preMotionEvent) {

	}

}
