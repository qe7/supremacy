package gay.nns.client.impl.ui.clickgui.comp;

import gay.nns.client.api.core.SupremacyCore;
import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.ui.clickgui.comp.Comp;
import gay.nns.client.impl.setting.SettingColor;
import gay.nns.client.impl.ui.clickgui.GuiClick;
import gay.nns.client.util.font.UtilCustomFontRenderer;
import gay.nns.client.util.font.UtilFont;
import net.minecraft.client.gui.Gui;

import java.awt.*;

/**
 * @Author Shae
 * A color picker component
 */
public class CompColorPicker extends Comp<SettingColor> {

	private boolean state; // if the color box is enabled or not
	private boolean dragging_hue; // if the color should be being set
	private boolean dragging_alpha; // if the alpha should be being set
	private boolean dragging_main; // if the saturation should be being set

	private float hue; // the hue of the color

	public CompColorPicker(GuiClick clickGUI, Feature abstractFeature, SettingColor setting) {
		super(clickGUI, abstractFeature, setting);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton, float x, float y) {
		super.mouseClicked(mouseX, mouseY, mouseButton, x, y);

		if (isInside(mouseX, mouseY, clickGUI.posX + x - 58 + 150 - 20, clickGUI.posY + y - 2, clickGUI.posX + x - 58 + 150 - 4 + 4, clickGUI.posY + y + 4 + 2) && mouseButton == 0) {
			state = !state;
		}

		if (state) {
			if (isInside(mouseX, mouseY, clickGUI.posX + x - 58, clickGUI.posY + y + 8, clickGUI.posX + x - 58 + 150, clickGUI.posY + y + 8 + 8) && mouseButton == 0) {
				dragging_hue = true;
			}
			if (isInside(mouseX, mouseY, clickGUI.posX + x - 58, clickGUI.posY + y + 16 + 2, clickGUI.posX + x - 58 + 150, clickGUI.posY + y + 16 + 8 + 2) && mouseButton == 0) {
				dragging_alpha = true;
			}
			if (isInside(mouseX, mouseY, clickGUI.posX + x - 58, clickGUI.posY + y + 24 + 4, clickGUI.posX + x - 58 + 150, clickGUI.posY + y + 24 + 80 + 4) && mouseButton == 0) {
				dragging_main = true;
			}
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);

		dragging_hue = false;
		dragging_alpha = false;
		dragging_main = false;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float x, float y) {
		super.drawScreen(mouseX, mouseY, x, y);

		UtilCustomFontRenderer roboto = UtilFont.clean;

		roboto.drawStringWithShadow(setting.getName().replace("_", " "), (int) (clickGUI.posX + x - 58), (int) (clickGUI.posY + y + 1), new Color(162, 162, 161).getRGB());

		Gui.drawRect(clickGUI.posX + x - 58 + 150 - 20, clickGUI.posY + y - 2, clickGUI.posX + x - 58 + 150 - 4 + 4, clickGUI.posY + y + 4 + 2, new Color(4, 4, 5).getRGB());
		Gui.drawGradientRect(clickGUI.posX + x - 58 + 150 - 20 + 0.5f, clickGUI.posY + y - 2 + 0.5f, clickGUI.posX + x - 58 + 150 - 4 + 4 - 0.5f, clickGUI.posY + y + 4 + 2 - 0.5f, setting.getValue().getRGB(), setting.getValue().darker().getRGB());

		float offset = 2f;
		if (state) {

			if (dragging_hue) {
				if (!isInside(mouseX, mouseY, clickGUI.posX + x - 58, clickGUI.posY + y + 8, clickGUI.posX + x - 58 + 150, clickGUI.posY + y + 8 + 8))
					dragging_hue = false;

				hue = (mouseX - (clickGUI.posX + x - 58)) / 150f;
				if (hue < 0f) hue = 0f;
				if (hue > 1f) hue = 1f;
			}

			if (dragging_alpha) {
				if (!isInside(mouseX, mouseY, clickGUI.posX + x - 58, clickGUI.posY + y + 16 + offset, clickGUI.posX + x - 58 + 150, clickGUI.posY + y + 16 + offset + 8))
					dragging_alpha = false;

				if (mouseX - (clickGUI.posX + x - 58) < 0)
					setting.setValue(new Color(setting.getValue().getRed(), setting.getValue().getGreen(), setting.getValue().getBlue(), 0));
				else if (mouseX - (clickGUI.posX + x - 58) > 150)
					setting.setValue(new Color(setting.getValue().getRed(), setting.getValue().getGreen(), setting.getValue().getBlue(), 255));

				if (isInside(mouseX, mouseY, clickGUI.posX + x - 58, clickGUI.posY + y + 16 + offset, clickGUI.posX + x - 58 + 150, clickGUI.posY + y + 16 + offset + 8))
					setting.setValue(new Color(setting.getValue().getRed(), setting.getValue().getGreen(), setting.getValue().getBlue(), (int) ((mouseX - (clickGUI.posX + x - 58)) / 150f * 255f)));
			}

			if (dragging_main) {
				if (!isInside(mouseX, mouseY, clickGUI.posX + x - 58, clickGUI.posY + y + 24 + (offset * 2), clickGUI.posX + x - 58 + 150, clickGUI.posY + y + 24 + (offset * 2) + (80 - 24 + (offset * 2))))
					dragging_main = false;

//				setting.setValue(Color.getHSBColor(hue, (mouseX - (clickGUI.posX + x - 58)) / 150f, 0f + ((mouseY - (clickGUI.posY + y + 24 + (offset * 2))) / (80 - 24 + (offset * 2)))));
				final Color color = Color.getHSBColor(hue, (mouseX - (clickGUI.posX + x - 58)) / 150f, 0f + ((mouseY - (clickGUI.posY + y + 24 + (offset * 2))) / (80 - 24 + (offset * 2))));
				setting.setValue(new Color(color.getRed(), color.getGreen(), color.getBlue(), setting.getValue().getAlpha()));
			}

			// hue
			Gui.drawRect(clickGUI.posX + x - 58, clickGUI.posY + y + 8, clickGUI.posX + x - 58 + 150, clickGUI.posY + y + 8 + 8, new Color(4, 4, 5).getRGB());

//			Gui.drawRect(clickGUI.posX + x - 58 + 0.5f, clickGUI.posY + y + 8 + 0.5f, clickGUI.posX + x - 58 + 150 - 0.5f, clickGUI.posY + y + 8 + 8 - 0.5f, -1);
			// render the hue bar
			for (int i = 0; i < 150; i++) {
				float hue = (float) i / 150f;
				int color = Color.HSBtoRGB(hue, 1f, 1f);
				Gui.drawRect(clickGUI.posX + x - 58 + i, clickGUI.posY + y + 8, clickGUI.posX + x - 58 + i + 1, clickGUI.posY + y + 8 + 8, color);
			}

			// alpha
			Gui.drawRect(clickGUI.posX + x - 58, clickGUI.posY + y + 16 + offset, clickGUI.posX + x - 58 + 150, clickGUI.posY + y + 16 + offset + 8, new Color(4, 4, 5).getRGB());

//			Gui.drawRect(clickGUI.posX + x - 58 + 0.5f, clickGUI.posY + y + 16 + offset + 0.5f, clickGUI.posX + x - 58 + 150 - 0.5f, clickGUI.posY + y + 16 + offset + 8 - 0.5f, -1);
			// render the alpha bar
			for (int i = 0; i < 150; i++) {
				float alpha = (float) i / 150f;
				int color = new Color(255, 255, 255, (int) (alpha * 255)).getRGB();
				Gui.drawRect(clickGUI.posX + x - 58 + i, clickGUI.posY + y + 16 + offset, clickGUI.posX + x - 58 + i + 1, clickGUI.posY + y + 16 + offset + 8, color);
			}

			// saturation/brightness
			Gui.drawRect(clickGUI.posX + x - 58, clickGUI.posY + y + 24 + (offset * 2), clickGUI.posX + x - 58 + 150, clickGUI.posY + y + 24 + (offset * 2) + (80 - 24 + (offset * 2)), new Color(4, 4, 5).getRGB());

			Gui.drawRect(clickGUI.posX + x - 58 + 0.5f, clickGUI.posY + y + 24 + (offset * 2) + 0.5f, clickGUI.posX + x - 58 + 150 - 0.5f, clickGUI.posY + y + 24 + (offset * 2) + (80 - 24 + (offset * 2)) - 0.5f, setting.getValue().getRGB());

			// render the saturation/brightness over the current setting color
			for (int i = 0; i < 150; i++) {
				for (int j = 0; j < 80 - 24 + (offset * 2); j++) {
					float saturation = (float) i / 150f;
					float brightness = (float) j / (80 - 24 + (offset * 2));
					int color = Color.HSBtoRGB(hue, saturation, brightness);
					Gui.drawRect(clickGUI.posX + x - 58 + i, clickGUI.posY + y + 24 + (offset * 2) + j, clickGUI.posX + x - 58 + i + 1, clickGUI.posY + y + 24 + (offset * 2) + j + 1, color);
				}
			}
		}
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	public float getHeight() {
		if (state) return 12 + 80;
		return 12;
	}

}
