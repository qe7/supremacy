package gay.nns.client.impl.feature.render;

import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.SettingMode;
import gay.nns.client.api.setting.annotations.Serialize;

@FeatureInfo(name = "Animation", description = "Animation", category = FeatureCategory.RENDER)
public class FeatureAnimation extends Feature {

	@Serialize(name = "Blocking_Animation")
	@SettingMode(modes = {"1.7", "Shae", "allah"})
	public static String blockingAnimation = "1.7";

	public FeatureAnimation() {
		super();
	}

}
