package de.svws_nrw.data.util;

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
class NoLeadingOrTrailingWhitespacesValidatorTest {

	@Mock
	private ConstraintValidatorContext context;

	@InjectMocks
	private NoLeadingOrTrailingWhitespacesValidator validator;

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {
			"A",
			"TestString",
			"Test String",
			"Test   String   Value",
			"Test\tString",
			"Test-String_123!@#",
			"",
			"   ",
			" "
	})
	@DisplayName("isValid | Gültige Strings")
	void isValid_validStrings(final String value) {
		final var result = validator.isValid(value, context);

		assertThat(result).isTrue();
	}

	@ParameterizedTest
	@ValueSource(strings = {
			" TestString",
			"TestString ",
			" TestString ",
			"   TestString",
			"TestString   ",
			"\tTestString",
			"TestString\t",
			"\nTestString",
			"TestString\n"
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

	private NoLeadingOrTrailingWhitespaces createMockAnnotation() {
		return new NoLeadingOrTrailingWhitespaces() {
			@Override
			public Class<? extends java.lang.annotation.Annotation> annotationType() {
				return NoLeadingOrTrailingWhitespaces.class;
			}

			@Override
			public String message() {
				return "Darf keine führenden oder nachgestellten Leerzeichen enthalten";
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
