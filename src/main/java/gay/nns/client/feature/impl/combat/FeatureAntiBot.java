package gay.nns.client.feature.impl.combat;

import gay.nns.client.event.api.interfaces.Subscribe;
import gay.nns.client.feature.api.types.Feature;
import gay.nns.client.feature.api.enums.FeatureCategory;
import gay.nns.client.feature.api.interfaces.SerializeFeature;
import gay.nns.client.setting.api.annotations.SettingMode;
import gay.nns.client.setting.api.annotations.SerializeSetting;
import gay.nns.client.event.impl.player.EventUpdate;
import net.minecraft.entity.Entity;

@SerializeFeature(name = "AntiBot", category = FeatureCategory.COMBAT, description = "Prevents Killaura from hitting bots")
public class FeatureAntiBot extends Feature {

    @SerializeSetting(name = "Mode")
    @SettingMode(modes = {"Hypixel", "Novoline (troll)"})
    public String antiBotMode = "Hypixel";

    @Override
    protected void onEnable() {
        super.onEnable();
    }

    @Override
    protected void onDisable() {
        super.onDisable();
    }

    @Subscribe
    public void eventUpdate(final EventUpdate event) {

        switch (antiBotMode.toLowerCase()) {
            case "hypixel": {
                for (Entity entity : this.mc.theWorld.loadedEntityList) {
                    if (entity.isInvisible() && entity.ticksExisted < 40 && entity != this.mc.thePlayer) {
                        this.mc.theWorld.removeEntity(entity);
                    }
                }
                break;
            }
        }
        this.setSuffix(antiBotMode);
    }
}
