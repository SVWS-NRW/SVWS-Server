package de.svws_nrw.validation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.svws_nrw.validation.validator.UrlValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.TYPE_USE, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UrlValidator.class)
public @interface ValidUrl {

	/**
	 * Standard-Fehlermeldung bei ungueltigem Wert.
	 *
	 * @return Message-Template
	 */
	String message() default "invalide URL";

	/**
	 * Bean-Validation Gruppen.
	 *
	 * @return Gruppenklassen
	 */
	Class<?>[] groups() default {};

	/**
	 * Payload fuer Clients der Bean Validation API.
	 *
	 * @return Payload-Typen
	 */
	Class<? extends Payload>[] payload() default {};
}
