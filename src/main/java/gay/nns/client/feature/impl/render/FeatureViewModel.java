package gay.nns.client.feature.impl.render;

import gay.nns.client.feature.api.enums.FeatureCategory;
import gay.nns.client.feature.api.interfaces.SerializeFeature;
import gay.nns.client.setting.api.annotations.SerializeSetting;
import gay.nns.client.setting.api.annotations.SettingSlider;
import gay.nns.client.feature.api.types.Feature;

@SerializeFeature(name = "ViewModel", category = FeatureCategory.RENDER)
public class FeatureViewModel extends Feature {

	@SerializeSetting(name = "View_Model_X")
	@SettingSlider(min = -2, max = 2, increment = 0.1f)
	public static double x = 0.0f;

	@SerializeSetting(name = "View_Model_Y")
	@SettingSlider(min = -2, max = 2, increment = 0.1f)
	public static double y = 0.0f;

	@SerializeSetting(name = "View_Model_Z")
	@SettingSlider(min = -2, max = 2, increment = 0.1f)
	public static double z = 0.0f;

	public FeatureViewModel() {
		super();
	}

}
