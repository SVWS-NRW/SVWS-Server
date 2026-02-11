package de.svws_nrw.data.schule;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;

/**
 * Testet den Service für den Zugriff auf die Schuljahresabschnitte
 */
@ExtendWith(MockitoExtension.class)
class SchuljahresabschnittServiceTest {

	@Mock
	private SchuljahresabschnitteRepository repoMock;

	@InjectMocks
	private SchuljahresabschnittService service;

	@Test
	@DisplayName("Test: getById liefert korrekt gemappte Daten")
	void testGetById() {
		final long id = 42L;
		final DTOSchuljahresabschnitte dto = new DTOSchuljahresabschnitte(id, 2023, 1);
		dto.VorigerAbschnitt_ID = 123L;
		dto.FolgeAbschnitt_ID = 19L;
		when(repoMock.getById(id)).thenReturn(dto);

		final var result = service.getById(id);

		assertNotNull(result);
		assertEquals(id, result.id);
		assertEquals(2023, result.schuljahr);
		assertEquals(1, result.abschnitt);
		assertEquals(123L, result.idVorigerAbschnitt);
		assertEquals(19L, result.idFolgeAbschnitt);
		verify(repoMock, times(1)).getById(id);
	}

	@Test
	@DisplayName("Test: Prüfe das DTO-Mapping")
	void testGetList() {
		final DTOSchuljahresabschnitte dto1 = new DTOSchuljahresabschnitte(1L, 2026, 2);
		dto1.FolgeAbschnitt_ID = 2L;
		final DTOSchuljahresabschnitte dto2 = new DTOSchuljahresabschnitte(2L, 2027, 1);
		dto2.VorigerAbschnitt_ID = 1L;
		when(repoMock.getAll()).thenReturn(List.of(dto1, dto2));

		final var resultList = service.getList();
		assertEquals(2, resultList.size());

		assertEquals(1L, resultList.get(0).id);
		assertEquals(2026, resultList.get(0).schuljahr);
		assertEquals(2, resultList.get(0).abschnitt);
		assertEquals(null, resultList.get(0).idVorigerAbschnitt);
		assertEquals(2L, resultList.get(0).idFolgeAbschnitt);

		assertEquals(2L, resultList.get(1).id);
		assertEquals(2027, resultList.get(1).schuljahr);
		assertEquals(1, resultList.get(1).abschnitt);
		assertEquals(1L, resultList.get(1).idVorigerAbschnitt);
		assertEquals(null, resultList.get(1).idFolgeAbschnitt);
	}

}
