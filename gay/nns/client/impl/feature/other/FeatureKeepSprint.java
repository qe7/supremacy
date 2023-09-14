package gay.nns.client.impl.feature.other;

import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.SerializeFeature;

@SerializeFeature(name = "KeepSprint", description = "Automatically keeps you sprinting.", category = FeatureCategory.OTHER)
public class FeatureKeepSprint extends Feature {
}
