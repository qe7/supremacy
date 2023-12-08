package gay.nns.client.ui.clickgui.comp;

import gay.nns.client.feature.api.types.Feature;
import gay.nns.client.setting.impl.SettingMode;
import gay.nns.client.ui.clickgui.GuiClick;
import gay.nns.client.util.font.UtilCustomFontRenderer;
import gay.nns.client.util.font.UtilFont;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class CompDropdown extends Comp<SettingMode> {

    public boolean state = false;

    private float dropdownHeight;

    public CompDropdown(GuiClick parent, Feature module, SettingMode settingMode) {
        super(parent, module, settingMode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton, float x, float y) {
        super.mouseClicked(mouseX, mouseY, mouseButton, x, y);

        if (isInside(mouseX, mouseY, clickGUI.posX + x - 58, clickGUI.posY + y + 8, clickGUI.posX + x - 58 + 150, clickGUI.posY + y + 8 + 14) && mouseButton == 0) {
            state = !state;
        }

        if (state) {
            for (int i = 0; i < setting.getModes().size(); i++) {
                if (isInside(mouseX, mouseY, clickGUI.posX + x - 58, clickGUI.posY + y + 8 + 14 + (i * 14), clickGUI.posX + x - 58 + 150, clickGUI.posY + y + 8 + 14 + (i * 14) + 14) && mouseButton == 0) {
                    setting.setValue(setting.getModes().get(i));
                }
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float x, float y) {
        super.drawScreen(mouseX, mouseY, x, y);

        UtilCustomFontRenderer roboto = UtilFont.menuFont;
        UtilCustomFontRenderer robotoB = UtilFont.menuFontBold;

        roboto.drawStringWithShadow(setting.getName().replace("_", " "), clickGUI.posX + x - 58, clickGUI.posY + y + 1, new Color(162, 162, 161).getRGB());

        if (state) {
            Gui.drawRect(clickGUI.posX + x - 58, clickGUI.posY + y + 8, clickGUI.posX + x - 58 + 150, clickGUI.posY + y + 8 + 14 + (dropdownHeight + 0.5f), new Color(4, 4, 5).getRGB());
            Gui.drawGradientRect(clickGUI.posX + x - 58 + 0.5f, clickGUI.posY + y + 8 + 0.5f, clickGUI.posX + x - 58 + 150 - 0.5f, clickGUI.posY + y + 8 + 14 + (dropdownHeight), new Color(42, 42, 43).getRGB(), new Color(42, 42, 43).darker().getRGB());
            roboto.drawStringWithShadow("Close", clickGUI.posX + x - 58 + 4, clickGUI.posY + y + 8 + 5, new Color(162, 162, 161).getRGB());

            for (int i = 0; i < setting.getModes().size(); i++) {
                String s = setting.getModes().get(i);

                if (setting.getValue().equals(s))
                    robotoB.drawStringWithShadow(s.replace("_", " "), clickGUI.posX + x - 58 + 4, clickGUI.posY + y + 8 + 14 + (i * 14) + 5, new Color(162, 162, 161).getRGB());
                else
                    roboto.drawStringWithShadow(s.replace("_", " "), clickGUI.posX + x - 58 + 4, clickGUI.posY + y + 8 + 14 + (i * 14) + 5, new Color(162, 162, 161).getRGB());

                dropdownHeight = 14 * (i + 1);
            }
        } else {
            Gui.drawRect(clickGUI.posX + x - 58, clickGUI.posY + y + 8, clickGUI.posX + x - 58 + 150, clickGUI.posY + y + 8 + 14, new Color(4, 4, 5).getRGB());
            Gui.drawGradientRect(clickGUI.posX + x - 58 + 0.5f, clickGUI.posY + y + 8 + 0.5f, clickGUI.posX + x - 58 + 150 - 0.5f, clickGUI.posY + y + 8 + 14 - 0.5f, new Color(42, 42, 43).getRGB(), new Color(42, 42, 43).darker().getRGB());
            robotoB.drawStringWithShadow(setting.getValue(), clickGUI.posX + x - 58 + 4, clickGUI.posY + y + 8 + 5, new Color(162, 162, 161).getRGB());
        }
    }

    @Override
    public float getHeight() {
        if (state) {
            return 26 + 2 + (14 * setting.getModes().size());
        }
        return 26;
    }

}
