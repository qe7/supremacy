package gay.nns.client.impl.feature.combat;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.CheckBox;
import gay.nns.client.api.setting.annotations.Mode;
import gay.nns.client.api.setting.annotations.Serialize;
import gay.nns.client.api.setting.annotations.Slider;
import gay.nns.client.impl.event.packet.PacketReceiveEvent;
import gay.nns.client.impl.event.player.PreMotionEvent;
import gay.nns.client.impl.event.player.UpdateEvent;
import gay.nns.client.impl.event.render.Render2DEvent;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@FeatureInfo(name = "Velocity", category = FeatureCategory.COMBAT, description = "Manipulates velocity")
public class FeatureVelocity extends AbstractFeature {

    @Serialize(name = "Mode")
    @Mode(modes = {"Standard", "Grim", "Hypixel"})
    public String mode = "Standard";

    @Serialize(name = "Horizontal")
    @Slider(min = -100, max = 100, increment = 1)
    public double horizontal = 0;

    @Serialize(name = "Vertical")
    @Slider(min = -100, max = 100, increment = 1)
    public double vertical = 0;

    @Serialize(name = "Water_Check")
    @CheckBox
    public boolean waterCheck = true;

    @Serialize(name = "Hypixel 0,0 OnGround")
    @CheckBox
    public boolean onGroundCheck = false;
    private boolean reset;
    private boolean realVelocity, tookVelocity;

    public FeatureVelocity() {
        super();
    }

    @Subscribe
    public void onRender(final Render2DEvent render2DEvent) {
        if (mode.equals("Standard")) {
            this.setSuffix((int) horizontal + "% " + (int) vertical + "%");
        } else {
            this.setSuffix(mode);
        }
    }

    @Subscribe
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (mc.theWorld == null) return;
        if (mc.thePlayer == null) return;
        if (mc.thePlayer.isInWater() && waterCheck) return;

        switch (mode) {
            case "Standard": {
                if (event.getPacket() instanceof S12PacketEntityVelocity s12) {
                    if (s12.getEntityID() == mc.thePlayer.getEntityId()) {
                        event.setCancelled(true);
                        s12.motionX *= (int) (horizontal / 100);
                        s12.motionY *= (int) (vertical / 100);
                        s12.motionZ *= (int) (horizontal / 100);
                    }
                }
                if (event.getPacket() instanceof S27PacketExplosion s27) {
                    event.setCancelled(true);
                    s27.posX *= horizontal / 100;
                    s27.posY *= vertical / 100;
                    s27.posZ *= horizontal / 100;
                }
                break;
            }

            case "Grim": {
                if (mc.thePlayer == null || mc.thePlayer.ticksExisted < 50 || mc.thePlayer.isInLava()) {
                    return;
                }

                if (event.getPacket() instanceof S19PacketEntityStatus s19) {

                    if (s19.getEntity(mc.theWorld) != mc.thePlayer || s19.getOpCode() != 2) {
                        return;
                    }

                    this.realVelocity = true;
                }

                if (event.getPacket() instanceof S12PacketEntityVelocity s12) {
                    if (s12.getEntityID() != mc.thePlayer.getEntityId() || !this.realVelocity) {
                        return;
                    }

                    event.setCancelled(true);
                    this.tookVelocity = true;
                    this.realVelocity = false;

                }
            }

            case "Hypixel": { //this should work
                if (!(event.getPacket() instanceof S12PacketEntityVelocity s12)) {
                    return;
                }

                if (s12.getEntityID() != mc.thePlayer.getEntityId()) {
                    return;
                }

                if (mc.thePlayer.onGround && onGroundCheck) { //onGroundCheck is a checkbox u can toggle on/off for 0,0 on ground
                    reset = true;
                    event.setCancelled(true);
                    return;
                }
                event.setCancelled(true);
                mc.thePlayer.motionY = s12.getMotionY() / 8000D;
            }
        }
    }

    @Subscribe
    public void UpdateEvent(final UpdateEvent event) {
        switch (mode) {
            case "Grim": {
                if (!this.tookVelocity) {
                    return;
                }
                mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), EnumFacing.DOWN));
                this.tookVelocity = false;
            }
        }
    }

    @Subscribe
    public void playerMotionEvent(PreMotionEvent event) {
        switch (mode) {
            case "Hypixel": { //this should work
                if (!reset) {
                    return;
                }

                mc.thePlayer.motionX = 0;
                mc.thePlayer.motionY = 0;
                mc.thePlayer.motionZ = 0;
                reset = false;
            }
        }
    }
}
