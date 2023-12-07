package gay.nns.client.impl.command;

import gay.nns.client.api.command.Command;
import gay.nns.client.api.command.interfaces.SerializeCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;

@SerializeCommand(name = "Say", description = "Sends a message to the chat.", usage = "say <message>")
public class CommandSay extends Command {

	public CommandSay() {
		super();
	}

	@Override
	public void onCommand(String[] args) {
		super.onCommand(args);

		if (args.length > 1) {
			StringBuilder message = new StringBuilder();
			for (int i = 1; i < args.length; i++) {
				message.append(args[i]).append(" ");
			}
			Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage(message.toString()));
		} else {
			chatUtil.chatCommand("Invalid usage.");
		}
	}
}
