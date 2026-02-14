package de.svws_nrw.repo.kurse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
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

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;

/**
 * Test für die Klasse {@link KurseRepositoryImpl}
 */
@ExtendWith(MockitoExtension.class)
class KurseRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private KurseRepositoryImpl repository;


	@Test
	@DisplayName("Test: getListBySchuljahresabschnitt() liefert die Liste der Kurse.")
	void testGetListBySchuljahresabschnitt() {
		final long idSchuljahresabschnitt = 123L;
		final DTOKurs kurs1 = new DTOKurs(1L, idSchuljahresabschnitt, "IF-GK1", 42);
		final List<DTOKurs> expectedList = List.of(kurs1);

		when(conn.queryList(DTOKurs.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKurs.class, idSchuljahresabschnitt))
				.thenReturn(expectedList);

		final List<DTOKurs> result = repository.getListBySchuljahresabschnitt(idSchuljahresabschnitt);

		assertThat(result).containsExactly(kurs1);
		verify(conn).queryList(DTOKurs.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKurs.class, idSchuljahresabschnitt);
	}

	@Test
	@DisplayName("Test: getMapBySchuljahresabschnitt() liefert die Kurse und erzeugt daraus eine Map.")
	void testGetMapBySchuljahresabschnitt() {
		final long idSchuljahresabschnitt = 456L;
		final DTOKurs kurs1 = new DTOKurs(1L, idSchuljahresabschnitt, "IF-GK1", 42);
		final DTOKurs kurs2 = new DTOKurs(2L, idSchuljahresabschnitt, "IF-GK2", 42);

		when(conn.queryList(DTOKurs.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKurs.class, idSchuljahresabschnitt))
				.thenReturn(List.of(kurs1, kurs2));

		final Map<Long, DTOKurs> resultMap = repository.getMapBySchuljahresabschnitt(idSchuljahresabschnitt);

		assertThat(resultMap)
				.hasSize(2)
				.containsOnly(
						entry(1L, kurs1),
						entry(2L, kurs2)
				);
	}

	@Test
	@DisplayName("Test: Der Konstruktor setzt die ID-Zugriffe (getId/setId) korrekt auf das ID-Feld von DTOKurs.")
	void testConstructorAndIdAccess() {
		// Teste getId indirekt via getMap()
		final DTOKurs k = new DTOKurs(999L, 1L, "IF-GK1", 42);
		when(conn.queryAll(DTOKurs.class)).thenReturn(List.of(k));
		assertThat(repository.getMap()).containsEntry(999L, k);

		// Teste setId über die create-Methode
		when(conn.transactionGetNextID(DTOKurs.class)).thenReturn(1000L);
		when(conn.transactionPersist(k)).thenReturn(true);
		repository.create(k);
		assertThat(k.ID).isEqualTo(1000L);
	}

}
