package qslv.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Logs elapsed time of method execution as an SLI.
 * @author SMS
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExternalResourceSLI {
	public String value();
	public String ait();
	public Class<? extends Throwable>[] remoteFailures();  
	public Class<?> logScope() default ExternalResourceSLI.class;
}
