package de.svws_nrw.module.reporting.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.berufskolleg.DTOSchuelerZuweisung;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemSchluessel;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemauswirkung;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemursache;
import de.svws_nrw.module.reporting.signing.SchulbescheinigungQrDaten;
import de.svws_nrw.module.reporting.types.schueler.erzieher.ReportingErzieher;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLernabschnitt;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerZuweisung;
import de.svws_nrw.module.reporting.types.schueler.sprachen.ReportingSchuelerSprachbelegung;
import de.svws_nrw.module.reporting.types.schueler.telefon.ReportingSchuelerTelefonkontakt;
import de.svws_nrw.module.reporting.types.schule.ReportingSchule;

/**
 * Tests der Teildaten-Zugriffe in {@link ReportingRepositorySchueler}: Scheitert das Laden von Erzieher-, Sprach-, Lernabschnitts- oder Telefondaten, wird der
 * betroffene Schüler <b>nicht</b> ausgelassen und die Ausgabe nicht beendet - es fehlen allein diese Teildaten, und genau das wird als Ausgabeproblem gemeldet.
 * <p>Der {@link ReportingContext} wird gemockt und liefert keine Datenbankverbindung. Jeder Ladevorgang scheitert dadurch bereits am Datenzugriff. Dieser
 * Fehler steht stellvertretend für jeden Zugriffsfehler, dessen Ursache sich an der Meldestelle nicht zuverlässig bestimmen lässt - solche Fehler werden bei
 * Teildaten als datensatzbezogen hingenommen. Ob eine Ursache eine Infrastrukturstörung ist und die Ausgabe beendet, entscheidet eine Meldestelle nicht
 * selbst; diese Klassifikation ist Aufgabe der zentralen Diagnose und nicht Gegenstand dieser Tests.</p>
 * <p>Hingenommen wird dabei <b>nur der Datenzugriff selbst</b>: Ein Fehler der anschließenden Verarbeitung ist ein Programmierfehler, wird nicht gemeldet und
 * beendet die Ausgabe - auch das ist hier abgedeckt. Umgekehrt zeigt der Weg über die Schulbescheinigung, dass ein aus der Erzeugung propagierter Fehler nicht
 * verloren geht: Der Lade-Fallback des Caches hält ihn fest und meldet ihn genau einmal. Der Cache der Stammdaten wird über
 * {@link ReportingRepositorySchueler#registriereStammdaten} vorbelegt, weil die Teildaten für die bereits bekannten Schüler nachgeladen werden.</p>
 * <p>Geprüft wird die Meldung und <b>nicht</b> die Deduplizierung: Die leistet der Problemsammler, den der gemockte Context nicht besitzt. Sein Verhalten ist in
 * {@code TestReportingProblemSammler} abgedeckt.</p>
 */
class TestReportingRepositorySchueler {

	/** Die ID des Schülers, dessen Teildaten in den Tests geladen werden. */
	private static final long ID_SCHUELER = 42L;

	/** Der gemockte Context, den das Repository erhält. */
	private ReportingContext reportingContext;

	/** Das Repository unter Test. */
	private ReportingRepositorySchueler repository;

	/** Die Liste, die die Einträge des Loggers sammelt. */
	private LogConsumerList log;


	@BeforeEach
	void setUp() {
		reportingContext = mock(ReportingContext.class);
		final Logger logger = new Logger();
		log = new LogConsumerList();
		logger.addConsumer(log);
		when(reportingContext.logger()).thenReturn(logger);

		repository = new ReportingRepositorySchueler(reportingContext);
		final SchuelerStammdaten stammdaten = new SchuelerStammdaten();
		stammdaten.id = ID_SCHUELER;
		repository.registriereStammdaten(ID_SCHUELER, stammdaten);
	}


	/**
	 * Prüft, dass genau ein hingenommenes Ausgabeproblem mit der erwarteten Objektart und ID gemeldet wurde.
	 *
	 * @param objektart Die erwartete Objektart der ausgelassenen Teildaten.
	 * @param id        Die erwartete ID des Datensatzes, zu dem die Teildaten gehören.
	 */
	private void erwarteTeildatenProblem(final Class<?> objektart, final long id) {
		verify(reportingContext, times(1)).meldeAusgabeproblem(eq(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN), eq(ReportingProblemSchluessel.fuer(objektart, id)), anyString(), any());
	}

	/**
	 * Gibt an, ob das Log einen Eintrag mit dem Level ERROR enthält.
	 *
	 * @return true, wenn ein ERROR-Eintrag vorliegt, sonst false.
	 */
	private boolean logEnthaeltFehler() {
		return log.getLogData().stream().anyMatch(eintrag -> eintrag.getLevel() == LogLevel.ERROR);
	}


	@Test
	void testNichtLadbareErzieherdatenWerdenAlsAusgabeproblemGemeldet() {
		final List<?> erzieher = repository.erzieherStammdaten(ID_SCHUELER);

		assertTrue(erzieher.isEmpty(), "Der Schüler bleibt in der Ausgabe, ihm fehlen allein die Erzieherdaten.");
		erwarteTeildatenProblem(ReportingErzieher.class, ID_SCHUELER);
	}

	@Test
	void testNichtLadbareSprachbelegungenWerdenAlsAusgabeproblemGemeldet() {
		assertTrue(repository.sprachbelegungen(ID_SCHUELER).isEmpty());

		erwarteTeildatenProblem(ReportingSchuelerSprachbelegung.class, ID_SCHUELER);
	}

	@Test
	void testNichtLadbareTelefonkontakteWerdenAlsAusgabeproblemGemeldet() {
		assertTrue(repository.telefonkontakte(ID_SCHUELER).isEmpty());

		erwarteTeildatenProblem(ReportingSchuelerTelefonkontakt.class, ID_SCHUELER);
	}

	@Test
	void testNichtLadbareLernabschnitteWerdenAlsAusgabeproblemGemeldet() {
		// Der Zugriff scheitert hier nicht an einer ApiOperationException. Würde nur diese gefangen, beendete der Fehler die Ausgabe, statt die Lernabschnitte
		// dieses Schülers auszulassen.
		assertTrue(repository.lernabschnitte(ID_SCHUELER).isEmpty());

		erwarteTeildatenProblem(ReportingSchuelerLernabschnitt.class, ID_SCHUELER);
	}

	@Test
	void testNichtLadbareZuweisungenWerdenUnterDerIdDesLernabschnittsGemeldet() {
		final long idLernabschnitt = 7L;

		assertTrue(repository.zuweisungen(idLernabschnitt, null).isEmpty());

		erwarteTeildatenProblem(ReportingSchuelerZuweisung.class, idLernabschnitt);
	}

	@Test
	void testEinFehlerDerVerarbeitungNachDemDatenzugriffWirdNichtHingenommen() {
		// Der Datenzugriff gelingt; erst der Aufbau der Reporting-Objekte scheitert, weil der Lernabschnitt fehlt. Das ist ein Programmierfehler und kein
		// Datenfehler: Er wird weder gemeldet noch hingenommen, sondern beendet die Ausgabe. Hingenommen würde er nur, wenn der try-Block über den
		// Datenzugriff hinausreichte.
		final long idLernabschnitt = 7L;
		final DBEntityManager conn = mock(DBEntityManager.class);
		when(reportingContext.conn()).thenReturn(conn);
		when(conn.queryList(DTOSchuelerZuweisung.QUERY_BY_ABSCHNITT_ID, DTOSchuelerZuweisung.class, idLernabschnitt))
				.thenReturn(List.of(new DTOSchuelerZuweisung(idLernabschnitt, 4711L)));

		assertThrows(NullPointerException.class, () -> repository.zuweisungen(idLernabschnitt, null));

		verify(reportingContext, never()).meldeAusgabeproblem(any(), any(), any(), anyString(), any());
	}

	@Test
	void testEinInkonsistenterZustandBeimQrAufbauWirdGenauEinmalGemeldet() {
		// Der volle Weg vom inkonsistenten Zustand bis zur Meldung: Der Zugriff auf die Schule gelingt, die Ableitung der Ausstellungsdaten scheitert am
		// fehlenden aktuellen Schuljahresabschnitt (Mockito-Default null). Die Factory nimmt das nicht hin, sondern lässt es propagieren; der Lade-Fallback
		// hält den Fehler je Schüler fest, und daraus entsteht genau ein Ausgabeproblem samt Exception. Ohne diese Kette bliebe eine unsignierte
		// Bescheinigung ohne gezähltes Problem - der Hinweisvertrag meldete gesamt=0.
		final ReportingRepositorySchule repositorySchule = mock(ReportingRepositorySchule.class);
		when(repositorySchule.schule()).thenReturn(mock(ReportingSchule.class));
		when(reportingContext.repositorySchule()).thenReturn(repositorySchule);

		final SchulbescheinigungQrDaten qrDaten = repository.schulbescheinigungQrDaten(ID_SCHUELER);

		assertNotNull(qrDaten, "Auch nach einem Fehler der Erzeugung erhält die Vorlage einen Eintrag statt null.");
		assertNotNull(qrDaten.fehlermeldung());
		verify(reportingContext, times(1)).meldeAusgabeproblem(eq(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN), eq(ReportingProblemSchluessel.fuer(SchulbescheinigungQrDaten.class, ID_SCHUELER)),
				anyString(), any(NullPointerException.class));
		// Genau einmal heißt: Die Factory meldet denselben Sachverhalt nicht zusätzlich als nicht darstellbaren Wert.
		verify(reportingContext, never()).meldeAusgabeproblem(eq(ReportingProblemursache.NICHT_DARSTELLBAR), any(), any(), anyString(), any());
	}

	@Test
	void testEinAusgelassenesTeildatumHinterlaesstKeinenFehlerImLog() {
		// ERROR ist dem Abbruch vorbehalten. Wer Teildaten auslässt und weiterläuft, darf keinen solchen Eintrag erzeugen.
		repository.erzieherStammdaten(ID_SCHUELER);
		repository.sprachbelegungen(ID_SCHUELER);
		repository.telefonkontakte(ID_SCHUELER);
		repository.lernabschnitte(ID_SCHUELER);
		repository.zuweisungen(7L, null);

		assertEquals(false, logEnthaeltFehler(), "Ein hingenommenes Ausgabeproblem hinterlässt höchstens eine Warnung.");
	}

	@Test
	void testDerFehlerDesGesammeltenZugriffsBleibtImLog() {
		// Er ist kein Ausgabeproblem, solange die Einzelzugriffe gelingen, und bleibt damit die Spur im Log.
		repository.erzieherStammdaten(ID_SCHUELER);

		assertTrue(log.getLogData().stream().anyMatch(eintrag -> eintrag.getText().contains("Fehler beim gesammelten Laden")));
	}

}
