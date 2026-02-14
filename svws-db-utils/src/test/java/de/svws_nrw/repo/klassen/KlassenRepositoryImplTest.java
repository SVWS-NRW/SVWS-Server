package de.svws_nrw.repo.klassen;

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
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;

/**
 * Test für die Klasse {@link KlassenRepositoryImpl}
 */
@ExtendWith(MockitoExtension.class)
class KlassenRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private KlassenRepositoryImpl repository;


	@Test
	@DisplayName("Test: getListBySchuljahresabschnitt() liefert die Liste der Klassen.")
	void testGetListBySchuljahresabschnitt() {
		final long idSchuljahresabschnitt = 123L;
		final DTOKlassen klasse1 = new DTOKlassen(1L, idSchuljahresabschnitt, "07a");
		final List<DTOKlassen> expectedList = List.of(klasse1);

		when(conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, idSchuljahresabschnitt))
				.thenReturn(expectedList);

		final List<DTOKlassen> result = repository.getListBySchuljahresabschnitt(idSchuljahresabschnitt);

		assertThat(result).containsExactly(klasse1);
		verify(conn).queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, idSchuljahresabschnitt);
	}

	@Test
	@DisplayName("Test: getMapBySchuljahresabschnitt() liefert die Klassen und erzeugt daraus eine Map.")
	void testGetMapBySchuljahresabschnitt() {
		final long idSchuljahresabschnitt = 456L;
		final DTOKlassen klasse1 = new DTOKlassen(1L, idSchuljahresabschnitt, "07a");
		final DTOKlassen klasse2 = new DTOKlassen(2L, idSchuljahresabschnitt, "07b");

		when(conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, idSchuljahresabschnitt))
				.thenReturn(List.of(klasse1, klasse2));

		final Map<Long, DTOKlassen> resultMap = repository.getMapBySchuljahresabschnitt(idSchuljahresabschnitt);

		assertThat(resultMap)
				.hasSize(2)
				.containsOnly(
						entry(1L, klasse1),
						entry(2L, klasse2)
				);
	}

	@Test
	@DisplayName("Test: Der Konstruktor setzt die ID-Zugriffe (getId/setId) korrekt auf das ID-Feld von DTOKlassen.")
	void testConstructorAndIdAccess() {
		// Teste getId indirekt via getMap()
		final DTOKlassen k = new DTOKlassen(999L, 1L, "08a");
		when(conn.queryAll(DTOKlassen.class)).thenReturn(List.of(k));
		assertThat(repository.getMap()).containsEntry(999L, k);

		// Teste setId über die create-Methode
		when(conn.transactionGetNextID(DTOKlassen.class)).thenReturn(1000L);
		when(conn.transactionPersist(k)).thenReturn(true);
		repository.create(k);
		assertThat(k.ID).isEqualTo(1000L);
	}

}
