package gay.nns.client.api.feature.interfaces;

import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.AbstractFeature;

import java.util.Collection;

public interface IFeatureManagerApi {

    Collection<AbstractFeature> getFeatures();
    Collection<AbstractFeature> getEnabledFeatures();
    AbstractFeature getFeatureByName(String name);
    AbstractFeature getFeatureFromType(Class<? extends AbstractFeature> clazz);
    Collection<AbstractFeature> getFeatureFromCategory(FeatureCategory category);

}
