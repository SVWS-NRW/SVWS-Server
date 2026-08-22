package de.svws_nrw.module.reporting.factories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.module.reporting.diagnose.ReportingAusgabeumfang;
import de.svws_nrw.module.reporting.diagnose.ReportingHinweisSerializer;
import de.svws_nrw.module.reporting.diagnose.ReportingProblem;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemSchluessel;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemauswirkung;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemursache;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import jakarta.ws.rs.core.Response;

/**
 * Tests des Bausteins, der eine erfolgreiche Ausgabe um den öffentlichen Hinweis-Header ergänzt.
 * <p>Der Header entsteht aus dem gemeldeten Ausgabeumfang und den gemeldeten Problemen. Fehlt der Umfang oder verletzt er die Zusagen des Vertrags, bleibt
 * die Antwort ohne Header: Ein fehlender Header bedeutet "unbekannt", ein vertragswidriger wäre eine falsche Auskunft.</p>
 * <p>Die Sollwerte des Headers stehen als Literale. Der Wert ist ein Vertrag mit dem Client; würde der Test ihn aus dem Serializer bilden, schriebe er jede
 * Vertragsänderung stillschweigend mit.</p>
 */
class TestReportingHinweiseHeader {

	/** Der Context, den der Helfer in den Tests erhält. */
	private ReportingContext reportingContext;

	/** Die Liste, die die Einträge des Loggers sammelt. */
	private LogConsumerList log;


	@BeforeEach
	void setUp() {
		reportingContext = mock(ReportingContext.class);
		when(reportingContext.ausgabeprobleme()).thenReturn(List.of());
		final Logger logger = new Logger();
		log = new LogConsumerList();
		logger.addConsumer(log);
		when(reportingContext.logger()).thenReturn(logger);
		// Der Header wird derzeit nur im Entwicklungsmodus ausgeliefert; die Tests des Vertrags messen deshalb dort.
		when(reportingContext.serverMode()).thenReturn(ServerMode.DEV);
	}


	/**
	 * Verdrahtet den gemeldeten Ausgabeumfang des Aufrufs.
	 *
	 * @param umfang Der Ausgabeumfang oder {@code null} für eine fehlende Meldung.
	 */
	private void gebeAusgabeumfangVor(final ReportingAusgabeumfang umfang) {
		when(reportingContext.ausgabeumfang()).thenReturn(umfang);
	}

	/**
	 * Gibt den Wert des Hinweis-Headers einer erfolgreichen Antwort zurück.
	 *
	 * @return Der Headerwert oder {@code null}, wenn die Antwort den Header nicht trägt.
	 */
	private String headerwert() {
		final Response response = ReportingHinweiseHeader.ergaenze(Response.ok("Inhalt"), reportingContext).build();
		return response.getHeaderString(ReportingHinweisSerializer.HEADER_NAME);
	}

	/**
	 * Gibt ein Ausgabeproblem zurück, das in der Kategorie der fehlenden Datensätze erscheint.
	 *
	 * @param id Die ID des betroffenen Datensatzes.
	 *
	 * @return Das Ausgabeproblem.
	 */
	private static ReportingProblem ausgelassenerDatensatz(final long id) {
		return new ReportingProblem(ReportingProblemursache.NICHT_VORHANDEN, ReportingProblemauswirkung.DATENSATZ_AUSGELASSEN,
				ReportingProblemSchluessel.fuer(ReportingSchueler.class, id));
	}


	@Test
	void testEinGemeldeterUmfangTraegtDenHeaderAuchOhneBefund() {
		// hinweise=0 ist eine belastbare Aussage: Die Meldestelle des Datenaufbaus hat die Zählwerte ermittelt und nichts wurde beanstandet.
		gebeAusgabeumfangVor(new ReportingAusgabeumfang(5, 5, false));

		assertEquals("v=1, angefordert=5, ausgegeben=5, hinweise=0", headerwert());
	}

	@Test
	void testDieGemeldetenProblemeErreichenDenHeader() {
		gebeAusgabeumfangVor(new ReportingAusgabeumfang(5, 3, false));
		when(reportingContext.ausgabeprobleme()).thenReturn(List.of(ausgelassenerDatensatz(1L), ausgelassenerDatensatz(2L)));

		assertEquals("v=1, angefordert=5, ausgegeben=3, hinweise=2, datensaetze=2", headerwert());
	}

	@Test
	void testEineZulaessigLeereAusgabeTraegtDieZaehlwerte() {
		// Der reine Filter-Leerfall: kein Befund, aber es blieb kein angeforderter Datensatz übrig. Der Client liest die Differenz aus den Zahlen.
		// Ob dabei ein Dokument entsteht, ist für diesen Wert unerheblich - er folgt der Auswahl.
		gebeAusgabeumfangVor(new ReportingAusgabeumfang(5, 0, true));

		assertEquals("v=1, angefordert=5, ausgegeben=0, hinweise=0", headerwert());
	}

	@Test
	void testAusserhalbDerEntwicklungEntstehtKeinHeader() {
		// Die Auslieferung wartet auf die Entscheidung über die Auswertung im Webclient. Bis dahin sieht eine stabile Installation die unveränderte Antwort;
		// der Header träte sonst in Umlauf, bevor ein Verbraucher feststeht.
		gebeAusgabeumfangVor(new ReportingAusgabeumfang(5, 5, false));

		for (final ServerMode modus : List.of(ServerMode.STABLE, ServerMode.BETA, ServerMode.ALPHA)) {
			when(reportingContext.serverMode()).thenReturn(modus);
			assertNull(headerwert(), "Im Modus %s darf der Header nicht gesetzt werden.".formatted(modus.name()));
		}
	}

	@Test
	void testDerRestDerAntwortBleibtAusserhalbDerEntwicklungUnveraendert() {
		// Ohne den Header ist die Antwort exakt die bisherige: Der Vertrag greift nicht in Inhalt, Dateiname oder Status ein.
		gebeAusgabeumfangVor(new ReportingAusgabeumfang(5, 5, false));
		when(reportingContext.serverMode()).thenReturn(ServerMode.STABLE);

		final Response response = ReportingHinweiseHeader
				.ergaenze(Response.ok("Inhalt").header("Content-Disposition", "attachment; filename=\"Test.pdf\""), reportingContext).build();

		assertEquals("attachment; filename=\"Test.pdf\"", response.getHeaderString("Content-Disposition"));
		assertEquals("Inhalt", response.getEntity());
	}

	@Test
	void testOhneGemeldetenUmfangEntstehtKeinHeader() {
		// Ohne Meldung sind die Zählwerte unbekannt. Der Helfer läuft im Erfolgspfad und darf dort nicht werfen; die Ausgabefactory erzwingt die Meldung
		// bereits nach dem Aufbau der Daten-Contexts.
		gebeAusgabeumfangVor(null);

		assertNull(headerwert());
	}

	@Test
	void testEinVertragswidrigerUmfangEntziehtDenHeader() {
		// Negative Werte oder mehr ausgegeben als angefordert verletzen die Zusagen des Vertrags. Ein fehlender Header sagt "unbekannt" - das ist ehrlicher
		// als eine falsche Auskunft, und die fertige Ausgabe wird nicht verworfen.
		gebeAusgabeumfangVor(new ReportingAusgabeumfang(3, 5, false));
		assertNull(headerwert());

		gebeAusgabeumfangVor(new ReportingAusgabeumfang(-1, 0, true));
		assertNull(headerwert());
	}

	@Test
	void testEinVertragswidrigerUmfangWirdInJedemModusProtokolliert() {
		// Die Prüfung hängt nicht an der Auslieferung: Ein Programmierfehler soll auch dort auffallen, wo der Header ohnehin nicht gesetzt wird - sonst
		// bemerkt ihn erst, wer zufällig in der Entwicklung nachsieht.
		gebeAusgabeumfangVor(new ReportingAusgabeumfang(3, 5, false));
		when(reportingContext.serverMode()).thenReturn(ServerMode.STABLE);

		assertNull(headerwert());

		assertTrue(log.getLogData().stream()
				.anyMatch(eintrag -> (eintrag.getLevel() == LogLevel.WARNING) && eintrag.getText().contains("verletzt die Zusagen des Vertrags")),
				"Der vertragswidrige Ausgabeumfang muss auch außerhalb der Entwicklung im Log erscheinen.");
	}

	@Test
	void testDerHeaderUeberstehtDieWeitergabeAnDenApiRand() {
		// Die Ausgabefactory reicht ihre Antwort nicht unverändert weiter: Die ReportingFactory klont sie, damit sie nicht auf einer bereits geschlossenen
		// Ressource sitzt. Verlöre der Klon die Header, käme der Hinweis-Header nie beim Aufrufer an - alle übrigen Header-Tests messen davor.
		gebeAusgabeumfangVor(new ReportingAusgabeumfang(5, 5, false));
		final Response ausgabe = ReportingHinweiseHeader
				.ergaenze(Response.ok("Inhalt").header("Content-Disposition", "attachment; filename=\"Test.pdf\""), reportingContext).build();

		final Response ausgeliefert = ReportingFactory.erzeugeResponse(() -> ausgabe);

		assertEquals("v=1, angefordert=5, ausgegeben=5, hinweise=0", ausgeliefert.getHeaderString(ReportingHinweisSerializer.HEADER_NAME));
		assertEquals("attachment; filename=\"Test.pdf\"", ausgeliefert.getHeaderString("Content-Disposition"),
				"Der Klon muss auch die übrigen Header übernehmen.");
	}

	@Test
	void testDerHeaderErsetztKeineAnderenAntwortangaben() {
		// Der Hinweis-Header tritt neben die vorhandenen Angaben der Antwort und ersetzt keine davon - insbesondere nicht den Dateinamen.
		gebeAusgabeumfangVor(new ReportingAusgabeumfang(5, 5, false));

		final Response response = ReportingHinweiseHeader
				.ergaenze(Response.ok("Inhalt").header("Content-Disposition", "attachment; filename=\"Test.pdf\""), reportingContext).build();

		assertEquals("attachment; filename=\"Test.pdf\"", response.getHeaderString("Content-Disposition"));
		assertEquals("v=1, angefordert=5, ausgegeben=5, hinweise=0", response.getHeaderString(ReportingHinweisSerializer.HEADER_NAME));
	}

}
