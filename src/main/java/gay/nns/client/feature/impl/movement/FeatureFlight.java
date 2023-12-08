package gay.nns.client.feature.impl.movement;

import gay.nns.client.event.api.interfaces.Subscribe;
import gay.nns.client.feature.api.types.Feature;
import gay.nns.client.feature.api.enums.FeatureCategory;
import gay.nns.client.feature.api.interfaces.SerializeFeature;
import gay.nns.client.setting.api.annotations.SettingMode;
import gay.nns.client.setting.api.annotations.SerializeSetting;
import gay.nns.client.setting.api.annotations.SettingSlider;
import gay.nns.client.event.impl.player.EventUpdate;
import gay.nns.client.event.impl.render.EventRender2D;
import gay.nns.client.util.player.UtilMovement;
import org.lwjgl.input.Keyboard;

@SerializeFeature(name = "Flight", description = "Allows you to fly.", category = FeatureCategory.MOVEMENT)
public class FeatureFlight extends Feature {

    @SerializeSetting(name = "Mode")
    @SettingMode(modes = {"Vanilla", "Creative", "No-Y"})
    public String mode = "Vanilla";

    @SerializeSetting(name = "Speed")
    @SettingSlider(min = 0.0D, max = 10.0D, increment = 0.01D)
    public double speed = 0.3D;

    private boolean savedFlyingCapabilityState;

    public FeatureFlight() {
        super();
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        savedFlyingCapabilityState = mc.thePlayer.capabilities.allowFlying;
    }

    @Override
    protected void onDisable() {
        super.onDisable();

        mc.thePlayer.capabilities.allowFlying = savedFlyingCapabilityState;
    }

    @Subscribe
    public void onRender(final EventRender2D render2DEvent) {
        this.setSuffix(mode);
    }

    @Subscribe
    public void onUpdate(final EventUpdate updateEvent) {
        switch (mode.toLowerCase()) {
            case "vanilla": {
                mc.thePlayer.motionY = 0.0D;
                if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
                    mc.thePlayer.motionY += (speed / 2);
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
                    mc.thePlayer.motionY -= (speed / 2);
                if (mc.thePlayer.moveForward != 0.F || mc.thePlayer.moveStrafing != 0.F)
                    UtilMovement.setSpeed(speed);
                else
                    UtilMovement.setSpeed(0.0D);
                break;
            }
            case "creative": {
                mc.thePlayer.capabilities.allowFlying = true;
                mc.thePlayer.capabilities.isFlying = true;
                break;
            }

            case "no-y" : {
                mc.thePlayer.motionY = 0;
                if (mc.thePlayer.moveForward != 0.F || mc.thePlayer.moveStrafing != 0.F)
                UtilMovement.setSpeed(speed);
                else
                    UtilMovement.setSpeed(0.0D);
            }
        }
    }
}
