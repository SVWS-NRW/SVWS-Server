package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.DBException;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 43.
 */
public final class Revision43Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates für Revision 43.
	 */
	public Revision43Updates() {
		super(SchemaRevisionen.REV_43);
	}

	@Override
	public boolean runFirst(final DBEntityManager conn, final Logger logger) {
		try {
			if (conn.getDBDriver() != DBDriver.MARIA_DB) {
				logger.logLn("DBMS wird für dieses Datenbank Revisions-Update nicht unterstützt.");
				return false;
			}
			logger.logLn("- LehrerLehramtFachr: Anlegen der Spalte LehramtKrz, falls diese noch nicht vorhanden ist.");
			String sql = "ALTER TABLE %s ADD COLUMN IF NOT EXISTS LehramtKrz varchar(10) NOT NULL COMMENT 'Lehramtskürzel'"
					.formatted(Schema.tab_LehrerLehramtFachr.name());
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql))
				throw new DBException("Fehler beim Anlegen der Spalte LehramtKrz");
			conn.transactionFlush();
			logger.logLn("- LehrerLehramtFachr: Neusetzen des Primärschlüssels mit der Spalte LehramtKrz.");
			sql = "ALTER TABLE %s DROP PRIMARY KEY, ADD PRIMARY KEY (Lehrer_ID, LehramtKrz, FachrKrz)"
					.formatted(Schema.tab_LehrerLehramtFachr.name());
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql))
				throw new DBException("Fehler beim Neusetzen des Primärschlüssels.");
			conn.transactionFlush();
			logger.logLn("- LehrerLehramtLehrbef: Anlegen der Spalte LehramtKrz, falls diese noch nicht vorhanden ist.");
			sql = "ALTER TABLE %s ADD COLUMN IF NOT EXISTS LehramtKrz varchar(10) NOT NULL COMMENT 'Lehramtskürzel'"
					.formatted(Schema.tab_LehrerLehramtLehrbef.name());
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql))
				throw new DBException("Fehler beim Anlegen der Spalte LehramtKrz");
			conn.transactionFlush();
			logger.logLn("- LehrerLehramtLehrbef: Neusetzen des Primärschlüssels mit der Spalte LehramtKrz.");
			sql = "ALTER TABLE %s DROP PRIMARY KEY, ADD PRIMARY KEY (Lehrer_ID, LehramtKrz, LehrbefKrz)"
					.formatted(Schema.tab_LehrerLehramtLehrbef.name());
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql))
				throw new DBException("Fehler beim Neusetzen des Primärschlüssels.");
			conn.transactionFlush();
			return true;
		} catch (final DBException e) {
			logger.logLn(e.getMessage());
			return false;
		}
	}

}
