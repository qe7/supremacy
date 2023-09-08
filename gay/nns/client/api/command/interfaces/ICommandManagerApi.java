package gay.nns.client.api.command.interfaces;

import gay.nns.client.api.command.AbstractCommand;

import java.util.Collection;

public interface ICommandManagerApi {
	
	Collection<AbstractCommand> getCommands();
	AbstractCommand getCommandByName(String name);
	AbstractCommand getCommandFromType(Class<? extends AbstractCommand> clazz);

}
