package gay.nns.client.impl.event.render;

import gay.nns.client.api.event.type.EventCancelable;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;

public class EventRenderItem extends EventCancelable {

    private EnumAction enumAction;

    public EventRenderItem(EnumAction enumAction, boolean useItem, float animationProgression, float partialTicks, float swingProgress, ItemStack itemToRender) {
        this.enumAction = enumAction;
        this.useItem = useItem;
        this.animationProgression = animationProgression;
        this.partialTicks = partialTicks;
        this.swingProgress = swingProgress;
        this.itemToRender = itemToRender;
    }
    private boolean useItem;

    public EnumAction getEnumAction() {
        return enumAction;
    }

    public boolean isUseItem() {
        return useItem;
    }

    public float getAnimationProgression() {
        return animationProgression;
    }

    public void setUseItem(boolean useItem) {
        this.useItem = useItem;
    }

    public void setAnimationProgression(float animationProgression) {
        this.animationProgression = animationProgression;
    }

    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public void setSwingProgress(float swingProgress) {
        this.swingProgress = swingProgress;
    }

    public void setItemToRender(ItemStack itemToRender) {
        this.itemToRender = itemToRender;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public float getSwingProgress() {
        return swingProgress;
    }

    public ItemStack getItemToRender() {
        return itemToRender;
    }

    private float animationProgression, partialTicks, swingProgress;
    private ItemStack itemToRender;

    public void setEnumAction(EnumAction enumAction) {
        this.enumAction = enumAction;
    }
}
