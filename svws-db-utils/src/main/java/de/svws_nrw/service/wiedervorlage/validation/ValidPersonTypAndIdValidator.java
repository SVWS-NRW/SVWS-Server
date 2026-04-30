package de.svws_nrw.service.wiedervorlage.validation;

import de.svws_nrw.service.wiedervorlage.WiedervorlageCreateRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator für {@link ValidPersonTypAndId}.
 * Prüft die Cross-Field-Abhängigkeit zwischen typPerson und idPerson.
 */
public final class ValidPersonTypAndIdValidator implements ConstraintValidator<ValidPersonTypAndId, Object> {

	@Override
	public boolean isValid(final Object value, final ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}

		final Integer typPerson;
		final Long idPerson;

		if (value instanceof final WiedervorlageCreateRequest req) {
			typPerson = req.typPerson;
			idPerson = req.idPerson;

			final boolean typPresent = typPerson != null;
			final boolean idPresent = idPerson != null;

			if (typPresent != idPresent) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate(
						"typPerson und idPerson müssen beide gesetzt oder beide null sein"
				).addConstraintViolation();
				return false;
			}
		}
		return true;
	}
}
