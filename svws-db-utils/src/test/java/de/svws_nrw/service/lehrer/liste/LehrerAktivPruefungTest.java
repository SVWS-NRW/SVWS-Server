package de.svws_nrw.service.lehrer.liste;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Termin;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests für die {@link LehrerAktivPruefung}. Die Fälle beziehen sich auf das Schuljahr 2024/25, dessen letzter
 * Unterrichtstag im ersten Halbjahr der 07.02.2025 ist.
 */
class LehrerAktivPruefungTest {

	private static final int SCHULJAHR = 2024;

	@BeforeAll
	static void setUpAll() {
		ASDCoreTypeUtils.initAll();
	}

	private static Schuljahresabschnitt abschnitt(final int schuljahr, final int halbjahr) {
		final var abschnitt = new Schuljahresabschnitt();
		abschnitt.schuljahr = schuljahr;
		abschnitt.abschnitt = halbjahr;
		return abschnitt;
	}

	private static DTOLehrer lehrer(final String datumZugang, final String datumAbgang) {
		final var lehrer = new DTOLehrer(1L, "MUS", "Mustermann");
		lehrer.DatumZugang = datumZugang;
		lehrer.DatumAbgang = datumAbgang;
		return lehrer;
	}

	@Test
	@DisplayName("Voraussetzung | der letzte Unterrichtstag des ersten Halbjahres 2024/25 ist der 07.02.2025")
	void voraussetzung_letzterUnterrichtstag() {
		final var date = Termin.getLetzterUnterrichtstagImErstenHalbjahr(SCHULJAHR);

		assertThat(date).isNotNull()
				.extracting(d -> d.getJahr(), d -> d.getMonat(), d -> d.getTag())
				.containsExactly(2025, 2, 7);
	}

	@ParameterizedTest(name = "Abschnitt {0}, Zugang {1}: aktiv = {2}")
	@DisplayName("Zugangsdatum")
	@CsvSource(nullValues = "NULL", value = {
			"1, NULL, true",
			"1, 2020-08-01, true",
			"1, 2024-09-01, true",
			"1, 2025-01-15, true",
			"1, 2025-02-07, true",
			"1, 2025-02-08, false",
			"1, 2025-03-01, false",
			"1, 2025-08-01, false",
			"1, 2025-12-31, false",
			"1, 2026-01-01, false",
			"1, 2030-01-01, false",
			"2, 2024-08-01, true",
			"2, 2025-02-08, true",
			"2, 2025-03-01, true",
			"2, 2025-07-31, true",
			"2, 2025-08-01, false",
			"2, 2025-12-31, false",
			"2, 2026-01-01, false",
			"2, 2030-01-01, false"
	})
	void zugangsdatum(final int halbjahr, final String datumZugang, final boolean erwartet) {
		assertThat(LehrerAktivPruefung.isAktiv(lehrer(datumZugang, null), abschnitt(SCHULJAHR, halbjahr))).isEqualTo(erwartet);
	}

	@ParameterizedTest(name = "Abschnitt {0}, Abgang {1}: aktiv = {2}")
	@DisplayName("Abgangsdatum")
	@CsvSource(nullValues = "NULL", value = {
			"1, NULL, true",
			"1, 2000-01-01, false",
			"1, 2023-12-31, false",
			"1, 2024-01-01, false",
			"1, 2024-07-31, false",
			"1, 2024-08-01, true",
			"1, 2024-12-31, true",
			"1, 2025-01-31, true",
			"1, 2025-02-08, true",
			"1, 2025-07-31, true",
			"1, 2025-08-01, true",
			"1, 2026-01-01, true",
			"1, 2030-01-01, true",
			"2, NULL, true",
			"2, 2000-01-01, false",
			"2, 2023-12-31, false",
			"2, 2024-01-01, false",
			"2, 2024-08-01, false",
			"2, 2024-12-31, false",
			"2, 2025-01-01, false",
			"2, 2025-01-31, false",
			"2, 2025-02-06, false",
			"2, 2025-02-07, false",
			"2, 2025-02-08, true",
			"2, 2025-07-31, true",
			"2, 2025-08-01, true",
			"2, 2026-01-01, true",
			"2, 2030-01-01, true"
	})
	void abgangsdatum(final int halbjahr, final String datumAbgang, final boolean erwartet) {
		assertThat(LehrerAktivPruefung.isAktiv(lehrer(null, datumAbgang), abschnitt(SCHULJAHR, halbjahr))).isEqualTo(erwartet);
	}

	@Test
	@DisplayName("Ohne Zugangs- und Abgangsdatum ist der Lehrer aktiv")
	void ohneDaten_istAktiv() {
		assertThat(LehrerAktivPruefung.isAktiv(lehrer(null, null), abschnitt(SCHULJAHR, 1))).isTrue();
		assertThat(LehrerAktivPruefung.isAktiv(lehrer(null, null), abschnitt(SCHULJAHR, 2))).isTrue();
	}

	@Test
	@DisplayName("Zugang und Abgang | ein Lehrer mit Abgang vor dem Abschnitt ist auch bei früherem Zugang nicht aktiv")
	void zugangUndAbgang() {
		assertThat(LehrerAktivPruefung.isAktiv(lehrer("2010-08-01", "2024-07-31"), abschnitt(SCHULJAHR, 1))).isFalse();
		assertThat(LehrerAktivPruefung.isAktiv(lehrer("2010-08-01", "2025-07-31"), abschnitt(SCHULJAHR, 1))).isTrue();
	}

	@Test
	@DisplayName("Ohne Termin für das Schuljahr werden die Halbjahresgrenzen nicht geprüft")
	void ohneTerminFuerSchuljahr() {
		assertThat(LehrerAktivPruefung.isAktiv(lehrer("1901-01-15", null), abschnitt(1900, 1))).isTrue();
		assertThat(LehrerAktivPruefung.isAktiv(lehrer(null, "1901-01-15"), abschnitt(1900, 2))).isTrue();
	}

}
