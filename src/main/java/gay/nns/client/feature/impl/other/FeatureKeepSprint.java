package gay.nns.client.feature.impl.other;

import gay.nns.client.feature.api.types.Feature;
import gay.nns.client.feature.api.enums.FeatureCategory;
import gay.nns.client.feature.api.interfaces.SerializeFeature;

@SerializeFeature(name = "KeepSprint", description = "Automatically keeps you sprinting.", category = FeatureCategory.OTHER)
public class FeatureKeepSprint extends Feature {
}
