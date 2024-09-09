package de.svws_nrw.transpiler.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**
 * This annotation is used to annotate type parameterts. It explicitly states
 * that the type parameter can also be a null value, so that the type in
 * typescript is transpiled as "TYPE | null".
 */
@Target({ ElementType.TYPE_PARAMETER, ElementType.TYPE_USE })
@Retention(RUNTIME)
public @interface AllowNull {
	// nur eine einfache Annotation
}
