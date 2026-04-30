package de.svws_nrw.service.wiedervorlage.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validierungsannotation für die Cross-Field-Abhängigkeit zwischen typPerson und idPerson.
 * Beide Felder müssen entweder beide gesetzt oder beide null sein.
 * Wenn gesetzt, muss typPerson zwischen 1 und 4 liegen.
 */
@Documented
@Constraint(validatedBy = ValidPersonTypAndIdValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPersonTypAndId {

    /** @return die Fehlermeldung */
    String message() default "typPerson und idPerson müssen beide gesetzt oder beide null sein, und typPerson muss zwischen 1 und 4 liegen";

    /** @return die Gruppen */
    Class<?>[] groups() default {};

    /** @return die Payload */
    Class<? extends Payload>[] payload() default {};
}


