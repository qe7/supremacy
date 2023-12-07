package gay.nns.client.util.player;

import net.minecraft.client.Minecraft;

public class UtilWindow {

	public static void shiftClickChest(int slot, int windowId) {
		Minecraft.getMinecraft().playerController.windowClick(windowId, slot, 0, 1, Minecraft.getMinecraft().thePlayer);
	}

	public static void swap(int slot, int hotBarSlot, int windowId) {
		Minecraft.getMinecraft().playerController.windowClick(windowId, slot, hotBarSlot, 2, Minecraft.getMinecraft().thePlayer);
	}

	public static void shiftClick(int slot, int windowId) {
		Minecraft.getMinecraft().playerController.windowClick(windowId, slot, 0, 1, Minecraft.getMinecraft().thePlayer);
	}

	public static void drop(int slot, int windowId) {
		Minecraft.getMinecraft().playerController.windowClick(windowId, slot, 1, 4, Minecraft.getMinecraft().thePlayer);
	}

}
