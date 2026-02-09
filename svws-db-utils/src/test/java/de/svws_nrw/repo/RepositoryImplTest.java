package de.svws_nrw.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;

/**
 * Test für die Klasse {@link RepositoryImpl}
 */
@ExtendWith(MockitoExtension.class)
class RepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	/**
	 * Eine minimale Hilfsklasse für den Test auf eine konkrete Instanz der abstrakten
	 * {@link RepositoryImpl}-Klasse. Diese braucht eine Datenbank-Entität für die Tests.
	 */
	private static final class TestEntity {
		/** Die ID für die Test-Entität */
		Long id;
	}

	/**
	 * Ein Test-Repository als konkrete Instanz des abtrakten {@link RepositoryImpl}
	 */
	private static class TestRepository extends RepositoryImpl<TestEntity> {
		protected TestRepository(final DBEntityManager conn) {
			super(conn, TestEntity.class, (o, id) -> o.id = id);
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
	@DisplayName("Test: Der Konstruktor wirft eine IllegalArgumentException, wenn kein Consumer für setId als Parameter übergeben wird.")
	@SuppressWarnings("unused")
	void testConstructorRequiresSetId() {
		assertThrows(IllegalArgumentException.class, () -> {
			new RepositoryImpl<TestEntity>(conn, TestEntity.class, null) {
				// Anonyme Klasse zum Testen des abstrakten RepositoryImpl
			};
		});
	}

	@Test
	@DisplayName("Test: findListByIds() ruft bei einer gültigen Übergabe von einer oder mehreren IDs queryByKeyList auf.")
	void testGetListByIds_WithValidIds() {
		final List<Long> ids = Arrays.asList(1L, 2L);
		final List<TestEntity> expectedList = Arrays.asList(new TestEntity(), new TestEntity());
		when(conn.queryByKeyList(TestEntity.class, ids)).thenReturn(expectedList);

		final List<TestEntity> result = repository.findListByIds(ids);

		assertEquals(2, result.size());
		verify(conn).queryByKeyList(TestEntity.class, ids);
	}

	@Test
	@DisplayName("Test: findListByIds() gibt bei null oder einer leeren Liste eine leere Liste zurück ohne eine Datenbank-Abfrage auszuführen.")
	void testGetListByIds_WithEmptyOrNull() {
		assertTrue(repository.findListByIds(null).isEmpty());
		assertTrue(repository.findListByIds(Collections.emptyList()).isEmpty());
		verifyNoInteractions(conn);
	}

	@Test
	@DisplayName("Test: Bei create(entity) wird die ID automatisch gesetzt und das Objekt wird erfolgreich persistiert.")
	void testCreate() {
		final TestEntity entity = new TestEntity();

		final long idNeu = 42L;
		when(conn.transactionGetNextID(TestEntity.class)).thenReturn(idNeu);
		when(conn.transactionPersist(entity)).thenReturn(true);
		repository.create(entity);

		assertEquals(idNeu, entity.id);
		verify(conn).transactionGetNextID(TestEntity.class);
		verify(conn).transactionPersist(entity);
	}

	@Test
	@DisplayName("Test: Bei create(entities) werden die IDs fortlaufend gesetzt und die Objekte werden erfolgreich persistiert.")
	void testCreateMultiple() {
		final List<TestEntity> entities = Arrays.asList(new TestEntity(), new TestEntity(), new TestEntity(), new TestEntity());

		final long startId = 42L;
		when(conn.transactionGetNextID(TestEntity.class)).thenReturn(startId);
		when(conn.transactionPersistAll(entities)).thenReturn(true);

		final List<TestEntity> result = repository.create(entities);

		assertEquals(4, result.size());
		assertEquals(42L, entities.get(0).id);
		assertEquals(43L, entities.get(1).id);
		assertEquals(44L, entities.get(2).id);
		assertEquals(45L, entities.get(3).id);

		verify(conn).transactionGetNextID(TestEntity.class);
		verify(conn).transactionPersistAll(entities);
	}


	@Test
	@DisplayName("Test: Prüfe, ob mapIdToParameter() die Long-ID korrekt in ein in ein Object-Array verpackt.")
	void testMapIdToParameter() {
		final Long id = 42L;
		final TestEntity entity = new TestEntity();
		entity.id = id;

		// Teste über die Methode getById der Basisklasse. Diese ruft intern die Methode mapIdToParameter(id) auf
		when(conn.queryByKey(TestEntity.class, id)).thenReturn(entity);

		final TestEntity result = repository.getById(id);

		assertNotNull(result);
		assertEquals(id, result.id);

		// Wenn einmal auf die ID zugegriffen wurde, so kann davon ausgegangen werden, dass auch mapIdToParameter verwendet wurde.
		verify(conn, times(1)).queryByKey(TestEntity.class, id);
	}
}
