package de.svws_nrw.validation;

import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.core.Response;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class BeanValidatorTest {

	@Test
	@DisplayName("validate | valides Objekt")
	void testValidateSuccess() {
		assertDoesNotThrow(() -> BeanValidator.validate(new TestValidation("valid", 33)));
	}

	@Test
	@DisplayName("validate | invalides Objekt")
	void testValidateFail() {
		final var invalidRecord = new TestValidation(null, 51);
		final var throwable = ThrowableAssert.catchThrowable(() -> BeanValidator.validate(invalidRecord));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("intValidation: muss kleiner kleiner-gleich 50 sein.")
				.hasMessageContaining("stringValidation: darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("validate | valides JsonNullable Objekt")
	void testValidateJsonNullableSuccess() {
		assertDoesNotThrow(() -> BeanValidator.validate(new TestValidationJsonNullable(JsonNullable.of("valid"), JsonNullable.of(33))));
	}

	@Test
	@DisplayName("validate | invalides JsonNullable Objekt")
	void testValidateJsonNullableFail() {
		final var invalidRecord = new TestValidationJsonNullable(JsonNullable.of(null), JsonNullable.of(32001));
		final var throwable = ThrowableAssert.catchThrowable(() -> BeanValidator.validate(invalidRecord));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("intValidation: muss kleiner-gleich 50 sein.")
				.hasMessageContaining("stringValidation: darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}


	@Test
	@DisplayName("validate | JsonNullable mit explizitem null (sollte fehlschlagen)")
	void testValidateJsonNullableExplicitNull() {
		final var invalidRecord = new TestValidationJsonNullable(JsonNullable.of(null), JsonNullable.undefined());
		final var throwable = ThrowableAssert.catchThrowable(() -> BeanValidator.validate(invalidRecord));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("stringValidation: darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("validate | JsonNullable undefined (sollte erfolgreich sein)")
	void testValidateJsonNullableUndefined() {
		assertDoesNotThrow(() -> BeanValidator.validate(new TestValidationJsonNullable(JsonNullable.undefined(), JsonNullable.undefined())));
	}

	@Test
	@DisplayName("validate | @Valid auf verschachteltem Objekt")
	void testValidationWithValidAnnotation() {
		record Wrapper(@Valid TestValidationJsonNullable patch) {
			/* empty*/ }

		final var invalidPatch = new TestValidationJsonNullable(JsonNullable.of(null), JsonNullable.of(110));
		final var container = new Wrapper(invalidPatch);

		final var throwable = ThrowableAssert.catchThrowable(() -> BeanValidator.validate(container));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("patch.stringValidation: darf nicht null sein.")
				.hasMessageContaining("patch.intValidation: muss kleiner-gleich 50 sein.");
	}

	private record TestValidation(
			@NotNull(message = "darf nicht null sein") @Size(min = 1, max = 50, message = "muss zwischen 1 und 50 liegen") String stringValidation,
			@Max(value = 50, message = "muss kleiner kleiner-gleich 50 sein") int intValidation) {
	}

	private record TestValidationJsonNullable(
			JsonNullable<@NotNull(message = "darf nicht null sein") @Size(min = 1, max = 50,
					message = "muss zwischen 1 und 50 liegen") String> stringValidation,
			JsonNullable<@Max(value = 50, message = "muss kleiner-gleich 50 sein") Integer> intValidation) {
	}

}
