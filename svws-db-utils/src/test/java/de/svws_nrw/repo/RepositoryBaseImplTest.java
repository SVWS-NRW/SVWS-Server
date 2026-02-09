package de.svws_nrw.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;

/**
 * Test für die Klasse {@link RepositoryBaseImpl}
 */
@ExtendWith(MockitoExtension.class)
class RepositoryBaseImplTest {

	@Mock
	private DBEntityManager conn;

	/**
	 * Eine minimale Hilfsklasse für den Test auf eine konkrete Instanz der abstrakten
	 * {@link RepositoryBaseImpl}-Klasse. Diese braucht eine Datenbank-Entität für die Tests.
	 */
	private static final class TestEntity {
		/** Die ID für die Test-Entität */
		Long id;
	}

	/**
	 * Ein Test-Repository als konkrete Instanz des abtrakten {@link RepositoryBaseImpl}
	 */
	private static class TestRepository extends RepositoryBaseImpl<TestEntity, Long> {
		protected TestRepository(final DBEntityManager conn) {
			super(conn, TestEntity.class);
		}

		@Override
		protected Object[] mapIdToParameter(final Long id) {
			return new Object[] { id };
		}
	}

	/** Das konkrete Repository für die einezelnen Tests */
	private TestRepository repository;

	/**
	 * Erstellt für jeden Test vorab eine neue Instanz des Test-Repositories
	 */
	@BeforeEach
	void setUp() {
		repository = new TestRepository(conn);
	}

	@Test
	@DisplayName("Test: getFirst() ruft querySingle für eine Datenbank-Abfrage auf.")
	void testGetFirst() {
		final TestEntity dto = new TestEntity();
		when(conn.querySingle(TestEntity.class)).thenReturn(dto);

		final TestEntity result = repository.getFirst();

		assertNotNull(result);
		verify(conn).querySingle(TestEntity.class);

		when(conn.querySingle(TestEntity.class)).thenReturn(null);
		assertThrows(RepositoryException.class, () -> repository.getFirst());
	}

	@Test
	@DisplayName("Test: findFirst() liefert ein leeres Optional, wenn keine Entität existiert.")
	void testFindFirst() {
		when(conn.querySingle(TestEntity.class)).thenReturn(null);
		assertTrue(repository.findFirst().isEmpty());

		final TestEntity entity = new TestEntity();
		when(conn.querySingle(TestEntity.class)).thenReturn(entity);
		assertEquals(entity, repository.findFirst().orElseThrow());
	}

	@Test
	@DisplayName("Test: getById() nutzt die Methode mapIdToParameter, um eine Datenbank-Entität anhand der ID zu bestimmen (bei einem Fehler gibt es eine Exception).")
	void testGetById() {
		final Long id = 1L;
		final TestEntity entity = new TestEntity();
		entity.id = id;

		// Teste den Fall, dass das Element existiert
		when(conn.queryByKey(TestEntity.class, id)).thenReturn(entity);
		assertEquals(entity.id, repository.getById(id).id);
		verify(conn).queryByKey(TestEntity.class, id);

		// Teste den Fall, dass das Element nicht existiert
		when(conn.queryByKey(TestEntity.class, id)).thenReturn(null);
		final RepositoryException exception = assertThrows(RepositoryException.class, () -> {
			repository.getById(id);
		});

		assertTrue(exception.getMessage().contains("Es konnte keine Entität der Klasse ")
				&& exception.getMessage().contains(" für die angegebene ID bestimmt werden."));
		verify(conn, times(2)).queryByKey(TestEntity.class, id);
	}

	@Test
	@DisplayName("Test: findById() nutzt die Methode mapIdToParameter, um eine Datenbank-Entität anhand der ID zu bestimmen (Optional).")
	void testFindById() {
		final Long id = 1042L;
		final TestEntity entity = new TestEntity();
		entity.id = id;

		// Teste den Fall, dass das Element existiert
		when(conn.queryByKey(TestEntity.class, id)).thenReturn(entity);
		var result = repository.findById(id);
		assertTrue(result.isPresent());
		assertEquals(entity.id, result.get().id);
		verify(conn).queryByKey(TestEntity.class, id);

		// Teste den Fall, dass das Element nicht existiert
		when(conn.queryByKey(TestEntity.class, id)).thenReturn(null);
		result = repository.findById(id);
		assertTrue(result.isEmpty());
		verify(conn, times(2)).queryByKey(TestEntity.class, id);
	}


	@Test
	@DisplayName("Test: getAll() sollte eine Datenbank-Abfrage auf alle Datenbank-Entitäten mit queryAll ausführen.")
	void testGetAll() {
		repository.getAll();
		verify(conn).queryAll(TestEntity.class);
	}

	@Test
	@DisplayName("Test: create(entity) delegiert an update(entity) und gibt das erstellte Objekt zurück.")
	void testCreate() {
		// Erfolgsfall
		final TestEntity entity = new TestEntity();
		when(conn.transactionPersist(entity)).thenReturn(true);
		final TestEntity result = repository.create(entity);
		assertNotNull(result);
		assertEquals(entity, result);
		verify(conn).transactionPersist(entity);

		// Fehlerfall
		when(conn.transactionPersist(entity)).thenReturn(false);
		assertThrows(RepositoryException.class, () -> repository.create(entity));
	}


	@Test
	@DisplayName("Test: update(entity) sollte die Datenbank-Entität mit transactionPersist bei der Datenbank-Verbindung persistieren.")
	void testUpdate() {
		// Erfolgsfall
		final TestEntity entity = new TestEntity();
		when(conn.transactionPersist(entity)).thenReturn(true);
		repository.update(entity);
		verify(conn).transactionPersist(entity);

		// Fehlerfall
		when(conn.transactionPersist(entity)).thenReturn(false);
		assertThrows(RepositoryException.class, () -> repository.update(entity));
	}

	@Test
	@DisplayName("Test: delete(entity) sollte für die Datenbank-Entität mit transactionRemove über die Datenbank-Verbindung entfernen.")
	void testDelete() {
		// Erfolgsfall
		final TestEntity entity = new TestEntity();
		when(conn.transactionRemove(entity)).thenReturn(true);
		repository.delete(entity);
		verify(conn).transactionRemove(entity);

		// Fehlerfall
		when(conn.transactionRemove(entity)).thenReturn(false);
		assertThrows(RepositoryException.class, () -> repository.delete(entity));
	}


	@Test
	@DisplayName("Test: create(entities) delegiert an update(entities) und gibt die erstellten Objekt zurück.")
	void testCreateList() {
		final List<TestEntity> entities = List.of(new TestEntity(), new TestEntity());

		// Erfolgsfall
		when(conn.transactionPersistAll(entities)).thenReturn(true);
		final List<TestEntity> result = repository.create(entities);
		assertNotNull(result);
		assertEquals(entities, result);
		verify(conn).transactionPersistAll(entities);

		// Fehlerfall
		when(conn.transactionPersistAll(entities)).thenReturn(false);
		assertThrows(RepositoryException.class, () -> repository.create(entities));
	}

	@Test
	@DisplayName("Test: update(entities) sollte die Datenbank-Entitäten mit transactionPersistAll bei der Datenbank-Verbindung persistieren.")
	void testUpdateList() {
		final List<TestEntity> list = List.of(new TestEntity(), new TestEntity());

		// Erfolgsfall
		when(conn.transactionPersistAll(list)).thenReturn(true);
		final List<TestEntity> result = repository.update(list);
		assertEquals(2, result.size());
		verify(conn).transactionPersistAll(list);

		// Fehlerfall
		when(conn.transactionPersistAll(list)).thenReturn(false);
		assertThrows(RepositoryException.class, () -> repository.update(list));
	}

	@Test
	@DisplayName("Test: delete(entities) entfernt die Datenbank-Entitäten mit transactionRemoveAll oder wirft im Fehlerfall eine RepositoryException.")
	void testDeleteList() {
		final List<TestEntity> list = List.of(new TestEntity(), new TestEntity());

		// Erfolgsfall
		when(conn.transactionRemoveAll(list)).thenReturn(true);
		final List<TestEntity> result = repository.delete(list);
		assertEquals(2, result.size());
		verify(conn).transactionRemoveAll(list);

		// Fehlerfall
		when(conn.transactionRemoveAll(list)).thenReturn(false);
		assertThrows(RepositoryException.class, () -> repository.delete(list));
	}


	@Test
	@DisplayName("Test: flush() ruft transactionFlush auf.")
	void testFlush() {
		repository.flush();
		verify(conn).transactionFlush();
	}

}
