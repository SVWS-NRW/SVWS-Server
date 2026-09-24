package de.svws_nrw.core.kursblockung.test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import de.svws_nrw.core.data.gost.GostBlockungRegelUpdate;
import de.svws_nrw.core.data.gost.GostBlockungSchiene;
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisBewertung;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKurs;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchienenZuordnung;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchienenZuordnungUpdate;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchuelerZuordnung;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchuelerZuordnungUpdate;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisSchiene;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.gost.GostSchriftlichkeit;
import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.core.utils.DTOUtils;

/**
 * Testet den {@link GostBlockungsergebnisManager}.
 *
 * @author Benjamin A. Bartsch
 */
@DisplayName("Testet den {@link GostBlockungsergebnisManager}.")
@TestMethodOrder(MethodOrderer.MethodName.class)
class GostBlockungsergebnisManagerTest {

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
	private static final int KURSART_ZK = GostKursart.ZK.id;

	private static final long KURS_ID_1 = 1;
	private static final long KURS_ID_2 = 2;
	private static final long KURS_ID_3 = 3;
	private static final long KURS_ID_4 = 4;
	private static final long KURS_ID_5 = 5;
	private static final long KURS_ID_6 = 6;
	private static final long KURS_ID_7 = 7;
	private static final long KURS_ID_8 = 8;
	private static final long KURS_NICHT_VORHANDEN = 999;
	private static final int KURS_NR_1 = 1;
	private static final int KURS_NR_2 = 2;

	private static final long LEHRER_ID_1 = 10;

	private static final long SCHIENE_ID_1 = 1;
	private static final long SCHIENE_ID_2 = 2;
	private static final long SCHIENE_ID_3 = 3;
	private static final long SCHIENE_NICHT_VORHANDEN = 999;
	private static final int SCHIENE_NR_1 = 1;
	private static final int SCHIENE_NR_2 = 2;
	private static final int SCHIENE_NR_3 = 3;

	private static final long REGEL_ID_1 = 1;

	private static final long ERGEBNIS_ID_1 = 1;

	private static final int HALBJAHR_EF1 = GostHalbjahr.EF1.id;

	private static void assertRegelListe(final List<GostBlockungRegel> actual, final int typ, final Set<List<Long>> expectedParameter) {
		assertEquals(expectedParameter.size(), actual.size());
		final Set<List<Long>> actualParameter = new HashSet<>();
		for (final GostBlockungRegel regel : actual) {
			assertEquals(typ, regel.typ);
			actualParameter.add(regel.parameter);
		}
		assertEquals(expectedParameter, actualParameter);
	}

	private static void assertRegelListeMitTypUndParameter(final List<GostBlockungRegel> actual, final Set<List<Long>> expected) {
		assertEquals(expected.size(), actual.size());
		final Set<List<Long>> actualTypUndParameter = new HashSet<>();
		for (final GostBlockungRegel regel : actual) {
			final List<Long> typUndParameter = new ArrayList<>();
			typUndParameter.add((long) regel.typ);
			typUndParameter.addAll(regel.parameter);
			actualTypUndParameter.add(typUndParameter);
		}
		assertEquals(expected, actualTypUndParameter);
	}

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

	private static Schueler createSchueler(final long id, final String nachname, final String vorname) {
		final Schueler s = new Schueler();
		s.id = id;
		s.nachname = nachname;
		s.vorname = vorname;
		s.geschlecht = Geschlecht.W.id;
		s.status = SchuelerStatus.AKTIV.ordinal();
		return s;
	}

	private static GostBlockungSchiene createSchiene(final long id, final int nummer, final String bezeichnung) {
		final GostBlockungSchiene schiene = new GostBlockungSchiene();
		schiene.id = id;
		schiene.nummer = nummer;
		schiene.bezeichnung = bezeichnung;
		return schiene;
	}

	private static GostBlockungKurs createKurs(final long id, final long fachID, final int kursart, final int nummer) {
		final GostBlockungKurs kurs = new GostBlockungKurs();
		kurs.id = id;
		kurs.fach_id = fachID;
		kurs.kursart = kursart;
		kurs.nummer = nummer;
		return kurs;
	}

	private static GostFachwahl createFachwahl(final long schuelerID, final long fachID, final int kursartID) {
		final GostFachwahl fachwahl = new GostFachwahl();
		fachwahl.schuelerID = schuelerID;
		fachwahl.fachID = fachID;
		fachwahl.kursartID = kursartID;
		return fachwahl;
	}

	private static GostBlockungRegel createRegelDummySUS(final long kursID, final long anzahl) {
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = kursID + 100;
		regel.typ = GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ;
		regel.parameter.add(kursID);
		regel.parameter.add(anzahl);
		return regel;
	}

	private static GostBlockungRegel createRegelSchuelerPaar(final long id, final int typ, final long idS1, final long idS2,
			final long... fachID) {
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = id;
		regel.typ = typ;
		regel.parameter.add(idS1);
		regel.parameter.add(idS2);
		for (final long idFach : fachID) {
			regel.parameter.add(idFach);
		}
		return regel;
	}

	/**
	 * Erzeugt einen GostBlockungsdatenManager als Eltern-Objekt mit:
	 * <br>3 Fächern,
	 * <br>2 Schülern,
	 * <br>3 Schienen,
	 * <br>5 Kursen und
	 * <br>5 Fachwahlen.
	 *
	 * @return den erzeugten GostBlockungsdatenManager
	 */
	private static GostBlockungsdatenManager createParentManager() {
		final GostFaecherManager faecherManager = new GostFaecherManager(2024);
		faecherManager.add(createFach(FACH_D_ID, "D"));
		faecherManager.add(createFach(FACH_M_ID, "M"));
		faecherManager.add(createFach(FACH_E_ID, "E"));

		final GostBlockungsdaten blockungsdaten = new GostBlockungsdaten();
		blockungsdaten.id = 1;
		blockungsdaten.gostHalbjahr = HALBJAHR_EF1;

		blockungsdaten.schienen.add(createSchiene(SCHIENE_ID_1, SCHIENE_NR_1, "Schiene 1"));
		blockungsdaten.schienen.add(createSchiene(SCHIENE_ID_2, SCHIENE_NR_2, "Schiene 2"));
		blockungsdaten.schienen.add(createSchiene(SCHIENE_ID_3, SCHIENE_NR_3, "Schiene 3"));

		final GostBlockungsdatenManager mgr = new GostBlockungsdatenManager(blockungsdaten, faecherManager);

		mgr.schuelerAddListe(List.of(
				createSchueler(SCHUELER_1_ID, "Test1", "Max"),
				createSchueler(SCHUELER_2_ID, "Test2", "Anna")
		));

		mgr.fachwahlAdd(createFachwahl(SCHUELER_1_ID, FACH_D_ID, KURSART_GK));
		mgr.fachwahlAdd(createFachwahl(SCHUELER_1_ID, FACH_M_ID, KURSART_LK));
		mgr.fachwahlAdd(createFachwahl(SCHUELER_1_ID, FACH_E_ID, KURSART_GK));
		mgr.fachwahlAdd(createFachwahl(SCHUELER_2_ID, FACH_D_ID, KURSART_GK));
		mgr.fachwahlAdd(createFachwahl(SCHUELER_2_ID, FACH_M_ID, KURSART_LK));

		mgr.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		mgr.kursAdd(createKurs(KURS_ID_2, FACH_M_ID, KURSART_LK, KURS_NR_1));
		mgr.kursAdd(createKurs(KURS_ID_3, FACH_E_ID, KURSART_GK, KURS_NR_1));
		mgr.kursAdd(createKurs(KURS_ID_4, FACH_D_ID, KURSART_GK, KURS_NR_2));
		mgr.kursAdd(createKurs(KURS_ID_5, FACH_M_ID, KURSART_GK, KURS_NR_1));

		return mgr;
	}

	/**
	 * Erzeugt einen leeren GostBlockungsergebnisManager (ohne Kurs-Schienen- und Schüler-Kurs-Zuordnungen).
	 *
	 * @return den erzeugten leeren GostBlockungsergebnisManager
	 */
	private static GostBlockungsergebnisManager createLeerManager() {
		return new GostBlockungsergebnisManager(createParentManager(), ERGEBNIS_ID_1);
	}

	/**
	 * Erzeugt einen GostBlockungsergebnisManager mit zugeordneten Kursen in Schienen und
	 * Schülern in Kursen, um nicht-leere Zustände zu testen.
	 * <p>
	 * Schienenbelegung:
	 * <br> K1 (D-GK1) --> Schiene 1
	 * <br> K2 (M-LK1) --> Schiene 1, Schiene 2
	 * <br> K3 (E-GK1) --> Schiene 2
	 * <br> K4 (D-GK2) --> Schiene 3
	 * <br> K5 (M-GK1) --> Schiene 3
	 * <p>
	 * Schülerbelegung:
	 * <br> S1 --> K1 (D-GK1), K2 (M-LK1), K3 (E-GK1)
	 * <br> S2 --> K4 (D-GK2), K2 (M-LK1)
	 *
	 * @return den erzeugten GostBlockungsergebnisManager mit Testdaten
	 */
	private static GostBlockungsergebnisManager createStandardManager() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1);

		// Kurse in Schienen legen
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_2, SCHIENE_ID_1));
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_2, SCHIENE_ID_2));
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_3, SCHIENE_ID_2));
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_4, SCHIENE_ID_3));
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_5, SCHIENE_ID_3));

		final GostBlockungsergebnisKursSchienenZuordnungUpdate updateKS = mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z);
		mgr.kursSchienenUpdateExecute(updateKS);

		// Schüler in Kurse legen
		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> s = new HashSet<>();
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_1_ID));
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_2, SCHUELER_1_ID));
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_3, SCHUELER_1_ID));
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_4, SCHUELER_2_ID));
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_2, SCHUELER_2_ID));

		final GostBlockungsergebnisKursSchuelerZuordnungUpdate updateSK = mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(s);
		mgr.kursSchuelerUpdateExecute(updateSK);

		return mgr;
	}

	/**
	 * Erzeugt einen GostBlockungsergebnisManager mit einer Regel zum Auffüllen mit Dummy-Schülern.
	 *
	 * @return den erzeugten GostBlockungsergebnisManager
	 */
	private static GostBlockungsergebnisManager createManagerMitDummySus() {
		final GostBlockungsdatenManager parent = createParentManager();
		parent.regelAdd(createRegelDummySUS(KURS_ID_1, 5));
		return new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1);
	}

	/**
	 * Erzeugt einen GostBlockungsergebnisManager mit einer Kursdifferenz in der Fachart D-GK.
	 * <br>D-GK besitzt die Kurse K1 (D-GK1) und K4 (D-GK2). K1 erhält die übergebene Anzahl SuS, K4 bleibt leer.
	 * <br>Daraus ergibt sich eine Kursdifferenz entsprechend der übergebenen Anzahl.
	 *
	 * @param anzahlSuSInK1  die Anzahl SuS, die dem Kurs K1 (D-GK1) zugeordnet werden
	 *
	 * @return den erzeugten GostBlockungsergebnisManager
	 */
	private static GostBlockungsergebnisManager createManagerMitKursdifferenzGK(final int anzahlSuSInK1) {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1);

		// K1 (D-GK1) in Schiene 1, K4 (D-GK2) in Schiene 2 legen.
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_4, SCHIENE_ID_2));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));

		// K1 die übergebene Anzahl SuS zuordnen, K4 bleibt leer.
		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> s = new HashSet<>();
		if (anzahlSuSInK1 >= 1) {
			s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_1_ID));
		}
		if (anzahlSuSInK1 >= 2) {
			s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_2_ID));
		}
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(s));

		return mgr;
	}

	/**
	 * Erzeugt einen GostBlockungsergebnisManager mit einer Kursdifferenz von 2 in der Fachart M-LK.
	 * <br>M-LK besitzt die Kurse K2 (M-LK1) und K6 (M-LK2). K2 erhält zwei SuS, K6 bleibt leer.
	 * <br>Daraus ergibt sich eine Kursdifferenz von 2 (max - min = 2 - 0).
	 *
	 * @return den erzeugten GostBlockungsergebnisManager
	 */
	private static GostBlockungsergebnisManager createManagerMitKursdifferenzLK() {
		final GostBlockungsdatenManager parent = createParentManager();
		// Zweiten LK-Kurs der Fachart M-LK hinzufügen.
		parent.kursAdd(createKurs(KURS_ID_6, FACH_M_ID, KURSART_LK, KURS_NR_2));

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1);

		// K2 (M-LK1) in Schiene 1, K6 (M-LK2) in Schiene 2 legen.
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_2, SCHIENE_ID_1));
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_6, SCHIENE_ID_2));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));

		// K2 zwei SuS zuordnen, K6 bleibt leer.
		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> s = new HashSet<>();
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_2, SCHUELER_1_ID));
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_2, SCHUELER_2_ID));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(s));

		return mgr;
	}

	/**
	 * Erzeugt einen GostBlockungsergebnisManager mit einer Kursdifferenz von 2 in der Fachart D-ZK (Zusatzkurs, "Rest"-Kursart).
	 * <br>D-ZK besitzt die Kurse K7 (D-ZK1) und K8 (D-ZK2). K7 erhält zwei SuS, K8 bleibt leer.
	 * <br>Daraus ergibt sich eine Kursdifferenz von 2 (max - min = 2 - 0) in der Kategorie "Rest".
	 *
	 * @return den erzeugten GostBlockungsergebnisManager
	 */
	private static GostBlockungsergebnisManager createManagerMitKursdifferenzRest() {
		final GostBlockungsdatenManager parent = createParentManager();
		// Zwei ZK-Kurse der Fachart D-ZK hinzufügen.
		parent.kursAdd(createKurs(KURS_ID_7, FACH_D_ID, KURSART_ZK, KURS_NR_1));
		parent.kursAdd(createKurs(KURS_ID_8, FACH_D_ID, KURSART_ZK, KURS_NR_2));

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1);

		// K7 (D-ZK1) in Schiene 1, K8 (D-ZK2) in Schiene 2 legen.
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_7, SCHIENE_ID_1));
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_8, SCHIENE_ID_2));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));

		// K7 zwei SuS zuordnen, K8 bleibt leer.
		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> s = new HashSet<>();
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_7, SCHUELER_1_ID));
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_7, SCHUELER_2_ID));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(s));

		return mgr;
	}

	/**
	 * Erzeugt einen GostBlockungsergebnisManager mit zwei Kursen der Fachart D-GK in derselben Schiene.
	 * <br>K1 (D-GK1) und K4 (D-GK2) liegen beide in Schiene 1.
	 * <br>Daraus ergibt sich eine Fächerparallelität von 1 (zwei Kurse gleicher Fachart pro Schiene).
	 *
	 * @return den erzeugten GostBlockungsergebnisManager
	 */
	private static GostBlockungsergebnisManager createManagerMitFaecherparallelitaet() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1);

		// K1 (D-GK1) und K4 (D-GK2) beide in Schiene 1 legen.
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_4, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));

		return mgr;
	}

	/**
	 * Erzeugt einen GostBlockungsdatenManager als Eltern-Objekt mit:
	 * <br>3 Fächern,
	 * <br>2 Schülern,
	 * <br>1 Schiene und
	 * <br>keinen Kursen.
	 *
	 * @return den erzeugten GostBlockungsdatenManager
	 */
	private static GostBlockungsdatenManager createParentManagerOhneKurse() {
		final GostFaecherManager faecherManager = new GostFaecherManager(2024);
		faecherManager.add(createFach(FACH_D_ID, "D"));
		faecherManager.add(createFach(FACH_M_ID, "M"));
		faecherManager.add(createFach(FACH_E_ID, "E"));

		final GostBlockungsdaten blockungsdaten = new GostBlockungsdaten();
		blockungsdaten.id = 1;
		blockungsdaten.gostHalbjahr = HALBJAHR_EF1;

		blockungsdaten.schienen.add(createSchiene(SCHIENE_ID_1, SCHIENE_NR_1, "Schiene 1"));

		final GostBlockungsdatenManager mgr = new GostBlockungsdatenManager(blockungsdaten, faecherManager);

		mgr.schuelerAddListe(List.of(
				createSchueler(SCHUELER_1_ID, "Test1", "Max"),
				createSchueler(SCHUELER_2_ID, "Test2", "Anna")
		));

		mgr.fachwahlAdd(createFachwahl(SCHUELER_1_ID, FACH_D_ID, KURSART_GK));
		mgr.fachwahlAdd(createFachwahl(SCHUELER_1_ID, FACH_M_ID, KURSART_LK));

		return mgr;
	}

	/**
	 * Erzeugt einen GostBlockungsdatenManager als Eltern-Objekt mit zusätzlichem externen Schüler.
	 *
	 * @return den erzeugten GostBlockungsdatenManager
	 */
	private static GostBlockungsdatenManager createParentManagerMitExternemSchueler() {
		final GostBlockungsdatenManager parent = createParentManager();
		final Schueler externer = new Schueler();
		externer.id = 300;
		externer.nachname = "Extern";
		externer.vorname = "Test";
		externer.geschlecht = Geschlecht.M.id;
		externer.status = 6; // SchuelerStatus.EXTERN Katalog-ID
		parent.schuelerAdd(externer);
		parent.fachwahlAdd(createFachwahl(300L, FACH_D_ID, KURSART_GK));
		return parent;
	}

	/**
	 * Erzeugt einen GostBlockungsdatenManager als Eltern-Objekt mit bereits vorhandenem
	 * GostBlockungsergebnis (Blockungsvorlage) und 3 Schülern.
	 *
	 * @return den erzeugten GostBlockungsdatenManager
	 */
	private static GostBlockungsdatenManager createParentManagerMitBlockungsvorlage() {
		final GostFaecherManager faecherManager = new GostFaecherManager(2024);
		faecherManager.add(createFach(FACH_D_ID, "D"));
		faecherManager.add(createFach(FACH_M_ID, "M"));
		faecherManager.add(createFach(FACH_E_ID, "E"));

		final GostBlockungsdaten blockungsdaten = new GostBlockungsdaten();
		blockungsdaten.id = 1;
		blockungsdaten.gostHalbjahr = HALBJAHR_EF1;
		blockungsdaten.schienen.add(createSchiene(SCHIENE_ID_1, SCHIENE_NR_1, "Schiene 1"));
		blockungsdaten.schienen.add(createSchiene(SCHIENE_ID_2, SCHIENE_NR_2, "Schiene 2"));
		blockungsdaten.schienen.add(createSchiene(SCHIENE_ID_3, SCHIENE_NR_3, "Schiene 3"));

		final GostBlockungsergebnis ergebnis = new GostBlockungsergebnis();
		ergebnis.id = ERGEBNIS_ID_1;
		ergebnis.blockungID = blockungsdaten.id;
		ergebnis.gostHalbjahr = blockungsdaten.gostHalbjahr;
		blockungsdaten.ergebnisse.add(ergebnis);

		final GostBlockungsdatenManager mgr = new GostBlockungsdatenManager(blockungsdaten, faecherManager);
		final Schueler s3 = createSchueler(301L, "Test3", "Chris");
		s3.abschlussjahrgang = 99;
		mgr.schuelerAddListe(List.of(
				createSchueler(SCHUELER_1_ID, "Test1", "Max"),
				createSchueler(SCHUELER_2_ID, "Test2", "Anna"),
				s3
		));
		mgr.fachwahlAdd(createFachwahl(SCHUELER_1_ID, FACH_D_ID, KURSART_GK));
		mgr.fachwahlAdd(createFachwahl(SCHUELER_1_ID, FACH_M_ID, KURSART_LK));
		mgr.fachwahlAdd(createFachwahl(SCHUELER_1_ID, FACH_E_ID, KURSART_GK));
		mgr.fachwahlAdd(createFachwahl(SCHUELER_2_ID, FACH_D_ID, KURSART_GK));
		mgr.fachwahlAdd(createFachwahl(SCHUELER_2_ID, FACH_M_ID, KURSART_LK));
		mgr.fachwahlAdd(createFachwahl(301L, FACH_D_ID, KURSART_GK));
		mgr.kursAdd(createKurs(KURS_ID_1, FACH_D_ID, KURSART_GK, KURS_NR_1));
		mgr.kursAdd(createKurs(KURS_ID_2, FACH_M_ID, KURSART_LK, KURS_NR_1));
		return mgr;
	}

	// #########################################################################
	// ##########       Allgemeine Anfragen (General Getters)         ##########
	// #########################################################################

	@Test
	@DisplayName("testGetAnzahlSchuelerExterne")
	void testGetAnzahlSchuelerExterne() {
		// Alle SuS haben Status AKTIV, also 0 externe
		final GostBlockungsergebnisManager mgr = createLeerManager();
		assertEquals(0, mgr.getAnzahlSchuelerExterne());

		// Ein externer Schüler hinzugefügt
		final GostBlockungsdatenManager parentExt = createParentManagerMitExternemSchueler();
		final GostBlockungsergebnisManager mgrExt = new GostBlockungsergebnisManager(parentExt, ERGEBNIS_ID_1);
		assertEquals(1, mgrExt.getAnzahlSchuelerExterne());
	}

	@Test
	@DisplayName("testGetKursfrequenz1AsString")
	void testGetKursfrequenz1AsString() {
		// 5 Fachwahlen / 5 interne Kurse = 1,0
		final GostBlockungsergebnisManager mgr = createStandardManager();
		assertEquals("1,0", mgr.getKursfrequenz1AsString());

		// 0 interne Kurse --> "Kursfrequenz = ?"
		final GostBlockungsdatenManager parentOhne = createParentManagerOhneKurse();
		final GostBlockungsergebnisManager mgrOhne = new GostBlockungsergebnisManager(parentOhne, ERGEBNIS_ID_1);
		assertEquals("Kursfrequenz = ?", mgrOhne.getKursfrequenz1AsString());
	}

	@Test
	@DisplayName("testGetKursfrequenz2AsString")
	void testGetKursfrequenz2AsString() {
		// 5 verteilte Schüler / 5 interne Kurse = 1,0
		final GostBlockungsergebnisManager mgr = createStandardManager();
		assertEquals("1,0", mgr.getKursfrequenz2AsString());

		// Keine Schüler verteilt, obwohl 5 Fachwahlen vorhanden --> 0,0
		assertEquals("0,0", createLeerManager().getKursfrequenz2AsString());

		// 0 interne Kurse --> "Kursfrequenz = ?"
		final GostBlockungsdatenManager parentOhne = createParentManagerOhneKurse();
		final GostBlockungsergebnisManager mgrOhne = new GostBlockungsergebnisManager(parentOhne, ERGEBNIS_ID_1);
		assertEquals("Kursfrequenz = ?", mgrOhne.getKursfrequenz2AsString());
	}

	@Test
	@DisplayName("testGetAnzahlSchienen")
	void testGetAnzahlSchienen() {
		final GostBlockungsergebnisManager mgr = createLeerManager();

		// 3 Schienen im Parent definiert
		assertEquals(3, mgr.getAnzahlSchienen());
	}

	@Test
	@DisplayName("testGetAnzahlSchuelerDummy")
	void testGetAnzahlSchuelerDummy() {
		// Ohne Dummy-SuS
		final GostBlockungsergebnisManager mgrLeer = createLeerManager();
		assertEquals(0, mgrLeer.getAnzahlSchuelerDummy());

		// Mit Dummy-SuS
		final GostBlockungsergebnisManager mgrDummy = createManagerMitDummySus();
		assertEquals(5, mgrDummy.getAnzahlSchuelerDummy());
	}

	@Test
	@DisplayName("testGetBlockungsdatenID")
	void testGetBlockungsdatenID() {
		final GostBlockungsergebnisManager mgr = createLeerManager();

		// Parent hat ID 1
		assertEquals(1, mgr.getBlockungsdatenID());
	}

	@Test
	@DisplayName("testGetErgebnis")
	void testGetErgebnis() {
		final GostBlockungsergebnisManager mgr = createLeerManager();

		final GostBlockungsergebnis ergebnis = mgr.getErgebnis();
		assertNotNull(ergebnis);
		assertEquals(ERGEBNIS_ID_1, ergebnis.id);
		assertEquals(1, ergebnis.blockungID);
		assertEquals(HALBJAHR_EF1, ergebnis.gostHalbjahr);
		assertEquals("Blockung", ergebnis.name);
		assertFalse(ergebnis.istAktiv);
		assertEquals(Set.of(SCHIENE_ID_1, SCHIENE_ID_2, SCHIENE_ID_3), ergebnis.schienen.stream().map(s -> s.id)
				.collect(java.util.stream.Collectors.toSet()));
	}

	@Test
	@DisplayName("testGetFehlermeldungen")
	void testGetFehlermeldungen() {
		final GostBlockungsergebnisManager mgr = createLeerManager();

		final List<String> fehlermeldungen = mgr.getFehlermeldungen();
		assertNotNull(fehlermeldungen);
		assertEquals(0, fehlermeldungen.size());
	}

	@Test
	@DisplayName("testGetFehlermeldungenBeiDoppeltenSchienenIDsUndNummern")
	void testGetFehlermeldungenBeiDoppeltenSchienenIDsUndNummern() {
		final GostBlockungsdatenManager parent = createParentManager();
		parent.daten().schienen.add(createSchiene(SCHIENE_ID_1, 4, "Doppelte ID"));
		parent.daten().schienen.add(createSchiene(4, SCHIENE_NR_1, "Doppelte Nummer"));

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1);
		final List<String> fehlermeldungen = mgr.getFehlermeldungen();

		assertEquals(2, fehlermeldungen.size());
		assertTrue(fehlermeldungen.contains("Die Schienen-ID 1 ist doppelt!"));
		assertTrue(fehlermeldungen.contains("Die Schienen-NR 1 ist doppelt!"));
	}

	@Test
	@DisplayName("testGetFehlermeldungenBeiKursOhneFach")
	void testGetFehlermeldungenBeiKursOhneFach() {
		final GostBlockungsdatenManager parent = createParentManager();
		parent.daten().kurse.add(createKurs(20, FACH_NICHT_VORHANDEN, KURSART_GK, KURS_NR_1));

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1);
		final List<String> fehlermeldungen = mgr.getFehlermeldungen();

		assertEquals(1, fehlermeldungen.size());
		assertTrue(fehlermeldungen.get(0).contains("Kurs [Kurs (20) nicht vorhanden] hat ein undefiniertes Fach (im Fächer-Manager)!"));
	}

	@Test
	@DisplayName("testGetParent")
	void testGetParent() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1);

		assertEquals(parent, mgr.getParent());
	}

	// #########################################################################
	// ##########       Bewertung (Assessment) Getters                ##########
	// #########################################################################

	@Test
	@DisplayName("testGetOfBewertung1Wert")
	void testGetOfBewertung1Wert() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// K2 hat anzahlSchienen=1 aber ist in 2 Schienen --> Abweichung 1
		// Keine Regelverletzungen --> 0
		// Bewertung1 = 1 + 0 = 1
		assertEquals(1, mgr.getOfBewertung1Wert());
	}

	@Test
	@DisplayName("testGetOfBewertung1Farbcode")
	void testGetOfBewertung1Farbcode() {
		final GostBlockungsergebnisManager mgr = createLeerManager();

		final double farbcode = mgr.getOfBewertung1Farbcode();
		// Nach stateClear: anzahlKurseNichtZugeordnet summiert alle Abweichungen
		// K1..K5 haben anzahlSchienen=1, alle sind in 0 Schienen --> jeder hat Abweichung 1 --> sum=5
		// Regelverletzungen: 0 --> Gesamt=5 --> Farbcode = 1 - 1/(0.25*5+1) = 1 - 1/2,25 = 0,555...
		assertEquals(5.0 / 9.0, farbcode, 1e-9);
	}

	@Test
	@DisplayName("testGetOfBewertung1WertStatic")
	void testGetOfBewertung1WertStatic() {
		final GostBlockungsergebnisBewertung bewertung = new GostBlockungsergebnisBewertung();
		bewertung.anzahlKurseNichtZugeordnet = 3;
		bewertung.regelVerletzungen = List.of(1L);

		// 3 + 1 = 4
		assertEquals(4, GostBlockungsergebnisManager.getOfBewertung1WertStatic(bewertung));
	}

	@Test
	@DisplayName("testGetOfBewertung2Wert")
	void testGetOfBewertung2Wert() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// S1 hat 2 Kollisionen, keine Nichtwahlen --> 2
		assertEquals(2, mgr.getOfBewertung2Wert());
	}

	@Test
	@DisplayName("testGetOfBewertung2Farbcode")
	void testGetOfBewertung2Farbcode() {
		final GostBlockungsergebnisManager mgr = createLeerManager();

		final double farbcode = mgr.getOfBewertung2Farbcode();
		// 5 nicht zugeordnete Fachwahlen + 0 Schülerkollisionen = 5
		assertEquals(5.0 / 9.0, farbcode, 1e-9);
	}

	@Test
	@DisplayName("testGetOfBewertung3Wert")
	void testGetOfBewertung3Wert() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// Keine Kursdifferenz bei verteilten Schülern mit gleichen Zahlen --> 0
		assertEquals(0, mgr.getOfBewertung3Wert());
	}

	@Test
	@DisplayName("testGetOfBewertung3WertMitKursdifferenz")
	void testGetOfBewertung3WertMitKursdifferenz() {
		// D-GK: K1 hat 2 SuS, K4 hat 0 SuS --> Kursdifferenz 2
		final GostBlockungsergebnisManager mgr = createManagerMitKursdifferenzGK(2);

		assertEquals(2, mgr.getOfBewertung3Wert());
	}

	@Test
	@DisplayName("testGetOfBewertung3Farbcode")
	void testGetOfBewertung3Farbcode() {
		final GostBlockungsergebnisManager mgr = createLeerManager();

		final double farbcode = mgr.getOfBewertung3Farbcode();
		assertEquals(0.0, farbcode, 1e-9);
	}

	@Test
	@DisplayName("testGetOfBewertung3FarbcodeMitKursdifferenz")
	void testGetOfBewertung3FarbcodeMitKursdifferenz() {
		// Kursdifferenz 1 --> als optimal (0) behandelt
		final GostBlockungsergebnisManager mgr1 = createManagerMitKursdifferenzGK(1);
		assertEquals(0.0, mgr1.getOfBewertung3Farbcode(), 1e-9);

		// Kursdifferenz 2 --> Farbcode über (wert - 1) = 1 --> 1 - 1/(0.25*1 + 1) = 0,2
		final GostBlockungsergebnisManager mgr2 = createManagerMitKursdifferenzGK(2);
		assertEquals(0.2, mgr2.getOfBewertung3Farbcode(), 1e-9);
	}

	@Test
	@DisplayName("testGetOfBewertung3WertNurLk")
	void testGetOfBewertung3WertNurLk() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		final int wert = mgr.getOfBewertung3WertNurLk();
		assertEquals(0, wert);
	}

	@Test
	@DisplayName("testGetOfBewertung3WertNurLkMitKursdifferenz")
	void testGetOfBewertung3WertNurLkMitKursdifferenz() {
		// Eine GK-Kursdifferenz beeinflusst die LK-Bewertung nicht --> 0
		final GostBlockungsergebnisManager mgrGk = createManagerMitKursdifferenzGK(2);
		assertEquals(0, mgrGk.getOfBewertung3WertNurLk());

		// M-LK: K2 hat 2 SuS, K6 hat 0 SuS --> LK-Kursdifferenz 2
		final GostBlockungsergebnisManager mgrLk = createManagerMitKursdifferenzLK();
		assertEquals(2, mgrLk.getOfBewertung3WertNurLk());
	}

	@Test
	@DisplayName("testGetOfBewertung3FarbcodeNurLk")
	void testGetOfBewertung3FarbcodeNurLk() {
		final GostBlockungsergebnisManager mgr = createLeerManager();

		final double farbcode = mgr.getOfBewertung3FarbcodeNurLk();
		assertEquals(0.0, farbcode, 1e-9);
	}

	@Test
	@DisplayName("testGetOfBewertung3FarbcodeNurLkMitKursdifferenz")
	void testGetOfBewertung3FarbcodeNurLkMitKursdifferenz() {
		// LK-Kursdifferenz 2 --> Farbcode über (wert - 1) = 1 --> 0,2
		final GostBlockungsergebnisManager mgr = createManagerMitKursdifferenzLK();
		assertEquals(0.2, mgr.getOfBewertung3FarbcodeNurLk(), 1e-9);
	}

	@Test
	@DisplayName("testGetOfBewertung3WertNurGk")
	void testGetOfBewertung3WertNurGk() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		final int wert = mgr.getOfBewertung3WertNurGk();
		assertEquals(0, wert);
	}

	@Test
	@DisplayName("testGetOfBewertung3WertNurGkMitKursdifferenz")
	void testGetOfBewertung3WertNurGkMitKursdifferenz() {
		// D-GK: K1 hat 2 SuS, K4 hat 0 SuS --> GK-Kursdifferenz 2
		final GostBlockungsergebnisManager mgr = createManagerMitKursdifferenzGK(2);
		assertEquals(2, mgr.getOfBewertung3WertNurGk());
	}

	@Test
	@DisplayName("testGetOfBewertung3FarbcodeNurGk")
	void testGetOfBewertung3FarbcodeNurGk() {
		final GostBlockungsergebnisManager mgr = createLeerManager();

		final double farbcode = mgr.getOfBewertung3FarbcodeNurGk();
		assertEquals(0.0, farbcode, 1e-9);
	}

	@Test
	@DisplayName("testGetOfBewertung3FarbcodeNurGkMitKursdifferenz")
	void testGetOfBewertung3FarbcodeNurGkMitKursdifferenz() {
		// GK-Kursdifferenz 2 --> Farbcode über (wert - 1) = 1 --> 0,2
		final GostBlockungsergebnisManager mgr = createManagerMitKursdifferenzGK(2);
		assertEquals(0.2, mgr.getOfBewertung3FarbcodeNurGk(), 1e-9);
	}

	@Test
	@DisplayName("testGetOfBewertung3WertNurRest")
	void testGetOfBewertung3WertNurRest() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// Es gibt keine nicht-LK/GK Kurse --> 0
		assertEquals(0, mgr.getOfBewertung3WertNurRest());
	}

	@Test
	@DisplayName("testGetOfBewertung3WertNurRestMitKursdifferenz")
	void testGetOfBewertung3WertNurRestMitKursdifferenz() {
		// D-ZK: K7 hat 2 SuS, K8 hat 0 SuS --> Rest-Kursdifferenz 2
		final GostBlockungsergebnisManager mgr = createManagerMitKursdifferenzRest();
		assertEquals(2, mgr.getOfBewertung3WertNurRest());
	}

	@Test
	@DisplayName("testGetOfBewertung3FarbcodeNurRest")
	void testGetOfBewertung3FarbcodeNurRest() {
		final GostBlockungsergebnisManager mgr = createLeerManager();

		final double farbcode = mgr.getOfBewertung3FarbcodeNurRest();
		assertEquals(0.0, farbcode, 1e-9);
	}

	@Test
	@DisplayName("testGetOfBewertung3FarbcodeNurRestMitKursdifferenz")
	void testGetOfBewertung3FarbcodeNurRestMitKursdifferenz() {
		// Rest-Kursdifferenz 2 --> Farbcode über (wert - 1) = 1 --> 0,2
		final GostBlockungsergebnisManager mgr = createManagerMitKursdifferenzRest();
		assertEquals(0.2, mgr.getOfBewertung3FarbcodeNurRest(), 1e-9);
	}

	@Test
	@DisplayName("testGetOfBewertung4Wert")
	void testGetOfBewertung4Wert() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// Keine Fachart doppelt in einer Schiene --> 0
		assertEquals(0, mgr.getOfBewertung4Wert());
	}

	@Test
	@DisplayName("testGetOfBewertung4Farbcode")
	void testGetOfBewertung4Farbcode() {
		final GostBlockungsergebnisManager mgr = createLeerManager();

		final double farbcode = mgr.getOfBewertung4Farbcode();
		assertEquals(0.0, farbcode, 1e-9);
	}

	@Test
	@DisplayName("testGetOfBewertung4WertMitFaecherparallelitaet")
	void testGetOfBewertung4WertMitFaecherparallelitaet() {
		// D-GK1 und D-GK2 liegen beide in Schiene 1 --> Fächerparallelität 1
		final GostBlockungsergebnisManager mgr = createManagerMitFaecherparallelitaet();
		assertEquals(1, mgr.getOfBewertung4Wert());
	}

	@Test
	@DisplayName("testGetOfBewertung4FarbcodeMitFaecherparallelitaet")
	void testGetOfBewertung4FarbcodeMitFaecherparallelitaet() {
		// Wert 1 --> Farbcode = 1 - 1/(0.25*1 + 1) = 0,2
		final GostBlockungsergebnisManager mgr = createManagerMitFaecherparallelitaet();
		assertEquals(0.2, mgr.getOfBewertung4Farbcode(), 1e-9);
	}

	@Test
	@DisplayName("testGetOfBewertungAnzahlNichtZugeordneterKurse")
	void testGetOfBewertungAnzahlNichtZugeordneterKurse() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// K2 hat anzahlSchienen=1 aber 2 Schienen --> 1
		assertEquals(1, mgr.getOfBewertungAnzahlNichtZugeordneterKurse());
	}

	@Test
	@DisplayName("testGetOfBewertungAnzahlNichtzugeordneterFachwahlen")
	void testGetOfBewertungAnzahlNichtzugeordneterFachwahlen() {
		// Standard: alle Fachwahlen zugeordnet
		final GostBlockungsergebnisManager mgr = createStandardManager();
		assertEquals(0, mgr.getOfBewertungAnzahlNichtzugeordneterFachwahlen());

		// Leerer Manager: 5 Fachwahlen, keine zugeordnet
		final GostBlockungsergebnisManager mgrLeer = createLeerManager();
		assertEquals(5, mgrLeer.getOfBewertungAnzahlNichtzugeordneterFachwahlen());
	}

	// #########################################################################
	// ##########       Fach / Fachart Getters                         ##########
	// #########################################################################

	@Test
	@DisplayName("testGetFach")
	void testGetFach() {
		final GostBlockungsergebnisManager mgr = createLeerManager();

		// Existierendes Fach
		final GostFach fach = mgr.getFach(FACH_D_ID);
		assertNotNull(fach);
		assertEquals(FACH_D_ID, fach.id);
		assertEquals("D", fach.kuerzel);

		// Nicht existierendes Fach
		assertThrows(DeveloperNotificationException.class, () -> mgr.getFach(FACH_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testGetOfFachKursmenge")
	void testGetOfFachKursmenge() {
		final GostBlockungsergebnisManager mgr = createLeerManager();

		// Fach D hat 2 Kurse (D-GK1, D-GK2)
		assertEquals(2, mgr.getOfFachKursmenge(FACH_D_ID).size());

		// Fach M hat 2 Kurse (M-LK1, M-GK1)
		assertEquals(2, mgr.getOfFachKursmenge(FACH_M_ID).size());

		// Fach E hat 1 Kurs (E-GK1)
		assertEquals(1, mgr.getOfFachKursmenge(FACH_E_ID).size());

		// Nicht existierendes Fach
		assertThrows(DeveloperNotificationException.class, () -> mgr.getOfFachKursmenge(FACH_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testGetOfFachartKursmenge")
	void testGetOfFachartKursmenge() {
		final GostBlockungsergebnisManager mgr = createLeerManager();

		// Eine Fachart-ID mit Kursen
		final long fachartDGK = GostKursart.getFachartID(FACH_D_ID, KURSART_GK);
		assertEquals(2, mgr.getOfFachartKursmenge(fachartDGK).size());

		// Fachart ohne Kurse (aber mit Fachwahlen --> leer aber nicht Exception)
		// Fach D-LK hat keine Kurse, aber keine Fachwahl --> nicht in kursmengeByFachartID
		final long fachartDLK = GostKursart.getFachartID(FACH_D_ID, KURSART_LK);
		// Da es keine Fachwahlen und Kurse für D-LK gibt, ist die Map leer --> Exception
		assertThrows(DeveloperNotificationException.class, () -> mgr.getOfFachartKursmenge(fachartDLK));
	}

	@Test
	@DisplayName("testGetOfFachartKursdifferenz")
	void testGetOfFachartKursdifferenz() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		final long fachartDGK = GostKursart.getFachartID(FACH_D_ID, KURSART_GK);
		final int kd = mgr.getOfFachartKursdifferenz(fachartDGK);
		assertEquals(0, kd);
	}

	@Test
	@DisplayName("testGetOfFachartKursdifferenzMitKursdifferenz")
	void testGetOfFachartKursdifferenzMitKursdifferenz() {
		// D-GK: K1 hat 2 SuS, K4 hat 0 SuS --> Kursdifferenz 2
		final GostBlockungsergebnisManager mgr = createManagerMitKursdifferenzGK(2);

		final long fachartDGK = GostKursart.getFachartID(FACH_D_ID, KURSART_GK);
		assertEquals(2, mgr.getOfFachartKursdifferenz(fachartDGK));
	}

	@Test
	@DisplayName("testGetOfFachOfKursartKursdifferenz")
	void testGetOfFachOfKursartKursdifferenz() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// D-GK existiert und hat Kurse
		assertDoesNotThrow(() -> mgr.getOfFachOfKursartKursdifferenz(FACH_D_ID, KURSART_GK));

		// D-LK hat keine Kurse --> Exception
		assertThrows(DeveloperNotificationException.class, () -> mgr.getOfFachOfKursartKursdifferenz(FACH_D_ID, KURSART_LK));
	}

	@Test
	@DisplayName("testGetOfFachOfKursartKursdifferenzMitKursdifferenz")
	void testGetOfFachOfKursartKursdifferenzMitKursdifferenz() {
		// D-GK: K1 hat 2 SuS, K4 hat 0 SuS --> Kursdifferenz 2
		final GostBlockungsergebnisManager mgr = createManagerMitKursdifferenzGK(2);

		assertEquals(2, mgr.getOfFachOfKursartKursdifferenz(FACH_D_ID, KURSART_GK));
	}

	// #########################################################################
	// ##########       Schueler (Student) Getters                     ##########
	// #########################################################################

	@Test
	@DisplayName("testGetOfSchuelerNameVorname")
	void testGetOfSchuelerNameVorname() {
		final GostBlockungsergebnisManager mgr = createLeerManager();

		// Existierender Schüler
		assertEquals("Test1, Max", mgr.getOfSchuelerNameVorname(SCHUELER_1_ID));
		assertEquals("Test2, Anna", mgr.getOfSchuelerNameVorname(SCHUELER_2_ID));

		// Nicht Existierender Schüler
		assertThrows(DeveloperNotificationException.class, () -> mgr.getOfSchuelerNameVorname(SCHUELER_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testGetOfSchuelerKursmenge")
	void testGetOfSchuelerKursmenge() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// S1 ist in K1 (D-GK1), K2 (M-LK1), K3 (E-GK1) --> 3 Kurse
		assertEquals(3, mgr.getOfSchuelerKursmenge(SCHUELER_1_ID).size());

		// S2 ist in K4 (D-GK2), K2 (M-LK1) --> 2 Kurse
		assertEquals(2, mgr.getOfSchuelerKursmenge(SCHUELER_2_ID).size());

		// Nicht Existierender Schüler
		assertThrows(DeveloperNotificationException.class, () -> mgr.getOfSchuelerKursmenge(SCHUELER_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testGetOfSchuelerKursmengeMitKollisionen")
	void testGetOfSchuelerKursmengeMitKollisionen() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// S1 ist in K1 (Schiene 1), K2 (Schiene 1,2), K3 (Schiene 2)
		// In Schiene 1: K1+K2 --> Kollision!
		// In Schiene 2: K2+K3 --> Kollision!
		// Kollidierende Kurse: K1, K2, K3
		final Set<GostBlockungsergebnisKurs> kollisionen = mgr.getOfSchuelerKursmengeMitKollisionen(SCHUELER_1_ID);
		assertEquals(3, kollisionen.size());

		// S2 ist in K4 (Schiene 3), K2 (Schiene 1,2) --> keine Kollision
		assertEquals(0, mgr.getOfSchuelerKursmengeMitKollisionen(SCHUELER_2_ID).size());

		// Nicht Existierender Schüler --> Exception (Parent wirft)
	}

	@Test
	@DisplayName("testGetOfSchuelerFachwahlmengeOhneKurszuordnung")
	void testGetOfSchuelerFachwahlmengeOhneKurszuordnung() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// S1 hat Fachwahlen für D, M, E --> alle zugeordnet --> keine Nichtwahlen
		assertEquals(0, mgr.getOfSchuelerFachwahlmengeOhneKurszuordnung(SCHUELER_1_ID).size());

		// S2 hat Fachwahlen für D, M --> M ist zugeordnet (K2), aber D ist NUR
		// über K4 zugeordnet --> warte, S2 ist in K4 (D-GK2) --> also auch zugeordnet
		// S2-D-GK ist zugeordnet via K4 --> 0 Nichtwahlen
		assertEquals(0, mgr.getOfSchuelerFachwahlmengeOhneKurszuordnung(SCHUELER_2_ID).size());

		// Leerer Manager: alle Fachwahlen ohne Zuordnung
		final GostBlockungsergebnisManager mgrLeer = createLeerManager();
		assertEquals(3, mgrLeer.getOfSchuelerFachwahlmengeOhneKurszuordnung(SCHUELER_1_ID).size());
		assertEquals(2, mgrLeer.getOfSchuelerFachwahlmengeOhneKurszuordnung(SCHUELER_2_ID).size());
	}

	@Test
	@DisplayName("testGetOfSchuelerHatNichtwahl")
	void testGetOfSchuelerHatNichtwahl() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// S1 hat 3 Fachwahlen und 3 Kurszuordnungen --> keine Nichtwahl
		assertFalse(mgr.getOfSchuelerHatNichtwahl(SCHUELER_1_ID));

		// S2 hat 2 Fachwahlen und 2 Kurszuordnungen --> keine Nichtwahl
		assertFalse(mgr.getOfSchuelerHatNichtwahl(SCHUELER_2_ID));

		// Leerer Manager: S1 hat 3 Fachwahlen aber 0 Kurse --> Nichtwahl
		final GostBlockungsergebnisManager mgrLeer = createLeerManager();
		assertTrue(mgrLeer.getOfSchuelerHatNichtwahl(SCHUELER_1_ID));
	}

	@Test
	@DisplayName("testGetOfSchuelerHatFachwahl")
	void testGetOfSchuelerHatFachwahl() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// S1 hat D-GK
		assertTrue(mgr.getOfSchuelerHatFachwahl(SCHUELER_1_ID, FACH_D_ID, KURSART_GK));

		// S1 hat nicht D-LK
		assertFalse(mgr.getOfSchuelerHatFachwahl(SCHUELER_1_ID, FACH_D_ID, KURSART_LK));

		// S2 hat M-LK
		assertTrue(mgr.getOfSchuelerHatFachwahl(SCHUELER_2_ID, FACH_M_ID, KURSART_LK));
	}

	@Test
	@DisplayName("testGetOfSchuelerHatKollision")
	void testGetOfSchuelerHatKollision() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// S1 hat Kollision (K1+K2 in Schiene 1, K2+K3 in Schiene 2)
		assertTrue(mgr.getOfSchuelerHatKollision(SCHUELER_1_ID));

		// S2 hat keine Kollision (K4 in Schiene 3, K2 in Schiene 1+2 --> keine Überschneidung)
		assertFalse(mgr.getOfSchuelerHatKollision(SCHUELER_2_ID));
	}

	@Test
	@DisplayName("testGetOfSchuelerAnzahlGefiltert")
	void testGetOfSchuelerAnzahlGefiltert() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// Kein Filter: 2 Schüler (S1, S2)
		assertEquals(2, mgr.getOfSchuelerAnzahlGefiltert(-1, -1, -1, 0, "", null, null));

		// Filter: KonfliktTyp=1 (mit Kollisionen)
		assertEquals(1, mgr.getOfSchuelerAnzahlGefiltert(-1, -1, -1, 1, "", null, null));

		// Filter: KonfliktTyp=2 (mit Nichtwahlen) --> keiner hat Nichtwahlen im Standard-Manager
		assertEquals(0, mgr.getOfSchuelerAnzahlGefiltert(-1, -1, -1, 2, "", null, null));

		// Filter: KonfliktTyp=3 (Kollisionen und Nichtwahlen)
		// S1 hat Kollision --> passt
		assertEquals(1, mgr.getOfSchuelerAnzahlGefiltert(-1, -1, -1, 3, "", null, null));
	}

	@Test
	@DisplayName("testGetOfSchuelerOfSchieneHatKollision")
	void testGetOfSchuelerOfSchieneHatKollision() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// S1 in Schiene 1: K1+K2 --> Kollision
		assertTrue(mgr.getOfSchuelerOfSchieneHatKollision(SCHUELER_1_ID, SCHIENE_ID_1));

		// S1 in Schiene 2: K2+K3 --> Kollision
		assertTrue(mgr.getOfSchuelerOfSchieneHatKollision(SCHUELER_1_ID, SCHIENE_ID_2));

		// S2 in Schiene 1: nur K2 --> keine Kollision
		assertFalse(mgr.getOfSchuelerOfSchieneHatKollision(SCHUELER_2_ID, SCHIENE_ID_1));
	}

	@Test
	@DisplayName("testGetOfSchuelerOfFachKursart")
	void testGetOfSchuelerOfFachKursart() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// S1 hat D als GK
		assertEquals(GostKursart.GK, mgr.getOfSchuelerOfFachKursart(SCHUELER_1_ID, FACH_D_ID));

		// S1 hat M als LK
		assertEquals(GostKursart.LK, mgr.getOfSchuelerOfFachKursart(SCHUELER_1_ID, FACH_M_ID));
	}

	@Test
	@DisplayName("testGetOfSchuelerOfFachZugeordneterKurs")
	void testGetOfSchuelerOfFachZugeordneterKurs() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// S1 ist in K1 (D-GK1) für Fach D
		final GostBlockungsergebnisKurs kursD = mgr.getOfSchuelerOfFachZugeordneterKurs(SCHUELER_1_ID, FACH_D_ID);
		assertNotNull(kursD);
		assertEquals(KURS_ID_1, kursD.id);

		// S1 ist in K2 (M-LK1) für Fach M
		final GostBlockungsergebnisKurs kursM = mgr.getOfSchuelerOfFachZugeordneterKurs(SCHUELER_1_ID, FACH_M_ID);
		assertNotNull(kursM);
		assertEquals(KURS_ID_2, kursM.id);

		// S2 ist in K4 (D-GK2) für Fach D
		final GostBlockungsergebnisKurs kursD2 = mgr.getOfSchuelerOfFachZugeordneterKurs(SCHUELER_2_ID, FACH_D_ID);
		assertNotNull(kursD2);
		assertEquals(KURS_ID_4, kursD2.id);

		// Leerer Manager: null, da keine Zuordnung
		final GostBlockungsergebnisManager mgrLeer = createLeerManager();
		assertNull(mgrLeer.getOfSchuelerOfFachZugeordneterKurs(SCHUELER_1_ID, FACH_D_ID));
	}

	@Test
	@DisplayName("testGetOfSchuelerNeuzuordnung")
	void testGetOfSchuelerNeuzuordnung() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// Ohne Fixierung werden K1 und K3 entfernt: K2 (M-LK1) belegt Schienen 1 und 2.
		// S1 wird für D-GK in den freien K4 verschoben; E-GK bleibt ohne Zuordnung.
		final GostBlockungsergebnisKursSchuelerZuordnungUpdate u = mgr.getOfSchuelerNeuzuordnung(SCHUELER_1_ID, false);
		final Set<List<Long>> entfernteZuordnungen = new HashSet<>();
		for (final GostBlockungsergebnisKursSchuelerZuordnung z : u.listEntfernen) {
			entfernteZuordnungen.add(List.of(z.idKurs, z.idSchueler));
		}
		final Set<List<Long>> hinzugefuegteZuordnungen = new HashSet<>();
		for (final GostBlockungsergebnisKursSchuelerZuordnung z : u.listHinzuzufuegen) {
			hinzugefuegteZuordnungen.add(List.of(z.idKurs, z.idSchueler));
		}
		assertEquals(Set.of(List.of(KURS_ID_1, SCHUELER_1_ID), List.of(KURS_ID_3, SCHUELER_1_ID)), entfernteZuordnungen);
		assertEquals(Set.of(List.of(KURS_ID_4, SCHUELER_1_ID)), hinzugefuegteZuordnungen);

		// Auch mit Fixierung wird die kollidierende Belegung auf K1 und K3 reduziert.
		final GostBlockungsergebnisKursSchuelerZuordnungUpdate uFixiert = mgr.getOfSchuelerNeuzuordnung(SCHUELER_1_ID, true);
		final Set<List<Long>> entfernteZuordnungenFixiert = new HashSet<>();
		for (final GostBlockungsergebnisKursSchuelerZuordnung z : uFixiert.listEntfernen) {
			entfernteZuordnungenFixiert.add(List.of(z.idKurs, z.idSchueler));
		}
		final Set<List<Long>> hinzugefuegteZuordnungenFixiert = new HashSet<>();
		for (final GostBlockungsergebnisKursSchuelerZuordnung z : uFixiert.listHinzuzufuegen) {
			hinzugefuegteZuordnungenFixiert.add(List.of(z.idKurs, z.idSchueler));
		}
		assertEquals(Set.of(List.of(KURS_ID_2, SCHUELER_1_ID)), entfernteZuordnungenFixiert);
		assertEquals(Set.of(), hinzugefuegteZuordnungenFixiert);
	}

	@Test
	@DisplayName("testGetOfSchuelerOfKursIstZugeordnet")
	void testGetOfSchuelerOfKursIstZugeordnet() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// S1 ist in K1
		assertTrue(mgr.getOfSchuelerOfKursIstZugeordnet(SCHUELER_1_ID, KURS_ID_1));

		// S1 ist nicht in K4
		assertFalse(mgr.getOfSchuelerOfKursIstZugeordnet(SCHUELER_1_ID, KURS_ID_4));

		// S2 ist in K2
		assertTrue(mgr.getOfSchuelerOfKursIstZugeordnet(SCHUELER_2_ID, KURS_ID_2));

		// S2 ist nicht in K3
		assertFalse(mgr.getOfSchuelerOfKursIstZugeordnet(SCHUELER_2_ID, KURS_ID_3));
	}

	@Test
	@DisplayName("testGetOfSchuelerOfKursIstUngueltig")
	void testGetOfSchuelerOfKursIstUngueltig() {
		// Standard: Schüler passen zu ihren Kursen (Fach+Schueler hat passende Fachwahl)
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// S1 in K1 (D-GK1) --> D-GK passt --> nicht ungültig
		assertFalse(mgr.getOfSchuelerOfKursIstUngueltig(SCHUELER_1_ID, KURS_ID_1));

		// S1 in K2 (M-LK1) --> M-LK passt --> nicht ungültig
		assertFalse(mgr.getOfSchuelerOfKursIstUngueltig(SCHUELER_1_ID, KURS_ID_2));

		// S2 in K4 (D-GK2) --> S2 hat D-GK --> nicht ungültig
		assertFalse(mgr.getOfSchuelerOfKursIstUngueltig(SCHUELER_2_ID, KURS_ID_4));

		// S2 in K3 (E-GK1) --> S2 hat E gar nicht gewählt --> ungültig
		// Zuerst S2 in K3 zuordnen (ohne passende Fachwahl)
		final GostBlockungsergebnisManager mgr2 = createStandardManager();
		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> setUngueltig = new HashSet<>();
		setUngueltig.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_3, SCHUELER_2_ID));
		// K3 ist bereits einer Schiene zugeordnet, bevor die Schüler-Zuordnung revalidiert wird.
		assertTrue(mgr2.getOfKursOfSchieneIstZugeordnet(KURS_ID_3, SCHIENE_ID_2));
		mgr2.kursSchuelerUpdateExecute(mgr2.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(setUngueltig));
		GostBlockungsergebnisKurs kursK3 = null;
		for (final GostBlockungsergebnisSchiene schiene : mgr2.getErgebnis().schienen) {
			for (final GostBlockungsergebnisKurs kurs : schiene.kurse) {
				if (kurs.id == KURS_ID_3) {
					kursK3 = kurs;
				}
			}
		}
		assertNotNull(kursK3);
		assertEquals(Set.of(SCHUELER_1_ID, SCHUELER_2_ID), new HashSet<>(kursK3.schueler));
		assertTrue(mgr2.getOfSchuelerOfKursIstUngueltig(SCHUELER_2_ID, KURS_ID_3));

		mgr2.stateRevalidateEverything();
		kursK3 = null;
		for (final GostBlockungsergebnisSchiene schiene : mgr2.getErgebnis().schienen) {
			for (final GostBlockungsergebnisKurs kurs : schiene.kurse) {
				if (kurs.id == KURS_ID_3) {
					kursK3 = kurs;
				}
			}
		}
		assertNotNull(kursK3);
		assertEquals(Set.of(SCHUELER_1_ID, SCHUELER_2_ID), new HashSet<>(kursK3.schueler));
		assertTrue(mgr2.getOfSchuelerOfKursIstUngueltig(SCHUELER_2_ID, KURS_ID_3));
	}

	@Test
	@DisplayName("testGetOfSchuelerOfKursIstGesperrt")
	void testGetOfSchuelerOfKursIstGesperrt() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1);

		// Ohne Regel: nicht gesperrt
		assertFalse(mgr.getOfSchuelerOfKursIstGesperrt(SCHUELER_1_ID, KURS_ID_1));

		// Mit Regel: gesperrt
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ;
		regel.parameter.add(SCHUELER_1_ID);
		regel.parameter.add(KURS_ID_1);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr2 = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 1);
		assertTrue(mgr2.getOfSchuelerOfKursIstGesperrt(SCHUELER_1_ID, KURS_ID_1));
	}

	@Test
	@DisplayName("testGetOfSchuelerOfKursFachwahl")
	void testGetOfSchuelerOfKursFachwahl() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// K1 ist D-GK1 --> Fach D; S1 hat D-GK Fachwahl
		final GostFachwahl fw = mgr.getOfSchuelerOfKursFachwahl(SCHUELER_1_ID, KURS_ID_1);
		assertNotNull(fw);
		assertEquals(SCHUELER_1_ID, fw.schuelerID);
		assertEquals(FACH_D_ID, fw.fachID);
		assertEquals(KURSART_GK, fw.kursartID);

		// K2 ist M-LK1 --> Fach M; S1 hat M-LK Fachwahl
		final GostFachwahl fw2 = mgr.getOfSchuelerOfKursFachwahl(SCHUELER_1_ID, KURS_ID_2);
		assertEquals(FACH_M_ID, fw2.fachID);
	}

	@Test
	@DisplayName("testGetOfSchuelerMengeGefiltert")
	void testGetOfSchuelerMengeGefiltert() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// Kein Filter: alle 2 Schüler
		final List<Schueler> ohneFilter = mgr.getOfSchuelerMengeGefiltert(-1, -1, -1, 0, "");
		assertEquals(2, ohneFilter.size());
		assertEquals(Set.of(SCHUELER_1_ID, SCHUELER_2_ID), ohneFilter.stream().map(s -> s.id)
				.collect(java.util.stream.Collectors.toSet()));

		// KonfliktTyp=1 (Kollisionen)
		final List<Schueler> mitKollisionen = mgr.getOfSchuelerMengeGefiltert(-1, -1, -1, 1, "");
		assertEquals(1, mitKollisionen.size());
		assertEquals(Set.of(SCHUELER_1_ID), mitKollisionen.stream().map(s -> s.id)
				.collect(java.util.stream.Collectors.toSet()));

		// SubString-Filter "Test1"
		final List<Schueler> mitTest1 = mgr.getOfSchuelerMengeGefiltert(-1, -1, -1, 0, "Test1");
		assertEquals(1, mitTest1.size());
		assertEquals(Set.of(SCHUELER_1_ID), mitTest1.stream().map(s -> s.id)
				.collect(java.util.stream.Collectors.toSet()));

		// SubString-Filter "Test"
		final List<Schueler> mitTest = mgr.getOfSchuelerMengeGefiltert(-1, -1, -1, 0, "Test");
		assertEquals(2, mitTest.size());
		assertEquals(Set.of(SCHUELER_1_ID, SCHUELER_2_ID), mitTest.stream().map(s -> s.id)
				.collect(java.util.stream.Collectors.toSet()));
	}

	@Test
	@DisplayName("testGetOfSchuelerMengeMitAbweichendemAbijahrgang")
	void testGetOfSchuelerMengeMitAbweichendemAbijahrgang() {
		// Standard-Manager: alle Schüler haben passenden Abijahrgang --> leere Liste
		final GostBlockungsergebnisManager mgr = createStandardManager();
		assertEquals(0, mgr.getOfSchuelerMengeMitAbweichendemAbijahrgang().size());
	}

	@Test
	@DisplayName("testGetOfSchuelerMengeMitAbweichendemAbijahrgangOhneZuordnung")
	void testGetOfSchuelerMengeMitAbweichendemAbijahrgangOhneZuordnung() {
		// createParentManagerMitBlockungsvorlage enthält S3 (301) mit abweichendem Abijahrgang (99),
		// aber ohne Kurszuordnung und ohne Regel. Ein solcher Schüler darf NICHT in der Liste erscheinen.
		final GostBlockungsdatenManager parent = createParentManagerMitBlockungsvorlage();
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 8801);

		final List<Schueler> abweichende = mgr.getOfSchuelerMengeMitAbweichendemAbijahrgang();
		assertEquals(0, abweichende.size());
	}

	@Test
	@DisplayName("testGetOfSchuelerMapIDzuUngueltigeKurse")
	void testGetOfSchuelerMapIDzuUngueltigeKurse() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		final Map<Long, Set<GostBlockungsergebnisKurs>> map = mgr.getOfSchuelerMapIDzuUngueltigeKurse();
		assertNotNull(map);

		// In Standard-Manager sind alle Zuordnungen gültig --> Map leer
		assertEquals(0, map.size());

		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> setUngueltig = new HashSet<>();
		setUngueltig.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_3, SCHUELER_2_ID));
		assertTrue(mgr.getOfKursOfSchieneIstZugeordnet(KURS_ID_3, SCHIENE_ID_2));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(setUngueltig));

		final Map<Long, Set<GostBlockungsergebnisKurs>> mapUngueltig = mgr.getOfSchuelerMapIDzuUngueltigeKurse();
		assertEquals(1, mapUngueltig.size());
		assertEquals(Set.of(SCHUELER_2_ID), mapUngueltig.keySet());
		final Set<Long> ungueltigeKursIDs = new HashSet<>();
		for (final GostBlockungsergebnisKurs kurs : mapUngueltig.get(SCHUELER_2_ID)) {
			ungueltigeKursIDs.add(kurs.id);
		}
		assertEquals(Set.of(KURS_ID_3), ungueltigeKursIDs);
	}

	@Test
	@DisplayName("testGetOfSchuelerMapIDzuUngueltigeKurseMehrereKurse")
	void testGetOfSchuelerMapIDzuUngueltigeKurseMehrereKurse() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// S2 hat die Fachwahlen D-GK und M-LK. Ungültig sind für S2 also Kurse anderer Facharten:
		// K3 (E-GK, Schiene 2) und K5 (M-GK, Schiene 3).
		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> setUngueltig = new HashSet<>();
		setUngueltig.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_3, SCHUELER_2_ID));
		setUngueltig.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_5, SCHUELER_2_ID));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(setUngueltig));

		final Map<Long, Set<GostBlockungsergebnisKurs>> map = mgr.getOfSchuelerMapIDzuUngueltigeKurse();
		assertEquals(1, map.size());
		final Set<Long> ungueltigeKursIDs = new HashSet<>();
		for (final GostBlockungsergebnisKurs kurs : map.get(SCHUELER_2_ID)) {
			ungueltigeKursIDs.add(kurs.id);
		}
		assertEquals(Set.of(KURS_ID_3, KURS_ID_5), ungueltigeKursIDs);
	}

	@Test
	@DisplayName("testGetOfSchuelerOfKursHatKollision")
	void testGetOfSchuelerOfKursHatKollision() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// S1 in K1 (Schiene 1), S1 auch in K2 (Schiene 1) --> Kollision in Schiene 1
		assertTrue(mgr.getOfSchuelerOfKursHatKollision(SCHUELER_1_ID, KURS_ID_1));

		// S2 in K2 (Schiene 1+2) und in K4 (Schiene 3) --> keine Kollision für K2
		assertFalse(mgr.getOfSchuelerOfKursHatKollision(SCHUELER_2_ID, KURS_ID_2));
	}

	@Test
	@DisplayName("testGetOfSchuelerIstZusammenMitSchuelerInFach")
	void testGetOfSchuelerIstZusammenMitSchuelerInFach() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// Beide sind in M-Kursen: S1 in K2 (M-LK1), S2 in K2 (M-LK1) --> gleicher Kurs
		assertTrue(mgr.getOfSchuelerIstZusammenMitSchuelerInFach(SCHUELER_1_ID, SCHUELER_2_ID, FACH_M_ID));

		// Beide in D: S1 in K1 (D-GK1), S2 in K4 (D-GK2) --> unterschiedliche Kurse
		assertFalse(mgr.getOfSchuelerIstZusammenMitSchuelerInFach(SCHUELER_1_ID, SCHUELER_2_ID, FACH_D_ID));
	}

	@Test
	@DisplayName("testGetOfSchuelerOfKursAnzahlZusammenWuensche")
	void testGetOfSchuelerOfKursAnzahlZusammenWuensche() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// Keine Regeln --> 0
		assertEquals(0, mgr.getOfSchuelerOfKursAnzahlZusammenWuensche(SCHUELER_1_ID, KURS_ID_1));
	}

	@Test
	@DisplayName("testGetOfSchuelerOfKursAnzahlZusammenWuenscheMitGenerischerRegel")
	void testGetOfSchuelerOfKursAnzahlZusammenWuenscheMitGenerischerRegel() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		mgr.getParent().regelAdd(createRegelSchuelerPaar(REGEL_ID_1,
				GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ, SCHUELER_2_ID, SCHUELER_1_ID));

		assertEquals(1, mgr.getOfSchuelerOfKursAnzahlZusammenWuensche(SCHUELER_1_ID, KURS_ID_2));
		assertEquals(1, mgr.getOfSchuelerOfKursAnzahlZusammenWuensche(SCHUELER_2_ID, KURS_ID_2));
		assertEquals(0, mgr.getOfSchuelerOfKursAnzahlZusammenWuensche(SCHUELER_1_ID, KURS_ID_1));

		final GostBlockungsergebnisManager mgrSelf = createStandardManager();
		mgrSelf.getParent().regelAdd(createRegelSchuelerPaar(REGEL_ID_1,
				GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ, SCHUELER_1_ID, SCHUELER_1_ID));
		assertEquals(0, mgrSelf.getOfSchuelerOfKursAnzahlZusammenWuensche(SCHUELER_1_ID, KURS_ID_2));
	}

	@Test
	@DisplayName("testGetOfSchuelerOfKursAnzahlZusammenWuenscheMitFachregel")
	void testGetOfSchuelerOfKursAnzahlZusammenWuenscheMitFachregel() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		mgr.getParent().regelAdd(createRegelSchuelerPaar(REGEL_ID_1,
				GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ, SCHUELER_2_ID, SCHUELER_1_ID, FACH_M_ID));

		assertEquals(1, mgr.getOfSchuelerOfKursAnzahlZusammenWuensche(SCHUELER_1_ID, KURS_ID_2));
		assertEquals(1, mgr.getOfSchuelerOfKursAnzahlZusammenWuensche(SCHUELER_2_ID, KURS_ID_2));

		final GostBlockungsergebnisManager mgrFalschesFach = createStandardManager();
		mgrFalschesFach.getParent().regelAdd(createRegelSchuelerPaar(REGEL_ID_1,
				GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ, SCHUELER_1_ID, SCHUELER_2_ID, FACH_D_ID));
		assertEquals(0, mgrFalschesFach.getOfSchuelerOfKursAnzahlZusammenWuensche(SCHUELER_1_ID, KURS_ID_2));
	}

	@Test
	@DisplayName("testGetOfSchuelerOfKursAnzahlVerbotenWuensche")
	void testGetOfSchuelerOfKursAnzahlVerbotenWuensche() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// Keine Regeln --> 0
		assertEquals(0, mgr.getOfSchuelerOfKursAnzahlVerbotenWuensche(SCHUELER_1_ID, KURS_ID_1));
	}

	@Test
	@DisplayName("testGetOfSchuelerOfKursAnzahlVerbotenWuenscheMitGenerischerRegel")
	void testGetOfSchuelerOfKursAnzahlVerbotenWuenscheMitGenerischerRegel() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		mgr.getParent().regelAdd(createRegelSchuelerPaar(REGEL_ID_1,
				GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ, SCHUELER_1_ID, SCHUELER_2_ID));

		assertEquals(1, mgr.getOfSchuelerOfKursAnzahlVerbotenWuensche(SCHUELER_1_ID, KURS_ID_2));
		assertEquals(1, mgr.getOfSchuelerOfKursAnzahlVerbotenWuensche(SCHUELER_2_ID, KURS_ID_2));
		assertEquals(0, mgr.getOfSchuelerOfKursAnzahlVerbotenWuensche(SCHUELER_1_ID, KURS_ID_1));

		final GostBlockungsergebnisManager mgrSelf = createStandardManager();
		mgrSelf.getParent().regelAdd(createRegelSchuelerPaar(REGEL_ID_1,
				GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ, SCHUELER_1_ID, SCHUELER_1_ID));
		assertEquals(0, mgrSelf.getOfSchuelerOfKursAnzahlVerbotenWuensche(SCHUELER_1_ID, KURS_ID_2));
	}

	@Test
	@DisplayName("testGetOfSchuelerOfKursAnzahlVerbotenWuenscheMitFachregel")
	void testGetOfSchuelerOfKursAnzahlVerbotenWuenscheMitFachregel() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		mgr.getParent().regelAdd(createRegelSchuelerPaar(REGEL_ID_1,
				GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ, SCHUELER_2_ID, SCHUELER_1_ID, FACH_M_ID));

		assertEquals(1, mgr.getOfSchuelerOfKursAnzahlVerbotenWuensche(SCHUELER_1_ID, KURS_ID_2));
		assertEquals(1, mgr.getOfSchuelerOfKursAnzahlVerbotenWuensche(SCHUELER_2_ID, KURS_ID_2));

		final GostBlockungsergebnisManager mgrFalschesFach = createStandardManager();
		mgrFalschesFach.getParent().regelAdd(createRegelSchuelerPaar(REGEL_ID_1,
				GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ, SCHUELER_1_ID, SCHUELER_2_ID, FACH_D_ID));
		assertEquals(0, mgrFalschesFach.getOfSchuelerOfKursAnzahlVerbotenWuensche(SCHUELER_1_ID, KURS_ID_2));
	}

	@Test
	@DisplayName("testInvalidIDsForStudentCourseCombinations")
	void testInvalidIDsForStudentCourseCombinations() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		assertFalse(mgr.getOfSchuelerOfKursIstZugeordnet(SCHUELER_NICHT_VORHANDEN, KURS_ID_1));
		assertFalse(mgr.getOfSchuelerOfKursIstZugeordnet(SCHUELER_1_ID, KURS_NICHT_VORHANDEN));
		assertFalse(mgr.getOfSchuelerOfKursIstUngueltig(SCHUELER_NICHT_VORHANDEN, KURS_NICHT_VORHANDEN));
		assertFalse(mgr.getOfSchuelerOfKursIstGesperrt(SCHUELER_NICHT_VORHANDEN, KURS_NICHT_VORHANDEN));
		assertFalse(mgr.getOfSchuelerIstZusammenMitSchuelerInFach(SCHUELER_NICHT_VORHANDEN, SCHUELER_2_ID, FACH_M_ID));
		assertFalse(mgr.getOfSchuelerIstZusammenMitSchuelerInFach(SCHUELER_1_ID, SCHUELER_2_ID, FACH_NICHT_VORHANDEN));

		assertThrows(DeveloperNotificationException.class,
				() -> mgr.getOfSchuelerOfKursFachwahl(SCHUELER_NICHT_VORHANDEN, KURS_ID_1));
		assertThrows(DeveloperNotificationException.class,
				() -> mgr.getOfSchuelerOfKursFachwahl(SCHUELER_1_ID, KURS_NICHT_VORHANDEN));
		assertThrows(DeveloperNotificationException.class,
				() -> mgr.getOfSchuelerOfKursHatKollision(SCHUELER_1_ID, KURS_NICHT_VORHANDEN));
		assertFalse(mgr.getOfSchuelerOfKursHatKollision(SCHUELER_2_ID, KURS_NICHT_VORHANDEN));
		assertThrows(DeveloperNotificationException.class,
				() -> mgr.getOfSchuelerOfKursHatKollision(SCHUELER_NICHT_VORHANDEN, KURS_ID_1));
		assertEquals(0, mgr.getOfSchuelerOfKursAnzahlZusammenWuensche(SCHUELER_NICHT_VORHANDEN, KURS_ID_1));
		assertEquals(0, mgr.getOfSchuelerOfKursAnzahlVerbotenWuensche(SCHUELER_NICHT_VORHANDEN, KURS_ID_1));
		assertThrows(DeveloperNotificationException.class,
				() -> mgr.getOfSchuelerOfKursAnzahlZusammenWuensche(SCHUELER_1_ID, KURS_NICHT_VORHANDEN));
		assertThrows(DeveloperNotificationException.class,
				() -> mgr.getOfSchuelerOfKursAnzahlVerbotenWuensche(SCHUELER_1_ID, KURS_NICHT_VORHANDEN));
	}

	// #########################################################################
	// ##########       Kurs (Course) Getters                          ##########
	// #########################################################################

	@Test
	@DisplayName("testGetKursG")
	void testGetKursG() {
		final GostBlockungsergebnisManager mgr = createLeerManager();

		// Existierender Kurs
		final GostBlockungKurs kurs = mgr.getKursG(KURS_ID_1);
		assertNotNull(kurs);
		assertEquals(KURS_ID_1, kurs.id);
		assertEquals(FACH_D_ID, kurs.fach_id);
		assertEquals(KURSART_GK, kurs.kursart);

		// Nicht existierender Kurs
		assertThrows(DeveloperNotificationException.class, () -> mgr.getKursG(KURS_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testGetKursE")
	void testGetKursE() {
		final GostBlockungsergebnisManager mgr = createLeerManager();

		// Existierender Kurs
		final GostBlockungsergebnisKurs kurs = mgr.getKursE(KURS_ID_1);
		assertNotNull(kurs);
		assertEquals(KURS_ID_1, kurs.id);

		// Nicht existierender Kurs
		assertThrows(DeveloperNotificationException.class, () -> mgr.getKursE(KURS_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testGetKursmenge")
	void testGetKursmenge() {
		final GostBlockungsergebnisManager mgr = createLeerManager();

		// 5 Kurse im Parent
		assertEquals(5, mgr.getKursmenge().size());
		assertEquals(Set.of(KURS_ID_1, KURS_ID_2, KURS_ID_3, KURS_ID_4, KURS_ID_5), mgr.getKursmenge().stream().map(k -> k.id)
				.collect(java.util.stream.Collectors.toSet()));
	}

	@Test
	@DisplayName("testGetOfKursName")
	void testGetOfKursName() {
		final GostBlockungsergebnisManager mgr = createLeerManager();

		assertEquals("D-GK1", mgr.getOfKursName(KURS_ID_1));
		assertEquals("M-LK1", mgr.getOfKursName(KURS_ID_2));

		// Nicht existierender Kurs
		assertThrows(DeveloperNotificationException.class, () -> mgr.getOfKursName(KURS_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testGetOfKursOfSchieneIstZugeordnet")
	void testGetOfKursOfSchieneIstZugeordnet() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// K1 ist in Schiene 1
		assertTrue(mgr.getOfKursOfSchieneIstZugeordnet(KURS_ID_1, SCHIENE_ID_1));
		assertFalse(mgr.getOfKursOfSchieneIstZugeordnet(KURS_ID_1, SCHIENE_ID_2));

		// K2 ist in Schiene 1 und Schiene 2
		assertTrue(mgr.getOfKursOfSchieneIstZugeordnet(KURS_ID_2, SCHIENE_ID_1));
		assertTrue(mgr.getOfKursOfSchieneIstZugeordnet(KURS_ID_2, SCHIENE_ID_2));
		assertFalse(mgr.getOfKursOfSchieneIstZugeordnet(KURS_ID_2, SCHIENE_ID_3));

		// Nicht existierende Schiene
		assertThrows(DeveloperNotificationException.class, () -> mgr.getOfKursOfSchieneIstZugeordnet(KURS_ID_1, SCHIENE_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testGetOfKursSchuelermenge")
	void testGetOfKursSchuelermenge() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// K1 hat 1 Schüler (S1)
		assertEquals(1, mgr.getOfKursSchuelermenge(KURS_ID_1).size());
		assertEquals(SCHUELER_1_ID, mgr.getOfKursSchuelermenge(KURS_ID_1).get(0).id);
		assertEquals(Set.of(SCHUELER_1_ID), new HashSet<>(mgr.getOfKursSchuelermenge(KURS_ID_1).stream().map(s -> s.id).toList()));

		// K2 hat 2 Schüler (S1, S2)
		assertEquals(2, mgr.getOfKursSchuelermenge(KURS_ID_2).size());
		assertEquals(Set.of(SCHUELER_1_ID, SCHUELER_2_ID), new HashSet<>(mgr.getOfKursSchuelermenge(KURS_ID_2).stream().map(s -> s.id).toList()));

		// K3 hat 1 Schüler (S1)
		assertEquals(1, mgr.getOfKursSchuelermenge(KURS_ID_3).size());
		assertEquals(Set.of(SCHUELER_1_ID), new HashSet<>(mgr.getOfKursSchuelermenge(KURS_ID_3).stream().map(s -> s.id).toList()));

		// K5 hat 0 Schüler
		assertEquals(0, mgr.getOfKursSchuelermenge(KURS_ID_5).size());
		assertEquals(Set.of(), new HashSet<>(mgr.getOfKursSchuelermenge(KURS_ID_5).stream().map(s -> s.id).toList()));

		// Nicht existierender Kurs
		assertThrows(DeveloperNotificationException.class, () -> mgr.getOfKursSchuelermenge(KURS_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testGetOfKursSchienenmenge")
	void testGetOfKursSchienenmenge() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// K1 ist in Schiene 1 --> 1 Schiene
		assertEquals(1, mgr.getOfKursSchienenmenge(KURS_ID_1).size());

		// K2 ist in Schiene 1,2 --> 2 Schienen
		assertEquals(2, mgr.getOfKursSchienenmenge(KURS_ID_2).size());

		// K5 ist in Schiene 3 --> 1 Schiene
		assertEquals(1, mgr.getOfKursSchienenmenge(KURS_ID_5).size());

		// Nicht existierender Kurs
		assertThrows(DeveloperNotificationException.class, () -> mgr.getOfKursSchienenmenge(KURS_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testGetOfKursSchienenNummern")
	void testGetOfKursSchienenNummern() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// K1 ist in Schiene 1
		final int[] n1 = mgr.getOfKursSchienenNummern(KURS_ID_1);
		assertEquals(1, n1.length);
		assertEquals(SCHIENE_NR_1, n1[0]);

		// K2 ist in Schienen 1 und 2
		final int[] n2 = mgr.getOfKursSchienenNummern(KURS_ID_2);
		Arrays.sort(n2);
		assertArrayEquals(new int[] { SCHIENE_NR_1, SCHIENE_NR_2 }, n2);

		// K5 hat 0 Schienen
		final GostBlockungsergebnisManager mgrLeer = createLeerManager();
		assertEquals(0, mgrLeer.getOfKursSchienenNummern(KURS_ID_5).length);
	}

	@Test
	@DisplayName("testGetOfKursAnzahlSchueler")
	void testGetOfKursAnzahlSchueler() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		assertEquals(1, mgr.getOfKursAnzahlSchueler(KURS_ID_1));
		assertEquals(2, mgr.getOfKursAnzahlSchueler(KURS_ID_2));
		assertEquals(1, mgr.getOfKursAnzahlSchueler(KURS_ID_3));
		assertEquals(0, mgr.getOfKursAnzahlSchueler(KURS_ID_5));

		// Nicht existierender Kurs
		assertThrows(DeveloperNotificationException.class, () -> mgr.getOfKursAnzahlSchueler(KURS_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testGetOfKursAnzahlSchuelerPlusDummy")
	void testGetOfKursAnzahlSchuelerPlusDummy() {
		// Ohne Dummy
		final GostBlockungsergebnisManager mgr = createStandardManager();
		assertEquals(1, mgr.getOfKursAnzahlSchuelerPlusDummy(KURS_ID_1));

		// Mit Dummy
		final GostBlockungsergebnisManager mgrDummy = createManagerMitDummySus();
		assertEquals(5, mgrDummy.getOfKursAnzahlSchuelerPlusDummy(KURS_ID_1)); // 0 echte + 5 Dummy
	}

	@Test
	@DisplayName("testGetOfKursAnzahlSchuelerDummy")
	void testGetOfKursAnzahlSchuelerDummy() {
		// Ohne Dummy: 0
		final GostBlockungsergebnisManager mgr = createStandardManager();
		assertEquals(0, mgr.getOfKursAnzahlSchuelerDummy(KURS_ID_1));

		// Mit Dummy
		final GostBlockungsergebnisManager mgrDummy = createManagerMitDummySus();
		assertEquals(5, mgrDummy.getOfKursAnzahlSchuelerDummy(KURS_ID_1));
		assertEquals(0, mgrDummy.getOfKursAnzahlSchuelerDummy(KURS_ID_2)); // Kein Dummy für K2
	}

	@Test
	@DisplayName("testGetOfKursAnzahlSchuelerExterne")
	void testGetOfKursAnzahlSchuelerExterne() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// Alle SuS sind aktiv --> 0 externe
		assertEquals(0, mgr.getOfKursAnzahlSchuelerExterne(KURS_ID_1));
	}

	@Test
	@DisplayName("testGetOfKursAnzahlSchuelerExternePlusDummies")
	void testGetOfKursAnzahlSchuelerExternePlusDummies() {
		// Mit Dummy
		final GostBlockungsergebnisManager mgrDummy = createManagerMitDummySus();
		assertEquals(5, mgrDummy.getOfKursAnzahlSchuelerExternePlusDummies(KURS_ID_1)); // 0 externe + 5 Dummy
	}

	@Test
	@DisplayName("testGetOfKursUndSchieneAnzahlSchuelerExternMitDummies")
	void testGetOfKursUndSchieneAnzahlSchuelerExternMitDummies() {
		final GostBlockungsdatenManager parent = createParentManagerMitExternemSchueler();
		parent.regelAdd(createRegelDummySUS(KURS_ID_1, 5));
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 9500);

		// K1 muss vor der Schüler-Zuordnung in Schiene 1 liegen, damit die Zuordnung erhalten bleibt.
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(Set.of(
				DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1))));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(Set.of(
				DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, 300L))));

		assertEquals(1, mgr.getOfKursAnzahlSchuelerExterne(KURS_ID_1));
		assertEquals(0, mgr.getOfKursAnzahlSchuelerInterne(KURS_ID_1));
		assertEquals(5, mgr.getOfKursAnzahlSchuelerDummy(KURS_ID_1));
		assertEquals(6, mgr.getOfKursAnzahlSchuelerExternePlusDummies(KURS_ID_1));
		assertEquals(1, mgr.getOfKursAnzahlSchueler(KURS_ID_1));
		assertEquals(6, mgr.getOfKursAnzahlSchuelerPlusDummy(KURS_ID_1));
		assertEquals(1, mgr.getOfSchieneAnzahlSchuelerExterne(SCHIENE_ID_1));
		assertEquals(5, mgr.getOfSchieneAnzahlSchuelerDummy(SCHIENE_ID_1));
	}

	@Test
	@DisplayName("testGetOfKursAnzahlSchuelerInterne")
	void testGetOfKursAnzahlSchuelerInterne() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// K1: 1 SuS intern (S1), 0 extern --> 1
		assertEquals(1, mgr.getOfKursAnzahlSchuelerInterne(KURS_ID_1));

		// K5: 0 SuS --> 0
		assertEquals(0, mgr.getOfKursAnzahlSchuelerInterne(KURS_ID_5));
	}

	@Test
	@DisplayName("testGetOfKursAnzahlSchuelerInterneMitExternUndDummy")
	void testGetOfKursAnzahlSchuelerInterneMitExternUndDummy() {
		// Mischung: 1 interner SuS (S1) + 1 externer SuS (S300) + 5 Dummy-SuS im selben Kurs K1.
		final GostBlockungsdatenManager parent = createParentManagerMitExternemSchueler();
		parent.regelAdd(createRegelDummySUS(KURS_ID_1, 5));
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 9503);

		// K1 (D-GK1) in Schiene 1 legen, damit die Schülerzuordnung erhalten bleibt.
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(Set.of(
				DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1))));

		// S1 (intern) und S300 (extern) dem Kurs K1 zuordnen.
		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> s = new HashSet<>();
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_1_ID));
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, 300L));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(s));

		// 2 echte SuS (1 intern + 1 extern), davon 1 extern, zusätzlich 5 Dummy.
		assertEquals(2, mgr.getOfKursAnzahlSchueler(KURS_ID_1));
		assertEquals(1, mgr.getOfKursAnzahlSchuelerExterne(KURS_ID_1));
		assertEquals(5, mgr.getOfKursAnzahlSchuelerDummy(KURS_ID_1));
		// Intern = alle - extern = 2 - 1 = 1 (Dummy wird NICHT mitgezählt).
		assertEquals(1, mgr.getOfKursAnzahlSchuelerInterne(KURS_ID_1));
	}

	@Test
	@DisplayName("testGetOfKursAnzahlSchuelerSchriftlich")
	void testGetOfKursAnzahlSchuelerSchriftlich() {
		// Standard: Fachwahlen haben istSchriftlich=false per default --> 0
		final GostBlockungsergebnisManager mgr = createStandardManager();
		assertEquals(0, mgr.getOfKursAnzahlSchuelerSchriftlich(KURS_ID_1));
	}

	@Test
	@DisplayName("testGetOfKursAnzahlSchienenIst")
	void testGetOfKursAnzahlSchienenIst() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		assertEquals(1, mgr.getOfKursAnzahlSchienenIst(KURS_ID_1));
		assertEquals(2, mgr.getOfKursAnzahlSchienenIst(KURS_ID_2));
		assertEquals(1, mgr.getOfKursAnzahlSchienenIst(KURS_ID_5));

		// Leerer Manager: 0
		final GostBlockungsergebnisManager mgrLeer = createLeerManager();
		assertEquals(0, mgrLeer.getOfKursAnzahlSchienenIst(KURS_ID_1));
	}

	@Test
	@DisplayName("testGetOfKursAnzahlSchienenSoll")
	void testGetOfKursAnzahlSchienenSoll() {
		final GostBlockungsergebnisManager mgr = createLeerManager();

		// Alle Kurse haben anzahlSchienen=1 (default)
		assertEquals(1, mgr.getOfKursAnzahlSchienenSoll(KURS_ID_1));
		assertEquals(1, mgr.getOfKursAnzahlSchienenSoll(KURS_ID_2));
	}

	@Test
	@DisplayName("testGetOfKursAnzahlSchuelerAbiturLK")
	void testGetOfKursAnzahlSchuelerAbiturLK() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// Fachwahlen haben kein abiturfach gesetzt --> 0
		assertEquals(0, mgr.getOfKursAnzahlSchuelerAbiturLK(KURS_ID_1));
		assertEquals(0, mgr.getOfKursAnzahlSchuelerAbiturLK(KURS_ID_2));
	}

	@Test
	@DisplayName("testGetOfKursAnzahlSchuelerAbitur3")
	void testGetOfKursAnzahlSchuelerAbitur3() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// Fachwahlen haben kein abiturfach gesetzt --> 0
		assertEquals(0, mgr.getOfKursAnzahlSchuelerAbitur3(KURS_ID_1));
	}

	@Test
	@DisplayName("testGetOfKursAnzahlSchuelerAbitur4")
	void testGetOfKursAnzahlSchuelerAbitur4() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// Fachwahlen haben kein abiturfach gesetzt --> 0
		assertEquals(0, mgr.getOfKursAnzahlSchuelerAbitur4(KURS_ID_1));
	}

	@Test
	@DisplayName("testInvalidIDsForCourseAndRailGetters")
	void testInvalidIDsForCourseAndRailGetters() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		assertThrows(DeveloperNotificationException.class, () -> mgr.getOfKursSchienenNummern(KURS_NICHT_VORHANDEN));
		assertThrows(DeveloperNotificationException.class, () -> mgr.getOfKursAnzahlSchuelerPlusDummy(KURS_NICHT_VORHANDEN));
		assertThrows(DeveloperNotificationException.class, () -> mgr.getOfKursAnzahlSchuelerExterne(KURS_NICHT_VORHANDEN));
		assertThrows(DeveloperNotificationException.class, () -> mgr.getOfKursAnzahlSchuelerInterne(KURS_NICHT_VORHANDEN));
		// Der Schriftlichkeitsfilter iteriert über vorhandene Schüler und liefert bei einer unbekannten Kurs-ID 0.
		assertEquals(0, mgr.getOfKursAnzahlSchuelerSchriftlich(KURS_NICHT_VORHANDEN));
		assertThrows(DeveloperNotificationException.class, () -> mgr.getOfKursAnzahlSchienenIst(KURS_NICHT_VORHANDEN));
		assertThrows(DeveloperNotificationException.class, () -> mgr.getOfKursAnzahlSchienenSoll(KURS_NICHT_VORHANDEN));
		assertThrows(DeveloperNotificationException.class, () -> mgr.getOfKursAnzahlSchuelerAbiturLK(KURS_NICHT_VORHANDEN));
		assertThrows(DeveloperNotificationException.class, () -> mgr.getOfKursAnzahlSchuelerAbitur3(KURS_NICHT_VORHANDEN));
		assertThrows(DeveloperNotificationException.class, () -> mgr.getOfKursAnzahlSchuelerAbitur4(KURS_NICHT_VORHANDEN));

		assertThrows(DeveloperNotificationException.class, () -> mgr.getOfSchieneIstLeer(SCHIENE_NICHT_VORHANDEN));
		assertThrows(DeveloperNotificationException.class, () -> mgr.getOfSchieneAnzahlSchueler(SCHIENE_NICHT_VORHANDEN));
		assertThrows(DeveloperNotificationException.class, () -> mgr.getOfSchieneAnzahlSchuelerMitKollisionen(SCHIENE_NICHT_VORHANDEN));
		assertThrows(DeveloperNotificationException.class, () -> mgr.getOfSchieneKursmengeMitKollisionen(SCHIENE_NICHT_VORHANDEN));
		assertThrows(DeveloperNotificationException.class, () -> mgr.getOfSchieneKursmengeSortiert(SCHIENE_NICHT_VORHANDEN));
		assertThrows(DeveloperNotificationException.class, () -> mgr.getOfSchieneTooltipKurskollisionenAsData(SCHIENE_NICHT_VORHANDEN));
		// Dieser Tooltip prüft die Schienen-ID nicht und enthält bei unbekannter ID keine Zeilen.
		assertEquals("", mgr.getOfSchieneTooltipSchuelerkollisionen(SCHIENE_NICHT_VORHANDEN));
		assertThrows(DeveloperNotificationException.class, () -> mgr.getOfSchieneAnzahlSchuelerExterne(SCHIENE_NICHT_VORHANDEN));
		assertThrows(DeveloperNotificationException.class, () -> mgr.getOfSchieneAnzahlSchuelerDummy(SCHIENE_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testGetOfKursAnzahlSchuelerAbiturIgnoriertFehlendeFachwahl")
	void testGetOfKursAnzahlSchuelerAbiturIgnoriertFehlendeFachwahl() {
		// LK: S1 hat M-LK als Abiturfach 1, S3 hat keine Fachwahl für M-LK.
		final GostBlockungsdatenManager parentLK = createParentManager();
		parentLK.schuelerAdd(createSchueler(301L, "Test3", "Chris"));
		parentLK.schuelerGetOfFachFachwahl(SCHUELER_1_ID, FACH_M_ID).abiturfach = 1;
		final GostBlockungsergebnisManager mgrLK = new GostBlockungsergebnisManager(parentLK, ERGEBNIS_ID_1 + 1);
		mgrLK.kursSchienenUpdateExecute(mgrLK.kursSchienenUpdateFuegeKursSchienenPaareHinzu(Set.of(
				DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_2, SCHIENE_ID_1))));
		mgrLK.kursSchuelerUpdateExecute(mgrLK.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(Set.of(
				DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_2, SCHUELER_1_ID),
				DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_2, 301L))));
		assertEquals(1, mgrLK.getOfKursAnzahlSchuelerAbiturLK(KURS_ID_2));

		// AB3: S1 hat E-GK als Abiturfach 3, S2 hat keine Fachwahl für E-GK.
		final GostBlockungsdatenManager parent3 = createParentManager();
		parent3.schuelerGetOfFachFachwahl(SCHUELER_1_ID, FACH_E_ID).abiturfach = 3;
		final GostBlockungsergebnisManager mgr3 = new GostBlockungsergebnisManager(parent3, ERGEBNIS_ID_1 + 2);
		mgr3.kursSchienenUpdateExecute(mgr3.kursSchienenUpdateFuegeKursSchienenPaareHinzu(Set.of(
				DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_3, SCHIENE_ID_2))));
		mgr3.kursSchuelerUpdateExecute(mgr3.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(Set.of(
				DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_3, SCHUELER_1_ID),
				DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_3, SCHUELER_2_ID))));
		assertEquals(1, mgr3.getOfKursAnzahlSchuelerAbitur3(KURS_ID_3));

		// AB4: S1 hat E-GK als Abiturfach 4, S2 hat keine Fachwahl für E-GK.
		final GostBlockungsdatenManager parent4 = createParentManager();
		parent4.schuelerGetOfFachFachwahl(SCHUELER_1_ID, FACH_E_ID).abiturfach = 4;
		final GostBlockungsergebnisManager mgr4 = new GostBlockungsergebnisManager(parent4, ERGEBNIS_ID_1 + 3);
		mgr4.kursSchienenUpdateExecute(mgr4.kursSchienenUpdateFuegeKursSchienenPaareHinzu(Set.of(
				DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_3, SCHIENE_ID_2))));
		mgr4.kursSchuelerUpdateExecute(mgr4.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(Set.of(
				DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_3, SCHUELER_1_ID),
				DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_3, SCHUELER_2_ID))));
		assertEquals(1, mgr4.getOfKursAnzahlSchuelerAbitur4(KURS_ID_3));
	}

	@Test
	@DisplayName("testGetOfKursMaxSuS")
	void testGetOfKursMaxSuS() {
		final GostBlockungsergebnisManager mgr = createLeerManager();

		// Ohne Regel: 999
		assertEquals(999, mgr.getOfKursMaxSuS(KURS_ID_1));

		// Mit Regel
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ;
		regel.parameter.add(KURS_ID_1);
		regel.parameter.add(30L);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr2 = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 1);
		assertEquals(30, mgr2.getOfKursMaxSuS(KURS_ID_1));
	}

	@Test
	@DisplayName("testGetOfKursMaxSuSGrenzwert")
	void testGetOfKursMaxSuSGrenzwert() {
		// Regel mit dem maximal erlaubten Wert (KURS_MAXIMALE_SCHUELERANZAHL_MAX).
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ;
		regel.parameter.add(KURS_ID_1);
		regel.parameter.add((long) GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL_MAX);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 9502);
		assertEquals(GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL_MAX, mgr.getOfKursMaxSuS(KURS_ID_1));
	}

	// #########################################################################
	// ##########       Mapping Getters                                ##########
	// #########################################################################

	@Test
	@DisplayName("testGetMappingKursIDSchuelerIDs")
	void testGetMappingKursIDSchuelerIDs() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		final Map<Long, Set<Long>> mapping = mgr.getMappingKursIDSchuelerIDs();
		assertNotNull(mapping);
		assertEquals(Set.of(KURS_ID_1, KURS_ID_2, KURS_ID_3, KURS_ID_4, KURS_ID_5), mapping.keySet());

		// K1 enthält die ID S1
		assertEquals(1, mapping.get(KURS_ID_1).size());
		assertTrue(mapping.get(KURS_ID_1).contains(SCHUELER_1_ID));

		// K2 enthält S1 und S2
		assertEquals(2, mapping.get(KURS_ID_2).size());

		// K5 ist leer
		assertEquals(0, mapping.get(KURS_ID_5).size());

		assertEquals(Set.of(SCHUELER_1_ID), mapping.get(KURS_ID_1));
		assertEquals(Set.of(SCHUELER_1_ID, SCHUELER_2_ID), mapping.get(KURS_ID_2));
		assertEquals(Set.of(SCHUELER_1_ID), mapping.get(KURS_ID_3));
		assertEquals(Set.of(SCHUELER_2_ID), mapping.get(KURS_ID_4));
		assertEquals(Set.of(), mapping.get(KURS_ID_5));
	}

	@Test
	@DisplayName("testGetMappingKursIDSchienenmenge")
	void testGetMappingKursIDSchienenmenge() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		final Map<Long, Set<GostBlockungsergebnisSchiene>> mapping = mgr.getMappingKursIDSchienenmenge();
		assertNotNull(mapping);
		assertEquals(Set.of(KURS_ID_1, KURS_ID_2, KURS_ID_3, KURS_ID_4, KURS_ID_5), mapping.keySet());

		// K1 belegt 1 Schiene
		assertEquals(1, mapping.get(KURS_ID_1).size());

		// K2 belegt 2 Schienen
		assertEquals(2, mapping.get(KURS_ID_2).size());

		// K5 belegt 1 Schiene
		assertEquals(1, mapping.get(KURS_ID_5).size());

		assertEquals(Set.of(SCHIENE_ID_1), mapping.get(KURS_ID_1).stream().map(s -> s.id).collect(java.util.stream.Collectors.toSet()));
		assertEquals(Set.of(SCHIENE_ID_1, SCHIENE_ID_2), mapping.get(KURS_ID_2).stream().map(s -> s.id)
				.collect(java.util.stream.Collectors.toSet()));
		assertEquals(Set.of(SCHIENE_ID_2), mapping.get(KURS_ID_3).stream().map(s -> s.id).collect(java.util.stream.Collectors.toSet()));
		assertEquals(Set.of(SCHIENE_ID_3), mapping.get(KURS_ID_4).stream().map(s -> s.id).collect(java.util.stream.Collectors.toSet()));
		assertEquals(Set.of(SCHIENE_ID_3), mapping.get(KURS_ID_5).stream().map(s -> s.id).collect(java.util.stream.Collectors.toSet()));
	}

	// #########################################################################
	// ##########       Regelverletzungen Getters                      ##########
	// #########################################################################

	@Test
	@DisplayName("testRegelGetMapRegelIdToVerletzungString")
	void testRegelGetMapRegelIdToVerletzungString() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		final Map<Long, String> map = mgr.regelGetMapRegelIdToVerletzungString();
		assertNotNull(map);

		// Ohne Regeln: leere Map
		assertEquals(0, map.size());
	}

	// #########################################################################
	// ##########       Schiene (Rail) Getters                         ##########
	// #########################################################################

	@Test
	@DisplayName("testGetSchieneG")
	void testGetSchieneG() {
		final GostBlockungsergebnisManager mgr = createLeerManager();

		final GostBlockungSchiene schiene = mgr.getSchieneG(SCHIENE_ID_1);
		assertNotNull(schiene);
		assertEquals(SCHIENE_ID_1, schiene.id);

		assertThrows(DeveloperNotificationException.class, () -> mgr.getSchieneG(SCHIENE_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testGetOfSchieneIstLeer")
	void testGetOfSchieneIstLeer() {
		// Leerer Manager: Schiene 1 hat keine Kurse --> leer
		final GostBlockungsergebnisManager mgrLeer = createLeerManager();
		assertTrue(mgrLeer.getOfSchieneIstLeer(SCHIENE_ID_1));

		// Standard-Manager: Schiene 1 hat K1 und K2 --> nicht leer
		final GostBlockungsergebnisManager mgr = createStandardManager();
		assertFalse(mgr.getOfSchieneIstLeer(SCHIENE_ID_1));
	}

	@Test
	@DisplayName("testGetOfSchieneID")
	void testGetOfSchieneID() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		assertEquals(SCHIENE_ID_1, mgr.getOfSchieneID(SCHIENE_NR_1));
		assertEquals(SCHIENE_ID_2, mgr.getOfSchieneID(SCHIENE_NR_2));
		assertEquals(SCHIENE_ID_3, mgr.getOfSchieneID(SCHIENE_NR_3));

		assertThrows(DeveloperNotificationException.class, () -> mgr.getOfSchieneID(99));
	}

	@Test
	@DisplayName("testGetMengeAllerSchienen")
	void testGetMengeAllerSchienen() {
		final GostBlockungsergebnisManager mgr = createLeerManager();

		assertEquals(3, mgr.getMengeAllerSchienen().size());
		assertEquals(Set.of(SCHIENE_ID_1, SCHIENE_ID_2, SCHIENE_ID_3), mgr.getMengeAllerSchienen().stream().map(s -> s.id)
				.collect(java.util.stream.Collectors.toSet()));
	}

	@Test
	@DisplayName("testGetOfSchieneExists")
	void testGetOfSchieneExists() {
		final GostBlockungsergebnisManager mgr = createLeerManager();

		assertTrue(mgr.getOfSchieneExists(SCHIENE_ID_1));
		assertFalse(mgr.getOfSchieneExists(SCHIENE_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testGetOfSchieneAnzahlSchueler")
	void testGetOfSchieneAnzahlSchueler() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// Schiene 1: K1(S1)+K2(S1,S2) = S1 zählt doppelt durch Kollision
		assertEquals(3, mgr.getOfSchieneAnzahlSchueler(SCHIENE_ID_1));
		// Schiene 3: K4(S2)+K5(0) = 1
		assertEquals(1, mgr.getOfSchieneAnzahlSchueler(SCHIENE_ID_3));
	}

	@Test
	@DisplayName("testGetOfSchieneHatKollision")
	void testGetOfSchieneHatKollision() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// Schiene 1: S1 doppelt (K1+K2) --> Kollision
		assertTrue(mgr.getOfSchieneHatKollision(SCHIENE_ID_1));
		// Schiene 3: S2 nur einmal in K4 --> keine Kollision
		assertFalse(mgr.getOfSchieneHatKollision(SCHIENE_ID_3));
	}

	@Test
	@DisplayName("testGetOfSchieneAnzahlSchuelerMitKollisionen")
	void testGetOfSchieneAnzahlSchuelerMitKollisionen() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// Schiene 1: S1 ist doppelt (K1+K2) --> 1 Kollision
		assertEquals(1, mgr.getOfSchieneAnzahlSchuelerMitKollisionen(SCHIENE_ID_1));
		// Schiene 3: keine Kollisionen
		assertEquals(0, mgr.getOfSchieneAnzahlSchuelerMitKollisionen(SCHIENE_ID_3));
	}

	@Test
	@DisplayName("testGetOfSchieneSchuelermengeMitKollisionen")
	void testGetOfSchieneSchuelermengeMitKollisionen() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// Schiene 1: S1 kollidiert
		final Set<Long> koll = mgr.getOfSchieneSchuelermengeMitKollisionen(SCHIENE_ID_1);
		assertEquals(1, koll.size());
		assertTrue(koll.contains(SCHUELER_1_ID));
	}

	@Test
	@DisplayName("testGetOfSchieneMaxKursanzahl")
	void testGetOfSchieneMaxKursanzahl() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// Schiene 1 hat 2 Kurse (K1+K2), Schiene 2 hat 2 Kurse (K2+K3), Schiene 3 hat 2 Kurse (K4+K5)
		assertEquals(2, mgr.getOfSchieneMaxKursanzahl());
	}

	@Test
	@DisplayName("testGetOfSchieneAnzahlSchuelerExterne")
	void testGetOfSchieneAnzahlSchuelerExterne() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// Keine externen Schüler --> 0
		assertEquals(0, mgr.getOfSchieneAnzahlSchuelerExterne(SCHIENE_ID_1));
	}

	@Test
	@DisplayName("testGetOfSchieneAnzahlSchuelerDummy")
	void testGetOfSchieneAnzahlSchuelerDummy() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// Keine Dummy-SuS --> 0
		assertEquals(0, mgr.getOfSchieneAnzahlSchuelerDummy(SCHIENE_ID_1));
	}

	// #########################################################################
	// ##########       Sortierung (Sorting)                            ##########
	// #########################################################################

	@Test
	@DisplayName("testKursSetSortierungKursartFachNummer")
	void testKursSetSortierungKursartFachNummer() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		mgr.kursSetSortierungKursartFachNummer();

		assertEquals(List.of(KURS_ID_2, KURS_ID_1), mgr.getOfSchieneKursmengeSortiert(SCHIENE_ID_1).stream().map(k -> k.id).toList());
		assertEquals(List.of(KURS_ID_2, KURS_ID_3), mgr.getOfSchieneKursmengeSortiert(SCHIENE_ID_2).stream().map(k -> k.id).toList());
		assertEquals(List.of(KURS_ID_4, KURS_ID_5), mgr.getOfSchieneKursmengeSortiert(SCHIENE_ID_3).stream().map(k -> k.id).toList());
		assertEquals(List.of(KURS_ID_1, KURS_ID_4), mgr.getOfFachartKursmenge(GostKursart.getFachartID(FACH_D_ID, KURSART_GK)).stream().map(k -> k.id).toList());
		assertEquals(List.of(KURS_ID_2), mgr.getOfFachartKursmenge(GostKursart.getFachartID(FACH_M_ID, KURSART_LK)).stream().map(k -> k.id).toList());
		assertEquals(List.of(KURS_ID_5), mgr.getOfFachartKursmenge(GostKursart.getFachartID(FACH_M_ID, KURSART_GK)).stream().map(k -> k.id).toList());
	}

	@Test
	@DisplayName("testKursSetSortierungFachKursartNummer")
	void testKursSetSortierungFachKursartNummer() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		mgr.kursSetSortierungFachKursartNummer();

		assertEquals(List.of(KURS_ID_1, KURS_ID_2), mgr.getOfSchieneKursmengeSortiert(SCHIENE_ID_1).stream().map(k -> k.id).toList());
		assertEquals(List.of(KURS_ID_3, KURS_ID_2), mgr.getOfSchieneKursmengeSortiert(SCHIENE_ID_2).stream().map(k -> k.id).toList());
		assertEquals(List.of(KURS_ID_4, KURS_ID_5), mgr.getOfSchieneKursmengeSortiert(SCHIENE_ID_3).stream().map(k -> k.id).toList());
		assertEquals(List.of(KURS_ID_1, KURS_ID_4), mgr.getOfFachartKursmenge(GostKursart.getFachartID(FACH_D_ID, KURSART_GK)).stream().map(k -> k.id).toList());
		assertEquals(List.of(KURS_ID_2), mgr.getOfFachartKursmenge(GostKursart.getFachartID(FACH_M_ID, KURSART_LK)).stream().map(k -> k.id).toList());
		assertEquals(List.of(KURS_ID_5), mgr.getOfFachartKursmenge(GostKursart.getFachartID(FACH_M_ID, KURSART_GK)).stream().map(k -> k.id).toList());
	}

	// #########################################################################
	// ##########       State Mutation (set*/patch*)                  ##########
	// #########################################################################

	@Test
	@DisplayName("testSetAddSchieneByID")
	void testSetAddSchieneByID() {
		final GostBlockungsdatenManager parent = createParentManagerMitBlockungsvorlage();
		final GostBlockungSchiene hinzugefuegteSchiene = parent.schieneGet(SCHIENE_ID_3);
		parent.schieneRemoveByID(SCHIENE_ID_3);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1);

		assertEquals(2, mgr.getMengeAllerSchienen().size());

		parent.schieneAdd(hinzugefuegteSchiene);
		assertTrue(parent.schieneGetExistiert(SCHIENE_ID_3));
		mgr.setAddSchieneByID(SCHIENE_ID_3);
		assertEquals(3, mgr.getMengeAllerSchienen().size());
		assertTrue(mgr.getOfSchieneExists(SCHIENE_ID_3));
		assertTrue(mgr.getOfSchieneIstLeer(SCHIENE_ID_3));
		assertTrue(mgr.getMengeAllerSchienen().stream().filter(s -> s.id == SCHIENE_ID_3).findFirst().orElseThrow().kurse.isEmpty());

		// Nicht existierende Schiene --> Exception
		assertThrows(DeveloperNotificationException.class, () -> mgr.setAddSchieneByID(SCHIENE_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testSetAddKursByID")
	void testSetAddKursByID() {
		final GostBlockungsergebnisManager mgr = createLeerManager();

		// Kurs existiert bereits
		mgr.setAddKursByID(KURS_ID_1);
		assertEquals(1, mgr.getOfKursSchienenmenge(KURS_ID_1).size());
		assertTrue(mgr.getOfKursOfSchieneIstZugeordnet(KURS_ID_1, SCHIENE_ID_1));
		assertEquals(0, mgr.getOfKursSchuelermenge(KURS_ID_1).size());

		// Nicht existierender Kurs --> Exception
		assertThrows(DeveloperNotificationException.class, () -> mgr.setAddKursByID(KURS_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testStateRevalidateEverything")
	void testStateRevalidateEverything() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		assertEquals(1, mgr.getOfBewertung1Wert());
		mgr.stateRevalidateEverything();
		assertEquals(1, mgr.getOfBewertung1Wert());
		assertEquals(1, mgr.getErgebnis().bewertung.anzahlKurseNichtZugeordnet);
		assertEquals(2, mgr.getErgebnis().bewertung.anzahlSchuelerKollisionen);
	}

	@Test
	@DisplayName("testPatchOfKursLehrkaefteChanged")
	void testPatchOfKursLehrkaefteChanged() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungKursLehrer lehrer = new GostBlockungKursLehrer();
		lehrer.id = LEHRER_ID_1;
		lehrer.reihenfolge = 1;
		lehrer.kuerzel = "L";
		parent.kursAddLehrkraft(KURS_ID_1, lehrer);
		parent.kursAddLehrkraft(KURS_ID_2, lehrer);
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN.typ;
		parent.regelAdd(regel);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1);
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_2, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));

		mgr.patchOfKursLehrkaefteChanged();
		assertEquals(1, mgr.getErgebnis().bewertung.regelVerletzungen.size());
		assertEquals(1, mgr.getErgebnis().bewertung.regelVerletzungen.stream().filter(id -> id == REGEL_ID_1).count());
	}

	// #########################################################################
	// ##########       Kurs-Schienen-Update Getters                  ##########
	// #########################################################################

	@Test
	@DisplayName("testKursSchienenUpdateFuegeKursSchienenPaareHinzu")
	void testKursSchienenUpdateFuegeKursSchienenPaareHinzu() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// K1 bereits in Schiene 1 -> listHinzuzufuegen leer
		final GostBlockungsergebnisKursSchienenZuordnung zuordnung = DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1);
		final GostBlockungsergebnisKursSchienenZuordnungUpdate update = mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(Set.of(zuordnung));
		assertNotNull(update);
		assertEquals(0, update.listHinzuzufuegen.size());
		assertEquals(Set.of(), update.listHinzuzufuegen.stream().map(z -> List.of(z.idKurs, z.idSchiene))
				.collect(java.util.stream.Collectors.toSet()));

		// K1 nicht in Schiene 2 --> wird hinzugefügt
		final GostBlockungsergebnisKursSchienenZuordnung zNeu = DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_2);
		final GostBlockungsergebnisKursSchienenZuordnungUpdate updateNeu = mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(Set.of(zNeu));
		assertEquals(1, updateNeu.listHinzuzufuegen.size());
		assertEquals(Set.of(List.of(KURS_ID_1, SCHIENE_ID_2)), updateNeu.listHinzuzufuegen.stream()
				.map(z -> List.of(z.idKurs, z.idSchiene)).collect(java.util.stream.Collectors.toSet()));
	}

	@Test
	@DisplayName("testKursSchienenUpdateEntferneKursSchienenPaare")
	void testKursSchienenUpdateEntferneKursSchienenPaare() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// K1 in Schiene 1 -> wird in listEntfernen aufgenommen
		final GostBlockungsergebnisKursSchienenZuordnung z = DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1);
		final GostBlockungsergebnisKursSchienenZuordnungUpdate update = mgr.kursSchienenUpdateEntferneKursSchienenPaare(Set.of(z));
		assertEquals(1, update.listEntfernen.size());
		assertEquals(Set.of(List.of(KURS_ID_1, SCHIENE_ID_1)), update.listEntfernen.stream()
				.map(pair -> List.of(pair.idKurs, pair.idSchiene)).collect(java.util.stream.Collectors.toSet()));

		// K1 nicht in Schiene 2 -> kein Eintrag
		final GostBlockungsergebnisKursSchienenZuordnung zOhne = DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_2);
		final GostBlockungsergebnisKursSchienenZuordnungUpdate updateOhne = mgr.kursSchienenUpdateEntferneKursSchienenPaare(Set.of(zOhne));
		assertEquals(0, updateOhne.listEntfernen.size());
		assertEquals(Set.of(), updateOhne.listEntfernen.stream().map(pair -> List.of(pair.idKurs, pair.idSchiene))
				.collect(java.util.stream.Collectors.toSet()));
	}

	// #########################################################################
	// ##########       Kurs-Schüler-Update Getters                    ##########
	// #########################################################################

	@Test
	@DisplayName("testKursSchuelerUpdateLeereAlleKurse")
	void testKursSchuelerUpdateLeereAlleKurse() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// Ohne Fixierte entfernen: S1 ist nicht fixiert -> wird entfernt
		final GostBlockungsergebnisKursSchuelerZuordnungUpdate u = mgr.kursSchuelerUpdateLeereAlleKurse(false);
		assertEquals(Set.of(
				List.of(KURS_ID_1, SCHUELER_1_ID),
				List.of(KURS_ID_2, SCHUELER_1_ID),
				List.of(KURS_ID_2, SCHUELER_2_ID),
				List.of(KURS_ID_3, SCHUELER_1_ID),
				List.of(KURS_ID_4, SCHUELER_2_ID)), u.listEntfernen.stream()
				.map(pair -> List.of(pair.idKurs, pair.idSchueler)).collect(java.util.stream.Collectors.toSet()));
		assertEquals(Set.of(), u.listHinzuzufuegen.stream().map(pair -> List.of(pair.idKurs, pair.idSchueler))
				.collect(java.util.stream.Collectors.toSet()));
	}

	@Test
	@DisplayName("testKursSchuelerUpdateLeereKursmenge")
	void testKursSchuelerUpdateLeereKursmenge() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// Nur K1 leeren: S1 ist in K1 -> wird entfernt
		final GostBlockungsergebnisKursSchuelerZuordnungUpdate u = mgr.kursSchuelerUpdateLeereKursmenge(Set.of(KURS_ID_1), false);
		assertNotNull(u);
		assertEquals(1, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testKursSchuelerUpdateEntferneSchuelermengeAusKurs")
	void testKursSchuelerUpdateEntferneSchuelermengeAusKurs() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// S1 aus K1 entfernen (nicht fixiert)
		final GostBlockungsergebnisKursSchuelerZuordnungUpdate u = mgr.kursSchuelerUpdateEntferneSchuelermengeAusKurs(Set.of(SCHUELER_1_ID), KURS_ID_1, false);
		assertEquals(1, u.listEntfernen.size());
		assertTrue(u.listEntfernen.contains(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_1_ID)));
		assertEquals(0, u.regelUpdates.listEntfernen.size());

		// S2 aus K1 entfernen (S2 ist gar nicht in K1)
		final GostBlockungsergebnisKursSchuelerZuordnungUpdate uLeer =
				mgr.kursSchuelerUpdateEntferneSchuelermengeAusKurs(Set.of(SCHUELER_2_ID), KURS_ID_1, false);
		assertEquals(0, uLeer.listEntfernen.size());
	}

	@Test
	@DisplayName("testKursSchuelerUpdateEntferneSchuelermengeAusAllenKursen")
	void testKursSchuelerUpdateEntferneSchuelermengeAusAllenKursen() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// S1 ist in 3 Kursen -> 3 Einträge
		final GostBlockungsergebnisKursSchuelerZuordnungUpdate u = mgr.kursSchuelerUpdateEntferneSchuelermengeAusAllenKursen(Set.of(SCHUELER_1_ID));
		assertEquals(3, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testKursSchuelerUpdateFuegeKursSchuelerPaareHinzu")
	void testKursSchuelerUpdateFuegeKursSchuelerPaareHinzu() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// S1 bereits in K1 -> kein Hinzufügen
		final GostBlockungsergebnisKursSchuelerZuordnung z = DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_1_ID);
		final GostBlockungsergebnisKursSchuelerZuordnungUpdate u = mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(Set.of(z));
		assertEquals(0, u.listHinzuzufuegen.size());
		assertEquals(Set.of(), u.listHinzuzufuegen.stream().map(pair -> List.of(pair.idKurs, pair.idSchueler))
				.collect(java.util.stream.Collectors.toSet()));

		// S1 wird zu K4 hinzugefügt und aus K1 entfernt (gleiches Fach D)
		final GostBlockungsergebnisKursSchuelerZuordnung zNeu = DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_4, SCHUELER_1_ID);
		final GostBlockungsergebnisKursSchuelerZuordnungUpdate uNeu = mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(Set.of(zNeu));
		// S1 bereits in K1 (D-GK1), wird zu K4 (D-GK2) verschoben -> Entfernen aus K1
		assertEquals(1, uNeu.listEntfernen.size());
		assertEquals(1, uNeu.listHinzuzufuegen.size());
		assertEquals(Set.of(List.of(KURS_ID_1, SCHUELER_1_ID)), uNeu.listEntfernen.stream()
				.map(pair -> List.of(pair.idKurs, pair.idSchueler)).collect(java.util.stream.Collectors.toSet()));
		assertEquals(Set.of(List.of(KURS_ID_4, SCHUELER_1_ID)), uNeu.listHinzuzufuegen.stream()
				.map(pair -> List.of(pair.idKurs, pair.idSchueler)).collect(java.util.stream.Collectors.toSet()));
	}

	@Test
	@DisplayName("testKursSchuelerUpdateEntferneKursSchuelerPaare")
	void testKursSchuelerUpdateEntferneKursSchuelerPaare() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// S1 ist in K1 -> wird entfernt
		final GostBlockungsergebnisKursSchuelerZuordnung z = DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_1_ID);
		final GostBlockungsergebnisKursSchuelerZuordnungUpdate u = mgr.kursSchuelerUpdateEntferneKursSchuelerPaare(Set.of(z));
		assertEquals(1, u.listEntfernen.size());
		assertEquals(Set.of(List.of(KURS_ID_1, SCHUELER_1_ID)), u.listEntfernen.stream()
				.map(pair -> List.of(pair.idKurs, pair.idSchueler)).collect(java.util.stream.Collectors.toSet()));
	}

	@Test
	@DisplayName("testKursSchuelerUpdateFuegeKursSchuelerPaareHinzuOhneFachwahl")
	void testKursSchuelerUpdateFuegeKursSchuelerPaareHinzuOhneFachwahl() {
		// Im Gegensatz zur Verschiebe-Variante (kursSchuelerUpdate03a...) prüft diese Methode nicht,
		// ob der Schüler die passende Fachwahl hat. Ein Schüler ohne passende Fachwahl wird trotzdem
		// hinzugefügt und anschließend als ungültige Zuordnung geführt.
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// S1 hat M-LK gewählt, aber KEINE M-GK. K5 ist M-GK1 --> ungültige Zuordnung.
		final GostBlockungsergebnisKursSchuelerZuordnung erfolg = DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_5, SCHUELER_1_ID);
		final GostBlockungsergebnisKursSchuelerZuordnungUpdate u = mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(Set.of(erfolg));

		// Die Zuordnung wird zugelassen (kein Fachwahl-Guard).
		assertEquals(1, u.listHinzuzufuegen.size());
		assertEquals(Set.of(List.of(KURS_ID_5, SCHUELER_1_ID)), u.listHinzuzufuegen.stream()
				.map(pair -> List.of(pair.idKurs, pair.idSchueler)).collect(java.util.stream.Collectors.toSet()));

		// Nach der Ausführung ist S1 dem Kurs K5 zugeordnet und gilt als ungültig.
		mgr.kursSchuelerUpdateExecute(u);
		assertTrue(mgr.getOfSchuelerOfKursIstZugeordnet(SCHUELER_1_ID, KURS_ID_5));
		assertTrue(mgr.getOfSchuelerOfKursIstUngueltig(SCHUELER_1_ID, KURS_ID_5));
	}

	// #########################################################################
	// ##########       Regel-Update Getters (Create)                 ##########
	// #########################################################################

	@Test
	@DisplayName("testRegelupdateCreateKursFixiereAlleInIhrenSchienen")
	void testRegelupdateCreateKursFixiereAlleInIhrenSchienen() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		final GostBlockungRegelUpdate u = mgr.regelupdateCreateKursFixiereAlleInIhrenSchienen();
		assertEquals(6, u.listHinzuzufuegen.size());
		assertEquals(0, u.listEntfernen.size());
		assertTrue(u.listHinzuzufuegen.stream().allMatch(r -> r.typ == GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ));
		final Set<List<Long>> parameter = new HashSet<>();
		for (final GostBlockungRegel regel : u.listHinzuzufuegen) {
			parameter.add(regel.parameter);
		}
		assertEquals(Set.of(List.of(KURS_ID_1, 1L), List.of(KURS_ID_2, 1L), List.of(KURS_ID_2, 2L),
				List.of(KURS_ID_3, 2L), List.of(KURS_ID_4, 3L), List.of(KURS_ID_5, 3L)), parameter);
	}

	@Test
	@DisplayName("testRegelupdateCreateKursSperreInSchiene")
	void testRegelupdateCreateKursSperreInSchiene() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// Keine bestehenden Sperren/ Fixierungen -> Kurs wird gesperrt
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateKursSperreInSchiene(Set.of(KURS_ID_1), Set.of(SCHIENE_NR_2));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ,
				Set.of(List.of(KURS_ID_1, (long) SCHIENE_NR_2)));
		assertEquals(0, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateSchuelerFixierenInAllenKursen")
	void testRegelupdateCreateSchuelerFixierenInAllenKursen() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		final GostBlockungRegelUpdate u = mgr.regelupdateCreateSchuelerFixierenInAllenKursen();
		assertNotNull(u);
		// S1 und S2 sind in insgesamt 5 Kurszuordnungen -> 5 Fixierungen
		assertEquals(5, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateLehrkaefteBeachten")
	void testRegelupdateCreateLehrkaefteBeachten() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// Regel existiert noch nicht -> hinzufügen
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateLehrkaefteBeachten(true);
		assertEquals(1, u.listHinzuzufuegen.size());
		assertEquals(0, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateKursMitDummySusAuffuellen")
	void testRegelupdateCreateKursMitDummySusAuffuellen() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// Noch keine Regel -> hinzufügen
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateKursMitDummySusAuffuellen(KURS_ID_1, 5);
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ,
				Set.of(List.of(KURS_ID_1, 5L)));
		assertEquals(0, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateKursVerbietenMitKurs")
	void testRegelupdateCreateKursVerbietenMitKurs() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		final GostBlockungRegelUpdate u = mgr.regelupdateCreateKursVerbietenMitKurs(Set.of(KURS_ID_1, KURS_ID_3));
		// K1 < K3 -> eine Paarung
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ,
				Set.of(List.of(KURS_ID_1, KURS_ID_3)));
		assertEquals(0, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateKursZusammenMitKurs")
	void testRegelupdateCreateKursZusammenMitKurs() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		final GostBlockungRegelUpdate u = mgr.regelupdateCreateKursZusammenMitKurs(Set.of(KURS_ID_1, KURS_ID_3));
		// K1 < K3 -> eine Paarung
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ,
				Set.of(List.of(KURS_ID_1, KURS_ID_3)));
		assertEquals(0, u.listEntfernen.size());
	}

	// #########################################################################
	// ##########       Regel-Update Getters (Remove)                 ##########
	// #########################################################################

	@Test
	@DisplayName("testRegelupdateRemoveKursFixiereAlleInIhrenSchienen")
	void testRegelupdateRemoveKursFixiereAlleInIhrenSchienen() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
		regel.parameter.add(KURS_ID_1);
		regel.parameter.add((long) SCHIENE_NR_1);
		parent.regelAdd(regel);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1);

		final GostBlockungRegelUpdate u = mgr.regelupdateRemoveKursFixiereAlleInIhrenSchienen();
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ,
				Set.of(List.of(KURS_ID_1, (long) SCHIENE_NR_1)));
		assertEquals(0, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testRegelupdateRemoveSchuelerFixierenInAllenKursen")
	void testRegelupdateRemoveSchuelerFixierenInAllenKursen() {
		final GostBlockungsdatenManager parent = createParentManager();
		for (final long idKurs : new long[] { KURS_ID_1, KURS_ID_2 }) {
			final GostBlockungRegel regel = new GostBlockungRegel();
			regel.id = REGEL_ID_1 + idKurs;
			regel.typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ;
			regel.parameter.add(SCHUELER_1_ID);
			regel.parameter.add(idKurs);
			parent.regelAdd(regel);
		}
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1);

		final GostBlockungRegelUpdate u = mgr.regelupdateRemoveSchuelerFixierenInAllenKursen();
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ,
				Set.of(List.of(SCHUELER_1_ID, KURS_ID_1), List.of(SCHUELER_1_ID, KURS_ID_2)));
		assertEquals(0, u.listHinzuzufuegen.size());
	}

	// #########################################################################
	// ##########       Sonstige (Misc)                                 ##########
	// #########################################################################

	@Test
	@DisplayName("testRegelupdateExecute")
	void testRegelupdateExecute() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regelEntfernen = createRegelDummySUS(KURS_ID_1, 5);
		final GostBlockungRegel regelHinzufuegen = createRegelDummySUS(KURS_ID_2, 7);
		parent.regelAdd(regelEntfernen);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1);
		final GostBlockungRegelUpdate update = new GostBlockungRegelUpdate();
		update.listEntfernen = List.of(regelEntfernen);
		update.listHinzuzufuegen = List.of(regelHinzufuegen);

		mgr.regelupdateExecute(update);

		assertEquals(1, parent.regelGetListe().size());
		assertFalse(parent.regelGetExistiert(regelEntfernen.id));
		assertTrue(parent.regelGetExistiert(regelHinzufuegen.id));
		assertEquals(regelHinzufuegen.typ, parent.regelGet(regelHinzufuegen.id).typ);
		assertEquals(regelHinzufuegen.parameter, parent.regelGet(regelHinzufuegen.id).parameter);
	}

	@Test
	@DisplayName("testDebug")
	void testDebug() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final Logger logger = new Logger();

		// Debug sollte keine Exception werfen
		assertDoesNotThrow(() -> mgr.debug(logger));
	}

	@Test
	@DisplayName("testSetMergeKurseByID")
	void testSetMergeKurseByID() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// K1 (D-GK1) und K4 (D-GK2) haben gleiches Fach und gleiche Kursart
		// setMergeKurseByID delegiert an parent.kursMerge, das Blockungsvorlage erwartet
		assertThrows(DeveloperNotificationException.class, () -> mgr.setMergeKurseByID(KURS_ID_1, KURS_ID_4));
	}

	@Test
	@DisplayName("testPatchOfKursSchienenAnzahl")
	void testPatchOfKursSchienenAnzahl() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// Ohne Blockungsvorlage -> Exception
		assertThrows(DeveloperNotificationException.class, () -> mgr.patchOfKursSchienenAnzahl(KURS_ID_1, 2));
	}

	@Test
	@DisplayName("testSetSplitKurs")
	void testSetSplitKurs() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// parent.kursAdd erlaubt Hinzufügen eines neuen Kurses
		// setSplitKurs erzeugt den Kurs und verschiebt Schüler
		final GostBlockungKurs kursNeu = createKurs(99, FACH_D_ID, KURSART_GK, 99);
		final GostBlockungKurs kursAlt = mgr.getKursG(KURS_ID_1);
		mgr.setSplitKurs(kursAlt, kursNeu, new long[] { SCHUELER_1_ID });
		assertNotNull(mgr.getKursG(99));
		assertFalse(mgr.getOfSchuelerOfKursIstZugeordnet(SCHUELER_1_ID, KURS_ID_1));
		assertTrue(mgr.getOfSchuelerOfKursIstZugeordnet(SCHUELER_1_ID, 99));
		assertEquals(Set.of(KURS_ID_1, KURS_ID_2, KURS_ID_3, KURS_ID_4, KURS_ID_5, 99L), mgr.getParent().daten().kurse.stream()
				.map(k -> k.id).collect(java.util.stream.Collectors.toSet()));
		assertEquals(Set.of(), mgr.getMappingKursIDSchuelerIDs().get(KURS_ID_1));
		assertEquals(Set.of(SCHUELER_1_ID), mgr.getMappingKursIDSchuelerIDs().get(99L));
		assertEquals(Set.copyOf(mgr.getKursE(KURS_ID_1).schienen), Set.copyOf(mgr.getKursE(99).schienen));
		for (final long idSchiene : mgr.getKursE(99).schienen) {
			assertTrue(mgr.getErgebnis().schienen.stream().filter(s -> s.id == idSchiene).findFirst().orElseThrow().kurse.stream()
					.anyMatch(k -> k.id == 99L));
		}
	}

	@Test
	@DisplayName("testKursSchuelerUpdateExecute")
	void testKursSchuelerUpdateExecute() {
		final GostBlockungsdatenManager parent = createParentManager();
		final List<GostFachwahl> fachwahlenVorher = new ArrayList<>(parent.schuelerGetListeOfFachwahlen(SCHUELER_1_ID));
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1);
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(Set.of(
				DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1))));
		final GostBlockungsergebnisKursSchuelerZuordnungUpdate u = mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(
				Set.of(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_1_ID)));
		mgr.kursSchuelerUpdateExecute(u);
		assertTrue(mgr.getOfSchuelerOfKursIstZugeordnet(SCHUELER_1_ID, KURS_ID_1));
		assertEquals(List.of(SCHUELER_1_ID), mgr.getKursE(KURS_ID_1).schueler);
		assertEquals(Set.of(SCHUELER_1_ID), mgr.getMappingKursIDSchuelerIDs().get(KURS_ID_1));
		assertEquals(List.of(SCHUELER_1_ID), mgr.getOfKursSchuelermenge(KURS_ID_1).stream().map(s -> s.id).toList());
		assertEquals(fachwahlenVorher, parent.schuelerGetListeOfFachwahlen(SCHUELER_1_ID));
		assertEquals(FACH_D_ID, parent.kursGet(KURS_ID_1).fach_id);
	}

	@Test
	@DisplayName("testKursSchienenUpdateExecute")
	void testKursSchienenUpdateExecute() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1);
		final GostBlockungsergebnisKursSchienenZuordnungUpdate u = mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(
				Set.of(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_3)));
		mgr.kursSchienenUpdateExecute(u);
		assertTrue(mgr.getOfKursOfSchieneIstZugeordnet(KURS_ID_1, SCHIENE_ID_3));
		assertEquals(Set.of(SCHIENE_ID_3), Set.copyOf(mgr.getKursE(KURS_ID_1).schienen));
		assertEquals(Set.of(KURS_ID_1), mgr.getErgebnis().schienen.stream().filter(s -> s.id == SCHIENE_ID_3).findFirst().orElseThrow().kurse.stream()
				.map(k -> k.id).collect(java.util.stream.Collectors.toSet()));
		assertEquals(Set.of(SCHIENE_ID_3), mgr.getMappingKursIDSchienenmenge().get(KURS_ID_1).stream()
				.map(s -> s.id).collect(java.util.stream.Collectors.toSet()));
		assertEquals(FACH_D_ID, parent.kursGet(KURS_ID_1).fach_id);
	}

	@Test
	@DisplayName("testKursSchienenUpdateExecuteVerschiebeKursVonSchieneNachSchiene")
	void testKursSchienenUpdateExecuteVerschiebeKursVonSchieneNachSchiene() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungsergebnisKursSchienenZuordnungUpdate u =
				mgr.kursSchienenUpdateVerschiebeKursVonSchieneNachSchiene(KURS_ID_1, SCHIENE_ID_1, SCHIENE_ID_3);

		mgr.kursSchienenUpdateExecute(u);

		assertEquals(Set.of(SCHIENE_ID_3), Set.copyOf(mgr.getKursE(KURS_ID_1).schienen));
		assertEquals(Set.of(KURS_ID_2), mgr.getErgebnis().schienen.stream().filter(s -> s.id == SCHIENE_ID_1).findFirst().orElseThrow().kurse.stream()
				.map(k -> k.id).collect(java.util.stream.Collectors.toSet()));
		assertEquals(Set.of(KURS_ID_1, KURS_ID_4, KURS_ID_5), mgr.getErgebnis().schienen.stream().filter(s -> s.id == SCHIENE_ID_3).findFirst().orElseThrow().kurse.stream()
				.map(k -> k.id).collect(java.util.stream.Collectors.toSet()));
		assertEquals(Set.of(SCHIENE_ID_3), mgr.getOfKursSchienenmenge(KURS_ID_1).stream().map(s -> s.id)
				.collect(java.util.stream.Collectors.toSet()));
		assertEquals(Set.of(SCHIENE_ID_3), mgr.getMappingKursIDSchienenmenge().get(KURS_ID_1).stream().map(s -> s.id)
				.collect(java.util.stream.Collectors.toSet()));
		assertEquals(FACH_D_ID, mgr.getKursE(KURS_ID_1).fachID);
	}

	@Test
	@DisplayName("testKursSchienenUpdateVerschiebeKursVonSchieneNachSchiene")
	void testKursSchienenUpdateVerschiebeKursVonSchieneNachSchiene() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// K1 von Schiene 1 nach Schiene 3 verschieben
		final GostBlockungsergebnisKursSchienenZuordnungUpdate u =
				mgr.kursSchienenUpdateVerschiebeKursVonSchieneNachSchiene(KURS_ID_1, SCHIENE_ID_1, SCHIENE_ID_3);
		assertNotNull(u);
		assertEquals(1, u.listEntfernen.size());
		assertEquals(1, u.listHinzuzufuegen.size());
		assertEquals(Set.of(List.of(KURS_ID_1, SCHIENE_ID_1)), u.listEntfernen.stream()
				.map(z -> List.of(z.idKurs, z.idSchiene)).collect(java.util.stream.Collectors.toSet()));
		assertEquals(Set.of(List.of(KURS_ID_1, SCHIENE_ID_3)), u.listHinzuzufuegen.stream()
				.map(z -> List.of(z.idKurs, z.idSchiene)).collect(java.util.stream.Collectors.toSet()));
	}

	@Test
	@DisplayName("testKursSchuelerUpdateExecuteVerschiebeSchuelerVonKursNachKurs")
	void testKursSchuelerUpdateExecuteVerschiebeSchuelerVonKursNachKurs() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungsdatenManager parent = mgr.getParent();
		final List<GostFachwahl> fachwahlenVorher = new ArrayList<>(parent.schuelerGetListeOfFachwahlen(SCHUELER_1_ID));
		final GostBlockungsergebnisKursSchuelerZuordnungUpdate u = mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(
				Set.of(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_4, SCHUELER_1_ID)));

		mgr.kursSchuelerUpdateExecute(u);

		assertFalse(mgr.getOfSchuelerOfKursIstZugeordnet(SCHUELER_1_ID, KURS_ID_1));
		assertTrue(mgr.getOfSchuelerOfKursIstZugeordnet(SCHUELER_1_ID, KURS_ID_4));
		assertEquals(Set.of(), Set.copyOf(mgr.getKursE(KURS_ID_1).schueler));
		assertEquals(Set.of(SCHUELER_1_ID, SCHUELER_2_ID), Set.copyOf(mgr.getKursE(KURS_ID_4).schueler));
		assertEquals(Set.of(), mgr.getMappingKursIDSchuelerIDs().get(KURS_ID_1));
		assertEquals(Set.of(SCHUELER_1_ID, SCHUELER_2_ID), mgr.getMappingKursIDSchuelerIDs().get(KURS_ID_4));
		assertEquals(Set.of(), mgr.getOfKursSchuelermenge(KURS_ID_1).stream().map(s -> s.id).collect(java.util.stream.Collectors.toSet()));
		assertEquals(Set.of(SCHUELER_1_ID, SCHUELER_2_ID), mgr.getOfKursSchuelermenge(KURS_ID_4).stream().map(s -> s.id)
				.collect(java.util.stream.Collectors.toSet()));
		assertEquals(Set.of(KURS_ID_2, KURS_ID_3, KURS_ID_4), mgr.getOfSchuelerKursmenge(SCHUELER_1_ID).stream().map(k -> k.id)
				.collect(java.util.stream.Collectors.toSet()));
		assertEquals(fachwahlenVorher, parent.schuelerGetListeOfFachwahlen(SCHUELER_1_ID));
	}

	@Test
	@DisplayName("testRegelupdateRemoveKursFixiereInEinerSchiene")
	void testRegelupdateRemoveKursFixiereInEinerSchiene() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
		regel.parameter.add(KURS_ID_1);
		regel.parameter.add((long) SCHIENE_NR_1);
		parent.regelAdd(regel);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1);
		final GostBlockungRegelUpdate u = mgr.regelupdateRemoveKursFixiereInEinerSchiene(KURS_ID_1, SCHIENE_NR_1);
		assertEquals(1, u.listEntfernen.size());
		assertEquals(GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, u.listEntfernen.get(0).typ);
		assertEquals(List.of(KURS_ID_1, (long) SCHIENE_NR_1), u.listEntfernen.get(0).parameter);
	}

	@Test
	@DisplayName("testRegelupdateRemoveSchuelerFixierenInKurs")
	void testRegelupdateRemoveSchuelerFixierenInKurs() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ;
		regel.parameter.add(SCHUELER_1_ID);
		regel.parameter.add(KURS_ID_1);
		parent.regelAdd(regel);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1);

		final GostBlockungRegelUpdate u = mgr.regelupdateRemoveSchuelerFixierenInKurs(Set.of(SCHUELER_1_ID), Set.of(KURS_ID_1));
		assertEquals(1, u.listEntfernen.size());
		assertEquals(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, u.listEntfernen.get(0).typ);
		assertEquals(List.of(SCHUELER_1_ID, KURS_ID_1), u.listEntfernen.get(0).parameter);
	}

	@Test
	@DisplayName("testRegelupdateCreateSchuelerFixierenTypLk")
	void testRegelupdateCreateSchuelerFixierenTypLk() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// Keine LK-Fachwahlen mit abiturfach gesetzt -> leeres Update
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateSchuelerFixierenTypLk();
		assertEquals(0, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateSchuelerFixierenTypAb")
	void testRegelupdateCreateSchuelerFixierenTypAb() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		final GostBlockungRegelUpdate u = mgr.regelupdateCreateSchuelerFixierenTypAb();
		assertEquals(0, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateSchuelerVerbietenInKurs")
	void testRegelupdateCreateSchuelerVerbietenInKurs() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// Keine bestehende Fixierung/Sperrung -> hinzufügen
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateSchuelerVerbietenInKurs(Set.of(SCHUELER_1_ID), Set.of(KURS_ID_3));
		assertEquals(1, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testGetOfSchieneKursmengeMitKollisionen")
	void testGetOfSchieneKursmengeMitKollisionen() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		// Schiene 1: K1+K2 haben S1 gemeinsam -> beide haben Kollisionen
		final Set<GostBlockungsergebnisKurs> koll = mgr.getOfSchieneKursmengeMitKollisionen(SCHIENE_ID_1);
		assertEquals(2, koll.size());
		assertEquals(Set.of(KURS_ID_1, KURS_ID_2), koll.stream().map(k -> k.id).collect(java.util.stream.Collectors.toSet()));
	}

	@Test
	@DisplayName("testGetOfSchieneKursmengeSortiert")
	void testGetOfSchieneKursmengeSortiert() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		// Schiene 1 hat K1 und K2 -> 2 Kurse
		assertEquals(2, mgr.getOfSchieneKursmengeSortiert(SCHIENE_ID_1).size());
		assertEquals(Set.of(KURS_ID_1, KURS_ID_2), mgr.getOfSchieneKursmengeSortiert(SCHIENE_ID_1).stream().map(k -> k.id)
				.collect(java.util.stream.Collectors.toSet()));
	}

	@Test
	@DisplayName("testGetOfSchieneTooltipKurskollisionenAsData")
	void testGetOfSchieneTooltipKurskollisionenAsData() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		// Schiene 1: K1+K2 haben S1 gemeinsam -> eine Kollisionszeile
		final var data = mgr.getOfSchieneTooltipKurskollisionenAsData(SCHIENE_ID_1);
		assertEquals(2, data.size());
		assertTrue(data.stream().allMatch(list -> list.size() == 2));
		assertTrue(data.stream().allMatch(list -> list.get(0).b == 1 && list.get(1).b == 1));
		final Set<Long> kursIDs = new HashSet<>();
		for (final var list : data) {
			kursIDs.add(list.get(0).a.id);
			kursIDs.add(list.get(1).a.id);
		}
		assertEquals(Set.of(KURS_ID_1, KURS_ID_2), kursIDs);
		final Set<List<Long>> kollisionsdaten = new HashSet<>();
		for (final var list : data) {
			kollisionsdaten.add(List.of(list.get(0).a.id, list.get(0).b.longValue(), list.get(1).a.id, list.get(1).b.longValue()));
		}
		assertEquals(Set.of(List.of(KURS_ID_1, 1L, KURS_ID_2, 1L), List.of(KURS_ID_2, 1L, KURS_ID_1, 1L)), kollisionsdaten);
	}

	@Test
	@DisplayName("testGetOfSchieneTooltipSchuelerkollisionen")
	void testGetOfSchieneTooltipSchuelerkollisionen() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		// Schiene 1: S1 in K1+K2 -> Kollisionstext
		final String tooltip = mgr.getOfSchieneTooltipSchuelerkollisionen(SCHIENE_ID_1);
		assertNotNull(tooltip);
		assertTrue(tooltip.contains("Test1, Max"));
	}

	@Test
	@DisplayName("testGetOfSchieneRemoveAllowed")
	void testGetOfSchieneRemoveAllowed() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		// Schiene 1 hat Kurse -> Löschen nicht erlaubt
		assertFalse(mgr.getOfSchieneRemoveAllowed(SCHIENE_ID_1));

		// Leerer Manager: Schiene 1 hat keine Kurse -> Löschen erlaubt
		assertTrue(createLeerManager().getOfSchieneRemoveAllowed(SCHIENE_ID_1));

		// Nicht existierende Schiene -> Exception
		assertThrows(DeveloperNotificationException.class, () -> mgr.getOfSchieneRemoveAllowed(SCHIENE_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testSetRemoveSchieneByID")
	void testSetRemoveSchieneByID() {
		final GostBlockungsergebnisManager mgr = createLeerManager();
		// Schiene existiert noch im Parent -> Exception
		assertThrows(DeveloperNotificationException.class, () -> mgr.setRemoveSchieneByID(SCHIENE_ID_1));

		// Nicht existierende Schiene -> Exception
		assertThrows(DeveloperNotificationException.class, () -> mgr.setRemoveSchieneByID(SCHIENE_NICHT_VORHANDEN));
	}

	@Test
	@DisplayName("testSetRemoveKurseByID")
	void testSetRemoveKurseByID() {
		final GostBlockungsergebnisManager mgr = createLeerManager();
		final List<Long> kurs1Liste = List.of(KURS_ID_1);
		final List<Long> kursFremdListe = List.of(KURS_NICHT_VORHANDEN);

		// Kurs existiert noch im Parent -> Exception
		assertThrows(DeveloperNotificationException.class, () -> mgr.setRemoveKurseByID(kurs1Liste));

		// Nicht existierender Kurs -> Exception
		assertThrows(DeveloperNotificationException.class, () -> mgr.setRemoveKurseByID(kursFremdListe));
	}

	@Test
	@DisplayName("testRegelupdateCreateKursFixiereInEinerSchiene")
	void testRegelupdateCreateKursFixiereInEinerSchiene() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		// K1 ist in Schiene 1 -> fixieren
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateKursFixiereInEinerSchiene(KURS_ID_1, SCHIENE_NR_1);
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ,
				Set.of(List.of(KURS_ID_1, (long) SCHIENE_NR_1)));
		assertEquals(0, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateKursFixiereInSchieneToggle")
	void testRegelupdateCreateKursFixiereInSchieneToggle() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		// K1 in Schiene 1, keine Fixierung -> wird fixiert
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateKursFixiereInSchieneToggle(Set.of(KURS_ID_1), Set.of(SCHIENE_NR_1));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ,
				Set.of(List.of(KURS_ID_1, (long) SCHIENE_NR_1)));
		assertEquals(0, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateKursSperreInSchieneToggle")
	void testRegelupdateCreateKursSperreInSchieneToggle() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		// K1 in Schiene 1 -> Sperrung toggeln
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateKursSperreInSchieneToggle(Set.of(KURS_ID_1), Set.of(SCHIENE_NR_1));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ,
				Set.of(List.of(KURS_ID_1, (long) SCHIENE_NR_1)));
		assertEquals(0, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateKursSperreInSchieneToggleBeiFixierung")
	void testRegelupdateCreateKursSperreInSchieneToggleBeiFixierung() {
		// K1 ist in Schiene 1 bereits fixiert (nicht gesperrt). Ein Sperr-Toggle soll in diesem Fall
		// KEINE Sperrung anlegen, da die Fixierung Vorrang hat (der Prüfschlüssel ist die Fixierung).
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel fixierung = new GostBlockungRegel();
		fixierung.id = REGEL_ID_1;
		fixierung.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
		fixierung.parameter.add(KURS_ID_1);
		fixierung.parameter.add((long) SCHIENE_NR_1);
		parent.regelAdd(fixierung);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1);
		// K1 in Schiene 1 legen, damit die Ist-Lage existiert.
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));

		final GostBlockungRegelUpdate u = mgr.regelupdateCreateKursSperreInSchieneToggle(Set.of(KURS_ID_1), Set.of(SCHIENE_NR_1));
		assertEquals(0, u.listHinzuzufuegen.size());
		assertEquals(0, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateKursFixiereMengeInIhrenSchienen")
	void testRegelupdateCreateKursFixiereMengeInIhrenSchienen() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
		regel.parameter.add(KURS_ID_1);
		regel.parameter.add((long) SCHIENE_NR_2);
		parent.regelAdd(regel);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1);
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateKursFixiereMengeInIhrenSchienen(Set.of(KURS_ID_1));
		assertEquals(1, u.listHinzuzufuegen.size());
		assertEquals(1, u.listEntfernen.size());
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ,
				Set.of(List.of(KURS_ID_1, (long) SCHIENE_NR_1)));
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ,
				Set.of(List.of(KURS_ID_1, (long) SCHIENE_NR_2)));
	}

	@Test
	@DisplayName("testRegelupdateCreateKursFixiereInSchieneMarkiert")
	void testRegelupdateCreateKursFixiereInSchieneMarkiert() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		// K1 in Schiene 1 -> Fixierung setzen
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateKursFixiereInSchieneMarkiert(Set.of(KURS_ID_1), Set.of(SCHIENE_NR_1));
		// K1 ist in Schiene 1 zugeordnet -> wird fixiert
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ,
				Set.of(List.of(KURS_ID_1, (long) SCHIENE_NR_1)));
		assertEquals(0, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateSchuelerFixierenInDenKursen")
	void testRegelupdateCreateSchuelerFixierenInDenKursen() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		// Alle Schüler in K1 fixieren: S1 ist in K1 -> wird fixiert
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateSchuelerFixierenInDenKursen(Set.of(KURS_ID_1));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ,
				Set.of(List.of(SCHUELER_1_ID, KURS_ID_1)));
		assertEquals(0, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateSchuelerFixierenTypSchriftlich")
	void testRegelupdateCreateSchuelerFixierenTypSchriftlich() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateSchuelerFixierenTypSchriftlich();
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, Set.of());
		assertEquals(0, u.listEntfernen.size()); // keine schriftlichen Fachwahlen
	}

	@Test
	@DisplayName("testRegelupdateCreateSchuelerFixierenTypAb3")
	void testRegelupdateCreateSchuelerFixierenTypAb3() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateSchuelerFixierenTypAb3();
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, Set.of());
		assertEquals(0, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateSchuelerFixierenTypAb4")
	void testRegelupdateCreateSchuelerFixierenTypAb4() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateSchuelerFixierenTypAb4();
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, Set.of());
		assertEquals(0, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateSchuelerFixierenTypLkUndAb3")
	void testRegelupdateCreateSchuelerFixierenTypLkUndAb3() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateSchuelerFixierenTypLkUndAb3();
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, Set.of());
		assertEquals(0, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testRegelupdateRemoveKursSperreInSchiene")
	void testRegelupdateRemoveKursSperreInSchiene() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ;
		regel.parameter.add(KURS_ID_1);
		regel.parameter.add((long) SCHIENE_NR_1);
		parent.regelAdd(regel);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1);
		final GostBlockungRegelUpdate u = mgr.regelupdateRemoveKursSperreInSchiene(Set.of(KURS_ID_1), Set.of(SCHIENE_NR_1));
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ,
				Set.of(List.of(KURS_ID_1, (long) SCHIENE_NR_1)));
		assertEquals(0, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testRegelupdateRemoveSchuelerFixierenInDenKursen")
	void testRegelupdateRemoveSchuelerFixierenInDenKursen() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ;
		regel.parameter.add(SCHUELER_1_ID);
		regel.parameter.add(KURS_ID_1);
		mgr.getParent().regelAdd(regel);
		final GostBlockungRegelUpdate u = mgr.regelupdateRemoveSchuelerFixierenInDenKursen(Set.of(KURS_ID_1));
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ,
				Set.of(List.of(SCHUELER_1_ID, KURS_ID_1)));
		assertEquals(0, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateKursartSperreSchienenVonBis")
	void testRegelupdateCreateKursartSperreSchienenVonBis() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		// GK in Schienen 1 bis 2 sperren
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateKursartSperreSchienenVonBis(KURSART_GK, 1, 2);
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ,
				Set.of(List.of((long) KURSART_GK, 1L, 2L)));
		assertEquals(0, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateSchuelerZusammenMitSchuelerInFach")
	void testRegelupdateCreateSchuelerZusammenMitSchuelerInFach() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateSchuelerZusammenMitSchuelerInFach(SCHUELER_1_ID, SCHUELER_2_ID, FACH_M_ID);
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ,
				Set.of(List.of(SCHUELER_1_ID, SCHUELER_2_ID, FACH_M_ID)));
		assertEquals(0, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateSchuelerVerbietenMitSchuelerInFach")
	void testRegelupdateCreateSchuelerVerbietenMitSchuelerInFach() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateSchuelerVerbietenMitSchuelerInFach(SCHUELER_1_ID, SCHUELER_2_ID, FACH_D_ID);
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ,
				Set.of(List.of(SCHUELER_1_ID, SCHUELER_2_ID, FACH_D_ID)));
		assertEquals(0, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateSchuelerZusammenMitSchueler")
	void testRegelupdateCreateSchuelerZusammenMitSchueler() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateSchuelerZusammenMitSchueler(SCHUELER_1_ID, SCHUELER_2_ID);
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ,
				Set.of(List.of(SCHUELER_1_ID, SCHUELER_2_ID)));
		assertEquals(0, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateSchuelerVerbietenMitSchueler")
	void testRegelupdateCreateSchuelerVerbietenMitSchueler() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateSchuelerVerbietenMitSchueler(SCHUELER_1_ID, SCHUELER_2_ID);
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ,
				Set.of(List.of(SCHUELER_1_ID, SCHUELER_2_ID)));
		assertEquals(0, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateKursartAlleinInSchienenVonBis")
	void testRegelupdateCreateKursartAlleinInSchienenVonBis() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		// LK allein in Schienen 1 bis 2
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateKursartAlleinInSchienenVonBis(KURSART_LK, 1, 2);
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ,
				Set.of(List.of((long) KURSART_LK, 1L, 2L)));
		assertEquals(0, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateKursMaximaleSchueleranzahl")
	void testRegelupdateCreateKursMaximaleSchueleranzahl() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		// Max 20 Schüler für K1
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateKursMaximaleSchueleranzahl(KURS_ID_1, 20);
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ,
				Set.of(List.of(KURS_ID_1, 20L)));
		assertEquals(0, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateKursMaximaleSchueleranzahlGrenzen")
	void testRegelupdateCreateKursMaximaleSchueleranzahlGrenzen() {
		GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(createParentManager(), ERGEBNIS_ID_1 + 801);
		GostBlockungRegelUpdate u = mgr.regelupdateCreateKursMaximaleSchueleranzahl(KURS_ID_1, 0);
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ,
				Set.of(List.of(KURS_ID_1, 0L)));
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ, Set.of());

		mgr = new GostBlockungsergebnisManager(createParentManager(), ERGEBNIS_ID_1 + 802);
		u = mgr.regelupdateCreateKursMaximaleSchueleranzahl(KURS_ID_1, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL_MAX);
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ,
				Set.of(List.of(KURS_ID_1, (long) GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL_MAX)));
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ, Set.of());

		for (final int anzahl : new int[] { -1, 100 }) {
			mgr = new GostBlockungsergebnisManager(createParentManager(), ERGEBNIS_ID_1 + 802 + anzahl);
			u = mgr.regelupdateCreateKursMaximaleSchueleranzahl(KURS_ID_1, anzahl);
			assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ, Set.of());
			assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ, Set.of());
		}

		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel alt = new GostBlockungRegel();
		alt.id = 80101;
		alt.typ = GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ;
		alt.parameter.add(KURS_ID_1);
		alt.parameter.add(20L);
		parent.regelAdd(alt);
		mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 803);
		u = mgr.regelupdateCreateKursMaximaleSchueleranzahl(KURS_ID_1, -1);
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ, Set.of());
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ,
				Set.of(List.of(KURS_ID_1, 20L)));

		final GostBlockungsdatenManager parentZero = createParentManager();
		final GostBlockungRegel altZero = new GostBlockungRegel();
		altZero.id = 80102;
		altZero.typ = GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ;
		altZero.parameter.add(KURS_ID_1);
		altZero.parameter.add(20L);
		parentZero.regelAdd(altZero);
		mgr = new GostBlockungsergebnisManager(parentZero, ERGEBNIS_ID_1 + 804);
		u = mgr.regelupdateCreateKursMaximaleSchueleranzahl(KURS_ID_1, 0);
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ,
				Set.of(List.of(KURS_ID_1, 0L)));
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ,
				Set.of(List.of(KURS_ID_1, 20L)));
	}

	@Test
	@DisplayName("testRegelupdateCreateKursMitDummySusAuffuellenGrenzen")
	void testRegelupdateCreateKursMitDummySusAuffuellenGrenzen() {
		GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(createParentManager(), ERGEBNIS_ID_1 + 804);
		GostBlockungRegelUpdate u = mgr.regelupdateCreateKursMitDummySusAuffuellen(KURS_ID_1, 1);
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ,
				Set.of(List.of(KURS_ID_1, 1L)));
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ, Set.of());

		for (final int anzahl : new int[] { 0, -1 }) {
			mgr = new GostBlockungsergebnisManager(createParentManager(), ERGEBNIS_ID_1 + 805 + anzahl);
			u = mgr.regelupdateCreateKursMitDummySusAuffuellen(KURS_ID_1, anzahl);
			assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ, Set.of());
			assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ, Set.of());
		}

		final GostBlockungsdatenManager parent = createParentManager();
		parent.regelAdd(createRegelDummySUS(KURS_ID_1, 5));
		mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 806);
		u = mgr.regelupdateCreateKursMitDummySusAuffuellen(KURS_ID_1, 0);
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ, Set.of());
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ,
				Set.of(List.of(KURS_ID_1, 5L)));
	}

	@Test
	@DisplayName("testRegelupdateCreateKursartBereicheNormalisiert")
	void testRegelupdateCreateKursartBereicheNormalisiert() {
		GostBlockungsdatenManager parent = createParentManager();
		GostBlockungRegel alt = new GostBlockungRegel();
		alt.id = 80701;
		alt.typ = GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ;
		alt.parameter.add((long) KURSART_GK);
		alt.parameter.add(3L);
		alt.parameter.add(1L);
		parent.regelAdd(alt);
		GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 807);
		GostBlockungRegelUpdate u = mgr.regelupdateCreateKursartSperreSchienenVonBis(KURSART_GK, 3, 1);
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ,
				Set.of(List.of((long) KURSART_GK, 1L, 3L)));
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ,
				Set.of());

		parent = createParentManager();
		alt = new GostBlockungRegel();
		alt.id = 80702;
		alt.typ = GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ;
		alt.parameter.add((long) KURSART_LK);
		alt.parameter.add(3L);
		alt.parameter.add(1L);
		parent.regelAdd(alt);
		mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 808);
		u = mgr.regelupdateCreateKursartAlleinInSchienenVonBis(KURSART_LK, 3, 1);
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ,
				Set.of(List.of((long) KURSART_LK, 1L, 3L)));
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ,
				Set.of());
	}

	@Test
	@DisplayName("testRegelupdateCreateKursFixiereInSchieneToggleNoOpUndKonflikt")
	void testRegelupdateCreateKursFixiereInSchieneToggleNoOpUndKonflikt() {
		GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungRegel ziel = new GostBlockungRegel();
		ziel.id = 80901;
		ziel.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
		ziel.parameter.add(KURS_ID_1);
		ziel.parameter.add((long) SCHIENE_NR_1);
		mgr.getParent().regelAdd(ziel);
		GostBlockungRegelUpdate u = mgr.regelupdateCreateKursFixiereInSchieneToggle(Set.of(KURS_ID_1), Set.of(SCHIENE_NR_1));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ, Set.of());
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ,
				Set.of(List.of(KURS_ID_1, (long) SCHIENE_NR_1)));

		mgr = createStandardManager();
		final GostBlockungRegel konflikt = new GostBlockungRegel();
		konflikt.id = 80902;
		konflikt.typ = GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ;
		konflikt.parameter.add(KURS_ID_1);
		konflikt.parameter.add((long) SCHIENE_NR_1);
		mgr.getParent().regelAdd(konflikt);
		u = mgr.regelupdateCreateKursFixiereInSchieneToggle(Set.of(KURS_ID_1), Set.of(SCHIENE_NR_1));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ,
				Set.of(List.of(KURS_ID_1, (long) SCHIENE_NR_1)));
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ,
				Set.of(List.of(KURS_ID_1, (long) SCHIENE_NR_1)));
	}

	@Test
	@DisplayName("testRegelupdateCreateKursSperreInSchieneLeereUndDoppelteMengen")
	void testRegelupdateCreateKursSperreInSchieneLeereUndDoppelteMengen() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		GostBlockungRegelUpdate u = mgr.regelupdateCreateKursSperreInSchiene(Set.of(), Set.of());
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, Set.of());
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, Set.of());

		final Set<Long> kurse = new HashSet<>(List.of(KURS_ID_1, KURS_ID_1));
		final Set<Integer> schienen = new HashSet<>(List.of(SCHIENE_NR_2, SCHIENE_NR_2));
		u = mgr.regelupdateCreateKursSperreInSchiene(kurse, schienen);
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ,
				Set.of(List.of(KURS_ID_1, (long) SCHIENE_NR_2)));
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ, Set.of());
	}

	@Test
	@DisplayName("testRegelupdateCreateSchuelerIgnorieren")
	void testRegelupdateCreateSchuelerIgnorieren() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateSchuelerIgnorieren(Set.of(SCHUELER_1_ID));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.SCHUELER_IGNORIEREN.typ,
				Set.of(List.of(SCHUELER_1_ID)));
		assertEquals(0, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateFachKursartMaximaleAnzahlProSchiene")
	void testRegelupdateCreateFachKursartMaximaleAnzahlProSchiene() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateFachKursartMaximaleAnzahlProSchiene(FACH_D_ID, KURSART_GK, 2);
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE.typ,
				Set.of(List.of(FACH_D_ID, (long) KURSART_GK, 2L)));
		assertEquals(0, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateFachKursartMaximaleAnzahlProSchieneAusserhalbBereich")
	void testRegelupdateCreateFachKursartMaximaleAnzahlProSchieneAusserhalbBereich() {
		// Das Javadoc nennt den gültigen Bereich [1;9]. Diese Methode führt jedoch KEINE Bereichsprüfung
		// durch und erzeugt die Regel auch für Werte außerhalb des Bereichs. Dieser Test dokumentiert
		// das IST-Verhalten (die Regel wird trotzdem hinzugefügt).
		final int unterhalb = GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE_MIN - 1; // 0
		final int oberhalb = GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE_MAX + 1; // 10

		for (final int maximal : new int[] { unterhalb, oberhalb }) {
			final GostBlockungsergebnisManager mgr = createStandardManager();
			final GostBlockungRegelUpdate u = mgr.regelupdateCreateFachKursartMaximaleAnzahlProSchiene(FACH_D_ID, KURSART_GK, maximal);
			assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE.typ,
					Set.of(List.of(FACH_D_ID, (long) KURSART_GK, (long) maximal)));
		}
	}

	@Test
	@DisplayName("testRegelupdateCreateKursKursdifferenzBeiDerVisualisierungIgnorieren")
	void testRegelupdateCreateKursKursdifferenzBeiDerVisualisierungIgnorieren() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateKursKursdifferenzBeiDerVisualisierungIgnorieren(Set.of(KURS_ID_1));
		assertRegelListe(u.listHinzuzufuegen,
				GostKursblockungRegelTyp.KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN.typ,
				Set.of(List.of(KURS_ID_1)));
		assertEquals(0, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateSchuelermengeEntfernen")
	void testRegelupdateCreateSchuelermengeEntfernen() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		// Keine Regeln für diesen Schüler -> leere Liste
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateSchuelermengeEntfernen(Set.of(SCHUELER_1_ID));
		assertNotNull(u);
		assertEquals(0, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateSchuelerFixierenInKurs")
	void testRegelupdateCreateSchuelerFixierenInKurs() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateSchuelerFixierenInKurs(Set.of(SCHUELER_1_ID), Set.of(KURS_ID_1));
		assertNotNull(u);
		assertEquals(1, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateSchuelerFixierenInKursOhneFachwahl")
	void testRegelupdateCreateSchuelerFixierenInKursOhneFachwahl() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		// S2 hat keine Fachwahl für E -> Fixierung in K3 (E-GK1) wird ignoriert
		final GostBlockungRegelUpdate u1 = mgr.regelupdateCreateSchuelerFixierenInKurs(Set.of(SCHUELER_2_ID), Set.of(KURS_ID_3));
		assertNotNull(u1);
		assertEquals(0, u1.listHinzuzufuegen.size());
		// S1 hat M-LK, aber K5 ist M-GK1 -> Fixierung wird ignoriert
		final GostBlockungRegelUpdate u2 = mgr.regelupdateCreateSchuelerFixierenInKurs(Set.of(SCHUELER_1_ID), Set.of(KURS_ID_5));
		assertNotNull(u2);
		assertEquals(0, u2.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateSchuelerFixierenTypLkDerKursmenge")
	void testRegelupdateCreateSchuelerFixierenTypLkDerKursmenge() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateSchuelerFixierenTypLkDerKursmenge(Set.of(KURS_ID_2));
		assertEquals(0, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateSchuelerFixierenTypAb3DerKursmenge")
	void testRegelupdateCreateSchuelerFixierenTypAb3DerKursmenge() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateSchuelerFixierenTypAb3DerKursmenge(Set.of(KURS_ID_2));
		assertEquals(0, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateSchuelerFixierenTypAb4DerKursmenge")
	void testRegelupdateCreateSchuelerFixierenTypAb4DerKursmenge() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateSchuelerFixierenTypAb4DerKursmenge(Set.of(KURS_ID_2));
		assertEquals(0, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateSchuelerFixierenTypLkUndAb3DerKursmenge")
	void testRegelupdateCreateSchuelerFixierenTypLkUndAb3DerKursmenge() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateSchuelerFixierenTypLkUndAb3DerKursmenge(Set.of(KURS_ID_2));
		assertEquals(0, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateSchuelerFixierenTypAbDerKursmenge")
	void testRegelupdateCreateSchuelerFixierenTypAbDerKursmenge() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateSchuelerFixierenTypAbDerKursmenge(Set.of(KURS_ID_2));
		assertEquals(0, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateSchuelerFixierenTypSchriftlichDerKursmenge")
	void testRegelupdateCreateSchuelerFixierenTypSchriftlichDerKursmenge() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateSchuelerFixierenTypSchriftlichDerKursmenge(Set.of(KURS_ID_1));
		assertNotNull(u);
		assertEquals(0, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testRegelupdatePatchByIdKursFixiereInEinerSchiene")
	void testRegelupdatePatchByIdKursFixiereInEinerSchiene() {
		// Setup: erst eine Fixier-Regel hinzufügen, dann patchen
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
		regel.parameter.add(KURS_ID_1);
		regel.parameter.add((long) SCHIENE_NR_1);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 50);
		// Regel patchen: gleiche Parameter -> nichts passiert (Regel existiert bereits)
		final GostBlockungRegelUpdate u = mgr.regelupdatePatchByIdKursFixiereInEinerSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_1);
		assertNotNull(u);
		assertEquals(0, u.listEntfernen.size());
		assertEquals(0, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testRegelupdatePatchByIdKursSperreInSchiene")
	void testRegelupdatePatchByIdKursSperreInSchiene() {
		// Setup: Sperr-Regel hinzufügen
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ;
		regel.parameter.add(KURS_ID_1);
		regel.parameter.add((long) SCHIENE_NR_1);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 60);
		// Patchen auf andere Schiene -> alte Regel entfernt, neue hinzugefügt
		final GostBlockungRegelUpdate u = mgr.regelupdatePatchByIdKursSperreInSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_2);
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ,
				Set.of(List.of(KURS_ID_1, (long) SCHIENE_NR_1)));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ,
				Set.of(List.of(KURS_ID_1, (long) SCHIENE_NR_2)));
	}

	@Test
	@DisplayName("testRegelupdatePatchByIdKursartSperreSchienenVonBis")
	void testRegelupdatePatchByIdKursartSperreSchienenVonBis() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ;
		regel.parameter.add((long) KURSART_GK);
		regel.parameter.add((long) SCHIENE_NR_1);
		regel.parameter.add((long) SCHIENE_NR_2);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 70);
		final GostBlockungRegelUpdate u = mgr.regelupdatePatchByIdKursartSperreSchienenVonBis(REGEL_ID_1, KURSART_GK, 1, 3);
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ,
				Set.of(List.of((long) KURSART_GK, 1L, 2L)));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ,
				Set.of(List.of((long) KURSART_GK, 1L, 3L)));
	}

	@Test
	@DisplayName("testRegelupdatePatchByIdSchuelerFixierenInKurs")
	void testRegelupdatePatchByIdSchuelerFixierenInKurs() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ;
		regel.parameter.add(SCHUELER_1_ID);
		regel.parameter.add(KURS_ID_1);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 80);
		// Gleiche Parameter -> kein Update
		final GostBlockungRegelUpdate u = mgr.regelupdatePatchByIdSchuelerFixierenInKurs(REGEL_ID_1, SCHUELER_1_ID, KURS_ID_1);
		assertEquals(0, u.listEntfernen.size());
		assertEquals(0, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testRegelupdatePatchByIdSchuelerVerbietenInKurs")
	void testRegelupdatePatchByIdSchuelerVerbietenInKurs() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ;
		regel.parameter.add(SCHUELER_1_ID);
		regel.parameter.add(KURS_ID_3);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 90);
		// Wechsel auf anderen Kurs -> entfernen + hinzufügen
		final GostBlockungRegelUpdate u = mgr.regelupdatePatchByIdSchuelerVerbietenInKurs(REGEL_ID_1, SCHUELER_1_ID, KURS_ID_1);
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ,
				Set.of(List.of(SCHUELER_1_ID, KURS_ID_3)));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ,
				Set.of(List.of(SCHUELER_1_ID, KURS_ID_1)));
	}

	@Test
	@DisplayName("testRegelupdatePatchByIdKursVerbietenMitKurs")
	void testRegelupdatePatchByIdKursVerbietenMitKurs() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ;
		regel.parameter.add(KURS_ID_1);
		regel.parameter.add(KURS_ID_3);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 100);
		final GostBlockungRegelUpdate u = mgr.regelupdatePatchByIdKursVerbietenMitKurs(REGEL_ID_1, KURS_ID_1, KURS_ID_2);
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ,
				Set.of(List.of(KURS_ID_1, KURS_ID_3)));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ,
				Set.of(List.of(KURS_ID_1, KURS_ID_2)));
	}

	@Test
	@DisplayName("testRegelupdatePatchByIdKursZusammenMitKurs")
	void testRegelupdatePatchByIdKursZusammenMitKurs() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ;
		regel.parameter.add(KURS_ID_1);
		regel.parameter.add(KURS_ID_3);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 110);
		final GostBlockungRegelUpdate u = mgr.regelupdatePatchByIdKursZusammenMitKurs(REGEL_ID_1, KURS_ID_1, KURS_ID_2);
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ,
				Set.of(List.of(KURS_ID_1, KURS_ID_3)));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ,
				Set.of(List.of(KURS_ID_1, KURS_ID_2)));
	}

	@Test
	@DisplayName("testRegelupdatePatchByIdKursMitDummySusAuffuellen")
	void testRegelupdatePatchByIdKursMitDummySusAuffuellen() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ;
		regel.parameter.add(KURS_ID_1);
		regel.parameter.add(5L);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 120);
		final GostBlockungRegelUpdate u = mgr.regelupdatePatchByIdKursMitDummySusAuffuellen(REGEL_ID_1, KURS_ID_1, 10);
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ,
				Set.of(List.of(KURS_ID_1, 5L)));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ,
				Set.of(List.of(KURS_ID_1, 10L)));
	}

	@Test
	@DisplayName("testRegelupdatePatchByIdKursMaximaleSchueleranzahl")
	void testRegelupdatePatchByIdKursMaximaleSchueleranzahl() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ;
		regel.parameter.add(KURS_ID_1);
		regel.parameter.add(30L);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 130);
		final GostBlockungRegelUpdate u = mgr.regelupdatePatchByIdKursMaximaleSchueleranzahl(REGEL_ID_1, KURS_ID_1, 25);
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ,
				Set.of(List.of(KURS_ID_1, 30L)));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ,
				Set.of(List.of(KURS_ID_1, 25L)));
	}

	@Test
	@DisplayName("testRegelupdateRemoveKursFixiereMengeInIhrenSchienen")
	void testRegelupdateRemoveKursFixiereMengeInIhrenSchienen() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
		regel.parameter.add(KURS_ID_1);
		regel.parameter.add((long) SCHIENE_NR_1);
		parent.regelAdd(regel);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1);
		final GostBlockungRegelUpdate u = mgr.regelupdateRemoveKursFixiereMengeInIhrenSchienen(Set.of(KURS_ID_1));
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ,
				Set.of(List.of(KURS_ID_1, (long) SCHIENE_NR_1)));
		assertEquals(0, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testRegelupdateRemoveKursFixiereInSchieneMarkiert")
	void testRegelupdateRemoveKursFixiereInSchieneMarkiert() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
		regel.parameter.add(KURS_ID_1);
		regel.parameter.add((long) SCHIENE_NR_1);
		parent.regelAdd(regel);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1);
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_2, SCHIENE_ID_2));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));
		final GostBlockungRegelUpdate u = mgr.regelupdateRemoveKursFixiereInSchieneMarkiert(
				Set.of(KURS_ID_1, KURS_ID_2), Set.of(SCHIENE_NR_1));
		assertEquals(1, u.listEntfernen.size());
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ,
				Set.of(List.of(KURS_ID_1, (long) SCHIENE_NR_1)));
		assertEquals(0, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testRegelupdateRemoveSchuelerFixierenInDenKursenToggle")
	void testRegelupdateRemoveSchuelerFixierenInDenKursenToggle() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungRegelUpdate u = mgr.regelupdateRemoveSchuelerFixierenInDenKursenToggle(Set.of(KURS_ID_1));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ,
				Set.of(List.of(SCHUELER_1_ID, KURS_ID_1)));
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, Set.of());
	}

	@Test
	@DisplayName("testGetOfSchuelerNeuzuordnungOhneKurse")
	void testGetOfSchuelerNeuzuordnungOhneKurse() {
		final GostBlockungsdatenManager parentOhne = createParentManagerOhneKurse();
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parentOhne, ERGEBNIS_ID_1);
		final GostBlockungsergebnisKursSchuelerZuordnungUpdate u = mgr.getOfSchuelerNeuzuordnung(SCHUELER_1_ID, false);
		assertEquals(List.of(), u.listEntfernen);
		assertEquals(List.of(), u.listHinzuzufuegen);
	}

	@Test
	@DisplayName("testKursSchuelerUpdateBildeKerngruppen")
	void testKursSchuelerUpdateBildeKerngruppen() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		// K1 und K4 sind beide D-GK -> bilde Kerngruppen
		final GostBlockungsergebnisKursSchuelerZuordnungUpdate u = mgr.kursSchuelerUpdateBildeKerngruppen(KURS_ID_1,
				Set.of(KURS_ID_4), false, false, false);
		assertEquals(1, u.listEntfernen.size());
		assertEquals(1, u.listHinzuzufuegen.size());
		assertTrue(u.listEntfernen.contains(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_1_ID)));
		assertTrue(u.listHinzuzufuegen.contains(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_4, SCHUELER_1_ID)));
		assertEquals(0, u.regelUpdates.listEntfernen.size());
		assertEquals(0, u.regelUpdates.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testRegelupdatePatchByIdKursartAlleinInSchienenVonBis")
	void testRegelupdatePatchByIdKursartAlleinInSchienenVonBis() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ;
		regel.parameter.add((long) KURSART_LK);
		regel.parameter.add((long) SCHIENE_NR_1);
		regel.parameter.add((long) SCHIENE_NR_2);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 140);
		final GostBlockungRegelUpdate u = mgr.regelupdatePatchByIdKursartAlleinInSchienenVonBis(REGEL_ID_1, KURSART_LK, 1, 3);
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ,
				Set.of(List.of((long) KURSART_LK, 1L, 2L)));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ,
				Set.of(List.of((long) KURSART_LK, 1L, 3L)));
	}

	@Test
	@DisplayName("testRegelupdatePatchByIdSchuelerIgnorieren")
	void testRegelupdatePatchByIdSchuelerIgnorieren() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.SCHUELER_IGNORIEREN.typ;
		regel.parameter.add(SCHUELER_1_ID);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 150);
		final GostBlockungRegelUpdate u = mgr.regelupdatePatchByIdSchuelerIgnorieren(REGEL_ID_1, SCHUELER_2_ID);
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.SCHUELER_IGNORIEREN.typ,
				Set.of(List.of(SCHUELER_1_ID)));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.SCHUELER_IGNORIEREN.typ,
				Set.of(List.of(SCHUELER_2_ID)));
	}

	@Test
	@DisplayName("testRegelupdatePatchByIdSchuelerZusammenMitSchuelerInFach")
	void testRegelupdatePatchByIdSchuelerZusammenMitSchuelerInFach() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ;
		regel.parameter.add(SCHUELER_1_ID);
		regel.parameter.add(SCHUELER_2_ID);
		regel.parameter.add(FACH_M_ID);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 160);
		// Patchen auf anderes Fach -> alte Regel weg, neue rein
		final GostBlockungRegelUpdate u = mgr.regelupdatePatchByIdSchuelerZusammenMitSchuelerInFach(REGEL_ID_1, SCHUELER_1_ID,
				SCHUELER_2_ID, FACH_D_ID);
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ,
				Set.of(List.of(SCHUELER_1_ID, SCHUELER_2_ID, FACH_M_ID)));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ,
				Set.of(List.of(SCHUELER_1_ID, SCHUELER_2_ID, FACH_D_ID)));
	}

	@Test
	@DisplayName("testRegelupdatePatchByIdSchuelerVerbietenMitSchuelerInFach")
	void testRegelupdatePatchByIdSchuelerVerbietenMitSchuelerInFach() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ;
		regel.parameter.add(SCHUELER_1_ID);
		regel.parameter.add(SCHUELER_2_ID);
		regel.parameter.add(FACH_M_ID);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 170);
		final GostBlockungRegelUpdate u = mgr.regelupdatePatchByIdSchuelerVerbietenMitSchuelerInFach(REGEL_ID_1, SCHUELER_1_ID,
				SCHUELER_2_ID, FACH_D_ID);
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ,
				Set.of(List.of(SCHUELER_1_ID, SCHUELER_2_ID, FACH_M_ID)));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ,
				Set.of(List.of(SCHUELER_1_ID, SCHUELER_2_ID, FACH_D_ID)));
	}

	@Test
	@DisplayName("testRegelupdatePatchByIdSchuelerZusammenMitSchueler")
	void testRegelupdatePatchByIdSchuelerZusammenMitSchueler() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ;
		regel.parameter.add(SCHUELER_1_ID);
		regel.parameter.add(SCHUELER_2_ID);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 180);
		// Gleiche Parameter -> nichts passiert
		final GostBlockungRegelUpdate u = mgr.regelupdatePatchByIdSchuelerZusammenMitSchueler(REGEL_ID_1, SCHUELER_1_ID, SCHUELER_2_ID);
		assertNotNull(u);
		assertEquals(0, u.listEntfernen.size());
		assertEquals(0, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testRegelupdatePatchByIdSchuelerVerbietenMitSchueler")
	void testRegelupdatePatchByIdSchuelerVerbietenMitSchueler() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ;
		regel.parameter.add(SCHUELER_1_ID);
		regel.parameter.add(SCHUELER_2_ID);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 190);
		// Wechsel auf andere Schueler-ID -> Regel wird geändert
		final GostBlockungRegelUpdate u = mgr.regelupdatePatchByIdSchuelerVerbietenMitSchueler(REGEL_ID_1, SCHUELER_1_ID, 999L);
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ,
				Set.of(List.of(SCHUELER_1_ID, SCHUELER_2_ID)));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ,
				Set.of(List.of(SCHUELER_1_ID, SCHUELER_NICHT_VORHANDEN)));
	}

	@Test
	@DisplayName("testRegelupdatePatchByIdKursKursdifferenzBeiDerVisualisierungIgnorieren")
	void testRegelupdatePatchByIdKursKursdifferenzBeiDerVisualisierungIgnorieren() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN.typ;
		regel.parameter.add(KURS_ID_1);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 200);
		// Patchen auf anderen Kurs
		final GostBlockungRegelUpdate u = mgr.regelupdatePatchByIdKursKursdifferenzBeiDerVisualisierungIgnorieren(REGEL_ID_1, KURS_ID_2);
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN.typ,
				Set.of(List.of(KURS_ID_1)));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN.typ,
				Set.of(List.of(KURS_ID_2)));
	}

	// #########################################################################
	// ##########       Coverage-Verbesserungen                        ##########
	// #########################################################################

	@Test
	@DisplayName("testGetOfSchuelerOfKursFachwahlMitUngueltigerZuordnung")
	void testGetOfSchuelerOfKursFachwahlMitUngueltigerZuordnung() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// S2 bekommt keine Fachwahl für Fach E -> parent wirft Exception
		assertThrows(DeveloperNotificationException.class,
				() -> mgr.getOfSchuelerOfKursFachwahl(SCHUELER_2_ID, KURS_ID_3));
	}

	@Test
	@DisplayName("testGetOfSchuelerAnzahlGefiltertMitSubstringUndGeschlecht")
	void testGetOfSchuelerAnzahlGefiltertMitSubstringUndGeschlecht() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// Filter nach Substring "Test2" -> nur S2
		assertEquals(1, mgr.getOfSchuelerAnzahlGefiltert(-1, -1, -1, 0, "Test2", null, null));

		// Filter nach Geschlecht (S1=W, S2=W -> beide W -> 2)
		assertEquals(2, mgr.getOfSchuelerAnzahlGefiltert(-1, -1, -1, 0, "", Geschlecht.W, null));

		// Filter nach Geschlecht M -> keiner
		assertEquals(0, mgr.getOfSchuelerAnzahlGefiltert(-1, -1, -1, 0, "", Geschlecht.M, null));
	}

	@Test
	@DisplayName("testGetOfSchuelerAnzahlGefiltertMitKursUndFach")
	void testGetOfSchuelerAnzahlGefiltertMitKursUndFach() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// Filter nach K1: nur S1 ist in K1
		assertEquals(1, mgr.getOfSchuelerAnzahlGefiltert(KURS_ID_1, -1, -1, 0, "", null, null));

		// Filter nach Fach D und Kursart GK -> S1 und S2 haben D-GK -> 2
		assertEquals(2, mgr.getOfSchuelerAnzahlGefiltert(-1, FACH_D_ID, KURSART_GK, 0, "", null, null));

		// Filter nach Fach D und Kursart LK -> keiner hat D-LK -> 0
		assertEquals(0, mgr.getOfSchuelerAnzahlGefiltert(-1, FACH_D_ID, KURSART_LK, 0, "", null, null));
	}

	@Test
	@DisplayName("testGetOfSchuelerErfuelltNachfilterKriterien")
	void testGetOfSchuelerErfuelltNachfilterKriterien() {
		final GostBlockungsergebnisManager mgr = createStandardManager();

		// Variante: idKurs < 0 und idFach < 0 --> alle Schüler bestehen, da kein Fach-Kriterium
		assertEquals(2, mgr.getOfSchuelerAnzahlGefiltert(-1, -1, -1, 0, "", null, GostSchriftlichkeit.MUENDLICH));
		assertEquals(2, mgr.getOfSchuelerAnzahlGefiltert(-1, -1, -1, 0, "", null, GostSchriftlichkeit.SCHRIFTLICH));

		// Variante: idKurs >= 0 und gültige Zuordnung, Schriftlichkeit stimmt überein --> passt
		// S1 ist in K1 (D-GK1) mit D-GK (nicht schriftlich) --> MUENDLICH passt
		assertEquals(1, mgr.getOfSchuelerAnzahlGefiltert(KURS_ID_1, -1, -1, 0, "", null, GostSchriftlichkeit.MUENDLICH));

		// Variante: idFach >= 0 (ohne Kurs), Schriftlichkeit stimmt überein --> passt
		// S1 und S2 haben D-GK (nicht schriftlich) --> MUENDLICH passt
		assertEquals(2, mgr.getOfSchuelerAnzahlGefiltert(-1, FACH_D_ID, -1, 0, "", null, GostSchriftlichkeit.MUENDLICH));

		// Variante: idKurs >= 0 und idFach >= 0 --> der Kurs-Filter gewinnt (K1 ist Fach D, nicht Fach M)
		assertEquals(1, mgr.getOfSchuelerAnzahlGefiltert(KURS_ID_1, FACH_M_ID, -1, 0, "", null, GostSchriftlichkeit.MUENDLICH));

		// Variante: geschlecht und schriftlichkeit gleichzeitig --> beide Kriterien müssen gelten
		assertEquals(2, mgr.getOfSchuelerAnzahlGefiltert(-1, -1, -1, 0, "", Geschlecht.W, GostSchriftlichkeit.MUENDLICH));
		assertEquals(0, mgr.getOfSchuelerAnzahlGefiltert(-1, -1, -1, 0, "", Geschlecht.M, GostSchriftlichkeit.MUENDLICH));
	}

	@Test
	@DisplayName("testGetOfSchuelerErfuelltNachfilterKriterienUngueltigeZuordnung")
	void testGetOfSchuelerErfuelltNachfilterKriterienUngueltigeZuordnung() {
		// S2 ohne E-Fachwahl in K3 (E-GK1) legen --> ungültige Zuordnung
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> set = new HashSet<>();
		set.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_3, SCHUELER_2_ID));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(set));
		assertTrue(mgr.getOfSchuelerOfKursIstUngueltig(SCHUELER_2_ID, KURS_ID_3));

		// Ungültige Zuordnung + SCHRIFTLICH --> wird abgelehnt (S2), S1 (gültig, nicht schriftlich) auch --> 0
		assertEquals(0, mgr.getOfSchuelerAnzahlGefiltert(KURS_ID_3, -1, -1, 0, "", null, GostSchriftlichkeit.SCHRIFTLICH));

		// Ungültige Zuordnung + MUENDLICH --> wird akzeptiert (S2), S1 (gültig, nicht schriftlich) auch --> 2
		assertEquals(2, mgr.getOfSchuelerAnzahlGefiltert(KURS_ID_3, -1, -1, 0, "", null, GostSchriftlichkeit.MUENDLICH));
	}

	@Test
	@DisplayName("testGetOfSchuelerErfuelltNachfilterKriterienSchriftlicheFachwahl")
	void testGetOfSchuelerErfuelltNachfilterKriterienSchriftlicheFachwahl() {
		// S1: D-GK als schriftlich markieren
		final GostBlockungsdatenManager parent = createParentManager();
		for (final GostFachwahl fw : parent.schuelerGetListeOfFachwahlen(SCHUELER_1_ID)) {
			if (fw.fachID == FACH_D_ID) {
				fw.istSchriftlich = true;
			}
		}
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1);

		// Kurse in Schienen legen (wie im Standard-Manager)
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_4, SCHIENE_ID_3));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));

		// Schüler in Kurse legen (S1 --> K1, S2 --> K4)
		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> s = new HashSet<>();
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_1_ID));
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_4, SCHUELER_2_ID));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(s));

		// Fach-Filter: S1 (D schriftlich) passt zu SCHRIFTLICH, S2 (D nicht schriftlich) nicht --> 1
		assertEquals(1, mgr.getOfSchuelerAnzahlGefiltert(-1, FACH_D_ID, -1, 0, "", null, GostSchriftlichkeit.SCHRIFTLICH));

		// Kurs-Filter K1 (D-GK1): nur S1 ist zugeordnet und D ist schriftlich --> passt --> 1
		assertEquals(1, mgr.getOfSchuelerAnzahlGefiltert(KURS_ID_1, -1, -1, 0, "", null, GostSchriftlichkeit.SCHRIFTLICH));

		// Kurs-Filter K4 (D-GK2): nur S2 ist zugeordnet und D ist nicht schriftlich --> MUENDLICH passt --> 1
		assertEquals(1, mgr.getOfSchuelerAnzahlGefiltert(KURS_ID_4, -1, -1, 0, "", null, GostSchriftlichkeit.MUENDLICH));
	}

	@Test
	@DisplayName("testGetOfSchuelerAnzahlGefiltertKursUndFachSchriftlichkeit")
	void testGetOfSchuelerAnzahlGefiltertKursUndFachSchriftlichkeit() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		// Die M-LK-Fachwahlen von S1 und S2 werden nachträglich als schriftlich markiert.
		mgr.getParent().schuelerGetOfFachFachwahl(SCHUELER_1_ID, FACH_M_ID).istSchriftlich = true;
		mgr.getParent().schuelerGetOfFachFachwahl(SCHUELER_2_ID, FACH_M_ID).istSchriftlich = true;

		// Kombinierter Filter (Kurs K1 = D-GK1 mündlich, Fach M = schriftlich): Sind Kurs- und Fach-Filter
		// gleichzeitig gesetzt, müssen BEIDE Schriftlichkeiten passen --> MUENDLICH passt nur zum Kurs, nicht zum Fach --> 0
		assertEquals(0, mgr.getOfSchuelerAnzahlGefiltert(KURS_ID_1, FACH_M_ID, -1, 0, "", null, GostSchriftlichkeit.MUENDLICH));

		// Nur Kurs-Filter K1 (D-GK1, mündlich) --> 1
		assertEquals(1, mgr.getOfSchuelerAnzahlGefiltert(KURS_ID_1, -1, -1, 0, "", null, GostSchriftlichkeit.MUENDLICH));

		// Nur Fach-Filter M (schriftlich) --> MUENDLICH passt nicht --> 0
		assertEquals(0, mgr.getOfSchuelerAnzahlGefiltert(-1, FACH_M_ID, -1, 0, "", null, GostSchriftlichkeit.MUENDLICH));
	}

	@Test
	@DisplayName("testGetOfSchuelerAnzahlGefiltertGeschlechtVorKursfilter")
	void testGetOfSchuelerAnzahlGefiltertGeschlechtVorKursfilter() {
		final GostBlockungsdatenManager parent = createParentManager();
		// S2 erhält ein ungültiges Geschlecht.
		parent.schuelerGet(SCHUELER_2_ID).geschlecht = 0;
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1);
		// S2 ist NICHT in K1. Da das Geschlechts-Kriterium VOR dem Kurs-Kriterium geprüft wird,
		// muss trotzdem eine Exception geworfen werden.
		assertThrows(DeveloperNotificationException.class,
				() -> mgr.getOfSchuelerAnzahlGefiltert(KURS_ID_1, -1, -1, 0, "", Geschlecht.W, null));
	}

	@Test
	@DisplayName("testGetOfSchuelerOfKursAnzahlZusammenWuenscheMitRegel")
	void testGetOfSchuelerOfKursAnzahlZusammenWuenscheMitRegel() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		// S1 ist mit S2 in K2 ohne Regel -> 0
		assertEquals(0, mgr.getOfSchuelerOfKursAnzahlZusammenWuensche(SCHUELER_1_ID, KURS_ID_2));

		// Auch für sich selbst: 0
		assertEquals(0, mgr.getOfSchuelerOfKursAnzahlZusammenWuensche(SCHUELER_2_ID, KURS_ID_2));
	}

	@Test
	@DisplayName("testGetOfSchuelerOfKursAnzahlVerbotenWuenscheMitRegel")
	void testGetOfSchuelerOfKursAnzahlVerbotenWuenscheMitRegel() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		// Keine Verboten-Regel -> 0
		assertEquals(0, mgr.getOfSchuelerOfKursAnzahlVerbotenWuensche(SCHUELER_1_ID, KURS_ID_2));
		assertEquals(0, mgr.getOfSchuelerOfKursAnzahlVerbotenWuensche(SCHUELER_2_ID, KURS_ID_2));
	}

	@Test
	@DisplayName("testGetOfSchuelerOfKursHatKollisionOhneKollision")
	void testGetOfSchuelerOfKursHatKollisionOhneKollision() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		// S2 hat keine Kollision -> Fast-Return greift
		assertFalse(mgr.getOfSchuelerOfKursHatKollision(SCHUELER_2_ID, KURS_ID_2));
	}

	@Test
	@DisplayName("testStateRegelvalidierung1Erfuellt")
	void testStateRegelvalidierung1Erfuellt() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 501);
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(Set.of(
				DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_3))));
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ;
		regel.parameter.add((long) KURSART_GK);
		regel.parameter.add((long) SCHIENE_NR_1);
		regel.parameter.add((long) SCHIENE_NR_2);
		parent.regelAdd(regel);
		mgr.stateRevalidateEverything();
		assertFalse(mgr.getErgebnis().bewertung.regelVerletzungen.contains(REGEL_ID_1));
	}

	@Test
	@DisplayName("testStateRegelvalidierung2Erfuellt")
	void testStateRegelvalidierung2Erfuellt() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 502);
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(Set.of(
				DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_2))));
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
		regel.parameter.add(KURS_ID_1);
		regel.parameter.add((long) SCHIENE_NR_2);
		parent.regelAdd(regel);
		mgr.stateRevalidateEverything();
		assertFalse(mgr.getErgebnis().bewertung.regelVerletzungen.contains(REGEL_ID_1));
	}

	@Test
	@DisplayName("testStateRegelvalidierung3Erfuellt")
	void testStateRegelvalidierung3Erfuellt() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 503);
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(Set.of(
				DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_2))));
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ;
		regel.parameter.add(KURS_ID_1);
		regel.parameter.add((long) SCHIENE_NR_1);
		parent.regelAdd(regel);
		mgr.stateRevalidateEverything();
		assertFalse(mgr.getErgebnis().bewertung.regelVerletzungen.contains(REGEL_ID_1));
	}

	@Test
	@DisplayName("testStateRegelvalidierung4Erfuellt")
	void testStateRegelvalidierung4Erfuellt() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 504);
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(Set.of(
				DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1))));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(Set.of(
				DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_1_ID))));
		assertTrue(mgr.getOfSchuelerOfKursIstZugeordnet(SCHUELER_1_ID, KURS_ID_1));
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ;
		regel.parameter.add(SCHUELER_1_ID);
		regel.parameter.add(KURS_ID_1);
		parent.regelAdd(regel);
		mgr.stateRevalidateEverything();
		assertFalse(mgr.getErgebnis().bewertung.regelVerletzungen.contains(REGEL_ID_1));
	}

	@Test
	@DisplayName("testStateRegelvalidierung5Erfuellt")
	void testStateRegelvalidierung5Erfuellt() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 505);
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(Set.of(
				DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_2, SCHUELER_1_ID))));
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ;
		regel.parameter.add(SCHUELER_1_ID);
		regel.parameter.add(KURS_ID_1);
		parent.regelAdd(regel);
		mgr.stateRevalidateEverything();
		assertFalse(mgr.getErgebnis().bewertung.regelVerletzungen.contains(REGEL_ID_1));
	}

	@Test
	@DisplayName("testStateRegelvalidierung6Erfuellt")
	void testStateRegelvalidierung6Erfuellt() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 506);
		final Set<GostBlockungsergebnisKursSchienenZuordnung> z = Set.of(
				DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_2, SCHIENE_ID_1),
				DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_3),
				DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_3, SCHIENE_ID_3),
				DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_4, SCHIENE_ID_3),
				DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_5, SCHIENE_ID_3));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ;
		regel.parameter.add((long) KURSART_LK);
		regel.parameter.add((long) SCHIENE_NR_1);
		regel.parameter.add((long) SCHIENE_NR_2);
		parent.regelAdd(regel);
		mgr.stateRevalidateEverything();
		assertFalse(mgr.getErgebnis().bewertung.regelVerletzungen.contains(REGEL_ID_1));
	}

	@Test
	@DisplayName("testStateRegelvalidierung7Erfuellt")
	void testStateRegelvalidierung7Erfuellt() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 507);
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(Set.of(
				DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1),
				DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_3, SCHIENE_ID_2))));
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ;
		regel.parameter.add(KURS_ID_1);
		regel.parameter.add(KURS_ID_3);
		parent.regelAdd(regel);
		mgr.stateRevalidateEverything();
		assertFalse(mgr.getErgebnis().bewertung.regelVerletzungen.contains(REGEL_ID_1));
	}

	@Test
	@DisplayName("testStateRegelvalidierung8Erfuellt")
	void testStateRegelvalidierung8Erfuellt() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 508);
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(Set.of(
				DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1),
				DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_3, SCHIENE_ID_1))));
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ;
		regel.parameter.add(KURS_ID_1);
		regel.parameter.add(KURS_ID_3);
		parent.regelAdd(regel);
		mgr.stateRevalidateEverything();
		assertFalse(mgr.getErgebnis().bewertung.regelVerletzungen.contains(REGEL_ID_1));
	}

	@Test
	@DisplayName("testStateRegelvalidierung10Erfuellt")
	void testStateRegelvalidierung10Erfuellt() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungKursLehrer lehrer = new GostBlockungKursLehrer();
		lehrer.id = LEHRER_ID_1;
		lehrer.reihenfolge = 1;
		lehrer.kuerzel = "L";
		parent.kursAddLehrkraft(KURS_ID_1, lehrer);
		parent.kursAddLehrkraft(KURS_ID_3, lehrer);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 510);
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(Set.of(
				DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1),
				DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_3, SCHIENE_ID_2))));
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN.typ;
		parent.regelAdd(regel);
		mgr.stateRevalidateEverything();
		assertFalse(mgr.getErgebnis().bewertung.regelVerletzungen.contains(REGEL_ID_1));
	}

	@Test
	@DisplayName("testStateRegelvalidierung11Erfuellt")
	void testStateRegelvalidierung11Erfuellt() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 511);
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(Set.of(
				DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_2, SCHIENE_ID_1))));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(Set.of(
				DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_2, SCHUELER_1_ID),
				DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_2, SCHUELER_2_ID))));
		assertTrue(parent.schuelerGetHatFachart(SCHUELER_1_ID, FACH_M_ID, KURSART_LK));
		assertTrue(parent.schuelerGetHatFachart(SCHUELER_2_ID, FACH_M_ID, KURSART_LK));
		assertEquals(KURS_ID_2, mgr.getOfSchuelerOfFachZugeordneterKurs(SCHUELER_1_ID, FACH_M_ID).id);
		assertEquals(KURS_ID_2, mgr.getOfSchuelerOfFachZugeordneterKurs(SCHUELER_2_ID, FACH_M_ID).id);
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ;
		regel.parameter.add(SCHUELER_1_ID);
		regel.parameter.add(SCHUELER_2_ID);
		regel.parameter.add(FACH_M_ID);
		parent.regelAdd(regel);
		mgr.stateRevalidateEverything();
		assertFalse(mgr.getErgebnis().bewertung.regelVerletzungen.contains(REGEL_ID_1));
	}

	@Test
	@DisplayName("testStateRegelvalidierung12Erfuellt")
	void testStateRegelvalidierung12Erfuellt() {
		final GostBlockungsdatenManager parent = createParentManager();
		parent.kursAdd(createKurs(6, FACH_M_ID, KURSART_LK, 2));
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 512);
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(Set.of(
				DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_2, SCHUELER_1_ID),
				DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(6, SCHUELER_2_ID))));
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ;
		regel.parameter.add(SCHUELER_1_ID);
		regel.parameter.add(SCHUELER_2_ID);
		regel.parameter.add(FACH_M_ID);
		parent.regelAdd(regel);
		mgr.stateRevalidateEverything();
		assertFalse(mgr.getErgebnis().bewertung.regelVerletzungen.contains(REGEL_ID_1));
	}

	@Test
	@DisplayName("testStateRegelvalidierung14Erfuellt")
	void testStateRegelvalidierung14Erfuellt() {
		final GostBlockungsdatenManager parent = createParentManager();
		parent.kursAdd(createKurs(6, FACH_M_ID, KURSART_LK, 2));
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 514);
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(Set.of(
				DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_1_ID),
				DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_2, SCHUELER_1_ID),
				DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_4, SCHUELER_2_ID),
				DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(6, SCHUELER_2_ID))));
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ;
		regel.parameter.add(SCHUELER_1_ID);
		regel.parameter.add(SCHUELER_2_ID);
		parent.regelAdd(regel);
		mgr.stateRevalidateEverything();
		assertFalse(mgr.getErgebnis().bewertung.regelVerletzungen.contains(REGEL_ID_1));
	}

	@Test
	@DisplayName("testStateRegelvalidierung15Erfuellt")
	void testStateRegelvalidierung15Erfuellt() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 515);
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(Set.of(
				DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_1_ID))));
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ;
		regel.parameter.add(KURS_ID_1);
		regel.parameter.add(1L);
		parent.regelAdd(regel);
		mgr.stateRevalidateEverything();
		assertFalse(mgr.getErgebnis().bewertung.regelVerletzungen.contains(REGEL_ID_1));
	}

	@Test
	@DisplayName("testStateRegelvalidierung18Erfuellt")
	void testStateRegelvalidierung18Erfuellt() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 518);
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(Set.of(
				DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1),
				DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_4, SCHIENE_ID_2))));
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE.typ;
		regel.parameter.add(FACH_D_ID);
		regel.parameter.add((long) KURSART_GK);
		regel.parameter.add(1L);
		parent.regelAdd(regel);
		mgr.stateRevalidateEverything();
		assertFalse(mgr.getErgebnis().bewertung.regelVerletzungen.contains(REGEL_ID_1));
	}

	@Test
	@DisplayName("testStateRegelvalidierung10LehrkraefteBeachten")
	void testStateRegelvalidierung10LehrkraefteBeachten() {
		final GostBlockungsdatenManager parent = createParentManager();
		// Gleiche Lehrkraft zu K1, K2, K3, K4 hinzufügen
		final GostBlockungKursLehrer lehrer = new GostBlockungKursLehrer();
		lehrer.id = LEHRER_ID_1;
		lehrer.reihenfolge = 1;
		lehrer.kuerzel = "L";
		parent.kursAddLehrkraft(KURS_ID_1, lehrer);
		parent.kursAddLehrkraft(KURS_ID_2, lehrer);
		parent.kursAddLehrkraft(KURS_ID_3, lehrer);
		parent.kursAddLehrkraft(KURS_ID_4, lehrer);
		// LEHRKRAEFTE_BEACHTEN Regel hinzufügen
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN.typ;
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 500);
		// Vier Kurse in Schiene 1
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_2, SCHIENE_ID_1));
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_3, SCHIENE_ID_1));
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_4, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));

		// stateRevalidateEverything führt stateRegelvalidierung10 aus
		mgr.stateRevalidateEverything();

		// Vier Kurse in Schiene 1 --> die gleiche Lehrkraft in allen --> quadratische Regelverletzungen
		// Bei 4 Kursen entstehen C(4,2) = 3+2+1 = 6 Regelverletzungen (eine pro Kurs-Paar)
		final List<Long> regelVerletzungen = mgr.getErgebnis().bewertung.regelVerletzungen;
		assertTrue(regelVerletzungen.contains(REGEL_ID_1));
		final long anzahlVerletzungen = regelVerletzungen.stream().filter(id -> id == REGEL_ID_1).count();
		assertEquals(6, anzahlVerletzungen);
		final Set<String> erwarteteKursPaare = Set.of(
				"D-GK1|M-LK1",
				"D-GK1|E-GK1",
				"E-GK1|M-LK1",
				"D-GK1|D-GK2",
				"D-GK2|M-LK1",
				"D-GK2|E-GK1"
		);
		final Set<String> tatsaechlicheKursPaare = new HashSet<>();
		for (final String zeile : mgr.regelGetTooltipFuerRegelverletzungen().split("\\R")) {
			final String beschreibung = zeile.trim();
			if (beschreibung.startsWith("Kurs ")) {
				assertTrue(beschreibung.contains(" haben die Lehrkraft L in der selben Schiene (1)."));
				final String[] kurse = beschreibung.substring("Kurs ".length(), beschreibung.indexOf(" haben die Lehrkraft"))
						.split(" und Kurs ", -1);
				assertEquals(2, kurse.length, beschreibung);
				Arrays.sort(kurse);
				tatsaechlicheKursPaare.add(kurse[0] + "|" + kurse[1]);
			}
		}
		assertEquals(erwarteteKursPaare, tatsaechlicheKursPaare);
	}

	@Test
	@DisplayName("testStateRegelvalidierung18FachartMaxProSchiene")
	void testStateRegelvalidierung18FachartMaxProSchiene() {
		final GostBlockungsdatenManager parent = createParentManager();
		// Regel: D-GK maximal 1x pro Schiene
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE.typ;
		regel.parameter.add(FACH_D_ID);
		regel.parameter.add((long) KURSART_GK);
		regel.parameter.add(1L); // max 1
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 600);
		// K1 (D-GK1) und K4 (D-GK2) beide in Schiene 1 -> 2x D-GK -> Verletzung
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_4, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));

		mgr.stateRevalidateEverything();
		final List<Long> regelVerletzungen = mgr.getErgebnis().bewertung.regelVerletzungen;
		assertTrue(regelVerletzungen.contains(REGEL_ID_1));
		assertEquals(1, regelVerletzungen.stream().filter(id -> id == REGEL_ID_1).count());
		final String beschreibung = mgr.regelGetMapRegelIdToVerletzungString().get(REGEL_ID_1);
		assertNotNull(beschreibung);
		assertTrue(beschreibung.contains("Schiene Nr. 1"));
		assertTrue(beschreibung.contains("Fachart D-GK"));
		assertTrue(beschreibung.contains("insgesamt 2 Mal"));
		assertTrue(beschreibung.contains("erlaubt sind aber nur 1"));
	}

	@Test
	@DisplayName("testConstructorMitErgebnis")
	void testConstructorMitErgebnis() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungsergebnis ergebnis = new GostBlockungsergebnis();
		ergebnis.id = ERGEBNIS_ID_1 + 700;
		ergebnis.name = "Vorhandenes Ergebnis";
		ergebnis.istAktiv = true;
		ergebnis.blockungID = 999;
		ergebnis.gostHalbjahr = GostHalbjahr.Q22.id;
		final GostBlockungsergebnisSchiene resultSchiene = new GostBlockungsergebnisSchiene();
		resultSchiene.id = SCHIENE_ID_2;
		final GostBlockungsergebnisKurs resultKurs = new GostBlockungsergebnisKurs();
		resultKurs.id = KURS_ID_1;
		resultKurs.fachID = FACH_D_ID;
		resultKurs.kursart = KURSART_GK;
		resultKurs.anzahlSchienen = 1;
		resultKurs.schienen.add(SCHIENE_ID_2);
		resultKurs.schueler.add(SCHUELER_1_ID);
		resultSchiene.kurse.add(resultKurs);
		ergebnis.schienen.add(resultSchiene);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ergebnis);
		assertSame(ergebnis, mgr.getErgebnis());
		assertEquals(resultKurs, mgr.getKursE(KURS_ID_1));
		assertEquals(List.of(SCHUELER_1_ID), mgr.getKursE(KURS_ID_1).schueler);
		assertEquals(Set.of(SCHUELER_1_ID), mgr.getMappingKursIDSchuelerIDs().get(KURS_ID_1));
		assertEquals(List.of(SCHUELER_1_ID), mgr.getOfKursSchuelermenge(KURS_ID_1).stream().map(s -> s.id).toList());
		assertEquals(Set.of(SCHIENE_ID_2), Set.copyOf(mgr.getKursE(KURS_ID_1).schienen));
		assertEquals(Set.of(SCHIENE_ID_2), mgr.getOfKursSchienenmenge(KURS_ID_1).stream().map(s -> s.id).collect(java.util.stream.Collectors.toSet()));
		assertEquals(Set.of(SCHIENE_ID_2), mgr.getMappingKursIDSchienenmenge().get(KURS_ID_1).stream()
				.map(s -> s.id).collect(java.util.stream.Collectors.toSet()));
		assertEquals(parent.getID(), mgr.getErgebnis().blockungID);
		assertEquals(parent.daten().gostHalbjahr, mgr.getErgebnis().gostHalbjahr);
		assertEquals("Vorhandenes Ergebnis", mgr.getErgebnis().name);
		assertTrue(mgr.getErgebnis().istAktiv);
		assertEquals(FACH_D_ID, parent.kursGet(KURS_ID_1).fach_id);
		assertTrue(parent.schuelerGetHatFach(SCHUELER_1_ID, FACH_D_ID));
	}

	@Test
	@DisplayName("testStateRegelvalidierung3KursSperreInSchiene")
	void testStateRegelvalidierung3KursSperreInSchiene() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ;
		regel.parameter.add(KURS_ID_1);
		regel.parameter.add((long) SCHIENE_NR_1);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 800);
		// K1 in Schiene 1 legen -> Regelverletzung da gesperrt
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));

		assertTrue(mgr.getErgebnis().bewertung.regelVerletzungen.contains(REGEL_ID_1));
		assertEquals(1, mgr.getErgebnis().bewertung.regelVerletzungen.stream().filter(id -> id == REGEL_ID_1).count());
		final String beschreibung = mgr.regelGetMapRegelIdToVerletzungString().get(REGEL_ID_1);
		assertNotNull(beschreibung);
		assertTrue(beschreibung.contains("D-GK1"));
		assertTrue(beschreibung.contains("Schiene 1"));
	}

	@Test
	@DisplayName("testStateRegelvalidierung5SchuelerVerbotenInKurs")
	void testStateRegelvalidierung5SchuelerVerbotenInKurs() {
		final GostBlockungsdatenManager parent = createParentManager();
		// Regel: S1 verboten in K1
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ;
		regel.parameter.add(SCHUELER_1_ID);
		regel.parameter.add(KURS_ID_1);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 900);
		// K1 in Schiene 1 legen, dann S1 in K1 -> Regelverletzung
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));

		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> s = new HashSet<>();
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_1_ID));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(s));

		assertTrue(mgr.getErgebnis().bewertung.regelVerletzungen.contains(REGEL_ID_1));
		assertEquals(1, mgr.getErgebnis().bewertung.regelVerletzungen.stream().filter(id -> id == REGEL_ID_1).count());
		final String beschreibung = mgr.regelGetMapRegelIdToVerletzungString().get(REGEL_ID_1);
		assertNotNull(beschreibung);
		assertTrue(beschreibung.contains("D-GK1"));
		assertTrue(beschreibung.contains("Test1, Max"));
		final String tooltip = mgr.regelGetTooltipFuerRegelverletzungen();
		assertTrue(tooltip.contains("1 Regelverletzungen"));
		assertTrue(tooltip.contains("Test1, Max"));
		assertTrue(tooltip.contains("sollte verboten sein in Kurs D-GK1."));
	}

	@Test
	@DisplayName("testStateRegelvalidierung7KursVerbietenMitKurs")
	void testStateRegelvalidierung7KursVerbietenMitKurs() {
		final GostBlockungsdatenManager parent = createParentManager();
		// Regel: K1 verboten mit K3
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ;
		regel.parameter.add(KURS_ID_1);
		regel.parameter.add(KURS_ID_3);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 1000);
		// K1 und K3 beide in Schiene 1 -> Regelverletzung
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_3, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));

		assertTrue(mgr.getErgebnis().bewertung.regelVerletzungen.contains(REGEL_ID_1));
		assertEquals(1, mgr.getErgebnis().bewertung.regelVerletzungen.stream().filter(id -> id == REGEL_ID_1).count());
		final String beschreibung = mgr.regelGetMapRegelIdToVerletzungString().get(REGEL_ID_1);
		assertNotNull(beschreibung);
		assertTrue(beschreibung.contains("D-GK1"));
		assertTrue(beschreibung.contains("(1)"));
	}

	@Test
	@DisplayName("testStateRegelvalidierung8KursZusammenMitKurs")
	void testStateRegelvalidierung8KursZusammenMitKurs() {
		final GostBlockungsdatenManager parent = createParentManager();
		// Regel: K1 zusammen mit K3
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ;
		regel.parameter.add(KURS_ID_1);
		regel.parameter.add(KURS_ID_3);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 1100);
		// K1 in Schiene 1, K3 in Schiene 2 -> nicht gemeinsam -> Verletzung
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_3, SCHIENE_ID_2));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));

		assertTrue(mgr.getErgebnis().bewertung.regelVerletzungen.contains(REGEL_ID_1));
		assertEquals(1, mgr.getErgebnis().bewertung.regelVerletzungen.stream().filter(id -> id == REGEL_ID_1).count());
		final String beschreibung = mgr.regelGetMapRegelIdToVerletzungString().get(REGEL_ID_1);
		assertNotNull(beschreibung);
		assertTrue(beschreibung.contains("D-GK1"));
		assertTrue(beschreibung.contains("sollten gemeinsam in einer Schiene sein."));
	}

	@Test
	@DisplayName("testStateRegelvalidierung6KursartAlleinInSchienenVonBis")
	void testStateRegelvalidierung6KursartAlleinInSchienenVonBis() {
		final GostBlockungsdatenManager parent = createParentManager();
		// Regel: LK allein in Schiene 1 bis 2
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ;
		regel.parameter.add((long) KURSART_LK);
		regel.parameter.add((long) SCHIENE_NR_1);
		regel.parameter.add((long) SCHIENE_NR_2);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 1200);
		// K2 (M-LK1) in Schiene 1 -> ok, LK ist in 1..2
		// K1 (D-GK1) auch in Schiene 1 -> GK in LK-Zone -> Verletzung!
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_2, SCHIENE_ID_1));
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));

		assertTrue(mgr.getErgebnis().bewertung.regelVerletzungen.contains(REGEL_ID_1));
		assertEquals(1, mgr.getErgebnis().bewertung.regelVerletzungen.stream().filter(id -> id == REGEL_ID_1).count());
		final String beschreibung = mgr.regelGetMapRegelIdToVerletzungString().get(REGEL_ID_1);
		assertNotNull(beschreibung);
		assertTrue(beschreibung.contains("D-GK1"));
		assertTrue(beschreibung.contains("Schienen 1 bis 2"));
	}

	@Test
	@DisplayName("testStateRegelvalidierung1KursartSperreSchienenVonBis")
	void testStateRegelvalidierung1KursartSperreSchienenVonBis() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ;
		regel.parameter.add((long) KURSART_GK);
		regel.parameter.add((long) SCHIENE_NR_1);
		regel.parameter.add((long) SCHIENE_NR_2);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 1300);
		// K1 (D-GK1) = GK, in Schiene 1 -> im gesperrten Bereich -> Verletzung
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));

		assertTrue(mgr.getErgebnis().bewertung.regelVerletzungen.contains(REGEL_ID_1));
		assertEquals(1, mgr.getErgebnis().bewertung.regelVerletzungen.stream().filter(id -> id == REGEL_ID_1).count());
		final String beschreibung = mgr.regelGetMapRegelIdToVerletzungString().get(REGEL_ID_1);
		assertNotNull(beschreibung);
		assertTrue(beschreibung.contains("D-GK1"));
		assertTrue(beschreibung.contains("Schiene 1"));
	}

	@Test
	@DisplayName("testStateRegelvalidierung11SchuelerZusammenInFach")
	void testStateRegelvalidierung11SchuelerZusammenInFach() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ;
		regel.parameter.add(SCHUELER_1_ID);
		regel.parameter.add(SCHUELER_2_ID);
		regel.parameter.add(FACH_D_ID);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 1400);
		// K1 (D-GK1) in Schiene 1 mit S1, K4 (D-GK2) in Schiene 3 mit S2 -> nicht zusammen -> Verletzung
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_4, SCHIENE_ID_3));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));

		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> s = new HashSet<>();
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_1_ID));
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_4, SCHUELER_2_ID));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(s));

		assertTrue(mgr.getErgebnis().bewertung.regelVerletzungen.contains(REGEL_ID_1));
		assertEquals(1, mgr.getErgebnis().bewertung.regelVerletzungen.stream().filter(id -> id == REGEL_ID_1).count());
		final String beschreibung = mgr.regelGetMapRegelIdToVerletzungString().get(REGEL_ID_1);
		assertNotNull(beschreibung);
		assertTrue(beschreibung.contains("Test1, Max"));
		assertTrue(beschreibung.contains("Test2, Anna"));
		assertTrue(beschreibung.contains("gemeinsam in D"));
	}

	@Test
	@DisplayName("testStateRegelvalidierung12SchuelerVerbietenInFach")
	void testStateRegelvalidierung12SchuelerVerbietenInFach() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ;
		regel.parameter.add(SCHUELER_1_ID);
		regel.parameter.add(SCHUELER_2_ID);
		regel.parameter.add(FACH_M_ID);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 1500);
		// K2 (M-LK1) in Schiene 1, S1 und S2 beide in K2 -> gleicher Kurs -> Verletzung
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_2, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));

		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> s = new HashSet<>();
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_2, SCHUELER_1_ID));
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_2, SCHUELER_2_ID));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(s));

		assertTrue(mgr.getErgebnis().bewertung.regelVerletzungen.contains(REGEL_ID_1));
		assertEquals(1, mgr.getErgebnis().bewertung.regelVerletzungen.stream().filter(id -> id == REGEL_ID_1).count());
		final String beschreibung = mgr.regelGetMapRegelIdToVerletzungString().get(REGEL_ID_1);
		assertNotNull(beschreibung);
		assertTrue(beschreibung.contains("Test1, Max"));
		assertTrue(beschreibung.contains("Test2, Anna"));
		assertTrue(beschreibung.contains("nicht gemeinsam in M"));
	}

	@Test
	@DisplayName("testStateRegelvalidierung15KursMaximaleSchueleranzahl")
	void testStateRegelvalidierung15KursMaximaleSchueleranzahl() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ;
		regel.parameter.add(KURS_ID_1);
		regel.parameter.add(0L); // max 0 Schüler
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 1600);
		// K1 in Schiene 1, S1 in K1 -> 1 > max 0 -> Verletzung
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));

		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> s = new HashSet<>();
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_1_ID));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(s));

		assertTrue(mgr.getErgebnis().bewertung.regelVerletzungen.contains(REGEL_ID_1));
		assertEquals(1, mgr.getErgebnis().bewertung.regelVerletzungen.stream().filter(id -> id == REGEL_ID_1).count());
		final String beschreibung = mgr.regelGetMapRegelIdToVerletzungString().get(REGEL_ID_1);
		assertNotNull(beschreibung);
		assertTrue(beschreibung.contains("D-GK1"));
		assertTrue(beschreibung.contains("1 SuS"));
		assertTrue(beschreibung.contains("nicht mehr als 0"));
	}

	@Test
	@DisplayName("testStateRegelvalidierung14SchuelerVerbietenMitSchueler")
	void testStateRegelvalidierung14SchuelerVerbietenMitSchueler() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ;
		regel.parameter.add(SCHUELER_1_ID);
		regel.parameter.add(SCHUELER_2_ID);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 1700);
		// K2 (M-LK1) in Schiene 1, S1 und S2 beide in K2 -> gemeinsamer Kurs -> Verletzung
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_2, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));

		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> s = new HashSet<>();
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_2, SCHUELER_1_ID));
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_2, SCHUELER_2_ID));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(s));

		assertTrue(mgr.getErgebnis().bewertung.regelVerletzungen.contains(REGEL_ID_1));
		assertEquals(1, mgr.getErgebnis().bewertung.regelVerletzungen.stream().filter(id -> id == REGEL_ID_1).count());
		final String beschreibung = mgr.regelGetMapRegelIdToVerletzungString().get(REGEL_ID_1);
		assertNotNull(beschreibung);
		assertTrue(beschreibung.contains("Test1, Max"));
		assertTrue(beschreibung.contains("Test2, Anna"));
		assertTrue(beschreibung.contains("nicht gemeinsam in M"));
	}

	@Test
	@DisplayName("testRegelupdateCreateFachKursartMaximaleAnzahlMaxEins")
	void testRegelupdateCreateFachKursartMaximaleAnzahlMaxEins() {
		final GostBlockungsdatenManager parent = createParentManager();
		// Zwei D-GK Kurse mit KURS_VERBIETEN_MIT_KURS Regel -> wird bei maximal==1 entfernt
		final GostBlockungRegel verbotenRegel = new GostBlockungRegel();
		verbotenRegel.id = 50;
		verbotenRegel.typ = GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ;
		verbotenRegel.parameter.add(KURS_ID_1);
		verbotenRegel.parameter.add(KURS_ID_4);
		parent.regelAdd(verbotenRegel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 1900);
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateFachKursartMaximaleAnzahlProSchiene(FACH_D_ID, KURSART_GK, 1);
		// Die KURS_VERBIETEN_MIT_KURS Regel muss entfernt werden (maximal==1)
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ,
				Set.of(List.of(KURS_ID_1, KURS_ID_4)));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE.typ,
				Set.of(List.of(FACH_D_ID, (long) KURSART_GK, 1L)));
	}

	@Test
	@DisplayName("testRegelupdateCreateSchuelermengeEntfernenMitRegel")
	void testRegelupdateCreateSchuelermengeEntfernenMitRegel() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ;
		regel.parameter.add(SCHUELER_1_ID);
		regel.parameter.add(KURS_ID_1);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 2000);
		// S1 ist in der Regel referenziert -> Regel muss entfernt werden
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateSchuelermengeEntfernen(Set.of(SCHUELER_1_ID));
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ,
				Set.of(List.of(SCHUELER_1_ID, KURS_ID_1)));
		assertEquals(0, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testStateRegelvalidierung2KursFixierung")
	void testStateRegelvalidierung2KursFixierung() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
		regel.parameter.add(KURS_ID_1);
		regel.parameter.add((long) SCHIENE_NR_2);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 2100);
		// K1 in Schiene 1, aber Regel verlangt Schiene 2 -> nicht erfüllt -> Verletzung
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));

		assertTrue(mgr.getErgebnis().bewertung.regelVerletzungen.contains(REGEL_ID_1));
		assertEquals(1, mgr.getErgebnis().bewertung.regelVerletzungen.stream().filter(id -> id == REGEL_ID_1).count());
		final String beschreibung = mgr.regelGetMapRegelIdToVerletzungString().get(REGEL_ID_1);
		assertNotNull(beschreibung);
		assertTrue(beschreibung.contains("D-GK1"));
		assertTrue(beschreibung.contains("Schiene 2"));
	}

	@Test
	@DisplayName("testStateRegelvalidierung4SchuelerFixierungNichtErfuellt")
	void testStateRegelvalidierung4SchuelerFixierungNichtErfuellt() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ;
		regel.parameter.add(SCHUELER_1_ID);
		regel.parameter.add(KURS_ID_3);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 2200);
		// K3 (E-GK1) in Schiene 1, aber S1 nicht in K3 -> Fixierung nicht erfüllt -> Verletzung
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_3, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));

		assertTrue(mgr.getErgebnis().bewertung.regelVerletzungen.contains(REGEL_ID_1));
		assertEquals(1, mgr.getErgebnis().bewertung.regelVerletzungen.stream().filter(id -> id == REGEL_ID_1).count());
		final String beschreibung = mgr.regelGetMapRegelIdToVerletzungString().get(REGEL_ID_1);
		assertNotNull(beschreibung);
		assertTrue(beschreibung.contains("E-GK1"));
		assertTrue(beschreibung.contains("Test1, Max"));
	}

	@Test
	@DisplayName("testStateRegelvalidierung11KeineFachwahl")
	void testStateRegelvalidierung11KeineFachwahl() {
		final GostBlockungsdatenManager parent = createParentManager();
		// Regel mit Fach E: S1 hat E, S2 nicht -> Verletzung wegen fehlender Fachwahl
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ;
		regel.parameter.add(SCHUELER_1_ID);
		regel.parameter.add(SCHUELER_2_ID);
		regel.parameter.add(FACH_E_ID);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 2300);
		assertTrue(mgr.getErgebnis().bewertung.regelVerletzungen.contains(REGEL_ID_1));
		assertEquals(1, mgr.getErgebnis().bewertung.regelVerletzungen.stream().filter(id -> id == REGEL_ID_1).count());
		final String beschreibung = mgr.regelGetMapRegelIdToVerletzungString().get(REGEL_ID_1);
		assertNotNull(beschreibung);
		assertTrue(beschreibung.contains("Test2, Anna"));
		assertTrue(beschreibung.contains("keine Fachwahl E"));
	}

	@Test
	@DisplayName("testStateRegelvalidierung12KeineFachwahl")
	void testStateRegelvalidierung12KeineFachwahl() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ;
		regel.parameter.add(SCHUELER_2_ID); // S2 zuerst
		regel.parameter.add(SCHUELER_1_ID);
		regel.parameter.add(FACH_E_ID);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 2400);
		assertTrue(mgr.getErgebnis().bewertung.regelVerletzungen.contains(REGEL_ID_1));
		assertEquals(1, mgr.getErgebnis().bewertung.regelVerletzungen.stream().filter(id -> id == REGEL_ID_1).count());
		final String beschreibung = mgr.regelGetMapRegelIdToVerletzungString().get(REGEL_ID_1);
		assertNotNull(beschreibung);
		assertTrue(beschreibung.contains("Test2, Anna"));
		assertTrue(beschreibung.contains("keine Fachwahl E"));
	}

	@Test
	@DisplayName("testStateRegelvalidierung13Verletzung")
	void testStateRegelvalidierung13Verletzung() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungRegel regel = createRegel13(SCHUELER_1_ID, SCHUELER_2_ID);
		mgr.getParent().regelAdd(regel);
		mgr.stateRevalidateEverything();
		final long anzahl = mgr.getErgebnis().bewertung.regelVerletzungen.stream().filter(id -> id == regel.id).count();
		assertEquals(1, anzahl);
		final String beschreibung = mgr.regelGetMapRegelIdToVerletzungString().get(regel.id);
		assertNotNull(beschreibung);
		assertTrue(beschreibung.contains("sollten gemeinsam in D sein."));
	}

	@Test
	@DisplayName("testStateRegelvalidierung13Erfuellt")
	void testStateRegelvalidierung13Erfuellt() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungsergebnisKursSchuelerZuordnung entfernen =
				DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_1_ID);
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateEntferneKursSchuelerPaare(Set.of(entfernen)));
		final GostBlockungsergebnisKursSchuelerZuordnung hinzufuegen =
				DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_4, SCHUELER_1_ID);
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(Set.of(hinzufuegen)));
		final GostBlockungRegel regel = createRegel13(SCHUELER_1_ID, SCHUELER_2_ID);
		mgr.getParent().regelAdd(regel);
		mgr.stateRevalidateEverything();
		assertFalse(mgr.getErgebnis().bewertung.regelVerletzungen.contains(regel.id));
	}

	@Test
	@DisplayName("testRegelupdateCreateSchuelerZusammenMitSchuelerSelberSchueler")
	void testRegelupdateCreateSchuelerZusammenMitSchuelerSelberSchueler() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		// Gleiche Schüler-ID -> leeres Update (Guard greift)
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateSchuelerZusammenMitSchueler(SCHUELER_1_ID, SCHUELER_1_ID);
		assertEquals(0, u.listEntfernen.size());
		assertEquals(0, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateSchuelerVerbietenMitSchuelerSelberSchueler")
	void testRegelupdateCreateSchuelerVerbietenMitSchuelerSelberSchueler() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateSchuelerVerbietenMitSchueler(SCHUELER_1_ID, SCHUELER_1_ID);
		assertEquals(0, u.listEntfernen.size());
		assertEquals(0, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testRegelupdatePatchByIdSchuelerFixierenInKursWechsel")
	void testRegelupdatePatchByIdSchuelerFixierenInKursWechsel() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ;
		regel.parameter.add(SCHUELER_1_ID);
		regel.parameter.add(KURS_ID_1);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 2700);
		// Patchen auf anderen Kurs (K4 statt K1)
		final GostBlockungRegelUpdate u = mgr.regelupdatePatchByIdSchuelerFixierenInKurs(REGEL_ID_1, SCHUELER_1_ID, KURS_ID_4);
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ,
				Set.of(List.of(SCHUELER_1_ID, KURS_ID_1)));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ,
				Set.of(List.of(SCHUELER_1_ID, KURS_ID_4)));
	}

	@Test
	@DisplayName("testKursSchuelerUpdateBildeKerngruppenMitFixierung")
	void testKursSchuelerUpdateBildeKerngruppenMitFixierung() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		// K1 (D-GK1) und K4 (D-GK2): S1 in K1, S2 in K4
		// Mit verschiebeFixierte=true und inZielKursenFixieren=true
		final GostBlockungsergebnisKursSchuelerZuordnungUpdate u = mgr.kursSchuelerUpdateBildeKerngruppen(KURS_ID_1,
				Set.of(KURS_ID_4), true, true, false);
		assertEquals(1, u.listEntfernen.size());
		assertEquals(Set.of(List.of(KURS_ID_1, SCHUELER_1_ID)), u.listEntfernen.stream()
				.map(pair -> List.of(pair.idKurs, pair.idSchueler)).collect(java.util.stream.Collectors.toSet()));
		assertEquals(1, u.listHinzuzufuegen.size());
		assertEquals(Set.of(List.of(KURS_ID_4, SCHUELER_1_ID)), u.listHinzuzufuegen.stream()
				.map(pair -> List.of(pair.idKurs, pair.idSchueler)).collect(java.util.stream.Collectors.toSet()));
		assertRegelListe(u.regelUpdates.listEntfernen, GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, Set.of());
		assertRegelListe(u.regelUpdates.listHinzuzufuegen, GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ,
				Set.of(List.of(SCHUELER_1_ID, KURS_ID_4)));
	}

	@Test
	@DisplayName("testKursSchuelerUpdateBildeKerngruppenMitLeeren")
	void testKursSchuelerUpdateBildeKerngruppenMitLeeren() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		// K4 enthält S2 außerhalb der Kerngruppe aus K1 (S1) und wird vorher geleert.
		final GostBlockungsergebnisKursSchuelerZuordnungUpdate u = mgr.kursSchuelerUpdateBildeKerngruppen(KURS_ID_1,
				Set.of(KURS_ID_4), false, true, true);
		assertEquals(Set.of(List.of(KURS_ID_1, SCHUELER_1_ID), List.of(KURS_ID_4, SCHUELER_2_ID)), u.listEntfernen.stream()
				.map(pair -> List.of(pair.idKurs, pair.idSchueler)).collect(java.util.stream.Collectors.toSet()));
		assertEquals(Set.of(List.of(KURS_ID_4, SCHUELER_1_ID)), u.listHinzuzufuegen.stream()
				.map(pair -> List.of(pair.idKurs, pair.idSchueler)).collect(java.util.stream.Collectors.toSet()));
		assertRegelListe(u.regelUpdates.listEntfernen, GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, Set.of());
		assertRegelListe(u.regelUpdates.listHinzuzufuegen, GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ,
				Set.of(List.of(SCHUELER_1_ID, KURS_ID_4)));
	}

	@Test
	@DisplayName("testRegelupdateCreateLehrkaefteBeachtenDeaktivieren")
	void testRegelupdateCreateLehrkaefteBeachtenDeaktivieren() {
		final GostBlockungsdatenManager parent = createParentManager();
		// Regel existiert -> deaktivieren entfernt sie
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN.typ;
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 2900);
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateLehrkaefteBeachten(false);
		assertEquals(1, u.listEntfernen.size());
		assertEquals(0, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateSchuelerFixierenTypLkDerKursmengeMitKurs2")
	void testRegelupdateCreateSchuelerFixierenTypLkDerKursmengeMitKurs2() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateSchuelerFixierenTypLkDerKursmenge(Set.of(KURS_ID_2));
		assertNotNull(u);
		// K2 hat S1 und S2, aber kein abiturfach -> keine LK-Fixierung
		assertEquals(0, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateSchuelerFixierenTypAbDerKursmengeMitKurs2")
	void testRegelupdateCreateSchuelerFixierenTypAbDerKursmengeMitKurs2() {
		final GostBlockungsdatenManager parent = createParentManager();
		for (final GostFachwahl fw : parent.schuelerGetListeOfFachwahlen(SCHUELER_1_ID)) {
			if (fw.fachID == FACH_M_ID) {
				fw.abiturfach = 1;
			}
		}
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 9000);
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_2, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));
		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> s = new HashSet<>();
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_2, SCHUELER_1_ID));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(s));
		assertEquals(1, mgr.regelupdateCreateSchuelerFixierenTypAbDerKursmenge(Set.of(KURS_ID_2)).listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testGetOfSchuelerOfKursIstSchriftlichFilter")
	void testGetOfSchuelerOfKursIstSchriftlichFilter() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		// Falscher Schriftlichkeits-Filter bei Kurs -> keiner hat Schriftlichkeit gesetzt
		assertEquals(0, mgr.getOfSchuelerAnzahlGefiltert(KURS_ID_1, -1, -1, 0, "", null,
				GostSchriftlichkeit.SCHRIFTLICH));
	}

	@Test
	@DisplayName("testPatchOfKursSchienenAnzahlMitBlockungsvorlage")
	void testPatchOfKursSchienenAnzahlMitBlockungsvorlage() {
		final GostBlockungsdatenManager parent = createParentManagerMitBlockungsvorlage();
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 3000);

		// K1 von anzahlSchienen=1 auf 2 erhöhen
		assertDoesNotThrow(() -> mgr.patchOfKursSchienenAnzahl(KURS_ID_1, 2));
		assertEquals(2, mgr.getOfKursAnzahlSchienenSoll(KURS_ID_1));
		assertEquals(1, mgr.getOfKursAnzahlSchienenIst(KURS_ID_1)); // Nur 1 Schiene frei, da vorher 0 belegt
	}

	@Test
	@DisplayName("testSetRemoveSchieneByIDMitBlockungsvorlage")
	void testSetRemoveSchieneByIDMitBlockungsvorlage() {
		final GostBlockungsdatenManager parent = createParentManagerMitBlockungsvorlage();
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 3100);

		// Zuerst Manager erstellen, dann Schiene 3 aus dem Parent entfernen
		parent.schieneRemoveByID(SCHIENE_ID_3);
		mgr.setRemoveSchieneByID(SCHIENE_ID_3);
		assertEquals(2, mgr.getErgebnis().schienen.size());
		assertFalse(mgr.getOfSchieneExists(SCHIENE_ID_3));
		assertTrue(mgr.getErgebnis().schienen.stream().noneMatch(s -> s.kurse.stream().anyMatch(k -> k.schienen.contains(SCHIENE_ID_3))));
	}

	@Test
	@DisplayName("testSetRemoveKurseByIDMitBlockungsvorlage")
	void testSetRemoveKurseByIDMitBlockungsvorlage() {
		final GostBlockungsdatenManager parent = createParentManagerMitBlockungsvorlage();
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 3200);

		// Kurs 2 aus dem Parent entfernen (Blockungsvorlage erlaubt das)
		parent.kursRemoveByID(KURS_ID_2);
		mgr.setRemoveKurseByID(List.of(KURS_ID_2));
		assertEquals(1, mgr.getMappingKursIDSchuelerIDs().size());
		assertFalse(mgr.getMappingKursIDSchuelerIDs().containsKey(KURS_ID_2));
		assertThrows(DeveloperNotificationException.class, () -> mgr.getKursE(KURS_ID_2));
		assertTrue(mgr.getErgebnis().schienen.stream().allMatch(s -> s.kurse.stream().noneMatch(k -> k.id == KURS_ID_2)));
	}

	@Test
	@DisplayName("testPatchOfKursSchienenAnzahlVerringern")
	void testPatchOfKursSchienenAnzahlVerringern() {
		final GostBlockungsdatenManager parent = createParentManagerMitBlockungsvorlage();
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 3300);

		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> schienen = new HashSet<>();
		schienen.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(schienen));

		// Erst auf 2 erhöhen, dann auf 1 verringern
		mgr.patchOfKursSchienenAnzahl(KURS_ID_1, 2);
		assertEquals(2, mgr.getOfKursAnzahlSchienenSoll(KURS_ID_1));
		assertEquals(2, parent.kursGet(KURS_ID_1).anzahlSchienen);
		assertEquals(2, mgr.getKursE(KURS_ID_1).anzahlSchienen);
		assertEquals(Set.of(SCHIENE_ID_1, SCHIENE_ID_2), Set.copyOf(mgr.getKursE(KURS_ID_1).schienen));
		mgr.patchOfKursSchienenAnzahl(KURS_ID_1, 1);
		assertEquals(1, mgr.getOfKursAnzahlSchienenSoll(KURS_ID_1));
		assertEquals(1, parent.kursGet(KURS_ID_1).anzahlSchienen);
		assertEquals(1, mgr.getKursE(KURS_ID_1).anzahlSchienen);
		assertEquals(Set.of(SCHIENE_ID_1), Set.copyOf(mgr.getKursE(KURS_ID_1).schienen));
	}

	@Test
	@DisplayName("testSetMergeKurseByIDMitBlockungsvorlage")
	void testSetMergeKurseByIDMitBlockungsvorlage() {
		final GostBlockungsdatenManager parent = createParentManagerMitBlockungsvorlage();
		parent.kursAdd(createKurs(KURS_ID_4, FACH_D_ID, KURSART_GK, 2)); // D-GK2

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 3400);
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> schienen = new HashSet<>();
		schienen.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		schienen.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_4, SCHIENE_ID_2));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(schienen));
		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> schueler = new HashSet<>();
		schueler.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_4, SCHUELER_1_ID));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(schueler));

		mgr.setMergeKurseByID(KURS_ID_1, KURS_ID_4);

		assertFalse(parent.kursGetExistiert(KURS_ID_4));
		assertEquals(List.of(SCHUELER_1_ID), mgr.getKursE(KURS_ID_1).schueler);
		assertEquals(Set.of(SCHIENE_ID_1), mgr.getMappingKursIDSchienenmenge().get(KURS_ID_1).stream().map(s -> s.id).collect(java.util.stream.Collectors.toSet()));
		assertThrows(DeveloperNotificationException.class, () -> mgr.getKursE(KURS_ID_4));
		assertTrue(mgr.getErgebnis().schienen.stream().noneMatch(s -> s.kurse.stream().anyMatch(k -> k.id == KURS_ID_4)));
	}

	@Test
	@DisplayName("testRegelupdateCreateSchuelerZusammenMitSchuelerMitBestehendenRegeln")
	void testRegelupdateCreateSchuelerZusammenMitSchuelerMitBestehendenRegeln() {
		final GostBlockungsdatenManager parent = createParentManager();
		// Vorhandene Regeln, die entfernt werden sollen
		parent.regelAdd(createRegel11(SCHUELER_1_ID, SCHUELER_2_ID, FACH_D_ID));
		parent.regelAdd(createRegel12(SCHUELER_1_ID, SCHUELER_2_ID, FACH_M_ID));
		parent.regelAdd(createRegel13(SCHUELER_1_ID, SCHUELER_2_ID));
		parent.regelAdd(createRegel14(SCHUELER_1_ID, SCHUELER_2_ID));

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 3500);
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateSchuelerZusammenMitSchueler(SCHUELER_1_ID, SCHUELER_2_ID);
		// Alle 4 alten Regeln werden entfernt, 1 neue hinzugefügt
		assertRegelListeMitTypUndParameter(u.listEntfernen, Set.of(
				List.of((long) GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ, SCHUELER_1_ID, SCHUELER_2_ID, FACH_D_ID),
				List.of((long) GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ, SCHUELER_1_ID, SCHUELER_2_ID, FACH_M_ID),
				List.of((long) GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ, SCHUELER_1_ID, SCHUELER_2_ID),
				List.of((long) GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ, SCHUELER_1_ID, SCHUELER_2_ID)));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ,
				Set.of(List.of(SCHUELER_1_ID, SCHUELER_2_ID)));
		assertEquals(1, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testRegelupdateCreateSchuelerVerbietenMitSchuelerMitBestehendenRegeln")
	void testRegelupdateCreateSchuelerVerbietenMitSchuelerMitBestehendenRegeln() {
		final GostBlockungsdatenManager parent = createParentManager();
		parent.regelAdd(createRegel11(SCHUELER_1_ID, SCHUELER_2_ID, FACH_D_ID));
		parent.regelAdd(createRegel12(SCHUELER_1_ID, SCHUELER_2_ID, FACH_M_ID));
		parent.regelAdd(createRegel13(SCHUELER_1_ID, SCHUELER_2_ID));
		parent.regelAdd(createRegel14(SCHUELER_1_ID, SCHUELER_2_ID));

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 3600);
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateSchuelerVerbietenMitSchueler(SCHUELER_1_ID, SCHUELER_2_ID);
		// Alle 4 werden entfernt, 1 neue hinzugefügt
		assertRegelListeMitTypUndParameter(u.listEntfernen, Set.of(
				List.of((long) GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ, SCHUELER_1_ID, SCHUELER_2_ID, FACH_D_ID),
				List.of((long) GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ, SCHUELER_1_ID, SCHUELER_2_ID, FACH_M_ID),
				List.of((long) GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ, SCHUELER_1_ID, SCHUELER_2_ID),
				List.of((long) GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ, SCHUELER_1_ID, SCHUELER_2_ID)));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ,
				Set.of(List.of(SCHUELER_1_ID, SCHUELER_2_ID)));
		assertEquals(1, u.listHinzuzufuegen.size());
	}

	// Helper-Methoden für Regeln
	private static GostBlockungRegel createRegel11(final long s1, final long s2, final long fach) {
		final GostBlockungRegel r = new GostBlockungRegel();
		r.id = s1 + s2 + fach + 1000;
		r.typ = GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ;
		r.parameter.add(s1);
		r.parameter.add(s2);
		r.parameter.add(fach);
		return r;
	}

	private static GostBlockungRegel createRegel12(final long s1, final long s2, final long fach) {
		final GostBlockungRegel r = new GostBlockungRegel();
		r.id = s1 + s2 + fach + 2000;
		r.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ;
		r.parameter.add(s1);
		r.parameter.add(s2);
		r.parameter.add(fach);
		return r;
	}

	private static GostBlockungRegel createRegel13(final long s1, final long s2) {
		final GostBlockungRegel r = new GostBlockungRegel();
		r.id = s1 + s2 + 3000;
		r.typ = GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ;
		r.parameter.add(s1);
		r.parameter.add(s2);
		return r;
	}

	private static GostBlockungRegel createRegel14(final long s1, final long s2) {
		final GostBlockungRegel r = new GostBlockungRegel();
		r.id = s1 + s2 + 4000;
		r.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ;
		r.parameter.add(s1);
		r.parameter.add(s2);
		return r;
	}

	@Test
	@DisplayName("testGetOfSchuelerMengeMitAbweichendemAbijahrgangMitKurs")
	void testGetOfSchuelerMengeMitAbweichendemAbijahrgangMitKurs() {
		final GostBlockungsdatenManager parent = createParentManagerMitBlockungsvorlage();
		final Schueler abweichend = new Schueler();
		abweichend.id = 300;
		abweichend.nachname = "Anders";
		abweichend.vorname = "Jahrgang";
		abweichend.geschlecht = Geschlecht.M.id;
		abweichend.status = SchuelerStatus.AKTIV.ordinal();
		abweichend.abschlussjahrgang = 99;
		parent.schuelerAdd(abweichend);
		parent.fachwahlAdd(createFachwahl(300L, FACH_D_ID, KURSART_GK));

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 3700);
		// S300 in K1 legen
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));

		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> s = new HashSet<>();
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, 300L));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(s));

		// Abweichender Abijahrgang mit Kurszuordnung -> erscheint in der Liste
		final List<Schueler> abweichende = mgr.getOfSchuelerMengeMitAbweichendemAbijahrgang();
		assertEquals(1, abweichende.size());
		assertEquals(300L, abweichende.get(0).id);
	}

	@Test
	@DisplayName("testRegelupdateCreateKursFixiereInEinerSchieneHelperOhneErlaubnis")
	void testRegelupdateCreateKursFixiereInEinerSchieneHelperOhneErlaubnis() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = 99001;
		regel.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
		regel.parameter.add(KURS_ID_1);
		regel.parameter.add((long) SCHIENE_NR_1);
		parent.regelAdd(regel);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 9100);
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateKursFixiereInEinerSchiene(KURS_ID_1, SCHIENE_NR_2);
		assertEquals(1, u.listEntfernen.size());
		assertEquals(1, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testSetMergeKurseByIDMitBlockungsvorlageVollstaendig")
	void testSetMergeKurseByIDMitBlockungsvorlageVollstaendig() {
		final GostBlockungsdatenManager parent = createParentManagerMitBlockungsvorlage();
		parent.kursAdd(createKurs(KURS_ID_4, FACH_D_ID, KURSART_GK, 2));

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 4100);
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> schienen = new HashSet<>();
		schienen.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		schienen.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_4, SCHIENE_ID_2));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(schienen));
		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> schueler = new HashSet<>();
		schueler.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_2_ID));
		schueler.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_4, SCHUELER_1_ID));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(schueler));

		// K1 (D-GK1) keep, K4 (D-GK2) delete
		mgr.setMergeKurseByID(KURS_ID_1, KURS_ID_4);
		assertFalse(parent.kursGetExistiert(KURS_ID_4));
		assertEquals(Set.of(SCHUELER_1_ID, SCHUELER_2_ID), Set.copyOf(mgr.getKursE(KURS_ID_1).schueler));
		assertEquals(Set.of(SCHIENE_ID_1), mgr.getMappingKursIDSchienenmenge().get(KURS_ID_1).stream().map(s -> s.id).collect(java.util.stream.Collectors.toSet()));
		assertEquals(Set.of(SCHUELER_1_ID, SCHUELER_2_ID), mgr.getMappingKursIDSchuelerIDs().get(KURS_ID_1));
		assertEquals(Set.of(KURS_ID_1, KURS_ID_2), parent.daten().kurse.stream().map(k -> k.id).collect(java.util.stream.Collectors.toSet()));
		assertEquals(Set.of(KURS_ID_1), mgr.getErgebnis().schienen.stream().filter(s -> s.id == SCHIENE_ID_1).findFirst().orElseThrow().kurse.stream()
				.map(k -> k.id).collect(java.util.stream.Collectors.toSet()));
		assertThrows(DeveloperNotificationException.class, () -> mgr.getKursE(KURS_ID_4));
	}

	@Test
	@DisplayName("testKursSchuelerUpdateEntferneSchuelermengeAusKursMitFixierten")
	void testKursSchuelerUpdateEntferneSchuelermengeAusKursMitFixierten() {
		final GostBlockungsdatenManager parent = createParentManager();
		// S1 in K1 fixieren
		final GostBlockungRegel fix = new GostBlockungRegel();
		fix.id = 5000;
		fix.typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ;
		fix.parameter.add(SCHUELER_1_ID);
		fix.parameter.add(KURS_ID_1);
		parent.regelAdd(fix);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 4200);
		// K1 in Schiene 1, S1 in K1
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));

		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> s = new HashSet<>();
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_1_ID));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(s));

		// Ohne Fixierte zu entfernen: S1 bleibt (weil fixiert)
		final GostBlockungsergebnisKursSchuelerZuordnungUpdate u1 = mgr.kursSchuelerUpdateEntferneSchuelermengeAusKurs(
				Set.of(SCHUELER_1_ID), KURS_ID_1, false);
		assertEquals(0, u1.listEntfernen.size());
		assertEquals(0, u1.regelUpdates.listEntfernen.size());

		// Mit Fixierte entfernen: S1 wird entfernt + Fixierung gelöst
		final GostBlockungsergebnisKursSchuelerZuordnungUpdate u2 = mgr.kursSchuelerUpdateEntferneSchuelermengeAusKurs(
				Set.of(SCHUELER_1_ID), KURS_ID_1, true);
		assertEquals(1, u2.listEntfernen.size());
		assertTrue(u2.listEntfernen.contains(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_1_ID)));
		assertEquals(1, u2.regelUpdates.listEntfernen.size());
		assertEquals(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, u2.regelUpdates.listEntfernen.get(0).typ);
		assertEquals(SCHUELER_1_ID, u2.regelUpdates.listEntfernen.get(0).parameter.get(0));
		assertEquals(KURS_ID_1, u2.regelUpdates.listEntfernen.get(0).parameter.get(1));
	}

	@Test
	@DisplayName("testKursSchuelerUpdateBildeKerngruppenAlleOptionen")
	void testKursSchuelerUpdateBildeKerngruppenAlleOptionen() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		// Teste alle boolean-Kombinationen
		final GostBlockungsergebnisKursSchuelerZuordnungUpdate u1 = mgr.kursSchuelerUpdateBildeKerngruppen(KURS_ID_1,
				Set.of(KURS_ID_4), true, true, true);
		assertEquals(Set.of(List.of(KURS_ID_1, SCHUELER_1_ID), List.of(KURS_ID_4, SCHUELER_2_ID)), u1.listEntfernen.stream()
				.map(pair -> List.of(pair.idKurs, pair.idSchueler)).collect(java.util.stream.Collectors.toSet()));
		assertEquals(Set.of(List.of(KURS_ID_4, SCHUELER_1_ID)), u1.listHinzuzufuegen.stream()
				.map(pair -> List.of(pair.idKurs, pair.idSchueler)).collect(java.util.stream.Collectors.toSet()));
		assertRegelListe(u1.regelUpdates.listEntfernen, GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, Set.of());
		assertRegelListe(u1.regelUpdates.listHinzuzufuegen, GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ,
				Set.of(List.of(SCHUELER_1_ID, KURS_ID_4)));

		final GostBlockungsergebnisKursSchuelerZuordnungUpdate u2 = mgr.kursSchuelerUpdateBildeKerngruppen(KURS_ID_1,
				Set.of(KURS_ID_4), false, false, false);
		assertEquals(Set.of(List.of(KURS_ID_1, SCHUELER_1_ID)), u2.listEntfernen.stream()
				.map(pair -> List.of(pair.idKurs, pair.idSchueler)).collect(java.util.stream.Collectors.toSet()));
		assertEquals(Set.of(List.of(KURS_ID_4, SCHUELER_1_ID)), u2.listHinzuzufuegen.stream()
				.map(pair -> List.of(pair.idKurs, pair.idSchueler)).collect(java.util.stream.Collectors.toSet()));
		assertRegelListe(u2.regelUpdates.listEntfernen, GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, Set.of());
		assertRegelListe(u2.regelUpdates.listHinzuzufuegen, GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, Set.of());
	}

	@Test
	@DisplayName("testKursSchienenUpdateExecuteMitEntfernen")
	void testKursSchienenUpdateExecuteMitEntfernen() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		// K1 in Schiene 1 -> entfernen
		final GostBlockungsergebnisKursSchienenZuordnungUpdate u = mgr.kursSchienenUpdateEntferneKursSchienenPaare(
				Set.of(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1)));
		assertDoesNotThrow(() -> mgr.kursSchienenUpdateExecute(u));
		assertEquals(Set.of(), Set.copyOf(mgr.getKursE(KURS_ID_1).schienen));
		assertEquals(Set.of(), mgr.getMappingKursIDSchienenmenge().get(KURS_ID_1).stream().map(s -> s.id)
				.collect(java.util.stream.Collectors.toSet()));
		assertEquals(Set.of(KURS_ID_2), mgr.getOfSchieneKursmengeSortiert(SCHIENE_ID_1).stream().map(k -> k.id)
				.collect(java.util.stream.Collectors.toSet()));
		assertFalse(mgr.getOfSchieneKursmengeSortiert(SCHIENE_ID_1).stream().map(k -> k.id).collect(java.util.stream.Collectors.toSet())
				.contains(KURS_ID_1));
	}

	@Test
	@DisplayName("testStateRegelvalidierung8KursZusammenSet2Groesser")
	void testStateRegelvalidierung8KursZusammenSet2Groesser() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ;
		regel.parameter.add(KURS_ID_3);
		regel.parameter.add(KURS_ID_1);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 4400);
		// K1 in Schiene 1, K3 in Schiene 1+2
		// set1(K1)=1, set2(K3)=2 -> set1 < set2 -> "set1 muss in set2"
		// K1(Sch1) ist in K3(Sch1,Sch2) -> keine Verletzung
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_3, SCHIENE_ID_1));
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_3, SCHIENE_ID_2));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));

		assertFalse(mgr.getErgebnis().bewertung.regelVerletzungen.contains(REGEL_ID_1));
	}

	@Test
	@DisplayName("testAbitur3und4MitAbitur")
	void testAbitur3und4MitAbitur() {
		final GostBlockungsdatenManager parent3 = createParentManager();
		for (final GostFachwahl fw : parent3.schuelerGetListeOfFachwahlen(SCHUELER_1_ID)) {
			if (fw.fachID == FACH_D_ID) {
				fw.abiturfach = 3;
			}
		}
		final GostBlockungsergebnisManager mgr3 = new GostBlockungsergebnisManager(parent3, ERGEBNIS_ID_1 + 5600);
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z3 = new HashSet<>();
		z3.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		mgr3.kursSchienenUpdateExecute(mgr3.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z3));
		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> s3 = new HashSet<>();
		s3.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_1_ID));
		mgr3.kursSchuelerUpdateExecute(mgr3.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(s3));
		assertEquals(1, mgr3.getOfKursAnzahlSchuelerAbitur3(KURS_ID_1));

		final GostBlockungsdatenManager parent4 = createParentManager();
		for (final GostFachwahl fw : parent4.schuelerGetListeOfFachwahlen(SCHUELER_1_ID)) {
			if (fw.fachID == FACH_D_ID) {
				fw.abiturfach = 4;
			}
		}
		final GostBlockungsergebnisManager mgr4 = new GostBlockungsergebnisManager(parent4, ERGEBNIS_ID_1 + 5700);
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z4 = new HashSet<>();
		z4.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		mgr4.kursSchienenUpdateExecute(mgr4.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z4));
		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> s4 = new HashSet<>();
		s4.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_1_ID));
		mgr4.kursSchuelerUpdateExecute(mgr4.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(s4));
		assertEquals(1, mgr4.getOfKursAnzahlSchuelerAbitur4(KURS_ID_1));
	}

	@Test
	@DisplayName("testFilterUndTooltipGetters")
	void testFilterUndTooltipGetters() {
		final GostBlockungsergebnisManager leer = createLeerManager();
		assertEquals("", leer.regelGetTooltipFuerRegelverletzungen());
		assertEquals("", leer.regelGetTooltipFuerFaecherparallelitaet());

		final GostBlockungsergebnisManager mgr = createStandardManager();
		assertEquals("", mgr.regelGetTooltipFuerRegelverletzungen());
		final String wahlkonflikte = mgr.regelGetTooltipFuerWahlkonflikte();
		assertFalse(wahlkonflikte.contains("%n"));
		assertTrue(wahlkonflikte.contains("\n"));
		assertTrue(wahlkonflikte.contains("Wahlkonflikte = 2"));
		assertTrue(wahlkonflikte.contains("Test1, Max"));
		assertTrue(wahlkonflikte.contains("D-GK1"));
		assertTrue(wahlkonflikte.contains("M-LK1"));
		final String kursdifferenzen = mgr.regelGetTooltipFuerKursdifferenzen();
		assertEquals("Maximale Kursdifferenz (LK, GK, REST): 0, 0, 0\nOptimal 0/1: 4x\n", kursdifferenzen);
		assertEquals("", mgr.regelGetTooltipFuerFaecherparallelitaet());
		assertEquals(2, mgr.getOfSchuelerAnzahlGefiltert(-1, FACH_D_ID, -1, 0, "", null, null));
		assertEquals(0, mgr.getOfSchuelerAnzahlGefiltert(-1, FACH_D_ID, -1, 0, "", null, GostSchriftlichkeit.SCHRIFTLICH));
	}

	@Test
	@DisplayName("testGetTooltipFuerFaecherparallelitaet")
	void testGetTooltipFuerFaecherparallelitaet() {
		// Zwei D-GK-Kurse (K1, K4) in derselben Schiene 1 --> Fächerparallelität.
		final GostBlockungsergebnisManager mgr = createManagerMitFaecherparallelitaet();

		final String tooltip = mgr.regelGetTooltipFuerFaecherparallelitaet();
		assertFalse(tooltip.contains("%n"));
		final String[] zeilen = tooltip.split("\n");
		assertEquals("Schiene 1:", zeilen[0]);
		assertTrue(zeilen[1].startsWith("  D-GK (+1):"));
		assertTrue(zeilen[1].contains("D-GK1"));
		assertTrue(zeilen[1].contains("D-GK2"));
	}

	@Test
	@DisplayName("testTooltipsEnthaltenKeineFormatplatzhalter")
	void testTooltipsEnthaltenKeineFormatplatzhalter() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		assertFalse(mgr.regelGetTooltipFuerRegelverletzungen().contains("%n"));
		assertFalse(mgr.regelGetTooltipFuerWahlkonflikte().contains("%n"));
		assertFalse(mgr.regelGetTooltipFuerKursdifferenzen().contains("%n"));
		assertFalse(mgr.regelGetTooltipFuerFaecherparallelitaet().contains("%n"));
	}

	@Test
	@DisplayName("testAbiturLKMitAbiturfach")
	void testAbiturLKMitAbiturfach() {
		final GostBlockungsdatenManager parent = createParentManager();
		for (final GostFachwahl fw : parent.schuelerGetListeOfFachwahlen(SCHUELER_1_ID)) {
			if (fw.fachID == FACH_D_ID) {
				fw.abiturfach = 1;
			}
		}
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 5400);
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));
		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> s = new HashSet<>();
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_1_ID));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(s));
		assertEquals(1, mgr.getOfKursAnzahlSchuelerAbiturLK(KURS_ID_1));
	}

	@Test
	@DisplayName("testSetRemoveKurseMitSchienenBlockungsvorlage")
	void testSetRemoveKurseMitSchienenBlockungsvorlage() {
		final GostBlockungsdatenManager parent = createParentManagerMitBlockungsvorlage();
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 3200);
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_2, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));
		parent.kursRemoveByID(KURS_ID_2);
		mgr.setRemoveKurseByID(List.of(KURS_ID_2));
		assertFalse(mgr.getMappingKursIDSchienenmenge().containsKey(KURS_ID_2));
		assertFalse(mgr.getMappingKursIDSchuelerIDs().containsKey(KURS_ID_2));
		assertThrows(DeveloperNotificationException.class, () -> mgr.getKursE(KURS_ID_2));
		assertTrue(mgr.getErgebnis().schienen.stream().allMatch(s -> s.kurse.stream().noneMatch(k -> k.id == KURS_ID_2)));
		assertEquals(Set.of(KURS_ID_1), parent.daten().kurse.stream()
				.map(k -> k.id).collect(java.util.stream.Collectors.toSet()));
	}

	@Test
	@DisplayName("testPatchByIdKursFixiereSchieneWechsel")
	void testPatchByIdKursFixiereSchieneWechsel() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
		regel.parameter.add(KURS_ID_1);
		regel.parameter.add((long) SCHIENE_NR_1);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 5000);
		final GostBlockungRegelUpdate u = mgr.regelupdatePatchByIdKursFixiereInEinerSchiene(REGEL_ID_1, KURS_ID_1, SCHIENE_NR_2);
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ,
				Set.of(List.of(KURS_ID_1, (long) SCHIENE_NR_1)));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ,
				Set.of(List.of(KURS_ID_1, (long) SCHIENE_NR_2)));
	}

	@Test
	@DisplayName("testPatchByIdSchuelerZusammenWechsel")
	void testPatchByIdSchuelerZusammenWechsel() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = REGEL_ID_1;
		regel.typ = GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ;
		regel.parameter.add(SCHUELER_1_ID);
		regel.parameter.add(SCHUELER_2_ID);
		parent.regelAdd(regel);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 5100);
		final GostBlockungRegelUpdate u = mgr.regelupdatePatchByIdSchuelerZusammenMitSchueler(REGEL_ID_1, SCHUELER_1_ID, SCHUELER_NICHT_VORHANDEN);
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ,
				Set.of(List.of(SCHUELER_1_ID, SCHUELER_2_ID)));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ,
				Set.of(List.of(SCHUELER_1_ID, SCHUELER_NICHT_VORHANDEN)));
	}

	@Test
	@DisplayName("testNurLkFarbcodeMitDiff")
	void testNurLkFarbcodeMitDiff() {
		final GostBlockungsdatenManager parent = createParentManager();
		parent.kursAdd(createKurs(99, FACH_M_ID, KURSART_LK, 2));

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 5200);
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_2, SCHIENE_ID_1));
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(99, SCHIENE_ID_2));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));

		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> s = new HashSet<>();
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_2, SCHUELER_1_ID));
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_2, SCHUELER_2_ID));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(s));

		assertEquals(0.2, mgr.getOfBewertung3FarbcodeNurLk(), 1e-9);
	}

	@Test
	@DisplayName("testNurRestFarbcodeMitPJK")
	void testNurRestFarbcodeMitPJK() {
		final GostBlockungsdatenManager parent = createParentManagerMitBlockungsvorlage();
		parent.schuelerGetOfFachFachwahl(SCHUELER_1_ID, FACH_E_ID).kursartID = GostKursart.PJK.id;
		parent.fachwahlAdd(createFachwahl(SCHUELER_2_ID, FACH_E_ID, GostKursart.PJK.id));
		parent.kursAdd(createKurs(98, FACH_E_ID, GostKursart.PJK.id, 1));
		parent.kursAdd(createKurs(99, FACH_E_ID, GostKursart.PJK.id, 2));

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 5300);
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(98, SCHIENE_ID_1));
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(99, SCHIENE_ID_2));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));

		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> s = new HashSet<>();
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(98, SCHUELER_1_ID));
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(98, SCHUELER_2_ID));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(s));
		assertTrue(mgr.getOfSchuelerOfKursIstZugeordnet(SCHUELER_1_ID, 98));
		assertTrue(mgr.getOfSchuelerOfKursIstZugeordnet(SCHUELER_2_ID, 98));
		assertFalse(mgr.getOfSchuelerOfKursIstUngueltig(SCHUELER_1_ID, 98));
		assertFalse(mgr.getOfSchuelerOfKursIstUngueltig(SCHUELER_2_ID, 98));

		assertEquals(0.2, mgr.getOfBewertung3FarbcodeNurRest(), 1e-9);
	}

	@Test
	@DisplayName("testFachKursartMaximaleAnzahlProSchieneMitAltRegel")
	void testFachKursartMaximaleAnzahlProSchieneMitAltRegel() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = 7000;
		regel.typ = GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE.typ;
		regel.parameter.add(FACH_D_ID);
		regel.parameter.add((long) KURSART_GK);
		regel.parameter.add(5L);
		parent.regelAdd(regel);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 6300);
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateFachKursartMaximaleAnzahlProSchiene(FACH_D_ID, KURSART_GK, 2);
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE.typ,
				Set.of(List.of(FACH_D_ID, (long) KURSART_GK, 5L)));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.FACH_KURSART_MAXIMALE_ANZAHL_PRO_SCHIENE.typ,
				Set.of(List.of(FACH_D_ID, (long) KURSART_GK, 2L)));
	}

	@Test
	@DisplayName("testFixierenTypenAlle")
	void testFixierenTypenAlle() {
		final GostBlockungsdatenManager parent = createParentManager();
		for (final GostFachwahl fw : parent.schuelerGetListeOfFachwahlen(SCHUELER_1_ID)) {
			if (fw.fachID == FACH_D_ID) {
				fw.abiturfach = 1;
				fw.istSchriftlich = true;
			}
		}
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 6600);
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));
		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> s = new HashSet<>();
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_1_ID));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(s));
		assertEquals(1, mgr.regelupdateCreateSchuelerFixierenTypAbDerKursmenge(Set.of(KURS_ID_1)).listHinzuzufuegen.size());
		assertEquals(1, mgr.regelupdateCreateSchuelerFixierenTypLkDerKursmenge(Set.of(KURS_ID_1)).listHinzuzufuegen.size());
		assertEquals(1, mgr.regelupdateCreateSchuelerFixierenTypLkUndAb3DerKursmenge(Set.of(KURS_ID_1)).listHinzuzufuegen.size());
		assertEquals(1, mgr.regelupdateCreateSchuelerFixierenTypSchriftlichDerKursmenge(Set.of(KURS_ID_1)).listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testAb3MitAbiturfach")
	void testAb3MitAbiturfach() {
		final GostBlockungsdatenManager parent = createParentManager();
		for (final GostFachwahl fw : parent.schuelerGetListeOfFachwahlen(SCHUELER_1_ID)) {
			if (fw.fachID == FACH_D_ID) {
				fw.abiturfach = 3;
			}
		}
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 8000);
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));
		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> s = new HashSet<>();
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_1_ID));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(s));
		assertEquals(1, mgr.regelupdateCreateSchuelerFixierenTypAb3DerKursmenge(Set.of(KURS_ID_1)).listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testAb4MitAbiturfach")
	void testAb4MitAbiturfach() {
		final GostBlockungsdatenManager parent = createParentManager();
		for (final GostFachwahl fw : parent.schuelerGetListeOfFachwahlen(SCHUELER_1_ID)) {
			if (fw.fachID == FACH_D_ID) {
				fw.abiturfach = 4;
			}
		}
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 9000);
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));
		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> s = new HashSet<>();
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_1_ID));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(s));
		assertEquals(1, mgr.regelupdateCreateSchuelerFixierenTypAb4DerKursmenge(Set.of(KURS_ID_1)).listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testKursSchuelerUpdateExecuteMitEntfernen")
	void testKursSchuelerUpdateExecuteMitEntfernen() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungsergebnisKursSchuelerZuordnungUpdate update = new GostBlockungsergebnisKursSchuelerZuordnungUpdate();
		update.listEntfernen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_1_ID));
		assertDoesNotThrow(() -> mgr.kursSchuelerUpdateExecute(update));
		assertEquals(Set.of(), Set.copyOf(mgr.getKursE(KURS_ID_1).schueler));
		assertEquals(Set.of(), mgr.getMappingKursIDSchuelerIDs().get(KURS_ID_1));
		assertEquals(List.of(), mgr.getOfKursSchuelermenge(KURS_ID_1));
		assertEquals(Set.of(KURS_ID_2, KURS_ID_3), mgr.getOfSchuelerKursmenge(SCHUELER_1_ID).stream().map(k -> k.id)
				.collect(java.util.stream.Collectors.toSet()));
	}

	@Test
	@DisplayName("testVerbietenInFachAlleBranches")
	void testVerbietenInFachAlleBranches() {
		final GostBlockungsdatenManager parent = createParentManager();
		GostBlockungRegel r = new GostBlockungRegel();
		r.id = 80001;
		r.typ = GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ;
		r.parameter.add(SCHUELER_1_ID);
		r.parameter.add(SCHUELER_2_ID);
		r.parameter.add(FACH_D_ID);
		parent.regelAdd(r);
		r = new GostBlockungRegel();
		r.id = 80002;
		r.typ = GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ;
		r.parameter.add(SCHUELER_2_ID);
		r.parameter.add(SCHUELER_1_ID);
		r.parameter.add(FACH_D_ID);
		parent.regelAdd(r);
		r = new GostBlockungRegel();
		r.id = 80003;
		r.typ = GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ;
		r.parameter.add(SCHUELER_1_ID);
		r.parameter.add(SCHUELER_2_ID);
		parent.regelAdd(r);
		r = new GostBlockungRegel();
		r.id = 80004;
		r.typ = GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ;
		r.parameter.add(SCHUELER_2_ID);
		r.parameter.add(SCHUELER_1_ID);
		parent.regelAdd(r);
		r = new GostBlockungRegel();
		r.id = 80005;
		r.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ;
		r.parameter.add(SCHUELER_1_ID);
		r.parameter.add(SCHUELER_2_ID);
		parent.regelAdd(r);
		r = new GostBlockungRegel();
		r.id = 80006;
		r.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ;
		r.parameter.add(SCHUELER_2_ID);
		r.parameter.add(SCHUELER_1_ID);
		parent.regelAdd(r);
		r = new GostBlockungRegel();
		r.id = 80007;
		r.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ;
		r.parameter.add(SCHUELER_2_ID);
		r.parameter.add(SCHUELER_1_ID);
		r.parameter.add(FACH_D_ID);
		parent.regelAdd(r);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 7000);
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateSchuelerVerbietenMitSchuelerInFach(SCHUELER_1_ID, SCHUELER_2_ID, FACH_D_ID);
		assertRegelListeMitTypUndParameter(u.listEntfernen, Set.of(
				List.of((long) GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ, SCHUELER_1_ID, SCHUELER_2_ID, FACH_D_ID),
				List.of((long) GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ, SCHUELER_2_ID, SCHUELER_1_ID, FACH_D_ID),
				List.of((long) GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ, SCHUELER_1_ID, SCHUELER_2_ID),
				List.of((long) GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ, SCHUELER_2_ID, SCHUELER_1_ID),
				List.of((long) GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ, SCHUELER_1_ID, SCHUELER_2_ID),
				List.of((long) GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ, SCHUELER_2_ID, SCHUELER_1_ID),
				List.of((long) GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ, SCHUELER_2_ID, SCHUELER_1_ID, FACH_D_ID)));
		assertRegelListeMitTypUndParameter(u.listHinzuzufuegen, Set.of(
				List.of((long) GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ, SCHUELER_1_ID, SCHUELER_2_ID, FACH_D_ID)));
	}

	@Test
	@DisplayName("testZusammenInFachAlleBranches")
	void testZusammenInFachAlleBranches() {
		final GostBlockungsdatenManager parent = createParentManager();
		GostBlockungRegel r = new GostBlockungRegel();
		r.id = 81001;
		r.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ;
		r.parameter.add(SCHUELER_1_ID);
		r.parameter.add(SCHUELER_2_ID);
		r.parameter.add(FACH_D_ID);
		parent.regelAdd(r);
		r = new GostBlockungRegel();
		r.id = 81002;
		r.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ;
		r.parameter.add(SCHUELER_2_ID);
		r.parameter.add(SCHUELER_1_ID);
		r.parameter.add(FACH_D_ID);
		parent.regelAdd(r);
		r = new GostBlockungRegel();
		r.id = 81003;
		r.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ;
		r.parameter.add(SCHUELER_1_ID);
		r.parameter.add(SCHUELER_2_ID);
		parent.regelAdd(r);
		r = new GostBlockungRegel();
		r.id = 81004;
		r.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ;
		r.parameter.add(SCHUELER_2_ID);
		r.parameter.add(SCHUELER_1_ID);
		parent.regelAdd(r);
		r = new GostBlockungRegel();
		r.id = 81005;
		r.typ = GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ;
		r.parameter.add(SCHUELER_1_ID);
		r.parameter.add(SCHUELER_2_ID);
		parent.regelAdd(r);
		r = new GostBlockungRegel();
		r.id = 81006;
		r.typ = GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ;
		r.parameter.add(SCHUELER_2_ID);
		r.parameter.add(SCHUELER_1_ID);
		parent.regelAdd(r);
		r = new GostBlockungRegel();
		r.id = 81007;
		r.typ = GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ;
		r.parameter.add(SCHUELER_2_ID);
		r.parameter.add(SCHUELER_1_ID);
		r.parameter.add(FACH_D_ID);
		parent.regelAdd(r);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 7100);
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateSchuelerZusammenMitSchuelerInFach(SCHUELER_1_ID, SCHUELER_2_ID, FACH_D_ID);
		assertRegelListeMitTypUndParameter(u.listEntfernen, Set.of(
				List.of((long) GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ, SCHUELER_1_ID, SCHUELER_2_ID, FACH_D_ID),
				List.of((long) GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER_IN_FACH.typ, SCHUELER_2_ID, SCHUELER_1_ID, FACH_D_ID),
				List.of((long) GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ, SCHUELER_1_ID, SCHUELER_2_ID),
				List.of((long) GostKursblockungRegelTyp.SCHUELER_VERBIETEN_MIT_SCHUELER.typ, SCHUELER_2_ID, SCHUELER_1_ID),
				List.of((long) GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ, SCHUELER_1_ID, SCHUELER_2_ID),
				List.of((long) GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER.typ, SCHUELER_2_ID, SCHUELER_1_ID),
				List.of((long) GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ, SCHUELER_2_ID, SCHUELER_1_ID, FACH_D_ID)));
		assertRegelListeMitTypUndParameter(u.listHinzuzufuegen, Set.of(
				List.of((long) GostKursblockungRegelTyp.SCHUELER_ZUSAMMEN_MIT_SCHUELER_IN_FACH.typ, SCHUELER_1_ID, SCHUELER_2_ID, FACH_D_ID)));
	}

	@Test
	@DisplayName("testRemoveKursFixiereUndPatches")
	void testRemoveKursFixiereUndPatches() {
		GostBlockungsdatenManager parent = createParentManager();
		GostBlockungRegel r = new GostBlockungRegel();
		r.id = REGEL_ID_1;
		r.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
		r.parameter.add(KURS_ID_1);
		r.parameter.add((long) SCHIENE_NR_1);
		parent.regelAdd(r);
		GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 500);
		GostBlockungRegelUpdate u = mgr.regelupdateRemoveKursFixiereInEinerSchiene(KURS_ID_1, SCHIENE_NR_1);
		assertEquals(1, u.listEntfernen.size());

		parent = createParentManager();
		r = new GostBlockungRegel();
		r.id = REGEL_ID_1;
		r.typ = GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ;
		r.parameter.add(KURS_ID_1);
		r.parameter.add(30L);
		parent.regelAdd(r);
		mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 600);
		u = mgr.regelupdatePatchByIdKursMaximaleSchueleranzahl(REGEL_ID_1, KURS_ID_1, 20);
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ,
				Set.of(List.of(KURS_ID_1, 30L)));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ,
				Set.of(List.of(KURS_ID_1, 20L)));

		parent = createParentManager();
		r = new GostBlockungRegel();
		r.id = REGEL_ID_1;
		r.typ = GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ;
		r.parameter.add(KURS_ID_1);
		r.parameter.add(5L);
		parent.regelAdd(r);
		mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 700);
		u = mgr.regelupdatePatchByIdKursMitDummySusAuffuellen(REGEL_ID_1, KURS_ID_1, 8);
		assertEquals(1, u.listEntfernen.size());
		assertEquals(1, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testPatchByIdMethods")
	void testPatchByIdMethods() {
		// Patch KursMaximaleSchueleranzahl: 30 -> 20
		GostBlockungsdatenManager parent = createParentManager();
		GostBlockungRegel r = new GostBlockungRegel();
		r.id = 90001;
		r.typ = GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ;
		r.parameter.add(KURS_ID_1);
		r.parameter.add(30L);
		parent.regelAdd(r);
		GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 7500);
		GostBlockungRegelUpdate u = mgr.regelupdatePatchByIdKursMaximaleSchueleranzahl(90001, KURS_ID_1, 20);
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ,
				Set.of(List.of(KURS_ID_1, 30L)));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ,
				Set.of(List.of(KURS_ID_1, 20L)));

		// Patch KursMitDummySusAuffuellen: 5 -> 8
		parent = createParentManager();
		r = new GostBlockungRegel();
		r.id = 90002;
		r.typ = GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ;
		r.parameter.add(KURS_ID_1);
		r.parameter.add(5L);
		parent.regelAdd(r);
		mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 7600);
		u = mgr.regelupdatePatchByIdKursMitDummySusAuffuellen(90002, KURS_ID_1, 8);
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ,
				Set.of(List.of(KURS_ID_1, 5L)));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ,
				Set.of(List.of(KURS_ID_1, 8L)));

		// Patch KursartAlleinInSchienenVonBis: LK(1-2) -> LK(1-3)
		parent = createParentManager();
		r = new GostBlockungRegel();
		r.id = 90003;
		r.typ = GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ;
		r.parameter.add((long) KURSART_LK);
		r.parameter.add(1L);
		r.parameter.add(2L);
		parent.regelAdd(r);
		mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 7700);
		u = mgr.regelupdatePatchByIdKursartAlleinInSchienenVonBis(90003, KURSART_LK, 1, 3);
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ,
				Set.of(List.of((long) KURSART_LK, 1L, 2L)));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ,
				Set.of(List.of((long) KURSART_LK, 1L, 3L)));

		// Patch SchuelerFixierenInKurs: K1 -> K4
		parent = createParentManager();
		r = new GostBlockungRegel();
		r.id = 90004;
		r.typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ;
		r.parameter.add(SCHUELER_1_ID);
		r.parameter.add(KURS_ID_1);
		parent.regelAdd(r);
		mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 7800);
		u = mgr.regelupdatePatchByIdSchuelerFixierenInKurs(90004, SCHUELER_1_ID, KURS_ID_4);
		assertRegelListe(u.listEntfernen, GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ,
				Set.of(List.of(SCHUELER_1_ID, KURS_ID_1)));
		assertRegelListe(u.listHinzuzufuegen, GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ,
				Set.of(List.of(SCHUELER_1_ID, KURS_ID_4)));
	}

	@Test
	@DisplayName("testPatchByIdSameParams")
	void testPatchByIdSameParams() {
		GostBlockungsdatenManager parent = createParentManager();
		GostBlockungRegel r = new GostBlockungRegel();
		r.id = 91001;
		r.typ = GostKursblockungRegelTyp.KURS_MAXIMALE_SCHUELERANZAHL.typ;
		r.parameter.add(KURS_ID_1);
		r.parameter.add(30L);
		parent.regelAdd(r);
		GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 8000);
		GostBlockungRegelUpdate u = mgr.regelupdatePatchByIdKursMaximaleSchueleranzahl(91001, KURS_ID_1, 30);
		assertEquals(0, u.listEntfernen.size());
		assertEquals(0, u.listHinzuzufuegen.size());

		parent = createParentManager();
		r = new GostBlockungRegel();
		r.id = 91002;
		r.typ = GostKursblockungRegelTyp.KURS_MIT_DUMMY_SUS_AUFFUELLEN.typ;
		r.parameter.add(KURS_ID_1);
		r.parameter.add(5L);
		parent.regelAdd(r);
		mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 8100);
		u = mgr.regelupdatePatchByIdKursMitDummySusAuffuellen(91002, KURS_ID_1, 5);
		assertEquals(0, u.listEntfernen.size());
		assertEquals(0, u.listHinzuzufuegen.size());

		parent = createParentManager();
		r = new GostBlockungRegel();
		r.id = 91003;
		r.typ = GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ;
		r.parameter.add((long) KURSART_LK);
		r.parameter.add(1L);
		r.parameter.add(2L);
		parent.regelAdd(r);
		mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 8200);
		u = mgr.regelupdatePatchByIdKursartAlleinInSchienenVonBis(91003, KURSART_LK, 1, 2);
		assertEquals(0, u.listEntfernen.size());
		assertEquals(0, u.listHinzuzufuegen.size());

		parent = createParentManager();
		r = new GostBlockungRegel();
		r.id = 91004;
		r.typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ;
		r.parameter.add(SCHUELER_1_ID);
		r.parameter.add(KURS_ID_1);
		parent.regelAdd(r);
		mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 8300);
		u = mgr.regelupdatePatchByIdSchuelerFixierenInKurs(91004, SCHUELER_1_ID, KURS_ID_1);
		assertEquals(0, u.listEntfernen.size());
		assertEquals(0, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testPatchByIdWrongType")
	void testPatchByIdWrongType() {
		GostBlockungsdatenManager parent = createParentManager();
		GostBlockungRegel r = new GostBlockungRegel();
		r.id = 92001;
		r.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
		r.parameter.add(KURS_ID_1);
		r.parameter.add((long) SCHIENE_NR_1);
		parent.regelAdd(r);
		GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 8400);
		GostBlockungRegelUpdate u = mgr.regelupdatePatchByIdKursMaximaleSchueleranzahl(92001, KURS_ID_1, 30);
		assertEquals(0, u.listEntfernen.size());

		parent = createParentManager();
		r = new GostBlockungRegel();
		r.id = 92002;
		r.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
		r.parameter.add(KURS_ID_1);
		r.parameter.add((long) SCHIENE_NR_1);
		parent.regelAdd(r);
		mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 8500);
		u = mgr.regelupdatePatchByIdKursMitDummySusAuffuellen(92002, KURS_ID_1, 5);
		assertEquals(0, u.listEntfernen.size());

		parent = createParentManager();
		r = new GostBlockungRegel();
		r.id = 92003;
		r.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
		r.parameter.add(KURS_ID_1);
		r.parameter.add((long) SCHIENE_NR_1);
		parent.regelAdd(r);
		mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 8600);
		u = mgr.regelupdatePatchByIdSchuelerFixierenInKurs(92003, SCHUELER_1_ID, KURS_ID_1);
		assertEquals(0, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testPatchByIdFalscherTypMitUnbekanntemZiel")
	void testPatchByIdFalscherTypMitUnbekanntemZiel() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = 900;
		regel.typ = GostKursblockungRegelTyp.LEHRKRAEFTE_BEACHTEN.typ;
		parent.regelAdd(regel);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 10800);
		// Der Ziel-Kurs 999 existiert nicht. Da der Typ der alten Regel nicht passt, wird der Patch abgebrochen,
		// ohne dass die Create-Logik (und damit der Kurs-Zugriff) ausgeführt wird.
		final GostBlockungRegelUpdate u = assertDoesNotThrow(
				() -> mgr.regelupdatePatchByIdSchuelerFixierenInKurs(900, SCHUELER_1_ID, KURS_NICHT_VORHANDEN));
		assertEquals(0, u.listEntfernen.size());
		assertEquals(0, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testPatchByIdOhneFachwahlErhaeltAlteFixierung")
	void testPatchByIdOhneFachwahlErhaeltAlteFixierung() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = 901;
		regel.typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ;
		regel.parameter.add(SCHUELER_2_ID);
		regel.parameter.add(KURS_ID_4);
		parent.regelAdd(regel);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 10900);
		// S2 hat keine E-Fachwahl. Ein Patch der D-GK-Fixierung (K4) auf den E-GK-Kurs K3 darf die alte Fixierung nicht entfernen.
		final GostBlockungRegelUpdate u = mgr.regelupdatePatchByIdSchuelerFixierenInKurs(901, SCHUELER_2_ID, KURS_ID_3);
		assertEquals(0, u.listHinzuzufuegen.size());
		assertEquals(0, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testAbweichenderAbijahrgangMitKurs")
	void testAbweichenderAbijahrgangMitKurs() {
		final GostBlockungsdatenManager parent = createParentManagerMitBlockungsvorlage();
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 8700);
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));
		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> s = new HashSet<>();
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, 301L));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(s));
		final List<Schueler> abweichende = mgr.getOfSchuelerMengeMitAbweichendemAbijahrgang();
		assertEquals(1, abweichende.size());
		assertEquals(301L, abweichende.get(0).id);
	}

	@Test
	@DisplayName("testAbiturLKCatchBlock")
	void testAbiturLKCatchBlock() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 8800);
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_3, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));
		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> s = new HashSet<>();
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_3, SCHUELER_2_ID));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(s));
		assertEquals(0, mgr.getOfKursAnzahlSchuelerAbiturLK(KURS_ID_3));
	}

	@Test
	@DisplayName("testAbiturLKAbiturfach2")
	void testAbiturLKAbiturfach2() {
		final GostBlockungsdatenManager parent = createParentManager();
		for (final GostFachwahl fw : parent.schuelerGetListeOfFachwahlen(SCHUELER_1_ID)) {
			if (fw.fachID == FACH_D_ID) {
				fw.abiturfach = 2;
			}
		}
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 8900);
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));
		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> s = new HashSet<>();
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_1_ID));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(s));
		assertEquals(1, mgr.getOfKursAnzahlSchuelerAbiturLK(KURS_ID_1));
	}

	@Test
	@DisplayName("testKursSchienenVerschiebeAndereRichtung")
	void testKursSchienenVerschiebeAndereRichtung() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungsergebnisKursSchienenZuordnungUpdate u = mgr.kursSchienenUpdateVerschiebeKursVonSchieneNachSchiene(
				KURS_ID_2, SCHIENE_ID_2, SCHIENE_ID_3);
		assertNotNull(u);
		assertEquals(1, u.listEntfernen.size());
		assertEquals(1, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testBildeKerngruppenFixiertUndLeeren")
	void testBildeKerngruppenFixiertUndLeeren() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungsergebnisKursSchuelerZuordnungUpdate u = mgr.kursSchuelerUpdateBildeKerngruppen(
				KURS_ID_1, Set.of(KURS_ID_4), true, false, true);
		assertEquals(Set.of(List.of(KURS_ID_1, SCHUELER_1_ID), List.of(KURS_ID_4, SCHUELER_2_ID)), u.listEntfernen.stream()
				.map(z -> List.of(z.idKurs, z.idSchueler)).collect(java.util.stream.Collectors.toSet()));
		assertEquals(Set.of(List.of(KURS_ID_4, SCHUELER_1_ID)), u.listHinzuzufuegen.stream()
				.map(z -> List.of(z.idKurs, z.idSchueler)).collect(java.util.stream.Collectors.toSet()));
		assertEquals(0, u.regelUpdates.listEntfernen.size());
		assertRegelListe(u.regelUpdates.listHinzuzufuegen, GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, Set.of());
	}

	@Test
	@DisplayName("testBildeKerngruppenAllTrue")
	void testBildeKerngruppenAllTrue() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungsergebnisKursSchuelerZuordnungUpdate u = mgr.kursSchuelerUpdateBildeKerngruppen(
				KURS_ID_1, Set.of(KURS_ID_4), true, true, true);
		assertEquals(Set.of(List.of(KURS_ID_1, SCHUELER_1_ID), List.of(KURS_ID_4, SCHUELER_2_ID)), u.listEntfernen.stream()
				.map(z -> List.of(z.idKurs, z.idSchueler)).collect(java.util.stream.Collectors.toSet()));
		assertEquals(Set.of(List.of(KURS_ID_4, SCHUELER_1_ID)), u.listHinzuzufuegen.stream()
				.map(z -> List.of(z.idKurs, z.idSchueler)).collect(java.util.stream.Collectors.toSet()));
		assertEquals(0, u.regelUpdates.listEntfernen.size());
		assertRegelListe(u.regelUpdates.listHinzuzufuegen, GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ,
				Set.of(List.of(SCHUELER_1_ID, KURS_ID_4)));
	}

	@Test
	@DisplayName("testEntferneKursSchuelerPaareNichtImKurs")
	void testEntferneKursSchuelerPaareNichtImKurs() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungsergebnisKursSchuelerZuordnung z = DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(
				KURS_ID_5, SCHUELER_1_ID);
		final GostBlockungsergebnisKursSchuelerZuordnungUpdate u = mgr.kursSchuelerUpdateEntferneKursSchuelerPaare(Set.of(z));
		assertEquals(0, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testVerschiebeKursNichtInQuellSchiene")
	void testVerschiebeKursNichtInQuellSchiene() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungsergebnisKursSchienenZuordnungUpdate u = mgr.kursSchienenUpdateVerschiebeKursVonSchieneNachSchiene(
				KURS_ID_1, SCHIENE_ID_2, SCHIENE_ID_3);
		assertEquals(List.of(), u.listEntfernen);
		assertEquals(List.of(), u.listHinzuzufuegen);
	}

	@Test
	@DisplayName("testAbitur3ZweiSchueler")
	void testAbitur3ZweiSchueler() {
		final GostBlockungsdatenManager parent = createParentManager();
		for (final GostFachwahl fw : parent.schuelerGetListeOfFachwahlen(SCHUELER_1_ID)) {
			if (fw.fachID == FACH_D_ID) {
				fw.abiturfach = 3;
			}
		}
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 9200);
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));
		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> s = new HashSet<>();
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_1_ID));
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_2_ID));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(s));
		assertEquals(1, mgr.getOfKursAnzahlSchuelerAbitur3(KURS_ID_1));
	}

	@Test
	@DisplayName("testAbitur4ZweiSchueler")
	void testAbitur4ZweiSchueler() {
		final GostBlockungsdatenManager parent = createParentManager();
		for (final GostFachwahl fw : parent.schuelerGetListeOfFachwahlen(SCHUELER_1_ID)) {
			if (fw.fachID == FACH_D_ID) {
				fw.abiturfach = 4;
			}
		}
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 9300);
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));
		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> s = new HashSet<>();
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_1_ID));
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_2_ID));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(s));
		assertEquals(1, mgr.getOfKursAnzahlSchuelerAbitur4(KURS_ID_1));
	}

	@Test
	@DisplayName("testSchieneAnzahlSchuelerExterneMitExtern")
	void testSchieneAnzahlSchuelerExterneMitExtern() {
		final GostBlockungsdatenManager parent = createParentManagerMitExternemSchueler();
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 9400);
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));
		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> s = new HashSet<>();
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, 300L));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(s));
		assertEquals(1, mgr.getOfSchieneAnzahlSchuelerExterne(SCHIENE_ID_1));
	}

	@Test
	@DisplayName("testBildeKerngruppenGleicheFachart")
	void testBildeKerngruppenGleicheFachart() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		assertThrows(de.svws_nrw.core.exceptions.UserNotificationException.class, () -> {
			mgr.kursSchuelerUpdateBildeKerngruppen(KURS_ID_1, Set.of(KURS_ID_4, KURS_ID_1), false, false, false);
		});
	}

	@Test
	@DisplayName("testBildeKerngruppenLeerenMitFremden")
	void testBildeKerngruppenLeerenMitFremden() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungsergebnisKursSchuelerZuordnungUpdate u = mgr.kursSchuelerUpdateBildeKerngruppen(
				KURS_ID_1, Set.of(KURS_ID_4), false, false, true);
		assertNotNull(u);
		assertTrue(u.listEntfernen.size() >= 1);
	}

	@Test
	@DisplayName("testBildeKerngruppenLeerenEntferntNurFremdeSchueler")
	void testBildeKerngruppenLeerenEntferntNurFremdeSchueler() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		// S1 und S2 in K5 (M-GK1) legen: S1 gehört zur Kerngruppe (K1), S2 ist fremd.
		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> s = new HashSet<>();
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_5, SCHUELER_1_ID));
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_5, SCHUELER_2_ID));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(s));

		// Kerngruppe von K1 = {S1}; K5 enthält S1 (Kerngruppe) und S2 (fremd).
		// Korrekt: nur S2 wird aus K5 entfernt. Ein defektes removeAll würde auch S1 entfernen.
		final GostBlockungsergebnisKursSchuelerZuordnungUpdate u = mgr.kursSchuelerUpdateBildeKerngruppen(
				KURS_ID_1, Set.of(KURS_ID_5), false, false, true);
		assertEquals(1, u.listEntfernen.size());
		assertTrue(u.listEntfernen.contains(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_5, SCHUELER_2_ID)));
	}

	@Test
	@DisplayName("testBildeKerngruppenVerschiebeFixierte")
	void testBildeKerngruppenVerschiebeFixierte() {
		final GostBlockungsdatenManager parent = createParentManager();
		// S1 in K1 fixieren
		final GostBlockungRegel fix = new GostBlockungRegel();
		fix.id = 5000;
		fix.typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ;
		fix.parameter.add(SCHUELER_1_ID);
		fix.parameter.add(KURS_ID_1);
		parent.regelAdd(fix);

		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 4300);
		// K1 in Schiene 1, S1 in K1
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));

		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> s = new HashSet<>();
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_1_ID));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(s));

		// (2) Fixier-Guard: verschiebeFixierte=false -> nichts passiert
		final GostBlockungsergebnisKursSchuelerZuordnungUpdate u1 = mgr.kursSchuelerUpdateBildeKerngruppen(
				KURS_ID_1, Set.of(KURS_ID_4), false, false, false);
		assertEquals(0, u1.listEntfernen.size());
		assertEquals(0, u1.listHinzuzufuegen.size());
		assertEquals(0, u1.regelUpdates.listEntfernen.size());
		assertEquals(0, u1.regelUpdates.listHinzuzufuegen.size());

		// (3)+(4): verschiebeFixierte=true -> S1 wird verschoben, Fixierung gelöst und neu gesetzt
		final GostBlockungsergebnisKursSchuelerZuordnungUpdate u2 = mgr.kursSchuelerUpdateBildeKerngruppen(
				KURS_ID_1, Set.of(KURS_ID_4), true, true, false);
		assertEquals(1, u2.listEntfernen.size());
		assertTrue(u2.listEntfernen.contains(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_1_ID)));
		assertEquals(1, u2.listHinzuzufuegen.size());
		assertTrue(u2.listHinzuzufuegen.contains(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_4, SCHUELER_1_ID)));
		assertEquals(1, u2.regelUpdates.listEntfernen.size());
		assertEquals(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, u2.regelUpdates.listEntfernen.get(0).typ);
		assertEquals(SCHUELER_1_ID, u2.regelUpdates.listEntfernen.get(0).parameter.get(0));
		assertEquals(KURS_ID_1, u2.regelUpdates.listEntfernen.get(0).parameter.get(1));
		assertEquals(1, u2.regelUpdates.listHinzuzufuegen.size());
		assertEquals(GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ, u2.regelUpdates.listHinzuzufuegen.get(0).typ);
		assertEquals(SCHUELER_1_ID, u2.regelUpdates.listHinzuzufuegen.get(0).parameter.get(0));
		assertEquals(KURS_ID_4, u2.regelUpdates.listHinzuzufuegen.get(0).parameter.get(1));
	}

	@Test
	@DisplayName("testBildeKerngruppenOhneAltenKurs")
	void testBildeKerngruppenOhneAltenKurs() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 4301);
		// K1 in Schiene 1, S1 in K1 (D-GK1), aber in keinem M-Kurs -> kursAlt == null
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));

		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> s = new HashSet<>();
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_1_ID));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(s));

		// Verschiebe S1 nach K2 (M-LK1): S1 hat die Fachwahl M-LK, aber keinen alten M-Kurs.
		final GostBlockungsergebnisKursSchuelerZuordnungUpdate u = mgr.kursSchuelerUpdateBildeKerngruppen(
				KURS_ID_1, Set.of(KURS_ID_2), false, false, false);
		assertEquals(0, u.listEntfernen.size());
		assertEquals(1, u.listHinzuzufuegen.size());
		assertTrue(u.listHinzuzufuegen.contains(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_2, SCHUELER_1_ID)));
	}

	@Test
	@DisplayName("testKursVerbietenZusammenAlleBranches")
	void testKursVerbietenZusammenAlleBranches() {
		// PART 1: KursVerbietenMitKurs - need types 7 and 8 in both orderings
		GostBlockungsdatenManager parent = createParentManager();
		GostBlockungRegel r = new GostBlockungRegel();
		r.id = 88001;
		r.typ = GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ;
		r.parameter.add(KURS_ID_1);
		r.parameter.add(KURS_ID_3);
		parent.regelAdd(r);
		r = new GostBlockungRegel();
		r.id = 88002;
		r.typ = GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ;
		r.parameter.add(KURS_ID_3);
		r.parameter.add(KURS_ID_1);
		parent.regelAdd(r);
		r = new GostBlockungRegel();
		r.id = 88003;
		r.typ = GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ;
		r.parameter.add(KURS_ID_3);
		r.parameter.add(KURS_ID_1);
		parent.regelAdd(r);
		GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 9500);
		GostBlockungRegelUpdate u = mgr.regelupdateCreateKursVerbietenMitKurs(Set.of(KURS_ID_1, KURS_ID_3));
		assertEquals(3, u.listEntfernen.size());
		assertEquals(1, u.listHinzuzufuegen.size());

		// PART 2: KursZusammenMitKurs - need types 7 and 8 in both orderings
		parent = createParentManager();
		r = new GostBlockungRegel();
		r.id = 88004;
		r.typ = GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ;
		r.parameter.add(KURS_ID_1);
		r.parameter.add(KURS_ID_3);
		parent.regelAdd(r);
		r = new GostBlockungRegel();
		r.id = 88005;
		r.typ = GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS.typ;
		r.parameter.add(KURS_ID_3);
		r.parameter.add(KURS_ID_1);
		parent.regelAdd(r);
		r = new GostBlockungRegel();
		r.id = 88006;
		r.typ = GostKursblockungRegelTyp.KURS_ZUSAMMEN_MIT_KURS.typ;
		r.parameter.add(KURS_ID_3);
		r.parameter.add(KURS_ID_1);
		parent.regelAdd(r);
		mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 9600);
		u = mgr.regelupdateCreateKursZusammenMitKurs(Set.of(KURS_ID_1, KURS_ID_3));
		assertEquals(3, u.listEntfernen.size());
		assertEquals(1, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testEntferneKursSchuelerPaareMitFixierung")
	void testEntferneKursSchuelerPaareMitFixierung() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = 98001;
		regel.typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ;
		regel.parameter.add(SCHUELER_1_ID);
		regel.parameter.add(KURS_ID_1);
		parent.regelAdd(regel);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 9700);
		final GostBlockungsergebnisKursSchuelerZuordnung z = DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(
				KURS_ID_1, SCHUELER_1_ID);
		final GostBlockungsergebnisKursSchuelerZuordnungUpdate u = mgr.kursSchuelerUpdateEntferneKursSchuelerPaare(Set.of(z));
		assertEquals(0, u.listEntfernen.size());
		assertEquals(1, u.regelUpdates.listEntfernen.size());
	}

	@Test
	@DisplayName("testPatchByIdKursartAlleinWrongType")
	void testPatchByIdKursartAlleinWrongType() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = 98002;
		regel.typ = GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ;
		regel.parameter.add((long) KURSART_GK);
		regel.parameter.add(1L);
		regel.parameter.add(2L);
		parent.regelAdd(regel);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 9800);
		final GostBlockungRegelUpdate u = mgr.regelupdatePatchByIdKursartAlleinInSchienenVonBis(98002, KURSART_GK, 1, 3);
		assertEquals(0, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testKursFixiereInSchieneToggleRemove")
	void testKursFixiereInSchieneToggleRemove() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = 98003;
		regel.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
		regel.parameter.add(KURS_ID_1);
		regel.parameter.add((long) SCHIENE_NR_1);
		parent.regelAdd(regel);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 9900);
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateKursFixiereInSchieneToggle(
				Set.of(KURS_ID_1), Set.of(SCHIENE_NR_1));
		assertEquals(1, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testRemoveKursFixiereMengeMitFixierung")
	void testRemoveKursFixiereMengeMitFixierung() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = 95001;
		regel.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
		regel.parameter.add(KURS_ID_1);
		regel.parameter.add((long) SCHIENE_NR_1);
		parent.regelAdd(regel);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 10200);
		final GostBlockungRegelUpdate u = mgr.regelupdateRemoveKursFixiereMengeInIhrenSchienen(Set.of(KURS_ID_1));
		assertEquals(1, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testPatchByIdKursKursdifferenzSameKurs")
	void testPatchByIdKursKursdifferenzSameKurs() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = 95002;
		regel.typ = GostKursblockungRegelTyp.KURS_KURSDIFFERENZ_BEI_DER_VISUALISIERUNG_IGNORIEREN.typ;
		regel.parameter.add(KURS_ID_1);
		parent.regelAdd(regel);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 10300);
		final GostBlockungRegelUpdate u = mgr.regelupdatePatchByIdKursKursdifferenzBeiDerVisualisierungIgnorieren(
				95002, KURS_ID_1);
		assertEquals(0, u.listEntfernen.size());
		assertEquals(0, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testRemoveSchuelerFixierenToggleMitFixierung")
	void testRemoveSchuelerFixierenToggleMitFixierung() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = 95003;
		regel.typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ;
		regel.parameter.add(SCHUELER_1_ID);
		regel.parameter.add(KURS_ID_1);
		parent.regelAdd(regel);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 10400);
		final HashSet<GostBlockungsergebnisKursSchienenZuordnung> z = new HashSet<>();
		z.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(KURS_ID_1, SCHIENE_ID_1));
		mgr.kursSchienenUpdateExecute(mgr.kursSchienenUpdateFuegeKursSchienenPaareHinzu(z));
		final HashSet<GostBlockungsergebnisKursSchuelerZuordnung> s = new HashSet<>();
		s.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(KURS_ID_1, SCHUELER_1_ID));
		mgr.kursSchuelerUpdateExecute(mgr.kursSchuelerUpdateFuegeKursSchuelerPaareHinzu(s));
		final GostBlockungRegelUpdate u = mgr.regelupdateRemoveSchuelerFixierenInDenKursenToggle(Set.of(KURS_ID_1));
		assertEquals(1, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testRemoveSchuelerFixierenInKursMitFixierung")
	void testRemoveSchuelerFixierenInKursMitFixierung() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = 96001;
		regel.typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ;
		regel.parameter.add(SCHUELER_1_ID);
		regel.parameter.add(KURS_ID_1);
		parent.regelAdd(regel);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 10500);
		final GostBlockungRegelUpdate u = mgr.regelupdateRemoveSchuelerFixierenInKurs(
				Set.of(SCHUELER_1_ID), Set.of(KURS_ID_1));
		assertEquals(1, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testKursartSperreMitBestehendenSperren")
	void testKursartSperreMitBestehendenSperren() {
		final GostBlockungsdatenManager parent = createParentManager();
		GostBlockungRegel r = new GostBlockungRegel();
		r.id = 96002;
		r.typ = GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ;
		r.parameter.add(KURS_ID_1);
		r.parameter.add(1L);
		parent.regelAdd(r);
		r = new GostBlockungRegel();
		r.id = 96003;
		r.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
		r.parameter.add(KURS_ID_1);
		r.parameter.add(1L);
		parent.regelAdd(r);
		r = new GostBlockungRegel();
		r.id = 96004;
		r.typ = GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ;
		r.parameter.add((long) KURSART_GK);
		r.parameter.add(2L);
		r.parameter.add(1L);
		parent.regelAdd(r);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 10600);
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateKursartSperreSchienenVonBis(KURSART_GK, 1, 2);
		assertTrue(u.listEntfernen.size() >= 2);
		assertEquals(1, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testPatchByIdSchuelerFixierenWrongType")
	void testPatchByIdSchuelerFixierenWrongType() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel regel = new GostBlockungRegel();
		regel.id = 96005;
		regel.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ;
		regel.parameter.add(SCHUELER_1_ID);
		regel.parameter.add(KURS_ID_1);
		parent.regelAdd(regel);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 10700);
		final GostBlockungRegelUpdate u = mgr.regelupdatePatchByIdSchuelerFixierenInKurs(96005, SCHUELER_1_ID, KURS_ID_1);
		assertEquals(0, u.listEntfernen.size());
		assertEquals(0, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testKursMaxSchueleranzahlWrongType")
	void testKursMaxSchueleranzahlWrongType() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel r = new GostBlockungRegel();
		r.id = 97001;
		r.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
		r.parameter.add(KURS_ID_1);
		r.parameter.add((long) SCHIENE_NR_1);
		parent.regelAdd(r);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 10800);
		final GostBlockungRegelUpdate u = mgr.regelupdatePatchByIdKursMaximaleSchueleranzahl(97001, KURS_ID_1, 20);
		assertEquals(0, u.listEntfernen.size());
		assertEquals(0, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testDummySusWrongType")
	void testDummySusWrongType() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel r = new GostBlockungRegel();
		r.id = 97002;
		r.typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE.typ;
		r.parameter.add(KURS_ID_1);
		r.parameter.add((long) SCHIENE_NR_1);
		parent.regelAdd(r);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 10900);
		final GostBlockungRegelUpdate u = mgr.regelupdatePatchByIdKursMitDummySusAuffuellen(97002, KURS_ID_1, 5);
		assertEquals(0, u.listEntfernen.size());
		assertEquals(0, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testRemoveKursSperreMitSperre")
	void testRemoveKursSperreMitSperre() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel r = new GostBlockungRegel();
		r.id = 97003;
		r.typ = GostKursblockungRegelTyp.KURS_SPERRE_IN_SCHIENE.typ;
		r.parameter.add(KURS_ID_1);
		r.parameter.add((long) SCHIENE_NR_1);
		parent.regelAdd(r);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 11000);
		final GostBlockungRegelUpdate u = mgr.regelupdateRemoveKursSperreInSchiene(
				Set.of(KURS_ID_1), Set.of(SCHIENE_NR_1));
		assertEquals(1, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testSchuelerFixierenWrongType")
	void testSchuelerFixierenWrongType() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel r = new GostBlockungRegel();
		r.id = 97004;
		r.typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS.typ;
		r.parameter.add(SCHUELER_1_ID);
		r.parameter.add(KURS_ID_1);
		parent.regelAdd(r);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 11100);
		final GostBlockungRegelUpdate u = mgr.regelupdatePatchByIdSchuelerFixierenInKurs(97004, SCHUELER_1_ID, KURS_ID_1);
		assertEquals(0, u.listEntfernen.size());
	}

	@Test
	@DisplayName("testPatchKursSchienenAnzahlMaxSchienen")
	void testPatchKursSchienenAnzahlMaxSchienen() {
		final GostBlockungsdatenManager parent = createParentManagerMitBlockungsvorlage();
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 11200);
		assertThrows(de.svws_nrw.core.exceptions.DeveloperNotificationException.class, () -> {
			mgr.patchOfKursSchienenAnzahl(KURS_ID_1, 4);
		});
	}

	@Test
	@DisplayName("testKursartAlleinMitBestehenderRegel")
	void testKursartAlleinMitBestehenderRegel() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel r = new GostBlockungRegel();
		r.id = 98004;
		r.typ = GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS.typ;
		r.parameter.add((long) KURSART_LK);
		r.parameter.add(1L);
		r.parameter.add(2L);
		parent.regelAdd(r);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 11300);
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateKursartAlleinInSchienenVonBis(KURSART_LK, 3, 4);
		assertEquals(1, u.listEntfernen.size());
		assertEquals(1, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testKursFixiereMarkiertNichtZugeordnet")
	void testKursFixiereMarkiertNichtZugeordnet() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		final GostBlockungRegelUpdate u = mgr.regelupdateCreateKursFixiereInSchieneMarkiert(
				Set.of(KURS_ID_1), Set.of(SCHIENE_NR_2));
		assertEquals(0, u.listEntfernen.size());
		assertEquals(0, u.listHinzuzufuegen.size());
		// No-op: K1 is not assigned to Schiene 2, so it is not fixiert there.
		assertFalse(mgr.getOfKursSchienenmenge(KURS_ID_1).stream().anyMatch(s -> s.id == SCHIENE_ID_2));
	}

	@Test
	@DisplayName("testFilterGeschlecht")
	void testFilterGeschlecht() {
		final GostBlockungsergebnisManager mgr = createStandardManager();
		assertEquals(2, mgr.getOfSchuelerAnzahlGefiltert(-1, -1, -1, 0, "", Geschlecht.W, null));
		assertEquals(0, mgr.getOfSchuelerAnzahlGefiltert(-1, -1, -1, 0, "", Geschlecht.M, null));
	}

	@Test
	@DisplayName("testKursartAlleinPatchWrongType")
	void testKursartAlleinPatchWrongType() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel r = new GostBlockungRegel();
		r.id = 99001;
		r.typ = GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS.typ;
		r.parameter.add((long) KURSART_GK);
		r.parameter.add(1L);
		r.parameter.add(2L);
		parent.regelAdd(r);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 11400);
		final GostBlockungRegelUpdate u = mgr.regelupdatePatchByIdKursartAlleinInSchienenVonBis(99001, KURSART_GK, 1, 3);
		assertEquals(0, u.listEntfernen.size());
		assertEquals(0, u.listHinzuzufuegen.size());
	}

	@Test
	@DisplayName("testSchuelerVerbietenPatchWrongType")
	void testSchuelerVerbietenPatchWrongType() {
		final GostBlockungsdatenManager parent = createParentManager();
		final GostBlockungRegel r = new GostBlockungRegel();
		r.id = 99002;
		r.typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS.typ;
		r.parameter.add(SCHUELER_1_ID);
		r.parameter.add(KURS_ID_1);
		parent.regelAdd(r);
		final GostBlockungsergebnisManager mgr = new GostBlockungsergebnisManager(parent, ERGEBNIS_ID_1 + 11500);
		final GostBlockungRegelUpdate u = mgr.regelupdatePatchByIdSchuelerVerbietenInKurs(99002, SCHUELER_1_ID, KURS_ID_1);
		assertEquals(0, u.listEntfernen.size());
	}
}
