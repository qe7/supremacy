package gay.nns.client.impl.feature.movement;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.SerializeFeature;
import gay.nns.client.api.setting.annotations.SettingMode;
import gay.nns.client.api.setting.annotations.SerializeSetting;
import gay.nns.client.api.setting.annotations.SettingSlider;
import gay.nns.client.impl.event.player.EventPreMotion;
import gay.nns.client.impl.event.player.EventUpdate;
import gay.nns.client.impl.event.render.EventRender2D;
import gay.nns.client.util.player.UtilMovement;
import gay.nns.client.util.player.UtilPlayer;
import org.lwjgl.input.Keyboard;

@SerializeFeature(name = "Flight", description = "Allows you to fly.", category = FeatureCategory.MOVEMENT)
public class FeatureFlight extends Feature {

    @SerializeSetting(name = "Mode")
    @SettingMode(modes = {"Vanilla", "Creative", "LJ"})
    public String mode = "Vanilla";

    @SerializeSetting(name = "Speed")
    @SettingSlider(min = 0.0D, max = 1.0D, increment = 0.01D)
    public double speed = 0.3D;

    private boolean savedFlyingCapabilityState;
    private int stage;
    private double x;
    private double y;
    private double z;
    private boolean damaged;
    private int ticks;

    public FeatureFlight() {
        super();
    }

    @Override
    protected void onEnable() {
        damaged = false;
        stage = 0;
        ticks = 0;
        x = mc.thePlayer.posX;
        y = mc.thePlayer.posY;
        z = mc.thePlayer.posZ;
        super.onEnable();
        mc.timer.timerSpeed = 1;

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
            case "vanilla" -> {
                mc.thePlayer.motionY = 0.0D;
                if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
                    mc.thePlayer.motionY += (speed / 2);
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
                    mc.thePlayer.motionY -= (speed / 2);
                if (mc.thePlayer.moveForward != 0.F || mc.thePlayer.moveStrafing != 0.F)
                    UtilMovement.setSpeed(speed);
                else
                    UtilMovement.setSpeed(0.0D);
            }
            case "creative" -> {
                mc.thePlayer.capabilities.allowFlying = true;
                mc.thePlayer.capabilities.isFlying = true;
            }
        }
    }

    @Subscribe
    public void eventPreMotion(EventPreMotion event) {
        switch (mode.toLowerCase()) {
            case "lj" -> {

                if (mc.thePlayer.onGround && !UtilPlayer.isOverVoid()) {
                    stage++;
                    if (stage <= 4)
                        mc.thePlayer.jump();
                    if (stage > 5 && damaged)
                        toggle();

                }

                if (stage <= 4) {
                    event.setGround(false);
                    mc.thePlayer.motionZ = 0;
                    mc.thePlayer.motionX = 0;
                }

                if(mc.thePlayer.hurtTime > 0) {
                    damaged = true;
                    ticks++;
                    if(ticks < 2)
                        mc.thePlayer.motionY = 0.41999998688698;
                    UtilMovement.setSpeed(0.43 + Math.random() / 100F);
                    speed -= 0.01;
                }

                if(damaged) {
                    mc.thePlayer.motionY = 0.42;
                }
            }
        }
    }
}
