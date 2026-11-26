package de.svws_nrw.validation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.svws_nrw.validation.validator.OAuthDomainValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Prüft, dass ein String dem Namen einer existierenden OAuth-Domäne entspricht. {@code null} gilt als gültig und
 * wird bei Bedarf über {@code @NotNull} geprüft.
 */
@Target({ ElementType.TYPE_USE, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OAuthDomainValidator.class)
public @interface ValidOAuthDomain {

	/**
	 * Standard-Fehlermeldung bei ungültigem Wert.
	 *
	 * @return Message-Template
	 */
	String message() default "unbekannte OAuth-Domäne";

	/**
	 * Bean-Validation Gruppen.
	 *
	 * @return Gruppenklassen
	 */
	Class<?>[] groups() default {};

	/**
	 * Payload für Clients der Bean Validation API.
	 *
	 * @return Payload-Typen
	 */
	Class<? extends Payload>[] payload() default {};
}
