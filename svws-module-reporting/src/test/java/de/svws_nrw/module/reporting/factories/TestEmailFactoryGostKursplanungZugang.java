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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import de.svws_nrw.base.email.EmailJobContext;
import de.svws_nrw.base.email.EmailJobManager;
import de.svws_nrw.base.email.EmailJobManagerFactory;
import de.svws_nrw.core.data.reporting.ReportingParameter;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.reporting.ReportingEMailEmpfaengerTyp;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.builders.ReportBuilderPdf;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.repositories.ReportingRepositoryGostKursplanung;
import de.svws_nrw.module.reporting.repositories.ReportingRepositorySchule;
import de.svws_nrw.module.reporting.types.schule.ProxyReportingBenutzer;
import jakarta.ws.rs.core.Response.Status;

/**
 * Prüft den Zugriff des E-Mail-Versands auf das Repository der GOSt-Kursplanung.
 * <p>Der Empfängertyp ist an keine Reportvorlage gebunden: Mit {@code GOSTKURSPLANUNG_KURSLEHRER} und einer beliebigen anderen Vorlage wird der Versand zum
 * ersten Zugriff auf das Kursplanungs-Repository, und zwar mit dem Vorgabewert der Reportparameter. Es gilt: Ohne ausgewähltes Blockungsergebnis bricht der
 * Versand mit {@code BAD_REQUEST} ab, und der Abbruch geschieht, bevor ein Job eingereiht ist - ein Teilversand kann daraus nicht entstehen.</p>
 * <p>Das Repository ist deshalb echt und nicht gemockt; ein Mock lieferte den Kurs heraus, ohne die Schranke des Aufbaus zu durchlaufen.</p>
 */
@DisplayName("Der Zugriff des E-Mail-Versands auf die GOSt-Kursplanung")
class TestEmailFactoryGostKursplanungZugang {

	/** Die ID, unter der die PDF-Builder gruppiert sind und zu der der Versand seine Empfänger sucht. */
	private static final long ID_EMPFAENGERGRUPPE = 42L;

	/** Der Context, den die Factory in den Tests erhält. */
	private ReportingContext reportingContext;

	/** Die Factory, deren Versand geprüft wird. */
	private EmailFactory emailFactory;

	/** Der gemockte Job-Manager, der den Versand einreihen würde. */
	private EmailJobManager jobManager;


	@BeforeEach
	void setUp() {
		reportingContext = mock(ReportingContext.class);
		when(reportingContext.logger()).thenReturn(new Logger());
		when(reportingContext.schemaName()).thenReturn("TestSchema");

		// Die Reportparameter sind echt und tragen damit den Vorgabewert der ID. Betreff und Text sind gesetzt, sonst endete der Versand vor dem Repository.
		final ReportingParameter reportingParameter = new ReportingParameter();
		reportingParameter.eMailDaten.betreff = "Kursliste";
		reportingParameter.eMailDaten.text = "Im Anhang finden Sie Ihre Kursliste.";
		reportingParameter.eMailDaten.empfaengerTyp = ReportingEMailEmpfaengerTyp.GOSTKURSPLANUNG_KURSLEHRER.getId();
		when(reportingContext.reportingParameter()).thenReturn(new ReportingParameterTypisiert(reportingContext, reportingParameter));

		final ProxyReportingBenutzer benutzer = mock(ProxyReportingBenutzer.class);
		when(benutzer.id()).thenReturn(1L);
		when(benutzer.emailSmtpAdresse()).thenReturn("absender@beispiel-schule.de");
		when(reportingContext.benutzer()).thenReturn(benutzer);

		final ReportingRepositorySchule repositorySchule = mock(ReportingRepositorySchule.class);
		when(repositorySchule.defaultEmailJobContext()).thenReturn(mock(EmailJobContext.class));
		when(reportingContext.repositorySchule()).thenReturn(repositorySchule);

		when(reportingContext.repositoryGostKursplanung()).thenReturn(new ReportingRepositoryGostKursplanung(reportingContext));

		jobManager = mock(EmailJobManager.class);
		emailFactory = new EmailFactory(reportingContext);
	}


	/**
	 * Erzeugt die PDF-Factory einer Ausgabe mit einer Gruppe, deren ID einen Empfänger bezeichnet.
	 * <p>Erst eine nicht negative ID mit mindestens einem Builder führt den Versand bis zur Ermittlung der Empfänger und damit bis zum Repository; eine leere
	 * Gruppe oder die neutrale Gruppe {@code -1} würde übersprungen.</p>
	 *
	 * @return Die PDF-Factory.
	 */
	private PdfFactory pdfFactoryMitEmpfaengergruppe() {
		final PdfFactory pdfFactory = mock(PdfFactory.class);
		when(pdfFactory.leereAusgabeZulaessig()).thenReturn(false);
		when(pdfFactory.getPdfBuildersById()).thenReturn(Map.of(ID_EMPFAENGERGRUPPE, List.of(mock(ReportBuilderPdf.class))));
		return pdfFactory;
	}

	/**
	 * Führt den Versand mit ersetztem Job-Manager aus.
	 *
	 * @throws ApiOperationException Wenn der Versand abbricht.
	 */
	private void versendeMitErsetztemJobManager() throws ApiOperationException {
		final EmailJobManagerFactory managerFactory = mock(EmailJobManagerFactory.class);
		when(managerFactory.getManager(anyString(), anyLong())).thenReturn(jobManager);
		try (MockedStatic<EmailJobManagerFactory> statisch = mockStatic(EmailJobManagerFactory.class)) {
			statisch.when(EmailJobManagerFactory::getInstance).thenReturn(managerFactory);
			emailFactory.sendEmails(pdfFactoryMitEmpfaengergruppe());
		}
	}


	@Test
	void testDerVersandAnKurslehrerOhneAusgewaehltesBlockungsergebnisBrichtMitBadRequestAb() {
		final ApiOperationException aoe = assertThrows(ApiOperationException.class, this::versendeMitErsetztemJobManager);

		assertEquals(Status.BAD_REQUEST, aoe.getStatus(), "Eine fehlende Auswahl ist eine fehlerhafte Anfrage und kein Serverfehler.");
		assertTrue(aoe.getMessage().contains("Blockungsergebnis"),
				"Der Abbruch muss aus dem Aufbau des Kursplanungs-Repositorys stammen und nicht aus einer früheren Prüfung: " + aoe.getMessage());
		verify(reportingContext).repositoryGostKursplanung();
	}

	@Test
	void testDerAbbruchGeschiehtVorDemEinreihenDesJobs() {
		// Ein eingereihter Auftrag versendete an die Empfänger, die vor dem Abbruch zusammengekommen sind.
		assertThrows(ApiOperationException.class, this::versendeMitErsetztemJobManager);

		verify(jobManager, never()).enqueue(any());
	}

}
