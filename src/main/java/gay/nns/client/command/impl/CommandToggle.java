package gay.nns.client.command.impl;

import gay.nns.client.command.api.types.Command;
import gay.nns.client.command.api.interfaces.SerializeCommand;
import gay.nns.client.SupremacyCore;
import gay.nns.client.feature.api.types.Feature;

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
