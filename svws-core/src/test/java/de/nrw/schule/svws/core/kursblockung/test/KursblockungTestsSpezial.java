package de.nrw.schule.svws.core.kursblockung.test;

import static org.junit.jupiter.api.Assertions.fail;

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
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInputSchueler;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungOutput;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungOutputFachwahlZuKurs;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungOutputKursZuSchiene;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungOutputs;
import de.nrw.schule.svws.core.kursblockung.KursblockungAlgorithmus;
import de.nrw.schule.svws.logger.LogData;
import de.nrw.schule.svws.logger.LogLevel;
import de.nrw.schule.svws.logger.Logger;

/** Diese Klasse erzeugte Sonderfälle und testet damit die Kursblockungsalgorithmen. */
@DisplayName("Testet die Kursblockungsalgorithmen mit bestimten Sonderfällen.")
public class KursblockungTestsSpezial {

	/** Initialisiert den Test. Die Methode ist leer. */
	@BeforeAll
	static void setup() {
	}

	/** Testet Wahlen, deren Kurse nicht eingerichtet sind. */
	@Test
	@DisplayName("Testet Wahlen, derenz Kurse nicht eingerichtet sind.")
	void testeWahlOhneKurs() {
		// Der Kursblockungsalgorithmus ist ein Service.
		KursblockungAlgorithmus kbAlgorithmus = new KursblockungAlgorithmus();

		// Logger vom Service übernehmen
		Logger log = kbAlgorithmus.getLogger();

		// Consumer triggert 'fail', wenn etwas kritisches geloggt wurde.
		log.addConsumer(new Consumer<LogData>() {
			@Override
			public void accept(LogData t) {
				if (t.getLevel().compareTo(LogLevel.ERROR) == 0)
					fail(t.getText());
			}
		});

		// Umwandlung von 'Kurs42Daten' zu 'KursblockungInput'.
		KursblockungInputKursart kbKursart1 = new KursblockungInputKursart();
		kbKursart1.id = 1;

		KursblockungInputKursart kbKursart2 = new KursblockungInputKursart();
		kbKursart2.id = 2;

		KursblockungInputFach kbFach1 = new KursblockungInputFach();
		kbFach1.id = 1;

		KursblockungInputFach kbFach2 = new KursblockungInputFach();
		kbFach2.id = 2;

		KursblockungInputKurs kbKurs = new KursblockungInputKurs();
		kbKurs.id = 1;
		kbKurs.fach = 1;
		kbKurs.kursart = 1;
		kbKurs.schienen = 1;

		KursblockungInputSchueler kbSchueler = new KursblockungInputSchueler();
		kbSchueler.id = 1;

		// Fachwahl ohne Kurs!
		KursblockungInputFachwahl kbFachwahl = new KursblockungInputFachwahl();
		kbFachwahl.schueler = 1;
		kbFachwahl.fach = 2;
		kbFachwahl.kursart = 2;

		KursblockungInput kbInput = new KursblockungInput();
		kbInput.maxSchienen = 11;
		kbInput.kursarten.add(kbKursart1);
		kbInput.kursarten.add(kbKursart2);
		kbInput.faecher.add(kbFach1);
		kbInput.faecher.add(kbFach2);
		kbInput.kurse.add(kbKurs);
		kbInput.schueler.add(kbSchueler);
		kbInput.fachwahlen.add(kbFachwahl);

		// Berechnung der Blockung und Rückgabe aller Blockungsergebnisse.
		KursblockungOutputs kbOutputs = kbAlgorithmus.handle(kbInput);

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

}
