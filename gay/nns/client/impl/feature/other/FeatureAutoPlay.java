package gay.nns.client.impl.feature.other;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.interfaces.SerializeFeature;
import gay.nns.client.api.setting.annotations.SerializeSetting;
import gay.nns.client.api.setting.annotations.SettingMode;
import gay.nns.client.impl.event.packet.EventPacketReceive;
import net.minecraft.network.play.server.S02PacketChat;

@SerializeFeature(name = "AutoPlay", description = "Automatically joins a new game for you.")
public class FeatureAutoPlay extends Feature {

	@SerializeSetting(name = "Mode")
	@SettingMode(modes = {"solo_insane", "solo_normal", "teams_normal", "teams_insane"})
	public String mode = "solo_insane";

	public FeatureAutoPlay() {
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
	public void onPacketReceived(final EventPacketReceive eventPacketReceive) {
		if (mc.theWorld == null || mc.thePlayer == null) return;
		if (!(eventPacketReceive.getPacket() instanceof S02PacketChat s02)) return;

		if (s02.getChatComponent().getUnformattedText().contains("play again?")) {
			mc.thePlayer.sendChatMessage("/play " + mode);
		}
	}

}
