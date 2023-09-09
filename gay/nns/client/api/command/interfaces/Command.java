package gay.nns.client.api.command.interfaces;

public interface Command {

	CommandInfo getCommandInfo();
	void onCommand(String[] args);

}
