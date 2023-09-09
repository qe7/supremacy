package gay.nns.client.impl.feature.render;

import gay.nns.client.api.setting.annotations.Serialize;
import gay.nns.client.impl.ui.clickgui.GuiClick;
import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.SettingColor;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@FeatureInfo(name = "ClickGUI", category = FeatureCategory.RENDER)
public class FeatureClickGUI extends Feature {

	@Serialize(name = "ClickGUI_Color")
	@SettingColor
	public static Color color = new Color(94, 94, 128);

	private GuiClick clickGUI;

	public FeatureClickGUI() {
		super();

		this.setKey(Keyboard.KEY_RSHIFT);
	}

	@Override
	public void onEnable() {
		if (this.clickGUI == null)
			this.clickGUI = new GuiClick();

		mc.displayGuiScreen(clickGUI);
		this.toggle();
	}

	@Override
	public void onDisable() {

	}

}
