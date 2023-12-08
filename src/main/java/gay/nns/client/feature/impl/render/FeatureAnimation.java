package gay.nns.client.feature.impl.render;

import gay.nns.client.feature.api.types.Feature;
import gay.nns.client.feature.api.enums.FeatureCategory;
import gay.nns.client.feature.api.interfaces.SerializeFeature;
import gay.nns.client.setting.api.annotations.SettingMode;
import gay.nns.client.setting.api.annotations.SerializeSetting;

@SerializeFeature(name = "Animation", description = "Animation", category = FeatureCategory.RENDER)
public class FeatureAnimation extends Feature {

	@SerializeSetting(name = "Blocking_Animation")
	@SettingMode(modes = {"1.7", "Shae", "allah"})
	public static String blockingAnimation = "1.7";

	public FeatureAnimation() {
		super();
	}

}
