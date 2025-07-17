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
 * auf Revision 34.
 */
public final class Revision34Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates für Revision 34.
	 *
	 * Diese beinhalten Anpassungen, die bei Datenbanken notwendig werden, welcher früher schon Migrationen ausgeführt haben, bevor sich dort nochmals
	 * Definitionen des Primärschlüssels der Tabelle Kurs_Schueler geändert haben, da die Spalte Leistung_ID ergänzt wurde.
	 */
	public Revision34Updates() {
		super(SchemaRevisionen.REV_34);
		add("Reinitialisierung Kurs_Schueler: Entfernen von Einträgen (sollte keiner vorhanden sein...)",
				"DELETE FROM Kurs_Schueler",
				Schema.tab_Kurs_Schueler
		);
		add("Ggf. nochmals Entfernen fehlerhafter Kurs-Einträge in den Leistungsdaten (Zuordnung zu Lernabschnitten)",
				"""
				UPDATE SchuelerLeistungsdaten
				SET Kurs_ID = NULL
				WHERE (SELECT Schuljahresabschnitts_ID FROM Kurse WHERE Kurse.ID = SchuelerLeistungsdaten.Kurs_ID)
				    <> (SELECT Schuljahresabschnitts_ID FROM SchuelerLernabschnittsdaten WHERE SchuelerLernabschnittsdaten.ID = SchuelerLeistungsdaten.Abschnitt_ID)
				""",
				Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_SchuelerLeistungsdaten
		);
		add("Ggf. nochmals Entfernen fehlerhafter Kurs-Einträge in den Leistungsdaten (Kurs mit nicht passenden Fächern)",
				"UPDATE SchuelerLeistungsdaten JOIN Kurse ON SchuelerLeistungsdaten.Kurs_ID = Kurse.ID "
						+ "SET SchuelerLeistungsdaten.Kurs_ID = NULL "
						+ "WHERE SchuelerLeistungsdaten.Fach_ID != Kurse.Fach_ID;",
				Schema.tab_SchuelerLeistungsdaten, Schema.tab_Kurse
		);
		add("Reinitialisierung Kurs_Schueler: Befüllen mit Daten",
				"""
				INSERT INTO Kurs_Schueler
				SELECT DISTINCT
				    Kurse.ID AS Kurs_ID,
				    Schueler.ID AS Schueler_ID,
				    SchuelerLernabschnittsdaten.WechselNr AS LernabschnittWechselNr,
				    SchuelerLeistungsdaten.ID AS Leistung_ID
				FROM
				    Kurse JOIN SchuelerLeistungsdaten ON Kurse.ID = SchuelerLeistungsdaten.Kurs_ID
				        JOIN SchuelerLernabschnittsdaten ON SchuelerLeistungsdaten.Abschnitt_ID = SchuelerLernabschnittsdaten.ID
				        JOIN Schueler ON SchuelerLernabschnittsdaten.Schueler_ID = Schueler.ID
				""",
				Schema.tab_Kurs_Schueler, Schema.tab_Schueler, Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_SchuelerLeistungsdaten
		);
	}

	@Override
	public boolean runFirst(final DBEntityManager conn, final Logger logger) {
		try {
			if (conn.getDBDriver() != DBDriver.MARIA_DB) {
				logger.logLn("DBMS wird für dieses Datenbank Revisions-Update nicht unterstützt.");
				return false;
			}
			logger.logLn("- Kurs_Schueler: Anlegen der Spalte Leistung_ID, falls diese noch nicht vorhanden ist.");
			String sql = "ALTER TABLE %s ADD COLUMN IF NOT EXISTS Leistung_ID bigint(20) NOT NULL COMMENT 'Die eindeutige ID der Leistungsdaten, in denen die Zuordnung stattgefunden hat'".formatted(Schema.tab_Kurs_Schueler.name());
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql))
				throw new DBException("Fehler beim Anlegen der Spalte Leistung_ID");
			conn.transactionFlush();
			logger.logLn("- Kurs_Schueler: Neusetzen des Primärschlüssels mit der Spalte Leistung_ID.");
			sql = "ALTER TABLE %s DROP PRIMARY KEY, ADD PRIMARY KEY (Kurs_ID, Schueler_ID, LernabschnittWechselNr, Leistung_ID)".formatted(Schema.tab_Kurs_Schueler.name());
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql))
				throw new DBException("Fehler beim Korrigieren des ASD-Jahrgangs");
			conn.transactionFlush();
			return true;
		} catch (final DBException e) {
			logger.logLn(e.getMessage());
			return false;
		}
	}

}
