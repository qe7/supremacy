package gay.nns.client.api.feature;

import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.feature.interfaces.Toggleable;
import gay.nns.client.api.core.SupremacyCore;
import net.minecraft.client.Minecraft;

public abstract class Feature implements gay.nns.client.api.feature.interfaces.Feature, Toggleable {

    protected final Minecraft mc = Minecraft.getMinecraft();

    private final FeatureInfo featureInfo;
    private boolean toggledState;
    private String suffix;
    private int key;

    protected Feature() {
        this.featureInfo = getClass().getAnnotation(FeatureInfo.class);
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

    @Override
    public boolean isEnabled() {
        return toggledState;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if(enabled)
            onEnable();
        else
            onDisable();
    }

    @Override
    public FeatureInfo getFeatureInfo() {
        if (featureInfo == null)
            return getClass().getAnnotation(FeatureInfo.class);
        return featureInfo;
    }

    @Override
    public boolean getToggleable() {
        return getFeatureInfo().toggleable();
    }

    @Override
    public String getSuffix() {
        return suffix;
    }

    @Override
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public int getKey() {
        return key;
    }

    @Override
    public void setKey(int key) {
        this.key = key;
    }

}
