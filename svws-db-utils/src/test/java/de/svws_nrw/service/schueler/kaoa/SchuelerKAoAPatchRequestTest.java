package de.svws_nrw.service.schueler.kaoa;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests für die Validierung von {@link SchuelerKAoAPatchRequest}.
 */
@DisplayName("Die Klasse SchuelerKAoAPatchRequest")
class SchuelerKAoAPatchRequestTest {

	@Test
	@DisplayName("akzeptiert einen leeren Patch ohne gesetzte Felder")
	void leererPatch() {
		final var request = new SchuelerKAoAPatchRequest();

		assertThatCode(() -> BeanValidator.validate(request)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("akzeptiert null bei den optionalen Feldern Anschlussoption, Berufsfeld, Ebene 4 und Bemerkung")
	void nullBeiOptionalenFeldern() {
		final var request = new SchuelerKAoAPatchRequest();
		request.idAnschlussoption = JsonNullable.of(null);
		request.idBerufsfeld = JsonNullable.of(null);
		request.idEbene4 = JsonNullable.of(null);
		request.bemerkung = JsonNullable.of(null);

		assertThatCode(() -> BeanValidator.validate(request)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("lehnt null bei den Pflichtfeldern mit 400 ab und nennt alle betroffenen Felder")
	void nullBeiPflichtfeldern() {
		final var request = new SchuelerKAoAPatchRequest();
		request.idSchuljahresabschnitt = JsonNullable.of(null);
		request.idJahrgang = JsonNullable.of(null);
		request.idKategorie = JsonNullable.of(null);
		request.idMerkmal = JsonNullable.of(null);
		request.idZusatzmerkmal = JsonNullable.of(null);

		assertThatThrownBy(() -> BeanValidator.validate(request))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST)
				.hasMessageContaining("idSchuljahresabschnitt")
				.hasMessageContaining("idJahrgang")
				.hasMessageContaining("idKategorie")
				.hasMessageContaining("idMerkmal")
				.hasMessageContaining("idZusatzmerkmal");
	}

	@Test
	@DisplayName("lehnt eine zu lange Bemerkung mit 400 ab")
	void bemerkungZuLang() {
		final var request = new SchuelerKAoAPatchRequest();
		request.bemerkung = JsonNullable.of("a".repeat(SchuelerKAoAValidator.MAX_LAENGE_BEMERKUNG + 1));

		assertThatThrownBy(() -> BeanValidator.validate(request))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST)
				.hasMessageContaining("Bemerkung darf nicht mehr als 255 Zeichen beinhalten");
	}

}
