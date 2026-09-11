package de.svws_nrw.core.utils.uv;

import java.util.List;

import de.svws_nrw.core.data.fach.FachDaten;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.data.lehrer.LehrerUnterrichtsfach;
import de.svws_nrw.core.data.uv.UvFach;
import de.svws_nrw.core.data.uv.UvGrunddatenBundle;
import de.svws_nrw.core.data.uv.UvKlasse;
import de.svws_nrw.core.data.uv.UvKlassenLehrer;
import de.svws_nrw.core.data.uv.UvKurs;
import de.svws_nrw.core.data.uv.UvLehrer;
import de.svws_nrw.core.data.uv.UvLehrerAnrechnungsstunden;
import de.svws_nrw.core.data.uv.UvLehrerPflichtstundensoll;
import de.svws_nrw.core.data.uv.UvLerngruppe;
import de.svws_nrw.core.data.uv.UvLerngruppenLehrer;
import de.svws_nrw.core.data.uv.UvLerngruppenSchiene;
import de.svws_nrw.core.data.uv.UvPlanungsabschnitt;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittLehrer;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittSchueler;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittZeitraster;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittsdatenBundle;
import de.svws_nrw.core.data.uv.UvRaum;
import de.svws_nrw.core.data.uv.UvRaumgruppe;
import de.svws_nrw.core.data.uv.UvSchiene;
import de.svws_nrw.core.data.uv.UvSchuelergruppe;
import de.svws_nrw.core.data.uv.UvSchuelergruppeSchueler;
import de.svws_nrw.core.data.uv.UvStundentafel;
import de.svws_nrw.core.data.uv.UvStundentafelFach;
import de.svws_nrw.core.data.uv.UvUnterricht;
import de.svws_nrw.core.data.uv.UvUnterrichtLerngruppenlehrer;
import de.svws_nrw.core.data.uv.UvUnterrichtRaum;
import de.svws_nrw.core.data.uv.UvZeitraster;
import de.svws_nrw.core.data.uv.UvZeitrasterEintrag;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testet erste öffentliche Methoden des {@link UvManager} mit minimalen Testdaten.
 */
class TestUvManager {

	@Test
	@DisplayName("Schülergruppenjahrgänge berücksichtigen nur Mitglieder, sind eindeutig und fachlich sortiert")
	void testJahrgangsdatenGetMengeBySchuelergruppe() {
		final JahrgangsDaten neun = createJahrgang(30L, "9");
		neun.sortierung = 9;
		final JahrgangsDaten zehn = createJahrgang(10L, "10");
		zehn.sortierung = 10;
		final JahrgangsDaten ef = createJahrgang(11L, "EF");
		ef.sortierung = 11;
		final UvManager manager = new UvManager(List.of(ef, zehn, neun), List.of());
		manager.planungsabschnittAdd(createPlanungsabschnitt(131L));
		final UvSchuelergruppe gruppe = createSchuelergruppe(631L, 131L, 11L);
		manager.schuelergruppeAdd(gruppe);
		// Ein erlaubter Jahrgang ohne Mitglieder erscheint nicht in der Auswertung.
		assertTrue(manager.jahrgangsdatenGetMengeBySchuelergruppe(gruppe).isEmpty());
		manager.planungsabschnittSchuelerAddAll(List.of(
				createPlanungsabschnittSchueler(131L, 501L, 10L, null),
				createPlanungsabschnittSchueler(131L, 502L, 30L, null),
				createPlanungsabschnittSchueler(131L, 503L, 10L, null),
				createPlanungsabschnittSchueler(131L, 504L, 11L, null)));
		manager.schuelergruppeSchuelerAddAll(List.of(
				createSchuelergruppeSchueler(131L, 631L, 501L),
				createSchuelergruppeSchueler(131L, 631L, 502L),
				createSchuelergruppeSchueler(131L, 631L, 503L)));
		assertEquals(List.of(neun, zehn), manager.jahrgangsdatenGetMengeBySchuelergruppe(gruppe));
		assertEquals(3, manager.planungsabschnittSchuelerGetMengeBySchuelergruppe(gruppe).size());
		// Änderungen an der Ergebnisliste verändern den Manager nicht.
		manager.jahrgangsdatenGetMengeBySchuelergruppe(gruppe).clear();
		assertEquals(List.of(neun, zehn), manager.jahrgangsdatenGetMengeBySchuelergruppe(gruppe));
		manager.schuelergruppeSchuelerRemoveById(131L, 631L, 502L);
		assertEquals(List.of(zehn), manager.jahrgangsdatenGetMengeBySchuelergruppe(gruppe));
		manager.planungsabschnittSchuelerAllPatchAttributes(List.of(createPlanungsabschnittSchueler(131L, 501L, 11L, null)));
		assertEquals(List.of(zehn, ef), manager.jahrgangsdatenGetMengeBySchuelergruppe(gruppe));
	}

	@Test
	void testLerngruppeCheckZuordnungAllCombinations() {
		for (int kombination = 0; kombination < 8; kombination++) {
			final UvLerngruppe lerngruppe = new UvLerngruppe();
			lerngruppe.idKurs = ((kombination & 1) != 0) ? 1L : null;
			lerngruppe.idKlasse = ((kombination & 2) != 0) ? 2L : null;
			lerngruppe.idFach = ((kombination & 4) != 0) ? 3L : null;
			if ((kombination == 1) || (kombination == 6)) {
				UvManager.lerngruppeCheckZuordnung(lerngruppe);
			} else {
				assertThrows(DeveloperNotificationException.class, () -> UvManager.lerngruppeCheckZuordnung(lerngruppe));
			}
		}
	}

	@Test
	void testLerngruppePatchRejectsIncompleteAssignmentWithoutChangingManager() {
		final KlassenLerngruppeScenario s = createKlassenLerngruppeScenario(206L, 634L, 94L, 734L, 834L);
		final UvLerngruppe patch = createKlassenLerngruppe(834L, 206L, 734L, 94L);
		patch.idFach = null;
		assertThrows(DeveloperNotificationException.class, () -> s.manager.lerngruppePatchAttributes(patch));
		assertSame(s.lerngruppe, s.manager.lerngruppeGetByIdOrException(834L));
	}

	/**
	 * @param manager  Der zugehörige {@link UvManager}.
	 * @param planungsabschnitt  Der verwendete {@link UvPlanungsabschnitt}.
	 * @param gruppe  Die verwendete {@link UvSchuelergruppe}.
	 * @param fach  Das verwendete {@link UvFach}.
	 * @param klasse  Die verwendete {@link UvKlasse}.
	 * @param lerngruppe  Die verwendete {@link UvLerngruppe}.  */
	private record KlassenLerngruppeScenario(UvManager manager, UvPlanungsabschnitt planungsabschnitt, UvSchuelergruppe gruppe, UvFach fach, UvKlasse klasse,
			UvLerngruppe lerngruppe) {
	}

	/**
	 * @param basis  Das zugrunde liegende Klassen-/Lerngruppen-Szenario.
	 * @param unterrichtA  Der erste {@link UvUnterricht}.
	 * @param unterrichtB  Der zweite {@link UvUnterricht}.  */
	private record UnterrichtScenario(KlassenLerngruppeScenario basis, UvUnterricht unterrichtA, UvUnterricht unterrichtB) {
	}

	/**
	 * @param manager  Der zugehörige {@link UvManager}.
	 * @param planungsabschnitt  Der verwendete {@link UvPlanungsabschnitt}.
	 * @param lehrer  Der verwendete {@link UvLehrer}.
	 * @param planungsabschnittLehrer  Die verwendete {@link UvPlanungsabschnittLehrer}-Zuordnung.
	 * @param raum  Der verwendete {@link UvRaum}.
	 * @param zeitraster  Das verwendete {@link UvZeitraster}.
	 * @param fachA  Das erste verwendete {@link UvFach}.
	 * @param fachB  Das zweite verwendete {@link UvFach}.
	 * @param stundentafel  Die verwendete {@link UvStundentafel}.
	 * @param stundentafelFach  Das verwendete {@link UvStundentafelFach}.
	 * @param gruppe  Die verwendete {@link UvSchuelergruppe}.
	 * @param klasse  Die verwendete {@link UvKlasse}.
	 * @param lerngruppe  Die verwendete {@link UvLerngruppe}.
	 * @param lerngruppenLehrer  Die verwendete {@link UvLerngruppenLehrer}-Zuordnung.
	 * @param schiene  Die verwendete {@link UvSchiene}.
	 * @param lerngruppenSchiene  Die verwendete {@link UvLerngruppenSchiene}-Zuordnung.
	 * @param schueler  Der verwendete {@link UvPlanungsabschnittSchueler}.
	 * @param gruppenSchueler  Die verwendete {@link UvSchuelergruppeSchueler}-Zuordnung.  */
	private record GrundstrukturScenario(UvManager manager, UvPlanungsabschnitt planungsabschnitt, UvLehrer lehrer,
			UvPlanungsabschnittLehrer planungsabschnittLehrer, UvRaum raum, UvZeitraster zeitraster, UvFach fachA, UvFach fachB, UvStundentafel stundentafel,
			UvStundentafelFach stundentafelFach, UvSchuelergruppe gruppe, UvKlasse klasse, UvLerngruppe lerngruppe, UvLerngruppenLehrer lerngruppenLehrer,
			UvSchiene schiene, UvLerngruppenSchiene lerngruppenSchiene, UvPlanungsabschnittSchueler schueler, UvSchuelergruppeSchueler gruppenSchueler) {
	}

	private static UvManager createManagerWithMinimalBasis() {
		return new UvManager(List.of(createJahrgang(11L, "EF")), List.of(createFachdaten(21L, "M", "Mathematik")));
	}

	private static KlassenLerngruppeScenario createKlassenLerngruppeScenario(final long idPlanungsabschnitt, final long idSchuelergruppe,
			final long idUvFach, final long idKlasse, final long idLerngruppe) {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(idPlanungsabschnitt);
		final UvSchuelergruppe gruppe = createSchuelergruppe(idSchuelergruppe, idPlanungsabschnitt, 11L);
		final UvFach fach = createUvFach(idUvFach, 21L);
		final UvKlasse klasse = createKlasse(idKlasse, idPlanungsabschnitt, idSchuelergruppe);
		final UvLerngruppe lerngruppe = createKlassenLerngruppe(idLerngruppe, idPlanungsabschnitt, idKlasse, idUvFach);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);
		manager.fachAdd(fach);
		manager.klasseAdd(klasse);
		manager.lerngruppeAdd(lerngruppe);
		return new KlassenLerngruppeScenario(manager, planungsabschnitt, gruppe, fach, klasse, lerngruppe);
	}

	private static UnterrichtScenario createUnterrichtScenario(final long idPlanungsabschnitt, final long idSchuelergruppe, final long idUvFach,
			final long idKlasse, final long idLerngruppe, final long idUnterrichtA, final long idUnterrichtB) {
		final KlassenLerngruppeScenario basis =
				createKlassenLerngruppeScenario(idPlanungsabschnitt, idSchuelergruppe, idUvFach, idKlasse, idLerngruppe);
		final UvUnterricht unterrichtA = createUnterricht(idUnterrichtA, idPlanungsabschnitt, null, idLerngruppe);
		final UvUnterricht unterrichtB = createUnterricht(idUnterrichtB, idPlanungsabschnitt, null, idLerngruppe);
		basis.manager.unterrichtAddAll(List.of(unterrichtA, unterrichtB));
		return new UnterrichtScenario(basis, unterrichtA, unterrichtB);
	}

	private static UnterrichtScenario createUnterrichtMitZuordnungenScenario() {
		final UnterrichtScenario s = createUnterrichtScenario(206L, 634L, 94L, 734L, 834L, 1204L, 1205L);
		final UvManager manager = s.basis.manager;
		manager.raumAdd(createRaum(65L, "R205", null));
		manager.lehrerAdd(createLehrer(66L, "AAA"));
		manager.lerngruppenLehrerAdd(createLerngruppenLehrer(1107L, 206L, 834L, 66L));
		manager.unterrichtRaumAddAll(List.of(createUnterrichtRaum(206L, 1204L, 65L), createUnterrichtRaum(206L, 1205L, 65L)));
		manager.unterrichtLerngruppenlehrerAddAll(List.of(
				createUnterrichtLerngruppenlehrer(206L, 1204L, 1107L), createUnterrichtLerngruppenlehrer(206L, 1205L, 1107L)));
		return s;
	}

	private static GrundstrukturScenario createGrundstrukturScenario() {
		final UvManager manager = new UvManager(List.of(createJahrgang(11L, "EF")),
				List.of(createFachdaten(21L, "M", "Mathematik"), createFachdaten(22L, "D", "Deutsch")));
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(214L);
		final UvLehrer lehrer = createLehrer(70L, "AAA");
		final UvPlanungsabschnittLehrer planungsabschnittLehrer = createPlanungsabschnittLehrer(214L, 70L);
		final UvRaum raum = createRaum(68L, "R208", null);
		final UvZeitraster zeitraster = createZeitraster(1501L, "Raster X");
		final UvFach fachA = createUvFach(101L, 21L);
		final UvFach fachB = createUvFach(102L, 22L);
		final UvStundentafel stundentafel = createStundentafel(215L, 11L);
		final UvStundentafelFach stundentafelFach = createStundentafelFach(216L, 215L, 1, 101L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(639L, 214L, 11L);
		final UvKlasse klasse = createKlasse(740L, 214L, 639L);
		final UvLerngruppe lerngruppe = createKlassenLerngruppe(839L, 214L, 740L, 101L);
		final UvLerngruppenLehrer lerngruppenLehrer = createLerngruppenLehrer(1109L, 214L, 839L, 70L);
		final UvSchiene schiene = createSchiene(1036L, 214L, 6);
		final UvLerngruppenSchiene lerngruppenSchiene = createLerngruppenSchiene(214L, 839L, 1036L);
		final UvPlanungsabschnittSchueler schueler = createPlanungsabschnittSchueler(214L, 530L, 11L, 740L);
		final UvSchuelergruppeSchueler gruppenSchueler = createSchuelergruppeSchueler(214L, 639L, 530L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.lehrerAdd(lehrer);
		manager.planungsabschnittLehrerAdd(planungsabschnittLehrer);
		manager.raumAdd(raum);
		manager.zeitrasterAdd(zeitraster);
		manager.fachAddAll(List.of(fachA, fachB));
		manager.stundentafelAdd(stundentafel);
		manager.stundentafelFachAdd(stundentafelFach);
		manager.schuelergruppeAdd(gruppe);
		manager.klasseAdd(klasse);
		manager.lerngruppeAdd(lerngruppe);
		manager.lerngruppenLehrerAdd(lerngruppenLehrer);
		manager.schieneAdd(schiene);
		manager.lerngruppenSchieneAdd(lerngruppenSchiene);
		manager.planungsabschnittSchuelerAdd(schueler);
		manager.schuelergruppeSchuelerAdd(gruppenSchueler);
		return new GrundstrukturScenario(manager, planungsabschnitt, lehrer, planungsabschnittLehrer, raum, zeitraster, fachA, fachB, stundentafel,
				stundentafelFach, gruppe, klasse, lerngruppe, lerngruppenLehrer, schiene, lerngruppenSchiene, schueler, gruppenSchueler);
	}

	private static JahrgangsDaten createJahrgang(final long id, final String kuerzel) {
		final JahrgangsDaten jahrgang = new JahrgangsDaten();
		jahrgang.id = id;
		jahrgang.kuerzel = kuerzel;
		return jahrgang;
	}

	private static FachDaten createFachdaten(final long id, final String kuerzel, final String bezeichnung) {
		final FachDaten fach = new FachDaten();
		fach.id = id;
		fach.kuerzel = kuerzel;
		fach.kuerzelStatistik = kuerzel;
		fach.bezeichnung = bezeichnung;
		fach.istSichtbar = true;
		fach.sortierung = 1;
		return fach;
	}

	private static UvLehrer createLehrer(final long id, final String kuerzel) {
		final UvLehrer lehrer = new UvLehrer();
		lehrer.id = id;
		lehrer.kuerzel = kuerzel;
		lehrer.nachname = "Mustermann";
		lehrer.vorname = "Max";
		lehrer.datumZugang = "2024-08-01";
		return lehrer;
	}

	private static LehrerUnterrichtsfach createLehrerUnterrichtsfach(final long id, final long idLehrer, final long idFach, final boolean istKLehrer) {
		final LehrerUnterrichtsfach fach = new LehrerUnterrichtsfach();
		fach.id = id;
		fach.idLehrer = idLehrer;
		fach.idFach = idFach;
		fach.istKLehrer = istKLehrer;
		fach.istSek1 = true;
		fach.istSek2 = true;
		return fach;
	}

	private static UvRaumgruppe createRaumgruppe(final long id, final String bezeichnung) {
		final UvRaumgruppe raumgruppe = new UvRaumgruppe();
		raumgruppe.id = id;
		raumgruppe.bezeichnung = bezeichnung;
		raumgruppe.gueltigVon = "2025-01-01";
		return raumgruppe;
	}

	private static UvRaum createRaum(final long id, final String kuerzel, final Long idRaumgruppe) {
		final UvRaum raum = new UvRaum();
		raum.id = id;
		raum.kuerzel = kuerzel;
		raum.idRaumgruppe = idRaumgruppe;
		raum.gueltigVon = "2025-01-01";
		return raum;
	}

	private static UvFach createUvFach(final long id, final long idFach) {
		final UvFach fach = new UvFach();
		fach.id = id;
		fach.idFach = idFach;
		fach.gueltigVon = "2025-01-01";
		return fach;
	}

	private static UvStundentafel createStundentafel(final long id, final long idJahrgang) {
		final UvStundentafel stundentafel = new UvStundentafel();
		stundentafel.id = id;
		stundentafel.idJahrgang = idJahrgang;
		stundentafel.bezeichnung = "Stundentafel " + idJahrgang;
		stundentafel.gueltigVon = "2025-01-01";
		stundentafel.gueltigBis = null;
		return stundentafel;
	}

	private static UvStundentafelFach createStundentafelFach(final long id, final long idStundentafel, final int abschnitt, final long idFach) {
		final UvStundentafelFach fach = new UvStundentafelFach();
		fach.id = id;
		fach.idStundentafel = idStundentafel;
		fach.abschnitt = abschnitt;
		fach.idFach = idFach;
		fach.wochenstunden = 4.0;
		return fach;
	}

	private static UvZeitraster createZeitraster(final long id, final String bezeichnung) {
		final UvZeitraster zeitraster = new UvZeitraster();
		zeitraster.id = id;
		zeitraster.bezeichnung = bezeichnung;
		zeitraster.gueltigVon = "2025-01-01";
		return zeitraster;
	}

	private static UvZeitrasterEintrag createZeitrasterEintrag(final long id, final long idZeitraster) {
		final UvZeitrasterEintrag eintrag = new UvZeitrasterEintrag();
		eintrag.id = id;
		eintrag.idZeitraster = idZeitraster;
		eintrag.wochentag = 1;
		eintrag.stunde = 1;
		eintrag.beginn = 480;
		eintrag.ende = 525;
		return eintrag;
	}

	private static UvPlanungsabschnitt createPlanungsabschnitt(final long id) {
		final UvPlanungsabschnitt planungsabschnitt = new UvPlanungsabschnitt();
		planungsabschnitt.id = id;
		planungsabschnitt.gueltigVon = "2025-02-01";
		planungsabschnitt.gueltigBis = "2025-07-31";
		return planungsabschnitt;
	}

	private static UvPlanungsabschnittZeitraster createPlanungsabschnittZeitraster(final long idPlanungsabschnitt, final long idZeitraster) {
		final UvPlanungsabschnittZeitraster zuordnung = new UvPlanungsabschnittZeitraster();
		zuordnung.idPlanungsabschnitt = idPlanungsabschnitt;
		zuordnung.idZeitraster = idZeitraster;
		zuordnung.idsJahrgaenge.add(11L);
		return zuordnung;
	}

	private static UvSchuelergruppe createSchuelergruppe(final long id, final long idPlanungsabschnitt, final long... idsJahrgaenge) {
		final UvSchuelergruppe gruppe = new UvSchuelergruppe();
		gruppe.id = id;
		gruppe.idPlanungsabschnitt = idPlanungsabschnitt;
		gruppe.bezeichnung = "Gruppe " + id;
		for (final long idJahrgang : idsJahrgaenge) {
			gruppe.idsJahrgaengeErlaubt.add(idJahrgang);
		}
		return gruppe;
	}

	private static UvPlanungsabschnittSchueler createPlanungsabschnittSchueler(final long idPlanungsabschnitt, final long idSchueler, final long idJahrgang,
			final Long idKlasse) {
		final UvPlanungsabschnittSchueler schueler = new UvPlanungsabschnittSchueler();
		schueler.idPlanungsabschnitt = idPlanungsabschnitt;
		schueler.idSchueler = idSchueler;
		schueler.idJahrgang = idJahrgang;
		schueler.idKlasse = idKlasse;
		return schueler;
	}

	private static UvSchuelergruppeSchueler createSchuelergruppeSchueler(final long idPlanungsabschnitt, final long idSchuelergruppe, final long idSchueler) {
		final UvSchuelergruppeSchueler zuordnung = new UvSchuelergruppeSchueler();
		zuordnung.idPlanungsabschnitt = idPlanungsabschnitt;
		zuordnung.idSchuelergruppe = idSchuelergruppe;
		zuordnung.idSchueler = idSchueler;
		return zuordnung;
	}

	private static UvKlasse createKlasse(final long id, final long idPlanungsabschnitt, final long idSchuelergruppe) {
		final UvKlasse klasse = new UvKlasse();
		klasse.id = id;
		klasse.idPlanungsabschnitt = idPlanungsabschnitt;
		klasse.idSchuelergruppe = idSchuelergruppe;
		klasse.kuerzel = "5A";
		klasse.parallelitaet = "A";
		return klasse;
	}

	private static UvKurs createKurs(final long id, final long idPlanungsabschnitt, final long idSchuelergruppe, final long idFach) {
		final UvKurs kurs = new UvKurs();
		kurs.id = id;
		kurs.idPlanungsabschnitt = idPlanungsabschnitt;
		kurs.idSchuelergruppe = idSchuelergruppe;
		kurs.idFach = idFach;
		kurs.kursart = "GK";
		kurs.kursnummer = 1;
		return kurs;
	}

	private static UvLerngruppe createKlassenLerngruppe(final long id, final long idPlanungsabschnitt, final long idKlasse, final long idFach) {
		final UvLerngruppe lerngruppe = new UvLerngruppe();
		lerngruppe.id = id;
		lerngruppe.idPlanungsabschnitt = idPlanungsabschnitt;
		lerngruppe.idKlasse = idKlasse;
		lerngruppe.idFach = idFach;
		lerngruppe.wochenstunden = 3.0;
		return lerngruppe;
	}

	private static UvLerngruppe createKursLerngruppe(final long id, final long idPlanungsabschnitt, final long idKurs) {
		final UvLerngruppe lerngruppe = new UvLerngruppe();
		lerngruppe.id = id;
		lerngruppe.idPlanungsabschnitt = idPlanungsabschnitt;
		lerngruppe.idKurs = idKurs;
		lerngruppe.wochenstunden = 3.0;
		return lerngruppe;
	}

	private static UvSchiene createSchiene(final long id, final long idPlanungsabschnitt, final int nummer) {
		final UvSchiene schiene = new UvSchiene();
		schiene.id = id;
		schiene.idPlanungsabschnitt = idPlanungsabschnitt;
		schiene.nummer = nummer;
		schiene.bezeichnung = "Schiene " + nummer;
		schiene.idsJahrgaengeErlaubt.add(11L);
		return schiene;
	}

	private static UvLerngruppenLehrer createLerngruppenLehrer(final long id, final long idPlanungsabschnitt, final long idLerngruppe, final long idLehrer) {
		final UvLerngruppenLehrer zuordnung = new UvLerngruppenLehrer();
		zuordnung.id = id;
		zuordnung.idPlanungsabschnitt = idPlanungsabschnitt;
		zuordnung.idLerngruppe = idLerngruppe;
		zuordnung.idLehrer = idLehrer;
		zuordnung.reihenfolge = 1;
		zuordnung.wochenstunden = 3.0;
		zuordnung.wochenstundenAngerechnet = 3.0;
		return zuordnung;
	}

	private static UvLerngruppenSchiene createLerngruppenSchiene(final long idPlanungsabschnitt, final long idLerngruppe, final long idSchiene) {
		final UvLerngruppenSchiene zuordnung = new UvLerngruppenSchiene();
		zuordnung.idPlanungsabschnitt = idPlanungsabschnitt;
		zuordnung.idLerngruppe = idLerngruppe;
		zuordnung.idSchiene = idSchiene;
		return zuordnung;
	}

	private static UvUnterricht createUnterricht(final long id, final long idPlanungsabschnitt, final Long idZeitrasterEintrag, final long idLerngruppe) {
		final UvUnterricht unterricht = new UvUnterricht();
		unterricht.id = id;
		unterricht.idPlanungsabschnitt = idPlanungsabschnitt;
		unterricht.idZeitrasterEintrag = idZeitrasterEintrag;
		unterricht.idLerngruppe = idLerngruppe;
		return unterricht;
	}

	private static UvUnterrichtRaum createUnterrichtRaum(final long idPlanungsabschnitt, final long idUnterricht, final long idRaum) {
		final UvUnterrichtRaum zuordnung = new UvUnterrichtRaum();
		zuordnung.idPlanungsabschnitt = idPlanungsabschnitt;
		zuordnung.idUnterricht = idUnterricht;
		zuordnung.idRaum = idRaum;
		return zuordnung;
	}

	private static UvUnterrichtLerngruppenlehrer createUnterrichtLerngruppenlehrer(final long idPlanungsabschnitt, final long idUnterricht,
			final long idLerngruppenLehrer) {
		final UvUnterrichtLerngruppenlehrer zuordnung = new UvUnterrichtLerngruppenlehrer();
		zuordnung.idPlanungsabschnitt = idPlanungsabschnitt;
		zuordnung.idUnterricht = idUnterricht;
		zuordnung.idLerngruppenLehrer = idLerngruppenLehrer;
		return zuordnung;
	}

	private static UvKlassenLehrer createKlassenLehrer(final long id, final long idPlanungsabschnitt, final long idKlasse, final long idLehrer) {
		final UvKlassenLehrer zuordnung = new UvKlassenLehrer();
		zuordnung.id = id;
		zuordnung.idPlanungsabschnitt = idPlanungsabschnitt;
		zuordnung.idKlasse = idKlasse;
		zuordnung.idLehrer = idLehrer;
		zuordnung.reihenfolge = 1;
		return zuordnung;
	}

	private static UvPlanungsabschnittLehrer createPlanungsabschnittLehrer(final long idPlanungsabschnitt, final long idLehrer) {
		final UvPlanungsabschnittLehrer zuordnung = new UvPlanungsabschnittLehrer();
		zuordnung.idPlanungsabschnitt = idPlanungsabschnitt;
		zuordnung.idLehrer = idLehrer;
		return zuordnung;
	}

	private static UvLehrerPflichtstundensoll createLehrerPflichtstundensoll(final long id, final long idLehrer, final double pflichtstdSoll,
			final String gueltigVon, final String gueltigBis) {
		final UvLehrerPflichtstundensoll eintrag = new UvLehrerPflichtstundensoll();
		eintrag.id = id;
		eintrag.idLehrer = idLehrer;
		eintrag.pflichtstdSoll = pflichtstdSoll;
		eintrag.gueltigVon = gueltigVon;
		eintrag.gueltigBis = gueltigBis;
		return eintrag;
	}

	private static UvLehrerAnrechnungsstunden createLehrerAnrechnungsstunden(final long id, final long idLehrer, final String krz,
			final double anzahlStunden, final String gueltigVon, final String gueltigBis) {
		final UvLehrerAnrechnungsstunden eintrag = new UvLehrerAnrechnungsstunden();
		eintrag.id = id;
		eintrag.idLehrer = idLehrer;
		eintrag.anrechnungsgrundKrz = krz;
		eintrag.anzahlStunden = anzahlStunden;
		eintrag.gueltigVon = gueltigVon;
		eintrag.gueltigBis = gueltigBis;
		return eintrag;
	}

	@Test
	@DisplayName("jahrgangsdatenGetById liefert die im Konstruktor übergebenen Jahrgangsdaten")
	void testJahrgangsdatenGetById() {
		final JahrgangsDaten jahrgang = createJahrgang(11L, "EF");
		final UvManager manager = new UvManager(List.of(jahrgang), List.of(createFachdaten(21L, "M", "Mathematik")));

		final JahrgangsDaten result = manager.jahrgangsdatenGetById(11L);

		assertSame(jahrgang, result);
		assertEquals("EF", result.kuerzel);
	}

	@Test
	@DisplayName("jahrgangsdatenGetById wirft bei unbekannter ID eine Exception")
	void testJahrgangsdatenGetByIdUnknown() {
		final UvManager manager = createManagerWithMinimalBasis();

		assertThrows(DeveloperNotificationException.class, () -> manager.jahrgangsdatenGetById(999L));
	}

	@Test
	@DisplayName("jahrgangsdatenGetMenge liefert alle übergebenen Jahrgänge")
	void testJahrgangsdatenGetMenge() {
		final JahrgangsDaten ef = createJahrgang(11L, "EF");
		final JahrgangsDaten q1 = createJahrgang(12L, "Q1");
		final UvManager manager = new UvManager(List.of(ef, q1), List.of(createFachdaten(21L, "M", "Mathematik")));

		assertEquals(2, manager.jahrgangsdatenGetMenge().size());
		assertTrue(manager.jahrgangsdatenGetMenge().contains(ef));
		assertTrue(manager.jahrgangsdatenGetMenge().contains(q1));
	}

	@Test
	@DisplayName("grunddatenBundleAdd übernimmt Lehrer in den UvManager")
	void testGrunddatenBundleAddMitLehrer() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvLehrer lehrer = createLehrer(31L, "MUS");
		final UvGrunddatenBundle grunddaten = new UvGrunddatenBundle();
		grunddaten.lehrer.add(lehrer);

		manager.grunddatenBundleAdd(grunddaten);

		assertSame(lehrer, manager.lehrerGetByIdOrException(31L));
		assertEquals(1, manager.lehrerGetMengeAsList().size());
	}

	@Test
	@DisplayName("planungsabschnittsdatenBundleAdd lädt den Planungsabschnitt und planungsabschnittIsGeladen erkennt ihn")
	void testPlanungsabschnittsdatenBundleAdd() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(41L);
		final UvPlanungsabschnittsdatenBundle daten = new UvPlanungsabschnittsdatenBundle();
		daten.planungsabschnitt = planungsabschnitt;

		manager.planungsabschnittsdatenBundleAdd(daten);

		assertTrue(manager.planungsabschnittIsGeladen(planungsabschnitt));
	}

	@Test
	@DisplayName("planungsabschnittIsGeladen liefert false für nicht geladene Planungsabschnitte")
	void testPlanungsabschnittIsGeladenFalse() {
		final UvManager manager = createManagerWithMinimalBasis();

		assertFalse(manager.planungsabschnittIsGeladen(createPlanungsabschnitt(999L)));
	}

	@Test
	@DisplayName("lehrerAdd und lehrerGetMengeAsList liefern Lehrer sortiert nach Kürzel")
	void testLehrerAddAndGetMengeAsListSorted() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvLehrer lehrerB = createLehrer(31L, "ZZZ");
		final UvLehrer lehrerA = createLehrer(32L, "AAA");

		manager.lehrerAddAll(List.of(lehrerB, lehrerA));

		assertEquals(List.of(lehrerA, lehrerB), manager.lehrerGetMengeAsList());
		assertSame(lehrerA, manager.lehrerGetByIdOrException(32L));
		assertSame(lehrerB, manager.lehrerGetByIdOrException(31L));
	}

	@Test
	@DisplayName("lehrerAdd verweigert doppelte Lehrer-IDs")
	void testLehrerAddDuplicateIdThrows() {
		final UvManager manager = createManagerWithMinimalBasis();
		manager.lehrerAdd(createLehrer(31L, "AAA"));

		assertThrows(DeveloperNotificationException.class, () -> manager.lehrerAdd(createLehrer(31L, "BBB")));
	}

	@Test
	@DisplayName("lehrerAdd verweigert ungueltige Datumsangaben")
	void testLehrerAddInvalidDateThrows() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvLehrer lehrer = createLehrer(31L, "AAA");
		lehrer.datumZugang = "ungueltig";

		assertThrows(DeveloperNotificationException.class, () -> manager.lehrerAdd(lehrer));
	}

	@Test
	@DisplayName("lehrerRemoveById entfernt einen vorhandenen Lehrer vollständig")
	void testLehrerRemoveById() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvLehrer lehrer = createLehrer(31L, "AAA");
		manager.lehrerAdd(lehrer);

		manager.lehrerRemoveById(31L);

		assertEquals(0, manager.lehrerGetMengeAsList().size());
		assertThrows(DeveloperNotificationException.class, () -> manager.lehrerGetByIdOrException(31L));
	}

	@Test
	@DisplayName("lehrerRemoveById entfernt abhängige Zuordnungen und Folgegetter liefern leere Ergebnisse")
	void testLehrerRemoveByIdCascadesAcrossDependentStructures() {
		final UvManager manager = new UvManager(List.of(createJahrgang(11L, "EF")),
				List.of(createFachdaten(21L, "M", "Mathematik"), createFachdaten(22L, "D", "Deutsch")));
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(299L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(699L, 299L, 11L);
		final UvKlasse klasse = createKlasse(799L, 299L, 699L);
		final UvLehrer lehrer = createLehrer(89L, "ABC");
		lehrer.idKLehrer = 9001L;
		final UvLehrer andererLehrer = createLehrer(90L, "DEF");
		final UvLehrerPflichtstundensoll pflicht = createLehrerPflichtstundensoll(2401L, 89L, 25.5, "2025-01-01", null);
		final UvLehrerAnrechnungsstunden anrechnung = createLehrerAnrechnungsstunden(2402L, 89L, "AG", 1.0, "2025-01-01", null);
		final UvPlanungsabschnittLehrer planungsabschnittLehrer = createPlanungsabschnittLehrer(299L, 89L);
		final UvKlassenLehrer klassenLehrer = createKlassenLehrer(2404L, 299L, 799L, 89L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);
		manager.klasseAdd(klasse);
		manager.lehrerAddAll(List.of(lehrer, andererLehrer));
		manager.planungsabschnittLehrerAdd(planungsabschnittLehrer);
		manager.lehrerPflichtstundensollAdd(pflicht);
		manager.lehrerAnrechnungsstundenAdd(anrechnung);
		manager.klassenLehrerAdd(klassenLehrer);

		manager.lehrerRemoveById(89L);

		assertThrows(DeveloperNotificationException.class, () -> manager.lehrerGetByIdOrException(89L));
		assertTrue(manager.planungsabschnittLehrerGetMengeByPlanungsabschnitt(planungsabschnitt).isEmpty());
		assertTrue(manager.klassenLehrerGetMengeByPlanungsabschnitt(planungsabschnitt).isEmpty());
		assertTrue(manager.lehrerPflichtstundensollGetMengeByLehrer(lehrer).isEmpty());
		assertTrue(manager.lehrerAnrechnungsstundenGetMengeByLehrer(lehrer).isEmpty());
	}

	@Test
	@DisplayName("lehrerRemoveById entfernt normale und K-Lehrer-Unterrichtsfächer aus allen Folgegettern")
	void testLehrerRemoveByIdRemovesNormalAndKLehrerUnterrichtsfaecher() {
		final UvManager manager = new UvManager(List.of(createJahrgang(11L, "EF")),
				List.of(createFachdaten(21L, "M", "Mathematik"), createFachdaten(22L, "D", "Deutsch")));
		final UvLehrer lehrer = createLehrer(95L, "ABC");
		lehrer.idKLehrer = 9002L;
		final UvLehrer andererLehrer = createLehrer(96L, "DEF");
		final UvFach fachA = createUvFach(951L, 21L);
		final UvFach fachB = createUvFach(952L, 22L);
		final LehrerUnterrichtsfach normal = createLehrerUnterrichtsfach(953L, 95L, 951L, false);
		final LehrerUnterrichtsfach kLehrer = createLehrerUnterrichtsfach(954L, 9002L, 952L, true);
		final LehrerUnterrichtsfach fremd = createLehrerUnterrichtsfach(955L, 96L, 951L, false);
		manager.lehrerAddAll(List.of(lehrer, andererLehrer));
		manager.fachAddAll(List.of(fachA, fachB));
		manager.lehrerUnterrichtsfachAddAll(List.of(normal, kLehrer, fremd));

		manager.lehrerRemoveById(95L);

		assertEquals(List.of(fremd), manager.lehrerUnterrichtsfachGetMengeAsList());
		assertThrows(DeveloperNotificationException.class, () -> manager.lehrerUnterrichtsfachGetByIdOrException(false, 953L));
		assertThrows(DeveloperNotificationException.class, () -> manager.lehrerUnterrichtsfachGetByIdOrException(true, 954L));
	}

	@Test
	@DisplayName("lehrerRemoveAllById entfernt mehrere Lehrer")
	void testLehrerRemoveAllById() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvLehrer lehrerA = createLehrer(31L, "AAA");
		final UvLehrer lehrerB = createLehrer(32L, "BBB");
		manager.lehrerAddAll(List.of(lehrerA, lehrerB));

		manager.lehrerRemoveAllById(List.of(31L, 32L));

		assertTrue(manager.lehrerGetMengeAsList().isEmpty());
		assertThrows(DeveloperNotificationException.class, () -> manager.lehrerGetByIdOrException(31L));
		assertThrows(DeveloperNotificationException.class, () -> manager.lehrerGetByIdOrException(32L));
	}

	@Test
	@DisplayName("lehrerAllPatchAttributes ersetzt die gespeicherten Lehrerattribute")
	void testLehrerAllPatchAttributes() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvLehrer original = createLehrer(31L, "AAA");
		final UvLehrer gepatcht = createLehrer(31L, "BBB");
		gepatcht.vorname = "Berta";
		manager.lehrerAdd(original);

		manager.lehrerAllPatchAttributes(List.of(gepatcht));

		assertEquals("BBB", manager.lehrerGetByIdOrException(31L).kuerzel);
		assertEquals("Berta", manager.lehrerGetByIdOrException(31L).vorname);
	}

	@Test
	@DisplayName("lehrerRemove und lehrerRemoveAll entfernen Lehrer auch über Objektlisten")
	void testLehrerRemoveAndRemoveAll() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvLehrer lehrerA = createLehrer(31L, "AAA");
		final UvLehrer lehrerB = createLehrer(32L, "BBB");
		final UvLehrer lehrerC = createLehrer(33L, "CCC");
		manager.lehrerAddAll(List.of(lehrerA, lehrerB, lehrerC));

		manager.lehrerRemove(lehrerA);
		manager.lehrerRemoveAll(List.of(lehrerB, lehrerC));

		assertTrue(manager.lehrerGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("lehrerUnterrichtsfachAdd und zugehörige Getter liefern den Eintrag für einen Lehrer")
	void testLehrerUnterrichtsfachAddAndGet() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvLehrer lehrer = createLehrer(31L, "AAA");
		final LehrerUnterrichtsfach unterrichtsfach = createLehrerUnterrichtsfach(41L, 31L, 21L, false);
		manager.lehrerAdd(lehrer);

		manager.lehrerUnterrichtsfachAdd(unterrichtsfach);

		assertSame(unterrichtsfach, manager.lehrerUnterrichtsfachGetByIdOrException(false, 41L));
		assertEquals(List.of(unterrichtsfach), manager.lehrerUnterrichtsfachGetMengeByLehrer(lehrer));
	}

	@Test
	@DisplayName("lehrerUnterrichtsfachAdd verweigert doppelte IDs innerhalb derselben Lehrerart")
	void testLehrerUnterrichtsfachAddDuplicateThrows() {
		final UvManager manager = createManagerWithMinimalBasis();
		manager.lehrerAdd(createLehrer(31L, "AAA"));
		manager.lehrerUnterrichtsfachAdd(createLehrerUnterrichtsfach(41L, 31L, 21L, false));

		assertThrows(DeveloperNotificationException.class,
				() -> manager.lehrerUnterrichtsfachAdd(createLehrerUnterrichtsfach(41L, 31L, 21L, false)));
	}

	@Test
	@DisplayName("lehrerUnterrichtsfachRemove entfernt den Eintrag wieder")
	void testLehrerUnterrichtsfachRemove() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvLehrer lehrer = createLehrer(31L, "AAA");
		final LehrerUnterrichtsfach unterrichtsfach = createLehrerUnterrichtsfach(41L, 31L, 21L, false);
		manager.lehrerAdd(lehrer);
		manager.lehrerUnterrichtsfachAdd(unterrichtsfach);

		manager.lehrerUnterrichtsfachRemove(unterrichtsfach);

		assertTrue(manager.lehrerUnterrichtsfachGetMengeByLehrer(lehrer).isEmpty());
		assertThrows(DeveloperNotificationException.class, () -> manager.lehrerUnterrichtsfachGetByIdOrException(false, 41L));
	}

	@Test
	@DisplayName("lehrerUnterrichtsfachAddAll, GetMengeAsList, Patch und Remove-Varianten arbeiten konsistent")
	void testLehrerUnterrichtsfachBulkAndPatchAndRemove() {
		final UvManager manager = new UvManager(List.of(createJahrgang(11L, "EF")),
				List.of(createFachdaten(21L, "M", "Mathematik"), createFachdaten(22L, "D", "Deutsch")));
		final UvLehrer lehrer = createLehrer(31L, "AAA");
		final LehrerUnterrichtsfach fachA = createLehrerUnterrichtsfach(41L, 31L, 21L, false);
		final LehrerUnterrichtsfach fachB = createLehrerUnterrichtsfach(42L, 31L, 22L, true);
		fachB.idLehrer = 1234L;
		lehrer.idKLehrer = 1234L;
		manager.lehrerAdd(lehrer);

		manager.lehrerUnterrichtsfachAddAll(List.of(fachA, fachB));

		assertEquals(2, manager.lehrerUnterrichtsfachGetMengeAsList().size());
		assertTrue(manager.lehrerUnterrichtsfachGetMengeAsList().contains(fachA));
		assertTrue(manager.lehrerUnterrichtsfachGetMengeAsList().contains(fachB));

		final LehrerUnterrichtsfach fachAPatch = createLehrerUnterrichtsfach(41L, 31L, 21L, false);
		fachAPatch.istSek2 = false;
		manager.lehrerUnterrichtsfachAllPatchAttributes(List.of(fachAPatch));
		assertFalse(manager.lehrerUnterrichtsfachGetByIdOrException(false, 41L).istSek2);

		manager.lehrerUnterrichtsfachRemoveById(true, 42L);
		assertThrows(DeveloperNotificationException.class, () -> manager.lehrerUnterrichtsfachGetByIdOrException(true, 42L));

		manager.lehrerUnterrichtsfachRemoveAll(List.of(fachAPatch));
		assertTrue(manager.lehrerUnterrichtsfachGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("raumgruppeAdd und raumAdd liefern die erwarteten Räume und Zugriff per Kürzel")
	void testRaumgruppeUndRaumAddAndGet() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvRaumgruppe raumgruppe = createRaumgruppe(51L, "Computerräume");
		final UvRaum raumA = createRaum(61L, "R201", 51L);
		final UvRaum raumB = createRaum(62L, "R202", 51L);

		manager.raumgruppeAdd(raumgruppe);
		manager.raumAddAll(List.of(raumA, raumB));

		assertSame(raumgruppe, manager.raumgruppeGetByIdOrException(51L));
		assertSame(raumA, manager.raumGetByIdOrException(61L));
		assertSame(raumB, manager.raumGetByKuerzelOrNull("R202"));
		assertEquals(List.of(raumA, raumB), manager.raumGetMengeByRaumgruppe(raumgruppe));
	}

	@Test
	@DisplayName("raumAdd verweigert Räume mit unbekannter Raumgruppe")
	void testRaumAddUnknownRaumgruppeThrows() {
		final UvManager manager = createManagerWithMinimalBasis();

		assertThrows(DeveloperNotificationException.class, () -> manager.raumAdd(createRaum(61L, "R201", 999L)));
	}

	@Test
	@DisplayName("raumgruppeRemoveById entfernt die Gruppe und setzt referenzierende Räume auf keine Raumgruppe")
	void testRaumgruppeRemoveNullifiesRaumgruppeOnRaeume() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvRaumgruppe raumgruppe = createRaumgruppe(51L, "Computerräume");
		final UvRaum raum = createRaum(61L, "R201", 51L);
		manager.raumgruppeAdd(raumgruppe);
		manager.raumAdd(raum);

		manager.raumgruppeRemoveById(51L);

		assertNull(manager.raumGetByIdOrException(61L).idRaumgruppe);
		assertThrows(DeveloperNotificationException.class, () -> manager.raumgruppeGetByIdOrException(51L));
	}

	@Test
	@DisplayName("raumGetMengeAsList, Patch und Remove-Varianten arbeiten konsistent")
	void testRaumBulkPatchAndRemove() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvRaum raumA = createRaum(61L, "R201", null);
		final UvRaum raumB = createRaum(62L, "R202", null);
		final UvRaum raumC = createRaum(63L, "R203", null);
		manager.raumAddAll(List.of(raumA, raumB, raumC));

		assertEquals(List.of(raumA, raumB, raumC), manager.raumGetMengeAsList());

		final UvRaum raumAPatch = createRaum(61L, "R101", null);
		manager.raumAllPatchAttributes(List.of(raumAPatch));
		assertEquals("R101", manager.raumGetByIdOrException(61L).kuerzel);

		manager.raumRemove(raumB);
		manager.raumRemoveAll(List.of(raumC));
		manager.raumRemoveAllById(List.of(61L));
		assertTrue(manager.raumGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("raumgruppeAddAll, GetMengeAsList, Patch und Remove-Varianten arbeiten konsistent")
	void testRaumgruppeBulkPatchAndRemove() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvRaumgruppe gruppeA = createRaumgruppe(51L, "A");
		final UvRaumgruppe gruppeB = createRaumgruppe(52L, "B");
		final UvRaumgruppe gruppeC = createRaumgruppe(53L, "C");
		manager.raumgruppeAddAll(List.of(gruppeB, gruppeA, gruppeC));

		assertEquals(List.of(gruppeA, gruppeB, gruppeC), manager.raumgruppeGetMengeAsList());

		final UvRaumgruppe gruppeAPatch = createRaumgruppe(51L, "AA");
		manager.raumgruppeAllPatchAttributes(List.of(gruppeAPatch));
		assertEquals("AA", manager.raumgruppeGetByIdOrException(51L).bezeichnung);

		manager.raumgruppeRemove(gruppeB);
		manager.raumgruppeRemoveAll(List.of(gruppeC, gruppeAPatch));
		assertTrue(manager.raumgruppeGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("fachAdd, fachGetByKurs und fachGetMengeByFachdaten liefern die erwarteten UV-Fächer")
	void testFachAddAndFachGetByKurs() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(171L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(603L, 171L, 11L);
		final UvFach fach = createUvFach(71L, 21L);
		final UvKurs kurs = createKurs(901L, 171L, 603L, 71L);

		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);

		manager.fachAdd(fach);
		manager.kursAdd(kurs);

		assertSame(fach, manager.fachGetByIdOrException(71L));
		assertSame(fach, manager.fachGetByKurs(kurs));
		assertEquals(List.of(fach), manager.fachGetMengeByFachdaten(createFachdaten(21L, "M", "Mathematik")));
	}

	@Test
	@DisplayName("fachdaten-Getter liefern die erwarteten Stammdaten aus verschiedenen UV-Kontexten")
	void testFachdatenGetter() {
		final FachDaten mathe = createFachdaten(21L, "M", "Mathematik");
		final FachDaten deutsch = createFachdaten(22L, "D", "Deutsch");
		final UvManager manager = new UvManager(List.of(createJahrgang(11L, "EF")), List.of(mathe, deutsch));
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(171L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(603L, 171L, 11L);
		final UvLehrer lehrer = createLehrer(31L, "AAA");
		lehrer.idKLehrer = 1234L;
		final UvFach fach = createUvFach(71L, 21L);
		final UvKurs kurs = createKurs(901L, 171L, 603L, 71L);
		final LehrerUnterrichtsfach unterrichtsfach = createLehrerUnterrichtsfach(41L, 31L, 21L, false);
		final UvStundentafel stundentafel = createStundentafel(81L, 11L);
		final UvStundentafelFach stundentafelFach = createStundentafelFach(91L, 81L, 1, 71L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);
		manager.lehrerAdd(lehrer);
		manager.fachAdd(fach);
		manager.kursAdd(kurs);
		manager.lehrerUnterrichtsfachAdd(unterrichtsfach);
		manager.stundentafelAdd(stundentafel);
		manager.stundentafelFachAdd(stundentafelFach);

		assertEquals(List.of(mathe, deutsch), manager.fachdatenGetMenge());
		assertSame(mathe, manager.fachdatenGetByFach(fach));
		assertSame(mathe, manager.fachdatenGetByKurs(kurs));
		assertSame(mathe, manager.fachdatenGetByLehrerUnterrichtsfach(unterrichtsfach));
		assertSame(mathe, manager.fachdatenGetByStundentafelFach(stundentafelFach));
	}

	@Test
	@DisplayName("fachAdd verweigert ein Fach mit ungültigem Gültigkeitsbeginn")
	void testFachAddInvalidDateThrows() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvFach fach = createUvFach(71L, 21L);
		fach.gueltigVon = "ungueltig";

		assertThrows(DeveloperNotificationException.class, () -> manager.fachAdd(fach));
	}

	@Test
	@DisplayName("stundentafelAdd und stundentafelFachAdd liefern die Fächer einer Stundentafel und verwendete Fächer")
	void testStundentafelAndStundentafelFachAddAndGet() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvFach fachVerwendet = createUvFach(71L, 21L);
		final UvFach fachUnverwendet = createUvFach(72L, 21L);
		fachUnverwendet.gueltigVon = "2026-01-01";
		final UvStundentafel stundentafel = createStundentafel(81L, 11L);
		final UvStundentafelFach stundentafelFach = createStundentafelFach(91L, 81L, 1, 71L);
		manager.fachAddAll(List.of(fachVerwendet, fachUnverwendet));
		manager.stundentafelAdd(stundentafel);

		manager.stundentafelFachAdd(stundentafelFach);

		assertSame(stundentafel, manager.stundentafelGetByIdOrException(81L));
		assertSame(stundentafelFach, manager.stundentafelFachGetByIdOrException(91L));
		assertEquals(List.of(stundentafelFach), manager.stundentafelFachGetMengeByStundentafelAndAbschnitt(stundentafel, 1));
		assertSame(fachVerwendet, manager.fachGetByStundentafelFach(stundentafelFach));
		assertTrue(manager.fachGetMengeVerwendetInStundentafel().contains(fachVerwendet));
	}

	@Test
	@DisplayName("stundentafelFachAdd verweigert Einträge mit unbekannter Stundentafel")
	void testStundentafelFachAddUnknownStundentafelThrows() {
		final UvManager manager = createManagerWithMinimalBasis();
		manager.fachAdd(createUvFach(71L, 21L));

		assertThrows(DeveloperNotificationException.class, () -> manager.stundentafelFachAdd(createStundentafelFach(91L, 999L, 1, 71L)));
	}

	@Test
	@DisplayName("stundentafelRemoveById entfernt auch zugehörige StundentafelFächer")
	void testStundentafelRemoveCascadesToStundentafelFach() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvFach fach = createUvFach(71L, 21L);
		final UvStundentafel stundentafel = createStundentafel(81L, 11L);
		final UvStundentafelFach stundentafelFach = createStundentafelFach(91L, 81L, 1, 71L);
		manager.fachAdd(fach);
		manager.stundentafelAdd(stundentafel);
		manager.stundentafelFachAdd(stundentafelFach);

		manager.stundentafelRemoveById(81L);

		assertThrows(DeveloperNotificationException.class, () -> manager.stundentafelGetByIdOrException(81L));
		assertThrows(DeveloperNotificationException.class, () -> manager.stundentafelFachGetByIdOrException(91L));
		assertTrue(manager.stundentafelFachGetMengeAsList().isEmpty());
		assertTrue(manager.fachGetMengeVerwendetInStundentafel().isEmpty());
	}

	@Test
	@DisplayName("stundentafelAddAll, Patch und Remove-Varianten arbeiten konsistent")
	void testStundentafelBulkPatchAndRemove() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvStundentafel tafelA = createStundentafel(81L, 11L);
		final UvStundentafel tafelB = createStundentafel(82L, 11L);
		final UvStundentafel tafelC = createStundentafel(83L, 11L);

		manager.stundentafelAddAll(List.of(tafelA, tafelB, tafelC));
		assertEquals(List.of(tafelA, tafelB, tafelC), manager.stundentafelGetMengeAsList());

		final UvStundentafel tafelAPatch = createStundentafel(81L, 12L);
		manager.stundentafelAllPatchAttributes(List.of(tafelAPatch));
		assertEquals(12L, manager.stundentafelGetByIdOrException(81L).idJahrgang);

		manager.stundentafelRemove(tafelB);
		manager.stundentafelRemoveAll(List.of(tafelC));
		manager.stundentafelRemoveAllById(List.of(81L));
		assertTrue(manager.stundentafelGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("stundentafelFachAddAll, Patch und Remove-Varianten arbeiten konsistent")
	void testStundentafelFachBulkPatchAndRemove() {
		final UvManager manager = new UvManager(List.of(createJahrgang(11L, "EF")),
				List.of(createFachdaten(21L, "M", "Mathematik"), createFachdaten(22L, "D", "Deutsch"), createFachdaten(23L, "E", "Englisch")));
		final UvFach fachA = createUvFach(71L, 21L);
		final UvFach fachB = createUvFach(72L, 22L);
		final UvFach fachC = createUvFach(73L, 23L);
		final UvStundentafel stundentafel = createStundentafel(81L, 11L);
		final UvStundentafelFach stfA = createStundentafelFach(91L, 81L, 1, 71L);
		final UvStundentafelFach stfB = createStundentafelFach(92L, 81L, 1, 72L);
		final UvStundentafelFach stfC = createStundentafelFach(93L, 81L, 2, 73L);
		manager.fachAddAll(List.of(fachA, fachB, fachC));
		manager.stundentafelAdd(stundentafel);

		manager.stundentafelFachAddAll(List.of(stfA, stfB, stfC));
		assertEquals(3, manager.stundentafelFachGetMengeAsList().size());
		assertTrue(manager.stundentafelFachGetMengeAsList().contains(stfA));
		assertTrue(manager.stundentafelFachGetMengeAsList().contains(stfB));
		assertTrue(manager.stundentafelFachGetMengeAsList().contains(stfC));

		final UvStundentafelFach stfAPatch = createStundentafelFach(91L, 81L, 1, 71L);
		stfAPatch.wochenstunden = 7.5;
		manager.stundentafelFachAllPatchAttributes(List.of(stfAPatch));
		assertSame(stfAPatch, manager.stundentafelFachGetByIdOrException(91L));
		assertEquals(7.5, manager.stundentafelFachGetByIdOrException(91L).wochenstunden);

		manager.stundentafelFachRemove(stfB);
		manager.stundentafelFachRemoveAll(List.of(stfC));
		manager.stundentafelFachRemoveAllById(List.of(91L));
		assertTrue(manager.stundentafelFachGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("fachGetMengeAsList, Patch und Remove-Varianten arbeiten konsistent")
	void testFachBulkPatchAndRemove() {
		final UvManager manager = new UvManager(List.of(createJahrgang(11L, "EF")),
				List.of(createFachdaten(21L, "M", "Mathematik"), createFachdaten(22L, "D", "Deutsch"), createFachdaten(23L, "E", "Englisch")));
		final UvFach fachA = createUvFach(71L, 21L);
		final UvFach fachB = createUvFach(72L, 22L);
		final UvFach fachC = createUvFach(73L, 23L);

		manager.fachAddAll(List.of(fachC, fachA, fachB));
		assertEquals(List.of(fachB, fachC, fachA), manager.fachGetMengeAsList());

		final UvFach fachAPatch = createUvFach(71L, 22L);
		fachAPatch.gueltigVon = "2025-08-01";
		manager.fachPatchAttributes(fachAPatch);
		assertEquals(22L, manager.fachGetByIdOrException(71L).idFach);

		final UvFach fachBPatch = createUvFach(72L, 21L);
		manager.fachAllPatchAttributes(List.of(fachBPatch));
		assertEquals(21L, manager.fachGetByIdOrException(72L).idFach);

		manager.fachRemove(fachC);
		manager.fachRemoveAll(List.of(fachAPatch));
		manager.fachRemoveAllById(List.of(72L));
		assertTrue(manager.fachGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("zeitrasterAdd und zeitrasterGetByIdOrNull liefern vorhandene und fehlende Zeitraster korrekt")
	void testZeitrasterAddAndGet() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvZeitraster zeitraster = createZeitraster(101L, "Raster A");

		manager.zeitrasterAdd(zeitraster);

		assertSame(zeitraster, manager.zeitrasterGetByIdOrException(101L));
		assertSame(zeitraster, manager.zeitrasterGetByIdOrNull(101L));
		assertNull(manager.zeitrasterGetByIdOrNull(999L));
	}

	@Test
	@DisplayName("zeitrasterAdd verweigert leere Bezeichnungen")
	void testZeitrasterAddBlankBezeichnungThrows() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvZeitraster zeitraster = createZeitraster(101L, " ");

		assertThrows(DeveloperNotificationException.class, () -> manager.zeitrasterAdd(zeitraster));
	}

	@Test
	@DisplayName("zeitrasterEintragAdd und zeitrasterEintragGetMengeByZeitraster liefern Einträge eines Zeitrasters")
	void testZeitrasterEintragAddAndGetByZeitraster() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvZeitraster zeitraster = createZeitraster(101L, "Raster A");
		final UvZeitrasterEintrag eintrag = createZeitrasterEintrag(111L, 101L);
		manager.zeitrasterAdd(zeitraster);

		manager.zeitrasterEintragAdd(eintrag);

		assertSame(eintrag, manager.zeitrasterEintragGetByIdOrException(101L, 111L));
		assertEquals(List.of(eintrag), manager.zeitrasterEintragGetMengeByZeitraster(101L));
	}

	@Test
	@DisplayName("zeitrasterEintragAdd verweigert Einträge mit ungültigem Wochentag")
	void testZeitrasterEintragAddInvalidWochentagThrows() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvZeitraster zeitraster = createZeitraster(101L, "Raster A");
		final UvZeitrasterEintrag eintrag = createZeitrasterEintrag(111L, 101L);
		eintrag.wochentag = 8;
		manager.zeitrasterAdd(zeitraster);

		assertThrows(DeveloperNotificationException.class, () -> manager.zeitrasterEintragAdd(eintrag));
	}

	@Test
	@DisplayName("zeitrasterEintragRemoveAllByZeitraster entfernt alle Einträge des Zeitrasters")
	void testZeitrasterEintragRemoveAllByZeitraster() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvZeitraster zeitraster = createZeitraster(101L, "Raster A");
		final UvZeitrasterEintrag eintragA = createZeitrasterEintrag(111L, 101L);
		final UvZeitrasterEintrag eintragB = createZeitrasterEintrag(112L, 101L);
		eintragB.stunde = 2;
		eintragB.beginn = 530;
		eintragB.ende = 575;
		manager.zeitrasterAdd(zeitraster);
		manager.zeitrasterEintragAddAll(List.of(eintragA, eintragB));

		manager.zeitrasterEintragRemoveAllByZeitraster(101L);

		assertTrue(manager.zeitrasterEintragGetMengeByZeitraster(101L).isEmpty());
		assertThrows(DeveloperNotificationException.class, () -> manager.zeitrasterEintragGetByIdOrException(101L, 111L));
		assertThrows(DeveloperNotificationException.class, () -> manager.zeitrasterEintragGetByIdOrException(101L, 112L));
	}

	@Test
	@DisplayName("zeitrasterAddAll, Patch und Remove-Varianten arbeiten konsistent")
	void testZeitrasterBulkPatchAndRemove() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvZeitraster rasterA = createZeitraster(101L, "Raster A");
		final UvZeitraster rasterB = createZeitraster(102L, "Raster B");
		final UvZeitraster rasterC = createZeitraster(103L, "Raster C");

		manager.zeitrasterAddAll(List.of(rasterC, rasterA, rasterB));
		assertEquals(List.of(rasterA, rasterB, rasterC), manager.zeitrasterGetMengeAsList());

		final UvZeitraster rasterAPatch = createZeitraster(101L, "Raster AA");
		manager.zeitrasterAllPatchAttributes(List.of(rasterAPatch));
		assertEquals("Raster AA", manager.zeitrasterGetByIdOrException(101L).bezeichnung);

		manager.zeitrasterRemove(rasterB);
		manager.zeitrasterRemoveAll(List.of(rasterC));
		manager.zeitrasterRemoveAllById(List.of(101L));
		assertTrue(manager.zeitrasterGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("zeitrasterEintragAddAll, Patch und Remove-Varianten arbeiten konsistent")
	void testZeitrasterEintragBulkPatchAndRemove() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvZeitraster raster = createZeitraster(101L, "Raster A");
		final UvZeitrasterEintrag eintragA = createZeitrasterEintrag(111L, 101L);
		final UvZeitrasterEintrag eintragB = createZeitrasterEintrag(112L, 101L);
		eintragB.stunde = 2;
		eintragB.beginn = 530;
		eintragB.ende = 575;
		final UvZeitrasterEintrag eintragC = createZeitrasterEintrag(113L, 101L);
		eintragC.stunde = 3;
		eintragC.beginn = 580;
		eintragC.ende = 625;
		manager.zeitrasterAdd(raster);

		manager.zeitrasterEintragAddAll(List.of(eintragC, eintragA, eintragB));
		assertEquals(List.of(eintragA, eintragB, eintragC), manager.zeitrasterEintragGetMengeAsList());

		final UvZeitrasterEintrag eintragAPatch = createZeitrasterEintrag(111L, 101L);
		eintragAPatch.wochentag = 2;
		manager.zeitrasterEintragAllPatchAttributes(List.of(eintragAPatch));
		assertEquals(2, manager.zeitrasterEintragGetByIdOrException(101L, 111L).wochentag);

		manager.zeitrasterEintragRemove(eintragB);
		manager.zeitrasterEintragRemoveAll(List.of(eintragC));
		manager.zeitrasterEintragRemoveById(101L, 111L);
		assertTrue(manager.zeitrasterEintragGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("planungsabschnittZeitrasterAdd und Getter liefern die Zuordnung korrekt")
	void testPlanungsabschnittZeitrasterAddAndGet() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(121L);
		final UvZeitraster zeitraster = createZeitraster(122L, "Raster A");
		final UvPlanungsabschnittZeitraster zuordnung = createPlanungsabschnittZeitraster(121L, 122L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.zeitrasterAdd(zeitraster);

		manager.planungsabschnittZeitrasterAdd(zuordnung);

		assertSame(zuordnung, manager.planungsabschnittZeitrasterGetByIdOrException(121L, 122L));
		assertEquals(List.of(zuordnung), manager.planungsabschnittZeitrasterGetMengeByPlanungsabschnitt(121L));
	}

	@Test
	@DisplayName("planungsabschnittZeitrasterAdd verweigert Zuordnungen mit unbekanntem Zeitraster")
	void testPlanungsabschnittZeitrasterAddUnknownZeitrasterThrows() {
		final UvManager manager = createManagerWithMinimalBasis();
		manager.planungsabschnittAdd(createPlanungsabschnitt(121L));

		assertThrows(DeveloperNotificationException.class,
				() -> manager.planungsabschnittZeitrasterAdd(createPlanungsabschnittZeitraster(121L, 999L)));
	}

	@Test
	@DisplayName("planungsabschnittZeitrasterAddAll, Patch und Remove-Varianten arbeiten konsistent")
	void testPlanungsabschnittZeitrasterBulkPatchAndRemove() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt paA = createPlanungsabschnitt(121L);
		final UvPlanungsabschnitt paB = createPlanungsabschnitt(122L);
		final UvZeitraster rasterA = createZeitraster(131L, "Raster A");
		final UvZeitraster rasterB = createZeitraster(132L, "Raster B");
		final UvZeitraster rasterC = createZeitraster(133L, "Raster C");
		final UvPlanungsabschnittZeitraster zuordnungA = createPlanungsabschnittZeitraster(121L, 131L);
		final UvPlanungsabschnittZeitraster zuordnungB = createPlanungsabschnittZeitraster(121L, 132L);
		final UvPlanungsabschnittZeitraster zuordnungC = createPlanungsabschnittZeitraster(122L, 133L);
		manager.planungsabschnittAdd(paA);
		manager.planungsabschnittAdd(paB);
		manager.zeitrasterAddAll(List.of(rasterA, rasterB, rasterC));

		manager.planungsabschnittZeitrasterAddAll(List.of(zuordnungA, zuordnungB, zuordnungC));
		assertEquals(3, manager.planungsabschnittZeitrasterGetMengeAsList().size());

		final UvPlanungsabschnittZeitraster zuordnungAPatch = createPlanungsabschnittZeitraster(121L, 131L);
		manager.planungsabschnittZeitrasterAllPatchAttributes(List.of(zuordnungAPatch));
		assertSame(zuordnungAPatch, manager.planungsabschnittZeitrasterGetByIdOrException(121L, 131L));

		manager.planungsabschnittZeitrasterRemove(zuordnungB);
		manager.planungsabschnittZeitrasterRemoveAllById(121L, List.of(131L));
		manager.planungsabschnittZeitrasterRemoveAllByPlanungsabschnitt(122L);
		assertTrue(manager.planungsabschnittZeitrasterGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("planungsabschnittSchuelerAdd und Getter liefern Schüler korrekt")
	void testPlanungsabschnittSchuelerAddAndGet() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(131L);
		final UvPlanungsabschnittSchueler schueler = createPlanungsabschnittSchueler(131L, 501L, 11L, null);
		manager.planungsabschnittAdd(planungsabschnitt);

		manager.planungsabschnittSchuelerAdd(schueler);

		assertSame(schueler, manager.planungsabschnittSchuelerGetByIdOrException(131L, 501L));
		assertEquals(List.of(schueler), manager.planungsabschnittSchuelerGetMengeByPlanungsabschnitt(planungsabschnitt));
	}

	@Test
	@DisplayName("planungsabschnittSchuelerAdd verweigert Zuordnungen mit unbekanntem Planungsabschnitt")
	void testPlanungsabschnittSchuelerAddUnknownPlanungsabschnittThrows() {
		final UvManager manager = createManagerWithMinimalBasis();

		assertThrows(DeveloperNotificationException.class,
				() -> manager.planungsabschnittSchuelerAdd(createPlanungsabschnittSchueler(131L, 501L, 11L, null)));
	}

	@Test
	@DisplayName("schuelergruppeAdd und schuelergruppeSchuelerAdd liefern Gruppen und Gruppenzuordnungen")
	void testSchuelergruppeAndSchuelergruppeSchuelerAddAndGet() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(141L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(601L, 141L, 11L);
		final UvPlanungsabschnittSchueler schueler = createPlanungsabschnittSchueler(141L, 501L, 11L, null);
		final UvSchuelergruppeSchueler zuordnung = createSchuelergruppeSchueler(141L, 601L, 501L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.planungsabschnittSchuelerAdd(schueler);
		manager.schuelergruppeAdd(gruppe);

		manager.schuelergruppeSchuelerAdd(zuordnung);

		assertSame(gruppe, manager.schuelergruppeGetByIdOrException(601L));
		assertEquals(List.of(gruppe), manager.schuelergruppeGetMengeByPlanungsabschnitt(planungsabschnitt));
		assertSame(zuordnung, manager.schuelergruppeSchuelerGetByIdOrException(141L, 601L, 501L));
		assertEquals(List.of(zuordnung), manager.schuelergruppeSchuelerGetMengeBySchuelergruppe(141L, 601L));
	}

	@Test
	@DisplayName("schuelergruppeSchuelerAdd verweigert Zuordnungen mit unbekannter Schülergruppe")
	void testSchuelergruppeSchuelerAddUnknownGruppeThrows() {
		final UvManager manager = createManagerWithMinimalBasis();
		manager.planungsabschnittAdd(createPlanungsabschnitt(141L));

		assertThrows(DeveloperNotificationException.class,
				() -> manager.schuelergruppeSchuelerAdd(createSchuelergruppeSchueler(141L, 999L, 501L)));
	}

	@Test
	@DisplayName("planungsabschnittSchuelerRemoveById entfernt auch Gruppenzuordnungen des Schülers")
	void testPlanungsabschnittSchuelerRemoveCascadesToSchuelergruppeSchueler() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(141L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(601L, 141L, 11L);
		final UvPlanungsabschnittSchueler schueler = createPlanungsabschnittSchueler(141L, 501L, 11L, null);
		final UvSchuelergruppeSchueler zuordnung = createSchuelergruppeSchueler(141L, 601L, 501L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.planungsabschnittSchuelerAdd(schueler);
		manager.schuelergruppeAdd(gruppe);
		manager.schuelergruppeSchuelerAdd(zuordnung);

		manager.planungsabschnittSchuelerRemoveById(141L, 501L);

		assertThrows(DeveloperNotificationException.class, () -> manager.planungsabschnittSchuelerGetByIdOrException(141L, 501L));
		assertTrue(manager.schuelergruppeSchuelerGetMengeBySchuelergruppe(141L, 601L).isEmpty());
		assertTrue(manager.planungsabschnittSchuelerGetMengeBySchuelergruppe(gruppe).isEmpty());
	}

	@Test
	@DisplayName("planungsabschnittSchuelerAddAll, Patch und Remove-Varianten arbeiten konsistent")
	void testPlanungsabschnittSchuelerBulkPatchAndRemove() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(142L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(602L, 142L, 11L);
		final UvKlasse klasse = createKlasse(701L, 142L, 602L);
		final UvPlanungsabschnittSchueler schuelerA = createPlanungsabschnittSchueler(142L, 501L, 11L, 701L);
		final UvPlanungsabschnittSchueler schuelerB = createPlanungsabschnittSchueler(142L, 502L, 11L, 701L);
		final UvPlanungsabschnittSchueler schuelerC = createPlanungsabschnittSchueler(142L, 503L, 11L, null);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);
		manager.klasseAdd(klasse);

		manager.planungsabschnittSchuelerAddAll(List.of(schuelerA, schuelerB, schuelerC));
		manager.schuelergruppeSchuelerAddAll(List.of(
				createSchuelergruppeSchueler(142L, 602L, 501L),
				createSchuelergruppeSchueler(142L, 602L, 502L)));
		assertEquals(3, manager.planungsabschnittSchuelerGetMengeAsList().size());
		assertEquals(2, manager.planungsabschnittSchuelerGetMengeByKlasse(klasse).size());
		assertTrue(manager.planungsabschnittSchuelerGetMengeByKlasse(klasse).contains(schuelerA));
		assertTrue(manager.planungsabschnittSchuelerGetMengeByKlasse(klasse).contains(schuelerB));

		final UvPlanungsabschnittSchueler schuelerAPatch = createPlanungsabschnittSchueler(142L, 501L, 12L, 701L);
		manager.planungsabschnittSchuelerAllPatchAttributes(List.of(schuelerAPatch));
		assertEquals(12L, manager.planungsabschnittSchuelerGetByIdOrException(142L, 501L).idJahrgang);

		manager.planungsabschnittSchuelerRemove(schuelerB);
		manager.planungsabschnittSchuelerRemoveAll(List.of(schuelerC));
		manager.planungsabschnittSchuelerRemoveAllById(142L, List.of(501L));
		assertTrue(manager.planungsabschnittSchuelerGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("planungsabschnittSchuelerRemoveAllByPlanungsabschnitt entfernt alle Schüler eines Abschnitts")
	void testPlanungsabschnittSchuelerRemoveAllByPlanungsabschnitt() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt paA = createPlanungsabschnitt(143L);
		final UvPlanungsabschnitt paB = createPlanungsabschnitt(144L);
		final UvPlanungsabschnittSchueler schuelerA = createPlanungsabschnittSchueler(143L, 511L, 11L, null);
		final UvPlanungsabschnittSchueler schuelerB = createPlanungsabschnittSchueler(143L, 512L, 11L, null);
		final UvPlanungsabschnittSchueler schuelerC = createPlanungsabschnittSchueler(144L, 513L, 11L, null);
		manager.planungsabschnittAdd(paA);
		manager.planungsabschnittAdd(paB);
		manager.planungsabschnittSchuelerAddAll(List.of(schuelerA, schuelerB, schuelerC));

		manager.planungsabschnittSchuelerRemoveAllByPlanungsabschnitt(143L);

		assertEquals(List.of(schuelerC), manager.planungsabschnittSchuelerGetMengeAsList());
	}

	@Test
	@DisplayName("schuelergruppeAddAll, GetByKlasse und Remove-Varianten arbeiten konsistent")
	void testSchuelergruppeBulkAndRemove() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(145L);
		final UvSchuelergruppe gruppeA = createSchuelergruppe(611L, 145L, 11L);
		final UvSchuelergruppe gruppeB = createSchuelergruppe(612L, 145L, 11L);
		final UvSchuelergruppe gruppeC = createSchuelergruppe(613L, 145L, 11L);
		final UvKlasse klasse = createKlasse(711L, 145L, 611L);
		manager.planungsabschnittAdd(planungsabschnitt);

		manager.schuelergruppeAddAll(List.of(gruppeA, gruppeB, gruppeC));
		manager.klasseAdd(klasse);
		assertEquals(3, manager.schuelergruppeGetMengeAsList().size());
		assertSame(gruppeA, manager.schuelergruppeGetByKlasse(klasse));

		manager.schuelergruppeRemove(gruppeB);
		manager.schuelergruppeRemoveAll(List.of(gruppeC));
		manager.schuelergruppeRemoveById(145L, 611L);
		assertTrue(manager.schuelergruppeGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("schuelergruppeSchuelerAddAll, GetMenge und Remove-Varianten arbeiten konsistent")
	void testSchuelergruppeSchuelerBulkAndRemove() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(146L);
		final UvSchuelergruppe gruppeA = createSchuelergruppe(621L, 146L, 11L);
		final UvSchuelergruppe gruppeB = createSchuelergruppe(622L, 146L, 11L);
		final UvPlanungsabschnittSchueler schuelerA = createPlanungsabschnittSchueler(146L, 521L, 11L, null);
		final UvPlanungsabschnittSchueler schuelerB = createPlanungsabschnittSchueler(146L, 522L, 11L, null);
		final UvSchuelergruppeSchueler zuordnungA = createSchuelergruppeSchueler(146L, 621L, 521L);
		final UvSchuelergruppeSchueler zuordnungB = createSchuelergruppeSchueler(146L, 621L, 522L);
		final UvSchuelergruppeSchueler zuordnungC = createSchuelergruppeSchueler(146L, 622L, 521L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAddAll(List.of(gruppeA, gruppeB));
		manager.planungsabschnittSchuelerAddAll(List.of(schuelerA, schuelerB));

		manager.schuelergruppeSchuelerAddAll(List.of(zuordnungA, zuordnungB, zuordnungC));
		assertEquals(3, manager.schuelergruppeSchuelerGetMengeAsList().size());
		assertEquals(3, manager.schuelergruppeSchuelerGetMengeByPlanungsabschnitt(146L).size());
		assertTrue(manager.schuelergruppeSchuelerGetMengeByPlanungsabschnitt(146L).contains(zuordnungA));
		assertTrue(manager.schuelergruppeSchuelerGetMengeByPlanungsabschnitt(146L).contains(zuordnungB));
		assertTrue(manager.schuelergruppeSchuelerGetMengeByPlanungsabschnitt(146L).contains(zuordnungC));

		manager.schuelergruppeSchuelerRemove(zuordnungB);
		manager.schuelergruppeSchuelerRemoveAll(List.of(zuordnungC));
		manager.schuelergruppeSchuelerRemoveById(146L, 621L, 521L);
		assertTrue(manager.schuelergruppeSchuelerGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("klasseAdd und lerngruppeAdd für Klassenunterricht liefern die Klasse und Lerngruppe im Planungsabschnitt")
	void testKlasseAndKlassenLerngruppeAddAndGet() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(151L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(601L, 151L, 11L);
		final UvFach fach = createUvFach(71L, 21L);
		final UvKlasse klasse = createKlasse(701L, 151L, 601L);
		final UvLerngruppe lerngruppe = createKlassenLerngruppe(801L, 151L, 701L, 71L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);
		manager.fachAdd(fach);
		manager.klasseAdd(klasse);

		manager.lerngruppeAdd(lerngruppe);

		assertSame(klasse, manager.klasseGetByIdOrException(701L));
		assertEquals(List.of(klasse), manager.klasseGetMengeByPlanungsabschnitt(planungsabschnitt));
		assertSame(lerngruppe, manager.lerngruppeGetByIdOrException(801L));
		assertEquals(List.of(lerngruppe), manager.lerngruppeGetMengeByPlanungsabschnitt(planungsabschnitt));
		assertSame(klasse, manager.klasseGetByLerngruppe(lerngruppe));
		assertSame(gruppe, manager.schuelergruppeGetByLerngruppe(lerngruppe));
	}

	@Test
	@DisplayName("lerngruppeAdd verweigert Klassenunterricht ohne Klasse oder Fach")
	void testLerngruppeAddInvalidKlassenunterrichtThrows() {
		final UvManager manager = createManagerWithMinimalBasis();
		manager.planungsabschnittAdd(createPlanungsabschnitt(151L));
		final UvLerngruppe lerngruppe = new UvLerngruppe();
		lerngruppe.id = 801L;
		lerngruppe.idPlanungsabschnitt = 151L;

		assertThrows(DeveloperNotificationException.class, () -> manager.lerngruppeAdd(lerngruppe));
	}

	@Test
	@DisplayName("kursAdd und kursgebundene Lerngruppe liefern Kurs und Schülergruppe korrekt")
	void testKursAndKursLerngruppeAddAndGet() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(161L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(602L, 161L, 11L);
		final UvKurs kurs = createKurs(901L, 161L, 602L, 21L);
		final UvLerngruppe lerngruppe = createKursLerngruppe(902L, 161L, 901L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);
		manager.kursAdd(kurs);

		manager.lerngruppeAdd(lerngruppe);

		assertSame(kurs, manager.kursGetByIdOrException(901L));
		assertEquals(List.of(kurs), manager.kursGetMengeByPlanungsabschnitt(planungsabschnitt));
		assertSame(kurs, manager.kursGetByLerngruppe(lerngruppe));
		assertSame(gruppe, manager.schuelergruppeGetByLerngruppe(lerngruppe));
	}

	@Test
	@DisplayName("kursRemoveById entfernt die zugehörige kursgebundene Lerngruppe mit")
	void testKursRemoveCascadesToLerngruppe() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(161L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(602L, 161L, 11L);
		final UvKurs kurs = createKurs(901L, 161L, 602L, 21L);
		final UvLerngruppe lerngruppe = createKursLerngruppe(902L, 161L, 901L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);
		manager.kursAdd(kurs);
		manager.lerngruppeAdd(lerngruppe);
		manager.unterrichtAdd(createUnterricht(1206L, 161L, null, 902L));
		manager.raumAdd(createRaum(65L, "R205", null));
		manager.unterrichtRaumAdd(createUnterrichtRaum(161L, 1206L, 65L));
		manager.lehrerAdd(createLehrer(66L, "AAA"));
		manager.lerngruppenLehrerAdd(createLerngruppenLehrer(1107L, 161L, 902L, 66L));
		manager.unterrichtLerngruppenlehrerAdd(createUnterrichtLerngruppenlehrer(161L, 1206L, 1107L));

		manager.kursRemoveById(161L, 901L);

		assertThrows(DeveloperNotificationException.class, () -> manager.kursGetByIdOrException(901L));
		assertThrows(DeveloperNotificationException.class, () -> manager.lerngruppeGetByIdOrException(902L));
		assertTrue(manager.kursGetMengeByPlanungsabschnitt(planungsabschnitt).isEmpty());
		assertTrue(manager.lerngruppeGetMengeBySchuelergruppe(gruppe).isEmpty());
		assertTrue(manager.unterrichtGetMengeAsList().isEmpty());
		assertTrue(manager.unterrichtRaumGetMengeAsList().isEmpty());
		assertTrue(manager.unterrichtLerngruppenlehrerGetMengeAsList().isEmpty());
		assertTrue(manager.lerngruppenLehrerGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("schieneAdd und Getter liefern Schienen eines Planungsabschnitts korrekt")
	void testSchieneAddAndGet() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(171L);
		final UvSchiene schiene = createSchiene(1001L, 171L, 1);
		manager.planungsabschnittAdd(planungsabschnitt);

		manager.schieneAdd(schiene);

		assertSame(schiene, manager.schieneGetByIdOrException(1001L));
		assertEquals(List.of(schiene), manager.schieneGetMengeByPlanungsabschnitt(planungsabschnitt));
	}

	@Test
	@DisplayName("schieneAdd verweigert Schienen mit unbekanntem Planungsabschnitt")
	void testSchieneAddUnknownPlanungsabschnittThrows() {
		final UvManager manager = createManagerWithMinimalBasis();

		assertThrows(DeveloperNotificationException.class, () -> manager.schieneAdd(createSchiene(1001L, 999L, 1)));
	}

	@Test
	@DisplayName("lerngruppenLehrerAdd und Getter liefern Lehrerzuordnungen einer Lerngruppe")
	void testLerngruppenLehrerAddAndGet() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(181L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(603L, 181L, 11L);
		final UvFach fach = createUvFach(72L, 21L);
		final UvKlasse klasse = createKlasse(702L, 181L, 603L);
		final UvLerngruppe lerngruppe = createKlassenLerngruppe(802L, 181L, 702L, 72L);
		final UvLehrer lehrer = createLehrer(33L, "BBB");
		final UvLerngruppenLehrer zuordnung = createLerngruppenLehrer(1101L, 181L, 802L, 33L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);
		manager.fachAdd(fach);
		manager.klasseAdd(klasse);
		manager.lerngruppeAdd(lerngruppe);
		manager.lehrerAdd(lehrer);

		manager.lerngruppenLehrerAdd(zuordnung);

		assertSame(zuordnung, manager.lerngruppenLehrerGetByIdOrException(181L, 802L, 33L));
		assertEquals(List.of(zuordnung), manager.lerngruppenLehrerGetMengeByPlanungsabschnitt(planungsabschnitt));
		assertEquals(List.of(zuordnung), manager.lerngruppenLehrerGetMengeByLerngruppe(lerngruppe));
	}

	@Test
	@DisplayName("lerngruppenLehrerAdd verweigert doppelte Lehrerzuordnungen pro Lerngruppe")
	void testLerngruppenLehrerAddDuplicateThrows() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(181L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(603L, 181L, 11L);
		final UvFach fach = createUvFach(72L, 21L);
		final UvKlasse klasse = createKlasse(702L, 181L, 603L);
		final UvLerngruppe lerngruppe = createKlassenLerngruppe(802L, 181L, 702L, 72L);
		final UvLehrer lehrer = createLehrer(33L, "BBB");
		final UvLerngruppenLehrer zuordnung = createLerngruppenLehrer(1101L, 181L, 802L, 33L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);
		manager.fachAdd(fach);
		manager.klasseAdd(klasse);
		manager.lerngruppeAdd(lerngruppe);
		manager.lehrerAdd(lehrer);
		manager.lerngruppenLehrerAdd(zuordnung);

		assertThrows(DeveloperNotificationException.class, () -> manager.lerngruppenLehrerAdd(createLerngruppenLehrer(1102L, 181L, 802L, 33L)));
	}

	@Test
	@DisplayName("lerngruppenSchieneAdd und Getter liefern Schienenzuordnungen einer Lerngruppe")
	void testLerngruppenSchieneAddAndGet() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(191L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(604L, 191L, 11L);
		final UvFach fach = createUvFach(73L, 21L);
		final UvKlasse klasse = createKlasse(703L, 191L, 604L);
		final UvLerngruppe lerngruppe = createKlassenLerngruppe(803L, 191L, 703L, 73L);
		final UvSchiene schiene = createSchiene(1002L, 191L, 2);
		final UvLerngruppenSchiene zuordnung = createLerngruppenSchiene(191L, 803L, 1002L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);
		manager.fachAdd(fach);
		manager.klasseAdd(klasse);
		manager.lerngruppeAdd(lerngruppe);
		manager.schieneAdd(schiene);

		manager.lerngruppenSchieneAdd(zuordnung);

		assertSame(zuordnung, manager.lerngruppenSchieneGetByIdOrException(191L, 803L, 1002L));
		assertEquals(List.of(zuordnung), manager.lerngruppenSchieneGetMengeByLerngruppe(191L, 803L));
		assertEquals(List.of(zuordnung), manager.lerngruppenSchieneGetMengeByLerngruppe(lerngruppe));
		assertEquals(List.of(zuordnung), manager.lerngruppenSchieneGetMengeBySchiene(schiene));
	}

	@Test
	@DisplayName("lerngruppenSchieneAdd verweigert doppelte Schienenzuordnungen")
	void testLerngruppenSchieneAddDuplicateThrows() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(191L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(604L, 191L, 11L);
		final UvFach fach = createUvFach(73L, 21L);
		final UvKlasse klasse = createKlasse(703L, 191L, 604L);
		final UvLerngruppe lerngruppe = createKlassenLerngruppe(803L, 191L, 703L, 73L);
		final UvSchiene schiene = createSchiene(1002L, 191L, 2);
		final UvLerngruppenSchiene zuordnung = createLerngruppenSchiene(191L, 803L, 1002L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);
		manager.fachAdd(fach);
		manager.klasseAdd(klasse);
		manager.lerngruppeAdd(lerngruppe);
		manager.schieneAdd(schiene);
		manager.lerngruppenSchieneAdd(zuordnung);

		assertThrows(DeveloperNotificationException.class, () -> manager.lerngruppenSchieneAdd(createLerngruppenSchiene(191L, 803L, 1002L)));
	}

	@Test
	@DisplayName("lerngruppeRemoveById entfernt auch zugehörige Lehrer- und Schienenzuordnungen")
	void testLerngruppeRemoveCascadesToLerngruppenZuordnungen() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(192L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(605L, 192L, 11L);
		final UvFach fach = createUvFach(74L, 21L);
		final UvKlasse klasse = createKlasse(704L, 192L, 605L);
		final UvLerngruppe lerngruppe = createKlassenLerngruppe(804L, 192L, 704L, 74L);
		final UvLehrer lehrer = createLehrer(34L, "CCC");
		final UvLerngruppenLehrer lehrerZuordnung = createLerngruppenLehrer(1102L, 192L, 804L, 34L);
		final UvSchiene schiene = createSchiene(1003L, 192L, 3);
		final UvLerngruppenSchiene schienenZuordnung = createLerngruppenSchiene(192L, 804L, 1003L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);
		manager.fachAdd(fach);
		manager.klasseAdd(klasse);
		manager.lerngruppeAdd(lerngruppe);
		manager.lehrerAdd(lehrer);
		manager.lerngruppenLehrerAdd(lehrerZuordnung);
		manager.schieneAdd(schiene);
		manager.lerngruppenSchieneAdd(schienenZuordnung);

		manager.lerngruppeRemoveById(192L, 804L);

		assertThrows(DeveloperNotificationException.class, () -> manager.lerngruppeGetByIdOrException(804L));
		assertTrue(manager.lerngruppenLehrerGetMengeByPlanungsabschnitt(planungsabschnitt).isEmpty());
		assertTrue(manager.lerngruppenSchieneGetMengeBySchiene(schiene).isEmpty());
	}

	@Test
	@DisplayName("lerngruppeRemoveById leert alle abhängigen Lerngruppen-Getter konsistent")
	void testLerngruppeRemoveByIdClearsAllDependentGetterViews() {
		final KlassenLerngruppeScenario s = createKlassenLerngruppeScenario(340L, 741L, 101L, 1041L, 841L);
		final UvLehrer lehrer = createLehrer(102L, "ABC");
		final UvSchiene schiene = createSchiene(103L, 340L, 3);
		final UvLerngruppenLehrer lgLehrer = createLerngruppenLehrer(104L, 340L, 841L, 102L);
		final UvLerngruppenSchiene lgSchiene = createLerngruppenSchiene(340L, 841L, 103L);
		s.manager.lehrerAdd(lehrer);
		s.manager.schieneAdd(schiene);
		s.manager.lerngruppenLehrerAdd(lgLehrer);
		s.manager.lerngruppenSchieneAdd(lgSchiene);

		s.manager.lerngruppeRemoveById(340L, 841L);

		assertTrue(s.manager.lerngruppeGetMengeAsList().isEmpty());
		assertTrue(s.manager.lerngruppeGetMengeBySchuelergruppe(s.gruppe).isEmpty());
		assertTrue(s.manager.lerngruppenLehrerGetMengeAsList().isEmpty());
		assertTrue(s.manager.lerngruppenSchieneGetMengeAsList().isEmpty());
		assertTrue(s.manager.lerngruppenSchieneGetMengeByPlanungsabschnitt(340L).isEmpty());
	}

	@Test
	@DisplayName("unterrichtAdd und Getter liefern Unterrichte je Planungsabschnitt und Lerngruppe")
	void testUnterrichtAddAndGet() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(201L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(606L, 201L, 11L);
		final UvFach fach = createUvFach(75L, 21L);
		final UvKlasse klasse = createKlasse(705L, 201L, 606L);
		final UvLerngruppe lerngruppe = createKlassenLerngruppe(805L, 201L, 705L, 75L);
		final UvUnterricht unterricht = createUnterricht(1201L, 201L, null, 805L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);
		manager.fachAdd(fach);
		manager.klasseAdd(klasse);
		manager.lerngruppeAdd(lerngruppe);

		manager.unterrichtAdd(unterricht);

		assertSame(unterricht, manager.unterrichtGetByIdOrException(1201L));
		assertEquals(List.of(unterricht), manager.unterrichtGetMengeByPlanungsabschnitt(planungsabschnitt));
		assertEquals(List.of(unterricht), manager.unterrichtGetMengeByLerngruppe(201L, 805L));
	}

	@Test
	@DisplayName("unterrichtAdd verweigert Unterricht mit unbekanntem Planungsabschnitt")
	void testUnterrichtAddUnknownPlanungsabschnittThrows() {
		final UvManager manager = createManagerWithMinimalBasis();

		assertThrows(DeveloperNotificationException.class, () -> manager.unterrichtAdd(createUnterricht(1201L, 999L, null, 805L)));
	}

	@Test
	@DisplayName("unterrichtRaumAdd und Getter liefern Raumzuordnungen eines Unterrichts")
	void testUnterrichtRaumAddAndGet() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(211L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(607L, 211L, 11L);
		final UvFach fach = createUvFach(76L, 21L);
		final UvKlasse klasse = createKlasse(706L, 211L, 607L);
		final UvLerngruppe lerngruppe = createKlassenLerngruppe(806L, 211L, 706L, 76L);
		final UvUnterricht unterricht = createUnterricht(1202L, 211L, null, 806L);
		final UvRaum raum = createRaum(63L, "R203", null);
		final UvUnterrichtRaum zuordnung = createUnterrichtRaum(211L, 1202L, 63L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);
		manager.fachAdd(fach);
		manager.klasseAdd(klasse);
		manager.lerngruppeAdd(lerngruppe);
		manager.unterrichtAdd(unterricht);
		manager.raumAdd(raum);

		manager.unterrichtRaumAdd(zuordnung);

		assertSame(zuordnung, manager.unterrichtRaumGetByIdOrException(211L, 1202L, 63L));
		assertEquals(List.of(zuordnung), manager.unterrichtRaumGetMengeByPlanungsabschnitt(planungsabschnitt));
		assertEquals(List.of(zuordnung), manager.unterrichtRaumGetMengeByUnterricht(unterricht));
	}

	@Test
	@DisplayName("unterrichtRaumAdd verweigert doppelte Raumzuordnungen")
	void testUnterrichtRaumAddDuplicateThrows() {
		final UvManager manager = createManagerWithMinimalBasis();
		manager.planungsabschnittAdd(createPlanungsabschnitt(211L));
		manager.unterrichtAdd(createUnterricht(1202L, 211L, null, 806L));
		manager.raumAdd(createRaum(63L, "R203", null));
		manager.unterrichtRaumAdd(createUnterrichtRaum(211L, 1202L, 63L));

		assertThrows(DeveloperNotificationException.class, () -> manager.unterrichtRaumAdd(createUnterrichtRaum(211L, 1202L, 63L)));
	}

	@Test
	@DisplayName("unterrichtLerngruppenlehrerAdd und Getter liefern Lehrerzuordnungen eines Unterrichts")
	void testUnterrichtLerngruppenlehrerAddAndGet() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(221L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(608L, 221L, 11L);
		final UvFach fach = createUvFach(77L, 21L);
		final UvKlasse klasse = createKlasse(707L, 221L, 608L);
		final UvLerngruppe lerngruppe = createKlassenLerngruppe(807L, 221L, 707L, 77L);
		final UvLehrer lehrer = createLehrer(35L, "DDD");
		final UvLerngruppenLehrer lerngruppenLehrer = createLerngruppenLehrer(1103L, 221L, 807L, 35L);
		final UvUnterricht unterricht = createUnterricht(1203L, 221L, null, 807L);
		final UvUnterrichtLerngruppenlehrer zuordnung = createUnterrichtLerngruppenlehrer(221L, 1203L, 1103L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);
		manager.fachAdd(fach);
		manager.klasseAdd(klasse);
		manager.lerngruppeAdd(lerngruppe);
		manager.lehrerAdd(lehrer);
		manager.lerngruppenLehrerAdd(lerngruppenLehrer);
		manager.unterrichtAdd(unterricht);

		manager.unterrichtLerngruppenlehrerAdd(zuordnung);

		assertSame(zuordnung, manager.unterrichtLerngruppenlehrerGetByIdOrException(221L, 1203L, 1103L));
		assertEquals(List.of(zuordnung), manager.unterrichtLerngruppenlehrerGetMengeByPlanungsabschnitt(planungsabschnitt));
		assertEquals(List.of(zuordnung), manager.unterrichtLerngruppenlehrerGetMengeByUnterricht(unterricht));
	}

	@Test
	@DisplayName("unterrichtLerngruppenlehrerAdd verweigert doppelte Lehrerzuordnungen")
	void testUnterrichtLerngruppenlehrerAddDuplicateThrows() {
		final UvManager manager = createManagerWithMinimalBasis();
		manager.planungsabschnittAdd(createPlanungsabschnitt(221L));
		manager.unterrichtAdd(createUnterricht(1203L, 221L, null, 807L));
		manager.unterrichtLerngruppenlehrerAdd(createUnterrichtLerngruppenlehrer(221L, 1203L, 1103L));

		assertThrows(DeveloperNotificationException.class,
				() -> manager.unterrichtLerngruppenlehrerAdd(createUnterrichtLerngruppenlehrer(221L, 1203L, 1103L)));
	}

	@Test
	@DisplayName("klasseGetMengeByLerngruppe liefert bei Kursunterricht alle Klassen der Schülergruppe")
	void testKlasseGetMengeByLerngruppeForKursunterricht() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(231L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(609L, 231L, 11L, 12L);
		final UvKlasse klasseA = createKlasse(708L, 231L, 609L);
		klasseA.kuerzel = "EF1";
		final UvKlasse klasseB = createKlasse(709L, 231L, 609L);
		klasseB.kuerzel = "Q11";
		final UvKurs kurs = createKurs(903L, 231L, 609L, 21L);
		final UvLerngruppe lerngruppe = createKursLerngruppe(808L, 231L, 903L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);
		manager.klasseAddAll(List.of(klasseA, klasseB));
		manager.kursAdd(kurs);
		manager.lerngruppeAdd(lerngruppe);

		assertEquals(List.of(klasseA, klasseB), manager.klasseGetMengeByLerngruppe(lerngruppe));
	}

	@Test
	@DisplayName("jahrgangsdatenGetMengeByLerngruppe liefert die real vertretenen Jahrgänge der Schülergruppe")
	void testJahrgangsdatenGetMengeByLerngruppeUsesRepresentedJahrgaenge() {
		final JahrgangsDaten ef = createJahrgang(11L, "EF");
		final JahrgangsDaten q1 = createJahrgang(12L, "Q1");
		final UvManager manager = new UvManager(List.of(ef, q1), List.of(createFachdaten(21L, "M", "Mathematik")));
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(232L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(610L, 232L, 11L, 12L);
		final UvKlasse klasseA = createKlasse(710L, 232L, 610L);
		final UvKlasse klasseB = createKlasse(711L, 232L, 610L);
		final UvKurs kurs = createKurs(904L, 232L, 610L, 21L);
		final UvLerngruppe lerngruppe = createKursLerngruppe(809L, 232L, 904L);
		final UvPlanungsabschnittSchueler schuelerA = createPlanungsabschnittSchueler(232L, 511L, 11L, 710L);
		final UvPlanungsabschnittSchueler schuelerB = createPlanungsabschnittSchueler(232L, 512L, 12L, 711L);
		final UvPlanungsabschnittSchueler schuelerC = createPlanungsabschnittSchueler(232L, 513L, 11L, 710L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);
		manager.klasseAddAll(List.of(klasseA, klasseB));
		manager.kursAdd(kurs);
		manager.lerngruppeAdd(lerngruppe);
		manager.planungsabschnittSchuelerAddAll(List.of(schuelerA, schuelerB, schuelerC));
		manager.schuelergruppeSchuelerAddAll(List.of(
				createSchuelergruppeSchueler(232L, 610L, 511L),
				createSchuelergruppeSchueler(232L, 610L, 512L),
				createSchuelergruppeSchueler(232L, 610L, 513L)));

		assertEquals(List.of(ef, q1), manager.jahrgangsdatenGetMengeByLerngruppe(lerngruppe));
	}

	@Test
	@DisplayName("fachGetByLerngruppe liefert das Fach sowohl für Klassen- als auch Kursunterricht")
	void testFachGetByLerngruppe() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(233L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(611L, 233L, 11L);
		final UvFach fach = createUvFach(78L, 21L);
		final UvKlasse klasse = createKlasse(712L, 233L, 611L);
		final UvLerngruppe klassenLerngruppe = createKlassenLerngruppe(810L, 233L, 712L, 78L);
		final UvKurs kurs = createKurs(905L, 233L, 611L, 78L);
		final UvLerngruppe kursLerngruppe = createKursLerngruppe(811L, 233L, 905L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);
		manager.fachAdd(fach);
		manager.klasseAdd(klasse);
		manager.kursAdd(kurs);
		manager.lerngruppeAddAll(List.of(klassenLerngruppe, kursLerngruppe));

		assertSame(fach, manager.fachGetByLerngruppe(klassenLerngruppe));
		assertSame(fach, manager.fachGetByLerngruppe(kursLerngruppe));
	}

	@Test
	@DisplayName("Lehrbefähigungen verwenden die Schulfach-ID und nicht die UV-Fach-ID")
	void testLehrerHatLehrbefaehigung() {
		final UvManager manager = new UvManager(List.of(createJahrgang(11L, "EF")),
				List.of(createFachdaten(21L, "M", "Mathematik"), createFachdaten(79L, "D", "Deutsch")));
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(234L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(612L, 234L, 11L);
		final UvFach fach = createUvFach(79L, 21L);
		final UvKlasse klasse = createKlasse(713L, 234L, 612L);
		final UvLerngruppe lerngruppe = createKlassenLerngruppe(812L, 234L, 713L, 79L);
		final UvLehrer lehrerMit = createLehrer(36L, "EEE");
		final UvLehrer lehrerOhne = createLehrer(37L, "FFF");
		final UvLehrer lehrerAnderesFach = createLehrer(38L, "GGG");
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);
		manager.fachAdd(fach);
		manager.klasseAdd(klasse);
		manager.lerngruppeAdd(lerngruppe);
		manager.lehrerAddAll(List.of(lehrerMit, lehrerOhne, lehrerAnderesFach));
		final LehrerUnterrichtsfach mathematik = createLehrerUnterrichtsfach(42L, 36L, 21L, false);
		manager.lehrerUnterrichtsfachAdd(mathematik);
		// Die Schulfach-ID von Deutsch entspricht absichtlich der UV-Fach-ID von Mathematik.
		manager.lehrerUnterrichtsfachAdd(createLehrerUnterrichtsfach(43L, 38L, 79L, false));

		assertTrue(manager.lehrerHatLehrbefaehigungFach(lehrerMit, fach));
		assertTrue(manager.lehrerHatLehrbefaehigungLerngruppe(lehrerMit, lerngruppe));
		assertFalse(manager.lehrerHatLehrbefaehigungFach(lehrerOhne, fach));
		assertFalse(manager.lehrerHatLehrbefaehigungLerngruppe(lehrerOhne, lerngruppe));
		assertFalse(manager.lehrerHatLehrbefaehigungFach(lehrerAnderesFach, fach));
		assertFalse(manager.lehrerHatLehrbefaehigungLerngruppe(lehrerAnderesFach, lerngruppe));
		assertEquals(List.of(mathematik), manager.lehrerUnterrichtsfachGetMengeByFach(fach));
	}

	@Test
	@DisplayName("Verwendet-Helper erkennen referenzierte Klassen, Schienen und Schülergruppen")
	void testVerwendetHelper() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(235L);
		final UvSchuelergruppe verwendeteGruppe = createSchuelergruppe(613L, 235L, 11L);
		final UvSchuelergruppe freieGruppe = createSchuelergruppe(614L, 235L, 11L);
		final UvSchuelergruppe klasseOhneVerwendungGruppe = createSchuelergruppe(615L, 235L, 11L);
		final UvKlasse verwendeteKlasse = createKlasse(714L, 235L, 613L);
		final UvKlasse freieKlasse = createKlasse(715L, 235L, 615L);
		final UvFach fach = createUvFach(80L, 21L);
		final UvLerngruppe lerngruppe = createKlassenLerngruppe(813L, 235L, 714L, 80L);
		final UvSchiene verwendeteSchiene = createSchiene(1004L, 235L, 4);
		final UvSchiene freieSchiene = createSchiene(1005L, 235L, 5);
		final UvPlanungsabschnittSchueler schueler = createPlanungsabschnittSchueler(235L, 514L, 11L, 714L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAddAll(List.of(verwendeteGruppe, freieGruppe, klasseOhneVerwendungGruppe));
		manager.klasseAddAll(List.of(verwendeteKlasse, freieKlasse));
		manager.fachAdd(fach);
		manager.lerngruppeAdd(lerngruppe);
		manager.schieneAddAll(List.of(verwendeteSchiene, freieSchiene));
		manager.lerngruppenSchieneAdd(createLerngruppenSchiene(235L, 813L, 1004L));
		manager.planungsabschnittSchuelerAdd(schueler);

		assertEquals(java.util.Set.of(verwendeteKlasse), manager.klasseGetMengeVerwendetByKlasseMenge(List.of(verwendeteKlasse, freieKlasse)));
		assertEquals(java.util.Set.of(verwendeteSchiene), manager.schieneGetMengeVerwendetBySchieneMenge(List.of(verwendeteSchiene, freieSchiene)));
		assertEquals(java.util.Set.of(verwendeteGruppe),
				manager.schuelergruppeGetMengeVerwendetBySchuelergruppeMenge(List.of(verwendeteGruppe, freieGruppe)));
	}

	@Test
	@DisplayName("klassenLehrerAdd und Getter liefern Klassenlehrer-Zuordnungen korrekt")
	void testKlassenLehrerAddAndGet() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(241L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(616L, 241L, 11L);
		final UvKlasse klasse = createKlasse(716L, 241L, 616L);
		final UvLehrer lehrer = createLehrer(38L, "GGG");
		final UvKlassenLehrer zuordnung = createKlassenLehrer(1301L, 241L, 716L, 38L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);
		manager.klasseAdd(klasse);
		manager.lehrerAdd(lehrer);

		manager.klassenLehrerAdd(zuordnung);

		assertSame(zuordnung, manager.klassenLehrerGetByIdOrException(241L, 716L, 38L));
		assertEquals(List.of(zuordnung), manager.klassenLehrerGetMengeByPlanungsabschnitt(planungsabschnitt));
		assertEquals(List.of(zuordnung), manager.klassenLehrerGetMengeByKlasse(klasse));
		assertSame(lehrer, manager.lehrerGetByKlassenLehrer(zuordnung));
	}

	@Test
	@DisplayName("klassenLehrerAdd verweigert doppelte Lehrerzuordnungen pro Klasse")
	void testKlassenLehrerAddDuplicateThrows() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(242L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(617L, 242L, 11L);
		final UvKlasse klasse = createKlasse(717L, 242L, 617L);
		final UvLehrer lehrer = createLehrer(39L, "HHH");
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);
		manager.klasseAdd(klasse);
		manager.lehrerAdd(lehrer);
		manager.klassenLehrerAdd(createKlassenLehrer(1302L, 242L, 717L, 39L));

		assertThrows(DeveloperNotificationException.class, () -> manager.klassenLehrerAdd(createKlassenLehrer(1303L, 242L, 717L, 39L)));
	}

	@Test
	@DisplayName("klasseRemoveById entfernt auch zugehörige Klassenlehrer-Zuordnungen")
	void testKlasseRemoveCascadesToKlassenLehrer() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(243L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(618L, 243L, 11L);
		final UvKlasse klasse = createKlasse(718L, 243L, 618L);
		final UvLehrer lehrer = createLehrer(40L, "III");
		final UvKlassenLehrer zuordnung = createKlassenLehrer(1304L, 243L, 718L, 40L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);
		manager.klasseAdd(klasse);
		manager.lehrerAdd(lehrer);
		manager.klassenLehrerAdd(zuordnung);

		manager.klasseRemoveById(243L, 718L);

		assertThrows(DeveloperNotificationException.class, () -> manager.klasseGetByIdOrException(718L));
		assertTrue(manager.klassenLehrerGetMengeByPlanungsabschnitt(planungsabschnitt).isEmpty());
		assertTrue(manager.klasseGetMengeByPlanungsabschnitt(planungsabschnitt).isEmpty());
	}

	@Test
	@DisplayName("klassenLehrerAllPatchAttributes ersetzt die gespeicherten Attribute")
	void testKlassenLehrerAllPatchAttributes() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(244L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(619L, 244L, 11L);
		final UvKlasse klasse = createKlasse(719L, 244L, 619L);
		final UvLehrer lehrer = createLehrer(41L, "JJJ");
		final UvKlassenLehrer original = createKlassenLehrer(1305L, 244L, 719L, 41L);
		final UvKlassenLehrer gepatcht = createKlassenLehrer(1305L, 244L, 719L, 41L);
		gepatcht.reihenfolge = 2;
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);
		manager.klasseAdd(klasse);
		manager.lehrerAdd(lehrer);
		manager.klassenLehrerAdd(original);

		manager.klassenLehrerAllPatchAttributes(List.of(gepatcht));

		assertEquals(2, manager.klassenLehrerGetByIdOrException(244L, 719L, 41L).reihenfolge);
	}

	@Test
	@DisplayName("planungsabschnittLehrerAdd und Getter liefern Lehrer je Planungsabschnitt")
	void testPlanungsabschnittLehrerAddAndGet() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(251L);
		final UvLehrer lehrer = createLehrer(42L, "KKK");
		final UvPlanungsabschnittLehrer zuordnung = createPlanungsabschnittLehrer(251L, 42L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.lehrerAdd(lehrer);

		manager.planungsabschnittLehrerAdd(zuordnung);

		assertEquals(List.of(zuordnung), manager.planungsabschnittLehrerGetMengeByPlanungsabschnitt(planungsabschnitt));
		assertTrue(manager.lehrerIsInPlanungsabschnitt(lehrer, planungsabschnitt));
	}

	@Test
	@DisplayName("planungsabschnittLehrerAdd verweigert Zuordnungen mit unbekanntem Lehrer")
	void testPlanungsabschnittLehrerAddUnknownLehrerThrows() {
		final UvManager manager = createManagerWithMinimalBasis();
		manager.planungsabschnittAdd(createPlanungsabschnitt(252L));

		assertThrows(DeveloperNotificationException.class, () -> manager.planungsabschnittLehrerAdd(createPlanungsabschnittLehrer(252L, 999L)));
	}

	@Test
	@DisplayName("planungsabschnittLehrerRemoveAllById entfernt mehrere Lehrer aus einem Planungsabschnitt")
	void testPlanungsabschnittLehrerRemoveAllById() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(253L);
		final UvLehrer lehrerA = createLehrer(43L, "LLL");
		final UvLehrer lehrerB = createLehrer(44L, "MMM");
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.lehrerAddAll(List.of(lehrerA, lehrerB));
		manager.planungsabschnittLehrerAddAll(List.of(
				createPlanungsabschnittLehrer(253L, 43L),
				createPlanungsabschnittLehrer(253L, 44L)));

		manager.planungsabschnittLehrerRemoveAllById(253L, List.of(43L, 44L));

		assertTrue(manager.planungsabschnittLehrerGetMengeByPlanungsabschnitt(planungsabschnitt).isEmpty());
		assertFalse(manager.lehrerIsInPlanungsabschnitt(lehrerA, planungsabschnitt));
		assertFalse(manager.lehrerIsInPlanungsabschnitt(lehrerB, planungsabschnitt));
	}

	@Test
	@DisplayName("planungsabschnittLehrerAddAll und Remove-Varianten arbeiten konsistent")
	void testPlanungsabschnittLehrerBulkAndRemove() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(254L);
		final UvLehrer lehrerA = createLehrer(45L, "NNN");
		final UvLehrer lehrerB = createLehrer(46L, "OOO");
		final UvLehrer lehrerC = createLehrer(47L, "PPP");
		final UvPlanungsabschnittLehrer zuordnungA = createPlanungsabschnittLehrer(254L, 45L);
		final UvPlanungsabschnittLehrer zuordnungB = createPlanungsabschnittLehrer(254L, 46L);
		final UvPlanungsabschnittLehrer zuordnungC = createPlanungsabschnittLehrer(254L, 47L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.lehrerAddAll(List.of(lehrerA, lehrerB, lehrerC));

		manager.planungsabschnittLehrerAddAll(List.of(zuordnungA, zuordnungB, zuordnungC));
		assertEquals(List.of(zuordnungA, zuordnungB, zuordnungC), manager.planungsabschnittLehrerGetMengeByPlanungsabschnitt(planungsabschnitt));

		manager.planungsabschnittLehrerRemoveById(254L, 45L);
		manager.planungsabschnittLehrerRemove(zuordnungB);
		manager.planungsabschnittLehrerRemoveAll(List.of(zuordnungC));
		assertTrue(manager.planungsabschnittLehrerGetMengeByPlanungsabschnitt(planungsabschnitt).isEmpty());
	}

	@Test
	@DisplayName("lehrerPflichtstundensollAdd, Stichtags-Getter und Remove verhalten sich konsistent")
	void testLehrerPflichtstundensollAddAndStichtagGetter() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvLehrer lehrer = createLehrer(45L, "NNN");
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(261L);
		final UvPlanungsabschnittLehrer planungsabschnittLehrer = createPlanungsabschnittLehrer(261L, 45L);
		final UvLehrerPflichtstundensoll alt = createLehrerPflichtstundensoll(1401L, 45L, 24.5, "2024-08-01", "2025-01-31");
		final UvLehrerPflichtstundensoll aktiv = createLehrerPflichtstundensoll(1402L, 45L, 25.5, "2025-02-01", null);
		manager.lehrerAdd(lehrer);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.planungsabschnittLehrerAdd(planungsabschnittLehrer);

		manager.lehrerPflichtstundensollAddAll(List.of(alt, aktiv));

		assertSame(aktiv, manager.lehrerPflichtstundensollGetByLehrerAndPlanungsabschnitt(lehrer, planungsabschnitt));
		assertSame(aktiv, manager.lehrerPflichtstundensollGetByPlanungsabschnittLehrer(planungsabschnittLehrer));
		assertEquals(25.5, manager.lehrerPflichtstundensollGetDoubleByLehrerAndPlanungsabschnitt(lehrer, planungsabschnitt));
		assertEquals(25.5, manager.lehrerPflichtstundensollGetDoubleByPlanungsabschnittLehrer(planungsabschnittLehrer));
		assertEquals(List.of(aktiv, alt), manager.lehrerPflichtstundensollGetMengeByLehrer(lehrer));

		manager.lehrerPflichtstundensollRemoveById(1401L);

		assertThrows(DeveloperNotificationException.class, () -> manager.lehrerPflichtstundensollGetByIdOrException(1401L));
		assertEquals(List.of(aktiv), manager.lehrerPflichtstundensollGetMengeByLehrer(lehrer));
	}

	@Test
	@DisplayName("lehrerPflichtstundensoll-Stichtagsgetter behandeln Randintervalle und fehlende Treffer korrekt")
	void testLehrerPflichtstundensollStichtagBoundaries() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvLehrer lehrer = createLehrer(145L, "NNO");
		final UvPlanungsabschnitt randBeginn = createPlanungsabschnitt(262L);
		randBeginn.gueltigVon = "2025-02-01";
		final UvPlanungsabschnitt ohneTreffer = createPlanungsabschnitt(263L);
		ohneTreffer.gueltigVon = "2026-01-01";
		final UvLehrerPflichtstundensoll passendAmRand = createLehrerPflichtstundensoll(1411L, 145L, 26.0, "2024-08-01", "2025-02-01");
		final UvLehrerPflichtstundensoll spaeter = createLehrerPflichtstundensoll(1412L, 145L, 27.0, "2026-02-01", null);
		manager.lehrerAdd(lehrer);
		manager.planungsabschnittAdd(randBeginn);
		manager.planungsabschnittAdd(ohneTreffer);
		manager.lehrerPflichtstundensollAddAll(List.of(passendAmRand, spaeter));

		assertSame(passendAmRand, manager.lehrerPflichtstundensollGetByLehrerAndPlanungsabschnitt(lehrer, randBeginn));
		assertEquals(26.0, manager.lehrerPflichtstundensollGetDoubleByLehrerAndPlanungsabschnitt(lehrer, randBeginn));
		assertNull(manager.lehrerPflichtstundensollGetByLehrerAndPlanungsabschnitt(lehrer, ohneTreffer));
		assertNull(manager.lehrerPflichtstundensollGetDoubleByLehrerAndPlanungsabschnitt(lehrer, ohneTreffer));
	}

	@Test
	@DisplayName("lehrerPflichtstundensollAllPatchAttributes ersetzt einen Soll-Eintrag")
	void testLehrerPflichtstundensollAllPatchAttributes() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvLehrer lehrer = createLehrer(46L, "OOO");
		final UvLehrerPflichtstundensoll original = createLehrerPflichtstundensoll(1403L, 46L, 23.0, "2025-01-01", null);
		final UvLehrerPflichtstundensoll gepatcht = createLehrerPflichtstundensoll(1403L, 46L, 27.0, "2025-01-01", null);
		manager.lehrerAdd(lehrer);
		manager.lehrerPflichtstundensollAdd(original);

		manager.lehrerPflichtstundensollAllPatchAttributes(List.of(gepatcht));

		assertEquals(27.0, manager.lehrerPflichtstundensollGetByIdOrException(1403L).pflichtstdSoll);
	}

	@Test
	@DisplayName("lehrerPflichtstundensollAdd verweigert ungültige Daten")
	void testLehrerPflichtstundensollAddInvalidThrows() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvLehrer lehrer = createLehrer(47L, "PPP");
		final UvLehrerPflichtstundensoll pflicht = createLehrerPflichtstundensoll(1404L, 47L, 25.0, "ungueltig", null);
		manager.lehrerAdd(lehrer);

		assertThrows(DeveloperNotificationException.class, () -> manager.lehrerPflichtstundensollAdd(pflicht));
	}

	@Test
	@DisplayName("lehrerAnrechnungsstundenAdd, Stichtags-Getter, Summen und Remove verhalten sich konsistent")
	void testLehrerAnrechnungsstundenAddAndStichtagGetter() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvLehrer lehrer = createLehrer(48L, "QQQ");
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(271L);
		final UvPlanungsabschnittLehrer planungsabschnittLehrer = createPlanungsabschnittLehrer(271L, 48L);
		final UvLehrerAnrechnungsstunden alt = createLehrerAnrechnungsstunden(1501L, 48L, "ALT", 1.0, "2024-08-01", "2025-01-31");
		final UvLehrerAnrechnungsstunden aktivA = createLehrerAnrechnungsstunden(1502L, 48L, "A", 1.5, "2025-02-01", null);
		final UvLehrerAnrechnungsstunden aktivB = createLehrerAnrechnungsstunden(1503L, 48L, "B", 2.0, "2024-08-01", "2025-07-31");
		manager.lehrerAdd(lehrer);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.planungsabschnittLehrerAdd(planungsabschnittLehrer);

		manager.lehrerAnrechnungsstundenAddAll(List.of(alt, aktivA, aktivB));

		assertEquals(List.of(aktivA, alt, aktivB), manager.lehrerAnrechnungsstundenGetMengeByLehrer(lehrer));
		assertEquals(List.of(aktivA, aktivB), manager.lehrerAnrechnungsstundenGetMengeByLehrerAndPlanungsabschnitt(lehrer, planungsabschnitt));
		assertEquals(List.of(aktivA, aktivB), manager.lehrerAnrechnungsstundenGetMengeByPlanungsabschnittLehrer(planungsabschnittLehrer));
		assertEquals(3.5, manager.lehrerAnrechnungsstundenGetDoubleByLehrerAndPlanungsabschnitt(lehrer, planungsabschnitt));
		assertEquals(3.5, manager.lehrerAnrechnungsstundenGetDoubleByPlanungsabschnittLehrer(planungsabschnittLehrer));

		manager.lehrerAnrechnungsstundenRemove(aktivA);

		assertThrows(DeveloperNotificationException.class, () -> manager.lehrerAnrechnungsstundenGetByIdOrException(1502L));
		assertEquals(List.of(aktivB), manager.lehrerAnrechnungsstundenGetMengeByLehrerAndPlanungsabschnitt(lehrer, planungsabschnitt));
	}

	@Test
	@DisplayName("lehrerAnrechnungsstunden-Stichtagsgetter behandeln Randintervalle und leere Treffer korrekt")
	void testLehrerAnrechnungsstundenStichtagBoundaries() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvLehrer lehrer = createLehrer(148L, "QQT");
		final UvPlanungsabschnitt randBeginn = createPlanungsabschnitt(272L);
		randBeginn.gueltigVon = "2025-02-01";
		final UvPlanungsabschnitt ohneTreffer = createPlanungsabschnitt(273L);
		ohneTreffer.gueltigVon = "2026-01-01";
		final UvLehrerAnrechnungsstunden passendAmRand =
				createLehrerAnrechnungsstunden(1511L, 148L, "A", 1.25, "2024-08-01", "2025-02-01");
		final UvLehrerAnrechnungsstunden spaeter =
				createLehrerAnrechnungsstunden(1512L, 148L, "B", 2.25, "2026-02-01", null);
		manager.lehrerAdd(lehrer);
		manager.planungsabschnittAdd(randBeginn);
		manager.planungsabschnittAdd(ohneTreffer);
		manager.lehrerAnrechnungsstundenAddAll(List.of(passendAmRand, spaeter));

		assertEquals(List.of(passendAmRand), manager.lehrerAnrechnungsstundenGetMengeByLehrerAndPlanungsabschnitt(lehrer, randBeginn));
		assertEquals(1.25, manager.lehrerAnrechnungsstundenGetDoubleByLehrerAndPlanungsabschnitt(lehrer, randBeginn));
		assertTrue(manager.lehrerAnrechnungsstundenGetMengeByLehrerAndPlanungsabschnitt(lehrer, ohneTreffer).isEmpty());
		assertEquals(0.0, manager.lehrerAnrechnungsstundenGetDoubleByLehrerAndPlanungsabschnitt(lehrer, ohneTreffer));
	}

	@Test
	@DisplayName("lehrerAnrechnungsstundenAllPatchAttributes ersetzt einen Anrechnungseintrag")
	void testLehrerAnrechnungsstundenAllPatchAttributes() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvLehrer lehrer = createLehrer(49L, "RRR");
		final UvLehrerAnrechnungsstunden original = createLehrerAnrechnungsstunden(1504L, 49L, "AG", 1.0, "2025-01-01", null);
		final UvLehrerAnrechnungsstunden gepatcht = createLehrerAnrechnungsstunden(1504L, 49L, "AG", 2.5, "2025-01-01", null);
		manager.lehrerAdd(lehrer);
		manager.lehrerAnrechnungsstundenAdd(original);

		manager.lehrerAnrechnungsstundenAllPatchAttributes(List.of(gepatcht));

		assertEquals(2.5, manager.lehrerAnrechnungsstundenGetByIdOrException(1504L).anzahlStunden);
	}

	@Test
	@DisplayName("lehrerAnrechnungsstundenAdd verweigert ungültige Daten")
	void testLehrerAnrechnungsstundenAddInvalidThrows() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvLehrer lehrer = createLehrer(50L, "SSS");
		final UvLehrerAnrechnungsstunden anrechnung = createLehrerAnrechnungsstunden(1505L, 50L, "AG", 1.0, "2025-01-01", "ungueltig");
		manager.lehrerAdd(lehrer);

		assertThrows(DeveloperNotificationException.class, () -> manager.lehrerAnrechnungsstundenAdd(anrechnung));
	}

	@Test
	@DisplayName("Schülergruppen- und Jahrgangsprüfungen erkennen passende und unpassende Zuordnungen")
	void testSchuelergruppenJahrgangsPruefungen() {
		final JahrgangsDaten ef = createJahrgang(11L, "EF");
		final JahrgangsDaten q1 = createJahrgang(12L, "Q1");
		final UvManager manager = new UvManager(List.of(ef, q1), List.of(createFachdaten(21L, "M", "Mathematik")));
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(281L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(620L, 281L, 11L);
		final UvPlanungsabschnittSchueler passend = createPlanungsabschnittSchueler(281L, 521L, 11L, null);
		final UvPlanungsabschnittSchueler unpassend = createPlanungsabschnittSchueler(281L, 522L, 12L, null);
		final UvSchuelergruppeSchueler passendZuordnung = createSchuelergruppeSchueler(281L, 620L, 521L);
		final UvSchuelergruppeSchueler unpassendZuordnung = createSchuelergruppeSchueler(281L, 620L, 522L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);
		manager.planungsabschnittSchuelerAddAll(List.of(passend, unpassend));
		manager.schuelergruppeSchuelerAddAll(List.of(passendZuordnung, unpassendZuordnung));

		assertTrue(manager.schuelergruppeSchuelerHatZurGruppePassendenJahrgang(passendZuordnung));
		assertFalse(manager.schuelergruppeSchuelerHatZurGruppePassendenJahrgang(unpassendZuordnung));
		assertTrue(manager.planungsabschnittSchuelerHatZurGruppePassendenJahrgang(passend, gruppe));
		assertFalse(manager.planungsabschnittSchuelerHatZurGruppePassendenJahrgang(unpassend, gruppe));
		assertTrue(manager.schuelergruppeHatSchuelerMitFalschemJahrgang(gruppe));
		assertSame(passend, manager.planungsabschnittSchuelerGetBySchuelergruppeSchueler(passendZuordnung));
		assertEquals(List.of(passend, unpassend), manager.planungsabschnittSchuelerGetMengeBySchuelergruppe(gruppe));
	}

	@Test
	@DisplayName("Planungsabschnitt-Zeitraster-Helfer liefern Jahrgänge und Gültigkeiten korrekt")
	void testPlanungsabschnittZeitrasterHelper() {
		final JahrgangsDaten ef = createJahrgang(11L, "EF");
		final JahrgangsDaten q1 = createJahrgang(12L, "Q1");
		final UvManager manager = new UvManager(List.of(ef, q1), List.of(createFachdaten(21L, "M", "Mathematik")));
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(282L);
		final UvZeitraster zeitrasterA = createZeitraster(171L, "Raster A");
		final UvZeitraster zeitrasterB = createZeitraster(172L, "Raster B");
		zeitrasterB.gueltigVon = "2026-01-01";
		final UvPlanungsabschnittZeitraster zuordnung = createPlanungsabschnittZeitraster(282L, 171L);
		zuordnung.idsJahrgaenge.clear();
		zuordnung.idsJahrgaenge.addAll(List.of(11L, 12L));
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.zeitrasterAddAll(List.of(zeitrasterA, zeitrasterB));
		manager.planungsabschnittZeitrasterAdd(zuordnung);

		assertTrue(manager.zeitrasterIsInPlanungsabschnitt(zeitrasterA, planungsabschnitt));
		assertFalse(manager.zeitrasterIsInPlanungsabschnitt(zeitrasterB, planungsabschnitt));
		assertEquals(List.of(zeitrasterA), manager.zeitrasterGetMengeByPlanungsabschnitt(planungsabschnitt));
		assertSame(zuordnung, manager.planungsabschnittZeitrasterGetByPlanungsabschnittAndZeitraster(planungsabschnitt, zeitrasterA));
		assertEquals(List.of(ef, q1), manager.jahrgangsdatenGetMengeByPlanungsabschnittZeitraster(zuordnung));
		assertEquals(List.of(ef, q1), manager.jahrgangsdatenGetMengeByPlanungsabschnittAndZeitraster(planungsabschnitt, zeitrasterA));
		assertEquals(List.of(zeitrasterA), manager.zeitrasterGetMengeGueltigByPlanungsabschnitt(planungsabschnitt));
	}

	@Test
	@DisplayName("zeitrasterGetMengeGueltigByPlanungsabschnitt sortiert bei gleichem Beginn nach ID")
	void testZeitrasterGetMengeGueltigByPlanungsabschnittTieBreaker() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(289L);
		planungsabschnitt.gueltigVon = "2025-01-01";
		planungsabschnitt.gueltigBis = "2025-12-31";
		final UvZeitraster rasterB = createZeitraster(175L, "Raster B");
		final UvZeitraster rasterA = createZeitraster(174L, "Raster A");
		rasterA.gueltigVon = "2025-01-01";
		rasterB.gueltigVon = "2025-01-01";
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.zeitrasterAddAll(List.of(rasterB, rasterA));

		assertEquals(List.of(rasterA, rasterB), manager.zeitrasterGetMengeGueltigByPlanungsabschnitt(planungsabschnitt));
	}

	@Test
	@DisplayName("zeitrasterGetMengeGueltigByPlanungsabschnitt ergänzt direkt zugeordnete Zeitraster auch außerhalb des Gültigkeitsintervalls")
	void testZeitrasterGetMengeGueltigByPlanungsabschnittIncludesExplicitAssignments() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(287L);
		planungsabschnitt.gueltigVon = "2025-01-01";
		planungsabschnitt.gueltigBis = "2025-12-31";
		final UvZeitraster direktZugeordnetAberUngueltig = createZeitraster(173L, "Raster A");
		direktZugeordnetAberUngueltig.gueltigVon = "2026-01-01";
		final UvZeitraster gueltigOhneZuordnung = createZeitraster(172L, "Raster B");
		gueltigOhneZuordnung.gueltigVon = "2025-01-01";
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.zeitrasterAddAll(List.of(direktZugeordnetAberUngueltig, gueltigOhneZuordnung));
		manager.planungsabschnittZeitrasterAdd(createPlanungsabschnittZeitraster(287L, 173L));

		assertEquals(List.of(gueltigOhneZuordnung, direktZugeordnetAberUngueltig),
				manager.zeitrasterGetMengeGueltigByPlanungsabschnitt(planungsabschnitt));
	}

	@Test
	@DisplayName("zeitrasterGetMengeGueltigByPlanungsabschnitt behandelt Intervallgrenzen inklusiv")
	void testZeitrasterGetMengeGueltigByPlanungsabschnittBoundaries() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(286L);
		planungsabschnitt.gueltigVon = "2025-02-10";
		planungsabschnitt.gueltigBis = "2025-03-10";
		final UvZeitraster touchStart = createZeitraster(176L, "TouchStart");
		touchStart.gueltigVon = "2025-01-01";
		touchStart.gueltigBis = "2025-02-10";
		final UvZeitraster touchEnd = createZeitraster(177L, "TouchEnd");
		touchEnd.gueltigVon = "2025-03-10";
		touchEnd.gueltigBis = "2025-04-01";
		final UvZeitraster disjunkt = createZeitraster(178L, "Disjunkt");
		disjunkt.gueltigVon = "2025-03-11";
		disjunkt.gueltigBis = "2025-04-01";
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.zeitrasterAddAll(List.of(disjunkt, touchEnd, touchStart));

		assertEquals(List.of(touchStart, touchEnd), manager.zeitrasterGetMengeGueltigByPlanungsabschnitt(planungsabschnitt));
	}

	@Test
	@DisplayName("Schienen-, Lerngruppen- und Schülergruppen-Helfer leiten die zugehörigen Objekte korrekt ab")
	void testSchienenUndLerngruppenHelper() {
		final UvManager manager = new UvManager(List.of(createJahrgang(11L, "EF"), createJahrgang(12L, "Q1")),
				List.of(createFachdaten(21L, "M", "Mathematik")));
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(283L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(621L, 283L, 11L, 12L);
		final UvFach fach = createUvFach(81L, 21L);
		final UvKurs kurs = createKurs(906L, 283L, 621L, 81L);
		final UvLerngruppe lerngruppe = createKursLerngruppe(814L, 283L, 906L);
		final UvSchiene schiene = createSchiene(1006L, 283L, 6);
		final UvLerngruppenSchiene zuordnung = createLerngruppenSchiene(283L, 814L, 1006L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);
		manager.fachAdd(fach);
		manager.kursAdd(kurs);
		manager.lerngruppeAdd(lerngruppe);
		manager.schieneAdd(schiene);
		manager.lerngruppenSchieneAdd(zuordnung);

		assertSame(schiene, manager.schieneGetByPlanungsabschnittAndNummer(planungsabschnitt, 6));
		assertSame(gruppe, manager.schuelergruppeGetByKurs(kurs));
		assertSame(gruppe, manager.schuelergruppeGetByLerngruppe(lerngruppe));
		assertSame(lerngruppe, manager.lerngruppeGetByLerngruppenSchiene(zuordnung));
		assertSame(schiene, manager.schieneGetByLerngruppenSchiene(zuordnung));
		assertEquals(List.of(lerngruppe), manager.lerngruppeGetMengeBySchuelergruppe(gruppe));
	}

	@Test
	@DisplayName("planungsabschnittSchuelerGetMengeBySchuelergruppe liefert leer, wenn der Gruppe keine Schüler zugeordnet sind")
	void testPlanungsabschnittSchuelerGetMengeBySchuelergruppeEmpty() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(288L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(624L, 288L, 11L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);

		assertTrue(manager.planungsabschnittSchuelerGetMengeBySchuelergruppe(gruppe).isEmpty());
	}

	@Test
	@DisplayName("Lehrer-Unterrichtsfach-Helfer unterstützen Fach- und KLehrer-Ableitungen")
	void testLehrerUnterrichtsfachHelper() {
		final UvManager manager = new UvManager(List.of(createJahrgang(11L, "EF")),
				List.of(createFachdaten(21L, "M", "Mathematik"), createFachdaten(22L, "D", "Deutsch")));
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(284L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(622L, 284L, 11L);
		final UvFach fach = createUvFach(82L, 21L);
		final UvFach fachK = createUvFach(88L, 22L);
		final UvKlasse klasse = createKlasse(720L, 284L, 622L);
		final UvLerngruppe lerngruppe = createKlassenLerngruppe(815L, 284L, 720L, 82L);
		final UvLehrer lehrer = createLehrer(51L, "TTT");
		lehrer.idKLehrer = 5001L;
		final LehrerUnterrichtsfach regulaer = createLehrerUnterrichtsfach(43L, 51L, 21L, false);
		final LehrerUnterrichtsfach kLehrerFach = createLehrerUnterrichtsfach(44L, 5001L, 22L, true);
		final UvPlanungsabschnittLehrer planungsabschnittLehrer = createPlanungsabschnittLehrer(284L, 51L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);
		manager.fachAddAll(List.of(fach, fachK));
		manager.klasseAdd(klasse);
		manager.lerngruppeAdd(lerngruppe);
		manager.lehrerAdd(lehrer);
		manager.lehrerUnterrichtsfachAddAll(List.of(regulaer, kLehrerFach));
		manager.planungsabschnittLehrerAdd(planungsabschnittLehrer);

		assertEquals(List.of(regulaer), manager.lehrerUnterrichtsfachGetMengeByFach(fach));
		assertSame(lehrer, manager.lehrerGetByKLehrerIdOrException(5001L));
		assertSame(lehrer, manager.lehrerGetByLehrerUnterrichtsfachOrException(regulaer));
		assertSame(lehrer, manager.lehrerGetByLehrerUnterrichtsfachOrException(kLehrerFach));
	}

	@Test
	@DisplayName("PlanungsabschnittLehrer-Helfer prüfen Lehrbefähigungen fach- und lerngruppenbezogen")
	void testPlanungsabschnittLehrerHatLehrbefaehigungHelper() {
		final UvManager manager = new UvManager(List.of(createJahrgang(11L, "EF")),
				List.of(createFachdaten(21L, "M", "Mathematik"), createFachdaten(22L, "D", "Deutsch")));
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(284L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(622L, 284L, 11L);
		final UvFach fach = createUvFach(82L, 21L);
		final UvKlasse klasse = createKlasse(720L, 284L, 622L);
		final UvLerngruppe lerngruppe = createKlassenLerngruppe(815L, 284L, 720L, 82L);
		final UvLehrer lehrer = createLehrer(51L, "TTT");
		final LehrerUnterrichtsfach regulaer = createLehrerUnterrichtsfach(43L, 51L, 21L, false);
		final UvPlanungsabschnittLehrer planungsabschnittLehrer = createPlanungsabschnittLehrer(284L, 51L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);
		manager.fachAdd(fach);
		manager.klasseAdd(klasse);
		manager.lerngruppeAdd(lerngruppe);
		manager.lehrerAdd(lehrer);
		manager.lehrerUnterrichtsfachAdd(regulaer);
		manager.planungsabschnittLehrerAdd(planungsabschnittLehrer);

		assertTrue(manager.planungsabschnittLehrerHatLehrbefaehigungFach(planungsabschnittLehrer, fach));
		assertTrue(manager.planungsabschnittLehrerHatLehrbefaehigungLerngruppe(planungsabschnittLehrer, lerngruppe));
	}

	@Test
	@DisplayName("Lehrer-Verwendungs- und Tätigkeitshelfer filtern Lehrer korrekt")
	void testLehrerVerwendungsUndTaetigkeitsHelper() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(285L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(623L, 285L, 11L);
		final UvFach fach = createUvFach(83L, 21L);
		final UvKlasse klasse = createKlasse(721L, 285L, 623L);
		final UvLerngruppe lerngruppe = createKlassenLerngruppe(816L, 285L, 721L, 83L);
		final UvLehrer imPlanungsabschnitt = createLehrer(52L, "UUU");
		final UvLehrer nurTaetig = createLehrer(53L, "VVV");
		final UvLehrer nichtTaetig = createLehrer(54L, "WWW");
		nichtTaetig.datumAbgang = "2025-01-31";
		final UvLerngruppenLehrer lerngruppenLehrer = createLerngruppenLehrer(1104L, 285L, 816L, 52L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);
		manager.fachAdd(fach);
		manager.klasseAdd(klasse);
		manager.lerngruppeAdd(lerngruppe);
		manager.lehrerAddAll(List.of(imPlanungsabschnitt, nurTaetig, nichtTaetig));
		manager.planungsabschnittLehrerAdd(createPlanungsabschnittLehrer(285L, 52L));
		manager.lerngruppenLehrerAdd(lerngruppenLehrer);

		assertEquals(java.util.Set.of(imPlanungsabschnitt),
				manager.lehrerGetMengeVerwendetInPlanungsabschnittByLehrerMenge(planungsabschnitt, List.of(imPlanungsabschnitt, nurTaetig)));
		assertEquals(java.util.Set.of(imPlanungsabschnitt),
				manager.lehrerGetMengeVerwendetByLehrerMenge(List.of(imPlanungsabschnitt, nurTaetig, nichtTaetig)));
		assertEquals(List.of(nurTaetig), manager.lehrerGetMengeTaetigAberNichtInPlanungsabschnitt(planungsabschnitt));
	}

	@Test
	@DisplayName("lehrerGetMengeTaetigAberNichtInPlanungsabschnitt berücksichtigt Zeitraumgrenzen und Sortierreihenfolge")
	void testLehrerGetMengeTaetigAberNichtInPlanungsabschnittBoundariesAndOrder() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(286L);
		planungsabschnitt.gueltigVon = "2025-02-01";
		planungsabschnitt.gueltigBis = "2025-07-31";
		final UvLehrer spaet = createLehrer(55L, "ZZZ");
		spaet.datumZugang = "2025-08-01";
		final UvLehrer frueh = createLehrer(56L, "YYY");
		frueh.datumAbgang = "2025-01-31";
		final UvLehrer grenzzugang = createLehrer(57L, "BBB");
		grenzzugang.datumZugang = "2025-07-31";
		final UvLehrer grenzabgang = createLehrer(58L, "AAA");
		grenzabgang.datumAbgang = "2025-02-01";
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.lehrerAddAll(List.of(spaet, frueh, grenzzugang, grenzabgang));

		assertEquals(List.of(grenzabgang, grenzzugang), manager.lehrerGetMengeTaetigAberNichtInPlanungsabschnitt(planungsabschnitt));
	}

	@Test
	@DisplayName("Fach-Gültigkeitshelfer liefern Konflikte und fehlende Fächer")
	void testFachGueltigkeitsHelper() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvFach fachA = createUvFach(84L, 21L);
		fachA.gueltigVon = "2025-01-01";
		fachA.gueltigBis = "2025-12-31";
		final UvFach fachB = createUvFach(85L, 21L);
		fachB.gueltigVon = "2025-06-01";
		fachB.gueltigBis = null;
		final UvFach fachC = createUvFach(86L, 21L);
		fachC.gueltigVon = "2027-01-01";
		final UvStundentafel stundentafel = createStundentafel(87L, 11L);
		stundentafel.gueltigVon = "2025-01-01";
		stundentafel.gueltigBis = "2025-12-31";
		final UvStundentafelFach stundentafelFach = createStundentafelFach(97L, 87L, 1, 84L);
		manager.fachAddAll(List.of(fachA, fachB, fachC));
		manager.stundentafelAdd(stundentafel);
		manager.stundentafelFachAdd(stundentafelFach);

		assertSame(fachB, manager.fachGetByGueltigkeitskonfliktMitFach(fachA));
		final UvFach konflikt = manager.fachGetByGueltigkeitskonfliktMitNeueGueltigkeit(fachC, "2025-07-01", "2025-07-31");
		assertTrue((konflikt == fachA) || (konflikt == fachB));
		assertTrue(manager.fachGetMengeGueltigUndFehlendByStundentafel(stundentafel, 1).contains(fachB));
		assertFalse(manager.fachGetMengeGueltigUndFehlendByStundentafel(stundentafel, 1).contains(fachA));
	}

	@Test
	@DisplayName("Fach-Gültigkeitshelfer behandeln Intervallgrenzen inklusiv und offene Enden korrekt")
	void testFachGueltigkeitsHelperBoundaries() {
		final UvManager manager = new UvManager(List.of(createJahrgang(11L, "EF")),
				List.of(createFachdaten(21L, "D", "Deutsch"), createFachdaten(22L, "E", "Englisch")));
		final UvFach touchStart = createUvFach(801L, 21L);
		touchStart.gueltigVon = "2025-02-01";
		touchStart.gueltigBis = "2025-02-10";
		final UvFach touchEnd = createUvFach(802L, 21L);
		touchEnd.gueltigVon = "2025-02-10";
		touchEnd.gueltigBis = "2025-02-20";
		final UvFach disjunkt = createUvFach(803L, 21L);
		disjunkt.gueltigVon = "2025-02-21";
		disjunkt.gueltigBis = "2025-03-01";
		final UvFach offen = createUvFach(804L, 21L);
		offen.gueltigVon = "2025-02-10";
		offen.gueltigBis = null;
		manager.fachAddAll(List.of(touchStart, touchEnd, disjunkt, offen));

		assertSame(touchEnd, manager.fachGetByGueltigkeitskonfliktMitFach(touchStart));
		assertSame(touchStart, manager.fachGetByGueltigkeitskonfliktMitNeueGueltigkeit(disjunkt, "2025-02-05", "2025-02-10"));
		assertSame(touchStart, manager.fachGetByGueltigkeitskonfliktMitNeueGueltigkeit(offen, "2025-02-10", null));
		assertNull(manager.fachGetByGueltigkeitskonfliktMitNeueGueltigkeit(disjunkt, "2025-01-01", "2025-01-31"));
	}

	@Test
	@DisplayName("fachGetMengeGueltigUndFehlendByStundentafel liefert exakt die fehlenden und gültigen Fächer")
	void testFachGetMengeGueltigUndFehlendByStundentafelExact() {
		final UvManager manager = new UvManager(List.of(createJahrgang(11L, "EF")),
				List.of(createFachdaten(21L, "D", "Deutsch"), createFachdaten(22L, "E", "Englisch"), createFachdaten(23L, "M", "Mathematik")));
		final UvFach deutsch = createUvFach(84L, 21L);
		deutsch.gueltigVon = "2025-01-01";
		deutsch.gueltigBis = "2025-12-31";
		final UvFach englisch = createUvFach(85L, 22L);
		englisch.gueltigVon = "2025-06-01";
		englisch.gueltigBis = null;
		final UvFach mathematik = createUvFach(86L, 23L);
		mathematik.gueltigVon = "2026-01-01";
		final UvStundentafel stundentafel = createStundentafel(87L, 11L);
		stundentafel.gueltigVon = "2025-01-01";
		stundentafel.gueltigBis = "2025-12-31";
		final UvStundentafelFach vorhandenesFach = createStundentafelFach(97L, 87L, 1, 84L);
		manager.fachAddAll(List.of(deutsch, englisch, mathematik));
		manager.stundentafelAdd(stundentafel);
		manager.stundentafelFachAdd(vorhandenesFach);

		assertEquals(List.of(englisch), manager.fachGetMengeGueltigUndFehlendByStundentafel(stundentafel, 1));
	}

	@Test
	@DisplayName("fehlende Fächer einer Stundentafel berücksichtigen nur im Intervall gültige Fächer")
	void testFachGetMengeGueltigUndFehlendByStundentafelBoundaries() {
		final UvManager manager = new UvManager(List.of(createJahrgang(11L, "EF")),
				List.of(createFachdaten(21L, "D", "Deutsch"), createFachdaten(22L, "E", "Englisch"), createFachdaten(23L, "M", "Mathematik")));
		final UvStundentafel stundentafel = createStundentafel(921L, 11L);
		stundentafel.gueltigVon = "2025-02-10";
		stundentafel.gueltigBis = "2025-03-10";
		final UvFach deutsch = createUvFach(922L, 21L);
		deutsch.gueltigVon = "2025-02-10";
		deutsch.gueltigBis = "2025-02-20";
		final UvFach englisch = createUvFach(923L, 22L);
		englisch.gueltigVon = "2025-03-10";
		englisch.gueltigBis = null;
		final UvFach mathematik = createUvFach(924L, 23L);
		mathematik.gueltigVon = "2025-03-11";
		mathematik.gueltigBis = null;
		manager.stundentafelAdd(stundentafel);
		manager.fachAddAll(List.of(deutsch, englisch, mathematik));
		manager.stundentafelFachAdd(createStundentafelFach(925L, 921L, 1, 922L));

		assertEquals(List.of(englisch), manager.fachGetMengeGueltigUndFehlendByStundentafel(stundentafel, 1));
	}

	@Test
	@DisplayName("Raumgruppen-Gültigkeitshelfer filtern Raumgruppen nach Raum und Zeitraum")
	void testRaumgruppeGueltigkeitsHelper() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvRaumgruppe raumgruppeA = createRaumgruppe(91L, "A");
		final UvRaumgruppe raumgruppeB = createRaumgruppe(92L, "B");
		raumgruppeB.gueltigVon = "2026-01-01";
		final UvRaum raum = createRaum(64L, "R204", null);
		raum.gueltigVon = "2025-02-01";
		raum.gueltigBis = "2025-07-31";
		manager.raumgruppeAddAll(List.of(raumgruppeA, raumgruppeB));
		manager.raumAdd(raum);

		assertEquals(List.of(raumgruppeA), manager.raumgruppeGetMengeGueltigByRaum(raum));
		assertEquals(List.of(raumgruppeA), manager.raumgruppeGetMengeGueltigByZeitraum("2025-02-01", "2025-07-31"));
	}

	@Test
	@DisplayName("Raumgruppen-Gültigkeitshelfer behandeln Intervallgrenzen inklusiv und offene Enden korrekt")
	void testRaumgruppeGueltigkeitsHelperBoundaries() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvRaumgruppe vorlauf = createRaumgruppe(911L, "Vorlauf");
		vorlauf.gueltigVon = "2025-01-01";
		vorlauf.gueltigBis = "2025-02-10";
		final UvRaumgruppe anschluss = createRaumgruppe(912L, "Anschluss");
		anschluss.gueltigVon = "2025-02-10";
		anschluss.gueltigBis = "2025-03-31";
		final UvRaumgruppe offen = createRaumgruppe(913L, "Offen");
		offen.gueltigVon = "2025-04-01";
		offen.gueltigBis = null;
		manager.raumgruppeAddAll(List.of(vorlauf, anschluss, offen));

		assertEquals(List.of(anschluss, vorlauf), manager.raumgruppeGetMengeGueltigByZeitraum("2025-02-10", "2025-02-10"));
		assertEquals(List.of(offen), manager.raumgruppeGetMengeGueltigByZeitraum("2025-05-01", null));
		assertTrue(manager.raumgruppeGetMengeGueltigByZeitraum("2024-12-01", "2024-12-31").isEmpty());
	}

	@Test
	@DisplayName("fachGetMengeVerwendetInStundentafel liefert nur referenzierte Fächer")
	void testFachGetMengeVerwendetInStundentafelExact() {
		final UvManager manager = new UvManager(List.of(createJahrgang(11L, "EF")),
				List.of(createFachdaten(21L, "D", "Deutsch"), createFachdaten(22L, "E", "Englisch")));
		final UvFach verwendet = createUvFach(87L, 21L);
		final UvFach unbenutzt = createUvFach(88L, 22L);
		final UvStundentafel stundentafel = createStundentafel(89L, 11L);
		final UvStundentafelFach zuordnung = createStundentafelFach(98L, 89L, 1, 87L);
		manager.fachAddAll(List.of(verwendet, unbenutzt));
		manager.stundentafelAdd(stundentafel);
		manager.stundentafelFachAdd(zuordnung);

		assertEquals(List.of(verwendet), manager.fachGetMengeVerwendetInStundentafel());
	}

	@Test
	@DisplayName("jahrgangsdatenGetByStundentafel liefert den referenzierten Jahrgang")
	void testJahrgangsdatenGetByStundentafel() {
		final JahrgangsDaten ef = createJahrgang(11L, "EF");
		final UvManager manager = new UvManager(List.of(ef), List.of(createFachdaten(21L, "M", "Mathematik")));
		final UvStundentafel stundentafel = createStundentafel(201L, 11L);

		manager.stundentafelAdd(stundentafel);

		assertSame(ef, manager.jahrgangsdatenGetByStundentafel(stundentafel));
	}

	@Test
	@DisplayName("planungsabschnittGetByIdOrException liefert den referenzierten Abschnitt")
	void testPlanungsabschnittGetByIdOrException() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(202L);

		manager.planungsabschnittAdd(planungsabschnitt);

		assertSame(planungsabschnitt, manager.planungsabschnittGetByIdOrException(202L));
	}

	@Test
	@DisplayName("Pflichtstundensoll-Listen und Lehrerableitung arbeiten konsistent")
	void testLehrerPflichtstundensollListAndRemoveHelper() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvLehrer lehrer = createLehrer(61L, "AAA");
		final UvLehrerPflichtstundensoll pflichtA = createLehrerPflichtstundensoll(2101L, 61L, 25.5, "2025-08-01", null);
		final UvLehrerPflichtstundensoll pflichtB = createLehrerPflichtstundensoll(2102L, 61L, 24.0, "2024-08-01", "2025-07-31");
		manager.lehrerAdd(lehrer);

		manager.lehrerPflichtstundensollAddAll(List.of(pflichtB, pflichtA));

		assertEquals(List.of(pflichtA, pflichtB), manager.lehrerPflichtstundensollGetMengeAsList());
		assertSame(lehrer, manager.lehrerGetByLehrerPflichtstundensoll(pflichtA));

		manager.lehrerPflichtstundensollRemove(pflichtB);
		manager.lehrerPflichtstundensollRemoveAll(List.of(pflichtA));

		assertTrue(manager.lehrerPflichtstundensollGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("Anrechnungsstunden-Listen und Lehrerableitung arbeiten konsistent")
	void testLehrerAnrechnungsstundenListAndRemoveHelper() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvLehrer lehrer = createLehrer(61L, "AAA");
		final UvLehrerAnrechnungsstunden anrA = createLehrerAnrechnungsstunden(2201L, 61L, "A", 1.5, "2025-08-01", null);
		final UvLehrerAnrechnungsstunden anrB = createLehrerAnrechnungsstunden(2202L, 61L, "B", 0.5, "2024-08-01", "2025-07-31");
		manager.lehrerAdd(lehrer);

		manager.lehrerAnrechnungsstundenAddAll(List.of(anrB, anrA));

		assertEquals(List.of(anrA, anrB), manager.lehrerAnrechnungsstundenGetMengeAsList());
		assertSame(lehrer, manager.lehrerGetByLehrerAnrechnungsstunden(anrA));

		manager.lehrerAnrechnungsstundenRemoveById(2202L);
		manager.lehrerAnrechnungsstundenRemoveAll(List.of(anrA));

		assertTrue(manager.lehrerAnrechnungsstundenGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("lehrerGetMengeByPlanungsabschnitt liefert die referenzierten Lehrer in Sortierreihenfolge")
	void testLehrerGetMengeByPlanungsabschnitt() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(203L);
		final UvLehrer lehrerB = createLehrer(62L, "BBB");
		final UvLehrer lehrerA = createLehrer(63L, "AAA");
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.lehrerAddAll(List.of(lehrerB, lehrerA));
		manager.planungsabschnittLehrerAddAll(List.of(
				createPlanungsabschnittLehrer(203L, 62L),
				createPlanungsabschnittLehrer(203L, 63L)));

		assertEquals(List.of(lehrerA, lehrerB), manager.lehrerGetMengeByPlanungsabschnitt(planungsabschnitt));
	}

	@Test
	@DisplayName("personenbezogene Mengen sind nach den definierten Comparatoren sortiert")
	void testPersonenbezogeneSortierungen() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(290L);
		final UvLehrer lehrerB = createLehrer(81L, "BBB");
		final UvLehrer lehrerA = createLehrer(82L, "AAA");
		final UvPlanungsabschnittLehrer paLehrerB = createPlanungsabschnittLehrer(290L, 81L);
		final UvPlanungsabschnittLehrer paLehrerA = createPlanungsabschnittLehrer(290L, 82L);
		final UvLehrerPflichtstundensoll pflichtAlt = createLehrerPflichtstundensoll(2501L, 82L, 24.0, "2024-08-01", "2025-01-31");
		final UvLehrerPflichtstundensoll pflichtNeu = createLehrerPflichtstundensoll(2502L, 82L, 25.0, "2025-02-01", null);
		final UvLehrerAnrechnungsstunden anrB = createLehrerAnrechnungsstunden(2503L, 82L, "B", 1.0, "2025-01-01", "2025-07-31");
		final UvLehrerAnrechnungsstunden anrA = createLehrerAnrechnungsstunden(2504L, 82L, "A", 1.0, "2025-01-01", "2025-07-31");
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.lehrerAddAll(List.of(lehrerB, lehrerA));
		manager.planungsabschnittLehrerAddAll(List.of(paLehrerB, paLehrerA));
		manager.lehrerPflichtstundensollAddAll(List.of(pflichtAlt, pflichtNeu));
		manager.lehrerAnrechnungsstundenAddAll(List.of(anrB, anrA));

		assertEquals(List.of(lehrerA, lehrerB), manager.lehrerGetMengeAsList());
		assertEquals(2, manager.planungsabschnittLehrerGetMengeByPlanungsabschnitt(planungsabschnitt).size());
		assertEquals(List.of(pflichtNeu, pflichtAlt), manager.lehrerPflichtstundensollGetMengeByLehrer(lehrerA));
		assertEquals(List.of(anrA, anrB), manager.lehrerAnrechnungsstundenGetMengeByLehrer(lehrerA));
	}

	@Test
	@DisplayName("sachliche Mengen sind nach den definierten Comparatoren sortiert")
	void testSachlicheSortierungen() {
		final FachDaten fdB = createFachdaten(31L, "B", "Beta");
		fdB.sortierung = 2;
		final FachDaten fdA = createFachdaten(32L, "A", "Alpha");
		fdA.sortierung = 1;
		final UvManager manager = new UvManager(List.of(createJahrgang(11L, "EF")), List.of(fdB, fdA));
		final UvRaumgruppe raumgruppeB = createRaumgruppe(301L, "B");
		final UvRaumgruppe raumgruppeA = createRaumgruppe(302L, "A");
		final UvRaum raumB = createRaum(303L, "R2", null);
		final UvRaum raumA = createRaum(304L, "R1", null);
		final UvFach fachB = createUvFach(305L, 31L);
		final UvFach fachA = createUvFach(306L, 32L);
		final UvStundentafel stundentafel = createStundentafel(307L, 11L);
		final UvStundentafelFach stfB = createStundentafelFach(308L, 307L, 1, 305L);
		final UvStundentafelFach stfA = createStundentafelFach(309L, 307L, 1, 306L);
		final UvZeitraster rasterSpaet = createZeitraster(311L, "Spaet");
		rasterSpaet.gueltigVon = "2025-02-01";
		final UvZeitraster rasterFrueh = createZeitraster(310L, "Frueh");
		rasterFrueh.gueltigVon = "2025-01-01";
		final UvZeitrasterEintrag eintragB = createZeitrasterEintrag(313L, 311L);
		eintragB.wochentag = 2;
		eintragB.stunde = 2;
		final UvZeitrasterEintrag eintragA = createZeitrasterEintrag(312L, 310L);
		eintragA.wochentag = 1;
		eintragA.stunde = 1;
		manager.raumgruppeAddAll(List.of(raumgruppeB, raumgruppeA));
		manager.raumAddAll(List.of(raumB, raumA));
		manager.fachAddAll(List.of(fachB, fachA));
		manager.stundentafelAdd(stundentafel);
		manager.stundentafelFachAddAll(List.of(stfB, stfA));
		manager.zeitrasterAddAll(List.of(rasterSpaet, rasterFrueh));
		manager.zeitrasterEintragAddAll(List.of(eintragB, eintragA));

		assertEquals(List.of(raumgruppeA, raumgruppeB), manager.raumgruppeGetMengeAsList());
		assertEquals(List.of(raumA, raumB), manager.raumGetMengeAsList());
		assertEquals(List.of(fachA, fachB), manager.fachGetMengeAsList());
		assertEquals(List.of(stfA, stfB), manager.stundentafelFachGetMengeAsList());
		assertEquals(List.of(rasterFrueh, rasterSpaet), manager.zeitrasterGetMengeAsList());
		assertEquals(List.of(eintragA, eintragB), manager.zeitrasterEintragGetMengeAsList());
	}

	@Test
	@DisplayName("planungsabschnittsbezogene Mengen sind nach den definierten Comparatoren sortiert")
	void testPlanungsabschnittsbezogeneSortierungen() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(291L);
		final UvSchuelergruppe gruppeB = createSchuelergruppe(401L, 291L, 11L);
		gruppeB.bezeichnung = "B";
		final UvSchuelergruppe gruppeA = createSchuelergruppe(402L, 291L, 11L);
		gruppeA.bezeichnung = "A";
		final UvKlasse klasseB = createKlasse(403L, 291L, 401L);
		klasseB.kuerzel = "6B";
		final UvKlasse klasseA = createKlasse(404L, 291L, 402L);
		klasseA.kuerzel = "5A";
		final UvKurs kursB = createKurs(405L, 291L, 401L, 21L);
		kursB.kursart = "LK";
		kursB.kursnummer = 2;
		final UvKurs kursA = createKurs(406L, 291L, 402L, 21L);
		kursA.kursart = "GK";
		kursA.kursnummer = 1;
		final UvSchiene schieneB = createSchiene(407L, 291L, 2);
		final UvSchiene schieneA = createSchiene(408L, 291L, 1);
		final UvLerngruppe lerngruppeKlassseB = createKlassenLerngruppe(409L, 291L, 403L, 21L);
		final UvLerngruppe lerngruppeKlasseA = createKlassenLerngruppe(410L, 291L, 404L, 21L);
		final UvLerngruppe lerngruppeKursB = createKursLerngruppe(411L, 291L, 405L);
		final UvLerngruppe lerngruppeKursA = createKursLerngruppe(412L, 291L, 406L);
		final UvLehrer lehrerB = createLehrer(413L, "BBB");
		final UvLehrer lehrerA = createLehrer(414L, "AAA");
		final UvLerngruppenLehrer lgLehrerB = createLerngruppenLehrer(415L, 291L, 410L, 413L);
		lgLehrerB.reihenfolge = 2;
		final UvLerngruppenLehrer lgLehrerA = createLerngruppenLehrer(416L, 291L, 410L, 414L);
		lgLehrerA.reihenfolge = 1;
		final UvKlassenLehrer klB = createKlassenLehrer(418L, 291L, 404L, 413L);
		klB.reihenfolge = 2;
		final UvKlassenLehrer klA = createKlassenLehrer(417L, 291L, 404L, 414L);
		klA.reihenfolge = 1;
		final UvUnterricht unterrichtB = createUnterricht(420L, 291L, null, 410L);
		final UvUnterricht unterrichtA = createUnterricht(419L, 291L, null, 409L);
		final UvPlanungsabschnittZeitraster paRasterB = createPlanungsabschnittZeitraster(291L, 2L);
		final UvPlanungsabschnittZeitraster paRasterA = createPlanungsabschnittZeitraster(291L, 1L);
		final UvZeitraster rasterB = createZeitraster(2L, "Raster B");
		final UvZeitraster rasterA = createZeitraster(1L, "Raster A");
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAddAll(List.of(gruppeB, gruppeA));
		manager.klasseAddAll(List.of(klasseB, klasseA));
		manager.kursAddAll(List.of(kursB, kursA));
		manager.schieneAddAll(List.of(schieneB, schieneA));
		manager.zeitrasterAddAll(List.of(rasterB, rasterA));
		manager.lerngruppeAddAll(List.of(lerngruppeKlassseB, lerngruppeKlasseA, lerngruppeKursB, lerngruppeKursA));
		manager.lehrerAddAll(List.of(lehrerB, lehrerA));
		manager.lerngruppenLehrerAddAll(List.of(lgLehrerB, lgLehrerA));
		manager.klassenLehrerAddAll(List.of(klB, klA));
		manager.unterrichtAddAll(List.of(unterrichtB, unterrichtA));
		manager.planungsabschnittZeitrasterAddAll(List.of(paRasterB, paRasterA));

		assertEquals(List.of(gruppeA, gruppeB), manager.schuelergruppeGetMengeAsList());
		assertEquals(List.of(klasseA, klasseB), manager.klasseGetMengeAsList());
		assertEquals(List.of(kursA, kursB), manager.kursGetMengeAsList());
		assertEquals(List.of(schieneA, schieneB), manager.schieneGetMengeAsList());
		assertEquals(List.of(lerngruppeKlasseA, lerngruppeKlassseB, lerngruppeKursA, lerngruppeKursB), manager.lerngruppeGetMengeAsList());
		assertEquals(List.of(lgLehrerA, lgLehrerB), manager.lerngruppenLehrerGetMengeAsList());
		assertEquals(List.of(klA, klB), manager.klassenLehrerGetMengeAsList());
		assertEquals(List.of(unterrichtA, unterrichtB), manager.unterrichtGetMengeAsList());
		assertEquals(List.of(paRasterA, paRasterB), manager.planungsabschnittZeitrasterGetMengeAsList());
	}

	@Test
	@DisplayName("planungsabschnittsbezogene Teilgetter liefern die definierte stabile Reihenfolge")
	void testPlanungsabschnittsbezogeneTeilgetterSortierungen() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(292L);
		final UvLehrer lehrerB = createLehrer(421L, "BBB");
		final UvLehrer lehrerA = createLehrer(422L, "AAA");
		final UvPlanungsabschnittLehrer paLehrerB = createPlanungsabschnittLehrer(292L, 421L);
		final UvPlanungsabschnittLehrer paLehrerA = createPlanungsabschnittLehrer(292L, 422L);
		final UvSchuelergruppe gruppeB = createSchuelergruppe(423L, 292L, 11L);
		gruppeB.bezeichnung = "B";
		final UvSchuelergruppe gruppeA = createSchuelergruppe(424L, 292L, 11L);
		gruppeA.bezeichnung = "A";
		final UvKlasse klasseB = createKlasse(425L, 292L, 423L);
		klasseB.kuerzel = "6B";
		final UvKlasse klasseA = createKlasse(426L, 292L, 424L);
		klasseA.kuerzel = "5A";
		final UvFach fach = createUvFach(427L, 21L);
		final UvKurs kursB = createKurs(428L, 292L, 423L, 427L);
		kursB.kursart = "LK";
		kursB.kursnummer = 2;
		final UvKurs kursA = createKurs(429L, 292L, 424L, 427L);
		kursA.kursart = "GK";
		kursA.kursnummer = 1;
		final UvLerngruppe lerngruppeB = createKlassenLerngruppe(432L, 292L, 425L, 427L);
		final UvLerngruppe lerngruppeA = createKlassenLerngruppe(433L, 292L, 426L, 427L);
		final UvLerngruppenLehrer lgLehrerB = createLerngruppenLehrer(434L, 292L, 433L, 421L);
		lgLehrerB.reihenfolge = 2;
		final UvLerngruppenLehrer lgLehrerA = createLerngruppenLehrer(435L, 292L, 433L, 422L);
		lgLehrerA.reihenfolge = 1;
		final UvKlassenLehrer klassenLehrerB = createKlassenLehrer(436L, 292L, 426L, 421L);
		klassenLehrerB.reihenfolge = 2;
		final UvKlassenLehrer klassenLehrerA = createKlassenLehrer(437L, 292L, 426L, 422L);
		klassenLehrerA.reihenfolge = 1;
		final UvZeitraster rasterB = createZeitraster(438L, "Raster B");
		rasterB.gueltigVon = "2025-03-01";
		final UvZeitraster rasterA = createZeitraster(439L, "Raster A");
		rasterA.gueltigVon = "2025-02-01";
		final UvPlanungsabschnittZeitraster paRasterB = createPlanungsabschnittZeitraster(292L, 438L);
		final UvPlanungsabschnittZeitraster paRasterA = createPlanungsabschnittZeitraster(292L, 439L);
		final UvUnterricht unterrichtB = createUnterricht(440L, 292L, null, 433L);
		final UvUnterricht unterrichtA = createUnterricht(441L, 292L, null, 432L);
		final UvPlanungsabschnittSchueler schuelerB = createPlanungsabschnittSchueler(292L, 442L, 11L, 425L);
		schuelerB.daten.nachname = "Bauer";
		schuelerB.daten.vorname = "Berta";
		final UvPlanungsabschnittSchueler schuelerA = createPlanungsabschnittSchueler(292L, 443L, 11L, 426L);
		schuelerA.daten.nachname = "Acker";
		schuelerA.daten.vorname = "Anton";
		final UvSchuelergruppeSchueler gruppenSchuelerB = createSchuelergruppeSchueler(292L, 424L, 442L);
		final UvSchuelergruppeSchueler gruppenSchuelerA = createSchuelergruppeSchueler(292L, 424L, 443L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.lehrerAddAll(List.of(lehrerB, lehrerA));
		manager.planungsabschnittLehrerAddAll(List.of(paLehrerB, paLehrerA));
		manager.schuelergruppeAddAll(List.of(gruppeB, gruppeA));
		manager.fachAdd(fach);
		manager.klasseAddAll(List.of(klasseB, klasseA));
		manager.kursAddAll(List.of(kursB, kursA));
		manager.lerngruppeAddAll(List.of(lerngruppeB, lerngruppeA));
		manager.lerngruppenLehrerAddAll(List.of(lgLehrerB, lgLehrerA));
		manager.klassenLehrerAddAll(List.of(klassenLehrerB, klassenLehrerA));
		manager.zeitrasterAddAll(List.of(rasterB, rasterA));
		manager.planungsabschnittZeitrasterAddAll(List.of(paRasterB, paRasterA));
		manager.unterrichtAddAll(List.of(unterrichtB, unterrichtA));
		manager.planungsabschnittSchuelerAddAll(List.of(schuelerB, schuelerA));
		manager.schuelergruppeSchuelerAddAll(List.of(gruppenSchuelerB, gruppenSchuelerA));

		assertEquals(List.of(paLehrerB, paLehrerA), manager.planungsabschnittLehrerGetMengeByPlanungsabschnitt(planungsabschnitt));
		assertEquals(List.of(lehrerA, lehrerB), manager.lehrerGetMengeByPlanungsabschnitt(planungsabschnitt));
		assertEquals(List.of(gruppeB, gruppeA), manager.schuelergruppeGetMengeByPlanungsabschnitt(planungsabschnitt));
		assertEquals(List.of(klasseA, klasseB), manager.klasseGetMengeByPlanungsabschnitt(planungsabschnitt));
		assertEquals(List.of(kursA, kursB), manager.kursGetMengeByPlanungsabschnitt(planungsabschnitt));
		assertEquals(List.of(lgLehrerB, lgLehrerA), manager.lerngruppenLehrerGetMengeByLerngruppe(lerngruppeA));
		assertEquals(List.of(klassenLehrerB, klassenLehrerA), manager.klassenLehrerGetMengeByKlasse(klasseA));
		assertEquals(List.of(unterrichtA, unterrichtB), manager.unterrichtGetMengeByPlanungsabschnitt(planungsabschnitt));
		assertEquals(List.of(paRasterB, paRasterA), manager.planungsabschnittZeitrasterGetMengeByPlanungsabschnitt(planungsabschnitt.id));
		assertEquals(List.of(schuelerA, schuelerB), manager.planungsabschnittSchuelerGetMengeAsList());
		assertEquals(List.of(gruppenSchuelerA, gruppenSchuelerB), manager.schuelergruppeSchuelerGetMengeAsList());
		assertEquals(List.of(gruppenSchuelerB, gruppenSchuelerA), manager.schuelergruppeSchuelerGetMengeBySchuelergruppe(292L, 424L));
	}

	@Test
	@DisplayName("Klassen-Bulk- und Remove-Varianten arbeiten konsistent")
	void testKlasseBulkAndRemove() {
		final UvManager manager = new UvManager(List.of(createJahrgang(11L, "EF")),
				List.of(createFachdaten(21L, "M", "Mathematik"), createFachdaten(22L, "D", "Deutsch")));
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(204L);
		final UvSchuelergruppe gruppeA = createSchuelergruppe(631L, 204L, 11L);
		final UvSchuelergruppe gruppeB = createSchuelergruppe(632L, 204L, 11L);
		final UvFach fachA = createUvFach(91L, 21L);
		final UvFach fachB = createUvFach(92L, 22L);
		final UvKlasse klasseA = createKlasse(731L, 204L, 631L);
		final UvKlasse klasseB = createKlasse(732L, 204L, 632L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAddAll(List.of(gruppeA, gruppeB));
		manager.fachAddAll(List.of(fachA, fachB));
		manager.klasseAddAll(List.of(klasseA, klasseB));

		assertEquals(2, manager.klasseGetMengeAsList().size());
		assertTrue(manager.klasseGetMengeAsList().contains(klasseA));

		manager.klasseRemove(klasseB);

		assertEquals(List.of(klasseA), manager.klasseGetMengeAsList());
	}

	@Test
	@DisplayName("Kurs- und Lerngruppen-Remove-Varianten arbeiten konsistent")
	void testKursUndLerngruppeBulkAndRemove() {
		final UvManager manager = new UvManager(List.of(createJahrgang(11L, "EF")),
				List.of(createFachdaten(21L, "M", "Mathematik"), createFachdaten(22L, "D", "Deutsch")));
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(204L);
		final UvSchuelergruppe gruppeA = createSchuelergruppe(631L, 204L, 11L);
		final UvSchuelergruppe gruppeB = createSchuelergruppe(632L, 204L, 11L);
		final UvFach fachA = createUvFach(91L, 21L);
		final UvFach fachB = createUvFach(92L, 22L);
		final UvKurs kursA = createKurs(931L, 204L, 631L, 91L);
		final UvKurs kursB = createKurs(932L, 204L, 632L, 92L);
		final UvKlasse klasseA = createKlasse(731L, 204L, 631L);
		final UvLerngruppe lerngruppeA = createKlassenLerngruppe(831L, 204L, 731L, 91L);
		final UvLerngruppe lerngruppeB = createKursLerngruppe(832L, 204L, 931L);
		final UvLerngruppe lerngruppeC = createKursLerngruppe(833L, 204L, 932L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAddAll(List.of(gruppeA, gruppeB));
		manager.fachAddAll(List.of(fachA, fachB));
		manager.klasseAdd(klasseA);
		manager.kursAddAll(List.of(kursA, kursB));
		manager.lerngruppeAddAll(List.of(lerngruppeA, lerngruppeB, lerngruppeC));

		assertEquals(2, manager.kursGetMengeAsList().size());
		assertEquals(3, manager.lerngruppeGetMengeAsList().size());

		manager.lerngruppeRemove(lerngruppeA);
		manager.kursRemoveAll(List.of(kursA));
		manager.kursRemove(kursB);

		assertTrue(manager.kursGetMengeAsList().isEmpty());
		assertTrue(manager.lerngruppeGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("Schienen-Bulk- und Remove-Varianten arbeiten konsistent")
	void testSchieneBulkAndRemove() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(204L);
		final UvSchiene schieneA = createSchiene(1031L, 204L, 1);
		final UvSchiene schieneB = createSchiene(1032L, 204L, 2);
		manager.planungsabschnittAdd(planungsabschnitt);

		manager.schieneAddAll(List.of(schieneA, schieneB));

		assertEquals(2, manager.schieneGetMengeAsList().size());
		assertTrue(manager.schieneGetMengeAsList().contains(schieneA));

		manager.schieneRemoveById(1032L);
		manager.schieneRemoveAll(List.of(schieneA));

		assertTrue(manager.schieneGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("LerngruppenLehrer-Bulk-, Patch- und Remove-Varianten arbeiten konsistent")
	void testLerngruppenLehrerBulkPatchAndRemove() {
		final KlassenLerngruppeScenario s = createKlassenLerngruppeScenario(205L, 633L, 93L, 733L, 833L);
		final UvLehrer lehrerA = createLehrer(64L, "AAA");
		final UvLehrer lehrerB = createLehrer(65L, "BBB");
		final UvLerngruppenLehrer lgLehrerA = createLerngruppenLehrer(1105L, 205L, 833L, 64L);
		final UvLerngruppenLehrer lgLehrerB = createLerngruppenLehrer(1106L, 205L, 833L, 65L);
		s.manager.lehrerAddAll(List.of(lehrerA, lehrerB));

		s.manager.lerngruppenLehrerAddAll(List.of(lgLehrerA, lgLehrerB));

		assertEquals(2, s.manager.lerngruppenLehrerGetMengeAsList().size());
		assertSame(lehrerA, s.manager.lehrerGetByLerngruppenLehrer(lgLehrerA));

		final UvLerngruppenLehrer lgLehrerAPatch = createLerngruppenLehrer(1105L, 205L, 833L, 64L);
		lgLehrerAPatch.reihenfolge = 2;
		s.manager.lerngruppenLehrerAllPatchAttributes(List.of(lgLehrerAPatch));
		assertEquals(2, s.manager.lerngruppenLehrerGetByIdOrException(205L, 833L, 64L).reihenfolge);

		s.manager.lerngruppenLehrerRemoveById(205L, 833L, 65L);
		s.manager.lerngruppenLehrerRemoveAll(List.of(lgLehrerAPatch));

		assertTrue(s.manager.lerngruppenLehrerGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("LerngruppenSchiene-Bulk- und Remove-Varianten arbeiten konsistent")
	void testLerngruppenSchieneBulkAndRemove() {
		final KlassenLerngruppeScenario s = createKlassenLerngruppeScenario(205L, 633L, 93L, 733L, 833L);
		final UvSchiene schieneA = createSchiene(1033L, 205L, 3);
		final UvSchiene schieneB = createSchiene(1034L, 205L, 4);
		final UvLerngruppenSchiene lgSchieneA = createLerngruppenSchiene(205L, 833L, 1033L);
		final UvLerngruppenSchiene lgSchieneB = createLerngruppenSchiene(205L, 833L, 1034L);
		s.manager.schieneAddAll(List.of(schieneA, schieneB));

		s.manager.lerngruppenSchieneAddAll(List.of(lgSchieneA, lgSchieneB));

		assertEquals(2, s.manager.lerngruppenSchieneGetMengeAsList().size());
		assertEquals(2, s.manager.lerngruppenSchieneGetMengeByPlanungsabschnitt(205L).size());

		s.manager.lerngruppenSchieneRemoveById(205L, 833L, 1034L);
		s.manager.lerngruppenSchieneRemoveAll(List.of(lgSchieneA));

		assertTrue(s.manager.lerngruppenSchieneGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("Alle Klassen- und Lerngruppen-Löschvarianten entfernen Unterrichte einschließlich ihrer Zuordnungen")
	void testKlasseUndLerngruppeRemoveCascadesToUnterrichtZuordnungen() {
		for (int variante = 0; variante < 6; variante++) {
			final UnterrichtScenario s = createUnterrichtMitZuordnungenScenario();
			final UvManager manager = s.basis.manager;
			switch (variante) {
				case 0 -> {
					manager.klasseRemoveById(206L, 734L);
				}
				case 1 -> {
					manager.klasseRemove(s.basis.klasse);
				}
				case 2 -> {
					manager.klasseRemoveAll(List.of(s.basis.klasse));
				}
				case 3 -> {
					manager.lerngruppeRemoveById(206L, 834L);
				}
				case 4 -> {
					manager.lerngruppeRemove(s.basis.lerngruppe);
				}
				default -> {
					manager.lerngruppeRemoveAll(List.of(s.basis.lerngruppe));
				}
			}
			assertTrue(manager.lerngruppeGetMengeAsList().isEmpty());
			assertTrue(manager.unterrichtGetMengeAsList().isEmpty());
			assertTrue(manager.unterrichtGetMengeByLerngruppe(206L, 834L).isEmpty());
			assertTrue(manager.unterrichtRaumGetMengeAsList().isEmpty());
			assertTrue(manager.unterrichtLerngruppenlehrerGetMengeAsList().isEmpty());
			assertTrue(manager.lerngruppenLehrerGetMengeAsList().isEmpty());
			assertEquals(1, manager.raumGetMengeAsList().size());
			assertEquals(1, manager.lehrerGetMengeAsList().size());
			assertEquals((variante < 3) ? 0 : 1, manager.klasseGetMengeAsList().size());
		}
	}

	@Test
	@DisplayName("Klassen-Löschkaskade erhält die Lerngruppen und Unterrichte anderer Klassen")
	void testKlasseRemovePreservesOtherKlasse() {
		final UnterrichtScenario s = createUnterrichtMitZuordnungenScenario();
		final UvManager manager = s.basis.manager;
		final UvKlasse andereKlasse = createKlasse(735L, 206L, 634L);
		final UvLerngruppe andereLerngruppe = createKlassenLerngruppe(835L, 206L, 735L, 94L);
		final UvUnterricht andererUnterricht = createUnterricht(1206L, 206L, null, 835L);
		final UvUnterrichtRaum raum = createUnterrichtRaum(206L, 1206L, 65L);
		final UvLerngruppenLehrer lehrer = createLerngruppenLehrer(1108L, 206L, 835L, 66L);
		final UvUnterrichtLerngruppenlehrer unterrichtLehrer = createUnterrichtLerngruppenlehrer(206L, 1206L, 1108L);
		manager.klasseAdd(andereKlasse);
		manager.lerngruppeAdd(andereLerngruppe);
		manager.unterrichtAdd(andererUnterricht);
		manager.unterrichtRaumAdd(raum);
		manager.lerngruppenLehrerAdd(lehrer);
		manager.unterrichtLerngruppenlehrerAdd(unterrichtLehrer);

		manager.klasseRemove(s.basis.klasse);

		assertEquals(List.of(andereKlasse), manager.klasseGetMengeAsList());
		assertEquals(List.of(andereLerngruppe), manager.lerngruppeGetMengeAsList());
		assertEquals(List.of(andererUnterricht), manager.unterrichtGetMengeAsList());
		assertEquals(List.of(raum), manager.unterrichtRaumGetMengeAsList());
		assertEquals(List.of(lehrer), manager.lerngruppenLehrerGetMengeAsList());
		assertEquals(List.of(unterrichtLehrer), manager.unterrichtLerngruppenlehrerGetMengeAsList());
	}

	@Test
	@DisplayName("Alle Unterricht-Löschvarianten entfernen nur die abhängigen Zuordnungen")
	void testUnterrichtRemoveCascadesToZuordnungen() {
		for (int variante = 0; variante < 3; variante++) {
			final UnterrichtScenario s = createUnterrichtMitZuordnungenScenario();
			final UvManager manager = s.basis.manager;
			switch (variante) {
				case 0 -> {
					manager.unterrichtRemoveById(1204L);
				}
				case 1 -> {
					manager.unterrichtRemove(s.unterrichtA);
				}
				default -> {
					manager.unterrichtRemoveAll(List.of(s.unterrichtA));
				}
			}
			assertEquals(List.of(s.unterrichtB), manager.unterrichtGetMengeAsList());
			assertTrue(manager.unterrichtRaumGetMengeByUnterricht(s.unterrichtA).isEmpty());
			assertTrue(manager.unterrichtLerngruppenlehrerGetMengeByUnterricht(s.unterrichtA).isEmpty());
			assertEquals(1, manager.unterrichtRaumGetMengeAsList().size());
			assertEquals(1, manager.unterrichtRaumGetMengeByUnterricht(s.unterrichtB).size());
			assertEquals(1, manager.unterrichtLerngruppenlehrerGetMengeAsList().size());
			assertEquals(1, manager.unterrichtLerngruppenlehrerGetMengeByUnterricht(s.unterrichtB).size());
			assertEquals(1, manager.lerngruppenLehrerGetMengeAsList().size());
		}
	}

	@Test
	@DisplayName("Patch erhält Unterrichtszuordnungen; Löschen des Lerngruppenlehrers entfernt nur seine Unterrichtszuordnungen")
	void testPatchPreservesUnterrichtZuordnungenAndLehrerRemoveCascades() {
		final UnterrichtScenario s = createUnterrichtMitZuordnungenScenario();
		final UvManager manager = s.basis.manager;
		final List<UvUnterrichtRaum> raeume = List.copyOf(manager.unterrichtRaumGetMengeAsList());
		final List<UvUnterrichtLerngruppenlehrer> lehrer = List.copyOf(manager.unterrichtLerngruppenlehrerGetMengeAsList());
		final UvUnterricht patch = createUnterricht(1204L, 206L, null, 834L);
		manager.unterrichtAllPatchAttributes(List.of(patch));
		manager.lerngruppenLehrerAllPatchAttributes(List.of(createLerngruppenLehrer(1107L, 206L, 834L, 66L)));

		assertSame(patch, manager.unterrichtGetByIdOrException(1204L));
		assertEquals(raeume, manager.unterrichtRaumGetMengeAsList());
		assertEquals(lehrer, manager.unterrichtLerngruppenlehrerGetMengeAsList());

		manager.lerngruppenLehrerRemoveById(206L, 834L, 66L);

		assertTrue(manager.unterrichtLerngruppenlehrerGetMengeAsList().isEmpty());
		assertEquals(raeume, manager.unterrichtRaumGetMengeAsList());
		assertEquals(2, manager.unterrichtGetMengeAsList().size());
	}

	@Test
	@DisplayName("OrNull-ID-Zugriffe liefern bei unbekannten IDs null")
	void testSelectionGetByIdOrNullUnknownIds() {
		final UvManager manager = createManagerWithMinimalBasis();
		assertNull(manager.lehrerGetByIdOrNull(999L));
		assertNull(manager.raumGetByIdOrNull(999L));
		assertNull(manager.raumgruppeGetByIdOrNull(999L));
		assertNull(manager.stundentafelGetByIdOrNull(999L));
		assertNull(manager.fachGetByIdOrNull(999L));
		assertNull(manager.zeitrasterGetByIdOrNull(999L));
		assertNull(manager.zeitrasterEintragGetByIdOrNull(999L));
		assertNull(manager.planungsabschnittSchuelerGetByIdOrNull(999L, 999L));
		assertNull(manager.schuelergruppeGetByIdOrNull(999L));
		assertNull(manager.klasseGetByIdOrNull(999L));
		assertNull(manager.kursGetByIdOrNull(999L));
		assertNull(manager.schieneGetByIdOrNull(999L));
		assertNull(manager.lerngruppeGetByIdOrNull(999L));
		assertNull(manager.unterrichtGetByIdOrNull(999L));
	}

	@Test
	@DisplayName("OrNull-ID-Zugriffe liefern die gespeicherten Instanzen und nach einem Patch die neue Instanz")
	void testSelectionGetByIdOrNullStoredInstances() {
		final GrundstrukturScenario s = createGrundstrukturScenario();
		final UvRaumgruppe raumgruppe = createRaumgruppe(69L, "Gruppe");
		final UvKurs kurs = createKurs(901L, 214L, 639L, 101L);
		final UvUnterricht unterricht = createUnterricht(1206L, 214L, null, 839L);
		final UvZeitrasterEintrag eintrag = createZeitrasterEintrag(1502L, 1501L);
		s.manager.raumgruppeAdd(raumgruppe);
		s.manager.kursAdd(kurs);
		s.manager.unterrichtAdd(unterricht);
		s.manager.zeitrasterEintragAdd(eintrag);

		assertSame(s.lehrer, s.manager.lehrerGetByIdOrNull(s.lehrer.id));
		assertSame(s.raum, s.manager.raumGetByIdOrNull(s.raum.id));
		assertSame(raumgruppe, s.manager.raumgruppeGetByIdOrNull(raumgruppe.id));
		assertSame(s.stundentafel, s.manager.stundentafelGetByIdOrNull(s.stundentafel.id));
		assertSame(s.fachA, s.manager.fachGetByIdOrNull(s.fachA.id));
		assertSame(s.zeitraster, s.manager.zeitrasterGetByIdOrNull(s.zeitraster.id));
		assertSame(eintrag, s.manager.zeitrasterEintragGetByIdOrNull(eintrag.id));
		assertSame(s.schueler, s.manager.planungsabschnittSchuelerGetByIdOrNull(214L, 530L));
		assertNull(s.manager.planungsabschnittSchuelerGetByIdOrNull(215L, 530L));
		assertSame(s.gruppe, s.manager.schuelergruppeGetByIdOrNull(s.gruppe.id));
		assertSame(s.klasse, s.manager.klasseGetByIdOrNull(s.klasse.id));
		assertSame(kurs, s.manager.kursGetByIdOrNull(kurs.id));
		assertSame(s.schiene, s.manager.schieneGetByIdOrNull(s.schiene.id));
		assertSame(s.lerngruppe, s.manager.lerngruppeGetByIdOrNull(s.lerngruppe.id));
		assertSame(unterricht, s.manager.unterrichtGetByIdOrNull(unterricht.id));

		final UvLerngruppe patch = createKlassenLerngruppe(839L, 214L, 740L, 101L);
		s.manager.lerngruppePatchAttributes(patch);
		assertSame(patch, s.manager.lerngruppeGetByIdOrNull(839L));
		s.manager.lerngruppeRemove(patch);
		assertNull(s.manager.lerngruppeGetByIdOrNull(839L));
		assertNull(s.manager.unterrichtGetByIdOrNull(1206L));
	}

	@Test
	@DisplayName("Klassen-Patch aktualisiert die Schülergruppenindizes ohne abhängige Daten zu löschen")
	void testKlassePatchUpdatesIndicesWithoutCascade() {
		final UnterrichtScenario s = createUnterrichtMitZuordnungenScenario();
		final UvManager manager = s.basis.manager;
		final UvSchuelergruppe neueGruppe = createSchuelergruppe(635L, 206L, 11L);
		manager.schuelergruppeAdd(neueGruppe);
		final UvKlasse patch = createKlasse(734L, 206L, 635L);

		manager.klassePatchAttributes(patch);

		assertEquals(634L, s.basis.klasse.idSchuelergruppe);
		assertSame(patch, manager.klasseGetByIdOrException(734L));
		assertTrue(manager.lerngruppeGetMengeBySchuelergruppe(s.basis.gruppe).isEmpty());
		assertEquals(List.of(s.basis.lerngruppe), manager.lerngruppeGetMengeBySchuelergruppe(neueGruppe));
		assertEquals(2, manager.unterrichtGetMengeAsList().size());
		assertEquals(2, manager.unterrichtRaumGetMengeAsList().size());
		assertEquals(2, manager.unterrichtLerngruppenlehrerGetMengeAsList().size());
		manager.klasseRemove(patch);
		assertTrue(manager.klasseGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("Kurs-Patch aktualisiert die Schülergruppe und erhält seine Lerngruppe")
	void testKursPatchUpdatesIndicesWithoutCascade() {
		final KlassenLerngruppeScenario s = createKlassenLerngruppeScenario(206L, 634L, 94L, 734L, 834L);
		final UvSchuelergruppe neueGruppe = createSchuelergruppe(635L, 206L, 11L);
		s.manager.schuelergruppeAdd(neueGruppe);
		s.manager.kursAdd(createKurs(901L, 206L, 634L, 94L));
		final UvLerngruppe lerngruppe = createKursLerngruppe(902L, 206L, 901L);
		s.manager.lerngruppeAdd(lerngruppe);
		final UvUnterricht unterricht = createUnterricht(1206L, 206L, null, 902L);
		s.manager.unterrichtAdd(unterricht);
		final UvKurs patch = createKurs(901L, 206L, 635L, 94L);

		s.manager.kursPatchAttributes(patch);

		assertSame(patch, s.manager.kursGetByIdOrException(901L));
		assertEquals(List.of(s.lerngruppe), s.manager.lerngruppeGetMengeBySchuelergruppe(s.gruppe));
		assertEquals(List.of(lerngruppe), s.manager.lerngruppeGetMengeBySchuelergruppe(neueGruppe));
		assertSame(unterricht, s.manager.unterrichtGetByIdOrException(1206L));
		s.manager.kursRemove(patch);
		assertTrue(s.manager.kursGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("Schienen-Patch entfernt die alte Nummer aus dem Index und erhält Lerngruppenzuordnungen")
	void testSchienePatchUpdatesNumberIndex() {
		final GrundstrukturScenario s = createGrundstrukturScenario();
		final UvSchiene patch = createSchiene(s.schiene.id, s.planungsabschnitt.id, 9);

		s.manager.schienePatchAttributes(patch);

		assertEquals(6, s.schiene.nummer);
		assertNull(s.manager.schieneGetByPlanungsabschnittAndNummer(s.planungsabschnitt, 6));
		assertSame(patch, s.manager.schieneGetByPlanungsabschnittAndNummer(s.planungsabschnitt, 9));
		assertEquals(List.of(s.lerngruppenSchiene), s.manager.lerngruppenSchieneGetMengeBySchiene(patch));
		assertEquals(1, s.manager.schieneGetMengeAsList().size());
	}

	@Test
	@DisplayName("Lerngruppen-Patch aktualisiert Klasse und Schülergruppe ohne Unterrichtsverlust")
	void testLerngruppePatchUpdatesIndicesWithoutCascade() {
		final UnterrichtScenario s = createUnterrichtMitZuordnungenScenario();
		final UvManager manager = s.basis.manager;
		final UvSchuelergruppe neueGruppe = createSchuelergruppe(635L, 206L, 11L);
		manager.schuelergruppeAdd(neueGruppe);
		final UvKlasse neueKlasse = createKlasse(735L, 206L, 635L);
		manager.klasseAdd(neueKlasse);
		final UvLerngruppe patch = createKlassenLerngruppe(834L, 206L, 735L, 94L);

		manager.lerngruppePatchAttributes(patch);

		assertSame(neueKlasse, manager.klasseGetByLerngruppe(patch));
		assertTrue(manager.lerngruppeGetMengeBySchuelergruppe(s.basis.gruppe).isEmpty());
		assertEquals(List.of(patch), manager.lerngruppeGetMengeBySchuelergruppe(neueGruppe));
		manager.klasseRemove(s.basis.klasse);
		assertSame(patch, manager.lerngruppeGetByIdOrException(834L));
		assertEquals(2, manager.unterrichtGetMengeAsList().size());
		assertEquals(2, manager.unterrichtRaumGetMengeAsList().size());
		assertEquals(2, manager.unterrichtLerngruppenlehrerGetMengeAsList().size());
	}

	@Test
	@DisplayName("Unterricht-Patch entfernt den alten Zeitschlüssel und erhält Raum- und Lehrerzuordnungen")
	void testUnterrichtPatchUsesOldTimeKey() {
		final UnterrichtScenario s = createUnterrichtMitZuordnungenScenario();
		final UvManager manager = s.basis.manager;
		manager.zeitrasterAdd(createZeitraster(1501L, "Raster"));
		manager.zeitrasterEintragAdd(createZeitrasterEintrag(1502L, 1501L));
		final UvUnterricht patch = createUnterricht(1204L, 206L, 1502L, 834L);

		manager.unterrichtAllPatchAttributes(List.of(patch));

		assertNull(s.unterrichtA.idZeitrasterEintrag);
		assertSame(patch, manager.unterrichtGetByIdOrException(1204L));
		assertEquals(2, manager.unterrichtGetMengeByLerngruppe(206L, 834L).size());
		assertEquals(2, manager.unterrichtRaumGetMengeAsList().size());
		assertEquals(2, manager.unterrichtLerngruppenlehrerGetMengeAsList().size());
		manager.unterrichtRemove(patch);
		assertEquals(List.of(s.unterrichtB), manager.unterrichtGetMengeAsList());
	}

	@Test
	@DisplayName("Lehrerzuordnungs-Patches verwenden alte Schlüssel und erhalten Unterrichtszuordnungen")
	void testLehrerZuordnungPatchUsesOldKeys() {
		final UnterrichtScenario s = createUnterrichtMitZuordnungenScenario();
		final UvManager manager = s.basis.manager;
		manager.lehrerAdd(createLehrer(67L, "BBB"));
		manager.klassenLehrerAdd(createKlassenLehrer(1301L, 206L, 734L, 66L));
		final UvKlassenLehrer klassenLehrer = createKlassenLehrer(1301L, 206L, 734L, 67L);
		final UvLerngruppenLehrer lerngruppenLehrer = createLerngruppenLehrer(1107L, 206L, 834L, 67L);

		manager.klassenLehrerAllPatchAttributes(List.of(klassenLehrer));
		manager.lerngruppenLehrerAllPatchAttributes(List.of(lerngruppenLehrer));

		assertThrows(DeveloperNotificationException.class, () -> manager.klassenLehrerGetByIdOrException(206L, 734L, 66L));
		assertThrows(DeveloperNotificationException.class, () -> manager.lerngruppenLehrerGetByIdOrException(206L, 834L, 66L));
		assertSame(klassenLehrer, manager.klassenLehrerGetByIdOrException(206L, 734L, 67L));
		assertSame(lerngruppenLehrer, manager.lerngruppenLehrerGetByIdOrException(206L, 834L, 67L));
		assertEquals(2, manager.unterrichtLerngruppenlehrerGetMengeAsList().size());
	}

	@Test
	@DisplayName("Schüler-Patch verwendet die alte Klasse und erhält Schülergruppenmitgliedschaften")
	void testSchuelerPatchPreservesMemberships() {
		final GrundstrukturScenario s = createGrundstrukturScenario();
		final UvPlanungsabschnittSchueler patch = createPlanungsabschnittSchueler(214L, 530L, 11L, null);

		s.manager.planungsabschnittSchuelerAllPatchAttributes(List.of(patch));

		assertEquals(1, s.manager.planungsabschnittSchuelerGetMengeAsList().size());
		assertNull(s.manager.planungsabschnittSchuelerGetByIdOrException(214L, 530L).idKlasse);
		assertSame(patch, s.manager.planungsabschnittSchuelerGetByIdOrException(214L, 530L));
		assertEquals(List.of(s.gruppenSchueler), s.manager.schuelergruppeSchuelerGetMengeAsList());
		assertEquals(List.of(patch), s.manager.planungsabschnittSchuelerGetMengeBySchuelergruppe(s.gruppe));
	}

	@Test
	@DisplayName("Grunddaten-Patches erhalten abhängige Daten")
	void testGrunddatenPatchPreservesDependencies() {
		final GrundstrukturScenario s = createGrundstrukturScenario();
		final UvRaumgruppe raumgruppe = createRaumgruppe(69L, "Gruppe");
		s.manager.raumgruppeAdd(raumgruppe);
		final UvRaum raum = createRaum(69L, "R209", 69L);
		s.manager.raumAdd(raum);
		final LehrerUnterrichtsfach fach = createLehrerUnterrichtsfach(100L, 70L, 21L, false);
		s.manager.lehrerUnterrichtsfachAdd(fach);

		s.manager.lehrerAllPatchAttributes(List.of(createLehrer(70L, "BBB")));
		s.manager.raumgruppeAllPatchAttributes(List.of(createRaumgruppe(69L, "Neue Gruppe")));
		s.manager.stundentafelAllPatchAttributes(List.of(createStundentafel(215L, 11L)));
		final UvSchuelergruppe gruppe = createSchuelergruppe(639L, 214L, 11L);
		s.manager.schuelergruppePatchAttributes(gruppe);

		assertEquals(List.of(s.planungsabschnittLehrer), s.manager.planungsabschnittLehrerGetMengeByPlanungsabschnitt(s.planungsabschnitt));
		assertEquals(List.of(fach), s.manager.lehrerUnterrichtsfachGetMengeAsList());
		assertEquals(List.of(raum), s.manager.raumGetMengeByRaumgruppe(raumgruppe));
		assertEquals(List.of(s.stundentafelFach), s.manager.stundentafelFachGetMengeAsList());
		assertSame(gruppe, s.manager.schuelergruppeGetByKlasse(s.klasse));
		assertEquals(List.of(s.gruppenSchueler), s.manager.schuelergruppeSchuelerGetMengeAsList());
	}

	@Test
	@DisplayName("Stundentafelfach-Patch verwendet den alten Abschnitts- und Fachschlüssel")
	void testStundentafelFachPatchUsesOldKeys() {
		final GrundstrukturScenario s = createGrundstrukturScenario();
		final UvStundentafelFach patch = createStundentafelFach(216L, 215L, 2, 102L);

		s.manager.stundentafelFachAllPatchAttributes(List.of(patch));

		assertSame(patch, s.manager.stundentafelFachGetByIdOrException(216L));
		assertEquals(List.of(patch), s.manager.stundentafelFachGetMengeAsList());
		assertEquals(1, s.stundentafelFach.abschnitt);
		s.manager.stundentafelFachRemove(patch);
		assertTrue(s.manager.stundentafelFachGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("Planungsabschnitt-Patch ersetzt das DTO und erhält abhängige Daten")
	void testPlanungsabschnittPatchReplacesDtoWithoutCascade() {
		final GrundstrukturScenario s = createGrundstrukturScenario();
		final UvPlanungsabschnitt patch = createPlanungsabschnitt(214L);
		patch.beschreibung = "Aktualisiert";
		patch.gueltigVon = "2025-03-01";

		s.manager.planungsabschnittPatchAttributes(patch);

		assertSame(patch, s.manager.planungsabschnittGetByIdOrException(214L));
		assertNull(s.planungsabschnitt.beschreibung);
		assertEquals("2025-02-01", s.planungsabschnitt.gueltigVon);
		assertEquals("Aktualisiert", s.manager.planungsabschnittGetByIdOrException(214L).beschreibung);
		assertEquals("2025-03-01", s.manager.planungsabschnittGetByIdOrException(214L).gueltigVon);
		assertEquals(List.of(s.klasse), s.manager.klasseGetMengeAsList());
		assertEquals(List.of(s.lerngruppe), s.manager.lerngruppeGetMengeAsList());
	}

	@Test
	@DisplayName("Unterricht-Bulk-, Patch- und Remove-Varianten arbeiten konsistent")
	void testUnterrichtBulkPatchAndRemove() {
		final UnterrichtScenario s = createUnterrichtScenario(206L, 634L, 94L, 734L, 834L, 1204L, 1205L);

		assertEquals(2, s.basis.manager.unterrichtGetMengeAsList().size());

		final UvUnterricht unterrichtAPatch = createUnterricht(1204L, 206L, null, 834L);
		s.basis.manager.unterrichtAllPatchAttributes(List.of(unterrichtAPatch));
		assertSame(unterrichtAPatch, s.basis.manager.unterrichtGetByIdOrException(1204L));

		s.basis.manager.unterrichtRemoveById(1205L);
		s.basis.manager.unterrichtRemoveAll(List.of(unterrichtAPatch));

		assertTrue(s.basis.manager.unterrichtGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("UnterrichtRaum-Bulk- und Remove-Varianten arbeiten konsistent")
	void testUnterrichtRaumBulkAndRemove() {
		final UnterrichtScenario s = createUnterrichtScenario(206L, 634L, 94L, 734L, 834L, 1204L, 1205L);
		final UvRaum raumA = createRaum(65L, "R205", null);
		final UvRaum raumB = createRaum(66L, "R206", null);
		final UvUnterrichtRaum urA = createUnterrichtRaum(206L, 1204L, 65L);
		final UvUnterrichtRaum urB = createUnterrichtRaum(206L, 1205L, 66L);
		s.basis.manager.raumAddAll(List.of(raumA, raumB));

		s.basis.manager.unterrichtRaumAddAll(List.of(urA, urB));

		assertEquals(2, s.basis.manager.unterrichtRaumGetMengeAsList().size());

		s.basis.manager.unterrichtRaumRemoveById(206L, 1205L, 66L);
		s.basis.manager.unterrichtRaumRemoveAll(List.of(urA));

		assertTrue(s.basis.manager.unterrichtRaumGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("UnterrichtLerngruppenlehrer-Bulk- und Remove-Varianten arbeiten konsistent")
	void testUnterrichtLerngruppenlehrerBulkAndRemove() {
		final UnterrichtScenario s = createUnterrichtScenario(206L, 634L, 94L, 734L, 834L, 1204L, 1205L);
		final UvLehrer lehrer = createLehrer(66L, "AAA");
		final UvLerngruppenLehrer lgLehrer = createLerngruppenLehrer(1107L, 206L, 834L, 66L);
		final UvUnterrichtLerngruppenlehrer ulgA = createUnterrichtLerngruppenlehrer(206L, 1204L, 1107L);
		final UvUnterrichtLerngruppenlehrer ulgB = createUnterrichtLerngruppenlehrer(206L, 1205L, 1107L);
		s.basis.manager.lehrerAdd(lehrer);
		s.basis.manager.lerngruppenLehrerAdd(lgLehrer);

		s.basis.manager.unterrichtLerngruppenlehrerAddAll(List.of(ulgA, ulgB));

		assertEquals(2, s.basis.manager.unterrichtLerngruppenlehrerGetMengeAsList().size());

		s.basis.manager.unterrichtLerngruppenlehrerRemoveById(206L, 1205L, 1107L);
		s.basis.manager.unterrichtLerngruppenlehrerRemoveAll(List.of(ulgA));

		assertTrue(s.basis.manager.unterrichtLerngruppenlehrerGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("Null-Pfade der Lerngruppen-Helfer liefern konsistente Rückgaben")
	void testLerngruppenHelperNullPaths() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvLerngruppe lerngruppeOhneBezug = new UvLerngruppe();
		lerngruppeOhneBezug.id = 901L;
		lerngruppeOhneBezug.idPlanungsabschnitt = 207L;

		assertNull(manager.kursGetByLerngruppe(lerngruppeOhneBezug));
		assertNull(manager.klasseGetByLerngruppe(lerngruppeOhneBezug));
		assertNull(manager.schuelergruppeGetByLerngruppe(lerngruppeOhneBezug));
		assertNull(manager.fachGetByLerngruppe(lerngruppeOhneBezug));
		assertTrue(manager.klasseGetMengeByLerngruppe(lerngruppeOhneBezug).isEmpty());
		assertTrue(manager.jahrgangsdatenGetMengeByLerngruppe(lerngruppeOhneBezug).isEmpty());
	}

	@Test
	@DisplayName("planungsabschnittZeitrasterRemoveById und RemoveAll entfernen eindeutige Zuordnungen konsistent")
	void testPlanungsabschnittZeitrasterRemoveVariants() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(208L);
		final UvZeitraster rasterA = createZeitraster(1401L, "A");
		final UvZeitraster rasterB = createZeitraster(1402L, "B");
		final UvPlanungsabschnittZeitraster zuordnungA = createPlanungsabschnittZeitraster(208L, 1401L);
		final UvPlanungsabschnittZeitraster zuordnungB = createPlanungsabschnittZeitraster(208L, 1402L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.zeitrasterAddAll(List.of(rasterA, rasterB));
		manager.planungsabschnittZeitrasterAddAll(List.of(zuordnungA, zuordnungB));

		manager.planungsabschnittZeitrasterRemoveById(208L, 1401L);
		manager.planungsabschnittZeitrasterRemoveAll(List.of(zuordnungB));

		assertTrue(manager.planungsabschnittZeitrasterGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("planungsabschnittZeitrasterRemoveAllByPlanungsabschnitt leert alle Folgegetter und Mitgliedschaftsprüfungen")
	void testPlanungsabschnittZeitrasterRemoveAllByPlanungsabschnittClearsDependentViews() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(360L);
		final UvZeitraster rasterA = createZeitraster(1601L, "A");
		final UvZeitraster rasterB = createZeitraster(1602L, "B");
		final UvPlanungsabschnittZeitraster paRasterA = createPlanungsabschnittZeitraster(360L, 1601L);
		final UvPlanungsabschnittZeitraster paRasterB = createPlanungsabschnittZeitraster(360L, 1602L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.zeitrasterAddAll(List.of(rasterA, rasterB));
		manager.planungsabschnittZeitrasterAddAll(List.of(paRasterA, paRasterB));

		manager.planungsabschnittZeitrasterRemoveAllByPlanungsabschnitt(360L);

		assertTrue(manager.planungsabschnittZeitrasterGetMengeAsList().isEmpty());
		assertTrue(manager.planungsabschnittZeitrasterGetMengeByPlanungsabschnitt(planungsabschnitt.id).isEmpty());
		assertFalse(manager.zeitrasterIsInPlanungsabschnitt(rasterA, planungsabschnitt));
		assertFalse(manager.zeitrasterIsInPlanungsabschnitt(rasterB, planungsabschnitt));
	}

	@Test
	@DisplayName("klassenLehrerAddAll, GetMengeAsList und Remove-Varianten arbeiten konsistent")
	void testKlassenLehrerBulkRemoveVariants() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(209L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(635L, 209L, 11L);
		final UvKlasse klasse = createKlasse(735L, 209L, 635L);
		final UvLehrer lehrerA = createLehrer(67L, "AAA");
		final UvLehrer lehrerB = createLehrer(68L, "BBB");
		final UvKlassenLehrer zuordnungA = createKlassenLehrer(1304L, 209L, 735L, 67L);
		final UvKlassenLehrer zuordnungB = createKlassenLehrer(1305L, 209L, 735L, 68L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);
		manager.klasseAdd(klasse);
		manager.lehrerAddAll(List.of(lehrerA, lehrerB));

		manager.klassenLehrerAddAll(List.of(zuordnungA, zuordnungB));
		assertEquals(2, manager.klassenLehrerGetMengeAsList().size());
		assertTrue(manager.klassenLehrerGetMengeAsList().contains(zuordnungA));
		assertTrue(manager.klassenLehrerGetMengeAsList().contains(zuordnungB));

		manager.klassenLehrerRemoveById(209L, 735L, 67L);
		manager.klassenLehrerRemoveAll(List.of(zuordnungB));
		assertTrue(manager.klassenLehrerGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("lerngruppeRemoveAll entfernt mehrere Lerngruppen in einem Schritt")
	void testLerngruppeRemoveAll() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(210L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(636L, 210L, 11L);
		final UvFach fach = createUvFach(95L, 21L);
		final UvKlasse klasseA = createKlasse(736L, 210L, 636L);
		final UvKlasse klasseB = createKlasse(737L, 210L, 636L);
		final UvLerngruppe lerngruppeA = createKlassenLerngruppe(835L, 210L, 736L, 95L);
		final UvLerngruppe lerngruppeB = createKlassenLerngruppe(836L, 210L, 737L, 95L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);
		manager.fachAdd(fach);
		manager.klasseAddAll(List.of(klasseA, klasseB));
		manager.lerngruppeAddAll(List.of(lerngruppeA, lerngruppeB));

		manager.lerngruppeRemoveAll(List.of(lerngruppeA, lerngruppeB));

		assertTrue(manager.lerngruppeGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("Unterricht-, Raum- und Lehrerzuordnungen lassen sich auch über die Einzel-Remove-Methoden entfernen")
	void testUnterrichtSingleRemoveVariants() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(211L);
		final UvSchuelergruppe gruppe = createSchuelergruppe(637L, 211L, 11L);
		final UvFach fach = createUvFach(96L, 21L);
		final UvKlasse klasse = createKlasse(738L, 211L, 637L);
		final UvLerngruppe lerngruppe = createKlassenLerngruppe(837L, 211L, 738L, 96L);
		final UvLehrer lehrer = createLehrer(69L, "AAA");
		final UvLerngruppenLehrer lgLehrer = createLerngruppenLehrer(1108L, 211L, 837L, 69L);
		final UvUnterricht unterricht = createUnterricht(1206L, 211L, null, 837L);
		final UvRaum raum = createRaum(67L, "R207", null);
		final UvUnterrichtRaum unterrichtRaum = createUnterrichtRaum(211L, 1206L, 67L);
		final UvUnterrichtLerngruppenlehrer ulg = createUnterrichtLerngruppenlehrer(211L, 1206L, 1108L);
		manager.planungsabschnittAdd(planungsabschnitt);
		manager.schuelergruppeAdd(gruppe);
		manager.fachAdd(fach);
		manager.klasseAdd(klasse);
		manager.lerngruppeAdd(lerngruppe);
		manager.lehrerAdd(lehrer);
		manager.lerngruppenLehrerAdd(lgLehrer);
		manager.unterrichtAdd(unterricht);
		manager.raumAdd(raum);
		manager.unterrichtRaumAdd(unterrichtRaum);
		manager.unterrichtLerngruppenlehrerAdd(ulg);

		manager.unterrichtRaumRemove(unterrichtRaum);
		manager.unterrichtLerngruppenlehrerRemove(ulg);
		manager.unterrichtRemove(unterricht);

		assertTrue(manager.unterrichtRaumGetMengeAsList().isEmpty());
		assertTrue(manager.unterrichtLerngruppenlehrerGetMengeAsList().isEmpty());
		assertTrue(manager.unterrichtGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("schieneGetByPlanungsabschnittAndNummer liefert bei unbekannter Nummer null")
	void testSchieneGetByPlanungsabschnittAndNummerReturnsNull() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvPlanungsabschnitt planungsabschnitt = createPlanungsabschnitt(212L);
		manager.planungsabschnittAdd(planungsabschnitt);

		assertNull(manager.schieneGetByPlanungsabschnittAndNummer(planungsabschnitt, 99));
	}

	@Test
	@DisplayName("Gültigkeitskonflikt-Helper liefern null, wenn kein Konflikt vorliegt")
	void testGueltigkeitskonfliktHelperReturnNullWithoutConflict() {
		final UvManager manager = createManagerWithMinimalBasis();
		final UvFach fachA = createUvFach(97L, 21L);
		fachA.gueltigVon = "2025-01-01";
		fachA.gueltigBis = "2025-06-30";
		final UvFach fachB = createUvFach(98L, 21L);
		fachB.gueltigVon = "2025-07-01";
		fachB.gueltigBis = null;
		manager.fachAddAll(List.of(fachA, fachB));

		assertNull(manager.fachGetByGueltigkeitskonfliktMitFach(fachA));
		assertNull(manager.fachGetByGueltigkeitskonfliktMitNeueGueltigkeit(fachA, "2024-01-01", "2024-12-31"));
	}

	@Test
	@DisplayName("lehrerGetByPlanungsabschnittLehrer liefert den referenzierten Lehrer")
	void testLehrerGetByPlanungsabschnittLehrer() {
		final GrundstrukturScenario s = createGrundstrukturScenario();
		assertSame(s.lehrer, s.manager.lehrerGetByPlanungsabschnittLehrer(s.planungsabschnittLehrer));
	}

	@Test
	@DisplayName("raumRemoveById entfernt den Raum")
	void testRaumRemoveByIdRemainingCase() {
		final GrundstrukturScenario s = createGrundstrukturScenario();
		s.manager.raumRemoveById(68L);
		assertThrows(DeveloperNotificationException.class, () -> s.manager.raumGetByIdOrException(68L));
	}

	@Test
	@DisplayName("zeitrasterRemoveById entfernt das Zeitraster")
	void testZeitrasterRemoveByIdRemainingCase() {
		final GrundstrukturScenario s = createGrundstrukturScenario();
		s.manager.zeitrasterRemoveById(1501L);
		assertThrows(DeveloperNotificationException.class, () -> s.manager.zeitrasterGetByIdOrException(1501L));
	}

	@Test
	@DisplayName("stundentafelFachRemoveById entfernt das Stundentafelfach")
	void testStundentafelFachRemoveByIdRemainingCase() {
		final GrundstrukturScenario s = createGrundstrukturScenario();
		s.manager.stundentafelFachRemoveById(216L);
		assertTrue(s.manager.stundentafelFachGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("fachRemoveById entfernt das Fach")
	void testFachRemoveByIdRemainingCase() {
		final GrundstrukturScenario s = createGrundstrukturScenario();
		s.manager.fachRemoveById(102L);
		assertThrows(DeveloperNotificationException.class, () -> s.manager.fachGetByIdOrException(102L));
	}

	@Test
	@DisplayName("klassenLehrerRemove entfernt die Zuordnung")
	void testKlassenLehrerRemoveRemainingCase() {
		final GrundstrukturScenario s = createGrundstrukturScenario();
		s.manager.klassenLehrerAdd(createKlassenLehrer(1306L, 214L, 740L, 70L));
		s.manager.klassenLehrerRemove(s.manager.klassenLehrerGetByIdOrException(214L, 740L, 70L));
		assertTrue(s.manager.klassenLehrerGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("lerngruppenLehrerRemove entfernt die Lehrerzuordnung")
	void testLerngruppenLehrerRemoveRemainingCase() {
		final GrundstrukturScenario s = createGrundstrukturScenario();
		s.manager.lerngruppenLehrerRemove(s.lerngruppenLehrer);
		assertTrue(s.manager.lerngruppenLehrerGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("lerngruppenSchieneRemove entfernt die Schienenzuordnung")
	void testLerngruppenSchieneRemoveRemainingCase() {
		final GrundstrukturScenario s = createGrundstrukturScenario();
		s.manager.lerngruppenSchieneRemove(s.lerngruppenSchiene);
		assertTrue(s.manager.lerngruppenSchieneGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("schieneRemove entfernt die Schiene")
	void testSchieneRemoveRemainingCase() {
		final GrundstrukturScenario s = createGrundstrukturScenario();
		s.manager.lerngruppenSchieneRemove(s.lerngruppenSchiene);
		s.manager.schieneRemove(s.schiene);
		assertTrue(s.manager.schieneGetMengeAsList().isEmpty());
	}

	@Test
	@DisplayName("planungsabschnittSchuelerGetBySchuelergruppeSchueler liefert den referenzierten Schüler")
	void testPlanungsabschnittSchuelerGetBySchuelergruppeSchuelerRemainingCase() {
		final GrundstrukturScenario s = createGrundstrukturScenario();
		assertSame(s.schueler, s.manager.planungsabschnittSchuelerGetBySchuelergruppeSchueler(s.gruppenSchueler));
	}

	@Test
	@DisplayName("klasseRemoveAll entfernt Klassen nach vorherigem Entfernen abhängiger Lerngruppen")
	void testKlasseRemoveAllRemainingCase() {
		final GrundstrukturScenario s = createGrundstrukturScenario();
		s.manager.lerngruppeRemove(s.lerngruppe);
		s.manager.klasseRemoveAll(List.of(s.klasse));
		assertTrue(s.manager.lerngruppeGetMengeAsList().isEmpty());
		assertTrue(s.manager.klasseGetMengeAsList().isEmpty());
	}
}
