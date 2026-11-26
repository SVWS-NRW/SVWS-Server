package de.svws_nrw.validation.validator;

import de.svws_nrw.oauth.internal.OAuthDomain;
import de.svws_nrw.validation.constraints.ValidOAuthDomain;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator für {@link ValidOAuthDomain}.
 */
public final class OAuthDomainValidator implements ConstraintValidator<ValidOAuthDomain, String> {

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		return (value == null) || OAuthDomain.fromName(value).isPresent();
	}
}
