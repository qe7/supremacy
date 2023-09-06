package gay.nns.client.util.player;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ContainerChest;

public class ContainerUtil {

	public static boolean isChestEmpty(ContainerChest chest) {
		for(int i = 0; i < 36; ++i) {
			if (chest.getLowerChestInventory().getStackInSlot(i) != null && !PlayerUtil.isBadItem(chest.getLowerChestInventory().getStackInSlot(i).getItem()) && !PlayerUtil.isBadPotion(chest.getLowerChestInventory().getStackInSlot(i)) && !PlayerUtil.getTrash().contains(chest.getLowerChestInventory().getStackInSlot(i))) {
				return false;
			}
		}
		return true;
	}

}
