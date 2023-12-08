package gay.nns.client.feature.impl.other;

import gay.nns.client.event.api.interfaces.Subscribe;
import gay.nns.client.feature.api.types.Feature;
import gay.nns.client.feature.api.interfaces.SerializeFeature;
import gay.nns.client.setting.api.annotations.SerializeSetting;
import gay.nns.client.setting.api.annotations.SettingMode;
import gay.nns.client.event.impl.packet.EventPacketReceive;
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
		if (!(eventPacketReceive.getPacket() instanceof S02PacketChat)) return;
		final S02PacketChat s02 = (S02PacketChat) eventPacketReceive.getPacket();

		if (s02.getChatComponent().getUnformattedText().contains("play again?")) {
			mc.thePlayer.sendChatMessage("/play " + mode);
		}
	}

}
