package de.svws_nrw.repo.schulwechsel.dokument;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchulwechselDokument;
import de.svws_nrw.repo.RepositoryException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchulwechselDokumentRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private SchulwechselDokumentRepositoryImpl repository;

	private DTOSchulwechselDokument createEntity(final long id) {
		return new DTOSchulwechselDokument(id, "test.xml", "<xml/>", "2025-01-01", "2025-06-01");
	}

	@Test
	@DisplayName("Konstruktor | Erfolg")
	void constructor_success() {
		assertThat(new SchulwechselDokumentRepositoryImpl(conn))
				.isNotNull()
				.isInstanceOf(SchulwechselDokumentRepositoryImpl.class)
				.isInstanceOf(SchulwechselDokumentRepository.class);
	}

	@Test
	@DisplayName("getAll | Gibt Liste zurück")
	void getAll_returnsList() {
		final var entity1 = createEntity(1L);
		final var entity2 = createEntity(2L);

		when(conn.queryAll(DTOSchulwechselDokument.class)).thenReturn(List.of(entity1, entity2));

		assertThat(repository.getAll())
				.isNotNull()
				.hasSize(2)
				.containsExactly(entity1, entity2);

		verify(conn, times(1)).queryAll(DTOSchulwechselDokument.class);
	}

	@Test
	@DisplayName("getAll | Leere Liste wenn keine Einträge vorhanden")
	void getAll_emptyList() {
		when(conn.queryAll(DTOSchulwechselDokument.class)).thenReturn(List.of());

		assertThat(repository.getAll()).isNotNull().isEmpty();
		verify(conn, times(1)).queryAll(DTOSchulwechselDokument.class);
	}

	@Test
	@DisplayName("getById | Gibt Entität zurück")
	void getById_success() {
		final var entity = createEntity(1L);

		when(conn.queryByKey(DTOSchulwechselDokument.class, 1L)).thenReturn(entity);

		assertThat(repository.getById(1L)).isNotNull().isEqualTo(entity);
		verify(conn, times(1)).queryByKey(DTOSchulwechselDokument.class, 1L);
	}

	@Test
	@DisplayName("getById | Nicht gefunden → RepositoryException")
	void getById_notFound() {
		when(conn.queryByKey(DTOSchulwechselDokument.class, 99L)).thenReturn(null);

		assertThatThrownBy(() -> repository.getById(99L))
				.isInstanceOf(RepositoryException.class);
	}

	@Test
	@DisplayName("create | Erfolg")
	void create_success() {
		final var entity = createEntity(0L);

		when(conn.transactionGetNextID(DTOSchulwechselDokument.class)).thenReturn(1L);
		when(conn.transactionPersist(entity)).thenReturn(true);

		assertThat(repository.create(entity)).isNotNull().isEqualTo(entity);
		assertThat(entity.id).isEqualTo(1L);

		verify(conn, times(1)).transactionGetNextID(DTOSchulwechselDokument.class);
		verify(conn, times(1)).transactionPersist(entity);
	}

	@Test
	@DisplayName("create | Persist schlägt fehl → RepositoryException")
	void create_persistFails() {
		final var entity = createEntity(0L);

		when(conn.transactionGetNextID(DTOSchulwechselDokument.class)).thenReturn(1L);
		when(conn.transactionPersist(entity)).thenReturn(false);

		assertThatThrownBy(() -> repository.create(entity))
				.isInstanceOf(RepositoryException.class);
	}

	@Test
	@DisplayName("delete | Erfolg")
	void delete_success() {
		final var entity = createEntity(1L);

		when(conn.transactionRemoveAll(List.of(entity))).thenReturn(true);

		assertThat(repository.delete(List.of(entity))).containsExactly(entity);
		verify(conn, times(1)).transactionRemoveAll(List.of(entity));
	}

	@Test
	@DisplayName("delete | Remove schlägt fehl → RepositoryException")
	void delete_removeFails() {
		final var entity = createEntity(1L);

		when(conn.transactionRemoveAll(List.of(entity))).thenReturn(false);

		assertThatThrownBy(() -> repository.delete(List.of(entity)))
				.isInstanceOf(RepositoryException.class);
	}

	@Test
	@DisplayName("findListByIds | Gibt gefundene Entitäten zurück")
	void findListByIds_success() {
		final var entity1 = createEntity(1L);
		final var entity2 = createEntity(2L);
		final var ids = List.of(1L, 2L);

		when(conn.queryByKeyList(DTOSchulwechselDokument.class, ids)).thenReturn(List.of(entity1, entity2));

		assertThat(repository.findListByIds(ids))
				.hasSize(2)
				.containsExactly(entity1, entity2);
	}

	@Test
	@DisplayName("findListByIds | Leere Liste → leere Rückgabe ohne DB-Aufruf")
	void findListByIds_emptyIds() {
		assertThat(repository.findListByIds(List.of())).isEmpty();
		verify(conn, times(0)).queryByKeyList(DTOSchulwechselDokument.class, List.of());
	}
}
