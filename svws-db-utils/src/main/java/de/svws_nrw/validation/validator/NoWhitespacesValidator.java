package de.svws_nrw.validation.validator;

import de.svws_nrw.validation.constraints.NoWhitespaces;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * Validator für die {@link NoWhitespaces}-Annotation.
 * <p>
 * Prüft, ob ein String keinerlei Whitespace-Zeichen enthält. Dabei werden sämtliche
 * Whitespace-Zeichen berücksichtigt (z. B. Leerzeichen, Tabs, Zeilenumbrüche).
 * {@code null}-Werte sowie leere Strings werden als gültig betrachtet.
 * </p>
 *
 * <p><b>Beispiele:</b></p>
 * <ul>
 *   <li>{@code "hallo"} — gültig</li>
 *   <li>{@code ""} — gültig (leerer String)</li>
 *   <li>{@code null} — gültig</li>
 *   <li>{@code "hallo welt"} — ungültig (Leerzeichen)</li>
 *   <li>{@code "hallo\t"} — ungültig (Tab)</li>
 *   <li>{@code "hallo\n"} — ungültig (Zeilenumbruch)</li>
 * </ul>
 *
 */
public final class NoWhitespacesValidator implements ConstraintValidator<NoWhitespaces, String> {

	@Override
	public void initialize(final NoWhitespaces constraintAnnotation) {
		// Keine Initialisierung erforderlich
	}

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		if (StringUtils.isBlank(value)) {
			return true; // null wird von @NotBlank gehandhabt
		}
		return value.matches("^\\S+$");
	}
}
