package de.svws_nrw.core.utils.uv;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.data.kurse.KursDaten;
import de.svws_nrw.asd.data.schueler.Schueler;
import de.svws_nrw.core.data.fach.FachDaten;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.data.uv.UvFach;
import de.svws_nrw.core.data.uv.UvPlanungsabschnitt;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittSchueler;
import de.svws_nrw.core.data.uv.UvSchuelergruppe;

class TestUvKursImportManager {

	@Test
	void gruppennameEnthaeltSortierteEindeutigeJahrgaengeDerImportiertenSchueler() {
		final UvManager manager = createManager();
		final UvKursImportManager importer = new UvKursImportManager(List.of(createKurs(1, 501, 502, 503, 999)),
				manager.planungsabschnittGetByIdOrException(1), manager);
		final var daten = importer.createImportDaten();
		assertEquals("Q1/Q2 BI-GK1", daten.schuelergruppen.getFirst().bezeichnung);
		assertEquals(3, daten.schuelergruppenschueler.size());
		assertEquals(List.of("Schüler-ID 999"), importer.getFehlendeSchueler());
	}

	@Test
	void gruppennamenSindGegenBestandUndInnerhalbDesImportsEindeutigUndWiederholbar() {
		final UvManager manager = createManager();
		addGruppe(manager, 10, "Q1/Q2 BI-GK1");
		addGruppe(manager, 11, "Q1/Q2 BI-GK1-2");
		addGruppe(manager, 12, "Q1/Q2 BI-GK1-4");
		final UvKursImportManager importer = new UvKursImportManager(List.of(createKurs(1, 501, 502), createKurs(2, 502, 501)),
				manager.planungsabschnittGetByIdOrException(1), manager);
		for (int i = 0; i < 2; i++) {
			final var daten = importer.createImportDaten();
			assertEquals(List.of("Q1/Q2 BI-GK1-3", "Q1/Q2 BI-GK1-5"),
					daten.schuelergruppen.stream().map(gruppe -> gruppe.bezeichnung).toList());
		}
	}

	@Test
	void einzelnerJahrgangUndLeereKurseErhaltenPassendeNamen() {
		final UvManager manager = createManager();
		final var daten = new UvKursImportManager(List.of(createKurs(1, 502), createKurs(2), createKurs(3, 999)),
				manager.planungsabschnittGetByIdOrException(1), manager).createImportDaten();
		assertEquals(List.of("Q1 BI-GK1", "BI-GK1", "BI-GK1-2"),
				daten.schuelergruppen.stream().map(gruppe -> gruppe.bezeichnung).toList());
	}

	private static UvManager createManager() {
		final JahrgangsDaten q1 = new JahrgangsDaten();
		q1.id = 12;
		q1.kuerzel = "Q1";
		q1.sortierung = 11;
		final JahrgangsDaten q2 = new JahrgangsDaten();
		q2.id = 11;
		q2.kuerzel = "Q2";
		q2.sortierung = 12;
		final FachDaten fach = new FachDaten();
		fach.id = 21;
		fach.kuerzel = "BI";
		final UvManager manager = new UvManager(List.of(q2, q1), List.of(fach));
		final UvPlanungsabschnitt plan = new UvPlanungsabschnitt();
		plan.id = 1;
		plan.gueltigVon = "2025-02-01";
		plan.gueltigBis = "2025-07-31";
		manager.planungsabschnittAdd(plan);
		final UvFach uvFach = new UvFach();
		uvFach.id = 31;
		uvFach.idFach = 21;
		uvFach.gueltigVon = "2025-01-01";
		manager.fachAdd(uvFach);
		for (long id = 501; id <= 503; id++) {
			final UvPlanungsabschnittSchueler schueler = new UvPlanungsabschnittSchueler();
			schueler.idPlanungsabschnitt = 1;
			schueler.idSchueler = id;
			schueler.idJahrgang = (id == 502) ? 12 : 11;
			manager.planungsabschnittSchuelerAdd(schueler);
		}
		return manager;
	}

	private static KursDaten createKurs(final long id, final long... schuelerIds) {
		final KursDaten kurs = new KursDaten();
		kurs.id = id;
		kurs.idFach = 21;
		kurs.idSchuljahresabschnitt = 1;
		kurs.kuerzel = "BI-GK1";
		kurs.kursartAllg = "GK";
		for (final long schuelerId : schuelerIds) {
			final Schueler schueler = new Schueler();
			schueler.id = schuelerId;
			kurs.schueler.add(schueler);
		}
		return kurs;
	}

	private static void addGruppe(final UvManager manager, final long id, final String bezeichnung) {
		final UvSchuelergruppe gruppe = new UvSchuelergruppe();
		gruppe.id = id;
		gruppe.idPlanungsabschnitt = 1;
		gruppe.bezeichnung = bezeichnung;
		manager.schuelergruppeAdd(gruppe);
	}
}
