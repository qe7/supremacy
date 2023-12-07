package gay.nns.client.impl.event.packet;

import gay.nns.client.api.event.type.EventCancelable;
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
