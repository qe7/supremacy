package gay.nns.client.impl.feature.combat;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.SettingBoolean;
import gay.nns.client.api.setting.annotations.SettingMode;
import gay.nns.client.api.setting.annotations.Serialize;
import gay.nns.client.api.setting.annotations.SettingSlider;
import gay.nns.client.impl.event.packet.EventPacketReceive;
import gay.nns.client.impl.event.player.EventUpdate;
import gay.nns.client.impl.event.render.EventRender2D;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@FeatureInfo(name = "Velocity", category = FeatureCategory.COMBAT, description = "Manipulates velocity")
public class FeatureVelocity extends AbstractFeature {

    @Serialize(name = "Mode")
    @SettingMode(modes = {"Standard", "Grim"})
    public String mode = "Standard";

    @Serialize(name = "Horizontal")
    @SettingSlider(min = -100, max = 100, increment = 1)
    public double horizontal = 0;

    @Serialize(name = "Vertical")
    @SettingSlider(min = -100, max = 100, increment = 1)
    public double vertical = 0;

    @Serialize(name = "Water_Check")
    @SettingBoolean
    public boolean waterCheck = true;
    private boolean realVelocity, tookVelocity;

    public FeatureVelocity() {
        super();
    }

    @Subscribe
    public void onRender(final EventRender2D render2DEvent) {
        if (mode.equals("Standard")) {
            this.setSuffix((int) horizontal + "% " + (int) vertical + "%");
        } else {
            this.setSuffix(mode);
        }
    }

    @Subscribe
    public void onPacketReceive(final EventPacketReceive event) {
        if (mc.theWorld == null) return;
        if (mc.thePlayer == null) return;
        if (mc.thePlayer.isInWater() && waterCheck) return;

        switch (mode) {
            case "Standard": {
                if (event.getPacket() instanceof S12PacketEntityVelocity s12) {
                    if (s12.getEntityID() == mc.thePlayer.getEntityId()) {
                        if (horizontal == 0 && vertical == 0)
                            event.setCancelled(true);
                        else {
                            s12.motionX = (int) (s12.getMotionX() * (horizontal / 100));
                            s12.motionY = (int) (s12.getMotionY() * (vertical / 100));
                            s12.motionZ = (int) (s12.getMotionZ() * (horizontal / 100));
                        }
                    }
                }
                if (event.getPacket() instanceof S27PacketExplosion s27) {
                    if (horizontal == 0 && vertical == 0)
                        event.setCancelled(true);
                    else {
                        s27.posX = (int) (s27.posX * (horizontal / 100));
                        s27.posY = (int) (s27.posY * (vertical / 100));
                        s27.posZ = (int) (s27.posZ * (horizontal / 100));
                    }
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
        }
    }

    @Subscribe
    public void UpdateEvent(final EventUpdate event) {
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
}
