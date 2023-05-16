package de.svws_nrw.db.schema.revisionen;

import java.util.List;
import java.util.stream.Collectors;

import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 3.
 */
public final class Revision3Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 3.
	 */
	public Revision3Updates() {
		super(SchemaRevisionen.REV_3);
		updateFremdschluessel_K_Ort();
	}

	private void updateFremdschluessel_K_Ort() {
		add("Fremdschlüssel auf K_Ort - Tabelle K_AllgAdresse",
			"UPDATE K_AllgAdresse SET AllgAdrOrt_ID = (SELECT K_Ort.ID FROM K_Ort WHERE K_Ort.PLZ=K_AllgAdresse.AllgAdrPLZ LIMIT 1)",
			DBDriver.MSSQL, "UPDATE K_AllgAdresse SET AllgAdrOrt_ID = (SELECT TOP 1 K_Ort.ID FROM K_Ort WHERE K_Ort.PLZ=K_AllgAdresse.AllgAdrPLZ)",
			Schema.tab_K_Ort, Schema.tab_K_AllgAdresse
		);
		add("Fremdschlüssel auf K_Ort - Tabelle K_Lehrer",
			"UPDATE K_Lehrer SET Ort_ID = (SELECT K_Ort.ID FROM K_Ort WHERE K_Ort.PLZ=K_Lehrer.PLZ LIMIT 1)",
			DBDriver.MSSQL, "UPDATE K_Lehrer SET Ort_ID = (SELECT TOP 1 K_Ort.ID FROM K_Ort WHERE K_Ort.PLZ=K_Lehrer.PLZ)",
			Schema.tab_K_Ort, Schema.tab_K_Lehrer
		);
		add("Fremdschlüssel auf K_Ort - Tabelle Schueler",
			"UPDATE Schueler SET Ort_ID = (SELECT K_Ort.ID FROM K_Ort WHERE K_Ort.PLZ=Schueler.PLZ LIMIT 1)",
			DBDriver.MSSQL, "UPDATE Schueler SET Ort_ID = (SELECT TOP 1 K_Ort.ID FROM K_Ort WHERE K_Ort.PLZ=Schueler.PLZ)",
			Schema.tab_K_Ort, Schema.tab_Schueler
		);
		add("Fremdschlüssel auf K_Ort - Tabelle SchuelerErzAdr",
			"UPDATE SchuelerErzAdr SET ErzOrt_ID = (SELECT K_Ort.ID FROM K_Ort WHERE K_Ort.PLZ=SchuelerErzAdr.ErzPLZ LIMIT 1)",
			DBDriver.MSSQL, "UPDATE SchuelerErzAdr SET ErzOrt_ID = (SELECT TOP 1 K_Ort.ID FROM K_Ort WHERE K_Ort.PLZ=SchuelerErzAdr.ErzPLZ)",
			Schema.tab_K_Ort, Schema.tab_SchuelerErzAdr
		);
		add("Fremdschlüssel auf K_Ort - Tabelle K_Ortsteil",
			"UPDATE K_Ortsteil SET Ort_ID = (SELECT K_Ort.ID FROM K_Ort WHERE K_Ort.PLZ=K_Ortsteil.PLZ LIMIT 1)",
			DBDriver.MSSQL, "UPDATE K_Ortsteil SET Ort_ID = (SELECT TOP 1 K_Ort.ID FROM K_Ort WHERE K_Ort.PLZ=K_Ortsteil.PLZ)",
			Schema.tab_K_Ort, Schema.tab_K_Ortsteil
		);
	}

	@Override
	public boolean runLast(final DBEntityManager conn, final Logger logger) {
		if (conn.getDBDriver() != DBDriver.MARIA_DB) {
			logger.logLn("DBMS wird für dieses Datenabank Revisions-Update nicht unterstützt.");
			return false;
		}
		final List<Integer> rowsAnzahlAbschnitte = conn.queryNative("SELECT AnzahlAbschnitte FROM EigeneSchule");
		int anzahlAbschnitte = -1;
		if ((rowsAnzahlAbschnitte.size() != 1) || (rowsAnzahlAbschnitte.get(0) == null))
			logger.logLn("Konnte die Anzahl der Abschnitte nicht bestimmen.");
		else
			anzahlAbschnitte = rowsAnzahlAbschnitte.get(0);
		if (anzahlAbschnitte == 4) {
			logger.logLn("Im Anschluss - Die Schule wurde in Schild 2 im \"Quartalsmodus\" betrieben und wird im folgenden auf den Betrieb mit Halbjahren umgestellt:");
			// Bestimme den aktuellen Abschnitt, in dem sich die Schule befindet
			final List<Long> tmpSchuljahresabschnittAktuell = conn.queryNative("SELECT Schuljahresabschnitts_ID FROM EigeneSchule");
			final long schuljahresabschnittAktuell = tmpSchuljahresabschnittAktuell.get(0);
			final List<Object[]> tmpAktAbschnitt = conn.queryNative("SELECT ID, Jahr, Abschnitt, VorigerAbschnitt_ID, FolgeAbschnitt_ID FROM Schuljahresabschnitte WHERE ID = " + schuljahresabschnittAktuell);
			final Object[] aktAbschnitt = tmpAktAbschnitt.get(0);
			final int aktSchuljahr = (Integer) aktAbschnitt[1];
			final int aktQuartal = (Integer) aktAbschnitt[2];
			final int aktHalbjahr = (aktQuartal % 2 == 1) ? aktQuartal / 2 + 1 : aktQuartal / 2;
			final Long aktFolgeAbschnittID = (Long) aktAbschnitt[4];
			// Passe das aktuelle Quartal an, falls es Quartal 1 oder 3 ist
			logger.logLn("* Sie befindet sich aktuell im Quartal " + aktQuartal + " (Schuljahr " + aktSchuljahr + "/" + (aktSchuljahr + 1 - 2000) + ", " + aktHalbjahr + ". Halbjahr)");
			logger.modifyIndent(2);
			if ((aktQuartal == 1) || (aktQuartal == 3)) {
				logger.logLn("... also im ersten Quartal eines Halbjahres");
				if (aktFolgeAbschnittID == null) {
					logger.logLn("... und es existiert noch kein Folgeabschnitt");
					// Erhöhe das Quartal in dem aktuellen Schuljahresabschnitt, so dass dies das zweite Quartal im Halbjahr wird
					logger.logLn("- Erhöhe das Quartal in dem aktuellen Schuljahresabschnitt, so dass dies das zweite Quartal im Halbjahr wird...");
					if (Integer.MIN_VALUE == conn.executeNativeUpdate("UPDATE Schuljahresabschnitte SET Schuljahresabschnitte.Abschnitt = Schuljahresabschnitte.Abschnitt + 1 WHERE ID = " + schuljahresabschnittAktuell)) {
						logger.logLn("Fehler beim Erhöhen des Quartals beim aktuellen Schuljahresabschnitt");
						logger.modifyIndent(-2);
						return false;
					}
					// Verschiebe die Noten-Einträge bei den Schülerleistungsdaten in das Feld für die Quartalsnoten des Halbjahres
					logger.logLn("- Verschiebe die Noten-Einträge bei den Schülerleistungsdaten in das Feld für die Quartalsnoten des Halbjahres...");
					if (Integer.MIN_VALUE == conn.executeNativeUpdate("UPDATE SchuelerLeistungsdaten JOIN SchuelerLernabschnittsdaten ON SchuelerLeistungsdaten.Abschnitt_ID = SchuelerLernabschnittsdaten.ID AND SchuelerLernabschnittsdaten.Schuljahresabschnitts_ID = " + schuljahresabschnittAktuell + " SET SchuelerLeistungsdaten.NotenKrzQuartal = SchuelerLeistungsdaten.NotenKrz, SchuelerLeistungsdaten.NotenKrz = NULL")) {
						logger.logLn("Fehler beim Anpassen der Schüler-Leistungsdaten des aktuellen Schuljahresabschnittes (Noten -> Quartalsnoten)");
						logger.modifyIndent(-2);
						return false;
					}
				} else {
					logger.logLn("... und es existiert noch ein Folgeabschnitt");
					// Setze den Abschnitt in EigeneSchule auf den FolgeAbschnitt, die Anpassung der Schülerleistungsdaten erfolgt später automatisch
					logger.logLn("- Setze den Abschnitt in EigeneSchule auf den FolgeAbschnitt, die Anpassung der Schülerleistungsdaten erfolgt später automatisch...");
					if (Integer.MIN_VALUE == conn.executeNativeUpdate("UPDATE EigeneSchule JOIN Schuljahresabschnitte ON EigeneSchule.Schuljahresabschnitts_ID = Schuljahresabschnitte.ID SET EigeneSchule.Schuljahresabschnitts_ID = Schuljahresabschnitte.FolgeAbschnitt_ID")) {
						logger.logLn("Fehler beim Anpassen des aktuellen Schuljahresabschnitt in der Tabelle EigeneSchule");
						logger.modifyIndent(-2);
						return false;
					}
					logger.logLn("- Verschiebe bei allen Schülern den aktuellen Lernabschnitt in den Folgelernabschnitt, wo noch kein Folgelernabschnitt für diesen existiert.");
					String sql = "UPDATE SchuelerLernabschnittsdaten sla1"
							+ " JOIN SchuljahresAbschnitte sja ON sla1.Schuljahresabschnitts_ID = sja.ID AND sla1.Schuljahresabschnitts_ID = " + schuljahresabschnittAktuell
							+ " LEFT JOIN SchuelerLernabschnittsdaten sla2 ON sla2.Schuljahresabschnitts_ID = sja.FolgeAbschnitt_ID AND sla1.Schueler_ID = sla2.Schueler_ID AND sla2.Schuljahresabschnitts_ID = " + aktFolgeAbschnittID
							+ " SET sla1.Schuljahresabschnitts_ID = " + aktFolgeAbschnittID + " WHERE sla2.ID IS NULL";
					if (Integer.MIN_VALUE == conn.executeNativeUpdate(sql)) {
						logger.logLn("Fehler beim Verschieben des aktuellen Lernabschnittes (1. Quartal des Halbjahres in das 2. Quartal)");
						logger.modifyIndent(-2);
						return false;
					}
					logger.logLn("- Verschiebe dann bei diesen Lernabschnitte die Noten-Einträge in das Feld für die Quartalsnoten des Halbjahres.");
					sql = "UPDATE SchuelerLeistungsdaten SET NotenKrzQuartal = NotenKrz, NotenKrz = NULL WHERE Abschnitt_ID IN ("
							+ "SELECT sla1.ID FROM SchuljahresAbschnitte sja"
							+ " JOIN SchuelerLernabschnittsdaten sla1 ON sla1.Schuljahresabschnitts_ID = sja.ID AND sla1.Schuljahresabschnitts_ID = " + aktFolgeAbschnittID
					        + " LEFT JOIN SchuelerLernabschnittsdaten sla2 ON sla2.Schuljahresabschnitts_ID = sja.VorigerAbschnitt_ID AND sla1.Schueler_ID = sla2.Schueler_ID AND sla2.Schuljahresabschnitts_ID = " + schuljahresabschnittAktuell
							+ " WHERE sla2.ID IS NULL)";
					if (Integer.MIN_VALUE == conn.executeNativeUpdate(sql)) {
						logger.logLn("Fehler beim Verschieben des zugehörigen Noteneinträge in die Quartalsnote");
						logger.modifyIndent(-2);
						return false;
					}
					logger.logLn("- Korrigiere bei diesen Lernabschnitte auch die Klassenzuordnung in den neuen Lernabschnitt.");
					sql = "UPDATE SchuelerLernabschnittsdaten sla JOIN Klassen k ON sla.Klassen_ID = k.ID AND sla.Schuljahresabschnitts_ID <> k.Schuljahresabschnitts_ID"
							+ " JOIN Klassen k2 ON sla.Schuljahresabschnitts_ID = k2.Schuljahresabschnitts_ID AND k.Klasse = k2.Klasse"
							+ " SET sla.Klassen_ID = k2.ID";
					if (Integer.MIN_VALUE == conn.executeNativeUpdate(sql)) {
						logger.logLn("Fehler beim Korrigieren der zugehörigen Klasseneinträge bei den Lernabschnitten");
						logger.modifyIndent(-2);
						return false;
					}
				}
			}
			// Anpassen des Schuljahresabschnittes bei allen Einträgen der Schüler-Tabelle (z.B. bei Abgängern)
			logger.logLn("- Anpassen des Schuljahresabschnittes bei allen Einträgen der Schüler-Tabelle (z.B. bei Abgängern)...");
			if (Integer.MIN_VALUE == conn.executeNativeUpdate("UPDATE Schueler JOIN Schuljahresabschnitte ON Schueler.Schuljahresabschnitts_ID = Schuljahresabschnitte.ID AND Schuljahresabschnitte.Abschnitt IN (1,3) SET Schueler.Schuljahresabschnitts_ID = Schuljahresabschnitte.FolgeAbschnitt_ID")) {
				logger.logLn("Fehler beim Anpassen der Schuljahresabschnitte in der Tabelle Schueler");
				logger.modifyIndent(-2);
				return false;
			}
			// Ggf. Korrektur bei Einträgen in der SchuelerAbitur-Tabelle (hier sollte eigentlich immer ein zweites Quartal eingetragen sein)
			logger.logLn("- Ggf. Korrektur bei Einträgen in der SchuelerAbitur-Tabelle (hier sollte eigentlich immer ein zweites Quartal eingetragen sein)...");
			if (Integer.MIN_VALUE == conn.executeNativeUpdate("UPDATE SchuelerAbitur JOIN Schuljahresabschnitte ON SchuelerAbitur.Schuljahresabschnitts_ID = Schuljahresabschnitte.ID AND Schuljahresabschnitte.Abschnitt IN (1,3) SET SchuelerAbitur.Schuljahresabschnitts_ID = Schuljahresabschnitte.FolgeAbschnitt_ID")) {
				logger.logLn("Fehler beim Anpassen der Schuljahresabschnitte in der Tabelle SchuelerAbitur");
				logger.modifyIndent(-2);
				return false;
			}
			// Ggf. Korrektur bei Einträgen in der SchuelerZP10-Tabelle (hier sollte eigentlich immer ein zweites Quartal eingetragen sein)
			logger.logLn("- Ggf. Korrektur bei Einträgen in der SchuelerZP10-Tabelle (hier sollte eigentlich immer ein zweites Quartal eingetragen sein)...");
			if (Integer.MIN_VALUE == conn.executeNativeUpdate("UPDATE SchuelerZP10 JOIN Schuljahresabschnitte ON SchuelerZP10.Schuljahresabschnitts_ID = Schuljahresabschnitte.ID AND Schuljahresabschnitte.Abschnitt IN (1,3) SET SchuelerZP10.Schuljahresabschnitts_ID = Schuljahresabschnitte.FolgeAbschnitt_ID")) {
				logger.logLn("Fehler beim Anpassen der Schuljahresabschnitte in der Tabelle SchuelerZP10");
				logger.modifyIndent(-2);
				return false;
			}
			// Ggf. Korrektur bei Einträgen in der SchuelerBKAbschluss-Tabelle (sollte nicht relevant sein)
			logger.logLn("- Ggf. Korrektur bei Einträgen in der SchuelerBKAbschluss-Tabelle (sollte nicht relevant sein)...");
			if (Integer.MIN_VALUE == conn.executeNativeUpdate("UPDATE SchuelerBKAbschluss JOIN Schuljahresabschnitte ON SchuelerBKAbschluss.Schuljahresabschnitts_ID = Schuljahresabschnitte.ID AND Schuljahresabschnitte.Abschnitt IN (1,3) SET SchuelerBKAbschluss.Schuljahresabschnitts_ID = Schuljahresabschnitte.FolgeAbschnitt_ID")) {
				logger.logLn("Fehler beim Anpassen der Schuljahresabschnitte in der Tabelle SchuelerBKAbschluss");
				logger.modifyIndent(-2);
				return false;
			}
			// Ggf. Korrektur bei Einträgen in der SchuelerBKFaecher-Tabelle (sollte nicht relevant sein)
			logger.logLn("- Ggf. Korrektur bei Einträgen in der SchuelerBKFaecher-Tabelle (sollte nicht relevant sein)...");
			if (Integer.MIN_VALUE == conn.executeNativeUpdate("UPDATE SchuelerBKFaecher JOIN Schuljahresabschnitte ON SchuelerBKFaecher.Schuljahresabschnitts_ID = Schuljahresabschnitte.ID AND Schuljahresabschnitte.Abschnitt IN (1,3) SET SchuelerBKFaecher.Schuljahresabschnitts_ID = Schuljahresabschnitte.FolgeAbschnitt_ID")) {
				logger.logLn("Fehler beim Anpassen der Schuljahresabschnitte in der Tabelle SchuelerBKFaecher");
				logger.modifyIndent(-2);
				return false;
			}
			// Durchwandere die Schüler-Lernabschnitte zu alle Schuljahresabschnitten im 1. und im 3. Quartal
			logger.logLn("- Durchwandere die Schüler-Lernabschnitte zu allen Schuljahresabschnitten im 1. und im 3. Quartal...");
			logger.modifyIndent(2);
			final List<Object[]> tmpSchuljahresAbschnitte = conn.queryNative("SELECT ID, Jahr, Abschnitt, VorigerAbschnitt_ID, FolgeAbschnitt_ID FROM Schuljahresabschnitte WHERE Abschnitt IN (1,3)");
			for (final Object[] schuljahreAbschnitt : tmpSchuljahresAbschnitte) {
				final long abschnittID = (Long) schuljahreAbschnitt[0];
				final int schuljahr = (Integer) schuljahreAbschnitt[1];
				final int quartal = (Integer) schuljahreAbschnitt[2];
				final int halbjahr = (quartal % 2 == 1) ? quartal / 2 + 1 : quartal / 2;
				final Long folgeAbschnittID = (Long) schuljahreAbschnitt[4];
				logger.logLn("- Schuljahres-Abschnitt " + abschnittID + " (Schuljahr " + schuljahr + "/" + (schuljahr + 1 - 2000) + ", " + halbjahr + ". Halbjahr, Quartal " + quartal + "):");
				logger.modifyIndent(2);
				// Bestimme die IDs aller Schüler-Lernabschnitte, die zu diesem Schuljahresabschnitt gehören
				final List<Long> lernabschnittIDs = conn.queryNative("SELECT ID FROM SchuelerLernabschnittsdaten WHERE WechselNr IS NULL AND Schuljahresabschnitts_ID = " + abschnittID);
				logger.logLn("- Übertragen der Leistungsdaten und der Teilleistungen der zugehörigen Schüler-Lernabschnitte in den jeweiligen Folgeabschnitt (ID des Folge-Schuljahresabschnitts: " + folgeAbschnittID + ")...");
				for (final long lernabschnittID : lernabschnittIDs) {
					String sql = "SELECT q2.ID FROM SchuelerLernabschnittsdaten q1 JOIN SchuelerLernabschnittsdaten q2 ON q1.ID = "
						+ lernabschnittID + " AND q1.Schueler_ID = q2.Schueler_ID AND q2.Schuljahresabschnitts_ID = "
						+ folgeAbschnittID + " AND q2.WechselNr IS NULL";
					final List<Long> tmpFolgeLernabschnitte = conn.queryNative(sql);
					if (tmpFolgeLernabschnitte.size() == 1) {
						final long folgeLernabschnittID = tmpFolgeLernabschnitte.get(0);
						// Kopiere die Quartalsnoten und die Teilleistungen, wenn sich die Leistungsdaten zuordnen lassen
						sql = "UPDATE SchuelerLeistungsdaten q1 JOIN SchuelerLeistungsdaten q2 "
							+ "ON q1.Abschnitt_ID = " + lernabschnittID + " AND q2.Abschnitt_ID = " + folgeLernabschnittID + " AND q1.Fach_ID = q2.Fach_ID AND q1.Kursart = q2.Kursart "
							+ "SET q2.NotenKrzQuartal = q1.NotenKrz";
						if (Integer.MIN_VALUE == conn.executeNativeUpdate(sql)) {
							logger.logLn("Fehler beim Kopieren der Quartalsnoten");
							return false;
						}
						// Verschiebe die Teilleistungen zu den Leistungsdaten im 2. Quartal, wenn sich die Leistungsdaten zuordnen lassen
						sql = "UPDATE SchuelerEinzelleistungen e JOIN SchuelerLeistungsdaten q1 ON e.Leistung_ID = q1.ID AND q1.Abschnitt_ID = "
							+ lernabschnittID + " JOIN SchuelerLeistungsdaten q2 ON q2.Abschnitt_ID = "
							+ folgeLernabschnittID + " AND q1.Fach_ID = q2.Fach_ID AND q1.Kursart = q2.Kursart SET e.Leistung_ID = q2.ID";
						if (Integer.MIN_VALUE == conn.executeNativeUpdate(sql)) {
							logger.logLn("Fehler beim Kopieren der Teilleistungen");
							return false;
						}
						// Entferne die Leistungsdaten, wo die Noten zuvor kopiert wurden bzw. die Teilleistungen verschoben wurden.
						sql = "DELETE FROM SchuelerLeistungsdaten WHERE Abschnitt_ID = "
							+ lernabschnittID + " AND (Fach_ID, Kursart) IN (SELECT Fach_ID, Kursart FROM SchuelerLeistungsdaten WHERE Abschnitt_ID = "
							+ folgeLernabschnittID + ")";
						if (Integer.MIN_VALUE == conn.executeNativeUpdate(sql)) {
							logger.logLn("Fehler beim Entfernen von zuvor kopierten verschobenen Leistungsdaten");
							return false;
						}
						// Verschiebe die Leistungsdaten in den anderen Lernabschnitt, wenn sich die Leistungsdaten nicht zuordnen lassen
						sql = "UPDATE SchuelerLeistungsdaten SET Abschnitt_ID = " + folgeLernabschnittID + " WHERE Abschnitt_ID = " + lernabschnittID;
						if (Integer.MIN_VALUE == conn.executeNativeUpdate(sql)) {
							logger.logLn("Fehler beim Verschieben von Leistungsdaten in das jeweilige zweite Quartal des Halbjahres.");
							return false;
						}
					}
				}
				// Verschiebe die Kurse in das zweite Quartal des Halbjahres, welche im zweiten Quartal nicht entsprechend angelegt wurden
				logger.logLn("- Verschiebe die Kurse in das zweite Quartal des Halbjahres, welche im zweiten Quartal nicht entsprechend angelegt wurden...");
				String sql = "UPDATE Kurse SET Schuljahresabschnitts_ID = " + folgeAbschnittID + " WHERE Schuljahresabschnitts_ID = "
					+ abschnittID + " AND ID NOT IN (SELECT k1.ID FROM Kurse k1 JOIN Kurse k2 ON "
					+ "k1.Schuljahresabschnitts_ID = " + abschnittID + " AND k2.Schuljahresabschnitts_ID = " + folgeAbschnittID
					+ " AND k1.KurzBez = k2.KurzBez AND ((k1.Jahrgang_ID IS NULL AND k2.Jahrgang_ID IS NULL) OR (k1.Jahrgang_ID = k2.Jahrgang_ID)) AND k1.ASDJahrgang = k2.ASDJahrgang "
					+ "AND k1.Fach_ID = k2.Fach_ID AND k1.KursartAllg = k2.KursartAllg)";
				if (Integer.MIN_VALUE == conn.executeNativeUpdate(sql)) {
					logger.logLn("Fehler beim Verschieben von Kursen in das jeweilige zweite Quartal des Halbjahres.");
					return false;
				}
				// Entferne die Schülerlernabschnitte, die zu dem Quartal gehört haben.
				logger.logLn("- Entferne die Schülerlernabschnitte, dessen Leistungsdaten gerade in das 2. Quartal übertragen wurden...");
				sql = "DELETE FROM SchuelerLernabschnittsdaten WHERE Schuljahresabschnitts_ID = " + abschnittID;
				if (Integer.MIN_VALUE == conn.executeNativeUpdate(sql)) {
					logger.logLn("Fehler beim Löschen der Schüler-Lernabschnitte des ersten Quartals.");
					return false;
				}
				// Entferne die Kurszuordnung bei Schülerleistungsdaten, wo der Schüler ansonsten einem Kurs doppelt zugeordnet wäre (Fehlerkorrektur von unstimmigen Leistungsdaten)
				logger.logLn("- Entferne die Kurszuordnung bei Schülerleistungsdaten, wo der Schüler ansonsten einem Kurs doppelt zugeordnet wäre (Fehlerkorrektur von unstimmigen Leistungsdaten)...");
				sql = "SELECT s.ID FROM SchuelerLeistungsdaten s "
					+ "JOIN Kurse k1 ON s.Kurs_ID = k1.ID AND k1.Schuljahresabschnitts_ID = " + abschnittID
					+ " JOIN Kurse k2 ON k2.Schuljahresabschnitts_ID = " + folgeAbschnittID
					+ " AND k1.KurzBez = k2.KurzBez AND k1.Jahrgang_ID = k2.Jahrgang_ID AND k1.ASDJahrgang = k2.ASDJahrgang "
					+ "AND k1.Fach_ID = k2.Fach_ID AND k1.KursartAllg = k2.KursartAllg "
				    + "JOIN SchuelerLernabschnittsdaten sla ON s.Abschnitt_ID = sla.ID "
				    + "AND (sla.Schueler_ID, k1.ID) IN (SELECT Schueler_ID, Kurs_ID FROM Kurs_Schueler) "
				    + "AND (sla.Schueler_ID, k2.ID) IN (SELECT Schueler_ID, Kurs_ID FROM Kurs_Schueler) ";
				final List<Long> tmpLeistungsdatenIDs = conn.queryNative(sql);
				if (!tmpLeistungsdatenIDs.isEmpty()) {
					sql = "UPDATE SchuelerLeistungsdaten SET Kurs_ID = NULL WHERE ID IN ";
					sql += tmpLeistungsdatenIDs.stream().map(id -> "" + id).collect(Collectors.joining(",", "(", ")"));
					if (Integer.MIN_VALUE == conn.executeNativeUpdate(sql)) {
						logger.logLn("Fehler beim Entfernen der Kurszuordnung in den Leistungsdaten.");
						return false;
					}
				}
				// Korrigiere die Kurszuordnung bei den Leistungsdaten, welche noch Kurse in diesem Schuljahresabschnitt haben
				logger.logLn("- Korrigiere die Kurszuordnung bei den übertragenen Leistungsdaten, welche noch Kurszurodnungen zu Kursen in dem übertragenen Schuljahresabschnitt haben...");
				sql = "UPDATE SchuelerLeistungsdaten s JOIN Kurse k1 ON s.Kurs_ID = k1.ID AND k1.Schuljahresabschnitts_ID = "
					+ abschnittID + " JOIN Kurse k2 ON k2.Schuljahresabschnitts_ID = "
					+ folgeAbschnittID + " AND k1.KurzBez = k2.KurzBez AND k1.Jahrgang_ID = k2.Jahrgang_ID AND k1.ASDJahrgang = k2.ASDJahrgang "
					+ "AND k1.Fach_ID = k2.Fach_ID AND k1.KursartAllg = k2.KursartAllg SET s.Kurs_ID = k2.ID";
				if (Integer.MIN_VALUE == conn.executeNativeUpdate(sql)) {
					logger.logLn("Fehler bei der Korrektur der Kurszuordnungen.");
					return false;
				}
				logger.modifyIndent(-2);
			}
			logger.modifyIndent(-2);
			// Nachdem die Leistungen in die Halbjahresabschnitte verschoben wurden, können die Schuljahresabschnitt mit allen daran "hängenden" Daten entfernt werden.
			logger.logLn("- Entferne die Schuljahresabschnitt der ersten Quartal der Halbjahre mit allen daran \"hängenden\" Daten - diese werden jetzt nicht mehr benötigt...");
			if (Integer.MIN_VALUE == conn.executeNativeUpdate("DELETE FROM Schuljahresabschnitte WHERE Schuljahresabschnitte.Abschnitt IN (1,3)")) {
				logger.logLn("Fehler beim Entfernen der Schuljahresabschnitte für die Quartale 1 und 3");
				logger.modifyIndent(-2);
				return false;
			}
			// Jetzt kann auch der Abschnittseintrag in der Tabelle Schuljahresabschnitte von Quartal auf Halbjahr abgeändert werden
			logger.logLn("- Ändern des Abschnittseintrags in der Tabelle Schuljahresabschnitte von Quartal auf Halbjahr...");
			if (Integer.MIN_VALUE == conn.executeNativeUpdate("UPDATE Schuljahresabschnitte SET Schuljahresabschnitte.Abschnitt = Schuljahresabschnitte.Abschnitt / 2")) {
				logger.logLn("Fehler beim Umstellen der Abschnitte bei den Schuljahresabschnitten von Quartal auf Halbjahrnummerierung");
				logger.modifyIndent(-2);
				return false;
			}
			// Und zum Abschluss werden die Anzahl der Abschnitte und deren Bezeichnungen bei der Tabelle EigeneSchule umgestellt
			logger.logLn("- Stelle die Anzahl der Abschnitte und deren Bezeichnungen bei der Tabelle EigeneSchule um...");
			if (Integer.MIN_VALUE == conn.executeNativeUpdate("UPDATE EigeneSchule SET EigeneSchule.AnzahlAbschnitte = 2, EigeneSchule.AbschnittBez = 'Halbjahr', EigeneSchule.BezAbschnitt1 = '1. Hj.', EigeneSchule.BezAbschnitt2 = '2. Hj.', EigeneSchule.BezAbschnitt3 = NULL, EigeneSchule.BezAbschnitt4 = NULL")) {
				logger.logLn("Fehler biem Umstellen der Tabelle EigeneSchule auf zwei Abschnitte.");
				logger.modifyIndent(-2);
				return false;
			}
			logger.modifyIndent(-2);
		}
		return true;
	}

}
