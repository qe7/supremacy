package gay.nns.client.impl.feature.render;

import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;

@FeatureInfo(name = "Rotate", description = "Renders your rotations of your player", category = FeatureCategory.RENDER)
public class FeatureRotate extends AbstractFeature {

	public FeatureRotate() {
		super();

		this.toggle();
	}

}
