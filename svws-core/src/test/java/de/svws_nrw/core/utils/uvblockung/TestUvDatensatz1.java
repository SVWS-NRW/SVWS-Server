package de.svws_nrw.core.utils.uvblockung;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.uv.UvFach;
import de.svws_nrw.core.data.uv.UvLehrer;
import de.svws_nrw.core.data.uv.UvLerngruppe;
import de.svws_nrw.core.data.uv.UvPlanungsabschnitt;
import de.svws_nrw.core.utils.uv.UvManager;

/**
 * Testet die Klasse {@link UvManager} mit dem Datensatz {@link UvDatensatz1}.
 *
 * @author Benjamin A. Bartsch
 */
@DisplayName("Testet den UvAlgorithmus.")
@TestMethodOrder(MethodOrderer.MethodName.class)
class TestUvDatensatz1 {


	/**
	 * Initialisierung der ASD-Core-Types vor allen Tests.
	 * Ohne diese Initialisierung können Enums und Core-Types nicht korrekt aufgelöst werden.
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}


	@Test
	@DisplayName("testUvDatensatz1")
	void testUvDatensatz1() {
		// UvManager aus dem einfachen Datensatz erzeugen
		final UvManager man = UvDatensatz1.erzeugeUvManager();

		// Prüfe Planungsabschnitt.
		assertDoesNotThrow(() -> man.planungsabschnittGetByIdOrException(UvDatensatz1.ID_PLANUNGSABSCHNITT));

		// Prüfe Fachdaten.
		assertDoesNotThrow(() -> man.fachdatenGetByFach(man.fachGetByIdOrException(UvDatensatz1.ID_FACH)));
		assertEquals(1, man.fachdatenGetMenge().size(), "Es darf genau 1 FachDaten geben.");

		// Prüfe Fach.
		assertDoesNotThrow(() -> man.fachGetByIdOrException(UvDatensatz1.ID_FACH));
		assertEquals(1, man.fachGetMengeAsList().size(), "Es darf genau 1 UvFach geben.");

		// Prüfe Klasse.
		assertDoesNotThrow(() -> man.klasseGetByIdOrException(UvDatensatz1.ID_KLASSE));
		assertEquals(1, man.klasseGetMengeAsList().size(), "Es darf genau 1 UvKlasse geben.");

		// Prüfe Lehrer.
		assertDoesNotThrow(() -> man.lehrerGetByIdOrException(UvDatensatz1.ID_LEHRER));
		assertEquals(1, man.lehrerGetMengeAsList().size(), "Es darf genau 1 UvLehrer geben.");

		// Prüfe Pflichtstunden.
		assertDoesNotThrow(() -> man.lehrerPflichtstundensollGetByIdOrException(UvDatensatz1.ID_PFLICHTSTUNDENSOLL));
		assertEquals(1, man.lehrerPflichtstundensollGetMengeAsList().size(), "Es darf genau 1 UvLehrerPflichtstundensoll geben.");

		// Prüfe Lehrermenge im Planungsabschnitt.
		final UvPlanungsabschnitt planungsabschnitt = man.planungsabschnittGetByIdOrException(UvDatensatz1.ID_PLANUNGSABSCHNITT);
		assertEquals(1, man.lehrerGetMengeByPlanungsabschnitt(planungsabschnitt).size(), "Es darf genau 1 UvLehrer im Planungsabschnitt geben.");

		// Prüfe Lehrbefähigungen.
		final UvLehrer lehrer = man.lehrerGetByIdOrException(UvDatensatz1.ID_LEHRER);
		final UvFach fach = man.fachGetByIdOrException(UvDatensatz1.ID_FACH);
		assertEquals(true, man.lehrerHatLehrbefaehigungFach(lehrer, fach), "Lehrer sollte Lehrbefähigung für das Fach haben.");

		// Prüfe Lerngruppen.
		assertDoesNotThrow(() -> man.lerngruppeGetByIdOrException(UvDatensatz1.ID_LERNGRUPPE));
		final UvLerngruppe lerngruppe = man.lerngruppeGetByIdOrException(UvDatensatz1.ID_LERNGRUPPE);
		assertEquals(true, man.lehrerHatLehrbefaehigungLerngruppe(lehrer, lerngruppe), "Lehrer sollte Lehrbefähigung für die Lerngruppe haben.");

		// Prüfe alle Jahrgänge.
		assertDoesNotThrow(man::jahrgangsdatenGetMenge);
		assertEquals(1, man.jahrgangsdatenGetMenge().size(), "Es sollte genau 1 Jahrgang geben.");

		// Prüfe Jahrgänge der Lerngruppe.
		assertDoesNotThrow(() -> man.jahrgangsdatenGetMengeByLerngruppe(lerngruppe));
		assertEquals(1, man.jahrgangsdatenGetMengeByLerngruppe(lerngruppe).size(), "Es sollte genau 1 Jahrgang der Lerngruppe geben.");
	}



}
