package gay.nns.client.impl.feature.other;

import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;

@FeatureInfo(name = "SecurityFeatures", description = "Security Features", category = FeatureCategory.OTHER)
public class FeatureSecurityFeatures extends Feature {

	public FeatureSecurityFeatures() {
		this.toggle();
	}

}
