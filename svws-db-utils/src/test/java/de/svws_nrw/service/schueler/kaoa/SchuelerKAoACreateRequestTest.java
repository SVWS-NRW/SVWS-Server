package de.svws_nrw.service.schueler.kaoa;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests für die Validierung von {@link SchuelerKAoACreateRequest}.
 */
@DisplayName("Die Klasse SchuelerKAoACreateRequest")
class SchuelerKAoACreateRequestTest {

	private static SchuelerKAoACreateRequest gueltigerRequest() {
		final var request = new SchuelerKAoACreateRequest();
		request.idSchuljahresabschnitt = 1L;
		request.idJahrgang = 2L;
		request.idKategorie = 3L;
		request.idMerkmal = 4L;
		request.idZusatzmerkmal = 5L;
		return request;
	}

	@Test
	@DisplayName("akzeptiert einen Request mit allen Pflichtfeldern und ohne optionale Felder")
	void pflichtfelderGenuegen() {
		final var request = gueltigerRequest();

		assertThatCode(() -> BeanValidator.validate(request)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("lehnt fehlende Pflichtfelder mit 400 ab und nennt alle fehlenden Felder")
	void fehlendePflichtfelder() {
		final var request = new SchuelerKAoACreateRequest();

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
	@DisplayName("akzeptiert eine Bemerkung mit der maximalen Länge")
	void bemerkungMaximaleLaenge() {
		final var request = gueltigerRequest();
		request.bemerkung = "a".repeat(SchuelerKAoAValidator.MAX_LAENGE_BEMERKUNG);

		assertThatCode(() -> BeanValidator.validate(request)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("lehnt eine zu lange Bemerkung mit 400 ab")
	void bemerkungZuLang() {
		final var request = gueltigerRequest();
		request.bemerkung = "a".repeat(SchuelerKAoAValidator.MAX_LAENGE_BEMERKUNG + 1);

		assertThatThrownBy(() -> BeanValidator.validate(request))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST)
				.hasMessageContaining("Bemerkung darf nicht mehr als 255 Zeichen beinhalten");
	}

}
