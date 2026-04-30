package de.svws_nrw.validation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.svws_nrw.validation.validator.DateTimeStringValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;


/**
 * Bean-Validation Constraint fuer DateTime-Strings im Format {@code yyyy-MM-dd HH:mm:ss}.
 * Die Validierung erfolgt ueber {@code DateTimeStringValidator}.
 * <p>
 * null Werte sind erlaubt
 */
@Target({ ElementType.TYPE_USE, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateTimeStringValidator.class)
public @interface ValidDateTime {

	/**
	 * Standard-Fehlermeldung bei ungueltigem Wert.
	 *
	 * @return Message-Template
	 */
	String message() default "muss dem Format yyyy-MM-dd HH:mm:ss entsprechen";

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

