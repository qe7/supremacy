package gay.nns.client.api.core;

import gay.nns.client.api.event.EventBus;
import gay.nns.client.impl.management.ManagerFeature;
import gay.nns.client.impl.management.ManagerRotation;
import gay.nns.client.impl.management.ManagerSetting;
import gay.nns.client.util.font.FontUtil;
import gay.nns.client.impl.management.ManagerCommand;
import net.minecraft.client.Minecraft;

public final class Core {

	private final static Core singleton = new Core();
	private String name;
	private final String version;
	private final String[] authors;
	private final EventBus eventBus;
	private final ManagerFeature featureManager;
	private final ManagerRotation rotationManager;
	private final ManagerCommand commandManager;
	private final ManagerSetting settingManager;
	private final FontUtil fontUtil;

	Core() {
		this.eventBus = new EventBus();

		this.featureManager = new ManagerFeature();
		this.eventBus.register(featureManager);

		this.commandManager = new ManagerCommand();
		this.eventBus.register(commandManager);

		this.rotationManager = new ManagerRotation();
		this.eventBus.register(rotationManager);

		this.settingManager = new ManagerSetting();

		this.fontUtil = new FontUtil();
	}

	{
		name = "Supremacy";
		version = "b1.0.0";
		authors = new String[]{"Shae", "Ahru"};
	}

	public void initialize() {
		featureManager.initialize();

		commandManager.initialize();

		fontUtil.initialize();

		Minecraft.getMinecraft().getSession().setUsername(getName());
	}

	public static Core getSingleton() {
		return singleton;
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

	public ManagerFeature getFeatureManager() {
		return featureManager;
	}

	public ManagerRotation getRotationManager() {
		return rotationManager;
	}

	public ManagerCommand getCommandManager() {
		return commandManager;
	}

	public ManagerSetting getSettingManager() {
		return settingManager;
	}

	public FontUtil getFontUtil() {
		return fontUtil;
	}
}
