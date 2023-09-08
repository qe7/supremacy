package gay.nns.client.impl.command;

import gay.nns.client.api.command.AbstractCommand;
import gay.nns.client.api.command.interfaces.CommandInfo;
import gay.nns.client.api.core.Core;

@CommandInfo(name = "Watermark", description = "Watermark")
public class CommandWatermark extends AbstractCommand {

	public CommandWatermark() {
		super();
	}

	@Override
	public void onCommand(String[] args) {
		super.onCommand(args);

		if (args.length != 1) {
			try {
				// sets the watermark to the given string, for all the given args
				Core.getSingleton().setName(args[1]);
				chatUtil.chatCommand("Set watermark to " + Core.getSingleton().getName());
			} catch (Exception e) {
				chatUtil.chatCommand("Invalid command.");
				chatUtil.chatCommand("E: " + e.getMessage());
			}
		}
	}

}
