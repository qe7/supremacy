package gay.nns.client.feature.impl.render;

import gay.nns.client.event.api.interfaces.Subscribe;
import gay.nns.client.feature.api.types.Feature;
import gay.nns.client.feature.api.enums.FeatureCategory;
import gay.nns.client.feature.api.interfaces.SerializeFeature;
import gay.nns.client.event.impl.render.EventRender2D;

@SerializeFeature(name = "Brightness", description = "Like Ahru it brightens my world. >w<", category = FeatureCategory.RENDER)
public class FeatureBrightness extends Feature {

	private float oldGamma;

	public FeatureBrightness() {
		this.toggle();
	}

	@Override
	protected void onEnable() {
		super.onEnable();

		oldGamma = mc.gameSettings.gammaSetting;
	}

	@Override
	protected void onDisable() {
		super.onDisable();

		mc.gameSettings.gammaSetting = oldGamma;
	}

	@Subscribe
	public void onRender2D(final EventRender2D render2DEvent) {
		mc.gameSettings.gammaSetting = 100.0F;
	}

}
