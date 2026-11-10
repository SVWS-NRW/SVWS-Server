package de.svws_nrw.asd.types.schueler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;

/**
 * Diese Klasse stellt JUnit-Tests für die Core-Types {@link HerkunftSonstige}, {@link HerkunftBildungsgang}
 * und {@link HerkunftSchulform} zur Verfügung, welche gemeinsam in die Datenbank-Tabellen Herkunft, Herkunft_Keys und
 * Herkunft_Schulformen sowie in den API-Katalog der Herkünfte übernommen werden. Dort wird zu der ID des jeweiligen
 * Core-Type-Eintrags ein fester Offset addiert, sodass die IDs über alle drei Core-Types hinweg eindeutig sind.
 * Die Tests stellen sicher, dass die Daten in den JSON-Dateien diese Anforderungen (Eindeutigkeit, Wertebereiche und
 * Spaltenbreiten) einhalten, sodass es bei Änderungen an den JSON-Dateien nicht zu unbemerkten Fehlern kommt.
 */
@DisplayName("Teste die gemeinsam genutzten Katalogwerte der Herkunft-Core-Types")
class TestHerkuenfteGemeinsameKatalogwerte {

	/** Der Offset für die IDs von {@link HerkunftSonstige} (siehe DBCoreTypeUpdater und DataKatalogHerkuenfte). */
	private static final long OFFSET_SONSTIGE = 1_000_000_000L;

	/** Der Offset für die IDs von {@link HerkunftBildungsgang} (siehe DBCoreTypeUpdater und DataKatalogHerkuenfte). */
	private static final long OFFSET_BILDUNGSGANG = 2_000_000_000L;

	/** Der Offset für die IDs von {@link HerkunftSchulform} (siehe DBCoreTypeUpdater und DataKatalogHerkuenfte). */
	private static final long OFFSET_SCHULFORM = 4_000_000_000L;

	/** Der kleinste Abstand zwischen zwei Offsets. Die ID eines Eintrags muss kleiner sein, damit sie nicht in den Bereich eines anderen Core-Types reicht. */
	private static final long OFFSET_ABSTAND = 1_000_000_000L;

	/** Die Länge der Spalten Herkunft.Kuerzel, Herkunft_Keys.Kuerzel und Herkunft_Schulformen.Schulform_Kuerzel */
	private static final int MAX_LAENGE_TABELLE_KUERZEL = 10;

	/** Die Länge der Spalte Herkunft.Beschreibung */
	private static final int MAX_LAENGE_TABELLE_BESCHREIBUNG = 255;

	/** Die Länge der Spalte Schueler.LSSchulformSIM, in welcher je nach Kontext Kürzel bzw. Schlüssel der Herkünfte gespeichert werden */
	private static final int MAX_LAENGE_LS_SCHULFORM_SIM = 3;

	/** Die Länge der Spalte Schueler.LSSchulform, in welcher das Kürzel von {@link HerkunftSchulform} gespeichert wird */
	private static final int MAX_LAENGE_LS_SCHULFORM = 2;


	/**
	 * Ein Eintrag aus einem der drei Core-Types in der für die Tests nötigen Form.
	 *
	 * @param typ          der Name des Core-Types
	 * @param bezeichner   der Bezeichner des Core-Type-Wertes
	 * @param offset       der Offset, der bei der Übernahme in die Datenbank auf die ID addiert wird
	 * @param eintrag      der Historieneintrag
	 * @param schulformen  die Bezeichner der Schulformen, bei denen der Eintrag zulässig ist
	 */
	private record Herkunft(String typ, String bezeichner, long offset, CoreTypeData eintrag, List<String> schulformen) {

		/** @return die ID, wie sie in der Datenbank und im API-Katalog verwendet wird */
		long idMitOffset() {
			return eintrag.id + offset;
		}

		/** @return eine Beschreibung des Eintrags für Fehlermeldungen */
		String beschreibung() {
			return typ + "." + bezeichner + " (ID " + eintrag.id + ", Schlüssel " + eintrag.schluessel + ")";
		}

	}


	/** Die Einträge aller drei Core-Types */
	private static final List<Herkunft> ALLE = new ArrayList<>();


	/**
	 * Initialisiert die Core-Types und sammelt die Einträge der drei Core-Types.
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
		for (final HerkunftSonstige wert : HerkunftSonstige.values()) {
			wert.historie().forEach(e -> ALLE.add(new Herkunft("HerkunftSonstige", wert.name(), OFFSET_SONSTIGE, e, e.schulformen)));
		}
		for (final HerkunftBildungsgang wert : HerkunftBildungsgang.values()) {
			wert.historie().forEach(e -> ALLE.add(new Herkunft("HerkunftBildungsgang", wert.name(), OFFSET_BILDUNGSGANG, e,
					e.zulaessig.stream().map(z -> z.schulform).toList())));
		}
		for (final HerkunftSchulform wert : HerkunftSchulform.values()) {
			wert.historie().forEach(e -> ALLE.add(new Herkunft("HerkunftSchulform", wert.name(), OFFSET_SCHULFORM, e, e.schulformen)));
		}
	}


	/**
	 * Test der Core-Types HerkunftSonstige, HerkunftBildungsgang und HerkunftSchulform
	 *
	 * Testfall: Die Sammlung der Einträge ist vollständig
	 * Ergebnis: Es sind Einträge aller drei Core-Types vorhanden
	 */
	@Test
	@DisplayName("Teste Herkünfte: Die Einträge aller drei Core-Types werden berücksichtigt.")
	void testHerkuenfte_EintraegeVollstaendig() {
		assertEquals(3, ALLE.stream().map(Herkunft::typ).distinct().count());
		assertEquals(HerkunftSonstige.values().length + HerkunftBildungsgang.values().length + HerkunftSchulform.values().length,
				ALLE.stream().map(h -> h.typ() + "." + h.bezeichner()).distinct().count());
	}


	/**
	 * Test der Core-Types HerkunftSonstige, HerkunftBildungsgang und HerkunftSchulform
	 *
	 * Testfall: Prüft die Eindeutigkeit der ID unter Berücksichtigung des Offsets über alle drei Core-Types
	 * Ergebnis: Keine ID (inkl. Offset) kommt mehrfach vor, da sie in der Datenbank der Primärschlüssel der Tabelle Herkunft ist
	 */
	@Test
	@DisplayName("Teste Herkünfte: Die ID inkl. Offset ist über alle drei Core-Types eindeutig.")
	void testHerkuenfte_IdMitOffsetEindeutig() {
		final Map<Long, Herkunft> mapIDs = new HashMap<>();
		for (final Herkunft h : ALLE) {
			final Herkunft vorhanden = mapIDs.put(h.idMitOffset(), h);
			assertTrue(vorhanden == null, "Die ID " + h.idMitOffset() + " kommt mehrfach vor: " + (vorhanden == null ? "" : vorhanden.beschreibung())
					+ " und " + h.beschreibung());
		}
	}


	/**
	 * Test der Core-Types HerkunftSonstige, HerkunftBildungsgang und HerkunftSchulform
	 *
	 * Testfall: Prüft den Wertebereich der IDs in den JSON-Dateien
	 * Ergebnis: Jede ID ist nicht negativ und kleiner als der Abstand der Offsets, sodass sie nicht in den ID-Bereich eines
	 *           anderen Core-Types reichen kann
	 */
	@Test
	@DisplayName("Teste Herkünfte: Die IDs liegen im Wertebereich, der durch die Offsets vorgegeben ist.")
	void testHerkuenfte_IdWertebereich() {
		for (final Herkunft h : ALLE) {
			assertTrue((h.eintrag().id >= 0) && (h.eintrag().id < OFFSET_ABSTAND),
					"Die ID liegt außerhalb des zulässigen Bereichs [0, " + OFFSET_ABSTAND + "): " + h.beschreibung());
		}
	}


	/**
	 * Test der Core-Types HerkunftSonstige, HerkunftBildungsgang und HerkunftSchulform
	 *
	 * Testfall: Prüft die Eindeutigkeit von Kürzel und Schlüssel innerhalb jedes Core-Types
	 * Ergebnis: Kürzel und Schlüssel gehören innerhalb eines Core-Types zu genau einem Wert, da der Zugriff darüber (z.B. beim
	 *           Rückmapping der in Schueler gespeicherten Kürzel) sonst nicht eindeutig ist
	 */
	@Test
	@DisplayName("Teste Herkünfte: Kürzel und Schlüssel sind innerhalb eines Core-Types eindeutig.")
	void testHerkuenfte_KuerzelUndSchluesselProCoreTypeEindeutig() {
		final Map<String, Set<String>> mapKuerzel = new HashMap<>();
		final Map<String, Set<String>> mapSchluessel = new HashMap<>();
		for (final Herkunft h : ALLE) {
			mapKuerzel.computeIfAbsent(h.typ() + "/" + h.eintrag().kuerzel, k -> new HashSet<>()).add(h.bezeichner());
			mapSchluessel.computeIfAbsent(h.typ() + "/" + h.eintrag().schluessel, k -> new HashSet<>()).add(h.bezeichner());
		}
		for (final Map.Entry<String, Set<String>> e : mapKuerzel.entrySet()) {
			assertEquals(1, e.getValue().size(), "Das Kürzel " + e.getKey() + " wird mehrfach verwendet: " + e.getValue());
		}
		for (final Map.Entry<String, Set<String>> e : mapSchluessel.entrySet()) {
			assertEquals(1, e.getValue().size(), "Der Schlüssel " + e.getKey() + " wird mehrfach verwendet: " + e.getValue());
		}
	}


	/**
	 * Test der Core-Types HerkunftSonstige, HerkunftBildungsgang und HerkunftSchulform
	 *
	 * Testfall: Prüft die Längen von Kürzel, Schlüssel und Text im Vergleich zu den Spaltenbreiten der Datenbank-Tabellen
	 * Ergebnis: Kürzel, Schlüssel und Text sind nicht leer und passen in die Spalten der Tabellen Herkunft und Herkunft_Keys
	 */
	@Test
	@DisplayName("Teste Herkünfte: Kürzel, Schlüssel und Text passen in die Spalten der Tabellen Herkunft und Herkunft_Keys.")
	void testHerkuenfte_LaengenTabellen() {
		for (final Herkunft h : ALLE) {
			assertFalse(h.eintrag().kuerzel.isBlank(), "Das Kürzel ist leer: " + h.beschreibung());
			assertFalse(h.eintrag().schluessel.isBlank(), "Der Schlüssel ist leer: " + h.beschreibung());
			assertFalse(h.eintrag().text.isBlank(), "Der Text ist leer: " + h.beschreibung());
			assertTrue(h.eintrag().kuerzel.length() <= MAX_LAENGE_TABELLE_KUERZEL, "Das Kürzel ist zu lang für die Tabelle Herkunft: " + h.beschreibung());
			assertTrue(h.eintrag().schluessel.length() <= MAX_LAENGE_TABELLE_KUERZEL,
					"Der Schlüssel ist zu lang für die Tabelle Herkunft_Keys: " + h.beschreibung());
			assertTrue(h.eintrag().text.length() <= MAX_LAENGE_TABELLE_BESCHREIBUNG,
					"Der Text ist zu lang für die Tabelle Herkunft: " + h.beschreibung());
		}
	}


	/**
	 * Test der Core-Types HerkunftSonstige, HerkunftBildungsgang und HerkunftSchulform
	 *
	 * Testfall: Prüft die Längen der Werte, die in der Tabelle Schueler (LSSchulformSIM bzw. LSSchulform) gespeichert werden
	 * Ergebnis: Das Kürzel bei HerkunftSonstige und HerkunftSchulform sowie der Schlüssel bei HerkunftBildungsgang (und der
	 *           Schlüssel bei HerkunftSchulform, welcher mit K_Schule.SchulformNr abgeglichen wird) haben maximal 3 Zeichen, das
	 *           Kürzel bei HerkunftSchulform (LSSchulform) maximal 2 Zeichen
	 */
	@Test
	@DisplayName("Teste Herkünfte: Die in Schueler gespeicherten Kürzel und Schlüssel passen in die Spalten LSSchulformSIM und LSSchulform.")
	void testHerkuenfte_LaengenSchueler() {
		for (final Herkunft h : ALLE) {
			if ("HerkunftBildungsgang".equals(h.typ())) {
				assertTrue(h.eintrag().schluessel.length() <= MAX_LAENGE_LS_SCHULFORM_SIM, "Der Schlüssel ist zu lang für LSSchulformSIM: " + h.beschreibung());
				continue;
			}
			assertTrue(h.eintrag().kuerzel.length() <= MAX_LAENGE_LS_SCHULFORM_SIM, "Das Kürzel ist zu lang für LSSchulformSIM: " + h.beschreibung());
			if ("HerkunftSchulform".equals(h.typ())) {
				assertTrue(h.eintrag().kuerzel.length() <= MAX_LAENGE_LS_SCHULFORM, "Das Kürzel ist zu lang für LSSchulform: " + h.beschreibung());
				assertTrue(h.eintrag().schluessel.length() <= MAX_LAENGE_LS_SCHULFORM_SIM,
						"Der Schlüssel ist zu lang für K_Schule.SchulformNr: " + h.beschreibung());
			}
		}
	}


	/**
	 * Test der Core-Types HerkunftSonstige, HerkunftBildungsgang und HerkunftSchulform
	 *
	 * Testfall: Prüft die Schulformen, die bei den Einträgen angegeben sind
	 * Ergebnis: Jede Schulform existiert im Core-Type Schulform, kommt pro Eintrag nur einmal vor (Primärschlüssel der Tabelle
	 *           Herkunft_Schulformen) und passt in die Spalte Schulform_Kuerzel
	 */
	@Test
	@DisplayName("Teste Herkünfte: Die Schulformen existieren, sind pro Eintrag eindeutig und passen in die Tabelle Herkunft_Schulformen.")
	void testHerkuenfte_Schulformen() {
		for (final Herkunft h : ALLE) {
			assertFalse(h.schulformen().isEmpty(), "Es sind keine Schulformen angegeben: " + h.beschreibung());
			assertEquals(h.schulformen().size(), new HashSet<>(h.schulformen()).size(), "Eine Schulform kommt mehrfach vor: " + h.beschreibung());
			for (final String schulform : h.schulformen()) {
				assertNotNull(Schulform.data().getWertByBezeichnerOrNull(schulform), "Die Schulform " + schulform + " existiert nicht: " + h.beschreibung());
				assertTrue(schulform.length() <= MAX_LAENGE_TABELLE_KUERZEL, "Die Schulform " + schulform + " ist zu lang: " + h.beschreibung());
			}
		}
	}

}
