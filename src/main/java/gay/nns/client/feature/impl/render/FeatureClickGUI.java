package gay.nns.client.feature.impl.render;

import gay.nns.client.setting.api.annotations.SerializeSetting;
import gay.nns.client.ui.clickgui.GuiClick;
import gay.nns.client.feature.api.types.Feature;
import gay.nns.client.feature.api.enums.FeatureCategory;
import gay.nns.client.feature.api.interfaces.SerializeFeature;
import gay.nns.client.setting.api.annotations.SettingColor;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@SerializeFeature(name = "ClickGUI", category = FeatureCategory.RENDER)
public class FeatureClickGUI extends Feature {

	@SerializeSetting(name = "ClickGUI_Color")
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
