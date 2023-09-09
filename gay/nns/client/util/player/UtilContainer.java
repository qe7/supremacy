package gay.nns.client.util.player;

import net.minecraft.inventory.ContainerChest;

public class UtilContainer {

	public static boolean isChestEmpty(ContainerChest chest) {
		for(int i = 0; i < 36; ++i) {
			if (chest.getLowerChestInventory().getStackInSlot(i) != null && !UtilPlayer.isBadItem(chest.getLowerChestInventory().getStackInSlot(i).getItem()) && !UtilPlayer.isBadPotion(chest.getLowerChestInventory().getStackInSlot(i)) && !UtilPlayer.getTrash().contains(chest.getLowerChestInventory().getStackInSlot(i))) {
				return false;
			}
		}
		return true;
	}

}
