package gay.nns.client.feature.impl.ghost;

import gay.nns.client.event.api.interfaces.Subscribe;
import gay.nns.client.feature.api.types.Feature;
import gay.nns.client.feature.api.enums.FeatureCategory;
import gay.nns.client.feature.api.interfaces.SerializeFeature;
import gay.nns.client.setting.api.annotations.SerializeSetting;
import gay.nns.client.setting.api.annotations.SettingBoolean;
import gay.nns.client.setting.api.annotations.SettingSlider;
import gay.nns.client.event.impl.player.EventPreMotion;
import gay.nns.client.util.player.UtilRotation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import org.lwjgl.input.Mouse;

import javax.vecmath.Vector2f;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@SerializeFeature(name = "AimAssist", description = "Automatically aims at the nearest entity", category = FeatureCategory.GHOST)
public class FeatureAimAssist extends Feature {

	@SerializeSetting(name = "Sword_Only")
	@SettingBoolean()
	public boolean swordOnly = false;

	@SerializeSetting(name = "Range")
	@SettingSlider(min = 1, max = 6, increment = 0.1f)
	public double range = 6.0f;

	public FeatureAimAssist() {
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
	public void onMotion(final EventPreMotion preMotionEvent) {
		if (mc.theWorld == null) return;
		if (mc.thePlayer == null) return;
		if (!Mouse.isButtonDown(0)) return;
		if (mc.thePlayer.isDead) return;
		if (mc.thePlayer.isUsingItem()) return;
		if (mc.currentScreen != null) return;
		if (swordOnly && mc.thePlayer.getHeldItem() != null && !(mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)) {
			return;
		}

		List<Entity> entities = new ArrayList<>(mc.theWorld.getLoadedEntityList());
		entities.sort(Comparator.comparingDouble(e -> e.getDistanceToEntity(mc.thePlayer)));
		entities.removeIf(e -> e == mc.thePlayer || !(e instanceof EntityPlayer || e instanceof EntityLiving) || e.getDistanceToEntity(mc.thePlayer) > 6.0f || e.isDead);

		Entity mcTarget;
		if (!entities.isEmpty() && entities.get(0).getDistanceToEntity(mc.thePlayer) < range) mcTarget = entities.get(0);
		else mcTarget = mc.thePlayer;

		if (mcTarget == mc.thePlayer) return;

		if (mcTarget.getDistanceToEntity(mc.thePlayer) <= range) {

			Vector2f rotations = UtilRotation.getRotations(mcTarget);
			Vector2f smoothRotations = UtilRotation.getSmoothRotations(mc.thePlayer.getPreviousRotation(), rotations, 2.5f);

			smoothRotations = UtilRotation.applySanity(smoothRotations);

			smoothRotations = UtilRotation.applyGCD(smoothRotations);

			mc.thePlayer.rotationYaw += (smoothRotations.x - mc.thePlayer.rotationYaw);
		}
	}

}
