package de.svws_nrw.db.schema.revisionen;

import java.util.List;

import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.ext.jbcrypt.BCrypt;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 80.
 */
public final class Revision80Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 80.
	 */
	public Revision80Updates() {
		super(SchemaRevisionen.REV_80);
	}


	@Override
	public boolean runLast(final DBEntityManager conn, final Logger logger) {
		if (conn.getDBDriver() != DBDriver.MARIA_DB) {
			logger.logLn("DBMS wird für dieses Datenbank Revisions-Update nicht unterstützt.");
			return false;
		}

		logger.logLn("Prüfe die Notenmodul-Credentials der Lehrer darauf, ob bereits ein neues Kennwort genutzt wird oder noch das Initialkennwort. Speichere diese Information in der Datenbank.");
		logger.modifyIndent(2);
		final List<Object[]> listCreds = conn.queryNative("SELECT idLehrer, initialkennwort, passwordHash, istInitialkennwort FROM Notenmodul_Credentials");
		Long idLehrer = null;
		String initialkennwort = null;
		String passwordHash = null;
		Integer istInitialkennwort = -1;
		for (final Object[] cred : listCreds) {
			idLehrer = (Long) cred[0];
			initialkennwort = (String) cred[1];
			passwordHash = (String) cred[2];
			istInitialkennwort = (Integer) cred[3];
			boolean check = false;
			try {
				check = (initialkennwort != null) && (passwordHash != null) && (!passwordHash.isBlank())
						&& BCrypt.checkpw(initialkennwort, passwordHash);
			} catch (@SuppressWarnings("unused") final Exception e) {
				check = false;
			}
			if (check && (istInitialkennwort != 1)) {
				conn.transactionNativeUpdate("UPDATE Notenmodul_Credentials SET istInitialkennwort = 1 WHERE idLehrer = %d".formatted(idLehrer));
			} else if (!check && istInitialkennwort != 0) {
				conn.transactionNativeUpdate("UPDATE Notenmodul_Credentials SET istInitialkennwort = 0 WHERE idLehrer = %d".formatted(idLehrer));
			}
		}
		logger.modifyIndent(-2);
		return true;
	}

}
