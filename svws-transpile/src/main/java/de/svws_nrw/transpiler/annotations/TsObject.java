package de.svws_nrw.transpiler.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**
 * This annotation is used to annotate type parameterts. It explicitly states
 * that the type parameter is transpiled to a Typescript object type and not
 * to string or number, so that the object type in Typescript
 * extends JavaObject.
 */
@Target({ ElementType.TYPE_PARAMETER, ElementType.TYPE_USE })
@Retention(RUNTIME)
public @interface TsObject {
	// nur eine einfache Annotation
}
