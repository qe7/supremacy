package gay.nns.client.api.event.interfaces;

import gay.nns.client.api.event.enums.EventPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Subscribe {

	EventPriority priority() default EventPriority.NORMAL;

}
