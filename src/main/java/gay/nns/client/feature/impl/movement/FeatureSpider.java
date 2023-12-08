package gay.nns.client.feature.impl.movement;

import gay.nns.client.event.api.interfaces.Subscribe;
import gay.nns.client.feature.api.types.Feature;
import gay.nns.client.feature.api.enums.FeatureCategory;
import gay.nns.client.feature.api.interfaces.SerializeFeature;
import gay.nns.client.setting.api.annotations.SerializeSetting;
import gay.nns.client.setting.api.annotations.SettingMode;
import gay.nns.client.event.impl.player.EventPreMotion;
import gay.nns.client.event.impl.render.EventRender2D;

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
            case "Verus": {
                if (mc.thePlayer.isCollidedHorizontally && mc.gameSettings.keyBindForward.isKeyDown() && mc.thePlayer.fallDistance < 1) {
                    mc.thePlayer.motionY = 0.5;
                }
                break;
            }
        }
    }
}
