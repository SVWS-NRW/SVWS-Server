package de.svws_nrw.validation.validator;

import de.svws_nrw.validation.constraints.NoWhitespaces;
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
class NoWhitespacesValidatorTest {

	@Mock
	private ConstraintValidatorContext context;

	@InjectMocks
	private NoWhitespacesValidator validator;

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {
			"",
			"   ",
			"TestString",
			"Test123",
			"Test-String_123"
	})
	@DisplayName("isValid | Gültige Strings")
	void isValid_validStrings(final String value) {
		final var result = validator.isValid(value, context);

		assertThat(result).isTrue();
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"Test String",
			" TestString",
			"TestString ",
			"Test   String",
			"Test\tString",
			"Test\nString",
			"Test\rString"
	})
	@DisplayName("isValid | Ungültige Strings")
	void isValid_invalidStrings(final String value) {
		final var result = validator.isValid(value, context);

		assertThat(result).isFalse();
	}

	@Test
	@DisplayName("initialize | Keine Exception")
	void initialize_noException() {
		final var annotation = createMockAnnotation();

		assertThatNoException().isThrownBy(() -> validator.initialize(annotation));

		// Keine Assertion nötig - Test prüft nur, dass keine Exception geworfen wird
	}

	private NoWhitespaces createMockAnnotation() {
		return new NoWhitespaces() {
			@Override
			public Class<? extends java.lang.annotation.Annotation> annotationType() {
				return NoWhitespaces.class;
			}

			@Override
			public String message() {
				return "Darf keine Leerzeichen enthalten";
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
