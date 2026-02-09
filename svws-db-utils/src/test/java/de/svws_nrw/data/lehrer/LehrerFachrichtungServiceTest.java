package de.svws_nrw.data.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.asd.data.lehrer.LehrerFachrichtungEintrag;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramtFachrichtung;
import de.svws_nrw.repo.lehrer.LehrerPersonaldatenLehramtFachrichtungRepository;

/**
 * Tests für den Service zu Fachrichtungen bei Lehrern.
 */
@ExtendWith(MockitoExtension.class)
class LehrerFachrichtungServiceTest {

	@Mock
	private LehrerPersonaldatenLehramtFachrichtungRepository repoMock;

	@InjectMocks
	private LehrerFachrichtungService service;

	@Test
	@DisplayName("Test: getMapByLehramt führt das Mapping von zwei Fachrichtungseinträgen bei einem Lehramt korrekt durch")
	void testGetMapByLehramt() {
		final long lehramtId = 500L;
		final var dto1 = new DTOLehrerPersonaldatenLehramtFachrichtung(1L, lehramtId, 10L);
		dto1.FachrichtungAnerkennung_Katalog_ID = 100L;
		final var dto2 = new DTOLehrerPersonaldatenLehramtFachrichtung(2L, lehramtId, 20L);
		// keine Anerkennung...

		when(repoMock.getMapByLehramt(anyCollection())).thenReturn(Map.of(lehramtId, List.of(dto1, dto2)));

		final Map<Long, List<LehrerFachrichtungEintrag>> result = service.getMapByLehramt(List.of(lehramtId));
		assertNotNull(result);
		assertTrue(result.containsKey(lehramtId));

		final List<LehrerFachrichtungEintrag> eintraege = result.get(lehramtId);
		assertEquals(2, eintraege.size());

		final LehrerFachrichtungEintrag e1 = eintraege.stream().filter(e -> e.id == 1L).findFirst().orElseThrow();
		assertEquals(lehramtId, e1.idLehramt);
		assertEquals(10L, e1.idFachrichtung);
		assertEquals(100L, e1.idAnerkennungsgrund);

		final LehrerFachrichtungEintrag e2 = eintraege.stream().filter(e -> e.id == 2L).findFirst().orElseThrow();
		assertEquals(20L, e2.idFachrichtung);
		assertNull(e2.idAnerkennungsgrund);

		verify(repoMock, times(1)).getMapByLehramt(anyCollection());
	}

	@Test
	@DisplayName("Test: getMapByLehramt liefert eine leere Map bei der nicht vorhandenen Daten")
	void testGetMapByLehramtEmpty() {
		when(repoMock.getMapByLehramt(anyCollection())).thenReturn(Map.of());
		final var result = service.getMapByLehramt(List.of(999L));
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

}
