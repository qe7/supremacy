package gay.nns.client.impl.feature.movement;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.Mode;
import gay.nns.client.api.setting.annotations.Serialize;
import gay.nns.client.impl.event.player.MotionEvent;
import gay.nns.client.impl.event.player.SlowDownEvent;
import gay.nns.client.impl.event.render.Render2DEvent;
import gay.nns.client.util.IMinecraft;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.apache.commons.lang3.RandomUtils;

@FeatureInfo(name = "No_Slowdown", description = "Prevents you from slowing down when you're blocking.", category = FeatureCategory.MOVEMENT)
public class FeatureNoSlowdown extends AbstractFeature {

	@Serialize(name = "Mode")
	@Mode(modes = {"Vanilla", "NCP"})
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
	public void onMotion(final MotionEvent motionEvent) {
		if (IMinecraft.mc.thePlayer.getHeldItem() != null && !(IMinecraft.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)) return;
		if (!IMinecraft.mc.thePlayer.isBlocking()) return;

		switch (mode.toLowerCase()) {
			case "ncp" -> {
				if (motionEvent.isPre()) {
					IMinecraft.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(RandomUtils.nextDouble(Double.MIN_VALUE, Double.MAX_VALUE), RandomUtils.nextDouble(Double.MIN_VALUE, Double.MAX_VALUE), RandomUtils.nextDouble(Double.MIN_VALUE, Double.MAX_VALUE)), EnumFacing.DOWN));
				}
				if (motionEvent.isPost()) {
					IMinecraft.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, IMinecraft.mc.thePlayer.getHeldItem(), 0, 0, 0));
				}
			}
		}
	}

}
