package gay.nns.client.impl.feature.combat;

import gay.nns.client.api.core.Core;
import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.CheckBox;
import gay.nns.client.api.setting.annotations.Mode;
import gay.nns.client.api.setting.annotations.Serialize;
import gay.nns.client.api.setting.annotations.Slider;
import gay.nns.client.impl.event.player.MotionEvent;
import gay.nns.client.util.IMinecraft;
import gay.nns.client.util.chat.ChatUtil;
import gay.nns.client.util.math.MathUtil;
import gay.nns.client.util.math.TimerUtil;
import gay.nns.client.util.player.PlayerUtil;
import gay.nns.client.util.player.RotationUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import javax.vecmath.Vector2f;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@FeatureInfo(name = "KillAura", description = "Automatically attacks entities around you.", category = FeatureCategory.COMBAT)
public class FeatureKillAura extends AbstractFeature {

    @Serialize(name = "Auto_Block")
    @Mode(modes = {"None", "Hypixel"})
    public String autoBlock = "None";
    @Serialize(name = "Attack_Range")
    @Slider(min = 1, max = 6, increment = 0.1f)
    public double attackRange = 3.f;

    @Serialize(name = "Max_CPS")
    @Slider(min = 1, max = 20, increment = 1)
    public double maxCPS = 12;

    @Serialize(name = "Min_CPS")
    @Slider(min = 1, max = 20, increment = 1)
    public double minCPS = 8;

    @Serialize(name = "Rotation_Speed")
    @Slider(min = 0, max = 20, increment = 1)
    public double rotationSpeed = 17;

    @Serialize(name = "Ray_Trace")
    @CheckBox
    public boolean rayTrace = true;

    private final TimerUtil timer = new TimerUtil();

    public FeatureKillAura() {
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
    public void onMotion(final MotionEvent motionEvent) {
        if (IMinecraft.mc.theWorld == null) return;
        if (IMinecraft.mc.thePlayer == null) return;
        if (IMinecraft.mc.thePlayer.isDead) {
            this.toggle();
        }
        if (IMinecraft.mc.thePlayer.getHeldItem() != null && (IMinecraft.mc.thePlayer.getHeldItem().getItem() instanceof ItemBow || IMinecraft.mc.thePlayer.getHeldItem().getItem() instanceof ItemPotion)) {
            return;
        }


        List<Entity> entities = new ArrayList<>(IMinecraft.mc.theWorld.getLoadedEntityList());
        entities.sort(Comparator.comparingDouble(e -> e.getDistanceToEntity(IMinecraft.mc.thePlayer)));
        entities.removeIf(e -> e == IMinecraft.mc.thePlayer || !(e instanceof EntityLiving || e instanceof EntityPlayer) || e.isDead);

        Entity mcTarget;
        if (!entities.isEmpty() && entities.get(0).getDistanceToEntity(IMinecraft.mc.thePlayer) < attackRange)
            mcTarget = entities.get(0);
        else mcTarget = IMinecraft.mc.thePlayer;

        if (mcTarget != IMinecraft.mc.thePlayer && IMinecraft.mc.thePlayer.getDistanceToEntity(mcTarget) < attackRange && !mcTarget.isDead && !mcTarget.isInvisibleToPlayer(IMinecraft.mc.thePlayer) && !IMinecraft.mc.thePlayer.isInvisible()) {

            Vector2f rotations = RotationUtil.getRotations(mcTarget);

            rotations = RotationUtil.applySanity(rotations);

            rotations = RotationUtil.applyGCD(rotations);

            Core.getSingleton().getRotationManager().setRotation(rotations);

            if (mcTarget == mc.thePlayer) {
                mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }

            switch (autoBlock) {
                case "None": {
                    break;
                }

                case "Hypixel": {
                    if (mcTarget != mc.thePlayer) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem % 8 + 3));
                        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                        mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                    }
                    break;
                }
            }

            if (timer.hasTimeElapsed(1000L / MathUtil.getRandom((int) minCPS, (int) maxCPS))) {
                if (rayTrace) {
                    PlayerUtil.sendClick(0, true);
                    PlayerUtil.sendClick(0, false);
                } else {
                    IMinecraft.mc.thePlayer.swingItem();
                    IMinecraft.mc.playerController.attackEntity(IMinecraft.mc.thePlayer, mcTarget);
                }
                timer.reset();
            }
        }
    }
}


