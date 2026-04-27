package de.svws_nrw.service.schueler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.asd.data.schueler.Sprachbelegung;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerSprachenfolge;
import de.svws_nrw.repo.schueler.SchuelerSprachenfolgeRepository;

@ExtendWith(MockitoExtension.class)
class SchuelerSprachenfolgeServiceTest {

	@Mock
	private SchuelerSprachenfolgeRepository schuelerSprachenfolgeRepository;

	private final SchuelerSprachenfolgeService service = new SchuelerSprachenfolgeService();

	@Test
	@DisplayName("Test: prüfe, ob toApi alle Felder korrekt auf das API-Objekt überträgt.")
	void testToApi() {
		final DTOSchuelerSprachenfolge dto = new DTOSchuelerSprachenfolge(10L, 100L, "E");
		dto.IstNachweis = true;
		dto.ReihenfolgeNr = 1;
		dto.ASDJahrgangVon = "05";
		dto.ASDJahrgangBis = "10";
		dto.AbschnittVon = 1;
		dto.AbschnittBis = 2;
		dto.Referenzniveau = "B1";
		dto.KleinesLatinumErreicht = false;
		dto.LatinumErreicht = true;
		dto.GraecumErreicht = false;
		dto.HebraicumErreicht = false;

		final Sprachbelegung result = service.toApi(dto);

		assertNotNull(result);
		assertEquals("E", result.sprache);
		assertTrue(result.istNachweis);
		assertEquals(1, result.reihenfolge);
		assertEquals("05", result.belegungVonJahrgang);
		assertEquals("10", result.belegungBisJahrgang);
		assertEquals(1, result.belegungVonAbschnitt);
		assertEquals(2, result.belegungBisAbschnitt);
		assertEquals("B1", result.referenzniveau);
		assertFalse(result.hatKleinesLatinum);
		assertTrue(result.hatLatinum);
		assertFalse(result.hatGraecum);
		assertFalse(result.hatHebraicum);
	}

	@Test
	@DisplayName("Test: prüfe, ob fetchData/getSprachenfolge die Daten über das Repository lädt und korrekt konvertiert.")
	void testFetchDataAndGetSprachenfolge() {
		final SchuelerSprachenfolgeService repositoryBackedService = new SchuelerSprachenfolgeService(schuelerSprachenfolgeRepository);
		final DTOSchuelerSprachenfolge dto = new DTOSchuelerSprachenfolge(10L, 100L, "E");
		dto.IstNachweis = true;
		dto.ReihenfolgeNr = 1;

		when(schuelerSprachenfolgeRepository.getMapBySchuelerIDs(List.of(100L)))
				.thenReturn(Map.of(100L, List.of(dto)));

		repositoryBackedService.fetchData(List.of(100L));
		final List<Sprachbelegung> result = repositoryBackedService.getSprachenfolge(100L);

		assertEquals(1, result.size());
		assertEquals("E", result.get(0).sprache);
		assertTrue(result.get(0).istNachweis);
		assertEquals(1, result.get(0).reihenfolge);
	}

	@Test
	@DisplayName("Test: prüfe, ob fetchData ohne Repository eine IllegalStateException wirft.")
	void testFetchDataWithoutRepositoryThrows() {
		assertThrows(IllegalStateException.class, () -> service.fetchData(List.of(100L)));
	}
}
