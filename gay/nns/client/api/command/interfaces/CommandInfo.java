package gay.nns.client.api.command.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandInfo {

	String name() default "ERROR: No name provided";
	String description() default "ERROR: No description provided";
	String usage() default "ERROR: No usage provided";
	String[] aliases() default {};

}
