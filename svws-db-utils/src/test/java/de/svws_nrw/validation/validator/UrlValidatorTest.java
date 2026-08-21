package de.svws_nrw.validation.validator;

import de.svws_nrw.validation.constraints.ValidUrl;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@ExtendWith(MockitoExtension.class)
class UrlValidatorTest {

	@Mock
	private ConstraintValidatorContext context;

	@InjectMocks
	private UrlValidator validator;

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {
			"",
			"   ",
			"https://example.com",
			"https://example.com/oauth2/token",
			"https://example.com:8443/oauth2/token?foo=bar",
			"https://user:pass@example.com/path",
			"https://[::1]:8080/token"
	})
	@DisplayName("isValid | Gültige Werte")
	void isValid_validValues(final String value) {
		final var result = validator.isValid(value, context);

		assertThat(result).isTrue();
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"\\w",
			"foo\\bar",
			"not a uri",
			"just-a-word",
			"relative/path",
			"a",
			"http://example.com",
			"ftp://example.com/token",
			"javascript:alert(1)",
			"urn:isbn:0451450523",
			"://missing-scheme",
			"https://",
			"https:///path",
			"%zz",
			"https:// example.com"
	})
	@DisplayName("isValid | Ungültige Werte")
	void isValid_invalidValues(final String value) {
		final var result = validator.isValid(value, context);

		assertThat(result).isFalse();
	}

	@Test
	@DisplayName("initialize | Keine Exception")
	void initialize_noException() {
		final var annotation = createMockAnnotation();

		assertThatNoException().isThrownBy(() -> validator.initialize(annotation));
	}

	private ValidUrl createMockAnnotation() {
		return new ValidUrl() {
			@Override
			public Class<? extends java.lang.annotation.Annotation> annotationType() {
				return ValidUrl.class;
			}

			@Override
			public String message() {
				return "invalide URL";
			}

			@Override
			public Class<?>[] groups() {
				return new Class<?>[0];
			}

			@Override
			public Class<? extends jakarta.validation.Payload>[] payload() {
				return new Class[0];
			}
		};
	}
}
