package gay.nns.client.impl.command;

import gay.nns.client.api.command.Command;
import gay.nns.client.api.core.SupremacyCore;
import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.command.interfaces.CommandInfo;
import org.lwjgl.input.Keyboard;

@CommandInfo(name = "Bind", description = "Change the keyBind of a feature.", usage = "bind", aliases = {"keybind", "key"})
public class CommandBind extends Command {

	public CommandBind() {
		super();
	}

	@Override
	public void onCommand(String[] args) {
		super.onCommand(args);

		if (args.length == 3) {
			try {
				for (Feature feature : SupremacyCore.getSingleton().getFeatureManager().getFeatures()) {
					if (feature.getFeatureInfo().name().equalsIgnoreCase(args[1])) {
						feature.setKey(Keyboard.getKeyIndex(args[2].toUpperCase()));
						chatUtil.chatCommand("Set the keyBind of " + feature.getFeatureInfo().name() + " to " + Keyboard.getKeyName(feature.getKey()));
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
