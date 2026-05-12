package de.svws_nrw.validation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.svws_nrw.validation.validator.NumericStringValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Validiert, dass ein String ausschließlich aus Ziffern besteht und somit
 * in einen numerischen Typ (z. B. {@code int} oder {@code long}) umgewandelt werden kann.
 * <p>
 * {@code null} und leere Strings werden als gültig betrachtet –
 * die Pflichtfeldprüfung obliegt {@code @NotBlank} bzw. {@code @NotNull}.
 * </p>
 */
@Documented
@Constraint(validatedBy = NumericStringValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface NumericString {

	/**
	 * Die Fehlermeldung, die bei einer Validierungsverletzung zurückgegeben wird.
	 *
	 * @return die Fehlermeldung
	 */
	String message() default "Der Wert darf nur Ziffern enthalten";

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
