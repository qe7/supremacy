package gay.nns.client.impl.feature.other;

import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.impl.ui.snakegui.GuiSnake;

@FeatureInfo(name = "Snake", description = "Snake!")
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
