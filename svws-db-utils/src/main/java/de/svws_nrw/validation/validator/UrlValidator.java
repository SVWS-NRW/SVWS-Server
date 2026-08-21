package de.svws_nrw.validation.validator;

import java.net.URI;
import java.net.URISyntaxException;

import de.svws_nrw.validation.constraints.ValidUrl;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public final class UrlValidator implements ConstraintValidator<ValidUrl, String> {

	@Override
	public void initialize(final ValidUrl constraintAnnotation) {
		//kein init notwendig
	}

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		if ((value == null) || value.isBlank()) {
			return true;
		}

		final URI uri;
		try {
			uri = new URI(value);
		} catch (final URISyntaxException e) {
			return false;
		}

		if (!uri.isAbsolute()) {
			return false;
		}

		if (!"https".equalsIgnoreCase(uri.getScheme())) {
			return false;
		}

		return (uri.getHost() != null) && !uri.getHost().isBlank();
	}
}
