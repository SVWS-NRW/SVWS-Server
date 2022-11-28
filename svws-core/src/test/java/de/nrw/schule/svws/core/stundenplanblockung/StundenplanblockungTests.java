package de.nrw.schule.svws.core.stundenplanblockung;

import java.util.Random;
import java.util.Vector;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.nrw.schule.svws.core.data.stundenplanblockung.StundenplanblockungLehrkraft;
import de.nrw.schule.svws.core.utils.stundenplanblockung.StundenplanblockungManager;

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

	/** 
	 * Diese Klasse testet den {@link StundenplanblockungManager} mit randomisierten Daten.
	 */
	@Test
	@DisplayName("Diese Klasse testet den {@link StundenplanblockungManager} mit randomisierten Daten.")
	void testManager() {
		Random lRandom = new Random(_SEED);
		for (int i = 0; i < _ANZAHL_MANAGER_TESTS; i++)
			testManagerZufaellig(lRandom);
	}

	private static void testManagerZufaellig(Random pRandom) {
		StundenplanblockungManager manager = new StundenplanblockungManager();
		for (int i = 0; i < _ANZAHL_MANAGER_SUB_TESTS; i++) 
			testManagerSubZufaellig(manager, pRandom);
	}

	private static void testManagerSubZufaellig(StundenplanblockungManager pManager, Random pRandom) {
		
		if (pRandom.nextInt(100) == 0) {
			long lehrkraftID = pRandom.nextLong(_MAX_LEHRKRAEFTE);
			if (pManager.lehrkraftExists(lehrkraftID))
				pManager.lehrkraftRemove(lehrkraftID);
			else
				pManager.lehrkraftCreate(lehrkraftID, "" + lehrkraftID);
		}

		if (pRandom.nextInt(2) == 0) {
			Vector<StundenplanblockungLehrkraft> leMenge = pManager.lehrkraftGetMengeSortiertNachKuerzel();
			if (leMenge.isEmpty() == false) {
				StundenplanblockungLehrkraft le = leMenge.get( pRandom.nextInt(leMenge.size()) ); 
				pManager.lehrkraftSetKuerzel(le.id, "Le" + pRandom.nextInt(_MAX_LEHRKRAEFTE));
				pManager.lehrkraftSetDarfVertreten(le.id, !le.darfVertreten);
			}
		}
		
	}

}