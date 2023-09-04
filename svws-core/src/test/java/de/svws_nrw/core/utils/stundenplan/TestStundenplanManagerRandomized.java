package de.svws_nrw.core.utils.stundenplan;

import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.svws_nrw.core.data.stundenplan.StundenplanKomplett;
import de.svws_nrw.core.data.stundenplan.StundenplanLehrer;

/**
 * Diese Klasse testet den {@link StundenplanManager}.
 */
@DisplayName("Diese Klasse testet den {@link StundenplanManager}.")
@TestMethodOrder(MethodOrderer.MethodName.class)
class TestStundenplanManagerRandomized {

	private static final long _SEED = 1L;
	private static final int _ANZAHL_TESTS = 1000;

	private final StundenplanLehrer[] aLehrer = new StundenplanLehrer[50];

	/**
	 * Diese Klasse testet den {@link StundenplanManager} mit randomisierten Daten.
	 */
	@DisplayName("testStundenplanManager")
	@Test
	void testManagerRandom() {
		final Random random = new Random(_SEED);
		final StundenplanKomplett komplett = new StundenplanKomplett();

		komplett.daten.gueltigAb = "2022-03-15";
		komplett.daten.gueltigBis = "2022-09-25";

		final StundenplanManager manager = new StundenplanManager(komplett);
		for (int i = 0; i < _ANZAHL_TESTS; i++)
			subTestManagerZufaellig(random, manager);
	}

	private void subTestManagerZufaellig(final Random random, final StundenplanManager manager) {
		lehrerAdd(random, manager);
	}

	private void lehrerAdd(final Random random, final StundenplanManager manager) {
		final int rnd = random.nextInt(aLehrer.length);

		if (aLehrer[rnd] == null) {
			aLehrer[rnd] = new StundenplanLehrer();
			aLehrer[rnd].id = rnd;
			aLehrer[rnd].kuerzel = "LEHR_KÜRZ" + rnd;
			aLehrer[rnd].nachname = "LEHR_NACH" + rnd;
			aLehrer[rnd].vorname = "LEHR_VORN" + rnd;
			manager.lehrerAdd(aLehrer[rnd]);
			assertSame(manager.lehrerGetByIdOrException(rnd), aLehrer[rnd]); // expected, actual
		} else {
			// TODO Auto-generated method stub
		}
	}


}
