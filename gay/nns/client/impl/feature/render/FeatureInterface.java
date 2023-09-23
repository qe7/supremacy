package gay.nns.client.impl.feature.render;

import gay.nns.client.api.core.SupremacyCore;
import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.SerializeFeature;
import gay.nns.client.api.setting.annotations.*;
import gay.nns.client.impl.event.game.EventKeyInput;
import gay.nns.client.impl.event.render.EventRender2D;
import gay.nns.client.util.player.UtilMovement;
import gay.nns.client.util.render.UtilColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.potion.PotionEffect;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

@SerializeFeature(name = "Interface", description = "HUD", category = FeatureCategory.RENDER)
public class FeatureInterface extends Feature {

    @SerializeSetting(name = "Interface_Color")
    @SettingColor
    public static Color color = new Color(255, 255, 255);
    @SerializeSetting(name = "Background_Color")
    @SettingColor
    public Color backgroundColor = new Color(0, 0, 0, 100);

    @SerializeSetting(name = "Color_Mode")
    @SettingMode(modes = {"Default", "Category", "Rainbow", "Astolfo"})
    public static String colorMode = "Default";

    @SerializeSetting(name = "Outline_Mode")
    @SettingMode(modes = {"Left", "Right", "Top", "Bottom", "All", "None"})
    public String arraylistMode = "All";

    @SerializeSetting(name = "Suffix_Mode")
    @SettingMode(modes = {"Parenthesis", "Squared_Brackets", "Hyphen", "Standard", "None"})
    public String suffixMode = "Parenthesis";

    @SerializeSetting(name = "Arraylist_Brightness")
    @SettingSlider(min = 0, max = 1, increment = 0.05)
    public static double brightness = 0.7;

    @SerializeSetting(name = "Arraylist_Saturation")
    @SettingSlider(min = 0, max = 1, increment = 0.05)
    public static double saturation = 0.5;

    @SerializeSetting(name = "Offset")
    @SettingSlider(min = 0, max = 10, increment = 1)
    public double offset = 1;

    @SerializeSetting(name = "Spacing")
    @SettingSlider(min = 0, max = 10, increment = 1)
    public double spacing = 2;

    @SerializeSetting(name = "Background")
    @SettingBoolean
    public boolean background = true;

    @SerializeSetting(name = "Info")
    @SettingBoolean
    public boolean info = false;

    public String coordinates;
    public String bps;

    public FeatureInterface() {
        this.toggle();
    }

    @Subscribe
    public void onRender2D(final EventRender2D render2DEvent) {
        if (mc.theWorld == null) return;
        if (mc.thePlayer == null) return;

        this.setSuffix("Default");

        List<Feature> featureList = new ArrayList<>(SupremacyCore.getSingleton().getFeatureManager().getEnabledFeatures());
        ScaledResolution sr = render2DEvent.scaledResolution();
        FontRenderer fr = mc.fontRendererObj;

        String time = new SimpleDateFormat("hh:mm a").format(new Date());
        if (time.startsWith("0")) {
            time = time.replaceFirst("0", "");
        }
        String watermark = SupremacyCore.getSingleton().getName() + " §r§w" + SupremacyCore.getSingleton().getVersion() + " §7(" + time.toUpperCase() + ")";
        watermark = watermark.replace("&", "§");

        fr.drawStringWithShadow(watermark, 2.f, 2.f, UtilColor.getColor(colorMode, 1, color, (float) saturation, (float) brightness).getRGB());

        coordinates = (int) mc.thePlayer.posX + ", " + (int) mc.thePlayer.posY + ", " + (int) mc.thePlayer.posZ;

        bps = String.valueOf(Math.round(UtilMovement.getBPS() * 100.0) / 100.0);

        int infoOffset = fr.FONT_HEIGHT - 1;
        if (info) {
            fr.drawStringWithShadow("XYZ: §7" + coordinates , 2, sr.getScaledHeight() - infoOffset, UtilColor.getColor(colorMode, 1, color, (float) saturation, (float) brightness).getRGB());
            fr.drawStringWithShadow("FPS: §7" + Minecraft.getDebugFPS(), 2, sr.getScaledHeight() - infoOffset - fr.FONT_HEIGHT, UtilColor.getColor(colorMode, 1, color, (float) saturation, (float) brightness).getRGB());
            fr.drawStringWithShadow("BPS: §7" + bps, 2, sr.getScaledHeight() - infoOffset - fr.FONT_HEIGHT * 2, UtilColor.getColor(colorMode, 1, color, (float) saturation, (float) brightness).getRGB());
        }

        switch (suffixMode) {
            case "Parenthesis" -> featureList.sort(Comparator.comparingInt(m -> fr.getStringWidth(m.getSuffix() != null ? m.getFeatureInfo().name() + " §7(" + m.getSuffix() + ")" : m.getFeatureInfo().name())));
            case "Squared_Brackets" -> featureList.sort(Comparator.comparingInt(m -> fr.getStringWidth(m.getSuffix() != null ? m.getFeatureInfo().name() + " §7[" + m.getSuffix() + "]" : m.getFeatureInfo().name())));
            case "Hyphen" -> featureList.sort(Comparator.comparingInt(m -> fr.getStringWidth(m.getSuffix() != null ? m.getFeatureInfo().name() + " §7- " + m.getSuffix() : m.getFeatureInfo().name())));
            case "Standard" -> featureList.sort(Comparator.comparingInt(m -> fr.getStringWidth(m.getSuffix() != null ? m.getFeatureInfo().name() + " §7" + m.getSuffix() : m.getFeatureInfo().name())));
            case "None" -> featureList.sort(Comparator.comparingInt(m -> fr.getStringWidth(m.getFeatureInfo().name())));
        }
        Collections.reverse(featureList);

        double lastLength = 0;
        double y = offset;
        for (Feature feature : featureList) {
            if (feature.isEnabled()) {
                String arraylist = getString(feature);

                if (background)
                    Gui.drawRect((float) (sr.getScaledWidth() - fr.getStringWidth(arraylist) - offset - 1), (float) (y - 1), (float) ((float) sr.getScaledWidth() - offset + 1), (float) (y + fr.FONT_HEIGHT + spacing - 1), new Color(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue(), backgroundColor.getAlpha()).getRGB());

                switch (arraylistMode.toLowerCase()) {
                    case "left" ->
                            Gui.drawRect((float) (sr.getScaledWidth() - fr.getStringWidth(arraylist) - offset - 2), (float) (y - 1), (float) (sr.getScaledWidth() - fr.getStringWidth(arraylist) - offset - 1), (float) (y + fr.FONT_HEIGHT + spacing - 1), UtilColor.getColor(feature, (int) y * 20, colorMode, color, (float) saturation, (float) brightness).getRGB());
                    case "right" ->
                            Gui.drawRect((float) (sr.getScaledWidth() - offset + 1), (float) (y - 1), (float) ((float) sr.getScaledWidth() - offset + 2), (float) (y + fr.FONT_HEIGHT + spacing - 1), UtilColor.getColor(feature, (int) y * 20, colorMode, color, (float) saturation, (float) brightness).getRGB());
                    case "top" -> {
                        if (featureList.indexOf(feature) == 0)
                            Gui.drawRect((float) (sr.getScaledWidth() - fr.getStringWidth(arraylist) - offset - 1), (float) (y - 2), (float) ((float) sr.getScaledWidth() - offset + 1), (float) (y - 1), UtilColor.getColor(feature, (int) y * 20, colorMode, color, (float) saturation, (float) brightness).getRGB());
                    }
                    case "bottom" -> {
                        if (featureList.indexOf(feature) == featureList.size() - 1)
                            Gui.drawRect((float) (sr.getScaledWidth() - fr.getStringWidth(arraylist) - offset - 1), (float) (y + fr.FONT_HEIGHT + spacing - 1), (float) ((float) sr.getScaledWidth() - offset + 1), (float) (y + fr.FONT_HEIGHT + spacing), UtilColor.getColor(feature, (int) y * 20, colorMode, color, (float) saturation, (float) brightness).getRGB());
                    }
                    case "all" -> {
                        // top
                        if (featureList.indexOf(feature) == 0)
                            Gui.drawRect((float) (sr.getScaledWidth() - fr.getStringWidth(arraylist) - offset - 2), (float) (y - 2), (float) ((float) sr.getScaledWidth() - offset + 2), (float) (y - 1), UtilColor.getColor(feature, (int) y * 20, colorMode, color, (float) saturation, (float) brightness).getRGB());
                        // bottom
                        if (featureList.indexOf(feature) == featureList.size() - 1)
                            Gui.drawRect((float) (sr.getScaledWidth() - fr.getStringWidth(arraylist) - offset - 2), (float) (y + fr.FONT_HEIGHT + spacing - 1), (float) ((float) sr.getScaledWidth() - offset + 2), (float) (y + fr.FONT_HEIGHT + spacing), UtilColor.getColor(feature, (int) y * 20, colorMode, color, (float) saturation, (float) brightness).getRGB());
                        // left
                        Gui.drawRect((float) (sr.getScaledWidth() - fr.getStringWidth(arraylist) - offset - 2), (float) (y - 1), (float) (sr.getScaledWidth() - fr.getStringWidth(arraylist) - offset - 1), (float) (y + fr.FONT_HEIGHT + spacing - 1), UtilColor.getColor(feature, (int) y * 20, colorMode, color, (float) saturation, (float) brightness).getRGB());
                        // right
                        Gui.drawRect((float) (sr.getScaledWidth() - offset + 1), (float) (y - 1), (float) ((float) sr.getScaledWidth() - offset + 2), (float) (y + fr.FONT_HEIGHT + spacing - 1), UtilColor.getColor(feature, (int) y * 20, colorMode, color, (float) saturation, (float) brightness).getRGB());
                        // middle? ig lol idfk
                        if (featureList.indexOf(feature) != 0)
                            Gui.drawRect((float) (sr.getScaledWidth() - lastLength - offset - 2), (float) (y - 1), (float) (sr.getScaledWidth() - fr.getStringWidth(arraylist) - offset - 1), (float) (y), UtilColor.getColor(feature, (int) y * 20, colorMode, color, (float) saturation, (float) brightness).getRGB());
                    }
                }

                fr.drawStringWithShadow(arraylist, (float) (sr.getScaledWidth() - fr.getStringWidth(arraylist) - offset), (float) ((float) y + (spacing / 2)), UtilColor.getColor(feature, (int) y * 20, colorMode, color, (float) saturation, (float) brightness).getRGB());
                lastLength = fr.getStringWidth(arraylist);
                y += fr.FONT_HEIGHT + spacing;
            }
        }
    }

    private String getString(Feature feature) {
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
    public void onKey(final EventKeyInput keyEvent) {
        // todo: handle TabUI key inputs
    }

}
