package gay.nns.client.impl.management;

import gay.nns.client.api.core.SupremacyCore;
import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.impl.event.player.EventPreMotion;
import gay.nns.client.impl.feature.render.FeatureRotate;
import net.minecraft.client.Minecraft;

import javax.vecmath.Vector2f;

public class ManagerRotation {

    private Vector2f rotations;
    private boolean rotating;

    public ManagerRotation() {

    }

    public void setRotation(Vector2f rotation) {
        rotations = rotation;
        rotating = true;
    }

    @Subscribe
    public void onMotion(EventPreMotion event) {
        if(rotations != null && rotating) {
            event.setYaw(rotations.x);
            event.setPitch(rotations.y);

            if (SupremacyCore.getSingleton().getFeatureManager().getFeatureFromType(FeatureRotate.class).isEnabled()) {
                if (FeatureRotate.rotateHead) {
                    Minecraft.getMinecraft().thePlayer.rotationYawHead = rotations.x;
                    Minecraft.getMinecraft().thePlayer.rotationPitchHead = rotations.y;
                }

                if (FeatureRotate.rotateBody)
                    Minecraft.getMinecraft().thePlayer.renderYawOffset = rotations.x;
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
