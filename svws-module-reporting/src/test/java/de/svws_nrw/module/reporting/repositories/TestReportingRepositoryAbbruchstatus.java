package de.svws_nrw.module.reporting.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.data.gost.DataGostBlockungsdaten;
import de.svws_nrw.data.gost.DataGostBlockungsergebnisse;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.gost.GostServiceFactory;
import de.svws_nrw.service.gost.GostServiceFactoryBuilder;
import jakarta.ws.rs.core.Response.Status;

/**
 * Prüft, mit welchem Status die Repositories abbrechen, wenn ein Zugriff scheitert, den der Report nicht auslassen kann.
 * <p>Zwei Fragen entscheidet dieser Test: Trägt der Abbruch überhaupt einen Status - eine {@code IllegalStateException} kommt am API-Rand als undefinierter
 * Serverfehler an -, und ist es der richtige. Ein pauschales {@code NOT_FOUND} über einem Serverfehler sagt dem Anwender, seine Daten gebe es nicht.</p>
 * <p>Kein Repository protokolliert den Abbruch selbst: Den einen {@code ERROR}-Eintrag schreibt die Abschlussgrenze, die auch den Status nach außen gibt.</p>
 */
class TestReportingRepositoryAbbruchstatus {

	/** Die ID des Blockungsergebnisses, das die Tests anfordern. */
	private static final long ID_BLOCKUNGSERGEBNIS = 3L;

	/** Die ID der Blockung, zu der das Blockungsergebnis gehört. */
	private static final long ID_BLOCKUNG = 7L;

	/** Das Abiturjahr, dessen Fachwahlstatistik die Tests anfordern. */
	private static final int ABITURJAHR = 2025;

	/** Der gemockte Context. Er liefert bewusst keine Datenbankverbindung, sodass jeder Datenzugriff scheitert. */
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
	 * Gibt die Texte der Log-Einträge mit dem Level ERROR zurück.
	 *
	 * @return Die Texte der Einträge, ohne die Einrückung des Loggers.
	 */
	private List<String> fehlermeldungenImLog() {
		return log.getLogData().stream().filter(eintrag -> eintrag.getLevel() == LogLevel.ERROR).map(eintrag -> eintrag.getText().strip()).toList();
	}


	@Test
	void testEinNichtLadbarerKatalogBrichtMitEinemServerfehlerAb() {
		final ReportingRepositoryKataloge repository = new ReportingRepositoryKataloge(reportingContext);

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> repository.ort(1L));

		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus(), "Ein nicht ladbarer Katalog ist ein Serverproblem und trägt diesen Status nach außen.");
		assertNotNull(aoe.getCause(), "Die Ursache gehört in den Abbruch; die Abschlussgrenze protokolliert sie mit Stacktrace.");
		assertEquals(List.of(), fehlermeldungenImLog(), "Den einen ERROR-Eintrag schreibt die Abschlussgrenze: " + fehlermeldungenImLog());
	}

	@Test
	void testNichtLadbareLehrerstammdatenBrechenMitEinemServerfehlerAb() {
		final ReportingRepositoryLehrer repository = new ReportingRepositoryLehrer(reportingContext);

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> repository.existiertLehrer(1L));

		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
		assertNotNull(aoe.getCause());
		assertEquals(List.of(), fehlermeldungenImLog(), "Den einen ERROR-Eintrag schreibt die Abschlussgrenze: " + fehlermeldungenImLog());
	}

	@Test
	void testEinServerfehlerDerDatenschichtBleibtEinServerfehler() {
		// Der entscheidende Fall der GOSt-Kursplanung: Ein pauschales NOT_FOUND meldete dem Anwender, sein Blockungsergebnis gebe es nicht, obwohl der
		// Serverfehler eine ganz andere Ursache hat.
		final ReportingRepositoryGostKursplanung repository = new ReportingRepositoryGostKursplanung(reportingContext);
		final ApiOperationException ursache = new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Die Blockungsdaten sind fehlerhaft.");

		try (MockedStatic<DataGostBlockungsergebnisse> dataErgebnisse = mockStatic(DataGostBlockungsergebnisse.class)) {
			dataErgebnisse.when(() -> DataGostBlockungsergebnisse.getErgebnisFromID(any(), anyLong())).thenThrow(ursache);

			final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> repository.initManager(ID_BLOCKUNGSERGEBNIS));

			assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
			assertSame(ursache, aoe.getCause(), "Die Meldung der Datenschicht bleibt als Ursache erhalten.");
		}
		assertEquals(List.of(), fehlermeldungenImLog(), "Den einen ERROR-Eintrag schreibt die Abschlussgrenze: " + fehlermeldungenImLog());
	}

	@Test
	void testEinServerfehlerBeimBlockungsdatenManagerBleibtEinServerfehler() {
		// Die zweite Statusstelle der GOSt-Kursplanung: Das Blockungsergebnis ist ladbar, erst der Zugriff auf die Blockungsdaten scheitert. Der Stub der
		// Blockungsdaten verlangt die ID der Blockung aus dem Ergebnis und belegt damit zugleich deren Durchreichung.
		final ReportingRepositoryGostKursplanung repository = new ReportingRepositoryGostKursplanung(reportingContext);
		final GostBlockungsergebnis blockungsergebnis = new GostBlockungsergebnis();
		blockungsergebnis.blockungID = ID_BLOCKUNG;
		final ApiOperationException ursache = new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Die Blockungsdaten sind fehlerhaft.");

		try (MockedStatic<DataGostBlockungsergebnisse> dataErgebnisse = mockStatic(DataGostBlockungsergebnisse.class);
				MockedStatic<DataGostBlockungsdaten> dataBlockungsdaten = mockStatic(DataGostBlockungsdaten.class)) {
			dataErgebnisse.when(() -> DataGostBlockungsergebnisse.getErgebnisFromID(any(), anyLong())).thenReturn(blockungsergebnis);
			dataBlockungsdaten.when(() -> DataGostBlockungsdaten.getBlockungsdatenManagerFromDB(any(), eq(ID_BLOCKUNG))).thenThrow(ursache);

			final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> repository.initManager(ID_BLOCKUNGSERGEBNIS));

			assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
			assertSame(ursache, aoe.getCause(), "Die Meldung der Datenschicht bleibt als Ursache erhalten.");
		}
		assertEquals(List.of(), fehlermeldungenImLog(), "Den einen ERROR-Eintrag schreibt die Abschlussgrenze: " + fehlermeldungenImLog());
	}

	@Test
	void testNichtLadbareAbiturjahrgaengeBrechenMitEinemServerfehlerAb() {
		// Gegen diese Liste prüfen die GOSt-Datenaufbauten ihre Parameter. Ein Ladefehler ist ein Serverproblem; als Parameterfehler ausgegeben, suchte der
		// Aufrufer die Ursache bei seinen Eingaben.
		final ReportingRepositoryGost repository = new ReportingRepositoryGost(reportingContext);

		final ApiOperationException aoe = assertThrows(ApiOperationException.class, repository::abiturjahrgaenge);

		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
		assertNotNull(aoe.getCause(), "Die Ursache gehört in den Abbruch; die Abschlussgrenze protokolliert sie mit Stacktrace.");
		assertEquals(List.of(), fehlermeldungenImLog(), "Den einen ERROR-Eintrag schreibt die Abschlussgrenze: " + fehlermeldungenImLog());
	}

	@Test
	void testEineNichtLadbareFachwahlstatistikBrichtDenStriktenZugriffMitEinemServerfehlerAb() {
		// Der strikte Zugriff bedient den Report, dessen Hauptinhalt die Statistik ist. Ein leerer Report mit Erfolgsstatus wäre dort ein stiller Leerlauf.
		final ReportingRepositoryGost repository = new ReportingRepositoryGost(reportingContext);
		final ApiOperationException ursache = new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Die Fachwahlen sind nicht lesbar.");

		try (MockedStatic<GostServiceFactoryBuilder> serviceFactoryBuilder = mockStatic(GostServiceFactoryBuilder.class)) {
			final GostServiceFactory serviceFactory = mock(GostServiceFactory.class, RETURNS_DEEP_STUBS);
			serviceFactoryBuilder.when(GostServiceFactoryBuilder::getGostServiceFactory).thenReturn(serviceFactory);
			when(serviceFactory.getGostJahrgangFachwahlService().getFachwahlStatistik(ABITURJAHR)).thenThrow(ursache);

			final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> repository.fachwahlen(ABITURJAHR));

			assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
			assertSame(ursache, aoe.getCause(), "Die Meldung der Datenschicht bleibt als Ursache erhalten.");
		}
		assertEquals(List.of(), fehlermeldungenImLog(), "Den einen ERROR-Eintrag schreibt die Abschlussgrenze: " + fehlermeldungenImLog());
	}

	@Test
	void testEinUngueltigesHalbjahrDerBlockungsdatenIstEinServerfehler() {
		// Die Halbjahres-ID stammt aus den gespeicherten Blockungsdaten des Servers; ein ungültiger Wert ist eine Inkonsistenz der Serverdaten. Als
		// NOT_FOUND suchte der Anwender die Ursache bei seiner Anfrage.
		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> ReportingRepositoryGostKursplanung.ermittleGostHalbjahr(9, ID_BLOCKUNG));

		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
	}

	@Test
	void testEinGueltigesHalbjahrDerBlockungsdatenWirdAufgeloest() {
		assertEquals(GostHalbjahr.Q11, ReportingRepositoryGostKursplanung.ermittleGostHalbjahr(GostHalbjahr.Q11.id, ID_BLOCKUNG));
	}

	@Test
	void testEinNichtVorhandenesBlockungsergebnisBleibtNotFound() {
		// Gegenprobe: Der durchgereichte Status ist keine pauschale Ersetzung durch 500 - eine unbekannte ID bleibt die Auskunft "gibt es nicht".
		final ReportingRepositoryGostKursplanung repository = new ReportingRepositoryGostKursplanung(reportingContext);

		try (MockedStatic<DataGostBlockungsergebnisse> dataErgebnisse = mockStatic(DataGostBlockungsergebnisse.class)) {
			dataErgebnisse.when(() -> DataGostBlockungsergebnisse.getErgebnisFromID(any(), anyLong()))
					.thenThrow(new ApiOperationException(Status.NOT_FOUND, "Ungültige Blockungsergebnis-ID übergeben."));

			final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> repository.initManager(ID_BLOCKUNGSERGEBNIS));

			assertEquals(Status.NOT_FOUND, aoe.getStatus());
		}
	}

}
