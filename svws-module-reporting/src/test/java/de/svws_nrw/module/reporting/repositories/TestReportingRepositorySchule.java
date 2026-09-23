package de.svws_nrw.module.reporting.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import de.svws_nrw.asd.data.schule.SchuleStammdaten;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.reporting.ReportingBildDefinition;
import de.svws_nrw.data.schule.DataSchuleStammdaten;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOLogo;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemSchluessel;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemauswirkung;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemursache;
import de.svws_nrw.module.reporting.types.schule.ReportingBild;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.repo.schule.logoverwaltung.LogoverwaltungRepository;
import de.svws_nrw.repo.schule.logoverwaltung.LogoverwaltungRepositoryFactory;
import jakarta.ws.rs.core.Response.Status;

/**
 * Prüft die Auskünfte des Schul-Repositories, die über den Ausgang eines Reports entscheiden: die Schulform der Schule, ob sie eine gymnasiale Oberstufe
 * besitzt und ob der angeforderte Schuljahresabschnitt zu ihr gehört. Die Oberstufe wird aus der Schulform gelesen statt aus dem Fehlschlag einer Prüfung
 * abgeleitet, und ein unbekannter Abschnitt ist ein Fehler des Aufrufers und keiner des Servers.
 * <p>Die Datenbank wird an ihren beiden Nähten ersetzt: den Stammdaten der Schule, die der Konstruktor lädt, und dem angemeldeten Benutzer, der die
 * Schulform kennt.</p>
 */
class TestReportingRepositorySchule {

	/** Die ID des Schuljahresabschnitts, den der Report auswählt. */
	private static final long ID_ABSCHNITT = 11L;

	/** Der Wert, den die Reporting-Parameter tragen, solange kein Schuljahresabschnitt gewählt wurde. */
	private static final long ID_ABSCHNITT_NICHT_GESETZT = -1L;

	/** Ein gültiges PNG im Base64-Format, wie es die Logoverwaltung führt. */
	private static final String PNG_BASE64 =
			"iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg==";

	/** Die Bildquelle, die zu {@link #PNG_BASE64} erwartet wird. */
	private static final String PNG_BILDQUELLE = "data:image/png;base64," + PNG_BASE64;

	/** Der Context, mit dem das Repository erzeugt wird. */
	private ReportingContext reportingContext;

	/** Der angemeldete Benutzer, über den die Schulform gelesen wird. */
	private Benutzer benutzer;

	/** Die Stammdaten der Schule; einzelne Tests stellen darüber das Kürzel der Schulform um. */
	private SchuleStammdaten stammdaten;

	/** Die Liste, die die Einträge des Loggers sammelt. */
	private LogConsumerList log;

	/** Die Datenbankverbindung, über die das Repository den angemeldeten Benutzer erreicht. */
	private DBEntityManager conn;

	/** Das Repository der Logoverwaltung; über es erhält der Test die Bilder in den Zugriff. */
	private LogoverwaltungRepository logoverwaltungRepository;

	/** Die Naht für das Laden der Stammdaten über die statische Methode. */
	private MockedStatic<DataSchuleStammdaten> dataSchuleStammdatenStatisch;

	/** Die Naht für die Repository-Factory der Logoverwaltung, die sonst die Verbindung des laufenden Requests verlangte. */
	private MockedStatic<LogoverwaltungRepositoryFactory> logoverwaltungRepositoryFactoryStatisch;

	/** Die Naht für die Repository-Factory der Schule, die der Logoverwaltungs-Service beim Erstellen mitbekommt. */
	private MockedStatic<EigeneSchuleRepositoryFactory> eigeneSchuleRepositoryFactoryStatisch;


	@BeforeAll
	static void initCoreTypes() {
		ASDCoreTypeUtils.initAll();
	}


	@BeforeEach
	void setUp() {
		final Schuljahresabschnitt abschnitt = new Schuljahresabschnitt();
		abschnitt.id = ID_ABSCHNITT;
		abschnitt.schuljahr = 2026;
		abschnitt.abschnitt = 1;

		stammdaten = new SchuleStammdaten();
		stammdaten.idSchuljahresabschnitt = ID_ABSCHNITT;
		stammdaten.abschnitte.add(abschnitt);

		benutzer = mock(Benutzer.class);
		when(benutzer.schuleGetStammdaten()).thenReturn(stammdaten);

		conn = mock(DBEntityManager.class);
		when(conn.getUser()).thenReturn(benutzer);

		final Logger logger = new Logger();
		log = new LogConsumerList();
		logger.addConsumer(log);

		reportingContext = mock(ReportingContext.class);
		when(reportingContext.logger()).thenReturn(logger);
		when(reportingContext.conn()).thenReturn(conn);

		dataSchuleStammdatenStatisch = mockStatic(DataSchuleStammdaten.class);
		dataSchuleStammdatenStatisch.when(() -> DataSchuleStammdaten.getStammdaten(any())).thenReturn(stammdaten);

		logoverwaltungRepository = mock(LogoverwaltungRepository.class);
		when(logoverwaltungRepository.findByKennung(any())).thenReturn(Optional.empty());
		final LogoverwaltungRepositoryFactory logoverwaltungRepositoryFactory = mock(LogoverwaltungRepositoryFactory.class);
		when(logoverwaltungRepositoryFactory.getRepository()).thenReturn(logoverwaltungRepository);
		logoverwaltungRepositoryFactoryStatisch = mockStatic(LogoverwaltungRepositoryFactory.class);
		logoverwaltungRepositoryFactoryStatisch.when(LogoverwaltungRepositoryFactory::getNewInstance).thenReturn(logoverwaltungRepositoryFactory);

		eigeneSchuleRepositoryFactoryStatisch = mockStatic(EigeneSchuleRepositoryFactory.class);
		eigeneSchuleRepositoryFactoryStatisch.when(EigeneSchuleRepositoryFactory::getNewInstance)
				.thenReturn(mock(EigeneSchuleRepositoryFactory.class));
	}

	@AfterEach
	void tearDown() {
		dataSchuleStammdatenStatisch.close();
		logoverwaltungRepositoryFactoryStatisch.close();
		eigeneSchuleRepositoryFactoryStatisch.close();
	}


	/**
	 * Gibt die Texte der Log-Einträge mit dem Level ERROR zurück.
	 *
	 * @return Die Texte der Einträge, ohne die Einrückung des Loggers.
	 */
	private List<String> fehlermeldungenImLog() {
		return log.getLogData().stream().filter(eintrag -> eintrag.getLevel() == LogLevel.ERROR).map(eintrag -> eintrag.getText().strip()).toList();
	}


	@Test
	void testEineSchuleMitGymnasialerOberstufeWirdErkannt() {
		when(benutzer.schuleHatGymOb()).thenReturn(true);

		assertTrue(new ReportingRepositorySchule(reportingContext, ID_ABSCHNITT).istSchuleMitGost());
	}

	@Test
	void testEineSchuleOhneGymnasialeOberstufeWirdErkannt() {
		when(benutzer.schuleHatGymOb()).thenReturn(false);

		assertFalse(new ReportingRepositorySchule(reportingContext, ID_ABSCHNITT).istSchuleMitGost());
	}

	@Test
	void testEinNichtGesetzterSchuljahresabschnittIstEinParameterfehler() {
		// Die Reporting-Parameter tragen -1, solange kein Abschnitt gewählt wurde. Ungeprüft übernommen, liefert das Auflösen später null und der Report endet
		// mit einem Serverfehler an einer beliebigen Stelle - obwohl der Aufrufer schlicht keinen Abschnitt benannt hat.
		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> new ReportingRepositorySchule(reportingContext, ID_ABSCHNITT_NICHT_GESETZT));

		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals(List.of(), fehlermeldungenImLog(), "Den einen ERROR-Eintrag schreibt die Abschlussgrenze: " + fehlermeldungenImLog());
	}

	@Test
	void testEinFremderSchuljahresabschnittIstEinParameterfehler() {
		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> new ReportingRepositorySchule(reportingContext, 999L));

		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
	}

	@Test
	void testNichtLadbareSchuldatenSindEinServerfehler() {
		// Gegenprobe zur Parameterprüfung: Hier ist nicht die Anfrage falsch, sondern der Server kommt an seine eigenen Daten nicht heran.
		final IllegalStateException ursache = new IllegalStateException("Die Verbindung zur Datenbank wurde unterbrochen.");
		dataSchuleStammdatenStatisch.when(() -> DataSchuleStammdaten.getStammdaten(any())).thenThrow(ursache);

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> new ReportingRepositorySchule(reportingContext, ID_ABSCHNITT));

		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus(), "Nicht ladbare Schuldaten sind kein NOT_FOUND und kein Fehler des Aufrufers.");
		assertSame(ursache, aoe.getCause(), "Die Ursache gehört in den Abbruch; die Abschlussgrenze protokolliert sie mit Stacktrace.");
		assertEquals(List.of(), fehlermeldungenImLog(), "Den einen ERROR-Eintrag schreibt die Abschlussgrenze: " + fehlermeldungenImLog());
	}

	@Test
	void testDieSchulformWirdAusDemKuerzelDerStammdatenAufgeloest() {
		stammdaten.schulform = Schulform.GY.historie().getLast().kuerzel;

		assertEquals(Schulform.GY, new ReportingRepositorySchule(reportingContext, ID_ABSCHNITT).schulform());
	}

	@Test
	void testEinFehlendesSchulformkuerzelLiefertKeineSchulform() {
		// Die Auskunft bleibt leer und liefert keinen Ersatzwert; die Bedeutung legt der Aufrufer fest.
		stammdaten.schulform = "";

		assertNull(new ReportingRepositorySchule(reportingContext, ID_ABSCHNITT).schulform());
	}

	@Test
	void testEinUnbekanntesSchulformkuerzelLiefertKeineSchulform() {
		stammdaten.schulform = "Kein Kuerzel einer Schulform";

		assertNull(new ReportingRepositorySchule(reportingContext, ID_ABSCHNITT).schulform());
	}

	@Test
	void testEineNichtBestimmbareSchulformGiltNichtAlsSchuleOhneOberstufe() {
		// Der entscheidende Fall: Lässt sich die Schulform nicht bestimmen, ist das ein Serverproblem. Als "keine gymnasiale Oberstufe" ausgegeben, erhielte
		// der Anwender die fachlich falsche Auskunft, seine Schule habe keine Oberstufe.
		when(benutzer.schuleHatGymOb()).thenThrow(new DeveloperNotificationException("Die Schulform der Schule konnte nicht bestimmt werden."));

		final ReportingRepositorySchule repository = new ReportingRepositorySchule(reportingContext, ID_ABSCHNITT);

		assertThrows(DeveloperNotificationException.class, repository::istSchuleMitGost);
	}


	// ##### Bilder aus der Logoverwaltung #####

	/**
	 * Legt fest, dass die Logoverwaltung zu der übergebenen Bilddefinition den angegebenen Datensatz führt.
	 *
	 * @param bildDefinition Die Bilddefinition, nach der das Repository fragt.
	 * @param base64         Die Bilddaten des Datensatzes.
	 */
	private void hinterlegeBild(final ReportingBildDefinition bildDefinition, final String base64) {
		when(logoverwaltungRepository.findByKennung(bildDefinition))
				.thenReturn(Optional.of(new DTOLogo(1L, bildDefinition, base64, "2026-08-26")));
	}

	@Test
	void testDasBildWirdAusDerLogoverwaltungGeladen() {
		hinterlegeBild(ReportingBildDefinition.DIN5008_BRIEFKOPF, PNG_BASE64);

		final ReportingBild bild = new ReportingRepositorySchule(reportingContext, ID_ABSCHNITT).bild(ReportingBildDefinition.DIN5008_BRIEFKOPF);

		assertTrue(bild.vorhanden());
		assertEquals(PNG_BILDQUELLE, bild.htmlImageSource(), "Geliefert werden müssen die Bilddaten des Datensatzes und nicht irgendein nicht leerer Wert.");
		assertEquals(190, bild.breiteMM(), "Die Maße stammen aus der Bilddefinition und belegen, dass das Bild ihr zugeordnet bleibt.");
	}

	@Test
	void testDasGeladeneBildWirdZwischengespeichert() {
		// Eine Ausgabe in einzelne Dateien fragt dasselbe Bild je Datei erneut ab. Ohne Zwischenspeicher liefe je Abfrage ein Datenbankzugriff.
		hinterlegeBild(ReportingBildDefinition.DIN5008_BRIEFKOPF, PNG_BASE64);
		final ReportingRepositorySchule repository = new ReportingRepositorySchule(reportingContext, ID_ABSCHNITT);

		final ReportingBild erst = repository.bild(ReportingBildDefinition.DIN5008_BRIEFKOPF);
		final ReportingBild zweit = repository.bild(ReportingBildDefinition.DIN5008_BRIEFKOPF);

		assertSame(erst, zweit);
		verify(logoverwaltungRepository, times(1)).findByKennung(ReportingBildDefinition.DIN5008_BRIEFKOPF);
	}

	@Test
	void testVerschiedeneBilddefinitionenWerdenGetrenntGehalten() {
		hinterlegeBild(ReportingBildDefinition.DIN5008_BRIEFKOPF, PNG_BASE64);
		final ReportingRepositorySchule repository = new ReportingRepositorySchule(reportingContext, ID_ABSCHNITT);

		assertTrue(repository.bild(ReportingBildDefinition.DIN5008_BRIEFKOPF).vorhanden());
		assertFalse(repository.bild(ReportingBildDefinition.SCHULLOGO_QUADRATISCH).vorhanden(),
				"Der Zwischenspeicher darf das Bild einer Definition nicht an eine andere weiterreichen.");
	}

	@Test
	void testOhneEintragEntstehtEinLeeresBildOhneAusgabeproblem() {
		// Welche Bilddefinitionen eine Schule pflegt, entscheidet sie selbst. Ein fehlendes Bild ist deshalb kein Befund für das Ausgabeprotokoll,
		// auch wenn der Service es mit dem Status NOT_FOUND beantwortet.
		final ReportingBild bild = new ReportingRepositorySchule(reportingContext, ID_ABSCHNITT).bild(ReportingBildDefinition.DIN5008_BRIEFKOPF);

		assertFalse(bild.vorhanden());
		assertEquals("", bild.htmlImageSource());
		verify(reportingContext, never()).meldeAusgabeproblem(any(), any(), any(), anyString(), any());
	}

	@Test
	void testNichtAufloesbareBilddatenErgebenEinLeeresBildOhneAusgabeproblem() {
		// Einen Datensatz, den sie nicht auflösen kann, reicht die Logoverwaltung unverändert zurück - auch mitsamt einem Kopf, der nicht zu seinem
		// Inhalt passt. Für die Ausgabe zählt er wie ein fehlendes Bild und ist kein Ladefehler.
		hinterlegeBild(ReportingBildDefinition.DIN5008_BRIEFKOPF, "data:image/png;base64,AAAA");

		final ReportingBild bild = new ReportingRepositorySchule(reportingContext, ID_ABSCHNITT).bild(ReportingBildDefinition.DIN5008_BRIEFKOPF);

		assertFalse(bild.vorhanden());
		verify(reportingContext, never()).meldeAusgabeproblem(any(), any(), any(), anyString(), any());
	}

	@Test
	void testEinGescheitertesLadenMeldetEinAusgabeproblemUndLiefertEinLeeresBild() {
		// Ein Bild ist untergeordnetes Datum: Die Ausgabe erscheint weiterhin, ihr fehlt allein das Bild.
		when(logoverwaltungRepository.findByKennung(ReportingBildDefinition.DIN5008_BRIEFKOPF))
				.thenThrow(new IllegalStateException("Der Datensatz konnte nicht gelesen werden."));

		final ReportingBild bild = new ReportingRepositorySchule(reportingContext, ID_ABSCHNITT).bild(ReportingBildDefinition.DIN5008_BRIEFKOPF);

		assertFalse(bild.vorhanden());
		verify(reportingContext).meldeAusgabeproblem(eq(ReportingProblemursache.DATENSATZBEZOGENER_LADEFEHLER),
				eq(ReportingProblemauswirkung.TEILDATEN_FEHLEN),
				eq(ReportingProblemSchluessel.fuer(ReportingBild.class, ReportingBildDefinition.DIN5008_BRIEFKOPF)), anyString(), any());
	}

	@Test
	void testOhneBilddefinitionWirdNichtGeladen() {
		final ReportingBild bild = new ReportingRepositorySchule(reportingContext, ID_ABSCHNITT).bild(null);

		assertFalse(bild.vorhanden());
		assertEquals(0, bild.breiteMM());
		verify(logoverwaltungRepository, never()).findByKennung(any());
	}

}
