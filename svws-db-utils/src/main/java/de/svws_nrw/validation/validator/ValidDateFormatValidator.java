package de.svws_nrw.validation.validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import de.svws_nrw.validation.constraints.ValidDateFormat;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * Validiert, dass ein String einem bestimmten Datumsformat entspricht.
 * <p>
 * Ein String gilt als gültig, wenn er null, leer ist oder dem angegebenen
 * Datumsformat entspricht und ein gültiges Datum repräsentiert.
 * </p>
 *
 * <p><b>Beispiele (für Format yyyy-MM-dd):</b></p>
 * <ul>
 *   <li>{@code "2023-12-01"} — gültig</li>
 *   <li>{@code ""} — gültig (leerer String)</li>
 *   <li>{@code null} — gültig (wird von @NotNull gehandhabt)</li>
 *   <li>{@code "2023-13-01"} — ungültig (ungültiger Monat)</li>
 *   <li>{@code "01.12.2023"} — ungültig (falsches Format)</li>
 *   <li>{@code "2023-02-30"} — ungültig (ungültiges Datum)</li>
 * </ul>
 */
public final class ValidDateFormatValidator implements ConstraintValidator<ValidDateFormat, String> {

	private DateTimeFormatter formatter;

	@Override
	public void initialize(final ValidDateFormat constraintAnnotation) {
		this.formatter = DateTimeFormatter.ofPattern(constraintAnnotation.pattern())
				.withResolverStyle(ResolverStyle.STRICT);
	}

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		if (StringUtils.isBlank(value)) {
			return true; // null oder leer wird von @NotNull/@NotBlank gehandhabt
		}

		try {
			LocalDate.parse(value, formatter);
			return true;
		} catch (final DateTimeParseException e) {
			return false;
		}
	}
}
