package gay.nns.client.impl.feature.render;

import gay.nns.client.api.setting.annotations.Serialize;
import gay.nns.client.impl.ui.clickgui.ClickGUI;
import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.ColorBox;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@FeatureInfo(name = "ClickGUI", category = FeatureCategory.RENDER)
public class FeatureClickGUI extends AbstractFeature {

	@Serialize(name = "ClickGUI_Color")
	@ColorBox
	public static Color color = new Color(94, 94, 128);

	private ClickGUI clickGUI;

	public FeatureClickGUI() {
		super();

		this.setKey(Keyboard.KEY_RSHIFT);
	}

	@Override
	public void onEnable() {
		if (this.clickGUI == null)
			this.clickGUI = new ClickGUI();

		mc.displayGuiScreen(clickGUI);
		this.toggle();
	}

	@Override
	public void onDisable() {

	}

}
