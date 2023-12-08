package gay.nns.client.management;

import gay.nns.client.feature.impl.bot.FeatureFightBot;
import gay.nns.client.feature.impl.combat.FeatureKillAura;
import gay.nns.client.feature.impl.combat.FeatureVelocity;
import gay.nns.client.feature.impl.exploit.FeatureAntiVoid;
import gay.nns.client.feature.impl.exploit.FeatureDisabler;
import gay.nns.client.feature.impl.exploit.FeatureFastBow;
import gay.nns.client.feature.impl.ghost.FeatureAimAssist;
import gay.nns.client.feature.impl.ghost.FeatureAutoClicker;
import gay.nns.client.feature.impl.ghost.FeatureNoClickDelay;
import gay.nns.client.feature.impl.movement.*;
import gay.nns.client.feature.impl.other.*;
import gay.nns.client.feature.impl.render.*;
import gay.nns.client.event.api.interfaces.Subscribe;
import gay.nns.client.feature.api.types.Feature;
import gay.nns.client.feature.api.enums.FeatureCategory;
import gay.nns.client.event.impl.game.EventKeyInput;

import gay.nns.client.feature.impl.combat.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ManagerFeature {

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
				new FeatureSpider(),

				/* Render */
				new FeatureAnimation(),
				new FeatureViewModel(),
				new FeatureInterface(),
				new FeatureFakePlayer(),
				new FeatureAmbience(),
				new FeatureBrightness(),
				new FeatureESP(),
				new FeatureRotate(),
				new FeatureTargetHud(),

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
				new FeatureAutoPlay(),
				new FeatureKeepSprint(),
				new FeatureTimer(),

				/* Bot */
				new FeatureFightBot(),

				/* Exploit */
				new FeatureFastBow(),
				new FeatureAntiVoid(),
				new FeatureDisabler(),

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

	public Collection<Feature> getFeatures() {
		return Features.values();
	}

	public Collection<Feature> getEnabledFeatures() {
		return Features.values().stream().filter(Feature::isEnabled).collect(Collectors.toList());
	}

	public Feature getFeatureFromType(Class<? extends Feature> clazz) {
		return Features.values().stream().filter(feature -> feature.getClass().equals(clazz)).findFirst().orElse(null);
	}
	
	public Collection<Feature> getFeatureFromCategory(FeatureCategory category) {
		return Features.values().stream().filter(Feature -> Feature.getFeatureInfo().category().equals(category)).collect(Collectors.toList());
	}

	@Subscribe
	public void onKey(final EventKeyInput event) {
		getFeatures().forEach(Features -> {
			if (event.getKey() == Features.getKey())
				Features.toggle();
		});
	}

}
