package gay.nns.client.util.font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UtilFont {
	public volatile int completed;

	public static UtilCustomFontRenderer menuWatermarkFont, clean, menuFont, menuFontBold;
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
		return completed >= 1;
	}

	public void initialize() {

		Executor executor = Executors.newSingleThreadExecutor();

		executor.execute(() -> {
			Map<String, Font> locationMap = new HashMap<>();

			//Fan Fonts
			clean_ = getFont(locationMap, "Roboto.ttf", 15);
			menuWatermarkFont_ = getFont(locationMap, "Roboto.ttf", 20);
			menuFont_ = getFont(locationMap, "Roboto.ttf", 13);
			menuFontBold_ = getFont(locationMap, "Roboto-Bold.ttf", 13);

			completed++;
		});

		while (!hasLoaded()) {
			try {
				//noinspection BusyWait
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		//Fan fonts
		clean = new UtilCustomFontRenderer(clean_, true, true);
		menuWatermarkFont = new UtilCustomFontRenderer(menuWatermarkFont_, true, true);
		menuFont = new UtilCustomFontRenderer(menuFont_, true, true);
		menuFontBold = new UtilCustomFontRenderer(menuFontBold_, true, true);
	}
}