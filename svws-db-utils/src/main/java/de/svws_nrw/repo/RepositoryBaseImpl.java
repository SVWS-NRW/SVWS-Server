package de.svws_nrw.repo;

import java.util.List;
import java.util.Optional;

import de.svws_nrw.db.DBEntityManager;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff.
 *
 * @param <P> der Typ der Primary-Key-Klasse zu den Datenbank-Entitäten
 * @param <T> der Typ der verwalteten Datenbank-Entitäten
 */
public abstract class RepositoryBaseImpl<T, P> implements RepositoryBase<T, P> {

	/** Die Datenbank-Verbindung */
	protected final DBEntityManager conn;

	/** Die Klasse zum Typ der Datenbank-Entitäten */
	protected final Class<T> entityClass;



	/**
	 * Erstellt ein neues Repository-Objekt mit der übergebenen Datenbank-Verbindung
	 *
	 * @param conn          die Datenbank-Verbindung
	 * @param entityClass   die Klasse der Datenbank-Entitäten des Repositories
	 */
	protected RepositoryBaseImpl(final DBEntityManager conn, final Class<T> entityClass) {
		this.conn = conn;
		this.entityClass = entityClass;
	}


	/**
	 * Bestimmt die erste Datenbank-Entität in einer Tabelle.
	 *
	 * @return die erste Datenbank-Entität
	 *
	 * @throws RepositoryException wenn keine Entität existiert
	 */
	@Override
	public @NotNull T getFirst() throws RepositoryException {
		final var result = conn.querySingle(entityClass);
		if (result == null)
			throw new RepositoryException("Es konnte keine Entität der Klasse " + entityClass + " bestimmt werden.");
		return result;
	}

	/**
	 * Bestimmt die erste Datenbank-Entität in einer Tabelle. Ist keine Entität
	 * vorhanden, so wird ein leerer Optional zurückgegeben.
	 *
	 * @return ein Optional mit der Datenbank-Entität
	 */
	@Override
	public Optional<T> findFirst() {
		return Optional.ofNullable(conn.querySingle(entityClass));
	}


	/**
	 * Führt ein Mapping von der übergebenen Primary-Key-Klasse auf eine Folge von Objekte
	 * durch, aus welchen sich der Primary zusammensetzt.
	 *
	 * @param id   die Primary-Key-Klasse
	 *
	 * @return die einzelnen Attribute des Primary-Keys
	 */
	protected abstract Object[] mapIdToParameter(P id);


	/**
	 * Bestimmt die Datenbank-Entität anhand der ID.
	 *
	 * @param id   die evtl. auch zusammengesetzte ID
	 *
	 * @return die Datenbank-Entität
	 *
	 * @throws RepositoryException wenn keine Entität mit der ID existiert
	 */
	@Override
	public @NotNull T getById(final P id) {
		final var result = conn.queryByKey(entityClass, mapIdToParameter(id));
		if (result == null)
			throw new RepositoryException("Es konnte keine Entität der Klasse " + entityClass + " für die angegebene ID bestimmt werden.");
		return result;
	}

	/**
	 * Bestimmt eine Datenbank-Entität anhand der ID. Ist diese nicht
	 * vorhanden, so wird ein leerer Optional zurückgegeben.
	 *
	 * @param id   die evtl. auch zusammengesetzte ID der Entität
	 *
	 * @return ein Optional mit der Datenbank-Entität
	 */
	@Override
	public Optional<T> findById(final P id) {
		return Optional.ofNullable(conn.queryByKey(entityClass, mapIdToParameter(id)));
	}


	/**
	 * Gibt eine Liste aller Datenbank-Entitäten zurück.
	 *
	 * @return die Liste mit den Datenbank-Entitäten
	 */
	@Override
	public List<T> getAll() {
		return conn.queryAll(entityClass);
	}


	/**
	 * Persistiert die Datenbank-Entität in der Datenbank. Für den Fall,
	 * dass IDs automatisch vergeben werden, wird eine neue ID bei der
	 * Entität vergeben. Die Rückgabe enthält dann die Entität mit der ID.
	 *
	 * @param entity   die zu persistierende Datenbank-Entität
	 *
	 * @return die persistierte Entität
	 *
	 * @throws RepositoryException im Fehlerfall
	 */
	@Override
	public T create(final T entity) throws RepositoryException {
		return this.update(entity);
	}


	/**
	 * Persistiert die Datenbank-Entitäten in der Datenbank. Für den Fall,
	 * dass IDs automatisch vergeben werden, werden fortlaufend neue IDs bei der
	 * Entität vergeben. Die Rückgabe enthält dann die Entitäten mit den neuen IDs.
	 *
	 * @param entities   die zu persistierenden Datenbank-Entitäten
	 *
	 * @return die persistierten Datenbank-Entitäten
	 *
	 * @throws RepositoryException im Fehlerfall
	 */
	@Override
	public List<T> create(final List<T> entities) throws RepositoryException {
		return this.update(entities);
	}


	/**
	 * Speichert bzw. aktualisiert die übergebene Datenbank-Entität.
	 *
	 * @param entity   die zu persistierende Datenbank-Entität
	 *
	 * @return die persistiere Datenbank-Entität
	 *
	 * @throws RepositoryException im Fehlerfall
	 */
	@Override
	public T update(final T entity) throws RepositoryException {
		if (conn.transactionPersist(entity))
			return entity;
		throw new RepositoryException("Fehler beim Aktualisieren der Entity vom Typ " + entityClass.getCanonicalName());
	}

	/**
	 * Speichert bzw. aktualisiert die übergebenen Datenbank-Entitäten.
	 *
	 * @param entities   die zu persistierenden Datenbank-Entitäten
	 *
	 * @return die persistierten Datenbank-Entitäten
	 *
	 * @throws RepositoryException im Fehlerfall
	 */
	@Override
	public List<T> update(final List<T> entities) throws RepositoryException {
		if (conn.transactionPersistAll(entities))
			return entities;
		throw new RepositoryException("Fehler beim Aktualisieren der Entitäten vom Typ " + entityClass.getCanonicalName());
	}

	/**
	 * Löscht die übergebene Datenbank-Entität.
	 *
	 * @param entity   die zu löschende Datenbank-Entität
	 *
	 * @return die gelöschte Datenbank-Entitäten
	 *
	 * @throws RepositoryException im Fehlerfall
	 */
	@Override
	public T delete(final T entity) throws RepositoryException {
		if (conn.transactionRemove(entity))
			return entity;
		throw new RepositoryException("Fehler beim Löschen der Entity vom Typ " + entityClass.getCanonicalName());
	}

	/**
	 * Löscht die übergebenen Datenbank-Entitäten.
	 *
	 * @param entities   die zu löschenden Datenbank-Entitäten
	 *
	 * @return die gelöschten Datenbank-Entitäten
	 */
	@Override
	public List<T> delete(final List<T> entities) throws RepositoryException {
		if (conn.transactionRemoveAll(entities))
			return entities;
		throw new RepositoryException("Fehler beim Löschen der Entitäten vom Typ " + entityClass.getCanonicalName());
	}

	/**
	 * Schreibt alle noch ausstehenden Änderungen in die Datenbank.
	 */
	@Override
	public void flush() {
		conn.transactionFlush();
	}

}
