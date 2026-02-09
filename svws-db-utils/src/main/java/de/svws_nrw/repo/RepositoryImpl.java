package de.svws_nrw.repo;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.ObjLongConsumer;

import de.svws_nrw.db.DBEntityManager;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff.
 *
 * @param <T> der Typ der verwalteten Datenbank-Entitäten
 */
public abstract class RepositoryImpl<T> extends RepositoryBaseImpl<T, Long> implements Repository<T> {

	/** eine optionale Methode, um die Long-ID automatisch zu setzen, wenn ein Objekt neu erstellt wird */
	protected final @NotNull ObjLongConsumer<T> setId;


	/**
	 * Erstellt ein neues Repository-Objekt mit der übergebenen Datenbank-Verbindung
	 *
	 * @param conn          die Datenbank-Verbindung
	 * @param entityClass   die Klasse der Datenbank-Entitäten
	 * @param setId         eine Methode, um die ID automatisch zu setzen, wenn ein Objekt neu erstellt wird
	 */
	protected RepositoryImpl(final DBEntityManager conn, final Class<T> entityClass, final ObjLongConsumer<T> setId) {
		super(conn, entityClass);
		if (setId == null)
			throw new IllegalArgumentException("Ein Consumer setId ist für ein Repository erforderlich.");
		this.setId = setId;
	}


	@Override
	protected final Object[] mapIdToParameter(final Long id) {
		return new Object[]{ id };
	}


	/**
	 * Bestimmt Datenbank-Entitäten anhand von IDs. Sind für Datenbank-IDs keine
	 * Entitäten vorhanden, so werden nur die gefunden Entitäten zurückgegeben.
	 *
	 * @param ids   die IDs
	 *
	 * @return die Liste mit den gefundenen Datenbank-Entitäten
	 */
	@Override
	public List<T> findListByIds(final Collection<Long> ids) {
		if ((ids == null) || (ids.isEmpty()))
			return Collections.emptyList();
		return conn.queryByKeyList(entityClass, ids);
	}


	/**
	 * Bestimmt die nächste freie ID, sofern es sich um eine einfache ID des Typs long
	 * handelt. In allen anderen Fällen wird 1 zurückgegeben.
	 *
	 * @return die nächste freie ID oder 1
	 */
	@Override
	public long getNextID() {
		return conn.transactionGetNextID(entityClass);
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
		// Bestimme ggf. zunächst die nächste freie ID in der Datenbank und setzt diese bei der Entität über den übergebenen Consumer
		setId.accept(entity, this.getNextID());
		// und persistiere die Entität
		return super.create(entity);
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
		// Bestimme ggf. zunächst die nächste freie ID in der Datenbank und ergänze die fehlenden IDs bei den Entitäten
		long firstId = this.getNextID();
		for (final T entity : entities) {
			setId.accept(entity, firstId++);
		}
		// und persistiere die Entitäten
		return super.create(entities);
	}

}
