package gay.nns.client.impl.event.render;

import gay.nns.client.api.event.type.EventCancelable;

public class Event3DRender extends EventCancelable {

    private final float ticks;

    public Event3DRender(float ticks) {
        this.ticks = ticks;
    }

    public float getPartialTicks() {
        return ticks;
    }
}