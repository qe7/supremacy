package gay.nns.client.api.feature;

import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.feature.interfaces.IFeature;
import gay.nns.client.api.feature.interfaces.IToggleable;
import gay.nns.client.api.core.Core;
import net.minecraft.client.Minecraft;

public abstract class AbstractFeature implements IFeature, IToggleable {

    protected final Minecraft mc = Minecraft.getMinecraft();

    private final FeatureInfo featureInfo;
    private boolean toggledState;
    private String suffix;
    private int key;

    protected AbstractFeature() {
        this.featureInfo = getClass().getAnnotation(FeatureInfo.class);
        this.key = getFeatureInfo().key();
        Core.getSingleton().getSettingManager().addToSettingManager(this);
    }

    protected void onEnable() {
        Core.getSingleton().getEventBus().register(this);
    }

    protected void onDisable() {
        Core.getSingleton().getEventBus().unregister(this);
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
