package gay.nns.client.impl.feature.combat;

import gay.nns.client.api.core.Core;
import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.SettingBoolean;
import gay.nns.client.api.setting.annotations.SettingMode;
import gay.nns.client.api.setting.annotations.Serialize;
import gay.nns.client.api.setting.annotations.SettingSlider;
import gay.nns.client.impl.event.packet.EventPacketSend;
import gay.nns.client.impl.event.player.EventPreMotion;
import gay.nns.client.impl.event.render.EventRender2D;
import gay.nns.client.impl.event.render.EventRenderItem;
import gay.nns.client.util.math.MathUtil;
import gay.nns.client.util.math.TimerUtil;
import gay.nns.client.util.player.RotationUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import javax.vecmath.Vector2f;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@FeatureInfo(name = "KillAura", description = "Automatically attacks entities around you.", category = FeatureCategory.COMBAT)
public class FeatureKillAura extends AbstractFeature {

    @Serialize(name = "Keep_Sprint")
    @SettingBoolean()
    public static boolean keepSprint = false;

    @Serialize(name = "Auto_Block")
    @SettingMode(modes = {"None", "Fake", "Hypixel"})
    public String autoBlock = "None";

    @Serialize(name = "Attack_Range")
    @SettingSlider(min = 1, max = 6, increment = 0.1f)
    public double attackRange = 3.f;

    @Serialize(name = "Max_CPS")
    @SettingSlider(min = 1, max = 20, increment = 1)
    public double maxCPS = 12;

    @Serialize(name = "Min_CPS")
    @SettingSlider(min = 1, max = 20, increment = 1)
    public double minCPS = 8;

    @Serialize(name = "Rotation_Speed")
    @SettingSlider(min = 0, max = 20, increment = 1)
    public double rotationSpeed = 17;

    private final TimerUtil timer = new TimerUtil();
    private final ArrayList<Packet> packets = new ArrayList<>();

    public boolean afterAttack;
    List<Entity> entities;
    private Entity mcTarget;
    private boolean isBlocking = false;
    private int hitTicks;
    private boolean attacked;

    public FeatureKillAura() {
        super();
    }

    @Override
    protected void onEnable() {
        super.onEnable();

        packets.clear();
        hitTicks = 0;
        isBlocking = false;
        timer.reset();
    }

    @Override
    protected void onDisable() {
        packets.clear();
        if (autoBlock.equalsIgnoreCase("hypixel") && this.isBlocking) {
            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            this.isBlocking = false;
        }
        super.onDisable();
    }

    @Subscribe
    public void onMotion(final EventPreMotion motionEvent) {
        if (mc.theWorld == null) return;
        if (mc.thePlayer == null) return;
        if (mc.thePlayer.isDead) {
            this.toggle();
            return;
        }
        if (mc.thePlayer.getHeldItem() != null && (mc.thePlayer.getHeldItem().getItem() instanceof ItemBow || mc.thePlayer.getHeldItem().getItem() instanceof ItemPotion)) {
            return;
        }

        this.hitTicks++;

        entities = new ArrayList<>(mc.theWorld.getLoadedEntityList());
        entities.sort(Comparator.comparingDouble(e -> e.getDistanceToEntity(mc.thePlayer)));
        entities.removeIf(e -> e == mc.thePlayer || !(e instanceof EntityPlayer || e instanceof EntityLiving) || e.getDistanceToEntity(mc.thePlayer) > 6.0f || e.isDead);

        if (!entities.isEmpty() && entities.get(0).getDistanceToEntity(mc.thePlayer) < attackRange)
            mcTarget = entities.get(0);
        else mcTarget = mc.thePlayer;

        if (mcTarget != mc.thePlayer && mc.thePlayer.getDistanceToEntity(mcTarget) < attackRange && !mcTarget.isDead && !mcTarget.isInvisibleToPlayer(mc.thePlayer) && !mc.thePlayer.isInvisible()) {

            Vector2f rotations = RotationUtil.getRotations(mcTarget);
            Vector2f smoothRotations = RotationUtil.getSmoothRotations(mc.thePlayer.getPreviousRotation(), rotations, rotationSpeed);

            smoothRotations = RotationUtil.applySanity(smoothRotations);

            smoothRotations = RotationUtil.applyGCD(smoothRotations);

            Core.getSingleton().getRotationManager().setRotation(smoothRotations);

            /*switch (autoBlock) {
                case "Fake" -> {
                }
                case "Hypixel" -> {
                }
            }*/

            if (timer.hasTimeElapsed(1000L / MathUtil.getRandom((int) minCPS, (int) maxCPS))) {
                mc.thePlayer.swingItem();
                mc.playerController.attackEntity(mc.thePlayer, mcTarget);
                this.hitTicks = 0;
                timer.reset();
            }

        } else {
            if (mcTarget == mc.thePlayer && isBlocking) {
                mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                isBlocking = false;
            }
            hitTicks = 0;
        }
    }

    @Subscribe
    public void onRender2D(final EventRender2D render2DEvent) {
        this.setSuffix(String.valueOf(entities.size()));

        FontRenderer fr = mc.fontRendererObj;

        if (!entities.isEmpty() && entities.get(0).getDistanceToEntity(mc.thePlayer) < attackRange)
            mcTarget = entities.get(0);
        else mcTarget = mc.thePlayer;

        if ((mcTarget != null) && mcTarget instanceof EntityPlayer) {
            String string = mcTarget.getName();
            fr.drawStringWithShadow(string, (float) (mc.displayWidth / 4 - fr.getStringWidth(string) / 2), (float) (mc.displayHeight / 4 + 20), new Color(255, 255, 255, 150).getRGB());
            string = "HP: " + Math.round(((EntityPlayer) mcTarget).getHealth());
            fr.drawStringWithShadow(string, (float) (mc.displayWidth / 4 - fr.getStringWidth(string) / 2), (float) (mc.displayHeight / 4 + 30), new Color(255, 255, 255, 150).getRGB());
            string = "HT: " + hitTicks;
            fr.drawStringWithShadow(string, (float) (mc.displayWidth / 4 - fr.getStringWidth(string) / 2), (float) (mc.displayHeight / 4 + 40), new Color(255, 255, 255, 150).getRGB());
        }
    }

    @Subscribe
    public void onRenderItem(EventRenderItem event) {
        switch (autoBlock) {
            case "Fake", "Hypixel" -> {
                if (mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                    if (mc.thePlayer.isSwingInProgress) {
                        event.setUseItem(true);
                        event.setEnumAction(EnumAction.BLOCK);
                    }
                }
            }
        }
    }

    @Subscribe
    public void onPacketSend(EventPacketSend event) {
        final Packet<?> packet = event.getPacket();

        if (mcTarget == null || mc.thePlayer == null || mcTarget == mc.thePlayer) return;

        if (autoBlock.equals("Hypixel")) {
            if (this.hitTicks == 1 && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                if (packet instanceof C03PacketPlayer) {
                    packets.add(packet);
                    event.setCancelled(true);
                }
            } else if (!packets.isEmpty()) {
                mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                this.isBlocking = false;
                for (Packet<?> packet1 : packets) {
                    mc.thePlayer.sendQueue.addToSendQueueNoEvent(packet1);
                }
                packets.clear();
                mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                this.isBlocking = true;
            }
        }
    }

}


