package de.nrw.schule.svws.core.kursblockung.test;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.nrw.schule.svws.core.data.kursblockung.KursblockungInput;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInputFach;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInputFachwahl;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInputKurs;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInputKursart;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInputRegel;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInputSchueler;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungOutput;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungOutputFachwahlZuKurs;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungOutputKursZuSchiene;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungOutputs;
import de.nrw.schule.svws.core.kursblockung.KursblockungAlgorithmus;
import de.nrw.schule.svws.core.types.kursblockung.GostKursblockungRegelTyp;
import de.nrw.schule.svws.logger.LogData;
import de.nrw.schule.svws.logger.LogLevel;
import de.nrw.schule.svws.logger.Logger;

/** Diese Klasse generiert zufällige Daten und testet damit die Kursblockungsalgorithmen. Der Zufall wird über einen
 * festen Seed = 1 gesteuert, damit Fehler nachvollziehbar sind. */
@DisplayName("Testet die Kursblockungsalgorithmen mit randomisierten Daten.")
public class KursblockungTestsRandom {

	/** Initialisiert den Test. Die Methode ist leer. */
	@BeforeAll
	static void setup() {
	}

	/** Testet die Kursblockungsalgorithmen mit randomisierten Daten. */
	@Test
	@DisplayName("Testet die Kursblockungsalgorithmen mit randomisierten Daten.")
	void testeKursblockungAlgorithmusZufaellig() {
		Random lRandom = new Random(1);
		for (int i = 0; i < 10; i++) {
			testeKursblockungAlgorithmusZufaelligEinMal(i, lRandom);
		}
	}

	private void testeKursblockungAlgorithmusZufaelligEinMal(long pID, Random pRandom) {

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

		// Umwandlung von 'Kurs42Daten' zu 'KursblockungInput'.

		KursblockungInput kbInput = erzeugeZufallsdaten(pID, pRandom);

		// Berechnung der Blockung und Rückgabe aller Blockungsergebnisse.
		KursblockungOutputs kbOutputs = kbAlgorithmus.handle(kbInput);

		// Überprüfung potentieller Fehler.
		for (LogData t : kbAlgorithmus.getLog().getLogData()) 
			if (t.getLevel().compareTo(LogLevel.APP) != 0)
				fail(t.getText());

		// Blockungsergebnisse vorhanden?
		if (kbOutputs == null) {
			fail("kbOutputs == null");
			return;
		}

		// Jedes einzelne Blockungsergebnis prüfen.
		for (int i = 0; i < kbOutputs.outputs.size(); i++) {
			KursblockungOutput kbOutput = kbOutputs.outputs.get(i);

			// Überprüfe Kurs-Zuordnung
			Vector<KursblockungOutputKursZuSchiene> vKursZuSchiene = kbOutput.kursZuSchiene;
			if (vKursZuSchiene == null) {
				fail("vKursZuSchiene == null");
				return;
			}
			int kursAnzIn = kbInput.kurse.size();
			int kursAnzOut = vKursZuSchiene.size();
			if (kursAnzOut != kursAnzIn) {
				fail("kursAnzIn != kursAnzOut (" + kursAnzIn + " !=  " + kursAnzOut + ")");
				return;
			}

			// Überprüfe Fachwahl-Zuordnung
			Vector<KursblockungOutputFachwahlZuKurs> vFachwahlenZuKurs = kbOutput.fachwahlenZuKurs;
			if (vFachwahlenZuKurs == null) {
				fail("vFachwahlenZuKurs == null");
				return;
			}
			int fachwahlAnzIn = kbInput.fachwahlen.size();
			int fachwahlAnzOut = vFachwahlenZuKurs.size();
			if (fachwahlAnzOut != fachwahlAnzIn) {
				fail("fachwahlAnzIn != fachwahlAnzOut (" + fachwahlAnzIn + " !=  " + fachwahlAnzOut + ")");
				return;
			}

		}
	}

	private KursblockungInput erzeugeZufallsdaten(long pID, Random pRandom) {
		KursblockungInput ki = new KursblockungInput();

		int nKurse = pRandom.nextInt(100); // Kurse: 0 bis 99
		int nFacharten = (2 * nKurse) / 3 + 1; // Facharten: ca. 2/3 der Kurse
		int nKursarten = 1; // Kursarten: 1 (würde z.B. GK entsprechen)
		int nSchueler = nKurse * 2; // SuS sind doppelt so wie viele Kurse
		int nSchienen = pRandom.nextInt(15) + 1; // 1 bis 15 Schienen
		ki.input = pID;
		ki.maxTimeMillis = 10;
		ki.maxSchienen = nSchienen;

		// Erzeuge alle Fächer.
		for (int fachID = 0; fachID < nFacharten; fachID++) {
			KursblockungInputFach fach = new KursblockungInputFach();
			fach.id = fachID;
			fach.representation = "Test-Fach Nr. " + fachID;
			ki.faecher.add(fach); // hinzufügen
		}

		// Erzeuge alle Kursarten.
		for (int kursartID = 0; kursartID < nKursarten; kursartID++) {
			KursblockungInputKursart kursart = new KursblockungInputKursart();
			kursart.id = kursartID;
			kursart.representation = "Test-Kursart Nr. " + kursartID;
			ki.kursarten.add(kursart); // hinzufügen
		}

		// Erzeuge alle Kurse und ordne sie einer zufälligen Schiene zu.
		int[] mapKursZuSchiene = new int[nKurse];
		for (int kursID = 0; kursID < nKurse; kursID++) {
			KursblockungInputKurs kurs = new KursblockungInputKurs();
			kurs.id = kursID;
			kurs.fach = pRandom.nextInt(nFacharten);
			kurs.kursart = pRandom.nextInt(nKursarten);
			kurs.representation = "Test-Kurs Nr. " + kursID;
			kurs.schienen = 1;
			mapKursZuSchiene[kursID] = pRandom.nextInt(nSchienen); // GUI ist 1-indiziert!
			ki.kurse.add(kurs); // hinzufügen
		}

		// Erzeuge alle SchülerInnen und zufällige Fachwahlen.
		boolean[] hatFach = new boolean[nFacharten];
		boolean[] hatSchiene = new boolean[nSchienen];
		int[] lKursPermutation = new int[nKurse];
		for (int schuelerID = 0; schuelerID < nSchueler; schuelerID++) {
			KursblockungInputSchueler schueler = new KursblockungInputSchueler();
			schueler.id = schuelerID;
			schueler.representation = "Test-SchülerIn Nr. " + schuelerID;
			ki.schueler.add(schueler); // hinzufügen

			reset(hatFach, hatSchiene, lKursPermutation, pRandom);
			for (int kursID : lKursPermutation) {
				KursblockungInputKurs kurs = ki.kurse.get(kursID);
				int fachID = (int) kurs.fach;
				int kursartID = (int) kurs.kursart;
				KursblockungInputFach fach = ki.faecher.get(fachID);
				KursblockungInputKursart kursart = ki.kursarten.get(kursartID);

				int schiene = mapKursZuSchiene[kursID];
				if (!hatFach[fachID] && !hatSchiene[schiene]) {
					hatFach[fachID] = true;
					hatSchiene[schiene] = true;
					KursblockungInputFachwahl fachwahl = new KursblockungInputFachwahl();
					fachwahl.id = ki.fachwahlen.size();
					fachwahl.fach = fachID;
					fachwahl.kursart = kursartID;
					fachwahl.representation = schueler.representation + " : " //
							+ fach.representation + "/" + kursart.representation;
					fachwahl.schueler = schuelerID;
					ki.fachwahlen.add(fachwahl); // hinzufügen
				}
			}
		}

		// Fixiere alle Kurse in ihren Schienen. Der Algorithmus muss nur die SuS verteilen.
		for (int kursID = 0; kursID < nKurse; kursID++) {
			KursblockungInputRegel regel = new KursblockungInputRegel();
			int schiene = mapKursZuSchiene[kursID];
			regel.databaseID = -1; // Dummy-Wert
			regel.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
			regel.daten = new Long[] { Long.valueOf(kursID), Long.valueOf(schiene + 1) }; // GUI-Schiene ist 1-indiziert!
			ki.regeln.add(regel); // hinzufügen
		}

		return ki;
	}

	@SuppressWarnings("static-method")
	private void reset(boolean[] pHatFach, boolean[] pSchiene, int[] pKursPermutation, Random pRandom) {
		Arrays.fill(pHatFach, false);
		Arrays.fill(pSchiene, false);
		
		for (int i = 0; i < pKursPermutation.length; i++) 
			pKursPermutation[i] = i;
		
		for (int i1 = 0; i1 < pKursPermutation.length; i1++) {
			int i2 = pRandom.nextInt(pKursPermutation.length);
			int s1 = pKursPermutation[i1];
			int s2 = pKursPermutation[i2];
			pKursPermutation[i1] = s2;
			pKursPermutation[i2] = s1;
		}
	}

}
