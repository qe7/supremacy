package gay.nns.client.util.render;

import gay.nns.client.api.feature.Feature;

import java.awt.*;

public class UtilColor {

	public static Color getColor(Feature feature, int index, String colorMode, Color color, float saturation, float brightness) {
		switch (colorMode) {
			case "Default" -> {
				return color;
			}
			case "Category" -> {
				return feature.getFeatureInfo().category().getColor();
			}
			case "Rainbow" -> {
				return generateRainbowColor(index, saturation, brightness);
			}
			case "Astolfo" -> {
				return generateAstolfoColor(index, saturation, brightness);
			}
		}
		return Color.white;
	}

	public static Color getColor(String colorMode, int index, Color color, float saturation, float brightness) {
		switch (colorMode) {
			case "Default" -> {
				return color;
			}
			case "Category" -> {
				return Color.white;
			}
			case "Rainbow" -> {
				return generateRainbowColor(index, saturation, brightness);
			}
			case "Astolfo" -> {
				return generateAstolfoColor(index, saturation, brightness);
			}
		}
		return Color.white;
	}

	private static Color generateRainbowColor(int i, float saturation, float brightness) {
		float hue = (System.currentTimeMillis() + i) % 4000;
		hue /= 4000f;
		return Color.getHSBColor(hue, saturation, brightness);
	}

	private static Color generateAstolfoColor(int i, float saturation, float brightness) {
		float speed = 4000f;
		float hue = (float) (System.currentTimeMillis() % 4000) + (i);
		while (hue > speed) {
			hue -= speed;
		}
		hue /= speed;
		if (hue > 0.5) {
			hue = 0.5f - (hue - 0.5f);
		}
		hue += 0.5f;
		return Color.getHSBColor(hue, saturation, brightness);
	}

}
