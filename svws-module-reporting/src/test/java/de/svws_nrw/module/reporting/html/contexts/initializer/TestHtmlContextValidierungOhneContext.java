package de.svws_nrw.module.reporting.html.contexts.initializer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests der Prüfungen aus {@link HtmlContextValidierung}, die <b>keine Daten nachladen</b>: die reinen Wertprüfungen für Abiturjahr und GOSt-Halbjahr sowie
 * die vollständige ID-Validierung.
 * <p>Diese Methoden benötigen weder Datenbank noch Reporting-Context. Die ID-Prüfungen erhalten allein einen {@link Logger}, der hier zusammen mit einer
 * {@link LogConsumerList} aufgebaut wird — so lässt sich neben dem Statuscode auch der geforderte ERROR-Logeintrag nachweisen.</p>
 * <p>Die Prüfungen für Abiturjahrgang und Halbjahre werfen bewusst <b>ohne</b> eigenen Log-Eintrag; das ist Bestandsverhalten und wird hier nicht
 * mitgeprüft.</p>
 */
class TestHtmlContextValidierungOhneContext {

	/** Die Abiturjahrgänge, die in den Tests als vorhanden gelten. */
	private static final List<Integer> VORHANDENE_ABITURJAHRGAENGE = List.of(2024, 2025);

	/** Die Bezeichnung des ID-Typs, mit der die Meldung über eine leere ID-Liste gebildet wird. */
	private static final String ID_TYP = "Test-IDs";

	/** Die Meldung, die bei einer nicht auflösbaren ID erwartet wird. */
	private static final String MELDUNG_UNVOLLSTAENDIG = "FEHLER: Es wurden ungültige Test-IDs übergeben.";

	/** Die Meldung, die bei einer leeren ID-Liste erwartet wird. */
	private static final String MELDUNG_LEER = "FEHLER: Es wurden keine Test-IDs übergeben.";

	/** Der Logger, den die ID-Prüfungen in den Tests verwenden. */
	private Logger logger;

	/** Die Liste, die die Einträge des Loggers sammelt. */
	private LogConsumerList log;


	/**
	 * Ein minimales geladenes Objekt für die Collection-Variante der ID-Prüfung.
	 *
	 * @param id Die ID des geladenen Objekts.
	 */
	private record GeladenesObjekt(long id) {
		// Nur Träger einer ID - die Prüfung greift ausschließlich über den ID-Extraktor darauf zu.
	}


	@BeforeEach
	void setUp() {
		logger = new Logger();
		log = new LogConsumerList();
		logger.addConsumer(log);
	}


	/**
	 * Gibt die Texte aller Logeinträge mit dem Level ERROR zurück. Die Einrückung, die der Logger dem Text voranstellt, wird dabei entfernt: Geprüft wird
	 * die Meldung, nicht ihre Formatierung.
	 *
	 * @return Die Texte der ERROR-Logeinträge in der Reihenfolge ihres Auftretens.
	 */
	private List<String> fehlermeldungenImLog() {
		return log.getLogData().stream().filter(eintrag -> eintrag.getLevel() == LogLevel.ERROR).map(eintrag -> eintrag.getText().strip()).toList();
	}


	// ##### Gruppe A: reine Wertprüfungen #####

	@Test
	void testEinVorhandenesAbiturjahrIstGueltig() {
		assertDoesNotThrow(() -> HtmlContextValidierung.validiereAbiturjahr(2025, VORHANDENE_ABITURJAHRGAENGE));
	}

	@Test
	void testEinNichtVorhandenesAbiturjahrIstUngueltig() {
		final ApiOperationException aoe =
				assertThrows(ApiOperationException.class, () -> HtmlContextValidierung.validiereAbiturjahr(2026, VORHANDENE_ABITURJAHRGAENGE));
		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals("FEHLER: Ein Abiturjahr liegt außerhalb des Wertebereichs.", aoe.getBody());
	}

	@Test
	void testEinAbiturjahrVor1900IstAuchDannUngueltigWennEsVorhandenIst() {
		// Die untere Wertebereichsgrenze wird vor der Prüfung auf den Bestand ausgewertet und entscheidet damit allein.
		final List<Integer> abiturjahrgaenge = List.of(1899);
		final ApiOperationException aoe =
				assertThrows(ApiOperationException.class, () -> HtmlContextValidierung.validiereAbiturjahr(1899, abiturjahrgaenge));
		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
	}

	@Test
	void testOhneVorhandeneAbiturjahrgaengeIstJedesAbiturjahrUngueltig() {
		final List<Integer> abiturjahrgaenge = List.of();
		final ApiOperationException aoe =
				assertThrows(ApiOperationException.class, () -> HtmlContextValidierung.validiereAbiturjahr(2025, abiturjahrgaenge));
		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
	}

	@Test
	void testAlleGostHalbjahreSindGueltig() {
		for (int halbjahrId = 0; halbjahrId <= 5; halbjahrId++) {
			final int id = halbjahrId;
			assertDoesNotThrow(() -> HtmlContextValidierung.validiereHalbjahr(id), "Das GOSt-Halbjahr mit der ID %d muss gültig sein.".formatted(id));
		}
	}

	@Test
	void testEinHalbjahrAusserhalbDesWertebereichsIstUngueltig() {
		for (final int halbjahrId : new int[] { -1, 6 }) {
			final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> HtmlContextValidierung.validiereHalbjahr(halbjahrId));
			assertEquals(Status.BAD_REQUEST, aoe.getStatus());
			assertEquals("FEHLER: Ein GOSt-Halbjahr liegt außerhalb des Wertebereichs.", aoe.getBody());
		}
	}

	@Test
	void testPaarweiseParameterZerlegenAbiturjahrUndHalbjahr() {
		// 20253 steht für den Abiturjahrgang 2025 und das GOSt-Halbjahr mit der ID 3.
		assertDoesNotThrow(() -> HtmlContextValidierung.validiereParameterPaarweise(List.of(20253L, 20240L), VORHANDENE_ABITURJAHRGAENGE));
	}

	@Test
	void testPaarweiseParameterMeldenEinUngueltigesHalbjahr() {
		final List<Long> parameterDaten = List.of(20256L);
		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereParameterPaarweise(parameterDaten, VORHANDENE_ABITURJAHRGAENGE));
		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals("FEHLER: Ein GOSt-Halbjahr liegt außerhalb des Wertebereichs.", aoe.getBody());
	}

	@Test
	void testPaarweiseParameterMeldenEinenUnbekanntenAbiturjahrgang() {
		final List<Long> parameterDaten = List.of(20263L);
		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereParameterPaarweise(parameterDaten, VORHANDENE_ABITURJAHRGAENGE));
		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals("FEHLER: Ein Abiturjahr liegt außerhalb des Wertebereichs.", aoe.getBody());
	}

	@Test
	void testEineZuLangeKombinierteIdWirdAbgewiesen() {
		// 202503 sieht nach "Abiturjahr 2025, Halbjahr 3" aus, wird von der Zerlegung aber zu Abiturjahr 20250 und Halbjahr 3. Da die Wertprüfung nur eine
		// untere Grenze kennt, fängt allein der Abgleich mit den vorhandenen Abiturjahrgängen diesen Fall ab.
		final List<Long> parameterDaten = List.of(202503L);
		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereParameterPaarweise(parameterDaten, VORHANDENE_ABITURJAHRGAENGE));
		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals("FEHLER: Ein Abiturjahr liegt außerhalb des Wertebereichs.", aoe.getBody());
	}

	@Test
	void testEineZuKurzeKombinierteIdWirdAbgewiesen() {
		// Der Gegenfall: Ein Abiturjahr ohne angehängtes Halbjahr zerfällt zu Abiturjahr 202 und Halbjahr 5 und scheitert an der unteren Grenze.
		final List<Long> parameterDaten = List.of(2025L);
		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereParameterPaarweise(parameterDaten, VORHANDENE_ABITURJAHRGAENGE));
		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals("FEHLER: Ein Abiturjahr liegt außerhalb des Wertebereichs.", aoe.getBody());
	}

	@Test
	void testPaarweiseParameterUeberspringenNullEintraege() {
		assertDoesNotThrow(() -> HtmlContextValidierung.validiereParameterPaarweise(Arrays.asList(20253L, null), VORHANDENE_ABITURJAHRGAENGE));
	}

	@Test
	void testEinzelneParameterAusAbiturjahrgangUndHalbjahrenSindGueltig() {
		assertDoesNotThrow(() -> HtmlContextValidierung.validiereParameterEinzeln(List.of(2025L, 0L, 1L), VORHANDENE_ABITURJAHRGAENGE));
	}

	@Test
	void testEinzelneParameterKommenOhneHalbjahreAus() {
		assertDoesNotThrow(() -> HtmlContextValidierung.validiereParameterEinzeln(List.of(2025L), VORHANDENE_ABITURJAHRGAENGE));
	}

	@Test
	void testEinzelneParameterMeldenEinenUnbekanntenAbiturjahrgang() {
		final List<Long> parameterDaten = List.of(2026L, 0L);
		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereParameterEinzeln(parameterDaten, VORHANDENE_ABITURJAHRGAENGE));
		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals("FEHLER: Ein Abiturjahr liegt außerhalb des Wertebereichs.", aoe.getBody());
	}

	@Test
	void testEinzelneParameterMeldenEinUngueltigesHalbjahr() {
		final List<Long> parameterDaten = List.of(2025L, 9L);
		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereParameterEinzeln(parameterDaten, VORHANDENE_ABITURJAHRGAENGE));
		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals("FEHLER: Ein GOSt-Halbjahr liegt außerhalb des Wertebereichs.", aoe.getBody());
	}


	// ##### Gruppe B: ID-Validierung über die Collection-Variante #####

	@Test
	void testVollstaendigGeladeneObjekteSindGueltig() {
		assertDoesNotThrow(() -> HtmlContextValidierung.validiereIds(logger, List.of(1L, 2L),
				List.of(new GeladenesObjekt(1L), new GeladenesObjekt(2L)), GeladenesObjekt::id, ID_TYP, MELDUNG_UNVOLLSTAENDIG));
		assertEquals(List.of(), fehlermeldungenImLog());
	}

	@Test
	void testDuplikateUndNullEintraegeWerdenVorDerPruefungEntfernt() {
		assertDoesNotThrow(() -> HtmlContextValidierung.validiereIds(logger, Arrays.asList(1L, 1L, null),
				List.of(new GeladenesObjekt(1L)), GeladenesObjekt::id, ID_TYP, MELDUNG_UNVOLLSTAENDIG));
		assertEquals(List.of(), fehlermeldungenImLog());
	}

	@Test
	void testMehrGeladeneObjekteAlsUebergebeneIdsSindZulaessig() {
		assertDoesNotThrow(() -> HtmlContextValidierung.validiereIds(logger, List.of(1L),
				List.of(new GeladenesObjekt(1L), new GeladenesObjekt(2L)), GeladenesObjekt::id, ID_TYP, MELDUNG_UNVOLLSTAENDIG));
	}

	@Test
	void testEineLeereIdListeWirdAbgewiesen() {
		final List<Long> idsUebergeben = List.of();
		final List<GeladenesObjekt> geladeneObjekte = List.of(new GeladenesObjekt(1L));
		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereIds(logger, idsUebergeben, geladeneObjekte, GeladenesObjekt::id, ID_TYP, MELDUNG_UNVOLLSTAENDIG));
		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals(MELDUNG_LEER, aoe.getBody());
		assertEquals(List.of(MELDUNG_LEER), fehlermeldungenImLog());
	}

	@Test
	void testEineNurAusNullEintraegenBestehendeIdListeWirdAbgewiesen() {
		final List<Long> idsUebergeben = Arrays.asList((Long) null, null);
		final List<GeladenesObjekt> geladeneObjekte = List.of(new GeladenesObjekt(1L));
		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereIds(logger, idsUebergeben, geladeneObjekte, GeladenesObjekt::id, ID_TYP, MELDUNG_UNVOLLSTAENDIG));
		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals(MELDUNG_LEER, aoe.getBody());
		assertEquals(List.of(MELDUNG_LEER), fehlermeldungenImLog());
	}

	@Test
	void testEineNichtGeladeneIdWirdAbgewiesen() {
		final List<Long> idsUebergeben = List.of(1L, 2L);
		final List<GeladenesObjekt> geladeneObjekte = List.of(new GeladenesObjekt(1L));
		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereIds(logger, idsUebergeben, geladeneObjekte, GeladenesObjekt::id, ID_TYP, MELDUNG_UNVOLLSTAENDIG));
		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals(MELDUNG_UNVOLLSTAENDIG, aoe.getBody());
		assertEquals(List.of(MELDUNG_UNVOLLSTAENDIG), fehlermeldungenImLog());
	}


	// ##### Gruppe B: ID-Validierung über die Map-Variante #####

	@Test
	void testVollstaendigBefuellteMapEintraegeSindGueltig() {
		assertDoesNotThrow(() -> HtmlContextValidierung.validiereIds(logger, List.of(1L, 2L), Map.of(1L, "a", 2L, "b"), ID_TYP, MELDUNG_UNVOLLSTAENDIG));
		assertEquals(List.of(), fehlermeldungenImLog());
	}

	@Test
	void testEinFehlenderMapEintragWirdAbgewiesen() {
		final List<Long> idsUebergeben = List.of(1L, 2L);
		final Map<Long, String> geladeneObjekte = Map.of(1L, "a");
		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereIds(logger, idsUebergeben, geladeneObjekte, ID_TYP, MELDUNG_UNVOLLSTAENDIG));
		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals(MELDUNG_UNVOLLSTAENDIG, aoe.getBody());
		assertEquals(List.of(MELDUNG_UNVOLLSTAENDIG), fehlermeldungenImLog());
	}

	@Test
	void testEinMapEintragMitWertNullGiltAlsNichtVorhanden() {
		// Der Null-Wert ist der heutige Fehler-Marker der Repositories. Er darf nicht als geladener Datensatz durchgehen.
		final Map<Long, String> geladeneObjekte = new HashMap<>();
		geladeneObjekte.put(1L, null);
		final List<Long> idsUebergeben = List.of(1L);
		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereIds(logger, idsUebergeben, geladeneObjekte, ID_TYP, MELDUNG_UNVOLLSTAENDIG));
		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals(MELDUNG_UNVOLLSTAENDIG, aoe.getBody());
		assertEquals(List.of(MELDUNG_UNVOLLSTAENDIG), fehlermeldungenImLog());
	}

	@Test
	void testEineLeereIdListeWirdAuchBeiDerMapVarianteAbgewiesen() {
		final List<Long> idsUebergeben = List.of();
		final Map<Long, String> geladeneObjekte = Map.of(1L, "a");
		final ApiOperationException aoe = assertThrows(ApiOperationException.class,
				() -> HtmlContextValidierung.validiereIds(logger, idsUebergeben, geladeneObjekte, ID_TYP, MELDUNG_UNVOLLSTAENDIG));
		assertEquals(Status.BAD_REQUEST, aoe.getStatus());
		assertEquals(MELDUNG_LEER, aoe.getBody());
		assertEquals(List.of(MELDUNG_LEER), fehlermeldungenImLog());
	}

}
