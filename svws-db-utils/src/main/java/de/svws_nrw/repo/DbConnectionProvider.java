package de.svws_nrw.repo;

import org.jboss.resteasy.spi.ResteasyProviderFactory;

import de.svws_nrw.db.DBEntityManager;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Diese Klasse stellt eine Hilfsmethode zur Verfügung, um auf die Datenbank-Verbindung des aktuellen Requests zuzugreifen
 */
public final class DbConnectionProvider {

	private DbConnectionProvider() {
		// Dies ist eine Utility-Klasse ohne eigene Instanzen
	}

	/**
	 * Bestimmt die Datenbank-Verbindung anhand des aktuellen Requests.
	 *
	 * @return die Datenbank-Verbindung
	 */
	public static DBEntityManager getConnection() {
		final HttpServletRequest request = ResteasyProviderFactory.getInstance().getContextData(HttpServletRequest.class);
		if (request == null) {
			throw new IllegalStateException("Es konnte kein aktueller Request ausgelesen werden.");
		}

		final Object daten = request.getAttribute("connection");
		if (daten instanceof final DBEntityManager conn) {
			return conn;
		}
		throw new IllegalStateException("Es konnte keine Datenbank-Verbindung für den aktuellen Request ausgelesen werden.");
	}

}
