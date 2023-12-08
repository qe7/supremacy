package gay.nns.client.event.impl.render;

import net.minecraft.client.gui.ScaledResolution;

public class EventRender2D {

        private final ScaledResolution scaledResolution;

        public EventRender2D(ScaledResolution scaledResolution) {
            this.scaledResolution = scaledResolution;
        }

        public ScaledResolution getScaledResolution() {
            return scaledResolution;
        }
}
