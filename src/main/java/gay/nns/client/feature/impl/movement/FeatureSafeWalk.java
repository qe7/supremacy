package gay.nns.client.feature.impl.movement;

import gay.nns.client.feature.api.types.Feature;
import gay.nns.client.feature.api.enums.FeatureCategory;
import gay.nns.client.feature.api.interfaces.SerializeFeature;

@SerializeFeature(name = "SafeWalk", description = "Prevents you from falling off blocks", category = FeatureCategory.MOVEMENT)
public class FeatureSafeWalk extends Feature {

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
