package gay.nns.client.impl.feature.render;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.CheckBox;
import gay.nns.client.api.setting.annotations.Mode;
import gay.nns.client.api.setting.annotations.Serialize;
import gay.nns.client.api.setting.annotations.Slider;
import gay.nns.client.impl.event.render.Render2DEvent;

@FeatureInfo(name = "Ambience", description = "Ambience", category = FeatureCategory.RENDER)
public class FeatureAmbience extends AbstractFeature {

	@Serialize(name = "Weather")
	@Mode(modes = {"Clear", "Rain", "Thunder"})
	public String weather = "Clear";

	@Serialize(name = "Amount")
	@Slider(min = 0d, max = 1d, increment = 0.1d)
	public double amount = 0.5d;

	@Serialize(name = "Strength")
	@Slider(min = 0d, max = 1d, increment = 0.1d)
	public double strength = 0.5d;

	@Serialize(name = "Time")
	@CheckBox
	public static boolean timeToggle = false;

	@Serialize(name = "Time")
	@Slider(min = 0d, max = 24000d, increment = 1000d)
	public static double time = 18000d;


	public FeatureAmbience() {
		this.toggle();
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
		this.setSuffix(weather);

		switch (weather.toLowerCase()) {
			case "clear" -> {
				mc.theWorld.setRainStrength(0.0F);
				mc.theWorld.setThunderStrength(0.0F);
			}
			case "rain" -> {
				mc.theWorld.setRainStrength((float) amount);
				mc.theWorld.setThunderStrength(0.0F);
			}
			case "thunder" -> {
				mc.theWorld.setRainStrength((float) amount);
				mc.theWorld.setThunderStrength((float) strength);
			}
		}

		if (timeToggle) mc.theWorld.setWorldTime((long) time);
	}

}
