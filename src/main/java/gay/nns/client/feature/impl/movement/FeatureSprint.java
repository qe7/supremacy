package gay.nns.client.feature.impl.movement;

import gay.nns.client.SupremacyCore;
import gay.nns.client.event.api.interfaces.Subscribe;
import gay.nns.client.feature.api.enums.FeatureCategory;
import gay.nns.client.feature.api.interfaces.SerializeFeature;
import gay.nns.client.setting.api.annotations.SettingMode;
import gay.nns.client.setting.api.annotations.SerializeSetting;
import gay.nns.client.event.impl.player.EventUpdate;
import gay.nns.client.event.impl.render.EventRender2D;
import gay.nns.client.feature.api.types.Feature;
import gay.nns.client.feature.impl.other.FeatureScaffold;

@SerializeFeature(name = "Sprint", description = "Automatically sprints for the player.", category = FeatureCategory.MOVEMENT)
public class FeatureSprint extends Feature {

	@SerializeSetting(name = "Mode")
	@SettingMode(modes = {"Legit", "Rage"})
	public static String mode = "Legit";

	public FeatureSprint() {
		this.toggle();
	}

	@Override
	protected void onEnable() {
		super.onEnable();
	}

	@Override
	protected void onDisable() {
		super.onDisable();
		mc.gameSettings.keyBindSprint.setKeyPressed(false);
	}

	@Subscribe
	public void onUpdate(final EventUpdate updateEvent) {
		if (mc.theWorld == null) return;
		if (mc.thePlayer == null) return;
		if (SupremacyCore.getSingleton().getFeatureManager().getFeatureFromType(FeatureScaffold.class).isEnabled()) {
			if (mc.gameSettings.keyBindSprint.isKeyDown())
				mc.gameSettings.keyBindSprint.setKeyPressed(false);
			mc.thePlayer.setSprinting(false);
			return;
		}

		switch (mode) {
			case "Legit": mc.gameSettings.keyBindSprint.setKeyPressed(true); break;
			case "Rage": {
				if (mc.currentScreen != null) return;
				if (mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0) {
					mc.thePlayer.setSprinting(true);
				}
				break;
			}
		}
	}

	@Subscribe
	public void onRender2D(final EventRender2D render2DEvent) {
		if (mc.theWorld == null) return;
		if (mc.thePlayer == null) return;

		this.setSuffix(mode);
	}
}
