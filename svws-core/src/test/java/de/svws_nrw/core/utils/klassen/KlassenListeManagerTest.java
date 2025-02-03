package de.svws_nrw.core.utils.klassen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.svws_nrw.asd.adt.Pair;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.data.klassen.KlassenDaten;
import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.core.data.schueler.Schueler;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class KlassenListeManagerTest {


	private static List<Schuljahresabschnitt> schuljahresabschnitte;

	private final Schulform schulform = Schulform.GY;

	private final List<KlassenDaten> klassen = new ArrayList<>();

	private final List<SchuelerListeEintrag> schueler = new ArrayList<>();

	private final List<JahrgangsDaten> jahrgaenge = new ArrayList<>();

	private final List<LehrerListeEintrag> lehrer = new ArrayList<>();

	private KlassenListeManager klassenListeManager;


	@BeforeAll
	public static void setUp() {
		// This method will be run once before all tests
		ASDCoreTypeUtils.initAll();

		final Schuljahresabschnitt sab = new Schuljahresabschnitt();
		sab.id = 1L;
		sab.schuljahr = 2020;
		schuljahresabschnitte = List.of(sab);
	}


	@Test
	@DisplayName("Initialization | KlassenListeManager should be initialized correctly")
	void constructor() {
		// Test the initialization of classes
		klassenListeManager = new KlassenListeManager(1L, 1L, schuljahresabschnitte, schulform, klassen, schueler, jahrgaenge, lehrer);
		assertNotNull(klassenListeManager);
	}

	@Test
	@DisplayName("Initialization | KlassenListeManager should be initialized correctly, even with Schulform = null")
	void constructorSchulformNull() {
		klassenListeManager = new KlassenListeManager(1L, 1L, schuljahresabschnitte, schulform, klassen, schueler, jahrgaenge, lehrer);

		assertEquals(Schulgliederung.getBySchuljahrAndSchulform(2020, schulform), klassenListeManager.schulgliederungen.list());
	}

	@Test
	@DisplayName("Initialization | Klasse not in Jahrgeange throws Exception")
	void initKlassen() {
		final List<KlassenDaten> _klassen = new ArrayList<>();

		final KlassenDaten k = new KlassenDaten();
		k.id = -1;
		k.idJahrgang = 2L;
		_klassen.add(k);

		final Exception exception = assertThrows(DeveloperNotificationException.class, () ->
			new KlassenListeManager(1L, 1L, schuljahresabschnitte, schulform, _klassen, schueler, jahrgaenge, lehrer)
		);

		final String expectedMessage = "Kein gültiger Schlüsselwert.";
		final String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	@DisplayName("Initialization | Klasse in Jahrgeange throws no Exception")
	void initKlassen2() {

		final List<JahrgangsDaten> _jahrgaenge = new ArrayList<>();

		final JahrgangsDaten jgd = new JahrgangsDaten();
		jgd.id = 1L;
		_jahrgaenge.add(jgd);

		final List<KlassenDaten> _klassen = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			final KlassenDaten k = new KlassenDaten();
			k.id = -1;
			k.idJahrgang = 1L;
			_klassen.add(k);
		}


		klassenListeManager = new KlassenListeManager(1L, 1L, schuljahresabschnitte, schulform, _klassen, schueler, _jahrgaenge, lehrer);
		assertNotNull(klassenListeManager);
		assertEquals(10, klassenListeManager.liste.size());
	}

	@Test
	@DisplayName("Initialization | Klasse not in Jahrgeange throws no Exception if klassen.jahrgaene is null")
	void initKlassen3() {
		final List<KlassenDaten> _klassen = new ArrayList<>();

		final List<JahrgangsDaten> _jahrgaenge = new ArrayList<>();

		final JahrgangsDaten jgd = new JahrgangsDaten();
		jgd.id = 1L;
		_jahrgaenge.add(jgd);

		final KlassenDaten k = new KlassenDaten();
		k.id = -1;
		_klassen.add(k);

		klassenListeManager = new KlassenListeManager(1L, 1L, schuljahresabschnitte, schulform, _klassen, schueler, _jahrgaenge, lehrer);
		assertNotNull(klassenListeManager);
	}

	@Test
	@DisplayName("Update | Updating class data should reflect changes")
	void onSetDaten() {
		klassenListeManager = new KlassenListeManager(1L, 1L, schuljahresabschnitte, schulform, klassen, schueler, jahrgaenge, lehrer);

		final KlassenDaten originalData = new KlassenDaten();
		originalData.kuerzel = "A";
		final KlassenDaten newData = new KlassenDaten();
		newData.kuerzel = "B";
		assertTrue(klassenListeManager.onSetDaten(originalData, newData));
		assertEquals("B", originalData.kuerzel);
	}

	@Test
	@DisplayName("Update | Updating class return true when klassenleitung set")
	void onSetDaten2() {
		klassenListeManager = new KlassenListeManager(1L, 1L, schuljahresabschnitte, schulform, klassen, schueler, jahrgaenge, lehrer);

		klassenListeManager.auswahlKlassenLeitung = new LehrerListeEintrag();
		final KlassenDaten originalData = new KlassenDaten();
		originalData.kuerzel = "A";
		assertTrue(klassenListeManager.onSetDaten(originalData, originalData));
	}

	@Test
	@DisplayName("Reihenfolge | Klassenleitung Reihenfolge should be updated correctly")
	void updateReihenfolgeKlassenleitung() {

		final List<Long> klassenleitungen = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
		assertTrue(KlassenListeManager.updateReihenfolgeKlassenleitung(klassenleitungen, 2L, true));
		assertEquals(Arrays.asList(2L, 1L, 3L), klassenleitungen);
		assertTrue(KlassenListeManager.updateReihenfolgeKlassenleitung(klassenleitungen, 2L, false));
		assertEquals(Arrays.asList(1L, 2L, 3L), klassenleitungen);
	}

	@Test
	@DisplayName("Sequence | Class management Sequence should be false if List has single element")
	void updateReihenfolgeKlassenleitung2() {

		final List<Long> klassenleitungen = new ArrayList<>(Arrays.asList(1L));
		assertFalse(KlassenListeManager.updateReihenfolgeKlassenleitung(klassenleitungen, 2L, true));
	}

	@Test
	@DisplayName("Sequence | Upper edge Condition")
	void updateReihenfolgeKlassenleitung3() {
		final List<Long> klassenleitungen = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
		assertFalse(KlassenListeManager.updateReihenfolgeKlassenleitung(klassenleitungen, 1L, true));
		assertEquals(Arrays.asList(1L, 2L, 3L), klassenleitungen);
	}

	@Test
	@DisplayName("Sequence | Bottom edge Condition")
	void updateReihenfolgeKlassenleitung4() {
		final List<Long> klassenleitungen = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
		assertFalse(KlassenListeManager.updateReihenfolgeKlassenleitung(klassenleitungen, 3L, false));
		assertEquals(Arrays.asList(1L, 2L, 3L), klassenleitungen);
	}

	@Test
	@DisplayName("Sequence | An error is thrown if the class teacher is not in the list")
	void updateReihenfolgeKlassenleitung5() {

		final List<Long> klassenleitungen = new ArrayList<>(Arrays.asList(1L, 2L));

		final Exception exception = assertThrows(DeveloperNotificationException.class, () ->
			KlassenListeManager.updateReihenfolgeKlassenleitung(klassenleitungen, 3L, true)
		);

		final String expectedMessage = "Es wurde keine Klassenleitung mit der angegebenen Klassen- und Lehrer-ID gefunden.";
		final String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	@DisplayName("Retrieve Students | Should return a non-null list of students")
	void getSchuelerListe() {
		klassenListeManager = new KlassenListeManager(1L, 1L, schuljahresabschnitte, schulform, klassen, schueler, jahrgaenge, lehrer);

		final List<Schueler> schuelerListe = klassenListeManager.getSchuelerListe();
		assertNotNull(schuelerListe);
	}

	@Test
	@DisplayName("Compare | KlassenDaten should be compared correctly based on 'klassen' criteria")
	void compareAuswahlByKlassen() {
		klassenListeManager = new KlassenListeManager(1L, 1L, schuljahresabschnitte, schulform, klassen, schueler, jahrgaenge, lehrer);

		final KlassenDaten klasse1 = new KlassenDaten();
		klasse1.id = 1L;
		klasse1.kuerzel = "A";
		klasse1.schueler = Arrays.asList(new Schueler(), new Schueler());

		final KlassenDaten klasse2 = new KlassenDaten();
		klasse2.id = 2L;
		klasse2.kuerzel = "B";
		klasse2.schueler = Arrays.asList(new Schueler());

		klassenListeManager.orderSet(Arrays.asList(new Pair<>("klassen", true)));
		assertTrue(klassenListeManager.compareAuswahl(klasse1, klasse2) < 0);
	}

	@Test
	@DisplayName("Compare | KlassenDaten should be compared correctly based on 'schueleranzahl' criteria ascending")
	void compareAuswahlBySchueleranzahlAscending() {
		klassenListeManager = new KlassenListeManager(1L, 1L, schuljahresabschnitte, schulform, klassen, schueler, jahrgaenge, lehrer);

		final KlassenDaten klasse1 = new KlassenDaten();
		klasse1.id = 1L;
		klasse1.kuerzel = "A";
		klasse1.schueler = Arrays.asList(new Schueler(), new Schueler());

		final KlassenDaten klasse2 = new KlassenDaten();
		klasse2.id = 2L;
		klasse2.kuerzel = "B";
		klasse2.schueler = Arrays.asList(new Schueler());

		klassenListeManager.orderSet(Arrays.asList(new Pair<>("schueleranzahl", true)));
		assertTrue(klassenListeManager.compareAuswahl(klasse1, klasse2) > 0);
	}

	@Test
	@DisplayName("Compare | KlassenDaten should be compared correctly based on 'schueleranzahl' criteria descending")
	void compareAuswahlBySchueleranzahlDescending() {
		klassenListeManager = new KlassenListeManager(1L, 1L, schuljahresabschnitte, schulform, klassen, schueler, jahrgaenge, lehrer);

		final KlassenDaten klasse1 = new KlassenDaten();
		klasse1.id = 1L;
		klasse1.kuerzel = "A";
		klasse1.schueler = Arrays.asList(new Schueler(), new Schueler());

		final KlassenDaten klasse2 = new KlassenDaten();
		klasse2.id = 2L;
		klasse2.kuerzel = "B";
		klasse2.schueler = Arrays.asList(new Schueler());

		klassenListeManager.orderSet(Arrays.asList(new Pair<>("schueleranzahl", false)));
		assertTrue(klassenListeManager.compareAuswahl(klasse1, klasse2) < 0);
	}

	@Test
	@DisplayName("Compare | DeveloperNotificationException should be thrown for unknown criteria")
	void compareAuswahlWithUnknownCriteria() {
		klassenListeManager = new KlassenListeManager(1L, 1L, schuljahresabschnitte, schulform, klassen, schueler, jahrgaenge, lehrer);

		final KlassenDaten klasse1 = new KlassenDaten();
		klasse1.id = 1L;
		klasse1.kuerzel = "A";
		klasse1.schueler = Arrays.asList(new Schueler(), new Schueler());

		final KlassenDaten klasse2 = new KlassenDaten();
		klasse2.id = 2L;
		klasse2.kuerzel = "B";
		klasse2.schueler = Arrays.asList(new Schueler());

		klassenListeManager.orderSet(Arrays.asList(new Pair<>("unknown", true)));
		assertThrows(DeveloperNotificationException.class, () -> klassenListeManager.compareAuswahl(klasse1, klasse2));
	}

	@Test
	@DisplayName("Filter | Only visible classes should pass the filter when filter is set")
	void checkFilterNurSichtbar() {
		klassenListeManager = new KlassenListeManager(1L, 1L, schuljahresabschnitte, schulform, klassen, schueler, jahrgaenge, lehrer);

		klassenListeManager.setFilterNurSichtbar(true);
		final KlassenDaten visibleClass = new KlassenDaten();
		visibleClass.istSichtbar = true;
		final KlassenDaten invisibleClass = new KlassenDaten();
		invisibleClass.istSichtbar = false;

		assertTrue(klassenListeManager.checkFilter(visibleClass));
		assertFalse(klassenListeManager.checkFilter(invisibleClass));
	}
}
