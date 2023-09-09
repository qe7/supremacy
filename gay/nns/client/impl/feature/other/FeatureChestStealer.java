package gay.nns.client.impl.feature.other;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.SettingBoolean;
import gay.nns.client.api.setting.annotations.Serialize;
import gay.nns.client.api.setting.annotations.SettingSlider;
import gay.nns.client.impl.event.player.EventUpdate;
import gay.nns.client.util.math.TimerUtil;
import gay.nns.client.util.player.ContainerUtil;
import gay.nns.client.util.player.PlayerUtil;
import gay.nns.client.util.player.WindowUtil;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

@FeatureInfo(name = "Stealer", description = "Automatically steals items from chests")
public class FeatureChestStealer extends Feature {

	@Serialize(name = "Title_Check")
	@SettingBoolean
	public boolean titleCheck = true;

	@Serialize(name = "No_Move")
	@SettingBoolean
	public boolean noMove = true;

	@Serialize(name = "Delay")
	@SettingSlider(min = 0, max = 1000, increment = 1)
	public double delay = 0;

	private final TimerUtil timerUtil = new TimerUtil();

	public FeatureChestStealer() {
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
	public void onUpdate(final EventUpdate ignoredUpdateEvent) {
		if (mc.currentScreen != null) {
			// Check if the current screen is a chest
			if (mc.currentScreen instanceof GuiChest) {
				if (noMove && (Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()) || Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode()) || Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode()) || Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode()))) {
					return;
				}

				ContainerChest chest = (ContainerChest)mc.thePlayer.openContainer;

				ArrayList<Integer> bestItems = PlayerUtil.getBestItems(chest);

				if (!(chest.getLowerChestInventory().getName().contains("Chest") || chest.getLowerChestInventory().getName().contains("Container") || chest.getLowerChestInventory().getName().contains("Storage")) && titleCheck) {
					return;
				}

				if (!PlayerUtil.isInventoryFull() && !ContainerUtil.isChestEmpty(chest)) {

					for (Integer bestItem : bestItems) {
						if (timerUtil.hasTimeElapsed((long) delay)) {
							WindowUtil.shiftClickChest(bestItem, chest.windowId);
							timerUtil.reset();
						}
					}
				} else {
					mc.thePlayer.closeScreen();
				}
			}
		}
	}

}
