package gay.nns.client.management;

import gay.nns.client.SupremacyCore;
import gay.nns.client.event.api.interfaces.Subscribe;
import gay.nns.client.event.impl.player.EventPreMotion;
import gay.nns.client.feature.impl.render.FeatureRotate;
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
