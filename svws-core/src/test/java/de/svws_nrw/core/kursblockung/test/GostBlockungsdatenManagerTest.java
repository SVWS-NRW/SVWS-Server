package de.svws_nrw.core.kursblockung.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.svws_nrw.asd.data.schueler.Schueler;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.gost.GostBlockungsdaten;
import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungKursLehrer;
import de.svws_nrw.core.data.gost.GostBlockungRegel;
import de.svws_nrw.core.data.gost.GostBlockungSchiene;
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.exceptions.UserNotificationException;
import de.svws_nrw.core.adt.LongArrayKey;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisBewertungComparator;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager;
import de.svws_nrw.core.utils.gost.GostFaecherManager;

/**
 * Testet den {@link GostBlockungsdatenManager}.
 *
 * @author Benjamin A. Bartsch
 */
@DisplayName("Testet den {@link GostBlockungsdatenManager}.")
@TestMethodOrder(MethodOrderer.MethodName.class)
class GostBlockungsdatenManagerTest {

	/**
	 * Initialisierung der Core-Types
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	private static final long SCHUELER_1_ID = 100;
	private static final long SCHUELER_2_ID = 200;
	private static final long SCHUELER_NICHT_VORHANDEN = 999;

	private static final long FACH_D_ID = 1;
	private static final long FACH_M_ID = 2;
	private static final long FACH_E_ID = 3;
	private static final long FACH_NICHT_VORHANDEN = 999;

	private static final int KURSART_GK = GostKursart.GK.id;
	private static final int KURSART_LK = GostKursart.LK.id;
	private static final int KURSART_UNGUELTIG = 99;

	private static final long KURS_ID_1 = 1;
	private static final long KURS_ID_2 = 2;
	private static final long KURS_ID_3 = 3;
	private static final long KURS_ID_4 = 4;
	private static final long KURS_ID_5 = 5;
	private static final long KURS_ID_6 = 6;
	private static final long KURS_ID_7 = 7;
	private static final long KURS_ID_8 = 8;
	private static final int KURS_NR_1 = 1;
	private static final int KURS_NR_2 = 2;
	private static final int KURS_NR_6 = 6;
	private static final int KURS_NR_7 = 7;
	private static final int KURS_NR_8 = 8;
	private static final long KURS_NICHT_VORHANDEN = 999;

	private static final long LEHRER_ID_1 = 10;
	private static final long LEHRER_ID_2 = 20;
	private static final int LEHRER_NICHT_VORHANDEN = 999;
	private static final int LEHRER_REIHENFOLGE_1 = 1;
	private static final int LEHRER_REIHENFOLGE_2 = 2;
	private static final int LEHRER_REIHENFOLGE_NICHT_VORHANDEN = 999;

	private static final long SCHIENE_ID_1 = 1;
	private static final long SCHIENE_ID_2 = 2;
	private static final long SCHIENE_ID_3 = 3;
	private static final long SCHIENE_ID_4 = 4;
	private static final long SCHIENE_ID_5 = 5;
	private static final long SCHIENE_ID_6 = 6;
	private static final long SCHIENE_ID_7 = 7;
	private static final long SCHIENE_ID_8 = 8;
	private static final long SCHIENE_NICHT_VORHANDEN = 999;
	private static final int SCHIENE_NR_1 = 1;
	private static final int SCHIENE_NR_2 = 2;
	private static final int SCHIENE_NR_3 = 3;
	private static final int SCHIENE_NR_4 = 4;
	private static final int SCHIENE_NR_UNGUELTIG = 99;

	private static final long REGEL_ID_1 = 1;
	private static final long REGEL_ID_2 = 2;
	private static final long REGEL_ID_3 = 3;
	private static final long REGEL_ID_4 = 4;
	private static final long REGEL_ID_5 = 5;
	private static final long REGEL_ID_6 = 6;
	private static final long REGEL_NICHT_VORHANDEN = 999;
	private static final int REGELTYP_UNGUELTIG = 999;

	private static final long ERGEBNIS_ID_1 = 1;
	private static final long ERGEBNIS_ID_2 = 2;
	private static final long ERGEBNIS_ID_3 = 3;
	private static final long ERGEBNIS_ID_4 = 4;
	private static final long ERGEBNIS_NICHT_VORHANDEN = 999;

	private static final int HALBJAHR_EF1 = GostHalbjahr.EF1.id;
	private static final int HALBJAHR_UNGUELTIG = 999;

	// #########################################################################
	// ##########           Statische Hilfsmethoden                   ##########
	// #########################################################################

	private static GostFach createFach(final long id, final String kuerzel) {
		final GostFach fach = new GostFach();
		fach.id = id;
		fach.kuerzel = kuerzel;
		fach.kuerzelAnzeige = kuerzel;
		return fach;
	}

	private static GostBlockungsdatenManager createStandardManager() {
		final GostFaecherManager faecherManager = new GostFaecherManager(2024);
		faecherManager.add(createFach(FACH_D_ID, "D"));
		faecherManager.add(createFach(FACH_M_ID, "M"));
		faecherManager.add(createFach(FACH_E_ID, "E"));

		final GostBlockungsdaten blockungsdaten = new GostBlockungsdaten();
		blockungsdaten.id = 1;
		blockungsdaten.gostHalbjahr = HALBJAHR_EF1;
		final GostBlockungsdatenManager mgr = new GostBlockungsdatenManager(blockungsdaten, faecherManager);

		final Schueler schueler = new Schueler();
		schueler.id = SCHUELER_1_ID;
		schueler.nachname = "Test";
		schueler.vorname = "Max";
		schueler.geschlecht = Geschlecht.M.id;
		schueler.status = SchuelerStatus.AKTIV.ordinal();
		mgr.schuelerAddListe(List.of(schueler));

		return mgr;
	}

	private static GostFachwahl createFachwahl(final long schuelerID, final long fachID, final int kursartID) {
		final GostFachwahl fachwahl = new GostFachwahl();
		fachwahl.schuelerID = schuelerID;
		fachwahl.fachID = fachID;
		fachwahl.kursartID = kursartID;
		return fachwahl;
	}

	private static void addDefaultFachwahlen(final GostBlockungsdatenManager m) {
		m.fachwahlAdd(createFachwahl(SCHUELER_1_ID, FACH_D_ID, KURSART_GK));
		m.fachwahlAdd(createFachwahl(SCHUELER_1_ID, FACH_M_ID, KURSART_LK));
	}

	private static GostBlockungSchiene createSchiene(final long id, final int nummer, final String bezeichnung) {
		final GostBlockungSchiene schiene = new GostBlockungSchiene();
		schiene.id = id;
		schiene.nummer = nummer;
		schiene.bezeichnung = bezeichnung;
		return schiene;
	}

	private static GostBlockungsdatenManager createManagerMitBlockungsvorlage() {
		final GostFaecherManager faecherManager = new GostFaecherManager(2024);
		faecherManager.add(createFach(FACH_D_ID, "D"));
		faecherManager.add(createFach(FACH_M_ID, "M"));
		faecherManager.add(createFach(FACH_E_ID, "E"));

		final GostBlockungsdaten blockungsdaten = new GostBlockungsdaten();
		blockungsdaten.id = 1;
		blockungsdaten.gostHalbjahr = HALBJAHR_EF1;

		final GostBlockungSchiene schiene = createSchiene(SCHIENE_ID_1, 1, "Schiene 1");
		blockungsdaten.schienen.add(schiene);

		final GostBlockungsergebnis ergebnis = new GostBlockungsergebnis();
		ergebnis.id = 1;
		ergebnis.blockungID = blockungsdaten.id;
		ergebnis.gostHalbjahr = blockungsdaten.gostHalbjahr;
		blockungsdaten.ergebnisse.add(ergebnis);

		final GostBlockungsdatenManager mgr = new GostBlockungsdatenManager(blockungsdaten, faecherManager);

		final Schueler schueler = new Schueler();
		schueler.id = SCHUELER_1_ID;
		schueler.nachname = "Test";
		schueler.vorname = "Max";
		schueler.geschlecht = Geschlecht.M.id;
		schueler.status = SchuelerStatus.AKTIV.ordinal();
		mgr.schuelerAddListe(List.of(schueler));

		return mgr;
	}

	private static GostBlockungKurs createKurs(final long id, final long fachID, final int kursart, final int nummer) {
		final GostBlockungKurs kurs = new GostBlockungKurs();
		kurs.id = id;
		kurs.fach_id = fachID;
		kurs.kursart = kursart;
		kurs.nummer = nummer;
		return kurs;
	}

	private static GostBlockungKursLehrer createKursLehrer(final long idLehrkraft, final int reihenfolge) {
		final GostBlockungKursLehrer lehrer = new GostBlockungKursLehrer();
		lehrer.id = idLehrkraft;
		lehrer.reihenfolge = reihenfolge;
		lehrer.kuerzel = "L" + idLehrkraft;
		lehrer.nachname = "Lehrer" + idLehrkraft;
		lehrer.vorname = "Max" + idLehrkraft;
		return lehrer;
	}

	private static GostBlockungsdatenManager createManagerMitSchienen(final int anzahl) {
		final GostBlockungsdatenManager mgr = createStandardManager();
		for (int i = 1; i <= anzahl; i++) {
			mgr.schieneAdd(createSchiene(i, i, "Schiene " + i));
		}
		return mgr;
	}

	private static GostBlockungRegel createRegel(final long id, final int typ) {
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = id;
		regel.typ = typ;
		return regel;
	}

	private static GostBlockungRegel createRegel02KursFixierungInSchiene(final long id, final long kursID, final int schienenNr) {
		final GostBlockungRegel regel = createRegel(id, GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ);
		regel.parameter.add(kursID);
		regel.parameter.add((long) schienenNr);
		return regel;
	}

	private static GostBlockungRegel createRegel03KursSperreInSchiene(final long id, final long kursID, final int schienenNr) {
		final GostBlockungRegel regel = createRegel(id, GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ);
		regel.parameter.add(kursID);
		regel.parameter.add((long) schienenNr);
		return regel;
	}

	private static GostBlockungRegel createRegel04SchuelerFixiertInKurs(final long id, final long schuelerID, final long kursID) {
		final GostBlockungRegel regel = createRegel(id, GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ);
		regel.parameter.add(schuelerID);
		regel.parameter.add(kursID);
		return regel;
	}

	private static GostBlockungRegel createRegel05SchuelerVerbotenInKurs(final long id, final long schuelerID, final long kursID) {
		final GostBlockungRegel regel = createRegel(id, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ);
		regel.parameter.add(schuelerID);
		regel.parameter.add(kursID);
		return regel;
	}

	private static GostBlockungsdatenManager createManagerFuerRegelTests() {
		final GostBlockungsdatenManager mgr = createManagerMitSchienen(3);
		mgr.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		return mgr;
	}

	private static GostBlockungsdatenManager createManagerFuerRegelTypTests() {
		final GostBlockungsdatenManager mgr = createManagerMitSchienen(3);
		mgr.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		mgr.kursAdd(createKurs(KURS_ID_2, FACH_D_ID, KURSART_LK, KURS_NR_1));
		mgr.schuelerAdd(createSchueler(SCHUELER_2_ID, "Test2", "Anna"));
		addDefaultFachwahlen(mgr);
		mgr.fachwahlAdd(createFachwahl(SCHUELER_2_ID, FACH_D_ID, KURSART_GK));
		return mgr;
	}

	private static Schueler createSchueler(final long id, final String nachname, final String vorname) {
		final Schueler s = new Schueler();
		s.id = id;
		s.nachname = nachname;
		s.vorname = vorname;
		s.geschlecht = Geschlecht.W.id;
		s.status = SchuelerStatus.AKTIV.ordinal();
		return s;
	}

	private static GostBlockungsdatenManager createManagerFuerSchuelerTests() {
		final GostBlockungsdatenManager mgr = createStandardManager();
		addDefaultFachwahlen(mgr);
		return mgr;
	}

	private static GostBlockungsdatenManager createManagerMitErgebnis() {
		final GostBlockungsdatenManager mgr = createStandardManager();
		final GostBlockungsergebnis ergebnis = new GostBlockungsergebnis();
		ergebnis.id = 1;
		ergebnis.blockungID = mgr.getID();
		ergebnis.gostHalbjahr = HALBJAHR_EF1;
		mgr.ergebnisAdd(ergebnis);
		return mgr;
	}

	private static void addRegelDummySUS(final GostBlockungsdatenManager m, final long kursID, final long anzahl) {
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = kursID + 100;
		regel.typ = GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ;
		regel.parameter.add(kursID);
		regel.parameter.add(anzahl);
		m.regelAdd(regel);
	}


	// #########################################################################
	// ##########              Fachwahl-Tests                         ##########
	// #########################################################################
	@Test
	@DisplayName("testFachwahlAdd")
	void testFachwahlAdd() {
		final GostBlockungsdatenManager manager = createStandardManager();

		// Gültige Fachwahl hinzufügen
		final GostFachwahl fachwahlGueltig = createFachwahl(SCHUELER_1_ID, FACH_D_ID, KURSART_GK);
		assertDoesNotThrow(() -> manager.fachwahlAdd(fachwahlGueltig));
		assertEquals(1, manager.fachwahlGetAnzahl());

		// Fachwahl-Duplikat hinzufügen
		final GostFachwahl fachwahlDuplikat = createFachwahl(SCHUELER_1_ID, FACH_D_ID, KURSART_GK);
		assertThrows(DeveloperNotificationException.class, () -> manager.fachwahlAdd(fachwahlDuplikat));

		// Fach mit unbekanntem Fach hinzufügen
		final GostFachwahl fachwahlUnbekanntesFach = createFachwahl(SCHUELER_1_ID, FACH_NICHT_VORHANDEN, KURSART_GK);
		assertThrows(DeveloperNotificationException.class, () -> manager.fachwahlAdd(fachwahlUnbekanntesFach));
	}

	@Test
	@DisplayName("testFachwahlAddListe")
	void testFachwahlAddListe() {
		final GostBlockungsdatenManager manager = createStandardManager();

		// Leere Liste hinzufügen
		final List<GostFachwahl> emptyList = new ArrayList<>();
		assertDoesNotThrow(() -> manager.fachwahlAddListe(emptyList));
		assertEquals(0, manager.fachwahlGetAnzahl());

		// Zwei gültige Fachwahlen hinzufügen
		final List<GostFachwahl> list = new ArrayList<>();
		list.add(createFachwahl(SCHUELER_1_ID, FACH_D_ID, KURSART_GK));
		list.add(createFachwahl(SCHUELER_1_ID, FACH_M_ID, KURSART_LK));
		assertDoesNotThrow(() -> manager.fachwahlAddListe(list));
		assertEquals(2, manager.fachwahlGetAnzahl());

		// Liste mit Duplikat hinzufügen
		final List<GostFachwahl> listWithDuplicate = new ArrayList<>();
		listWithDuplicate.add(createFachwahl(SCHUELER_1_ID, FACH_E_ID, KURSART_GK));
		listWithDuplicate.add(createFachwahl(SCHUELER_1_ID, FACH_E_ID, KURSART_GK));
		assertThrows(DeveloperNotificationException.class, () -> manager.fachwahlAddListe(listWithDuplicate));
		assertEquals(2, manager.fachwahlGetAnzahl()); // Es muss 2 bleiben (kein Teil-Hinzufügen bei Fehler).

		// Liste mit Duplikat hinzufügen
		final List<GostFachwahl> listSingle = new ArrayList<>();
		listSingle.add(createFachwahl(SCHUELER_1_ID, FACH_E_ID, KURSART_GK));
		assertDoesNotThrow(() -> manager.fachwahlAddListe(listSingle));
		assertEquals(3, manager.fachwahlGetAnzahl()); // So muss das Hinzufügen klappen.
	}

	@Test
	@DisplayName("testFachwahlGetAnzahl")
	void testFachwahlGetAnzahl() {
		final GostBlockungsdatenManager manager = createStandardManager();

		// Ohne Fachwahlen: 0
		assertEquals(0, manager.fachwahlGetAnzahl());

		// Eine Fachwahl hinzufügen
		manager.fachwahlAdd(createFachwahl(SCHUELER_1_ID, FACH_D_ID, KURSART_GK));
		assertEquals(1, manager.fachwahlGetAnzahl());

		// Zwei weitere Fachwahlen hinzufügen
		manager.fachwahlAdd(createFachwahl(SCHUELER_1_ID, FACH_M_ID, KURSART_LK));
		manager.fachwahlAdd(createFachwahl(SCHUELER_1_ID, FACH_E_ID, KURSART_LK));
		assertEquals(3, manager.fachwahlGetAnzahl());
	}

	@Test
	@DisplayName("testFachwahlGetName")
	void testFachwahlGetName() {
		final GostBlockungsdatenManager manager = createStandardManager();
		addDefaultFachwahlen(manager);

		// Gültiger Fachwahlnamen
		final GostFachwahl fw = createFachwahl(SCHUELER_1_ID, FACH_D_ID, KURSART_GK);
		assertEquals("D-GK", manager.fachwahlGetName(fw));

		// Fachwahlnamen mit unbekanntem Fach
		final GostFachwahl unknownFach = createFachwahl(SCHUELER_1_ID, FACH_NICHT_VORHANDEN, KURSART_GK);
		assertThrows(DeveloperNotificationException.class, () -> manager.fachwahlGetName(unknownFach));
	}

	@Test
	@DisplayName("testFachwahlGetListeOfFachart")
	void testFachwahlGetListeOfFachart() {
		final GostBlockungsdatenManager manager = createStandardManager();
		addDefaultFachwahlen(manager);

		// Eine Fachart mit einer Fachwahl
		final long fachartDGK = GostKursart.getFachartID(FACH_D_ID, KURSART_GK);
		assertEquals(1, manager.fachwahlGetListeOfFachart(fachartDGK).size());

		// Gleiche Fachart mit zwei Fachwahlen (zweiten Schüler hinzufügen)
		final Schueler schueler2 = new Schueler();
		schueler2.id = SCHUELER_2_ID;
		schueler2.nachname = "Test2";
		schueler2.vorname = "Anna";
		schueler2.geschlecht = Geschlecht.W.id;
		schueler2.status = SchuelerStatus.AKTIV.ordinal();
		manager.schuelerAddListe(List.of(schueler2));
		manager.fachwahlAdd(createFachwahl(SCHUELER_2_ID, FACH_D_ID, KURSART_GK));
		assertEquals(2, manager.fachwahlGetListeOfFachart(fachartDGK).size());

		// Fachart ohne Fachwahl
		final long fachartPJK = GostKursart.getFachartID(FACH_D_ID, KURSART_UNGUELTIG);
		assertEquals(0, manager.fachwahlGetListeOfFachart(fachartPJK).size());
	}

	@Test
	@DisplayName("testFachwahlGetAnzahlVerwendeterKursarten")
	void testFachwahlGetAnzahlVerwendeterKursarten() {
		final GostBlockungsdatenManager manager = createStandardManager();

		// Ohne Fachwahlen: 0 Kursarten
		assertEquals(0, manager.fachwahlGetAnzahlVerwendeterKursarten());

		// Zwei verschiedene Kursarten
		manager.fachwahlAdd(createFachwahl(SCHUELER_1_ID, FACH_D_ID, KURSART_GK));
		manager.fachwahlAdd(createFachwahl(SCHUELER_1_ID, FACH_M_ID, KURSART_LK));
		assertEquals(2, manager.fachwahlGetAnzahlVerwendeterKursarten());

		// Nur eine Kursart
		final GostBlockungsdatenManager manager2 = createStandardManager();
		manager2.fachwahlAdd(createFachwahl(SCHUELER_1_ID, FACH_D_ID, KURSART_GK));
		manager2.fachwahlAdd(createFachwahl(SCHUELER_1_ID, FACH_M_ID, KURSART_GK));
		assertEquals(1, manager2.fachwahlGetAnzahlVerwendeterKursarten());
	}

	@Test
	@DisplayName("testFachGetMengeKursarten")
	void testFachGetMengeKursarten() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(1);

		// Fach ohne Kurse und Fachwahlen: leere Liste
		assertEquals(0, manager.fachGetMengeKursarten(FACH_D_ID).size());

		// Fach mit Kursen: Kursart des Kurses erscheint
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		assertEquals(1, manager.fachGetMengeKursarten(FACH_D_ID).size());
		assertEquals(KURSART_GK, manager.fachGetMengeKursarten(FACH_D_ID).get(0).id);

		// Fach mit Kursen und Fachwahlen: vereinigte Kursarten
		addDefaultFachwahlen(manager); // D-GK, M-LK --> bereits vorhanden
		manager.kursAdd(createKurs(KURS_ID_2, FACH_D_ID, KURSART_LK, KURS_NR_1)); // D-LK
		assertEquals(2, manager.fachGetMengeKursarten(FACH_D_ID).size());
	}

	// #########################################################################
	// ##########               Schiene-Tests                         ##########
	// #########################################################################

	@Test
	@DisplayName("testSchieneAdd")
	void testSchieneAdd() {
		final GostBlockungsdatenManager manager = createStandardManager();

		// Gültige Schiene hinzufügen
		final GostBlockungSchiene schiene1 = createSchiene(SCHIENE_ID_1, 1, "Schiene 1");
		assertDoesNotThrow(() -> manager.schieneAdd(schiene1));
		assertEquals(1, manager.schieneGetAnzahl());
		assertTrue(manager.schieneGetExistiert(SCHIENE_ID_1));

		// Schiene mit doppelter ID hinzufügen
		final GostBlockungSchiene schieneDoppeltID = createSchiene(SCHIENE_ID_1, 2, "Schiene doppelte ID");
		assertThrows(DeveloperNotificationException.class, () -> manager.schieneAdd(schieneDoppeltID));

		// Schiene mit ungültiger ID hinzufügen
		final GostBlockungSchiene schieneUngueltigeID = createSchiene(-1, 3, "Schiene ungültige ID");
		assertThrows(DeveloperNotificationException.class, () -> manager.schieneAdd(schieneUngueltigeID));

		// Schiene mit leerer Bezeichnung hinzufügen
		final GostBlockungSchiene schieneLeer = createSchiene(SCHIENE_ID_3, 4, "");
		assertThrows(DeveloperNotificationException.class, () -> manager.schieneAdd(schieneLeer));

		// Schiene mit Nummer < 1 hinzufügen
		final GostBlockungSchiene schieneNr0 = createSchiene(SCHIENE_ID_4, 0, "Schiene Nummer 0");
		assertThrows(DeveloperNotificationException.class, () -> manager.schieneAdd(schieneNr0));

		// Schiene mit Wochenstunden < 1 hinzufügen
		final GostBlockungSchiene schieneWs0 = createSchiene(SCHIENE_ID_5, 5, "Schiene Wochenstunden 0");
		schieneWs0.wochenstunden = 0;
		assertThrows(DeveloperNotificationException.class, () -> manager.schieneAdd(schieneWs0));

		// Schiene mit doppelter Nummer hinzufügen
		final GostBlockungSchiene schieneDoppeltNr = createSchiene(SCHIENE_ID_6, 1, "Schiene doppelte Nummer");
		assertThrows(DeveloperNotificationException.class, () -> manager.schieneAdd(schieneDoppeltNr));

		// Schiene mit Lücke in der Nummern-Reihenfolge hinzufügen
		final GostBlockungSchiene schieneLuecke = createSchiene(SCHIENE_ID_7, 3, "Schiene Lücke");
		assertThrows(DeveloperNotificationException.class, () -> manager.schieneAdd(schieneLuecke));

		// Schiene mit korrekter Folgenummer (2) hinzufügen
		final GostBlockungSchiene schiene2 = createSchiene(SCHIENE_ID_2, 2, "Schiene 2");
		assertDoesNotThrow(() -> manager.schieneAdd(schiene2));
		assertEquals(2, manager.schieneGetAnzahl());
	}

	@Test
	@DisplayName("testSchieneAddListe")
	void testSchieneAddListe() {
		final GostBlockungsdatenManager manager = createStandardManager();

		// Leere Liste hinzufügen
		final List<GostBlockungSchiene> emptyList = new ArrayList<>();
		assertDoesNotThrow(() -> manager.schieneAddListe(emptyList));
		assertEquals(0, manager.schieneGetAnzahl());

		// Zwei gültige Schienen hinzufügen
		final List<GostBlockungSchiene> list = new ArrayList<>();
		list.add(createSchiene(SCHIENE_ID_1, 1, "Schiene 1"));
		list.add(createSchiene(SCHIENE_ID_2, 2, "Schiene 2"));
		assertDoesNotThrow(() -> manager.schieneAddListe(list));
		assertEquals(2, manager.schieneGetAnzahl());

		// Liste mit doppelter ID hinzufügen
		final List<GostBlockungSchiene> listDoppeltID = new ArrayList<>();
		listDoppeltID.add(createSchiene(SCHIENE_ID_3, 3, "Schiene 3"));
		listDoppeltID.add(createSchiene(SCHIENE_ID_3, 4, "Schiene doppelte ID"));
		assertThrows(DeveloperNotificationException.class, () -> manager.schieneAddListe(listDoppeltID));
		assertEquals(2, manager.schieneGetAnzahl()); // Muss 2 bleiben (kein Teil-Hinzufügen).
		assertFalse(manager.schieneGetExistiert(SCHIENE_ID_3)); // Da eine Exception auftrat, darf es die ID nicht geben.


		// Liste mit Lücke in der Nummern-Reihenfolge hinzufügen
		final List<GostBlockungSchiene> listLuecke = new ArrayList<>();
		listLuecke.add(createSchiene(SCHIENE_ID_4, 5, "Schiene 5"));
		listLuecke.add(createSchiene(SCHIENE_ID_5, 6, "Schiene 6"));
		assertThrows(DeveloperNotificationException.class, () -> manager.schieneAddListe(listLuecke));
		assertEquals(2, manager.schieneGetAnzahl()); // Muss 2 bleiben (kein Teil-Hinzufügen).
		assertFalse(manager.schieneGetExistiert(SCHIENE_ID_4)); // Da eine Exception auftrat, darf es die ID nicht geben.
		assertFalse(manager.schieneGetExistiert(SCHIENE_ID_5)); // Da eine Exception auftrat, darf es die ID nicht geben.

		// Liste mit ungültiger Nummer hinzufügen
		final List<GostBlockungSchiene> listNrUngueltig = new ArrayList<>();
		listNrUngueltig.add(createSchiene(SCHIENE_ID_6, 7, "Schiene 7"));
		listNrUngueltig.add(createSchiene(SCHIENE_ID_7, 0, "Schiene Nr 0"));
		assertThrows(DeveloperNotificationException.class, () -> manager.schieneAddListe(listNrUngueltig));
		assertEquals(2, manager.schieneGetAnzahl()); // Muss 2 bleiben (kein Teil-Hinzufügen).
		assertFalse(manager.schieneGetExistiert(SCHIENE_ID_6)); // Da eine Exception auftrat, darf es die ID nicht geben.
		assertFalse(manager.schieneGetExistiert(SCHIENE_ID_7)); // Da eine Exception auftrat, darf es die ID nicht geben.

		// Zwei weitere gültige Schienen hinzufügen
		final List<GostBlockungSchiene> listWeitere = new ArrayList<>();
		listWeitere.add(createSchiene(SCHIENE_ID_7, 3, "Schiene 3"));
		listWeitere.add(createSchiene(SCHIENE_ID_8, 4, "Schiene 4"));
		assertDoesNotThrow(() -> manager.schieneAddListe(listWeitere));
		assertEquals(4, manager.schieneGetAnzahl());
	}

	@Test
	@DisplayName("testSchieneGet")
	void testSchieneGet() {
		final GostBlockungsdatenManager manager = createStandardManager();
		manager.schieneAdd(createSchiene(SCHIENE_ID_1, 1, "Schiene 1"));

		// Existierende Schiene abrufen
		final GostBlockungSchiene schiene = manager.schieneGet(SCHIENE_ID_1);
		assertNotNull(schiene);
		assertEquals(SCHIENE_ID_1, schiene.id);
		assertEquals(1, schiene.nummer);
		assertEquals("Schiene 1", schiene.bezeichnung);

		// Nicht existierende Schiene abrufen
		assertThrows(DeveloperNotificationException.class, () -> manager.schieneGet(SCHIENE_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testSchieneGetExistiert")
	void testSchieneGetExistiert() {
		final GostBlockungsdatenManager manager = createStandardManager();
		manager.schieneAdd(createSchiene(SCHIENE_ID_1, 1, "Schiene 1"));

		// Existierende Schiene
		assertTrue(manager.schieneGetExistiert(SCHIENE_ID_1));

		// Nicht existierende Schiene
		assertFalse(manager.schieneGetExistiert(SCHIENE_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testSchieneGetListe")
	void testSchieneGetListe() {
		final GostBlockungsdatenManager manager = createStandardManager();

		// Leere Liste
		assertEquals(0, manager.schieneGetListe().size());

		// Liste mit Schienen
		manager.schieneAdd(createSchiene(SCHIENE_ID_1, 1, "Schiene 1"));
		manager.schieneAdd(createSchiene(SCHIENE_ID_2, 2, "Schiene 2"));
		assertEquals(2, manager.schieneGetListe().size());
	}

	@Test
	@DisplayName("testSchieneGetIsRemoveAllowed")
	void testSchieneGetIsRemoveAllowed() {
		// Ohne Blockungsvorlage: Entfernen nicht erlaubt
		final GostBlockungsdatenManager manager = createStandardManager();
		manager.schieneAdd(createSchiene(SCHIENE_ID_1, 1, "Schiene 1"));
		assertFalse(manager.schieneGetIsRemoveAllowed(SCHIENE_ID_1));

		// Mit Blockungsvorlage: Entfernen erlaubt
		final GostBlockungsdatenManager managerVorlage = createManagerMitBlockungsvorlage();
		assertEquals(1, managerVorlage.schieneGetAnzahl());
		assertTrue(managerVorlage.schieneGetIsRemoveAllowed(SCHIENE_ID_1));

		// Nicht existierende Schiene
		assertThrows(DeveloperNotificationException.class, () -> managerVorlage.schieneGetIsRemoveAllowed(SCHIENE_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testSchienePatchBezeichnung")
	void testSchienePatchBezeichnung() {
		final GostBlockungsdatenManager manager = createStandardManager();
		manager.schieneAdd(createSchiene(SCHIENE_ID_1, 1, "Schiene 1"));

		// Bezeichnung ändern
		manager.schienePatchBezeichnung(SCHIENE_ID_1, "Neue Bezeichnung");
		assertEquals("Neue Bezeichnung", manager.schieneGet(SCHIENE_ID_1).bezeichnung);

		// Nicht existierende Schiene
		assertThrows(DeveloperNotificationException.class, () -> manager.schienePatchBezeichnung(SCHIENE_NICHT_VORHANDEN, "egal"));
	}

	@Test
	@DisplayName("testSchienePatchWochenstunden")
	void testSchienePatchWochenstunden() {
		final GostBlockungsdatenManager manager = createStandardManager();
		manager.schieneAdd(createSchiene(SCHIENE_ID_1, 1, "Schiene 1"));

		// Wochenstunden ändern
		manager.schienePatchWochenstunden(SCHIENE_ID_1, 5);
		assertEquals(5, manager.schieneGet(SCHIENE_ID_1).wochenstunden);

		// Nicht existierende Schiene
		assertThrows(DeveloperNotificationException.class, () -> manager.schienePatchWochenstunden(SCHIENE_NICHT_VORHANDEN, 3));
	}

	@Test
	@DisplayName("testSchieneRemoveByID")
	void testSchieneRemoveByID() {
		// Ohne Blockungsvorlage: Löschen nicht erlaubt (existierende Schiene)
		final GostBlockungsdatenManager manager = createStandardManager();
		manager.schieneAdd(createSchiene(SCHIENE_ID_1, 1, "Schiene 1"));
		assertThrows(DeveloperNotificationException.class, () -> manager.schieneRemoveByID(SCHIENE_ID_1));

		// Ohne Blockungsvorlage: Löschen nicht erlaubt (nicht existierende Schiene)
		assertThrows(DeveloperNotificationException.class, () -> manager.schieneRemoveByID(SCHIENE_NICHT_VORHANDEN));

		// Mit Blockungsvorlage: nicht existierende Schiene
		final GostBlockungsdatenManager managerVorlage = createManagerMitBlockungsvorlage();
		assertThrows(DeveloperNotificationException.class, () -> managerVorlage.schieneRemoveByID(SCHIENE_NICHT_VORHANDEN));

		// Mit Blockungsvorlage: gültiges Löschen
		assertEquals(1, managerVorlage.schieneGetAnzahl());
		assertDoesNotThrow(() -> managerVorlage.schieneRemoveByID(SCHIENE_ID_1));
		assertEquals(0, managerVorlage.schieneGetAnzahl());
		assertFalse(managerVorlage.schieneGetExistiert(SCHIENE_ID_1));
	}

	@Test
	@DisplayName("testSchieneRemove")
	void testSchieneRemove() {
		// Ohne Blockungsvorlage: Löschen nicht erlaubt
		final GostBlockungsdatenManager manager = createStandardManager();
		final GostBlockungSchiene schiene = createSchiene(SCHIENE_ID_1, 1, "Schiene 1");
		manager.schieneAdd(schiene);
		final GostBlockungSchiene schieneAnderesObjekt = createSchiene(SCHIENE_ID_1, 1, "Schiene 1");
		assertThrows(DeveloperNotificationException.class, () -> manager.schieneRemove(schieneAnderesObjekt));

		// Mit Blockungsvorlage: gültiges Löschen (über anderes Objekt mit gleicher ID)
		final GostBlockungsdatenManager managerVorlage = createManagerMitBlockungsvorlage();
		assertEquals(1, managerVorlage.schieneGetAnzahl());
		final GostBlockungSchiene schieneAndereRef = createSchiene(SCHIENE_ID_1, 1, "Schiene 1");
		assertDoesNotThrow(() -> managerVorlage.schieneRemove(schieneAndereRef));
		assertEquals(0, managerVorlage.schieneGetAnzahl());
	}

	@Test
	@DisplayName("testSchieneGetAnzahl")
	void testSchieneGetAnzahl() {
		final GostBlockungsdatenManager manager = createStandardManager();

		// Ohne Schienen: 0
		assertEquals(0, manager.schieneGetAnzahl());

		// Eine Schiene
		manager.schieneAdd(createSchiene(SCHIENE_ID_1, 1, "Schiene 1"));
		assertEquals(1, manager.schieneGetAnzahl());

		// Mehrere Schienen
		manager.schieneAdd(createSchiene(SCHIENE_ID_2, 2, "Schiene 2"));
		manager.schieneAdd(createSchiene(SCHIENE_ID_3, 3, "Schiene 3"));
		assertEquals(3, manager.schieneGetAnzahl());
	}

	@Test
	@DisplayName("testSchieneGetDefaultAnzahl")
	void testSchieneGetDefaultAnzahl() {
		// EF1: 13 Schienen
		assertEquals(13, GostBlockungsdatenManager.schieneGetDefaultAnzahl(GostHalbjahr.EF1));

		// Q22: 11 Schienen (id >= 2)
		assertEquals(11, GostBlockungsdatenManager.schieneGetDefaultAnzahl(GostHalbjahr.Q22));
	}


	// #########################################################################
	// ##########                Kurs-Tests                           ##########
	// #########################################################################

	@Test
	@DisplayName("testKursAdd")
	void testKursAdd() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(3);

		// Gültigen Kurs hinzufügen
		final GostBlockungKurs kurs1 = createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1);
		assertDoesNotThrow(() -> manager.kursAdd(kurs1));
		assertEquals(1, manager.kursGetAnzahl());
		assertTrue(manager.kursGetExistiert(KURS_ID_1));

		// Kurs mit doppelter ID hinzufügen
		final GostBlockungKurs kursDoppeltID = createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_2);
		assertThrows(DeveloperNotificationException.class, () -> manager.kursAdd(kursDoppeltID));

		// Kurs mit ungültiger ID hinzufügen
		final GostBlockungKurs kursUngueltigeID = createKurs(-2, FACH_D_ID, KURSART_GK, 3);
		assertThrows(DeveloperNotificationException.class, () -> manager.kursAdd(kursUngueltigeID));

		// Kurs mit unbekanntem Fach hinzufügen
		final GostBlockungKurs kursUnbekanntesFach = createKurs(KURS_ID_3, FACH_NICHT_VORHANDEN, KURSART_GK, 4);
		assertThrows(DeveloperNotificationException.class, () -> manager.kursAdd(kursUnbekanntesFach));

		// Kurs mit unbekannter Kursart hinzufügen
		final GostBlockungKurs kursUnbekannteKursart = createKurs(KURS_ID_4, FACH_D_ID, KURSART_UNGUELTIG, 5);
		assertThrows(DeveloperNotificationException.class, () -> manager.kursAdd(kursUnbekannteKursart));

		// Kurs mit negativen Wochenstunden hinzufügen
		final GostBlockungKurs kursNegativeWS = createKurs(KURS_ID_5, FACH_D_ID, KURSART_GK, KURS_NR_6);
		kursNegativeWS.wochenstunden = -1;
		assertThrows(DeveloperNotificationException.class, () -> manager.kursAdd(kursNegativeWS));

		// Kurs mit anzahlSchienen < 1 hinzufügen
		final GostBlockungKurs kursSchienen0 = createKurs(KURS_ID_6, FACH_D_ID, KURSART_GK, KURS_NR_7);
		kursSchienen0.anzahlSchienen = 0;
		assertThrows(DeveloperNotificationException.class, () -> manager.kursAdd(kursSchienen0));

		// Kurs mit anzahlSchienen > Schienenanzahl hinzufügen
		final GostBlockungKurs kursZuVieleSchienen = createKurs(KURS_ID_7, FACH_D_ID, KURSART_GK, KURS_NR_8);
		kursZuVieleSchienen.anzahlSchienen = 10;
		assertThrows(DeveloperNotificationException.class, () -> manager.kursAdd(kursZuVieleSchienen));

		// Kurs mit Nummer < 1 hinzufügen
		final GostBlockungKurs kursNr0 = createKurs(KURS_ID_8, FACH_D_ID, KURSART_GK, 0);
		assertThrows(DeveloperNotificationException.class, () -> manager.kursAdd(kursNr0));

		// Nach allen Exceptions sollte die Kursanzahl unverändert 1 sein.
		assertEquals(1, manager.kursGetAnzahl());
	}

	@Test
	@DisplayName("testKursAddListe")
	void testKursAddListe() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(3);

		// Leere Liste hinzufügen
		final List<GostBlockungKurs> emptyList = new ArrayList<>();
		assertDoesNotThrow(() -> manager.kursAddListe(emptyList));
		assertEquals(0, manager.kursGetAnzahl());

		// Zwei gültige Kurse hinzufügen
		final List<GostBlockungKurs> list = new ArrayList<>();
		list.add(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		list.add(createKurs(KURS_ID_2, FACH_M_ID, KURSART_LK, KURS_NR_1));
		assertDoesNotThrow(() -> manager.kursAddListe(list));
		assertEquals(2, manager.kursGetAnzahl());

		// Liste mit doppelter ID hinzufügen
		final List<GostBlockungKurs> listDoppelt = new ArrayList<>();
		listDoppelt.add(createKurs(KURS_ID_3, FACH_E_ID, KURSART_GK, KURS_NR_1));
		listDoppelt.add(createKurs(KURS_ID_3, FACH_E_ID, KURSART_GK, KURS_NR_2));
		assertThrows(DeveloperNotificationException.class, () -> manager.kursAddListe(listDoppelt));
		assertEquals(2, manager.kursGetAnzahl()); // Bei Exception darf kein Teil-Hinzufügen passieren.
	}

	@Test
	@DisplayName("testKursGet")
	void testKursGet() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(1);
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));

		// Existierenden Kurs abrufen
		final GostBlockungKurs kurs = manager.kursGet(KURS_ID_1);
		assertNotNull(kurs);
		assertEquals(KURS_ID_1, kurs.id);
		assertEquals(FACH_D_ID, kurs.fach_id);
		assertEquals(KURSART_GK, kurs.kursart);

		// Nicht existierenden Kurs abrufen
		assertThrows(DeveloperNotificationException.class, () -> manager.kursGet(KURS_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testKursGetExistiert")
	void testKursGetExistiert() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(1);
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));

		// Existierender Kurs
		assertTrue(manager.kursGetExistiert(KURS_ID_1));

		// Nicht existierender Kurs
		assertFalse(manager.kursGetExistiert(KURS_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testKursGetAnzahl")
	void testKursGetAnzahl() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(1);

		// Ohne Kurse: 0
		assertEquals(0, manager.kursGetAnzahl());

		// Ein Kurs
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		assertEquals(1, manager.kursGetAnzahl());

		// Mehrere Kurse
		manager.kursAdd(createKurs(KURS_ID_2, FACH_M_ID, KURSART_LK, KURS_NR_1));
		manager.kursAdd(createKurs(KURS_ID_3, FACH_E_ID, KURSART_GK, KURS_NR_1));
		assertEquals(3, manager.kursGetAnzahl());

		// Kurs löschen (Blockungsvorlage benötigt)
		final GostBlockungsdatenManager managerVorlage = createManagerMitBlockungsvorlage();
		managerVorlage.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		managerVorlage.kursAdd(createKurs(KURS_ID_2, FACH_M_ID, KURSART_LK, KURS_NR_1));
		assertEquals(2, managerVorlage.kursGetAnzahl());
		managerVorlage.kursRemoveByID(KURS_ID_1);
		assertEquals(1, managerVorlage.kursGetAnzahl());
	}

	@Test
	@DisplayName("testKursGetAnzahlIntener")
	void testKursGetAnzahlIntener() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(1);

		// Ohne Kurse: 0
		assertEquals(0, manager.kursGetAnzahlIntener());

		// Nur interner Kurs (kein Koop-Kurs)
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		assertEquals(1, manager.kursGetAnzahlIntener());

		// Interner Kurs + Kooperationskurs
		final GostBlockungKurs koopKurs = createKurs(KURS_ID_2, FACH_M_ID, KURSART_GK, KURS_NR_1);
		koopKurs.istKoopKurs = true;
		manager.kursAdd(koopKurs);
		assertEquals(1, manager.kursGetAnzahlIntener());

		// Zwei interne Kurse + ein Koop-Kurs
		manager.kursAdd(createKurs(KURS_ID_3, FACH_E_ID, KURSART_GK, KURS_NR_1));
		assertEquals(2, manager.kursGetAnzahlIntener());
	}

	@Test
	@DisplayName("testKursGetName")
	void testKursGetName() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(1);

		// Kurs ohne Suffix
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		assertEquals("D-GK1", manager.kursGetName(KURS_ID_1));

		// Kurs mit Suffix
		final GostBlockungKurs kursMitSuffix = createKurs(KURS_ID_2, FACH_M_ID, KURSART_LK, KURS_NR_1);
		kursMitSuffix.suffix = "PJK";
		manager.kursAdd(kursMitSuffix);
		assertEquals("M-LK1-PJK", manager.kursGetName(KURS_ID_2));

		// Nicht existierender Kurs
		assertThrows(DeveloperNotificationException.class, () -> manager.kursGetName(KURS_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testKursGetNameOhneSuffix")
	void testKursGetNameOhneSuffix() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(1);

		// Kurs mit Suffix – OhneSuffix ignoriert den Suffix
		final GostBlockungKurs kursMitSuffix = createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1);
		kursMitSuffix.suffix = "PJK";
		manager.kursAdd(kursMitSuffix);
		assertEquals("D-GK1", manager.kursGetNameOhneSuffix(KURS_ID_1));

		// Nicht existierender Kurs
		assertThrows(DeveloperNotificationException.class, () -> manager.kursGetNameOhneSuffix(KURS_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testKursAddLehrkraft")
	void testKursAddLehrkraft() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(1);
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));

		// Gültige Lehrkraft hinzufügen
		final GostBlockungKursLehrer lehrer1 = createKursLehrer(LEHRER_ID_1, LEHRER_REIHENFOLGE_1);
		assertDoesNotThrow(() -> manager.kursAddLehrkraft(KURS_ID_1, lehrer1));
		assertTrue(manager.kursGetLehrkraftMitIDExists(KURS_ID_1, LEHRER_ID_1));

		// Lehrkraft mit doppelter ID hinzufügen
		final GostBlockungKursLehrer lehrerDoppeltID = createKursLehrer(LEHRER_ID_1, LEHRER_REIHENFOLGE_2);
		assertThrows(DeveloperNotificationException.class, () -> manager.kursAddLehrkraft(KURS_ID_1, lehrerDoppeltID));

		// Lehrkraft mit doppelter Reihenfolgenummer hinzufügen
		final GostBlockungKursLehrer lehrerDoppeltNr = createKursLehrer(LEHRER_ID_2, LEHRER_REIHENFOLGE_1);
		assertThrows(DeveloperNotificationException.class, () -> manager.kursAddLehrkraft(KURS_ID_1, lehrerDoppeltNr));

		// Lehrkraft zu nicht existierendem Kurs hinzufügen
		assertThrows(DeveloperNotificationException.class, () -> manager.kursAddLehrkraft(KURS_NICHT_VORHANDEN, lehrer1));

		// Mit Blockungsvorlage: ergebnisAlleRevalidieren wird durchlaufen
		final GostBlockungsdatenManager managerVorlage = createManagerMitBlockungsvorlage();
		managerVorlage.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		assertDoesNotThrow(() -> managerVorlage.kursAddLehrkraft(KURS_ID_1, lehrer1));
		assertTrue(managerVorlage.kursGetLehrkraftMitIDExists(KURS_ID_1, LEHRER_ID_1));
	}

	@Test
	@DisplayName("testKursRemoveLehrkraft")
	void testKursRemoveLehrkraft() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(1);
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		manager.kursAddLehrkraft(KURS_ID_1, createKursLehrer(LEHRER_ID_1, LEHRER_REIHENFOLGE_1));

		// Existierende Lehrkraft entfernen
		assertDoesNotThrow(() -> manager.kursRemoveLehrkraft(KURS_ID_1, LEHRER_ID_1));
		assertFalse(manager.kursGetLehrkraftMitIDExists(KURS_ID_1, LEHRER_ID_1));

		// Nicht existierende Lehrkraft entfernen
		assertThrows(DeveloperNotificationException.class, () -> manager.kursRemoveLehrkraft(KURS_ID_1, LEHRER_NICHT_VORHANDEN));

		// Lehrkraft aus nicht existierendem Kurs entfernen
		assertThrows(DeveloperNotificationException.class, () -> manager.kursRemoveLehrkraft(KURS_NICHT_VORHANDEN, LEHRER_ID_1));

		// Mit Blockungsvorlage: ergebnisAlleRevalidieren wird durchlaufen
		final GostBlockungsdatenManager managerVorlage = createManagerMitBlockungsvorlage();
		managerVorlage.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		managerVorlage.kursAddLehrkraft(KURS_ID_1, createKursLehrer(LEHRER_ID_1, LEHRER_REIHENFOLGE_1));
		assertDoesNotThrow(() -> managerVorlage.kursRemoveLehrkraft(KURS_ID_1, LEHRER_ID_1));
	}

	@Test
	@DisplayName("testKursGetLehrkraftMitNummer")
	void testKursGetLehrkraftMitNummer() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(1);
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		manager.kursAddLehrkraft(KURS_ID_1, createKursLehrer(LEHRER_ID_1, LEHRER_REIHENFOLGE_1));
		manager.kursAddLehrkraft(KURS_ID_1, createKursLehrer(LEHRER_ID_2, LEHRER_REIHENFOLGE_2));

		// Existierende Lehrkraft mit Nummer abrufen
		assertEquals(LEHRER_ID_1, manager.kursGetLehrkraftMitNummer(KURS_ID_1, LEHRER_REIHENFOLGE_1).id);
		assertEquals(LEHRER_ID_2, manager.kursGetLehrkraftMitNummer(KURS_ID_1, LEHRER_REIHENFOLGE_2).id);

		// Nicht existierende Nummer
		assertThrows(DeveloperNotificationException.class, () -> manager.kursGetLehrkraftMitNummer(KURS_ID_1, LEHRER_REIHENFOLGE_NICHT_VORHANDEN));

		// Nicht existierender Kurs
		assertThrows(DeveloperNotificationException.class, () -> manager.kursGetLehrkraftMitNummer(KURS_NICHT_VORHANDEN, LEHRER_REIHENFOLGE_1));
	}

	@Test
	@DisplayName("testKursGetLehrkraftMitID")
	void testKursGetLehrkraftMitID() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(1);
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		manager.kursAddLehrkraft(KURS_ID_1, createKursLehrer(LEHRER_ID_1, LEHRER_REIHENFOLGE_1));

		// Existierende Lehrkraft mit ID abrufen
		assertEquals(LEHRER_REIHENFOLGE_1, manager.kursGetLehrkraftMitID(KURS_ID_1, LEHRER_ID_1).reihenfolge);

		// Nicht existierende ID
		assertThrows(DeveloperNotificationException.class, () -> manager.kursGetLehrkraftMitID(KURS_ID_1, LEHRER_NICHT_VORHANDEN));

		// Nicht existierender Kurs
		assertThrows(DeveloperNotificationException.class, () -> manager.kursGetLehrkraftMitID(KURS_NICHT_VORHANDEN, LEHRER_ID_1));
	}

	@Test
	@DisplayName("testKursGetLehrkraftMitNummerExists")
	void testKursGetLehrkraftMitNummerExists() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(1);
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		manager.kursAddLehrkraft(KURS_ID_1, createKursLehrer(LEHRER_ID_1, LEHRER_REIHENFOLGE_1));

		// Existierende Nummer
		assertTrue(manager.kursGetLehrkraftMitNummerExists(KURS_ID_1, LEHRER_REIHENFOLGE_1));

		// Nicht existierende Nummer
		assertFalse(manager.kursGetLehrkraftMitNummerExists(KURS_ID_1, LEHRER_REIHENFOLGE_NICHT_VORHANDEN));

		// Nicht existierender Kurs
		assertThrows(DeveloperNotificationException.class, () -> manager.kursGetLehrkraftMitNummerExists(KURS_NICHT_VORHANDEN, LEHRER_REIHENFOLGE_1));
	}

	@Test
	@DisplayName("testKursGetLehrkraftMitIDExists")
	void testKursGetLehrkraftMitIDExists() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(1);
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		manager.kursAddLehrkraft(KURS_ID_1, createKursLehrer(LEHRER_ID_1, LEHRER_REIHENFOLGE_1));

		// Existierende ID
		assertTrue(manager.kursGetLehrkraftMitIDExists(KURS_ID_1, LEHRER_ID_1));

		// Nicht existierende ID
		assertFalse(manager.kursGetLehrkraftMitIDExists(KURS_ID_1, LEHRER_NICHT_VORHANDEN));

		// Nicht existierender Kurs
		assertThrows(DeveloperNotificationException.class, () -> manager.kursGetLehrkraftMitIDExists(KURS_NICHT_VORHANDEN, LEHRER_ID_1));
	}

	@Test
	@DisplayName("testKursGetLehrkraefteSortiert")
	void testKursGetLehrkraefteSortiert() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(1);
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));

		// Keine Lehrkräfte
		assertEquals(0, manager.kursGetLehrkraefteSortiert(KURS_ID_1).size());

		// Lehrkräfte hinzufügen und sortiert prüfen
		manager.kursAddLehrkraft(KURS_ID_1, createKursLehrer(LEHRER_ID_2, LEHRER_REIHENFOLGE_2));
		manager.kursAddLehrkraft(KURS_ID_1, createKursLehrer(LEHRER_ID_1, LEHRER_REIHENFOLGE_1));
		final List<GostBlockungKursLehrer> lehrkraefte = manager.kursGetLehrkraefteSortiert(KURS_ID_1);
		assertEquals(2, lehrkraefte.size());
		assertEquals(LEHRER_REIHENFOLGE_1, lehrkraefte.get(0).reihenfolge);
		assertEquals(LEHRER_REIHENFOLGE_2, lehrkraefte.get(1).reihenfolge);

		// Nicht existierender Kurs
		assertThrows(DeveloperNotificationException.class, () -> manager.kursGetLehrkraefteSortiert(KURS_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testKursGetListeSortiertNachFachKursartNummer")
	void testKursGetListeSortiertNachFachKursartNummer() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(1);

		// Leere Liste
		assertEquals(0, manager.kursGetListeSortiertNachFachKursartNummer().size());

		// Kurse unsortiert hinzufügen
		manager.kursAdd(createKurs(KURS_ID_4, FACH_M_ID, KURSART_GK, KURS_NR_1));
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_LK, KURS_NR_1));
		manager.kursAdd(createKurs(KURS_ID_3, FACH_E_ID, KURSART_LK, KURS_NR_1));
		manager.kursAdd(createKurs(KURS_ID_2, FACH_D_ID, KURSART_GK, KURS_NR_2));

		// Sortierung: Fach --> Kursart --> Nummer
		final List<GostBlockungKurs> liste = manager.kursGetListeSortiertNachFachKursartNummer();
		assertEquals(4, liste.size());
		for (int i = 1; i < liste.size(); i++) {
			final GostBlockungKurs a = liste.get(i - 1);
			final GostBlockungKurs b = liste.get(i);
			final GostFach fa = manager.faecherManager().get(a.fach_id);
			final GostFach fb = manager.faecherManager().get(b.fach_id);
			final int cmpFach = GostFaecherManager.comp.compare(fa, fb);
			assertTrue((cmpFach < 0)
					|| ((cmpFach == 0) && (a.kursart < b.kursart))
					|| ((cmpFach == 0) && (a.kursart == b.kursart) && (a.nummer < b.nummer)));
		}
	}

	@Test
	@DisplayName("testKursGetListeSortiertNachKursartFachNummer")
	void testKursGetListeSortiertNachKursartFachNummer() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(1);

		// Leere Liste
		assertEquals(0, manager.kursGetListeSortiertNachKursartFachNummer().size());

		// Kurse unsortiert hinzufügen
		manager.kursAdd(createKurs(KURS_ID_4, FACH_M_ID, KURSART_GK, KURS_NR_1));
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_LK, KURS_NR_1));
		manager.kursAdd(createKurs(KURS_ID_3, FACH_E_ID, KURSART_LK, KURS_NR_1));
		manager.kursAdd(createKurs(KURS_ID_2, FACH_D_ID, KURSART_GK, KURS_NR_2));

		// Sortierung: Kursart --> Fach --> Nummer
		final List<GostBlockungKurs> liste = manager.kursGetListeSortiertNachKursartFachNummer();
		assertEquals(4, liste.size());
		for (int i = 1; i < liste.size(); i++) {
			final GostBlockungKurs a = liste.get(i - 1);
			final GostBlockungKurs b = liste.get(i);
			if (a.kursart < b.kursart) {
				continue;
			}
			assertEquals(a.kursart, b.kursart);
			final GostFach fa = manager.faecherManager().get(a.fach_id);
			final GostFach fb = manager.faecherManager().get(b.fach_id);
			final int cmpFach = GostFaecherManager.comp.compare(fa, fb);
			assertTrue((cmpFach < 0)
					|| ((cmpFach == 0) && (a.nummer < b.nummer)));
		}
	}

	@Test
	@DisplayName("testKursGetListeByFachUndKursart")
	void testKursGetListeByFachUndKursart() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(1);
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		manager.kursAdd(createKurs(KURS_ID_2, FACH_D_ID, KURSART_GK, KURS_NR_2));
		manager.kursAdd(createKurs(KURS_ID_3, FACH_M_ID, KURSART_LK, KURS_NR_1));

		// Fach 1, Kursart 2: 2 Kurse
		assertEquals(2, manager.kursGetListeByFachUndKursart(FACH_D_ID, KURSART_GK).size());

		// Fach 2, Kursart 1: 1 Kurs
		assertEquals(1, manager.kursGetListeByFachUndKursart(FACH_M_ID, KURSART_LK).size());

		// Fach 3, Kursart 2: 0 Kurse
		assertEquals(0, manager.kursGetListeByFachUndKursart(FACH_E_ID, KURSART_GK).size());
	}

	@Test
	@DisplayName("testKursGetIsRemoveAllowed")
	void testKursGetIsRemoveAllowed() {
		// Ohne Blockungsvorlage: Entfernen nicht erlaubt
		final GostBlockungsdatenManager manager = createManagerMitSchienen(1);
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		assertFalse(manager.kursGetIsRemoveAllowed(KURS_ID_1));

		// Mit Blockungsvorlage: Entfernen erlaubt
		final GostBlockungsdatenManager managerVorlage = createManagerMitBlockungsvorlage();
		managerVorlage.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		assertTrue(managerVorlage.kursGetIsRemoveAllowed(KURS_ID_1));

		// Nicht existierender Kurs
		assertThrows(DeveloperNotificationException.class, () -> managerVorlage.kursGetIsRemoveAllowed(KURS_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testKursRemoveByID")
	void testKursRemoveByID() {
		// Ohne Blockungsvorlage: Löschen nicht erlaubt
		final GostBlockungsdatenManager manager = createManagerMitSchienen(1);
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		assertThrows(DeveloperNotificationException.class, () -> manager.kursRemoveByID(KURS_ID_1));

		// Mit Blockungsvorlage: nicht existierender Kurs
		final GostBlockungsdatenManager managerVorlage = createManagerMitBlockungsvorlage();
		assertThrows(DeveloperNotificationException.class, () -> managerVorlage.kursRemoveByID(KURS_NICHT_VORHANDEN));

		// Mit Blockungsvorlage: gültiges Löschen
		managerVorlage.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		assertEquals(1, managerVorlage.kursGetAnzahl());
		assertDoesNotThrow(() -> managerVorlage.kursRemoveByID(KURS_ID_1));
		assertEquals(0, managerVorlage.kursGetAnzahl());
		assertFalse(managerVorlage.kursGetExistiert(KURS_ID_1));
	}

	@Test
	@DisplayName("testKursRemove")
	void testKursRemove() {
		// Ohne Blockungsvorlage: Löschen nicht erlaubt
		final GostBlockungsdatenManager manager = createManagerMitSchienen(1);
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		final GostBlockungKurs kursAnderesObjekt = createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1);
		assertThrows(DeveloperNotificationException.class, () -> manager.kursRemove(kursAnderesObjekt));

		// Mit Blockungsvorlage: gültiges Löschen (über anderes Objekt mit gleicher ID)
		final GostBlockungsdatenManager managerVorlage = createManagerMitBlockungsvorlage();
		managerVorlage.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		final GostBlockungKurs kursAndereRef = createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1);
		assertDoesNotThrow(() -> managerVorlage.kursRemove(kursAndereRef));
		assertEquals(0, managerVorlage.kursGetAnzahl());
	}

	@Test
	@DisplayName("testKursSetSuffix")
	void testKursSetSuffix() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(1);
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));

		// Suffix setzen
		manager.kursSetSuffix(KURS_ID_1, "X");
		assertEquals("D-GK1-X", manager.kursGetName(KURS_ID_1));

		// Suffix leeren
		manager.kursSetSuffix(KURS_ID_1, "");
		assertEquals("D-GK1", manager.kursGetName(KURS_ID_1));

		// Nicht existierender Kurs
		assertThrows(DeveloperNotificationException.class, () -> manager.kursSetSuffix(KURS_NICHT_VORHANDEN, "X"));
	}

	@Test
	@DisplayName("testKursMerge")
	void testKursMerge() {
		// Ohne Blockungsvorlage: Merge nicht erlaubt
		final GostBlockungsdatenManager manager = createManagerMitSchienen(1);
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		manager.kursAdd(createKurs(KURS_ID_2, FACH_M_ID, KURSART_GK, KURS_NR_1));
		assertThrows(DeveloperNotificationException.class, () -> manager.kursMerge(KURS_ID_1, KURS_ID_2));

		// Mit Blockungsvorlage: gleiche IDs
		final GostBlockungsdatenManager managerVorlage = createManagerMitBlockungsvorlage();
		managerVorlage.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		assertThrows(DeveloperNotificationException.class, () -> managerVorlage.kursMerge(KURS_ID_1, KURS_ID_1));

		// Mit Blockungsvorlage: nicht existierender Ziel-Kurs (keep)
		assertThrows(DeveloperNotificationException.class, () -> managerVorlage.kursMerge(KURS_NICHT_VORHANDEN, KURS_ID_1));

		// Mit Blockungsvorlage: nicht existierender Quell-Kurs (delete)
		assertThrows(DeveloperNotificationException.class, () -> managerVorlage.kursMerge(KURS_ID_1, KURS_NICHT_VORHANDEN));

		// Mit Blockungsvorlage: gültiger Merge ohne DummySUS-Regeln
		managerVorlage.kursAdd(createKurs(KURS_ID_2, FACH_M_ID, KURSART_GK, KURS_NR_1));
		assertEquals(2, managerVorlage.kursGetAnzahl());
		assertDoesNotThrow(() -> managerVorlage.kursMerge(KURS_ID_1, KURS_ID_2));
		assertEquals(1, managerVorlage.kursGetAnzahl());
		assertTrue(managerVorlage.kursGetExistiert(KURS_ID_1));
		assertFalse(managerVorlage.kursGetExistiert(KURS_ID_2));
	}

	@Test
	@DisplayName("testKursMergeMitDummySUS")
	void testKursMergeMitDummySUS() {
		// Beide Kurse haben DummySUS-Regel: Summe wird gebildet
		final GostBlockungsdatenManager manBeide = createManagerMitBlockungsvorlage();
		manBeide.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		manBeide.kursAdd(createKurs(KURS_ID_2, FACH_M_ID, KURSART_GK, KURS_NR_1));
		addRegelDummySUS(manBeide, KURS_ID_1, 5);
		addRegelDummySUS(manBeide, KURS_ID_2, 3);
		assertDoesNotThrow(() -> manBeide.kursMerge(KURS_ID_1, KURS_ID_2));
		// Keep-Kurs hat nun die Summe der DummySUS-Zahlen
		final GostBlockungRegel regelBeide = manBeide.kursGetRegelDummySchuelerOrNull(KURS_ID_1);
		assertNotNull(regelBeide);
		assertEquals(8, regelBeide.parameter.get(1));

		// Nur Delete hat DummySUS-Regel: Regel wird auf Keep umgeschrieben
		final GostBlockungsdatenManager manNurDelete = createManagerMitBlockungsvorlage();
		manNurDelete.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		manNurDelete.kursAdd(createKurs(KURS_ID_2, FACH_M_ID, KURSART_GK, KURS_NR_1));
		addRegelDummySUS(manNurDelete, KURS_ID_2, 7);
		assertDoesNotThrow(() -> manNurDelete.kursMerge(KURS_ID_1, KURS_ID_2));
		final GostBlockungRegel regelDelete = manNurDelete.kursGetRegelDummySchuelerOrNull(KURS_ID_1);
		assertNotNull(regelDelete);
		assertEquals(7, regelDelete.parameter.get(1));

		// Nur Keep hat DummySUS-Regel: Regel bleibt unverändert
		final GostBlockungsdatenManager manNurKeep = createManagerMitBlockungsvorlage();
		manNurKeep.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		manNurKeep.kursAdd(createKurs(KURS_ID_2, FACH_M_ID, KURSART_GK, KURS_NR_1));
		addRegelDummySUS(manNurKeep, KURS_ID_1, 4);
		assertDoesNotThrow(() -> manNurKeep.kursMerge(KURS_ID_1, KURS_ID_2));
		final GostBlockungRegel regelKeep = manNurKeep.kursGetRegelDummySchuelerOrNull(KURS_ID_1);
		assertNotNull(regelKeep);
		assertEquals(4, regelKeep.parameter.get(1));

		// Keiner hat DummySUS-Regel: kein Fehler
		final GostBlockungsdatenManager manKeiner = createManagerMitBlockungsvorlage();
		manKeiner.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		manKeiner.kursAdd(createKurs(KURS_ID_2, FACH_M_ID, KURSART_GK, KURS_NR_1));
		assertDoesNotThrow(() -> manKeiner.kursMerge(KURS_ID_1, KURS_ID_2));
		assertNull(manKeiner.kursGetRegelDummySchuelerOrNull(KURS_ID_1));
	}

	@Test
	@DisplayName("testKursGetIstVerbotenInSchiene")
	void testKursGetIstVerbotenInSchiene() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(3);
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));

		// Ohne Regeln: nicht verboten
		assertFalse(manager.kursGetIstVerbotenInSchiene(KURS_ID_1, SCHIENE_ID_1));

		// Nicht existierende Schiene
		assertThrows(DeveloperNotificationException.class, () -> manager.kursGetIstVerbotenInSchiene(KURS_ID_1, SCHIENE_NICHT_VORHANDEN));

		// Nicht existierender Kurs
		assertThrows(DeveloperNotificationException.class, () -> manager.kursGetIstVerbotenInSchiene(KURS_NICHT_VORHANDEN, SCHIENE_ID_1));

		// Typ 01 (KURSART_SPERRE_SCHIENEN_VON_BIS): Kursart GK in Schiene 1..3 verboten
		final GostBlockungRegel r01 = createRegel(10, GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ);
		r01.parameter.add((long) KURSART_GK);
		r01.parameter.add((long) SCHIENE_NR_1);
		r01.parameter.add((long) SCHIENE_NR_3);
		manager.regelAdd(r01);
		assertTrue(manager.kursGetIstVerbotenInSchiene(KURS_ID_1, 1));

		// Typ 06 (KURSART_ALLEIN_IN_SCHIENEN_VON_BIS): LK allein in Schiene 1..2, GK dort verboten
		final GostBlockungRegel r06 = createRegel(11, GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ);
		r06.parameter.add((long) KURSART_LK);
		r06.parameter.add(1L);
		r06.parameter.add(2L);
		manager.regelAdd(r06);
		assertTrue(manager.kursGetIstVerbotenInSchiene(KURS_ID_1, 1)); // GK in LK-Zone --> verboten
	}

	@Test
	@DisplayName("testKursGetHatSperrungInSchiene")
	void testKursGetHatSperrungInSchiene() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(3);
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));

		// Ohne Regeln: keine Sperrung
		assertFalse(manager.kursGetHatSperrungInSchiene(KURS_ID_1, SCHIENE_ID_1));

		// Mit Regel: Sperrung vorhanden
		manager.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1));
		assertTrue(manager.kursGetHatSperrungInSchiene(KURS_ID_1, SCHIENE_ID_1));

		// Nicht existierende Schiene
		assertThrows(DeveloperNotificationException.class, () -> manager.kursGetHatSperrungInSchiene(KURS_ID_1, SCHIENE_NICHT_VORHANDEN));

		// Nicht existierender Kurs
		assertThrows(DeveloperNotificationException.class, () -> manager.kursGetHatSperrungInSchiene(KURS_NICHT_VORHANDEN, SCHIENE_ID_1));
	}

	@Test
	@DisplayName("testKursGetRegelGesperrtInSchiene")
	void testKursGetRegelGesperrtInSchiene() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(3);
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));

		// Ohne Regeln: Exception, da keine Regel existiert
		assertThrows(DeveloperNotificationException.class, () -> manager.kursGetRegelGesperrtInSchiene(KURS_ID_1, SCHIENE_ID_1));

		// Mit Regel: wird gefunden
		manager.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1));
		final GostBlockungRegel regel = manager.kursGetRegelGesperrtInSchiene(KURS_ID_1, SCHIENE_ID_1);
		assertEquals(REGEL_ID_1, regel.id);

		// Nicht existierende Schiene
		assertThrows(DeveloperNotificationException.class, () -> manager.kursGetRegelGesperrtInSchiene(KURS_ID_1, SCHIENE_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testKursGetHatFixierungInSchiene")
	void testKursGetHatFixierungInSchiene() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(3);
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));

		// Ohne Regeln: keine Fixierung
		assertFalse(manager.kursGetHatFixierungInSchiene(KURS_ID_1, SCHIENE_ID_1));

		// Mit Regel: Fixierung vorhanden
		manager.regelAdd(createRegel02KursFixierungInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1));
		assertTrue(manager.kursGetHatFixierungInSchiene(KURS_ID_1, SCHIENE_ID_1));

		// Nicht existierende Schiene
		assertThrows(DeveloperNotificationException.class, () -> manager.kursGetHatFixierungInSchiene(KURS_ID_1, SCHIENE_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testKursGetRegelFixierungInSchiene")
	void testKursGetRegelFixierungInSchiene() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(3);
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));

		// Ohne Regeln: Exception, da keine Regel existiert
		assertThrows(DeveloperNotificationException.class, () -> manager.kursGetRegelFixierungInSchiene(KURS_ID_1, SCHIENE_ID_1));

		// Mit Regel: wird gefunden
		manager.regelAdd(createRegel02KursFixierungInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1));
		final GostBlockungRegel regel = manager.kursGetRegelFixierungInSchiene(KURS_ID_1, SCHIENE_ID_1);
		assertEquals(REGEL_ID_1, regel.id);
	}

	@Test
	@DisplayName("testKursIstWeitereFixierungErlaubt")
	void testKursIstWeitereFixierungErlaubt() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(3);
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));

		// Ohne Fixierungen: weitere erlaubt (anzahlSchienen=1, Fixierungen=0)
		assertTrue(manager.kursIstWeitereFixierungErlaubt(KURS_ID_1));

		// Mit Fixierung: keine weitere erlaubt (anzahlSchienen=1, Fixierungen=1)
		manager.regelAdd(createRegel02KursFixierungInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1));
		assertFalse(manager.kursIstWeitereFixierungErlaubt(KURS_ID_1));

		// Nicht existierender Kurs
		assertThrows(DeveloperNotificationException.class, () -> manager.kursIstWeitereFixierungErlaubt(KURS_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testKursGetRegelDummySchuelerOrNull")
	void testKursGetRegelDummySchuelerOrNull() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(1);
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));

		// Ohne Regel: null
		assertNull(manager.kursGetRegelDummySchuelerOrNull(KURS_ID_1));

		// Mit Regel: nicht null
		addRegelDummySUS(manager, KURS_ID_1, 5);
		final GostBlockungRegel regel = manager.kursGetRegelDummySchuelerOrNull(KURS_ID_1);
		assertNotNull(regel);
		assertEquals(KURS_ID_1, (long) regel.parameter.get(0));
	}

	@Test
	@DisplayName("testKurseRemoveByID")
	void testKurseRemoveByID() {
		// Ohne Blockungsvorlage: Löschen nicht erlaubt
		final GostBlockungsdatenManager manager = createManagerMitSchienen(1);
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		final Set<Long> setKurs1 = Set.of(KURS_ID_1);
		assertThrows(DeveloperNotificationException.class, () -> manager.kurseRemoveByID(setKurs1));

		// Mit Blockungsvorlage: nicht existierender Kurs
		final GostBlockungsdatenManager managerVorlage = createManagerMitBlockungsvorlage();
		final Set<Long> setFremd = Set.of(KURS_NICHT_VORHANDEN);
		assertThrows(DeveloperNotificationException.class, () -> managerVorlage.kurseRemoveByID(setFremd));

		// Mit Blockungsvorlage: gültiges Löschen (mehrere Kurse)
		managerVorlage.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		managerVorlage.kursAdd(createKurs(KURS_ID_2, FACH_M_ID, KURSART_LK, KURS_NR_1));
		assertEquals(2, managerVorlage.kursGetAnzahl());
		assertDoesNotThrow(() -> managerVorlage.kurseRemoveByID(Set.of(KURS_ID_1, KURS_ID_2)));
		assertEquals(0, managerVorlage.kursGetAnzahl());

		// Gültiger und ungültiger Kurs gemischt: Exception + keine Teiloperation
		final GostBlockungsdatenManager manAtomar = createManagerMitBlockungsvorlage();
		manAtomar.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		manAtomar.kursAdd(createKurs(KURS_ID_2, FACH_M_ID, KURSART_LK, KURS_NR_1));
		assertEquals(2, manAtomar.kursGetAnzahl());
		final Set<Long> setMixed = new HashSet<>();
		setMixed.add(KURS_ID_1);
		setMixed.add(KURS_NICHT_VORHANDEN);
		assertThrows(DeveloperNotificationException.class, () -> manAtomar.kurseRemoveByID(setMixed));
		// Atomarität: keine Kurse wurden gelöscht
		assertEquals(2, manAtomar.kursGetAnzahl());
		assertTrue(manAtomar.kursGetExistiert(KURS_ID_1));
		assertTrue(manAtomar.kursGetExistiert(KURS_ID_2));
	}

	@Test
	@DisplayName("testKurseRemove")
	void testKurseRemove() {
		// Ohne Blockungsvorlage: Löschen nicht erlaubt
		final GostBlockungsdatenManager manager = createManagerMitSchienen(1);
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		final List<GostBlockungKurs> list = new ArrayList<>();
		list.add(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		assertThrows(DeveloperNotificationException.class, () -> manager.kurseRemove(list));

		// Mit Blockungsvorlage: gültiges Löschen
		final GostBlockungsdatenManager managerVorlage = createManagerMitBlockungsvorlage();
		managerVorlage.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		final List<GostBlockungKurs> listRemove = new ArrayList<>();
		listRemove.add(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		assertDoesNotThrow(() -> managerVorlage.kurseRemove(listRemove));
		assertEquals(0, managerVorlage.kursGetAnzahl());
	}


	// #########################################################################
	// ##########               Regel-Tests                          ##########
	// #########################################################################

	@Test
	@DisplayName("testRegelAdd")
	void testRegelAdd() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTests();

		// Gültige Regel hinzufügen
		final GostBlockungRegel regel = createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1);
		assertDoesNotThrow(() -> manager.regelAdd(regel));
		assertEquals(1, manager.regelGetAnzahl());
		assertTrue(manager.regelGetExistiert(REGEL_ID_1));

		// Regel mit doppelter ID hinzufügen --> Exception
		final GostBlockungRegel regelDoppeltID = createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_2);
		assertThrows(DeveloperNotificationException.class, () -> manager.regelAdd(regelDoppeltID));
		assertEquals(1, manager.regelGetAnzahl());

		// Regel mit nicht existierendem Kurs hinzufügen --> wird ungültig
		final GostBlockungRegel regelFremderKurs = createRegel03KursSperreInSchiene(REGEL_ID_2, KURS_NICHT_VORHANDEN, SCHIENE_NR_1);
		assertDoesNotThrow(() -> manager.regelAdd(regelFremderKurs));
		assertEquals(1, manager.regelGetAnzahl());
		assertEquals(1, manager.regelGetMapUngueltig().size());

		// Regel mit ungültiger ID --> Exception
		final GostBlockungRegel regelUngueltigeID = createRegel03KursSperreInSchiene(-1, 1, 2);
		assertThrows(DeveloperNotificationException.class, () -> manager.regelAdd(regelUngueltigeID));
		assertEquals(1, manager.regelGetAnzahl());
		assertEquals(1, manager.regelGetMapUngueltig().size());

		// Regel mit falscher Parameter-Anzahl --> Exception
		final GostBlockungRegel regelFalscheParam = createRegel(3, GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ);
		regelFalscheParam.parameter.add(1L);
		assertThrows(DeveloperNotificationException.class, () -> manager.regelAdd(regelFalscheParam));
		assertEquals(1, manager.regelGetAnzahl());
		assertEquals(1, manager.regelGetMapUngueltig().size());

		// Regel mit unbekanntem Typ --> wird ungültig (default-Case im Switch)
		final GostBlockungRegel regelUnbekannterTyp = createRegel(4, REGELTYP_UNGUELTIG);
		manager.regelAdd(regelUnbekannterTyp);
		assertEquals(1, manager.regelGetAnzahl());
		assertEquals(2, manager.regelGetMapUngueltig().size());

		// Mit Blockungsvorlage: ergebnisAlleRevalidieren wird durchlaufen
		final GostBlockungsdatenManager managerVorlage = createManagerMitBlockungsvorlage();
		managerVorlage.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		assertDoesNotThrow(() -> managerVorlage.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1)));
	}

	@Test
	@DisplayName("testRegelAddListe")
	void testRegelAddListe() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTests();

		// Leere Liste hinzufügen
		final List<GostBlockungRegel> emptyList = new ArrayList<>();
		assertDoesNotThrow(() -> manager.regelAddListe(emptyList));
		assertEquals(0, manager.regelGetAnzahl());

		// Zwei gültige Regeln hinzufügen
		final List<GostBlockungRegel> list = new ArrayList<>();
		list.add(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1));
		list.add(createRegel03KursSperreInSchiene(REGEL_ID_2, KURS_ID_1, SCHIENE_NR_2));
		assertDoesNotThrow(() -> manager.regelAddListe(list));
		assertEquals(2, manager.regelGetAnzahl());

		// Liste mit Duplikat (gleicher Multikey) hinzufügen --> zweite wird ungültig
		final List<GostBlockungRegel> listeDuplikat = new ArrayList<>();
		listeDuplikat.add(createRegel03KursSperreInSchiene(REGEL_ID_3, KURS_ID_1, SCHIENE_NR_3));
		listeDuplikat.add(createRegel03KursSperreInSchiene(REGEL_ID_4, KURS_ID_1, SCHIENE_NR_3));
		manager.regelAddListe(listeDuplikat);
		assertEquals(3, manager.regelGetAnzahl());
		assertEquals(1, manager.regelGetMapUngueltig().size());

		// Mit Blockungsvorlage: ergebnisAlleRevalidieren wird durchlaufen
		final GostBlockungsdatenManager managerVorlage = createManagerMitBlockungsvorlage();
		managerVorlage.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		final List<GostBlockungRegel> listVorlage = new ArrayList<>();
		listVorlage.add(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1));
		assertDoesNotThrow(() -> managerVorlage.regelAddListe(listVorlage));
	}

	@Test
	@DisplayName("testRegelGet")
	void testRegelGet() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTests();
		manager.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1));

		// Existierende Regel abrufen
		final GostBlockungRegel regel = manager.regelGet(REGEL_ID_1);
		assertNotNull(regel);
		assertEquals(REGEL_ID_1, regel.id);
		assertEquals(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, regel.typ);

		// Nicht existierende Regel abrufen
		assertThrows(DeveloperNotificationException.class, () -> manager.regelGet(REGEL_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testRegelGetExistiert")
	void testRegelGetExistiert() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTests();
		manager.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1));

		// Existierende Regel
		assertTrue(manager.regelGetExistiert(REGEL_ID_1));

		// Nicht existierende Regel
		assertFalse(manager.regelGetExistiert(REGEL_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testRegelGetAnzahl")
	void testRegelGetAnzahl() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTests();

		// Ohne Regeln: 0
		assertEquals(0, manager.regelGetAnzahl());

		// Eine Regel
		manager.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1));
		assertEquals(1, manager.regelGetAnzahl());

		// Mehrere Regeln
		manager.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_2, KURS_ID_1, SCHIENE_NR_2));
		manager.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_3, KURS_ID_1, SCHIENE_NR_3));
		assertEquals(3, manager.regelGetAnzahl());
	}

	@Test
	@DisplayName("testRegelGetListe")
	void testRegelGetListe() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTests();

		// Leere Liste
		assertEquals(0, manager.regelGetListe().size());

		// Liste mit Regeln
		manager.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1));
		manager.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_2, KURS_ID_1, SCHIENE_NR_2));
		assertEquals(2, manager.regelGetListe().size());
	}

	@Test
	@DisplayName("testRegelGetListeOfTyp")
	void testRegelGetListeOfTyp() {
		// Typ 03: 0 Regeln
		final GostBlockungsdatenManager man0 = createManagerFuerRegelTypTests();
		assertEquals(0, man0.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE).size());

		// Typ 02: 1 Regel
		final GostBlockungsdatenManager man1 = createManagerFuerRegelTypTests();
		man1.regelAdd(createRegel02KursFixierungInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1));
		assertEquals(1, man1.regelGetListeOfTyp(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE).size());

		// Typ 04: 2 Regeln
		final GostBlockungsdatenManager man2 = createManagerFuerRegelTypTests(); // Dieser Manager hat 2 SuS.
		man2.regelAdd(createRegel04SchuelerFixiertInKurs(REGEL_ID_1, SCHUELER_1_ID, KURS_ID_1));
		man2.regelAdd(createRegel04SchuelerFixiertInKurs(REGEL_ID_2, SCHUELER_2_ID, KURS_ID_2));
		assertEquals(2, man2.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS).size());

		// Typ 05: 3 Regeln
		final GostBlockungsdatenManager man3 = createManagerFuerRegelTypTests();
		man3.regelAdd(createRegel05SchuelerVerbotenInKurs(REGEL_ID_1, SCHUELER_1_ID, KURS_ID_1));
		man3.regelAdd(createRegel05SchuelerVerbotenInKurs(REGEL_ID_2, SCHUELER_2_ID, KURS_ID_2));
		man3.regelAdd(createRegel05SchuelerVerbotenInKurs(REGEL_ID_3, SCHUELER_2_ID, KURS_ID_1));
		assertEquals(3, man3.regelGetListeOfTyp(GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS).size());
	}

	@Test
	@DisplayName("testRegelGetByLongArrayKeyOrNull")
	void testRegelGetByLongArrayKeyOrNull() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTests();
		manager.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1));

		// Existierender Schlüssel
		final LongArrayKey keyVorhanden = new LongArrayKey(new long[] {
				GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, KURS_ID_1, SCHIENE_NR_1
		});
		assertNotNull(manager.regelGetByLongArrayKeyOrNull(keyVorhanden));

		// Nicht existierender Schlüssel
		final LongArrayKey keyFremd = new LongArrayKey(new long[] {
				GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, KURS_ID_1, SCHIENE_NR_UNGUELTIG
		});
		assertNull(manager.regelGetByLongArrayKeyOrNull(keyFremd));
	}

	@Test
	@DisplayName("testRegelGetRegelOrDummyKursGesperrtInSchiene")
	void testRegelGetRegelOrDummyKursGesperrtInSchiene() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTests();
		manager.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1));

		// Existierende Regel
		final GostBlockungRegel regel = manager.regelGetRegelOrDummyKursGesperrtInSchiene(KURS_ID_1, SCHIENE_NR_1);
		assertEquals(REGEL_ID_1, regel.id);

		// Nicht existierende Regel --> Dummy (ID negativ)
		final GostBlockungRegel dummy = manager.regelGetRegelOrDummyKursGesperrtInSchiene(KURS_ID_1, SCHIENE_NR_2);
		assertEquals(-1, dummy.id);
		assertEquals(GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, dummy.typ);
	}

	@Test
	@DisplayName("testRegelGetRegelOrDummyKursFixierungInSchiene")
	void testRegelGetRegelOrDummyKursFixierungInSchiene() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTests();
		manager.regelAdd(createRegel02KursFixierungInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1));

		// Existierende Regel
		final GostBlockungRegel regel = manager.regelGetRegelOrDummyKursFixierungInSchiene(KURS_ID_1, SCHIENE_NR_1);
		assertEquals(REGEL_ID_1, regel.id);

		// Nicht existierende Regel --> Dummy (ID negativ)
		final GostBlockungRegel dummy = manager.regelGetRegelOrDummyKursFixierungInSchiene(KURS_ID_1, SCHIENE_NR_2);
		assertEquals(-1, dummy.id);
		assertEquals(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, dummy.typ);
	}

	@Test
	@DisplayName("testRegelGetRegelOrDummySchuelerInKursFixierung")
	void testRegelGetRegelOrDummySchuelerInKursFixierung() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTests();

		// Nicht existierende Regel --> Dummy (ID negativ)
		final GostBlockungRegel dummy = manager.regelGetRegelOrDummySchuelerInKursFixierung(SCHUELER_1_ID, KURS_ID_1);
		assertEquals(-1, dummy.id);
		assertEquals(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, dummy.typ);
	}

	@Test
	@DisplayName("testRegelGetIsRemoveAllowed")
	void testRegelGetIsRemoveAllowed() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTests();
		manager.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1));

		// Existierende Regel: true
		assertTrue(manager.regelGetIsRemoveAllowed(REGEL_ID_1));

		// Nicht existierende Regel: false
		assertFalse(manager.regelGetIsRemoveAllowed(REGEL_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testRegelRemoveByID")
	void testRegelRemoveByID() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTests();
		manager.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1));

		// Gültiges Löschen
		assertEquals(1, manager.regelGetAnzahl());
		assertDoesNotThrow(() -> manager.regelRemoveByID(REGEL_ID_1));
		assertEquals(0, manager.regelGetAnzahl());
		assertFalse(manager.regelGetExistiert(REGEL_ID_1));

		// Nicht existierende Regel löschen
		assertThrows(DeveloperNotificationException.class, () -> manager.regelRemoveByID(REGEL_NICHT_VORHANDEN));

		// Ungültige Regel löschen (z.B. Regel mit nicht existierendem Kurs)
		final GostBlockungRegel regelUngueltig = createRegel03KursSperreInSchiene(REGEL_ID_2, KURS_NICHT_VORHANDEN, SCHIENE_NR_1);
		manager.regelAdd(regelUngueltig);
		assertEquals(0, manager.regelGetAnzahl());
		assertEquals(1, manager.regelGetMapUngueltig().size());
		assertDoesNotThrow(() -> manager.regelRemoveByID(REGEL_ID_2));
		assertEquals(0, manager.regelGetMapUngueltig().size());
		assertEquals(0, manager.regelGetMapUngueltigBeschreibung().size());

		// Mit Blockungsvorlage: ergebnisAlleRevalidieren wird durchlaufen
		final GostBlockungsdatenManager managerVorlage = createManagerMitBlockungsvorlage();
		managerVorlage.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		managerVorlage.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1));
		assertDoesNotThrow(() -> managerVorlage.regelRemoveByID(REGEL_ID_1));
	}

	@Test
	@DisplayName("testRegelRemove")
	void testRegelRemove() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTests();
		manager.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1));

		// Gültiges Löschen (über anderes Objekt mit gleicher ID)
		final GostBlockungRegel regelAndereRef = createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1);
		assertDoesNotThrow(() -> manager.regelRemove(regelAndereRef));
		assertEquals(0, manager.regelGetAnzahl());
	}

	@Test
	@DisplayName("testRegelRemoveListe")
	void testRegelRemoveListe() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTests();
		manager.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1));
		manager.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_2, KURS_ID_1, SCHIENE_NR_2));

		// Leere Liste entfernen
		final List<GostBlockungRegel> emptyList = new ArrayList<>();
		assertDoesNotThrow(() -> manager.regelRemoveListe(emptyList));
		assertEquals(2, manager.regelGetAnzahl());

		// Zwei Regeln entfernen
		final List<GostBlockungRegel> list = new ArrayList<>();
		list.add(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1));
		list.add(createRegel03KursSperreInSchiene(REGEL_ID_2, KURS_ID_1, SCHIENE_NR_2));
		assertDoesNotThrow(() -> manager.regelRemoveListe(list));
		assertEquals(0, manager.regelGetAnzahl());
	}

	@Test
	@DisplayName("testRegelRemoveListeByIDs")
	void testRegelRemoveListeByIDs() {
		// Leeres Set entfernen
		final GostBlockungsdatenManager manLeer = createManagerFuerRegelTests();
		final Set<Long> emptySet = new HashSet<>();
		assertDoesNotThrow(() -> manLeer.regelRemoveListeByIDs(emptySet));
		assertEquals(0, manLeer.regelGetAnzahl());

		// gültig + gültig: beide werden gelöscht
		final GostBlockungsdatenManager manGG = createManagerFuerRegelTests();
		manGG.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1));
		manGG.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_2, KURS_ID_1, SCHIENE_NR_2));
		assertEquals(2, manGG.regelGetAnzahl());
		final Set<Long> setGG = Set.of(REGEL_ID_1, REGEL_ID_2);
		assertDoesNotThrow(() -> manGG.regelRemoveListeByIDs(setGG));
		assertEquals(0, manGG.regelGetAnzahl());

		// gültig + ungültig: beide werden gelöscht
		final GostBlockungsdatenManager manGU = createManagerFuerRegelTests();
		manGU.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1));
		manGU.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_2, KURS_NICHT_VORHANDEN, SCHIENE_NR_1));
		assertEquals(1, manGU.regelGetAnzahl());
		assertEquals(1, manGU.regelGetMapUngueltig().size());
		final Set<Long> setGU = Set.of(REGEL_ID_1, REGEL_ID_2);
		assertDoesNotThrow(() -> manGU.regelRemoveListeByIDs(setGU));
		assertEquals(0, manGU.regelGetAnzahl());
		assertEquals(0, manGU.regelGetMapUngueltig().size());

		// ungültig + gültig: beide werden gelöscht
		final GostBlockungsdatenManager manUG = createManagerFuerRegelTests();
		manUG.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_2, KURS_NICHT_VORHANDEN, SCHIENE_NR_1));
		manUG.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1));
		assertEquals(1, manUG.regelGetAnzahl());
		assertEquals(1, manUG.regelGetMapUngueltig().size());
		final Set<Long> setUG = Set.of(REGEL_ID_1, REGEL_ID_2);
		assertDoesNotThrow(() -> manUG.regelRemoveListeByIDs(setUG));
		assertEquals(0, manUG.regelGetAnzahl());
		assertEquals(0, manUG.regelGetMapUngueltig().size());

		// ungültig + ungültig: beide werden gelöscht
		final GostBlockungsdatenManager manUU = createManagerFuerRegelTests();
		manUU.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_NICHT_VORHANDEN, SCHIENE_NR_1));
		manUU.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_2, KURS_NICHT_VORHANDEN, SCHIENE_NR_1));
		assertEquals(0, manUU.regelGetAnzahl());
		assertEquals(2, manUU.regelGetMapUngueltig().size());
		final Set<Long> setUU = Set.of(REGEL_ID_1, REGEL_ID_2);
		assertDoesNotThrow(() -> manUU.regelRemoveListeByIDs(setUU));
		assertEquals(0, manUU.regelGetMapUngueltig().size());

		// Mit Blockungsvorlage: ergebnisAlleRevalidieren wird durchlaufen
		final GostBlockungsdatenManager manVorlage = createManagerMitBlockungsvorlage();
		manVorlage.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		manVorlage.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1));
		final Set<Long> setVorlage = new HashSet<>();
		setVorlage.add(REGEL_ID_1);
		assertDoesNotThrow(() -> manVorlage.regelRemoveListeByIDs(setVorlage));
	}

	@Test
	@DisplayName("testRegelRemoveListeByIDsAtomar")
	void testRegelRemoveListeByIDsAtomar() {
		// gültig + nicht vorhanden: Exception, keine Löschung
		final GostBlockungsdatenManager manGN = createManagerFuerRegelTests();
		manGN.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1));
		assertEquals(1, manGN.regelGetAnzahl());
		final Set<Long> setGN = Set.of(REGEL_ID_1, REGEL_NICHT_VORHANDEN);
		assertThrows(DeveloperNotificationException.class, () -> manGN.regelRemoveListeByIDs(setGN));
		assertEquals(1, manGN.regelGetAnzahl());

		// ungültig + nicht vorhanden: Exception, keine Löschung
		final GostBlockungsdatenManager manUN = createManagerFuerRegelTests();
		manUN.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_NICHT_VORHANDEN, SCHIENE_NR_1));
		assertEquals(1, manUN.regelGetMapUngueltig().size());
		final Set<Long> setUN = Set.of(REGEL_ID_1, REGEL_NICHT_VORHANDEN);
		assertThrows(DeveloperNotificationException.class, () -> manUN.regelRemoveListeByIDs(setUN));
		assertEquals(1, manUN.regelGetMapUngueltig().size());

		// gültig + nicht vorhanden + gültig: Exception, keine Löschung (auch die gültigen bleiben)
		final GostBlockungsdatenManager manGNG = createManagerFuerRegelTests();
		manGNG.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1));
		manGNG.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_2, KURS_ID_1, SCHIENE_NR_2));
		assertEquals(2, manGNG.regelGetAnzahl());
		final Set<Long> setGNG = Set.of(REGEL_ID_1, REGEL_NICHT_VORHANDEN, REGEL_ID_2);
		assertThrows(DeveloperNotificationException.class, () -> manGNG.regelRemoveListeByIDs(setGNG));
		assertEquals(2, manGNG.regelGetAnzahl());

		// ungültig + nicht vorhanden + ungültig: Exception, ungültige bleiben erhalten
		final GostBlockungsdatenManager manUNU = createManagerFuerRegelTests();
		manUNU.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_NICHT_VORHANDEN, SCHIENE_NR_1));
		manUNU.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_2, KURS_NICHT_VORHANDEN, SCHIENE_NR_2));
		assertEquals(2, manUNU.regelGetMapUngueltig().size());
		final Set<Long> setUNU = Set.of(REGEL_ID_1, REGEL_NICHT_VORHANDEN, REGEL_ID_2);
		assertThrows(DeveloperNotificationException.class, () -> manUNU.regelRemoveListeByIDs(setUNU));
		assertEquals(2, manUNU.regelGetMapUngueltig().size());

		// ungültig + nicht vorhanden + gültig: Exception, alle bleiben erhalten
		final GostBlockungsdatenManager manUNG = createManagerFuerRegelTests();
		manUNG.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_NICHT_VORHANDEN, SCHIENE_NR_1));
		manUNG.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_2, KURS_ID_1, SCHIENE_NR_1));
		assertEquals(1, manUNG.regelGetAnzahl());
		assertEquals(1, manUNG.regelGetMapUngueltig().size());
		final Set<Long> setUNG = Set.of(REGEL_ID_1, REGEL_NICHT_VORHANDEN, REGEL_ID_2);
		assertThrows(DeveloperNotificationException.class, () -> manUNG.regelRemoveListeByIDs(setUNG));
		assertEquals(1, manUNG.regelGetAnzahl());
		assertEquals(1, manUNG.regelGetMapUngueltig().size());
	}

	@Test
	@DisplayName("testRegelTyp01")
	void testRegelTyp01() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTypTests();

		// Gültig: Kursart GK, Schienen 1 bis 3
		final GostBlockungRegel r = createRegel(REGEL_ID_1, GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ);
		r.parameter.add((long) KURSART_GK);
		r.parameter.add(1L);
		r.parameter.add(3L);
		manager.regelAdd(r);
		assertTrue(manager.regelGetExistiert(REGEL_ID_1));

		// Ungültige Kursart
		final GostBlockungRegel rK = createRegel(2, GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ);
		rK.parameter.add((long) KURSART_UNGUELTIG);
		rK.parameter.add(1L);
		rK.parameter.add(3L);
		manager.regelAdd(rK);
		assertEquals(1, manager.regelGetMapUngueltig().size());

		// Ungültige Schienen-Nr
		final GostBlockungRegel rS = createRegel(3, GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ);
		rS.parameter.add((long) KURSART_GK);
		rS.parameter.add(0L);
		rS.parameter.add(3L);
		manager.regelAdd(rS);
		assertEquals(2, manager.regelGetMapUngueltig().size());

		// bis < von
		final GostBlockungRegel rB = createRegel(4, GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ);
		rB.parameter.add((long) KURSART_GK);
		rB.parameter.add(3L);
		rB.parameter.add(1L);
		manager.regelAdd(rB);
		assertEquals(3, manager.regelGetMapUngueltig().size());
	}

	@Test
	@DisplayName("testRegelTyp02")
	void testRegelTyp02() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTypTests();

		// Gültig: KURS_FIXIERE_IN_SCHIENE
		manager.regelAdd(createRegel02KursFixierungInSchiene(REGEL_ID_1, KURS_ID_2, SCHIENE_NR_1));
		assertTrue(manager.regelGetExistiert(REGEL_ID_1));

		// Ungültige Kurs-ID
		final GostBlockungRegel rK = createRegel02KursFixierungInSchiene(REGEL_ID_2, KURS_NICHT_VORHANDEN, SCHIENE_NR_1);
		manager.regelAdd(rK);
		assertEquals(1, manager.regelGetMapUngueltig().size());

		// Ungültige Schienen-Nr
		final GostBlockungRegel rS = createRegel02KursFixierungInSchiene(REGEL_ID_3, KURS_ID_1, SCHIENE_NR_UNGUELTIG);
		manager.regelAdd(rS);
		assertEquals(2, manager.regelGetMapUngueltig().size());
	}

	@Test
	@DisplayName("testRegelTyp03")
	void testRegelTyp03() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTypTests();

		// Gültig: KURS_SPERRE_IN_SCHIENE
		manager.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1));
		assertTrue(manager.regelGetExistiert(REGEL_ID_1));

		// Ungültige Kurs-ID
		final GostBlockungRegel rK = createRegel03KursSperreInSchiene(REGEL_ID_2, KURS_NICHT_VORHANDEN, SCHIENE_NR_1);
		manager.regelAdd(rK);
		assertEquals(1, manager.regelGetMapUngueltig().size());

		// Ungültige Schienen-Nr
		final GostBlockungRegel rS = createRegel03KursSperreInSchiene(REGEL_ID_3, KURS_ID_1, SCHIENE_NR_UNGUELTIG);
		manager.regelAdd(rS);
		assertEquals(2, manager.regelGetMapUngueltig().size());
	}

	@Test
	@DisplayName("testRegelTyp04")
	void testRegelTyp04() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTypTests();

		// Gültig: SCHUELER_FIXIEREN_IN_KURS
		manager.regelAdd(createRegel04SchuelerFixiertInKurs(REGEL_ID_1, SCHUELER_1_ID, KURS_ID_1));
		assertTrue(manager.regelGetExistiert(REGEL_ID_1));

		// Ungültige Schüler-ID
		final GostBlockungRegel rS = createRegel04SchuelerFixiertInKurs(REGEL_ID_2, SCHUELER_NICHT_VORHANDEN, KURS_ID_1);
		manager.regelAdd(rS);
		assertEquals(1, manager.regelGetMapUngueltig().size());

		// Ungültige Kurs-ID
		final GostBlockungRegel rK = createRegel04SchuelerFixiertInKurs(REGEL_ID_3, SCHUELER_1_ID, KURS_NICHT_VORHANDEN);
		manager.regelAdd(rK);
		assertEquals(2, manager.regelGetMapUngueltig().size());
	}

	@Test
	@DisplayName("testRegelTyp05")
	void testRegelTyp05() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTypTests();

		// Gültig: SCHUELER_VERBIETEN_IN_KURS
		manager.regelAdd(createRegel05SchuelerVerbotenInKurs(REGEL_ID_1, SCHUELER_2_ID, KURS_ID_2));
		assertTrue(manager.regelGetExistiert(REGEL_ID_1));

		// Ungültige Schüler-ID
		final GostBlockungRegel rS = createRegel05SchuelerVerbotenInKurs(REGEL_ID_2, SCHUELER_NICHT_VORHANDEN, KURS_ID_1);
		manager.regelAdd(rS);
		assertEquals(1, manager.regelGetMapUngueltig().size());

		// Ungültige Kurs-ID
		final GostBlockungRegel rK = createRegel05SchuelerVerbotenInKurs(REGEL_ID_3, SCHUELER_1_ID, KURS_NICHT_VORHANDEN);
		manager.regelAdd(rK);
		assertEquals(2, manager.regelGetMapUngueltig().size());
	}

	@Test
	@DisplayName("testRegelTyp06")
	void testRegelTyp06() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTypTests();

		// Gültig: Kursart LK, Schienen 1 bis 3
		final GostBlockungRegel r = createRegel(REGEL_ID_1, GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ);
		r.parameter.add((long) KURSART_LK);
		r.parameter.add((long) SCHIENE_NR_1);
		r.parameter.add((long) SCHIENE_NR_3);
		manager.regelAdd(r);
		assertTrue(manager.regelGetExistiert(REGEL_ID_1));

		// Ungültige Kursart
		final GostBlockungRegel rK = createRegel(REGEL_ID_2, GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ);
		rK.parameter.add((long) KURSART_UNGUELTIG);
		rK.parameter.add((long) SCHIENE_NR_1);
		rK.parameter.add((long) SCHIENE_NR_3);
		manager.regelAdd(rK);
		assertEquals(1, manager.regelGetMapUngueltig().size());

		// Ungültige Schienen-Nr
		final GostBlockungRegel rS = createRegel(REGEL_ID_3, GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ);
		rS.parameter.add((long) KURSART_LK);
		rS.parameter.add(0L);
		rS.parameter.add((long) SCHIENE_NR_3);
		manager.regelAdd(rS);
		assertEquals(2, manager.regelGetMapUngueltig().size());

		// bis < von
		final GostBlockungRegel rB = createRegel(REGEL_ID_4, GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ);
		rB.parameter.add((long) KURSART_LK);
		rB.parameter.add((long) SCHIENE_NR_3);
		rB.parameter.add((long) SCHIENE_NR_1);
		manager.regelAdd(rB);
		assertEquals(3, manager.regelGetMapUngueltig().size());

		// Cross-Call-Duplikat: selbe Kursart LK in separatem regelAdd
		final GostBlockungRegel rCross = createRegel(REGEL_ID_5, GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ);
		rCross.parameter.add((long) KURSART_LK);
		rCross.parameter.add((long) SCHIENE_NR_1);
		rCross.parameter.add((long) SCHIENE_NR_2);
		manager.regelAdd(rCross);
		assertEquals(4, manager.regelGetMapUngueltig().size());
	}

	@Test
	@DisplayName("testRegelTyp07")
	void testRegelTyp07() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTypTests();

		// Gültig: KURS_VERBIETEN_MIT_KURS
		final GostBlockungRegel r = createRegel(REGEL_ID_1, GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ);
		r.parameter.add(KURS_ID_1);
		r.parameter.add(KURS_ID_2);
		manager.regelAdd(r);
		assertTrue(manager.regelGetExistiert(REGEL_ID_1));

		// Ungültige Kurs-ID 0
		final GostBlockungRegel rK0 = createRegel(REGEL_ID_2, GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ);
		rK0.parameter.add(KURS_NICHT_VORHANDEN);
		rK0.parameter.add(KURS_ID_2);
		manager.regelAdd(rK0);
		assertEquals(1, manager.regelGetMapUngueltig().size());

		// Ungültige Kurs-ID 1
		final GostBlockungRegel rK1 = createRegel(REGEL_ID_3, GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ);
		rK1.parameter.add(KURS_ID_1);
		rK1.parameter.add(KURS_NICHT_VORHANDEN);
		manager.regelAdd(rK1);
		assertEquals(2, manager.regelGetMapUngueltig().size());
	}

	@Test
	@DisplayName("testRegelTyp08")
	void testRegelTyp08() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTypTests();

		// Gültig: KURS_ZUSAMMEN_MIT_KURS
		final GostBlockungRegel r = createRegel(REGEL_ID_1, GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ);
		r.parameter.add(KURS_ID_1);
		r.parameter.add(KURS_ID_2);
		manager.regelAdd(r);
		assertTrue(manager.regelGetExistiert(REGEL_ID_1));

		// Ungültige Kurs-ID 0
		final GostBlockungRegel rK0 = createRegel(REGEL_ID_2, GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ);
		rK0.parameter.add(KURS_NICHT_VORHANDEN);
		rK0.parameter.add(KURS_ID_2);
		manager.regelAdd(rK0);
		assertEquals(1, manager.regelGetMapUngueltig().size());

		// Ungültige Kurs-ID 1
		final GostBlockungRegel rK1 = createRegel(REGEL_ID_3, GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ);
		rK1.parameter.add(KURS_ID_1);
		rK1.parameter.add(KURS_NICHT_VORHANDEN);
		manager.regelAdd(rK1);
		assertEquals(2, manager.regelGetMapUngueltig().size());
	}

	@Test
	@DisplayName("testRegelTyp09")
	void testRegelTyp09() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTypTests();

		// Gültig: Kurs 1, Anzahl 5
		final GostBlockungRegel r = createRegel(REGEL_ID_1, GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ);
		r.parameter.add(KURS_ID_1);
		r.parameter.add(5L);
		manager.regelAdd(r);
		assertTrue(manager.regelGetExistiert(REGEL_ID_1));

		// Ungültige Kurs-ID
		final GostBlockungRegel rK = createRegel(REGEL_ID_2, GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ);
		rK.parameter.add(KURS_NICHT_VORHANDEN);
		rK.parameter.add(5L);
		manager.regelAdd(rK);
		assertEquals(1, manager.regelGetMapUngueltig().size());

		// Anzahl zu klein (MIN - 1)
		final long dummyMinUnterschritten = GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN_MIN - 1L;
		final GostBlockungRegel rMin = createRegel(REGEL_ID_3, GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ);
		rMin.parameter.add(KURS_ID_1);
		rMin.parameter.add(dummyMinUnterschritten);
		manager.regelAdd(rMin);
		assertEquals(2, manager.regelGetMapUngueltig().size());

		// Anzahl zu groß (MAX + 1)
		final long dummyMaxUeberschritten = GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN_MAX + 1L;
		final GostBlockungRegel rMax = createRegel(REGEL_ID_4, GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ);
		rMax.parameter.add(KURS_ID_1);
		rMax.parameter.add(dummyMaxUeberschritten);
		manager.regelAdd(rMax);
		assertEquals(3, manager.regelGetMapUngueltig().size());

		// Duplikat (gleicher Kurs in einer Liste)
		final GostBlockungRegel rA = createRegel(5, GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ);
		rA.parameter.add(KURS_ID_2);
		rA.parameter.add(5L);
		final GostBlockungRegel rB = createRegel(6, GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ);
		rB.parameter.add(KURS_ID_2);
		rB.parameter.add(10L);
		final List<GostBlockungRegel> list = new ArrayList<>();
		list.add(rA);
		list.add(rB);
		manager.regelAddListe(list);
		assertEquals(4, manager.regelGetMapUngueltig().size());

		// Cross-Call-Duplikat: selber Kurs in separatem regelAdd-Aufruf
		// Nach erfolgreicher Regel für Kurs 2 (rA) darf keine weitere für Kurs 2 hinzugefügt werden
		final GostBlockungRegel rCross = createRegel(7, GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ);
		rCross.parameter.add(KURS_ID_2);
		rCross.parameter.add(7L);
		manager.regelAdd(rCross);
		assertEquals(5, manager.regelGetMapUngueltig().size());
	}

	@Test
	@DisplayName("testRegelTyp10")
	void testRegelTyp10() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTypTests();

		// Gültig
		final GostBlockungRegel r = createRegel(REGEL_ID_1, GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN.typ);
		manager.regelAdd(r);
		assertTrue(manager.regelGetExistiert(REGEL_ID_1));

		// Zwei Duplikate --> beide kollidieren mit vorhandener Regel
		final GostBlockungRegel rA = createRegel(REGEL_ID_2, GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN.typ);
		final GostBlockungRegel rB = createRegel(REGEL_ID_3, GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN.typ);
		final List<GostBlockungRegel> list = new ArrayList<>();
		list.add(rA);
		list.add(rB);
		manager.regelAddListe(list);
		assertEquals(2, manager.regelGetMapUngueltig().size());
	}

	@Test
	@DisplayName("testRegelTyp11")
	void testRegelTyp11() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTypTests();

		// Gültig: SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH
		final GostBlockungRegel r = createRegel(REGEL_ID_1, GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ);
		r.parameter.add(SCHUELER_1_ID);
		r.parameter.add(SCHUELER_2_ID);
		r.parameter.add(FACH_D_ID);
		manager.regelAdd(r);
		assertTrue(manager.regelGetExistiert(REGEL_ID_1));

		// Ungültige Schüler-ID 0
		final GostBlockungRegel rS0 = createRegel(REGEL_ID_2, GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ);
		rS0.parameter.add(SCHUELER_NICHT_VORHANDEN);
		rS0.parameter.add(SCHUELER_2_ID);
		rS0.parameter.add(FACH_D_ID);
		manager.regelAdd(rS0);
		assertEquals(1, manager.regelGetMapUngueltig().size());

		// Ungültige Schüler-ID 1
		final GostBlockungRegel rS1 = createRegel(REGEL_ID_3, GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ);
		rS1.parameter.add(SCHUELER_1_ID);
		rS1.parameter.add(SCHUELER_NICHT_VORHANDEN);
		rS1.parameter.add(FACH_D_ID);
		manager.regelAdd(rS1);
		assertEquals(2, manager.regelGetMapUngueltig().size());

		// Ungültige Fach-ID
		final GostBlockungRegel rF = createRegel(REGEL_ID_4, GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ);
		rF.parameter.add(SCHUELER_1_ID);
		rF.parameter.add(SCHUELER_2_ID);
		rF.parameter.add(FACH_NICHT_VORHANDEN);
		manager.regelAdd(rF);
		assertEquals(3, manager.regelGetMapUngueltig().size());
	}

	@Test
	@DisplayName("testRegelTyp12")
	void testRegelTyp12() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTypTests();

		// Gültig: SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH
		final GostBlockungRegel r = createRegel(REGEL_ID_1, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ);
		r.parameter.add(SCHUELER_1_ID);
		r.parameter.add(SCHUELER_2_ID);
		r.parameter.add(FACH_M_ID);
		manager.regelAdd(r);
		assertTrue(manager.regelGetExistiert(REGEL_ID_1));

		// Ungültige Schüler-ID 0
		final GostBlockungRegel rS0 = createRegel(REGEL_ID_2, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ);
		rS0.parameter.add(SCHUELER_NICHT_VORHANDEN);
		rS0.parameter.add(SCHUELER_2_ID);
		rS0.parameter.add(FACH_D_ID);
		manager.regelAdd(rS0);
		assertEquals(1, manager.regelGetMapUngueltig().size());

		// Ungültige Schüler-ID 1
		final GostBlockungRegel rS1 = createRegel(REGEL_ID_3, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ);
		rS1.parameter.add(SCHUELER_1_ID);
		rS1.parameter.add(SCHUELER_NICHT_VORHANDEN);
		rS1.parameter.add(FACH_D_ID);
		manager.regelAdd(rS1);
		assertEquals(2, manager.regelGetMapUngueltig().size());

		// Ungültige Fach-ID
		final GostBlockungRegel rF = createRegel(REGEL_ID_4, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ);
		rF.parameter.add(SCHUELER_1_ID);
		rF.parameter.add(SCHUELER_2_ID);
		rF.parameter.add(FACH_NICHT_VORHANDEN);
		manager.regelAdd(rF);
		assertEquals(3, manager.regelGetMapUngueltig().size());
	}

	@Test
	@DisplayName("testRegelTyp13")
	void testRegelTyp13() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTypTests();

		// Gültig: SCHUELER_ZUSAMMEN_MIT_SCHUELER
		final GostBlockungRegel r = createRegel(REGEL_ID_1, GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ);
		r.parameter.add(SCHUELER_1_ID);
		r.parameter.add(SCHUELER_2_ID);
		manager.regelAdd(r);
		assertTrue(manager.regelGetExistiert(REGEL_ID_1));

		// Ungültige Schüler-ID 0
		final GostBlockungRegel rS0 = createRegel(REGEL_ID_2, GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ);
		rS0.parameter.add(SCHUELER_NICHT_VORHANDEN);
		rS0.parameter.add(SCHUELER_2_ID);
		manager.regelAdd(rS0);
		assertEquals(1, manager.regelGetMapUngueltig().size());

		// Ungültige Schüler-ID 1
		final GostBlockungRegel rS1 = createRegel(REGEL_ID_3, GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ);
		rS1.parameter.add(SCHUELER_1_ID);
		rS1.parameter.add(SCHUELER_NICHT_VORHANDEN);
		manager.regelAdd(rS1);
		assertEquals(2, manager.regelGetMapUngueltig().size());
	}

	@Test
	@DisplayName("testRegelTyp14")
	void testRegelTyp14() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTypTests();

		// Gültig: SCHUELER_VERBIETEN_MIT_SCHUELER
		final GostBlockungRegel r = createRegel(REGEL_ID_1, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ);
		r.parameter.add(SCHUELER_1_ID);
		r.parameter.add(SCHUELER_2_ID);
		manager.regelAdd(r);
		assertTrue(manager.regelGetExistiert(REGEL_ID_1));

		// Ungültige Schüler-ID 0
		final GostBlockungRegel rS0 = createRegel(REGEL_ID_2, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ);
		rS0.parameter.add(SCHUELER_NICHT_VORHANDEN);
		rS0.parameter.add(SCHUELER_2_ID);
		manager.regelAdd(rS0);
		assertEquals(1, manager.regelGetMapUngueltig().size());

		// Ungültige Schüler-ID 1
		final GostBlockungRegel rS1 = createRegel(REGEL_ID_3, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ);
		rS1.parameter.add(SCHUELER_1_ID);
		rS1.parameter.add(SCHUELER_NICHT_VORHANDEN);
		manager.regelAdd(rS1);
		assertEquals(2, manager.regelGetMapUngueltig().size());
	}

	@Test
	@DisplayName("testRegelTyp15")
	void testRegelTyp15() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTypTests();

		// Gültig: Kurs 1, max 30
		final GostBlockungRegel r = createRegel(REGEL_ID_1, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ);
		r.parameter.add(KURS_ID_1);
		r.parameter.add(30L);
		manager.regelAdd(r);
		assertTrue(manager.regelGetExistiert(REGEL_ID_1));

		// Ungültige Kurs-ID
		final GostBlockungRegel rK = createRegel(REGEL_ID_2, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ);
		rK.parameter.add(KURS_NICHT_VORHANDEN);
		rK.parameter.add(30L);
		manager.regelAdd(rK);
		assertEquals(1, manager.regelGetMapUngueltig().size());

		// Anzahl zu klein (MIN - 1)
		final long maxSchuelerMinUnterschritten = GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL_MIN - 1L;
		final GostBlockungRegel rMin = createRegel(REGEL_ID_3, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ);
		rMin.parameter.add(KURS_ID_1);
		rMin.parameter.add(maxSchuelerMinUnterschritten);
		manager.regelAdd(rMin);
		assertEquals(2, manager.regelGetMapUngueltig().size());

		// Anzahl zu groß (MAX + 1)
		final long maxSchuelerMaxUeberschritten = GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL_MAX + 1L;
		final GostBlockungRegel rMax = createRegel(REGEL_ID_4, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ);
		rMax.parameter.add(KURS_ID_1);
		rMax.parameter.add(maxSchuelerMaxUeberschritten);
		manager.regelAdd(rMax);
		assertEquals(3, manager.regelGetMapUngueltig().size());

		// Same-Call-Duplikat: zwei Regeln für denselben Kurs in einer Liste
		final GostBlockungRegel rA = createRegel(REGEL_ID_5, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ);
		rA.parameter.add(KURS_ID_2);
		rA.parameter.add(30L);
		final GostBlockungRegel rB = createRegel(REGEL_ID_6, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ);
		rB.parameter.add(KURS_ID_2);
		rB.parameter.add(25L);
		final List<GostBlockungRegel> list = new ArrayList<>();
		list.add(rA);
		list.add(rB);
		manager.regelAddListe(list);
		assertEquals(4, manager.regelGetMapUngueltig().size());

		// Cross-Call-Duplikat: selber Kurs in separatem regelAdd
		final GostBlockungRegel rCross = createRegel(7, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ);
		rCross.parameter.add(KURS_ID_1);
		rCross.parameter.add(20L);
		manager.regelAdd(rCross);
		assertEquals(5, manager.regelGetMapUngueltig().size());
	}

	@Test
	@DisplayName("testRegelTyp16")
	void testRegelTyp16() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTypTests();

		// Gültig
		final GostBlockungRegel r = createRegel(REGEL_ID_1, GostKursblockungRegelTyp.SCHUELER_IGNORIEREN.typ);
		r.parameter.add(SCHUELER_1_ID);
		manager.regelAdd(r);
		assertTrue(manager.regelGetExistiert(REGEL_ID_1));

		// Ungültige Schüler-ID
		final GostBlockungRegel rS = createRegel(REGEL_ID_2, GostKursblockungRegelTyp.SCHUELER_IGNORIEREN.typ);
		rS.parameter.add(SCHUELER_NICHT_VORHANDEN);
		manager.regelAdd(rS);
		assertEquals(1, manager.regelGetMapUngueltig().size());

		// Duplikat: gleicher Schüler wird ignoriert (Multikey-Kollision)
		final GostBlockungRegel rDup = createRegel(REGEL_ID_3, GostKursblockungRegelTyp.SCHUELER_IGNORIEREN.typ);
		rDup.parameter.add(SCHUELER_1_ID);
		manager.regelAdd(rDup);
		assertEquals(2, manager.regelGetMapUngueltig().size());
	}

	@Test
	@DisplayName("testRegelTyp17")
	void testRegelTyp17() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTypTests();

		// Gültig
		final GostBlockungRegel r = createRegel(REGEL_ID_1, GostKursblockungRegelTyp.KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN.typ);
		r.parameter.add(KURS_ID_1);
		manager.regelAdd(r);
		assertTrue(manager.regelGetExistiert(REGEL_ID_1));

		// Ungültige Kurs-ID
		final GostBlockungRegel rK = createRegel(REGEL_ID_2, GostKursblockungRegelTyp.KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN.typ);
		rK.parameter.add(KURS_NICHT_VORHANDEN);
		manager.regelAdd(rK);
		assertEquals(1, manager.regelGetMapUngueltig().size());

		// Duplikat: gleicher Kurs (Multikey-Kollision)
		final GostBlockungRegel rDup = createRegel(REGEL_ID_3, GostKursblockungRegelTyp.KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN.typ);
		rDup.parameter.add(KURS_ID_1);
		manager.regelAdd(rDup);
		assertEquals(2, manager.regelGetMapUngueltig().size());
	}

	@Test
	@DisplayName("testRegelTyp18")
	void testRegelTyp18() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTypTests();

		// Gültig: Fach 1, Kursart GK, max 3
		final GostBlockungRegel r = createRegel(REGEL_ID_1, GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE.typ);
		r.parameter.add(1L);
		r.parameter.add((long) KURSART_GK);
		r.parameter.add(3L);
		manager.regelAdd(r);
		assertTrue(manager.regelGetExistiert(REGEL_ID_1));

		// Ungültige Fach-ID
		final GostBlockungRegel rF = createRegel(2, GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE.typ);
		rF.parameter.add(FACH_NICHT_VORHANDEN);
		rF.parameter.add((long) KURSART_GK);
		rF.parameter.add(3L);
		manager.regelAdd(rF);
		assertEquals(1, manager.regelGetMapUngueltig().size());

		// Ungültige Kursart
		final GostBlockungRegel rK = createRegel(3, GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE.typ);
		rK.parameter.add(1L);
		rK.parameter.add((long) KURSART_UNGUELTIG);
		rK.parameter.add(3L);
		manager.regelAdd(rK);
		assertEquals(2, manager.regelGetMapUngueltig().size());

		// Anzahl zu klein (MIN - 1)
		final long fachKursartMinUnterschritten = GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE_MIN - 1L;
		final GostBlockungRegel rMin = createRegel(4, GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE.typ);
		rMin.parameter.add(FACH_D_ID);
		rMin.parameter.add((long) KURSART_GK);
		rMin.parameter.add(fachKursartMinUnterschritten);
		manager.regelAdd(rMin);
		assertEquals(3, manager.regelGetMapUngueltig().size());

		// Anzahl zu groß (MAX + 1)
		final long fachKursartMaxUeberschritten = GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE_MAX + 1L;
		final GostBlockungRegel rMax = createRegel(5, GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE.typ);
		rMax.parameter.add(FACH_D_ID);
		rMax.parameter.add((long) KURSART_GK);
		rMax.parameter.add(fachKursartMaxUeberschritten);
		manager.regelAdd(rMax);
		assertEquals(4, manager.regelGetMapUngueltig().size());

		// Duplikat: gleiches Fach + gleiche Kursart + gleiche Anzahl (Multikey-Kollision)
		final GostBlockungRegel rDup = createRegel(6, GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE.typ);
		rDup.parameter.add(FACH_D_ID);
		rDup.parameter.add((long) KURSART_GK);
		rDup.parameter.add(3L);
		manager.regelAdd(rDup);
		assertEquals(5, manager.regelGetMapUngueltig().size());
	}

	@Test
	@DisplayName("testRegelGetMapUngueltig")
	void testRegelGetMapUngueltig() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTests();

		// Ohne ungültige Regeln: leere Map
		assertEquals(0, manager.regelGetMapUngueltig().size());

		// Regel mit nicht existierendem Kurs hinzufügen --> wird ungültig
		final GostBlockungRegel regelFremderKurs = createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_NICHT_VORHANDEN, SCHIENE_NR_1);
		manager.regelAdd(regelFremderKurs);
		assertEquals(1, manager.regelGetMapUngueltig().size());
		assertEquals(0, manager.regelGetAnzahl());
	}

	@Test
	@DisplayName("testRegelGetMapUngueltigBeschreibung")
	void testRegelGetMapUngueltigBeschreibung() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTests();

		// Ohne ungültige Regeln: beide Maps leer
		assertEquals(0, manager.regelGetMapUngueltigBeschreibung().size());
		assertEquals(0, manager.regelGetMapUngueltig().size());

		// Eine ungültige Regel: Beschreibung vorhanden, Maps synchron
		manager.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_NICHT_VORHANDEN, SCHIENE_NR_1));
		assertEquals(1, manager.regelGetMapUngueltigBeschreibung().size());
		assertEquals(1, manager.regelGetMapUngueltig().size());
		assertEquals(manager.regelGetMapUngueltig().keySet(), manager.regelGetMapUngueltigBeschreibung().keySet());

		// Zwei ungültige Regeln: Maps synchron
		manager.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_2, KURS_NICHT_VORHANDEN, SCHIENE_NR_1));
		assertEquals(2, manager.regelGetMapUngueltigBeschreibung().size());
		assertEquals(2, manager.regelGetMapUngueltig().size());
		assertEquals(manager.regelGetMapUngueltig().keySet(), manager.regelGetMapUngueltigBeschreibung().keySet());

		// Drei ungültige Regeln: Maps synchron
		manager.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_3, KURS_NICHT_VORHANDEN, SCHIENE_NR_1));
		assertEquals(3, manager.regelGetMapUngueltigBeschreibung().size());
		assertEquals(3, manager.regelGetMapUngueltig().size());
		assertEquals(manager.regelGetMapUngueltig().keySet(), manager.regelGetMapUngueltigBeschreibung().keySet());
	}


	// #########################################################################
	// ##########              Schueler-Tests                        ##########
	// #########################################################################

	@Test
	@DisplayName("testSchuelerAdd")
	void testSchuelerAdd() {
		final GostBlockungsdatenManager manager = createStandardManager();

		// Gültigen Schüler hinzufügen
		final Schueler s = new Schueler();
		s.id = SCHUELER_2_ID;
		s.nachname = "Neu";
		s.vorname = "Anna";
		s.geschlecht = Geschlecht.W.id;
		s.status = SchuelerStatus.AKTIV.ordinal();
		assertDoesNotThrow(() -> manager.schuelerAdd(s));
		assertEquals(2, manager.schuelerGetAnzahl());

		// Schüler mit doppelter ID hinzufügen
		final Schueler sDoppelt = new Schueler();
		sDoppelt.id = SCHUELER_2_ID;
		sDoppelt.nachname = "Doppelt";
		sDoppelt.vorname = "Eva";
		sDoppelt.geschlecht = Geschlecht.W.id;
		sDoppelt.status = SchuelerStatus.AKTIV.ordinal();
		assertThrows(DeveloperNotificationException.class, () -> manager.schuelerAdd(sDoppelt));

		// Schüler mit ungültiger ID hinzufügen
		final Schueler sUngueltig = new Schueler();
		sUngueltig.id = -1;
		sUngueltig.nachname = "Ungueltig";
		sUngueltig.vorname = "Max";
		sUngueltig.geschlecht = Geschlecht.M.id;
		sUngueltig.status = SchuelerStatus.AKTIV.ordinal();
		assertThrows(DeveloperNotificationException.class, () -> manager.schuelerAdd(sUngueltig));

		// Schüler mit ungültigem Geschlecht hinzufügen
		final Schueler sGeschlecht = new Schueler();
		sGeschlecht.id = 300;
		sGeschlecht.nachname = "Unbekannt";
		sGeschlecht.vorname = "Kim";
		sGeschlecht.geschlecht = 99;
		sGeschlecht.status = SchuelerStatus.AKTIV.ordinal();
		assertThrows(DeveloperNotificationException.class, () -> manager.schuelerAdd(sGeschlecht));
	}

	@Test
	@DisplayName("testSchuelerAddListe")
	void testSchuelerAddListe() {
		final GostBlockungsdatenManager manager = createStandardManager();

		// Leere Liste hinzufügen
		final List<Schueler> emptyList = new ArrayList<>();
		assertDoesNotThrow(() -> manager.schuelerAddListe(emptyList));
		assertEquals(1, manager.schuelerGetAnzahl());

		// Gültigen Schüler hinzufügen
		final Schueler s1 = new Schueler();
		s1.id = SCHUELER_2_ID;
		s1.nachname = "Neu";
		s1.vorname = "Anna";
		s1.geschlecht = Geschlecht.W.id;
		s1.status = SchuelerStatus.AKTIV.ordinal();
		final List<Schueler> list = new ArrayList<>();
		list.add(s1);
		assertDoesNotThrow(() -> manager.schuelerAddListe(list));
		assertEquals(2, manager.schuelerGetAnzahl());

		// Liste mit Duplikat hinzufügen: Atomarität - auch der gültige Schüler wird nicht hinzugefügt
		final Schueler sNeu = createSchueler(SCHUELER_NICHT_VORHANDEN, "Neu", "Lisa");
		final Schueler sDup = createSchueler(SCHUELER_2_ID, "Doppelt", "Eva");
		final List<Schueler> listDup = new ArrayList<>();
		listDup.add(sNeu);
		listDup.add(sDup);
		assertThrows(DeveloperNotificationException.class, () -> manager.schuelerAddListe(listDup));
		assertEquals(2, manager.schuelerGetAnzahl());
		assertNotNull(manager.schuelerGetOrNull(SCHUELER_2_ID));
		assertNull(manager.schuelerGetOrNull(SCHUELER_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testSchuelerGet")
	void testSchuelerGet() {
		final GostBlockungsdatenManager manager = createStandardManager();

		// Existierenden Schüler abrufen
		final Schueler s = manager.schuelerGet(SCHUELER_1_ID);
		assertNotNull(s);
		assertEquals("Test", s.nachname);

		// Nicht existierenden Schüler abrufen
		assertThrows(DeveloperNotificationException.class, () -> manager.schuelerGet(SCHUELER_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testSchuelerGetOrNull")
	void testSchuelerGetOrNull() {
		final GostBlockungsdatenManager manager = createStandardManager();

		// Existierenden Schüler abrufen
		assertNotNull(manager.schuelerGetOrNull(SCHUELER_1_ID));

		// Nicht existierenden Schüler abrufen
		assertNull(manager.schuelerGetOrNull(SCHUELER_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testSchuelerGetListe")
	void testSchuelerGetListe() {
		final GostBlockungsdatenManager manager = createStandardManager();

		// Liste mit einem Schüler
		assertEquals(1, manager.schuelerGetListe().size());

		// Liste nach weiterem Hinzufügen
		final Schueler s = new Schueler();
		s.id = SCHUELER_2_ID;
		s.nachname = "Neu";
		s.vorname = "Anna";
		s.geschlecht = Geschlecht.W.id;
		s.status = SchuelerStatus.AKTIV.ordinal();
		manager.schuelerAdd(s);
		assertEquals(2, manager.schuelerGetListe().size());
	}

	@Test
	@DisplayName("testSchuelerGetAnzahl")
	void testSchuelerGetAnzahl() {
		final GostBlockungsdatenManager manager = createStandardManager();

		// Ein Schüler (aus createStandardManager)
		assertEquals(1, manager.schuelerGetAnzahl());

		// Zweiter Schüler
		final Schueler s = new Schueler();
		s.id = SCHUELER_2_ID;
		s.nachname = "Neu";
		s.vorname = "Anna";
		s.geschlecht = Geschlecht.W.id;
		s.status = SchuelerStatus.AKTIV.ordinal();
		manager.schuelerAdd(s);
		assertEquals(2, manager.schuelerGetAnzahl());
	}

	@Test
	@DisplayName("testSchuelerGetAnzahlMitMindestensEinerFachwahl")
	void testSchuelerGetAnzahlMitMindestensEinerFachwahl() {
		// Ohne Fachwahlen: 0
		final GostBlockungsdatenManager manager = createStandardManager();
		assertEquals(0, manager.schuelerGetAnzahlMitMindestensEinerFachwahl());

		// Zweiten Schüler ohne Fachwahlen hinzufügen: weiterhin 0 mit Fachwahl
		manager.schuelerAdd(createSchueler(SCHUELER_2_ID, "Ohne", "Wahl"));
		assertEquals(2, manager.schuelerGetAnzahl());
		assertEquals(0, manager.schuelerGetAnzahlMitMindestensEinerFachwahl());

		// Fachwahlen für ersten Schüler: jetzt 1 von 2 Schülern hat Fachwahlen
		addDefaultFachwahlen(manager);
		assertEquals(2, manager.schuelerGetAnzahl());
		assertEquals(1, manager.schuelerGetAnzahlMitMindestensEinerFachwahl());
	}

	@Test
	@DisplayName("testSchuelerGetOfFachKursart")
	void testSchuelerGetOfFachKursart() {
		final GostBlockungsdatenManager manager = createManagerFuerSchuelerTests();

		// Fachwahl D-GK (Kursart 2)
		assertEquals(KURSART_GK, manager.schuelerGetOfFachKursart(SCHUELER_1_ID, 1).id);

		// Fachwahl M-LK (Kursart 1)
		assertEquals(KURSART_LK, manager.schuelerGetOfFachKursart(SCHUELER_1_ID, 2).id);

		// Fach nicht gewählt
		assertThrows(DeveloperNotificationException.class, () -> manager.schuelerGetOfFachKursart(SCHUELER_1_ID, FACH_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testSchuelerGetOfFachFachwahl")
	void testSchuelerGetOfFachFachwahl() {
		final GostBlockungsdatenManager manager = createManagerFuerSchuelerTests();

		// Existierende Fachwahl
		final GostFachwahl fw = manager.schuelerGetOfFachFachwahl(SCHUELER_1_ID, 1);
		assertEquals(SCHUELER_1_ID, fw.schuelerID);
		assertEquals(FACH_D_ID, fw.fachID);
		assertEquals(KURSART_GK, fw.kursartID);

		// Fach nicht gewählt
		assertThrows(DeveloperNotificationException.class, () -> manager.schuelerGetOfFachFachwahl(SCHUELER_1_ID, FACH_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testSchuelerGetOfFachFachwahlOrNull")
	void testSchuelerGetOfFachFachwahlOrNull() {
		final GostBlockungsdatenManager manager = createManagerFuerSchuelerTests();

		// Existierende Fachwahl
		assertNotNull(manager.schuelerGetOfFachFachwahlOrNull(SCHUELER_1_ID, 1));

		// Fach nicht gewählt --> null
		assertNull(manager.schuelerGetOfFachFachwahlOrNull(SCHUELER_1_ID, FACH_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testSchuelerGetHatFach")
	void testSchuelerGetHatFach() {
		final GostBlockungsdatenManager manager = createManagerFuerSchuelerTests();

		// Gewähltes Fach: true
		assertTrue(manager.schuelerGetHatFach(SCHUELER_1_ID, 1));
		assertTrue(manager.schuelerGetHatFach(SCHUELER_1_ID, 2));

		// Nicht gewähltes Fach: false
		assertFalse(manager.schuelerGetHatFach(SCHUELER_1_ID, FACH_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testSchuelerGetHatFachart")
	void testSchuelerGetHatFachart() {
		final GostBlockungsdatenManager manager = createManagerFuerSchuelerTests();

		// D-GK (Fach 1, Kursart 2): gewählt
		assertTrue(manager.schuelerGetHatFachart(SCHUELER_1_ID, FACH_D_ID, KURSART_GK));

		// Fach gewählt, aber andere Kursart
		assertFalse(manager.schuelerGetHatFachart(SCHUELER_1_ID, FACH_D_ID, KURSART_LK));

		// Fach nicht gewählt
		assertFalse(manager.schuelerGetHatFachart(SCHUELER_1_ID, FACH_NICHT_VORHANDEN, KURSART_GK));
	}

	@Test
	@DisplayName("testSchuelerGetHatDieSelbeKursartMitSchuelerInFach")
	void testSchuelerGetHatDieSelbeKursartMitSchuelerInFach() {
		final GostBlockungsdatenManager manager = createManagerFuerSchuelerTests();

		// Zweiten Schüler mit gleicher Fachwahl anlegen
		final Schueler s2 = new Schueler();
		s2.id = SCHUELER_2_ID;
		s2.nachname = "Test2";
		s2.vorname = "Anna";
		s2.geschlecht = Geschlecht.W.id;
		s2.status = SchuelerStatus.AKTIV.ordinal();
		manager.schuelerAdd(s2);
		manager.fachwahlAdd(createFachwahl(SCHUELER_2_ID, FACH_D_ID, KURSART_GK));

		// Gleiche Kursart in Fach 1
		assertTrue(manager.schuelerGetHatDieSelbeKursartMitSchuelerInFach(SCHUELER_1_ID, SCHUELER_2_ID, FACH_D_ID));

		// Unterschiedliche Kursart in Fach 2
		manager.fachwahlAdd(createFachwahl(SCHUELER_2_ID, FACH_M_ID, KURSART_GK));
		assertFalse(manager.schuelerGetHatDieSelbeKursartMitSchuelerInFach(SCHUELER_1_ID, SCHUELER_2_ID, FACH_M_ID));

		// Schüler hat Fach nicht gewählt
		assertThrows(DeveloperNotificationException.class,
				() -> manager.schuelerGetHatDieSelbeKursartMitSchuelerInFach(SCHUELER_1_ID, SCHUELER_2_ID, FACH_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testSchuelerGetListeOfFachwahlen")
	void testSchuelerGetListeOfFachwahlen() {
		final GostBlockungsdatenManager manager = createManagerFuerSchuelerTests();

		// Schüler mit Fachwahlen
		assertEquals(2, manager.schuelerGetListeOfFachwahlen(SCHUELER_1_ID).size());

		// Schüler ohne Fachwahlen: leere Liste
		final Schueler s = new Schueler();
		s.id = SCHUELER_2_ID;
		s.nachname = "Ohne";
		s.vorname = "Wahl";
		s.geschlecht = Geschlecht.W.id;
		s.status = SchuelerStatus.AKTIV.ordinal();
		manager.schuelerAdd(s);
		assertEquals(0, manager.schuelerGetListeOfFachwahlen(SCHUELER_2_ID).size());

		// Ungültige Schüler-ID: leere Liste
		assertEquals(0, manager.schuelerGetListeOfFachwahlen(SCHUELER_NICHT_VORHANDEN).size());
	}

	@Test
	@DisplayName("testSchuelerGetFachListeGemeinsamerFacharten")
	void testSchuelerGetFachListeGemeinsamerFacharten() {
		final GostBlockungsdatenManager manager = createManagerFuerSchuelerTests();

		// Zweiten Schüler mit teilweise gleichen Facharten anlegen
		final Schueler s2 = new Schueler();
		s2.id = SCHUELER_2_ID;
		s2.nachname = "Test2";
		s2.vorname = "Anna";
		s2.geschlecht = Geschlecht.W.id;
		s2.status = SchuelerStatus.AKTIV.ordinal();
		manager.schuelerAdd(s2);
		manager.fachwahlAdd(createFachwahl(SCHUELER_2_ID, FACH_D_ID, KURSART_GK)); // D-GK gleiche Kursart
		// Kein M-LK gewählt --> keine gemeinsame Kursart für M

		final List<GostFach> gemeinsam = manager.schuelerGetFachListeGemeinsamerFacharten(SCHUELER_1_ID, SCHUELER_2_ID);
		assertEquals(1, gemeinsam.size());
		assertEquals(FACH_D_ID, gemeinsam.get(0).id);
	}

	@Test
	@DisplayName("testSchuelerGetIstVerbotenInKurs")
	void testSchuelerGetIstVerbotenInKurs() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTests();
		addDefaultFachwahlen(manager);

		// Ohne Regel: nicht verboten
		assertFalse(manager.schuelerGetIstVerbotenInKurs(SCHUELER_1_ID, KURS_ID_1));

		// Mit Regel: verboten
		manager.regelAdd(createRegel05SchuelerVerbotenInKurs(REGEL_ID_1, SCHUELER_1_ID, KURS_ID_1));
		assertTrue(manager.schuelerGetIstVerbotenInKurs(SCHUELER_1_ID, KURS_ID_1));
	}

	@Test
	@DisplayName("testSchuelerGetRegelVerbotenInKurs")
	void testSchuelerGetRegelVerbotenInKurs() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTests();
		addDefaultFachwahlen(manager);

		// Ohne Regel: Exception
		assertThrows(DeveloperNotificationException.class, () -> manager.schuelerGetRegelVerbotenInKurs(SCHUELER_1_ID, KURS_ID_1));

		// Mit Regel: Regel wird gefunden
		manager.regelAdd(createRegel05SchuelerVerbotenInKurs(REGEL_ID_1, SCHUELER_1_ID, KURS_ID_1));
		final GostBlockungRegel regel = manager.schuelerGetRegelVerbotenInKurs(SCHUELER_1_ID, KURS_ID_1);
		assertEquals(REGEL_ID_1, regel.id);
	}

	@Test
	@DisplayName("testSchuelerGetIstFixiertInKurs")
	void testSchuelerGetIstFixiertInKurs() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTests();
		addDefaultFachwahlen(manager);

		// Ohne Regel: nicht fixiert
		assertFalse(manager.schuelerGetIstFixiertInKurs(SCHUELER_1_ID, KURS_ID_1));

		// Mit Regel: fixiert
		manager.regelAdd(createRegel04SchuelerFixiertInKurs(REGEL_ID_1, SCHUELER_1_ID, KURS_ID_1));
		assertTrue(manager.schuelerGetIstFixiertInKurs(SCHUELER_1_ID, KURS_ID_1));
	}

	@Test
	@DisplayName("testSchuelerGetRegelFixiertInKurs")
	void testSchuelerGetRegelFixiertInKurs() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTests();
		addDefaultFachwahlen(manager);

		// Ohne Regel: Exception
		assertThrows(DeveloperNotificationException.class, () -> manager.schuelerGetRegelFixiertInKurs(SCHUELER_1_ID, KURS_ID_1));

		// Mit Regel: Regel wird gefunden
		manager.regelAdd(createRegel04SchuelerFixiertInKurs(REGEL_ID_1, SCHUELER_1_ID, KURS_ID_1));
		final GostBlockungRegel regel = manager.schuelerGetRegelFixiertInKurs(SCHUELER_1_ID, KURS_ID_1);
		assertEquals(REGEL_ID_1, regel.id);
	}


	// #########################################################################
	// ##########              Ergebnis-Tests                        ##########
	// #########################################################################

	@Test
	@DisplayName("testErgebnisAdd")
	void testErgebnisAdd() {
		final GostBlockungsdatenManager manager = createStandardManager();

		// Gültiges Ergebnis hinzufügen
		final GostBlockungsergebnis ergebnis = new GostBlockungsergebnis();
		ergebnis.id = 1;
		ergebnis.blockungID = manager.getID();
		ergebnis.gostHalbjahr = HALBJAHR_EF1;
		assertDoesNotThrow(() -> manager.ergebnisAdd(ergebnis));
		assertEquals(1, manager.ergebnisGetAnzahl());
		assertTrue(manager.ergebnisManagerExists(ERGEBNIS_ID_1));

		// Ergebnis mit doppelter ID hinzufügen
		final GostBlockungsergebnis ergebnisDoppelt = new GostBlockungsergebnis();
		ergebnisDoppelt.id = 1;
		ergebnisDoppelt.blockungID = manager.getID();
		ergebnisDoppelt.gostHalbjahr = HALBJAHR_EF1;
		assertThrows(DeveloperNotificationException.class, () -> manager.ergebnisAdd(ergebnisDoppelt));

		// Ergebnis mit ungültiger ID hinzufügen
		final GostBlockungsergebnis ergebnisUngueltig = new GostBlockungsergebnis();
		ergebnisUngueltig.id = -1;
		ergebnisUngueltig.blockungID = manager.getID();
		ergebnisUngueltig.gostHalbjahr = HALBJAHR_EF1;
		assertThrows(DeveloperNotificationException.class, () -> manager.ergebnisAdd(ergebnisUngueltig));

		// Ergebnis mit ungültiger blockungID hinzufügen
		final GostBlockungsergebnis ergebnisBlockung = new GostBlockungsergebnis();
		ergebnisBlockung.id = 2;
		ergebnisBlockung.blockungID = -1;
		ergebnisBlockung.gostHalbjahr = HALBJAHR_EF1;
		assertThrows(DeveloperNotificationException.class, () -> manager.ergebnisAdd(ergebnisBlockung));

		// Ergebnis mit ungültigem Halbjahr hinzufügen
		final GostBlockungsergebnis ergebnisHalbjahr = new GostBlockungsergebnis();
		ergebnisHalbjahr.id = 3;
		ergebnisHalbjahr.blockungID = manager.getID();
		ergebnisHalbjahr.gostHalbjahr = HALBJAHR_UNGUELTIG;
		assertThrows(DeveloperNotificationException.class, () -> manager.ergebnisAdd(ergebnisHalbjahr));
	}

	@Test
	@DisplayName("testErgebnisAddListe")
	void testErgebnisAddListe() {
		final GostBlockungsdatenManager manager = createStandardManager();

		// Leere Liste hinzufügen
		final List<GostBlockungsergebnis> emptyList = new ArrayList<>();
		assertDoesNotThrow(() -> manager.ergebnisAddListe(emptyList));
		assertEquals(0, manager.ergebnisGetAnzahl());

		// Zwei gültige Ergebnisse hinzufügen
		final GostBlockungsergebnis e1 = new GostBlockungsergebnis();
		e1.id = ERGEBNIS_ID_1;
		e1.blockungID = manager.getID();
		e1.gostHalbjahr = HALBJAHR_EF1;
		final GostBlockungsergebnis e2 = new GostBlockungsergebnis();
		e2.id = ERGEBNIS_ID_2;
		e2.blockungID = manager.getID();
		e2.gostHalbjahr = HALBJAHR_EF1;
		final List<GostBlockungsergebnis> list = new ArrayList<>();
		list.add(e1);
		list.add(e2);
		assertDoesNotThrow(() -> manager.ergebnisAddListe(list));
		assertEquals(2, manager.ergebnisGetAnzahl());

		// Atomarität: gültiges + doppeltes Ergebnis --> Exception, keines wird hinzugefügt
		final GostBlockungsdatenManager manAtomar = createStandardManager();
		final GostBlockungsergebnis eNeu = new GostBlockungsergebnis();
		eNeu.id = ERGEBNIS_ID_1;
		eNeu.blockungID = manAtomar.getID();
		eNeu.gostHalbjahr = HALBJAHR_EF1;
		final GostBlockungsergebnis eDup = new GostBlockungsergebnis();
		eDup.id = ERGEBNIS_ID_1;
		eDup.blockungID = manAtomar.getID();
		eDup.gostHalbjahr = HALBJAHR_EF1;
		final List<GostBlockungsergebnis> listAtomar = new ArrayList<>();
		listAtomar.add(eNeu);
		listAtomar.add(eDup);
		assertThrows(DeveloperNotificationException.class, () -> manAtomar.ergebnisAddListe(listAtomar));
		assertEquals(0, manAtomar.ergebnisGetAnzahl());
	}

	@Test
	@DisplayName("testErgebnisGet")
	void testErgebnisGet() {
		final GostBlockungsdatenManager manager = createManagerMitErgebnis();

		// Existierendes Ergebnis abrufen
		final GostBlockungsergebnis e = manager.ergebnisGet(ERGEBNIS_ID_1);
		assertNotNull(e);
		assertEquals(ERGEBNIS_ID_1, e.id);

		// Nicht existierendes Ergebnis abrufen
		assertThrows(DeveloperNotificationException.class, () -> manager.ergebnisGet(ERGEBNIS_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testErgebnisManagerGet")
	void testErgebnisManagerGet() {
		final GostBlockungsdatenManager manager = createManagerMitErgebnis();

		// Existierenden Manager abrufen
		final GostBlockungsergebnisManager em = manager.ergebnisManagerGet(ERGEBNIS_ID_1);
		assertNotNull(em);

		// Nicht existierenden Manager abrufen
		assertThrows(DeveloperNotificationException.class, () -> manager.ergebnisManagerGet(ERGEBNIS_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testErgebnisManagerExists")
	void testErgebnisManagerExists() {
		final GostBlockungsdatenManager manager = createManagerMitErgebnis();

		// Existierender Manager
		assertTrue(manager.ergebnisManagerExists(ERGEBNIS_ID_1));

		// Nicht existierender Manager
		assertFalse(manager.ergebnisManagerExists(ERGEBNIS_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testErgebnisManagerGetListeUnsortiert")
	void testErgebnisManagerGetListeUnsortiert() {
		// Ohne Ergebnisse: leer
		final GostBlockungsdatenManager manager = createStandardManager();
		assertEquals(0, manager.ergebnisManagerGetListeUnsortiert().size());

		// Mit einem Ergebnis
		final GostBlockungsdatenManager managerMit = createManagerMitErgebnis();
		assertEquals(1, managerMit.ergebnisManagerGetListeUnsortiert().size());
	}

	@Test
	@DisplayName("testErgebnisGetListeSortiertNachBewertung")
	void testErgebnisGetListeSortiertNachBewertung() {
		final GostBlockungsdatenManager manager = createStandardManager();
		assertEquals(0, manager.ergebnisGetListeSortiertNachBewertung().size());

		// 4 Ergebnisse hinzufügen
		final GostBlockungsergebnis e1 = new GostBlockungsergebnis();
		e1.id = ERGEBNIS_ID_1;
		e1.blockungID = manager.getID();
		e1.gostHalbjahr = HALBJAHR_EF1;
		manager.ergebnisAdd(e1);

		final GostBlockungsergebnis e2 = new GostBlockungsergebnis();
		e2.id = ERGEBNIS_ID_2;
		e2.blockungID = manager.getID();
		e2.gostHalbjahr = HALBJAHR_EF1;
		manager.ergebnisAdd(e2);

		final GostBlockungsergebnis e3 = new GostBlockungsergebnis();
		e3.id = ERGEBNIS_ID_3;
		e3.blockungID = manager.getID();
		e3.gostHalbjahr = HALBJAHR_EF1;
		manager.ergebnisAdd(e3);

		final GostBlockungsergebnis e4 = new GostBlockungsergebnis();
		e4.id = ERGEBNIS_ID_4;
		e4.blockungID = manager.getID();
		e4.gostHalbjahr = HALBJAHR_EF1;
		manager.ergebnisAdd(e4);

		// Bewertungen NACHTRÄGLICH unterschiedlich setzen (werden beim Add überschrieben)
		e1.bewertung.anzahlKurseNichtZugeordnet = 3; // schlechteste (Kriterium 1)
		e2.bewertung.anzahlSchuelerNichtZugeordnet = 2; // mittel (Kriterium 2)
		e3.bewertung.kursdifferenzMax = 1; // besser (Kriterium 3)
		// e4 bleibt bei 0 --> beste

		// Neu sortieren nach geänderten Bewertungen
		manager.ergebnisUpdateBewertung(e1);
		manager.ergebnisUpdateBewertung(e2);
		manager.ergebnisUpdateBewertung(e3);
		manager.ergebnisUpdateBewertung(e4);

		// Iterativ prüfen: jeder Vorgänger ist besser als der Nachfolger
		final List<GostBlockungsergebnis> liste = manager.ergebnisGetListeSortiertNachBewertung();
		assertEquals(4, liste.size());
		for (int i = 1; i < liste.size(); i++) {
			final int cmp = GostBlockungsergebnisBewertungComparator.compareBewertungen(
					liste.get(i - 1).bewertung, liste.get(i).bewertung);
			assertTrue(cmp < 0, "Bewertung an Position " + (i - 1) + " muss besser sein als an Position " + i);
		}
	}

	@Test
	@DisplayName("testErgebnisGetListeSortiertNachID")
	void testErgebnisGetListeSortiertNachID() {
		final GostBlockungsdatenManager manager = createStandardManager();
		assertEquals(0, manager.ergebnisGetListeSortiertNachID().size());

		// 4 Ergebnisse mit unterschiedlichen IDs, unsortiert hinzufügen
		final GostBlockungsergebnis e4 = new GostBlockungsergebnis();
		e4.id = ERGEBNIS_ID_4;
		e4.blockungID = manager.getID();
		e4.gostHalbjahr = HALBJAHR_EF1;
		manager.ergebnisAdd(e4);

		final GostBlockungsergebnis e1 = new GostBlockungsergebnis();
		e1.id = ERGEBNIS_ID_1;
		e1.blockungID = manager.getID();
		e1.gostHalbjahr = HALBJAHR_EF1;
		manager.ergebnisAdd(e1);

		final GostBlockungsergebnis e3 = new GostBlockungsergebnis();
		e3.id = ERGEBNIS_ID_3;
		e3.blockungID = manager.getID();
		e3.gostHalbjahr = HALBJAHR_EF1;
		manager.ergebnisAdd(e3);

		final GostBlockungsergebnis e2 = new GostBlockungsergebnis();
		e2.id = ERGEBNIS_ID_2;
		e2.blockungID = manager.getID();
		e2.gostHalbjahr = HALBJAHR_EF1;
		manager.ergebnisAdd(e2);

		// Iterativ prüfen: IDs aufsteigend sortiert
		final List<GostBlockungsergebnis> liste = manager.ergebnisGetListeSortiertNachID();
		assertEquals(4, liste.size());
		for (int i = 1; i < liste.size(); i++) {
			assertTrue(liste.get(i - 1).id < liste.get(i).id,
					"ID an Position " + (i - 1) + " muss kleiner sein als an Position " + i);
		}
	}

	@Test
	@DisplayName("testErgebnisRemoveByID")
	void testErgebnisRemoveByID() {
		final GostBlockungsdatenManager manager = createManagerMitErgebnis();

		// Gültiges Löschen
		assertEquals(1, manager.ergebnisGetAnzahl());
		assertDoesNotThrow(() -> manager.ergebnisRemoveByID(ERGEBNIS_ID_1));
		assertEquals(0, manager.ergebnisGetAnzahl());
		assertFalse(manager.ergebnisManagerExists(ERGEBNIS_ID_1));

		// Nicht existierendes Ergebnis löschen
		assertThrows(DeveloperNotificationException.class, () -> manager.ergebnisRemoveByID(ERGEBNIS_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testErgebnisRemove")
	void testErgebnisRemove() {
		final GostBlockungsdatenManager manager = createManagerMitErgebnis();

		// Gültiges Löschen (über anderes Objekt mit gleicher ID)
		final GostBlockungsergebnis eAndereRef = new GostBlockungsergebnis();
		eAndereRef.id = 1;
		assertDoesNotThrow(() -> manager.ergebnisRemove(eAndereRef));
		assertEquals(0, manager.ergebnisGetAnzahl());
	}

	@Test
	@DisplayName("testErgebnisRemoveListe")
	void testErgebnisRemoveListe() {
		final GostBlockungsdatenManager manager = createStandardManager();
		final GostBlockungsergebnis e1 = new GostBlockungsergebnis();
		e1.id = 1;
		e1.blockungID = manager.getID();
		e1.gostHalbjahr = HALBJAHR_EF1;
		final GostBlockungsergebnis e2 = new GostBlockungsergebnis();
		e2.id = ERGEBNIS_ID_2;
		e2.blockungID = manager.getID();
		e2.gostHalbjahr = HALBJAHR_EF1;
		manager.ergebnisAdd(e1);
		manager.ergebnisAdd(e2);

		// Leere Liste entfernen
		final List<GostBlockungsergebnis> emptyList = new ArrayList<>();
		assertDoesNotThrow(() -> manager.ergebnisRemoveListe(emptyList));
		assertEquals(2, manager.ergebnisGetAnzahl());

		// Zwei Ergebnisse entfernen
		final GostBlockungsergebnis r1 = new GostBlockungsergebnis();
		r1.id = 1;
		final GostBlockungsergebnis r2 = new GostBlockungsergebnis();
		r2.id = 2;
		final List<GostBlockungsergebnis> list = new ArrayList<>();
		list.add(r1);
		list.add(r2);
		assertDoesNotThrow(() -> manager.ergebnisRemoveListe(list));
		assertEquals(0, manager.ergebnisGetAnzahl());
	}

	@Test
	@DisplayName("testErgebnisRemoveListeByIDs")
	void testErgebnisRemoveListeByIDs() {
		final GostBlockungsdatenManager manager = createManagerMitErgebnis();

		// Leeres Set entfernen
		final Set<Long> emptySet = new HashSet<>();
		assertDoesNotThrow(() -> manager.ergebnisRemoveListeByIDs(emptySet));
		assertEquals(1, manager.ergebnisGetAnzahl());

		// Gültiges Set entfernen
		final Set<Long> set = new HashSet<>();
		set.add(ERGEBNIS_ID_1);
		assertDoesNotThrow(() -> manager.ergebnisRemoveListeByIDs(set));
		assertEquals(0, manager.ergebnisGetAnzahl());

		// Nicht existierende ID
		final Set<Long> setFremd = Set.of(ERGEBNIS_NICHT_VORHANDEN);
		assertThrows(DeveloperNotificationException.class, () -> manager.ergebnisRemoveListeByIDs(setFremd));

		// Atomarität: gültig + nicht vorhanden + gültig --> Exception, keines wird gelöscht
		final GostBlockungsdatenManager manAtomar = createStandardManager();
		final GostBlockungsergebnis e1 = new GostBlockungsergebnis();
		e1.id = ERGEBNIS_ID_1;
		e1.blockungID = manAtomar.getID();
		e1.gostHalbjahr = HALBJAHR_EF1;
		manAtomar.ergebnisAdd(e1);
		final GostBlockungsergebnis e2 = new GostBlockungsergebnis();
		e2.id = ERGEBNIS_ID_2;
		e2.blockungID = manAtomar.getID();
		e2.gostHalbjahr = HALBJAHR_EF1;
		manAtomar.ergebnisAdd(e2);
		assertEquals(2, manAtomar.ergebnisGetAnzahl());
		final Set<Long> setAtomar = new HashSet<>();
		setAtomar.add(ERGEBNIS_ID_1);
		setAtomar.add(ERGEBNIS_NICHT_VORHANDEN);
		setAtomar.add(ERGEBNIS_ID_2);
		assertThrows(DeveloperNotificationException.class, () -> manAtomar.ergebnisRemoveListeByIDs(setAtomar));
		assertEquals(2, manAtomar.ergebnisGetAnzahl());
		assertTrue(manAtomar.ergebnisManagerExists(ERGEBNIS_ID_1));
		assertTrue(manAtomar.ergebnisManagerExists(ERGEBNIS_ID_2));
	}

	@Test
	@DisplayName("testErgebnisGetAnzahl")
	void testErgebnisGetAnzahl() {
		final GostBlockungsdatenManager manager = createStandardManager();

		// Ohne Ergebnisse: 0
		assertEquals(0, manager.ergebnisGetAnzahl());

		// Ein Ergebnis
		final GostBlockungsdatenManager manager1 = createManagerMitErgebnis();
		assertEquals(1, manager1.ergebnisGetAnzahl());
	}

	@Test
	@DisplayName("testErgebnisGetBewertung1Wert")
	void testErgebnisGetBewertung1Wert() {
		final GostBlockungsdatenManager manager = createManagerMitErgebnis();

		// Frisches Ergebnis: Bewertung1 = 0
		assertEquals(0, manager.ergebnisGetBewertung1Wert(ERGEBNIS_ID_1));

		// Nicht existierendes Ergebnis
		assertThrows(DeveloperNotificationException.class, () -> manager.ergebnisGetBewertung1Wert(ERGEBNIS_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testErgebnisGetBewertung1Intervall")
	void testErgebnisGetBewertung1Intervall() {
		// Wert 0: optimal
		final GostBlockungsdatenManager manager = createManagerMitErgebnis();
		assertEquals(0.0, manager.ergebnisGetBewertung1Intervall(ERGEBNIS_ID_1), 0.001);

		// Wert > 0: nicht zugeordnete Kurse --> Intervall steigt
		manager.ergebnisGet(ERGEBNIS_ID_1).bewertung.anzahlKurseNichtZugeordnet = 3;
		assertTrue(manager.ergebnisGetBewertung1Intervall(ERGEBNIS_ID_1) > 0.0);
	}

	@Test
	@DisplayName("testErgebnisGetBewertung2Wert")
	void testErgebnisGetBewertung2Wert() {
		final GostBlockungsdatenManager manager = createManagerMitErgebnis();
		assertEquals(0, manager.ergebnisGetBewertung2Wert(ERGEBNIS_ID_1));
		assertThrows(DeveloperNotificationException.class, () -> manager.ergebnisGetBewertung2Wert(ERGEBNIS_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testErgebnisGetBewertung2Intervall")
	void testErgebnisGetBewertung2Intervall() {
		// Wert 0: optimal
		final GostBlockungsdatenManager manager = createManagerMitErgebnis();
		assertEquals(0.0, manager.ergebnisGetBewertung2Intervall(ERGEBNIS_ID_1), 0.001);

		// Wert > 0: Schüler nicht zugeordnet --> Intervall steigt
		manager.ergebnisGet(ERGEBNIS_ID_1).bewertung.anzahlSchuelerNichtZugeordnet = 2;
		assertTrue(manager.ergebnisGetBewertung2Intervall(ERGEBNIS_ID_1) > 0.0);
	}

	@Test
	@DisplayName("testErgebnisGetBewertung3Wert")
	void testErgebnisGetBewertung3Wert() {
		final GostBlockungsdatenManager manager = createManagerMitErgebnis();
		assertEquals(0, manager.ergebnisGetBewertung3Wert(ERGEBNIS_ID_1));
	}

	@Test
	@DisplayName("testErgebnisGetBewertung3Intervall")
	void testErgebnisGetBewertung3Intervall() {
		// Wert 0: optimal (sehr gut)
		final GostBlockungsdatenManager manager = createManagerMitErgebnis();
		assertEquals(0.0, manager.ergebnisGetBewertung3Intervall(ERGEBNIS_ID_1), 0.001);

		// Wert > 0: geht durch die Reduktionslogik (wert--)
		manager.ergebnisGet(ERGEBNIS_ID_1).bewertung.kursdifferenzMax = 2;
		assertTrue(manager.ergebnisGetBewertung3Intervall(ERGEBNIS_ID_1) > 0.0);
	}

	@Test
	@DisplayName("testErgebnisGetBewertung4Wert")
	void testErgebnisGetBewertung4Wert() {
		final GostBlockungsdatenManager manager = createManagerMitErgebnis();
		assertEquals(0, manager.ergebnisGetBewertung4Wert(ERGEBNIS_ID_1));
	}

	@Test
	@DisplayName("testErgebnisGetBewertung4Intervall")
	void testErgebnisGetBewertung4Intervall() {
		// Wert 0: optimal
		final GostBlockungsdatenManager manager = createManagerMitErgebnis();
		assertEquals(0.0, manager.ergebnisGetBewertung4Intervall(ERGEBNIS_ID_1), 0.001);

		// Wert > 0: gleiche Fachart in Schiene --> Intervall steigt
		manager.ergebnisGet(ERGEBNIS_ID_1).bewertung.anzahlKurseMitGleicherFachartProSchiene = 1;
		assertTrue(manager.ergebnisGetBewertung4Intervall(ERGEBNIS_ID_1) > 0.0);
	}

	@Test
	@DisplayName("testErgebnisAlleRevalidieren")
	void testErgebnisAlleRevalidieren() {
		// Ohne Ergebnisse: kein Fehler
		final GostBlockungsdatenManager manager = createStandardManager();
		assertDoesNotThrow(manager::ergebnisAlleRevalidieren);

		// Mit Ergebnis: revalidieren läuft durch
		final GostBlockungsdatenManager managerMit = createManagerMitErgebnis();
		assertDoesNotThrow(managerMit::ergebnisAlleRevalidieren);
	}

	@Test
	@DisplayName("testErgebnisUpdateBewertung")
	void testErgebnisUpdateBewertung() {
		// Gültige Bewertung aktualisieren
		final GostBlockungsdatenManager manager = createManagerMitErgebnis();
		final GostBlockungsergebnis e = manager.ergebnisGet(ERGEBNIS_ID_1);
		assertDoesNotThrow(() -> manager.ergebnisUpdateBewertung(e));

		// Ungültige ID
		final GostBlockungsergebnis eUngueltig = new GostBlockungsergebnis();
		eUngueltig.id = -1;
		eUngueltig.blockungID = manager.getID();
		assertThrows(DeveloperNotificationException.class, () -> manager.ergebnisUpdateBewertung(eUngueltig));

		// Ungültige blockungID
		final GostBlockungsergebnis eBlockung = new GostBlockungsergebnis();
		eBlockung.id = 1;
		eBlockung.blockungID = -1;
		assertThrows(DeveloperNotificationException.class, () -> manager.ergebnisUpdateBewertung(eBlockung));
	}


	// #########################################################################
	// ##########                Sonstiges                            ##########
	// #########################################################################

	@Test
	@DisplayName("testGetID")
	void testGetID() {
		final GostBlockungsdatenManager manager = createStandardManager();
		assertEquals(1, manager.getID());
	}

	@Test
	@DisplayName("testSetID")
	void testSetID() {
		final GostBlockungsdatenManager manager = createStandardManager();

		// Gültige ID setzen
		manager.setID(42);
		assertEquals(42, manager.getID());

		// Ungültige ID setzen
		assertThrows(DeveloperNotificationException.class, () -> manager.setID(-1));
	}

	@Test
	@DisplayName("testGetName")
	void testGetName() {
		final GostBlockungsdatenManager manager = createStandardManager();
		assertEquals("Neue Blockung", manager.getName());
	}

	@Test
	@DisplayName("testSetName")
	void testSetName() {
		final GostBlockungsdatenManager manager = createStandardManager();

		// Gültigen Namen setzen
		manager.setName("Meine Blockung");
		assertEquals("Meine Blockung", manager.getName());

		// Leeren Namen setzen
		assertThrows(UserNotificationException.class, () -> manager.setName(""));
	}

	@Test
	@DisplayName("testGetHalbjahr")
	void testGetHalbjahr() {
		final GostBlockungsdatenManager manager = createStandardManager();
		assertEquals(GostHalbjahr.EF1, manager.getHalbjahr());
	}

	@Test
	@DisplayName("testSetHalbjahr")
	void testSetHalbjahr() {
		final GostBlockungsdatenManager manager = createStandardManager();

		// Halbjahr ändern
		manager.setHalbjahr(GostHalbjahr.Q11);
		assertEquals(GostHalbjahr.Q11, manager.getHalbjahr());
	}

	@Test
	@DisplayName("testGetMaxTimeMillis")
	void testGetMaxTimeMillis() {
		final GostBlockungsdatenManager manager = createStandardManager();
		assertEquals(1000, manager.getMaxTimeMillis());
	}

	@Test
	@DisplayName("testSetMaxTimeMillis")
	void testSetMaxTimeMillis() {
		final GostBlockungsdatenManager manager = createStandardManager();

		// Gültigen Wert setzen
		manager.setMaxTimeMillis(5000);
		assertEquals(5000, manager.getMaxTimeMillis());

		// Wert 0 setzen
		assertThrows(DeveloperNotificationException.class, () -> manager.setMaxTimeMillis(0));

		// Negativen Wert setzen
		assertThrows(DeveloperNotificationException.class, () -> manager.setMaxTimeMillis(-1));
	}

	@Test
	@DisplayName("testGetFaecherAnzahl")
	void testGetFaecherAnzahl() {
		final GostBlockungsdatenManager manager = createStandardManager();
		assertEquals(3, manager.getFaecherAnzahl());
	}

	@Test
	@DisplayName("testGetIstBlockungsVorlage")
	void testGetIstBlockungsVorlage() {
		// Ohne Ergebnis: false
		final GostBlockungsdatenManager manager = createStandardManager();
		assertFalse(manager.getIstBlockungsVorlage());

		// Mit einem Ergebnis: true
		final GostBlockungsdatenManager managerVorlage = createManagerMitErgebnis();
		assertTrue(managerVorlage.getIstBlockungsVorlage());

		// Mit mehreren Ergebnissen: false
		final GostBlockungsergebnis e2 = new GostBlockungsergebnis();
		e2.id = ERGEBNIS_ID_2;
		e2.blockungID = managerVorlage.getID();
		e2.gostHalbjahr = HALBJAHR_EF1;
		managerVorlage.ergebnisAdd(e2);
		assertFalse(managerVorlage.getIstBlockungsVorlage());
	}

	@Test
	@DisplayName("testToStringKursartSimple")
	void testToStringKursartSimple() {
		final GostBlockungsdatenManager manager = createStandardManager();

		// Bekannte Kursart
		assertEquals("GK", manager.toStringKursartSimple(KURSART_GK));
		assertEquals("LK", manager.toStringKursartSimple(KURSART_LK));

		// Unbekannte Kursart
		assertTrue(manager.toStringKursartSimple(KURSART_UNGUELTIG).contains("ohne Mapping"));
	}

	@Test
	@DisplayName("testToStringKurs")
	void testToStringKurs() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(1);
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));

		// Existierender Kurs
		assertTrue(manager.toStringKurs(KURS_ID_1).contains("D-GK1"));

		// Nicht existierender Kurs
		assertTrue(manager.toStringKurs(KURS_NICHT_VORHANDEN).contains("nicht im Mapping"));

		// Kurs mit Suffix
		final GostBlockungKurs kursMitSuffix = createKurs(KURS_ID_2, FACH_M_ID, KURSART_LK, KURS_NR_1);
		kursMitSuffix.suffix = "PJK";
		manager.kursAdd(kursMitSuffix);
		assertTrue(manager.toStringKurs(KURS_ID_2).contains("-PJK"));
	}

	@Test
	@DisplayName("testToStringKursSimple")
	void testToStringKursSimple() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(1);
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));

		// Existierender Kurs
		assertTrue(manager.toStringKursSimple(KURS_ID_1).contains("D-GK1"));

		// Nicht existierender Kurs
		assertTrue(manager.toStringKursSimple(KURS_NICHT_VORHANDEN).contains("nicht vorhanden"));

		// Kurs mit Suffix
		final GostBlockungKurs kursMitSuffix = createKurs(KURS_ID_2, FACH_M_ID, KURSART_LK, KURS_NR_1);
		kursMitSuffix.suffix = "PJK";
		manager.kursAdd(kursMitSuffix);
		assertTrue(manager.toStringKursSimple(KURS_ID_2).contains("-PJK"));
	}

	@Test
	@DisplayName("testToStringKursSimpleOhneID")
	void testToStringKursSimpleOhneID() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(1);
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));

		// Existierender Kurs
		assertEquals("D-GK1", manager.toStringKursSimpleOhneID(KURS_ID_1));

		// Nicht existierender Kurs
		assertTrue(manager.toStringKursSimpleOhneID(KURS_NICHT_VORHANDEN).contains("nicht zugeordnet"));

		// Kurs mit Suffix --> OhneID ignoriert Suffix nicht, prüfe Format
		final GostBlockungKurs kursMitSuffix = createKurs(KURS_ID_2, FACH_M_ID, KURSART_LK, KURS_NR_1);
		kursMitSuffix.suffix = "X";
		manager.kursAdd(kursMitSuffix);
		assertTrue(manager.toStringKursSimpleOhneID(KURS_ID_2).contains("-X"));
	}

	@Test
	@DisplayName("testToStringFachSimple")
	void testToStringFachSimple() {
		final GostBlockungsdatenManager manager = createStandardManager();

		// Existierendes Fach
		assertEquals("D", manager.toStringFachSimple(FACH_D_ID));

		// Nicht existierendes Fach
		assertTrue(manager.toStringFachSimple(FACH_NICHT_VORHANDEN).contains("ohne Mapping"));
	}

	@Test
	@DisplayName("testToStringFachartSimple")
	void testToStringFachartSimple() {
		final GostBlockungsdatenManager manager = createStandardManager();
		assertEquals("D-GK", manager.toStringFachartSimple(FACH_D_ID, KURSART_GK));
	}

	@Test
	@DisplayName("testToStringFachartSimpleByFachartID")
	void testToStringFachartSimpleByFachartID() {
		final GostBlockungsdatenManager manager = createStandardManager();
		final long idFachart = GostKursart.getFachartID(FACH_D_ID, KURSART_GK);
		assertEquals("D-GK", manager.toStringFachartSimpleByFachartID(idFachart));
	}

	@Test
	@DisplayName("testToStringSchueler")
	void testToStringSchueler() {
		final GostBlockungsdatenManager manager = createStandardManager();

		// Existierender Schüler
		assertTrue(manager.toStringSchueler(SCHUELER_1_ID).contains("Test, Max"));

		// Nicht existierender Schüler
		assertTrue(manager.toStringSchueler(SCHUELER_NICHT_VORHANDEN).contains("ohne Mapping"));
	}

	@Test
	@DisplayName("testToStringSchuelerSimple")
	void testToStringSchuelerSimple() {
		final GostBlockungsdatenManager manager = createStandardManager();
		assertEquals("Test, Max", manager.toStringSchuelerSimple(SCHUELER_1_ID));
		assertTrue(manager.toStringSchuelerSimple(SCHUELER_NICHT_VORHANDEN).contains("ohne Mapping"));
	}

	@Test
	@DisplayName("testToStringSchiene")
	void testToStringSchiene() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(1);

		// Existierende Schiene
		assertTrue(manager.toStringSchiene(SCHIENE_ID_1).contains("Schiene 1"));

		// Nicht existierende Schiene
		assertTrue(manager.toStringSchiene(SCHIENE_NICHT_VORHANDEN).contains("ohne Mapping"));
	}

	@Test
	@DisplayName("testToStringSchieneSimple")
	void testToStringSchieneSimple() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(1);
		assertTrue(manager.toStringSchieneSimple(SCHIENE_ID_1).contains("Schiene Nr. 1"));
		assertTrue(manager.toStringSchieneSimple(SCHIENE_NICHT_VORHANDEN).contains("ohne Mapping"));
	}

	@Test
	@DisplayName("testToStringKursLehrkraft")
	void testToStringKursLehrkraft() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(1);
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		manager.kursAddLehrkraft(KURS_ID_1, createKursLehrer(LEHRER_ID_1, LEHRER_REIHENFOLGE_1));

		// Existierende Lehrkraft
		assertTrue(manager.toStringKursLehrkraft(KURS_ID_1, LEHRER_ID_1).contains("L10"));

		// Nicht existierender Kurs
		assertTrue(manager.toStringKursLehrkraft(KURS_NICHT_VORHANDEN, LEHRER_ID_1).contains("Lehrkraft (ID=10)"));

		// Nicht existierende Lehrkraft im Kurs
		assertTrue(manager.toStringKursLehrkraft(KURS_ID_1, LEHRER_NICHT_VORHANDEN).contains("Lehrkraft (ID=999)"));
	}

	@Test
	@DisplayName("testToStringFachwahlSimple")
	void testToStringFachwahlSimple() {
		final GostBlockungsdatenManager manager = createStandardManager();

		// Gültige Fachwahl
		final GostFachwahl fw = createFachwahl(SCHUELER_1_ID, FACH_D_ID, KURSART_GK);
		assertTrue(manager.toStringFachwahlSimple(fw).contains("D-GK"));

		// Fachwahl mit unbekanntem Fach
		final GostFachwahl fwUnbekannt = createFachwahl(SCHUELER_1_ID, FACH_NICHT_VORHANDEN, KURSART_GK);
		assertTrue(manager.toStringFachwahlSimple(fwUnbekannt).contains("ohne Mapping"));
	}

	@Test
	@DisplayName("testToStringRegel")
	void testToStringRegel() {
		final GostBlockungsdatenManager manager = createManagerFuerRegelTests();
		manager.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1));

		// Existierende Regel
		assertTrue(manager.toStringRegel(REGEL_ID_1).contains("Regel (1"));

		// Nicht existierende Regel
		assertTrue(manager.toStringRegel(REGEL_NICHT_VORHANDEN).contains("ohne Mapping"));
	}

	@Test
	@DisplayName("testKursmengeGetSetDerIDs")
	void testKursmengeGetSetDerIDs() {
		final GostBlockungsdatenManager manager = createManagerMitSchienen(1);

		// Ohne Kurse: leeres Set
		assertEquals(0, manager.kursmengeGetSetDerIDs().size());

		// Mit Kursen
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		manager.kursAdd(createKurs(KURS_ID_2, FACH_M_ID, KURSART_LK, KURS_NR_1));
		assertEquals(2, manager.kursmengeGetSetDerIDs().size());
		assertTrue(manager.kursmengeGetSetDerIDs().contains(KURS_ID_1));
		assertTrue(manager.kursmengeGetSetDerIDs().contains(KURS_ID_2));
	}

	@Test
	@DisplayName("testFaecherManager")
	void testFaecherManager() {
		final GostBlockungsdatenManager manager = createStandardManager();
		assertNotNull(manager.faecherManager());
	}

	@Test
	@DisplayName("testDaten")
	void testDaten() {
		final GostBlockungsdatenManager manager = createStandardManager();
		assertNotNull(manager.daten());
		assertEquals(1, manager.daten().id);
	}

	@Test
	@DisplayName("testGetDebugString")
	void testGetDebugString() {
		// Einfacher Manager
		final GostBlockungsdatenManager manager = createStandardManager();
		assertNotNull(manager.getDebugString());
		assertFalse(manager.getDebugString().isEmpty());

		// Manager mit Schienen, Kursen, Fachwahlen, Regeln und Ergebnis
		final GostBlockungsdatenManager managerKomplex = createManagerMitBlockungsvorlage();
		managerKomplex.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		addDefaultFachwahlen(managerKomplex);
		managerKomplex.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1));
		final String debug = managerKomplex.getDebugString();
		assertNotNull(debug);
		assertTrue(debug.contains("Schienen = 1"));
		assertTrue(debug.contains("Kurse = 1"));
		assertTrue(debug.contains("Regeln = 1"));
	}


	// #########################################################################
	// ##########          Operationsfolgen (Integration)              ##########
	// #########################################################################

	@Test
	@DisplayName("testSchienenLoeschungVerschiebtRegelparameter")
	void testSchienenLoeschungVerschiebtRegelparameter() {
		// 5 Schienen anlegen (Blockungsvorlage hat bereits Schiene 1)
		final GostBlockungsdatenManager manager = createManagerMitBlockungsvorlage();
		manager.schieneAdd(createSchiene(SCHIENE_ID_2, 2, "Schiene 2"));
		manager.schieneAdd(createSchiene(SCHIENE_ID_3, 3, "Schiene 3"));
		manager.schieneAdd(createSchiene(SCHIENE_ID_4, 4, "Schiene 4"));
		manager.schieneAdd(createSchiene(SCHIENE_ID_5, 5, "Schiene 5"));
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));

		// Regel A: KURS_SPERRE_IN_SCHIENE auf Schiene-Nr 2
		manager.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_2));
		// Regel B: KURS_SPERRE_IN_SCHIENE auf Schiene-Nr 4
		manager.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_2, KURS_ID_1, SCHIENE_NR_4));
		// Regel C: KURS_FIXIERE_IN_SCHIENE auf Schiene-Nr 1
		manager.regelAdd(createRegel02KursFixierungInSchiene(REGEL_ID_3, KURS_ID_1, SCHIENE_NR_1));

		// Mittlere Schiene 3 löschen: Nr 4-->3, Nr 5-->4
		manager.schieneRemoveByID(SCHIENE_ID_3);
		final GostBlockungRegel regelB = manager.regelGet(REGEL_ID_2);
		assertEquals(SCHIENE_NR_3, (long) regelB.parameter.get(1),
				"Regel B (Nr 4) muss nach Schienen-Löschung auf Nr 3 zeigen");

		// Erste Schiene 1 löschen: Nr 2-->1, Nr 3-->2 (war 4)
		// Regel A (Nr 2) --> Nr 1, Regel B (jetzt Nr 3) --> Nr 2, Regel C (Nr 1) --> gelöscht
		manager.schieneRemoveByID(SCHIENE_ID_1);
		final GostBlockungRegel regelA = manager.regelGet(REGEL_ID_1);
		assertEquals(SCHIENE_NR_1, (long) regelA.parameter.get(1));
		assertEquals(SCHIENE_NR_2, (long) regelB.parameter.get(1));
		assertFalse(manager.regelGetExistiert(REGEL_ID_3));

		// Letzte Schiene löschen (jetzt Nr 3, war Nr 5): keine Regel betroffen
		manager.schieneRemoveByID(SCHIENE_ID_5);
		assertEquals(SCHIENE_NR_1, (long) regelA.parameter.get(1));
		assertEquals(SCHIENE_NR_2, (long) regelB.parameter.get(1));
	}

	@Test
	@DisplayName("testKursLoeschungBereinigtRegeln")
	void testKursLoeschungBereinigtRegeln() {
		// 2 Schienen, 2 Kurse, Regeln auf beide Kurse verteilt
		final GostBlockungsdatenManager manager = createManagerMitBlockungsvorlage();
		manager.schieneAdd(createSchiene(SCHIENE_ID_2, 2, "Schiene 2"));
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		manager.kursAdd(createKurs(KURS_ID_2, FACH_M_ID, KURSART_LK, KURS_NR_1));

		// Regel A: KURS_SPERRE_IN_SCHIENE auf Kurs 1 / Schiene 1
		manager.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1));
		// Regel B: KURS_SPERRE_IN_SCHIENE auf Kurs 2 / Schiene 2
		manager.regelAdd(createRegel03KursSperreInSchiene(REGEL_ID_2, KURS_ID_2, SCHIENE_NR_2));
		// Regel C: KURS_VERBIETEN_MIT_KURS zwischen Kurs 1 und 2
		final GostBlockungRegel rC = createRegel(REGEL_ID_3, GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ);
		rC.parameter.add(KURS_ID_1);
		rC.parameter.add(KURS_ID_2);
		manager.regelAdd(rC);

		assertEquals(3, manager.regelGetAnzahl());

		// Kurs 1 löschen --> Regel A (direkt) und C (Referenz auf Kurs 1) müssen weg
		manager.kurseRemoveByID(Set.of(KURS_ID_1));
		assertEquals(1, manager.regelGetAnzahl());
		assertFalse(manager.regelGetExistiert(REGEL_ID_1));
		assertTrue(manager.regelGetExistiert(REGEL_ID_2));
		assertFalse(manager.regelGetExistiert(REGEL_ID_3));
		assertFalse(manager.kursGetExistiert(KURS_ID_1));
		assertTrue(manager.kursGetExistiert(KURS_ID_2));
	}

	@Test
	@DisplayName("testErgebnisLoeschungAendertBlockungsvorlage")
	void testErgebnisLoeschungAendertBlockungsvorlage() {
		// Basis: Blockungsvorlage mit 1 Schiene, 1 Kurs
		final GostBlockungsdatenManager manager = createManagerMitBlockungsvorlage();
		manager.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));

		// Zweites Ergebnis hinzufügen --> keine Blockungsvorlage mehr (size=2)
		final GostBlockungsergebnis e2 = new GostBlockungsergebnis();
		e2.id = ERGEBNIS_ID_2;
		e2.blockungID = manager.getID();
		e2.gostHalbjahr = HALBJAHR_EF1;
		manager.ergebnisAdd(e2);
		assertFalse(manager.getIstBlockungsVorlage());
		assertThrows(DeveloperNotificationException.class, () -> manager.kursRemoveByID(KURS_ID_1));

		// Ein Ergebnis löschen --> wieder Blockungsvorlage (size=1)
		manager.ergebnisRemoveByID(ERGEBNIS_ID_2);
		assertTrue(manager.getIstBlockungsVorlage());
		assertDoesNotThrow(() -> manager.kursRemoveByID(KURS_ID_1));
		assertEquals(0, manager.kursGetAnzahl());

		// Letztes Ergebnis löschen --> keine Blockungsvorlage (size=0)
		manager.ergebnisRemoveByID(ERGEBNIS_ID_1);
		assertFalse(manager.getIstBlockungsVorlage());
		manager.kursAdd(createKurs(KURS_ID_2, FACH_M_ID, KURSART_LK, KURS_NR_1));
		assertThrows(DeveloperNotificationException.class, () -> manager.kursRemoveByID(KURS_ID_2));
	}

}
