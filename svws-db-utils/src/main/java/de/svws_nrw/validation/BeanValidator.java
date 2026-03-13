package de.svws_nrw.validation;

import java.util.List;
import java.util.Set;

import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.ws.rs.core.Response;

/**
 * Utility Klasse für manuelle jakarta bean validation mit Hilfe von Jakarta Validation Constraints.
 * Siehe <a href="https://jakarta.ee/specifications/bean-validation/3.1/apidocs/jakarta/validation/constraints/package-summary">Jakarta Validation Constraints</a>
 */
public final class BeanValidator {

	private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
	private static final Validator VALIDATOR = VALIDATOR_FACTORY.getValidator();

	private BeanValidator() {
		//static only
	}

	/**
	 * Validiert das übergebene DTO gegen die jakarta bean validation
	 * @param input zu überprüfendes DTO
	 * @param <T> Generic Type
	 */
	public static <T> void validate(final T input) {
		final Set<ConstraintViolation<T>> violations = VALIDATOR.validate(input);
		if (!violations.isEmpty()) {
			final List<String> errors = violations.stream()
					.map(v -> String.format("%s: %s.", v.getPropertyPath(), v.getMessage()))
					.sorted()
					.toList();

			throw new ApiOperationException(Response.Status.BAD_REQUEST, String.join(" ", errors));
		}
	}

}
