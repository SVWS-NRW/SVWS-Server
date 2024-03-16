package de.svws_nrw.core.kursblockung.test;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKurs;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchienenZuordnung;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchienenZuordnungUpdate;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchuelerZuordnung;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchuelerZuordnungUpdate;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisSchiene;
import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.kursblockung.KursblockungAlgorithmus;
import de.svws_nrw.core.logger.LogData;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.DTOUtils;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager;

/**
 * Testet den {@link GostBlockungsergebnisManager}.
 */
@DisplayName("Testet den {@link GostBlockungsergebnisManager}.")
@TestMethodOrder(MethodOrderer.MethodName.class)
class GostBlockungsergebnisManagerTest {
	private static final long RANDOM_SEED = 6L;
	private static final String PFAD_DATEN_001 = "de/svws_nrw/core/kursblockung/blockung001/";
	private static final String PFAD_DATEN_002 = "de/svws_nrw/core/kursblockung/blockung002/";

	/**
	 * Testet das Einlesen und Konvertieren der Daten 001. Diese befinden sich hier {@link #PFAD_DATEN_001}.
	 */
	@Test
	@DisplayName("Daten 001 einlesen.")
	void test001_data() {
		// Der Kursblockungsalgorithmus ist ein Service.
		final KursblockungAlgorithmus kbAlgorithmus = new KursblockungAlgorithmus();

		// Logger vom Service übernehmen
		final Logger log = kbAlgorithmus.getLogger();

		// Consumer triggert 'fail', wenn etwas kritisches geloggt wurde.
		log.addConsumer(new Consumer<LogData>() {

			@Override
			public void accept(final LogData t) {
				if (t.getLevel().compareTo(LogLevel.APP) != 0)
					fail(t.getText());
			}
		});

		// Einlesen der Kurs42-Daten aus den Textdateien
		final long maxTimeMillis = 999;
		final Kurs42Converter k42Converter = new Kurs42Converter(PFAD_DATEN_001, maxTimeMillis, false);
		final GostBlockungsdatenManager input = k42Converter.gibKursblockungInput();
		final GostBlockungsergebnisManager out = new GostBlockungsergebnisManager(input, 1L);

		/// GostBlockungsergebnisManager dynamisch testen.
		teste_in_out(input, out);
	}

	/** Testet das Einlesen und Konvertieren der Daten 002. Diese befinden sich hier {@link #PFAD_DATEN_002}. */
	@Test
	@DisplayName("Daten 002 einlesen.")
	void test002_data() {

		// Erzeugen eines Loggers mit Consumer.
		final Logger log = new Logger();

		// Hinzufügen des Consumers, der im kritischen Fall 'fail' aufruft.
		log.addConsumer(new Consumer<LogData>() {
			@Override
			public void accept(final LogData t) {
				if (t.getLevel().compareTo(LogLevel.APP) != 0)
					fail(t.getText());
			}
		});

		// Einlesen der Kurs42-Daten aus den Textdateien
		final long maxTimeMillis = 1000 * 1;
		final Kurs42Converter k42Converter = new Kurs42Converter(PFAD_DATEN_002, maxTimeMillis, false);
		final GostBlockungsdatenManager input = k42Converter.gibKursblockungInput();
		final GostBlockungsergebnisManager out = new GostBlockungsergebnisManager(input, 1L);

		/// GostBlockungsergebnisManager dynamisch testen.
		teste_in_out(input, out);
	}

	private static void teste_in_out(final GostBlockungsdatenManager input, final GostBlockungsergebnisManager out) {
		// Die Kurs-Map erstellen.
		final HashMap<Long, HashSet<Long>> mapFaKu = new HashMap<>();
		final HashMap<Long, HashSet<Long>> mapKuSchiene = new HashMap<>();
		for (final GostBlockungKurs gKurs : input.daten().kurse) {
			// mapFaKu
			final long fachartID = GostKursart.getFachartIDByKurs(gKurs);
			HashSet<Long> setK = mapFaKu.get(fachartID);
			if (setK == null) {
				setK = new HashSet<>();
				if (mapFaKu.put(fachartID, setK) != null)
					fail("Doppelte Fachart-ID!");
			}
			if (!setK.add(gKurs.id))
				fail("Fachart " + fachartID + " hat den Kurs bereits " + gKurs.id);

			// mapKuSchiene
			mapKuSchiene.put(gKurs.id, new HashSet<>());
		}

		// Schüler-ID --> Fachart-ID --> Kurs-ID
		final HashMap<Long, HashMap<Long, Long>> mapScFaKu = new HashMap<>();
		for (final GostFachwahl gFachwahl : input.daten().fachwahlen) {
			HashMap<Long, Long> mapFW = mapScFaKu.get(gFachwahl.schuelerID);
			if (mapFW == null) {
				mapFW = new HashMap<>();
				mapScFaKu.put(gFachwahl.schuelerID, mapFW);
			}
			final long fachartID = GostKursart.getFachartIDByFachwahl(gFachwahl);
			if (mapFW.put(fachartID, null) != null)
				fail("Schüler-ID " + gFachwahl.schuelerID + ", Fachart-ID " + fachartID + " doppelt!");
		}

		final Random lRandom = new Random(RANDOM_SEED);

		for (int i = 0; i < 1000; i++) {

			if (lRandom.nextBoolean()) {
				// Schüler-Kurs-Veränderung
				final long schuelerID = getRandom(mapScFaKu.keySet(), lRandom);
				final long fachartID = getRandom(mapScFaKu.get(schuelerID).keySet(), lRandom);
				final long fachID = GostKursart.getFachID(fachartID);
				final GostBlockungsergebnisKurs old = out.getOfSchuelerOfFachZugeordneterKurs(schuelerID, fachID);
				if (old == null) {
					// Hinzufügen (Vorsicht: Hinzufügen nur dann erlaubt, wenn Schienenanzahl des Kurses >= 1!)
					final long kursID = getRandom(mapFaKu.get(fachartID), lRandom);
					if (out.getOfKursSchienenmenge(kursID).size() >= 1) {
						// Schüler-Kurs-Hinzufügen
//						setSchuelerKurs(out, schuelerID, kursID, true);
//						mapScFaKu.get(schuelerID).put(fachartID, kursID);
					}
				} else {
					// Schüler-Kurs-Entfernen
					// Entfernen
//					setSchuelerKurs(out, schuelerID, old.id, false);
//					mapScFaKu.get(schuelerID).put(fachartID, null);
				}
			} else {
				// Kursveränderung
				final long kursID = getRandom(mapKuSchiene.keySet(), lRandom);
				final HashSet<Long> kuSchienen = mapKuSchiene.get(kursID);
				if ((kuSchienen.size() < 1) || ((kuSchienen.size() == 1) && (lRandom.nextBoolean()))) {
					// Hinzufügen?
					final List<GostBlockungsergebnisSchiene> schienenAlle = out.getMengeAllerSchienen();
					final GostBlockungsergebnisSchiene schieneRnd = getRandom(schienenAlle, lRandom);
					if (kuSchienen.contains(schieneRnd.id)) {
						// Vorsicht: Entfernen Nur dann erlaubt, wenn der Kurs danach nicht schienenlos wäre!
						if (out.getOfKursSchienenmenge(kursID).size() >= 2) {
							// Kurs-Schiene-Entfernen
							setKursSchiene(out, kursID, schieneRnd.id, false);
							kuSchienen.remove(schieneRnd.id);
						}
					} else {
						// Vorsicht: Hinzufügen nur dann erlaubt, wenn der Kurs nicht bereits alle Schienen belegt!
						if (out.getOfKursSchienenmenge(kursID).size() < schienenAlle.size()) {
							// Kurs-Schiene-Hinzufügen
							setKursSchiene(out, kursID, schieneRnd.id, true);
							kuSchienen.add(schieneRnd.id);
						}
					}
				} else {
					// Vorsicht: Entfernen Nur dann erlaubt, wenn der Kurs danach nicht schienenlos wäre!
					if (out.getOfKursSchienenmenge(kursID).size() >= 2) {
						// Kurs-Schiene-Entfernen
						final long schieneAlt = getRandom(kuSchienen, lRandom);
						setKursSchiene(out, kursID, schieneAlt, false);
						kuSchienen.remove(schieneAlt);
					}
				}
			}

			check_conistency(mapFaKu, mapScFaKu, out);
		}

	}

	private static void setSchuelerKurs(final GostBlockungsergebnisManager manager, final long schuelerID, final long kursID, final boolean hinzufuegen) {
	 	final Set<GostBlockungsergebnisKursSchuelerZuordnung> z = new HashSet<>();
     	z.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(kursID, schuelerID));

     	final GostBlockungsergebnisKursSchuelerZuordnungUpdate update = hinzufuegen
     			? manager.kursSchuelerUpdate_03a_FUEGE_KURS_SCHUELER_PAARE_HINZU(z)
     		    : manager.kursSchuelerUpdate_03b_ENTFERNE_KURS_SCHUELER_PAARE(z);

        manager.kursSchuelerUpdateExecute(update);
	}

	private static void setKursSchiene(final GostBlockungsergebnisManager manager, final long kursID, final long schieneID, final boolean hinzufuegen) {
	 	final Set<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
     	z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(kursID, schieneID));

     	final GostBlockungsergebnisKursSchienenZuordnungUpdate update = hinzufuegen
     			? manager.kursSchienenUpdate_01a_FUEGE_KURS_SCHIENEN_PAARE_HINZU(z)
     		    : manager.kursSchienenUpdate_01b_ENTFERNE_KURS_SCHIENEN_PAARE(z);

        manager.kursSchienenUpdateExecute(update);
	}

	private static void check_conistency(final HashMap<Long, HashSet<Long>> mapFaKu, final HashMap<Long, HashMap<Long, Long>> mapScFaKu, final GostBlockungsergebnisManager out) {
		// 1a) ergebnis.bewertung.anzahlKurseNichtZugeordnet;
		int sum1a = 0;
		for (final long fachartID : mapFaKu.keySet())
			for (final long kursID : mapFaKu.get(fachartID)) {
				final int schienenIst = out.getOfKursAnzahlSchienenIst(kursID);
				final int schienenSoll = out.getOfKursAnzahlSchienenSoll(kursID);
				sum1a += Math.abs(schienenIst - schienenSoll);
			}
		final int out1a = out.getErgebnis().bewertung.anzahlKurseNichtZugeordnet;
		if (sum1a != out1a)
			fail("sum1a != out1a (" + sum1a + " != " + out1a + ")");

		// 1b) ergebnis.bewertung.regelVerletzungen.size();

		// 2a) ergebnis.bewertung.anzahlSchuelerNichtZugeordnet;
		int sum2a = 0;
		for (final long schuelerID : mapScFaKu.keySet())
			for (final long fachartID : mapScFaKu.get(schuelerID).keySet())
				if (mapScFaKu.get(schuelerID).get(fachartID) == null)
					sum2a++;
		final int out2a = out.getErgebnis().bewertung.anzahlSchuelerNichtZugeordnet;
		if (sum2a != out2a)
			fail("sum2a != out2a (" + sum2a + " != " + out2a + ")");

		// 2b) ergebnis.bewertung.anzahlSchuelerKollisionen;
		int sum2b = 0;
		for (final long schuelerID : mapScFaKu.keySet())
			for (final GostBlockungsergebnisSchiene schiene : out.getMengeAllerSchienen()) {
				int summeSchiene = 0;
				for (final long fachartID : mapScFaKu.get(schuelerID).keySet())
					if (mapScFaKu.get(schuelerID).get(fachartID) != null) {
						final long kursID = mapScFaKu.get(schuelerID).get(fachartID);
						if (out.getOfKursOfSchieneIstZugeordnet(kursID, schiene.id))
							summeSchiene++;
					}
				if (summeSchiene > 1)
					sum2b += summeSchiene - 1;
			}
		final int out2b = out.getErgebnis().bewertung.anzahlSchuelerKollisionen;
		if (sum2b != out2b)
			fail("sum2b != out2b (" + sum2b + " != " + out2b + ")");

		// 3a+3b) _ergebnis.bewertung.kursdifferenzMax;
		final HashMap<Long, Integer> mapKC = new HashMap<>();
		for (final long fachartID : mapFaKu.keySet())
			for (final long kursID : mapFaKu.get(fachartID))
				mapKC.put(kursID, 0);
		for (final long schuelerID : mapScFaKu.keySet())
			for (final long fachartID : mapScFaKu.get(schuelerID).keySet())
				if (mapScFaKu.get(schuelerID).get(fachartID) != null) {
					final long kursID = mapScFaKu.get(schuelerID).get(fachartID);
					mapKC.put(kursID, mapKC.get(kursID) + 1);
				}

		final int[] kursDiffHisto = new int[mapScFaKu.size() + 1];
		int maxKD = 0;
		for (final long fachartID : mapFaKu.keySet()) {
			final long kursID1 = mapFaKu.get(fachartID).iterator().next();
			int min = mapKC.get(kursID1);
			int max = min;
			for (final long kursID2 : mapFaKu.get(fachartID)) {
				final int c = mapKC.get(kursID2);
				min = Math.min(min, c);
				max = Math.max(max, c);
			}
			final int kd = max - min;
			maxKD = Math.max(maxKD, kd);
			kursDiffHisto[kd]++;
		}
		final int outKD = out.getErgebnis().bewertung.kursdifferenzMax;
		if (maxKD != outKD)
			fail("maxKD (" + maxKD + ") != outKD (" + outKD + ")");
		for (int i = 0; i < kursDiffHisto.length; i++) {
			final int histo = kursDiffHisto[i];
			final int outHisto = out.getErgebnis().bewertung.kursdifferenzHistogramm[i];
			if (histo != outHisto)
				fail("histo != outHisto (" + histo + " != " + outHisto + ")");
		}

		// 4) anzahlKurseMitGleicherFachartProSchiene;
		int sum4 = 0;
		for (final long fachartID : mapFaKu.keySet())
			for (final GostBlockungsergebnisSchiene schiene : out.getMengeAllerSchienen()) {
				int summeSchiene = 0;
				for (final long kursID : mapFaKu.get(fachartID))
					if (out.getOfKursOfSchieneIstZugeordnet(kursID, schiene.id))
						summeSchiene++;
				if (summeSchiene > 1)
					sum4 += summeSchiene - 1;
			}
		final int sum4out = out.getErgebnis().bewertung.anzahlKurseMitGleicherFachartProSchiene;
		if (sum4 != sum4out)
			fail("sum4 != sum4out (" + sum4 + " != " + sum4out + ")");
	}

	private static Long getRandom(final Set<Long> keySet, final Random rnd) {
		double max = 1.0;
		Long winner = null;
		for (final Long current : keySet) {
			final double d = rnd.nextDouble();
			if (d < max) {
				max = d;
				winner = current;
			}
		}
		return winner;
	}

	private static GostBlockungsergebnisSchiene getRandom(final List<GostBlockungsergebnisSchiene> vSchienen, final Random rnd) {
		final int i = rnd.nextInt(vSchienen.size());
		return vSchienen.get(i);
	}

}
