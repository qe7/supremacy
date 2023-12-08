package gay.nns.client.feature.impl.movement;

import gay.nns.client.setting.api.annotations.SettingBoolean;
import gay.nns.client.setting.api.annotations.SerializeSetting;
import gay.nns.client.ui.clickgui.GuiClick;
import gay.nns.client.event.api.interfaces.Subscribe;
import gay.nns.client.feature.api.types.Feature;
import gay.nns.client.feature.api.enums.FeatureCategory;
import gay.nns.client.feature.api.interfaces.SerializeFeature;
import gay.nns.client.event.impl.player.EventUpdate;
import net.minecraft.client.gui.GuiChat;
import org.lwjgl.input.Keyboard;

@SerializeFeature(name = "InventoryMove", description = "Allows you to walk on the screen", category = FeatureCategory.MOVEMENT)
public class FeatureScreenWalk extends Feature {

	@SerializeSetting(name = "ClickGUI_only")
	@SettingBoolean
	public boolean clickGUIOnly = true;

	@SerializeSetting(name = "Rotate")
	@SettingBoolean
	public boolean rotate = true;

	@SerializeSetting(name = "Allow_Sprinting")
	@SettingBoolean
	public boolean allowSprinting = true;

	public FeatureScreenWalk() {
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
		if (mc.currentScreen == null) return;
		if (mc.currentScreen instanceof GuiChat) return;
		if (clickGUIOnly && !(mc.currentScreen instanceof GuiClick)) return;

		if (!allowSprinting) {
			mc.thePlayer.setSprinting(false);
			mc.gameSettings.keyBindSprint.pressed = false;
		}

		mc.gameSettings.keyBindForward.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode());
		mc.gameSettings.keyBindBack.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode());
		mc.gameSettings.keyBindLeft.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode());
		mc.gameSettings.keyBindRight.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode());
		mc.gameSettings.keyBindJump.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode());

		if (rotate) {
			if (Keyboard.isKeyDown(Keyboard.KEY_UP))
				if (mc.thePlayer.rotationPitch > -90.0F)
					mc.thePlayer.rotationPitch += 5.0F;
			if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
				if (mc.thePlayer.rotationPitch < 90.0F)
					mc.thePlayer.rotationPitch -= 5.0F;
			if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
				mc.thePlayer.rotationYaw -= 5.0F;
			if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
				mc.thePlayer.rotationYaw += 5.0F;
		}
	}

}
