package gay.nns.client.impl.feature.other;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.SettingMode;
import gay.nns.client.api.setting.annotations.Serialize;
import gay.nns.client.impl.event.player.EventPreMotion;
import gay.nns.client.impl.event.render.EventRender2D;

@FeatureInfo(name = "NoFall", description = "Prevents fall damage.", category = FeatureCategory.OTHER)
public class FeatureNoFall extends Feature {

	@Serialize(name = "Mode")
	@SettingMode(modes = {"Packet", "Damage"})
	public String mode = "Packet";

	public FeatureNoFall() {
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
	public void onRender(final EventRender2D render2DEvent) {
		this.setSuffix(mode);
	}

	@Subscribe
	public void onMotionUpdate(final EventPreMotion motionEvent) {
		switch (mode.toLowerCase()) {
			case "packet" -> {
				if (mc.thePlayer.fallDistance > 3.0F) {
					motionEvent.setGround(true);
					mc.thePlayer.fallDistance = 0.0F;
				}
			}
			case "damage" -> {
				if (mc.thePlayer.fallDistance > 3.2F) {
					motionEvent.setGround(true);
					mc.thePlayer.fallDistance = 0.0F;
				}
			}
		}
	}

}
