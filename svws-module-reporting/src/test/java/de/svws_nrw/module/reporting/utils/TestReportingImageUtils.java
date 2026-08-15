package de.svws_nrw.module.reporting.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;

/**
 * Tests der Bildumwandlung mit fehlerhaften Daten. Ein nicht dekodierbares Bild wird mit dem Typ "unknown" ausgeliefert; dieser Rückfallwert bleibt nur
 * wirksam, wenn die Stelle höchstens eine Warnung protokolliert - ein {@code ERROR}-Eintrag beendet den Report auch ohne Wurf.
 * <p>Die Hilfsklasse ist statisch und hat keinen Zugang zum Reporting-Context. Der Befund wird deshalb nicht gezählt und erscheint nicht im Hinweis-Header:
 * eine bewusste, dokumentierte Ausnahme, bis die Vorlagen-Dialekte Zugang zur Diagnose haben.</p>
 */
class TestReportingImageUtils {

	/** Ein Text, der kein gültiges Base64 ist und die Dekodierung scheitern lässt. */
	private static final String KEIN_BASE64 = "%%%kein-base64%%%";


	@Test
	void testEinNichtDekodierbaresBildWirdMitUnbekanntemTypAusgeliefert() {
		final Logger logger = new Logger();
		final LogConsumerList log = new LogConsumerList();
		logger.addConsumer(log);

		final String quelle = ReportingImageUtils.base64ImageToHtmlImageSource(KEIN_BASE64, null, logger);

		assertEquals("data:unknown;base64," + KEIN_BASE64, quelle, "Das Bild wird mit unbekanntem Typ ausgeliefert statt die Ausgabe zu beenden.");
		assertTrue(log.getLogData().stream().noneMatch(eintrag -> eintrag.getLevel() == LogLevel.ERROR),
				"Ein ERROR-Eintrag beendete den Report und machte den Rückfall auf den unbekannten Typ wirkungslos.");
		assertTrue(log.getLogData().stream().anyMatch(eintrag -> eintrag.getLevel() == LogLevel.WARNING),
				"Der Sachverhalt bleibt als Warnung im Log; gezählt wird er ohne Context-Zugang nicht.");
	}

	@Test
	void testOhneLoggerBleibtDasVerhaltenUnveraendert() {
		// Die Vorlagen-Dialekte rufen die Hilfsklasse teils ohne Logger auf; auch dann darf sie nicht scheitern.
		assertEquals("data:unknown;base64," + KEIN_BASE64, ReportingImageUtils.base64ImageToHtmlImageSource(KEIN_BASE64, null, null));
	}

}
