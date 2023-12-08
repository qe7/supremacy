package gay.nns.client.feature.impl.other;

import gay.nns.client.event.api.interfaces.Subscribe;
import gay.nns.client.feature.api.types.Feature;
import gay.nns.client.feature.api.enums.FeatureCategory;
import gay.nns.client.feature.api.interfaces.SerializeFeature;
import gay.nns.client.setting.api.annotations.SerializeSetting;
import gay.nns.client.setting.api.annotations.SettingSlider;
import gay.nns.client.event.impl.player.EventUpdate;

@SerializeFeature(name = "FastPlace", description = "Lowers block place cooldown", category = FeatureCategory.OTHER)
public class FeatureFastPlace extends Feature {

    @SerializeSetting(name = "Delay")
    @SettingSlider(min = 0, max = 4, increment = 1)
    public double delay = 3;

    public FeatureFastPlace() {
        super();
    }

    @Override
    public void onDisable() {
        mc.rightClickDelayTimer = 4;
        super.onDisable();
    }

    @Subscribe
    public void onUpdate(EventUpdate event) {
        if (mc.rightClickDelayTimer > delay)
            mc.rightClickDelayTimer = (int)delay;
    }
}