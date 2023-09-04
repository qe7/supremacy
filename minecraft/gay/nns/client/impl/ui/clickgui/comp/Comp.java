package gay.nns.client.impl.ui.clickgui.comp;

import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.impl.ui.clickgui.ClickGUI;

public abstract class Comp<T> {

    public double width, height;
    public ClickGUI clickGUI;
    public AbstractFeature abstractFeature;
    public T setting;

    public Comp(ClickGUI clickGUI, AbstractFeature abstractFeature, T setting) {
        this.clickGUI = clickGUI;
        this.abstractFeature = abstractFeature;
        this.setting = setting;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton, float x, float y) {

    }

    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    public void drawScreen(int mouseX, int mouseY, float x, float y) {

    }

    public void keyTyped(char typedChar, int keyCode) {

    }

    public boolean isInside(int mouseX, int mouseY, double x, double y, double x2, double y2) {
        return (mouseX > x && mouseX < x2) && (mouseY > y && mouseY < y2);
    }

    public abstract float getHeight();

}
