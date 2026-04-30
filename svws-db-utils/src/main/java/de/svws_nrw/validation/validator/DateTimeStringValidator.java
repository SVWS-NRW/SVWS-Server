package de.svws_nrw.validation.validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

import de.svws_nrw.validation.constraints.ValidDateTime;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public final class DateTimeStringValidator implements ConstraintValidator<ValidDateTime, String> {

	private static final DateTimeFormatter FMT = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm:ss")
			.appendFraction(ChronoField.MILLI_OF_SECOND, 0, 3, true).toFormatter();

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext ctx) {
		if ((value == null) || value.isBlank()) {
			return true;
		}
		try {
			LocalDateTime.parse(value, FMT);
			return true;
		} catch (final DateTimeParseException ex) {
			return false;
		}
	}
}
