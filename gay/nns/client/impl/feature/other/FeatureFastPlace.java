package gay.nns.client.impl.feature.other;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.Serialize;
import gay.nns.client.api.setting.annotations.Slider;
import gay.nns.client.impl.event.player.UpdateEvent;

@FeatureInfo(name = "FastPlace", description = "Lowers block place cooldown", category = FeatureCategory.OTHER)
public class FeatureFastPlace extends AbstractFeature {

    @Serialize(name = "Delay")
    @Slider(min = 0, max = 4, increment = 1f)
    public double delay = 3.f;

    public FeatureFastPlace() {
        super();
    }

    @Override
    public void onDisable() {
        mc.rightClickDelayTimer = 4;
        super.onDisable();
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (mc.rightClickDelayTimer > delay)
            mc.rightClickDelayTimer = (int)delay;
    }
}