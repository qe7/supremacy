package gay.nns.client.api.command.interfaces;

import gay.nns.client.api.command.Command;

import java.util.Collection;

public interface ICommandManagerApi {
	
	Collection<Command> getCommands();
	Command getCommandByName(String name);
	Command getCommandFromType(Class<? extends Command> clazz);

}
