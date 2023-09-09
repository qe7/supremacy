package gay.nns.client.impl.ui.clickgui;

import gay.nns.client.api.core.Core;
import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.setting.Setting;
import gay.nns.client.api.ui.clickgui.comp.Comp;
import gay.nns.client.impl.setting.SettingBoolean;
import gay.nns.client.impl.setting.SettingColor;
import gay.nns.client.impl.setting.SettingMode;
import gay.nns.client.impl.setting.SettingSlider;
import gay.nns.client.impl.ui.clickgui.comp.*;
import gay.nns.client.util.font.CustomFontRendererUtil;
import gay.nns.client.util.render.AnimateUtil;
import gay.nns.client.util.render.ScissorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiClick extends GuiScreen {

    // GUI position and dragging variables
    public float posX, posY, width, height, dragX, dragY;
    public boolean dragging;

    // Selected category and module variables
    public FeatureCategory selectedCategory;

    // Scroll variables
    private float scrollY;
    private final AnimateUtil scrollAnimate = new AnimateUtil(0, 1);

    // List of components (settings) to be displayed on the GUI
    public final List<Comp<?>> comps = new ArrayList<>();

    public GuiClick() {
        dragging = false;
        posX = getScaledRes().getScaledWidth() / 2f - 150;
        posY = getScaledRes().getScaledHeight() / 2f - 100;
        selectedCategory = FeatureCategory.COMBAT;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    // Draw the GUI on the screen
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        // Font renderers
        CustomFontRendererUtil roboto = Core.getSingleton().getFontUtil().getFont("clean");
        CustomFontRendererUtil robotoSmall = Core.getSingleton().getFontUtil().getFont("menu");
        CustomFontRendererUtil robotoSmallBold = Core.getSingleton().getFontUtil().getFont("menubold");

        // Dragging behavior
        if (dragging) {
            posX = mouseX - dragX;
            posY = mouseY - dragY;
        }

        // Clamp GUI position within screen bounds
        if (posX < 0) posX = 0;
        if (posY < 0) posY = 0;
        if (posX + 295 > getScaledRes().getScaledWidth()) posX = getScaledRes().getScaledWidth() - 295;
        if (posY + 300 > getScaledRes().getScaledHeight()) posY = getScaledRes().getScaledHeight() - 300;

        width = posX + 295;
        height = posY + 300;
        Gui.drawRect(posX - 1, posY - 1, width + 1, height + 1, new Color(0, 0, 0, 255).getRGB());
        Gui.drawRect((float) (posX - 0.5), (float) (posY - 0.5), (float) (width + 0.5), (float) (height + 0.5), new Color(48, 48, 49, 255).getRGB());
        Gui.drawRect(posX, posY, width, height, new Color(25, 25, 24, 255).getRGB());
        Gui.drawRect((float) (posX + 0.5), (float) (posY + 0.5), (float) (width - 0.5), (float) (height - 0.5), new Color(17, 18, 16, 255).getRGB());

        Gui.drawRect(posX, posY, width, posY + 20 + 1, new Color(25, 25, 24, 255).getRGB());
        Gui.drawRect(posX, posY, width, (float) (posY + 20 + 0.5), new Color(48, 48, 49, 255).getRGB());
        Gui.drawRect(posX, posY, width, posY + 20, new Color(13, 11, 11, 255).getRGB());

        float offset = 4;
        for (FeatureCategory category : FeatureCategory.values()) {
//            Gui.drawRect(posX + offset,posY,posX + robotoSmall.getStringWidth(category.getName()) + 3 + offset,posY + 20, new Color(0, 0, 0, 255).getRGB());
            if (category.equals(selectedCategory)) {
                Gui.drawRect((float) (posX + offset - 1 - 0.5), posY, (float) (posX + roboto.getStringWidth(category.getName()) + offset + 3 + 0.5), (float) (posY + 20 + 0.5), new Color(48, 48, 49, 255).getRGB());
                Gui.drawRect(posX + offset - 1, posY, (float) (posX + roboto.getStringWidth(category.getName()) + offset + 3), posY + 22, new Color(19, 19, 18, 255).getRGB());
                Gui.drawRect(posX + offset - 1, posY, (float) (posX + roboto.getStringWidth(category.getName()) + offset + 3), (float) (posY + 0.5), new Color(17, 18, 16, 255).getRGB());
            }
            roboto.drawStringWithShadow(category.getName(),posX + 1 + offset, posY + 9, category.equals(selectedCategory) ? new Color(162, 162, 161).getRGB() : new Color(79, 81, 82).getRGB());
            offset += (float) (roboto.getStringWidth(category.getName()) + 6);
        }

        Gui.drawRect(posX + 10 - 1, posY + 30 - 1, posX + 90 + 1, height - 10 + 1, new Color(0, 0, 0, 255).getRGB());
        Gui.drawRect((float) (posX + 10 - 0.5), (float) (posY + 30 - 0.5), (float) (posX + 90 + 0.5), (float) (height - 10 + 0.5), new Color(48, 48, 49, 255).getRGB());
        Gui.drawRect(posX + 10, posY + 30, posX + 90, height - 10, new Color(25, 25, 24, 255).getRGB());
        Gui.drawRect((float) (posX + 10 + 0.5), (float) (posY + 30 + 0.5), (float) (posX + 90 - 0.5), (float) (height - 10 - 0.5), new Color(19, 19, 18, 255).getRGB());
        robotoSmallBold.drawStringWithShadow("Modules", posX + 10 + 4, posY + 30 - 2, new Color(204, 204, 203, 255).getRGB());

        Gui.drawRect(posX + 100 - 1, posY + 30 - 1, width - 10 + 1, height - 10 + 1, new Color(0, 0, 0, 255).getRGB());
        Gui.drawRect((float) (posX + 100 - 0.5), (float) (posY + 30 - 0.5), (float) (width - 10 + 0.5), (float) (height - 10 + 0.5), new Color(48, 48, 49, 255).getRGB());
        Gui.drawRect(posX + 100, posY + 30, width - 10, height - 10, new Color(25, 25, 24, 255).getRGB());
        Gui.drawRect((float) (posX + 100 + 0.5), (float) (posY + 30 + 0.5), (float) (width - 10 - 0.5), (float) (height - 10 - 0.5), new Color(19, 19, 18, 255).getRGB());
        robotoSmallBold.drawStringWithShadow("Settings", posX + 100 + 4, posY + 30 - 2, new Color(204, 204, 203, 255).getRGB());

        offset = 32;
        for (Feature m : Core.getSingleton().getFeatureManager().getFeatureFromCategory(selectedCategory)) {
//            Gui.drawRect(posX + 12, posY + offset + 1, posX + 87, posY + 12 + offset,new Color(0, 0, 0, 255).getRGB());
            robotoSmall.drawStringWithShadow(m.getFeatureInfo().name().replace("_", " "),(int)posX + 14, (int)(posY + 5) + offset, m.isEnabled() ? new Color(162, 162, 161).getRGB() : new Color(79, 81, 82).getRGB());
            offset += 12;
        }

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        ScissorUtil.prepareScissorBox(posX + 100, posY + 35, width - 10, height - 10);

        float componentHeight = (float) comps.stream().mapToDouble(Comp::getHeight).sum();
        scrollY += ((float) Mouse.getDWheel() / 120) * 10;
        scrollY = MathHelper.clamp_float(scrollY, Math.min(-componentHeight + (295 - 50), 0), 0);
        scrollAnimate.interpolate(scrollY);
        offset = 40;

        for (Comp comp : comps) {
            comp.drawScreen(mouseX, mouseY, 175F, (float) (offset + scrollAnimate.getValue()));
            offset += comp.getHeight();
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

    }

    // Handle key presses
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        for (Comp comp : comps) {
            comp.keyTyped(typedChar, keyCode);
        }
    }

    // Handle mouse clicks
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        CustomFontRendererUtil roboto = Core.getSingleton().getFontUtil().getFont("clean");

        if (isInside(mouseX, mouseY, posX, posY + 20, width, height) && !isInside(mouseX, mouseY, posX + 10 - 1, posY + 30 - 1, posX + 90 + 1, height - 10 + 1) && !isInside(mouseX, mouseY, posX + 100 - 1, posY + 30 - 1, width - 10 + 1, height - 10 + 1) && mouseButton == 0) {
            dragging = true;
            dragX = mouseX - posX;
            dragY = mouseY - posY;
        }

        float offset = 4;
        for (FeatureCategory category : FeatureCategory.values()) {
            if (isInside(mouseX, mouseY, posX + offset, posY, posX + roboto.getStringWidth(category.getName()) + 3 + offset, posY + 20)) {
                comps.clear();
                selectedCategory = category;
            }
            offset += (float) (roboto.getStringWidth(category.getName()) + 6);
        }
        offset = 32;
        for (Feature m : Core.getSingleton().getFeatureManager().getFeatureFromCategory(selectedCategory)) {
            if (isInside(mouseX, mouseY, posX + 12, posY + offset + 1, posX + 87, posY + 12 + offset)) {
                if (mouseButton == 0 && !m.getFeatureInfo().name().equalsIgnoreCase("ClickGUI")) {
                    m.toggle();
                }
                if (mouseButton == 1) {
                    comps.clear();
                    scrollY = 0;
                    for (Setting<?, ?> setting : Core.getSingleton().getSettingManager().getSettingsFromType(m.getClass())) {
                        if (setting instanceof SettingMode settingMode) {
                            comps.add(new CompDropdown(this, m, settingMode));
                        }
                        if (setting instanceof SettingSlider settingSlider) {
                            comps.add(new CompSlider(this, m, settingSlider));
                        }
                        if (setting instanceof SettingBoolean settingCheckBox) {
                            comps.add(new CompCheckBox(this, m, settingCheckBox));
                        }
                        if (setting instanceof SettingColor settingColor) {
                            comps.add(new CompColorPicker(this, m, settingColor));
                        }
                    }
                }
            }
            offset += 12;
        }

        offset = 40;
        for (Comp comp : comps) {
            comp.mouseClicked(mouseX, mouseY, mouseButton, 175F, (float) (offset + scrollAnimate.getValue()));
            offset += comp.getHeight();
        }
    }

    // Handle mouse releases
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        dragging = false;
        for (Comp comp : comps) {
            comp.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        dragging = false;
    }

    // Check if a given point is inside a specified area
    public boolean isInside(int mouseX, int mouseY, double x, double y, double x2, double y2) {
        return (mouseX > x && mouseX < x2) && (mouseY > y && mouseY < y2);
    }

    // Get the scaled resolution of the screen
    public ScaledResolution getScaledRes() {
        return new ScaledResolution(Minecraft.getMinecraft());
    }

}
