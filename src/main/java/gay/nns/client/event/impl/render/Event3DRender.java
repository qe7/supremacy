package gay.nns.client.event.impl.render;

import gay.nns.client.event.api.types.EventCancelable;

public class Event3DRender extends EventCancelable {

    private final float ticks;

    public Event3DRender(float ticks) {
        this.ticks = ticks;
    }

    public float getPartialTicks() {
        return ticks;
    }
}