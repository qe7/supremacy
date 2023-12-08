package gay.nns.client.feature.impl.other;

import gay.nns.client.event.api.interfaces.Subscribe;
import gay.nns.client.feature.api.types.Feature;
import gay.nns.client.feature.api.enums.FeatureCategory;
import gay.nns.client.feature.api.interfaces.SerializeFeature;
import gay.nns.client.setting.api.annotations.SettingMode;
import gay.nns.client.setting.api.annotations.SerializeSetting;
import gay.nns.client.event.impl.packet.EventPacketReceive;
import gay.nns.client.event.impl.render.EventRender2D;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

@SerializeFeature(name = "NoRotate", description = "Prevents you from rotating your player", category = FeatureCategory.OTHER)
public class FeatureNoRotate extends Feature {

	@SerializeSetting(name = "Mode")
	@SettingMode(modes = {"Packet", "Edit"})
	public String mode = "Edit";

	public FeatureNoRotate() {
		super();
	}

	@Override
	protected void onEnable() {
		super.onEnable();
	}

	@Override
	protected void onDisable() {
		super.onDisable();
	}

	@Subscribe
	public void onRender(final EventRender2D render2DEvent) {
		this.setSuffix(mode);
	}

	@Subscribe
	public void onPacket(final EventPacketReceive packetReceiveEvent) {
		if (packetReceiveEvent.getPacket() instanceof S08PacketPlayerPosLook) {
			final S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) packetReceiveEvent.getPacket();
			switch (mode.toLowerCase()) {
				case "packet": {
					if (packet.getYaw() != mc.thePlayer.rotationYaw || packet.getPitch() != mc.thePlayer.rotationPitch) {
						packetReceiveEvent.setCancelled(true);
						mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch(), mc.thePlayer.onGround));
						mc.thePlayer.setPosition(packet.getX(), packet.getY(), packet.getZ());
					}
				}
				case "edit": {
					if (packet.getYaw() != mc.thePlayer.rotationYaw || packet.getPitch() != mc.thePlayer.rotationPitch) {
						packet.yaw = mc.thePlayer.rotationYaw;
						packet.pitch = mc.thePlayer.rotationPitch;
						mc.thePlayer.setPosition(packet.getX(), packet.getY(), packet.getZ());
					}
				}
			}
		}
	}

}