package de.nrw.schule.svws.core.stundenplanblockung;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.nrw.schule.svws.core.utils.stundenplanblockung.StundenplanblockungManager;
import de.nrw.schule.svws.core.utils.stundenplanblockung.StundenplanblockungManagerKlasse;
import de.nrw.schule.svws.core.utils.stundenplanblockung.StundenplanblockungManagerKopplung;
import de.nrw.schule.svws.core.utils.stundenplanblockung.StundenplanblockungManagerLehrkraft;
import de.nrw.schule.svws.core.utils.stundenplanblockung.StundenplanblockungManagerLerngruppe;
import de.nrw.schule.svws.core.utils.stundenplanblockung.StundenplanblockungManagerRaum;

/**
 * Diese Klasse testet den {@link StundenplanblockungManager}.
 */
@DisplayName("Diese Klasse testet den {@link StundenplanblockungManager}.")
@TestMethodOrder(MethodOrderer.MethodName.class)
public class StundenplanblockungTests {

	private static final long _SEED = 1L;
	private static final int _ANZAHL_MANAGER_TESTS = 2;
	private static final int _ANZAHL_MANAGER_SUB_TESTS = 1000000;
	private static final int _MAX_LEHRKRAEFTE = 50;
	private static final int _MAX_KLASSEN = 50;
	private static final int _MAX_FAECHER = 20;
	private static final int _MAX_RAEUME = 90;
	private static final int _MAX_KOPPLUNGEN = 40;
	private static final int _MAX_LERNGRUPPEN = 800;

	/** 
	 * Diese Klasse testet den {@link StundenplanblockungManager} mit randomisierten Daten.
	 */
	@DisplayName("Diese Klasse testet den {@link StundenplanblockungManager} mit randomisierten Daten.")
	@Test
	public void testManagerRandom() {
		Random lRandom = new Random(_SEED);
		for (int i = 0; i < _ANZAHL_MANAGER_TESTS; i++)
			subTestManagerZufaellig(lRandom);
	}

	private static void subTestManagerZufaellig(Random pRandom) {
		StundenplanblockungManager manager = new StundenplanblockungManager();

		for (int i = 0; i < _ANZAHL_MANAGER_SUB_TESTS; i++)
			subTestManagerSubZufaellig(manager, pRandom);

		manager.miscCheckConsistencyOrException();
	}

	private static void subTestManagerSubZufaellig(StundenplanblockungManager pMan, Random pRandom) {
		subTestLehrkraefte(pMan, pRandom);
		subTestKlassen(pMan, pRandom);
		subTestFaecher(pMan, pRandom);
		subTestRaeume(pMan, pRandom);
		subTestKopplungen(pMan, pRandom);
		subTestLerngruppen(pMan, pRandom);
	}


	private static void subTestLehrkraefte(StundenplanblockungManager pMan, Random pRandom) {
		// exists, removeOrException, addOrException
		long lehrkraftID = pRandom.nextLong(_MAX_LEHRKRAEFTE);
		if (pMan.getLehrkraefte().exists(lehrkraftID)) {
			pMan.getLehrkraefte().removeOrException(lehrkraftID);
			if (pMan.getLehrkraefte().exists(lehrkraftID))
				fail("Lehrkraft sollte nicht mehr existieren!");
		} else {
			pMan.getLehrkraefte().addOrException(lehrkraftID, "");
			if (!pMan.getLehrkraefte().exists(lehrkraftID))
				fail("Lehrkraft sollte nun existieren!");
		}
		// getRandomOrException --> setKuerzel
		if (pMan.getLehrkraefte().size() > 0) {
			StundenplanblockungManagerLehrkraft le = pMan.getLehrkraefte().getRandomOrException(pRandom);
			le.setKuerzel("Lehrkraft " + le.getID() + "-" + pRandom.nextInt(_MAX_LEHRKRAEFTE));
		}
	}

	private static void subTestKlassen(StundenplanblockungManager pMan, Random pRandom) {
		// exists, removeOrException, addOrException
		long klasseID = pRandom.nextLong(_MAX_KLASSEN);
		if (pMan.getKlassen().exists(klasseID)) {
			pMan.getKlassen().removeOrException(klasseID);
			if (pMan.getKlassen().exists(klasseID))
				fail("Klasse sollte nicht mehr existieren!");
		} else {
			pMan.getKlassen().addOrException(klasseID, "");
			if (!pMan.getKlassen().exists(klasseID))
				fail("Klasse sollte nun existieren!");
		}
		// getRandomOrException --> setKuerzel
		if (pMan.getKlassen().size() > 0) {
			StundenplanblockungManagerKlasse kl = pMan.getKlassen().getRandomOrException(pRandom);
			kl.setKuerzel("Klasse " + kl.getID() + "-" + pRandom.nextInt(_MAX_KLASSEN));
		}
	}
	
	private static void subTestFaecher(StundenplanblockungManager pMan, Random pRandom) {
		// exists, removeOrException, addOrException
		long fachID = pRandom.nextLong(_MAX_FAECHER);
		if (pMan.getFaecher().exists(fachID)) {
			pMan.getFaecher().removeOrException(fachID);
			if (pMan.getFaecher().exists(fachID))
				fail("Fach sollte nicht mehr existieren!");
		} else {
			pMan.getFaecher().addOrException(fachID, "");
			if (!pMan.getFaecher().exists(fachID))
				fail("Fach sollte nun existieren!");
		}
	}

	private static void subTestRaeume(StundenplanblockungManager pMan, Random pRandom) {
		switch (pRandom.nextInt(2)) {
			case 0 -> {
				long raumID = pRandom.nextLong(_MAX_RAEUME);
				if (pMan.getRaeume().exists(raumID)) {
					pMan.getRaeume().removeOrException(raumID);
					if (pMan.getRaeume().exists(raumID))
						fail("Raum sollte nicht mehr existieren!");
				} else {
					pMan.getRaeume().addOrException(raumID, "");
					if (!pMan.getRaeume().exists(raumID))
						fail("Raum sollte nun existieren!");
				}
			}
			case 1 -> {
				if (pMan.getRaeume().size() > 0) {
					StundenplanblockungManagerRaum ra = pMan.getRaeume().getRandomOrException(pRandom);
					ra.setKuerzel("Raum " + ra.getID() + "-" + pRandom.nextInt(_MAX_RAEUME));
				}
			}
		}
	}

	private static void subTestKopplungen(StundenplanblockungManager pMan, Random pRandom) {
		switch (pRandom.nextInt(2)) {
			case 0 -> {
				long kopplungID = pRandom.nextLong(_MAX_KOPPLUNGEN);
				if (pMan.getKopplungen().exists(kopplungID)) {
					pMan.getKopplungen().removeOrException(kopplungID);
					if (pMan.getKopplungen().exists(kopplungID))
						fail("Kopplung sollte nicht mehr existieren!");
				} else {
					pMan.getKopplungen().addOrException(kopplungID, "");
					if (!pMan.getKopplungen().exists(kopplungID))
						fail("Kopplung sollte nun existieren!");
				}
			}
			case 1 -> {
				if (pMan.getKopplungen().size() > 0) {
					StundenplanblockungManagerKopplung ko = pMan.getKopplungen().getRandomOrException(pRandom);
					ko.setKuerzel("Kopplung " + ko.getID() + "-" + pRandom.nextInt(_MAX_KOPPLUNGEN));
				}
			}
		}
	}

	private static void subTestLerngruppen(StundenplanblockungManager pMan, Random pRandom) {
		// exists, removeOrException, addOrException
		long lerngruppeID = pRandom.nextLong(_MAX_LERNGRUPPEN);
		if (pMan.getLerngruppen().exists(lerngruppeID) == true) {
			pMan.getLerngruppen().removeOrException(lerngruppeID);
			if (pMan.getLerngruppen().exists(lerngruppeID))
				fail("Kopplung sollte nicht mehr existieren!");
		} else {
			pMan.getLerngruppen().createOrException(lerngruppeID);
			if (!pMan.getLerngruppen().exists(lerngruppeID))
				fail("Kopplung sollte nun existieren!");
		}
		// size
		if (pMan.getLerngruppen().size() > 0) {
			StundenplanblockungManagerLerngruppe gr = pMan.getLerngruppen().getRandomOrException(pRandom);
			// hasLehrkraft, removeLehrkraftOrException, addLehrkraftOrException
			if (pMan.getLehrkraefte().size() > 0) {
				StundenplanblockungManagerLehrkraft le = pMan.getLehrkraefte().getRandomOrException(pRandom);
				if (gr.hasLehrkraft(le) == true)
					gr.removeLehrkraftOrException(le);
				else
					gr.addLehrkraftOrException(le);
			}
			// hasKlasse, removeKlasseOrException, addKlasseOrException
			if (pMan.getKlassen().size() > 0) {
				StundenplanblockungManagerKlasse kl = pMan.getKlassen().getRandomOrException(pRandom);
				if (gr.hasKlasse(kl) == true)
					gr.removeKlasseOrException(kl);
				else
					gr.addKlasseOrException(kl);
			}
		}

	}

	/** 
	 * Erstellt Pseudo-Daten für den Stundenplanblockungs-Algorithmus.
	 */
	@DisplayName("Erstellt Pseudo-Daten für den Stundenplanblockungs-Algorithmus.")
	@Test
	public void testStundenplanblockungsAlgorithmus() {
		StundenplanblockungManager man = new StundenplanblockungManager();
		
		// Lehrkräfte anonymisiert erstellen.
		for (int leID = 1; leID <= 58; leID++)
			man.getLehrkraefte().addOrException(leID, "");
		
		// Klassen erstellen.
		man.getKlassen().addOrException( 1, "05a");
		man.getKlassen().addOrException( 2, "05b");
		man.getKlassen().addOrException( 3, "05c");
		man.getKlassen().addOrException( 4, "05d");
		man.getKlassen().addOrException( 5, "06a");
		man.getKlassen().addOrException( 6, "06b");
		man.getKlassen().addOrException( 7, "06c");
		man.getKlassen().addOrException( 8, "07a");
		man.getKlassen().addOrException( 9, "07b");
		man.getKlassen().addOrException(10, "07c");
		man.getKlassen().addOrException(11, "07d");
		man.getKlassen().addOrException(12, "08a");
		man.getKlassen().addOrException(13, "08b");
		man.getKlassen().addOrException(14, "08c");
		man.getKlassen().addOrException(15, "09a");
		man.getKlassen().addOrException(16, "09b");
		man.getKlassen().addOrException(17, "09c");
		man.getKlassen().addOrException(18, "09d");
		man.getKlassen().addOrException(19, "EF");
		man.getKlassen().addOrException(20, "Q1");
		man.getKlassen().addOrException(21, "Q2");
		
		// Fächer erstellen.
		man.getFaecher().addOrException(1,  "BI");
		man.getFaecher().addOrException(2,  "M");
		man.getFaecher().addOrException(3,  "MU");
		man.getFaecher().addOrException(4,  "E");
		
		// Klassen erstellen.
		man.getRaeume().addOrException( 1, "AM1");
		man.getRaeume().addOrException( 2, "AM2");
		man.getRaeume().addOrException( 3, "AM3");
		man.getRaeume().addOrException( 4, "AO1");
		man.getRaeume().addOrException( 5, "AO2");
		man.getRaeume().addOrException( 6, "AO3");
		man.getRaeume().addOrException( 7, "AU1");
		man.getRaeume().addOrException( 8, "AU3");
		man.getRaeume().addOrException( 9, "BK4");
		man.getRaeume().addOrException(10, "BK5");
		man.getRaeume().addOrException(12, "BM2");
		man.getRaeume().addOrException(13, "BM3");
		man.getRaeume().addOrException(14, "BO1");
		man.getRaeume().addOrException(15, "BO2");
		man.getRaeume().addOrException(16, "BO3");
		man.getRaeume().addOrException(17, "BU1");
		man.getRaeume().addOrException(18, "BU3");
		man.getRaeume().addOrException(19, "CM1");
		man.getRaeume().addOrException(20, "CM2");
		man.getRaeume().addOrException(21, "CM3");
		man.getRaeume().addOrException(22, "CM4");
		man.getRaeume().addOrException(23, "CO1");
		man.getRaeume().addOrException(24, "CO2");
		man.getRaeume().addOrException(25, "CO3");
		man.getRaeume().addOrException(26, "CO4/5");
		man.getRaeume().addOrException(27, "CO6");
		man.getRaeume().addOrException(28, "CO7");
		man.getRaeume().addOrException(29, "CU2");
		man.getRaeume().addOrException(30, "CU3");
		man.getRaeume().addOrException(31, "CU5");
		man.getRaeume().addOrException(33, "Aula");
		man.getRaeume().addOrException(36, "CH-Alt");
		man.getRaeume().addOrException(37, "CH-Neu");
		man.getRaeume().addOrException(38, "Ph-A");
		man.getRaeume().addOrException(39, "Ph-B");
		
		// Objekte sammeln
		StundenplanblockungManagerLehrkraft le55 = man.getLehrkraefte().getOrException(55);
		// StundenplanblockungManagerLehrkraft le27 = man.getLehrkraefte().getOrException(27);
		
		StundenplanblockungManagerKlasse kl05a = man.getKlassen().getOrException(1);
		
		// Lerngruppen erstellen
		// TODO BAR
		StundenplanblockungManagerLerngruppe gr001 = man.getLerngruppen().createOrException(1); 
		gr001.addLehrkraftOrException(le55);
		gr001.addKlasseOrException(kl05a);
		
	}

}