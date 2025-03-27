package de.svws_nrw.db.schema.revisionen;

import java.util.List;

import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.DBException;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 33.
 */
public final class Revision33Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 33.
	 */
	public Revision33Updates() {
		super(SchemaRevisionen.REV_33);
	}


	@Override
	public boolean runLast(final DBEntityManager conn, final Logger logger) {
		if (conn.getDBDriver() != DBDriver.MARIA_DB) {
			logger.logLn("DBMS wird für dieses Datenbank Revisions-Update nicht unterstützt.");
			return false;
		}
		// Ergänze evtl. fehlende Trigger auf der Tabelle SchuelerMerkmale
		try {
			final List<String> pkTrigger = Schema.tab_SchuelerMerkmale.getPrimaerschluesselTriggerSQLList(DBDriver.MARIA_DB, SchemaRevisionen.REV_33.revision, true);
			if (!pkTrigger.isEmpty()) {
				logger.logLn("- %s: Erstelle Trigger für Auto-Inkremente, falls dies nicht bereits bei der Migration erfolgt ist.".formatted(Schema.tab_SchuelerMerkmale.name()));
				for (final String scriptTrigger : pkTrigger)
					if (conn.transactionNativeUpdateAndFlush(scriptTrigger) == Integer.MIN_VALUE)
						throw new DBException("Fehler beim Anlegen der Trigger");
			}
		} catch (final DBException e) {
			logger.logLn(e.getMessage());
			return false;
		}
		return true;
	}

}
