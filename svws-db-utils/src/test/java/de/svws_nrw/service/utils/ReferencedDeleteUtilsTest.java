package de.svws_nrw.service.utils;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.repo.ReferencedBulkDeletionRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReferencedDeleteUtilsTest {

	private record TestEntity(long id) {
	}

	private record ResponseView(Long id, boolean success, List<String> log) {
	}

	private static final class TestRepository implements ReferencedBulkDeletionRepository<TestEntity> {

		private final Set<Long> referencedIds;
		private final List<TestEntity> foundEntities;
		private final Set<Long> deletedIds;

		private TestRepository(
				final Set<Long> referencedIds,
				final List<TestEntity> foundEntities,
				final Set<Long> deletedIds) {
			this.referencedIds = referencedIds;
			this.foundEntities = foundEntities;
			this.deletedIds = deletedIds;
		}

		@Override
		public Set<Long> getReferencedIds(final List<Long> idsToCheck) {
			return referencedIds;
		}

		@Override
		public List<TestEntity> findListByIds(final Collection<Long> ids) {
			return foundEntities.stream()
					.filter(entity -> ids.contains(entity.id()))
					.toList();
		}

		@Override
		@SuppressWarnings("unchecked")
		public <C extends Collection<TestEntity>> C delete(final C entities) {
			return (C) entities.stream()
					.filter(entity -> deletedIds.contains(entity.id()))
					.toList();
		}
	}

	private static Function<TestEntity, Long> getIdFunction() {
		return TestEntity::id;
	}

	private static List<ResponseView> toViews(final List<SimpleOperationResponse> responses) {
		return responses.stream()
				.map(r -> new ResponseView(r.id, r.success, r.log))
				.toList();
	}

	@Test
	void delete_successfullyDeletesEntity() {
		final var entity = new TestEntity(1L);
		final var repository = new TestRepository(
				Set.of(),
				List.of(entity),
				Set.of(1L));

		final var result = ReferencedDeleteUtils.delete(
				List.of(1L),
				repository,
				getIdFunction(),
				"Ort");

		assertEquals(
				List.of(new ResponseView(1L, true, List.of())),
				toViews(result));
	}

	@Test
	void delete_returnsNotFoundError() {
		final var repository = new TestRepository(
				Set.of(),
				List.of(),
				Set.of());

		final var result = ReferencedDeleteUtils.delete(
				List.of(1L),
				repository,
				getIdFunction(),
				"Ort");

		assertEquals(
				List.of(new ResponseView(
						1L,
						false,
						List.of("Ort mit ID 1 wurde nicht gefunden."))),
				toViews(result));
	}

	@Test
	void delete_returnsErrorWhenEntityIsNotReturnedByDelete() {
		final var entity = new TestEntity(1L);
		final var repository = new TestRepository(
				Set.of(),
				List.of(entity),
				Set.of());

		final var result = ReferencedDeleteUtils.delete(
				List.of(1L),
				repository,
				getIdFunction(),
				"Ort");

		assertEquals(
				List.of(new ResponseView(
						1L,
						false,
						List.of("Ort mit ID 1 konnte nicht gelöscht werden."))),
				toViews(result));
	}

	@Test
	void delete_returnsResponsesSortedById() {
		final var entity1 = new TestEntity(1L);
		final var entity2 = new TestEntity(2L);
		final var entity5 = new TestEntity(5L);

		final var repository = new TestRepository(
				Set.of(2L),
				List.of(entity1, entity2, entity5),
				Set.of(1L));

		final var result = ReferencedDeleteUtils.delete(
				List.of(5L, 1L, 3L, 2L),
				repository,
				getIdFunction(),
				"Ort");

		assertEquals(
				List.of(
						new ResponseView(1L, true, List.of()),
						new ResponseView(
								2L,
								false,
								List.of("Ort mit ID 2 ist referenziert und kann nicht gelöscht werden.")),
						new ResponseView(
								3L,
								false,
								List.of("Ort mit ID 3 wurde nicht gefunden.")),
						new ResponseView(
								5L,
								false,
								List.of("Ort mit ID 5 konnte nicht gelöscht werden."))),
				toViews(result));
	}
}
