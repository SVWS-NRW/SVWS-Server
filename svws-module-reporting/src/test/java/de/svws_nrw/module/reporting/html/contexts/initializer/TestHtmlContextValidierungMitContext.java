package de.svws_nrw.module.reporting.html.contexts.initializer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.data.gost.GostLaufbahnplanungBeratungsdaten;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.diagnose.ReportingAuswahlergebnis;
import de.svws_nrw.module.reporting.diagnose.ReportingLadezustand;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemursache;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.repositories.ReportingRepositoryGost;
import de.svws_nrw.module.reporting.repositories.ReportingRepositorySchule;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests der Prüfungen aus {@link HtmlContextValidierung}, die auf die Domänen-Repositories zugreifen.
 * <p>Der {@link ReportingContext} wird gemockt, weil sein einziger Konstruktor alle neun Repositories aufbaut und dafür eine Datenbankverbindung
 * benötigt. Der Mock umgeht den Konstruktor; je Test werden nur der Logger und die tatsächlich benötigten Repository-Getter verdrahtet.</p>
 * <p>Der Schwerpunkt liegt auf den <b>Fehlerpfaden</b>: Sie legen fest, mit welchem Status ein Abbruch die Prüfung verlässt, und bilden die Grundlage für
 * die Statustests an Repository und Factory.</p>
 */
class TestHtmlContextValidierungMitContext {

	/** Die Meldung, die eine fehlende gymnasiale Oberstufe anzeigt. */
	private static final String MELDUNG_KEINE_GOST = "### FEHLER: Der Report ist nur für Schulen mit gymnasialer Oberstufe (GOSt) vorgesehen.";

	/** Die Meldung, die die Parameterprüfung bei einem nicht auswertbaren Wert erzeugt. */
	private static final String MELDUNG_PARAMETER_UNLESBAR =
			"### FEHLER: Die Angaben zu Abiturjahrgang und GOSt-Halbjahr konnten nicht ausgewertet werden.";

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
	 * Gibt die Texte aller Logeinträge mit dem Level ERROR zurück, ohne die Einrückung des Loggers.
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


	// ##### validiereAbiturjahrgangAlsHauptressource #####

	@Test
	void testOhneHauptdatenIdsWirdDieParameterpruefungAbgewiesen() {
		gebeHauptdatenIdsVor(List.of());

		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereAbiturjahrgangAlsHauptressource(reportingContext));

		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals("### FEHLER: Es wurden kein Abiturjahrgang und keine GOSt-Halbjahre übergeben.", aoe.getBody());
		verify(reportingContext, never()).repositoryGost();
	}


	@Test
	void testGueltigeEinzelneParameterWerdenAkzeptiert() {
		gebeHauptdatenIdsVor(List.of(2025L, 3L));
		when(gebeRepositoryGostVor().abiturjahrgaenge()).thenReturn(List.of(2025));

		assertDoesNotThrow(() -> HtmlContextValidierung.validiereAbiturjahrgangAlsHauptressource(reportingContext));
	}


	@Test
	void testEinNullEintragInDenEinzelnenParameternFuehrtNichtZumServerfehler() {
		// Der allgemeine catch-Zweig fängt die NullPointerException eines null-Eintrags ab, sodass am API-Rand ein Client-Fehler und kein
		// Serverfehler ankommt.
		gebeHauptdatenIdsVor(Arrays.asList(2025L, null));
		when(gebeRepositoryGostVor().abiturjahrgaenge()).thenReturn(List.of(2025));

		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereAbiturjahrgangAlsHauptressource(reportingContext));

		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals(MELDUNG_PARAMETER_UNLESBAR, aoe.getBody());
	}

	@Test
	void testEinNichtVorhandenerAbiturjahrgangDerHauptressourceErgibtNotFound() {
		// Der Abiturjahrgang ist die adressierte Hauptressource des Reports: Ein formal gültiges, aber nicht vorhandenes Jahr ist die Auskunft
		// "gibt es nicht" und kein Parameterfehler.
		gebeHauptdatenIdsVor(List.of(2026L));
		when(gebeRepositoryGostVor().abiturjahrgaenge()).thenReturn(List.of(2025));

		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereAbiturjahrgangAlsHauptressource(reportingContext));

		assertEquals(Status.NOT_FOUND, aoe.getStatus());
		assertEquals("### FEHLER: Der Abiturjahrgang '2026' ist an dieser Schule nicht vorhanden.", aoe.getBody());
		assertEquals(List.of(), fehlermeldungenImLog(), "Die Meldung nennt den Wert bereits; eine Zusatzzeile wiederholte ihn nur.");
	}

	@Test
	void testEinFehlerBeimLadenDerAbiturjahrgaengeBleibtEinServerfehler() {
		// Das Repository wirft den Ladefehler statustragend mit 500. Die Prüfung reicht ihn unverändert durch, statt ihn als unlesbaren Parameter
		// mit 400 auszugeben - der Aufrufer kann an seinen Parametern nichts korrigieren.
		gebeHauptdatenIdsVor(List.of(2025L));
		final ApiOperationException ladefehler = new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
				new IllegalStateException("Datenbank nicht erreichbar"), "### FEHLER: Die vorhandenen Abiturjahrgänge konnten nicht ermittelt werden.");
		when(gebeRepositoryGostVor().abiturjahrgaenge()).thenThrow(ladefehler);

		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereAbiturjahrgangAlsHauptressource(reportingContext));

		assertSame(ladefehler, aoe, "Der statustragende Wurf des Repositorys erreicht den Aufrufer unverändert.");
		assertEquals(List.of(), fehlermeldungenImLog(), "Das Laden scheitert vor der Parameterprüfung; die Parameter sind hier nicht der Befund.");
	}

	@Test
	void testEinUngueltigesHalbjahrHaeltDenBeanstandetenWertImLogFest() {
		// Die Meldung nennt den beanstandeten Wert nicht, und das Eingangsprotokoll zeigt nur einen Auszug der Rohwerte. Ohne diese Zeile bliebe
		// unauffindbar, welcher Wert die Ausgabe beendet hat.
		gebeHauptdatenIdsVor(List.of(2025L, 0L, 99L));
		when(gebeRepositoryGostVor().abiturjahrgaenge()).thenReturn(List.of(2025));

		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereAbiturjahrgangAlsHauptressource(reportingContext));

		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals("### FEHLER: Ein angegebenes GOSt-Halbjahr ist ungültig.", aoe.getBody());
		assertEquals(List.of("Beanstandeter Parameter: 99"), fehlermeldungenImLog(),
				"Die Zeile nennt allein den beanstandeten Wert - nicht die Liste und nicht die Meldung.");
	}

	@Test
	void testEinWertJenseitsDesZahlenbereichsHaeltDenBeanstandetenWertImLogFest() {
		// Math.toIntExact wirft hier eine ArithmeticException, deren Meldung allein "integer overflow" lautet. Weder sie noch die Sammelmeldung
		// nennen den Wert.
		gebeHauptdatenIdsVor(List.of(2025L, 2147483648L));
		when(gebeRepositoryGostVor().abiturjahrgaenge()).thenReturn(List.of(2025));

		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereAbiturjahrgangAlsHauptressource(reportingContext));

		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals(MELDUNG_PARAMETER_UNLESBAR, aoe.getBody());
		assertEquals(List.of("Beanstandeter Parameter: 2147483648"), fehlermeldungenImLog());
	}

	@Test
	void testEinAbiturjahrAusserhalbDesWertebereichsErzeugtKeinenZusatzeintrag() {
		// Die Gegenprobe zum nicht vorhandenen Jahrgang: Auch diese Meldung trägt den Wert selbst.
		gebeHauptdatenIdsVor(List.of(202L));
		when(gebeRepositoryGostVor().abiturjahrgaenge()).thenReturn(List.of(2025));

		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereAbiturjahrgangAlsHauptressource(reportingContext));

		assertEquals("### FEHLER: Das Abiturjahr '202' liegt außerhalb des zulässigen Bereichs.", aoe.getBody());
		assertEquals(List.of(), fehlermeldungenImLog());
	}

	@Test
	void testEinUebergelaufenesAbiturjahrHaeltDenWertTrotzdemImLogFest() {
		// Beim Überlauf erreicht der Wert die Prüfung nicht, die Meldung kann ihn also nicht nennen - hier braucht auch der erste Parameter die Zeile.
		gebeHauptdatenIdsVor(List.of(2147483648L));
		when(gebeRepositoryGostVor().abiturjahrgaenge()).thenReturn(List.of(2025));

		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereAbiturjahrgangAlsHauptressource(reportingContext));

		assertEquals(MELDUNG_PARAMETER_UNLESBAR, aoe.getBody());
		assertEquals(List.of("Beanstandeter Parameter: 2147483648"), fehlermeldungenImLog());
	}

	@Test
	void testDasLogBleibtAuchBeiEinerLangenParameterlisteKurz() {
		// Die Zeile darf die Kürzung des Eingangsprotokolls nicht unterlaufen: Eine Anfrage mit vielen IDs ergibt genau einen Eintrag mit einem Wert.
		final List<Long> vieleIds = new ArrayList<>(List.of(2025L));
		for (long halbjahr = 0; halbjahr < 200; halbjahr++) {
			vieleIds.add(halbjahr % 6);
		}
		vieleIds.add(99L);
		gebeHauptdatenIdsVor(vieleIds);
		when(gebeRepositoryGostVor().abiturjahrgaenge()).thenReturn(List.of(2025));

		assertThrows(ApiOperationException.class, () -> HtmlContextValidierung.validiereAbiturjahrgangAlsHauptressource(reportingContext));

		assertEquals(List.of("Beanstandeter Parameter: 99"), fehlermeldungenImLog());
	}

	@Test
	void testEineGelungenePruefungHinterlaesstKeinenFehlereintrag() {
		gebeHauptdatenIdsVor(List.of(2025L, 0L, 1L));
		when(gebeRepositoryGostVor().abiturjahrgaenge()).thenReturn(List.of(2025));

		assertDoesNotThrow(() -> HtmlContextValidierung.validiereAbiturjahrgangAlsHauptressource(reportingContext));

		assertEquals(List.of(), fehlermeldungenImLog());
	}

	// ##### validiereSchuleMitGost #####

	@Test
	void testEineSchuleMitGostBestehtDiePruefung() {
		gebeSchuleVor(true);

		assertDoesNotThrow(() -> HtmlContextValidierung.validiereSchuleMitGost(reportingContext));
	}

	@Test
	void testEineSchuleOhneGostWirdAbgewiesen() {
		gebeSchuleVor(false);

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> HtmlContextValidierung.validiereSchuleMitGost(reportingContext));

		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals(MELDUNG_KEINE_GOST, aoe.getBody());
	}


	// ##### GOSt-Laufbahnplanung #####

	/**
	 * Erzeugt eine Auswahl, in der zu jeder übergebenen ID ein Schüler steht.
	 *
	 * @param ids Die IDs der ausgewählten Schüler.
	 *
	 * @return Die Auswahl ohne ausgelassene und ohne ausgefilterte Datensätze.
	 */
	private static ReportingAuswahlergebnis<ReportingSchueler> auswahlMit(final List<Long> ids) {
		final Map<Long, ReportingSchueler> objekte = new LinkedHashMap<>();
		for (final Long id : ids) {
			objekte.put(id, schuelerMitId(id));
		}
		return ReportingAuswahlergebnis.aus(ids, objekte, Map.of(), List.of());
	}


	/**
	 * Bildet die übergebenen IDs auf den Zustand "geladen" ab.
	 *
	 * @param <V>  Der Typ des geladenen Wertes.
	 * @param ids  Die IDs, zu denen Daten vorliegen.
	 * @param wert Der Wert, der für jede ID als geladen gilt.
	 *
	 * @return Die Zustände je ID.
	 */
	private static <V> Map<Long, ReportingLadezustand<V>> zustaendeGeladen(final List<Long> ids, final V wert) {
		final Map<Long, ReportingLadezustand<V>> zustaende = new LinkedHashMap<>();
		for (final Long id : ids) {
			zustaende.put(id, ReportingLadezustand.geladen(wert));
		}
		return zustaende;
	}


	@Test
	void testDieLaufbahnplanungPruefungLaesstVollstaendigeDatenUnveraendert() {
		gebeSchuleVor(true);
		final ReportingRepositoryGost repositoryGost = gebeRepositoryGostVor();
		when(repositoryGost.zustaendeBeratungsdaten(List.of(1L))).thenReturn(zustaendeGeladen(List.of(1L), new GostLaufbahnplanungBeratungsdaten()));
		when(repositoryGost.zustaendeBeratungsdatenAbiturdaten(List.of(1L))).thenReturn(zustaendeGeladen(List.of(1L), new Abiturdaten()));

		final ReportingAuswahlergebnis<ReportingSchueler> auswahl =
				assertDoesNotThrow(() -> HtmlContextValidierung.pruefungenGostLaufbahnplanung(reportingContext, auswahlMit(List.of(1L))));

		assertEquals(List.of(1L), auswahl.idsAusgewaehlt());
	}

	@Test
	void testDieLaufbahnplanungPruefungBrichtOhneGostVorDenBeratungsdatenAb() {
		// Eine Schule ohne gymnasiale Oberstufe ist eine verletzte Voraussetzung des gesamten Reports und kein Befund an einem einzelnen Datensatz.
		gebeSchuleVor(false);
		final ReportingAuswahlergebnis<ReportingSchueler> auswahl = auswahlMit(List.of(1L));

		final ApiOperationException aoe =
				assertThrows(ApiOperationException.class, () -> HtmlContextValidierung.pruefungenGostLaufbahnplanung(reportingContext, auswahl));

		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals(MELDUNG_KEINE_GOST, aoe.getBody());
		verify(reportingContext, never()).repositoryGost();
	}

	@Test
	void testEinSchuelerOhneBeratungsdatenWirdAusgelassen() {
		gebeSchuleVor(true);
		final List<Long> ids = List.of(1L);
		final ReportingRepositoryGost repositoryGost = gebeRepositoryGostVor();
		when(repositoryGost.zustaendeBeratungsdaten(ids)).thenReturn(Map.of(1L, ReportingLadezustand.nichtVorhanden()));

		final ReportingAuswahlergebnis<ReportingSchueler> auswahl =
				assertDoesNotThrow(() -> HtmlContextValidierung.pruefungenGostLaufbahnplanung(reportingContext, auswahlMit(ids)));

		assertEquals(List.of(), auswahl.idsAusgewaehlt(), "Ein Schüler ohne Beratungsdaten gehört nicht zur GOSt und entfällt.");
		assertEquals(Set.of(1L), auswahl.ausgelassen().keySet());
		assertEquals(ReportingProblemursache.NICHT_VORHANDEN, auswahl.ausgelassen().get(1L).ursache(),
				"Ein ausgelassener Datensatz bricht die Ausgabe nicht ab; gemeldet wird er erst beim Prüfen der Auswahl.");
	}

	@Test
	void testEinGescheiterterZugriffAufBeratungsdatenBleibtEinLadefehler() {
		// Eine fehlende GOSt-Akte und ein gescheiterter Datenbankzugriff lassen den Schüler beide entfallen. Würde die Störung als "nicht vorhanden"
		// ausgewiesen, liefe sie unbemerkt als gewöhnliche Auslassung durch.
		gebeSchuleVor(true);
		final List<Long> ids = List.of(1L);
		final IllegalStateException fehler = new IllegalStateException("Der Dienst antwortet nicht.");
		final ReportingRepositoryGost repositoryGost = gebeRepositoryGostVor();
		when(repositoryGost.zustaendeBeratungsdaten(ids))
				.thenReturn(Map.of(1L, ReportingLadezustand.fehlgeschlagen(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER, fehler)));

		final ReportingAuswahlergebnis<ReportingSchueler> auswahl =
				assertDoesNotThrow(() -> HtmlContextValidierung.pruefungenGostLaufbahnplanung(reportingContext, auswahlMit(ids)));

		assertEquals(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER, auswahl.ausgelassen().get(1L).ursache());
		assertEquals(fehler, auswahl.ausgelassen().get(1L).fehler(), "Der auslösende Fehler gehört in die spätere Meldung.");
	}

	@Test
	void testEinSchuelerOhneAbiturdatenDerLaufbahnplanungWirdAusgelassen() {
		gebeSchuleVor(true);
		final List<Long> ids = List.of(1L, 2L);
		final ReportingRepositoryGost repositoryGost = gebeRepositoryGostVor();
		when(repositoryGost.zustaendeBeratungsdaten(ids)).thenReturn(zustaendeGeladen(ids, new GostLaufbahnplanungBeratungsdaten()));
		when(repositoryGost.zustaendeBeratungsdatenAbiturdaten(ids)).thenReturn(Map.of(1L, ReportingLadezustand.geladen(new Abiturdaten()),
				2L, ReportingLadezustand.nichtVorhanden()));

		final ReportingAuswahlergebnis<ReportingSchueler> auswahl =
				assertDoesNotThrow(() -> HtmlContextValidierung.pruefungenGostLaufbahnplanung(reportingContext, auswahlMit(ids)));

		assertEquals(List.of(1L), auswahl.idsAusgewaehlt(), "Der Schüler mit Abiturdaten bleibt in der Ausgabe.");
		assertEquals(Set.of(2L), auswahl.ausgelassen().keySet());
	}

	@Test
	void testDieZweitePruefungFragtNurNochDieVerbliebenenSchuelerAb() {
		// Ein Schüler, der mangels Beratungsdaten bereits entfallen ist, wird nicht erneut geladen - sonst entstünde für ihn ein zweiter Befund.
		gebeSchuleVor(true);
		final List<Long> ids = List.of(1L, 2L);
		final ReportingRepositoryGost repositoryGost = gebeRepositoryGostVor();
		when(repositoryGost.zustaendeBeratungsdaten(ids)).thenReturn(Map.of(1L, ReportingLadezustand.geladen(new GostLaufbahnplanungBeratungsdaten()),
				2L, ReportingLadezustand.nichtVorhanden()));
		when(repositoryGost.zustaendeBeratungsdatenAbiturdaten(List.of(1L))).thenReturn(zustaendeGeladen(List.of(1L), new Abiturdaten()));

		final ReportingAuswahlergebnis<ReportingSchueler> auswahl =
				assertDoesNotThrow(() -> HtmlContextValidierung.pruefungenGostLaufbahnplanung(reportingContext, auswahlMit(ids)));

		assertEquals(List.of(1L), auswahl.idsAusgewaehlt());
		verify(repositoryGost).zustaendeBeratungsdatenAbiturdaten(List.of(1L));
		verify(repositoryGost, never()).zustaendeBeratungsdatenAbiturdaten(ids);
	}


	// ##### GOSt-Abitur #####

	@Test
	void testDieAbiturpruefungLaesstVollstaendigeDatenUnveraendert() {
		gebeSchuleVor(true);
		when(gebeRepositoryGostVor().zustaendeSchuelerAbiturdaten(List.of(1L))).thenReturn(zustaendeGeladen(List.of(1L), new Abiturdaten()));

		final ReportingAuswahlergebnis<ReportingSchueler> auswahl =
				assertDoesNotThrow(() -> HtmlContextValidierung.pruefungenGostAbitur(reportingContext, auswahlMit(List.of(1L))));

		assertEquals(List.of(1L), auswahl.idsAusgewaehlt());
	}

	@Test
	void testDieAbiturpruefungBrichtOhneGostVorDenAbiturdatenAb() {
		gebeSchuleVor(false);
		final ReportingAuswahlergebnis<ReportingSchueler> auswahl = auswahlMit(List.of(1L));

		final ApiOperationException aoe =
				assertThrows(ApiOperationException.class, () -> HtmlContextValidierung.pruefungenGostAbitur(reportingContext, auswahl));

		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals(MELDUNG_KEINE_GOST, aoe.getBody());
		verify(reportingContext, never()).repositoryGost();
	}

	@Test
	void testEinSchuelerOhneAbiturdatenWirdAusgelassen() {
		gebeSchuleVor(true);
		final List<Long> ids = List.of(1L, 2L);
		when(gebeRepositoryGostVor().zustaendeSchuelerAbiturdaten(ids))
				.thenReturn(Map.of(1L, ReportingLadezustand.geladen(new Abiturdaten()), 2L, ReportingLadezustand.nichtVorhanden()));

		final ReportingAuswahlergebnis<ReportingSchueler> auswahl =
				assertDoesNotThrow(() -> HtmlContextValidierung.pruefungenGostAbitur(reportingContext, auswahlMit(ids)));

		assertEquals(List.of(1L), auswahl.idsAusgewaehlt(), "Eine einzelne fehlende Abiturakte lässt die übrige Ausgabe nicht scheitern.");
		assertEquals(Set.of(2L), auswahl.ausgelassen().keySet());
	}

}
