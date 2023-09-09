package gay.nns.client.impl.management;

import gay.nns.client.impl.feature.combat.FeatureKillAura;
import gay.nns.client.impl.feature.combat.FeatureVelocity;
import gay.nns.client.impl.feature.exploit.FeatureAntiVoid;
import gay.nns.client.impl.feature.exploit.FeatureFastBow;
import gay.nns.client.impl.feature.ghost.FeatureAimAssist;
import gay.nns.client.impl.feature.ghost.FeatureAutoClicker;
import gay.nns.client.impl.feature.ghost.FeatureNoClickDelay;
import gay.nns.client.impl.feature.movement.*;
import gay.nns.client.impl.feature.other.*;
import gay.nns.client.impl.feature.render.*;
import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureManagerApi;
import gay.nns.client.impl.event.game.EventKeyInput;

import gay.nns.client.impl.feature.combat.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ManagerFeature implements FeatureManagerApi {

	private Map<String, Feature> Features;

	public ManagerFeature() {

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
				new FeatureAimAssist(),

				/* Movement */
				new FeatureSprint(),
				new FeatureNoSlowdown(),
				new FeatureFlight(),
				new FeatureSpeed(),
				new FeatureNoWeb(),
				new FeatureScreenWalk(),
				new FeatureSafeWalk(),

				/* Render */
				new FeatureAnimation(),
				new FeatureViewModel(),
				new FeatureInterface(),
				new FeatureFakePlayer(),
				new FeatureAmbience(),
				new FeatureBrightness(),
				new FeatureESP(),
				new FeatureRotate(),

				/* Other */
				new FeatureNoFall(),
				new FeatureYaw(),
				new FeatureNoRotate(),
				new FeatureSecurityFeatures(),
				new FeatureFastPlace(),
				new FeatureAutoTool(),
				new FeatureChestStealer(),
				new FeatureInventoryManager(),
				new FeatureScaffold(),
				new FeatureSnake(),

				/* Exploit */
				new FeatureFastBow(),
				new FeatureAntiVoid(),

				new FeatureClickGUI()
		);

		System.out.printf("Features (%d): %s%n", Features.size(), Features.keySet());
	}

	private HashMap<String, Feature> addFeatures(final Feature... FeatureArray) {
		final HashMap<String, Feature> FeatureAdder = new HashMap<>();
		Arrays.stream(FeatureArray).forEach(abstractFeature -> {
			FeatureAdder.put(abstractFeature.getFeatureInfo().name(), abstractFeature);
		});
		return FeatureAdder;
	}

	@Override
	public Collection<Feature> getFeatures() {
		return Features.values();
	}

	@Override
	public Collection<Feature> getEnabledFeatures() {
		return Features.values().stream().filter(Feature::isEnabled).toList();
	}

	@Override
	public Feature getFeatureByName(String name) {
		return Features.values().stream().filter(Feature -> Feature.getFeatureInfo().name().equalsIgnoreCase(name)).findFirst().orElse(null);
	}

	@Override
	public Feature getFeatureFromType(Class<? extends Feature> clazz) {
		return Features.values().stream().filter(feature -> feature.getClass().equals(clazz)).findFirst().orElse(null);
	}

	@Override
	public Collection<Feature> getFeatureFromCategory(FeatureCategory category) {
		return Features.values().stream().filter(Feature -> Feature.getFeatureInfo().category().equals(category)).toList();
	}

	@Subscribe
	public void onKey(final EventKeyInput event) {
		getFeatures().forEach(Features -> {
			if (event.key() == Features.getKey())
				Features.toggle();
		});
	}

}
