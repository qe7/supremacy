package gay.nns.client.impl.feature.combat;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.Mode;
import gay.nns.client.api.setting.annotations.Serialize;
import gay.nns.client.impl.event.player.UpdateEvent;
import net.minecraft.entity.Entity;

@FeatureInfo(name = "AntiBot", category = FeatureCategory.COMBAT, description = "Prevents Killaura from hitting bots")
public class FeatureAntiBot extends AbstractFeature {

    @Serialize(name = "Mode")
    @Mode(modes = {"None", "Hypixel"})
    public String antiBotMode = "None";

    @Override
    protected void onEnable() {
        super.onEnable();
    }

    @Override
    protected void onDisable() {
        super.onDisable();
    }

    @Subscribe
    public void eventUpdate(final UpdateEvent event) {

        switch (antiBotMode) {
            case "Hypixel" -> {
                for (Object entity : this.mc.theWorld.loadedEntityList) {
                    if (((Entity) entity).isInvisible() && (Entity) entity != this.mc.thePlayer) {
                        this.mc.theWorld.removeEntity((Entity) entity);
                    }
                }
            }
        }
    }
}
