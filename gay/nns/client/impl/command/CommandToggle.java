package gay.nns.client.impl.command;

import gay.nns.client.api.command.Command;
import gay.nns.client.api.command.interfaces.CommandInfo;
import gay.nns.client.api.core.Core;
import gay.nns.client.api.feature.AbstractFeature;

@CommandInfo(name = "Toggle", description = "Toggles a feature.", usage = "toggle <feature>", aliases = {"t"})
public class CommandToggle extends Command {

	public CommandToggle() {
		super();
	}

	@Override
	public void onCommand(String[] args) {
		super.onCommand(args);

		if (args.length == 2) {
			try {
				for (AbstractFeature feature : Core.getSingleton().getFeatureManager().getFeatures()) {
					if (feature.getFeatureInfo().name().equalsIgnoreCase(args[1])) {
						feature.toggle();
						this.chatCommand("Toggled " + feature.getFeatureInfo().name() + " to " + feature.isEnabled());
						return;
					}
				}
			} catch (Exception e) {
				this.chatCommand("Invalid command.");
				this.chatCommand("E: " + e.getMessage());
			}
		}
	}

}
