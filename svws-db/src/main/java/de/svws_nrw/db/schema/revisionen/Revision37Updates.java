package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.DBException;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelleTrigger;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 37.
 */
public final class Revision37Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 37.
	 */
	public Revision37Updates() {
		super(SchemaRevisionen.REV_37);
	}

	private static void dropTrigger(final DBEntityManager conn, final Logger logger, final SchemaTabelleTrigger trig) throws DBException {
		final var sql = trig.getSQL(conn.getDBDriver(), false);
		if ((sql == null) || ("".equals(sql)))
			throw new DBException("Kein SQL-Befehl für das Entfernen des Triggers " + trig.name() + " vorhanden");
		if (conn.transactionNativeUpdateAndFlush(sql) == Integer.MIN_VALUE)
			throw new DBException("Fehler beim Entfernen des Trigger " + trig.name());
		logger.logLn("  ... entfernt");
	}

	private static void createTrigger(final DBEntityManager conn, final Logger logger, final SchemaTabelleTrigger trig) throws DBException {
		final String sql = trig.getSQL(conn.getDBDriver(), true);
		if ((sql == null) || ("".equals(sql)))
			throw new DBException("Kein SQL-Befehl für das Erstellen des Triggers " + trig.name() + " vorhanden");
		logger.logLn(trig.name());
		if (conn.transactionExecuteWithJDBCConnection(sql) == Integer.MIN_VALUE)
			throw new DBException("Fehler beim Erstellen des Trigger " + trig.name());
		logger.logLn("  ...neu erstellt");
	}

	private static void recreateTrigger(final DBEntityManager conn, final Logger logger, final SchemaTabelleTrigger trig) throws DBException {
		logger.logLn("Erneuere Trigger " + trig.name() + "...");
		dropTrigger(conn, logger, trig);
		createTrigger(conn, logger, trig);
		conn.transactionFlush();
	}

	@Override
	public boolean runLast(final DBEntityManager conn, final Logger logger) {
		if (conn.getDBDriver() != DBDriver.MARIA_DB) {
			logger.logLn("DBMS wird für dieses Datenbank Revisions-Update nicht unterstützt.");
			return false;
		}
		try {
			recreateTrigger(conn, logger, Schema.tab_DavSyncTokenLehrer.trigger_MariaDB_INSERT_DavSyncTokenLehrer_EigeneSchule_Jahrgaenge);
			recreateTrigger(conn, logger, Schema.tab_DavSyncTokenLehrer.trigger_MariaDB_UPDATE_DavSyncTokenLehrer_EigeneSchule_Jahrgaenge);
			recreateTrigger(conn, logger, Schema.tab_DavSyncTokenLehrer.trigger_MariaDB_DELETE_DavSyncTokenLehrer_EigeneSchule_Jahrgaenge);
			recreateTrigger(conn, logger, Schema.tab_DavSyncTokenSchueler.trigger_MariaDB_INSERT_DavSyncTokenSchueler_EigeneSchule_Jahrgaenge);
			recreateTrigger(conn, logger, Schema.tab_DavSyncTokenSchueler.trigger_MariaDB_UPDATE_DavSyncTokenSchueler_EigeneSchule_Jahrgaenge);
			recreateTrigger(conn, logger, Schema.tab_DavSyncTokenSchueler.trigger_MariaDB_DELETE_DavSyncTokenSchueler_EigeneSchule_Jahrgaenge);
			conn.transactionFlush();
			return true;
		} catch (final DBException e) {
			logger.logLn(e.getMessage());
			return false;
		}
	}

}
