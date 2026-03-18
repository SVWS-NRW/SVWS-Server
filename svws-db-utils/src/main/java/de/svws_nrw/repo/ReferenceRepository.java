package de.svws_nrw.repo;

import java.util.List;
import java.util.Set;

public interface ReferenceRepository<T> extends Repository<T> {

	/**
	 *  Liefert Ids die in anderen Tabellen referenziert werden.
	 * @param idsToDelete zu überprüfende Ids
	 * @return Liste referenzierter ids
	 */
	Set<Long> getReferencedIds(List<Long> idsToDelete);
}
