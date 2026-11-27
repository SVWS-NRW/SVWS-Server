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
 * Testet den zweiten großen Gymnasium-Testdatensatz {@link UvDatensatzGymnasium2} auf strukturelle
 * Konsistenz und die Startfähigkeit des Blockungsalgorithmus.
 *
 * @author Benjamin A. Bartsch
 */
@DisplayName("Testet den UvDatensatzGymnasium2.")
class TestUvDatensatzGymnasium2 {

	private static final Logger LOG = new Logger();
	private static final Random RND = new Random(42L);


	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}


	@Test
	@DisplayName("Struktur: Anzahl der Objekte und Verknüpfungen")
	void testStruktur() {
		final UvManager man = UvDatensatzGymnasium2.erzeugeUvManager();
		final UvPlanungsabschnitt plan = man.planungsabschnittGetByIdOrException(UvDatensatzGymnasium2.PLANUNGSABSCHNITT);

		assertEquals(932, man.lerngruppeGetMengeByPlanungsabschnitt(plan).size(), "Es muss 932 Lerngruppen geben.");
		assertEquals(196, man.lehrerGetMengeAsList().size(), "Es muss 196 Lehrkräfte geben.");
		assertEquals(72, man.klasseGetMengeAsList().size(), "Es muss 72 Klassen geben.");
		assertEquals(49, man.schieneGetMengeAsList().size(), "Es muss 49 Schienen geben.");
		assertEquals(278, man.schuelergruppeGetMengeAsList().size(), "Es muss 278 Schülergruppen geben (72 Klassen + 206 Kurse).");
		assertEquals(171, man.lerngruppenSchieneGetMengeAsList().size(), "Es muss 171 Lerngruppen-Schienen-Zuordnungen geben.");

		// Extremfall: 56FörSp ist EIN Kurs über alle Klassen der Jahrgänge 05 und 06 (24 Klassen, 2 Jahrgänge)
		// und hat nur genau eine Lehrkraft mit Lehrbefähigung.
		final UvLerngruppe lgFoerSp = man.lerngruppeGetByIdOrException(1931L);
		assertEquals(2, man.jahrgangsdatenGetMengeByLerngruppe(lgFoerSp).size(), "Die Lerngruppe 1931 (56FörSp) muss 2 Jahrgänge haben.");
		assertEquals(24, man.klasseGetMengeByLerngruppe(lgFoerSp).size(), "Die Lerngruppe 1931 (56FörSp) muss 24 Klassen enthalten.");
		int anzahlFoerSp = 0;
		for (final UvLehrer lehrer : man.lehrerGetMengeAsList()) {
			if (man.lehrerHatLehrbefaehigungLerngruppe(lehrer, lgFoerSp)) {
				anzahlFoerSp++;
			}
		}
		assertEquals(1, anzahlFoerSp, "Für die Lerngruppe 1931 (56FörSp) darf nur eine Lehrkraft eine Lehrbefähigung haben.");

		// Alle Lehrkräfte haben ein Pflichtstundensoll > 0.
		for (final UvLehrer lehrer : man.lehrerGetMengeAsList()) {
			final Double soll = man.lehrerPflichtstundensollGetDoubleByLehrerAndPlanungsabschnitt(lehrer, plan);
			assertNotNull(soll, "Jede Lehrkraft muss ein Pflichtstundensoll haben.");
			assertTrue(soll > 0.0, "Das Pflichtstundensoll muss größer als 0 sein.");
		}

		// Fixierung und Verbot: Die fixierte Lehrkraft hat eine Lehrbefähigung für die Lerngruppe,
		// die verbotene Lehrkraft ebenfalls (sonst wäre das Verbot wirkungslos).
		final UvLerngruppe lgFixiert = man.lerngruppeGetByIdOrException(UvDatensatzGymnasium2.idLerngruppeFixiert);
		assertTrue(man.lehrerHatLehrbefaehigungLerngruppe(man.lehrerGetByIdOrException(UvDatensatzGymnasium2.idLehrkraftFixiert), lgFixiert),
				"Die fixierte Lehrkraft muss für die fixierte Lerngruppe eine Lehrbefähigung haben.");
		assertTrue(man.lehrerHatLehrbefaehigungLerngruppe(man.lehrerGetByIdOrException(UvDatensatzGymnasium2.idLehrkraftVerboten), lgFixiert),
				"Die verbotene Lehrkraft muss für die Lerngruppe eine Lehrbefähigung haben (sonst wäre das Verbot wirkungslos).");
	}


	@Test
	@DisplayName("Regeln: ein leerer Regelsatz enthält keine fehlerhaften Regeln")
	void testRegeln() {
		final UvManager man = UvDatensatzGymnasium2.erzeugeUvManager();
		final UvRegelManager manRegeln = new UvRegelManager(man, List.of());
		assertTrue(manRegeln.regelnGetMengeFehlerhaftAsList().isEmpty(), "Ein leerer Regelsatz darf keine fehlerhaften Regeln enthalten.");
	}


	@Test
	@DisplayName("Algorithmus: startet ohne Fehler und verbessert die Gesamtbewertung")
	void testAlgorithmus() {
		final UvManager man = UvDatensatzGymnasium2.erzeugeUvManager();
		final UvRegelManager manRegeln = new UvRegelManager(man, List.of());
		final UvPlanungsabschnitt plan = man.planungsabschnittGetByIdOrException(UvDatensatzGymnasium2.PLANUNGSABSCHNITT);
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
		assertTrue(malusNachher[UvBlockungRegelPrioritaet.SEHR_HOCH.nr] < 932,
				"Es müssen Lerngruppen besetzt werden (weniger als 932 fehlende Lehrkräfte).");
		assertFalse(dyn.gibAktuelleZuordnung().isEmpty(), "Es muss mindestens eine Lerngruppen-Lehrkraft-Zuordnung entstehen.");
		assertNotNull(dyn.gibMalusBeschreibung());
	}

}
