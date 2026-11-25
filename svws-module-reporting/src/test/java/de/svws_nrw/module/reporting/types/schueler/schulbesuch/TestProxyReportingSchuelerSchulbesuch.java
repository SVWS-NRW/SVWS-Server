package de.svws_nrw.module.reporting.types.schueler.schulbesuch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchsdaten;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.module.reporting.repositories.ReportingContext;

/**
 * Prüft, wie der Schulbesuch-Proxy den Schlüssel des höchsten Schulabschlusses aus berufsbildendem und allgemeinbildendem Anteil zusammensetzt.
 */
class TestProxyReportingSchuelerSchulbesuch {

	@BeforeAll
	static void initCoreTypes() {
		ASDCoreTypeUtils.initAll();
	}

	/**
	 * Fehlende Anteile werden ausgelassen und erscheinen nicht als "null" im Schlüssel.
	 *
	 * @param berufsbildend Der Schlüssel des berufsbildenden Abschlusses oder null.
	 * @param allgemeinbildend Der Schlüssel des allgemeinbildenden Abschlusses oder null.
	 * @param erwartet Der erwartete zusammengesetzte Schlüssel.
	 */
	@ParameterizedTest
	@CsvSource({ "2, D, 2D", ", D, D", ", , ''" })
	void hoechsterSchulabschlussSchluessel(final String berufsbildend, final String allgemeinbildend, final String erwartet) {
		final var schulbesuchsdaten = new SchuelerSchulbesuchsdaten();
		schulbesuchsdaten.schluesselAbschlussartBerufsbildendDieseSchule = berufsbildend;
		schulbesuchsdaten.schluesselAbschlussartAllgemeinbildendDieseSchule = allgemeinbildend;

		final var schulbesuch = new ProxyReportingSchuelerSchulbesuch(mock(ReportingContext.class, RETURNS_DEEP_STUBS), schulbesuchsdaten);

		assertEquals(erwartet, schulbesuch.hoechsterSchulabschlussSchluessel());
	}

}
