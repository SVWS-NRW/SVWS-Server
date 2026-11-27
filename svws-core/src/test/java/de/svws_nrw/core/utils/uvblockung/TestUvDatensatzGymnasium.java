package de.svws_nrw.core.utils.uvblockung;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.uv.UvLehrer;
import de.svws_nrw.core.data.uv.UvLerngruppe;
import de.svws_nrw.core.data.uv.UvPlanungsabschnitt;
import de.svws_nrw.core.data.uv.regel.UvBlockungRegelPrioritaet;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.utils.uv.UvManager;
import de.svws_nrw.core.utils.uv.UvRegelManager;

/**
 * Testet den großen Gymnasium-Testdatensatz {@link UvDatensatzGymnasium} auf strukturelle
 * Konsistenz und die Startfähigkeit des Blockungsalgorithmus.
 *
 * @author Benjamin A. Bartsch
 */
@DisplayName("Testet den UvDatensatzGymnasium.")
class TestUvDatensatzGymnasium {

	private static final Logger LOG = new Logger();
	private static final Random RND = new Random(42L);


	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}


	@Test
	@DisplayName("Struktur: Anzahl der Objekte und Verknüpfungen")
	void testStruktur() {
		final UvManager man = UvDatensatzGymnasium.erzeugeUvManager();
		final UvPlanungsabschnitt plan = man.planungsabschnittGetByIdOrException(UvDatensatzGymnasium.PLANUNGSABSCHNITT);

		assertEquals(416, man.lerngruppeGetMengeByPlanungsabschnitt(plan).size(), "Es muss 416 Lerngruppen geben.");
		assertEquals(68, man.lehrerGetMengeAsList().size(), "Es muss 68 Lehrkräfte geben.");
		assertEquals(22, man.klasseGetMengeAsList().size(), "Es muss 22 Klassen geben.");
		assertEquals(48, man.schieneGetMengeAsList().size(), "Es muss 48 Schienen geben.");
		assertEquals(209, man.schuelergruppeGetMengeAsList().size(), "Es muss 209 Schülergruppen geben (22 Klassen + 187 Kurse).");
		assertEquals(186, man.lerngruppenSchieneGetMengeAsList().size(), "Es muss 186 Lerngruppen-Schienen-Zuordnungen geben.");

		// Kopplung: 56FörSp (ID 36) ist EIN Kurs mit Schülern aus den Jahrgängen 05 und 06 (7 Klassen).
		final UvLerngruppe lg36 = man.lerngruppeGetByIdOrException(36L);
		assertEquals(2, man.jahrgangsdatenGetMengeByLerngruppe(lg36).size(), "Die Lerngruppe 36 (56FörSp) muss Schüler aus 2 Jahrgängen haben.");
		assertEquals(7, man.klasseGetMengeByLerngruppe(lg36).size(), "Die Lerngruppe 36 (56FörSp) muss 7 Klassen enthalten.");

		// Oberstufen-LK: Schiene 'Q1LK1' und Jahrgang 'Q1'.
		final UvLerngruppe lg369 = man.lerngruppeGetByIdOrException(369L);
		assertEquals(1, man.lerngruppenSchieneGetMengeByLerngruppe(lg369).size(), "Die Lerngruppe 369 muss genau eine Schiene haben.");
		assertEquals("Q1LK1", man.schieneGetByLerngruppenSchiene(man.lerngruppenSchieneGetMengeByLerngruppe(lg369).get(0)).bezeichnung,
				"Die Schiene der Lerngruppe 369 muss 'Q1LK1' sein.");
		assertEquals(1, man.jahrgangsdatenGetMengeByLerngruppe(lg369).size(), "Die Lerngruppe 369 muss genau einen Jahrgang haben.");
		assertEquals("Q1", man.jahrgangsdatenGetMengeByLerngruppe(lg369).get(0).kuerzel, "Der Jahrgang der Lerngruppe 369 muss 'Q1' sein.");

		// Alle Lehrkräfte haben ein Pflichtstundensoll > 0.
		for (final UvLehrer lehrer : man.lehrerGetMengeAsList()) {
			final Double soll = man.lehrerPflichtstundensollGetDoubleByLehrerAndPlanungsabschnitt(lehrer, plan);
			assertNotNull(soll, "Jede Lehrkraft muss ein Pflichtstundensoll haben.");
			assertTrue(soll > 0.0, "Das Pflichtstundensoll muss größer als 0 sein.");
		}

		// Lehrbefähigungen: L044 (ID 1044) unterrichtet BI in der Lerngruppe 37, L051 (ID 1051) unterrichtet D.
		final UvLerngruppe lg37 = man.lerngruppeGetByIdOrException(37L);
		assertTrue(man.lehrerHatLehrbefaehigungLerngruppe(man.lehrerGetByIdOrException(1044L), lg37),
				"Die Lehrkraft L044 muss für die Lerngruppe 37 (BI) eine Lehrbefähigung haben.");
		assertFalse(man.lehrerHatLehrbefaehigungLerngruppe(man.lehrerGetByIdOrException(1051L), lg37),
				"Die Lehrkraft L051 (D) darf für die Lerngruppe 37 (BI) keine Lehrbefähigung haben.");
	}


	@Test
	@DisplayName("Regeln: ein leerer Regelsatz enthält keine fehlerhaften Regeln")
	void testRegeln() {
		final UvManager man = UvDatensatzGymnasium.erzeugeUvManager();
		final UvRegelManager manRegeln = new UvRegelManager(man, List.of());
		assertTrue(manRegeln.regelnGetMengeFehlerhaftAsList().isEmpty(), "Ein leerer Regelsatz darf keine fehlerhaften Regeln enthalten.");
	}


	@Test
	@DisplayName("Algorithmus: startet ohne Fehler und verbessert die Gesamtbewertung")
	void testAlgorithmus() {
		final UvManager man = UvDatensatzGymnasium.erzeugeUvManager();
		final UvRegelManager manRegeln = new UvRegelManager(man, List.of());
		final UvPlanungsabschnitt plan = man.planungsabschnittGetByIdOrException(UvDatensatzGymnasium.PLANUNGSABSCHNITT);
		final UvAlgorithmusDynDaten dyn = new UvAlgorithmusDynDaten(LOG, RND, man, manRegeln, plan);

		final int[] malusVorher = dyn.gibMalusKopie();
		final long zeitEnde = System.currentTimeMillis() + 1000;
		while (System.currentTimeMillis() < zeitEnde) {
			dyn.strategieLerngruppeLehrkraftHinzufuegen();
			dyn.strategieLerngruppeLehrkraftEntfernen();
			dyn.strategieKlassenleitung1Hinzufuegen();
			dyn.strategieKlassenleitung1Entfernen();
			dyn.strategieKlassenleitung2Hinzufuegen();
			dyn.strategieKlassenleitung2Entfernen();
		}
		final int[] malusNachher = dyn.gibMalusKopie();

		long summeVorher = 0;
		long summeNachher = 0;
		for (int i = 0; i < malusVorher.length; i++) {
			summeVorher += malusVorher[i];
			summeNachher += malusNachher[i];
		}
		assertTrue(summeNachher < summeVorher, "Die Gesamtbewertung muss sich nach einer Sekunde verbessern.");
		assertTrue(malusNachher[UvBlockungRegelPrioritaet.SEHR_HOCH.nr] < 416,
				"Es müssen Lerngruppen besetzt werden (weniger als 416 fehlende Lehrkräfte).");
		assertFalse(dyn.gibAktuelleZuordnung().isEmpty(), "Es muss mindestens eine Lerngruppen-Lehrkraft-Zuordnung entstehen.");
		assertNotNull(dyn.gibMalusBeschreibung());
	}

}
