package de.svws_nrw.validation.validator;

import de.svws_nrw.validation.constraints.ValidDateFormat;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidDateFormatValidatorTest {

	@Mock
	private ConstraintValidatorContext context;

	@InjectMocks
	private ValidDateFormatValidator validator;

	@BeforeEach
	void setUp() {
		validator = new ValidDateFormatValidator();
		final var annotation = mock(ValidDateFormat.class);
		when(annotation.pattern()).thenReturn("uuuu-MM-dd");
		validator.initialize(annotation);
	}

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {
			"",
			"   ",
			"2023-12-01",
			"2024-02-29", // Schaltjahr
			"2000-01-01"
	})
	@DisplayName("isValid | Gültige Werte")
	void isValid_validValues(final String value) {
		final var result = validator.isValid(value, context);

		assertThat(result).isTrue();
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"2023-13-01",   // ungültiger Monat
			"2023-00-01",   // ungültiger Monat
			"2023-12-32",   // ungültiger Tag
			"2023-02-30",   // ungültiges Datum
			"2023-02-29",   // kein Schaltjahr
			"01.12.2023",   // falsches Format
			"2023/12/01",   // falsches Format
			"20231201",     // kein Trennzeichen
			"abc",          // kein Datum
			"2023-1-1"      // führende Nullen fehlen
	})
	@DisplayName("isValid | Ungültige Werte")
	void isValid_invalidValues(final String value) {
		final var result = validator.isValid(value, context);

		assertThat(result).isFalse();
	}

	@Test
	@DisplayName("initialize | Kein Exception bei gültigem Pattern")
	void initialize_noException() {
		final var annotation = createMockAnnotation();

		assertThatNoException().isThrownBy(() -> validator.initialize(annotation));
	}

	private ValidDateFormat createMockAnnotation() {
		return new ValidDateFormat() {
			@Override
			public Class<? extends java.lang.annotation.Annotation> annotationType() {
				return ValidDateFormat.class;
			}

			@Override
			public String pattern() {
				return "yyyy-MM-dd";
			}

			@Override
			public String message() {
				return "Ungültiges Datumsformat";
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
