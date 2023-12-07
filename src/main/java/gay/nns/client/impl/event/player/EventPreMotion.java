package gay.nns.client.impl.event.player;

public class EventPreMotion {

    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private boolean onGround;

    public EventPreMotion(double x, double y, double z, float yaw, float pitch, boolean onGround) {
        super();
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public boolean isGround() {
        return onGround;
    }

    public void setGround(boolean ground) {
        this.onGround = ground;
    }

}
