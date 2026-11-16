package de.svws_nrw.validation.validator;

import de.svws_nrw.validation.constraints.NotBlankIfPresent;
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
class NotBlankIfPresentValidatorTest {

	@Mock
	private ConstraintValidatorContext context;

	@InjectMocks
	private NotBlankIfPresentValidator validator;

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {
			"a",
			"TestString",
			"Test String",
			" TestString ",
			"APO-GOSt(B)10/G8"
	})
	@DisplayName("isValid | Gültige Werte")
	void isValid_validValues(final String value) {
		assertThat(validator.isValid(value, context)).isTrue();
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"",
			" ",
			"   ",
			"\t",
			"\n",
			" \t\r\n "
	})
	@DisplayName("isValid | Ungültige Werte")
	void isValid_invalidValues(final String value) {
		assertThat(validator.isValid(value, context)).isFalse();
	}

	@Test
	@DisplayName("initialize | Keine Exception")
	void initialize_noException() {
		final var annotation = createAnnotation();

		assertThatNoException().isThrownBy(() -> validator.initialize(annotation));
	}

	private NotBlankIfPresent createAnnotation() {
		return new NotBlankIfPresent() {
			@Override
			public Class<? extends java.lang.annotation.Annotation> annotationType() {
				return NotBlankIfPresent.class;
			}

			@Override
			public String message() {
				return "darf nicht leer sein";
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
