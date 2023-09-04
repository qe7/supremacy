package gay.nns.client.impl.feature.other;

import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;

@FeatureInfo(name = "SecurityFeatures", description = "Security Features", category = FeatureCategory.OTHER)
public class FeatureSecurityFeatures extends AbstractFeature {

	public FeatureSecurityFeatures() {
		this.toggle();
	}

}
