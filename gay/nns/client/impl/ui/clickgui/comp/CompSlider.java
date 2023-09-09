package gay.nns.client.impl.ui.clickgui.comp;

import gay.nns.client.api.ui.clickgui.comp.Comp;
import gay.nns.client.impl.setting.SettingSlider;
import gay.nns.client.api.core.Core;
import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.impl.feature.render.FeatureClickGUI;
import gay.nns.client.impl.ui.clickgui.GuiClick;
import gay.nns.client.util.font.CustomFontRendererUtil;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CompSlider extends Comp<SettingSlider> {

    private boolean dragging = false;
    private double renderWidth;
    private double renderWidth2;

    public CompSlider(GuiClick parent, AbstractFeature module, SettingSlider settingSlider) {
        super(parent, module, settingSlider);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton, float x, float y) {
        if (isInside(mouseX, mouseY, clickGUI.posX + x - 58 + 0.5f, clickGUI.posY + y + 8 + 0.5f, (float) (clickGUI.posX + x - 58 + renderWidth2) - 0.5f, clickGUI.posY + y + 13 - 0.5f) && mouseButton == 0) {
            dragging = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        dragging = false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float x, float y) {

        CustomFontRendererUtil roboto = Core.getSingleton().getFontUtil().getFont("menu");
        CustomFontRendererUtil robotoSmallBold = Core.getSingleton().getFontUtil().getFont("menubold");

        double min = setting.min();
        double max = setting.max();
        double l = 150;

        renderWidth = (l) * (setting.getValue() - min) / (max - min);
        renderWidth2 = (l) * (setting.max() - min) / (max - min);

        double diff = Math.min(l, Math.max(0, mouseX - (clickGUI.posX + x - 58)));
        if (dragging) {
            if (diff == 0) {
                setting.setValue(setting.min());
            }
            else {
                double newValue = roundToIncrement(((diff / l) * (max - min) + min), setting.increment());
                newValue = roundToPlace(newValue);
                if (newValue > setting.max()) {
                    newValue = setting.max();
                }
                else if (newValue < setting.min()) {
                    newValue = setting.min();
                }
                setting.setValue(newValue);
            }
        }

        Gui.drawRect(clickGUI.posX + x - 58, clickGUI.posY + y + 8, (float) (clickGUI.posX + x - 58 + renderWidth2), clickGUI.posY + y + 13, new Color(4, 4, 5).getRGB());
        Gui.drawGradientRect(clickGUI.posX + x - 58 + 0.5f, clickGUI.posY + y + 8 + 0.5f, (float) (clickGUI.posX + x - 58 + renderWidth2) - 0.5f, clickGUI.posY + y + 13 - 0.5f, new Color(42, 42, 43).getRGB(), new Color(42, 42, 43).darker().getRGB());
        Gui.drawGradientRect(clickGUI.posX + x - 58 + 0.5f, clickGUI.posY + y + 8 + 0.5f, (float) (clickGUI.posX + x - 58 + renderWidth) - 0.5f, clickGUI.posY + y + 13 - 0.5f, FeatureClickGUI.color.getRGB(), FeatureClickGUI.color.darker().getRGB());
        roboto.drawStringWithShadow(setting.getName().replace("_", " "),(int)(clickGUI.posX + x - 58),(int)(clickGUI.posY + y), new Color(162, 162, 161).getRGB());
        robotoSmallBold.drawStringWithShadow(setting.getValue() + "",(int)(clickGUI.posX + x - 60 + renderWidth),(int)(clickGUI.posY + y + 10), new Color(162, 162, 161).getRGB());
    }

    private double roundToPlace(double value) {
		BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private double roundToIncrement(double value, double increment) {
        return Math.round(value / increment) * increment;
    }

    @Override
    public float getHeight() {
        return 18;
    }

}
