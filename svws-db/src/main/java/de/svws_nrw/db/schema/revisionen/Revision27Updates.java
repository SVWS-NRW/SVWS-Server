package de.svws_nrw.db.schema.revisionen;

import java.util.List;
import java.util.Map;

import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.DBException;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 27.
 */
public final class Revision27Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 27.
	 */
	public Revision27Updates() {
		super(SchemaRevisionen.REV_27);
	}


	private static void updateASDJahrgang(final DBEntityManager conn, final Logger logger, final String tabname, final String colname, final String jgNeu,
			final String jgAlt, final String bedingung) throws DBException {
		logger.logLn("- %s: Setzte den ASD-Jahrgang auf '%s', wenn zuvor '%s' gesetzt war.".formatted(tabname, jgNeu, jgAlt));
		final String sql = "UPDATE %1$s SET %2$s = '%3$s' WHERE %2$s = '%4$s'%5$s".formatted(tabname, colname, jgNeu, jgAlt, bedingung);
		if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql))
			throw new DBException("Fehler beim Korrigieren des ASD-Jahrgangs");
	}



	@Override
	public boolean runLast(final DBEntityManager conn, final Logger logger) {
		if (conn.getDBDriver() != DBDriver.MARIA_DB) {
			logger.logLn("DBMS wird für dieses Datenbank Revisions-Update nicht unterstützt.");
			return false;
		}
		// Entferne fehlerhaften Trigger, sofern er bereits angelegt wurde

		// Bestimme die Schulform
		final List<String> rowsSchulformKrz = conn.queryNative("SELECT SchulformKrz FROM EigeneSchule");
		if ((rowsSchulformKrz.size() != 1) || (rowsSchulformKrz.get(0) == null)) {
			logger.logLn("Konnte die Schulform der Schule nicht bestimmen.");
			return false;
		}
		final String schulformKrz = rowsSchulformKrz.get(0);
		if (schulformKrz == null) {
			logger.logLn("Konnte die Schulform der Schule nicht bestimmen.");
			return false;
		}
		// Die folgenden Korrektur sind nur für die Schulform WB vorgesehen...
		if (!schulformKrz.equals("WB"))
			return true;

		// Aktualisiere die ASD-Jahrgangs-Einträge
		final Map<String, String> mapJg = Map.of("S1", "01", "S2", "02", "S3", "03", "S4", "04", "S5", "05", "S6", "06");
		try {
			for (final Map.Entry<String, String> jg : mapJg.entrySet()) {
				updateASDJahrgang(conn, logger, Schema.tab_EigeneSchule_Jahrgaenge.name(), Schema.tab_EigeneSchule_Jahrgaenge.col_ASDJahrgang.name(),
						jg.getKey(), jg.getValue(), "");
				updateASDJahrgang(conn, logger, Schema.tab_Floskeln.name(), Schema.tab_Floskeln.col_FloskelJahrgang.name(),
						jg.getKey(), jg.getValue(), "");
				updateASDJahrgang(conn, logger, Schema.tab_Kurse.name(), Schema.tab_Kurse.col_ASDJahrgang.name(),
						jg.getKey(), jg.getValue(), "");
				updateASDJahrgang(conn, logger, Schema.tab_Schueler.name(), Schema.tab_Schueler.col_Entlassjahrgang.name(),
						jg.getKey(), jg.getValue(), "");
				updateASDJahrgang(conn, logger, Schema.tab_SchuelerAbgaenge.name(), Schema.tab_SchuelerAbgaenge.col_LSJahrgang.name(),
						jg.getKey(), jg.getValue(), " AND %s = 'WB'".formatted(Schema.tab_SchuelerAbgaenge.col_LSSchulformSIM.name()));
				updateASDJahrgang(conn, logger, Schema.tab_SchuelerLernabschnittsdaten.name(), Schema.tab_SchuelerLernabschnittsdaten.col_ASDJahrgang.name(),
						jg.getKey(), jg.getValue(), "");
				updateASDJahrgang(conn, logger, Schema.tab_Stundentafel.name(), Schema.tab_Stundentafel.col_ASDJahrgang.name(),
						jg.getKey(), jg.getValue(), "");
				// ASDKlasse in Tabelle Klassen hat nur den ASD-Jahrgang als Präfix...
				logger.logLn("- %s: Setzte den ASD-Jahrgang in ASD-Klasse auf '%s', wenn zuvor '%s' gesetzt war.".formatted(Schema.tab_Klassen.name(),
						jg.getKey(), jg.getValue()));
				final String sql = "UPDATE %1$s SET %2$s = CONCAT('%3$s', SUBSTRING(%2$s, 3)) WHERE %2$s LIKE '%4$s%%'".formatted(Schema.tab_Klassen.name(),
						Schema.tab_Klassen.col_ASDKlasse.name(), jg.getKey(), jg.getValue());
				if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql))
					throw new DBException("Fehler beim Korrigieren der ASDKlasse");

			}
		} catch (final DBException e) {
			logger.logLn(e.getMessage());
			return false;
		}
		return true;
	}

}
