package de.svws_nrw.validation.validator;

import static org.assertj.core.api.Assertions.assertThat;

import de.svws_nrw.oauth.internal.OAuthDomain;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("OAuthDomainValidator")
class OAuthDomainValidatorTest {

	@Mock
	private ConstraintValidatorContext context;

	private final OAuthDomainValidator validator = new OAuthDomainValidator();

	@ParameterizedTest(name = "{0}")
	@EnumSource(OAuthDomain.class)
	@DisplayName("isValid | gueltig bei bekannter Domaene")
	void isValidBekannteDomain(final OAuthDomain domain) {
		assertThat(validator.isValid(domain.name(), context)).isTrue();
	}

	@ParameterizedTest
	@NullSource
	@DisplayName("isValid | null ist gueltig, die Pflicht prueft @NotNull")
	void isValidNull(final String value) {
		assertThat(validator.isValid(value, context)).isTrue();
	}

	@ParameterizedTest(name = "value = ''{0}''")
	@ValueSource(strings = {"", "  ", "UNBEKANNT", "it_nrw"})
	@DisplayName("isValid | ungueltig bei unbekannter Domaene")
	void isValidUnbekannteDomain(final String value) {
		assertThat(validator.isValid(value, context)).isFalse();
	}
}
