package de.svws_nrw.repo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import de.svws_nrw.db.DBEntityManager;

/**
 * Eine Factory zum Erstellen von Repositories für Datenbank-Entitäten und ggf. auch komplexere Abfragen.
 */
public abstract class RepositoryFactory {

	/** Die Datenbank-Verbindung */
	protected final DBEntityManager conn;

	/** Ein Cache für Repositories, um die Instanz-Identität innerhalb der Factory zu garantieren - Simulation von Request-Scope für Repositories */
	private final Map<Class<?>, Object> _repositoryCache = new HashMap<>();


	/**
	 * Erstellt eine neue Factory mit der übergebenen Datenbank-Verbindung
	 */
	protected RepositoryFactory() {
		this.conn = DbConnectionProvider.getConnection();
	}


	/**
	 * Liefert ein Repository aus dem Cache oder erstellt es neu, falls es noch nicht existiert.
	 * Mithilfe des Caches wird ein RequestScope simuliert.
	 *
	 * @param <T>           der Typ des Repositories
	 * @param clazz         die Klasse des Repositories als Key im Cache
	 * @param constructor   der Supplier zur Erstellung der Repository-Instanz bei einem Cache-Miss
	 *
	 * @return die Repository-Instanz für den Request
	 */
	@SuppressWarnings("unchecked")
	protected <T> T getOrCreate(final Class<T> clazz, final Supplier<T> constructor) {
		return (T) _repositoryCache.computeIfAbsent(clazz, k -> constructor.get());
	}

}
