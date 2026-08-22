package de.svws_nrw.module.reporting.factories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import de.svws_nrw.base.email.EmailJobContext;
import de.svws_nrw.base.email.EmailJobManager;
import de.svws_nrw.base.email.EmailJobManagerFactory;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.reporting.ReportingEMailDaten;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.reporting.ReportingEMailEmpfaengerTyp;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.builders.ReportBuilderHtml;
import de.svws_nrw.module.reporting.diagnose.ReportingAusgabeumfang;
import de.svws_nrw.module.reporting.diagnose.ReportingHinweisSerializer;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.repositories.ReportingRepositorySchule;
import de.svws_nrw.module.reporting.types.schule.ProxyReportingBenutzer;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests des E-Mail-Versands für eine Auswahl, die bewusst keinen Datensatz enthält.
 * <p>Seit die PDF-Ausgabe eine leere Auswahl als gültige Antwort behandelt, erreicht dieselbe leere Builder-Liste auch den E-Mail-Pfad. Dort darf sie <b>nicht</b>
 * zu einem eingereihten Job führen: Ein Job ohne Anhänge sähe erfolgreich aus, ohne etwas zu versenden - der Anwender erhielte eine Job-ID, verfolgte deren
 * Status und erfuhr nie, dass nichts unterwegs war.</p>
 * <p>Der Job-Manager wird über seine statische Factory ersetzt, damit der Test die entscheidende Aussage belegen kann: {@code enqueue} wird nicht aufgerufen.
 * Ein Test, der nur die Antwort prüft, bliebe grün, wenn der Job daneben trotzdem eingereiht würde.</p>
 */
class TestEmailFactoryLeerfall {

	/** Der Context, den die Factory in den Tests erhält. */
	private ReportingContext reportingContext;

	/** Die Factory, deren Startantwort geprüft wird. */
	private EmailFactory emailFactory;

	/** Der gemockte Job-Manager, der den Versand einreihen würde. */
	private EmailJobManager jobManager;


	@BeforeEach
	void setUp() {
		reportingContext = mock(ReportingContext.class);
		when(reportingContext.logger()).thenReturn(new Logger());
		when(reportingContext.schemaName()).thenReturn("TestSchema");

		// Der E-Mail-Versand prüft zuerst Betreff und Text. Beide sind gesetzt, damit der Test den Leerfall und nicht eine fehlerhafte Anfrage prüft.
		final ReportingEMailDaten eMailDaten = new ReportingEMailDaten();
		eMailDaten.betreff = "Schulbescheinigung";
		eMailDaten.text = "Im Anhang finden Sie Ihre Schulbescheinigung.";
		final ReportingParameterTypisiert reportingParameter = mock(ReportingParameterTypisiert.class);
		when(reportingParameter.eMailDaten()).thenReturn(eMailDaten);
		when(reportingContext.reportingParameter()).thenReturn(reportingParameter);

		final ProxyReportingBenutzer benutzer = mock(ProxyReportingBenutzer.class);
		when(benutzer.id()).thenReturn(1L);
		when(reportingContext.benutzer()).thenReturn(benutzer);

		jobManager = mock(EmailJobManager.class);
		emailFactory = new EmailFactory(reportingContext);
	}


	/**
	 * Erzeugt die PDF-Factory einer bewusst leeren Auswahl: keine Builder und ein Ausgabeumfang, der die leere Ausgabe zulässt.
	 *
	 * @return Die PDF-Factory.
	 *
	 * @throws ApiOperationException Wenn die Factory die übergebenen Daten ablehnt.
	 */
	private PdfFactory pdfFactoryOhneDokument() throws ApiOperationException {
		when(reportingContext.ausgabeumfang()).thenReturn(new ReportingAusgabeumfang(5, 0, true));
		return new PdfFactory(List.of(), reportingContext);
	}

	/**
	 * Erzeugt die PDF-Factory einer regulären Ausgabe mit einem Dokument.
	 * <p>Die Kombination "keine Builder, Kennzeichen nicht gesetzt" ist hier nicht verwendbar: Der Konstruktor der PDF-Factory lehnt sie als
	 * Programmierfehler ab. Die Gegenprobe braucht daher einen Builder.</p>
	 *
	 * @return Die PDF-Factory.
	 *
	 * @throws ApiOperationException Wenn die Factory die übergebenen Daten ablehnt.
	 */
	private PdfFactory pdfFactoryMitDokument() throws ApiOperationException {
		when(reportingContext.ausgabeumfang()).thenReturn(new ReportingAusgabeumfang(1, 1, false));
		return new PdfFactory(List.of(mock(ReportBuilderHtml.class)), reportingContext);
	}

	/**
	 * Führt den übergebenen Versand mit ersetztem Job-Manager aus.
	 *
	 * @param versand Der auszuführende Versand.
	 *
	 * @throws ApiOperationException Wenn der Versand abbricht.
	 */
	private void mitErsetztemJobManager(final Versand versand) throws ApiOperationException {
		final EmailJobManagerFactory managerFactory = mock(EmailJobManagerFactory.class);
		when(managerFactory.getManager(anyString(), anyLong())).thenReturn(jobManager);
		try (MockedStatic<EmailJobManagerFactory> statisch = mockStatic(EmailJobManagerFactory.class)) {
			statisch.when(EmailJobManagerFactory::getInstance).thenReturn(managerFactory);
			versand.fuehreAus();
		}
	}

	/** Ein Versand, der eine {@link ApiOperationException} auslösen darf. */
	private interface Versand {

		/**
		 * Führt den Versand aus.
		 *
		 * @throws ApiOperationException Wenn der Versand abbricht.
		 */
		void fuehreAus() throws ApiOperationException;
	}


	@Test
	void testBeiBewusstLeererAuswahlWirdKeinJobEingereiht() throws ApiOperationException {
		final Response[] response = new Response[1];

		mitErsetztemJobManager(() -> response[0] = emailFactory.sendEmails(pdfFactoryOhneDokument()));

		verify(jobManager, never()).enqueue(any());
		assertEquals(Status.OK.getStatusCode(), response[0].getStatus(),
				"Die Anfrage ist ordnungsgemäß bearbeitet; ein Fehlerstatus wäre falsch, denn eine leere Auswahl ist eine gewollte Auswahlentscheidung.");
		final SimpleOperationResponse simple = (SimpleOperationResponse) response[0].getEntity();
		assertTrue(simple.success);
		assertEquals(1, simple.log.size());
		assertTrue(simple.log.getFirst().contains("kein E-Mail-Versand-Job gestartet"),
				"Die Startantwort muss beschreiben, dass kein Job eingereiht wurde: " + simple.log.getFirst());
	}

	@Test
	void testDieAntwortOhneJobTraegtKeineJobId() throws ApiOperationException {
		// Eine Job-ID lüde den Anwender ein, einen Status zu verfolgen, den es nicht gibt.
		final Response[] response = new Response[1];

		mitErsetztemJobManager(() -> response[0] = emailFactory.sendEmails(pdfFactoryOhneDokument()));

		assertEquals(null, ((SimpleOperationResponse) response[0].getEntity()).id);
	}

	@Test
	void testDieStartantwortTraegtKeinenDokumentheader() throws ApiOperationException {
		// Der Hinweis-Header gehört zu den Dokumentantworten (HTML, PDF, ZIP). Die JSON-Startantwort des Versands liefert kein Dokument; ob und wo Hinweise
		// dort erscheinen, ist eine eigene fachliche Entscheidung und bewusst nicht Teil des Vertrags.
		final Response[] response = new Response[1];

		mitErsetztemJobManager(() -> response[0] = emailFactory.sendEmails(pdfFactoryOhneDokument()));

		assertEquals(null, response[0].getHeaderString(ReportingHinweisSerializer.HEADER_NAME),
				"Die E-Mail-Startantwort darf den Dokumentheader nicht tragen.");
	}

	@Test
	void testDieRegulaereStartantwortNachDemEinreihenTraegtKeinenDokumentheader() throws ApiOperationException {
		// Der Nachweis am Leerfall allein genügte nicht: Erhielte nur der Hauptpfad mit eingereihtem Job später den Dokumentheader, bliebe das unbemerkt.
		// Die PDF-Factory ist gemockt und liefert eine Gruppierung ohne zuordenbaren Empfänger (ID -1) - sie wird übersprungen, der Job wird dennoch
		// eingereiht, und die reguläre Startantwort entsteht ohne Empfänger-Auflösung und ohne PDF-Rendering.
		final ReportingRepositorySchule repositorySchule = mock(ReportingRepositorySchule.class);
		when(repositorySchule.defaultEmailJobContext()).thenReturn(mock(EmailJobContext.class));
		when(reportingContext.repositorySchule()).thenReturn(repositorySchule);
		when(reportingContext.benutzer().emailSmtpAdresse()).thenReturn("absender@beispiel-schule.de");
		reportingContext.reportingParameter().eMailDaten().empfaengerTyp = ReportingEMailEmpfaengerTyp.SCHUELER.getId();
		final PdfFactory pdfFactory = mock(PdfFactory.class);
		when(pdfFactory.leereAusgabeZulaessig()).thenReturn(false);
		when(pdfFactory.getPdfBuildersById()).thenReturn(Map.of(-1L, List.of()));
		final Response[] response = new Response[1];

		mitErsetztemJobManager(() -> response[0] = emailFactory.sendEmails(pdfFactory));

		verify(jobManager).enqueue(any());
		assertEquals(Status.ACCEPTED.getStatusCode(), response[0].getStatus(), "Der Hauptpfad muss das Einreihen erreichen, sonst prüft der Test ihn nicht.");
		assertEquals(null, response[0].getHeaderString(ReportingHinweisSerializer.HEADER_NAME),
				"Auch die reguläre Startantwort darf den Dokumentheader nicht tragen.");
	}

	@Test
	void testOhneDasKennzeichenLaeuftDerVersandWeiter() {
		// Gegenprobe: Ohne das Kennzeichen kommt der Ablauf über die Prüfung hinaus und erreicht die Ermittlung der Absenderadresse, an der er hier scheitert.
		// Damit ist belegt, dass die neue Prüfung den Leerfall abfängt und nicht irgendein anderer Abbruch die Einreihung verhindert hat.
		final ReportingRepositorySchule repositorySchule = mock(ReportingRepositorySchule.class);
		when(repositorySchule.defaultEmailJobContext()).thenReturn(mock(EmailJobContext.class));
		when(reportingContext.repositorySchule()).thenReturn(repositorySchule);
		when(reportingContext.benutzer().emailSmtpAdresse()).thenReturn("");

		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> mitErsetztemJobManager(() -> emailFactory.sendEmails(pdfFactoryMitDokument())));

		assertEquals(Status.BAD_REQUEST, aoe.getStatus(), "Der Ablauf muss die Absenderprüfung erreichen und dort mit deren eigenem Status abbrechen.");
		verify(jobManager, never()).enqueue(any());
	}

}
