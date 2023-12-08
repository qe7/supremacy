package gay.nns.client.feature.impl.other;

import gay.nns.client.feature.api.types.Feature;
import gay.nns.client.feature.api.enums.FeatureCategory;
import gay.nns.client.feature.api.interfaces.SerializeFeature;

@SerializeFeature(name = "SecurityFeatures", description = "Security Features", category = FeatureCategory.OTHER)
public class FeatureSecurityFeatures extends Feature {

	public FeatureSecurityFeatures() {
		this.toggle();
	}

}
