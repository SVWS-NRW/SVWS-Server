package de.svws_nrw.repo.gost;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerFachbelegungen;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerFachbelegungenPK;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerWiederholung;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerWiederholungFachwahlen;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerWiederholungFachwahlenPK;

/**
 * Prüft die Regel, dass die Fachwahlen der Laufbahnplanung bei einem Schüler mit Wiederholung aus die Wiederholung-Tabelle kommen
 * und dorthin geschrieben werden, während die Fachwahlen der aktuellen Planung unverändert bleiben.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests für das Planungs-Repository der Fachwahlen mit Wiederholung")
class GostSchuelerPlanungFachbelegungenRepositoryImplTest {

	private static final long ID_OHNE_WIEDERHOLUNG = 1L;
	private static final long ID_MIT_WIEDERHOLUNG = 2L;
	private static final long ID_FACH = 10L;

	@Mock
	private GostSchuelerFachbelegungenRepository fachbelegungenRepository;

	@Mock
	private GostSchuelerWiederholungFachwahlenRepository wiederholungFachwahlenRepository;

	@Mock
	private GostSchuelerWiederholungRepository wiederholungRepository;

	private GostSchuelerPlanungFachbelegungenRepositoryImpl repository;

	@BeforeEach
	void setUp() {
		repository = new GostSchuelerPlanungFachbelegungenRepositoryImpl(fachbelegungenRepository, wiederholungFachwahlenRepository, wiederholungRepository);
	}

	private void mockWiederholung() {
		when(wiederholungRepository.findById(ID_MIT_WIEDERHOLUNG)).thenReturn(Optional.of(new DTOGostSchuelerWiederholung(ID_MIT_WIEDERHOLUNG, 2029, GostHalbjahr.Q11)));
	}

	private static DTOGostSchuelerWiederholungFachwahlen erzeugeWiederholungFachwahl(final String kursartQ11) {
		final DTOGostSchuelerWiederholungFachwahlen fw = new DTOGostSchuelerWiederholungFachwahlen(ID_MIT_WIEDERHOLUNG, ID_FACH);
		fw.Q11_Kursart = kursartQ11;
		return fw;
	}

	@Test
	@DisplayName("findById: Ohne Wiederholung wird die Fachwahl der aktuellen Planung gelesen")
	void testFindByIdOhneWiederholung() {
		final DTOGostSchuelerFachbelegungenPK pk = new DTOGostSchuelerFachbelegungenPK(ID_OHNE_WIEDERHOLUNG, ID_FACH);
		final DTOGostSchuelerFachbelegungen fb = new DTOGostSchuelerFachbelegungen(ID_OHNE_WIEDERHOLUNG, ID_FACH);
		when(wiederholungRepository.findById(ID_OHNE_WIEDERHOLUNG)).thenReturn(Optional.empty());
		when(fachbelegungenRepository.findById(pk)).thenReturn(Optional.of(fb));

		assertThat(repository.findById(pk)).containsSame(fb);
		verify(wiederholungFachwahlenRepository, never()).findById(any());
	}

	@Test
	@DisplayName("findById: Mit Wiederholung wird die für den neuen Jahrgang geplante Fachwahl gelesen")
	void testFindByIdMitWiederholung() {
		mockWiederholung();
		when(wiederholungFachwahlenRepository.findById(new DTOGostSchuelerWiederholungFachwahlenPK(ID_MIT_WIEDERHOLUNG, ID_FACH)))
				.thenReturn(Optional.of(erzeugeWiederholungFachwahl("LK")));

		final Optional<DTOGostSchuelerFachbelegungen> result = repository.findById(new DTOGostSchuelerFachbelegungenPK(ID_MIT_WIEDERHOLUNG, ID_FACH));

		assertThat(result).isPresent();
		assertThat(result.get().Q11_Kursart).isEqualTo("LK");
		verify(fachbelegungenRepository, never()).findById(any());
	}

	@Test
	@DisplayName("update: Mit Wiederholung wird die Fachwahl in die Wiederholung-Tabelle geschrieben")
	void testUpdateMitWiederholung() {
		mockWiederholung();
		when(wiederholungFachwahlenRepository.findById(new DTOGostSchuelerWiederholungFachwahlenPK(ID_MIT_WIEDERHOLUNG, ID_FACH))).thenReturn(Optional.empty());
		final DTOGostSchuelerFachbelegungen fb = new DTOGostSchuelerFachbelegungen(ID_MIT_WIEDERHOLUNG, ID_FACH);
		fb.Q11_Kursart = "GK";

		repository.update(fb);

		verify(wiederholungFachwahlenRepository).update(argThat((final DTOGostSchuelerWiederholungFachwahlen fw) ->
				(fw.Schueler_ID == ID_MIT_WIEDERHOLUNG) && (fw.Fach_ID == ID_FACH) && "GK".equals(fw.Q11_Kursart)));
		verify(fachbelegungenRepository, never()).update(any(DTOGostSchuelerFachbelegungen.class));
	}

	@Test
	@DisplayName("create mit Liste: Mit Wiederholung wird die Fachwahl in die Wiederholung-Tabelle geschrieben")
	void testCreateListeMitWiederholung() {
		mockWiederholung();
		when(wiederholungFachwahlenRepository.findById(new DTOGostSchuelerWiederholungFachwahlenPK(ID_MIT_WIEDERHOLUNG, ID_FACH))).thenReturn(Optional.empty());
		final DTOGostSchuelerFachbelegungen fb = new DTOGostSchuelerFachbelegungen(ID_MIT_WIEDERHOLUNG, ID_FACH);
		fb.Q11_Kursart = "GK";

		repository.create(new ArrayList<>(List.of(fb)));

		verify(wiederholungFachwahlenRepository).update(argThat((final DTOGostSchuelerWiederholungFachwahlen fw) ->
				(fw.Schueler_ID == ID_MIT_WIEDERHOLUNG) && (fw.Fach_ID == ID_FACH) && "GK".equals(fw.Q11_Kursart)));
		verify(fachbelegungenRepository, never()).create(any(DTOGostSchuelerFachbelegungen.class));
	}

	@Test
	@DisplayName("getMap2DBySchuelerIDAndFachID: Die Fachwahlen kommen je Schüler aus der passenden Tabelle")
	void testGetMap2DGemischt() {
		final DTOGostSchuelerFachbelegungen fb = new DTOGostSchuelerFachbelegungen(ID_OHNE_WIEDERHOLUNG, ID_FACH);
		fb.Q11_Kursart = "M";
		final HashMap2D<Long, Long, DTOGostSchuelerFachbelegungen> normal = new HashMap2D<>();
		normal.put(ID_OHNE_WIEDERHOLUNG, ID_FACH, fb);
		when(wiederholungRepository.findMapByIds(anyCollection()))
				.thenReturn(Map.of(ID_MIT_WIEDERHOLUNG, new DTOGostSchuelerWiederholung(ID_MIT_WIEDERHOLUNG, 2029, GostHalbjahr.Q11)));
		when(fachbelegungenRepository.getMap2DBySchuelerIDAndFachID(List.of(ID_OHNE_WIEDERHOLUNG))).thenReturn(normal);
		when(wiederholungFachwahlenRepository.getMapBySchuelerID(anyCollection()))
				.thenReturn(Map.of(ID_MIT_WIEDERHOLUNG, List.of(erzeugeWiederholungFachwahl("LK"))));

		final HashMap2D<Long, Long, DTOGostSchuelerFachbelegungen> result =
				repository.getMap2DBySchuelerIDAndFachID(List.of(ID_OHNE_WIEDERHOLUNG, ID_MIT_WIEDERHOLUNG));

		assertThat(result.getOrNull(ID_OHNE_WIEDERHOLUNG, ID_FACH).Q11_Kursart).isEqualTo("M");
		assertThat(result.getOrNull(ID_MIT_WIEDERHOLUNG, ID_FACH).Q11_Kursart).isEqualTo("LK");
	}

	@Test
	@DisplayName("deleteMultipleBySchuelerID: Bei Schülern mit Wiederholung werden nur die Wiederholung-Fachwahlen gelöscht")
	void testDeleteMultipleBySchuelerID() {
		when(wiederholungRepository.findMapByIds(anyCollection()))
				.thenReturn(Map.of(ID_MIT_WIEDERHOLUNG, new DTOGostSchuelerWiederholung(ID_MIT_WIEDERHOLUNG, 2029, GostHalbjahr.Q11)));

		repository.deleteMultipleBySchuelerID(List.of(ID_OHNE_WIEDERHOLUNG, ID_MIT_WIEDERHOLUNG));

		verify(fachbelegungenRepository).deleteMultipleBySchuelerID(List.of(ID_OHNE_WIEDERHOLUNG));
		verify(wiederholungFachwahlenRepository).deleteMultipleBySchuelerID(argThat(ids -> ids.size() == 1 && ids.contains(ID_MIT_WIEDERHOLUNG)));
	}

}
