package gay.nns.client.util.font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FontUtil {
	public volatile int completed;

	public MinecraftFontRenderer menuWatermarkFont, clean, menuFont, menuFontBold;
	private Font menuWatermarkFont_, clean_, menuFont_, menuFontBold_;

	private Font getFont(Map<String, Font> locationMap, String location, int size) {
		Font font;

		try {
			if (locationMap.containsKey(location)) {
				font = locationMap.get(location).deriveFont(Font.PLAIN, size);
			} else {
				InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("sup/font/" + location)).getInputStream();
				font = Font.createFont(0, is);
				locationMap.put(location, font);
				font = font.deriveFont(Font.PLAIN, size);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error loading font");
			font = new Font("default", Font.PLAIN, +10);
		}

		return font;
	}

	public boolean hasLoaded() {
		return completed >= 3;
	}

	public void initialize() {
		new Thread(() -> {
			Map<String, Font> locationMap = new HashMap<>();

			//Fan Fonts
			clean_ = getFont(locationMap, "Roboto.ttf", 15);
			menuWatermarkFont_ = getFont(locationMap, "Roboto.ttf", 20);
			menuFont_ = getFont(locationMap, "Roboto.ttf", 13);
			menuFontBold_ = getFont(locationMap, "Roboto-Bold.ttf", 13);

			completed++;
		}).start();
		new Thread(() -> {
			completed++;
		}).start();
		new Thread(() -> {
			completed++;
		}).start();

		while (!hasLoaded()) {
			try {
				//noinspection BusyWait
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		//Fan fonts
		clean = new MinecraftFontRenderer(clean_, true, true);
		menuWatermarkFont = new MinecraftFontRenderer(menuWatermarkFont_, true, true);
		menuFont = new MinecraftFontRenderer(menuFont_, true, true);
		menuFontBold = new MinecraftFontRenderer(menuFontBold_, true, true);

	}

	public MinecraftFontRenderer getFont(String font) {
		return switch (font.toLowerCase()) {
			case "clean" -> clean;
			case "menuwatermark" -> menuWatermarkFont;
			case "menu" -> menuFont;
			case "menubold" -> menuFontBold;
			default -> clean;
		};
	}

}