package gay.nns.client.impl.management;

import gay.nns.client.impl.feature.combat.FeatureKillAura;
import gay.nns.client.impl.feature.combat.FeatureVelocity;
import gay.nns.client.impl.feature.exploit.FeatureAntiVoid;
import gay.nns.client.impl.feature.exploit.FeatureFastBow;
import gay.nns.client.impl.feature.ghost.FeatureAutoClicker;
import gay.nns.client.impl.feature.ghost.FeatureNoClickDelay;
import gay.nns.client.impl.feature.movement.*;
import gay.nns.client.impl.feature.other.*;
import gay.nns.client.impl.feature.render.*;
import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.IFeatureManagerApi;
import gay.nns.client.impl.event.game.KeyEvent;

import gay.nns.client.impl.feature.combat.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FeatureManager implements IFeatureManagerApi {

	private Map<String, AbstractFeature> Features;

	public FeatureManager() {

	}

	public void initialize() {
		Features = addFeatures(
				/* Combat */
				new FeatureKillAura(),
				new FeatureVelocity(),
				new FeatureAntiBot(),

				/* Ghost */
				new FeatureAutoClicker(),
				new FeatureNoClickDelay(),

				/* Movement */
				new FeatureSprint(),
				new FeatureNoSlowdown(),
				new FeatureFlight(),
				new FeatureSpeed(),
				new FeatureNoWeb(),
				new FeatureScreenWalk(),

				/* Render */
				new FeatureAnimation(),
				new FeatureViewModel(),
				new FeatureInterface(),
				new FeatureFakePlayer(),
				new FeatureAmbience(),
				new FeatureBrightness(),
				new FeatureESP(),

				/* Other */
				new FeatureNoFall(),
				new FeatureYaw(),
				new FeatureNoRotate(),
				new FeatureSecurityFeatures(),
				new FeatureFastPlace(),
				new FeatureAutoTool(),
				new FeatureChestStealer(),
				new FeatureInventoryManager(),

				/* Exploit */
				new FeatureFastBow(),
				new FeatureAntiVoid(),

				new FeatureClickGUI()
		);

		System.out.printf("Features (%d): %s%n", Features.size(), Features.keySet());
	}

	private HashMap<String, AbstractFeature> addFeatures(final AbstractFeature... FeatureArray) {
		final HashMap<String, AbstractFeature> FeatureAdder = new HashMap<>();
		Arrays.stream(FeatureArray).forEach(abstractFeature -> {
			FeatureAdder.put(abstractFeature.getFeatureInfo().name(), abstractFeature);
		});
		return FeatureAdder;
	}

	@Override
	public Collection<AbstractFeature> getFeatures() {
		return Features.values();
	}

	@Override
	public Collection<AbstractFeature> getEnabledFeatures() {
		return Features.values().stream().filter(AbstractFeature::isEnabled).toList();
	}

	@Override
	public AbstractFeature getFeatureByName(String name) {
		return Features.values().stream().filter(Feature -> Feature.getFeatureInfo().name().equalsIgnoreCase(name)).findFirst().orElse(null);
	}

	@Override
	public AbstractFeature getFeatureFromType(Class<? extends AbstractFeature> clazz) {
		return Features.values().stream().filter(feature -> feature.getClass().equals(clazz)).findFirst().orElse(null);
	}

	@Override
	public Collection<AbstractFeature> getFeatureFromCategory(FeatureCategory category) {
		return Features.values().stream().filter(Feature -> Feature.getFeatureInfo().category().equals(category)).toList();
	}

	@Subscribe
	public void onKey(final KeyEvent event) {
		getFeatures().forEach(Features -> {
			if (event.key() == Features.getKey())
				Features.toggle();
		});
	}

}
