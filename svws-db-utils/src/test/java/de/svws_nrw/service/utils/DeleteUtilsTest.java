package de.svws_nrw.service.utils;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

import de.svws_nrw.core.data.SimpleOperationResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeleteUtilsTest {

	private record TestEntity(long id) {
	}

	private record ResponseView(Long id, boolean success, List<String> log) {
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

		final var result = DeleteUtils.delete(
				List.of(1L),
				ids -> Set.of(),
				ids -> List.of(entity),
				entities -> entities,
				getIdFunction(),
				"Ort");

		assertEquals(
				List.of(new ResponseView(1L, true, List.of())),
				toViews(result));
	}

	@Test
	void delete_returnsNotFoundError() {
		final var result = DeleteUtils.delete(
				List.of(1L),
				ids -> Set.of(),
				ids -> List.of(),
				entities -> entities,
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

		final var result = DeleteUtils.delete(
				List.of(1L),
				ids -> Set.of(),
				ids -> List.of(entity),
				entities -> List.of(),
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

		final var result = DeleteUtils.delete(
				List.of(5L, 1L, 3L, 2L),
				ids -> Set.of(2L),
				ids -> List.of(entity1, entity2, entity5),
				entities -> entities.stream()
						.filter(e -> e.id() != 5L)
						.toList(),
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
