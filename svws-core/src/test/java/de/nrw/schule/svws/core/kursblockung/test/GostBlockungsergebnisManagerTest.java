package de.nrw.schule.svws.core.kursblockung.test;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import java.util.function.Consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.nrw.schule.svws.core.data.gost.GostBlockungKurs;
import de.nrw.schule.svws.core.data.gost.GostBlockungsergebnisKurs;
import de.nrw.schule.svws.core.data.gost.GostBlockungsergebnisSchiene;
import de.nrw.schule.svws.core.data.gost.GostFachwahl;
import de.nrw.schule.svws.core.kursblockung.KursblockungAlgorithmus;
import de.nrw.schule.svws.core.logger.LogData;
import de.nrw.schule.svws.core.logger.LogLevel;
import de.nrw.schule.svws.core.logger.Logger;
import de.nrw.schule.svws.core.types.gost.GostKursart;
import de.nrw.schule.svws.core.utils.gost.GostBlockungsdatenManager;
import de.nrw.schule.svws.core.utils.gost.GostBlockungsergebnisManager;

/** 
 * Testet den {@link GostBlockungsergebnisManager}. 
 */
@DisplayName("Testet den {@link GostBlockungsergebnisManager}.")
@TestMethodOrder(MethodOrderer.MethodName.class)
public class GostBlockungsergebnisManagerTest {

	private static final String PFAD_DATEN_001 = "de/nrw/schule/svws/core/kursblockung/blockung001/";
	private static final String PFAD_DATEN_002 = "de/nrw/schule/svws/core/kursblockung/blockung002/";

	/** 
	 * Testet das Einlesen und Konvertieren der Daten 001. Diese befinden sich hier {@link #PFAD_DATEN_001}. 
	 */
	@Test
	@DisplayName("Daten 001 einlesen.")
	void test001_data() {
		// Der Kursblockungsalgorithmus ist ein Service.
		KursblockungAlgorithmus kbAlgorithmus = new KursblockungAlgorithmus();

		// Logger vom Service übernehmen
		Logger log = kbAlgorithmus.getLogger();

		// Consumer triggert 'fail', wenn etwas kritisches geloggt wurde.
		log.addConsumer(new Consumer<LogData>() {

			@Override
			public void accept(LogData t) {
				if (t.getLevel().compareTo(LogLevel.APP) != 0)
					fail(t.getText());
			}
		});

		// Einlesen der Kurs42-Daten aus den Textdateien
		long maxTimeMillis = 999;
		Kurs42Converter k42Converter = new Kurs42Converter(log, PFAD_DATEN_001, maxTimeMillis, false);
		GostBlockungsdatenManager input = k42Converter.gibKursblockungInput();
		GostBlockungsergebnisManager out = new GostBlockungsergebnisManager(input, 1L);

		/// GostBlockungsergebnisManager dynamisch testen. 
		teste_in_out(input, out);
	}
	
	/** Testet das Einlesen und Konvertieren der Daten 002. Diese befinden sich hier {@link #PFAD_DATEN_002}. */
	@Test
	@DisplayName("Daten 002 einlesen.")
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

		// Einlesen der Kurs42-Daten aus den Textdateien
		long maxTimeMillis = 1000 * 1;
		Kurs42Converter k42Converter = new Kurs42Converter(log, PFAD_DATEN_002, maxTimeMillis, false);
		GostBlockungsdatenManager input = k42Converter.gibKursblockungInput();
		GostBlockungsergebnisManager out = new GostBlockungsergebnisManager(input, 1L);

		/// GostBlockungsergebnisManager dynamisch testen. 
		teste_in_out(input, out);
	}

	private static void teste_in_out(GostBlockungsdatenManager input, GostBlockungsergebnisManager out) {
		// Die Kurs-Map erstellen.
		HashMap<Long, HashSet<Long>> mapFaKu = new HashMap<>();
		HashMap<Long, HashSet<Long>> mapKuSchiene = new HashMap<>();
		for (GostBlockungKurs gKurs : input.daten().kurse) {
			// mapFaKu
			long fachartID = GostKursart.getFachartIDByKurs(gKurs);
			HashSet<Long> setK = mapFaKu.get(fachartID);
			if (setK == null) {
				setK = new HashSet<>();
				if (mapFaKu.put(fachartID, setK) != null)
					fail("Doppelte Fachart-ID!");
			}
			if (setK.add(gKurs.id) == false)
				fail("Fachart " + fachartID + " hat den Kurs bereits " + gKurs.id);

			// mapKuSchiene
			mapKuSchiene.put(gKurs.id, new HashSet<>());
		}

		// Schüler-ID --> Fachart-ID --> Kurs-ID
		HashMap<Long, HashMap<Long, Long>> mapScFaKu = new HashMap<>();
		for (GostFachwahl gFachwahl : input.daten().fachwahlen) {
			HashMap<Long, Long> mapFW = mapScFaKu.get(gFachwahl.schuelerID);
			if (mapFW == null) {
				mapFW = new HashMap<>();
				mapScFaKu.put(gFachwahl.schuelerID, mapFW);
			}
			long fachartID = GostKursart.getFachartIDByFachwahl(gFachwahl);
			if (mapFW.put(fachartID, null) != null)
				fail("Schüler-ID " + gFachwahl.schuelerID + ", Fachart-ID " + fachartID + " doppelt!");
		}

		Random lRandom = new Random(1);

		// System.out.println(out.getOfBewertung1Wert() + "/" + out.getOfBewertung1Farbcode());
		// System.out.println(out.getOfBewertung2Wert() + "/" + out.getOfBewertung2Farbcode());
		// System.out.println(out.getOfBewertung3Wert() + "/" + out.getOfBewertung3Farbcode());
		// System.out.println(out.getOfBewertung4Wert() + "/" + out.getOfBewertung4Farbcode());
		// System.out.println();

		for (int i = 0; i < 1000; i++) {
			if (lRandom.nextBoolean()) {
				// Schülerveränderung
				long schuelerID = getRandom(mapScFaKu.keySet(), lRandom);
				long fachartID = getRandom(mapScFaKu.get(schuelerID).keySet(), lRandom);
				long fachID = GostKursart.getFachID(fachartID);
				GostBlockungsergebnisKurs old = out.getOfSchuelerOfFachZugeordneterKurs(schuelerID, fachID);
				if (old == null) {
					// Hinzufügen
					long kursID = getRandom(mapFaKu.get(fachartID), lRandom);
					out.setSchuelerKurs(schuelerID, kursID, true);
					mapScFaKu.get(schuelerID).put(fachartID, kursID);
				} else {
					// Entfernen
					out.setSchuelerKurs(schuelerID, old.id, false);
					mapScFaKu.get(schuelerID).put(fachartID, null);
				}
			} else {
				// Kursveränderung
				long kursID = getRandom(mapKuSchiene.keySet(), lRandom);
				HashSet<Long> kuSchienen = mapKuSchiene.get(kursID);
				if ((kuSchienen.size() < 1) || ((kuSchienen.size() == 1) && (lRandom.nextBoolean()))) {
					// Hinzufügen
					Vector<GostBlockungsergebnisSchiene> schienenAlle = out.getMengeAllerSchienen();
					GostBlockungsergebnisSchiene schieneNeu = getRandom(schienenAlle, lRandom);
					out.setKursSchiene(kursID, schieneNeu.id, true);
					kuSchienen.add(schieneNeu.id);
				} else {
					// Entfernen
					long schieneAlt = getRandom(kuSchienen, lRandom);
					out.setKursSchiene(kursID, schieneAlt, false);
					kuSchienen.remove(schieneAlt);
				}
			}

			check_conistency(mapFaKu, mapScFaKu, out);
		}

		// System.out.println(out.getOfBewertung1Wert() + "/" + out.getOfBewertung1Farbcode());
		// System.out.println(out.getOfBewertung2Wert() + "/" + out.getOfBewertung2Farbcode());
		// System.out.println(out.getOfBewertung3Wert() + "/" + out.getOfBewertung3Farbcode());
		// System.out.println(out.getOfBewertung4Wert() + "/" + out.getOfBewertung4Farbcode());
		// System.out.println();

	}

	private static void check_conistency(HashMap<Long, HashSet<Long>> mapFaKu, HashMap<Long, HashMap<Long, Long>> mapScFaKu, GostBlockungsergebnisManager out) {
		// 1a) ergebnis.bewertung.anzahlKurseNichtZugeordnet;
		int sum1a = 0;
		for (long fachartID : mapFaKu.keySet())
			for (long kursID : mapFaKu.get(fachartID)) {
				int schienenIst = out.getOfKursAnzahlSchienenIst(kursID);
				int schienenSoll = out.getOfKursAnzahlSchienenSoll(kursID);
				sum1a += Math.abs(schienenIst - schienenSoll);
			}
		int out1a = out.getErgebnis().bewertung.anzahlKurseNichtZugeordnet;
		if (sum1a != out1a)
			fail("sum1a != out1a (" + sum1a + " != " + out1a + ")");

		// 1b) ergebnis.bewertung.regelVerletzungen.size();

		// 2a) ergebnis.bewertung.anzahlSchuelerNichtZugeordnet;
		int sum2a = 0;
		for (long schuelerID : mapScFaKu.keySet())
			for (long fachartID : mapScFaKu.get(schuelerID).keySet())
				if (mapScFaKu.get(schuelerID).get(fachartID) == null)
					sum2a++;
		int out2a = out.getErgebnis().bewertung.anzahlSchuelerNichtZugeordnet;
		if (sum2a != out2a)
			fail("sum2a != out2a (" + sum2a + " != " + out2a + ")");

		// 2b) ergebnis.bewertung.anzahlSchuelerKollisionen;
		int sum2b = 0;
		for (long schuelerID : mapScFaKu.keySet())
			for (GostBlockungsergebnisSchiene schiene : out.getMengeAllerSchienen()) {
				int summeSchiene = 0;
				for (long fachartID : mapScFaKu.get(schuelerID).keySet())
					if (mapScFaKu.get(schuelerID).get(fachartID) != null) {
						long kursID = mapScFaKu.get(schuelerID).get(fachartID);
						if (out.getOfKursOfSchieneIstZugeordnet(kursID, schiene.id))
							summeSchiene++;
					}
				if (summeSchiene > 1)
					sum2b += summeSchiene - 1;
			}
		int out2b = out.getErgebnis().bewertung.anzahlSchuelerKollisionen;
		if (sum2b != out2b)
			fail("sum2b != out2b (" + sum2b + " != " + out2b + ")");

		// 3a+3b) _ergebnis.bewertung.kursdifferenzMax;
		HashMap<Long, Integer> mapKC = new HashMap<>();
		for (long fachartID : mapFaKu.keySet())
			for (long kursID : mapFaKu.get(fachartID))
				mapKC.put(kursID, 0);
		for (long schuelerID : mapScFaKu.keySet())
			for (long fachartID : mapScFaKu.get(schuelerID).keySet())
				if (mapScFaKu.get(schuelerID).get(fachartID) != null) {
					long kursID = mapScFaKu.get(schuelerID).get(fachartID);
					mapKC.put(kursID, mapKC.get(kursID) + 1);
				}

		int[] kursDiffHisto = new int[mapScFaKu.size() + 1];
		int maxKD = 0;
		for (long fachartID : mapFaKu.keySet()) {
			long kursID1 = mapFaKu.get(fachartID).iterator().next();
			int min = mapKC.get(kursID1);
			int max = min;
			for (long kursID2 : mapFaKu.get(fachartID)) {
				int c = mapKC.get(kursID2);
				min = Math.min(min, c);
				max = Math.max(max, c);
			}
			int kd = max - min;
			maxKD = Math.max(maxKD, kd);
			kursDiffHisto[kd]++;
		}
		int outKD = out.getErgebnis().bewertung.kursdifferenzMax;
		if (maxKD != outKD)
			fail("maxKD != outKD");
		for (int i = 0; i < kursDiffHisto.length; i++) {
			int histo = kursDiffHisto[i];
			int outHisto = out.getErgebnis().bewertung.kursdifferenzHistogramm[i];
			if (histo != outHisto)
				fail("histo != outHisto (" + histo + " != " + outHisto + ")");
		}

		// 4) anzahlKurseMitGleicherFachartProSchiene;
		int sum4 = 0;
		for (long fachartID : mapFaKu.keySet())
			for (GostBlockungsergebnisSchiene schiene : out.getMengeAllerSchienen()) {
				int summeSchiene = 0;
				for (long kursID : mapFaKu.get(fachartID))
					if (out.getOfKursOfSchieneIstZugeordnet(kursID, schiene.id))
						summeSchiene++;
				if (summeSchiene > 1)
					sum4 += summeSchiene - 1;
			}
		int sum4out = out.getErgebnis().bewertung.anzahlKurseMitGleicherFachartProSchiene;
		if (sum4 != sum4out)
			fail("sum4 != sum4out (" + sum4 + " != " + sum4out + ")");
	}

	private static Long getRandom(Set<Long> keySet, Random rnd) {
		double max = 1.0;
		Long winner = null;
		for (Long current : keySet) {
			double d = rnd.nextDouble();
			if (d < max) {
				max = d;
				winner = current;
			}
		}
		return winner;
	}

	private static GostBlockungsergebnisSchiene getRandom(Vector<GostBlockungsergebnisSchiene> vSchienen, Random rnd) {
		int i = rnd.nextInt(vSchienen.size());
		return vSchienen.get(i);
	}

}
