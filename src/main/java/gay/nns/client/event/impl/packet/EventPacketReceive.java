package gay.nns.client.event.impl.packet;

import gay.nns.client.event.api.types.EventCancelable;
import net.minecraft.network.Packet;

public class EventPacketReceive extends EventCancelable {

    private final Packet packet;

    public EventPacketReceive(final Packet<?> packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }

}
