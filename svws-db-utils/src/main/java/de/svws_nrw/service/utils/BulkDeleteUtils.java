package de.svws_nrw.service.utils;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.repo.ReferencedBulkDeletionRepository;
import de.svws_nrw.repo.Repository;

/**
 * Utility-Klasse für transaktionale Bulk-Löschvorgänge.
 * Nicht gefundene oder referenzierte Einträge werden nicht gelöscht,
 * sondern mit einer Fehlermeldung in der Rückgabe markiert.
 */
public final class BulkDeleteUtils {

	private BulkDeleteUtils() {
	}

	/**
	 * Führt einen transaktionalen Löschvorgang für eine Liste von IDs durch.
	 *
	 * @param <T>         der Typ der Datenbank-Entität
	 * @param idsToDelete die zu löschenden IDs
	 * @param repository  Repository
	 * @param getId       extrahiert die ID aus einer Entität
	 * @param entityLabel fachlicher Name der Entität für Fehlermeldungen (z. B. "Ort")
	 * @return Liste von {@link SimpleOperationResponse}-Einträgen, aufsteigend nach ID sortiert
	 */
	public static <T> List<SimpleOperationResponse> delete(
			final List<Long> idsToDelete,
			final Repository<T> repository,
			final Function<T, Long> getId,
			final String entityLabel) {

		final var entitiesToDelete = repository.findListByIds(idsToDelete);
		final var foundIds = entitiesToDelete.stream().map(getId).toList();
		final var deletedIds = repository.delete(entitiesToDelete).stream().map(getId).toList();
		return buildResponseLog(idsToDelete, foundIds, Set.of(), deletedIds, entityLabel);
	}

	/**
	 * Führt einen transaktionalen Löschvorgang für eine Liste von IDs durch.
	 * Referenzierte Einträge werden nicht gelöscht, sondern mit einer Fehlermeldung markiert.
	 *
	 * @param <T>                            der Typ der Datenbank-Entität
	 * @param idsToDelete                    die zu löschenden IDs
	 * @param referencedBulkDeletionRepository Repository mit Referenzprüfung
	 * @param getId                          extrahiert die ID aus einer Entität
	 * @param entityLabel                    fachlicher Name der Entität für Fehlermeldungen (z. B. "Ort")
	 * @return Liste von {@link SimpleOperationResponse}-Einträgen, aufsteigend nach ID sortiert
	 */
	public static <T> List<SimpleOperationResponse> deleteWithReferenceCheck(
			final List<Long> idsToDelete,
			final ReferencedBulkDeletionRepository<T> referencedBulkDeletionRepository,
			final Function<T, Long> getId,
			final String entityLabel) {

		final var referencedIds = referencedBulkDeletionRepository.getReferencedIds(idsToDelete);
		final var entitiesToDelete = referencedBulkDeletionRepository.findListByIds(idsToDelete);
		final var foundIds = entitiesToDelete.stream().map(getId).toList();

		final var unreferenced = entitiesToDelete.stream()
				.filter(e -> !referencedIds.contains(getId.apply(e)))
				.toList();

		final var deletedIds = referencedBulkDeletionRepository.delete(unreferenced).stream().map(getId).toList();
		return buildResponseLog(idsToDelete, foundIds, referencedIds, deletedIds, entityLabel);
	}

	private static List<SimpleOperationResponse> buildResponseLog(
			final List<Long> idsToDelete,
			final List<Long> foundIds,
			final Set<Long> referencedIds,
			final List<Long> deletedIds,
			final String entityLabel) {

		return idsToDelete.stream()
				.map(id -> {
					if (!foundIds.contains(id)) {
						return SimpleOperationResponse.ofError(id,
								"%s mit ID %d wurde nicht gefunden.".formatted(entityLabel, id));
					}
					if (referencedIds.contains(id)) {
						return SimpleOperationResponse.ofError(id,
								"%s mit ID %d ist referenziert und kann nicht gelöscht werden.".formatted(entityLabel, id));
					}
					if (!deletedIds.contains(id)) {
						return SimpleOperationResponse.ofError(id,
								"%s mit ID %d konnte nicht gelöscht werden.".formatted(entityLabel, id));
					}
					return SimpleOperationResponse.ofSuccess(id);
				})
				.sorted(Comparator.comparingLong(r -> r.id))
				.toList();
	}

}
