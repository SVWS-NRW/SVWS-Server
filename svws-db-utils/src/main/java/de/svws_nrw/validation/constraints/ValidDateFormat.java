package de.svws_nrw.validation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.svws_nrw.validation.validator.ValidDateFormatValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = ValidDateFormatValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateFormat {

	/**
	 * Das erwartete Datumsformat (Standard: yyyy-MM-dd)
	 *
	 * @return das Datumsformat
	 */
	String pattern() default "uuuu-MM-dd";

	/**
	 * Die Fehlermeldung, die bei einer Validierungsverletzung zurückgegeben wird.
	 *
	 * @return die Fehlermeldung
	 */
	String message() default "Datum entspricht nicht dem erwarteten Format {pattern}";

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
