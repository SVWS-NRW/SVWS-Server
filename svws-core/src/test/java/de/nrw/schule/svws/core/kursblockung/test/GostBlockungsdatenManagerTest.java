package de.nrw.schule.svws.core.kursblockung.test;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.nrw.schule.svws.core.data.gost.GostBlockungKurs;
import de.nrw.schule.svws.core.data.gost.GostBlockungKursLehrer;
import de.nrw.schule.svws.core.data.gost.GostBlockungSchiene;
import de.nrw.schule.svws.core.data.gost.GostFach;
import de.nrw.schule.svws.core.exceptions.DeveloperNotificationException;
import de.nrw.schule.svws.core.utils.gost.GostBlockungsdatenManager;
import jakarta.validation.constraints.NotNull;

/** 
 * Testet den {@link GostBlockungsdatenManager}. 
 */
@DisplayName("Testet den {@link GostBlockungsdatenManager}.")
@TestMethodOrder(MethodOrderer.MethodName.class)
public class GostBlockungsdatenManagerTest {

	/** 
	 * Testet 001. 
	 */
	@SuppressWarnings("unused")
	@Test
	@DisplayName("Test 001.")
	void test001() {
		// Pseudofach erzeugen
		@NotNull GostFach fa1 = new GostFach();
		fa1.id = 1;
		
		// Pseudoschiene erzeugen
		@NotNull GostBlockungSchiene sch1 = new GostBlockungSchiene();
		sch1.id = 1;
				
		// Pseudokurs erzeugen
		@NotNull GostBlockungKurs ku1 = new GostBlockungKurs();
		ku1.id = 1;
		ku1.nummer = 1;
		ku1.fach_id = 1;
		ku1.kursart = 1;
		
		// Pseudolehrkraft erzeugen
		@NotNull GostBlockungKursLehrer le1 = new GostBlockungKursLehrer();
		le1.id = 1;
		
		GostBlockungsdatenManager man = new GostBlockungsdatenManager();
		man.faecherManager().add(fa1);
		man.addSchiene(sch1);
		man.addKurs(ku1);
		
		man.patchOfKursAddLehrkraft(1, le1);
		try {
			man.patchOfKursAddLehrkraft(1, le1);
			fail("Ein doppeltes Hinzufügen hätte eine Exception werfen müssen!");
		} catch (DeveloperNotificationException ex) {
			// richtig
		}
		
		man.patchOfKursRemoveLehrkraft(1, 1);
		try {
			man.patchOfKursRemoveLehrkraft(1, 1);
			fail("Ein doppeltes Löschen hätte eine Exception werfen müssen!");
		} catch (DeveloperNotificationException ex) {
			// richtig
		}
	}
	
	

}
