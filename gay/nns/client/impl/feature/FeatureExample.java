package gay.nns.client.impl.feature;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.*;
import gay.nns.client.impl.event.player.UpdateEvent;
import gay.nns.client.api.setting.annotations.*;

import java.awt.*;

@FeatureInfo(name = "ExampleFeature", description = "Example Feature", category = FeatureCategory.OTHER)
public class FeatureExample extends AbstractFeature {

	@Serialize(name = "Example_Color")
	@ColorBox
	public Color exampleColor = new Color(94, 94, 128);

	@Serialize(name = "Example_Mode")
	@Mode(modes = {"Example_Mode_1", "Example_Mode_2"})
	public String exampleMode = "Example_Mode_1";

	@Serialize(name = "Example_Slider")
	@Slider(min = 0d, max = 10d, increment = 1d)
	public double exampleSlider = 5d;

	@Serialize(name = "Example_Toggle")
	@CheckBox
	public boolean exampleToggle = false;

	public FeatureExample() {
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
	public void onUpdate(final UpdateEvent updateEvent) {
		// do something
	}

}
