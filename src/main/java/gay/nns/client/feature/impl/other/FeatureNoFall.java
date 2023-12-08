package gay.nns.client.feature.impl.other;

import gay.nns.client.event.api.interfaces.Subscribe;
import gay.nns.client.feature.api.types.Feature;
import gay.nns.client.feature.api.enums.FeatureCategory;
import gay.nns.client.feature.api.interfaces.SerializeFeature;
import gay.nns.client.setting.api.annotations.SettingMode;
import gay.nns.client.setting.api.annotations.SerializeSetting;
import gay.nns.client.event.impl.player.EventPreMotion;
import gay.nns.client.event.impl.render.EventRender2D;

@SerializeFeature(name = "NoFall", description = "Prevents fall damage.", category = FeatureCategory.OTHER)
public class FeatureNoFall extends Feature {

    @SerializeSetting(name = "Mode")
    @SettingMode(modes = {"Packet", "Damage", "Verus"})
    public String mode = "Packet";


    public FeatureNoFall() {
        super();
    }

    @Override
    protected void onEnable() {
        super.onEnable();
    }

    @Override
    protected void onDisable() {
        super.onDisable();
    }

    @Subscribe
    public void onRender(final EventRender2D render2DEvent) {
        this.setSuffix(mode);
    }

    @Subscribe
    public void onMotionUpdate(final EventPreMotion motionEvent) {
        switch (mode.toLowerCase()) {
            case "packet": {
                if (mc.thePlayer.fallDistance > 3.0F) {
                    motionEvent.setGround(true);
                    mc.thePlayer.fallDistance = 0.0F;
                }
            }
            case "verus":
            case "damage": {
                if (mc.thePlayer.fallDistance > 3.2F) {
                    motionEvent.setGround(true);
                    mc.thePlayer.fallDistance = 0.0F;
                }
            }
        }
    }
}
