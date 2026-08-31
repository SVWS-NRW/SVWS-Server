package de.svws_nrw.repo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;

/**
 * Tests für die Klasse {@link RepositoryImpl}.
 */
@ExtendWith(MockitoExtension.class)
class RepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	/**
	 * Eine minimale Hilfsklasse als Datenbank-Entität für die Tests.
	 */
	private static final class TestEntity {
		/** Die ID der Test-Entität */
		Long id;
	}

	/**
	 * Standardimplementierung des abstrakten {@link RepositoryImpl} für Tests.
	 * {@link #autoAssignId()} gibt {@code true} zurück (Standardverhalten).
	 */
	private static class TestRepository extends RepositoryImpl<TestEntity> {
		protected TestRepository(final DBEntityManager conn) {
			super(conn, TestEntity.class, o -> o.id, (o, id) -> o.id = id);
		}
	}

	/**
	 * Implementierung des abstrakten {@link RepositoryImpl} für Tests mit FK-als-PK-Verhalten.
	 * {@link #autoAssignId()} gibt {@code false} zurück.
	 */
	private static class TestRepositoryFkAlsPk extends RepositoryImpl<TestEntity> {
		protected TestRepositoryFkAlsPk(final DBEntityManager conn) {
			super(conn, TestEntity.class, o -> o.id, (o, id) -> o.id = id);
		}

		@Override
		protected boolean autoAssignId() {
			return false;
		}
	}

	/** Das Standard-Repository für die einzelnen Tests */
	private TestRepository repository;

	/**
	 * Erstellt für jeden Test vorab eine neue Instanz des Test-Repositories.
	 */
	@BeforeEach
	void setUp() {
		repository = new TestRepository(conn);
	}


	@Nested
	@DisplayName("Konstruktor")
	class KonstruktorTests {

		@Test
		@DisplayName("Wirft IllegalArgumentException, wenn kein Consumer für setId übergeben wird.")
		@SuppressWarnings("unused")
		void testKonstruktorOhneSetIdWirftException() {
			assertThrows(IllegalArgumentException.class, () -> new RepositoryImpl<>(conn, TestEntity.class, null, null) {
				// Anonyme Klasse zum Testen des abstrakten RepositoryImpl
			});
		}
	}


	@Nested
	@DisplayName("autoAssignId()")
	class AutoAssignIdTests {

		@Test
		@DisplayName("Gibt standardmäßig true zurück.")
		void testAutoAssignIdStandardTrue() {
			assertThat(repository.autoAssignId()).isTrue();
		}

		@Test
		@DisplayName("Gibt false zurück, wenn von Subklasse mit false überschrieben.")
		void testAutoAssignIdFkAlsPkFalse() {
			final var fkAlsPkRepository = new TestRepositoryFkAlsPk(conn);
			assertThat(fkAlsPkRepository.autoAssignId()).isFalse();
		}
	}


	@Nested
	@DisplayName("create(entity)")
	class CreateSingleTests {

		@Test
		@DisplayName("Setzt die ID automatisch und persistiert die Entität.")
		void testCreateSetzt_IdUndPersistiert() {
			final var entity = new TestEntity();
			when(conn.transactionGetNextID(TestEntity.class)).thenReturn(42L);
			when(conn.transactionPersist(entity)).thenReturn(true);

			repository.create(entity);

			assertEquals(42L, entity.id);
			verify(conn).transactionGetNextID(TestEntity.class);
			verify(conn).transactionPersist(entity);
		}

		@Test
		@DisplayName("Überschreibt die ID nicht, wenn autoAssignId() false zurückgibt.")
		void testCreateUeberschreibtIdNichtBeiAutoAssignIdFalse() {
			final var fkAlsPkRepository = new TestRepositoryFkAlsPk(conn);
			final var entity = new TestEntity();
			entity.id = 9296L;
			when(conn.transactionPersist(entity)).thenReturn(true);

			fkAlsPkRepository.create(entity);

			assertEquals(9296L, entity.id);
			verify(conn, never()).transactionGetNextID(TestEntity.class);
			verify(conn).transactionPersist(entity);
		}

		@Test
		@DisplayName("Wirft RepositoryException, wenn transactionPersist false zurückgibt.")
		void testCreateWirftExceptionBeiPersistFehler() {
			final var entity = new TestEntity();
			when(conn.transactionGetNextID(TestEntity.class)).thenReturn(1L);
			when(conn.transactionPersist(entity)).thenReturn(false);

			assertThatThrownBy(() -> repository.create(entity))
					.isInstanceOf(RepositoryException.class);
		}
	}


	@Nested
	@DisplayName("create(entities)")
	class CreateMultipleTests {

		@Test
		@DisplayName("Setzt IDs fortlaufend und persistiert alle Entitäten.")
		void testCreateMultipleSetzt_IdsUndPersistiert() {
			final var entities = Arrays.asList(new TestEntity(), new TestEntity(), new TestEntity(), new TestEntity());
			when(conn.transactionGetNextID(TestEntity.class)).thenReturn(42L);
			when(conn.transactionPersistAll(entities)).thenReturn(true);

			final var result = repository.create(entities);

			assertThat(result)
					.hasSize(4)
					.extracting(e -> e.id)
					.containsExactly(42L, 43L, 44L, 45L);
			verify(conn).transactionGetNextID(TestEntity.class);
			verify(conn).transactionPersistAll(entities);
		}

		@Test
		@DisplayName("Wirft RepositoryException, wenn transactionPersistAll false zurückgibt.")
		void testCreateMultipleWirftExceptionBeiPersistFehler() {
			final var entities = List.of(new TestEntity());
			when(conn.transactionGetNextID(TestEntity.class)).thenReturn(1L);
			when(conn.transactionPersistAll(entities)).thenReturn(false);

			assertThatThrownBy(() -> repository.create(entities))
					.isInstanceOf(RepositoryException.class);
		}
	}


	@Nested
	@DisplayName("getNextID()")
	class GetNextIdTests {

		@Test
		@DisplayName("Delegiert an conn.transactionGetNextID und gibt die nächste freie ID zurück.")
		void testGetNextId() {
			when(conn.transactionGetNextID(TestEntity.class)).thenReturn(99L);

			final long result = repository.getNextID();

			assertEquals(99L, result);
			verify(conn).transactionGetNextID(TestEntity.class);
		}
	}


	@Nested
	@DisplayName("findListByIds()")
	class FindListByIdsTests {

		@Test
		@DisplayName("Gibt die gefundenen Entitäten zurück, wenn gültige IDs übergeben werden.")
		void testFindListByIdsGueltigeIds() {
			final var ids = Arrays.asList(1L, 2L);
			final var expected = Arrays.asList(new TestEntity(), new TestEntity());
			when(conn.queryByKeyList(TestEntity.class, ids)).thenReturn(expected);

			final var result = repository.findListByIds(ids);

			assertThat(result).hasSize(2);
			verify(conn).queryByKeyList(TestEntity.class, ids);
		}

		@Test
		@DisplayName("Gibt eine leere Liste zurück und führt keine DB-Abfrage aus, wenn null übergeben wird.")
		void testFindListByIdsNull() {
			assertTrue(repository.findListByIds(null).isEmpty());
			verifyNoInteractions(conn);
		}

		@Test
		@DisplayName("Gibt eine leere Liste zurück und führt keine DB-Abfrage aus, wenn eine leere Liste übergeben wird.")
		void testFindListByIdsLeereListe() {
			assertTrue(repository.findListByIds(Collections.emptyList()).isEmpty());
			verifyNoInteractions(conn);
		}
	}


	@Nested
	@DisplayName("findMapByIds()")
	class FindMapByIdsTests {

		@Test
		@DisplayName("Gibt eine Map der gefundenen Entitäten zurück, indiziert nach ID.")
		void testFindMapByIdsGueltigeIds() {
			final var ids = Arrays.asList(1L, 3L);
			final var e1 = new TestEntity();
			e1.id = 1L;
			final var e2 = new TestEntity();
			e2.id = 3L;
			when(conn.queryByKeyList(TestEntity.class, ids)).thenReturn(Arrays.asList(e1, e2));

			final var result = repository.findMapByIds(ids);

			assertThat(result)
					.hasSize(2)
					.containsEntry(1L, e1)
					.containsEntry(3L, e2);
			verify(conn).queryByKeyList(TestEntity.class, ids);
		}

		@Test
		@DisplayName("Filtert null-Einträge aus der DB-Antwort heraus.")
		void testFindMapByIdsFiltertNull() {
			final var ids = Arrays.asList(1L, 2L);
			final var e1 = new TestEntity();
			e1.id = 1L;
			when(conn.queryByKeyList(TestEntity.class, ids)).thenReturn(Arrays.asList(e1, null));

			final var result = repository.findMapByIds(ids);

			assertThat(result)
					.hasSize(1)
					.containsEntry(1L, e1);
		}

		@Test
		@DisplayName("Gibt eine leere Map zurück, wenn eine leere ID-Liste übergeben wird.")
		void testFindMapByIdsLeereListe() {
			final var result = repository.findMapByIds(Collections.emptyList());
			assertThat(result).isEmpty();
			verifyNoInteractions(conn);
		}
	}


	@Nested
	@DisplayName("getMap()")
	class GetMapTests {

		@Test
		@DisplayName("Gibt eine Map aller Entitäten zurück, indiziert nach ID.")
		void testGetMap() {
			final var e1 = new TestEntity();
			e1.id = 10L;
			final var e2 = new TestEntity();
			e2.id = 20L;
			when(conn.queryAll(TestEntity.class)).thenReturn(Arrays.asList(e1, e2));

			final var result = repository.getMap();

			assertThat(result)
					.hasSize(2)
					.containsOnly(entry(10L, e1), entry(20L, e2));
			verify(conn).queryAll(TestEntity.class);
		}
	}


	@Nested
	@DisplayName("mapIdToParameter()")
	class MapIdToParameterTests {

		@Test
		@DisplayName("Verpackt die Long-ID korrekt in ein Object-Array für DB-Abfragen.")
		void testMapIdToParameter() {
			final var entity = new TestEntity();
			entity.id = 42L;
			when(conn.queryByKey(TestEntity.class, 42L)).thenReturn(entity);

			final var result = repository.getById(42L);

			assertThat(result)
					.isNotNull()
					.extracting(e -> e.id)
					.isEqualTo(42L);
			verify(conn, times(1)).queryByKey(TestEntity.class, 42L);
		}
	}
}
