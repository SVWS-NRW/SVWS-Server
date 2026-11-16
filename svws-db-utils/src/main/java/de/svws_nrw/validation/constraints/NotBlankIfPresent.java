package de.svws_nrw.validation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.svws_nrw.validation.validator.NotBlankIfPresentValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Prüft, dass ein optionaler Text, sofern er angegeben ist, mindestens ein Zeichen enthält, das kein Leerraum ist.
 * Im Gegensatz zu {@code @NotBlank} ist {@code null} gültig, sodass die Annotation für optionale Attribute verwendet
 * werden kann. Die Pflichtprüfung obliegt {@code @NotNull} bzw. {@code @NotBlank}.
 */
@Documented
@Constraint(validatedBy = NotBlankIfPresentValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlankIfPresent {

	/**
	 * Die Fehlermeldung, die bei einer Validierungsverletzung zurückgegeben wird.
	 *
	 * @return die Fehlermeldung
	 */
	String message() default "darf nicht leer sein";

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
