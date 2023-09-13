package gay.nns.client.impl.feature.other;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.interfaces.SerializeFeature;
import gay.nns.client.api.setting.annotations.SettingBoolean;
import gay.nns.client.api.setting.annotations.SettingMode;
import gay.nns.client.api.setting.annotations.SerializeSetting;
import gay.nns.client.api.setting.annotations.SettingSlider;
import gay.nns.client.impl.event.player.EventUpdate;
import gay.nns.client.util.math.UtilTimer;
import gay.nns.client.util.player.UtilInventory;
import gay.nns.client.util.player.UtilItem;
import gay.nns.client.util.player.UtilWindow;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

@SerializeFeature(name = "Manager", description = "Automatically sorts your inventory")
public class FeatureInventoryManager extends Feature {

	@SerializeSetting(name = "Mode")
	@SettingMode(modes = {"Normal", "Spoof", "Inventory"})
	public String mode = "Inventory";

	@SerializeSetting(name = "No_Move")
	@SettingBoolean
	public boolean noMove = true;

	@SerializeSetting(name = "Delay")
	@SettingSlider(min = 0, max = 1000, increment = 1)
	public double delay = 0;

	@SerializeSetting(name = "Sword_Slot")
	@SettingSlider(min = 1, max = 9, increment = 1)
	public static double swordSlot = 1;

	@SerializeSetting(name = "Pickaxe_Slot")
	@SettingSlider(min = 1, max = 9, increment = 1)
	public static double pickaxeSlot = 2;

	@SerializeSetting(name = "Axe_Slot")
	@SettingSlider(min = 1, max = 9, increment = 1)
	public static double axeSlot = 3;

	@SerializeSetting(name = "Shovel_Slot")
	@SettingSlider(min = 1, max = 9, increment = 1)
	public static double shovelSlot = 4;

	@SerializeSetting(name = "Block_Slot")
	@SettingSlider(min = 1, max = 9, increment = 1)
	public static double blockSlot = 5;

	@SerializeSetting(name = "Food_Slot")
	@SettingSlider(min = 1, max = 9, increment = 1)
	public static double foodSlot = 9;

	private final UtilTimer timerUtil = new UtilTimer();

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
			if (!UtilInventory.getTrash().isEmpty() || UtilInventory.hasSortNeed()) {
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
				if (!UtilInventory.getTrash().isEmpty()) {
					open();
					removeJunk(UtilInventory.getTrash());
					close();
				} else if (UtilInventory.hasSortNeed()) {
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
				UtilWindow.drop(i, mc.thePlayer.openContainer.windowId);
				timerUtil.reset();
			}
		}
	}

	private void manageGaps() {
		boolean doManage = true;
		if (UtilInventory.getFoodSlot() != -1 && UtilInventory.getFoodSlot() - 36 != foodSlot - 1) {
			if (mc.thePlayer.inventoryContainer.getSlot((int) (foodSlot - 1 + 36)).getStack() != null && mc.thePlayer.inventoryContainer.getSlot((int) (foodSlot - 1 + 36)).getStack().getItem() instanceof ItemAppleGold) {
				doManage = false;
			}

			if (doManage && timerUtil.hasTimeElapsed((long) delay)) {
				UtilWindow.swap(UtilInventory.getFoodSlot(), (int) (foodSlot - 1), mc.thePlayer.openContainer.windowId);
				timerUtil.reset();
			}
		}

	}

	private void manageBlocks() {
		boolean doManage = true;
		if (UtilInventory.getBlockSlot() != -1 && UtilInventory.getBlockSlot() - 36 != blockSlot - 1) {
			if (mc.thePlayer.inventoryContainer.getSlot((int) (blockSlot - 1 + 36)).getStack() != null && mc.thePlayer.inventoryContainer.getSlot((int) (blockSlot - 1 + 36)).getStack().getItem() instanceof ItemBlock) {
				doManage = false;
			}

			if (doManage && timerUtil.hasTimeElapsed((long) delay)) {
				UtilWindow.swap(UtilInventory.getBlockSlot(), (int) (blockSlot - 1), mc.thePlayer.openContainer.windowId);
				timerUtil.reset();
			}
		}

	}

	private void manageWeapon() {
		if (UtilItem.getBestWeaponInInventoryAsSlot() != -1 && UtilItem.getBestWeaponInInventoryAsSlot() - 36 != swordSlot - 1 && timerUtil.hasTimeElapsed((long) delay)) {
			UtilWindow.swap(UtilItem.getBestWeaponInInventoryAsSlot(), (int) (swordSlot - 1), mc.thePlayer.openContainer.windowId);
			timerUtil.reset();
		}
	}

	private void manageArmor() {
		if (mc.thePlayer.inventoryContainer.getSlot(5).getStack() != null) {
			if (5 != UtilItem.getBestArmorInInventoryAsSlots()[0] && UtilItem.getBestArmorInInventoryAsSlots()[0] != -1 && timerUtil.hasTimeElapsed((long) delay)) {
				UtilWindow.drop(5, mc.thePlayer.openContainer.windowId);
				UtilWindow.shiftClick(UtilItem.getBestArmorInInventoryAsSlots()[0], mc.thePlayer.openContainer.windowId);
				timerUtil.reset();
			}
		} else if (timerUtil.hasTimeElapsed((long) delay) && UtilItem.getBestArmorInInventoryAsSlots()[0] != -1) {
			UtilWindow.shiftClick(UtilItem.getBestArmorInInventoryAsSlots()[0], mc.thePlayer.openContainer.windowId);
			timerUtil.reset();
		}

		if (mc.thePlayer.inventoryContainer.getSlot(6).getStack() != null) {
			if (6 != UtilItem.getBestArmorInInventoryAsSlots()[1] && UtilItem.getBestArmorInInventoryAsSlots()[1] != -1 && timerUtil.hasTimeElapsed((long) delay)) {
				UtilWindow.drop(6, mc.thePlayer.openContainer.windowId);
				UtilWindow.shiftClick(UtilItem.getBestArmorInInventoryAsSlots()[1], mc.thePlayer.openContainer.windowId);
				timerUtil.reset();
			}
		} else if (timerUtil.hasTimeElapsed((long) delay) && UtilItem.getBestArmorInInventoryAsSlots()[1] != -1) {
			UtilWindow.shiftClick(UtilItem.getBestArmorInInventoryAsSlots()[1], mc.thePlayer.openContainer.windowId);
			timerUtil.reset();
		}

		if (mc.thePlayer.inventoryContainer.getSlot(7).getStack() != null) {
			if (7 != UtilItem.getBestArmorInInventoryAsSlots()[2] && UtilItem.getBestArmorInInventoryAsSlots()[2] != -1 && timerUtil.hasTimeElapsed((long) delay)) {
				UtilWindow.drop(7, mc.thePlayer.openContainer.windowId);
				UtilWindow.shiftClick(UtilItem.getBestArmorInInventoryAsSlots()[2], mc.thePlayer.openContainer.windowId);
				timerUtil.reset();
			}
		} else if (timerUtil.hasTimeElapsed((long) delay) && UtilItem.getBestArmorInInventoryAsSlots()[2] != -1) {
			UtilWindow.shiftClick(UtilItem.getBestArmorInInventoryAsSlots()[2], mc.thePlayer.openContainer.windowId);
			timerUtil.reset();
		}

		if (mc.thePlayer.inventoryContainer.getSlot(8).getStack() != null) {
			if (8 != UtilItem.getBestArmorInInventoryAsSlots()[3] && UtilItem.getBestArmorInInventoryAsSlots()[3] != -1 && timerUtil.hasTimeElapsed((long) delay)) {
				UtilWindow.drop(8, mc.thePlayer.openContainer.windowId);
				UtilWindow.shiftClick(UtilItem.getBestArmorInInventoryAsSlots()[3], mc.thePlayer.openContainer.windowId);
				timerUtil.reset();
			}
		} else if (timerUtil.hasTimeElapsed((long) delay) && UtilItem.getBestArmorInInventoryAsSlots()[3] != -1) {
			UtilWindow.shiftClick(UtilItem.getBestArmorInInventoryAsSlots()[3], mc.thePlayer.openContainer.windowId);
			timerUtil.reset();
		}
	}

	private void manageTools() {
		if (UtilItem.getBestToolsInInventoryAsSlots()[0] != -1 && UtilItem.getBestToolsInInventoryAsSlots()[0] - 36 != pickaxeSlot - 1 && UtilItem.getBestToolsInInventoryAsSlots()[0] != UtilItem.getBestWeaponInInventoryAsSlot() && timerUtil.hasTimeElapsed((long) delay)) {
			UtilWindow.swap(UtilItem.getBestToolsInInventoryAsSlots()[0], (int) (pickaxeSlot - 1), mc.thePlayer.openContainer.windowId);
			timerUtil.reset();
		}

		if (UtilItem.getBestToolsInInventoryAsSlots()[1] != -1 && UtilItem.getBestToolsInInventoryAsSlots()[1] - 36 != axeSlot - 1 && UtilItem.getBestToolsInInventoryAsSlots()[1] != UtilItem.getBestWeaponInInventoryAsSlot() && timerUtil.hasTimeElapsed((long) delay)) {
			UtilWindow.swap(UtilItem.getBestToolsInInventoryAsSlots()[1], (int) (axeSlot - 1), mc.thePlayer.openContainer.windowId);
			timerUtil.reset();
		}

		if (UtilItem.getBestToolsInInventoryAsSlots()[2] != -1 && UtilItem.getBestToolsInInventoryAsSlots()[2] - 36 != shovelSlot - 1 && UtilItem.getBestToolsInInventoryAsSlots()[2] != UtilItem.getBestWeaponInInventoryAsSlot() && timerUtil.hasTimeElapsed((long) delay)) {
			UtilWindow.swap(UtilItem.getBestToolsInInventoryAsSlots()[2], (int) (shovelSlot - 1), mc.thePlayer.openContainer.windowId);
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
