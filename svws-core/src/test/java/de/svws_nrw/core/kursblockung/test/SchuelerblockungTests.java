package de.svws_nrw.core.kursblockung.test;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Random;
import java.util.ArrayList;
import java.util.function.Consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.svws_nrw.core.adt.set.AVLSet;
import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.data.kursblockung.SchuelerblockungInput;
import de.svws_nrw.core.data.kursblockung.SchuelerblockungInputKurs;
import de.svws_nrw.core.data.kursblockung.SchuelerblockungOutput;
import de.svws_nrw.core.data.kursblockung.SchuelerblockungOutputFachwahlZuKurs;
import de.svws_nrw.core.kursblockung.SchuelerblockungAlgorithmus;
import de.svws_nrw.core.logger.LogData;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse testet den {@link SchuelerblockungAlgorithmus}.
 */
@DisplayName("Diese Klasse testet den {@link SchuelerblockungAlgorithmus}.")
@TestMethodOrder(MethodOrderer.MethodName.class)
class SchuelerblockungTests {

	private static final long _SEED = 1L;
	private static final int _ANZAHL_AN_ZUFALLSTESTS = 1000;

	/** Testet den Schülerblockungs-Algorithmus mit randomisierten Daten. */
	@Test
	@DisplayName("Testet den Schülerblockungs-Algorithmus mit randomisierten Daten.")
	void testeSchuelerblockungAlgorithmusZufaellig() {
		final Random lRandom = new Random(_SEED);
		for (int i = 0; i < _ANZAHL_AN_ZUFALLSTESTS; i++)
			testeSchuelerblockAlgorithmusZufaelligEinMal(lRandom);
	}

	private static void testeSchuelerblockAlgorithmusZufaelligEinMal(final Random pRandom) {

		// Der Kursblockungsalgorithmus ist ein Service.
		final SchuelerblockungAlgorithmus alg = new SchuelerblockungAlgorithmus();

		// Logger vom Service übernehmen
		final Logger log = alg.getLogger();

		// Consumer triggert 'fail', wenn etwas kritisches geloggt wurde.
		log.addConsumer(new Consumer<LogData>() {
			@Override
			public void accept(final LogData t) {
				if (t.getLevel().compareTo(LogLevel.APP) != 0)
					fail(t.getText());
			}
		});

		// Erzeugen von zufälligen Eingabedaten
		final SchuelerblockungInput in = new SchuelerblockungInput();
		// in.schuelerID = pRunde; // irrelevant

		final int nFachwahlen = 1 + pRandom.nextInt(15); // 1 bis 15 Fachwahlen
		int nFachwahlenDavonMulti = pRandom.nextInt(4); // 0 bis 4 Multikurse
		if (nFachwahlenDavonMulti > nFachwahlen)
			nFachwahlenDavonMulti = pRandom.nextInt(nFachwahlen);
		int nKurse = 0; // Zähler für die Kurse.
		final int nSchienen = nFachwahlen + nFachwahlenDavonMulti;
		in.schienen = nSchienen;
		final AVLSet<String> setFachart = new AVLSet<>();
		// System.out.println("nFachwahlen = " + nFachwahlen);
		// System.out.println("nFachwahlenDavonMulti = " + nFachwahlenDavonMulti);

		// Erzeuge Fachwahlen.
		int startSchiene = 1;
		for (int i = 0; i < nFachwahlen; i++) {
			final GostFachwahl fachwahl = new GostFachwahl();
			fachwahl.schuelerID = 1; // Fake-ID
			fachwahl.fachID = pRandom.nextLong(30); // Es gibt ca. 30 verschiedene Fächer.
			fachwahl.kursartID = pRandom.nextInt(5); // Es gibt ca. 5 verschiedene Kursarten.
			in.fachwahlen.add(fachwahl);

			String sFachart = fachwahl.fachID + ";" + fachwahl.kursartID;
			in.fachwahlenText.add(sFachart);
			while (setFachart.contains(sFachart)) {
				fachwahl.fachID = pRandom.nextLong(30);
				fachwahl.kursartID = pRandom.nextInt(5);
				sFachart = fachwahl.fachID + ";" + fachwahl.kursartID;
			}

			// Erzeuge die zugehörigen Kurse der Fachwahl.
			final int nKurseDieserFachwahl = pRandom.nextInt(6) + 1; // 1 bis 6 Kurse erzeugen.
			for (int j = 0; j < nKurseDieserFachwahl; j++) {
				final SchuelerblockungInputKurs kurs = new SchuelerblockungInputKurs();
				kurs.id = nKurse;
				kurs.anzahlSuS = pRandom.nextInt(35); // Bis zu 34 SuS pro Kurs.
				kurs.fach = fachwahl.fachID; // Fach gehört zur obigen Fachwahl.
				kurs.kursart = fachwahl.kursartID; // Kursart gehört zur obigen Fachwahl.
				if (j == 0) {
					if (i < nFachwahlenDavonMulti) {
						kurs.schienen = new int[] { startSchiene, startSchiene + 1 };
						startSchiene += 2;
					} else {
						kurs.schienen = new int[] { startSchiene };
						startSchiene += 1;
					}
				} else {
					final int s1 = pRandom.nextInt(nSchienen) + 1; // Schienen sind 1-indiziert.
					if (i < nFachwahlenDavonMulti) {
						int s2 = pRandom.nextInt(nSchienen) + 1; // Schienen sind 1-indiziert.
						while (s1 == s2)
							s2 = pRandom.nextInt(nSchienen) + 1; // Schienen sind 1-indiziert.
						kurs.schienen = new int[] { s1, s2 };
					} else {
						kurs.schienen = new int[] { s1 };
					}
				}
				in.kurse.add(kurs);
				nKurse++;
			}
		}

		// ##################################################
		// ##################################################
		// Algorithmus berechnet und liefert genau ein Ergebnis.
		final SchuelerblockungOutput out = alg.handle(in);
		// ##################################################
		// ##################################################

		// Überprüfung potentieller Fehler.
		for (final LogData t : alg.getLog().getLogData())
			if (t.getLevel().compareTo(LogLevel.APP) != 0)
				fail(t.getText());

		// Kein Blockungsergebnis vorhanden?
		if (out == null) {
			fail("SchuelerblockungOutput == null");
			return;
		}

		// Keine Fachwahlen vorhanden?
		@NotNull
		final ArrayList<@NotNull SchuelerblockungOutputFachwahlZuKurs> fachwahlen = out.fachwahlenZuKurs;
		if (fachwahlen == null) {
			fail("SchuelerblockungOutput.fachwahlenZuKurs == null");
			return;
		}

		// Nicht die richtigen Anzahl an Fachwahlen?
		if (fachwahlen.size() != nFachwahlen) {
			fail("SchuelerblockungOutput.fachwahlenZuKurs.size() != " + nFachwahlen);
			return;
		}

		// Wurde wirklich jeder Fachwahl etwas zugeordnet?
		for (int i = 0; i < fachwahlen.size(); i++) {
			@NotNull
			final SchuelerblockungOutputFachwahlZuKurs fachwahlZuKurs = fachwahlen.get(i);
			if (fachwahlZuKurs == null) {
				fail("fachwahl == null");
				return;
			}
			if (fachwahlZuKurs.kursID < 0) {
				fail("fachwahl.kursID < 0, dabei ist eine optimale Verteilung möglich!");
				return;
			}
		}

	}

}
