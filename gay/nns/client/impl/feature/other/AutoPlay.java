package gay.nns.client.impl.feature.other;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.Serialize;
import gay.nns.client.api.setting.annotations.SettingMode;
import gay.nns.client.impl.event.packet.EventPacketReceive;
import net.minecraft.network.play.server.S02PacketChat;

@FeatureInfo(name = "AutoPlay", description = "Automatically joins a new game for you.")
public class AutoPlay extends Feature {

	@Serialize(name = "Mode")
	@SettingMode(modes = {"solo_insane", "solo_normal", "teams_normal", "teams_insane"})
	public String mode = "solo_insane";

	public AutoPlay() {
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
		if (eventPacketReceive.getPacket() instanceof S02PacketChat s02) {
			if (s02.getChatComponent().getUnformattedText().contains("play again?")) {
				mc.thePlayer.sendChatMessage("/play " + mode);
			}
		}
	}

}
