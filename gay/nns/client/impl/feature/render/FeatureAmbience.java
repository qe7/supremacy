package gay.nns.client.impl.feature.render;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.SerializeFeature;
import gay.nns.client.api.setting.annotations.SettingBoolean;
import gay.nns.client.api.setting.annotations.SettingMode;
import gay.nns.client.api.setting.annotations.SerializeSetting;
import gay.nns.client.api.setting.annotations.SettingSlider;
import gay.nns.client.impl.event.render.EventRender2D;

@SerializeFeature(name = "Ambience", description = "Ambience", category = FeatureCategory.RENDER)
public class FeatureAmbience extends Feature {

	@SerializeSetting(name = "Weather")
	@SettingMode(modes = {"Clear", "Rain", "Thunder"})
	public String weather = "Clear";

	@SerializeSetting(name = "Amount")
	@SettingSlider(min = 0d, max = 1d, increment = 0.1d)
	public double amount = 0.5d;

	@SerializeSetting(name = "Strength")
	@SettingSlider(min = 0d, max = 1d, increment = 0.1d)
	public double strength = 0.5d;

	@SerializeSetting(name = "Time")
	@SettingBoolean
	public static boolean timeToggle = false;

	@SerializeSetting(name = "Time")
	@SettingSlider(min = 0d, max = 24000d, increment = 1000d)
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
	public void onRender(final EventRender2D render2DEvent) {
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
