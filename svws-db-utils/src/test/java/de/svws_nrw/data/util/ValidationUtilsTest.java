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
	@DisplayName("isBlankOrUnchanged | blank value")
	void testIsBlankOrUnchangedBlank() {
		Assertions.assertThat(ValidationUtils.isBlankOrUnchanged("existing-value", "")).isTrue();
	}

	@Test
	@DisplayName("isBlankOrUnchanged | null value")
	void testIsBlankOrUnchangedNull() {
		Assertions.assertThat(ValidationUtils.isBlankOrUnchanged(null, null)).isTrue();
	}

	@Test
	@DisplayName("isBlankOrUnchanged | no change")
	void testIsBlankOrUnchangedUnchanged() {
		Assertions.assertThat(ValidationUtils.isBlankOrUnchanged("existing-value", "existing-value")).isTrue();
	}

	@Test
	@DisplayName("isBlankOrUnchanged | created value")
	void testIsBlankOrUnchangedCreate() {
		Assertions.assertThat(ValidationUtils.isBlankOrUnchanged(null, "new-value")).isFalse();
	}

	@Test
	@DisplayName("isBlankOrUnchanged | updated value")
	void testIsBlankOrUnchangedUpdate() {
		Assertions.assertThat(ValidationUtils.isBlankOrUnchanged("existing-value", "new-value")).isFalse();
	}

	@Test
	@DisplayName("isBlankOrUnchanged | deleted value")
	void testIsBlankOrUnchangedDelete() {
		Assertions.assertThat(ValidationUtils.isBlankOrUnchanged("existing-value", null)).isTrue();
	}

	@Test
	@DisplayName("isRelevantUpdate | same value")
	void testIsRelevantUpdateSameValue() {
		Assertions.assertThat(ValidationUtils.isRelevantUpdate("existing-value", "existing-value")).isFalse();
	}

	@Test
	@DisplayName("isRelevantUpdate | updated value")
	void testIsRelevantUpdateUpdate() {
		Assertions.assertThat(ValidationUtils.isRelevantUpdate("existing-value", "new-value")).isTrue();
	}

	@Test
	@DisplayName("isRelevantUpdate | deleted value")
	void testIsRelevantUpdateDelete() {
		Assertions.assertThat(ValidationUtils.isRelevantUpdate(null, "existing-value")).isTrue();
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

	@Test
	@DisplayName("validateRequiredNonEmpty | Erfolg")
	void testValidateRequiredNonEmptySuccess() {
		assertDoesNotThrow(() -> ValidationUtils.validateRequiredNonEmpty("bezeichnung", 11, "bezeichnung"));
	}

	@Test
	@DisplayName("validateRequiredNonEmpty | Failed Max Length")
	void testValidateRequiredNonEmptyFailedLength() {
		assertThatException()
				.isThrownBy(() -> ValidationUtils.validateRequiredNonEmpty("bezeichnung-length", 11, "bezeichnung"))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut bezeichnung: Die Länge des Strings ist auf 11 Zeichen limitiert.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("validateRequiredNonEmpty | Failed Empty")
	void testValidateRequiredNonEmptyFailedEmpty() {
		assertThatException()
				.isThrownBy(() -> ValidationUtils.validateRequiredNonEmpty("bezeichnung-length", 11, "bezeichnung"))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut bezeichnung: Die Länge des Strings ist auf 11 Zeichen limitiert.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("validateMaxInteger | Erfolg")
	void testValidateMaxIntegerSuccess() {
		assertDoesNotThrow(() -> ValidationUtils.validateMaxInteger(1, 2, "sortierung"));
	}

	@Test
	@DisplayName("validateMaxInteger | Failed Max Length")
	void testValidateMaxIntegerFailedMaxLength() {
		assertThatException()
				.isThrownBy(() -> ValidationUtils.validateMaxInteger(3, 2, "sortierung"))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut sortierung: Fehler beim Konvertieren: Der Zahlwert liegt außerhalb des geforderten Bereichs.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}
}
