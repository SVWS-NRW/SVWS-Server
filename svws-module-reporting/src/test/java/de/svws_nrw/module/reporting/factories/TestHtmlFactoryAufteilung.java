package de.svws_nrw.module.reporting.factories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.builders.ReportBuilderHtml;
import de.svws_nrw.module.reporting.diagnose.ReportingAusgabeumfang;
import de.svws_nrw.module.reporting.diagnose.ReportingAuswahlergebnis;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.repositories.ReportingRepositoryLehrer;
import de.svws_nrw.module.reporting.repositories.ReportingRepositorySchule;
import de.svws_nrw.module.reporting.repositories.ReportingRepositoryStundenplan;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schule.ProxyReportingBenutzer;
import de.svws_nrw.module.reporting.types.schule.ReportingSchule;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungStundenplan;

/**
 * Tests der Entscheidung, ob die {@link HtmlFactory} ein Dokument je Datensatz erzeugt.
 * <p>Zwei Optionen führen dazu. Die Einzelausgabe, weil der Anwender je Datensatz eine Datei möchte, und der Broschürendruck, weil der Umbau auf DIN A3 die
 * Seitenzahl eines einzelnen Datensatzes braucht: Nur so füllt jeder Datensatz eigene Bogen. Im kombinierten Dokument ließe sich nicht erkennen, wo ein
 * Datensatz endet.</p>
 * <p>Gezählt werden die erzeugten Builder. Das Rendern übernimmt ein Konstruktions-Mock, denn für die Aufteilung ist der Inhalt ohne Belang. Der Datenaufbau
 * ist eine Sichtweise der Stundenplanung; sie ist aufteilbar und kommt mit gemockten Repositories aus.</p>
 */
class TestHtmlFactoryAufteilung {

	/** Die ID, unter der der Stundenplan angefordert wird. */
	private static final long ID_STUNDENPLAN = 42L;

	/** Die IDs der beiden Lehrkräfte, für die der Report erzeugt wird. */
	private static final List<Long> IDS_LEHRER = List.of(1L, 2L);

	/** Der gemockte Context, den die Factory erhält. */
	private ReportingContext reportingContext;

	/** Die Reporting-Parameter, die der Context liefert. Über sie stellen die Tests die Ausgabeoptionen ein. */
	private ReportingParameterTypisiert reportingParameter;


	@BeforeEach
	void setUp() throws ApiOperationException {
		reportingContext = mock(ReportingContext.class);
		when(reportingContext.logger()).thenReturn(new Logger());
		when(reportingContext.ausgabeprobleme()).thenReturn(List.of());
		// Der Initializer meldet den Umfang an den Context; der Mock schluckt die Meldung, deshalb liefert er den Wert hier direkt.
		when(reportingContext.ausgabeumfang()).thenReturn(new ReportingAusgabeumfang(2, 2, false));
		when(reportingContext.serverMode()).thenReturn(ServerMode.DEV);

		reportingParameter = mock(ReportingParameterTypisiert.class);
		when(reportingParameter.reportVorlage()).thenReturn(ReportingReportvorlage.STUNDENPLANUNG_V_LEHRER_STUNDENPLAN);
		when(reportingParameter.idHauptdatenObjekt()).thenReturn(ID_STUNDENPLAN);
		when(reportingParameter.idsHauptdaten()).thenReturn(IDS_LEHRER);
		when(reportingContext.reportingParameter()).thenReturn(reportingParameter);

		final ProxyReportingBenutzer benutzer = mock(ProxyReportingBenutzer.class);
		when(benutzer.pruefeKompetenz(any())).thenReturn(true);
		when(reportingContext.benutzer()).thenReturn(benutzer);

		final ReportingRepositorySchule repositorySchule = mock(ReportingRepositorySchule.class);
		when(repositorySchule.schule()).thenReturn(mock(ReportingSchule.class));
		when(reportingContext.repositorySchule()).thenReturn(repositorySchule);

		final ReportingRepositoryStundenplan repositoryStundenplan = mock(ReportingRepositoryStundenplan.class);
		when(repositoryStundenplan.stundenplan(ID_STUNDENPLAN))
				.thenReturn(new ReportingStundenplanungStundenplan("", null, null, ID_STUNDENPLAN, null, null, null, 1, null, null));
		when(reportingContext.repositoryStundenplan()).thenReturn(repositoryStundenplan);

		// Die Lehrkräfte entstehen vor den Stubs: Ein Mock, der innerhalb eines when-Aufrufs selbst Stubs setzt, bricht die laufende Stub-Anweisung ab.
		final ReportingLehrer lehrer1 = lehrer(1L);
		final ReportingLehrer lehrer2 = lehrer(2L);

		final ReportingRepositoryLehrer repositoryLehrer = mock(ReportingRepositoryLehrer.class);
		when(repositoryLehrer.waehleAus(IDS_LEHRER)).thenReturn(ReportingAuswahlergebnis.aus(IDS_LEHRER,
				Map.of(1L, lehrer1, 2L, lehrer2), Map.of(), List.of()));
		// Der Context baut seine Daten über diesen Aufruf auf - der Gesamt-Context mit beiden IDs, die Einzel-Contexts mit je einer. Beantwortet wird jede
		// Zusammenstellung von IDs, denn ihre Reihenfolge legt die Auswahl fest.
		when(repositoryLehrer.lehrer(anyList())).thenAnswer(aufruf -> {
			final List<Long> angefragteIds = aufruf.getArgument(0);
			return angefragteIds.stream().map(id -> (id == 1L) ? lehrer1 : lehrer2).toList();
		});
		when(reportingContext.repositoryLehrer()).thenReturn(repositoryLehrer);
	}

	/**
	 * Eine Lehrkraft mit den Angaben, die Aufbau und Sortierung des Contexts lesen.
	 *
	 * @param id Die ID der Lehrkraft.
	 *
	 * @return Die gemockte Lehrkraft.
	 */
	private static ReportingLehrer lehrer(final long id) {
		final ReportingLehrer lehrer = mock(ReportingLehrer.class);
		when(lehrer.id()).thenReturn(id);
		when(lehrer.kuerzel()).thenReturn("L" + id);
		when(lehrer.nachname()).thenReturn("Nachname" + id);
		when(lehrer.vorname()).thenReturn("Vorname" + id);
		return lehrer;
	}


	@Test
	void testOhneAufteilendeOptionEntstehtEinDokumentFuerAlleDatensaetze() throws ApiOperationException {
		assertEquals(1, anzahlDerDokumente(), "Ohne Einzelausgabe und ohne Broschürendruck liegen alle Datensätze in einem Dokument.");
	}

	@Test
	void testDieEinzelausgabeErzeugtEinDokumentJeDatensatz() throws ApiOperationException {
		when(reportingParameter.einzelausgabeDaten()).thenReturn(true);

		assertEquals(2, anzahlDerDokumente());
	}

	@Test
	void testDerBroschuerendruckErzeugtAuchOhneEinzelausgabeEinDokumentJeDatensatz() throws ApiOperationException {
		when(reportingParameter.broschuerendruck()).thenReturn(true);
		when(reportingParameter.einzelausgabeDaten()).thenReturn(false);

		assertEquals(2, anzahlDerDokumente(),
				"Der Umbau auf DIN A3 setzt an einem einzelnen Datensatz an und verlangt deshalb die Aufteilung, auch ohne gewählte Einzelausgabe.");
	}


	/**
	 * Erzeugt die HTML-Builder auf dem produktiven Weg und gibt deren Zahl zurück. Allein das Rendern übernimmt ein Konstruktions-Mock.
	 *
	 * @return Die Zahl der erzeugten Builder.
	 *
	 * @throws ApiOperationException Falls der Aufbau der Factory oder der Builder scheitert.
	 */
	private int anzahlDerDokumente() throws ApiOperationException {
		final HtmlFactory factory = HtmlFactory.erzeuge(reportingContext);
		try (MockedConstruction<ReportBuilderHtml> builder = mockConstruction(ReportBuilderHtml.class)) {
			return factory.createHtmlBuilders().size();
		}
	}

}
