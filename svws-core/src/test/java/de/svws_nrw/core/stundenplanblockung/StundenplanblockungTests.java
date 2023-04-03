package de.svws_nrw.core.stundenplanblockung;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManager;
import de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerKlasse;
import de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerKopplung;
import de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerLehrkraft;
import de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerLerngruppe;
import de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerRaum;

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
	@DisplayName("testManagerRandom")
	@Test
	public void testManagerRandom() {
		final Random lRandom = new Random(_SEED);
		for (int i = 0; i < _ANZAHL_MANAGER_TESTS; i++)
			subTestManagerZufaellig(lRandom);
	}

	private static void subTestManagerZufaellig(final Random pRandom) {
		final StundenplanblockungManager manager = new StundenplanblockungManager();

		for (int i = 0; i < _ANZAHL_MANAGER_SUB_TESTS; i++)
			subTestManagerSubZufaellig(manager, pRandom);

		manager.miscCheckConsistencyOrException();
	}

	private static void subTestManagerSubZufaellig(final StundenplanblockungManager pMan, final Random pRandom) {
		subTestLehrkraefte(pMan, pRandom);
		subTestKlassen(pMan, pRandom);
		subTestFaecher(pMan, pRandom);
		subTestRaeume(pMan, pRandom);
		subTestKopplungen(pMan, pRandom);
		subTestLerngruppen(pMan, pRandom);
	}

	private static void subTestLehrkraefte(final StundenplanblockungManager pMan, final Random pRandom) {
		// exists, removeOrException, addOrException
		final long lehrkraftID = pRandom.nextLong(_MAX_LEHRKRAEFTE);
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
			final StundenplanblockungManagerLehrkraft le = pMan.getLehrkraefte().getRandomOrException(pRandom);
			le.setKuerzel("Lehrkraft " + le.getID() + "-" + pRandom.nextInt(_MAX_LEHRKRAEFTE));
		}
	}

	private static void subTestKlassen(final StundenplanblockungManager pMan, final Random pRandom) {
		// exists, removeOrException, addOrException
		final long klasseID = pRandom.nextLong(_MAX_KLASSEN);
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
			final StundenplanblockungManagerKlasse kl = pMan.getKlassen().getRandomOrException(pRandom);
			kl.setKuerzel("Klasse " + kl.getID() + "-" + pRandom.nextInt(_MAX_KLASSEN));
		}
	}

	private static void subTestFaecher(final StundenplanblockungManager pMan, final Random pRandom) {
		// exists, removeOrException, addOrException
		final long fachID = pRandom.nextLong(_MAX_FAECHER);
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

	private static void subTestRaeume(final StundenplanblockungManager pMan, final Random pRandom) {
		switch (pRandom.nextInt(2)) {
			case 0 -> {
				final long raumID = pRandom.nextLong(_MAX_RAEUME);
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
					final StundenplanblockungManagerRaum ra = pMan.getRaeume().getRandomOrException(pRandom);
					ra.setKuerzel("Raum " + ra.getID() + "-" + pRandom.nextInt(_MAX_RAEUME));
				}
			}
		}
	}

	private static void subTestKopplungen(final StundenplanblockungManager pMan, final Random pRandom) {
		switch (pRandom.nextInt(2)) {
			case 0 -> {
				final long kopplungID = pRandom.nextLong(_MAX_KOPPLUNGEN);
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
					final StundenplanblockungManagerKopplung ko = pMan.getKopplungen().getRandomOrException(pRandom);
					ko.setKuerzel("Kopplung " + ko.getID() + "-" + pRandom.nextInt(_MAX_KOPPLUNGEN));
				}
			}
		}
	}

	private static void subTestLerngruppen(final StundenplanblockungManager pMan, final Random pRandom) {
		// exists, removeOrException, addOrException
		final long lerngruppeID = pRandom.nextLong(_MAX_LERNGRUPPEN);
		if (pMan.getLerngruppen().exists(lerngruppeID)) {
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
			final StundenplanblockungManagerLerngruppe gr = pMan.getLerngruppen().getRandomOrException(pRandom);
			// hasLehrkraft, removeLehrkraftOrException, addLehrkraftOrException
			if (pMan.getLehrkraefte().size() > 0) {
				final StundenplanblockungManagerLehrkraft le = pMan.getLehrkraefte().getRandomOrException(pRandom);
				if (gr.hasLehrkraft(le))
					gr.removeLehrkraftOrException(le);
				else
					gr.addLehrkraftOrException(le);
			}
			// hasKlasse, removeKlasseOrException, addKlasseOrException
			if (pMan.getKlassen().size() > 0) {
				final StundenplanblockungManagerKlasse kl = pMan.getKlassen().getRandomOrException(pRandom);
				if (gr.hasKlasse(kl))
					gr.removeKlasseOrException(kl);
				else
					gr.addKlasseOrException(kl);
			}
		}

	}

	/**
	 * Diese Klasse testet den {@link StundenplanblockungManager} mit echten Testdaten.
	 */
	@DisplayName("testEchteDaten1")
	@Test
	public void testEchteDaten1() {
		final StundenplanblockungManager man = new StundenplanblockungManager();

		// Alle Lehrkräfte hinzufügen.
		man.getLehrkraefte().addOrException(18, "BCS");
		man.getLehrkraefte().addOrException(12, "BMC");
		man.getLehrkraefte().addOrException(36, "BQQ");
		man.getLehrkraefte().addOrException(37, "CBDI");
		man.getLehrkraefte().addOrException(24, "CBS");
		man.getLehrkraefte().addOrException(28, "CIS");
		man.getLehrkraefte().addOrException(77, "CPA");
		man.getLehrkraefte().addOrException(16, "EFB");
		man.getLehrkraefte().addOrException(17, "FIBT");
		man.getLehrkraefte().addOrException(38, "GFIS");
		man.getLehrkraefte().addOrException(21, "GHST");
		man.getLehrkraefte().addOrException(1, "GSPI");
		man.getLehrkraefte().addOrException(22, "HFXB1");
		man.getLehrkraefte().addOrException(27, "HPFS");
		man.getLehrkraefte().addOrException(39, "HSF");
		man.getLehrkraefte().addOrException(23, "HST");
		man.getLehrkraefte().addOrException(30, "HZXB1");
		man.getLehrkraefte().addOrException(33, "HZXB2");
		man.getLehrkraefte().addOrException(67, "HZXB3");
		man.getLehrkraefte().addOrException(40, "IBZ");
		man.getLehrkraefte().addOrException(2, "IP");
		man.getLehrkraefte().addOrException(6, "IPF");
		man.getLehrkraefte().addOrException(41, "IPS");
		man.getLehrkraefte().addOrException(42, "IVFO");
		man.getLehrkraefte().addOrException(7, "IA");
		man.getLehrkraefte().addOrException(71, "IA2");
		man.getLehrkraefte().addOrException(43, "JIN");
		man.getLehrkraefte().addOrException(31, "JMC");
		man.getLehrkraefte().addOrException(4, "LJF");
		man.getLehrkraefte().addOrException(44, "LMT");
		man.getLehrkraefte().addOrException(45, "LVO");
		man.getLehrkraefte().addOrException(29, "MBOH");
		man.getLehrkraefte().addOrException(25, "MFJ");
		man.getLehrkraefte().addOrException(32, "MFU");
		man.getLehrkraefte().addOrException(34, "MHF");
		man.getLehrkraefte().addOrException(46, "MPX");
		man.getLehrkraefte().addOrException(20, "MVE");
		man.getLehrkraefte().addOrException(47, "NSN");
		man.getLehrkraefte().addOrException(76, "OFF");
		man.getLehrkraefte().addOrException(48, "PGH");
		man.getLehrkraefte().addOrException(49, "QVOH");
		man.getLehrkraefte().addOrException(50, "SFOL");
		man.getLehrkraefte().addOrException(8, "SPTU");
		man.getLehrkraefte().addOrException(19, "TBFG");
		man.getLehrkraefte().addOrException(13, "TBHP");
		man.getLehrkraefte().addOrException(35, "TBI");
		man.getLehrkraefte().addOrException(51, "TDIF");
		man.getLehrkraefte().addOrException(52, "TDIS");
		man.getLehrkraefte().addOrException(9, "TNS");
		man.getLehrkraefte().addOrException(53, "TSG");
		man.getLehrkraefte().addOrException(10, "TUI");
		man.getLehrkraefte().addOrException(26, "UBV");
		man.getLehrkraefte().addOrException(65, "UBV2");
		man.getLehrkraefte().addOrException(54, "UBVI");
		man.getLehrkraefte().addOrException(55, "UIFJ");
		man.getLehrkraefte().addOrException(66, "UJF");
		man.getLehrkraefte().addOrException(56, "VUC");
		man.getLehrkraefte().addOrException(57, "WPPS");
		man.getLehrkraefte().addOrException(5, "XBMU");
		man.getLehrkraefte().addOrException(3, "XCT");
		man.getLehrkraefte().addOrException(58, "XJO");
		man.getLehrkraefte().addOrException(59, "XPMG");
		man.getLehrkraefte().addOrException(60, "XVF");
		man.getLehrkraefte().addOrException(75, "ACSY");
		man.getLehrkraefte().addOrException(74, "AHFO");
		man.getLehrkraefte().addOrException(73, "ALZB");
		man.getLehrkraefte().addOrException(72, "ATCM");
		man.getLehrkraefte().addOrException(68, "_QI6");
		man.getLehrkraefte().addOrException(69, "_QI8");
		man.getLehrkraefte().addOrException(70, "_QI9");

		// Alle Klassen hinzufügen.
		man.getKlassen().addOrException(18, "05a");
		man.getKlassen().addOrException(19, "05b");
		man.getKlassen().addOrException(20, "05c");
		man.getKlassen().addOrException(21, "05d");
		man.getKlassen().addOrException(17, "06a");
		man.getKlassen().addOrException(16, "06b");
		man.getKlassen().addOrException(15, "06c");
		man.getKlassen().addOrException(11, "07a");
		man.getKlassen().addOrException(12, "07b");
		man.getKlassen().addOrException(13, "07c");
		man.getKlassen().addOrException(14, "07d");
		man.getKlassen().addOrException(8, "08a");
		man.getKlassen().addOrException(9, "08b");
		man.getKlassen().addOrException(10, "08c");
		man.getKlassen().addOrException(4, "09a");
		man.getKlassen().addOrException(5, "09b");
		man.getKlassen().addOrException(6, "09c");
		man.getKlassen().addOrException(7, "09d");
		man.getKlassen().addOrException(22, "AUF");
		man.getKlassen().addOrException(3, "EF");
		man.getKlassen().addOrException(2, "Q1");
		man.getKlassen().addOrException(1, "Q2");

		// Alle Fächer hinzufügen.
		man.getFaecher().addOrException(40, "AG");
		man.getFaecher().addOrException(5, "BI");
		man.getFaecher().addOrException(15, "CH");
		man.getFaecher().addOrException(1, "D");
		man.getFaecher().addOrException(38, "Dummy");
		man.getFaecher().addOrException(2, "E5");
		man.getFaecher().addOrException(6, "EK");
		man.getFaecher().addOrException(10, "ER");
		man.getFaecher().addOrException(21, "F0");
		man.getFaecher().addOrException(16, "F6");
		man.getFaecher().addOrException(33, "F7");
		man.getFaecher().addOrException(42, "För");
		man.getFaecher().addOrException(3, "GE");
		man.getFaecher().addOrException(29, "GEbi");
		man.getFaecher().addOrException(30, "GeWi");
		man.getFaecher().addOrException(18, "IF");
		man.getFaecher().addOrException(36, "KL");
		man.getFaecher().addOrException(11, "KR");
		man.getFaecher().addOrException(12, "KU");
		man.getFaecher().addOrException(22, "L0");
		man.getFaecher().addOrException(17, "L6");
		man.getFaecher().addOrException(34, "L7");
		man.getFaecher().addOrException(20, "LI");
		man.getFaecher().addOrException(43, "LZ");
		man.getFaecher().addOrException(4, "M");
		man.getFaecher().addOrException(28, "MI");
		man.getFaecher().addOrException(23, "MU");
		man.getFaecher().addOrException(41, "Mia");
		man.getFaecher().addOrException(31, "NW");
		man.getFaecher().addOrException(7, "PA");
		man.getFaecher().addOrException(8, "PH");
		man.getFaecher().addOrException(13, "PL");
		man.getFaecher().addOrException(32, "PPL");
		man.getFaecher().addOrException(35, "RE");
		man.getFaecher().addOrException(19, "S0");
		man.getFaecher().addOrException(9, "SP");
		man.getFaecher().addOrException(14, "SW");
		man.getFaecher().addOrException(39, "Schwi");
		man.getFaecher().addOrException(24, "VE");
		man.getFaecher().addOrException(25, "VE_K");
		man.getFaecher().addOrException(26, "VM");
		man.getFaecher().addOrException(27, "VM_I");
		man.getFaecher().addOrException(37, "WP");

		// Alle Räume hinzufügen.
		man.getRaeume().addOrException(20, "AO1");
		man.getRaeume().addOrException(7, "Aula");
		man.getRaeume().addOrException(5, "BK04");
		man.getRaeume().addOrException(8, "BK05");
		man.getRaeume().addOrException(21, "Bereit1");
		man.getRaeume().addOrException(16, "Bi-HS");
		man.getRaeume().addOrException(15, "Bi-Ü");
		man.getRaeume().addOrException(10, "Ch-Alt");
		man.getRaeume().addOrException(11, "Ch-Neu");
		man.getRaeume().addOrException(12, "MiaR1");
		man.getRaeume().addOrException(13, "MiaR2");
		man.getRaeume().addOrException(14, "MiaR3");
		man.getRaeume().addOrException(17, "Ph-A");
		man.getRaeume().addOrException(18, "Ph-B");
		man.getRaeume().addOrException(9, "S2");
		man.getRaeume().addOrException(19, "S3");
		man.getRaeume().addOrException(1, "Schwi1");
		man.getRaeume().addOrException(2, "Schwi2");
		man.getRaeume().addOrException(3, "SpH1");
		man.getRaeume().addOrException(4, "SpH2");
		man.getRaeume().addOrException(6, "Z. Saal");

		// Alle Kopplungen hinzufügen.
		man.getKopplungen().addOrException(46, "5För");
		man.getKopplungen().addOrException(45, "5RE");
		man.getKopplungen().addOrException(47, "6För");
		man.getKopplungen().addOrException(44, "6RE");
		man.getKopplungen().addOrException(43, "7FL");
		man.getKopplungen().addOrException(48, "7För");
		man.getKopplungen().addOrException(42, "7RE");
		man.getKopplungen().addOrException(41, "8FL");
		man.getKopplungen().addOrException(49, "8För");
		man.getKopplungen().addOrException(40, "8RE");
		man.getKopplungen().addOrException(39, "9FL");
		man.getKopplungen().addOrException(50, "9För");
		man.getKopplungen().addOrException(38, "9RE");
		man.getKopplungen().addOrException(37, "9WPL");
		man.getKopplungen().addOrException(24, "EFB01");
		man.getKopplungen().addOrException(25, "EFB02");
		man.getKopplungen().addOrException(26, "EFB03");
		man.getKopplungen().addOrException(27, "EFB04");
		man.getKopplungen().addOrException(28, "EFB05");
		man.getKopplungen().addOrException(29, "EFB06");
		man.getKopplungen().addOrException(30, "EFB07");
		man.getKopplungen().addOrException(31, "EFB08");
		man.getKopplungen().addOrException(32, "EFB09");
		man.getKopplungen().addOrException(33, "EFB10");
		man.getKopplungen().addOrException(34, "EFB11");
		man.getKopplungen().addOrException(35, "EFB12");
		man.getKopplungen().addOrException(36, "EFB13");
		man.getKopplungen().addOrException(15, "Q1B01");
		man.getKopplungen().addOrException(16, "Q1B02");
		man.getKopplungen().addOrException(17, "Q1B03");
		man.getKopplungen().addOrException(18, "Q1B04");
		man.getKopplungen().addOrException(19, "Q1B05");
		man.getKopplungen().addOrException(20, "Q1B06");
		man.getKopplungen().addOrException(21, "Q1B07");
		man.getKopplungen().addOrException(22, "Q1B08");
		man.getKopplungen().addOrException(23, "Q1B09");
		man.getKopplungen().addOrException(13, "Q1LK01");
		man.getKopplungen().addOrException(14, "Q1LK02");
		man.getKopplungen().addOrException(3, "Q2B01");
		man.getKopplungen().addOrException(4, "Q2B02");
		man.getKopplungen().addOrException(5, "Q2B03");
		man.getKopplungen().addOrException(6, "Q2B04");
		man.getKopplungen().addOrException(7, "Q2B05");
		man.getKopplungen().addOrException(8, "Q2B06");
		man.getKopplungen().addOrException(9, "Q2B07");
		man.getKopplungen().addOrException(10, "Q2B08");
		man.getKopplungen().addOrException(11, "Q2B09");
		man.getKopplungen().addOrException(12, "Q2B10");
		man.getKopplungen().addOrException(1, "Q2LK01");
		man.getKopplungen().addOrException(2, "Q2LK02");
	}

}
