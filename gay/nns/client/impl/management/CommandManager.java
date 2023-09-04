package gay.nns.client.impl.management;

import gay.nns.client.api.command.interfaces.ICommandManagerApi;
import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.impl.event.player.MessageSentEvent;
import gay.nns.client.api.command.Command;
import gay.nns.client.impl.command.CommandBind;
import gay.nns.client.impl.command.CommandHelp;
import gay.nns.client.impl.command.CommandSay;
import gay.nns.client.impl.command.CommandToggle;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CommandManager implements ICommandManagerApi {

	private Map<String, Command> Commands;

	public void initialize() {
		Commands = addCommands(
				new CommandBind(),
				new CommandToggle(),
				new CommandSay(),
				new CommandHelp()
		);
	}

	private HashMap<String, Command> addCommands(final Command... CommandArray) {
		final HashMap<String, Command> CommandAdder = new HashMap<>();
		Arrays.stream(CommandArray).forEach(abstractCommand -> {
			CommandAdder.put(abstractCommand.getName(), abstractCommand);
		});
		return CommandAdder;
	}

	@Override
	public Collection<Command> getCommands() {
		return Commands.values();
	}

	@Override
	public Command getCommandByName(String name) {
		return Commands.get(name);
	}

	@Override
	public Command getCommandFromType(Class<? extends Command> clazz) {
		return Commands.values().stream().filter(command -> command.getClass().equals(clazz)).findFirst().orElse(null);
	}

	@Subscribe
	public void onSentMessage(final MessageSentEvent messageSentEvent) {
		if (messageSentEvent.getMessage().startsWith(".")) {
			String[] args = messageSentEvent.getMessage().substring(1).split(" ");
			Arrays.stream(args).forEach(System.out::println);
			Commands.values().stream().filter(command -> command.getName().equalsIgnoreCase(args[0])).findFirst().ifPresent(command -> {
				command.onCommand(args);
			});
			messageSentEvent.setCancelled(true);
		}
	}

}
