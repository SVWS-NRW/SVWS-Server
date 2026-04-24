package de.svws_nrw.validation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.svws_nrw.validation.validator.NoLeadingOrTrailingWhitespacesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = NoLeadingOrTrailingWhitespacesValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoLeadingOrTrailingWhitespaces {

	/**
	 * Die Fehlermeldung, die bei einer Validierungsverletzung zurückgegeben wird.
	 *
	 * @return die Fehlermeldung
	 */
	String message() default "Bezeichnung darf keine führenden oder nachfolgenden Leerzeichen enthalten";

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
