package gay.nns.client.feature.impl.render;


import gay.nns.client.event.api.interfaces.Subscribe;
import gay.nns.client.feature.api.types.Feature;
import gay.nns.client.feature.api.enums.FeatureCategory;
import gay.nns.client.feature.api.interfaces.SerializeFeature;
import gay.nns.client.setting.api.annotations.SerializeSetting;
import gay.nns.client.setting.api.annotations.SettingMode;
import gay.nns.client.event.impl.render.Event3DRender;
import gay.nns.client.event.impl.render.EventRender2D;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

@SerializeFeature(name = "ESP", category = FeatureCategory.RENDER, description = "Blah blah blah this isnt done")
public class FeatureESP extends Feature {

    @SerializeSetting(name = "Mode")
    @SettingMode(modes = {"Bounding_Box", "Box"})
    public static String mode = "Box";
    public FeatureESP() {
        super();
    }


    @Subscribe
    public void onRender3D(Event3DRender event) {

        switch (mode) {
            case "Bounding_Box": {
                for (Entity entity : mc.theWorld.loadedEntityList) {
                    if (entity == mc.thePlayer || !(entity instanceof EntityPlayer)) continue;
                    double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) event.getPartialTicks() - this.mc.getRenderManager().renderPosX;
                    double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) event.getPartialTicks() - this.mc.getRenderManager().renderPosY;
                    double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) event.getPartialTicks() - this.mc.getRenderManager().renderPosZ;
                    drawEntityESP(posX, posY, posZ, 0.4, entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY, 0 / 255f, 255 / 255f, 50 / 255f, 0.2f);
                }
                break;
            }
        }
    }

    @Subscribe
    public void onEventRender2D(EventRender2D event) {
    }

    public static void drawEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha) {
        switch (mode) {
            case "Bounding_Box": {
                GL11.glPushMatrix();
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);

                GL11.glDisable(3553);
                GL11.glEnable(2848);
                GL11.glDisable(2929);
                GL11.glDepthMask(false);
                GL11.glColor4f(red, green, blue, alpha);
                drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
                GL11.glDisable(2848);
                GL11.glEnable(3553);
                GL11.glEnable(2929);
                GL11.glDepthMask(true);
                GL11.glDisable(3042);
                GL11.glPopMatrix();
            }
        }
    }

    public static void drawBoundingBox(AxisAlignedBB aa) {
        switch (mode) {
            case "Bounding_Box": {
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldRenderer = tessellator.getWorldRenderer();
                worldRenderer.begin(1, DefaultVertexFormats.POSITION);
                worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
                worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
                worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
                worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
                worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
                worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
                worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
                worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
                tessellator.draw();
                worldRenderer.begin(1, DefaultVertexFormats.POSITION);
                worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
                worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
                worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
                worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
                worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
                worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
                worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
                worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
                tessellator.draw();
                worldRenderer.begin(1, DefaultVertexFormats.POSITION);
                worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
                worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
                worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
                worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
                worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
                worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
                worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
                worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
                tessellator.draw();
                worldRenderer.begin(1, DefaultVertexFormats.POSITION);
                worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
                worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
                worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
                worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
                worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
                worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
                worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
                worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
                tessellator.draw();
                worldRenderer.begin(1, DefaultVertexFormats.POSITION);
                worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
                worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
                worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
                worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
                worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
                worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
                worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
                worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
                tessellator.draw();
                worldRenderer.begin(1, DefaultVertexFormats.POSITION);
                worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
                worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
                worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
                worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
                worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
                worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
                worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
                worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
                tessellator.draw();

            }
        }
    }
}

