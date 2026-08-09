package de.svws_nrw.module.reporting.html.contexts.initializer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostLaufbahnplanungBeratungsdaten;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.repositories.ReportingRepositoryGost;
import de.svws_nrw.module.reporting.repositories.ReportingRepositoryLehrer;
import de.svws_nrw.module.reporting.repositories.ReportingRepositoryLerngruppen;
import de.svws_nrw.module.reporting.repositories.ReportingRepositorySchueler;
import de.svws_nrw.module.reporting.repositories.ReportingRepositorySchule;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKlasse;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests der Prüfungen aus {@link HtmlContextValidierung}, die auf die Domänen-Repositories zugreifen.
 * <p>Der {@link ReportingContext} wird gemockt, weil sein einziger Konstruktor alle neun Repositories aufbaut und dafür eine Datenbankverbindung
 * benötigt. Der Mock umgeht den Konstruktor; je Test werden nur der Logger und die tatsächlich benötigten Repository-Getter verdrahtet.</p>
 * <p>Der Schwerpunkt liegt auf den <b>Fehlerpfaden</b>: Sie sind heute nirgends abgedeckt und legen zugleich das Verhalten fest, gegen das die
 * Statusklassifikation und der Repository-Fehlervertrag später geprüft werden.</p>
 */
class TestHtmlContextValidierungMitContext {

	/** Die Meldung, die eine fehlende gymnasiale Oberstufe anzeigt. */
	private static final String MELDUNG_KEINE_GOST = "FEHLER: Die Schule besitzt keine gymnasiale Oberstufe (GOSt).";

	/** Die Meldung, die die Parameterprüfung bei einem nicht auswertbaren Wert erzeugt. */
	private static final String MELDUNG_PARAMETER_UNLESBAR =
			"FEHLER: Die Parameter für Abiturjahrgang und GOSt-Halbjahr konnten nicht gelesen werden oder sind außerhalb des Wertebereichs.";

	/** Der gemockte Context, den die Prüfungen in den Tests erhalten. */
	private ReportingContext reportingContext;

	/** Die Liste, die die Einträge des Loggers sammelt. */
	private LogConsumerList log;


	@BeforeEach
	void setUp() {
		reportingContext = mock(ReportingContext.class);
		final Logger logger = new Logger();
		log = new LogConsumerList();
		logger.addConsumer(log);
		when(reportingContext.logger()).thenReturn(logger);
	}


	/**
	 * Gibt die Texte aller Logeinträge mit dem Level ERROR zurück. Die Einrückung, die der Logger dem Text voranstellt, wird dabei entfernt.
	 *
	 * @return Die Texte der ERROR-Logeinträge in der Reihenfolge ihres Auftretens.
	 */
	private List<String> fehlermeldungenImLog() {
		return log.getLogData().stream().filter(eintrag -> eintrag.getLevel() == LogLevel.ERROR).map(eintrag -> eintrag.getText().strip()).toList();
	}

	/**
	 * Verdrahtet das Schul-Repository und legt fest, ob die Schule eine gymnasiale Oberstufe besitzt.
	 *
	 * @param mitGost true, wenn die Schule eine gymnasiale Oberstufe besitzt.
	 */
	private void gebeSchuleVor(final boolean mitGost) {
		final ReportingRepositorySchule repositorySchule = mock(ReportingRepositorySchule.class);
		when(repositorySchule.istSchuleMitGost()).thenReturn(mitGost);
		when(reportingContext.repositorySchule()).thenReturn(repositorySchule);
	}

	/**
	 * Verdrahtet die typisierten Reporting-Parameter mit den übergebenen Hauptdaten-IDs.
	 *
	 * @param idsHauptdaten Die IDs, die als Hauptdaten des Requests gelten sollen.
	 */
	private void gebeHauptdatenIdsVor(final List<Long> idsHauptdaten) {
		final ReportingParameterTypisiert reportingParameter = mock(ReportingParameterTypisiert.class);
		when(reportingParameter.idsHauptdaten()).thenReturn(idsHauptdaten);
		when(reportingContext.reportingParameter()).thenReturn(reportingParameter);
	}

	/**
	 * Verdrahtet das GOSt-Repository und gibt es zur weiteren Konfiguration zurück.
	 *
	 * @return Das gemockte GOSt-Repository.
	 */
	private ReportingRepositoryGost gebeRepositoryGostVor() {
		final ReportingRepositoryGost repositoryGost = mock(ReportingRepositoryGost.class);
		when(reportingContext.repositoryGost()).thenReturn(repositoryGost);
		return repositoryGost;
	}

	/**
	 * Erzeugt eine Klasse mit der angegebenen ID.
	 *
	 * @param id Die ID der Klasse.
	 *
	 * @return Die Klasse als Mock, der allein seine ID kennt.
	 */
	private static ReportingKlasse klasseMitId(final long id) {
		final ReportingKlasse klasse = mock(ReportingKlasse.class);
		when(klasse.id()).thenReturn(id);
		return klasse;
	}

	/**
	 * Erzeugt eine Lehrkraft mit der angegebenen ID.
	 *
	 * @param id Die ID der Lehrkraft.
	 *
	 * @return Die Lehrkraft als Mock, der allein seine ID kennt.
	 */
	private static ReportingLehrer lehrerMitId(final long id) {
		final ReportingLehrer lehrer = mock(ReportingLehrer.class);
		when(lehrer.id()).thenReturn(id);
		return lehrer;
	}

	/**
	 * Erzeugt einen Schüler mit der angegebenen ID.
	 *
	 * @param id Die ID des Schülers.
	 *
	 * @return Der Schüler als Mock, der allein seine ID kennt.
	 */
	private static ReportingSchueler schuelerMitId(final long id) {
		final ReportingSchueler schueler = mock(ReportingSchueler.class);
		when(schueler.id()).thenReturn(id);
		return schueler;
	}


	// ##### validiereParameterFuerAbiturjahrgangUndHalbjahre #####

	@Test
	void testOhneHauptdatenIdsWirdDieParameterpruefungAbgewiesen() {
		gebeHauptdatenIdsVor(List.of());

		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereParameterFuerAbiturjahrgangUndHalbjahre(reportingContext, false));

		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals("FEHLER: Die Parameter für Abiturjahrgang und GOSt-Halbjahr wurden nicht übergeben.", aoe.getBody());
		verify(reportingContext, never()).repositoryGost();
	}

	@Test
	void testGueltigePaarweiseParameterWerdenAkzeptiert() {
		gebeHauptdatenIdsVor(List.of(20253L));
		when(gebeRepositoryGostVor().abiturjahrgaenge()).thenReturn(List.of(2025));

		assertDoesNotThrow(() -> HtmlContextValidierung.validiereParameterFuerAbiturjahrgangUndHalbjahre(reportingContext, true));
	}

	@Test
	void testGueltigeEinzelneParameterWerdenAkzeptiert() {
		gebeHauptdatenIdsVor(List.of(2025L, 3L));
		when(gebeRepositoryGostVor().abiturjahrgaenge()).thenReturn(List.of(2025));

		assertDoesNotThrow(() -> HtmlContextValidierung.validiereParameterFuerAbiturjahrgangUndHalbjahre(reportingContext, false));
	}

	@Test
	void testEineWertebereichsverletzungBehaeltIhreEigeneMeldung() {
		// Die Wertprüfungen werfen selbst eine ApiOperationException. Der vorgezogene catch-Zweig muss sie unverändert durchreichen, statt sie im
		// allgemeinen catch in die pauschale Meldung zu verpacken.
		gebeHauptdatenIdsVor(List.of(20256L));
		when(gebeRepositoryGostVor().abiturjahrgaenge()).thenReturn(List.of(2025));

		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereParameterFuerAbiturjahrgangUndHalbjahre(reportingContext, true));

		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals("FEHLER: Ein GOSt-Halbjahr liegt außerhalb des Wertebereichs.", aoe.getBody());
	}

	@Test
	void testEinNullEintragInDenEinzelnenParameternFuehrtNichtZumServerfehler() {
		// Die paarweise Variante überspringt null-Einträge ausdrücklich, die einzelne nicht. Der allgemeine catch-Zweig fängt die dabei entstehende
		// NullPointerException ab, sodass am API-Rand ein Client-Fehler und kein Serverfehler ankommt.
		gebeHauptdatenIdsVor(Arrays.asList(2025L, null));
		when(gebeRepositoryGostVor().abiturjahrgaenge()).thenReturn(List.of(2025));

		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereParameterFuerAbiturjahrgangUndHalbjahre(reportingContext, false));

		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals(MELDUNG_PARAMETER_UNLESBAR, aoe.getBody());
	}

	@Test
	void testEinFehlerBeimLadenDerAbiturjahrgaengeWirdHeuteAlsBadRequestGemeldet() {
		// BESTANDSVERHALTEN: Ein serverseitiges Ladeproblem wird hier als Client-Fehler gemeldet. Der Fehlervertrag aus Block 5 des
		// Stabilisierungsplans löst das auf; bis dahin hält dieser Test den Ist-Zustand fest, damit die spätere Änderung sichtbar wird.
		gebeHauptdatenIdsVor(List.of(2025L));
		final IllegalStateException ursache = new IllegalStateException("Datenbank nicht erreichbar");
		when(gebeRepositoryGostVor().abiturjahrgaenge()).thenThrow(ursache);

		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereParameterFuerAbiturjahrgangUndHalbjahre(reportingContext, false));

		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals(MELDUNG_PARAMETER_UNLESBAR, aoe.getBody());
		assertSame(ursache, aoe.getCause());
	}


	// ##### validiereSchuleMitGost #####

	@Test
	void testEineSchuleMitGostBestehtDiePruefung() {
		gebeSchuleVor(true);

		assertDoesNotThrow(() -> HtmlContextValidierung.validiereSchuleMitGost(reportingContext));
		assertEquals(List.of(), fehlermeldungenImLog());
	}

	@Test
	void testEineSchuleOhneGostWirdAbgewiesen() {
		gebeSchuleVor(false);

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> HtmlContextValidierung.validiereSchuleMitGost(reportingContext));

		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals(MELDUNG_KEINE_GOST, aoe.getBody());
		assertEquals(List.of(MELDUNG_KEINE_GOST), fehlermeldungenImLog());
	}


	// ##### Stundenplan-Prüfungen #####

	@Test
	void testVollstaendigeKlassenIdsBestehenDieStundenplanpruefung() {
		final ReportingKlasse klasse = klasseMitId(1L);
		final ReportingRepositoryLerngruppen repositoryLerngruppen = mock(ReportingRepositoryLerngruppen.class);
		when(repositoryLerngruppen.klassen(List.of(1L), false)).thenReturn(List.of(klasse));
		when(reportingContext.repositoryLerngruppen()).thenReturn(repositoryLerngruppen);

		assertDoesNotThrow(() -> HtmlContextValidierung.pruefungenStundenplanKlassen(reportingContext, List.of(1L)));
		assertEquals(List.of(), fehlermeldungenImLog());
	}

	@Test
	void testEineNichtGeladeneKlassenIdWirdAbgewiesen() {
		// Die Meldung nennt die betroffene ID nicht. Das ist Bestandsverhalten und wird mit Block 5.3 des Stabilisierungsplans ergänzt.
		final List<Long> ids = List.of(1L, 2L);
		final ReportingKlasse klasse = klasseMitId(1L);
		final ReportingRepositoryLerngruppen repositoryLerngruppen = mock(ReportingRepositoryLerngruppen.class);
		when(repositoryLerngruppen.klassen(ids, false)).thenReturn(List.of(klasse));
		when(reportingContext.repositoryLerngruppen()).thenReturn(repositoryLerngruppen);

		final ApiOperationException aoe =
				assertThrows(ApiOperationException.class, () -> HtmlContextValidierung.pruefungenStundenplanKlassen(reportingContext, ids));

		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals("FEHLER: Es wurden ungültige Klassen-IDs übergeben.", aoe.getBody());
		assertEquals(List.of("FEHLER: Es wurden ungültige Klassen-IDs übergeben."), fehlermeldungenImLog());
	}

	@Test
	void testVollstaendigeLehrerIdsBestehenDieStundenplanpruefung() {
		final ReportingLehrer lehrer = lehrerMitId(1L);
		final ReportingRepositoryLehrer repositoryLehrer = mock(ReportingRepositoryLehrer.class);
		when(repositoryLehrer.lehrer(List.of(1L), false)).thenReturn(List.of(lehrer));
		when(reportingContext.repositoryLehrer()).thenReturn(repositoryLehrer);

		assertDoesNotThrow(() -> HtmlContextValidierung.pruefungenStundenplanLehrer(reportingContext, List.of(1L)));
		assertEquals(List.of(), fehlermeldungenImLog());
	}

	@Test
	void testEineNichtGeladeneLehrerIdWirdAbgewiesen() {
		final List<Long> ids = List.of(1L, 2L);
		final ReportingLehrer lehrer = lehrerMitId(1L);
		final ReportingRepositoryLehrer repositoryLehrer = mock(ReportingRepositoryLehrer.class);
		when(repositoryLehrer.lehrer(ids, false)).thenReturn(List.of(lehrer));
		when(reportingContext.repositoryLehrer()).thenReturn(repositoryLehrer);

		final ApiOperationException aoe =
				assertThrows(ApiOperationException.class, () -> HtmlContextValidierung.pruefungenStundenplanLehrer(reportingContext, ids));

		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals("FEHLER: Es wurden ungültige Lehrer-IDs übergeben.", aoe.getBody());
		assertEquals(List.of("FEHLER: Es wurden ungültige Lehrer-IDs übergeben."), fehlermeldungenImLog());
	}

	@Test
	void testVollstaendigeSchuelerIdsBestehenDieStundenplanpruefung() {
		final ReportingSchueler schueler = schuelerMitId(1L);
		final ReportingRepositorySchueler repositorySchueler = mock(ReportingRepositorySchueler.class);
		when(repositorySchueler.schueler(List.of(1L), false)).thenReturn(List.of(schueler));
		when(reportingContext.repositorySchueler()).thenReturn(repositorySchueler);

		assertDoesNotThrow(() -> HtmlContextValidierung.pruefungenStundenplanSchueler(reportingContext, List.of(1L)));
		assertEquals(List.of(), fehlermeldungenImLog());
	}

	@Test
	void testEineNichtGeladeneSchuelerIdWirdAbgewiesen() {
		final List<Long> ids = List.of(1L, 2L);
		final ReportingSchueler schueler = schuelerMitId(1L);
		final ReportingRepositorySchueler repositorySchueler = mock(ReportingRepositorySchueler.class);
		when(repositorySchueler.schueler(ids, false)).thenReturn(List.of(schueler));
		when(reportingContext.repositorySchueler()).thenReturn(repositorySchueler);

		final ApiOperationException aoe =
				assertThrows(ApiOperationException.class, () -> HtmlContextValidierung.pruefungenStundenplanSchueler(reportingContext, ids));

		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals("FEHLER: Es wurden ungültige Schüler-IDs übergeben.", aoe.getBody());
		assertEquals(List.of("FEHLER: Es wurden ungültige Schüler-IDs übergeben."), fehlermeldungenImLog());
	}


	// ##### GOSt-Laufbahnplanung #####

	@Test
	void testDieLaufbahnplanungPruefungBestehtBeiVollstaendigenDaten() {
		gebeSchuleVor(true);
		final ReportingRepositoryGost repositoryGost = gebeRepositoryGostVor();
		when(repositoryGost.beratungsdaten(List.of(1L))).thenReturn(Map.of(1L, new GostLaufbahnplanungBeratungsdaten()));
		when(repositoryGost.beratungsdatenAbiturdaten(List.of(1L))).thenReturn(Map.of(1L, new Abiturdaten()));

		assertDoesNotThrow(() -> HtmlContextValidierung.pruefungenGostLaufbahnplanung(reportingContext, List.of(1L)));
		assertEquals(List.of(), fehlermeldungenImLog());
	}

	@Test
	void testDieLaufbahnplanungPruefungBrichtOhneGostVorDenBeratungsdatenAb() {
		gebeSchuleVor(false);
		final List<Long> ids = List.of(1L);

		final ApiOperationException aoe =
				assertThrows(ApiOperationException.class, () -> HtmlContextValidierung.pruefungenGostLaufbahnplanung(reportingContext, ids));

		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals(MELDUNG_KEINE_GOST, aoe.getBody());
		verify(reportingContext, never()).repositoryGost();
	}

	@Test
	void testFehlendeBeratungsdatenWerdenAlsNichtZurGostGehoerendGemeldet() {
		gebeSchuleVor(true);
		final List<Long> ids = List.of(1L);
		final ReportingRepositoryGost repositoryGost = gebeRepositoryGostVor();
		final Map<Long, GostLaufbahnplanungBeratungsdaten> beratungsdaten = new HashMap<>();
		beratungsdaten.put(1L, null);
		when(repositoryGost.beratungsdaten(ids)).thenReturn(beratungsdaten);

		final ApiOperationException aoe =
				assertThrows(ApiOperationException.class, () -> HtmlContextValidierung.pruefungenGostLaufbahnplanung(reportingContext, ids));

		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals("FEHLER: Es wurden Schüler-IDs übergeben, die nicht zur GOSt gehören.", aoe.getBody());
	}

	@Test
	void testFehlendeAbiturdatenDerLaufbahnplanungWerdenGesondertGemeldet() {
		gebeSchuleVor(true);
		final List<Long> ids = List.of(1L);
		final ReportingRepositoryGost repositoryGost = gebeRepositoryGostVor();
		when(repositoryGost.beratungsdaten(ids)).thenReturn(Map.of(1L, new GostLaufbahnplanungBeratungsdaten()));
		when(repositoryGost.beratungsdatenAbiturdaten(ids)).thenReturn(Map.of());

		final ApiOperationException aoe =
				assertThrows(ApiOperationException.class, () -> HtmlContextValidierung.pruefungenGostLaufbahnplanung(reportingContext, ids));

		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals("FEHLER: Es wurden Schüler-IDs übergeben, für die keine Abiturdaten in der GOSt-Laufbahnplanung existieren.", aoe.getBody());
	}


	// ##### GOSt-Abitur #####

	@Test
	void testDieAbiturpruefungBestehtBeiVollstaendigenDaten() {
		gebeSchuleVor(true);
		when(gebeRepositoryGostVor().schuelerAbiturdaten(List.of(1L))).thenReturn(Map.of(1L, new Abiturdaten()));

		assertDoesNotThrow(() -> HtmlContextValidierung.pruefungenGostAbitur(reportingContext, List.of(1L)));
		assertEquals(List.of(), fehlermeldungenImLog());
	}

	@Test
	void testDieAbiturpruefungBrichtOhneGostVorDenAbiturdatenAb() {
		gebeSchuleVor(false);
		final List<Long> ids = List.of(1L);

		final ApiOperationException aoe =
				assertThrows(ApiOperationException.class, () -> HtmlContextValidierung.pruefungenGostAbitur(reportingContext, ids));

		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals(MELDUNG_KEINE_GOST, aoe.getBody());
		verify(reportingContext, never()).repositoryGost();
	}

	@Test
	void testFehlendeAbiturdatenWerdenGemeldet() {
		gebeSchuleVor(true);
		final List<Long> ids = List.of(1L, 2L);
		when(gebeRepositoryGostVor().schuelerAbiturdaten(ids)).thenReturn(Map.of(1L, new Abiturdaten()));

		final ApiOperationException aoe =
				assertThrows(ApiOperationException.class, () -> HtmlContextValidierung.pruefungenGostAbitur(reportingContext, ids));

		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals("FEHLER: Es wurden Schüler-IDs übergeben, für die keine Abiturdaten in der GOSt existieren.", aoe.getBody());
	}

}
