package gay.nns.client.impl.feature.render;

import gay.nns.client.api.core.Core;
import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.*;
import gay.nns.client.impl.event.game.KeyEvent;
import gay.nns.client.impl.event.render.Render2DEvent;
import gay.nns.client.util.player.MovementUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import oshi.util.FormatUtil;

import java.awt.*;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

@FeatureInfo(name = "Interface", description = "HUD", category = FeatureCategory.RENDER)
public class FeatureInterface extends AbstractFeature {

    @Serialize(name = "Arraylist_Color")
    @ColorBox
    public Color color = new Color(255, 255, 255);

    @Serialize(name = "Background_Color")
    @ColorBox
    public Color backgroundColor = new Color(0, 0, 0, 100);

    @Serialize(name = "Color_Mode")
    @Mode(modes = {"Default", "Category", "Rainbow", "Astolfo"})
    public String colorMode = "Default";

    @Serialize(name = "Outline_Mode")
    @Mode(modes = {"Left", "Right", "Top", "Bottom", "All", "None"})
    public String arraylistMode = "All";

    @Serialize(name = "Suffix_Mode")
    @Mode(modes = {"Parenthesis", "Squared_Brackets", "Hyphen", "Space", "None"})
    public String suffixMode = "Parenthesis";

    @Serialize(name = "Info")
    @CheckBox
    public boolean info = false;

    @Serialize(name = "Arraylist_Brightness")
    @Slider(min = 0, max = 1, increment = 0.05)
    public double brightness = 0.7;

    @Serialize(name = "Arraylist_Saturation")
    @Slider(min = 0, max = 1, increment = 0.05)
    public double saturation = 0.5;

    @Serialize(name = "Offset")
    @Slider(min = 0, max = 10, increment = 1)
    public double offset = 1;

    @Serialize(name = "Spacing")
    @Slider(min = 0, max = 10, increment = 1)
    public double spacing = 2;

    @Serialize(name = "Background")
    @CheckBox
    public boolean background = true;

    public String coordinates;
    public String bps;

    public FeatureInterface() {
        this.toggle();
    }

    @Subscribe
    public void onRender2D(final Render2DEvent render2DEvent) {
        if (mc.theWorld == null) return;
        if (mc.thePlayer == null) return;

        this.setSuffix("Default");

        List<AbstractFeature> featureList = new ArrayList<>(Core.getSingleton().getFeatureManager().getEnabledFeatures());
        ScaledResolution sr = render2DEvent.scaledResolution();
        FontRenderer fr = mc.fontRendererObj;

        String time = new SimpleDateFormat("hh:mm a").format(new Date());
        if (time.startsWith("0")) {
            time = time.replaceFirst("0", "");
        }
        String watermark = Core.getSingleton().getName() + " §w" + Core.getSingleton().getVersion() + " §7(" + time.toUpperCase() + ")";

        fr.drawStringWithShadow(watermark, 2.f, 2.f, getColor().getRGB());

        coordinates = (int) mc.thePlayer.posX + ", " + (int) mc.thePlayer.posY + ", " + (int) mc.thePlayer.posZ;

        bps = String.valueOf(Math.round(MovementUtil.getBPS() * 100.0) / 100.0);

        int infoOffset = fr.FONT_HEIGHT - 1;
        if (info) {
            fr.drawStringWithShadow("XYZ: §7" + coordinates , 2, sr.getScaledHeight() - infoOffset, getColor().getRGB());
            fr.drawStringWithShadow("FPS: §7" + Minecraft.getDebugFPS(), 2, sr.getScaledHeight() - infoOffset - fr.FONT_HEIGHT, getColor().getRGB());
            fr.drawStringWithShadow("BPS: §7" + bps, 2, sr.getScaledHeight() - infoOffset - fr.FONT_HEIGHT * 2, getColor().getRGB());

            List<PotionEffect> potions = new ArrayList<>(mc.thePlayer.getActivePotionEffects());
            potions.sort(Comparator.comparingInt(m -> fr.getStringWidth("§7" + m.getEffectName() + " " + m.getAmplifier() + " : " + m.getDuration())));

            int timeOffset = 1;
            for (PotionEffect potion : potions) {
//                fr.drawStringWithShadow(potion.getEffectName(), sr.getScaledWidth() - fr.getStringWidth(potion.getEffectName()) - infoOffset, sr.getScaledHeight() - infoOffset - timeOffset, getColor().getRGB());
                timeOffset += fr.FONT_HEIGHT;
            }
        }

        switch (suffixMode) {
            case "Parenthesis" -> featureList.sort(Comparator.comparingInt(m -> fr.getStringWidth(m.getSuffix() != null ? m.getFeatureInfo().name() + " §7(" + m.getSuffix() + ")" : m.getFeatureInfo().name())));
            case "Squared_Brackets" -> featureList.sort(Comparator.comparingInt(m -> fr.getStringWidth(m.getSuffix() != null ? m.getFeatureInfo().name() + " §7[" + m.getSuffix() + "]" : m.getFeatureInfo().name())));
            case "Hyphen" -> featureList.sort(Comparator.comparingInt(m -> fr.getStringWidth(m.getSuffix() != null ? m.getFeatureInfo().name() + " §7- " + m.getSuffix() : m.getFeatureInfo().name())));
            case "Space" -> featureList.sort(Comparator.comparingInt(m -> fr.getStringWidth(m.getSuffix() != null ? m.getFeatureInfo().name() + " §7" + m.getSuffix() : m.getFeatureInfo().name())));
            case "None" -> featureList.sort(Comparator.comparingInt(m -> fr.getStringWidth(m.getFeatureInfo().name())));
        }
        Collections.reverse(featureList);

        double lastLength = 0;
        double y = offset;
        for (AbstractFeature feature : featureList) {
            if (feature.isEnabled()) {
                String arraylist = getString(feature);

                if (background)
                    Gui.drawRect((float) (sr.getScaledWidth() - fr.getStringWidth(arraylist) - offset - 1), (float) (y - 1), (float) ((float) sr.getScaledWidth() - offset + 1), (float) (y + fr.FONT_HEIGHT + spacing - 1), new Color(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue(), backgroundColor.getAlpha()).getRGB());

                switch (arraylistMode.toLowerCase()) {
                    case "left" ->
                            Gui.drawRect((float) (sr.getScaledWidth() - fr.getStringWidth(arraylist) - offset - 2), (float) (y - 1), (float) (sr.getScaledWidth() - fr.getStringWidth(arraylist) - offset - 1), (float) (y + fr.FONT_HEIGHT + spacing - 1), getColor(feature, (int) y * 10).getRGB());
                    case "right" ->
                            Gui.drawRect((float) (sr.getScaledWidth() - offset + 1), (float) (y - 1), (float) ((float) sr.getScaledWidth() - offset + 2), (float) (y + fr.FONT_HEIGHT + spacing - 1), getColor(feature, (int) y * 10).getRGB());
                    case "top" -> {
                        if (featureList.indexOf(feature) == 0)
                            Gui.drawRect((float) (sr.getScaledWidth() - fr.getStringWidth(arraylist) - offset - 1), (float) (y - 2), (float) ((float) sr.getScaledWidth() - offset + 1), (float) (y - 1), getColor(feature, (int) y * 10).getRGB());
                    }
                    case "bottom" -> {
                        if (featureList.indexOf(feature) == featureList.size() - 1)
                            Gui.drawRect((float) (sr.getScaledWidth() - fr.getStringWidth(arraylist) - offset - 1), (float) (y + fr.FONT_HEIGHT + spacing - 1), (float) ((float) sr.getScaledWidth() - offset + 1), (float) (y + fr.FONT_HEIGHT + spacing), getColor(feature, (int) y * 10).getRGB());
                    }
                    case "all" -> {
                        // top
                        if (featureList.indexOf(feature) == 0)
                            Gui.drawRect((float) (sr.getScaledWidth() - fr.getStringWidth(arraylist) - offset - 2), (float) (y - 2), (float) ((float) sr.getScaledWidth() - offset + 2), (float) (y - 1), getColor(feature, (int) y * 10).getRGB());
                        // bottom
                        if (featureList.indexOf(feature) == featureList.size() - 1)
                            Gui.drawRect((float) (sr.getScaledWidth() - fr.getStringWidth(arraylist) - offset - 2), (float) (y + fr.FONT_HEIGHT + spacing - 1), (float) ((float) sr.getScaledWidth() - offset + 2), (float) (y + fr.FONT_HEIGHT + spacing), getColor(feature, (int) y * 10).getRGB());
                        // left
                        Gui.drawRect((float) (sr.getScaledWidth() - fr.getStringWidth(arraylist) - offset - 2), (float) (y - 1), (float) (sr.getScaledWidth() - fr.getStringWidth(arraylist) - offset - 1), (float) (y + fr.FONT_HEIGHT + spacing - 1), getColor(feature, (int) y * 10).getRGB());
                        // right
                        Gui.drawRect((float) (sr.getScaledWidth() - offset + 1), (float) (y - 1), (float) ((float) sr.getScaledWidth() - offset + 2), (float) (y + fr.FONT_HEIGHT + spacing - 1), getColor(feature, (int) y * 10).getRGB());
                        // middle? ig lol idfk
                        if (featureList.indexOf(feature) != 0)
                            Gui.drawRect((float) (sr.getScaledWidth() - lastLength - offset - 2), (float) (y - 1), (float) (sr.getScaledWidth() - fr.getStringWidth(arraylist) - offset - 1), (float) (y), getColor(feature, (int) y * 10).getRGB());
                    }
                }

                fr.drawStringWithShadow(arraylist, (float) (sr.getScaledWidth() - fr.getStringWidth(arraylist) - offset), (float) ((float) y + (spacing / 2)), getColor(feature, (int) y * 10).getRGB());
                lastLength = fr.getStringWidth(arraylist);
                y += fr.FONT_HEIGHT + spacing;
            }
        }
    }

    private String getString(AbstractFeature feature) {
        String arraylist = feature.getFeatureInfo().name();

        if (feature.getSuffix() != null) {
            switch (suffixMode) {
                case "Parenthesis" -> arraylist += " §7(" + feature.getSuffix() + ")";
                case "Squared_Brackets" -> arraylist += " §7[" + feature.getSuffix() + "]";
                case "Hyphen" -> arraylist += " §7- " + feature.getSuffix();
                case "Space" -> arraylist += " §7" + feature.getSuffix();
                case "None" -> arraylist += "";
            }
        }

        return arraylist;
    }

    @Subscribe
    public void onKey(final KeyEvent keyEvent) {
        // todo: handle TabUI key inputs
    }

    private Color getColor(AbstractFeature feature, int index) {
        switch (colorMode) {
            case "Default" -> {
                return color;
            }
            case "Category" -> {
                return feature.getFeatureInfo().category().getColor();
            }
            case "Rainbow" -> {
                return generateRainbowColor(index);
            }
            case "Astolfo" -> {
                return generateAstolfoColor(index);
            }
        }
        return Color.white;
    }

    private Color getColor() {
        switch (colorMode) {
            case "Default" -> {
                return color;
            }
            case "Category" -> {
                return Color.white;
            }
            case "Rainbow" -> {
                return generateRainbowColor(1);
            }
            case "Astolfo" -> {
                return generateAstolfoColor(1);
            }
        }
        return Color.white;
    }

    private Color generateRainbowColor(int i) {
        float hue = (System.currentTimeMillis() + i) % 4000;
        hue /= 4000f;
        return Color.getHSBColor(hue, (float) (saturation), (float) (brightness));
    }

    private Color generateAstolfoColor(int i) {
        float speed = 4000f;
        float hue = (float) (System.currentTimeMillis() % 4000) + (i);
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5f - (hue - 0.5f);
        }
        hue += 0.5f;
        return Color.getHSBColor(hue, (float) (saturation), (float) (brightness));
    }

}
