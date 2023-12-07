package gay.nns.client.util.chat;

import gay.nns.client.api.core.SupremacyCore;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class UtilChat {

	private final String lightGrey = "\u00A77";
	private final String reset = "\u00A7r";
	private final String prefix = lightGrey + "(" + SupremacyCore.getSingleton().getName().charAt(0) + ") " + reset;

	public void chatCommand(String message) {
		String text = prefix + message;
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(text));
	}

}
