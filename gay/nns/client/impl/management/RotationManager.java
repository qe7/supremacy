package gay.nns.client.impl.management;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.impl.event.player.MotionEvent;

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
    public void onMotion(MotionEvent event) {
        if(rotations != null && rotating) {
            event.setYaw(rotations.x);
            event.setPitch(rotations.y);
            rotations = null;
            rotating = false;
        }
    }

}
