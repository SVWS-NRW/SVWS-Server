package de.svws_nrw.db;

import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.SQLTransientConnectionException;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * Diese Exception zeigt an, dass keine Verbindung zur Datenbank aufgebaut werden konnte, z.B. weil
 * der Datenbank-Server nicht erreichbar ist oder nicht antwortet. Eine abgelehnte Anmeldung an der
 * Datenbank ist dagegen kein Verbindungsfehler und wird weiterhin als {@link DBException} gemeldet.
 */
public class DBConnectionException extends DBException {

	private static final long serialVersionUID = 4466129530786612416L;

	/**
	 * Erzeugt eine {@link DBConnectionException} mit der angegebenen textuellen Beschreibung
	 * und mit dem angegebenen Grund.
	 *
	 * @param message   die textuelle Beschreibung zu der Exception
	 * @param cause     der Grund für diese Exception
	 */
	public DBConnectionException(final String message, final Throwable cause) {
		super(message);
		initCause(cause);
	}


	/**
	 * Prüft, ob die übergebene Exception auf einen Verbindungsfehler zur Datenbank zurückgeht. Dies ist der
	 * Fall, wenn in der Ursachenkette eine SQL-Exception mit einem SQL-State der Klasse 08 (Verbindungsfehler)
	 * oder ein Netzwerkfehler vorkommt. Kommt in der Ursachenkette eine abgelehnte Anmeldung vor, so liegt
	 * kein Verbindungsfehler vor.
	 *
	 * @param e   die zu prüfende Exception, ggf. null
	 *
	 * @return true, wenn ein Verbindungsfehler vorliegt, und ansonsten false
	 */
	public static boolean istVerbindungsfehler(final Throwable e) {
		boolean verbindungsfehler = false;
		final Set<Throwable> besucht = Collections.newSetFromMap(new IdentityHashMap<>());
		for (Throwable t = e; (t != null) && besucht.add(t); t = t.getCause()) {
			if (t instanceof SQLInvalidAuthorizationSpecException) {
				return false;
			}
			if ((t instanceof SQLNonTransientConnectionException) || (t instanceof SQLTransientConnectionException)
					|| ((t instanceof final SQLException sqle) && (sqle.getSQLState() != null) && sqle.getSQLState().startsWith("08"))
					|| (t instanceof ConnectException) || (t instanceof NoRouteToHostException)
					|| (t instanceof SocketTimeoutException) || (t instanceof UnknownHostException)) {
				verbindungsfehler = true;
			}
		}
		return verbindungsfehler;
	}

}
