package gay.nns.client.api.feature.interfaces;

public interface Feature {

    FeatureInfo getFeatureInfo();
    boolean isEnabled();
    void setEnabled(boolean enabled);
    String getSuffix();
    void setSuffix(String suffix);
    int getKey();
    void setKey(int key);

}
