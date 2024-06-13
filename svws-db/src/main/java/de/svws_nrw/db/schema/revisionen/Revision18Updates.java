package de.svws_nrw.db.schema.revisionen;

import java.util.List;

import de.svws_nrw.base.crypto.Passwords;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.ext.jbcrypt.BCrypt;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 18.
 */
public final class Revision18Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 18.
	 */
	public Revision18Updates() {
		super(SchemaRevisionen.REV_18);
	}

	@Override
	public boolean runLast(final DBEntityManager conn, final Logger logger) {
		if (conn.getDBDriver() != DBDriver.MARIA_DB) {
			logger.logLn("DBMS wird für dieses Datenbank Revisions-Update nicht unterstützt.");
			return false;
		}
		conn.transactionFlush();
		// Lese die Einträge aus der Tabelle K_Lehrer ein, welche bei KennwortTools ein Kennwort gesetzt haben und übertrage dieses in die neue Tabelle LehrerNotenmodulCredentials
		final List<Object[]> list = conn.queryNative("SELECT ID, KennwortTools FROM K_Lehrer WHERE KennwortTools IS NOT NULL");
		for (final Object[] datensatz : list) {
			final long idLehrer = ((Number) datensatz[0]).longValue();
			final String altKennwort = String.valueOf(datensatz[1]);
			final boolean isBCryptHash = altKennwort.startsWith("$2a$10$");
			final String initial = isBCryptHash ? Passwords.generateRandomPasswordWithoutSpecialChars(10) : altKennwort;
			final String hash = isBCryptHash ? altKennwort : BCrypt.hashpw(altKennwort, BCrypt.gensalt());
			conn.transactionNativeUpdate("INSERT INTO LehrerNotenmodulCredentials(Lehrer_ID, Initialkennwort, PasswordHash) VALUES (%d,'%s','%s')"
					.formatted(idLehrer, initial, hash));
		}
		conn.transactionFlush();
		return true;
	}

}
