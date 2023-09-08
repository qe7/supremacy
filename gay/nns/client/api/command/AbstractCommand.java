package gay.nns.client.api.command;

import gay.nns.client.api.command.interfaces.ICommand;
import gay.nns.client.util.chat.ChatUtil;
import gay.nns.client.api.command.interfaces.CommandInfo;

public abstract class AbstractCommand implements ICommand {

	private CommandInfo commandInfo;

	protected final ChatUtil chatUtil = new ChatUtil();

	protected AbstractCommand() {
		this.commandInfo = getClass().getAnnotation(CommandInfo.class);
	}

	@Override
	public void onCommand(String[] args) {
		chatUtil.chatCommand("Command called: " + getCommandInfo().name());
	}

	@Override
	public CommandInfo getCommandInfo() {
		if (commandInfo == null)
			commandInfo = getClass().getAnnotation(CommandInfo.class);
		return commandInfo;
	}

}
