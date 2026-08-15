package de.svws_nrw.module.reporting.factories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.module.reporting.diagnose.ReportingHinweisSerializer;
import de.svws_nrw.module.reporting.diagnose.ReportingProblem;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemSchluessel;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemauswirkung;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemursache;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import jakarta.ws.rs.core.Response;

/**
 * Tests der Entscheidung, ob eine erfolgreiche Ausgabe den öffentlichen Hinweis-Header trägt.
 * <p>Die Abdeckung ist der Kern dieser Tests. Ein Header mit {@code gesamt=0} an einem Datenaufbau, dessen Datenzugriffe noch nicht über die Diagnose melden,
 * wäre schlimmer als kein Header: Er bescheinigte dem Client eine geprüfte Vollständigkeit, die niemand festgestellt hat. Ein fehlender Header bedeutet dagegen
 * "Abdeckung unbekannt" - eine Aussage, die immer zutrifft.</p>
 * <p>Die Sollwerte des Headers stehen als Literale. Der Wert ist ein Vertrag mit dem Client; würde der Test ihn aus dem Serializer bilden, schriebe er jede
 * Vertragsänderung stillschweigend mit.</p>
 */
class TestReportingHinweiseHeader {

	/** Eine Reportvorlage, deren Datenaufbau vollständig an die Diagnose angebunden ist. */
	private static final ReportingReportvorlage ANGEBUNDEN = ReportingReportvorlage.SCHUELER_V_SCHULBESCHEINIGUNG;

	/** Eine Reportvorlage, deren Datenaufbau noch nicht vollständig angebunden ist. */
	private static final ReportingReportvorlage NICHT_ANGEBUNDEN = ReportingReportvorlage.SCHUELER_V_GOST_LAUFBAHNPLANUNG_WAHLBOGEN;

	/** Der Context, den der Helfer in den Tests erhält. */
	private ReportingContext reportingContext;


	@BeforeEach
	void setUp() {
		reportingContext = mock(ReportingContext.class);
		when(reportingContext.ausgabeprobleme()).thenReturn(List.of());
	}


	/**
	 * Verdrahtet die Reportvorlage des Aufrufs.
	 *
	 * @param reportvorlage Die Reportvorlage oder {@code null}.
	 */
	private void gebeReportvorlageVor(final ReportingReportvorlage reportvorlage) {
		final ReportingParameterTypisiert reportingParameter = mock(ReportingParameterTypisiert.class);
		when(reportingParameter.reportVorlage()).thenReturn(reportvorlage);
		when(reportingContext.reportingParameter()).thenReturn(reportingParameter);
	}

	/**
	 * Gibt den Wert des Hinweis-Headers einer erfolgreichen Antwort zurück.
	 *
	 * @param bewusstLeer true, wenn Datensätze angefordert waren und keiner übrig blieb.
	 *
	 * @return Der Headerwert oder {@code null}, wenn die Antwort den Header nicht trägt.
	 */
	private String headerwert(final boolean bewusstLeer) {
		final Response response = ReportingHinweiseHeader.ergaenze(Response.ok("Inhalt"), reportingContext, bewusstLeer).build();
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
	void testEinAngebundenerDatenaufbauTraegtDenHeaderAuchOhneBefund() {
		// gesamt=0 ist hier eine belastbare Aussage: Die Datenzugriffe dieses Datenaufbaus melden vollständig über die Diagnose.
		gebeReportvorlageVor(ANGEBUNDEN);

		assertEquals("v=0, gesamt=0, leer=?0", headerwert(false));
	}

	@Test
	void testEinMitDerAuswahlpipelineAngebundenerStundenplanPfadTraegtDenHeader() {
		// Die Anbindung wuchs vom Schüler-Piloten auf die Listen-Aufbauten und die Sichtweisen der Stundenplanung. Der Nachweis hängt deshalb nicht mehr
		// allein am Piloten.
		gebeReportvorlageVor(ReportingReportvorlage.STUNDENPLANUNG_V_RAUM_STUNDENPLAN);

		assertEquals("v=0, gesamt=0, leer=?0", headerwert(false));
	}

	@Test
	void testEinNichtAngebundenerDatenaufbauTraegtKeinenHeader() {
		// Der Header fehlt ganz, statt eine Vollständigkeit zu behaupten, die niemand geprüft hat.
		gebeReportvorlageVor(NICHT_ANGEBUNDEN);

		assertNull(headerwert(false), "Ein nicht angebundener Datenaufbau darf keine Nullmeldung senden.");
	}

	@Test
	void testDieGemeldetenProblemeErreichenDenHeader() {
		gebeReportvorlageVor(ANGEBUNDEN);
		when(reportingContext.ausgabeprobleme()).thenReturn(List.of(ausgelassenerDatensatz(1L), ausgelassenerDatensatz(2L)));

		assertEquals("v=0, gesamt=2, leer=?0, datensaetze=2", headerwert(false));
	}

	@Test
	void testEineBewusstLeereAusgabeMeldetDenLeerstand() {
		// Der reine Filter-Leerfall: kein Befund, aber es blieb kein angeforderter Datensatz übrig. Der Client unterscheidet daran eine gewollt leere Ausgabe
		// von einem Fehler. Ob dabei ein Dokument entsteht, ist für diesen Wert unerheblich - er folgt der Auswahl.
		gebeReportvorlageVor(ANGEBUNDEN);

		assertEquals("v=0, gesamt=0, leer=?1", headerwert(true));
	}

	@Test
	void testOhneReportvorlageEntstehtKeinHeader() {
		// Ohne Vorlage ist der Datenaufbau unbekannt und damit auch seine Abdeckung. Der Helfer läuft im Erfolgspfad und darf dort nicht werfen.
		gebeReportvorlageVor(null);

		assertNull(headerwert(false));
	}

	@Test
	void testDerHeaderErsetztKeineAnderenAntwortangaben() {
		// Der Hinweis-Header tritt neben die vorhandenen Angaben der Antwort und ersetzt keine davon - insbesondere nicht den Dateinamen.
		gebeReportvorlageVor(ANGEBUNDEN);

		final Response response = ReportingHinweiseHeader
				.ergaenze(Response.ok("Inhalt").header("Content-Disposition", "attachment; filename=\"Test.pdf\""), reportingContext, false).build();

		assertEquals("attachment; filename=\"Test.pdf\"", response.getHeaderString("Content-Disposition"));
		assertEquals("v=0, gesamt=0, leer=?0", response.getHeaderString(ReportingHinweisSerializer.HEADER_NAME));
	}

}
