package gay.nns.client.feature.impl.render;

import gay.nns.client.feature.api.types.Feature;
import gay.nns.client.feature.api.enums.FeatureCategory;
import gay.nns.client.feature.api.interfaces.SerializeFeature;
import gay.nns.client.setting.api.annotations.SerializeSetting;
import gay.nns.client.setting.api.annotations.SettingBoolean;

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
