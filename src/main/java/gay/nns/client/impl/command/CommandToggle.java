package gay.nns.client.impl.command;

import gay.nns.client.api.command.Command;
import gay.nns.client.api.command.interfaces.SerializeCommand;
import gay.nns.client.api.core.SupremacyCore;
import gay.nns.client.api.feature.Feature;

@SerializeCommand(name = "Toggle", description = "Toggles a feature.", usage = "toggle <feature>", aliases = {"t"})
public class CommandToggle extends Command {

	public CommandToggle() {
		super();
	}

	@Override
	public void onCommand(String[] args) {
		super.onCommand(args);

		if (args.length == 2) {
			try {
				for (Feature feature : SupremacyCore.getSingleton().getFeatureManager().getFeatures()) {
					if (feature.getFeatureInfo().name().equalsIgnoreCase(args[1])) {
						feature.toggle();
						chatUtil.chatCommand("Toggled " + feature.getFeatureInfo().name() + " to " + feature.isEnabled());
						return;
					}
				}
			} catch (Exception e) {
				chatUtil.chatCommand("Invalid command.");
				chatUtil.chatCommand("E: " + e.getMessage());
			}
		}
	}

}
