package gay.nns.client.impl.management;

import gay.nns.client.api.command.AbstractCommand;
import gay.nns.client.api.command.interfaces.ICommandManagerApi;
import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.impl.command.*;
import gay.nns.client.impl.event.player.MessageSentEvent;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CommandManager implements ICommandManagerApi {

	private Map<String, AbstractCommand> Commands;

	public CommandManager() {

	}

	public void initialize() {
		Commands = addCommands(
				new CommandBind(),
				new CommandToggle(),
				new CommandSay(),
				new CommandWatermark(),
				new CommandHelp()
		);

		System.out.printf("Commands (%d): %s%n", Commands.size(), Commands.keySet());
	}

	private HashMap<String, AbstractCommand> addCommands(final AbstractCommand... CommandArray) {
		final HashMap<String, AbstractCommand> CommandAdder = new HashMap<>();
		Arrays.stream(CommandArray).forEach(abstractCommand -> {
			CommandAdder.put(abstractCommand.getCommandInfo().name(), abstractCommand);
		});
		return CommandAdder;
	}

	@Override
	public Collection<AbstractCommand> getCommands() {
		return Commands.values();
	}

	@Override
	public AbstractCommand getCommandByName(String name) {
		return Commands.get(name);
	}

	@Override
	public AbstractCommand getCommandFromType(Class<? extends AbstractCommand> clazz) {
		return Commands.values().stream().filter(command -> command.getClass().equals(clazz)).findFirst().orElse(null);
	}

	@Subscribe
	public void onSentMessage(final MessageSentEvent messageSentEvent) {
		if (messageSentEvent.getMessage().startsWith(".")) {
			String[] args = messageSentEvent.getMessage().substring(1).split(" ");
			Arrays.stream(args).forEach(System.out::println);
			Commands.values().stream().filter(command -> command.getCommandInfo().name().equalsIgnoreCase(args[0])).findFirst().ifPresent(command -> {
				command.onCommand(args);
			});
			messageSentEvent.setCancelled(true);
		}
	}

}
