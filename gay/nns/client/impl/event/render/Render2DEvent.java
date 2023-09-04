package gay.nns.client.impl.event.render;

import net.minecraft.client.gui.ScaledResolution;

public class Render2DEvent {

    private final ScaledResolution scaledResolution;

    public Render2DEvent(final ScaledResolution scaledResolution) {
        this.scaledResolution = scaledResolution;
    }

    public ScaledResolution getScaledResolution() {
        return scaledResolution;
    }

}
