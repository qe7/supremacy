package gay.nns.client.feature.impl.other;

import gay.nns.client.event.api.interfaces.Subscribe;
import gay.nns.client.feature.api.types.Feature;
import gay.nns.client.feature.api.enums.FeatureCategory;
import gay.nns.client.feature.api.interfaces.SerializeFeature;
import gay.nns.client.setting.api.annotations.SerializeSetting;
import gay.nns.client.setting.api.annotations.SettingSlider;
import gay.nns.client.event.impl.player.EventPreMotion;
import gay.nns.client.event.impl.render.EventRender2D;

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

    @Subscribe
    public void render2DEvent(EventRender2D event) {
        if (mc.theWorld == null) return;
        if (mc.thePlayer == null) return;
        this.setSuffix(String.valueOf(timerSpeed));
    }
}
