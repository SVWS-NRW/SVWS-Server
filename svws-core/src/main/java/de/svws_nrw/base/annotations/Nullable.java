package de.svws_nrw.base.annotations;

import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Die Klasse wird genutzt, um Typen zu annotieren, die Nullable sind.
 */
@Documented
@Retention(value=CLASS)
@Target(value=TYPE_USE)
public @interface Nullable {
	 //
}
