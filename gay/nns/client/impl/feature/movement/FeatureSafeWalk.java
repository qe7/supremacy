package gay.nns.client.impl.feature.movement;

import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;

@FeatureInfo(name = "SafeWalk", description = "Prevents you from falling off blocks", category = FeatureCategory.MOVEMENT)
public class FeatureSafeWalk extends AbstractFeature {

	public FeatureSafeWalk() {
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

}
