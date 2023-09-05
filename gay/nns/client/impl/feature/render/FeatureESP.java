package gay.nns.client.impl.feature.render;


import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.Mode;
import gay.nns.client.api.setting.annotations.Serialize;

@FeatureInfo(name = "ESP", category = FeatureCategory.RENDER, description = "Blah blah blah this isnt done")
public class FeatureESP extends AbstractFeature {

    @Serialize(name = "ESP_Mode")
    @Mode(modes = {"Box"})
    public static String espMode = "Box";

    public FeatureESP() {
        super();
    }

//TODO: @shae ;3
}
