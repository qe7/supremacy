package gay.nns.client.api.command;

import gay.nns.client.util.chat.ChatUtil;
import gay.nns.client.api.command.interfaces.CommandInfo;

public abstract class Command extends ChatUtil {

	private String name;
	private String description;
	private String usage;
	private String[] aliases;

	public Command() {
		if (getClass().isAnnotationPresent(CommandInfo.class)) {
			CommandInfo commandInfo = getClass().getAnnotation(CommandInfo.class);
			this.name = commandInfo.name();
			this.description = commandInfo.description();
			this.usage = commandInfo.usage();
			this.aliases = commandInfo.aliases();
		}
	}

	public void onCommand(String[] args) {
		this.chatCommand("Command called: " + name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public String[] getAliases() {
		return aliases;
	}

	public void setAliases(String[] aliases) {
		this.aliases = aliases;
	}

}
