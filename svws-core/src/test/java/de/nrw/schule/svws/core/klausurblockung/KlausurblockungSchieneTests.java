package de.nrw.schule.svws.core.klausurblockung;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.nrw.schule.svws.core.data.klausurblockung.KlausurblockungSchienenInput;
import de.nrw.schule.svws.core.data.klausurblockung.KlausurblockungSchienenInputSchueler;
import de.nrw.schule.svws.core.data.klausurblockung.KlausurblockungSchienenOutput;
import de.nrw.schule.svws.core.data.klausurblockung.KlausurblockungSchienenOutputKlausur;
import de.nrw.schule.svws.core.data.klausurblockung.KlausurblockungSchienenOutputs;
import de.nrw.schule.svws.csv.CsvReader;
import de.nrw.schule.svws.logger.LogData;
import de.nrw.schule.svws.logger.LogLevel;
import de.nrw.schule.svws.logger.Logger;
import jakarta.validation.constraints.NotNull;

/** Diese Klasse testet die Klasse {@link KlausurblockungSchienenAlgorithmus}. */
@DisplayName("Diese Klasse testet die Klasse KlausurblockungSchienenAlgorithmus")
@TestMethodOrder(MethodOrderer.MethodName.class)
public class KlausurblockungSchieneTests {

	private static final long BLOCKUNGS_ZEIT = 1000 * 1;
	
	private static final String PFAD_DATEN_001 = "de/nrw/schule/svws/core/klausurblockung/blockungschiene001/";
	private static final String PFAD_DATEN_002 = "de/nrw/schule/svws/core/klausurblockung/blockungschiene002/";
	private static final String PFAD_DATEN_003 = "de/nrw/schule/svws/core/klausurblockung/blockungschiene003/";


	/** Initialisiert den Test. Die Methode ist leer. */
	@BeforeAll
	static void setup() {
	}

	/** Testet das Einlesen und Konvertieren der Daten 001. Diese befinden sich hier {@link #PFAD_DATEN_001}. */
	@Test
	@DisplayName("Klausurblockung 001 einlesen und blocken.")
	void test001_data() {
		// Erzeugen eines Loggers mit Consumer.
		Logger log = new Logger();

		// Hinzufügen des Consumers, der im kritischen Fall 'fail' aufruft.
		log.addConsumer(new Consumer<LogData>() {
			@Override
			public void accept(LogData t) {
				if (t.getLevel().compareTo(LogLevel.APP) != 0)
					fail(t.getText());
			}
		});

		// Einlesen der Kurs-Datensätze
		HashMap<Integer, EsserFormatKurs> mapKurs = new HashMap<>();
		for (EsserFormatKurs kurs : CsvReader.fromResource(PFAD_DATEN_001 + "kurs.txt", EsserFormatKurs.class))
			mapKurs.put(kurs.id, kurs);

		// Einlesen der SuS-Datensätze
		HashMap<Integer, EsserFormatSchueler> mapSuS = new HashMap<>();
		for (EsserFormatSchueler schueler : CsvReader.fromResource(PFAD_DATEN_001 + "schueler.txt",
				EsserFormatSchueler.class))
			mapSuS.put(schueler.id, schueler);

		// Einlesen der SuS-Fachwahl-Datensätze (nur diejenigen, die es schriftlich haben.
		HashMap<Integer, LinkedList<EsserFormatSchueler>> mapKursSuS1 = new HashMap<>();
		HashMap<Integer, LinkedList<EsserFormatSchueler>> mapKursSuS2 = new HashMap<>();
		for (EsserFormatFachwahl fachwahl : CsvReader.fromResource(PFAD_DATEN_001 + "fachwahl.txt",
				EsserFormatFachwahl.class)) {
			if (mapKursSuS1.get(fachwahl.kurs) == null)
				mapKursSuS1.put(fachwahl.kurs, new LinkedList<>());
			if (mapKursSuS2.get(fachwahl.kurs) == null)
				mapKursSuS2.put(fachwahl.kurs, new LinkedList<>());

			EsserFormatSchueler schueler = mapSuS.get(fachwahl.schueler);
			if (fachwahl.schriftlich > 1)
				mapKursSuS1.get(fachwahl.kurs).addLast(schueler);
			if ((fachwahl.schriftlich == 3) || (fachwahl.schriftlich == 5))
				mapKursSuS2.get(fachwahl.kurs).addLast(schueler);
		}

		// Einlesen der KData-Datensätze
		HashMap<Integer, EsserFormatKData> mapKData = new HashMap<>();
		for (EsserFormatKData kdata : CsvReader.fromResource(PFAD_DATEN_001 + "kdata.txt", EsserFormatKData.class))
			mapKData.put(kdata.id, kdata);

		// Einlesen der Klausur-Datensätze
		HashMap<Integer, EsserFormatKlausur> mapKlausur = new HashMap<>();
		HashMap<Integer, EsserFormatKurs> mapKlausurZuKurs = new HashMap<>();
		HashMap<Integer, HashMap<Integer, HashMap<String, LinkedList<EsserFormatKlausur>>>> mapQuartalZuKlausuren = new HashMap<>();
		for (EsserFormatKlausur klausur : CsvReader.fromResource(PFAD_DATEN_001 + "klausur.txt",
				EsserFormatKlausur.class)) {
			mapKlausur.put(klausur.id, klausur);

			EsserFormatKurs kurs = mapKurs.get(klausur.kurs);
			mapKlausurZuKurs.put(klausur.id, kurs);

			EsserFormatKData kdata = mapKData.get(klausur.kdata);

			if (mapQuartalZuKlausuren.get(kdata.halbjahr) == null)
				mapQuartalZuKlausuren.put(kdata.halbjahr, new HashMap<>());
			if (mapQuartalZuKlausuren.get(kdata.halbjahr).get(kdata.klausnr) == null)
				mapQuartalZuKlausuren.get(kdata.halbjahr).put(kdata.klausnr, new HashMap<>());
			if (mapQuartalZuKlausuren.get(kdata.halbjahr).get(kdata.klausnr).get(kdata.stufe) == null)
				mapQuartalZuKlausuren.get(kdata.halbjahr).get(kdata.klausnr).put(kdata.stufe, new LinkedList<>());

			mapQuartalZuKlausuren.get(kdata.halbjahr).get(kdata.klausnr).get(kdata.stufe).addLast(klausur);
		}

		// Quartale durchgehen und blocken.
		for (int halbjahr : mapQuartalZuKlausuren.keySet())
			for (int klausnr : mapQuartalZuKlausuren.get(halbjahr).keySet())
				for (String stufe : mapQuartalZuKlausuren.get(halbjahr).get(klausnr).keySet()) {
					// Was war die Mindestanzahl an Terminen bis jetzt?
					TreeSet<Integer> termine = new TreeSet<>();
					for (EsserFormatKlausur klausur : mapQuartalZuKlausuren.get(halbjahr).get(klausnr).get(stufe))
						termine.add(klausur.termin);

					// Welche Klausuren müssen geschrieben werden?
					LinkedList<EsserFormatKlausur> klausuren = mapQuartalZuKlausuren.get(halbjahr).get(klausnr)
							.get(stufe);

					// Blockungsalgorithmus...
					klausurblockung(halbjahr, klausnr, stufe, termine.size(), klausuren, mapKursSuS1, mapKursSuS2);

				}

	}

	private void klausurblockung(int halbjahr, int klausnr, String stufe, int termine,
			LinkedList<EsserFormatKlausur> klausuren,
			HashMap<Integer, LinkedList<EsserFormatSchueler>> mapKursSuSschriftlich1,
			HashMap<Integer, LinkedList<EsserFormatSchueler>> mapKursSuSschriftlich2) {

		if (stufe.equals("EF"))
			return;

		// System.out.println();
		// System.out.println(halbjahr + "," + klausnr + "," + stufe + " --> " + klausuren.size() + " Klausuren, auf " +
		// termine + " Termine");

		// Input-Erzeugen
		KlausurblockungSchienenInput input = new KlausurblockungSchienenInput();
		input.datenbankID = halbjahr * 10 + klausnr;
		input.maxTimeMillis = BLOCKUNGS_ZEIT;
		// System.out.println(" Starte Blockung mit 10 Sekunden.");

		HashMap<Integer, KlausurblockungSchienenInputSchueler> mapInputSchueler = new HashMap<>();
		for (EsserFormatKlausur klausur : klausuren) {
			// Wähle die richtige Map.
			HashMap<Integer, LinkedList<EsserFormatSchueler>> map = mapKursSuSschriftlich1;
			if (stufe.equals("Q2") && (halbjahr == 2))
				map = mapKursSuSschriftlich2;
			// Erzeuge KlausurblockungSchienenInput.schueler.klausuren
			for (EsserFormatSchueler schueler : map.get(klausur.kurs)) {
				if (mapInputSchueler.get(schueler.id) == null) {
					KlausurblockungSchienenInputSchueler iSchueler = new KlausurblockungSchienenInputSchueler();
					iSchueler.id = schueler.id;
					mapInputSchueler.put(schueler.id, iSchueler);
					input.schueler.add(iSchueler);
				}
				mapInputSchueler.get(schueler.id).klausuren.add((long) klausur.id);
			}
		}

		starteKlausurblockungSchiene(input);
	}

	/** Testet das Einlesen und Konvertieren der Daten 002. Diese befinden sich hier {@link #PFAD_DATEN_002}. */
	@Test
	@DisplayName("Klausurblockung 002 einlesen und blocken.")
	void test002_data() {
		// Erzeugen eines Loggers mit Consumer.
		Logger log = new Logger();

		// Hinzufügen des Consumers, der im kritischen Fall 'fail' aufruft.
		log.addConsumer(new Consumer<LogData>() {
			@Override
			public void accept(LogData t) {
				if (t.getLevel().compareTo(LogLevel.APP) != 0)
					fail(t.getText());
			}
		});

		// Einlesen der Kurs-Datensätze
		TreeMap<String, HashMap<Long, LinkedList<Long>>> map = new TreeMap<>();
		for (PluemperFormatStufeSchuelerKurs daten : CsvReader.fromResource(PFAD_DATEN_002 + "StufeSchuelerKurs.txt",
				PluemperFormatStufeSchuelerKurs.class)) {

			if (map.get(daten.stufe) == null)
				map.put(daten.stufe, new HashMap<>());

			if (map.get(daten.stufe).get(daten.schuelerid) == null)
				map.get(daten.stufe).put(daten.schuelerid, new LinkedList<>());

			map.get(daten.stufe).get(daten.schuelerid).push(daten.kursid);
		}

		// Blocken pro Stufe
		for (String stufe : map.keySet()) {
			KlausurblockungSchienenInput input = new KlausurblockungSchienenInput();
			input.datenbankID = 1;
			input.maxTimeMillis = BLOCKUNGS_ZEIT;

			for (Long schuelerID : map.get(stufe).keySet()) {
				KlausurblockungSchienenInputSchueler schueler = new KlausurblockungSchienenInputSchueler();
				schueler.id = schuelerID;
				for (Long klausurID : map.get(stufe).get(schuelerID))
					schueler.klausuren.add(klausurID);
				input.schueler.add(schueler);
			}

			starteKlausurblockungSchiene(input);
		}

	}

	/** Testet das Einlesen und Konvertieren der Daten 003. Diese befinden sich hier {@link #PFAD_DATEN_003}. */
	@Test
	@DisplayName("Klausurblockung 003 einlesen und blocken.")
	void test003_data() {
		// Erzeugen eines Loggers mit Consumer.
		Logger log = new Logger();

		// Hinzufügen des Consumers, der im kritischen Fall 'fail' aufruft.
		log.addConsumer(new Consumer<LogData>() {
			@Override
			public void accept(LogData t) {
				if (t.getLevel().compareTo(LogLevel.APP) != 0)
					fail(t.getText());
			}
		});

		// Einlesen der Kurs-Datensätze
		TreeMap<String, HashMap<Long, LinkedList<Long>>> map = new TreeMap<>();
		for (PluemperFormatStufeSchuelerKurs daten : CsvReader.fromResource(PFAD_DATEN_003 + "Westphal_EF.txt",
				PluemperFormatStufeSchuelerKurs.class)) {

			if (map.get(daten.stufe) == null)
				map.put(daten.stufe, new HashMap<>());

			if (map.get(daten.stufe).get(daten.schuelerid) == null)
				map.get(daten.stufe).put(daten.schuelerid, new LinkedList<>());

			map.get(daten.stufe).get(daten.schuelerid).push(daten.kursid);
		}

		// Blocken pro Stufe
		for (String stufe : map.keySet()) {
			// System.out.println("Stufe " + stufe);

			KlausurblockungSchienenInput input = new KlausurblockungSchienenInput();
			input.datenbankID = 1;
			input.maxTimeMillis = BLOCKUNGS_ZEIT;

			for (Long schuelerID : map.get(stufe).keySet()) {
				KlausurblockungSchienenInputSchueler schueler = new KlausurblockungSchienenInputSchueler();
				schueler.id = schuelerID;
				for (Long klausurID : map.get(stufe).get(schuelerID))
					schueler.klausuren.add(klausurID);
				input.schueler.add(schueler);
			}

			starteKlausurblockungSchiene(input);
		}

	}

	
	private void starteKlausurblockungSchiene(KlausurblockungSchienenInput input) {
		// Algorithmus-Objekt erzeugen.
		KlausurblockungSchienenAlgorithmus alg = new KlausurblockungSchienenAlgorithmus();

		// Blockung starten (Service).
		KlausurblockungSchienenOutputs outputs = alg.handle(input);

		// Gibt es Ergebnisse?
		if (outputs.ergebnisse.isEmpty())
			fail("'KlausurblockungSchienenOutputs.klausuren' ist leer.");

		int max = 0;
		for (KlausurblockungSchienenInputSchueler schueler : input.schueler)
			if (schueler.klausuren.size() > max)
				max = schueler.klausuren.size();

		// Jedes Ergebnis überprüfen.
		for (KlausurblockungSchienenOutput output : outputs.ergebnisse)
			check(input, output);

	}

	private void check(KlausurblockungSchienenInput input, KlausurblockungSchienenOutput output) {
		// Überprüfe 'output.datenbankID'.
		if (output.datenbankID < 0)
			fail("'KlausurblockungSchienenOutput.datenbankID' ist negativ --> " + output.datenbankID);

		// Überprüfe 'output.schienenAnzahl'.
		if (output.schienenAnzahl < 0)
			fail("'KlausurblockungSchienenOutput.schienenAnzahl' ist negativ --> " + output.datenbankID);

		// Überprüfe 'output.klausuren'.
		if (output.klausuren.isEmpty())
			fail("'KlausurblockungSchienenOutput.klausuren' ist leer.");

		// Überprüfe jede einzelne Klausur...
		HashMap<Long, Integer> mapKlausurZuSchiene = new HashMap<>();
		for (KlausurblockungSchienenOutputKlausur klausur : output.klausuren) {
			// Überprüfe 'klausur.id'.
			if (klausur.id < 0)
				fail("'KlausurblockungSchienenOutputKlausur.id' ist negativ --> " + klausur.id);

			// Überprüfe 'klausur.schiene'.
			if (klausur.schiene < 0)
				fail("'KlausurblockungSchienenOutputKlausur.schiene' ist zu klein --> " + klausur.schiene);

			// Überprüfe 'klausur.schiene'.
			if (klausur.schiene >= output.schienenAnzahl)
				fail("'KlausurblockungSchienenOutputKlausur.schiene' ist zu groß --> " + klausur.schiene);

			// Map füllen.
			if (mapKlausurZuSchiene.containsKey(klausur.id))
				fail("'KlausurblockungSchienenOutputKlausur' --> Zwei Klausuren haben die selbe ID.");
			mapKlausurZuSchiene.put(klausur.id, klausur.schiene);
		}

		// Überprüfe die Konsistent von 'input' vs. 'output'.
		for (KlausurblockungSchienenInputSchueler schueler : input.schueler) {
			@NotNull Vector<@NotNull Long> klausuren = schueler.klausuren;
			for (int i1 = 0; i1 < klausuren.size(); i1++)
				for (int i2 = i1 + 1; i2 < klausuren.size(); i2++) {
					// Pro Klausur-Paar des Schülers müssen die Klausur-Schienen verschieden sein.
					Integer schiene1 = mapKlausurZuSchiene.get(klausuren.get(i1));
					Integer schiene2 = mapKlausurZuSchiene.get(klausuren.get(i2));

					if (schiene1 == null)
						fail("Klausur " + klausuren.get(i1) + " hat keine zugeordnete Schiene!");

					if (schiene2 == null)
						fail("Klausur " + klausuren.get(i2) + " hat keine zugeordnete Schiene!");

					if (schiene1.equals(schiene2))
						fail("Schüler " + schueler.id + " hat zwei Klausuruen in der selben Schiene.");
				}
		}
		// System.out.println(" Output hat " + output.schienenAnzahl + " Schienen");
	}

}
