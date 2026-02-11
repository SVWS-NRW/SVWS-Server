package de.svws_nrw.core.kursblockung.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Random;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.gost.GostBlockungKursLehrer;
import de.svws_nrw.core.data.gost.GostBlockungRegel;
import de.svws_nrw.core.kursblockung.KursblockungDynDaten;
import de.svws_nrw.core.logger.LogData;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;

/**
 * Testet die Klasse {@link KursblockungDynDaten}.
 * Diese Klasse ist für die Kursblockung im Client relevant.
 */
@DisplayName("Testet KursblockungAlgorithmusPermanent und KursblockungDynDaten")
@TestMethodOrder(MethodOrderer.MethodName.class)
class KursblockungDynDatenTest {

	private static final String PFAD_DATEN_001 = "de/svws_nrw/core/kursblockung/blockung001/";
	// private static final String PFAD_DATEN_002 = "de/svws_nrw/core/kursblockung/blockung002/";

	/**
	 * Initialisierung der Core-Types
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	private static KursblockungDynDaten ladeDaten001(final long seed, final GostBlockungRegel r1) {
		// Erzeugen eines Loggers mit Consumer.
		final Logger log = new Logger();

		// Hinzufügen des Consumers, der im kritischen Fall 'fail' aufruft.
		log.addConsumer(new Consumer<LogData>() {
			@Override
			public void accept(final LogData t) {
				if (t.getLevel().compareTo(LogLevel.APP) != 0)
					fail(t.getText());
				System.out.println(t.getText());
			}
		});

		// Datei --> Kurs42Converter
		final Kurs42Converter k42Converter = new Kurs42Converter(PFAD_DATEN_001, 999, false);

		// Kurs42-Daten --> GostBlockungsdatenManager
		final GostBlockungsdatenManager manager = k42Converter.gibKursblockungInput();
		if (r1 != null)
			manager.regelAdd(r1);

		// GostBlockungsdatenManager --> KursblockungDynDaten
		final Random random = new Random(seed);
		return new KursblockungDynDaten(random, log, manager);
	}


	private static KursblockungDynDaten ladeDaten001mitFixiertenLehrkraeften(final long seed, final GostBlockungRegel r1) {
		// Erzeugen eines Loggers mit Consumer.
		final Logger log = new Logger();

		// Hinzufügen des Consumers, der im kritischen Fall 'fail' aufruft.
		log.addConsumer(new Consumer<LogData>() {
			@Override
			public void accept(final LogData t) {
				if (t.getLevel().compareTo(LogLevel.APP) != 0)
					fail(t.getText());
				System.out.println(t.getText());
			}
		});

		// Datei --> Kurs42Converter
		final Kurs42Converter k42Converter = new Kurs42Converter(PFAD_DATEN_001, 999, false);

		// Kurs42-Daten --> GostBlockungsdatenManager
		final GostBlockungsdatenManager manager = k42Converter.gibKursblockungInput();
		if (r1 != null)
			manager.regelAdd(r1);

		// Fixiere Lehrkraft in der selben Schiene
		/*
		 Schiene 14
			ID 48, VT;GK
			ID 0, BI;GK
			ID 26, M;GK
			ID 27, M;GK
			ID 44, VT;GK
		 */
		final GostBlockungKursLehrer l1 = new GostBlockungKursLehrer();
		l1.kuerzel = "BAR";
		l1.id = 1;
		manager.kursAddLehrkraft(26, l1);
		manager.kursAddLehrkraft(27, l1);


		// GostBlockungsdatenManager --> KursblockungDynDaten
		final Random random = new Random(seed);
		return new KursblockungDynDaten(random, log, manager);
	}

	private static void assertKeineNegativeBewertung(final KursblockungDynDaten dd1) {
		assertTrue(dd1.gibStatistik().gibBewertungRegelverletzungen() >= 0);
		assertTrue(dd1.gibStatistik().gibBewertungNichtwahlen() >= 0);
		assertTrue(dd1.gibStatistik().gibBewertungKursdifferenz() >= 0);
		assertTrue(dd1.gibStatistik().gibBewertungFachartPaar() >= 0);
	}

	/**
	 * Testet Grundannahmen für die geladene Blockung.
	 */
	@Test
	@DisplayName("test_regel_00_Grundannahmen")
	void test_regel_00_Grundannahmen() {
		final KursblockungDynDaten dd = ladeDaten001(1, null);
		assertTrue(dd.gibIstKursInSchiene(45, 1));
		assertTrue(dd.gibIstKursInSchiene(62, 10)); // 62=M;LK
		assertTrue(dd.gibIstKursInSchiene(63, 13)); // M;LK
		assertKeineNegativeBewertung(dd);

		// Nach der Verschiebung sollte der Kurs in Schiene 2 sein.
		dd.aktionSetzeKursInSchiene(45, 2);
		assertFalse(dd.gibIstKursInSchiene(45, 1));
		assertTrue(dd.gibIstKursInSchiene(45, 2));
		assertKeineNegativeBewertung(dd);
	}

	/**
	 * Testet die Regel: {@link GostKursblockungRegelTyp#KURSART_SPERRE_SCHIENEN_VON_BIS}
	 */
	@Test
	@DisplayName("test_regel_01_KURSART_SPERRE_SCHIENEN_VON_BIS")
	void test_regel_01_KURSART_SPERRE_SCHIENEN_VON_BIS() {
		// Sperre GK in Schiene 1.
		final GostBlockungRegel regel1 = new GostBlockungRegel();
		regel1.id = 1;
		regel1.typ = GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ;
		regel1.parameter.add(Long.valueOf(GostKursart.GK.id));
		regel1.parameter.add(Long.valueOf(1));
		regel1.parameter.add(Long.valueOf(1));
		final KursblockungDynDaten dd1 = ladeDaten001(1, regel1);
		assertFalse(dd1.gibIstKursInSchiene(45, 1));
		assertKeineNegativeBewertung(dd1);

		// Verschiebe nun K-45 nach Schiene 1.
		dd1.aktionSetzeKursInSchiene(45, 1);
		assertFalse(dd1.gibIstKursInSchiene(45, 1));
		assertKeineNegativeBewertung(dd1);
	}

	/**
	 * Testet die Regel: {@link GostKursblockungRegelTyp#KURS_FIXIERE_IN_SCHIENE}
	 */
	@Test
	@DisplayName("test_regel_02_KURS_FIXIERE_IN_SCHIENE")
	void test_regel_02_KURS_FIXIERE_IN_SCHIENE() {
		// Fixierung K-45 in Schiene 1.
		final GostBlockungRegel regel1 = new GostBlockungRegel();
		regel1.id = 1;
		regel1.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
		regel1.parameter.add(Long.valueOf(45));
		regel1.parameter.add(Long.valueOf(1));
		final KursblockungDynDaten dd1 = ladeDaten001(1, regel1);
		assertTrue(dd1.gibIstKursInSchiene(45, 1));
		assertKeineNegativeBewertung(dd1);

		// Verschieben nun nach Schiene 2.
		dd1.aktionSetzeKursInSchiene(45, 2);
		assertFalse(dd1.gibIstKursInSchiene(45, 2));
		assertTrue(dd1.gibIstKursInSchiene(45, 1));
		assertKeineNegativeBewertung(dd1);

		// Verschieben nun nach Schiene 1.
		dd1.aktionSetzeKursInSchiene(45, 1);
		assertTrue(dd1.gibIstKursInSchiene(45, 1));
		assertKeineNegativeBewertung(dd1);
	}

	/**
	 * Testet die Regel: {@link GostKursblockungRegelTyp#KURS_SPERRE_IN_SCHIENE}
	 */
	@Test
	@DisplayName("test_regel_03_KURS_SPERRE_IN_SCHIENE")
	void test_regel_03_KURS_SPERRE_IN_SCHIENE() {
		// Sperre K-45 in Schiene-1.
		final GostBlockungRegel regel1 = new GostBlockungRegel();
		regel1.id = 1;
		regel1.typ = GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ;
		regel1.parameter.add(Long.valueOf(45));
		regel1.parameter.add(Long.valueOf(1));
		final KursblockungDynDaten dd1 = ladeDaten001(1, regel1);
		assertFalse(dd1.gibIstKursInSchiene(45, 1));
		assertKeineNegativeBewertung(dd1);

		// Verschiebe nun nach Schiene 1
		dd1.aktionSetzeKursInSchiene(45, 1);
		assertFalse(dd1.gibIstKursInSchiene(45, 1));
		assertKeineNegativeBewertung(dd1);

		// Verschieben nun nach Schiene 3.
		dd1.aktionSetzeKursInSchiene(45, 3);
		assertTrue(dd1.gibIstKursInSchiene(45, 3));
		assertKeineNegativeBewertung(dd1);
	}

	/**
	 * Testet die Regel: {@link GostKursblockungRegelTyp#SCHUELER_FIXIEREN_IN_KURS}
	 * <br> Kurs in Schiene 1 gesperrt und wir versuchen nach Schiene 1 zu verschieben.
	 */
	@Test
	@DisplayName("test_regel_04_SCHUELER_FIXIEREN_IN_KURS")
	void test_regel_04_SCHUELER_FIXIEREN_IN_KURS() {
		// ##### Variante 1 - Grundannahmen #####
		final KursblockungDynDaten dd1 = ladeDaten001(1, null);
		dd1.aktionSchuelerVerteilenMitBipartitemMatching();
		assertTrue(dd1.gibIstKursInSchiene(62, 10)); // M;LK (62)
		assertTrue(dd1.gibIstKursInSchiene(63, 13)); // M;LK (63)
		assertTrue(dd1.gibIstSchuelerInKurs(17, 62)); // S-17 ist in K-62 (M;LK)
		assertKeineNegativeBewertung(dd1);

		// ##### Variante 2 - Fixiere S-17 in K-63 #####
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = 1;
		regel.typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ;
		regel.parameter.add(Long.valueOf(17));
		regel.parameter.add(Long.valueOf(63));
		final KursblockungDynDaten dd2 = ladeDaten001(1, regel);
		dd2.aktionSchuelerVerteilenMitBipartitemMatching();
		assertTrue(dd2.gibIstKursInSchiene(62, 10)); // M;LK (62)
		assertTrue(dd2.gibIstKursInSchiene(63, 13)); // M;LK (63)
		assertFalse(dd2.gibIstSchuelerInKurs(17, 62)); // S-17 darf nicht in K-62 sein
		// Es ist nicht garantiert, dass S-17 dann in K-63 ist, er könnte auch eine Nichtwahl haben.
		assertKeineNegativeBewertung(dd2);
	}

	/**
	 * Testet die Regel: {@link GostKursblockungRegelTyp#SCHUELER_VERBIETEN_IN_KURS}
	 * <br> Kurs in Schiene 1 gesperrt und wir versuchen nach Schiene 1 zu verschieben.
	 */
	@Test
	@DisplayName("test_regel_05_SCHUELER_VERBIETEN_IN_KURS")
	void test_regel_05_SCHUELER_VERBIETEN_IN_KURS() {
		// ##### Variante 1 - Grundannahmen #####
		final KursblockungDynDaten dd1 = ladeDaten001(1, null);
		dd1.aktionSchuelerVerteilenMitBipartitemMatching();
		assertTrue(dd1.gibIstKursInSchiene(62, 10)); // M;LK (62)
		assertTrue(dd1.gibIstKursInSchiene(63, 13)); // M;LK (63)
		assertTrue(dd1.gibIstSchuelerInKurs(17, 62)); // S-17 ist in K-62 (M;LK)
		assertKeineNegativeBewertung(dd1);

		// ##### Variante 2 - Fixiere S-17 in K-63 #####
		// Es ist nicht garantiet, dass S-17 dann in K-62 ist.
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = 1;
		regel.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ;
		regel.parameter.add(Long.valueOf(17));
		regel.parameter.add(Long.valueOf(62));
		final KursblockungDynDaten dd2 = ladeDaten001(1, regel);
		dd2.aktionSchuelerVerteilenMitBipartitemMatching();
		assertTrue(dd2.gibIstKursInSchiene(62, 10)); // M;LK (62)
		assertTrue(dd2.gibIstKursInSchiene(63, 13)); // M;LK (63)
		assertFalse(dd2.gibIstSchuelerInKurs(17, 62)); // S-17 darf nicht in K-62 sein
		assertKeineNegativeBewertung(dd2);
	}

	/**
	 * Testet die Regel: {@link GostKursblockungRegelTyp#KURSART_ALLEIN_IN_SCHIENEN_VON_BIS}
	 */
	@Test
	@DisplayName("test_regel_06_KURSART_ALLEIN_IN_SCHIENEN_VON_BIS")
	void test_regel_06_KURSART_ALLEIN_IN_SCHIENEN_VON_BIS() {
		final GostBlockungRegel regel1 = new GostBlockungRegel();
		regel1.id = 1;
		regel1.typ = GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ;
		regel1.parameter.add(Long.valueOf(GostKursart.LK.id));
		regel1.parameter.add(Long.valueOf(1));
		regel1.parameter.add(Long.valueOf(5));
		final KursblockungDynDaten dd1 = ladeDaten001(1, regel1);

		// Schienen 1 bis  5 darf nur LKs haben.
		for (int schienenNr = 1; schienenNr <= 5; schienenNr++)
			assertTrue(dd1.gibHatSchieneNurLK(schienenNr));

		// Schienen 5 bis 14 darf keine LKs sein.
		for (int schienenNr = 6; schienenNr <= 14; schienenNr++)
			assertTrue(dd1.gibHatSchieneKeineLK(schienenNr));

		assertKeineNegativeBewertung(dd1);
	}

	/**
	 * Testet die Regel: {@link GostKursblockungRegelTyp#KURS_VERBIETEN_MIT_KURS}
	 */
	@Test
	@DisplayName("test_regel_07_KURS_VERBIETEN_MIT_KURS")
	void test_regel_07_KURS_VERBIETEN_MIT_KURS() {
		/*
		##### Anfangskonfiguration mit Seed = 1 #####
		Schiene 1
		    ID 64, PH;LK
		    ID 57, EK;LK
		    ID 45, D;GK
		Schiene 2
		    ID 33, S0;GK
		    ID 23, L8;GK
		    ID 11, E;GK
		 */
		final GostBlockungRegel regel1 = new GostBlockungRegel();
		regel1.id = 1;
		regel1.typ = GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ;
		regel1.parameter.add(Long.valueOf(64));
		regel1.parameter.add(Long.valueOf(57));
		final KursblockungDynDaten dd1 = ladeDaten001(1, regel1);

		// Da die Kurse zusammen sind in Schiene 1, muss es genau eine Regelverletzung geben.
		assertEquals(1, dd1.gibStatistik().gibBewertungRegelverletzungen());
		assertKeineNegativeBewertung(dd1);

		// Nach dem Verschieben, müssen die Regelverletzungen 0 sein.
		dd1.aktionSetzeKursInSchiene(64, 2);
		assertEquals(0, dd1.gibStatistik().gibBewertungRegelverletzungen());
		assertKeineNegativeBewertung(dd1);

		// Nach dem Verschieben, müssen die Regelverletzungen wieder 1 sein.
		dd1.aktionSetzeKursInSchiene(57, 2);
		assertEquals(1, dd1.gibStatistik().gibBewertungRegelverletzungen());
		assertKeineNegativeBewertung(dd1);
	}

	/**
	 * Testet die Regel: {@link GostKursblockungRegelTyp#KURS_ZUSAMMEN_MIT_KURS}
	 */
	@Test
	@DisplayName("test_regel_08_KURS_ZUSAMMEN_MIT_KURS")
	void test_regel_08_KURS_ZUSAMMEN_MIT_KURS() {
		/*
		##### Anfangskonfiguration mit Seed = 1 #####
		Schiene 1
		    ID 64, PH;LK
		    ID 57, EK;LK
		    ID 45, D;GK
		Schiene 2
		    ID 33, S0;GK
		    ID 23, L8;GK
		    ID 11, E;GK
		 */
		final GostBlockungRegel regel1 = new GostBlockungRegel();
		regel1.id = 1;
		regel1.typ = GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ;
		regel1.parameter.add(Long.valueOf(64));
		regel1.parameter.add(Long.valueOf(57));
		final KursblockungDynDaten dd1 = ladeDaten001(1, regel1);

		// Da die Kurse zusammen sind in Schiene 1, darf es derzeit keine Regelverletzung geben.
		assertEquals(0, dd1.gibStatistik().gibBewertungRegelverletzungen());
		assertKeineNegativeBewertung(dd1);

		// Nach dem Verschieben, müssen die Regelverletzungen 1 sein.
		dd1.aktionSetzeKursInSchiene(64, 2);
		assertEquals(1, dd1.gibStatistik().gibBewertungRegelverletzungen());
		assertKeineNegativeBewertung(dd1);

		// Nach dem Verschieben, müssen die Regelverletzungen wieder 0 sein.
		dd1.aktionSetzeKursInSchiene(57, 2);
		assertEquals(0, dd1.gibStatistik().gibBewertungRegelverletzungen());
		assertKeineNegativeBewertung(dd1);
	}

	/**
	 * Testet die Regel: {@link GostKursblockungRegelTyp#KURS_MIT_DUMMY_SUS_AUFFUELLEN}
	 */
	@Test
	@DisplayName("test_regel_09_KURS_MIT_DUMMY_SUS_AUFFUELLEN")
	void test_regel_09_KURS_MIT_DUMMY_SUS_AUFFUELLEN() {
		/*
			M;GK
			    Kurs (dbID=26, iID=14, SuS = 0) in Schiene 14
			    Kurs (dbID=27, iID=15, SuS = 0) in Schiene 14
			    Kurs (dbID=25, iID=17, SuS = 0) in Schiene  7
		 */

		// Es wurden keine SuS verteilt. Im D-GK sind dann 0 SuS.
		final KursblockungDynDaten dd1 = ladeDaten001(1, null);
		assertEquals(0, dd1.gibKursgroesseDesKurses(25));
		assertEquals(0, dd1.gibKursdifferenzDesKurses(25));
		assertKeineNegativeBewertung(dd1);

		// Es wurden 5 Dummy-SuS verteilt. Im D-GK sind dann 5 SuS.
		final GostBlockungRegel regel1 = new GostBlockungRegel();
		regel1.id = 1;
		regel1.typ = GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ;
		regel1.parameter.add(Long.valueOf(25));
		regel1.parameter.add(Long.valueOf(5));
		final KursblockungDynDaten dd2 = ladeDaten001(1, regel1);
		assertEquals(5, dd2.gibKursgroesseDesKurses(25));
		assertEquals(5, dd2.gibKursdifferenzDesKurses(25));
		assertKeineNegativeBewertung(dd2);
	}

	/**
	 * Testet die Regel: {@link GostKursblockungRegelTyp#LEHRKRAEFTE_BEACHTEN}
	 */
	@Test
	@DisplayName("test_regel_10_LEHRKRAEFTE_BEACHTEN")
	void test_regel_10_LEHRKRAEFTE_BEACHTEN() {
		// Es wurden noch keine SuS verteilt.
		final KursblockungDynDaten dd1 = ladeDaten001(1, null);
		assertTrue(dd1.gibIstKursInSchiene(26, 14));
		assertTrue(dd1.gibIstKursInSchiene(27, 14));
		assertKeineNegativeBewertung(dd1);

		// Dieses mal sollen Lehrkräfte beachtet werden.
		final GostBlockungRegel regel1 = new GostBlockungRegel();
		regel1.id = 1;
		regel1.typ = GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN.typ;
		final KursblockungDynDaten dd2 = ladeDaten001mitFixiertenLehrkraeften(1, regel1);
		/*
		 Schiene 14
			ID 48, VT;GK
			ID 0, BI;GK
			ID 26, M;GK   <-- BAR
			ID 27, M;GK   <-- BAR
			ID 44, VT;GK
		 */

		// Da die Kurse zusammen sind in Schiene 14, muss es genau eine Regelverletzung geben.
		assertEquals(1, dd2.gibStatistik().gibBewertungRegelverletzungen());
		assertKeineNegativeBewertung(dd2);

		// Nach dem Verschieben, gibt keine 0 Regelverletzungen.
		dd2.aktionSetzeKursInSchiene(26, 13);
		assertEquals(0, dd2.gibStatistik().gibBewertungRegelverletzungen());
		assertKeineNegativeBewertung(dd2);

		// Nach dem Verschieben, gibt es wieder 1 Regelverletzung.
		dd2.aktionSetzeKursInSchiene(27, 13);
		assertEquals(1, dd2.gibStatistik().gibBewertungRegelverletzungen());
		assertKeineNegativeBewertung(dd2);
	}

	/**
	 * Testet die Regel: {@link GostKursblockungRegelTyp#SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH}
	 */
	@Test
	@DisplayName("test_regel_11_SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH")
	void test_regel_11_SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH() {
		/*
		M;GK (Fach-ID = 14)
		    Kurs (dbID=26, iID=14, SuS = 0) in Schiene 14
		    Kurs (dbID=27, iID=15, SuS = 0) in Schiene 14
		    Kurs (dbID=25, iID=17, SuS = 0) in Schiene  7
		*/
		final GostBlockungRegel regel1 = new GostBlockungRegel();
		regel1.id = 1;
		regel1.typ = GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ;
		regel1.parameter.add(Long.valueOf(2));
		regel1.parameter.add(Long.valueOf(9));
		regel1.parameter.add(Long.valueOf(14));

		// Setze Schüler 2 und 9 zusammen in Kurs 25.
		final KursblockungDynDaten dd1 = ladeDaten001(1, regel1);
		dd1.aktionSchuelerSetzenInKurs(2, 25);
		dd1.aktionSchuelerSetzenInKurs(9, 25);
		assertKeineNegativeBewertung(dd1);
		assertTrue(dd1.gibIstSchuelerInKurs(2, 25));
		assertTrue(dd1.gibIstSchuelerInKurs(9, 25));

		// Schüler 9 darf nicht wechseln, da der Algorithmus verbietet, dass die SuS getrennt werden.
		dd1.aktionSchuelerSetzenInKurs(9, 26);
		assertKeineNegativeBewertung(dd1);
		assertTrue(dd1.gibIstSchuelerInKurs(2, 25));
		assertTrue(dd1.gibIstSchuelerInKurs(9, 25));
	}

	/**
	 * Testet die Regel: {@link GostKursblockungRegelTyp#SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH}
	 */
	@Test
	@DisplayName("test_regel_12_SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH")
	void test_regel_12_SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH() {
		/*
		M;GK (Fach-ID = 14)
		    Kurs (dbID=26, iID=14, SuS = 0) in Schiene 14
		    Kurs (dbID=27, iID=15, SuS = 0) in Schiene 14
		    Kurs (dbID=25, iID=17, SuS = 0) in Schiene  7
		*/
		final GostBlockungRegel regel1 = new GostBlockungRegel();
		regel1.id = 1;
		regel1.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ;
		regel1.parameter.add(Long.valueOf(2));
		regel1.parameter.add(Long.valueOf(9));
		regel1.parameter.add(Long.valueOf(14));

		// Setze Schüler 2, dann 9 in Kurs 25 --> Der Algorithmus verwehrt es dann Schüler 9.
		final KursblockungDynDaten dd1 = ladeDaten001(1, regel1);
		dd1.aktionSchuelerSetzenInKurs(2, 25);
		dd1.aktionSchuelerSetzenInKurs(9, 25);
		assertKeineNegativeBewertung(dd1);
		assertTrue(dd1.gibIstSchuelerInKurs(2, 25));
		assertFalse(dd1.gibIstSchuelerInKurs(9, 25));

		// Aber Schüler 9 darf in Kurs 26.
		dd1.aktionSchuelerSetzenInKurs(9, 26);
		assertKeineNegativeBewertung(dd1);
		assertTrue(dd1.gibIstSchuelerInKurs(2, 25));
		assertTrue(dd1.gibIstSchuelerInKurs(9, 26));
	}

	/**
	 * Testet die Regel: {@link GostKursblockungRegelTyp#SCHUELER_ZUSAMMEN_MIT_SCHUELER}
	 */
	@Test
	@DisplayName("test_regel_13_SCHUELER_ZUSAMMEN_MIT_SCHUELER")
	void test_regel_13_SCHUELER_ZUSAMMEN_MIT_SCHUELER() {
		/*
		M;GK (Fach-ID = 14)
		    Kurs (dbID=26, iID=14, SuS = 0) in Schiene 14
		    Kurs (dbID=27, iID=15, SuS = 0) in Schiene 14
		    Kurs (dbID=25, iID=17, SuS = 0) in Schiene  7
		*/
		final GostBlockungRegel regel1 = new GostBlockungRegel();
		regel1.id = 1;
		regel1.typ = GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ;
		regel1.parameter.add(Long.valueOf(2));
		regel1.parameter.add(Long.valueOf(9));

		// Setze Schüler 2 und 9 zusammen in Kurs 25.
		final KursblockungDynDaten dd1 = ladeDaten001(1, regel1);
		dd1.aktionSchuelerSetzenInKurs(2, 25);
		dd1.aktionSchuelerSetzenInKurs(9, 25);
		assertKeineNegativeBewertung(dd1);
		assertTrue(dd1.gibIstSchuelerInKurs(2, 25));
		assertTrue(dd1.gibIstSchuelerInKurs(9, 25));

		// Schüler 9 darf nicht wechseln, da der Algorithmus verbietet, dass die SuS getrennt werden.
		dd1.aktionSchuelerSetzenInKurs(9, 26);
		assertKeineNegativeBewertung(dd1);
		assertTrue(dd1.gibIstSchuelerInKurs(2, 25));
		assertTrue(dd1.gibIstSchuelerInKurs(9, 25));
	}

	/**
	 * Testet die Regel: {@link GostKursblockungRegelTyp#SCHUELER_VERBIETEN_MIT_SCHUELER}
	 */
	@Test
	@DisplayName("test_regel_14_SCHUELER_VERBIETEN_MIT_SCHUELER")
	void test_regel_14_SCHUELER_VERBIETEN_MIT_SCHUELER() {
		/*
		 Kurs I0;GK (dbID=18, iID=46, SuS = 12, Fach-ID=9) --> gibt es nur einen von
		    ID 15, Schüler 15
		    ID 22, Schüler 22
		    ID 31, Schüler 31
		    ID 34, Schüler 34
		    ID 41, Schüler 41
		    ID 56, Schüler 56
		    ID 61, Schüler 61
		    ID 75, Schüler 75
		    ID 102, Schüler 102
		    ID 113, Schüler 113
		    ID 114, Schüler 114
		    ID 116, Schüler 116
		 */

		final GostBlockungRegel regel1 = new GostBlockungRegel();
		regel1.id = 1;
		regel1.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ;
		regel1.parameter.add(Long.valueOf(15));
		regel1.parameter.add(Long.valueOf(22));

		// Setze Schüler 15, dann 22 in Kurs 18 --> Der Algorithmus verwehrt es dann Schüler 22.
		final KursblockungDynDaten dd1 = ladeDaten001(1, regel1);
		dd1.aktionSchuelerSetzenInKurs(15, 18);
		dd1.aktionSchuelerSetzenInKurs(22, 18);
		assertKeineNegativeBewertung(dd1);
		assertTrue(dd1.gibIstSchuelerInKurs(15, 18));
		assertFalse(dd1.gibIstSchuelerInKurs(22, 18));
	}

	/**
	 * Testet die Regel: {@link GostKursblockungRegelTyp#KURS_MAXIMALE_SCHUELERANZAHL}
	 */
	@Test
	@DisplayName("test_regel_15_KURS_MAXIMALE_SCHUELERANZAHL")
	void test_regel_15_KURS_MAXIMALE_SCHUELERANZAHL() {
		/*
		 Kurs I0;GK  (dbID=18, iID=46, SuS = 12, Fach-ID=9) --> gibt es nur einen von
		    ID 15, Schüler 15
		    ID 22, Schüler 22
		    ID 31, Schüler 31
		    ID 34, Schüler 34
		    ID 41, Schüler 41
		    ID 56, Schüler 56
		    ID 61, Schüler 61
		    ID 75, Schüler 75
		    ID 102, Schüler 102
		    ID 113, Schüler 113
		    ID 114, Schüler 114
		    ID 116, Schüler 116
		 */

		final GostBlockungRegel regel1 = new GostBlockungRegel();
		regel1.id = 1;
		regel1.typ = GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ;
		regel1.parameter.add(Long.valueOf(18));
		regel1.parameter.add(Long.valueOf(2));

		// 0 SuS
		final KursblockungDynDaten dd1 = ladeDaten001(1, regel1);
		assertEquals(0, dd1.gibKursgroesseDesKurses(18));
		assertKeineNegativeBewertung(dd1);

		// 1 SuS
		dd1.aktionSchuelerSetzenInKurs(15, 18);
		assertEquals(1, dd1.gibKursgroesseDesKurses(18));
		assertKeineNegativeBewertung(dd1);

		// 2 SuS
		dd1.aktionSchuelerSetzenInKurs(22, 18);
		assertEquals(2, dd1.gibKursgroesseDesKurses(18));
		assertKeineNegativeBewertung(dd1);

		// 3 SuS --> 2 SuS (da die Obergrenze erreicht wurde)
		dd1.aktionSchuelerSetzenInKurs(31, 18);
		assertEquals(2, dd1.gibKursgroesseDesKurses(18));
		assertKeineNegativeBewertung(dd1);

		// 1 SuS
		dd1.aktionSchuelerEntfernenAusKurs(22, 18);
		assertEquals(1, dd1.gibKursgroesseDesKurses(18));
		assertKeineNegativeBewertung(dd1);

		// 2 SuS
		dd1.aktionSchuelerSetzenInKurs(31, 18);
		assertEquals(2, dd1.gibKursgroesseDesKurses(18));
		assertKeineNegativeBewertung(dd1);
	}

	/**
	 * Testet die Regel: {@link GostKursblockungRegelTyp#SCHUELER_IGNORIEREN}
	 */
	@Test
	@DisplayName("test_regel_16_SCHUELER_IGNORIEREN")
	void test_regel_16_SCHUELER_IGNORIEREN() {
		/*
		 Kurs I0;GK  (dbID=18, iID=46, SuS = 12, Fach-ID=9) --> gibt es nur einen von
		    ID 15, Schüler 15
		    ID 22, Schüler 22
		    ID 31, Schüler 31
		    ID 34, Schüler 34
		    ID 41, Schüler 41
		    ID 56, Schüler 56
		    ID 61, Schüler 61
		    ID 75, Schüler 75
		    ID 102, Schüler 102
		    ID 113, Schüler 113
		    ID 114, Schüler 114
		    ID 116, Schüler 116
		 */

		final GostBlockungRegel regel1 = new GostBlockungRegel();
		regel1.id = 1;
		regel1.typ = GostKursblockungRegelTyp.SCHUELER_IGNORIEREN.typ;
		regel1.parameter.add(Long.valueOf(15));

		// 0 SuS
		final KursblockungDynDaten dd1 = ladeDaten001(1, regel1);
		assertEquals(0, dd1.gibKursgroesseDesKurses(18));
		assertKeineNegativeBewertung(dd1);

		// 1 SuS --> 0 SuS (da Schüler 15 nicht verteilt werden darf).
		dd1.aktionSchuelerSetzenInKurs(15, 18);
		assertEquals(0, dd1.gibKursgroesseDesKurses(18));
		assertKeineNegativeBewertung(dd1);
	}

	/**
	 * Testet die Regel: {@link GostKursblockungRegelTyp#KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN}
	 */
	@Test
	@DisplayName("test_regel_17_KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN")
	void test_regel_17_KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN() {
		/*
		 Kurs I0;GK  (dbID=18, iID=46, SuS = 12, Fach-ID=9) --> gibt es nur einen von
		    ID 15, Schüler 15
		    ID 22, Schüler 22
		    ID 31, Schüler 31
		    ID 34, Schüler 34
		    ID 41, Schüler 41
		    ID 56, Schüler 56
		    ID 61, Schüler 61
		    ID 75, Schüler 75
		    ID 102, Schüler 102
		    ID 113, Schüler 113
		    ID 114, Schüler 114
		    ID 116, Schüler 116
		 */

		final GostBlockungRegel regel1 = new GostBlockungRegel();
		regel1.id = 1;
		regel1.typ = GostKursblockungRegelTyp.KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN.typ;
		regel1.parameter.add(Long.valueOf(15));
		final KursblockungDynDaten dd1 = ladeDaten001(1, regel1);
		assertKeineNegativeBewertung(dd1);
		// Diese Regel wird nur geparsed, aber intern beim Algorithmus nicht beachtet.
	}


	/**
	 * Testet die Regel: {@link GostKursblockungRegelTyp#FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE}
	 */
	@Test
	@DisplayName("test_regel_18_FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE")
	void test_regel_18_FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE() {
		/*
		BI;GK / 0;2
    		Kurs (dbID=0, iID=68, SuS = 0, Schienen = 14)
    		Kurs (dbID=1, iID=62, SuS = 0, Schienen = 5)
    		Kurs (dbID=2, iID=63, SuS = 0, Schienen = 4)
    		Kurs (dbID=3, iID=61, SuS = 0, Schienen = 12)
		 */

		final GostBlockungRegel regel1 = new GostBlockungRegel();
		regel1.id = 1;
		regel1.typ = GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE.typ;
		regel1.parameter.add(Long.valueOf(0)); // BI
		regel1.parameter.add(Long.valueOf(2)); // GK
		regel1.parameter.add(Long.valueOf(2)); // max 2
		final KursblockungDynDaten dd1 = ladeDaten001(1, regel1);
		assertTrue(dd1.gibIstKursInSchiene(0, 14));
		assertTrue(dd1.gibIstKursInSchiene(1, 5));
		assertTrue(dd1.gibIstKursInSchiene(2, 4));
		assertTrue(dd1.gibIstKursInSchiene(3, 12));
		assertEquals(0, dd1.gibStatistik().gibBewertungRegelverletzungen());
		assertKeineNegativeBewertung(dd1);

		dd1.aktionSetzeKursInSchiene(1, 14);
		assertTrue(dd1.gibIstKursInSchiene(0, 14));
		assertTrue(dd1.gibIstKursInSchiene(1, 14));
		assertTrue(dd1.gibIstKursInSchiene(2, 4));
		assertTrue(dd1.gibIstKursInSchiene(3, 12));
		assertEquals(0, dd1.gibStatistik().gibBewertungRegelverletzungen());
		assertKeineNegativeBewertung(dd1);

		dd1.aktionSetzeKursInSchiene(2, 14);
		assertTrue(dd1.gibIstKursInSchiene(0, 14));
		assertTrue(dd1.gibIstKursInSchiene(1, 14));
		assertTrue(dd1.gibIstKursInSchiene(2, 14));
		assertTrue(dd1.gibIstKursInSchiene(3, 12));
		assertEquals(1, dd1.gibStatistik().gibBewertungRegelverletzungen());
		assertKeineNegativeBewertung(dd1);

		dd1.aktionSetzeKursInSchiene(3, 14);
		assertTrue(dd1.gibIstKursInSchiene(0, 14));
		assertTrue(dd1.gibIstKursInSchiene(1, 14));
		assertTrue(dd1.gibIstKursInSchiene(2, 14));
		assertTrue(dd1.gibIstKursInSchiene(3, 14));
		assertEquals(2, dd1.gibStatistik().gibBewertungRegelverletzungen());
		assertKeineNegativeBewertung(dd1);

		dd1.aktionSetzeKursInSchiene(3, 13);
		assertTrue(dd1.gibIstKursInSchiene(0, 14));
		assertTrue(dd1.gibIstKursInSchiene(1, 14));
		assertTrue(dd1.gibIstKursInSchiene(2, 14));
		assertTrue(dd1.gibIstKursInSchiene(3, 13));
		assertEquals(1, dd1.gibStatistik().gibBewertungRegelverletzungen());
		assertKeineNegativeBewertung(dd1);

		dd1.aktionSetzeKursInSchiene(2, 13);
		assertTrue(dd1.gibIstKursInSchiene(0, 14));
		assertTrue(dd1.gibIstKursInSchiene(1, 14));
		assertTrue(dd1.gibIstKursInSchiene(2, 13));
		assertTrue(dd1.gibIstKursInSchiene(3, 13));
		assertEquals(0, dd1.gibStatistik().gibBewertungRegelverletzungen());
		assertKeineNegativeBewertung(dd1);

		dd1.aktionSetzeKursInSchiene(1, 13);
		assertTrue(dd1.gibIstKursInSchiene(0, 14));
		assertTrue(dd1.gibIstKursInSchiene(1, 13));
		assertTrue(dd1.gibIstKursInSchiene(2, 13));
		assertTrue(dd1.gibIstKursInSchiene(3, 13));
		assertEquals(1, dd1.gibStatistik().gibBewertungRegelverletzungen());
		assertKeineNegativeBewertung(dd1);

		// M-GK sind anfangs 2 in Schiene 14 und einer in Schiene 7
		final GostBlockungRegel regel2 = new GostBlockungRegel();
		regel2.id = 1;
		regel2.typ = GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE.typ;
		regel2.parameter.add(Long.valueOf(14)); // M
		regel2.parameter.add(Long.valueOf(2)); // GK
		regel2.parameter.add(Long.valueOf(1)); // max 1
		final KursblockungDynDaten dd2 = ladeDaten001(1, regel2);
		assertEquals(1, dd2.gibStatistik().gibBewertungRegelverletzungen());

		dd2.aktionSetzeKursInSchiene(26, 8);
		assertEquals(0, dd2.gibStatistik().gibBewertungRegelverletzungen());

		dd2.aktionSetzeKursInSchiene(26, 7);
		assertEquals(1, dd2.gibStatistik().gibBewertungRegelverletzungen());
	}

}
