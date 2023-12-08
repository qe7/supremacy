package gay.nns.client.event.impl.packet;

import gay.nns.client.event.api.types.EventCancelable;
import net.minecraft.network.Packet;

public class EventPacketSend extends EventCancelable {

    private final Packet<?> packet;

    public EventPacketSend(final Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }

}
