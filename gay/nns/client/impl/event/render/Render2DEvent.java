package gay.nns.client.impl.event.render;

import net.minecraft.client.gui.ScaledResolution;

public record Render2DEvent(ScaledResolution scaledResolution, float partialTicks) { }
