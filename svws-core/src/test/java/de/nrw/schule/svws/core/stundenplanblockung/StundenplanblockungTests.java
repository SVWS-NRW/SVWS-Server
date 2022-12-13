package de.nrw.schule.svws.core.stundenplanblockung;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Random;
import java.util.Vector;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.nrw.schule.svws.core.data.stundenplanblockung.StundenplanblockungFach;
import de.nrw.schule.svws.core.data.stundenplanblockung.StundenplanblockungKlasse;
import de.nrw.schule.svws.core.data.stundenplanblockung.StundenplanblockungKopplung;
import de.nrw.schule.svws.core.data.stundenplanblockung.StundenplanblockungLehrkraft;
import de.nrw.schule.svws.core.data.stundenplanblockung.StundenplanblockungLerngruppe;
import de.nrw.schule.svws.core.data.stundenplanblockung.StundenplanblockungRaum;
import de.nrw.schule.svws.core.data.stundenplanblockung.StundenplanblockungStundenelement;
import de.nrw.schule.svws.core.utils.stundenplanblockung.StundenplanblockungManager;

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
			testManagerZufaellig(lRandom);
	}

	private static void testManagerZufaellig(Random pRandom) {
		StundenplanblockungManager manager = new StundenplanblockungManager();
		
		for (int i = 0; i < _ANZAHL_MANAGER_SUB_TESTS; i++)
			testManagerSubZufaellig(manager, pRandom);
		
		manager.miscCheckConsistencyOrException();
	}

	private static void testManagerSubZufaellig(StundenplanblockungManager pMan, Random pRandom) {

		switch (pRandom.nextInt(100)) {
			
			// Lehrkräfte testen
			case 0 -> {
				long lehrkraftID = pRandom.nextLong(_MAX_LEHRKRAEFTE);
				if (pMan.lehrkraftExists(lehrkraftID)) {
					pMan.lehrkraftRemove(lehrkraftID);
					if (pMan.lehrkraftExists(lehrkraftID))
						fail("Lehrkraft sollte nicht mehr existieren!");
				} else {
					pMan.lehrkraftAdd(lehrkraftCreate(lehrkraftID));
					if (!pMan.lehrkraftExists(lehrkraftID))
						fail("Lehrkraft sollte nun existieren!");
				}
			}
			case 1 -> {
				Vector<StundenplanblockungLehrkraft> lehrkraefte = pMan.lehrkraftGetMengeSortiertNachKuerzel();
				if (lehrkraefte.isEmpty() == false) {
					StundenplanblockungLehrkraft lehrkraft = lehrkraefte.get(pRandom.nextInt(lehrkraefte.size()));
					pMan.lehrkraftSetKuerzel(lehrkraft.id, "Lehrkraft" + pRandom.nextInt(_MAX_LEHRKRAEFTE));
				}
			}
			
			// Klassen testen
			case 10 -> {
				long klasseID = pRandom.nextLong(_MAX_KLASSEN);
				if (pMan.klasseExists(klasseID)) {
					pMan.klasseRemove(klasseID);
					if (pMan.klasseExists(klasseID))
						fail("Klasse sollte nicht mehr existieren!");
				} else {
					pMan.klasseAdd(klasseCreate(klasseID));
					if (!pMan.klasseExists(klasseID))
						fail("Klasse sollte nun existieren!");
				}
			}
			case 11 -> {
				Vector<StundenplanblockungKlasse> klassen = pMan.klasseGetMengeSortiertNachKuerzel();
				if (klassen.isEmpty() == false) {
					StundenplanblockungKlasse klasse = klassen.get(pRandom.nextInt(klassen.size()));
					pMan.klasseSetKuerzel(klasse.id, "Klasse " + pRandom.nextInt(_MAX_KLASSEN));
				}
			}

			
			// Fächer testen
			case 20 -> {
				long fachID = pRandom.nextLong(_MAX_FAECHER);
				if (pMan.fachExists(fachID)) {
					pMan.fachRemove(fachID);
					if (pMan.fachExists(fachID))
						fail("Fach sollte nicht mehr existieren!");
				} else {
					pMan.fachAdd(fachCreate(fachID, pRandom));
					if (!pMan.fachExists(fachID))
						fail("Fach sollte nun existieren!");
				}
			}
			case 21 -> {
				Vector<StundenplanblockungFach> faecher = pMan.fachGetMengeSortiertNachSortiernummer();
				if (faecher.isEmpty() == false) {
					StundenplanblockungFach fach = faecher.get(pRandom.nextInt(faecher.size()));
					pMan.fachSetKuerzel(fach.id, "Fach" + pRandom.nextInt(_MAX_FAECHER));
					pMan.fachSetSortiernummer(fach.id, pRandom.nextInt(32000));
				}
			}

			
			// Räume testen
			case 30 -> {
				long raumID = pRandom.nextLong(_MAX_RAEUME);
				if (pMan.raumExists(raumID)) {
					pMan.raumRemove(raumID);
					if (pMan.raumExists(raumID))
						fail("Raum sollte nicht mehr existieren!");
				} else {
					pMan.raumAdd(raumCreate(raumID));
					if (!pMan.raumExists(raumID))
						fail("Raum sollte nun existieren!");
				}
			}
			case 31 -> {
				Vector<StundenplanblockungRaum> raeume = pMan.raumGetMengeSortiertNachKuerzel();
				if (raeume.isEmpty() == false) {
					StundenplanblockungRaum raum = raeume.get(pRandom.nextInt(raeume.size()));
					pMan.raumSetKuerzel(raum.id, "Raum" + pRandom.nextInt(_MAX_RAEUME));
				}
			}

			
			// Kopplungen testen
			case 40 -> {
				long kopplungID = pRandom.nextLong(_MAX_KOPPLUNGEN);
				if (pMan.kopplungExists(kopplungID)) {
					pMan.kopplungRemove(kopplungID);
					if (pMan.kopplungExists(kopplungID))
						fail("Kopplung sollte nicht mehr existieren!");
				} else {
					pMan.kopplungAdd(kopplungCreate(kopplungID));
					if (!pMan.kopplungExists(kopplungID))
						fail("Kopplung sollte nun existieren!");
				}
			}
			case 41 -> {
				Vector<StundenplanblockungKopplung> kopplungen = pMan.kopplungGetMengeSortiertNachKuerzel();
				if (kopplungen.isEmpty() == false) {
					StundenplanblockungKopplung kopplung = kopplungen.get(pRandom.nextInt(kopplungen.size()));
					pMan.kopplungSetKuerzel(kopplung.id, "Kopplung" + pRandom.nextInt(_MAX_RAEUME));
				}
			}

			
			// Lerngruppen testen
			case 50 -> {
				long lerngruppeID = pRandom.nextLong(_MAX_LERNGRUPPEN);
				if (pMan.lerngruppeExists(lerngruppeID)) {
					pMan.lerngruppeRemove(lerngruppeID);
					if (pMan.lerngruppeExists(lerngruppeID))
						fail("Kopplung sollte nicht mehr existieren!");
				} else {
					pMan.lerngruppeAdd(lerngruppeCreate(lerngruppeID, pMan, pRandom));
					if (!pMan.lerngruppeExists(lerngruppeID))
						fail("Kopplung sollte nun existieren!");
				}
			}
			case 51 -> {
				Vector<StundenplanblockungLerngruppe> lerngruppen = pMan.lerngruppeGetMengeSortiertNachID();
				if (lerngruppen.isEmpty() == false) {
					// StundenplanblockungLerngruppe lerngruppe = lerngruppen.get(pRandom.nextInt(lerngruppen.size()));
					// pMan.lerngruppeSetKuerzel(lerngruppe.id, "Lerngruppe" + pRandom.nextInt(_MAX_LERNGRUPPEN));
					// TODO BAR Lerngruppen randomisiert testen
				}
			}

		}

	}

	private static StundenplanblockungLehrkraft lehrkraftCreate(long pLehrkraftID) {
		StundenplanblockungLehrkraft lehrkraft = new StundenplanblockungLehrkraft();
		lehrkraft.id = pLehrkraftID;
		//lehrkraft.darfVertreten = false;
		lehrkraft.kuerzel = "Lehrkraft" + pLehrkraftID;
		return lehrkraft;
	}

	private static StundenplanblockungKlasse klasseCreate(long pKlasseID) {
		StundenplanblockungKlasse klasse = new StundenplanblockungKlasse();
		klasse.id = pKlasseID;
		klasse.kuerzel = "Klasse" + pKlasseID;
		return klasse;
	}

	private static StundenplanblockungKlasse klasseCreateMitKuerzel(long pKlasseID, String pKuerzel) {
		StundenplanblockungKlasse klasse = new StundenplanblockungKlasse();
		klasse.id = pKlasseID;
		klasse.kuerzel = pKuerzel;
		return klasse;
	}
	
	private static StundenplanblockungFach fachCreate(long pFachID, Random pRandom) {
		StundenplanblockungFach fach = new StundenplanblockungFach();
		fach.id = pFachID;
		fach.kuerzel = "Fach" + pFachID;
		fach.sortierung = pRandom.nextInt(32000);
		return fach;
	}
	
	private static StundenplanblockungFach fachCreateMitKuerzel(long pFachID, String pKuerzel) {
		StundenplanblockungFach fach = new StundenplanblockungFach();
		fach.id = pFachID;
		fach.kuerzel = pKuerzel;
		fach.sortierung = 1;
		return fach;
	}
	
	private static StundenplanblockungRaum raumCreate(long pRaumID) {
		StundenplanblockungRaum raum = new StundenplanblockungRaum();
		raum.id = pRaumID;
		raum.kuerzel = "Raum" + pRaumID;
		return raum;
	}
	
	private static StundenplanblockungRaum raumCreateMitKuerzel(long pRaumID, String pKuerzel) {
		StundenplanblockungRaum raum = new StundenplanblockungRaum();
		raum.id = pRaumID;
		raum.kuerzel = pKuerzel;
		return raum;
	}
	
	private static StundenplanblockungKopplung kopplungCreate(long pKopplungID) {
		StundenplanblockungKopplung kopplung = new StundenplanblockungKopplung();
		kopplung.id = pKopplungID;
		kopplung.kuerzel = "Kopplung" + pKopplungID;
		return kopplung;
	}
	

	private static StundenplanblockungLerngruppe lerngruppeCreate(long lerngruppeID, StundenplanblockungManager pMan, Random pRandom) {
		StundenplanblockungLerngruppe lerngruppe = new StundenplanblockungLerngruppe();
		lerngruppe.id = lerngruppeID;
		
		Vector<StundenplanblockungLehrkraft> lehrkraefte = pMan.lehrkraftGetMengeSortiertNachKuerzel();
		if (lehrkraefte.isEmpty() == false) {
			StundenplanblockungLehrkraft lehrkraft1 = lehrkraefte.get(pRandom.nextInt(lehrkraefte.size()));
			lerngruppe.lehrkraefte1.add(lehrkraft1);
			
			StundenplanblockungLehrkraft lehrkraft2 = lehrkraefte.get(pRandom.nextInt(lehrkraefte.size()));
			lerngruppe.lehrkraefte1.add(lehrkraft2);
		}
		
		return lerngruppe;
	}
	
	private static StundenplanblockungLerngruppe lerngruppeCreateMitID(long lerngruppeID) {
		StundenplanblockungLerngruppe lerngruppe = new StundenplanblockungLerngruppe();
		lerngruppe.id = lerngruppeID;
		return lerngruppe;
	}
	
	private static StundenplanblockungStundenelement stundenelementCreateDoppelstunde(long pStundenelementID) {
		StundenplanblockungStundenelement stundenelement = new StundenplanblockungStundenelement();
		stundenelement.id = pStundenelementID;
		stundenelement.stunden = 2;
		stundenelement.typ = 1;
		return stundenelement;
	}
	
	private static StundenplanblockungStundenelement stundenelementCreateEinzelstunde(long pStundenelementID) {
		StundenplanblockungStundenelement stundenelement = new StundenplanblockungStundenelement();
		stundenelement.id = pStundenelementID;
		stundenelement.stunden = 1;
		stundenelement.typ = 1;
		return stundenelement;
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
			man.lehrkraftAdd(lehrkraftCreate(leID));
		
		// Klassen erstellen.
		StundenplanblockungKlasse kl05a = klasseCreateMitKuerzel( 1, "05a"); 
		man.klasseAdd(kl05a);
		man.klasseAdd(klasseCreateMitKuerzel( 2, "05b"));
		man.klasseAdd(klasseCreateMitKuerzel( 3, "05c"));
		man.klasseAdd(klasseCreateMitKuerzel( 4, "05d"));
		man.klasseAdd(klasseCreateMitKuerzel( 5, "06a"));
		man.klasseAdd(klasseCreateMitKuerzel( 6, "06b"));
		man.klasseAdd(klasseCreateMitKuerzel( 7, "06c"));
		man.klasseAdd(klasseCreateMitKuerzel( 8, "07a"));
		man.klasseAdd(klasseCreateMitKuerzel( 9, "07b"));
		man.klasseAdd(klasseCreateMitKuerzel(10, "07c"));
		man.klasseAdd(klasseCreateMitKuerzel(11, "07d"));
		man.klasseAdd(klasseCreateMitKuerzel(12, "08a"));
		man.klasseAdd(klasseCreateMitKuerzel(13, "08b"));
		man.klasseAdd(klasseCreateMitKuerzel(14, "08c"));
		man.klasseAdd(klasseCreateMitKuerzel(15, "09a"));
		man.klasseAdd(klasseCreateMitKuerzel(16, "09b"));
		man.klasseAdd(klasseCreateMitKuerzel(17, "09c"));
		man.klasseAdd(klasseCreateMitKuerzel(18, "09d"));
		man.klasseAdd(klasseCreateMitKuerzel(19, "EF"));
		man.klasseAdd(klasseCreateMitKuerzel(20, "Q1"));
		man.klasseAdd(klasseCreateMitKuerzel(21, "Q2"));
		
		// Fächer erstellen.
		StundenplanblockungFach faBI = fachCreateMitKuerzel( 1, "BI"); man.fachAdd(faBI);
		StundenplanblockungFach faM  = fachCreateMitKuerzel( 2,  "M"); man.fachAdd(faM );
		StundenplanblockungFach faMU = fachCreateMitKuerzel( 3, "MU"); man.fachAdd(faMU);
		StundenplanblockungFach faE  = fachCreateMitKuerzel( 4,  "E"); man.fachAdd(faE );
		
		// Klassen erstellen.
		man.raumAdd(raumCreateMitKuerzel( 1, "AM1"));
		man.raumAdd(raumCreateMitKuerzel( 2, "AM2"));
		man.raumAdd(raumCreateMitKuerzel( 3, "AM3"));
		man.raumAdd(raumCreateMitKuerzel( 4, "AO1"));
		man.raumAdd(raumCreateMitKuerzel( 5, "AO2"));
		man.raumAdd(raumCreateMitKuerzel( 6, "AO3"));
		man.raumAdd(raumCreateMitKuerzel( 7, "AU1"));
		man.raumAdd(raumCreateMitKuerzel( 8, "AU3"));
		
		man.raumAdd(raumCreateMitKuerzel( 9, "BK4"));
		man.raumAdd(raumCreateMitKuerzel(10, "BK5"));
		
		StundenplanblockungRaum raBM1 = raumCreateMitKuerzel(11, "BM1"); 
		man.raumAdd(raBM1);
		man.raumAdd(raumCreateMitKuerzel(12, "BM2"));
		man.raumAdd(raumCreateMitKuerzel(13, "BM3"));
		man.raumAdd(raumCreateMitKuerzel(14, "BO1"));
		man.raumAdd(raumCreateMitKuerzel(15, "BO2"));
		man.raumAdd(raumCreateMitKuerzel(16, "BO3"));
		man.raumAdd(raumCreateMitKuerzel(17, "BU1"));
		man.raumAdd(raumCreateMitKuerzel(18, "BU3"));

		man.raumAdd(raumCreateMitKuerzel(19, "CM1"));
		man.raumAdd(raumCreateMitKuerzel(20, "CM2"));
		man.raumAdd(raumCreateMitKuerzel(21, "CM3"));
		man.raumAdd(raumCreateMitKuerzel(22, "CM4"));
		man.raumAdd(raumCreateMitKuerzel(23, "CO1"));
		man.raumAdd(raumCreateMitKuerzel(24, "CO2"));
		man.raumAdd(raumCreateMitKuerzel(25, "CO3"));
		man.raumAdd(raumCreateMitKuerzel(26, "CO4/5"));
		man.raumAdd(raumCreateMitKuerzel(27, "CO6"));
		man.raumAdd(raumCreateMitKuerzel(28, "CO7"));
		man.raumAdd(raumCreateMitKuerzel(29, "CU2"));
		man.raumAdd(raumCreateMitKuerzel(30, "CU3"));
		man.raumAdd(raumCreateMitKuerzel(31, "CU5"));
		StundenplanblockungRaum raMU = raumCreateMitKuerzel(32, "MU-R"); man.raumAdd(raMU);
		man.raumAdd(raumCreateMitKuerzel(33, "Aula"));
		StundenplanblockungRaum raBiHS = raumCreateMitKuerzel(34, "Bi-HS"); man.raumAdd(raBiHS);
		StundenplanblockungRaum raBiU = raumCreateMitKuerzel(35, "Bi-Ü"); man.raumAdd(raBiU);
		man.raumAdd(raumCreateMitKuerzel(36, "CH-Alt"));
		man.raumAdd(raumCreateMitKuerzel(37, "CH-Neu"));
		man.raumAdd(raumCreateMitKuerzel(38, "Ph-A"));
		man.raumAdd(raumCreateMitKuerzel(39, "Ph-B"));
		
		// Lerngruppen erstellen
		long sElementID = 1;
		
		StundenplanblockungLerngruppe lg001 =  lerngruppeCreateMitID(1);
		lg001.klassen.add(kl05a);
		lg001.lehrkraefte1.add(man.lehrkraftGet(55));
		lg001.faecher.add(faBI);
		lg001.raeume1.add(raBiHS);
		lg001.raeume1.add(raBiU);
		lg001.stundenelemente.add(stundenelementCreateDoppelstunde(sElementID++));
		man.lerngruppeAdd(lg001);
		
		StundenplanblockungLerngruppe lg002 =  lerngruppeCreateMitID(2);
		lg002.klassen.add(kl05a);
		lg002.lehrkraefte1.add(man.lehrkraftGet(27));
		lg002.faecher.add(faM);
		lg002.raeume1.add(raBM1);
		lg002.stundenelemente.add(stundenelementCreateDoppelstunde(sElementID++));
		lg002.stundenelemente.add(stundenelementCreateDoppelstunde(sElementID++));
		lg002.stundenelemente.add(stundenelementCreateEinzelstunde(sElementID++));
		man.lerngruppeAdd(lg002);
		
		StundenplanblockungLerngruppe lg003 =  lerngruppeCreateMitID(3);
		lg003.klassen.add(kl05a);
		lg003.lehrkraefte1.add(man.lehrkraftGet(22));
		lg003.faecher.add(faMU);
		lg003.raeume1.add(raMU);
		lg003.stundenelemente.add(stundenelementCreateEinzelstunde(sElementID++));
		man.lerngruppeAdd(lg003);
		
		StundenplanblockungLerngruppe lg004 =  lerngruppeCreateMitID(4);
		lg004.klassen.add(kl05a);
		lg004.lehrkraefte1.add(man.lehrkraftGet(57));
		lg004.faecher.add(faE);
		lg004.raeume1.add(raBM1);
		lg004.stundenelemente.add(stundenelementCreateEinzelstunde(sElementID++));
		lg004.stundenelemente.add(stundenelementCreateDoppelstunde(sElementID++));
		lg004.stundenelemente.add(stundenelementCreateDoppelstunde(sElementID++));
		man.lerngruppeAdd(lg004);
		
	}
	
}