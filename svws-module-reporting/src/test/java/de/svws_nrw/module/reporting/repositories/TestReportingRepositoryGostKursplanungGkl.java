package de.svws_nrw.module.reporting.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import de.svws_nrw.asd.data.schueler.Schueler;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungSchiene;
import de.svws_nrw.core.data.gost.GostBlockungsdaten;
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKurs;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisSchiene;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.DTOUtils;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.data.gost.DataGostBlockungsdaten;
import de.svws_nrw.data.gost.DataGostBlockungsergebnisse;
import de.svws_nrw.module.reporting.filterung.ReportingFilterService;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.gost.kursplanung.ReportingSchuelerGostKursplanungKursbelegung;

/**
 * Prüft die GKL-Kennzeichnung der Kursbelegungen beim Aufbau der Kursplanung. Der Aufbau fragt das GOSt-Repository für jede Belegung mit Abiturjahrgang,
 * Halbjahr und Fach der Blockung ab und übernimmt dessen Antwort, gleich welche Kursart, Schriftlichkeit oder Gültigkeit die Belegung hat.
 */
@DisplayName("Die GKL-Kennzeichnung der Kursbelegungen in der Kursplanung")
class TestReportingRepositoryGostKursplanungGkl {

	/** Die ID des Blockungsergebnisses, das die Reportparameter benennen. */
	private static final long ID_BLOCKUNGSERGEBNIS = 3L;

	/** Die ID der Blockung, die das Blockungsergebnis nennt. */
	private static final long ID_BLOCKUNG = 7L;

	/** Die ID der einzigen Schiene der Blockung. */
	private static final long ID_SCHIENE = 1L;

	/** Die ID des Faches mit dem Grundkurs. */
	private static final long ID_FACH_GK = 1L;

	/** Die ID des Faches mit dem Leistungskurs. */
	private static final long ID_FACH_LK = 2L;

	/** Die ID des Grundkurses. */
	private static final long ID_KURS_GK = 11L;

	/** Die ID des Leistungskurses. */
	private static final long ID_KURS_LK = 12L;

	/** Der Schüler mit schriftlichem Grundkurs. */
	private static final long ID_SCHRIFTLICH = 100L;

	/** Der Schüler mit mündlichem Grundkurs. */
	private static final long ID_MUENDLICH = 101L;

	/** Der Schüler mit schriftlichem Leistungskurs. */
	private static final long ID_LK = 102L;

	/** Der Schüler, der dem Grundkurs ohne passende Fachwahl zugeordnet ist, etwa über eine gespeicherte Kurszuordnung. */
	private static final long ID_UNGUELTIG = 103L;

	/** Das Abiturjahr der Blockung. */
	private static final int ABITURJAHR = 2030;

	/** Der gemockte Context, über den das Repository seine Umgebung erreicht. */
	private ReportingContext reportingContext;

	/** Das gemockte GOSt-Repository; es meldet für jede Anfrage einen GKL. */
	private ReportingRepositoryGost repositoryGost;

	/** Die Kursbelegungen je Schüler-ID, an die das Repository seine Einträge hängt. */
	private Map<Long, List<ReportingSchuelerGostKursplanungKursbelegung>> kursbelegungen;


	/** Geschlecht und Schülerstatus der Blockungsdaten stammen aus den Core-Types und verlangen deren Initialisierung. */
	@BeforeAll
	static void setUpCoreTypes() {
		ASDCoreTypeUtils.initAll();
	}


	@BeforeEach
	void setUp() {
		reportingContext = mock(ReportingContext.class);
		when(reportingContext.logger()).thenReturn(new Logger());
		final ReportingParameterTypisiert reportingParameter = mock(ReportingParameterTypisiert.class);
		when(reportingParameter.idHauptdatenObjekt()).thenReturn(ID_BLOCKUNGSERGEBNIS);
		when(reportingContext.reportingParameter()).thenReturn(reportingParameter);
		when(reportingContext.filterService()).thenReturn(mock(ReportingFilterService.class));
		when(reportingContext.repositorySchule()).thenReturn(mock(ReportingRepositorySchule.class, RETURNS_DEEP_STUBS));
		when(reportingContext.repositoryLehrer()).thenReturn(mock(ReportingRepositoryLehrer.class));

		repositoryGost = mock(ReportingRepositoryGost.class);
		when(repositoryGost.hatGkl(anyLong(), anyInt(), any(), anyLong())).thenReturn(true);
		when(reportingContext.repositoryGost()).thenReturn(repositoryGost);

		kursbelegungen = new HashMap<>();
		final List<ReportingSchueler> schueler = new ArrayList<>();
		for (final long id : List.of(ID_SCHRIFTLICH, ID_MUENDLICH, ID_LK, ID_UNGUELTIG)) {
			final List<ReportingSchuelerGostKursplanungKursbelegung> belegungen = new ArrayList<>();
			kursbelegungen.put(id, belegungen);
			final ReportingSchueler s = mock(ReportingSchueler.class);
			when(s.id()).thenReturn(id);
			when(s.gostKursplanungKursbelegungen()).thenReturn(belegungen);
			schueler.add(s);
		}
		final ReportingRepositorySchueler repositorySchueler = mock(ReportingRepositorySchueler.class);
		when(repositorySchueler.schueler(any())).thenReturn(schueler);
		when(reportingContext.repositorySchueler()).thenReturn(repositorySchueler);
	}


	/**
	 * Erzeugt ein Fach der gymnasialen Oberstufe.
	 *
	 * @param id      Die ID des Faches.
	 * @param kuerzel Das Kürzel des Faches.
	 *
	 * @return Das Fach.
	 */
	private static GostFach fach(final long id, final String kuerzel) {
		final GostFach fach = new GostFach();
		fach.id = id;
		fach.kuerzel = kuerzel;
		fach.kuerzelAnzeige = kuerzel;
		return fach;
	}

	/**
	 * Erzeugt einen Kurs der Blockung.
	 *
	 * @param id      Die ID des Kurses.
	 * @param idFach  Die ID des Faches.
	 * @param kursart Die Kursart.
	 *
	 * @return Der Kurs.
	 */
	private static GostBlockungKurs kurs(final long id, final long idFach, final GostKursart kursart) {
		final GostBlockungKurs kurs = new GostBlockungKurs();
		kurs.id = id;
		kurs.fach_id = idFach;
		kurs.kursart = kursart.id;
		kurs.nummer = 1;
		return kurs;
	}

	/**
	 * Erzeugt einen Schüler der Blockung.
	 *
	 * @param id Die ID des Schülers.
	 *
	 * @return Der Schüler.
	 */
	private static Schueler schueler(final long id) {
		final Schueler schueler = new Schueler();
		schueler.id = id;
		schueler.nachname = "Muster";
		schueler.vorname = "Schüler " + id;
		schueler.geschlecht = Geschlecht.M.id;
		schueler.status = SchuelerStatus.AKTIV.ordinal();
		return schueler;
	}

	/**
	 * Erzeugt eine Fachwahl.
	 *
	 * @param idSchueler     Die ID des Schülers.
	 * @param idFach         Die ID des Faches.
	 * @param kursart        Die Kursart der Fachwahl.
	 * @param istSchriftlich Gibt an, ob das Fach schriftlich gewählt ist.
	 *
	 * @return Die Fachwahl.
	 */
	private static GostFachwahl fachwahl(final long idSchueler, final long idFach, final GostKursart kursart, final boolean istSchriftlich) {
		final GostFachwahl fachwahl = new GostFachwahl();
		fachwahl.schuelerID = idSchueler;
		fachwahl.fachID = idFach;
		fachwahl.kursartID = kursart.id;
		fachwahl.istSchriftlich = istSchriftlich;
		return fachwahl;
	}

	/**
	 * Baut die Blockungsdaten auf: ein Grundkurs und ein Leistungskurs in einer Schiene, vier Schüler. Der Schüler {@link #ID_UNGUELTIG} hat keine
	 * Fachwahl im Fach des Grundkurses.
	 *
	 * @return Der Blockungsdaten-Manager zu diesen Daten.
	 */
	private static GostBlockungsdatenManager blockungsdatenManager() {
		final GostFaecherManager faecherManager = new GostFaecherManager(GostHalbjahr.Q11.getSchuljahrFromAbiturjahr(ABITURJAHR));
		faecherManager.add(fach(ID_FACH_GK, "D"));
		faecherManager.add(fach(ID_FACH_LK, "E"));

		final GostBlockungsdaten blockungsdaten = new GostBlockungsdaten();
		blockungsdaten.id = ID_BLOCKUNG;
		blockungsdaten.name = "Blockung mit GK und LK";
		blockungsdaten.abijahrgang = ABITURJAHR;
		blockungsdaten.gostHalbjahr = GostHalbjahr.Q11.id;

		final GostBlockungSchiene schiene = new GostBlockungSchiene();
		schiene.id = ID_SCHIENE;
		schiene.nummer = 1;
		schiene.bezeichnung = "Schiene 1";
		blockungsdaten.schienen.add(schiene);

		for (final long id : List.of(ID_SCHRIFTLICH, ID_MUENDLICH, ID_LK, ID_UNGUELTIG)) {
			blockungsdaten.schueler.add(schueler(id));
		}
		blockungsdaten.fachwahlen.add(fachwahl(ID_SCHRIFTLICH, ID_FACH_GK, GostKursart.GK, true));
		blockungsdaten.fachwahlen.add(fachwahl(ID_MUENDLICH, ID_FACH_GK, GostKursart.GK, false));
		blockungsdaten.fachwahlen.add(fachwahl(ID_LK, ID_FACH_LK, GostKursart.LK, true));
		blockungsdaten.fachwahlen.add(fachwahl(ID_UNGUELTIG, ID_FACH_LK, GostKursart.LK, true));

		blockungsdaten.kurse.add(kurs(ID_KURS_GK, ID_FACH_GK, GostKursart.GK));
		blockungsdaten.kurse.add(kurs(ID_KURS_LK, ID_FACH_LK, GostKursart.LK));
		return new GostBlockungsdatenManager(blockungsdaten, faecherManager);
	}

	/**
	 * Baut das Blockungsergebnis auf. Der Grundkurs enthält den schriftlichen, den mündlichen und den ungültig zugeordneten Schüler, der Leistungskurs den
	 * Schüler mit Leistungskurs.
	 *
	 * @return Das Blockungsergebnis.
	 */
	private static GostBlockungsergebnis blockungsergebnis() {
		final GostBlockungsergebnisKurs kursGk = DTOUtils.newGostBlockungsergebnisKurs(ID_KURS_GK, ID_FACH_GK, GostKursart.GK.id, 1);
		kursGk.schueler.addAll(List.of(ID_SCHRIFTLICH, ID_MUENDLICH, ID_UNGUELTIG));
		final GostBlockungsergebnisKurs kursLk = DTOUtils.newGostBlockungsergebnisKurs(ID_KURS_LK, ID_FACH_LK, GostKursart.LK.id, 1);
		kursLk.schueler.add(ID_LK);

		final GostBlockungsergebnisSchiene schiene = DTOUtils.newGostBlockungsergebnisSchiene(ID_SCHIENE);
		schiene.kurse.add(kursGk);
		schiene.kurse.add(kursLk);

		final GostBlockungsergebnis ergebnis = new GostBlockungsergebnis();
		ergebnis.id = ID_BLOCKUNGSERGEBNIS;
		ergebnis.blockungID = ID_BLOCKUNG;
		ergebnis.gostHalbjahr = GostHalbjahr.Q11.id;
		ergebnis.schienen.add(schiene);
		return ergebnis;
	}

	/**
	 * Baut die Kursplanung auf und liefert die einzige Kursbelegung des Schülers.
	 *
	 * @param idSchueler Die ID des Schülers.
	 *
	 * @return Die Kursbelegung des Schülers.
	 */
	private ReportingSchuelerGostKursplanungKursbelegung belegungNachAufbau(final long idSchueler) {
		final ReportingRepositoryGostKursplanung repository = new ReportingRepositoryGostKursplanung(reportingContext);
		try (MockedStatic<DataGostBlockungsergebnisse> dataErgebnisse = mockStatic(DataGostBlockungsergebnisse.class);
				MockedStatic<DataGostBlockungsdaten> dataBlockungsdaten = mockStatic(DataGostBlockungsdaten.class)) {
			dataErgebnisse.when(() -> DataGostBlockungsergebnisse.getErgebnisFromID(any(), anyLong())).thenReturn(blockungsergebnis());
			dataBlockungsdaten.when(() -> DataGostBlockungsdaten.getBlockungsdatenManagerFromDB(any(), eq(ID_BLOCKUNG)))
					.thenReturn(blockungsdatenManager());
			repository.kurse();
		} catch (final Exception e) {
			throw new AssertionError("Der Aufbau der Kursplanung darf nicht scheitern.", e);
		}
		final List<ReportingSchuelerGostKursplanungKursbelegung> belegungen = kursbelegungen.get(idSchueler);
		assertEquals(1, belegungen.size());
		return belegungen.getFirst();
	}


	@Test
	void testEinSchriftlicherGrundkursMitGklZeigtSPlus() {
		final ReportingSchuelerGostKursplanungKursbelegung belegung = belegungNachAufbau(ID_SCHRIFTLICH);

		assertTrue(belegung.hatGkl());
		assertEquals("S+", belegung.kuerzelSchriftlichkeit());
		assertFalse(belegung.hatUnzulaessigeGkl());
		verify(repositoryGost, times(1)).hatGkl(ID_SCHRIFTLICH, ABITURJAHR, GostHalbjahr.Q11, ID_FACH_GK);
	}

	@Test
	void testEinMuendlicherGrundkursMitGklZeigtMPlus() {
		final ReportingSchuelerGostKursplanungKursbelegung belegung = belegungNachAufbau(ID_MUENDLICH);

		assertTrue(belegung.hatGkl());
		assertEquals("M+", belegung.kuerzelSchriftlichkeit());
		assertTrue(belegung.hatUnzulaessigeGkl());
		verify(repositoryGost, times(1)).hatGkl(ID_MUENDLICH, ABITURJAHR, GostHalbjahr.Q11, ID_FACH_GK);
	}

	@Test
	void testEinLeistungskursMitGklZeigtSPlus() {
		final ReportingSchuelerGostKursplanungKursbelegung belegung = belegungNachAufbau(ID_LK);

		assertTrue(belegung.hatGkl());
		assertEquals("S+", belegung.kuerzelSchriftlichkeit());
		assertTrue(belegung.hatUnzulaessigeGkl());
		verify(repositoryGost, times(1)).hatGkl(ID_LK, ABITURJAHR, GostHalbjahr.Q11, ID_FACH_LK);
	}

	@Test
	void testEineUngueltigeZuordnungUebernimmtDieGklAbfrage() {
		final ReportingSchuelerGostKursplanungKursbelegung belegung = belegungNachAufbau(ID_UNGUELTIG);

		assertFalse(belegung.hatGueltigeFachwahl());
		assertTrue(belegung.hatGkl());
		assertTrue(belegung.hatUnzulaessigeGkl());
		verify(repositoryGost, times(1)).hatGkl(ID_UNGUELTIG, ABITURJAHR, GostHalbjahr.Q11, ID_FACH_GK);
	}

	@Test
	void testOhneGklBleibtDasKuerzelOhnePlus() {
		when(repositoryGost.hatGkl(anyLong(), anyInt(), any(), anyLong())).thenReturn(false);

		assertEquals("S", belegungNachAufbau(ID_SCHRIFTLICH).kuerzelSchriftlichkeit());
		final ReportingSchuelerGostKursplanungKursbelegung muendlich = kursbelegungen.get(ID_MUENDLICH).getFirst();
		assertEquals("M", muendlich.kuerzelSchriftlichkeit());
		assertFalse(muendlich.hatUnzulaessigeGkl());
	}

}
