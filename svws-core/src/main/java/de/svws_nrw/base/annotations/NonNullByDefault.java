package de.svws_nrw.base.annotations;

import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

/**
 * Die Klasse wird genutzt, um Typen zu annotieren, die nicht Null sein können.
 */
@Documented
@Retention(value = CLASS)
public @interface NonNullByDefault {
	//
}
