package de.svws_nrw.db.schema.revisionen;

import java.util.List;
import java.util.Set;

import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.DBException;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 36.
 */
public final class Revision36Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 36.
	 */
	public Revision36Updates() {
		super(SchemaRevisionen.REV_36);
	}

	private static void doUpdate(final DBEntityManager conn, final Logger logger, final String logInfo, final String sql) throws DBException {
		logger.logLn(logInfo);
		if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql))
			throw new DBException("Fehler beim Korrigieren des ASD-Jahrgangs");
	}

	@Override
	public boolean runLast(final DBEntityManager conn, final Logger logger) {
		if (conn.getDBDriver() != DBDriver.MARIA_DB) {
			logger.logLn("DBMS wird für dieses Datenbank Revisions-Update nicht unterstützt.");
			return false;
		}

		// Bestimme die Schulform
		final List<String> rowsSchulformKrz = conn.queryNative("SELECT SchulformKrz FROM EigeneSchule");
		if ((rowsSchulformKrz.size() != 1) || (rowsSchulformKrz.get(0) == null)) {
			logger.logLn("Konnte die Schulform der Schule nicht bestimmen.");
			return true;
		}
		final String schulformKrz = rowsSchulformKrz.get(0);
		if (schulformKrz == null) {
			logger.logLn("Konnte die Schulform der Schule nicht bestimmen.");
			return true;
		}

		// Die folgenden Korrekturen sind nur für die Schulformen mit Schuleingangsphase vorgesehen...
		if (!Set.of("FW", "HI", "WF", "G", "PS", "S", "KS", "V").contains(schulformKrz))
			return true;

		try {
			// Prüfe, ob die Jahrgänge E1 und E2 in der Tabelle EigeneSchule_Jahrgänge angelegt sind. Ist dies der Fall, so setze diese auf 01 und 02, ansonsten lege sie an
			doUpdate(conn, logger, "- EigeneSchule_Jahrgaenge: Ersetze ASDJahrgang und ASDBezeichnung für die ASD-Jahrgänge E1 und E2.",
					"""
					UPDATE %s SET
					InternKrz = CASE WHEN InternKrz = 'E1' THEN '01' WHEN InternKrz = 'E2' THEN '02' ELSE InternKrz END,
					Spaltentitel = CASE WHEN Spaltentitel = 'E1' THEN '01' WHEN Spaltentitel = 'E2' THEN '02' ELSE Spaltentitel END,
					ASDBezeichnung = CASE WHEN ASDJahrgang = 'E1' THEN '1. Jahrgang' ELSE '2. Jahrgang' END,
					ASDJahrgang = CASE WHEN ASDJahrgang = 'E1' THEN '01' ELSE '02' END
					WHERE ASDJahrgang IN ('E1', 'E2')
					""".formatted(Schema.tab_EigeneSchule_Jahrgaenge.name()));
			conn.transactionFlush();
			doUpdate(conn, logger, "- EigeneSchule_Jahrgaenge: Setze den ASD-Jahrgang 01, falls dieser nicht bereits existiert.",
					"""
					INSERT INTO %1$s(InternKrz, ASDJahrgang, ASDBezeichnung, Sortierung, Spaltentitel, SekStufe, SGL, Restabschnitte)
					SELECT '01' AS InternKrz, '01' AS ASDJahrgang, '1. Jahrgang' AS ASDBezeichnung, '10' AS Sortierung, '01' AS Spaltentitel, 'Pr' AS SekStufe, '***' AS SGL, 8 AS Restabschnitte
					WHERE '01' NOT IN (SELECT ASDJahrgang FROM %1$s)
					""".formatted(Schema.tab_EigeneSchule_Jahrgaenge.name()));
			doUpdate(conn, logger, "- EigeneSchule_Jahrgaenge: Setze den ASD-Jahrgang 02, falls dieser nicht bereits existiert.",
					"""
					INSERT INTO %1$s(InternKrz, ASDJahrgang, ASDBezeichnung, Sortierung, Spaltentitel, SekStufe, SGL, Restabschnitte)
					SELECT '02' AS InternKrz, '02' AS ASDJahrgang, '2. Jahrgang' AS ASDBezeichnung, '20' AS Sortierung, '02' AS Spaltentitel, 'Pr' AS SekStufe, '***' AS SGL, 6 AS Restabschnitte
					WHERE '02' NOT IN (SELECT ASDJahrgang FROM %1$s)
					""".formatted(Schema.tab_EigeneSchule_Jahrgaenge.name()));
			conn.transactionFlush();

			// Klassen - Ändere 1E in 01 bzw. 2E in 02 um und ergänze ggf. die Jahrgangs-ID aus der Jahrgangs-Tabelle
			doUpdate(conn, logger, "- Klassen: 1E in 01 bzw. 2E in 02 in der Spalte ASDKlasse.",
					"UPDATE %s SET ASDKlasse = CONCAT(CONCAT('0', SUBSTRING(ASDKlasse, 1, 1)), SUBSTRING(ASDKlasse, 3)) WHERE ASDKlasse LIKE '_E%%'"
							.formatted(Schema.tab_Klassen.name()));
			conn.transactionFlush();
			doUpdate(conn, logger, "- Klassen: 1E in 01 bzw. 2E in 02 in der Spalte ASDKlasse.",
					"UPDATE %s SET Jahrgang_ID = (SELECT ID FROM %s ej WHERE ej.ASDJahrgang = SUBSTRING(ASDKlasse, 1, 2)) WHERE ASDKlasse LIKE '__%%' AND Jahrgang_ID IS NULL"
							.formatted(Schema.tab_Klassen.name(), Schema.tab_EigeneSchule_Jahrgaenge.name()));
			conn.transactionFlush();

			//	Schuelerlernabschnittsdaten - Jahrgang-ID mithilfe des Klasseneintrages und ASDJahrgang über die Jahrgangstabelle
			doUpdate(conn, logger, "- Schuelerlernabschnittsdaten: Setze die EPJahre anhand der Entragung beim ASDJahrgang",
					"UPDATE %s SET EPJahre = CAST(SUBSTRING(ASDJahrgang, 2, 1) AS INTEGER) WHERE ASDJahrgang IN ('E1','E2','E3')"
							.formatted(Schema.tab_SchuelerLernabschnittsdaten.name()));
			conn.transactionFlush();
			doUpdate(conn, logger, "- Schuelerlernabschnittsdaten: Setze Jahrgang_ID, falls diese NULL ist über die Klassen-Tabelle.",
					"UPDATE %s sla JOIN %s k ON sla.Klassen_ID = k.ID SET sla.Jahrgang_ID = k.Jahrgang_ID"
							.formatted(Schema.tab_SchuelerLernabschnittsdaten.name(), Schema.tab_Klassen.name()));
			conn.transactionFlush();
			doUpdate(conn, logger,
					"- Schuelerlernabschnittsdaten: Aktualisiere ASDJahrgang, falls Jahrgang_ID gesetzt ist über einen Abgleich mit EigeneSchule_Jahrgaenge.",
					"UPDATE %s sla JOIN %s esj ON sla.Jahrgang_ID = esj.ID SET sla.ASDJahrgang = esj.ASDJahrgang"
							.formatted(Schema.tab_SchuelerLernabschnittsdaten.name(), Schema.tab_EigeneSchule_Jahrgaenge.name()));
			conn.transactionFlush();
			doUpdate(conn, logger,
					"- Schuelerlernabschnittsdaten: Kopiere die EPJahre von dem Schülereintrag zu den Lernabschnitten.",
					"UPDATE %s sla JOIN %s s ON sla.Schueler_ID = s.ID SET sla.EPJahre = s.EPJahre WHERE sla.EPJahre IS NULL AND s.EPJahre IS NOT NULL AND s.EPJahre IN (1,2,3)"
							.formatted(Schema.tab_SchuelerLernabschnittsdaten.name(), Schema.tab_Schueler.name()));
			conn.transactionFlush();
			doUpdate(conn, logger,
					"- Schuelerlernabschnittsdaten: Setze die EPJahre bei Lernabschnitten, wo diese zuvor nicht bestimmt werden konnten auf Default-Werte.",
					"UPDATE %s sla SET sla.EPJahre = CASE WHEN sla.ASDJahrgang='01' THEN 1 ELSE 2 END WHERE sla.EPJahre IS NULL"
							.formatted(Schema.tab_SchuelerLernabschnittsdaten.name()));
			conn.transactionFlush();

			// Aktualisiere die ASD-Jahrgänge in der Kurstabelle über die Jahrgangs-ID
			doUpdate(conn, logger, "- Kurse: Aktualisiere die Einträge zum Attribut ASDJahrgang anhand der Tabelle EigeneSchule_Jahrgaenge.",
					"UPDATE %s k JOIN %s j ON k.Jahrgang_ID = j.ID AND k.ASDJahrgang <> j.ASDJahrgang SET k.ASDJahrgang = j.ASDJahrgang"
							.formatted(Schema.tab_Kurse.name(), Schema.tab_EigeneSchule_Jahrgaenge.name()));

			// Aktualisiere den Entlassjahrgang in der Tabelle Schueler
			doUpdate(conn, logger, "- Schueler: Aktualisiere die Einträge zum Entlassjahrgang anhand der Tabelle EigeneSchule_Jahrgaenge.",
					"UPDATE %s s JOIN %s j ON s.Entlassjahrgang_ID = j.ID SET s.Entlassjahrgang = j.ASDJahrgang WHERE s.Entlassjahrgang_ID IS NOT NULL"
							.formatted(Schema.tab_Schueler.name(), Schema.tab_EigeneSchule_Jahrgaenge.name()));
			conn.transactionFlush();
			doUpdate(conn, logger, "- Schueler: Korrigiere ggf. den Entlassjahrgang, wenn keine ID zugeordnet ist.",
					"UPDATE %s SET Entlassjahrgang = CASE WHEN Entlassjahrgang = 'E1' THEN '01' ELSE '02' END WHERE Entlassjahrgang IN ('E1', 'E2', 'E3') AND Entlassjahrgang_ID IS NULL"
							.formatted(Schema.tab_Schueler.name()));
			conn.transactionFlush();
			doUpdate(conn, logger, "- Schueler: Korrigiere ggf. den Entlassjahrgang, wenn keine ID zugeordnet ist.",
					"""
					UPDATE %s s SET s.Entlassjahrgang_ID = (SELECT j.ID FROM %s j WHERE j.ASDJahrgang = s.Entlassjahrgang LIMIT 1)
					WHERE s.Entlassjahrgang_ID IS NULL AND s.Entlassjahrgang IS NOT NULL
					""".formatted(Schema.tab_Schueler.name(), Schema.tab_EigeneSchule_Jahrgaenge.name()));
			conn.transactionFlush();

			// Stundentafel
			doUpdate(conn, logger, "- Stundentafel: Passe den ASDJahrgang an.",
					"""
					UPDATE %s SET ASDJahrgang = CONCAT('0',SUBSTRING(ASDJahrgang, 2)) WHERE ASDJahrgang IN ('E1','E2')
					""".formatted(Schema.tab_Stundentafel.name()));
			conn.transactionFlush();
			doUpdate(conn, logger, "- Stundentafel: Bestimme die zugehörige Jahrgangs-ID.",
					"UPDATE %s st SET st.Jahrgang_ID = (SELECT j.ID FROM %s j WHERE j.ASDJahrgang = st.ASDJahrgang LIMIT 1) WHERE st.Jahrgang_ID IS NULL"
							.formatted(Schema.tab_Stundentafel.name(), Schema.tab_EigeneSchule_Jahrgaenge.name()));
			conn.transactionFlush();

			// K_Ankreuzfloskeln
			doUpdate(conn, logger, "- K_Ankreuzfloskeln: Passe den Jahrgang an.",
					"UPDATE %s SET Jahrgang = CONCAT('0',SUBSTRING(Jahrgang, 2)) WHERE Jahrgang IN ('E1','E2')"
							.formatted(Schema.tab_K_Ankreuzfloskeln.name()));
			conn.transactionFlush();

			// Floskeln - FloskelJahrgang
			doUpdate(conn, logger, "- Floskeln: Passe den FloskelJahrgang an.",
					"UPDATE %s SET FloskelJahrgang = CONCAT('0',SUBSTRING(FloskelJahrgang, 2)) WHERE FloskelJahrgang IN ('E1','E2')"
							.formatted(Schema.tab_Floskeln.name()));
			conn.transactionFlush();

			// Entferne den Eintrag für Jahrgang E3 aus der Tabelle EigeneSchule_Jahrgänge. Dieser sollte zu diesem Zeitpunkt nicht mehr in Verwendung sein.
			doUpdate(conn, logger, "- EigeneSchule_Jahrgaenge: Entferne den Eintrag für Jahrgang E3.",
					"DELETE FROM %s WHERE ASDJahrgang = 'E3'".formatted(Schema.tab_EigeneSchule_Jahrgaenge.name()));
			conn.transactionFlush();
			return true;
		} catch (final DBException e) {
			logger.logLn(e.getMessage());
			return false;
		}

	}

}
