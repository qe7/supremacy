package gay.nns.client.command.impl;

import gay.nns.client.command.api.types.Command;
import gay.nns.client.command.api.interfaces.SerializeCommand;
import gay.nns.client.SupremacyCore;

@SerializeCommand(name = "Watermark", description = "Watermark")
public class CommandWatermark extends Command {

	public CommandWatermark() {
		super();
	}

	@Override
	public void onCommand(String[] args) {
		super.onCommand(args);

		if (args.length != 1) {
			try {
				// sets the watermark to the given string, for all the given args
				SupremacyCore.getSingleton().setName(String.join(" ", args).replace("watermark ", ""));
				chatUtil.chatCommand("Set watermark to " + SupremacyCore.getSingleton().getName());
			} catch (Exception e) {
				chatUtil.chatCommand("Invalid command.");
				chatUtil.chatCommand("E: " + e.getMessage());
			}
		}
	}

}
