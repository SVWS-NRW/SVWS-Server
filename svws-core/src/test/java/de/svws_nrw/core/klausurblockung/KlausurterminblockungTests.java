package de.svws_nrw.core.klausurblockung;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.svws_nrw.base.CsvReader;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungDaten;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungErgebnis;
import de.svws_nrw.core.data.gost.klausurplanung.GostKlausurterminblockungErgebnisTermin;
import de.svws_nrw.core.data.gost.klausurplanung.GostKursklausur;
import de.svws_nrw.core.utils.klausurplanung.KlausurblockungSchienenAlgorithmus;
import de.svws_nrw.core.utils.klausurplanung.KlausurterminblockungAlgorithmus;
import jakarta.validation.constraints.NotNull;

/** Diese Klasse testet die Klasse {@link KlausurblockungSchienenAlgorithmus}. */
@DisplayName("Diese Klasse testet die Klasse KlausurblockungSchienenAlgorithmus")
@TestMethodOrder(MethodOrderer.MethodName.class)
class KlausurterminblockungTests {

	private static final long BLOCKUNGS_ZEIT = 1000 * 1;

	private static final String PFAD_DATEN_001 = "de/svws_nrw/core/klausurblockung/blockungschiene001/";
	private static final String PFAD_DATEN_002 = "de/svws_nrw/core/klausurblockung/blockungschiene002/";
	private static final String PFAD_DATEN_003 = "de/svws_nrw/core/klausurblockung/blockungschiene003/";


	/** Testet das Einlesen und Konvertieren der Daten 001. Diese befinden sich hier {@link #PFAD_DATEN_001}. */
	@Test
	@DisplayName("Klausurblockung 001 einlesen und blocken.")
	void test001_data() {

		// Einlesen der Kurs-Datensätze
		final HashMap<Integer, EsserFormatKurs> mapKurs = new HashMap<>();
		for (final EsserFormatKurs kurs : CsvReader.fromResource(PFAD_DATEN_001 + "kurs.txt", EsserFormatKurs.class))
			mapKurs.put(kurs.id, kurs);

		// Einlesen der SuS-Datensätze
		final HashMap<Integer, EsserFormatSchueler> mapSuS = new HashMap<>();
		for (final EsserFormatSchueler schueler : CsvReader.fromResource(PFAD_DATEN_001 + "schueler.txt", EsserFormatSchueler.class))
			mapSuS.put(schueler.id, schueler);

		// Einlesen der SuS-Fachwahl-Datensätze (nur diejenigen, die es schriftlich haben).
		final HashMap<Integer, LinkedList<EsserFormatSchueler>> mapKursSuS1 = new HashMap<>();
		final HashMap<Integer, LinkedList<EsserFormatSchueler>> mapKursSuS2 = new HashMap<>();
		for (final EsserFormatFachwahl fachwahl : CsvReader.fromResource(PFAD_DATEN_001 + "fachwahl.txt", EsserFormatFachwahl.class)) {
			if (mapKursSuS1.get(fachwahl.kurs) == null)
				mapKursSuS1.put(fachwahl.kurs, new LinkedList<>());
			if (mapKursSuS2.get(fachwahl.kurs) == null)
				mapKursSuS2.put(fachwahl.kurs, new LinkedList<>());

			final EsserFormatSchueler schueler = mapSuS.get(fachwahl.schueler);
			if (fachwahl.schriftlich > 1)
				mapKursSuS1.get(fachwahl.kurs).addLast(schueler);
			if ((fachwahl.schriftlich == 3) || (fachwahl.schriftlich == 5))
				mapKursSuS2.get(fachwahl.kurs).addLast(schueler);
		}

		// Einlesen der KData-Datensätze
		final HashMap<Integer, EsserFormatKData> mapKData = new HashMap<>();
		for (final EsserFormatKData kdata : CsvReader.fromResource(PFAD_DATEN_001 + "kdata.txt", EsserFormatKData.class))
			mapKData.put(kdata.id, kdata);

		// Einlesen der Klausur-Datensätze
		final HashMap<Integer, EsserFormatKlausur> mapKlausur = new HashMap<>();
		final HashMap<Integer, EsserFormatKurs> mapKlausurZuKurs = new HashMap<>();
		final HashMap<Integer, HashMap<Integer, HashMap<String, LinkedList<EsserFormatKlausur>>>> mapQuartalZuKlausuren = new HashMap<>();
		for (final EsserFormatKlausur klausur : CsvReader.fromResource(PFAD_DATEN_001 + "klausur.txt", EsserFormatKlausur.class)) {
			mapKlausur.put(klausur.id, klausur);

			final EsserFormatKurs kurs = mapKurs.get(klausur.kurs);
			mapKlausurZuKurs.put(klausur.id, kurs);

			final EsserFormatKData kdata = mapKData.get(klausur.kdata);

			if (mapQuartalZuKlausuren.get(kdata.halbjahr) == null)
				mapQuartalZuKlausuren.put(kdata.halbjahr, new HashMap<>());
			if (mapQuartalZuKlausuren.get(kdata.halbjahr).get(kdata.klausnr) == null)
				mapQuartalZuKlausuren.get(kdata.halbjahr).put(kdata.klausnr, new HashMap<>());
			if (mapQuartalZuKlausuren.get(kdata.halbjahr).get(kdata.klausnr).get(kdata.stufe) == null)
				mapQuartalZuKlausuren.get(kdata.halbjahr).get(kdata.klausnr).put(kdata.stufe, new LinkedList<>());

			mapQuartalZuKlausuren.get(kdata.halbjahr).get(kdata.klausnr).get(kdata.stufe).addLast(klausur);
		}

		// Quartale durchgehen und blocken.
		for (final int halbjahr : mapQuartalZuKlausuren.keySet())
			for (final int klausnr : mapQuartalZuKlausuren.get(halbjahr).keySet())
				for (final String stufe : mapQuartalZuKlausuren.get(halbjahr).get(klausnr).keySet()) {
					// Was war die Mindestanzahl an Terminen bis jetzt?
					final TreeSet<Integer> termine = new TreeSet<>();
					for (final EsserFormatKlausur klausur : mapQuartalZuKlausuren.get(halbjahr).get(klausnr).get(stufe))
						termine.add(klausur.termin);

					// Welche Klausuren müssen geschrieben werden?
					final LinkedList<EsserFormatKlausur> klausuren = mapQuartalZuKlausuren.get(halbjahr).get(klausnr).get(stufe);

					// Blockungsalgorithmus...
					klausurblockung(halbjahr, stufe, klausuren, mapKursSuS1, mapKursSuS2);
				}

	}


	/** Testet das Einlesen und Konvertieren der Daten 002.
	 * Diese befinden sich hier {@link #PFAD_DATEN_002}.
	 */
	@Test
	@DisplayName("Klausurblockung 002 einlesen und blocken.")
	void test002_data() {

		// Einlesen der Kurs-Datensätze
		final TreeMap<String, HashMap<Long, LinkedList<Long>>> map = new TreeMap<>();
		for (final PluemperFormatStufeSchuelerKurs daten : CsvReader.fromResource(PFAD_DATEN_002 + "StufeSchuelerKurs.txt", PluemperFormatStufeSchuelerKurs.class)) {
			if (map.get(daten.stufe) == null)
				map.put(daten.stufe, new HashMap<>());
			if (map.get(daten.stufe).get(daten.schuelerid) == null)
				map.get(daten.stufe).put(daten.schuelerid, new LinkedList<>());
			map.get(daten.stufe).get(daten.schuelerid).push(daten.kursid);
		}

		// Blocken pro Stufe
		for (final String stufe : map.keySet()) {
			// Input-Erzeugen
			@NotNull
			final List<@NotNull GostKursklausur> input = new ArrayList<>();


			final HashMap<Long, GostKursklausur> mapKlausur = new HashMap<>();
			for (final long schuelerID : map.get(stufe).keySet()) {

				for (final long klausurID : map.get(stufe).get(schuelerID)) {
					if (!mapKlausur.containsKey(klausurID)) {
						final GostKursklausur gostKlausur = new GostKursklausur();
						gostKlausur.id = klausurID;
						mapKlausur.put(klausurID, gostKlausur);
						input.add(gostKlausur);
					}
					mapKlausur.get(klausurID).schuelerIds.add(schuelerID);
				}
			}

			starteKlausurblockungSchiene(input);
		}

	}

	/** Testet das Einlesen und Konvertieren der Daten 003. Diese befinden sich hier {@link #PFAD_DATEN_003}. */
	@Test
	@DisplayName("Klausurblockung 003 einlesen und blocken.")
	void test003_data() {

		// Einlesen der Kurs-Datensätze
		final TreeMap<String, HashMap<Long, LinkedList<Long>>> map = new TreeMap<>();
		for (final PluemperFormatStufeSchuelerKurs daten : CsvReader.fromResource(PFAD_DATEN_003 + "Westphal_EF.txt", PluemperFormatStufeSchuelerKurs.class)) {

			if (map.get(daten.stufe) == null)
				map.put(daten.stufe, new HashMap<>());

			if (map.get(daten.stufe).get(daten.schuelerid) == null)
				map.get(daten.stufe).put(daten.schuelerid, new LinkedList<>());

			map.get(daten.stufe).get(daten.schuelerid).push(daten.kursid);
		}

		// Blocken pro Stufe
		for (final String stufe : map.keySet()) {
			// Input-Erzeugen
			@NotNull
			final List<@NotNull GostKursklausur> input = new ArrayList<>();

			final HashMap<Long, GostKursklausur> mapKlausur = new HashMap<>();
			for (final long schuelerID : map.get(stufe).keySet()) {
				for (final long klausurID : map.get(stufe).get(schuelerID)) {
					if (!mapKlausur.containsKey(klausurID)) {
						final GostKursklausur gostKlausur = new GostKursklausur();
						gostKlausur.id = klausurID;
						mapKlausur.put(klausurID, gostKlausur);
						input.add(gostKlausur);
					}
					mapKlausur.get(klausurID).schuelerIds.add(schuelerID);
				}
			}

			starteKlausurblockungSchiene(input);
		}

	}


	private static void klausurblockung(final int halbjahr, final String stufe,
			final LinkedList<EsserFormatKlausur> klausuren,
			final HashMap<Integer, LinkedList<EsserFormatSchueler>> mapKursSuSschriftlich1,
			final HashMap<Integer, LinkedList<EsserFormatSchueler>> mapKursSuSschriftlich2) {

		if (stufe.equals("EF"))
			return;

		// System.out.println();
		// System.out.println(halbjahr + "," + klausnr + "," + stufe + " --> " + klausuren.size() + " Klausuren, auf " +
		// termine + " Termine");

		// Wähle die richtige Map.
		HashMap<Integer, LinkedList<EsserFormatSchueler>> mapSchriftlich = mapKursSuSschriftlich1;
		if (stufe.equals("Q2") && (halbjahr == 2))
			mapSchriftlich = mapKursSuSschriftlich2;

		// Input-Erzeugen
		@NotNull
		final List<@NotNull GostKursklausur> input = new ArrayList<>();

		// Für alle Klausuren ...
		for (final EsserFormatKlausur klausur : klausuren) {
			@NotNull
			final GostKursklausur gostKlausur = new GostKursklausur();
			gostKlausur.id = klausur.id;

			// Für alle schriftlichen Schüler ...
			for (final EsserFormatSchueler schueler : mapSchriftlich.get(klausur.kurs)) {
				gostKlausur.schuelerIds.add(Long.parseLong("" + schueler.id));
			}

			input.add(gostKlausur);
		}

		starteKlausurblockungSchiene(input);
	}


	private static void starteKlausurblockungSchiene(@NotNull final List<@NotNull GostKursklausur> pInput) {
		// Algorithmus-Objekt erzeugen.
		final @NotNull KlausurterminblockungAlgorithmus alg = new KlausurterminblockungAlgorithmus();

		final @NotNull GostKlausurterminblockungDaten daten = new GostKlausurterminblockungDaten();
		daten.konfiguration.maxTimeMillis = BLOCKUNGS_ZEIT;
		daten.klausuren = pInput;

		// Blockung starten
		final @NotNull GostKlausurterminblockungErgebnis ergebnis = alg.apply(daten);

		// Gibt es Ergebnisse?
		assert !ergebnis.termine.isEmpty() : "'KlausurblockungSchienenOutputs.klausuren' ist leer.";


		// Ergebnis überprüfen.
		check(daten, ergebnis);
	}

	private static void check(@NotNull final GostKlausurterminblockungDaten daten, @NotNull final GostKlausurterminblockungErgebnis ergebnis) {

		// Map: Klausur-ID --> Klausur-Objekt
		final HashMap<@NotNull Long, @NotNull GostKursklausur> mapKlausur = new HashMap<>();
		for (@NotNull final GostKursklausur klausur : daten.klausuren) {
			mapKlausur.put(klausur.id, klausur);
		}

		for (final @NotNull GostKlausurterminblockungErgebnisTermin termin : ergebnis.termine) {
			final TreeSet<Long> schueler = new TreeSet<>();
			for (final long klausurID : termin.kursklausuren) {
				for (final long susID : mapKlausur.get(klausurID).schuelerIds) {
					if (!schueler.add(susID)) {
						fail("Doppelter Schüler an einem Termin!");
					}
				}
			}
		}

	}


}
