package de.svws_nrw.module.reporting.types.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.junit.jupiter.api.Test;

import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;

/**
 * Prüft das Foto der Person und seine Bildquelle. Sie entsteht erst beim Zugriff, denn Personendaten werden auch für Ausgaben geladen, die keine Fotos zeigen.
 */
class TestReportingPersonFoto {

	/** Ein gültiges PNG im Base64-Format. */
	private static final String PNG_BASE64 =
			"iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg==";


	/**
	 * Erzeugt eine Person mit dem übergebenen Foto. Der Konstruktor bleibt außen vor, weil er alle Personendaten verlangt.
	 *
	 * @param foto Das Foto im Base64-Format.
	 *
	 * @return Die Person.
	 */
	private static ReportingPerson personMitFoto(final String foto) {
		final ReportingPerson person = mock(ReportingPerson.class, CALLS_REAL_METHODS);
		person.foto = foto;
		return person;
	}


	@Test
	void testDasFotoWirdMitSeinemMimeTypeAusgeliefert() {
		assertEquals("data:image/png;base64," + PNG_BASE64, personMitFoto(PNG_BASE64).fotoHtmlSource());
	}

	@Test
	void testOhneFotoBleibtDieBildquelleLeer() {
		assertEquals("", personMitFoto("").fotoHtmlSource());
	}

	@Test
	void testEinNichtAufloesbaresFotoBleibtOhneBildquelle() {
		// Eine Bildquelle mit unbrauchbarem Inhalt zeigte im Druck ein defektes Bild. Die Vorlagen prüfen auf den leeren String und weichen dann aus.
		assertEquals("", personMitFoto("Kein gueltiges Base64!").fotoHtmlSource());
	}

	@Test
	void testGueltigesBase64OhneBildinhaltBleibtOhneBildquelle() {
		// Der Resolver löst auch Daten auf, die kein Bild sind. Als Bildquelle übernommen, zeigte die Vorlage ein defektes Bild statt ihres Platzhalters.
		final String text = Base64.getEncoder().encodeToString("Dies ist kein Bild.".getBytes(StandardCharsets.UTF_8));

		assertEquals("", personMitFoto(text).fotoHtmlSource());
	}

	@Test
	void testEinNachladenOhneErgebnisLiefertDenLeerenString() {
		// Der Getter sagt "nie null" zu. Diese Zusage darf nicht davon abhängen, was eine überschriebene ladeFoto-Methode zurückgibt.
		final ReportingPerson person = mock(ReportingPerson.class, CALLS_REAL_METHODS);
		doReturn(null).when(person).ladeFoto();

		assertEquals("", person.foto());
		assertEquals("", person.fotoHtmlSource());
		// Nach dem Nachladen steht im Feld ein leerer String. Ohne eigenes Kennzeichen wäre er von "noch nicht geladen" nicht zu unterscheiden.
		verify(person, times(1)).ladeFoto();
	}

	@Test
	void testDieBildquelleEntstehtNurEinmal() {
		final ReportingPerson person = personMitFoto(PNG_BASE64);
		assertSame(person.fotoHtmlSource(), person.fotoHtmlSource());
	}


	// ##### Weitergabe durch die Konstruktoren der Unterklassen #####

	@Test
	void testDerLehrerReichtDasFotoAnDieRichtigeStelleWeiter() {
		// Der Konstruktor gibt über zwanzig Zeichenketten an die Basisklasse weiter. Eine um eine Stelle verschobene Angabe fiele dem Compiler nicht auf,
		// deshalb tragen die Nachbarn des Fotos eigene Werte.
		final ReportingLehrer lehrer = new ReportingLehrer(null, null, null, null, "Fax", PNG_BASE64, "2000-01-01", null, null, null, null, null, null, 0,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

		assertEquals(PNG_BASE64, lehrer.foto());
		assertEquals("Fax", lehrer.faxSchule(), "Das Foto darf nicht an der Stelle der Fax-Nummer landen.");
		assertEquals("2000-01-01", lehrer.geburtsdatum(), "Das Foto darf nicht an der Stelle des Geburtsdatums landen.");
		assertEquals("data:image/png;base64," + PNG_BASE64, lehrer.fotoHtmlSource());
	}

	@Test
	void testDerSchuelerReichtDasFotoAnDieRichtigeStelleWeiter() {
		final ReportingSchueler schueler = new ReportingSchueler(null, null, null, null, null, false, null, null, false, false, null, null, null, null, null,
				PNG_BASE64, "2000-01-01", null, null, null, null, null, null, null, null, null, null, null, false, false, null, null, 0, null, false, null,
				null, false, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

		assertEquals(PNG_BASE64, schueler.foto());
		assertEquals("2000-01-01", schueler.geburtsdatum(), "Das Foto darf nicht an der Stelle des Geburtsdatums landen.");
		assertEquals("data:image/png;base64," + PNG_BASE64, schueler.fotoHtmlSource());
	}

}
