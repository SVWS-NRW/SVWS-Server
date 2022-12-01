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
import de.nrw.schule.svws.core.data.stundenplanblockung.StundenplanblockungRaum;
import de.nrw.schule.svws.core.utils.stundenplanblockung.StundenplanblockungManager;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse testet den {@link StundenplanblockungManager}.
 */
@DisplayName("Diese Klasse testet den {@link StundenplanblockungManager}.")
@TestMethodOrder(MethodOrderer.MethodName.class)
public class StundenplanblockungTests {

	private static final long _SEED = 1L;
	private static final int _ANZAHL_MANAGER_TESTS = 10;
	private static final int _ANZAHL_MANAGER_SUB_TESTS = 100000;
	private static final int _MAX_LEHRKRAEFTE = 50;
	private static final int _MAX_KLASSEN = 50;
	private static final int _MAX_FAECHER = 20;
	private static final int _MAX_RAEUME = 90;
	private static final int _MAX_KOPPLUNGEN = 40;

	/** 
	 * Diese Klasse testet den {@link StundenplanblockungManager} mit randomisierten Daten.
	 */
	@DisplayName("Diese Klasse testet den {@link StundenplanblockungManager} mit randomisierten Daten.")
	@Test
	public void testManager() {
		Random lRandom = new Random(_SEED);
		for (int i = 0; i < _ANZAHL_MANAGER_TESTS; i++)
			testManagerZufaellig(lRandom);
	}

	private static void testManagerZufaellig(Random pRandom) {
		StundenplanblockungManager manager = new StundenplanblockungManager();
		
		for (int i = 0; i < _ANZAHL_MANAGER_SUB_TESTS; i++)
			testManagerSubZufaellig(manager, pRandom);
		
		if (manager.miscCheckConsistency() == false)
			fail("Die Konsistenz der Daten wurde verletzt!");
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
					pMan.lehrkraftSetDarfVertreten(lehrkraft.id, !lehrkraft.darfVertreten);
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

		}

	}

	private static @NotNull StundenplanblockungLehrkraft lehrkraftCreate(long pLehrkraftID) {
		StundenplanblockungLehrkraft lehrkraft = new StundenplanblockungLehrkraft();
		lehrkraft.id = pLehrkraftID;
		lehrkraft.darfVertreten = false;
		lehrkraft.kuerzel = "Lehrkraft" + pLehrkraftID;
		return lehrkraft;
	}

	private static @NotNull StundenplanblockungKlasse klasseCreate(long pKlasseID) {
		StundenplanblockungKlasse klasse = new StundenplanblockungKlasse();
		klasse.id = pKlasseID;
		klasse.kuerzel = "Klasse" + pKlasseID;
		return klasse;
	}

	private static @NotNull StundenplanblockungFach fachCreate(long pFachID, Random pRandom) {
		StundenplanblockungFach fach = new StundenplanblockungFach();
		fach.id = pFachID;
		fach.kuerzel = "Fach" + pFachID;
		fach.sortierung = pRandom.nextInt(32000);
		return fach;
	}
	
	private static @NotNull StundenplanblockungRaum raumCreate(long pRaumID) {
		StundenplanblockungRaum raum = new StundenplanblockungRaum();
		raum.id = pRaumID;
		raum.kuerzel = "Raum" + pRaumID;
		return raum;
	}
	
	private static @NotNull StundenplanblockungKopplung kopplungCreate(long pKopplungID) {
		StundenplanblockungKopplung kopplung = new StundenplanblockungKopplung();
		kopplung.id = pKopplungID;
		kopplung.kuerzel = "Kopplung" + pKopplungID;
		return kopplung;
	}
}