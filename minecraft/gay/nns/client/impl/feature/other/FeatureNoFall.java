package gay.nns.client.impl.feature.other;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.Mode;
import gay.nns.client.api.setting.annotations.Serialize;
import gay.nns.client.impl.event.player.MotionEvent;
import gay.nns.client.impl.event.render.Render2DEvent;
import gay.nns.client.util.IMinecraft;

@FeatureInfo(name = "No_Fall", description = "Prevents fall damage.", category = FeatureCategory.OTHER)
public class FeatureNoFall extends AbstractFeature {

	@Serialize(name = "Mode")
	@Mode(modes = {"Packet", "Damage"})
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
	public void onRender(final Render2DEvent render2DEvent) {
		this.setSuffix(mode);
	}

	@Subscribe
	public void onMotionUpdate(final MotionEvent motionEvent) {
		switch (mode.toLowerCase()) {
			case "packet" -> {
				if (IMinecraft.mc.thePlayer.fallDistance > 3.0F) {
					motionEvent.setGround(true);
					IMinecraft.mc.thePlayer.fallDistance = 0.0F;
				}
			}
			case "damage" -> {
				if (IMinecraft.mc.thePlayer.fallDistance > 3.2F) {
					motionEvent.setGround(true);
					IMinecraft.mc.thePlayer.fallDistance = 0.0F;
				}
			}
		}
	}

}
