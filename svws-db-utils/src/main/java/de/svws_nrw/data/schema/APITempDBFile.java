package de.svws_nrw.data.schema;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;

import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.DBConfig;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.utils.OperationError;

/**
 * Diese Klasse repräsentiert eine temporäre Datei, welche für eine MDB oder SQLite-DB
 * erzeugt wird, wenn diese über die API-Schnittstelle an den SVWS-Server übergeben wird.
 */
public class APITempDBFile implements AutoCloseable {

	/** Ein Zufallszahlen-Generator */
	private static final Random random = new Random();

	private final DBDriver _dbms;

	/** Der zu verwendende Logger */
	private final Logger _logger;

	/** Das Verzeichnis, wo die temporäre MDB-Datei abgelegt wird */
	private final String _tmpDir;

	/** Der Dateiname für die temporäre MDB-Datei */
	private final String _tmpFilename;

	/** Das Kennwort für den Zugriff auf die MDB */
	private final String _password;

	/** Gibt an, ob die close-Methode sich um das Löschen der temporären Datei kümmert oder nicht */
	private final boolean _doDelete;

	/**
	 * Erzeugt eine einfache Anwort mit der Angabe, ob die Operation erfolgreich war und
	 * mit dem Log derOperation.
	 *
	 * @param success   gibt an, ob die Operation erfolgreich war oder nicht
	 * @param log       der Log der Operation
	 *
	 * @return das Response-Objekt
	 */
	private static SimpleOperationResponse simpleResponse(final boolean success, final LogConsumerList log) {
		final SimpleOperationResponse response = new SimpleOperationResponse();
		response.success = success;
		response.log = log.getStrings();
		return response;
	}

	/**
	 * Erzeugt eine neue temporäre Datei für die Datenbank (MDB oder SQLite).
	 *
	 * @param dbms         das DBMS
	 * @param praefix      der Präfix für die temporäre Datei (z.B. der Name des Zielschemas)
	 * @param logger       der zu verwendende Logger
	 * @param log          die Liste, welche die Meldungen der Loggers mitprotokolliert
	 * @param data         die DB als Byte-Array
	 * @param password     ggf. das Kennwort für die DB
	 * @param doDelete     gibt an, ob die Datei beim close gelöscht werden soll oder ob sich die aufrufende Methode darum kümmert.
	 */
	public APITempDBFile(final DBDriver dbms, final String praefix, final Logger logger, final LogConsumerList log,
			final byte[] data, final String password, final boolean doDelete) {
		this._dbms = dbms;
		this._doDelete = doDelete;
		this._logger = logger;
		if (!dbms.isFileBased()) {
			_logger.logLn("Fehler: Das DBMS %s wird für das Erstellen von temporären DBMS nicht unterstützt.");
			throw OperationError.INTERNAL_SERVER_ERROR.exception(simpleResponse(false, log));
		}
		this._password = password;
    	// Erstelle temporär eine Datenbank-Datei aus dem übergebenen Byte-Array
		_tmpDir = SVWSKonfiguration.get().getTempPath();
		_tmpFilename = praefix +  "_" + random.ints(48, 123)  // from 0 to z
          .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))  // filter some unicode characters
          .limit(40)
          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
          .toString() + "." + dbms.getFileSuffix();
        _logger.logLn("Erstelle eine temporäre Datenbank unter dem Namen \"" + _tmpDir + "/" + _tmpFilename + "\"");
    	try {
    		Files.createDirectories(Paths.get(_tmpDir));
    		// Schreibe Daten nur, wenn die DB für den Import genutzt wird. Bei einem Export ist die Datei zunächst leer
    		if (data != null)
    			Files.write(Paths.get(_tmpDir + "/" + _tmpFilename), data, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
		} catch (@SuppressWarnings("unused") final IOException e) {
			_logger.logLn(2, "Fehler beim Erstellen der temporären Datenbank unter dem Namen \"" + _tmpDir + "/" + _tmpFilename + "\"");
			throw OperationError.INTERNAL_SERVER_ERROR.exception(simpleResponse(false, log));
		}
	}


	/**
	 * Gibt den Dateinamen der temporären Datei zurück.
	 *
	 * @return der Dateiname
	 */
	public String getFilename() {
		return _tmpDir + "/" + _tmpFilename;
	}


	/**
	 * Erstellt eine Datenbank-Konfiguration für den Zugriff auf die temporär angelegte Datenbank.
	 *
	 * @return die Datenbank-Konfiguration
	 */
	public DBConfig getConfig() {
		final String username = (_dbms == DBDriver.MDB) ? "admin" : null;
		return new DBConfig(_dbms, _tmpDir + "/" + _tmpFilename, "PUBLIC", false, username, _password, true, false, 0, 0);
	}


	/**
	 * Entfernt die Datei mit der temporären Datenbank wieder.
	 */
	@Override
	public void close() {
		_logger.logLn("Löschen der temporären Datenbank unter dem Namen \"" + _tmpDir + "/" + _tmpFilename + "\".");
		try {
			if (_doDelete)
				Files.delete(Paths.get(_tmpDir + "/" + _tmpFilename));
		} catch (@SuppressWarnings("unused") final IOException e) {
			_logger.logLn(2, "[FEHLER]");
		}
	}

}
