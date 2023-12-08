package gay.nns.client.command.api.types;

import gay.nns.client.util.chat.UtilChat;
import gay.nns.client.command.api.interfaces.SerializeCommand;

public abstract class Command {

	private SerializeCommand serializeCommand;

	protected final UtilChat chatUtil = new UtilChat();

	protected Command() {
		this.serializeCommand = getClass().getAnnotation(SerializeCommand.class);
	}

	public void onCommand(String[] args) {
		chatUtil.chatCommand("Command called: " + getCommandInfo().name());
	}

	public SerializeCommand getCommandInfo() {
		if (serializeCommand == null)
			serializeCommand = getClass().getAnnotation(SerializeCommand.class);
		return serializeCommand;
	}

}
