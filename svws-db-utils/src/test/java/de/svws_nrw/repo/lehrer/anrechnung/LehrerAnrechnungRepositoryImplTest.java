package de.svws_nrw.repo.lehrer.anrechnung;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAnrechnungsstunde;

@ExtendWith(MockitoExtension.class)
class LehrerAnrechnungRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private LehrerAnrechnungRepositoryImpl repository;

	@Test
	@DisplayName("Test: Prüfe, ob getListByIdLehrerAbschnittsdaten die Anrechnungen nach Abschnitt-IDs korrekt gruppiert.")
	void testGetListByIdLehrerAbschnittsdaten() {
		final List<Long> ids = Arrays.asList(100L, 200L);

		final DTOLehrerAnrechnungsstunde e1 = new DTOLehrerAnrechnungsstunde(1L, 100L);
		final DTOLehrerAnrechnungsstunde e2 = new DTOLehrerAnrechnungsstunde(2L, 100L);
		final DTOLehrerAnrechnungsstunde e3 = new DTOLehrerAnrechnungsstunde(3L, 200L);

		when(conn.queryList(DTOLehrerAnrechnungsstunde.QUERY_LIST_BY_ABSCHNITT_ID,
				DTOLehrerAnrechnungsstunde.class,
				ids))
				.thenReturn(Arrays.asList(e1, e2, e3));

		final Map<Long, List<DTOLehrerAnrechnungsstunde>> result = repository.getListByIdLehrerAbschnittsdaten(ids);

		assertNotNull(result);
		assertEquals(2, result.size());

		assertTrue(result.containsKey(100L));
		assertEquals(2, result.get(100L).size());
		assertTrue(result.get(100L).contains(e1));
		assertTrue(result.get(100L).contains(e2));

		assertTrue(result.containsKey(200L));
		assertEquals(1, result.get(200L).size());
		assertEquals(e3, result.get(200L).getFirst());

		verify(conn).queryList(DTOLehrerAnrechnungsstunde.QUERY_LIST_BY_ABSCHNITT_ID,
				DTOLehrerAnrechnungsstunde.class,
				ids);
	}

	@Test
	@DisplayName("Test: Prüfe, ob getListByIdLehrerAbschnittsdaten bei leerem DB-Ergebnis eine leere Map liefert.")
	void testGetListByIdLehrerAbschnittsdatenEmptyResult() {
		final List<Long> ids = List.of(100L);

		when(conn.queryList(DTOLehrerAnrechnungsstunde.QUERY_LIST_BY_ABSCHNITT_ID,
				DTOLehrerAnrechnungsstunde.class,
				ids))
				.thenReturn(Collections.emptyList());

		final Map<Long, List<DTOLehrerAnrechnungsstunde>> result = repository.getListByIdLehrerAbschnittsdaten(ids);

		assertNotNull(result);
		assertTrue(result.isEmpty());

		verify(conn).queryList(DTOLehrerAnrechnungsstunde.QUERY_LIST_BY_ABSCHNITT_ID,
				DTOLehrerAnrechnungsstunde.class,
				ids);
	}

	@Test
	@DisplayName("Test: Prüfe, ob getListByIdLehrerAbschnittsdaten bei null oder leeren IDs eine leere Map liefert.")
	void testGetListByIdLehrerAbschnittsdatenNullOrEmpty() {
		assertTrue(repository.getListByIdLehrerAbschnittsdaten(null).isEmpty());
		assertTrue(repository.getListByIdLehrerAbschnittsdaten(List.of()).isEmpty());
		verifyNoInteractions(conn);
	}

}
