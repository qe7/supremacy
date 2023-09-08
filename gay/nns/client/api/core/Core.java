package gay.nns.client.api.core;

import gay.nns.client.api.event.EventBus;
import gay.nns.client.impl.management.FeatureManager;
import gay.nns.client.impl.management.RotationManager;
import gay.nns.client.impl.management.SettingManager;
import gay.nns.client.util.font.FontUtil;
import gay.nns.client.impl.management.CommandManager;
import net.minecraft.client.Minecraft;

public final class Core {

	private final static Core singleton = new Core();
	private String name;
	private final String version;
	private final String[] authors;
	private final EventBus eventBus;
	private final FeatureManager featureManager;
	private final RotationManager rotationManager;
	private final CommandManager commandManager;
	private final SettingManager settingManager;
	private final FontUtil fontUtil;

	public Core() {
		this.eventBus = new EventBus();

		this.featureManager = new FeatureManager();
		this.eventBus.register(featureManager);

		this.commandManager = new CommandManager();
		this.eventBus.register(commandManager);

		this.rotationManager = new RotationManager();
		this.eventBus.register(rotationManager);

		this.settingManager = new SettingManager();

		this.fontUtil = new FontUtil();
	}

	{
		name = "Supremacy";
		version = "b1.0.0";
		authors = new String[]{"Shae", "Ahru"};
	}

	public static Core getSingleton() {
		if (singleton == null)
			return new Core();
		return singleton;
	}

	public void initialize() {
		featureManager.initialize();

		commandManager.initialize();

		fontUtil.initialize();

		Minecraft.getMinecraft().getSession().setUsername(getName());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public String[] getAuthors() {
		return authors;
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public FeatureManager getFeatureManager() {
		return featureManager;
	}

	public RotationManager getRotationManager() {
		return rotationManager;
	}

	public CommandManager getCommandManager() {
		return commandManager;
	}

	public SettingManager getSettingManager() {
		return settingManager;
	}

	public FontUtil getFontUtil() {
		return fontUtil;
	}

}
