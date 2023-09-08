package gay.nns.client.impl.command;

import gay.nns.client.api.command.AbstractCommand;
import gay.nns.client.api.command.interfaces.CommandInfo;
import gay.nns.client.api.core.Core;

@CommandInfo(name = "Help", description = "Displays a list of commands.", usage = ".help", aliases = {"?"})
public class CommandHelp extends AbstractCommand {

	public CommandHelp() {
		super();
	}

	@Override
	public void onCommand(String[] args) {
		super.onCommand(args);

		if (args.length == 1) {
			for (AbstractCommand abstractCommand : Core.getSingleton().getCommandManager().getCommands()) {
				chatUtil.chatCommand(abstractCommand.getCommandInfo().name() + " - " + abstractCommand.getCommandInfo().usage());
			}
		}
	}

}
