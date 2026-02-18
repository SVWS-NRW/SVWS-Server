package de.svws_nrw.data.util;

import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ValidationUtilsTest {

	@Test
	@DisplayName("valueIsBlankOrHasNotChanged | blank value")
	void testIsBlankOrUnchangedBlank() {
		Assertions.assertThat(ValidationUtils.isBlankOrUnchanged("existing-value", "")).isTrue();
	}

	@Test
	@DisplayName("valueIsBlankOrHasNotChanged | null value")
	void testIsBlankOrUnchangedNull() {
		Assertions.assertThat(ValidationUtils.isBlankOrUnchanged(null, null)).isTrue();
	}

	@Test
	@DisplayName("valueIsBlankOrHasNotChanged | no change")
	void testIsBlankOrUnchangedUnchanged() {
		Assertions.assertThat(ValidationUtils.isBlankOrUnchanged("existing-value", "existing-value")).isTrue();
	}

	@Test
	@DisplayName("valueIsBlankOrHasNotChanged | created value")
	void testIsBlankOrUnchangedCreate() {
		Assertions.assertThat(ValidationUtils.isBlankOrUnchanged(null, "new-value")).isFalse();
	}

	@Test
	@DisplayName("valueIsBlankOrHasNotChanged | updated value")
	void testIsBlankOrUnchangedUpdate() {
		Assertions.assertThat(ValidationUtils.isBlankOrUnchanged("existing-value", "new-value")).isFalse();
	}

	@Test
	@DisplayName("valueIsBlankOrHasNotChanged | deleted value")
	void testIsBlankOrUnchangedDelete() {
		Assertions.assertThat(ValidationUtils.isBlankOrUnchanged("existing-value", null)).isTrue();
	}

	@Test
	@DisplayName("validateId | valid sequence")
	void validateIdValid() {
		assertDoesNotThrow(() -> ValidationUtils.validateId(1L, "id", 1));
	}

	@Test
	@DisplayName("validateId | invalid null input")
	void validateIdInvalidNull() {
		assertThatException()
				.isThrownBy(() -> ValidationUtils.validateId(1L, "id", null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut id: Der Wert null ist nicht erlaubt")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("validateId | invalid sequence out of order")
	void validateIdInvalidDifferent() {
		assertThatException()
				.isThrownBy(() -> ValidationUtils.validateId(2L, "id", 1L))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID 1 des Patches ist null oder stimmt nicht mit der ID 2 in der Datenbank überein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}
}
