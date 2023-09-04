package gay.nns.client.impl.feature.movement;

import gay.nns.client.api.setting.annotations.CheckBox;
import gay.nns.client.api.setting.annotations.Serialize;
import gay.nns.client.impl.ui.clickgui.ClickGUI;
import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.impl.event.player.UpdateEvent;
import net.minecraft.client.gui.GuiChat;
import org.lwjgl.input.Keyboard;

@FeatureInfo(name = "ScreenWalk", description = "Allows you to walk on the screen", category = FeatureCategory.MOVEMENT)
public class FeatureScreenWalk extends AbstractFeature {

	@Serialize(name = "ClickGUI_only")
	@CheckBox
	public boolean clickGUIOnly = true;

	@Serialize(name = "Rotate")
	@CheckBox
	public boolean rotate = true;

	@Serialize(name = "Allow_Sprinting")
	@CheckBox
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
	public void onUpdate(final UpdateEvent updateEvent) {
		if (mc.currentScreen == null) return;
		if (mc.currentScreen instanceof GuiChat) return;
		if (clickGUIOnly && !(mc.currentScreen instanceof ClickGUI)) return;

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
