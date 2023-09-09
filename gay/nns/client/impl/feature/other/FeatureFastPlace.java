package gay.nns.client.impl.feature.other;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.Serialize;
import gay.nns.client.api.setting.annotations.SettingSlider;
import gay.nns.client.impl.event.player.EventUpdate;

@FeatureInfo(name = "FastPlace", description = "Lowers block place cooldown", category = FeatureCategory.OTHER)
public class FeatureFastPlace extends Feature {

    @Serialize(name = "Delay")
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