package de.nrw.schule.svws.core.kursblockung.test;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Random;
import java.util.Vector;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.nrw.schule.svws.core.data.kursblockung.KursblockungInput;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInputFachwahl;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungInputKurs;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungOutput;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungOutputFachwahlZuKurs;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungOutputKursZuSchiene;
import de.nrw.schule.svws.core.data.kursblockung.KursblockungOutputs;
import de.nrw.schule.svws.core.kursblockung.KursblockungAlgorithmus;
import de.nrw.schule.svws.core.kursblockung.KursblockungMatrix;
import de.nrw.schule.svws.core.kursblockung.KursblockungStatic;
import de.nrw.schule.svws.logger.LogData;
import de.nrw.schule.svws.logger.LogLevel;
import de.nrw.schule.svws.logger.Logger;
import jakarta.validation.constraints.NotNull;

/** Diese Klasse testet <b>alle</b> relevanten Methoden, die für die Kursblockung benötigt werden. */
@DisplayName("Testet alle relevanten Kursblockungsalgorithmen.")
@TestMethodOrder(MethodOrderer.MethodName.class)
public class KursblockungTests {

	private static final String PFAD_DATEN_001 = "de/nrw/schule/svws/core/kursblockung/blockung001/";
	private static final String PFAD_DATEN_002 = "de/nrw/schule/svws/core/kursblockung/blockung002/";

	/** Initialisiert den Test. Die Methode ist leer. */
	@BeforeAll
	static void setup() {
	}

	/** Testet das Einlesen und Konvertieren der Daten 001. Diese befinden sich hier {@link #PFAD_DATEN_001}. */
	@Test
	@DisplayName("Daten 001 einlesen.")
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

		// Einlesen der Kurs42-Daten aus den Textdateien
		Kurs42Converter k42Converter = new Kurs42Converter(log, PFAD_DATEN_001, 999, false);
		KursblockungInput ki = k42Converter.gibKursblockungInput();

		// Überprüfen der Ergebnisse
		if (ki.maxSchienen != 14)
			fail("Blockung001 hat nicht 14 Schienen!");

		if (ki.schueler.size() != 137)
			fail("Blockung001 hat nicht 137 SuS!");

		if (ki.faecher.size() != 33)
			fail("Blockung001 hat nicht 33 Fächer!");

		if (ki.kursarten.size() != 3)
			fail("Blockung001 hat nicht 3 Kursarten!");

		if (ki.kurse.size() != 69)
			fail("Blockung001 hat nicht 69 Kurse!");

		if (ki.fachwahlen.size() != 1146)
			fail("Blockung001 hat nicht 1146 Fachwahlen!");
	}

	/** Liest diese {@link #PFAD_DATEN_001} Daten ein. Fixiert anschließend alle Kurse und lässt dann den
	 * Kursblockungsalgorithmus lediglich die SuS verteilen. */
	@Test
	@DisplayName("Daten 001 blocken (nur SuS).")
	void test001_fixed_complete() {
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

		// Einlesen der Kurs42-Daten aus den Textdateien.
		long maxTimeMillis = 1000 * 1;
		Kurs42Converter k42Converter = new Kurs42Converter(log, PFAD_DATEN_001, maxTimeMillis, true);

		// Umwandlung von 'Kurs42Daten' zu 'KursblockungInput'.
		KursblockungInput kbInput = k42Converter.gibKursblockungInput();

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

			if (kbOutput.input != kbInput.input)
				fail("Input-Output-IDs stimmen nicht überein: kbOutput.input != kbInput.input");

			Vector<KursblockungOutputKursZuSchiene> vKursZuSchiene = kbOutput.kursZuSchiene;
			if (vKursZuSchiene == null)
				fail("vKursZuSchiene == null");

			int kursAnz = vKursZuSchiene.size();
			if (kursAnz != 69)
				fail("Der Blockungsalgorithmus hat nicht 69 Kurse zugeordnet, sondern " + kursAnz + ".");

			Vector<KursblockungOutputFachwahlZuKurs> vFachwahlenZuKurs = kbOutput.fachwahlenZuKurs;
			if (vFachwahlenZuKurs == null)
				fail("vFachwahlenZuKurs == null");

			int fachwahlAnz = vFachwahlenZuKurs.size();
			if (fachwahlAnz != 1146)
				fail("Der Blockungsalgorithmus hat nicht 1146 Fachwahlen zugeordnet, sondern " + fachwahlAnz + ".");

		}

	}

	/** Liest diese {@link #PFAD_DATEN_001} Daten ein. Definiert einige Regeln und lässt dann den
	 * Kursblockungsalgorithmus Kurse und SuS verteilen. */
	@Test
	@DisplayName("Daten 001 blocken (Kurse und SuS).")
	void test001_fixed_some() {
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

		// Einlesen der Kurs42-Daten aus den Textdateien.
		long maxTimeMillis = 1000 * 1;
		Kurs42Converter k42Converter = new Kurs42Converter(log, PFAD_DATEN_001, maxTimeMillis, false);

		// Umwandlung von 'Kurs42Daten' zu 'KursblockungInput'.
		KursblockungInput kbInput = k42Converter.gibKursblockungInput();

		// Fixierungen
		KursblockungStatic.aktionSperreSchieneFuerKursart(kbInput, "LK", 3, kbInput.maxSchienen);
		KursblockungStatic.aktionSperreSchieneFuerKursart(kbInput, "GK", 1, 2);
		KursblockungStatic.aktionSperreSchieneFuerKursart(kbInput, "PJK", 1, 2);
		KursblockungStatic.aktionFixiereKurseInSchieneSonstNichts(kbInput, new String[] { "SP-GK1", "SP-GK2", "SP-GK3" }, 12);
		KursblockungStatic.aktionFixiereKurseInSchieneSonstNichts(kbInput, new String[] { "PXSP-PJK1" }, 13);
		KursblockungStatic.aktionFixiereKurseInSchieneSonstNichts(kbInput, new String[] { "I0-GK1", "S0-GK1" }, 14);

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

			if (kbOutput.input != kbInput.input)
				fail("Input-Output-IDs stimmen nicht überein: kbOutput.input != kbInput.input");

			Vector<KursblockungOutputKursZuSchiene> vKursZuSchiene = kbOutput.kursZuSchiene;
			if (vKursZuSchiene == null) {
				fail("vKursZuSchiene == null");
				return;
			}

			int kursAnz = vKursZuSchiene.size();
			if (kursAnz != 69) {
				fail("Der Blockungsalgorithmus hat nicht 69 Kurse zugeordnet, sondern " + kursAnz + ".");
				return;
			}

			Vector<KursblockungOutputFachwahlZuKurs> vFachwahlenZuKurs = kbOutput.fachwahlenZuKurs;
			if (vFachwahlenZuKurs == null) {
				fail("vFachwahlenZuKurs == null");
				return;
			}

			int fachwahlAnz = vFachwahlenZuKurs.size();
			if (fachwahlAnz != 1146) {
				fail("Der Blockungsalgorithmus hat nicht 1146 Fachwahlen zugeordnet, sondern " + fachwahlAnz + ".");
				return;
			}

		}

	}

	/** Liest diese {@link #PFAD_DATEN_001} Daten ein. Definiert einige Regeln und explizit Regel 5, indem für Schüler
	 * Nr. 18 alle Kurse gesperrt werden. Lässt dann den Kursblockungsalgorithmus Kurse und SuS verteilen. */
	@Test
	@DisplayName("Daten 001 blocken mit Regel 5 (Verbiete Schüler X in Kurs Y).")
	void test001_regel_5() {
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

		// Einlesen der Kurs42-Daten aus den Textdateien.
		long maxTimeMillis = 1000 * 1;
		Kurs42Converter k42Converter = new Kurs42Converter(log, PFAD_DATEN_001, maxTimeMillis, false);

		// Umwandlung von 'Kurs42Daten' zu 'KursblockungInput'.
		KursblockungInput kbInput = k42Converter.gibKursblockungInput();

		// Fixierungen
		KursblockungStatic.aktionSperreSchieneFuerKursart(kbInput, "LK", 3, kbInput.maxSchienen);
		KursblockungStatic.aktionSperreSchieneFuerKursart(kbInput, "GK", 1, 2);
		KursblockungStatic.aktionSperreSchieneFuerKursart(kbInput, "PJK", 1, 2);
		KursblockungStatic.aktionFixiereKurseInSchieneSonstNichts(kbInput, new String[] { "SP-GK1", "SP-GK2", "SP-GK3" }, 12);
		KursblockungStatic.aktionFixiereKurseInSchieneSonstNichts(kbInput, new String[] { "PXSP-PJK1" }, 13);
		KursblockungStatic.aktionFixiereKurseInSchieneSonstNichts(kbInput, new String[] { "I0-GK1", "S0-GK1" }, 14);

		// Regel 5
		for (int kursID = 0; kursID < kbInput.kurse.size(); kursID++) {
			KursblockungStatic.aktionVerbieteSchuelerInKurs(kbInput, 18, kursID);
		}

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

			if (kbOutput.input != kbInput.input)
				fail("Input-Output-IDs stimmen nicht überein: kbOutput.input != kbInput.input");

			Vector<KursblockungOutputKursZuSchiene> vKursZuSchiene = kbOutput.kursZuSchiene;
			if (vKursZuSchiene == null) {
				fail("vKursZuSchiene == null");
				return;
			}

			int kursAnz = vKursZuSchiene.size();
			if (kursAnz != 69) {
				fail("Der Blockungsalgorithmus hat nicht 69 Kurse zugeordnet, sondern " + kursAnz + ".");
				return;
			}

			Vector<KursblockungOutputFachwahlZuKurs> vFachwahlenZuKurs = kbOutput.fachwahlenZuKurs;
			if (vFachwahlenZuKurs == null) {
				fail("vFachwahlenZuKurs == null");
				return;
			}

			int fachwahlAnz = vFachwahlenZuKurs.size();
			if (fachwahlAnz != 1146) {
				fail("Der Blockungsalgorithmus hat nicht 1146 Fachwahlen zugeordnet, sondern " + fachwahlAnz + ".");
				return;
			}

		}

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
		KursblockungInput ki = k42Converter.gibKursblockungInput();

		// Überprüfen der Ergebnisse
		if (ki.maxSchienen != 12) {
			fail("Blockung002 hat nicht 12 Schienen, sondern " + ki.maxSchienen);
		}
		if (ki.schueler.size() != 153) {
			fail("Blockung002 hat nicht 153 SuS, sondern " + ki.schueler.size());
		}
		if (ki.faecher.size() != 23) {
			fail("Blockung002 hat nicht 23 Fächer, sondern " + ki.faecher.size());
		}
		if (ki.kursarten.size() != 2) {
			fail("Blockung002 hat nicht 2 Kursarten, sondern " + ki.kursarten.size());
		}
		if (ki.kurse.size() != 86) {
			fail("Blockung002 hat nicht 86 Kurse, sondern " + ki.kurse.size());
		}
		if (ki.fachwahlen.size() != 1798) {
			fail("Blockung002 hat nicht 1798 Fachwahlen, sondern " + ki.fachwahlen.size());
		}

	}

	/** Liest diese {@link #PFAD_DATEN_002} Daten ein. Fixiert anschließend alle Kurse und lässt dann den
	 * Kursblockungsalgorithmus lediglich die SuS verteilen. */
	@Test
	@DisplayName("Daten 002 blocken (nur SuS).")
	void test002_fixed_complete() {
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

		// Einlesen der Kurs42-Daten aus den Textdateien.
		long maxTimeMillis = 1000 * 1;
		boolean kurseFixieren = true;
		Kurs42Converter k42Converter = new Kurs42Converter(log, PFAD_DATEN_002, maxTimeMillis, kurseFixieren);

		// Umwandlung von 'Kurs42Daten' zu 'KursblockungInput'.
		KursblockungInput kbInput = k42Converter.gibKursblockungInput();

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

			if (kbOutput.input != kbInput.input)
				fail("Input-Output-IDs stimmen nicht überein: kbOutput.input != kbInput.input");

			Vector<KursblockungOutputKursZuSchiene> vKursZuSchiene = kbOutput.kursZuSchiene;
			if (vKursZuSchiene == null) {
				fail("vKursZuSchiene == null");
				return;
			}
			int kursIn = kbInput.kurse.size();
			int kursOut = vKursZuSchiene.size();
			if (kursIn != kursOut) {
				fail("kursIn != kursOut (" + kursIn + " != " + kursOut + ")");
				return;
			}

			Vector<KursblockungOutputFachwahlZuKurs> vFachwahlenZuKurs = kbOutput.fachwahlenZuKurs;
			if (vFachwahlenZuKurs == null) {
				fail("vFachwahlenZuKurs == null");
				return;
			}
			int fachwahlIn = kbInput.fachwahlen.size();
			int fachwahlOut = vFachwahlenZuKurs.size();
			if (fachwahlIn != fachwahlOut) {
				fail("fachwahlIn != fachwahlOut (" + fachwahlIn + " != " + fachwahlOut + ")");
				return;
			}

		}

	}

	/** Liest diese {@link #PFAD_DATEN_002} Daten ein. Definiert einige Regeln und lässt dann den
	 * Kursblockungsalgorithmus Kurse und SuS verteilen. */
	@Test
	@DisplayName("Daten 002 blocken (Kurse und SuS).")
	void test002_fixed_some() {
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

		// Einlesen der Kurs42-Daten aus den Textdateien.
		long maxTimeMillis = 1000 * 1;
		boolean kurseFixieren = false;
		Kurs42Converter k42Converter = new Kurs42Converter(log, PFAD_DATEN_002, maxTimeMillis, kurseFixieren);

		// Umwandlung von 'Kurs42Daten' zu 'KursblockungInput'.
		KursblockungInput kbInput = k42Converter.gibKursblockungInput();
		KursblockungStatic.aktionFixiereKursInSchiene(kbInput, "BI-GK1", 1);
		KursblockungStatic.aktionFixiereKursInSchiene(kbInput, "M-GK1", 1);
		KursblockungStatic.aktionFixiereKursInSchiene(kbInput, "L-GK1", 1);

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

			if (kbOutput.input != kbInput.input)
				fail("Input-Output-IDs stimmen nicht überein: kbOutput.input != kbInput.input");

			Vector<KursblockungOutputKursZuSchiene> vKursZuSchiene = kbOutput.kursZuSchiene;
			if (vKursZuSchiene == null) {
				fail("vKursZuSchiene == null");
				return;
			}
			int kursIn = kbInput.kurse.size();
			int kursOut = vKursZuSchiene.size();
			if (kursIn != kursOut) {
				fail("kursIn != kursOut (" + kursIn + " != " + kursOut + ")");
				return;
			}

			Vector<KursblockungOutputFachwahlZuKurs> vFachwahlenZuKurs = kbOutput.fachwahlenZuKurs;
			if (vFachwahlenZuKurs == null) {
				fail("vFachwahlenZuKurs == null");
				return;
			}
			int fachwahlIn = kbInput.fachwahlen.size();
			int fachwahlOut = vFachwahlenZuKurs.size();
			if (fachwahlIn != fachwahlOut) {
				fail("fachwahlIn != fachwahlOut (" + fachwahlIn + " != " + fachwahlOut + ")");
				return;
			}

		}

	}

	/** Liest diese {@link #PFAD_DATEN_002} Daten ein. Definiert einige Regeln und explizit Regel 4, die 19 SuS in Kurs
	 * 0 fixiert. Lässt dann den Kursblockungsalgorithmus Kurse und SuS verteilen. */
	@Test
	@DisplayName("Daten 002 blocken mit Regel 4 (Schüler-Kurs-Fixierung).")
	void test002_regel_4() {
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

		// Einlesen der Kurs42-Daten aus den Textdateien.
		long maxTimeMillis = 1000 * 1;
		boolean kurseFixieren = false;
		Kurs42Converter k42Converter = new Kurs42Converter(log, PFAD_DATEN_002, maxTimeMillis, kurseFixieren);

		// Umwandlung von 'Kurs42Daten' zu 'KursblockungInput'.
		KursblockungInput kbInput = k42Converter.gibKursblockungInput();

		// Weitere Regeln manuell hinzufügen.
		KursblockungStatic.aktionFixiereKursInSchiene(kbInput, "BI-GK1", 1);
		KursblockungStatic.aktionFixiereKursInSchiene(kbInput, "M-GK1", 1);
		KursblockungStatic.aktionFixiereKursInSchiene(kbInput, "L-GK1", 1);

		KursblockungStatic.aktionFixiereSchuelerInKurs(kbInput, 4, 0);
		KursblockungStatic.aktionFixiereSchuelerInKurs(kbInput, 10, 0);
		KursblockungStatic.aktionFixiereSchuelerInKurs(kbInput, 18, 0);
		KursblockungStatic.aktionFixiereSchuelerInKurs(kbInput, 21, 0);
		KursblockungStatic.aktionFixiereSchuelerInKurs(kbInput, 22, 0);
		KursblockungStatic.aktionFixiereSchuelerInKurs(kbInput, 27, 0);
		KursblockungStatic.aktionFixiereSchuelerInKurs(kbInput, 31, 0);
		KursblockungStatic.aktionFixiereSchuelerInKurs(kbInput, 55, 0);
		KursblockungStatic.aktionFixiereSchuelerInKurs(kbInput, 56, 0);
		KursblockungStatic.aktionFixiereSchuelerInKurs(kbInput, 58, 0);
		KursblockungStatic.aktionFixiereSchuelerInKurs(kbInput, 59, 0);
		KursblockungStatic.aktionFixiereSchuelerInKurs(kbInput, 61, 0);
		KursblockungStatic.aktionFixiereSchuelerInKurs(kbInput, 66, 0);
		KursblockungStatic.aktionFixiereSchuelerInKurs(kbInput, 78, 0);
		KursblockungStatic.aktionFixiereSchuelerInKurs(kbInput, 101, 0);
		KursblockungStatic.aktionFixiereSchuelerInKurs(kbInput, 118, 0);
		KursblockungStatic.aktionFixiereSchuelerInKurs(kbInput, 122, 0);
		KursblockungStatic.aktionFixiereSchuelerInKurs(kbInput, 125, 0);
		KursblockungStatic.aktionFixiereSchuelerInKurs(kbInput, 128, 0);

		// Berechnung der Blockung und Rückgabe aller Blockungsergebnisse.
		KursblockungOutputs kbOutputs = kbAlgorithmus.handle(kbInput);

		// Blockungsergebnisse vorhanden?
		if (kbOutputs == null) {
			fail("kbOutputs == null");
			return;
		}

		// Jedes einzelne Blockungsergebnis prüfen.
		if (kbOutputs.outputs.size() == 0) {
			fail("Es gab keine Blockungsergebnisse!");
		}
		for (int i = 0; i < kbOutputs.outputs.size(); i++) {
			KursblockungOutput kbOutput = kbOutputs.outputs.get(i);

			if (kbOutput.input != kbInput.input)
				fail("Input-Output-IDs stimmen nicht überein: kbOutput.input != kbInput.input");

			Vector<KursblockungOutputKursZuSchiene> vKursZuSchiene = kbOutput.kursZuSchiene;
			if (vKursZuSchiene == null) {
				fail("vKursZuSchiene == null");
				return;
			}
			int kursIn = kbInput.kurse.size();
			int kursOut = vKursZuSchiene.size();
			if (kursIn != kursOut) {
				fail("kursIn != kursOut (" + kursIn + " != " + kursOut + ")");
				return;
			}

			Vector<KursblockungOutputFachwahlZuKurs> vFachwahlenZuKurs = kbOutput.fachwahlenZuKurs;
			if (vFachwahlenZuKurs == null) {
				fail("vFachwahlenZuKurs == null");
				return;
			}
			int fachwahlIn = kbInput.fachwahlen.size();
			int fachwahlOut = vFachwahlenZuKurs.size();
			if (fachwahlIn != fachwahlOut) {
				fail("fachwahlIn != fachwahlOut (" + fachwahlIn + " != " + fachwahlOut + ")");
				return;
			}

			// Überprüfung der Kursfixierung
			checkKursfixierung(kbInput, vFachwahlenZuKurs, 4, 0);
			checkKursfixierung(kbInput, vFachwahlenZuKurs, 10, 0);
			checkKursfixierung(kbInput, vFachwahlenZuKurs, 18, 0);
			checkKursfixierung(kbInput, vFachwahlenZuKurs, 21, 0);
			checkKursfixierung(kbInput, vFachwahlenZuKurs, 22, 0);
			checkKursfixierung(kbInput, vFachwahlenZuKurs, 27, 0);
			checkKursfixierung(kbInput, vFachwahlenZuKurs, 31, 0);
			checkKursfixierung(kbInput, vFachwahlenZuKurs, 55, 0);
			checkKursfixierung(kbInput, vFachwahlenZuKurs, 56, 0);
			checkKursfixierung(kbInput, vFachwahlenZuKurs, 58, 0);
			checkKursfixierung(kbInput, vFachwahlenZuKurs, 59, 0);
			checkKursfixierung(kbInput, vFachwahlenZuKurs, 61, 0);
			checkKursfixierung(kbInput, vFachwahlenZuKurs, 66, 0);
			checkKursfixierung(kbInput, vFachwahlenZuKurs, 78, 0);
			checkKursfixierung(kbInput, vFachwahlenZuKurs, 101, 0);
			checkKursfixierung(kbInput, vFachwahlenZuKurs, 118, 0);
			checkKursfixierung(kbInput, vFachwahlenZuKurs, 122, 0);
			checkKursfixierung(kbInput, vFachwahlenZuKurs, 125, 0);
			checkKursfixierung(kbInput, vFachwahlenZuKurs, 128, 0);
		}

	}

	@SuppressWarnings("static-method")
	private void checkKursfixierung(KursblockungInput kbInput,
			Vector<KursblockungOutputFachwahlZuKurs> vFachwahlenZuKurs, long schuelerID, long kursID) {
		// "fachartID" von "kursID" herausfinden
		long fachartID = -1;
		long kursartID = -1;
		for (@NotNull KursblockungInputKurs kurs : kbInput.kurse) {
			if (kurs.id == kursID) {
				fachartID = kurs.fach;
				kursartID = kurs.kursart;
			}
		}
		if ((fachartID < 0) || (kursartID < 0)) {
			fail("kursID " + kursID + " hat keine zugeordnete fachart/kursart!");
		}

		// "fachwahlID" von "fachartID" + "kursartID" + "schuelerID" herausfinden
		long fachwahlID = -1;
		for (@NotNull KursblockungInputFachwahl wahlIn : kbInput.fachwahlen) {
			if ((wahlIn.fach == fachartID) && (wahlIn.kursart == kursartID) && (wahlIn.schueler == schuelerID)) {
				fachwahlID = wahlIn.id;
			}
		}
		if (fachwahlID < 0) {
			fail("fachartID=" + fachartID + " + kursartID=" + kursartID + " + schuelerID=" + schuelerID
					+ " hat keine zugeordnete fachwahlID!");
		}

		long zugeordneterKurs = -1;
		int treffer = 0;
		for (KursblockungOutputFachwahlZuKurs wahlOut : vFachwahlenZuKurs) {
			if (wahlOut.fachwahl == fachwahlID) {
				treffer++;

				zugeordneterKurs = wahlOut.kurs;
			}
		}
		if (treffer != 1) {
			fail("Die Fachwahl " + fachwahlID + " wurde " + treffer + " mal gefunden, statt 1 Mal!");
		}
		if ((zugeordneterKurs >= 0) && (zugeordneterKurs != kursID)) {
			fail("Kursfixierung von Schüler " + schuelerID + " in Kurs " + kursID + " klappte nicht, stattdessen Kurs "
					+ zugeordneterKurs + ".");
		}

	}

	/** Testet den Algorithmus "Maximum Cardinality Bipartite Matching". Hierfür werden Matrizen unterschiedlicher
	 * Dimension erzeugt und mit 0en und 1en zufällig gefüllt. Anschließend wird ein maximales Matching berechnet.
	 * Gleichzeitig wird die negierte Matrix an einen "Minimum Weight Bipartite Matching" übergeben. Die Ergebnisse
	 * beider Algorithmen muss identisch sein (nicht die Zuordnung selbst, sondern die Größe des Matchings). */
	@Test
	@DisplayName("Maximum Cardinality Bipartite Matching testen.")
	void test000_max_bipartite_matching() {
		Random rnd = new Random(1);

		// Führe viele zufällige Tests durch...
		for (int i = 0; i < 100000; i++) {
			int rows = rnd.nextInt(10) + 2;
			int cols = rnd.nextInt(10) + 2;
			double percent = rnd.nextDouble();

			// Erzeuge identische Matrizen mit +1 (m1) und -1 (m2) Werten
			KursblockungMatrix m1 = new KursblockungMatrix(rnd, rows, cols); // Maximales bipartites Matching
			KursblockungMatrix m2 = new KursblockungMatrix(rnd, rows, cols); // Minimales gewichtetes bipartites
																				// Matching
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					if (rnd.nextDouble() < percent) {
						m1.getMatrix()[r][c] = 1;
						m2.getMatrix()[r][c] = -1;
					}
				}
			}

			// Berechne
			int[] r2c1 = m1.gibMaximalesBipartitesMatching(true);
			int[] r2c2 = m2.gibMinimalesBipartitesMatchingGewichtet(true);

			// Berechne die Summe der Zuordnung
			int summe1 = 0;
			int summe2 = 0;
			for (int r = 0; r < rows; r++) {
				summe1 += r2c1[r] < 0 ? 0 : m1.getMatrix()[r][r2c1[r]];
				summe2 += r2c2[r] < 0 ? 0 : -m2.getMatrix()[r][r2c2[r]];
			}

			// Die Summe beider Verfahren muss gleich sein
			if (summe1 != summe2) {
				fail(m1.convertToString("Summe1(" + summe1 + ") != Summe2(" + summe2 + ")", 5, false));
			}
		}

	}

	/** Testet den Algorithmus "Minimum Weight Bipartite Matching". Hierfür werden Matrizen unterschiedlicher Dimension
	 * erzeugt und mit Zahlen von -9 bis +9 zufällig gefüllt. Ebenso wird durch einen Brute-Force-Algorithmus auch ein
	 * "Minimum Weight Bipartite Matching" berechnet. Die Ergebnisse beider Algorithmen muss identisch sein (nicht die
	 * Zuordnung selbst, sondern die Größe des Matchings). */
	@Test
	@DisplayName("Minimum Weight Bipartite Matching testen.")
	void test000_min_weight_bipartite_matching() {
		Random rnd = new Random(1);

		// Zufallsmatrizen testen
		// Vorsicht mit der Dimension! Zu groß --> exponentielle Laufzeit!
		for (int i = 0; i < 100000; i++) {
			// Füllen
			int rows = rnd.nextInt(5) + 2; // Dimension
			int cols = rnd.nextInt(5) + 2; // Dimension
			KursblockungMatrix m = new KursblockungMatrix(rnd, rows, cols);
			m.fuelleMitZufallszahlenVonBis(-9, 9);

			// Berechnen
			int[] r2c = m.gibMinimalesBipartitesMatchingGewichtet(true);

			// Summe berechnen (Minimum Matching Algorithmus)
			long wert1 = 0;
			for (int r = 0; r < r2c.length; r++) {
				int c = r2c[r];
				if (c >= 0)
					wert1 += m.getMatrix()[r][c];
			}

			// Summe berechnen (alle Kombinationen durchgehen)
			long wert2 = rows <= cols ? recursive_min_sum_r(m.getMatrix(), 0, new boolean[cols])
					: recursive_min_sum_c(m.getMatrix(), 0, new boolean[rows]);

			// Die Summen müssen gleich sein
			if (wert1 != wert2) {
				System.out.println(m.convertToString("Summe1(" + wert1 + ") != Summe2(" + wert2 + ")", 5, false));
				System.out.println(m.convertToString("Summe1(" + wert1 + ") != Summe2(" + wert2 + ")", 5, true));
				fail("Summe1(" + wert1 + ") != Summe2(" + wert2 + ")");
			}

		}

	}

	private long recursive_min_sum_r(long[][] matrix, int r, boolean[] usedC) {
		if (r == matrix.length)
			return 0;

		long min = Long.MAX_VALUE;
		for (int c = 0; c < matrix[r].length; c++) {
			if (!usedC[c]) {
				usedC[c] = true;
				long value = matrix[r][c] + recursive_min_sum_r(matrix, r + 1, usedC);
				min = Math.min(value, min);
				usedC[c] = false;
			}
		}

		return min;
	}

	private long recursive_min_sum_c(long[][] matrix, int c, boolean[] usedR) {
		if (c == matrix[0].length)
			return 0;

		long min = Long.MAX_VALUE;
		for (int r = 0; r < matrix.length; r++) {
			if (!usedR[r]) {
				usedR[r] = true;
				long value = matrix[r][c] + recursive_min_sum_c(matrix, c + 1, usedR);
				min = Math.min(value, min);
				usedR[r] = false;
			}
		}

		return min;
	}

}
