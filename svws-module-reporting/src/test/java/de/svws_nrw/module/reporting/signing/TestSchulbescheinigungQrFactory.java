package de.svws_nrw.module.reporting.signing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemSchluessel;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemauswirkung;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemursache;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.repositories.ReportingRepositorySchueler;
import de.svws_nrw.module.reporting.repositories.ReportingRepositorySchule;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schule.ReportingSchule;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;
import de.svws_nrw.service.signature.Signature;
import de.svws_nrw.service.signature.SignatureService;
import de.svws_nrw.service.signature.SignatureStatus;

/**
 * Tests der Meldegrenze in der {@link SchulbescheinigungQrFactory}: Scheitert die Signatur trotz geladener Ausgangsdaten, entsteht je Schüler genau ein
 * Ausgabeproblem; fehlen dagegen die Ausgangsdaten, meldet die Factory nichts - das gehört der Datenzugriffsstelle.
 * <p>Der Signierfehler wird über den injizierbaren {@link SignatureService} erzeugt: Ausstellungsdaten und XML entstehen regulär aus gemockten Ausgangsdaten,
 * erst der Signierdienst antwortet mit einem Fehlerstatus. Damit prüft der Test den Erzeugungsfehler selbst und nicht - wie ein abgerissener
 * Datenbankzugriff - einen vorgelagerten Ladefehler.</p>
 */
class TestSchulbescheinigungQrFactory {

	/** Die ID des Schülers, dessen Bescheinigung in den Tests erzeugt wird. */
	private static final long ID_SCHUELER = 42L;

	/** Eine feste Uhr, damit das Ausstellungsdatum der Bescheinigung reproduzierbar ist. */
	private static final Clock UHR = Clock.fixed(Instant.parse("2026-08-11T08:00:00Z"), ZoneOffset.UTC);

	/** Der gemockte Context, den die Factory erhält. */
	private ReportingContext reportingContext;

	/** Die gemockte Schule, aus der die Ausstellungsdaten der Bescheinigung abgeleitet werden. */
	private ReportingSchule schule;

	/** Das gemockte Schüler-Repository, aus dem die Factory die Ausgangsdaten bezieht. */
	private ReportingRepositorySchueler repositorySchueler;


	@BeforeEach
	void setUp() {
		reportingContext = mock(ReportingContext.class);
		when(reportingContext.logger()).thenReturn(new Logger());

		final ReportingSchuljahresabschnitt abschnitt = mock(ReportingSchuljahresabschnitt.class);
		when(abschnitt.schuljahr()).thenReturn(2025);
		schule = mock(ReportingSchule.class);
		when(schule.aktuellerSchuljahresabschnitt()).thenReturn(abschnitt);
		when(schule.ort()).thenReturn("Musterstadt");
		when(schule.bezeichnungSchuleZeileEins()).thenReturn("Testschule");
		final ReportingRepositorySchule repositorySchule = mock(ReportingRepositorySchule.class);
		when(repositorySchule.schule()).thenReturn(schule);
		when(reportingContext.repositorySchule()).thenReturn(repositorySchule);

		final ReportingSchueler schueler = mock(ReportingSchueler.class);
		when(schueler.nachname()).thenReturn("Muster");
		when(schueler.vornamen()).thenReturn("Max");
		when(schueler.geburtsdatum()).thenReturn("2008-01-01");
		repositorySchueler = mock(ReportingRepositorySchueler.class);
		when(repositorySchueler.schueler(ID_SCHUELER)).thenReturn(schueler);
		when(reportingContext.repositorySchueler()).thenReturn(repositorySchueler);
	}


	/**
	 * Erzeugt einen Signierdienst, der jede Anfrage mit dem übergebenen Fehlertext ablehnt.
	 *
	 * @param fehlertext Der Fehlertext der abgelehnten Signatur.
	 *
	 * @return Der Signierdienst.
	 */
	private static SignatureService abweisenderSignierdienst(final String fehlertext) {
		return payloadById -> {
			final Map<Object, Signature> signaturen = new HashMap<>();
			for (final Object id : payloadById.keySet()) {
				signaturen.put(id, new Signature(null, SignatureStatus.ERROR, fehlertext));
			}
			return signaturen;
		};
	}


	@Test
	void testEinSignierfehlerTrotzGeladenerDatenMeldetGenauEinAusgabeproblem() {
		final SchulbescheinigungQrFactory factory =
				new SchulbescheinigungQrFactory(reportingContext, () -> abweisenderSignierdienst("Der Signierdienst hat die Anfrage abgelehnt."), UHR);

		final Map<Long, SchulbescheinigungQrDaten> ergebnis = factory.erzeuge(List.of(ID_SCHUELER));

		final SchulbescheinigungQrDaten qrDaten = ergebnis.get(ID_SCHUELER);
		assertNotNull(qrDaten.qr1Svg(), "Der Inhalt-QR-Code entsteht unabhängig von der Signatur.");
		assertNull(qrDaten.qr2Svg());
		assertEquals("Der Signierdienst hat die Anfrage abgelehnt.", qrDaten.fehlermeldung());
		verify(reportingContext, times(1)).meldeAusgabeproblem(eq(ReportingProblemursache.NICHT_DARSTELLBAR),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN), eq(ReportingProblemSchluessel.fuer(SchulbescheinigungQrDaten.class, ID_SCHUELER)),
				anyString(), isNull());
	}

	@Test
	void testEinNichtGeladenerSchuelerErzeugtKeinProblemDerQrErzeugung() {
		// Der Schüler ist nicht geladen - ausgefiltert oder nicht ladbar. Beides ist kein Erzeugungsfehler der Factory: Eine Auswahlentscheidung wird gar
		// nicht gemeldet, einen Ladefehler meldet die Datenzugriffsstelle.
		when(repositorySchueler.schueler(ID_SCHUELER)).thenReturn(null);
		final SchulbescheinigungQrFactory factory =
				new SchulbescheinigungQrFactory(reportingContext, () -> abweisenderSignierdienst("wird nicht erreicht"), UHR);

		final Map<Long, SchulbescheinigungQrDaten> ergebnis = factory.erzeuge(List.of(ID_SCHUELER));

		assertNotNull(ergebnis.get(ID_SCHUELER).fehlermeldung(), "Der Eintrag entsteht trotzdem, damit kein null in den Cache gelangt.");
		verify(reportingContext, never()).meldeAusgabeproblem(any(), any(), any(), anyString(), any());
	}

	@Test
	void testEinWerfenderSignierdienstReichtDieExceptionBisZurMeldung() {
		// Ohne die Exception stünde in der Diagnose nur noch ein Text. Ursachenkette und Stacktrace wären verloren, und die spätere zentrale Klassifikation
		// könnte den Fehler nicht mehr bewerten.
		final IllegalStateException fehler = new IllegalStateException("Der Signierdienst verweigert die Verbindung.");
		final SchulbescheinigungQrFactory factory = new SchulbescheinigungQrFactory(reportingContext, () -> payloadById -> {
			throw fehler;
		}, UHR);

		final Map<Long, SchulbescheinigungQrDaten> ergebnis = factory.erzeuge(List.of(ID_SCHUELER));

		assertNotNull(ergebnis.get(ID_SCHUELER).qr1Svg(), "Der Inhalt-QR-Code entsteht unabhängig von der Signatur.");
		assertNotNull(ergebnis.get(ID_SCHUELER).fehlermeldung());
		verify(reportingContext, times(1)).meldeAusgabeproblem(eq(ReportingProblemursache.NICHT_DARSTELLBAR),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN), eq(ReportingProblemSchluessel.fuer(SchulbescheinigungQrDaten.class, ID_SCHUELER)),
				anyString(), same(fehler));
	}

	@Test
	void testEinFehlerstatusOhneFehlermeldungWirdTrotzdemGemeldet() {
		// Der Dienst darf einen Status ungleich OK ohne eigenen Text liefern. Hinge der Befund am Vorhandensein der Fehlermeldung, entstünde hier eine
		// unsignierte Bescheinigung ohne gezähltes Problem - und die Vorlage zeigte an der Stelle des Codes nichts an.
		final SignatureService ohneFehlertext = payloadById -> {
			final Map<Object, Signature> signaturen = new HashMap<>();
			for (final Object id : payloadById.keySet()) {
				signaturen.put(id, new Signature(null, SignatureStatus.UNKNOWN, null));
			}
			return signaturen;
		};
		final SchulbescheinigungQrFactory factory = new SchulbescheinigungQrFactory(reportingContext, () -> ohneFehlertext, UHR);

		final Map<Long, SchulbescheinigungQrDaten> ergebnis = factory.erzeuge(List.of(ID_SCHUELER));

		assertNull(ergebnis.get(ID_SCHUELER).qr2Svg());
		assertEquals("Die Signierung wurde mit dem Status UNKNOWN ohne Fehlermeldung beendet.", ergebnis.get(ID_SCHUELER).fehlermeldung(),
				"Ohne Ersatztext bliebe die Bescheinigung ohne jeden Hinweis, warum ihr die Signatur fehlt.");
		verify(reportingContext, times(1)).meldeAusgabeproblem(eq(ReportingProblemursache.NICHT_DARSTELLBAR),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN), eq(ReportingProblemSchluessel.fuer(SchulbescheinigungQrDaten.class, ID_SCHUELER)),
				anyString(), isNull());
	}

	@Test
	void testEinFehlerBeimAufbauDerSchuldatenWirdNichtStillHingenommen() {
		// schule() lädt nichts nach - die Stammdaten stehen seit dem Konstruktor des Repositories. Ein Fehler hier ist deshalb kein Ladefehler, den eine
		// Zugriffsstelle melden würde, sondern ein Programmierfehler oder inkonsistenter Zustand. Er propagiert.
		final ReportingRepositorySchule repositorySchule = mock(ReportingRepositorySchule.class);
		when(repositorySchule.schule()).thenThrow(new IllegalStateException("Die Schuldaten sind nicht verwendbar."));
		when(reportingContext.repositorySchule()).thenReturn(repositorySchule);
		final SchulbescheinigungQrFactory factory =
				new SchulbescheinigungQrFactory(reportingContext, () -> abweisenderSignierdienst("wird nicht erreicht"), UHR);
		final List<Long> idsSchueler = List.of(ID_SCHUELER);

		assertThrows(IllegalStateException.class, () -> factory.erzeuge(idsSchueler));

		verify(reportingContext, never()).meldeAusgabeproblem(any(), any(), any(), anyString(), any());
	}

	@Test
	void testEinInkonsistenterZustandDerAusstellungsdatenWirdNichtStillHingenommen() {
		// Der Zugriff auf die Schule gelingt, erst die Ableitung scheitert am fehlenden aktuellen Schuljahresabschnitt. Das ist kein Ladefehler, den eine
		// Zugriffsstelle meldet - finge die Factory ihn, entstünde eine unsignierte Bescheinigung ohne ein einziges gezähltes Ausgabeproblem. Er propagiert
		// deshalb; der Lade-Fallback des Repositories hält ihn je Schüler samt Exception fest.
		when(schule.aktuellerSchuljahresabschnitt()).thenReturn(null);
		final SchulbescheinigungQrFactory factory =
				new SchulbescheinigungQrFactory(reportingContext, () -> abweisenderSignierdienst("wird nicht erreicht"), UHR);
		final List<Long> idsSchueler = List.of(ID_SCHUELER);

		assertThrows(NullPointerException.class, () -> factory.erzeuge(idsSchueler));

		verify(reportingContext, never()).meldeAusgabeproblem(any(), any(), any(), anyString(), any());
	}

}
