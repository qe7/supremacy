package gay.nns.client.feature.impl.other;

import gay.nns.client.feature.api.types.Feature;
import gay.nns.client.feature.api.interfaces.SerializeFeature;
import gay.nns.client.ui.snakegui.GuiSnake;

@SerializeFeature(name = "Snake", description = "Snake!")
public class FeatureSnake extends Feature {

	private GuiSnake guiSnake;

	public FeatureSnake() {
		super();
	}

	@Override
	public void onEnable() {
		super.onEnable();
		if (guiSnake == null) {
			guiSnake = new GuiSnake();
		}
		mc.displayGuiScreen(guiSnake);
		this.toggle();
	}

}
