package gay.nns.client.util.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

public class ScissorUtil {

	public static void prepareScissorBox(double x, double y, double x2, double y2) {
		ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
		int scaleFactor = scaledResolution.getScaleFactor();

		int scissorX = (int) (x * scaleFactor);
		int scissorY = (int) ((scaledResolution.getScaledHeight() - y2) * scaleFactor);
		int scissorWidth = (int) ((x2 - x) * scaleFactor);
		int scissorHeight = (int) ((y2 - y) * scaleFactor);

		GL11.glScissor(scissorX, scissorY, scissorWidth, scissorHeight);
	}
}
