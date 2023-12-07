package gay.nns.client.impl.feature.render;

import gay.nns.client.api.core.SupremacyCore;
import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.SerializeFeature;
import gay.nns.client.impl.event.render.EventRender2D;
import gay.nns.client.impl.feature.combat.FeatureKillAura;
import gay.nns.client.impl.feature.other.FeatureKeepSprint;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.Entity;

import java.awt.*;

@SerializeFeature(name = "Target Hud", category = FeatureCategory.RENDER, description = "Blah blah blah this isnt done")
public class FeatureTargetHud extends Feature {

    public Entity target = FeatureKillAura.mcTarget;
    FontRenderer fr = mc.fontRendererObj;

    public double nameWidth;

    public FeatureTargetHud() {
        super();
    }

    public void onRender2D(EventRender2D event) {
        if (target == null) return;

        String name = target.getName();

        fr.drawStringWithShadow(name, 100, 100, Color.yellow.getRGB());
    }
}


