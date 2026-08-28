package de.svws_nrw.module.reporting.types.schule;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import de.svws_nrw.core.types.reporting.ReportingBildDefinition;

/**
 * Prüft die Herkunft des Schullogos. Beide Quellen liegen in der Logoverwaltung: Vorrang hat das quadratische Schullogo, sonst greift das aus SchILD-NRW
 * übernommene. Die beiden tragen im Test verschiedene Bildformate, so dass die Bildquelle zeigt, welche gegriffen hat.
 */
class TestReportingSchuleSchullogo {

	/** Ein gültiges PNG im Base64-Format, hinterlegt als quadratisches Schullogo. */
	private static final String PNG_BASE64 =
			"iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg==";

	/** Ein gültiges GIF im Base64-Format, hinterlegt als aus SchILD-NRW übernommenes Schullogo. */
	private static final String GIF_BASE64 = "R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7";

	/** Die Bilddefinitionen, nach denen eine Schule im Verlauf eines Tests fragt, in der Reihenfolge der Abfrage. */
	private final List<ReportingBildDefinition> abgefragt = new ArrayList<>();


	/**
	 * Erzeugt eine Schule, deren Logoverwaltung die übergebenen Bilder enthält. Sie tritt an die Stelle der Proxy-Klasse, die für ihren Zugriff eine
	 * Datenbankverbindung benötigte.
	 *
	 * @param logoverwaltung Die hinterlegten Bilder je Bilddefinition. Fehlt ein Eintrag, so ist zu der Definition kein Bild hinterlegt.
	 *
	 * @return Die Schule.
	 */
	private ReportingSchule schule(final Map<ReportingBildDefinition, String> logoverwaltung) {
		return new ReportingSchule(null, null, null, null, null, null, 0, null, null, null, null, 0, 0, null, null, null, 0, null, null, null, null,
				null) {
			@Override
			public ReportingBild bild(final ReportingBildDefinition bildDefinition) {
				abgefragt.add(bildDefinition);
				return new ReportingBild(bildDefinition, logoverwaltung.get(bildDefinition));
			}
		};
	}


	@Test
	void testDasQuadratischeSchullogoHatVorrang() {
		final String bildquelle = schule(Map.of(
				ReportingBildDefinition.SCHULLOGO_QUADRATISCH, PNG_BASE64,
				ReportingBildDefinition.SCHULLOGO_SCHILD, GIF_BASE64)).schullogoHtmlImageSource();
		assertEquals("data:image/png;base64," + PNG_BASE64, bildquelle);
	}

	@Test
	void testOhneQuadratischesSchullogoGreiftDasAusSchild() {
		final String bildquelle = schule(Map.of(ReportingBildDefinition.SCHULLOGO_SCHILD, GIF_BASE64)).schullogoHtmlImageSource();
		assertEquals("data:image/gif;base64," + GIF_BASE64, bildquelle);
	}

	@Test
	void testOhneBeideEintraegeBleibtDieBildquelleLeer() {
		assertEquals("", schule(Map.of()).schullogoHtmlImageSource());
	}

	@Test
	void testDasQuadratischeSchullogoWirdZuerstGefragt() {
		schule(Map.of(ReportingBildDefinition.SCHULLOGO_SCHILD, GIF_BASE64)).schullogoHtmlImageSource();
		assertEquals(List.of(ReportingBildDefinition.SCHULLOGO_QUADRATISCH, ReportingBildDefinition.SCHULLOGO_SCHILD), abgefragt);
	}

	@Test
	void testDasAusSchildUebernommeneWirdOhneNotNichtGeladen() {
		// Das Bild wird nur bei Bedarf geladen; ist das quadratische Schullogo hinterlegt, bleibt die zweite Abfrage aus.
		schule(Map.of(ReportingBildDefinition.SCHULLOGO_QUADRATISCH, PNG_BASE64)).schullogoHtmlImageSource();
		assertEquals(List.of(ReportingBildDefinition.SCHULLOGO_QUADRATISCH), abgefragt);
	}

}
