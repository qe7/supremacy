package gay.nns.client.impl.feature.movement;

import gay.nns.client.api.core.Core;
import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.Mode;
import gay.nns.client.api.setting.annotations.Serialize;
import gay.nns.client.impl.event.player.UpdateEvent;
import gay.nns.client.impl.event.render.Render2DEvent;
import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.impl.feature.other.FeatureScaffold;
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
		mc.gameSettings.keyBindSprint.setKeyPressed(false);
	}

	@Subscribe
	public void onUpdate(final UpdateEvent updateEvent) {
		if (mc.theWorld == null) return;
		if (mc.thePlayer == null) return;
		if (Core.getSingleton().getFeatureManager().getFeatureFromType(FeatureScaffold.class).isEnabled()) {
			if (mc.gameSettings.keyBindSprint.isKeyDown())
				mc.gameSettings.keyBindSprint.setKeyPressed(false);
			mc.thePlayer.setSprinting(false);
			return;
		}

		switch (mode) {
			case "Legit" -> mc.gameSettings.keyBindSprint.setKeyPressed(true);
			case "Rage" -> {
				if (mc.currentScreen != null) return;
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
