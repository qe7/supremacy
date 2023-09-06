package gay.nns.client.impl.feature.other;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.Mode;
import gay.nns.client.api.setting.annotations.Serialize;
import gay.nns.client.impl.event.packet.PacketReceiveEvent;
import gay.nns.client.impl.event.render.Render2DEvent;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

@FeatureInfo(name = "NoRotate", description = "Prevents you from rotating your player", category = FeatureCategory.OTHER)
public class FeatureNoRotate extends AbstractFeature {

	@Serialize(name = "Mode")
	@Mode(modes = {"Packet", "Edit"})
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
	public void onRender(final Render2DEvent render2DEvent) {
		this.setSuffix(mode);
	}

	@Subscribe
	public void onPacket(final PacketReceiveEvent packetReceiveEvent) {
		if (packetReceiveEvent.getPacket() instanceof S08PacketPlayerPosLook packet) {
			switch (mode.toLowerCase()) {
				case "packet" -> {
					packetReceiveEvent.setCancelled(true);
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch(), mc.thePlayer.onGround));
					mc.thePlayer.setPosition(packet.getX(), packet.getY(), packet.getZ());
				}
				case "edit" -> {
					packet.yaw = mc.thePlayer.rotationYaw;
					packet.pitch = mc.thePlayer.rotationPitch;
					mc.thePlayer.setPosition(packet.getX(), packet.getY(), packet.getZ());
				}
			}
		}
	}

}