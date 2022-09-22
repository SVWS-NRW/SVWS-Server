package de.nrw.schule.svws.db;

import java.net.SocketException;
import java.sql.SQLNonTransientConnectionException;

import org.eclipse.persistence.exceptions.DatabaseException;
import org.eclipse.persistence.exceptions.ExceptionHandler;
import org.mariadb.jdbc.internal.util.exceptions.MariaDbSqlException;


/**
 * Dieser Handler dient dazu auf einzelne Exceptions zu reagieren, die von der Jakarta Persistence API (JPA)
 * bei Datenbankanfragen oder anderweitigen Datenbankoperationen erzeugt werden. 
 */
// TODO Dieser Code kann entfernt werden, wenn es unter Windows-System zu keinen Schwierigkeiten mehr kommen sollte, falls Verbindungen von DB- oder Netzwerkseite verloren gehen und dies vom Server nicht bemerkt wird.   
@Deprecated
public class DBExceptionHandler implements ExceptionHandler {

	@Override
	public Object handleException(RuntimeException exception) {
		if ((exception != null) && (exception instanceof DatabaseException de)) {
			if ((de.getCause() != null) && (de.getCause() instanceof SQLNonTransientConnectionException sqlntce)) {
				if ((sqlntce.getCause() != null) && (sqlntce.getCause() instanceof MariaDbSqlException mdbsqle)) {
					if ((mdbsqle.getCause() != null) && (mdbsqle.getCause() instanceof SQLNonTransientConnectionException sqlntce2)) {
						if ((sqlntce2.getCause() != null) && (sqlntce2.getCause() instanceof SocketException se)) {
//							System.err.println("Socket Exception: " + se.getMessage());
//							throw exception;								
						}
					}
//					System.err.println("Database Connection Error: " + mdbsqle.getMessage());
//					de.printStackTrace();
//					System.err.println("Stopping Server!");
//					System.exit(42);
				}
			}
		}
		throw exception;
	}

}
