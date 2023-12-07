package gay.nns.client.impl.feature.combat;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.SerializeFeature;
import gay.nns.client.api.setting.annotations.SettingMode;
import gay.nns.client.api.setting.annotations.SerializeSetting;
import gay.nns.client.impl.event.player.EventUpdate;
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
