package de.svws_nrw.davapi.api;

import jakarta.ws.rs.HttpMethod;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Interface für Annotation zur Erweiterung des APIs um die DAV-spezifische
 * HTTP-Methode PROPFIND. Mit PROPFIND können Eigenschaften einer Ressource
 * abgefragt werden.
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@HttpMethod("PROPFIND")
public @interface PROPFIND {
	// kein code nötig
}
