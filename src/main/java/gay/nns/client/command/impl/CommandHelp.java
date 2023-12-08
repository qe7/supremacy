package gay.nns.client.command.impl;

import gay.nns.client.command.api.types.Command;
import gay.nns.client.command.api.interfaces.SerializeCommand;
import gay.nns.client.SupremacyCore;

@SerializeCommand(name = "Help", description = "Displays a list of commands.", usage = ".help", aliases = {"?"})
public class CommandHelp extends Command {

	public CommandHelp() {
		super();
	}

	@Override
	public void onCommand(String[] args) {
		super.onCommand(args);

		if (args.length == 1) {
			for (Command abstractCommand : SupremacyCore.getSingleton().getCommandManager().getCommands()) {
				chatUtil.chatCommand(abstractCommand.getCommandInfo().name() + " - " + abstractCommand.getCommandInfo().usage());
			}
		}
	}

}
