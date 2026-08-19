package de.svws_nrw.service.utils;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import de.svws_nrw.core.data.SimpleOperationResponse;

public final class DeleteUtils {

	private DeleteUtils() {
	}

	/**
	 * Führt einen transaktionalen Löschvorgang für eine Liste von IDs durch.
	 * Referenzierte oder nicht gefundene Einträge werden nicht gelöscht,
	 * sondern mit einer Fehlermeldung in der Rückgabe markiert.
	 *
	 * @param <T>                  der Typ der Datenbank-Entität
	 * @param idsToDelete          die zu löschenden IDs
	 * @param getReferencedIds     liefert die Menge der referenzierten IDs
	 * @param findListByIds        liefert die gefundenen Entitäten anhand der IDs
	 * @param deleteFunction       löscht eine Liste von Entitäten und gibt die gelöschten zurück
	 * @param getId                extrahiert die ID aus einer Entität
	 * @param entityLabel          fachlicher Name der Entität für Fehlermeldungen (z. B. "Ort")
	 * @return Liste von {@link SimpleOperationResponse}-Einträgen, aufsteigend nach ID sortiert
	 */
	public static <T> List<SimpleOperationResponse> delete(
			final List<Long> idsToDelete,
			final Function<List<Long>, Set<Long>> getReferencedIds,
			final Function<List<Long>, List<T>> findListByIds,
			final UnaryOperator<List<T>> deleteFunction,
			final Function<T, Long> getId,
			final String entityLabel) {

		final var referencedIds = getReferencedIds.apply(idsToDelete);
		final var entitiesToDelete = findListByIds.apply(idsToDelete);
		final var foundIds = entitiesToDelete.stream().map(getId).toList();

		final var unreferenced = entitiesToDelete.stream()
				.filter(e -> !referencedIds.contains(getId.apply(e)))
				.toList();

		final var deletedIds = deleteFunction.apply(unreferenced).stream().map(getId).toList();

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
