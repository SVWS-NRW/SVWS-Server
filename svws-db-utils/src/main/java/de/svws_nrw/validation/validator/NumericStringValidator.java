package de.svws_nrw.validation.validator;

import de.svws_nrw.validation.constraints.NumericString;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validiert, dass ein String ausschließlich aus Ziffern (0–9) besteht.
 * <p>
 * Ein gültiger String kann anschließend verlustfrei in einen numerischen Typ
 * wie {@code int} oder {@code long} umgewandelt werden.
 * </p>
 *
 * <p><b>Beispiele:</b></p>
 * <ul>
 *   <li>{@code "123"} — gültig</li>
 *   <li>{@code "0"} — gültig</li>
 *   <li>{@code null} — gültig (wird von {@code @NotNull} gehandhabt)</li>
 *   <li>{@code ""} — gültig (wird von {@code @NotBlank} gehandhabt)</li>
 *   <li>{@code "12.3"} — ungültig (Dezimalpunkt)</li>
 *   <li>{@code "-5"} — ungültig (Vorzeichen)</li>
 *   <li>{@code "12 3"} — ungültig (Leerzeichen)</li>
 *   <li>{@code "abc"} — ungültig (Buchstaben)</li>
 * </ul>
 */
public final class NumericStringValidator implements ConstraintValidator<NumericString, String> {

	@Override
	public void initialize(final NumericString constraintAnnotation) {
		// Keine Initialisierung erforderlich
	}

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		if ((value == null) || value.isEmpty()) {
			return true; // null/leer wird von @NotNull/@NotBlank gehandhabt
		}
		try {
			Long.parseLong(value);
		} catch (final NumberFormatException ignored) {
			return false;
		}
		return value.chars().allMatch(Character::isDigit);
	}
}
