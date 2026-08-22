package de.svws_nrw.module.reporting.signing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
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
import de.svws_nrw.db.utils.ApiOperationException;
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
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests der Fehlermatrix in der {@link SchulbescheinigungQrFactory}: Ein Dienst- oder Anmeldefehler und ein Stapel ohne eine einzige erfolgreiche Signatur
 * brechen die gesamte Erstellung statustragend ab. Einzelne Fehlschläge sind tolerierbar: Die Bescheinigung erscheint mit dem passenden Fehlerzustand, und
 * je Schüler entsteht genau ein Ausgabeproblem.
 * <p>Der Signierfehler wird über den injizierbaren {@link SignatureService} erzeugt: Ausstellungsdaten und XML entstehen regulär aus gemockten Ausgangsdaten,
 * erst der Signierdienst antwortet mit einem Fehlerstatus. Damit prüft der Test den Erzeugungsfehler selbst und nicht - wie ein abgerissener
 * Datenbankzugriff - einen vorgelagerten Ladefehler.</p>
 */
class TestSchulbescheinigungQrFactory {

	/** Die ID des Schülers, dessen Bescheinigung in den Tests erzeugt wird. */
	private static final long ID_SCHUELER = 42L;

	/** Die ID des zweiten Schülers für Fälle, in denen ein Fehlschlag neben einem Erfolg steht. */
	private static final long ID_SCHUELER_ZWEI = 43L;

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
		final ReportingSchueler schuelerZwei = mock(ReportingSchueler.class);
		when(schuelerZwei.nachname()).thenReturn("Beispiel");
		when(schuelerZwei.vornamen()).thenReturn("Mia");
		when(schuelerZwei.geburtsdatum()).thenReturn("2008-02-02");
		repositorySchueler = mock(ReportingRepositorySchueler.class);
		when(repositorySchueler.schueler(ID_SCHUELER)).thenReturn(schueler);
		when(repositorySchueler.schueler(ID_SCHUELER_ZWEI)).thenReturn(schuelerZwei);
		when(reportingContext.repositorySchueler()).thenReturn(repositorySchueler);
	}

	/**
	 * Erzeugt einen Signierdienst, der für den ersten Schüler einen fallbezogenen Fehlerstatus und für den zweiten die übergebene Signatur liefert.
	 *
	 * @param signaturZweiterSchueler Die Signatur des zweiten Schülers.
	 *
	 * @return Der Signierdienst.
	 */
	private static SignatureService teilweiseErfolgreicherSignierdienst(final Signature signaturZweiterSchueler) {
		return payloadById -> {
			final Map<Object, Signature> signaturen = new HashMap<>();
			signaturen.put(ID_SCHUELER, new Signature(null, SignatureStatus.ERROR, "Fallbezogener Fehlerstatus des Signierdienstes."));
			signaturen.put(ID_SCHUELER_ZWEI, signaturZweiterSchueler);
			return signaturen;
		};
	}


	/**
	 * Erzeugt einen Signierdienst, der zu jedem Fall einen fallbezogenen Fehlerstatus mit dem übergebenen Fehlertext liefert.
	 *
	 * @param fehlertext Der Fehlertext des fallbezogenen Fehlerstatus.
	 *
	 * @return Der Signierdienst.
	 */
	private static SignatureService dienstMitFehlerstatusJeFall(final String fehlertext) {
		return payloadById -> {
			final Map<Object, Signature> signaturen = new HashMap<>();
			for (final Object id : payloadById.keySet()) {
				signaturen.put(id, new Signature(null, SignatureStatus.ERROR, fehlertext));
			}
			return signaturen;
		};
	}


	@Test
	void testEinFallbezogenerFehlerstatusWirdGemeldetUndDieUebrigenBescheinigungenErscheinen() {
		final Signature ok = new Signature("sig".getBytes(java.nio.charset.StandardCharsets.UTF_8), SignatureStatus.OK, null);
		final SchulbescheinigungQrFactory factory =
				new SchulbescheinigungQrFactory(reportingContext, () -> teilweiseErfolgreicherSignierdienst(ok), UHR);

		final Map<Long, SchulbescheinigungQrDaten> ergebnis = factory.erzeuge(List.of(ID_SCHUELER, ID_SCHUELER_ZWEI));

		final SchulbescheinigungQrDaten fehlerfall = ergebnis.get(ID_SCHUELER);
		assertNull(fehlerfall.qr1Svg(), "Im Fehlerfall entfallen beide Codes gemeinsam; ein halber Signaturblock sähe wie ein defektes Dokument aus.");
		assertNull(fehlerfall.qr2Svg());
		assertEquals(SchulbescheinigungSignaturzustand.SIGNIERFEHLER, fehlerfall.zustand());
		assertEquals(SchulbescheinigungSignaturzustand.SIGNIERT, ergebnis.get(ID_SCHUELER_ZWEI).zustand());
		assertNotNull(ergebnis.get(ID_SCHUELER_ZWEI).qr1Svg());
		assertNotNull(ergebnis.get(ID_SCHUELER_ZWEI).qr2Svg());
		verify(reportingContext, times(1)).meldeAusgabeproblem(eq(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN), eq(ReportingProblemSchluessel.fuer(SchulbescheinigungQrDaten.class, ID_SCHUELER)),
				anyString(), isNull());
	}

	@Test
	void testEinNichtRenderbarerSignaturQrCodeWirdAlsNichtDarstellbarGemeldet() {
		// Die Signatur selbst ist erfolgreich, aber ihr Inhalt sprengt die Kapazität eines QR-Codes: Nicht komprimierbare Zufallsbytes ergeben nach
		// Base45 mehr Zeichen, als die größte QR-Version aufnimmt. Erst das Rendern scheitert - der einzige Fall, der als nicht darstellbar gilt.
		final byte[] zuGross = new byte[8000];
		new java.util.Random(42L).nextBytes(zuGross);
		final Signature nichtRenderbar = new Signature(zuGross, SignatureStatus.OK, null);
		final Signature ok = new Signature("sig".getBytes(java.nio.charset.StandardCharsets.UTF_8), SignatureStatus.OK, null);
		final SignatureService dienst = payloadById -> {
			final Map<Object, Signature> signaturen = new HashMap<>();
			signaturen.put(ID_SCHUELER, nichtRenderbar);
			signaturen.put(ID_SCHUELER_ZWEI, ok);
			return signaturen;
		};
		final SchulbescheinigungQrFactory factory = new SchulbescheinigungQrFactory(reportingContext, () -> dienst, UHR);

		final Map<Long, SchulbescheinigungQrDaten> ergebnis = factory.erzeuge(List.of(ID_SCHUELER, ID_SCHUELER_ZWEI));

		assertEquals(SchulbescheinigungSignaturzustand.SIGNIERFEHLER, ergebnis.get(ID_SCHUELER).zustand(),
				"Ein nicht renderbarer Signatur-QR-Code führt zum Fehlerzustand, nicht zum Abbruch.");
		assertNull(ergebnis.get(ID_SCHUELER).qr1Svg(), "Im Fehlerfall entfallen beide Codes gemeinsam.");
		assertEquals(SchulbescheinigungSignaturzustand.SIGNIERT, ergebnis.get(ID_SCHUELER_ZWEI).zustand(),
				"Der Renderfehler bleibt auf den betroffenen Schüler beschränkt.");
		verify(reportingContext, times(1)).meldeAusgabeproblem(eq(ReportingProblemursache.NICHT_DARSTELLBAR),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN), eq(ReportingProblemSchluessel.fuer(SchulbescheinigungQrDaten.class, ID_SCHUELER)),
				anyString(), any(Exception.class));
	}

	@Test
	void testEinStapelOhneErfolgreicheSignaturBrichtDieErstellungAb() {
		// Kein einziges erfolgreiches Ergebnis im tatsächlich übergebenen Stapel ist ein Dienstproblem, keine Sammlung von Einzelfällen.
		final SchulbescheinigungQrFactory factory =
				new SchulbescheinigungQrFactory(reportingContext, () -> dienstMitFehlerstatusJeFall("Fallbezogener Fehlerstatus des Signierdienstes."), UHR);
		final List<Long> idsSchueler = List.of(ID_SCHUELER);

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> factory.erzeuge(idsSchueler));

		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
	}

	@Test
	void testEinFehlendesErgebnisInDerDienstantwortZaehltAlsFallbezogenerSignierfehler() {
		// Der Dienst liefert für einen Fall kein Ergebnis. Das wird wie ein fallbezogener Fehlerstatus behandelt: Fehlerbilder und Meldung für diesen
		// Fall, die übrigen Bescheinigungen erscheinen. Eine strengere Vollständigkeitsprüfung ist bewusst nicht vorgesehen.
		final Signature ok = new Signature("sig".getBytes(java.nio.charset.StandardCharsets.UTF_8), SignatureStatus.OK, null);
		final SignatureService ohneErgebnisFuerErsten = payloadById -> {
			final Map<Object, Signature> signaturen = new HashMap<>();
			signaturen.put(ID_SCHUELER_ZWEI, ok);
			return signaturen;
		};
		final SchulbescheinigungQrFactory factory = new SchulbescheinigungQrFactory(reportingContext, () -> ohneErgebnisFuerErsten, UHR);

		final Map<Long, SchulbescheinigungQrDaten> ergebnis = factory.erzeuge(List.of(ID_SCHUELER, ID_SCHUELER_ZWEI));

		assertEquals(SchulbescheinigungSignaturzustand.SIGNIERFEHLER, ergebnis.get(ID_SCHUELER).zustand());
		assertEquals(SchulbescheinigungSignaturzustand.SIGNIERT, ergebnis.get(ID_SCHUELER_ZWEI).zustand());
		verify(reportingContext, times(1)).meldeAusgabeproblem(eq(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN), eq(ReportingProblemSchluessel.fuer(SchulbescheinigungQrDaten.class, ID_SCHUELER)),
				anyString(), isNull());
	}

	@Test
	void testEineOkSignaturOhneInhaltZaehltAlsSignierfehler() {
		// Der Dienst darf OK ohne Inhalt liefern; ohne den Guard bräche das QR-Rendering an den leeren Bytes ab. Neben der leeren Antwort steht eine
		// verwertbare Signatur, damit der Stapel nicht als komplett gescheitert abbricht.
		final Signature okOhneInhalt = new Signature(new byte[0], SignatureStatus.OK, null);
		final Signature okMitInhalt = new Signature("sig".getBytes(java.nio.charset.StandardCharsets.UTF_8), SignatureStatus.OK, null);
		final SignatureService dienst = payloadById -> {
			final Map<Object, Signature> signaturen = new HashMap<>();
			signaturen.put(ID_SCHUELER, okMitInhalt);
			signaturen.put(ID_SCHUELER_ZWEI, okOhneInhalt);
			return signaturen;
		};
		final SchulbescheinigungQrFactory factory = new SchulbescheinigungQrFactory(reportingContext, () -> dienst, UHR);

		final Map<Long, SchulbescheinigungQrDaten> ergebnis = factory.erzeuge(List.of(ID_SCHUELER, ID_SCHUELER_ZWEI));

		assertEquals(SchulbescheinigungSignaturzustand.SIGNIERFEHLER, ergebnis.get(ID_SCHUELER_ZWEI).zustand());
		verify(reportingContext, times(1)).meldeAusgabeproblem(eq(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN), eq(ReportingProblemSchluessel.fuer(SchulbescheinigungQrDaten.class, ID_SCHUELER_ZWEI)),
				anyString(), isNull());
	}

	@Test
	void testEinNichtGeladenerSchuelerBleibtOhneEigeneMeldungUndOhneDienstaufruf() {
		// Den vollständigen Entfall meldet bereits die Auswahl als ausgelassenen Datensatz; eine zweite Meldung mit anderem Schlüssel würde ihn doppelt
		// zählen. Bei leerem XML-Stapel wird der Signierdienst gar nicht aufgerufen.
		when(repositorySchueler.schueler(ID_SCHUELER)).thenReturn(null);
		final SchulbescheinigungQrFactory factory = new SchulbescheinigungQrFactory(reportingContext, () -> {
			throw new AssertionError("Bei leerem XML-Stapel darf der Signierdienst nicht aufgerufen werden.");
		}, UHR);

		final Map<Long, SchulbescheinigungQrDaten> ergebnis = factory.erzeuge(List.of(ID_SCHUELER));

		assertEquals(SchulbescheinigungSignaturzustand.DATENFEHLER, ergebnis.get(ID_SCHUELER).zustand(),
				"Der Eintrag entsteht trotzdem, damit kein null in den Cache gelangt.");
		verify(reportingContext, never()).meldeAusgabeproblem(any(), any(), any(), anyString(), any());
	}

	@Test
	void testEinNichtErreichbarerSignierdienstBrichtMitEinemServerfehlerAb() {
		final IllegalStateException fehler = new IllegalStateException("Der Signierdienst verweigert die Verbindung.");
		final SchulbescheinigungQrFactory factory = new SchulbescheinigungQrFactory(reportingContext, () -> payloadById -> {
			throw fehler;
		}, UHR);
		final List<Long> idsSchueler = List.of(ID_SCHUELER);

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> factory.erzeuge(idsSchueler));

		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
		assertEquals(fehler, aoe.getCause(), "Die Originalursache gehört in die Kette; die Abschlussgrenze protokolliert sie im Server-Log.");
		verify(reportingContext, never()).meldeAusgabeproblem(any(), any(), any(), anyString(), any());
	}

	@Test
	void testEinErkennbarerAnmeldefehlerBrichtMitEinemClientFehlerAb() {
		// Die Diagnose "Anmeldedaten falsch" ist fachlich korrekt und weitersagbar; erkannt wird sie allein am Status der Ursachenkette, nie am Text.
		final ApiOperationException anmeldefehler = new ApiOperationException(Status.UNAUTHORIZED, "Signature-API returned 401");
		final SchulbescheinigungQrFactory factory = new SchulbescheinigungQrFactory(reportingContext, () -> payloadById -> {
			throw new IllegalStateException("Aufruf fehlgeschlagen", anmeldefehler);
		}, UHR);
		final List<Long> idsSchueler = List.of(ID_SCHUELER);

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> factory.erzeuge(idsSchueler));

		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
	}

	@Test
	void testEinBerechtigungsfehlerBehaeltSeinenEigenenStatus() {
		// Ein 403 kann auch lokal aus der Kompetenzprüfung des Services stammen und ist deshalb kein sicherer Anmeldefehler des Dienstes: Er wird mit
		// eigenem Status durchgereicht statt als Zugangsdaten-Problem ausgegeben.
		final ApiOperationException berechtigungsfehler = new ApiOperationException(Status.FORBIDDEN, "User has no permissions to create digital signatures.");
		final SchulbescheinigungQrFactory factory = new SchulbescheinigungQrFactory(reportingContext, () -> payloadById -> {
			throw berechtigungsfehler;
		}, UHR);
		final List<Long> idsSchueler = List.of(ID_SCHUELER);

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> factory.erzeuge(idsSchueler));

		assertEquals(Status.FORBIDDEN, aoe.getStatus());
	}

	@Test
	void testEinStapelAusLeerenOkAntwortenBrichtDieErstellungAb() {
		// Für den Gesamtabbruch gilt dasselbe Kriterium wie für den Einzelfehler beim QR-Bau: OK ohne Inhalt ist keine verwertbare Signatur. Mit
		// zweierlei Maß entstünden hier ausschließlich unsignierte Bescheinigungen statt des Abbruchs.
		final Signature okOhneInhalt = new Signature(new byte[0], SignatureStatus.OK, null);
		final SignatureService nurLeereAntworten = payloadById -> {
			final Map<Object, Signature> signaturen = new HashMap<>();
			for (final Object id : payloadById.keySet()) {
				signaturen.put(id, okOhneInhalt);
			}
			return signaturen;
		};
		final SchulbescheinigungQrFactory factory = new SchulbescheinigungQrFactory(reportingContext, () -> nurLeereAntworten, UHR);
		final List<Long> idsSchueler = List.of(ID_SCHUELER, ID_SCHUELER_ZWEI);

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> factory.erzeuge(idsSchueler));

		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
	}


	@Test
	void testEinFehlerBeimAufbauDerSchuldatenWirdNichtStillHingenommen() {
		// schule() lädt nichts nach - die Stammdaten stehen seit dem Konstruktor des Repositories. Ein Fehler hier ist deshalb kein Ladefehler, den eine
		// Zugriffsstelle melden würde, sondern ein Programmierfehler oder inkonsistenter Zustand. Er propagiert.
		final ReportingRepositorySchule repositorySchule = mock(ReportingRepositorySchule.class);
		when(repositorySchule.schule()).thenThrow(new IllegalStateException("Die Schuldaten sind nicht verwendbar."));
		when(reportingContext.repositorySchule()).thenReturn(repositorySchule);
		final SchulbescheinigungQrFactory factory =
				new SchulbescheinigungQrFactory(reportingContext, () -> dienstMitFehlerstatusJeFall("wird nicht erreicht"), UHR);
		final List<Long> idsSchueler = List.of(ID_SCHUELER);

		assertThrows(IllegalStateException.class, () -> factory.erzeuge(idsSchueler));

		verify(reportingContext, never()).meldeAusgabeproblem(any(), any(), any(), anyString(), any());
	}

	@Test
	void testEinInkonsistenterZustandDerAusstellungsdatenWirdNichtStillHingenommen() {
		// Der Zugriff auf die Schule gelingt, erst die Ableitung scheitert am fehlenden aktuellen Schuljahresabschnitt. Das ist kein Ladefehler, den eine
		// Zugriffsstelle meldet - finge die Factory ihn, entstünde eine unsignierte Bescheinigung ohne ein einziges gezähltes Ausgabeproblem. Er propagiert
		// deshalb bis zur Abschlussgrenze.
		when(schule.aktuellerSchuljahresabschnitt()).thenReturn(null);
		final SchulbescheinigungQrFactory factory =
				new SchulbescheinigungQrFactory(reportingContext, () -> dienstMitFehlerstatusJeFall("wird nicht erreicht"), UHR);
		final List<Long> idsSchueler = List.of(ID_SCHUELER);

		assertThrows(NullPointerException.class, () -> factory.erzeuge(idsSchueler));

		verify(reportingContext, never()).meldeAusgabeproblem(any(), any(), any(), anyString(), any());
	}

}
