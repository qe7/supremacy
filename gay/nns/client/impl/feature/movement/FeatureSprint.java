package gay.nns.client.impl.feature.movement;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.Mode;
import gay.nns.client.api.setting.annotations.Serialize;
import gay.nns.client.impl.event.player.UpdateEvent;
import gay.nns.client.impl.event.render.Render2DEvent;
import gay.nns.client.api.feature.AbstractFeature;
import org.lwjgl.input.Keyboard;

@FeatureInfo(name = "Sprint", description = "Automatically sprints for the player.", category = FeatureCategory.MOVEMENT)
public class FeatureSprint extends AbstractFeature {

	@Serialize(name = "Mode")
	@Mode(modes = {"Legit", "Rage"})
	public static String mode = "Legit";

	public FeatureSprint() {
		super();
	}

	@Override
	protected void onEnable() {
		super.onEnable();
	}

	@Override
	protected void onDisable() {
		super.onDisable();
		mc.gameSettings.keyBindSprint.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindSprint.getKeyCode());
	}

	@Subscribe
	public void onUpdate(final UpdateEvent updateEvent) {
		if (mc.theWorld == null) return;
		if (mc.thePlayer == null) return;

		switch (mode) {
			case "Legit" -> mc.gameSettings.keyBindSprint.pressed = true;
			case "Rage" -> {
				if (mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0) {
					mc.thePlayer.setSprinting(true);
				}
			}
		}
	}

	@Subscribe
	public void onRender2D(final Render2DEvent render2DEvent) {
		if (mc.theWorld == null) return;
		if (mc.thePlayer == null) return;

		this.setSuffix(mode);
	}
}
