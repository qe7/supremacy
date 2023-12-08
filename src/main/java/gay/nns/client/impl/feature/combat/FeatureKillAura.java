package gay.nns.client.impl.feature.combat;

import gay.nns.client.api.core.SupremacyCore;
import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.SerializeFeature;
import gay.nns.client.api.setting.annotations.SerializeSetting;
import gay.nns.client.api.setting.annotations.SettingBoolean;
import gay.nns.client.api.setting.annotations.SettingMode;
import gay.nns.client.api.setting.annotations.SettingSlider;
import gay.nns.client.impl.event.packet.EventPacketSend;
import gay.nns.client.impl.event.player.EventPostMotion;
import gay.nns.client.impl.event.player.EventPreMotion;
import gay.nns.client.impl.event.player.EventSlowdown;
import gay.nns.client.impl.event.render.EventRender2D;
import gay.nns.client.impl.event.render.EventRenderItem;
import gay.nns.client.util.math.UtilMath;
import gay.nns.client.util.math.UtilTimer;
import gay.nns.client.util.player.UtilRotation;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector2f;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@SerializeFeature(name = "KillAura", description = "Automatically attacks entities around you.", category = FeatureCategory.COMBAT)
public class FeatureKillAura extends Feature {

    @SerializeSetting(name = "Auto_Block")
    @SettingMode(modes = {"None", "Fake", "Vanilla", "BlocksMC", "NCP"})
    public String autoBlock = "None";

    @SerializeSetting(name = "Attack_Range")
    @SettingSlider(min = 1, max = 6, increment = 0.1f)
    public double attackRange = 3.f;

    @SerializeSetting(name = "Max_CPS")
    @SettingSlider(min = 1, max = 20, increment = 1)
    public double maxCPS = 12;

    @SerializeSetting(name = "Min_CPS")
    @SettingSlider(min = 1, max = 20, increment = 1)
    public double minCPS = 8;

    @SerializeSetting(name = "Rotation_Speed")
    @SettingSlider(min = 0, max = 100, increment = 1)
    public double rotationSpeed = 17;
    @SerializeSetting(name = "Keep_Sprint")
    @SettingBoolean()
    public static boolean keepSprint = false;

    private final UtilTimer timer = new UtilTimer();
    private final ArrayList<Packet> packets = new ArrayList<>();

    public boolean afterAttack;
    private List<Entity> entities;
    public static Entity mcTarget;
    private boolean isBlocking = false;
    private int hitTicks;
    private int ticks = 0;

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
        this.afterAttack = false;

        entities = new ArrayList<>(mc.theWorld.getLoadedEntityList());
        entities.sort(Comparator.comparingDouble(e -> e.getDistanceToEntity(mc.thePlayer)));
        entities.removeIf(e -> e == mc.thePlayer || !(e instanceof EntityPlayer || e instanceof EntityLiving) || e.getDistanceToEntity(mc.thePlayer) > attackRange || e.isDead);

        if (!entities.isEmpty() && entities.get(0).getDistanceToEntity(mc.thePlayer) < attackRange) mcTarget = entities.get(0);
        else mcTarget = mc.thePlayer;

        if (mcTarget != mc.thePlayer && mc.thePlayer.getDistanceToEntity(mcTarget) < attackRange && !mcTarget.isDead && !mcTarget.isInvisibleToPlayer(mc.thePlayer) && !mc.thePlayer.isInvisible()) {
            Vector2f rotations = UtilRotation.getRotations(mcTarget);
            Vector2f smoothRotations = UtilRotation.getSmoothRotations(mc.thePlayer.getPreviousRotation(), rotations, rotationSpeed);
            smoothRotations = UtilRotation.applySanity(smoothRotations);
            smoothRotations = UtilRotation.applyGCD(smoothRotations);
            SupremacyCore.getSingleton().getRotationManager().setRotation(smoothRotations);

            if (this.canBlock()) this.pre();

            if (timer.hasTimeElapsed(1000L / UtilMath.getRandom((int) minCPS, (int) maxCPS))) {
                mc.thePlayer.swingItem();
                mc.playerController.attackEntity(mc.thePlayer, mcTarget);
                if (this.canBlock()) this.postAttack();
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
    }


    @Subscribe
    public void onRenderItem(EventRenderItem event) {
        if (!autoBlock.equals("none")) {
            if (mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                if (mc.thePlayer.isSwingInProgress) {
                    event.setUseItem(true);
                    event.setEnumAction(EnumAction.BLOCK);
                }
            }
        }
    }


    @Subscribe
    public void onPostMotionEvent(EventPostMotion event) {
        if (mc.thePlayer != null && this.canBlock()) this.post();
    }

    public void pre() {
        switch (autoBlock) {
            case "NCP":{
                mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                break;
            }
            case "Vanilla": {
                if (this.hitTicks != 0) {
                    mc.playerController.interactWithEntitySendPacket(mc.thePlayer, mcTarget);
                    mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                }
                break;
            }

            case "BlocksMC": {
                if (!this.isBlocking && mc.thePlayer.isSwingInProgress && Math.random() > 0.1 || this.hitTicks == 1 && mc.thePlayer.isSwingInProgress && Math.random() > 0.1) {
                    mc.playerController.interactWithEntitySendPacket(mc.thePlayer, mcTarget);
                    mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                    this.isBlocking = true;
                }
                break;
            }
        }
    }


    public void postAttack() {

            }





    public void post() {
        switch (autoBlock) {
            case "NCP": {
                mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                break;
            }
        }
    }

    private boolean canBlock() {
        return mc.thePlayer.inventory.getCurrentItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && mcTarget != null && mcTarget != mc.thePlayer;
    }
}



