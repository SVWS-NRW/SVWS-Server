package de.svws_nrw.validation.validator;

import de.svws_nrw.validation.constraints.NumericString;
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
class NumericStringValidatorTest {

	@Mock
	private ConstraintValidatorContext context;

	@InjectMocks
	private NumericStringValidator validator;

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {
			"",
			"0",
			"1",
			"123",
			"0001",
			"9999999999"
	})
	@DisplayName("isValid | Gültige Werte")
	void isValid_validValues(final String value) {
		assertThat(validator.isValid(value, context)).isTrue();
	}

	@ParameterizedTest
	@ValueSource(strings = {
			" ",
			"12 3",
			"-1",
			"+1",
			"1.0",
			"1,0",
			"abc",
			"12abc",
			"abc12",
			" 123",
			"123 ",
			"\t123",
			"123\n"
	})
	@DisplayName("isValid | Ungültige Werte")
	void isValid_invalidValues(final String value) {
		assertThat(validator.isValid(value, context)).isFalse();
	}

	@Test
	@DisplayName("initialize | Keine Exception")
	void initialize_noException() {
		final var annotation = createMockAnnotation();

		assertThatNoException().isThrownBy(() -> validator.initialize(annotation));
	}

	private NumericString createMockAnnotation() {
		return new NumericString() {
			@Override
			public Class<? extends java.lang.annotation.Annotation> annotationType() {
				return NumericString.class;
			}

			@Override
			public String message() {
				return "Der Wert darf nur Ziffern enthalten";
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
