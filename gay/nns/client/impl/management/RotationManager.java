package gay.nns.client.impl.management;

import gay.nns.client.api.core.Core;
import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.impl.event.player.PreMotionEvent;
import gay.nns.client.impl.feature.render.FeatureRotate;
import net.minecraft.client.Minecraft;

import javax.vecmath.Vector2f;

public class RotationManager {

    private Vector2f rotations;
    private boolean rotating;

    public RotationManager() {

    }

    public void setRotation(Vector2f rotation) {
        rotations = rotation;
        rotating = true;
    }

    @Subscribe
    public void onMotion(PreMotionEvent event) {
        if(rotations != null && rotating) {
            event.setYaw(rotations.x);
            event.setPitch(rotations.y);

            if (Core.getSingleton().getFeatureManager().getFeatureFromType(FeatureRotate.class).isEnabled()) {
                Minecraft.getMinecraft().thePlayer.rotationYawHead = rotations.x;
                Minecraft.getMinecraft().thePlayer.renderYawOffset = rotations.x;
                Minecraft.getMinecraft().thePlayer.rotationPitchHead = rotations.y;
            }

            rotations = null;
            rotating = false;
        }
    }

    public Vector2f getRotations() {
        return rotations;
    }

    public boolean isRotating() {
        return rotating;
    }

}
