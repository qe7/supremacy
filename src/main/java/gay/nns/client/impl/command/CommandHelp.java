package gay.nns.client.impl.command;

import gay.nns.client.api.command.Command;
import gay.nns.client.api.command.interfaces.SerializeCommand;
import gay.nns.client.api.core.SupremacyCore;

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
