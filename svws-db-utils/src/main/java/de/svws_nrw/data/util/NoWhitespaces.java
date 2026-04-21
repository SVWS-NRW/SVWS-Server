package de.svws_nrw.data.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = NoWhitespacesValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoWhitespaces {

	/**
	 * Die Fehlermeldung, die bei einer Validierungsverletzung zurückgegeben wird.
	 *
	 * @return die Fehlermeldung
	 */
	String message() default "Darf keine Leerzeichen enthalten";

	/**
	 * Ermöglicht die Spezifikation von Validierungsgruppen.
	 *
	 * @return die Validierungsgruppen
	 */
	Class<?>[] groups() default {};

	/**
	 * Kann von Clients der Bean Validation API verwendet werden, um benutzerdefinierte Payload-Objekte zuzuweisen.
	 *
	 * @return die Payload
	 */
	Class<? extends Payload>[] payload() default {};
}
