package gay.nns.client.api.command.interfaces;

public interface ICommand {

	CommandInfo getCommandInfo();
	void onCommand(String[] args);

}
