package gay.nns.client.impl.feature.combat;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.SerializeFeature;
import gay.nns.client.api.setting.annotations.SettingBoolean;
import gay.nns.client.api.setting.annotations.SettingMode;
import gay.nns.client.api.setting.annotations.SerializeSetting;
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

@SerializeFeature(name = "Velocity", category = FeatureCategory.COMBAT, description = "Manipulates velocity")
public class FeatureVelocity extends Feature {

    @SerializeSetting(name = "Mode")
    @SettingMode(modes = {"Standard"})
    public String mode = "Standard";

    @SerializeSetting(name = "Horizontal")
    @SettingSlider(min = -100, max = 100, increment = 1)
    public double horizontal = 0;

    @SerializeSetting(name = "Vertical")
    @SettingSlider(min = -100, max = 100, increment = 1)
    public double vertical = 0;

    @SerializeSetting(name = "Water_Check")
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
                if (event.getPacket() instanceof S12PacketEntityVelocity) {
                    S12PacketEntityVelocity s12 = (S12PacketEntityVelocity) event.getPacket();

                    if (s12.getEntityID() == mc.thePlayer.getEntityId()) {
                        if (horizontal == 0) {
                            event.setCancelled(true);

                            if (vertical != 0) {
                                mc.thePlayer.motionY = s12.getMotionY() / 8000.0D;
                            }
                            return;
                        }
                    }
                }


                if (event.getPacket() instanceof S27PacketExplosion) {
                    S27PacketExplosion s27 = (S27PacketExplosion) event.getPacket();

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

        }
    }
}
