package gay.nns.client.util.player;

import gay.nns.client.impl.feature.other.FeatureInventoryManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.*;

import java.util.ArrayList;

/* A lot of tomfoolery is about to be committed. */
public class InventoryUtil {

	public static boolean isGood(ItemStack i) {
		if (i.getItem() instanceof ItemPotion && !PlayerUtil.isBadPotion(i)) {
			return true;
		}

		String unlocalizedName = i.getUnlocalizedName();
		String[] keywords = {
				"stone", "sword", "pickaxe", "axe", "shovel", "helmet", "chestplate",
				"leggings", "boots", "dirt", "wood", "grass", "craft", "fire", "log",
				"glass", "apple", "spade", "hatchet", "ender"
		};

		for (String keyword : keywords) {
			if (unlocalizedName.contains(keyword)) {
				return true;
			}
		}

		return false;
	}


	public static int getBlockCount() {
		int count = 0;

		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
		ContainerPlayer container = (ContainerPlayer) player.openContainer; 

		for (Slot slot : container.inventorySlots) {
			ItemStack itemStack = slot.getStack();
			if (itemStack != null && itemStack.getItem() instanceof ItemBlock && itemStack.stackSize >= 1) {
				count += itemStack.stackSize;
			}
		}

		return count;
	}

	public static ArrayList<Integer> getTrash() {
		ArrayList<Integer> trash = new ArrayList<>();
		int removedBlocks = 0;
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
		ContainerPlayer container = (ContainerPlayer) player.openContainer; 

		for (int i = 0; i < 45; ++i) {
			Slot slot = container.getSlot(i);
			if (!slot.getHasStack()) {
				continue; // Skip empty slots
			}

			ItemStack itemStack = slot.getStack();
			if (!isGood(itemStack) && !(itemStack.getItem() instanceof ItemBlock)) {
				trash.add(i);
			}

			if (itemStack.getItem() instanceof ItemBlock && getBlockCount() - removedBlocks > 128) {
				trash.add(i);
				removedBlocks += itemStack.stackSize;
			}

			if (itemStack.getItem() instanceof ItemArmor) {
				int[] bestArmorSlots = ItemUtil.getBestArmorInInventoryAsSlots();
				if (i != bestArmorSlots[0] && i != bestArmorSlots[1] && i != bestArmorSlots[2] && i != bestArmorSlots[3]) {
					trash.add(i);
				}
			}

			if (itemStack.getItem() instanceof ItemSword) {
				if (i != ItemUtil.getBestWeaponInInventoryAsSlot()) {
					trash.add(i);
				}
			}

			if (itemStack.getItem() instanceof ItemTool) {
				int[] bestToolSlots = ItemUtil.getBestToolsInInventoryAsSlots();
				if (i != ItemUtil.getBestWeaponInInventoryAsSlot() && i != bestToolSlots[0] && i != bestToolSlots[1] && i != bestToolSlots[2]) {
					trash.add(i);
				}
			}
		}

		return trash;
	}

	public static int getFoodSlot() {
		int size = -1;
		int slot = -1;

		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
		ContainerPlayer container = (ContainerPlayer) player.openContainer; 

		for (int i = 0; i < 45; ++i) {
			Slot inventorySlot = container.getSlot(i);
			ItemStack itemStack = inventorySlot.getStack();

			if (itemStack != null && itemStack.getItem() instanceof ItemAppleGold && itemStack.stackSize > size) {
				size = itemStack.stackSize;
				slot = i;
			}
		}

		return slot;
	}

	public static int getBlockSlot() {
		int size = -1;
		int slot = -1;

		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
		ContainerPlayer container = (ContainerPlayer) player.openContainer; 

		for (int i = 0; i < 45; ++i) {
			Slot inventorySlot = container.getSlot(i);
			ItemStack itemStack = inventorySlot.getStack();

			if (itemStack != null && itemStack.getItem() instanceof ItemBlock && itemStack.stackSize > size) {
				size = itemStack.stackSize;
				slot = i;
			}
		}

		return slot;
	}

	public static boolean hasSortNeed() {
		int swordSlot = ItemUtil.getBestWeaponInInventoryAsSlot();
		int[] toolSlots = ItemUtil.getBestToolsInInventoryAsSlots();
		int foodSlot = getFoodSlot();
		int[] armorSlots = ItemUtil.getBestArmorInInventoryAsSlots();
		int blockSlot = getBlockSlot();

		boolean swordNeedsSort = swordSlot != -1 && swordSlot - 36 != FeatureInventoryManager.swordSlot - 1;
		boolean pickaxeNeedsSort = toolSlots[0] != -1 && toolSlots[0] - 36 != FeatureInventoryManager.pickaxeSlot - 1;
		boolean axeNeedsSort = toolSlots[1] != -1 && toolSlots[1] - 36 != FeatureInventoryManager.axeSlot - 1;
		boolean shovelNeedsSort = toolSlots[2] != -1 && toolSlots[2] - 36 != FeatureInventoryManager.shovelSlot - 1;
		boolean foodNeedsSort = foodSlot != -1 && foodSlot - 36 != FeatureInventoryManager.foodSlot - 1;
		boolean armor0NeedsSort = armorSlots[0] != -1 && armorSlots[0] != 5;
		boolean armor1NeedsSort = armorSlots[1] != -1 && armorSlots[1] != 6;
		boolean armor2NeedsSort = armorSlots[2] != -1 && armorSlots[2] != 7;
		boolean armor3NeedsSort = armorSlots[3] != -1 && armorSlots[3] != 8;
		boolean blockNeedsSort = blockSlot != -1 && blockSlot - 36 != FeatureInventoryManager.blockSlot - 1;

		return swordNeedsSort || pickaxeNeedsSort || axeNeedsSort || shovelNeedsSort || foodNeedsSort || armor0NeedsSort || armor1NeedsSort || armor2NeedsSort || armor3NeedsSort || blockNeedsSort;
	}

}
