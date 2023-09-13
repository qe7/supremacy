package gay.nns.client.impl.feature.other;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.interfaces.SerializeFeature;
import gay.nns.client.impl.event.player.EventUpdate;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import org.lwjgl.input.Mouse;

@SerializeFeature(name = "AutoTool", description = "Automatically switches to the best tool for the block you're mining")
public class FeatureAutoTool extends Feature {

	private boolean savedOldSlot = false;
	private boolean hasSwapped = false;

	private int previousSlot = -1;

	public FeatureAutoTool() {
		super();
	}

	@Override
	protected void onEnable() {
		super.onEnable();
		hasSwapped = false;
	}

	@Override
	protected void onDisable() {
		super.onDisable();
		hasSwapped = false;
	}

	@Subscribe
	public void onUpdate(final EventUpdate updateEvent) {
		if (mc.objectMouseOver != null && mc.objectMouseOver.getBlockPos() != null && mc.theWorld.getBlockState(mc.objectMouseOver.getBlockPos()) != null && Mouse.isButtonDown(0)) {
			Block block = mc.theWorld.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock();
			if (!savedOldSlot) {
				this.previousSlot = mc.thePlayer.inventory.currentItem;
				this.savedOldSlot = true;
			}
			if (this.getBestTool(block) != -1 && mc.objectMouseOver.entityHit == null && block != null && mc.playerController.getIsHittingBlock()) {
				mc.thePlayer.inventory.currentItem = this.getBestTool(block);
				this.hasSwapped = true;
			}
		} else if (this.hasSwapped && this.previousSlot != -1) {
			mc.thePlayer.inventory.currentItem = this.previousSlot;
			this.hasSwapped = false;
			this.savedOldSlot = false;
			this.previousSlot = -1;
		}
	}

	private int getBestTool(Block block) {
		float bestStr = 0.0f;
		int itemToUse = -1;

		for(int i = 0; i < 9; ++i) {
			ItemStack item = mc.thePlayer.inventory.getStackInSlot(i);
			if (item != null && (item.getItem() instanceof ItemSword || item.getItem() instanceof ItemPickaxe || item.getItem() instanceof ItemAxe || item.getItem() instanceof ItemSpade || item.getItem() instanceof ItemShears) && bestStr < item.getStrVsBlock(block)) {
				bestStr = item.getStrVsBlock(block);
				itemToUse = i;
			}
		}
		return itemToUse;
	}

}
