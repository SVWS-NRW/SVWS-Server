package de.svws_nrw.service.schueler.stammdaten;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.function.Consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

/**
 * Tests für die Bean-Validierung des {@link SchuelerStammdatenCreateRequest}.
 */
class SchuelerStammdatenCreateRequestTest {

	private static SchuelerStammdatenCreateRequest gueltigerRequest() {
		final var request = new SchuelerStammdatenCreateRequest();
		request.nachname = "Mustermann";
		request.vorname = "Max";
		request.geschlecht = 3;
		request.geburtsdatum = "2000-01-01";
		request.status = 2;
		request.idSchuljahresabschnitt = 8L;
		return request;
	}

	private static void assertUngueltig(final Consumer<SchuelerStammdatenCreateRequest> aenderung, final String erwarteterFeldname) {
		final var request = gueltigerRequest();
		aenderung.accept(request);
		assertThatThrownBy(() -> BeanValidator.validate(request))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
				.hasMessageContaining(erwarteterFeldname);
	}

	@Test
	@DisplayName("Nur Pflichtfelder gesetzt ist gültig")
	void nurPflichtfelder() {
		assertThatCode(() -> BeanValidator.validate(gueltigerRequest())).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Alle Felder gesetzt ist gültig")
	void alleFelder() {
		final var request = gueltigerRequest();
		request.alleVornamen = "Max Moritz";
		request.anmeldedatum = "2024-08-01";
		request.aufnahmedatum = "2024-08-15";
		request.beginnBildungsgang = "2024-09-01";
		request.dauerBildungsgang = 3;
		request.idReligion = 0L;
		assertThatCode(() -> BeanValidator.validate(request)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Fehlende Pflichtfelder werden abgelehnt")
	void pflichtfelder() {
		assertUngueltig(r -> r.nachname = null, "nachname");
		assertUngueltig(r -> r.vorname = null, "vorname");
		assertUngueltig(r -> r.geschlecht = null, "geschlecht");
		assertUngueltig(r -> r.geburtsdatum = null, "geburtsdatum");
		assertUngueltig(r -> r.status = null, "status");
		assertUngueltig(r -> r.idSchuljahresabschnitt = null, "idSchuljahresabschnitt");
	}

	@Test
	@DisplayName("Leere Pflicht-Strings werden abgelehnt")
	void leerePflichtStrings() {
		assertUngueltig(r -> r.nachname = "", "nachname");
		assertUngueltig(r -> r.vorname = " ", "vorname");
		assertUngueltig(r -> r.geburtsdatum = "", "geburtsdatum");
	}

	@Test
	@DisplayName("Zu lange Strings werden abgelehnt")
	void maximaleLaengen() {
		assertUngueltig(r -> r.nachname = "a".repeat(121), "nachname");
		assertUngueltig(r -> r.vorname = "a".repeat(81), "vorname");
		assertUngueltig(r -> r.alleVornamen = "a".repeat(256), "alleVornamen");
	}

	@Test
	@DisplayName("Führende und nachfolgende Leerzeichen in Namen werden abgelehnt")
	void namenMitLeerzeichen() {
		assertUngueltig(r -> r.nachname = " Mustermann", "nachname");
		assertUngueltig(r -> r.vorname = "Max ", "vorname");
		assertUngueltig(r -> r.alleVornamen = " Max Moritz ", "alleVornamen");
	}

	@ParameterizedTest(name = "ungültiges Datum \"{0}\"")
	@ValueSource(strings = { "2023-02-30", "2023-13-01", "01.12.2023", "2023-1-1", "abc" })
	@DisplayName("Ungültige Datumswerte werden abgelehnt")
	void ungueltigeDaten(final String datum) {
		assertUngueltig(r -> r.geburtsdatum = datum, "geburtsdatum");
		assertUngueltig(r -> r.anmeldedatum = datum, "anmeldedatum");
		assertUngueltig(r -> r.aufnahmedatum = datum, "aufnahmedatum");
		assertUngueltig(r -> r.beginnBildungsgang = datum, "beginnBildungsgang");
	}

	@ParameterizedTest(name = "leerer oder blanker Wert \"{0}\"")
	@ValueSource(strings = { "", " ", "   ", "\t" })
	@DisplayName("Leere oder blanke optionale Datumsfelder werden abgelehnt")
	void leereOptionaleFelder(final String wert) {
		assertUngueltig(r -> r.anmeldedatum = wert, "anmeldedatum");
		assertUngueltig(r -> r.aufnahmedatum = wert, "aufnahmedatum");
		assertUngueltig(r -> r.beginnBildungsgang = wert, "beginnBildungsgang");
	}

	@Test
	@DisplayName("Negative Religions-ID wird abgelehnt")
	void negativeReligion() {
		assertUngueltig(r -> r.idReligion = -1L, "idReligion");
	}

}
