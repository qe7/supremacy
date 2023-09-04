package gay.nns.client.api.feature.enums;

import java.awt.*;

public enum FeatureCategory {

    COMBAT("Combat", new Color(185, 120, 148)),
    MOVEMENT("Movement", new Color(110, 143, 179)),
    RENDER("Render", new Color(213, 161, 143)),
    OTHER("Other", new Color(222, 174, 174)),
    GHOST("Ghost", new Color(224, 217, 206)),
    EXPLOIT("Exploit", new Color(156, 173, 222));

    final String name;
    final Color color;

    FeatureCategory(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

}
