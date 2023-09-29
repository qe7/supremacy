package gay.nns.client.impl.feature.movement;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.SerializeFeature;
import gay.nns.client.api.setting.annotations.SerializeSetting;
import gay.nns.client.api.setting.annotations.SettingMode;
import gay.nns.client.impl.event.player.EventPreMotion;
import gay.nns.client.impl.event.render.EventRender2D;

@SerializeFeature(name = "Spider", description = "Casually climb up Walls", category = FeatureCategory.EXPLOIT)
public class FeatureSpider extends Feature {

    @SerializeSetting(name = "Mode")
    @SettingMode(modes = {"Verus"})
    public String mode = "Verus";


    public FeatureSpider() {
        super();
    }

    @Subscribe
    public void onRender2D(final EventRender2D eventRender2D) {
        this.setSuffix(mode);
    }

    @Subscribe
    public void preMotionEvent(EventPreMotion event) {

        switch (mode) {
            case "Verus" -> {
                if (mc.thePlayer.isCollidedHorizontally && mc.gameSettings.keyBindForward.isKeyDown() && mc.thePlayer.fallDistance < 1) {
                    mc.thePlayer.motionY = 0.5;
                }
            }
        }
    }
}
