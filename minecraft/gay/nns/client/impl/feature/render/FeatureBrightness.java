package gay.nns.client.impl.feature.render;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.impl.event.render.Render2DEvent;

@FeatureInfo(name = "Brightness", description = "Like Ahru it brightens my world. >w<", category = FeatureCategory.RENDER)
public class FeatureBrightness extends AbstractFeature {

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
	public void onRender2D(final Render2DEvent render2DEvent) {
		mc.gameSettings.gammaSetting = 100.0F;
	}

}
