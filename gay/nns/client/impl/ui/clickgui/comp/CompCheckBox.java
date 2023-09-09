package gay.nns.client.impl.ui.clickgui.comp;

import gay.nns.client.api.ui.clickgui.comp.Comp;
import gay.nns.client.impl.setting.SettingBoolean;
import gay.nns.client.api.core.Core;
import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.impl.feature.render.FeatureClickGUI;
import gay.nns.client.impl.ui.clickgui.GuiClick;
import gay.nns.client.util.font.CustomFontRendererUtil;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class CompCheckBox extends Comp<SettingBoolean> {

    public CompCheckBox(GuiClick parent, AbstractFeature module, SettingBoolean settingCheckBox) {
        super(parent, module, settingCheckBox);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float x, float y) {

        CustomFontRendererUtil roboto = Core.getSingleton().getFontUtil().getFont("menu");

        roboto.drawStringWithShadow(setting.getName().replace("_", " "), (int)(clickGUI.posX + x - 58), (int)(clickGUI.posY + y + 1), new Color(162, 162, 161).getRGB());

        Gui.drawRect(clickGUI.posX + x - 58 + 150 - 5, clickGUI.posY + y, clickGUI.posX + x - 58 + 150 - 5 + 5, clickGUI.posY + y + 5, new Color(4, 4, 5).getRGB());

        if (setting.getValue())
            Gui.drawGradientRect(clickGUI.posX + x - 58 + 150 - 5 + 0.5f, clickGUI.posY + y + 0.5f, clickGUI.posX + x - 58 + 150 - 5 + 5 - 0.5f, clickGUI.posY + y + 5 - 0.5f, FeatureClickGUI.color.getRGB(), FeatureClickGUI.color.darker().getRGB());
        else
            Gui.drawGradientRect(clickGUI.posX + x - 58 + 150 - 5 + 0.5f, clickGUI.posY + y + 0.5f, clickGUI.posX + x - 58 + 150 - 5 + 5 - 0.5f, clickGUI.posY + y + 5 - 0.5f, new Color(42, 42, 43).getRGB(), new Color(42, 42, 43).darker().getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton, float x, float y) {
        if (isInside(mouseX, mouseY, clickGUI.posX + x - 58 + 150 - 5, clickGUI.posY + y, clickGUI.posX + x - 58 + 150 - 5 + 5, clickGUI.posY + y + 5) && mouseButton == 0) {
            setting.setValue(!setting.getValue());
        }
    }

    @Override
    public float getHeight() {
        return 12;
    }

}
