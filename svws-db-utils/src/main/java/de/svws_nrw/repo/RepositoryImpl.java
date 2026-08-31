package de.svws_nrw.repo;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.ObjLongConsumer;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

import de.svws_nrw.db.DBEntityManager;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Repository-Klasse dient dem Datenbank-Zugriff.
 *
 * @param <T> der Typ der verwalteten Datenbank-Entitäten
 */
public abstract class RepositoryImpl<T> extends RepositoryBaseImpl<T, Long> implements Repository<T> {

	/** eine Methode, um die Long-ID automatisch zu setzen, u.a. wenn ein Objekt neu erstellt wird */
	protected final @NotNull ObjLongConsumer<T> setId;

	/** eine Methode, um die Long-ID auszulesen */
	protected final @NotNull ToLongFunction<T> getId;


	/**
	 * Erstellt ein neues Repository-Objekt mit der übergebenen Datenbank-Verbindung
	 *
	 * @param conn          die Datenbank-Verbindung
	 * @param entityClass   die Klasse der Datenbank-Entitäten
	 * @param getId         eine Methode, um die ID auszulesen
	 * @param setId         eine Methode, um die ID automatisch zu setzen, z.B. wenn ein Objekt neu erstellt wird
	 */
	protected RepositoryImpl(final DBEntityManager conn, final Class<T> entityClass,
			final ToLongFunction<T> getId, final ObjLongConsumer<T> setId) {
		super(conn, entityClass);
		if (setId == null) {
			throw new IllegalArgumentException("Ein Consumer setId ist für ein Repository erforderlich.");
		}
		this.getId = getId;
		this.setId = setId;
	}


	@Override
	protected final Object[] mapIdToParameter(final Long id) {
		return new Object[]{ id };
	}


	/**
	 * Erstellt eine Map mit allen Datenbank-Entitäten und ordnet diese ihrer ID zu.
	 *
	 * @return die Map mit der Zuordnung der Datenbank-Entitäten zu ihren IDs
	 */
	@Override
	public Map<Long, T> getMap() {
		return super.getMap(this.getId::applyAsLong);
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
		if ((ids == null) || (ids.isEmpty())) {
			return Collections.emptyList();
		}
		return conn.queryByKeyList(entityClass, ids);
	}


	/**
	 * Erstellt eine Map mit den zu den übergebenen IDs gehörenden Datenbank-Entitäten und ordnet
	 * diese ihrer jeweiligen ID zu.
	 *
	 * @param ids   die IDs
	 *
	 * @return die Map mit der Zuordnung der Datenbank-Entitäten zu ihren IDs
	 */
	@Override
	public Map<Long, T> findMapByIds(final Collection<Long> ids) {
		return this.findListByIds(ids).stream().filter(Objects::nonNull).collect(Collectors.toMap(this.getId::applyAsLong, e -> e));
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
	 * Gibt an, ob beim Anlegen einer neuen Entität automatisch eine neue ID vergeben werden soll.
	 * <p>
	 * Der Standardwert ist {@code true} und eignet sich für Entitäten mit eigenem Auto-Increment-PK.
	 * Subklassen, deren Primärschlüssel gleichzeitig ein Fremdschlüssel ist (FK-als-PK-Muster,
	 * z. B. 1:1-Erweiterungstabellen), überschreiben diese Methode mit {@code false},
	 * damit die bereits gesetzte ID nicht durch {@link #getNextID()} überschrieben wird.
	 *
	 * @return {@code true}, wenn eine neue ID per {@link #getNextID()} vergeben werden soll,
	 *         {@code false}, wenn die ID der Entität unverändert übernommen wird
	 */
	protected boolean autoAssignId() {
		return true;
	}

	/**
	 * Persistiert die Datenbank-Entität in der Datenbank.
	 * <p>
	 * Gibt {@link #autoAssignId()} {@code true} zurück, wird vor dem Persistieren
	 * eine neue ID per {@link #getNextID()} ermittelt und an der Entität gesetzt.
	 * Andernfalls wird die bereits an der Entität gesetzte ID unverändert übernommen.
	 *
	 * @param entity   die zu persistierende Datenbank-Entität
	 *
	 * @return die persistierte Entität
	 *
	 * @throws RepositoryException im Fehlerfall
	 */
	@Override
	public T create(final T entity) throws RepositoryException {
		if (autoAssignId()) {
			setId.accept(entity, this.getNextID());
		}
		return super.create(entity);
	}


	/**
	 * Persistiert die Datenbank-Entitäten in der Datenbank. Für den Fall,
	 * dass IDs automatisch vergeben werden, werden fortlaufend neue IDs bei der
	 * Entität vergeben. Die Rückgabe enthält dann die Entitäten mit den neuen IDs.
	 *
	 * @param <C>        der Typ der Collection
	 * @param entities   die zu persistierenden Datenbank-Entitäten
	 *
	 * @return die persistierten Datenbank-Entitäten
	 *
	 * @throws RepositoryException im Fehlerfall
	 */
	@Override
	public <C extends Collection<T>> C create(final C entities) throws RepositoryException {
		// Bestimme ggf. zunächst die nächste freie ID in der Datenbank und ergänze die fehlenden IDs bei den Entitäten
		long firstId = this.getNextID();
		for (final T entity : entities) {
			setId.accept(entity, firstId++);
		}
		// und persistiere die Entitäten
		return super.create(entities);
	}

}
