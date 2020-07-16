package qslv.util;

import java.lang.annotation.ElementType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * Add this annotation to a Spring Configuration Class to import the QuickSilver Configuraiton and its annotations.
 * 
 *  You must: 
 * @author SMS
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(QuickSilverConfiguration.class)
public @interface EnableQuickSilver {
}
