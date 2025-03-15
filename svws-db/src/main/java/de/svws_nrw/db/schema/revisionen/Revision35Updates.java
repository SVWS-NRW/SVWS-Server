package de.svws_nrw.db.schema.revisionen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.DBException;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.tabellen.Tabelle_K_Ankreuzfloskeln;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 35.
 */
public final class Revision35Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 35.
	 */
	public Revision35Updates() {
		super(SchemaRevisionen.REV_35);
	}


	private static void updateASDJahrgang(final DBEntityManager conn, final Logger logger, final String tabname, final String colname, final String jgNeu,
			final String jgAlt, final String bedingung) throws DBException {
		logger.logLn("- %s: Setze den ASD-Jahrgang auf '%s', wenn zuvor '%s' gesetzt war.".formatted(tabname, jgNeu, jgAlt));
		final String sql = "UPDATE %1$s SET %2$s = '%3$s' WHERE %2$s = '%4$s'%5$s".formatted(tabname, colname, jgNeu, jgAlt, bedingung);
		if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql))
			throw new DBException("Fehler beim Korrigieren des ASD-Jahrgangs");
	}


	private static boolean updateKatalogAnkreuzfloskeln(final DBEntityManager conn, final Logger logger) {
		final Tabelle_K_Ankreuzfloskeln tab = Schema.tab_K_Ankreuzfloskeln;
		try {
			// Aktualisiere die ASD-Jahrgangs-Einträge für die Tabelle "K_Ankreuzfloskeln"
			final Map<String, String> mapJg = Map.of("S1", "01", "S2", "02", "S3", "03", "S4", "04", "S5", "05", "S6", "06");
			for (final Map.Entry<String, String> jg : mapJg.entrySet())
				updateASDJahrgang(conn, logger, tab.name(), tab.col_Jahrgang.name(), jg.getKey(), jg.getValue(), "");

			// Korrigiere zunächst die Einträge, bei welchen die Schulgliederung angegeben wurde und angepasst werden muss
			logger.logLn("- K_Ankreuzfloskeln: Gliederung G02/K02: Setze den ASD-Jahrgang auf 'V1', wenn zuvor '91' gesetzt ist.");
			String sql = "UPDATE %s SET Jahrgang = 'V1' WHERE Jahrgang = '91' AND (Gliederung IN ('G02','K02') OR Gliederung IS NULL)".formatted(tab.name());
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql))
				throw new DBException("Fehler beim Korrigieren des ASD-Jahrgangs");
			logger.logLn("- K_Ankreuzfloskeln: Gliederung G02/K02: Setze den ASD-Jahrgang auf 'V2', wenn zuvor '92' gesetzt ist.");
			sql = "UPDATE %s SET Jahrgang = 'V2' WHERE Jahrgang = '92' AND (Gliederung IN ('G02','K02') OR Gliederung IS NULL)".formatted(tab.name());
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql))
				throw new DBException("Fehler beim Korrigieren des ASD-Jahrgangs");
			logger.logLn("- K_Ankreuzfloskeln: Gliederung R02: Setze den ASD-Jahrgang auf 'R1', wenn zuvor 'S1' gesetzt ist.");
			sql = "UPDATE %s SET Jahrgang = 'R1' WHERE Jahrgang = 'S1' AND Gliederung = 'R02'".formatted(tab.name());
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql))
				throw new DBException("Fehler beim Korrigieren des ASD-Jahrgangs");
			logger.logLn("- K_Ankreuzfloskeln: Gliederung R02: Setze den ASD-Jahrgang auf 'R2', wenn zuvor 'S2' gesetzt ist.");
			sql = "UPDATE %s SET Jahrgang = 'R2' WHERE Jahrgang = 'S2' AND Gliederung = 'R02'".formatted(tab.name());
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql))
				throw new DBException("Fehler beim Korrigieren des ASD-Jahrgangs");
			logger.logLn("- K_Ankreuzfloskeln: Gliederung R02: Setze den ASD-Jahrgang auf 'R3', wenn zuvor 'S3' gesetzt ist.");
			sql = "UPDATE %s SET Jahrgang = 'R3' WHERE Jahrgang = 'S3' AND Gliederung = 'R02'".formatted(tab.name());
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql))
				throw new DBException("Fehler beim Korrigieren des ASD-Jahrgangs");
			logger.logLn("- K_Ankreuzfloskeln: Gliederung R02: Setze den ASD-Jahrgang auf 'R4', wenn zuvor 'S4' gesetzt ist.");
			sql = "UPDATE %s SET Jahrgang = 'R4' WHERE Jahrgang = 'S4' AND Gliederung = 'R02'".formatted(tab.name());
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql))
				throw new DBException("Fehler beim Korrigieren des ASD-Jahrgangs");
			// Entferne alle Eintragungen, wo die Gliederung der Abendrealschule für Semester gesetzt ist, die es dort gar nicht gibt
			logger.logLn("- K_Ankreuzfloskeln: Gliederung R02: Entferne Einträge, wo Jahrgang 'S5' oder 'S6' gesetzt ist.");
			sql = "DELETE FROM %s WHERE Gliederung = 'R02' AND Jahrgang IN ('S5','S6')".formatted(tab.name());
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql))
				throw new DBException("Fehler beim Korrigieren des ASD-Jahrgangs");
			// Dupliziere für die Abendrealschule zunächst alle Einträge, welche als Gliederung NULL eingetragen haben, ignoriere dabei die Einträge mit S5 und S6
			logger.logLn("- K_Ankreuzfloskeln: Gliederung R02: Erzeuge Einträge für die Schulgliederung R02, wenn die Gliederung NULL ist.");
			conn.transactionFlush();
			final List<Object[]> list = conn.queryNative(
					"SELECT ID, Fach_ID, IstASV, Jahrgang, Gliederung, FloskelText, Sortierung, FachSortierung, Abschnitt, Sichtbar, Aktiv FROM %s WHERE Gliederung IS NULL AND Jahrgang NOT IN ('S5','S6')"
							.formatted(tab.name()));
			long nextID = conn.transactionGetNextIDByTablename(tab.name());
			final Map<Long, Long> map = new HashMap<>();
			for (final Object[] data : list) {
				if ((data[0] instanceof final Number n) && (data[3] instanceof final String s)) {
					final long id = n.longValue();
					final String jg = switch (s) {
						case "V1" -> "91";
						case "V2" -> "92";
						case "S1" -> "R1";
						case "S2" -> "R2";
						case "S3" -> "R3";
						case "S4" -> "R4";
						default -> "91"; // Sollte nicht vorkommen...
					};
					map.put(id, nextID);
					sql = "INSERT INTO %1$s SELECT %3$d AS ID, Fach_ID, IstASV, '%4$s' AS Jahrgang, '%5$s' AS Gliederung, FloskelText, Sortierung, FachSortierung, Abschnitt, Sichtbar, Aktiv FROM %1$s WHERE ID = %2$d"
							.formatted(tab.name(), id, nextID++, jg, "R02");
					if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql))
						throw new DBException("Fehler beim Duplizieren des Eintrags mit der ID %d zu Jahrgang %s".formatted(id, jg));
				} else
					throw new DBException("Fehler beim Bestimmen der ID oder des Jahrgangs der Datensätze");
			}
			conn.transactionFlush();
			// ggf. Korrektur der Einträge in SchuelerAnkreuzfloskeln
			for (final Map.Entry<Long, Long> e : map.entrySet()) {
				final long idAlt = e.getKey();
				final long idNeu = e.getValue();
				sql = "UPDATE %s sa JOIN %s sla ON sa.Abschnitt_ID = sla.ID AND sla.ASDSchulgliederung = 'R02' AND sa.Floskel_ID = %d SET sa.Floskel_ID = %d"
						.formatted(Schema.tab_SchuelerAnkreuzfloskeln.name(), Schema.tab_SchuelerLernabschnittsdaten.name(), idAlt, idNeu);
				if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql))
					throw new DBException("Fehler beim Setzen der Einträge in SchuelerAnkreuzfloskeln für die duplizierten Einträge von ID %d aus ID %d"
							.formatted(idAlt, idNeu));
			}
			return true;
		} catch (final DBException e) {
			logger.logLn(e.getMessage());
			return false;
		}
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
		// Die folgenden Korrektur sind nur für die Schulform WB vorgesehen...
		if (!schulformKrz.equals("WB"))
			return true;

		try {
			// EigeneSchule_Jahrgaenge - Abendgymnasium / Kolleg
			doUpdate(conn, logger, "- EigeneSchule_Jahrgaenge: Gliederung G02/K02: Setze den ASD-Jahrgang auf 'V1', wenn zuvor '91' gesetzt ist.",
					"UPDATE %s SET ASDJahrgang = 'V1', ASDBezeichnung = 'Vorkurs (1. Semester)' WHERE ASDJahrgang = '91' AND (SGL IN ('G02','K02') OR SGL IS NULL)"
							.formatted(Schema.tab_EigeneSchule_Jahrgaenge.name()));
			doUpdate(conn, logger, "- EigeneSchule_Jahrgaenge: Gliederung G02/K02: Setze den ASD-Jahrgang auf 'V2', wenn zuvor '92' gesetzt ist.",
					"UPDATE %s SET ASDJahrgang = 'V2', ASDBezeichnung = 'Vorkurs (2. Semester)' WHERE ASDJahrgang = '92' AND (SGL IN ('G02','K02') OR SGL IS NULL)"
							.formatted(Schema.tab_EigeneSchule_Jahrgaenge.name()));
			doUpdate(conn, logger, "- EigeneSchule_Jahrgaenge: Gliederung G02/K02: Setze die ASD-Bezeichnung für den ASD-Jahrgang 'S1'.",
					"UPDATE %s SET ASDBezeichnung = '1. Semester (EF.1)' WHERE ASDJahrgang = 'S1' AND (SGL IN ('G02','K02') OR SGL IS NULL)"
							.formatted(Schema.tab_EigeneSchule_Jahrgaenge.name()));
			doUpdate(conn, logger, "- EigeneSchule_Jahrgaenge: Gliederung G02/K02: Setze die ASD-Bezeichnung für den ASD-Jahrgang 'S2'.",
					"UPDATE %s SET ASDBezeichnung = '2. Semester (EF.2)' WHERE ASDJahrgang = 'S2' AND (SGL IN ('G02','K02') OR SGL IS NULL)"
							.formatted(Schema.tab_EigeneSchule_Jahrgaenge.name()));
			doUpdate(conn, logger, "- EigeneSchule_Jahrgaenge: Gliederung G02/K02: Setze die ASD-Bezeichnung für den ASD-Jahrgang 'S3'.",
					"UPDATE %s SET ASDBezeichnung = '3. Semester (Q1.1)' WHERE ASDJahrgang = 'S3' AND (SGL IN ('G02','K02') OR SGL IS NULL)"
							.formatted(Schema.tab_EigeneSchule_Jahrgaenge.name()));
			doUpdate(conn, logger, "- EigeneSchule_Jahrgaenge: Gliederung G02/K02: Setze die ASD-Bezeichnung für den ASD-Jahrgang 'S4'.",
					"UPDATE %s SET ASDBezeichnung = '4. Semester (Q1.2)' WHERE ASDJahrgang = 'S4' AND (SGL IN ('G02','K02') OR SGL IS NULL)"
							.formatted(Schema.tab_EigeneSchule_Jahrgaenge.name()));
			doUpdate(conn, logger, "- EigeneSchule_Jahrgaenge: Gliederung G02/K02: Setze die ASD-Bezeichnung für den ASD-Jahrgang 'S5'.",
					"UPDATE %s SET ASDBezeichnung = '5. Semester (Q2.1)' WHERE ASDJahrgang = 'S5' AND (SGL IN ('G02','K02') OR SGL IS NULL)"
							.formatted(Schema.tab_EigeneSchule_Jahrgaenge.name()));
			doUpdate(conn, logger, "- EigeneSchule_Jahrgaenge: Gliederung G02/K02: Setze die ASD-Bezeichnung für den ASD-Jahrgang 'S6'.",
					"UPDATE %s SET ASDBezeichnung = '6. Semester (Q2.2)' WHERE ASDJahrgang = 'S6' AND (SGL IN ('G02','K02') OR SGL IS NULL)"
							.formatted(Schema.tab_EigeneSchule_Jahrgaenge.name()));

			// EigeneSchule_Jahrgaenge - Abendrealschule
			doUpdate(conn, logger, "- EigeneSchule_Jahrgaenge: Gliederung R02: Setze die ASD-Bezeichnung für den ASD-Jahrgang '91'.",
					"UPDATE %s SET ASDBezeichnung = 'Vorkurs (1. Semester, Abendrealschule)' WHERE ASDJahrgang = '91' AND SGL = 'R02'"
							.formatted(Schema.tab_EigeneSchule_Jahrgaenge.name()));
			doUpdate(conn, logger, "- EigeneSchule_Jahrgaenge: Gliederung R02: Setze die ASD-Bezeichnung für den ASD-Jahrgang '92'.",
					"UPDATE %s SET ASDBezeichnung = 'Vorkurs (2. Semester, Abendrealschule)' WHERE ASDJahrgang = '92' AND SGL = 'R02'"
							.formatted(Schema.tab_EigeneSchule_Jahrgaenge.name()));
			doUpdate(conn, logger, "- EigeneSchule_Jahrgaenge: Gliederung R02: Setze den ASD-Jahrgang auf 'R1', wenn zuvor 'S1' gesetzt ist.",
					"UPDATE %s SET ASDJahrgang = 'R1', ASDBezeichnung = '1. Semester (Hauptphase, Abendrealschule)' WHERE ASDJahrgang = 'S1' AND SGL = 'R02'"
							.formatted(Schema.tab_EigeneSchule_Jahrgaenge.name()));
			doUpdate(conn, logger, "- EigeneSchule_Jahrgaenge: Gliederung R02: Setze den ASD-Jahrgang auf 'R2', wenn zuvor 'S2' gesetzt ist.",
					"UPDATE %s SET ASDJahrgang = 'R2', ASDBezeichnung = '2. Semester (Hauptphase, Abendrealschule)' WHERE ASDJahrgang = 'S2' AND SGL = 'R02'"
							.formatted(Schema.tab_EigeneSchule_Jahrgaenge.name()));
			doUpdate(conn, logger, "- EigeneSchule_Jahrgaenge: Gliederung R02: Setze den ASD-Jahrgang auf 'R3', wenn zuvor 'S3' gesetzt ist.",
					"UPDATE %s SET ASDJahrgang = 'R3', ASDBezeichnung = '3. Semester (Hauptphase, Abendrealschule)' WHERE ASDJahrgang = 'S3' AND SGL = 'R02'"
							.formatted(Schema.tab_EigeneSchule_Jahrgaenge.name()));
			doUpdate(conn, logger, "- EigeneSchule_Jahrgaenge: Gliederung R02: Setze den ASD-Jahrgang auf 'R4', wenn zuvor 'S4' gesetzt ist.",
					"UPDATE %s SET ASDJahrgang = 'R4', ASDBezeichnung = '4. Semester (Hauptphase, Abendrealschule)' WHERE ASDJahrgang = 'S4' AND SGL = 'R02'"
							.formatted(Schema.tab_EigeneSchule_Jahrgaenge.name()));
			conn.transactionFlush();

			//	Schuelerlernabschnittsdaten - ASDJahrgang (mithilfe von ASDSchulgliederung)
			doUpdate(conn, logger, "- SchuelerLernabschnittsdaten: Gliederung G02/K02: Setze den ASD-Jahrgang auf 'V1', wenn zuvor '91' gesetzt ist.",
					"UPDATE %s SET ASDJahrgang = 'V1' WHERE ASDJahrgang = '91' AND ASDSchulgliederung IN ('G02','K02')"
							.formatted(Schema.tab_SchuelerLernabschnittsdaten.name()));
			doUpdate(conn, logger, "- SchuelerLernabschnittsdaten: Gliederung G02/K02: Setze den ASD-Jahrgang auf 'V2', wenn zuvor '92' gesetzt ist.",
					"UPDATE %s SET ASDJahrgang = 'V2' WHERE ASDJahrgang = '92' AND ASDSchulgliederung IN ('G02','K02')"
							.formatted(Schema.tab_SchuelerLernabschnittsdaten.name()));

			doUpdate(conn, logger, "- SchuelerLernabschnittsdaten: Gliederung R02: Setze den ASD-Jahrgang auf 'R1', wenn zuvor 'S1' gesetzt ist.",
					"UPDATE %s SET ASDJahrgang = 'R1' WHERE ASDJahrgang = 'S1' AND ASDSchulgliederung = 'R02'"
							.formatted(Schema.tab_SchuelerLernabschnittsdaten.name()));
			doUpdate(conn, logger, "- Schuelerlernabschnittsdaten: Gliederung R02: Setze den ASD-Jahrgang auf 'R2', wenn zuvor 'S2' gesetzt ist.",
					"UPDATE %s SET ASDJahrgang = 'R2' WHERE ASDJahrgang = 'S2' AND ASDSchulgliederung = 'R02'"
							.formatted(Schema.tab_SchuelerLernabschnittsdaten.name()));
			doUpdate(conn, logger, "- Schuelerlernabschnittsdaten: Gliederung R02: Setze den ASD-Jahrgang auf 'R3', wenn zuvor 'S3' gesetzt ist.",
					"UPDATE %s SET ASDJahrgang = 'R3' WHERE ASDJahrgang = 'S3' AND ASDSchulgliederung = 'R02'"
							.formatted(Schema.tab_SchuelerLernabschnittsdaten.name()));
			doUpdate(conn, logger, "- Schuelerlernabschnittsdaten: Gliederung R02: Setze den ASD-Jahrgang auf 'R4', wenn zuvor 'S4' gesetzt ist.",
					"UPDATE %s SET ASDJahrgang = 'R4' WHERE ASDJahrgang = 'S4' AND ASDSchulgliederung = 'R02'"
							.formatted(Schema.tab_SchuelerLernabschnittsdaten.name()));

			conn.transactionFlush();

			// Aktualisiere den Katalog der Ankreuzfloskeln
			if (!updateKatalogAnkreuzfloskeln(conn, logger))
				return false;

			// Aktualisiere die ASD-Jahrgänge in der Kurstabelle über die Jahrgangs-ID
			doUpdate(conn, logger, "- Kurse: Aktualisiere die Einträge zum Attribut ASDJahrgang anhand der Tabelle EigeneSchule_Jahrgaenge.",
					"UPDATE %s k JOIN %s j ON k.Jahrgang_ID = j.ID AND k.ASDJahrgang <> j.ASDJahrgang SET k.ASDJahrgang = j.ASDJahrgang"
							.formatted(Schema.tab_Kurse.name(), Schema.tab_EigeneSchule_Jahrgaenge.name()));

			// Aktualisiere das Feld ASDKlasse in der Klassentabelle über die Jahrgangs-ID
			doUpdate(conn, logger, "- Klassen: Aktualisiere die Einträge zum Attribut ASDKlasse anhand der Tabelle EigeneSchule_Jahrgaenge.",
					"UPDATE %s k JOIN %s j ON k.Jahrgang_ID = j.ID SET ASDKlasse = CONCAT(j.ASDJahrgang, SUBSTRING(ASDKlasse, 3)) WHERE ASDKlasse LIKE '__%%'"
							.formatted(Schema.tab_Klassen.name(), Schema.tab_EigeneSchule_Jahrgaenge.name()));

			// Aktualisiere den Entlassjahrgang in der Tabelle Schueler
			doUpdate(conn, logger, "- Schueler: Gliederung G02/K02: Aktualisiere die Einträge zum Attribut Entlassjahrgang (91 -> V1).",
					"""
					UPDATE %s s JOIN %s sla ON s.ID = sla.Schueler_ID AND s.Schuljahresabschnitts_ID = sla.Schuljahresabschnitts_ID AND sla.WechselNr = 0
					AND sla.ASDSchulgliederung IS NOT NULL AND s.Entlassjahrgang IS NOT NULL
					AND sla.ASDSchulgliederung IN ('G02', 'K02') AND s.Entlassjahrgang = '91' SET Entlassjahrgang = 'V1'
					""".formatted(Schema.tab_Schueler.name(), Schema.tab_SchuelerLernabschnittsdaten.name()));

			doUpdate(conn, logger, "- Schueler: Gliederung G02/K02: Aktualisiere die Einträge zum Attribut Entlassjahrgang (92 -> V2).",
					"""
					UPDATE %s s JOIN %s sla ON s.ID = sla.Schueler_ID AND s.Schuljahresabschnitts_ID = sla.Schuljahresabschnitts_ID AND sla.WechselNr = 0
					AND sla.ASDSchulgliederung IS NOT NULL AND s.Entlassjahrgang IS NOT NULL
					AND sla.ASDSchulgliederung IN ('G02', 'K02') AND s.Entlassjahrgang = '92' SET Entlassjahrgang = 'V2'
					""".formatted(Schema.tab_Schueler.name(), Schema.tab_SchuelerLernabschnittsdaten.name()));

			doUpdate(conn, logger, "- Schueler: Gliederung R02: Aktualisiere die Einträge zum Attribut Entlassjahrgang (S1 -> R1).",
					"""
					UPDATE %s s JOIN %s sla ON s.ID = sla.Schueler_ID AND s.Schuljahresabschnitts_ID = sla.Schuljahresabschnitts_ID AND sla.WechselNr = 0
					AND sla.ASDSchulgliederung IS NOT NULL AND s.Entlassjahrgang IS NOT NULL
					AND sla.ASDSchulgliederung = 'R02' AND s.Entlassjahrgang = 'S1' SET Entlassjahrgang = 'R1'
					""".formatted(Schema.tab_Schueler.name(), Schema.tab_SchuelerLernabschnittsdaten.name()));

			doUpdate(conn, logger, "- Schueler: Gliederung R02: Aktualisiere die Einträge zum Attribut Entlassjahrgang (S2 -> R2).",
					"""
					UPDATE %s s JOIN %s sla ON s.ID = sla.Schueler_ID AND s.Schuljahresabschnitts_ID = sla.Schuljahresabschnitts_ID AND sla.WechselNr = 0
					AND sla.ASDSchulgliederung IS NOT NULL AND s.Entlassjahrgang IS NOT NULL
					AND sla.ASDSchulgliederung = 'R02' AND s.Entlassjahrgang = 'S2' SET Entlassjahrgang = 'R2'
					""".formatted(Schema.tab_Schueler.name(), Schema.tab_SchuelerLernabschnittsdaten.name()));

			doUpdate(conn, logger, "- Schueler: Gliederung R02: Aktualisiere die Einträge zum Attribut Entlassjahrgang (S3 -> R3).",
					"""
					UPDATE %s s JOIN %s sla ON s.ID = sla.Schueler_ID AND s.Schuljahresabschnitts_ID = sla.Schuljahresabschnitts_ID AND sla.WechselNr = 0
					AND sla.ASDSchulgliederung IS NOT NULL AND s.Entlassjahrgang IS NOT NULL
					AND sla.ASDSchulgliederung = 'R02' AND s.Entlassjahrgang = 'S3' SET Entlassjahrgang = 'R3'
					""".formatted(Schema.tab_Schueler.name(), Schema.tab_SchuelerLernabschnittsdaten.name()));

			doUpdate(conn, logger, "- Schueler: Gliederung R02: Aktualisiere die Einträge zum Attribut Entlassjahrgang (S4 -> R4).",
					"""
					UPDATE %s s JOIN %s sla ON s.ID = sla.Schueler_ID AND s.Schuljahresabschnitts_ID = sla.Schuljahresabschnitts_ID AND sla.WechselNr = 0
					AND sla.ASDSchulgliederung IS NOT NULL AND s.Entlassjahrgang IS NOT NULL
					AND sla.ASDSchulgliederung = 'R02' AND s.Entlassjahrgang = 'S4' SET Entlassjahrgang = 'R4'
					""".formatted(Schema.tab_Schueler.name(), Schema.tab_SchuelerLernabschnittsdaten.name()));

			conn.transactionFlush();

			doUpdate(conn, logger,
					"- Schueler: Aktualisiere die Einträge zum Attribut Entlassjahrgang_ID, sofern sich diese aus dem Attribut Entlassjahrgang und den aktuellen Schülerlernabschnittsdaten des Schüler herleiten lassen.",
					"""
					UPDATE %s s JOIN %s sla ON s.ID = sla.Schueler_ID AND s.Schuljahresabschnitts_ID = sla.Schuljahresabschnitts_ID AND sla.WechselNr = 0
					AND sla.ASDSchulgliederung IS NOT NULL AND s.Entlassjahrgang_ID IS NULL
					JOIN %s j ON s.Entlassjahrgang = j.ASDJahrgang AND sla.ASDSchulgliederung = j.SGL
					SET Entlassjahrgang_ID = j.ID
					""".formatted(Schema.tab_Schueler.name(), Schema.tab_SchuelerLernabschnittsdaten.name(), Schema.tab_EigeneSchule_Jahrgaenge.name()));

			// SchuelerAbgaenge - LSJahrgang
			doUpdate(conn, logger,
					"- SchuelerAbgaenge: Gliederung G02/K02: Aktualisiere die Einträge zum Attribut LSJahrgang (91 -> V1).",
					"UPDATE %s SET LSJahrgang = 'V1' WHERE LSJahrgang IS NOT NULL AND LSSGL IS NOT NULL AND LSSchulformSIM = 'WB' AND SUBSTRING(LSSGL, 3) IN ('G02','K02') AND LSJahrgang = '91'"
							.formatted(Schema.tab_SchuelerAbgaenge.name()));
			doUpdate(conn, logger,
					"- SchuelerAbgaenge: Gliederung G02/K02: Aktualisiere die Einträge zum Attribut LSJahrgang (92 -> V2).",
					"UPDATE %s SET LSJahrgang = 'V2' WHERE LSJahrgang IS NOT NULL AND LSSGL IS NOT NULL AND LSSchulformSIM = 'WB' AND SUBSTRING(LSSGL, 3) IN ('G02','K02') AND LSJahrgang = '92'"
							.formatted(Schema.tab_SchuelerAbgaenge.name()));
			doUpdate(conn, logger,
					"- SchuelerAbgaenge: Gliederung R02: Aktualisiere die Einträge zum Attribut LSJahrgang (S1 -> R1).",
					"UPDATE %s SET LSJahrgang = 'R1' WHERE LSJahrgang IS NOT NULL AND LSSGL IS NOT NULL AND LSSchulformSIM = 'WB' AND SUBSTRING(LSSGL, 3) = 'R02' AND LSJahrgang = 'S1'"
							.formatted(Schema.tab_SchuelerAbgaenge.name()));
			doUpdate(conn, logger,
					"- SchuelerAbgaenge: Gliederung R02: Aktualisiere die Einträge zum Attribut LSJahrgang (S2 -> R2).",
					"UPDATE %s SET LSJahrgang = 'R2' WHERE LSJahrgang IS NOT NULL AND LSSGL IS NOT NULL AND LSSchulformSIM = 'WB' AND SUBSTRING(LSSGL, 3) = 'R02' AND LSJahrgang = 'S2'"
							.formatted(Schema.tab_SchuelerAbgaenge.name()));
			doUpdate(conn, logger,
					"- SchuelerAbgaenge: Gliederung R02: Aktualisiere die Einträge zum Attribut LSJahrgang (S3 -> R3).",
					"UPDATE %s SET LSJahrgang = 'R3' WHERE LSJahrgang IS NOT NULL AND LSSGL IS NOT NULL AND LSSchulformSIM = 'WB' AND SUBSTRING(LSSGL, 3) = 'R02' AND LSJahrgang = 'S3'"
							.formatted(Schema.tab_SchuelerAbgaenge.name()));
			doUpdate(conn, logger,
					"- SchuelerAbgaenge: Gliederung R02: Aktualisiere die Einträge zum Attribut LSJahrgang (S4 -> R4).",
					"UPDATE %s SET LSJahrgang = 'R4' WHERE LSJahrgang IS NOT NULL AND LSSGL IS NOT NULL AND LSSchulformSIM = 'WB' AND SUBSTRING(LSSGL, 3) = 'R02' AND LSJahrgang = 'S4'"
							.formatted(Schema.tab_SchuelerAbgaenge.name()));

			// SchuelerAbgaenge - LSBeginnJahrgang
			doUpdate(conn, logger,
					"- SchuelerAbgaenge: Gliederung G02/K02: Aktualisiere die Einträge zum Attribut LSBeginnJahrgang (91 -> V1).",
					"UPDATE %s SET LSBeginnJahrgang = 'V1' WHERE LSBeginnJahrgang IS NOT NULL AND LSSGL IS NOT NULL AND LSSchulformSIM = 'WB' AND SUBSTRING(LSSGL, 3) IN ('G02','K02') AND LSBeginnJahrgang = '91'"
							.formatted(Schema.tab_SchuelerAbgaenge.name()));
			doUpdate(conn, logger,
					"- SchuelerAbgaenge: Gliederung G02/K02: Aktualisiere die Einträge zum Attribut LSBeginnJahrgang (92 -> V2).",
					"UPDATE %s SET LSBeginnJahrgang = 'V2' WHERE LSBeginnJahrgang IS NOT NULL AND LSSGL IS NOT NULL AND LSSchulformSIM = 'WB' AND SUBSTRING(LSSGL, 3) IN ('G02','K02') AND LSBeginnJahrgang = '92'"
							.formatted(Schema.tab_SchuelerAbgaenge.name()));
			doUpdate(conn, logger,
					"- SchuelerAbgaenge: Gliederung R02: Aktualisiere die Einträge zum Attribut LSBeginnJahrgang (S1 -> R1).",
					"UPDATE %s SET LSBeginnJahrgang = 'R1' WHERE LSBeginnJahrgang IS NOT NULL AND LSSGL IS NOT NULL AND LSSchulformSIM = 'WB' AND SUBSTRING(LSSGL, 3) = 'R02' AND LSBeginnJahrgang = 'S1'"
							.formatted(Schema.tab_SchuelerAbgaenge.name()));
			doUpdate(conn, logger,
					"- SchuelerAbgaenge: Gliederung R02: Aktualisiere die Einträge zum Attribut LSBeginnJahrgang (S2 -> R2).",
					"UPDATE %s SET LSBeginnJahrgang = 'R2' WHERE LSBeginnJahrgang IS NOT NULL AND LSSGL IS NOT NULL AND LSSchulformSIM = 'WB' AND SUBSTRING(LSSGL, 3) = 'R02' AND LSBeginnJahrgang = 'S2'"
							.formatted(Schema.tab_SchuelerAbgaenge.name()));
			doUpdate(conn, logger,
					"- SchuelerAbgaenge: Gliederung R02: Aktualisiere die Einträge zum Attribut LSBeginnJahrgang (S3 -> R3).",
					"UPDATE %s SET LSBeginnJahrgang = 'R3' WHERE LSBeginnJahrgang IS NOT NULL AND LSSGL IS NOT NULL AND LSSchulformSIM = 'WB' AND SUBSTRING(LSSGL, 3) = 'R02' AND LSBeginnJahrgang = 'S3'"
							.formatted(Schema.tab_SchuelerAbgaenge.name()));
			doUpdate(conn, logger,
					"- SchuelerAbgaenge: Gliederung R02: Aktualisiere die Einträge zum Attribut LSBeginnJahrgang (S4 -> R4).",
					"UPDATE %s SET LSBeginnJahrgang = 'R4' WHERE LSBeginnJahrgang IS NOT NULL AND LSSGL IS NOT NULL AND LSSchulformSIM = 'WB' AND SUBSTRING(LSSGL, 3) = 'R02' AND LSBeginnJahrgang = 'S4'"
							.formatted(Schema.tab_SchuelerAbgaenge.name()));

			// Stundentafel - Aktualisiere die Spalte SGL anhand der Jahrgangstabelle
			doUpdate(conn, logger, "- Stundentafel: Aktualisiere die Spalte SGL anhand der Jahrgangstabelle.",
					"UPDATE %s st JOIN %s j ON st.Jahrgang_ID = j.ID AND j.ASDJahrgang IS NOT NULL SET st.SGL = j.SGL"
							.formatted(Schema.tab_Stundentafel.name(), Schema.tab_EigeneSchule_Jahrgaenge.name()));
			conn.transactionFlush();

			// Stundentafel - Dupliziere alle Einträge, wo die Schulgliederung nicht gesetzt ist, mit der Schulgliederung R02
			doUpdate(conn, logger, "- Stundentafel: Dupliziere alle Einträge, wo die Schulgliederung NULL ist",
					"""
					INSERT INTO %1$s (Bezeichnung, Jahrgang_ID, ASDJahrgang, SGL, Fachklasse_ID, Sichtbar, Sortierung)
					SELECT Bezeichnung, Jahrgang_ID, ASDJahrgang, 'R02' AS SGL, Fachklasse_ID, Sichtbar, Sortierung FROM %1$s WHERE SGL IS NULL
					""".formatted(Schema.tab_Stundentafel.name()));
			conn.transactionFlush();

			// Stundentafel ASDJahrgang, ggf. mithilfe von ASDSchulgliederung, wenn nicht NULL
			doUpdate(conn, logger, "- Stundentafel: Gliederung G02/K02: Setze den ASD-Jahrgang auf 'V1', wenn zuvor '91' gesetzt ist.",
					"UPDATE %s SET ASDJahrgang = 'V1' WHERE ASDJahrgang = '91' AND (SGL IN ('G02','K02') OR SGL IS NULL)"
							.formatted(Schema.tab_Stundentafel.name()));
			doUpdate(conn, logger, "- Stundentafel: Gliederung G02/K02: Setze den ASD-Jahrgang auf 'V2', wenn zuvor '92' gesetzt ist.",
					"UPDATE %s SET ASDJahrgang = 'V2' WHERE ASDJahrgang = '92' AND (SGL IN ('G02','K02') OR SGL IS NULL)"
							.formatted(Schema.tab_Stundentafel.name()));
			doUpdate(conn, logger, "- Stundentafel: Gliederung R02: Setze den ASD-Jahrgang auf 'R1', wenn zuvor 'S1' gesetzt ist.",
					"UPDATE %s SET ASDJahrgang = 'R1' WHERE ASDJahrgang = 'S1' AND SGL = 'R02'"
							.formatted(Schema.tab_Stundentafel.name()));
			doUpdate(conn, logger, "- Stundentafel: Gliederung R02: Setze den ASD-Jahrgang auf 'R2', wenn zuvor 'S2' gesetzt ist.",
					"UPDATE %s SET ASDJahrgang = 'R2' WHERE ASDJahrgang = 'S2' AND SGL = 'R02'"
							.formatted(Schema.tab_Stundentafel.name()));
			doUpdate(conn, logger, "- Stundentafel: Gliederung R02: Setze den ASD-Jahrgang auf 'R3', wenn zuvor 'S3' gesetzt ist.",
					"UPDATE %s SET ASDJahrgang = 'R3' WHERE ASDJahrgang = 'S3' AND SGL = 'R02'"
							.formatted(Schema.tab_Stundentafel.name()));
			doUpdate(conn, logger, "- Stundentafel: Gliederung R02: Setze den ASD-Jahrgang auf 'R4', wenn zuvor 'S4' gesetzt ist.",
					"UPDATE %s SET ASDJahrgang = 'R4' WHERE ASDJahrgang = 'S4' AND SGL = 'R02'"
							.formatted(Schema.tab_Stundentafel.name()));

			// Floskeln - FloskelJahrgang - Bezug zur Schulgliederung fehlt hier so dass ggf. Floskeln dupliziert werden
			doUpdate(conn, logger, "- Floskeln: Setze den FloskelJahrgang auf 'V1', wenn zuvor '91' gesetzt ist.",
					"UPDATE %s SET FloskelJahrgang = 'V1' WHERE FloskelJahrgang = '91'".formatted(Schema.tab_Floskeln.name()));
			doUpdate(conn, logger, "- Floskeln: Setze den FloskelJahrgang auf 'V2', wenn zuvor '92' gesetzt ist.",
					"UPDATE %s SET FloskelJahrgang = 'V2' WHERE FloskelJahrgang = '92'".formatted(Schema.tab_Floskeln.name()));

			doUpdate(conn, logger, "- Floskeln: Dupliziere ggf. Floskeln für die Abendrealschule, wenn der Jahrgang nicht eindeutig gegeben ist.",
					"""
					INSERT INTO %1$s(Kuerzel, FloskelText, FloskelGruppe, FloskelFach, FloskelNiveau, FloskelJahrgang)
					SELECT CONCAT(Kuerzel, '_R02') AS Kuerzel, FloskelText, FloskelGruppe, FloskelFach, FloskelNiveau, '91' AS FloskelJahrgang
					FROM %1$s WHERE FloskelJahrgang = 'V1'
					UNION
					SELECT CONCAT(Kuerzel, '_R02') AS Kuerzel, FloskelText, FloskelGruppe, FloskelFach, FloskelNiveau, '92' AS FloskelJahrgang
					FROM %1$s WHERE FloskelJahrgang = 'V2'
					UNION
					SELECT CONCAT(Kuerzel, '_R02') AS Kuerzel, FloskelText, FloskelGruppe, FloskelFach, FloskelNiveau, 'R1' AS FloskelJahrgang
					FROM %1$s WHERE FloskelJahrgang = 'S1'
					UNION
					SELECT CONCAT(Kuerzel, '_R02') AS Kuerzel, FloskelText, FloskelGruppe, FloskelFach, FloskelNiveau, 'R2' AS FloskelJahrgang
					FROM %1$s WHERE FloskelJahrgang = 'S2'
					UNION
					SELECT CONCAT(Kuerzel, '_R02') AS Kuerzel, FloskelText, FloskelGruppe, FloskelFach, FloskelNiveau, 'R3' AS FloskelJahrgang
					FROM %1$s WHERE FloskelJahrgang = 'S3'
					UNION
					SELECT CONCAT(Kuerzel, '_R02') AS Kuerzel, FloskelText, FloskelGruppe, FloskelFach, FloskelNiveau, 'R4' AS FloskelJahrgang
					FROM %1$s WHERE FloskelJahrgang = 'S4'
					""".formatted(Schema.tab_Floskeln.name()));
			conn.transactionFlush();

			// SchuelerSprachenfolge - ASDJahrgangVon und ASDJahrgangBis anpassen
			doUpdate(conn, logger, "- SchuelerSprachenfolge: Passe für die Abendrealschule die Spalten ASDJahrgangVon und ASDJahrgangBis an.",
					"""
					UPDATE %1$s ssf JOIN %2$s s ON ssf.Schueler_ID = s.ID
					JOIN %3$s sla ON s.ID = sla.Schueler_ID AND s.Schuljahresabschnitts_ID = sla.Schuljahresabschnitts_ID AND sla.WechselNr = 0
					AND sla.ASDSchulgliederung IS NOT NULL AND sla.ASDSchulgliederung = 'R02'
					SET ssf.ASDJahrgangVon = CASE WHEN ssf.ASDJahrgangVon IS NULL THEN NULL WHEN ssf.ASDJahrgangVon IN ('01','02','03','04') THEN CONCAT('R', SUBSTRING(ssf.ASDJahrgangVon, 2)) ELSE ssf.ASDJahrgangVon END,
					ssf.ASDJahrgangBis = CASE WHEN ssf.ASDJahrgangBis IS NULL THEN NULL WHEN ssf.ASDJahrgangBis IN ('01','02','03','04') THEN CONCAT('R', SUBSTRING(ssf.ASDJahrgangBis, 2)) ELSE ssf.ASDJahrgangBis END
					""".formatted(Schema.tab_SchuelerSprachenfolge.name(), Schema.tab_Schueler.name(), Schema.tab_SchuelerLernabschnittsdaten.name()));
			doUpdate(conn, logger, "- SchuelerSprachenfolge: Passe für das Abendgymnasium und das Kolleg die Spalten ASDJahrgangVon und ASDJahrgangBis an.",
					"""
					UPDATE %1$s ssf JOIN %2$s s ON ssf.Schueler_ID = s.ID
					JOIN %3$s sla ON s.ID = sla.Schueler_ID AND s.Schuljahresabschnitts_ID = sla.Schuljahresabschnitts_ID AND sla.WechselNr = 0
					AND sla.ASDSchulgliederung IS NOT NULL AND sla.ASDSchulgliederung IN ('G02', 'K02') AND ssf.ASDJahrgangVon IN ('91', '92', '01','02','03','04')
					SET ssf.ASDJahrgangVon = CASE WHEN ssf.ASDJahrgangVon IS NULL THEN NULL WHEN ssf.ASDJahrgangVon IN ('01','02','03','04','05','06') THEN CONCAT('S', SUBSTRING(ssf.ASDJahrgangVon, 2)) WHEN ssf.ASDJahrgangVon IN ('91','92') THEN CONCAT('V', SUBSTRING(ssf.ASDJahrgangVon, 2)) ELSE ssf.ASDJahrgangVon END,
					ssf.ASDJahrgangBis = CASE WHEN ssf.ASDJahrgangBis IS NULL THEN NULL WHEN ssf.ASDJahrgangBis IN ('01','02','03','04','05','06') THEN CONCAT('S', SUBSTRING(ssf.ASDJahrgangBis, 2)) WHEN ssf.ASDJahrgangBis IN ('91','92') THEN CONCAT('V', SUBSTRING(ssf.ASDJahrgangBis, 2)) ELSE ssf.ASDJahrgangBis END;
					""".formatted(Schema.tab_SchuelerSprachenfolge.name(), Schema.tab_Schueler.name(), Schema.tab_SchuelerLernabschnittsdaten.name()));

			// SchuelerSprachpruefungen - ASDJahrgang anpassen
			doUpdate(conn, logger, "- SchuelerSprachpruefungen: Passe für die Abendrealschule an die Spalte ASDJahrgang an.",
					"""
					UPDATE %1$s ssp JOIN %2$s s ON ssp.Schueler_ID = s.ID
					JOIN %3$s sla ON s.ID = sla.Schueler_ID AND s.Schuljahresabschnitts_ID = sla.Schuljahresabschnitts_ID AND sla.WechselNr = 0
					AND ssp.ASDJahrgang IS NOT NULL AND sla.ASDSchulgliederung IS NOT NULL AND sla.ASDSchulgliederung = 'R02'
					SET ssp.ASDJahrgang = CASE WHEN ssp.ASDJahrgang IN ('01','02','03','04') THEN CONCAT('R', SUBSTRING(ssp.ASDJahrgang, 2)) ELSE ssp.ASDJahrgang END;
					""".formatted(Schema.tab_SchuelerSprachpruefungen.name(), Schema.tab_Schueler.name(), Schema.tab_SchuelerLernabschnittsdaten.name()));
			doUpdate(conn, logger, "- SchuelerSprachpruefungen: Passe für das Abendgymnasium und das Kolleg an die Spalte ASDJahrgang an.",
					"""
					UPDATE %1$s ssp JOIN %2$s s ON ssp.Schueler_ID = s.ID
					JOIN %3$s sla ON s.ID = sla.Schueler_ID AND s.Schuljahresabschnitts_ID = sla.Schuljahresabschnitts_ID AND sla.WechselNr = 0
					AND ssp.ASDJahrgang IS NOT NULL AND sla.ASDSchulgliederung IS NOT NULL AND sla.ASDSchulgliederung IN ('G02', 'K02') AND ssp.ASDJahrgang IN ('91', '92', '01','02','03','04','05','06')
					SET ssp.ASDJahrgang = CASE WHEN ssp.ASDJahrgang IN ('01','02','03','04','05','06') THEN CONCAT('S', SUBSTRING(ssp.ASDJahrgang, 2)) WHEN ssp.ASDJahrgang IN ('91','92') THEN CONCAT('V', SUBSTRING(ssp.ASDJahrgang, 2)) ELSE ssp.ASDJahrgang END;
					""".formatted(Schema.tab_SchuelerSprachpruefungen.name(), Schema.tab_Schueler.name(), Schema.tab_SchuelerLernabschnittsdaten.name()));

			conn.transactionFlush();
			return true;
		} catch (final DBException e) {
			logger.logLn(e.getMessage());
			return false;
		}

	}

}
