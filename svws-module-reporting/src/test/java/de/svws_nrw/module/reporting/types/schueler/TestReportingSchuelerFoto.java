package de.svws_nrw.module.reporting.types.schueler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.junit.jupiter.api.Test;

/**
 * Prüft die Bildquelle des Schülerfotos. Sie entsteht erst beim Zugriff, denn die Schülerdaten werden auch für Ausgaben geladen, die keine Fotos zeigen.
 */
class TestReportingSchuelerFoto {

	/** Ein gültiges PNG im Base64-Format. */
	private static final String PNG_BASE64 =
			"iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg==";


	/**
	 * Erzeugt einen Schüler mit dem übergebenen Foto. Der Konstruktor bleibt außen vor, weil er alle Stammdaten des Schülers verlangt.
	 *
	 * @param foto Das Foto im Base64-Format.
	 *
	 * @return Der Schüler.
	 */
	private static ReportingSchueler schuelerMitFoto(final String foto) {
		final ReportingSchueler schueler = mock(ReportingSchueler.class, CALLS_REAL_METHODS);
		schueler.foto = foto;
		return schueler;
	}


	@Test
	void testDasFotoWirdMitSeinemMimeTypeAusgeliefert() {
		assertEquals("data:image/png;base64," + PNG_BASE64, schuelerMitFoto(PNG_BASE64).fotoHtmlSource());
	}

	@Test
	void testOhneFotoBleibtDieBildquelleLeer() {
		assertEquals("", schuelerMitFoto("").fotoHtmlSource());
	}

	@Test
	void testEinNichtAufloesbaresFotoBleibtOhneBildquelle() {
		// Eine Bildquelle mit unbrauchbarem Inhalt zeigte im Druck ein defektes Bild. Die Vorlagen prüfen auf den leeren String und weichen dann aus.
		assertEquals("", schuelerMitFoto("Kein gueltiges Base64!").fotoHtmlSource());
	}

	@Test
	void testGueltigesBase64OhneBildinhaltBleibtOhneBildquelle() {
		// Der Resolver löst auch Daten auf, die kein Bild sind. Als Bildquelle übernommen, zeigte die Vorlage ein defektes Bild statt ihres Platzhalters.
		final String text = Base64.getEncoder().encodeToString("Dies ist kein Bild.".getBytes(StandardCharsets.UTF_8));

		assertEquals("", schuelerMitFoto(text).fotoHtmlSource());
	}

	@Test
	void testDieBildquelleEntstehtNurEinmal() {
		final ReportingSchueler schueler = schuelerMitFoto(PNG_BASE64);
		assertSame(schueler.fotoHtmlSource(), schueler.fotoHtmlSource());
	}

}
