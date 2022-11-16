package de.nrw.schule.svws.base.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Eine Annotation, um bei einem Attribut eines Core-DTOs zu kennzeichnen, dass dieses 
 * als Memo-Feld zu interpretieren ist.  
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface SchildReportingMemo {
	/**/
}
