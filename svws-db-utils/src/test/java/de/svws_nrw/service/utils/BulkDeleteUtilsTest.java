package de.svws_nrw.service.utils;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.repo.ReferencedBulkDeletionRepository;
import de.svws_nrw.repo.Repository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BulkDeleteUtilsTest {

	private record TestEntity(long id) {
	}

	private static final Function<TestEntity, Long> GET_ID = TestEntity::id;
	private static final String LABEL = "Testobjekt";

	@Mock
	private Repository<TestEntity> repository;

	@Mock
	private ReferencedBulkDeletionRepository<TestEntity> referencedRepository;

	// --- Repository-Variante ---

	@Test
	void delete_repository_alleGefundenUndGeloescht_alleSuccess() {
		final var ids = List.of(1L, 2L, 3L);
		final var entities = List.of(new TestEntity(1L), new TestEntity(2L), new TestEntity(3L));
		when(repository.findListByIds(ids)).thenReturn(entities);
		when(repository.delete(entities)).thenReturn(entities);

		final var result = BulkDeleteUtils.delete(ids, repository, GET_ID, LABEL);

		assertThat(result)
				.hasSize(3)
				.allMatch(r -> r.success)
				.extracting(r -> r.id)
				.containsExactly(1L, 2L, 3L);
	}

	@Test
	void delete_repository_idNichtGefunden_error() {
		final var ids = List.of(1L, 2L);
		final var entities = List.of(new TestEntity(1L));
		when(repository.findListByIds(ids)).thenReturn(entities);
		when(repository.delete(entities)).thenReturn(entities);

		final var result = BulkDeleteUtils.delete(ids, repository, GET_ID, LABEL);

		assertThat(ergebnisFuer(result, 1L).success).isTrue();
		assertThat(ergebnisFuer(result, 2L))
				.satisfies(r -> {
					assertThat(r.success).isFalse();
					assertThat(r.log).anyMatch(m -> m.contains("nicht gefunden"));
				});
	}

	@Test
	void delete_repository_gefundenAberNichtGeloescht_error() {
		final var ids = List.of(1L);
		final var entities = List.of(new TestEntity(1L));
		when(repository.findListByIds(ids)).thenReturn(entities);
		when(repository.delete(entities)).thenReturn(Collections.emptyList());

		final var result = BulkDeleteUtils.delete(ids, repository, GET_ID, LABEL);

		assertThat(ergebnisFuer(result, 1L))
				.satisfies(r -> {
					assertThat(r.success).isFalse();
					assertThat(r.log).anyMatch(m -> m.contains("konnte nicht gelöscht werden"));
				});
	}

	@Test
	void delete_repository_gemischt_korrekteReihenfolge() {
		final var ids = List.of(3L, 1L, 2L);
		final var entities = List.of(new TestEntity(3L), new TestEntity(1L));
		when(repository.findListByIds(ids)).thenReturn(entities);
		when(repository.delete(entities)).thenReturn(entities);

		final var result = BulkDeleteUtils.delete(ids, repository, GET_ID, LABEL);

		assertThat(result)
				.extracting(r -> r.id)
				.containsExactly(1L, 2L, 3L);
		assertThat(ergebnisFuer(result, 1L).success).isTrue();
		assertThat(ergebnisFuer(result, 2L))
				.satisfies(r -> assertThat(r.success).isFalse());
		assertThat(ergebnisFuer(result, 3L).success).isTrue();
	}

	// --- ReferencedBulkDeletionRepository-Variante ---

	@Test
	void delete_referenced_alleGefundenKeineReferenzAlleGeloescht_alleSuccess() {
		final var ids = List.of(1L, 2L, 3L);
		final var entities = List.of(new TestEntity(1L), new TestEntity(2L), new TestEntity(3L));
		when(referencedRepository.getReferencedIds(ids)).thenReturn(Set.of());
		when(referencedRepository.findListByIds(ids)).thenReturn(entities);
		when(referencedRepository.delete(entities)).thenReturn(entities);

		final var result = BulkDeleteUtils.deleteWithReferenceCheck(ids, referencedRepository, GET_ID, LABEL);

		assertThat(result)
				.hasSize(3)
				.allMatch(r -> r.success)
				.extracting(r -> r.id)
				.containsExactly(1L, 2L, 3L);
	}

	@Test
	void delete_referenced_idReferenziert_errorUndNichtAnDeleteWeitergegeben() {
		final var ids = List.of(1L, 2L);
		final var entities = List.of(new TestEntity(1L), new TestEntity(2L));
		final var unreferenced = List.of(new TestEntity(1L));
		when(referencedRepository.getReferencedIds(ids)).thenReturn(Set.of(2L));
		when(referencedRepository.findListByIds(ids)).thenReturn(entities);
		when(referencedRepository.delete(unreferenced)).thenReturn(unreferenced);

		final var result = BulkDeleteUtils.deleteWithReferenceCheck(ids, referencedRepository, GET_ID, LABEL);

		assertThat(ergebnisFuer(result, 1L).success).isTrue();
		assertThat(ergebnisFuer(result, 2L))
				.satisfies(r -> {
					assertThat(r.success).isFalse();
					assertThat(r.log).anyMatch(m -> m.contains("referenziert"));
				});
		verify(referencedRepository, never()).delete(entities);
	}

	@Test
	void delete_referenced_idNichtGefunden_error() {
		final var ids = List.of(1L, 2L);
		final var entities = List.of(new TestEntity(1L));
		when(referencedRepository.getReferencedIds(ids)).thenReturn(Set.of());
		when(referencedRepository.findListByIds(ids)).thenReturn(entities);
		when(referencedRepository.delete(entities)).thenReturn(entities);

		final var result = BulkDeleteUtils.deleteWithReferenceCheck(ids, referencedRepository, GET_ID, LABEL);

		assertThat(ergebnisFuer(result, 1L).success).isTrue();
		assertThat(ergebnisFuer(result, 2L))
				.satisfies(r -> {
					assertThat(r.success).isFalse();
					assertThat(r.log).anyMatch(m -> m.contains("nicht gefunden"));
				});
	}

	@Test
	void delete_referenced_gefundenNichtReferenziertAberNichtGeloescht_error() {
		final var ids = List.of(1L);
		final var entities = List.of(new TestEntity(1L));
		when(referencedRepository.getReferencedIds(ids)).thenReturn(Set.of());
		when(referencedRepository.findListByIds(ids)).thenReturn(entities);
		when(referencedRepository.delete(entities)).thenReturn(Collections.emptyList());

		final var result = BulkDeleteUtils.deleteWithReferenceCheck(ids, referencedRepository, GET_ID, LABEL);

		assertThat(ergebnisFuer(result, 1L))
				.satisfies(r -> {
					assertThat(r.success).isFalse();
					assertThat(r.log).anyMatch(m -> m.contains("konnte nicht gelöscht werden"));
				});
	}

	@Test
	void delete_referenced_gemischt_korrekteReihenfolge() {
		final var ids = List.of(3L, 1L, 2L);
		final var entities = List.of(new TestEntity(3L), new TestEntity(1L), new TestEntity(2L));
		final var unreferenced = List.of(new TestEntity(1L), new TestEntity(2L));
		when(referencedRepository.getReferencedIds(ids)).thenReturn(Set.of(3L));
		when(referencedRepository.findListByIds(ids)).thenReturn(entities);
		when(referencedRepository.delete(unreferenced)).thenReturn(unreferenced);

		final var result = BulkDeleteUtils.deleteWithReferenceCheck(ids, referencedRepository, GET_ID, LABEL);

		assertThat(result)
				.extracting(r -> r.id)
				.containsExactly(1L, 2L, 3L);
		assertThat(ergebnisFuer(result, 1L).success).isTrue();
		assertThat(ergebnisFuer(result, 2L).success).isTrue();
		assertThat(ergebnisFuer(result, 3L))
				.satisfies(r -> assertThat(r.success).isFalse());
	}

	@Test
	void delete_referenced_leereIdListe_leereRueckgabe() {
		when(referencedRepository.getReferencedIds(List.of())).thenReturn(Set.of());
		when(referencedRepository.findListByIds(List.of())).thenReturn(List.of());
		when(referencedRepository.delete(List.of())).thenReturn(List.of());

		final var result = BulkDeleteUtils.deleteWithReferenceCheck(List.of(), referencedRepository, GET_ID, LABEL);

		assertThat(result).isEmpty();
	}

	// --- Hilfsmethode ---

	private static SimpleOperationResponse ergebnisFuer(final List<SimpleOperationResponse> result, final long id) {
		return result.stream()
				.filter(r -> r.id == id)
				.findFirst()
				.orElseThrow(() -> new AssertionError("Kein Ergebnis für ID " + id));
	}

}
