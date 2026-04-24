package de.svws_nrw.validation.validator;

import de.svws_nrw.validation.constraints.NoLeadingOrTrailingWhitespaces;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * Validiert, dass ein String keine führenden oder nachfolgenden Leerzeichen enthält.
 * <p>
 * Ein String gilt als gültig, sofern er weder mit einem Leerzeichen beginnt noch endet.
 * Leere oder ausschließlich aus Leerzeichen bestehende Strings werden explizit als
 * gültig gewertet.
 * </p>
 *
 * <p><b>Beispiele:</b></p>
 * <ul>
 *   <li>{@code "hallo"} — gültig</li>
 *   <li>{@code ""} — gültig (leerer String)</li>
 *   <li>{@code " "} — gültig (Blank-String)</li>
 *   <li>{@code " hallo"} — ungültig (führendes Leerzeichen)</li>
 *   <li>{@code "hallo "} — ungültig (nachfolgendes Leerzeichen)</li>
 *   <li>{@code " hallo "} — ungültig (führendes und nachfolgendes Leerzeichen)</li>
 * </ul>
 */

public final class NoLeadingOrTrailingWhitespacesValidator implements ConstraintValidator<NoLeadingOrTrailingWhitespaces, String> {

	@Override
	public void initialize(final NoLeadingOrTrailingWhitespaces constraintAnnotation) {
		// Keine Initialisierung erforderlich
	}

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		if (StringUtils.isBlank(value)) {
			return true; // null wird von @NotBlank gehandhabt
		}
		return value.matches("^\\S.*\\S$|^\\S$");
	}
}
