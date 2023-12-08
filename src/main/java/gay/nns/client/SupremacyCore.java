package gay.nns.client;

import gay.nns.client.event.api.EventBus;
import gay.nns.client.management.ManagerCommand;
import gay.nns.client.management.ManagerFeature;
import gay.nns.client.management.ManagerRotation;
import gay.nns.client.management.ManagerSetting;
import gay.nns.client.util.font.UtilFont;

public final class SupremacyCore {

	private final static SupremacyCore singleton = new SupremacyCore();
	private String name;
	private final String version;
	private final String[] authors;
	private final EventBus eventBus;
	private final ManagerFeature featureManager;
	private final ManagerRotation rotationManager;
	private final ManagerCommand commandManager;
	private final ManagerSetting settingManager;
	private final UtilFont fontUtil;

	SupremacyCore() {
		this.eventBus = new EventBus();

		this.featureManager = new ManagerFeature();
		this.eventBus.register(featureManager);

		this.commandManager = new ManagerCommand();
		this.eventBus.register(commandManager);

		this.rotationManager = new ManagerRotation();
		this.eventBus.register(rotationManager);

		this.settingManager = new ManagerSetting();

		this.fontUtil = new UtilFont();
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
	}

	public static SupremacyCore getSingleton() {
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

	public UtilFont getFontUtil() {
		return fontUtil;
	}
}
