package de.svws_nrw.service.schule.schulleitung;

import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Prüft die Validierung des {@link SchulleitungCreateRequest}, insbesondere das Pflichtfeld {@code bezeichnung}.
 */
class SchulleitungCreateRequestTest {

	private static SchulleitungCreateRequest createRequest(final String bezeichnung) {
		final var request = new SchulleitungCreateRequest();
		request.idLehrer = 1L;
		request.idLeitungsfunktion = 1L;
		request.bezeichnung = bezeichnung;
		return request;
	}

	@Test
	@DisplayName("validate | gültige Bezeichnung")
	void validate_validBezeichnung_success() {
		assertThatCode(() -> BeanValidator.validate(createRequest("Schulleitung"))).doesNotThrowAnyException();
	}

	@ParameterizedTest(name = "validate | ungültige Bezeichnung \"{0}\"")
	@NullAndEmptySource
	@ValueSource(strings = { " ", "   " })
	void validate_invalidBezeichnung_throwsBadRequest(final String bezeichnung) {
		final var request = createRequest(bezeichnung);

		assertThatThrownBy(() -> BeanValidator.validate(request))
				.isInstanceOf(ApiOperationException.class)
				.hasMessageContaining("bezeichnung")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

}
