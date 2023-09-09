package gay.nns.client.impl.feature.other;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.SettingBoolean;
import gay.nns.client.api.setting.annotations.SettingMode;
import gay.nns.client.api.setting.annotations.Serialize;
import gay.nns.client.api.setting.annotations.SettingSlider;
import gay.nns.client.impl.event.player.EventUpdate;
import gay.nns.client.util.math.TimerUtil;
import gay.nns.client.util.player.InventoryUtil;
import gay.nns.client.util.player.ItemUtil;
import gay.nns.client.util.player.WindowUtil;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

@FeatureInfo(name = "Manager", description = "Automatically sorts your inventory")
public class FeatureInventoryManager extends Feature {

	@Serialize(name = "Mode")
	@SettingMode(modes = {"Normal", "Spoof", "Inventory"})
	public String mode = "Inventory";

	@Serialize(name = "No_Move")
	@SettingBoolean
	public boolean noMove = true;

	@Serialize(name = "Delay")
	@SettingSlider(min = 0, max = 1000, increment = 1)
	public double delay = 0;

	@Serialize(name = "Sword_Slot")
	@SettingSlider(min = 1, max = 9, increment = 1)
	public static double swordSlot = 1;

	@Serialize(name = "Pickaxe_Slot")
	@SettingSlider(min = 1, max = 9, increment = 1)
	public static double pickaxeSlot = 2;

	@Serialize(name = "Axe_Slot")
	@SettingSlider(min = 1, max = 9, increment = 1)
	public static double axeSlot = 3;

	@Serialize(name = "Shovel_Slot")
	@SettingSlider(min = 1, max = 9, increment = 1)
	public static double shovelSlot = 4;

	@Serialize(name = "Block_Slot")
	@SettingSlider(min = 1, max = 9, increment = 1)
	public static double blockSlot = 5;

	@Serialize(name = "Food_Slot")
	@SettingSlider(min = 1, max = 9, increment = 1)
	public static double foodSlot = 9;

	private final TimerUtil timerUtil = new TimerUtil();

	private boolean invOpen = false;

	public FeatureInventoryManager() {
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
	public void onUpdate(final EventUpdate updateEvent) {

		if (!(mc.currentScreen instanceof GuiChest) && mc.thePlayer != null && mc.theWorld != null) {
			boolean canDo = false;
			if (!InventoryUtil.getTrash().isEmpty() || InventoryUtil.hasSortNeed()) {
				if (noMove && (Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()) || Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode()) || Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode()) || Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode()))) {
					return;
				}
				switch (mode) {
					case "Spoof", "Normal" -> canDo = true;
					case "Inventory" -> {
						if (mc.currentScreen instanceof GuiInventory) {
							canDo = true;
						}
					}
				}
			}

			if (canDo) {
				if (!InventoryUtil.getTrash().isEmpty()) {
					open();
					removeJunk(InventoryUtil.getTrash());
					close();
				} else if (InventoryUtil.hasSortNeed()) {
					open();
					manage();
					close();
				}
				
			} else {
				timerUtil.reset();
			}
		}
	}

	private void removeJunk(ArrayList<Integer> junk) {
		for (int i : junk) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getStack() != null && timerUtil.hasTimeElapsed((long) delay)) {
				WindowUtil.drop(i, mc.thePlayer.openContainer.windowId);
				timerUtil.reset();
			}
		}
	}

	private void manageGaps() {
		boolean doManage = true;
		if (InventoryUtil.getFoodSlot() != -1 && InventoryUtil.getFoodSlot() - 36 != foodSlot - 1) {
			if (mc.thePlayer.inventoryContainer.getSlot((int) (foodSlot - 1 + 36)).getStack() != null && mc.thePlayer.inventoryContainer.getSlot((int) (foodSlot - 1 + 36)).getStack().getItem() instanceof ItemAppleGold) {
				doManage = false;
			}

			if (doManage && timerUtil.hasTimeElapsed((long) delay)) {
				WindowUtil.swap(InventoryUtil.getFoodSlot(), (int) (foodSlot - 1), mc.thePlayer.openContainer.windowId);
				timerUtil.reset();
			}
		}

	}

	private void manageBlocks() {
		boolean doManage = true;
		if (InventoryUtil.getBlockSlot() != -1 && InventoryUtil.getBlockSlot() - 36 != blockSlot - 1) {
			if (mc.thePlayer.inventoryContainer.getSlot((int) (blockSlot - 1 + 36)).getStack() != null && mc.thePlayer.inventoryContainer.getSlot((int) (blockSlot - 1 + 36)).getStack().getItem() instanceof ItemBlock) {
				doManage = false;
			}

			if (doManage && timerUtil.hasTimeElapsed((long) delay)) {
				WindowUtil.swap(InventoryUtil.getBlockSlot(), (int) (blockSlot - 1), mc.thePlayer.openContainer.windowId);
				timerUtil.reset();
			}
		}

	}

	private void manageWeapon() {
		if (ItemUtil.getBestWeaponInInventoryAsSlot() != -1 && ItemUtil.getBestWeaponInInventoryAsSlot() - 36 != swordSlot - 1 && timerUtil.hasTimeElapsed((long) delay)) {
			WindowUtil.swap(ItemUtil.getBestWeaponInInventoryAsSlot(), (int) (swordSlot - 1), mc.thePlayer.openContainer.windowId);
			timerUtil.reset();
		}
	}

	private void manageArmor() {
		if (mc.thePlayer.inventoryContainer.getSlot(5).getStack() != null) {
			if (5 != ItemUtil.getBestArmorInInventoryAsSlots()[0] && ItemUtil.getBestArmorInInventoryAsSlots()[0] != -1 && timerUtil.hasTimeElapsed((long) delay)) {
				WindowUtil.drop(5, mc.thePlayer.openContainer.windowId);
				WindowUtil.shiftClick(ItemUtil.getBestArmorInInventoryAsSlots()[0], mc.thePlayer.openContainer.windowId);
				timerUtil.reset();
			}
		} else if (timerUtil.hasTimeElapsed((long) delay) && ItemUtil.getBestArmorInInventoryAsSlots()[0] != -1) {
			WindowUtil.shiftClick(ItemUtil.getBestArmorInInventoryAsSlots()[0], mc.thePlayer.openContainer.windowId);
			timerUtil.reset();
		}

		if (mc.thePlayer.inventoryContainer.getSlot(6).getStack() != null) {
			if (6 != ItemUtil.getBestArmorInInventoryAsSlots()[1] && ItemUtil.getBestArmorInInventoryAsSlots()[1] != -1 && timerUtil.hasTimeElapsed((long) delay)) {
				WindowUtil.drop(6, mc.thePlayer.openContainer.windowId);
				WindowUtil.shiftClick(ItemUtil.getBestArmorInInventoryAsSlots()[1], mc.thePlayer.openContainer.windowId);
				timerUtil.reset();
			}
		} else if (timerUtil.hasTimeElapsed((long) delay) && ItemUtil.getBestArmorInInventoryAsSlots()[1] != -1) {
			WindowUtil.shiftClick(ItemUtil.getBestArmorInInventoryAsSlots()[1], mc.thePlayer.openContainer.windowId);
			timerUtil.reset();
		}

		if (mc.thePlayer.inventoryContainer.getSlot(7).getStack() != null) {
			if (7 != ItemUtil.getBestArmorInInventoryAsSlots()[2] && ItemUtil.getBestArmorInInventoryAsSlots()[2] != -1 && timerUtil.hasTimeElapsed((long) delay)) {
				WindowUtil.drop(7, mc.thePlayer.openContainer.windowId);
				WindowUtil.shiftClick(ItemUtil.getBestArmorInInventoryAsSlots()[2], mc.thePlayer.openContainer.windowId);
				timerUtil.reset();
			}
		} else if (timerUtil.hasTimeElapsed((long) delay) && ItemUtil.getBestArmorInInventoryAsSlots()[2] != -1) {
			WindowUtil.shiftClick(ItemUtil.getBestArmorInInventoryAsSlots()[2], mc.thePlayer.openContainer.windowId);
			timerUtil.reset();
		}

		if (mc.thePlayer.inventoryContainer.getSlot(8).getStack() != null) {
			if (8 != ItemUtil.getBestArmorInInventoryAsSlots()[3] && ItemUtil.getBestArmorInInventoryAsSlots()[3] != -1 && timerUtil.hasTimeElapsed((long) delay)) {
				WindowUtil.drop(8, mc.thePlayer.openContainer.windowId);
				WindowUtil.shiftClick(ItemUtil.getBestArmorInInventoryAsSlots()[3], mc.thePlayer.openContainer.windowId);
				timerUtil.reset();
			}
		} else if (timerUtil.hasTimeElapsed((long) delay) && ItemUtil.getBestArmorInInventoryAsSlots()[3] != -1) {
			WindowUtil.shiftClick(ItemUtil.getBestArmorInInventoryAsSlots()[3], mc.thePlayer.openContainer.windowId);
			timerUtil.reset();
		}
	}

	private void manageTools() {
		if (ItemUtil.getBestToolsInInventoryAsSlots()[0] != -1 && ItemUtil.getBestToolsInInventoryAsSlots()[0] - 36 != pickaxeSlot - 1 && ItemUtil.getBestToolsInInventoryAsSlots()[0] != ItemUtil.getBestWeaponInInventoryAsSlot() && timerUtil.hasTimeElapsed((long) delay)) {
			WindowUtil.swap(ItemUtil.getBestToolsInInventoryAsSlots()[0], (int) (pickaxeSlot - 1), mc.thePlayer.openContainer.windowId);
			timerUtil.reset();
		}

		if (ItemUtil.getBestToolsInInventoryAsSlots()[1] != -1 && ItemUtil.getBestToolsInInventoryAsSlots()[1] - 36 != axeSlot - 1 && ItemUtil.getBestToolsInInventoryAsSlots()[1] != ItemUtil.getBestWeaponInInventoryAsSlot() && timerUtil.hasTimeElapsed((long) delay)) {
			WindowUtil.swap(ItemUtil.getBestToolsInInventoryAsSlots()[1], (int) (axeSlot - 1), mc.thePlayer.openContainer.windowId);
			timerUtil.reset();
		}

		if (ItemUtil.getBestToolsInInventoryAsSlots()[2] != -1 && ItemUtil.getBestToolsInInventoryAsSlots()[2] - 36 != shovelSlot - 1 && ItemUtil.getBestToolsInInventoryAsSlots()[2] != ItemUtil.getBestWeaponInInventoryAsSlot() && timerUtil.hasTimeElapsed((long) delay)) {
			WindowUtil.swap(ItemUtil.getBestToolsInInventoryAsSlots()[2], (int) (shovelSlot - 1), mc.thePlayer.openContainer.windowId);
			timerUtil.reset();
		}

	}

	private void manage() {
		manageArmor();
		manageWeapon();
		manageTools();
		manageBlocks();
		manageGaps();
	}

	private void open() {
		if (mode.equalsIgnoreCase("Spoof") && !invOpen) {
			mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
			invOpen = true;
		}
	}

	private void close() {
		if (mode.equalsIgnoreCase("Spoof") && invOpen) {
			mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0DPacketCloseWindow());
			invOpen = false;
		}
	}

}
