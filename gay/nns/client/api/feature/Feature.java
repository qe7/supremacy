package gay.nns.client.api.feature;

import gay.nns.client.api.feature.interfaces.SerializeFeature;
import gay.nns.client.api.feature.interfaces.Toggleable;
import gay.nns.client.api.core.SupremacyCore;
import net.minecraft.client.Minecraft;

public abstract class Feature implements Toggleable {

    protected final Minecraft mc = Minecraft.getMinecraft();

    private final SerializeFeature serializeFeature;
    private boolean toggledState;
    private String suffix;
    private int key;

    protected Feature() {
        this.serializeFeature = getClass().getAnnotation(SerializeFeature.class);
        this.key = getFeatureInfo().key();
        SupremacyCore.getSingleton().getSettingManager().addToSettingManager(this);
    }

    protected void onEnable() {
        SupremacyCore.getSingleton().getEventBus().register(this);
    }

    protected void onDisable() {
        SupremacyCore.getSingleton().getEventBus().unregister(this);
    }

    public void toggle() {
        if(!getToggleable()) return;
        toggledState = !toggledState;
        if(toggledState)
            onEnable();
        else
            onDisable();
    }

    public boolean isEnabled() {
        return toggledState;
    }

    public void setEnabled(boolean enabled) {
        if(enabled)
            onEnable();
        else
            onDisable();
    }

    public SerializeFeature getFeatureInfo() {
        if (serializeFeature == null)
            return getClass().getAnnotation(SerializeFeature.class);
        return serializeFeature;
    }

    public boolean getToggleable() {
        return getFeatureInfo().toggleable();
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

}
