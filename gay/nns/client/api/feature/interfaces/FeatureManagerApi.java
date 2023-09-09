package gay.nns.client.api.feature.interfaces;

import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.Feature;

import java.util.Collection;

public interface FeatureManagerApi {

    Collection<Feature> getFeatures();
    Collection<Feature> getEnabledFeatures();
    Feature getFeatureByName(String name);
    Feature getFeatureFromType(Class<? extends Feature> clazz);
    Collection<Feature> getFeatureFromCategory(FeatureCategory category);

}
