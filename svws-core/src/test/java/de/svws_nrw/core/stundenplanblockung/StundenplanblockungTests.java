package de.svws_nrw.core.stundenplanblockung;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.svws_nrw.core.data.stundenplan.StundenplanKomplett;
import de.svws_nrw.core.utils.stundenplan.StundenplanManager;
import de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManager;
import de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerKlasse;
import de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerKopplung;
import de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerLehrkraft;
import de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerLerngruppe;
import de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerRaum;

/**
 * Diese Klasse testet den {@link StundenplanManager}.
 */
@DisplayName("Diese Klasse testet den {@link StundenplanManager}.")
@TestMethodOrder(MethodOrderer.MethodName.class)
class StundenplanblockungTests {

	private static final long _SEED = 1L;
	private static final int _ANZAHL_MANAGER_SUB_TESTS = 1000000;
	private static final int _MAX_LEHRKRAEFTE = 50;
	private static final int _MAX_KLASSEN = 50;
	private static final int _MAX_FAECHER = 20;
	private static final int _MAX_RAEUME = 90;
	private static final int _MAX_KOPPLUNGEN = 40;
	private static final int _MAX_LERNGRUPPEN = 800;

	/**
	 * Diese Klasse testet den {@link StundenplanManager} mit randomisierten Daten.
	 */
	@DisplayName("testStundenplanManager")
	@Test
	void testManagerRandom() {
		final Random random = new Random(_SEED);
		final StundenplanManager manager = new StundenplanManager(new StundenplanKomplett());
		for (int i = 0; i < _ANZAHL_MANAGER_SUB_TESTS; i++)
			subTestManagerSubZufaellig(manager, random);
	}

	private static void subTestManagerSubZufaellig(final StundenplanManager m, final Random r) {
		// ---
	}

}
