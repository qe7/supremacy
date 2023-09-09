package gay.nns.client.impl.feature.ghost;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.SettingBoolean;
import gay.nns.client.api.setting.annotations.Serialize;
import gay.nns.client.api.setting.annotations.SettingSlider;
import gay.nns.client.impl.event.player.EventUpdate;
import gay.nns.client.util.math.UtilTimer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.*;
import org.lwjgl.input.Mouse;

@FeatureInfo(name = "AutoClicker", category = FeatureCategory.GHOST, description = "Automatically clicks")
public class FeatureAutoClicker extends Feature {

	@Serialize(name = "Left_Click")
	@SettingBoolean
	public boolean leftClick = false;

	@Serialize(name = "Right_Click")
	@SettingBoolean
	public boolean rightClick = false;

	@Serialize(name = "Left_Max_CPS")
	@SettingSlider(min = 1, max = 20, increment = 1)
	public double maxCPS = 18;

	@Serialize(name = "Left_Min_CPS")
	@SettingSlider(min = 1, max = 20, increment = 1)
	public double minCPS = 14;

	@Serialize(name = "Right_Max_CPS")
	@SettingSlider(min = 1, max = 20, increment = 1)
	public double rightMaxCPS = 18;

	@Serialize(name = "Right_Min_CPS")
	@SettingSlider(min = 1, max = 20, increment = 1)
	public double rightMinCPS = 14;

	private final UtilTimer leftTimerUtil = new UtilTimer();
	private final UtilTimer rightTimerUtil = new UtilTimer();

	public FeatureAutoClicker() {
		super();
	}

	@Subscribe
	public void onUpdate(final EventUpdate updateEvent) {

		if (Mouse.isButtonDown(0) && leftClick) {
			if (mc.thePlayer.getHeldItem() != null && (mc.thePlayer.getHeldItem().getItem() instanceof ItemPickaxe || mc.thePlayer.getHeldItem().getItem() instanceof ItemAxe || mc.thePlayer.getHeldItem().getItem() instanceof ItemSpade)) return;
			long delay = 1000L / getRandom((int) minCPS, (int) maxCPS);
			if (leftTimerUtil.hasTimeElapsed(delay)) {
				sendClick(0, true);
			}
			if (leftTimerUtil.hasTimeElapsed(delay + getRandom(12, 42))) {
				sendClick(0, false);
				leftTimerUtil.reset();
			}
		}

		if (Mouse.isButtonDown(1) && rightClick) {
			if (mc.thePlayer.getHeldItem() != null && (mc.thePlayer.getHeldItem().getItem() instanceof ItemBow || mc.thePlayer.getHeldItem().getItem() instanceof ItemFood || mc.thePlayer.getHeldItem().getItem() instanceof ItemPotion)) return;
			long delay = 1000L / getRandom((int) rightMinCPS, (int) rightMaxCPS);
			if (rightTimerUtil.hasTimeElapsed(delay)) {
				sendClick(1, true);
			}
			if (rightTimerUtil.hasTimeElapsed(delay + getRandom(12, 41))) {
				sendClick(1, false);
				rightTimerUtil.reset();
			}
		}

	}

	private void sendClick(final int button, final boolean state) {
		final int keyBind = button == 0 ? mc.gameSettings.keyBindAttack.getKeyCode() : mc.gameSettings.keyBindUseItem.getKeyCode();

		KeyBinding.setKeyBindState(keyBind, state);

		if (state) {
			KeyBinding.onTick(keyBind);
		}
	}

	// get a random number between min and max
	private int getRandom(int min, int max) {
		return (int) (Math.random() * (max - min)) + min;
	}

}
