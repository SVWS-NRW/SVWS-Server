package de.svws_nrw.repo;

import java.util.List;
import java.util.Optional;

import jakarta.validation.constraints.NotNull;

/**
 * Ein generisches Interface für den Datenbank-Zugriff nach dem Repository-Pattern.
 *
 * @param <T> der Typ der verwalteten Datenbank-Entität
 * @param <P> der Typ des Primary-Keys
 */
public interface RepositoryBase<T, P> {

	/**
	 * Bestimmt die erste Datenbank-Entität in einer Tabelle.
	 *
	 * @return die erste Datenbank-Entität
	 *
	 * @throws RepositoryException wenn keine Entität existiert
	 */
	@NotNull T getFirst() throws RepositoryException;

	/**
	 * Bestimmt die erste Datenbank-Entität in einer Tabelle. Ist keine Entität
	 * vorhanden, so wird ein leerer Optional zurückgegeben.
	 *
	 * @return ein Optional mit der Datenbank-Entität
	 */
	Optional<T> findFirst();

	/**
	 * Bestimmt die Datenbank-Entität anhand der ID.
	 *
	 * @param id   die evtl. auch zusammengesetzte ID
	 *
	 * @return die Datenbank-Entität
	 *
	 * @throws RepositoryException wenn keine Entität mit der ID existiert
	 */
	@NotNull T getById(P id);

	/**
	 * Bestimmt eine Datenbank-Entität anhand der ID. Ist diese nicht
	 * vorhanden, so wird ein leerer Optional zurückgegeben.
	 *
	 * @param id   die evtl. auch zusammengesetzte ID der Entität
	 *
	 * @return ein Optional mit der Datenbank-Entität
	 */
	Optional<T> findById(P id);

	/**
	 * Gibt eine Liste aller Datenbank-Entitäten zurück.
	 *
	 * @return die Liste mit den Datenbank-Entitäten
	 */
	List<T> getAll();

	/**
	 * Persistiert die Datenbank-Entität in der Datenbank. Für den Fall,
	 * dass IDs automatisch vergeben werden, wird eine neue ID bei der
	 * Entität vergeben. Die Rückgabe enthält dann die Entität mit der ID.
	 *
	 * @param entity   die zu persistierende Datenbank-Entität
	 *
	 * @return die persistierte Entität
	 */
	T create(T entity);

	/**
	 * Persistiert die Datenbank-Entitäten in der Datenbank. Für den Fall,
	 * dass IDs automatisch vergeben werden, werden fortlaufend neue IDs bei der
	 * Entität vergeben. Die Rückgabe enthält dann die Entitäten mit den neuen IDs.
	 *
	 * @param entities   die zu persistierenden Datenbank-Entitäten
	 *
	 * @return die persistierten Datenbank-Entitäten
	 */
	List<T> create(List<T> entities);

	/**
	 * Speichert bzw. aktualisiert die übergebene Datenbank-Entität.
	 *
	 * @param entity   die zu persistierende Datenbank-Entität
	 *
	 * @return die persistiere Datenbank-Entität
	 */
	T update(T entity);

	/**
	 * Speichert bzw. aktualisiert die übergebenen Datenbank-Entitäten.
	 *
	 * @param entities   die zu persistierenden Datenbank-Entitäten
	 *
	 * @return die persistierten Datenbank-Entitäten
	 */
	List<T> update(List<T> entities);

	/**
	 * Löscht die übergebene Datenbank-Entität.
	 *
	 * @param entity   die zu löschende Datenbank-Entität
	 *
	 * @return die gelöschte Datenbank-Entität
	 */
	T delete(T entity);

	/**
	 * Löscht die übergebenen Datenbank-Entitäten.
	 *
	 * @param entities   die zu löschenden Datenbank-Entitäten
	 *
	 * @return die gelöschten Datenbank-Entitäten
	 */
	List<T> delete(List<T> entities);

	/**
	 * Schreibt alle noch ausstehenden Änderungen in die Datenbank.
	 */
	void flush();

}
