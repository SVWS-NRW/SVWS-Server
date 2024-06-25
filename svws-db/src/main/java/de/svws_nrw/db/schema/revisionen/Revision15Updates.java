package de.svws_nrw.db.schema.revisionen;

import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 15.
 */
public final class Revision15Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 15.
	 */
	public Revision15Updates() {
		super(SchemaRevisionen.REV_15);
	}

	@Override
	public boolean runLast(final DBEntityManager conn, final Logger logger) {
		if (conn.getDBDriver() != DBDriver.MARIA_DB) {
			logger.logLn("DBMS wird für dieses Datenbank Revisions-Update nicht unterstützt.");
			return false;
		}
		logger.logLn("- Benenne ggf. eine Vermerkart Bemerkungen um...");
		if (Integer.MIN_VALUE == conn
				.transactionNativeUpdateAndFlush("UPDATE K_Vermerkart SET Bezeichnung = 'Bemerkungen (vor Migration)' WHERE Bezeichnung = 'Bemerkungen'")) {
			logger.logLn(2, "Fehler beim Umbenennen der alten Vermerkart Bemerkungen.");
			return false;
		}
		final long idVermerkart = conn.transactionGetNextIDByTablename(Schema.tab_K_Vermerkart.name());
		if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(
				"INSERT INTO K_Vermerkart(ID, Bezeichnung, Sortierung, Sichtbar, Aenderbar) VALUES (%d, 'Bemerkungen', 1, '+', '+')".formatted(idVermerkart))) {
			logger.logLn(2, "Fehler beim Erstellen der neuen Vermerkart.");
			return false;
		}
		final long idVermerk = conn.transactionGetNextIDByTablename(Schema.tab_SchuelerVermerke.name());
		final String sql = "INSERT INTO SchuelerVermerke (ID, Schueler_ID, VermerkArt_ID, Datum, Bemerkung, AngelegtVon, GeaendertVon) "
				+ "SELECT ROW_NUMBER() OVER () + (" + idVermerk + "-1) AS ID, Schueler.ID AS Schueler_ID, " + idVermerkart
				+ " AS VermerkArt_ID, now() AS Datum,"
				+ "Schueler.Bemerkungen AS Bemerkung, NULL AS Angelegt_Von, NULL AS GeaendertVon FROM Schueler WHERE Bemerkungen IS NOT NULL";
		if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql)) {
			logger.logLn(2, "Fehler beim Erstellen der neuen Vermerkart.");
			return false;
		}
		return true;
	}

}
