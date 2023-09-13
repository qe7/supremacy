package gay.nns.client.impl.feature.render;

import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.SerializeFeature;
import gay.nns.client.api.setting.annotations.SerializeSetting;
import gay.nns.client.api.setting.annotations.SettingBoolean;

@SerializeFeature(name = "Rotate", description = "Renders your rotations of your player", category = FeatureCategory.RENDER)
public class FeatureRotate extends Feature {

	@SerializeSetting(name = "Rotate_Head")
	@SettingBoolean()
	public static boolean rotateHead = true;

	@SerializeSetting(name = "Rotate_Body")
	@SettingBoolean()
	public static boolean rotateBody = true;

	public FeatureRotate() {
		super();

		this.toggle();
	}

}
