package gay.nns.client.management;

import gay.nns.client.command.api.types.Command;
import gay.nns.client.event.api.interfaces.Subscribe;
import gay.nns.client.command.impl.*;
import gay.nns.client.event.impl.player.EventMessageSent;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ManagerCommand {

	private Map<String, Command> Commands;

	public ManagerCommand() {

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

	private HashMap<String, Command> addCommands(final Command... CommandArray) {
		final HashMap<String, Command> CommandAdder = new HashMap<>();
		Arrays.stream(CommandArray).forEach(abstractCommand -> {
			CommandAdder.put(abstractCommand.getCommandInfo().name(), abstractCommand);
		});
		return CommandAdder;
	}

	public Collection<Command> getCommands() {
		return Commands.values();
	}

	public Command getCommandByName(String name) {
		return Commands.get(name);
	}

	public Command getCommandFromType(Class<? extends Command> clazz) {
		return Commands.values().stream().filter(command -> command.getClass().equals(clazz)).findFirst().orElse(null);
	}

	@Subscribe
	public void onSentMessage(final EventMessageSent messageSentEvent) {
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
