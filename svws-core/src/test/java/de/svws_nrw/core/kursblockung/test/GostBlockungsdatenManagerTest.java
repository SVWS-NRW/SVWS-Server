package de.svws_nrw.core.kursblockung.test;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungKursLehrer;
import de.svws_nrw.core.data.gost.GostBlockungSchiene;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import jakarta.validation.constraints.NotNull;

/**
 * Testet den {@link GostBlockungsdatenManager}.
 */
@DisplayName("Testet den {@link GostBlockungsdatenManager}.")
@TestMethodOrder(MethodOrderer.MethodName.class)
class GostBlockungsdatenManagerTest {

	/**
	 * Testet 001.
	 */
	@SuppressWarnings("unused")
	@Test
	@DisplayName("Test 001.")
	void test001() {
		// Pseudofach erzeugen
		final @NotNull GostFach fa1 = new GostFach();
		fa1.id = 1;

		// Pseudoschiene erzeugen
		final @NotNull GostBlockungSchiene sch1 = new GostBlockungSchiene();
		sch1.id = 1;

		// Pseudokurs erzeugen
		final @NotNull GostBlockungKurs ku1 = new GostBlockungKurs();
		ku1.id = 1;
		ku1.nummer = 1;
		ku1.fach_id = 1;
		ku1.kursart = 1;

		// Pseudolehrkraft erzeugen
		final @NotNull GostBlockungKursLehrer le1 = new GostBlockungKursLehrer();
		le1.id = 1;

		final GostBlockungsdatenManager man = new GostBlockungsdatenManager();
		man.faecherManager().add(fa1);
		man.schieneAdd(sch1);
		man.kursAdd(ku1);

		man.kursAddLehrkraft(1, le1);
		try {
			man.kursAddLehrkraft(1, le1);
			fail("Ein doppeltes Hinzufügen hätte eine Exception werfen müssen!");
		} catch (final DeveloperNotificationException ex) {
			// richtig
		}

		man.kursRemoveLehrkraft(1, 1);
		try {
			man.kursRemoveLehrkraft(1, 1);
			fail("Ein doppeltes Löschen hätte eine Exception werfen müssen!");
		} catch (final DeveloperNotificationException ex) {
			// richtig
		}
	}

}
