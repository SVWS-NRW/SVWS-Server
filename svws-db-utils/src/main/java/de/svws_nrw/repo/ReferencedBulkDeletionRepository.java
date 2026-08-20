package de.svws_nrw.repo;

import java.util.List;
import java.util.Set;

public interface ReferencedBulkDeletionRepository<T> extends Repository<T> {

	/**
	 * Liefert die IDs, die in anderen Tabellen referenziert werden.
	 *
	 * @param idsToCheck zu überprüfende IDs
	 * @return Menge referenzierter IDs
	 */
	Set<Long> getReferencedIds(List<Long> idsToCheck);

}
