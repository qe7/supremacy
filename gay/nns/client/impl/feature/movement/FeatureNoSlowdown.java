package gay.nns.client.impl.feature.movement;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.Mode;
import gay.nns.client.api.setting.annotations.Serialize;
import gay.nns.client.impl.event.packet.PacketSendEvent;
import gay.nns.client.impl.event.player.PostMotionEvent;
import gay.nns.client.impl.event.player.PreMotionEvent;
import gay.nns.client.impl.event.player.SlowDownEvent;
import gay.nns.client.impl.event.render.Render2DEvent;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.apache.commons.lang3.RandomUtils;

@FeatureInfo(name = "NoSlowdown", description = "Prevents you from slowing down when you're blocking.", category = FeatureCategory.MOVEMENT)
public class FeatureNoSlowdown extends AbstractFeature {

    @Serialize(name = "Mode")
    @Mode(modes = {"Vanilla", "NCP", "Hypixel"})
    public String mode = "Vanilla";


    public FeatureNoSlowdown() {
        super();
    }

    @Override
    protected void onEnable() {
        super.onEnable();
    }

    @Override
    protected void onDisable() {
        super.onDisable();
    }

    @Subscribe
    public void onRender(final Render2DEvent render2DEvent) {
        this.setSuffix(mode);
    }

    @Subscribe
    public void onSlowdown(final SlowDownEvent slowDownEvent) {
        slowDownEvent.setCancelled(true);
    }

    @Subscribe
    public void onMotion(final PreMotionEvent motionEvent) {
        if (mc.thePlayer.getHeldItem() != null && !(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword))
            return;
        if (!mc.thePlayer.isBlocking()) return;

        switch (mode.toLowerCase()) {
            case "ncp" -> {
                mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(RandomUtils.nextDouble(Double.MIN_VALUE, Double.MAX_VALUE), RandomUtils.nextDouble(Double.MIN_VALUE, Double.MAX_VALUE), RandomUtils.nextDouble(Double.MIN_VALUE, Double.MAX_VALUE)), EnumFacing.DOWN));
            }
            case "hypixel" -> {
                if (mc.thePlayer.isUsingItem() && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem % 8 + 3));
                    mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                    mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                }
            }
        }
    }

    @Subscribe
    public void onPostMotion(final PostMotionEvent motionEvent) {
        if (mc.thePlayer.getHeldItem() != null && !(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword))
            return;
        if (!mc.thePlayer.isBlocking()) return;

        switch (mode.toLowerCase()) {
            case "ncp" -> {
                mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
            }
        }
    }


    @Subscribe
    public void packetSend(final PacketSendEvent event) {
        switch (mode.toLowerCase()) {
            case "hypixel" -> {
                if (mc.thePlayer.getHeldItem() != null && (mc.thePlayer.getHeldItem().getItem() instanceof ItemFood || mc.thePlayer.getHeldItem().getItem() instanceof ItemPotion)) {
                    if (event.getPacket() instanceof C08PacketPlayerBlockPlacement c08) {
                        if (c08.getPlacedBlockDirection() == 255) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(mc.thePlayer).down(), EnumFacing.DOWN.getIndex(), null, 0.0F, 1.0F, 0.0F));
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}


