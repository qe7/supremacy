package gay.nns.client.impl.feature.ghost;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.impl.event.player.UpdateEvent;
import gay.nns.client.util.IMinecraft;

@FeatureInfo(name = "No_Click_Delay", category = FeatureCategory.GHOST, description = "Removes the click delay")
public class FeatureNoClickDelay extends AbstractFeature {

	public FeatureNoClickDelay() {
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
	public void onUpdate(final UpdateEvent event) {
		if (IMinecraft.mc.theWorld == null) return;
		if (IMinecraft.mc.thePlayer == null) return;

		if (IMinecraft.mc.leftClickCounter != 0)
			IMinecraft.mc.leftClickCounter = 0;
	}

}
