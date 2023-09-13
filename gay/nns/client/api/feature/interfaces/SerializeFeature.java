package gay.nns.client.api.feature.interfaces;

import gay.nns.client.api.feature.enums.FeatureCategory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SerializeFeature {

    String name() default "ERROR: No name provided";
    String description() default "ERROR: No description provided";
    FeatureCategory category() default FeatureCategory.OTHER;
    int key() default 0;
    boolean toggleable() default true;

}
