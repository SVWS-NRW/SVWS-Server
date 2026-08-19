package de.svws_nrw.repo;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ReferencedBulkDeletionRepository<T> {

	/**
	 *  Liefert Ids die in anderen Tabellen referenziert werden.
	 * @param idsToCheck zu überprüfende Ids
	 * @return Liste referenzierter ids
	 */
	Set<Long> getReferencedIds(List<Long> idsToCheck);

	/**
	 * Bestimmt Datenbank-Entitäten anhand von IDs. Sind für Datenbank-IDs keine
	 * Entitäten vorhanden, so werden nur die gefunden Entitäten zurückgegeben.
	 *
	 * @param ids   die IDs
	 *
	 * @return die Liste mit den gefundenen Datenbank-Entitäten
	 */
	List<T> findListByIds(Collection<Long> ids);

	/**
	 * Löscht die übergebenen Datenbank-Entitäten.
	 *
	 * @param <C>        der Typ der Collection
	 * @param entities   die zu löschenden Datenbank-Entitäten
	 *
	 * @return die gelöschten Datenbank-Entitäten
	 */
	<C extends Collection<T>> C delete(C entities);

}
