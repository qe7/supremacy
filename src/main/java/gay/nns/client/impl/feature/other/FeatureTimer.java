package gay.nns.client.impl.feature.other;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.SerializeFeature;
import gay.nns.client.api.setting.annotations.SerializeSetting;
import gay.nns.client.api.setting.annotations.SettingSlider;
import gay.nns.client.impl.event.player.EventPreMotion;

@SerializeFeature(name = "Timer", description = "Changes how fast your game runs", category = FeatureCategory.OTHER)
public class FeatureTimer extends Feature {

    @SerializeSetting(name = "Timer_Speed")
    @SettingSlider(min = 0.1D, max = 10D, increment = 0.1D)
    public double timerSpeed = 1.0D;

    public FeatureTimer() {
        super();
    }

    @Override
    protected void onEnable() {
        super.onEnable();

    }
    @Override
    protected void onDisable() {
        super.onDisable();
        mc.timer.timerSpeed = 1;
    }
    @Subscribe
    public void eventPreMotion(EventPreMotion event) {
        mc.timer.timerSpeed = (float) timerSpeed;
    }
}
