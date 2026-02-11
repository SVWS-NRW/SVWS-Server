package de.svws_nrw.repo;

import java.util.Collection;
import java.util.List;

/**
 * Ein generisches Interface für den Datenbank-Zugriff nach dem Repository-Pattern.
 *
 * @param <T> der Typ der verwalteten Datenbank-Entität
 */
public interface Repository<T> extends RepositoryBase<T, Long> {

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
	 * Bestimmt die nächste freie ID, sofern es sich um eine einfache ID des Typs long
	 * handelt. In allen anderen Fällen wird 1 zurückgegeben.
	 *
	 * @return die nächste freie ID oder 1
	 */
	long getNextID();

}
