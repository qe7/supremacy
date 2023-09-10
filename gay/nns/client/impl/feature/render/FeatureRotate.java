package gay.nns.client.impl.feature.render;

import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.Serialize;
import gay.nns.client.api.setting.annotations.SettingBoolean;

@FeatureInfo(name = "Rotate", description = "Renders your rotations of your player", category = FeatureCategory.RENDER)
public class FeatureRotate extends Feature {

	@Serialize(name = "Rotate_Head")
	@SettingBoolean()
	public static boolean rotateHead = true;

	@Serialize(name = "Rotate_Body")
	@SettingBoolean()
	public static boolean rotateBody = true;

	public FeatureRotate() {
		super();

		this.toggle();
	}

}
