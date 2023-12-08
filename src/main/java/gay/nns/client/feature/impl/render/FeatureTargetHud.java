package gay.nns.client.feature.impl.render;

import gay.nns.client.feature.api.types.Feature;
import gay.nns.client.feature.api.enums.FeatureCategory;
import gay.nns.client.feature.api.interfaces.SerializeFeature;
import gay.nns.client.event.impl.render.EventRender2D;
import gay.nns.client.feature.impl.combat.FeatureKillAura;
import net.minecraft.client.gui.FontRenderer;
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


