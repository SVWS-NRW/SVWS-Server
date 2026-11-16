package de.svws_nrw.validation.validator;

import de.svws_nrw.validation.constraints.NotBlankIfPresent;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator für die {@link NotBlankIfPresent}-Annotation.
 * <p>
 * Prüft, dass ein Text, sofern er angegeben ist, mindestens ein Zeichen enthält, das kein Leerraum ist.
 * {@code null} ist gültig, damit die Annotation für optionale Attribute verwendet werden kann.
 * </p>
 *
 * <p><b>Beispiele:</b></p>
 * <ul>
 *   <li>{@code null} — gültig (nicht angegeben)</li>
 *   <li>{@code "hallo"} — gültig</li>
 *   <li>{@code " hallo "} — gültig (Leerzeichen am Rand werden nicht geprüft)</li>
 *   <li>{@code "hallo welt"} — gültig</li>
 *   <li>{@code ""} — ungültig (leerer String)</li>
 *   <li>{@code "   "} — ungültig (nur Leerraum)</li>
 *   <li>{@code "\t\n"} — ungültig (nur Leerraum)</li>
 * </ul>
 */
public final class NotBlankIfPresentValidator implements ConstraintValidator<NotBlankIfPresent, String> {

	@Override
	public void initialize(final NotBlankIfPresent constraintAnnotation) {
		// Keine Initialisierung erforderlich
	}

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		return (value == null) || !value.isBlank();
	}

}
