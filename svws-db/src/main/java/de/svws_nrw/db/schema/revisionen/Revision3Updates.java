package de.svws_nrw.db.schema.revisionen;

import java.util.List;
import java.util.stream.Collectors;

import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
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


	private static boolean entferneQuartalsmodus(final DBEntityManager conn, final Logger logger) {
		final List<Integer> rowsAnzahlAbschnitte = conn.queryNative("SELECT AnzahlAbschnitte FROM EigeneSchule");
		int anzahlAbschnitte = -1;
		if ((rowsAnzahlAbschnitte.size() != 1) || (rowsAnzahlAbschnitte.get(0) == null))
			logger.logLn("Konnte die Anzahl der Abschnitte nicht bestimmen.");
		else
			anzahlAbschnitte = rowsAnzahlAbschnitte.get(0);
		if (anzahlAbschnitte == 4) {
			logger.logLn(
					"Im Anschluss - Die Schule wurde in Schild 2 im \"Quartalsmodus\" betrieben und wird im folgenden auf den Betrieb mit Halbjahren umgestellt:");
			// Bestimme den aktuellen Abschnitt, in dem sich die Schule befindet
			final List<Long> tmpSchuljahresabschnittAktuell = conn.queryNative("SELECT Schuljahresabschnitts_ID FROM EigeneSchule");
			final List<Long> tmpSchuljahresabschnittMax = conn.queryNative(
					"SELECT ID FROM Schuljahresabschnitte WHERE Jahr = (SELECT max(Jahr) FROM Schuljahresabschnitte) AND Abschnitt = (SELECT max(Abschnitt) FROM Schuljahresabschnitte WHERE Jahr = (SELECT max(Jahr) FROM Schuljahresabschnitte))");
			final long schuljahresabschnittAktuell = tmpSchuljahresabschnittAktuell.get(0);
			final long schuljahresabschnittMax = tmpSchuljahresabschnittMax.get(0);
			final List<Object[]> tmpAktAbschnitt = conn.queryNative(
					"SELECT ID, Jahr, Abschnitt, VorigerAbschnitt_ID, FolgeAbschnitt_ID FROM Schuljahresabschnitte WHERE ID = " + schuljahresabschnittAktuell);
			final Object[] aktAbschnitt = tmpAktAbschnitt.get(0);
			final int aktSchuljahr = (Integer) aktAbschnitt[1];
			final int aktQuartal = (Integer) aktAbschnitt[2];
			final int aktHalbjahr = ((aktQuartal % 2) == 1) ? ((aktQuartal / 2) + 1) : (aktQuartal / 2);
			final Long aktFolgeAbschnittID = (Long) aktAbschnitt[4];
			// Lege temporär Indizes an, um die Umstellung zu beschleunigen
			logger.logLn("* Lege temporär Indizes an, um die Umstellung zu beschleunigen");
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("CREATE INDEX quartals_modus_1 ON SchuelerLeistungsdaten(Abschnitt_ID)")) {
				logger.logLn(2, "Fehler beim Erstellen des Index quartals_modus_1");
				return false;
			}
			if (Integer.MIN_VALUE == conn
					.transactionNativeUpdateAndFlush("CREATE INDEX quartals_modus_2 ON SchuelerLernabschnittsdaten(Schuljahresabschnitts_ID)")) {
				logger.logLn(2, "Fehler beim Erstellen des Index quartals_modus_2");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("CREATE INDEX quartals_modus_3 ON SchuelerLernabschnittsdaten(Schueler_ID)")) {
				logger.logLn(2, "Fehler beim Erstellen des Index quartals_modus_3");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("CREATE INDEX quartals_modus_4 ON Schuljahresabschnitte(FolgeAbschnitt_ID)")) {
				logger.logLn(2, "Fehler beim Erstellen des Index quartals_modus_4");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("CREATE INDEX quartals_modus_5 ON Schuljahresabschnitte(VorigerAbschnitt_ID)")) {
				logger.logLn(2, "Fehler beim Erstellen des Index quartals_modus_5");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("CREATE INDEX quartals_modus_6 ON SchuelerLernabschnittsdaten(Klassen_ID)")) {
				logger.logLn(2, "Fehler beim Erstellen des Index quartals_modus_6");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("CREATE INDEX quartals_modus_7 ON Klassen(Schuljahresabschnitts_ID)")) {
				logger.logLn(2, "Fehler beim Erstellen des Index quartals_modus_7");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("CREATE INDEX quartals_modus_8 ON Klassen(Klasse)")) {
				logger.logLn(2, "Fehler beim Erstellen des Index quartals_modus_8");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("CREATE INDEX quartals_modus_9 ON SchuelerLernabschnittsdaten(WechselNr)")) {
				logger.logLn(2, "Fehler beim Erstellen des Index quartals_modus_9");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("CREATE INDEX quartals_modus_10 ON SchuelerLeistungsdaten(Fach_ID)")) {
				logger.logLn(2, "Fehler beim Erstellen des Index quartals_modus_10");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("CREATE INDEX quartals_modus_11 ON SchuelerLeistungsdaten(Kursart)")) {
				logger.logLn(2, "Fehler beim Erstellen des Index quartals_modus_11");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("CREATE INDEX quartals_modus_12 ON SchuelerLeistungsdaten(KursartAllg)")) {
				logger.logLn(2, "Fehler beim Erstellen des Index quartals_modus_12");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("CREATE INDEX quartals_modus_13 ON Kurse(Schuljahresabschnitts_ID)")) {
				logger.logLn(2, "Fehler beim Erstellen des Index quartals_modus_13");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("CREATE INDEX quartals_modus_14 ON Kurse(KurzBez)")) {
				logger.logLn(2, "Fehler beim Erstellen des Index quartals_modus_14");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("CREATE INDEX quartals_modus_15 ON Kurse(Jahrgang_ID)")) {
				logger.logLn(2, "Fehler beim Erstellen des Index quartals_modus_15");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("CREATE INDEX quartals_modus_16 ON Kurse(ASDJahrgang)")) {
				logger.logLn(2, "Fehler beim Erstellen des Index quartals_modus_16");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("CREATE INDEX quartals_modus_17 ON Kurse(Fach_ID)")) {
				logger.logLn(2, "Fehler beim Erstellen des Index quartals_modus_17");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("CREATE INDEX quartals_modus_18 ON Kurse(KursartAllg)")) {
				logger.logLn(2, "Fehler beim Erstellen des Index quartals_modus_18");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("CREATE INDEX quartals_modus_19 ON SchuelerLeistungsdaten(Kurs_ID)")) {
				logger.logLn(2, "Fehler beim Erstellen des Index quartals_modus_19");
				return false;
			}
			// Passe das aktuelle Quartal an, falls es Quartal 1 oder 3 ist
			logger.logLn("* Sie befindet sich aktuell im Quartal " + aktQuartal + " (Schuljahr " + aktSchuljahr + "/" + ((aktSchuljahr + 1) - 2000) + ", "
					+ aktHalbjahr + ". Halbjahr)");
			logger.modifyIndent(2);
			if ((aktQuartal == 1) || (aktQuartal == 3)) {
				logger.logLn("... also im ersten Quartal eines Halbjahres");
				if (aktFolgeAbschnittID == null) {
					logger.logLn("... und es existiert noch kein Folgeabschnitt");
					// Erhöhe das Quartal in dem aktuellen Schuljahresabschnitt, so dass dies das zweite Quartal im Halbjahr wird
					logger.logLn("- Erhöhe das Quartal in dem aktuellen Schuljahresabschnitt, so dass dies das zweite Quartal im Halbjahr wird...");
					if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(
							"UPDATE Schuljahresabschnitte SET Schuljahresabschnitte.Abschnitt = Schuljahresabschnitte.Abschnitt + 1 WHERE ID = "
									+ schuljahresabschnittAktuell)) {
						logger.logLn("Fehler beim Erhöhen des Quartals beim aktuellen Schuljahresabschnitt");
						logger.modifyIndent(-2);
						return false;
					}
					// Verschiebe die Noten-Einträge bei den Schülerleistungsdaten in das Feld für die Quartalsnoten des Halbjahres
					logger.logLn("- Verschiebe die Noten-Einträge bei den Schülerleistungsdaten in das Feld für die Quartalsnoten des Halbjahres...");
					if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(
							"UPDATE SchuelerLeistungsdaten JOIN SchuelerLernabschnittsdaten ON SchuelerLeistungsdaten.Abschnitt_ID = SchuelerLernabschnittsdaten.ID AND SchuelerLernabschnittsdaten.Schuljahresabschnitts_ID = "
									+ schuljahresabschnittAktuell
									+ " SET SchuelerLeistungsdaten.NotenKrzQuartal = SchuelerLeistungsdaten.NotenKrz, SchuelerLeistungsdaten.NotenKrz = NULL")) {
						logger.logLn("Fehler beim Anpassen der Schüler-Leistungsdaten des aktuellen Schuljahresabschnittes (Noten -> Quartalsnoten)");
						logger.modifyIndent(-2);
						return false;
					}
				} else {
					logger.logLn("... und es existiert ein Folgeabschnitt");
					// Setze den Abschnitt in EigeneSchule auf den FolgeAbschnitt, die Anpassung der Schülerleistungsdaten erfolgt später automatisch
					logger.logLn(
							"- Setze den Abschnitt in EigeneSchule auf den FolgeAbschnitt, die Anpassung der Schülerleistungsdaten erfolgt später automatisch...");
					if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(
							"UPDATE EigeneSchule JOIN Schuljahresabschnitte ON EigeneSchule.Schuljahresabschnitts_ID = Schuljahresabschnitte.ID SET EigeneSchule.Schuljahresabschnitts_ID = Schuljahresabschnitte.FolgeAbschnitt_ID")) {
						logger.logLn("Fehler beim Anpassen des aktuellen Schuljahresabschnitt in der Tabelle EigeneSchule");
						logger.modifyIndent(-2);
						return false;
					}
					logger.logLn(
							"- Verschiebe bei allen Schülern den aktuellen Lernabschnitt in den Folgelernabschnitt, wo noch kein Folgelernabschnitt für diesen existiert.");
					String sql = "UPDATE SchuelerLernabschnittsdaten sla1"
							+ " JOIN Schuljahresabschnitte sja ON sla1.Schuljahresabschnitts_ID = sja.ID AND sla1.Schuljahresabschnitts_ID = "
							+ schuljahresabschnittAktuell
							+ " LEFT JOIN SchuelerLernabschnittsdaten sla2 ON sla2.Schuljahresabschnitts_ID = sja.FolgeAbschnitt_ID AND sla1.Schueler_ID = sla2.Schueler_ID AND sla2.Schuljahresabschnitts_ID = "
							+ aktFolgeAbschnittID
							+ " SET sla1.Schuljahresabschnitts_ID = " + aktFolgeAbschnittID + " WHERE sla2.ID IS NULL";
					if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql)) {
						logger.logLn("Fehler beim Verschieben des aktuellen Lernabschnittes (1. Quartal des Halbjahres in das 2. Quartal)");
						logger.modifyIndent(-2);
						return false;
					}
					logger.logLn("- Verschiebe dann bei diesen Lernabschnitte die Noten-Einträge in das Feld für die Quartalsnoten des Halbjahres.");
					sql = "UPDATE SchuelerLeistungsdaten SET NotenKrzQuartal = NotenKrz, NotenKrz = NULL WHERE Abschnitt_ID IN ("
							+ "SELECT sla1.ID FROM Schuljahresabschnitte sja"
							+ " JOIN SchuelerLernabschnittsdaten sla1 ON sla1.Schuljahresabschnitts_ID = sja.ID AND sla1.Schuljahresabschnitts_ID = "
							+ aktFolgeAbschnittID
							+ " LEFT JOIN SchuelerLernabschnittsdaten sla2 ON sla2.Schuljahresabschnitts_ID = sja.VorigerAbschnitt_ID AND sla1.Schueler_ID = sla2.Schueler_ID AND sla2.Schuljahresabschnitts_ID = "
							+ schuljahresabschnittAktuell
							+ " WHERE sla2.ID IS NULL)";
					if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql)) {
						logger.logLn("Fehler beim Verschieben des zugehörigen Noteneinträge in die Quartalsnote");
						logger.modifyIndent(-2);
						return false;
					}
					logger.logLn("- Korrigiere bei diesen Lernabschnitte auch die Klassenzuordnung in den neuen Lernabschnitt.");
					sql = "UPDATE SchuelerLernabschnittsdaten sla JOIN Klassen k ON sla.Klassen_ID = k.ID AND sla.Schuljahresabschnitts_ID <> k.Schuljahresabschnitts_ID"
							+ " JOIN Klassen k2 ON sla.Schuljahresabschnitts_ID = k2.Schuljahresabschnitts_ID AND k.Klasse = k2.Klasse"
							+ " SET sla.Klassen_ID = k2.ID";
					if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql)) {
						logger.logLn("Fehler beim Korrigieren der zugehörigen Klasseneinträge bei den Lernabschnitten");
						logger.modifyIndent(-2);
						return false;
					}
				}
			}
			// Ggf. Verschieben des letzten Schuljahresabschnitts, sofern es nicht der aktuelle oder dessen Folgeabschnitt ist
			if ((schuljahresabschnittMax != schuljahresabschnittAktuell)
					&& ((aktFolgeAbschnittID != null) && (schuljahresabschnittMax != aktFolgeAbschnittID))) {
				// Lade die Daten des letzten Schuljahresabschnitts
				final List<Integer> tmpMaxQuartal = conn.queryNative("SELECT Abschnitt FROM Schuljahresabschnitte WHERE ID = " + schuljahresabschnittMax);
				final int maxQuartal = tmpMaxQuartal.get(0);
				if ((maxQuartal == 1) || (maxQuartal == 3)) {
					logger.logLn(
							"- Der letzte Schuljahresabschnitt ist ein erstes oder drittes Quartal, so dass dieses verschoben werden muss, da noch kein Folgeabschnitt existiert");
					// Erhöhe das Quartal in dem letzten Schuljahresabschnitt, so dass dies das zweite Quartal im Halbjahr wird
					logger.logLn("- Erhöhe das Quartal in dem letzten Schuljahresabschnitt, so dass dies das zweite Quartal im Halbjahr wird...");
					if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(
							"UPDATE Schuljahresabschnitte SET Schuljahresabschnitte.Abschnitt = Schuljahresabschnitte.Abschnitt + 1 WHERE ID = "
									+ schuljahresabschnittMax)) {
						logger.logLn("Fehler beim Erhöhen des Quartals beim letzten Schuljahresabschnitt");
						logger.modifyIndent(-2);
						return false;
					}
					// Verschiebe die Noten-Einträge bei den Schülerleistungsdaten in das Feld für die Quartalsnoten des Halbjahres
					logger.logLn("- Verschiebe die Noten-Einträge bei den Schülerleistungsdaten in das Feld für die Quartalsnoten des Halbjahres...");
					if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(
							"UPDATE SchuelerLeistungsdaten JOIN SchuelerLernabschnittsdaten ON SchuelerLeistungsdaten.Abschnitt_ID = SchuelerLernabschnittsdaten.ID AND SchuelerLernabschnittsdaten.Schuljahresabschnitts_ID = "
									+ schuljahresabschnittMax
									+ " SET SchuelerLeistungsdaten.NotenKrzQuartal = SchuelerLeistungsdaten.NotenKrz, SchuelerLeistungsdaten.NotenKrz = NULL")) {
						logger.logLn("Fehler beim Anpassen der Schüler-Leistungsdaten des letzten Schuljahresabschnittes (Noten -> Quartalsnoten)");
						logger.modifyIndent(-2);
						return false;
					}
				}
			}

			// Anpassen des Klassenlehrern bei zusammenzufassenden Schuljahresabschnitten, welche keine Klassenlehrer zugeordnet haben, aber aus deren Vorgängerabschnitt zugeordnet bekommen können.
			logger.logLn("- Anpassen des Klassenlehrern bei zusammenzufassenden Schuljahresabschnitten...");
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("INSERT INTO KlassenLehrer "
					+ "SELECT k2.ID AS Klassen_ID, kl.Lehrer_ID AS Lehrer_ID, kl.Reihenfolge AS Reihenfolge "
					+ "FROM KlassenLehrer kl "
					+ "JOIN Klassen k1 ON kl.Klassen_ID = k1.ID "
					+ "JOIN Schuljahresabschnitte sj ON k1.Schuljahresabschnitts_ID = sj.ID AND sj.Abschnitt IN (1,3) "
					+ "AND 0 = (SELECT count(*) FROM KlassenLehrer kli JOIN Klassen ki ON kli.Klassen_ID = ki.ID AND ki.Schuljahresabschnitts_ID = sj.FolgeAbschnitt_ID) "
					+ "JOIN Klassen k2 ON k2.Schuljahresabschnitts_ID = sj.FolgeAbschnitt_ID AND k1.Klasse = k2.Klasse")) {
				logger.logLn("Fehler beim Anpassen der Klassenlehrer bei zusammenzufassenden Schuljahresabschnitten");
				logger.modifyIndent(-2);
				return false;
			}

			// Anpassen des Schuljahresabschnittes bei allen Einträgen der Schüler-Tabelle (z.B. bei Abgängern)
			logger.logLn("- Anpassen des Schuljahresabschnittes bei allen Einträgen der Schüler-Tabelle (z.B. bei Abgängern)...");
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(
					"UPDATE Schueler JOIN Schuljahresabschnitte ON Schueler.Schuljahresabschnitts_ID = Schuljahresabschnitte.ID AND Schuljahresabschnitte.Abschnitt IN (1,3) SET Schueler.Schuljahresabschnitts_ID = Schuljahresabschnitte.FolgeAbschnitt_ID")) {
				logger.logLn("Fehler beim Anpassen der Schuljahresabschnitte in der Tabelle Schueler");
				logger.modifyIndent(-2);
				return false;
			}
			// Ggf. Korrektur bei Einträgen in der SchuelerAbitur-Tabelle (hier sollte eigentlich immer ein zweites Quartal eingetragen sein)
			logger.logLn("- Ggf. Korrektur bei Einträgen in der SchuelerAbitur-Tabelle (hier sollte eigentlich immer ein zweites Quartal eingetragen sein)...");
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(
					"UPDATE SchuelerAbitur JOIN Schuljahresabschnitte ON SchuelerAbitur.Schuljahresabschnitts_ID = Schuljahresabschnitte.ID AND Schuljahresabschnitte.Abschnitt IN (1,3) SET SchuelerAbitur.Schuljahresabschnitts_ID = Schuljahresabschnitte.FolgeAbschnitt_ID")) {
				logger.logLn("Fehler beim Anpassen der Schuljahresabschnitte in der Tabelle SchuelerAbitur");
				logger.modifyIndent(-2);
				return false;
			}
			// Ggf. Korrektur bei Einträgen in der SchuelerZP10-Tabelle (hier sollte eigentlich immer ein zweites Quartal eingetragen sein)
			logger.logLn("- Ggf. Korrektur bei Einträgen in der SchuelerZP10-Tabelle (hier sollte eigentlich immer ein zweites Quartal eingetragen sein)...");
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(
					"UPDATE SchuelerZP10 JOIN Schuljahresabschnitte ON SchuelerZP10.Schuljahresabschnitts_ID = Schuljahresabschnitte.ID AND Schuljahresabschnitte.Abschnitt IN (1,3) SET SchuelerZP10.Schuljahresabschnitts_ID = Schuljahresabschnitte.FolgeAbschnitt_ID")) {
				logger.logLn("Fehler beim Anpassen der Schuljahresabschnitte in der Tabelle SchuelerZP10");
				logger.modifyIndent(-2);
				return false;
			}
			// Ggf. Korrektur bei Einträgen in der SchuelerBKAbschluss-Tabelle (sollte nicht relevant sein)
			logger.logLn("- Ggf. Korrektur bei Einträgen in der SchuelerBKAbschluss-Tabelle (sollte nicht relevant sein)...");
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(
					"UPDATE SchuelerBKAbschluss JOIN Schuljahresabschnitte ON SchuelerBKAbschluss.Schuljahresabschnitts_ID = Schuljahresabschnitte.ID AND Schuljahresabschnitte.Abschnitt IN (1,3) SET SchuelerBKAbschluss.Schuljahresabschnitts_ID = Schuljahresabschnitte.FolgeAbschnitt_ID")) {
				logger.logLn("Fehler beim Anpassen der Schuljahresabschnitte in der Tabelle SchuelerBKAbschluss");
				logger.modifyIndent(-2);
				return false;
			}
			// Ggf. Korrektur bei Einträgen in der SchuelerBKFaecher-Tabelle (sollte nicht relevant sein)
			logger.logLn("- Ggf. Korrektur bei Einträgen in der SchuelerBKFaecher-Tabelle (sollte nicht relevant sein)...");
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(
					"UPDATE SchuelerBKFaecher JOIN Schuljahresabschnitte ON SchuelerBKFaecher.Schuljahresabschnitts_ID = Schuljahresabschnitte.ID AND Schuljahresabschnitte.Abschnitt IN (1,3) SET SchuelerBKFaecher.Schuljahresabschnitts_ID = Schuljahresabschnitte.FolgeAbschnitt_ID")) {
				logger.logLn("Fehler beim Anpassen der Schuljahresabschnitte in der Tabelle SchuelerBKFaecher");
				logger.modifyIndent(-2);
				return false;
			}
			// Durchwandere die Schüler-Lernabschnitte zu alle Schuljahresabschnitten im 1. und im 3. Quartal
			logger.logLn("- Durchwandere die Schüler-Lernabschnitte zu allen Schuljahresabschnitten im 1. und im 3. Quartal...");
			logger.modifyIndent(2);
			final List<Object[]> tmpSchuljahresAbschnitte =
					conn.queryNative("SELECT ID, Jahr, Abschnitt, VorigerAbschnitt_ID, FolgeAbschnitt_ID FROM Schuljahresabschnitte WHERE Abschnitt IN (1,3)");
			for (final Object[] schuljahreAbschnitt : tmpSchuljahresAbschnitte) {
				final long abschnittID = (Long) schuljahreAbschnitt[0];
				final int schuljahr = (Integer) schuljahreAbschnitt[1];
				final int quartal = (Integer) schuljahreAbschnitt[2];
				final int halbjahr = ((quartal % 2) == 1) ? ((quartal / 2) + 1) : (quartal / 2);
				final Long folgeAbschnittID = (Long) schuljahreAbschnitt[4];
				logger.logLn("- Schuljahres-Abschnitt " + abschnittID + " (Schuljahr " + schuljahr + "/" + ((schuljahr + 1) - 2000) + ", " + halbjahr
						+ ". Halbjahr, Quartal " + quartal + "):");
				logger.modifyIndent(2);
				// Bestimme die IDs aller Schüler-Lernabschnitte, die zu diesem Schuljahresabschnitt gehören
				final List<Long> lernabschnittIDs =
						conn.queryNative("SELECT ID FROM SchuelerLernabschnittsdaten WHERE WechselNr = 0 AND Schuljahresabschnitts_ID = " + abschnittID);
				logger.logLn(
						"- Übertragen der Leistungsdaten und der Teilleistungen der zugehörigen Schüler-Lernabschnitte in den jeweiligen Folgeabschnitt (ID des Folge-Schuljahresabschnitts: "
								+ folgeAbschnittID + ")...");
				for (final long lernabschnittID : lernabschnittIDs) {
					String sql = "SELECT q2.ID FROM SchuelerLernabschnittsdaten q1 JOIN SchuelerLernabschnittsdaten q2 ON q1.ID = "
							+ lernabschnittID + " AND q1.Schueler_ID = q2.Schueler_ID AND q2.Schuljahresabschnitts_ID = "
							+ folgeAbschnittID + " AND q2.WechselNr = 0";
					final List<Long> tmpFolgeLernabschnitte = conn.queryNative(sql);
					if (tmpFolgeLernabschnitte.size() == 1) {
						final long folgeLernabschnittID = tmpFolgeLernabschnitte.get(0);
						// Kopiere die Quartalsnoten und die Teilleistungen, wenn sich die Leistungsdaten zuordnen lassen
						sql = "UPDATE SchuelerLeistungsdaten q1 JOIN SchuelerLeistungsdaten q2 "
								+ "ON q1.Abschnitt_ID = " + lernabschnittID + " AND q2.Abschnitt_ID = " + folgeLernabschnittID + " AND q1.Fach_ID = q2.Fach_ID "
								+ "AND (q1.Kursart = q2.Kursart OR (q1.KursartAllg IN ('GK', 'LK') AND q1.KursartAllg = q2.KursartAllg)) "
								+ "SET q2.NotenKrzQuartal = q1.NotenKrz";
						if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql)) {
							logger.logLn("Fehler beim Kopieren der Quartalsnoten");
							return false;
						}
						// Verschiebe die Teilleistungen zu den Leistungsdaten im 2. Quartal, wenn sich die Leistungsdaten zuordnen lassen
						sql = "UPDATE SchuelerEinzelleistungen e JOIN SchuelerLeistungsdaten q1 ON e.Leistung_ID = q1.ID AND q1.Abschnitt_ID = "
								+ lernabschnittID + " JOIN SchuelerLeistungsdaten q2 ON q2.Abschnitt_ID = "
								+ folgeLernabschnittID + " AND q1.Fach_ID = q2.Fach_ID"
								+ " AND (q1.Kursart = q2.Kursart OR (q1.KursartAllg IN ('GK', 'LK') AND q1.KursartAllg = q2.KursartAllg))"
								+ " SET e.Leistung_ID = q2.ID";
						if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql)) {
							logger.logLn("Fehler beim Kopieren der Teilleistungen");
							return false;
						}
						// Entferne die Leistungsdaten, wo die Noten zuvor kopiert wurden bzw. die Teilleistungen verschoben wurden (Teil 1).
						sql = "DELETE FROM SchuelerLeistungsdaten WHERE Abschnitt_ID = "
								+ lernabschnittID + " AND (Fach_ID, Kursart) IN (SELECT Fach_ID, Kursart FROM SchuelerLeistungsdaten WHERE Abschnitt_ID = "
								+ folgeLernabschnittID + ")";
						if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql)) {
							logger.logLn("Fehler beim Entfernen von zuvor kopierten verschobenen Leistungsdaten");
							return false;
						}
						// Entferne die Leistungsdaten, wo die Noten zuvor kopiert wurden bzw. die Teilleistungen verschoben wurden (Teil 2).
						sql = "DELETE FROM SchuelerLeistungsdaten WHERE Abschnitt_ID = "
								+ lernabschnittID
								+ " AND (Fach_ID, KursartAllg) IN (SELECT Fach_ID, KursartAllg FROM SchuelerLeistungsdaten WHERE Abschnitt_ID = "
								+ folgeLernabschnittID + " AND KursartAllg IN ('GK', 'LK'))";
						if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql)) {
							logger.logLn("Fehler beim Entfernen von zuvor kopierten verschobenen Leistungsdaten");
							return false;
						}
						// Verschiebe die Leistungsdaten in den anderen Lernabschnitt, wenn sich die Leistungsdaten nicht zuordnen lassen
						sql = "UPDATE SchuelerLeistungsdaten SET Abschnitt_ID = " + folgeLernabschnittID + " WHERE Abschnitt_ID = " + lernabschnittID;
						if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql)) {
							logger.logLn("Fehler beim Verschieben von Leistungsdaten in das jeweilige zweite Quartal des Halbjahres.");
							return false;
						}
					} else if (tmpFolgeLernabschnitte.isEmpty()) {
						// Verschiebe den Lernabschnitt in das zweite Quartal des Halbjahres
						sql = "UPDATE SchuelerLernabschnittsdaten sla SET sla.Schuljahresabschnitts_ID = " + folgeAbschnittID + " WHERE sla.ID = " + lernabschnittID;
						if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql)) {
							logger.logLn("Fehler beim Anpassen des Schuljahresabschnittes beim Lernabschnitt.");
							return false;
						}

						// Kopiere die Quartalsnoten und die Teilleistungen, wenn sich die Leistungsdaten zuordnen lassen
						sql = "UPDATE SchuelerLeistungsdaten sld SET sld.NotenKrzQuartal = sld.NotenKrz WHERE sld.Abschnitt_ID = " + lernabschnittID;
						if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql)) {
							logger.logLn("Fehler beim Kopieren der Quartalsnoten");
							return false;
						}

						// Setzte die Halbjahresnoten zurück.
						sql = "UPDATE SchuelerLeistungsdaten sld SET sld.NotenKrz = NULL WHERE sld.Abschnitt_ID = " + lernabschnittID;
						if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql)) {
							logger.logLn("Fehler beim Zurücksetzen der Halbjahresnoten");
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
				if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql)) {
					logger.logLn("Fehler beim Verschieben von Kursen in das jeweilige zweite Quartal des Halbjahres.");
					return false;
				}
				// Entferne die Schülerlernabschnitte, die zu dem Quartal gehört haben.
				logger.logLn("- Entferne die Schülerlernabschnitte, dessen Leistungsdaten gerade in das 2. Quartal übertragen wurden...");
				sql = "DELETE FROM SchuelerLernabschnittsdaten WHERE Schuljahresabschnitts_ID = " + abschnittID;
				if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql)) {
					logger.logLn("Fehler beim Löschen der Schüler-Lernabschnitte des ersten Quartals.");
					return false;
				}
				// Entferne die Kurszuordnung bei Schülerleistungsdaten, wo der Schüler ansonsten einem Kurs doppelt zugeordnet wäre (Fehlerkorrektur von unstimmigen Leistungsdaten)
				logger.logLn(
						"- Entferne die Kurszuordnung bei Schülerleistungsdaten, wo der Schüler ansonsten einem Kurs doppelt zugeordnet wäre (Fehlerkorrektur von unstimmigen Leistungsdaten)...");
				sql = "SELECT s.ID FROM SchuelerLeistungsdaten s "
						+ "JOIN Kurse k1 ON s.Kurs_ID = k1.ID AND k1.Schuljahresabschnitts_ID = " + abschnittID
						+ " JOIN Kurse k2 ON k2.Schuljahresabschnitts_ID = " + folgeAbschnittID
						+ " AND k1.KurzBez = k2.KurzBez AND k1.Jahrgang_ID = k2.Jahrgang_ID AND k1.ASDJahrgang = k2.ASDJahrgang "
						+ "AND k1.Fach_ID = k2.Fach_ID AND k1.KursartAllg = k2.KursartAllg "
						+ "JOIN SchuelerLernabschnittsdaten sla ON s.Abschnitt_ID = sla.ID "
						+ "AND (sla.Schueler_ID, k1.ID, sla.WechselNr) IN (SELECT Schueler_ID, Kurs_ID, LernabschnittWechselNr FROM Kurs_Schueler) "
						+ "AND (sla.Schueler_ID, k2.ID, sla.WechselNr) IN (SELECT Schueler_ID, Kurs_ID, LernabschnittWechselNr FROM Kurs_Schueler) ";
				final List<Long> tmpLeistungsdatenIDs = conn.queryNative(sql);
				if (!tmpLeistungsdatenIDs.isEmpty()) {
					sql = "UPDATE SchuelerLeistungsdaten SET Kurs_ID = NULL WHERE ID IN ";
					sql += tmpLeistungsdatenIDs.stream().map(String::valueOf).collect(Collectors.joining(",", "(", ")"));
					if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql)) {
						logger.logLn("Fehler beim Entfernen der Kurszuordnung in den Leistungsdaten.");
						return false;
					}
				}
				// Korrigiere die Kurszuordnung bei den Leistungsdaten, welche noch Kurse in diesem Schuljahresabschnitt haben
				logger.logLn(
						"- Korrigiere die Kurszuordnung bei den übertragenen Leistungsdaten, welche noch Kurszuordnungen zu Kursen in dem übertragenen Schuljahresabschnitt haben...");
				sql = "UPDATE SchuelerLeistungsdaten s JOIN Kurse k1 ON s.Kurs_ID = k1.ID AND k1.Schuljahresabschnitts_ID = "
						+ abschnittID + " JOIN Kurse k2 ON k2.Schuljahresabschnitts_ID = "
						+ folgeAbschnittID + " AND k1.KurzBez = k2.KurzBez AND k1.Jahrgang_ID = k2.Jahrgang_ID AND k1.ASDJahrgang = k2.ASDJahrgang "
						+ "AND k1.Fach_ID = k2.Fach_ID AND k1.KursartAllg = k2.KursartAllg SET s.Kurs_ID = k2.ID";
				if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql)) {
					logger.logLn("Fehler bei der Korrektur der Kurszuordnungen.");
					return false;
				}
				logger.modifyIndent(-2);
			}
			logger.modifyIndent(-2);
			// Nachdem die Leistungen in die Halbjahresabschnitte verschoben wurden, können die Schuljahresabschnitt mit allen daran "hängenden" Daten entfernt werden.
			logger.logLn(
					"- Entferne die Schuljahresabschnitt der ersten Quartal der Halbjahre mit allen daran \"hängenden\" Daten - diese werden jetzt nicht mehr benötigt...");
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("DELETE FROM Schuljahresabschnitte WHERE Schuljahresabschnitte.Abschnitt IN (1,3)")) {
				logger.logLn("Fehler beim Entfernen der Schuljahresabschnitte für die Quartale 1 und 3");
				logger.modifyIndent(-2);
				return false;
			}
			// Jetzt kann auch der Abschnittseintrag in der Tabelle Schuljahresabschnitte von Quartal auf Halbjahr abgeändert werden
			logger.logLn("- Ändern des Abschnittseintrags in der Tabelle Schuljahresabschnitte von Quartal auf Halbjahr...");
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(
					"UPDATE Schuljahresabschnitte SET Schuljahresabschnitte.Abschnitt = Schuljahresabschnitte.Abschnitt / 2")) {
				logger.logLn("Fehler beim Umstellen der Abschnitte bei den Schuljahresabschnitten von Quartal auf Halbjahrnummerierung");
				logger.modifyIndent(-2);
				return false;
			}
			// Außerdem müssen die Restabschnitte in der Jahrgangstabelle angepasst werden
			logger.logLn("- Passe die Anzahl der Restabschnitte und bei der Tabelle EigeneSchule_Jahrgaenge an...");
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(
					"UPDATE EigeneSchule_Jahrgaenge SET Restabschnitte = Restabschnitte / 2 WHERE Restabschnitte IS NOT NULL")) {
				logger.logLn("Fehler beim Anpassen der Restabschnitte");
				logger.modifyIndent(-2);
				return false;
			}
			// Und zum Abschluss werden die Anzahl der Abschnitte und deren Bezeichnungen bei der Tabelle EigeneSchule umgestellt
			logger.logLn("- Stelle die Anzahl der Abschnitte und deren Bezeichnungen bei der Tabelle EigeneSchule um...");
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(
					"UPDATE EigeneSchule SET EigeneSchule.AnzahlAbschnitte = 2, EigeneSchule.AbschnittBez = 'Halbjahr', EigeneSchule.BezAbschnitt1 = '1. Hj.', EigeneSchule.BezAbschnitt2 = '2. Hj.', EigeneSchule.BezAbschnitt3 = NULL, EigeneSchule.BezAbschnitt4 = NULL")) {
				logger.logLn("Fehler beim Umstellen der Tabelle EigeneSchule auf zwei Abschnitte.");
				logger.modifyIndent(-2);
				return false;
			}

			// In der Sprachenfolge der Schüler werden auch Abschnitte gespeichert. Passe diese ebenfalls an.
			logger.logLn("- Passe die Eintragungen der Abschnitte in der Sprachenfolge der Schülerinnen und Schüler um...");
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(
					"UPDATE SchuelerSprachenfolge SET SchuelerSprachenfolge.AbschnittVon = 1 WHERE SchuelerSprachenfolge.AbschnittVon IN (1,2)")) {
				logger.logLn("Fehler beim Umstellen der Tabelle SchuelerSprachenfolge auf zwei Abschnitte im Bereich AbschnittVon.");
				logger.modifyIndent(-2);
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(
					"UPDATE SchuelerSprachenfolge SET SchuelerSprachenfolge.AbschnittVon = 2 WHERE SchuelerSprachenfolge.AbschnittVon IN (3,4)")) {
				logger.logLn("Fehler beim Umstellen der Tabelle SchuelerSprachenfolge auf zwei Abschnitte im Bereich AbschnittVon.");
				logger.modifyIndent(-2);
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(
					"UPDATE SchuelerSprachenfolge SET SchuelerSprachenfolge.AbschnittBis = 1 WHERE SchuelerSprachenfolge.AbschnittBis IN (1,2)")) {
				logger.logLn("Fehler beim Umstellen der Tabelle SchuelerSprachenfolge auf zwei Abschnitte im Bereich AbschnittBis.");
				logger.modifyIndent(-2);
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(
					"UPDATE SchuelerSprachenfolge SET SchuelerSprachenfolge.AbschnittBis = 2 WHERE SchuelerSprachenfolge.AbschnittBis IN (3,4)")) {
				logger.logLn("Fehler beim Umstellen der Tabelle SchuelerSprachenfolge auf zwei Abschnitte im Bereich AbschnittBis.");
				logger.modifyIndent(-2);
				return false;
			}
			logger.modifyIndent(-2);
			// Lege temporär Indizes an, um die Umstellung zu beschleunigen
			logger.logLn("* Entferne die temporären Indizes wieder");
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("DROP INDEX quartals_modus_1 ON SchuelerLeistungsdaten")) {
				logger.logLn(2, "Fehler beim Entfernen des Index quartals_modus_1");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("DROP INDEX quartals_modus_2 ON SchuelerLernabschnittsdaten")) {
				logger.logLn(2, "Fehler beim Entfernen des Index quartals_modus_2");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("DROP INDEX quartals_modus_3 ON SchuelerLernabschnittsdaten")) {
				logger.logLn(2, "Fehler beim Entfernen des Index quartals_modus_3");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("DROP INDEX quartals_modus_4 ON Schuljahresabschnitte")) {
				logger.logLn(2, "Fehler beim Entfernen des Index quartals_modus_4");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("DROP INDEX quartals_modus_5 ON Schuljahresabschnitte")) {
				logger.logLn(2, "Fehler beim Entfernen des Index quartals_modus_5");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("DROP INDEX quartals_modus_6 ON SchuelerLernabschnittsdaten")) {
				logger.logLn(2, "Fehler beim Entfernen des Index quartals_modus_6");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("DROP INDEX quartals_modus_7 ON Klassen")) {
				logger.logLn(2, "Fehler beim Entfernen des Index quartals_modus_7");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("DROP INDEX quartals_modus_8 ON Klassen")) {
				logger.logLn(2, "Fehler beim Entfernen des Index quartals_modus_8");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("DROP INDEX quartals_modus_9 ON SchuelerLernabschnittsdaten")) {
				logger.logLn(2, "Fehler beim Entfernen des Index quartals_modus_9");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("DROP INDEX quartals_modus_10 ON SchuelerLeistungsdaten")) {
				logger.logLn(2, "Fehler beim Entfernen des Index quartals_modus_10");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("DROP INDEX quartals_modus_11 ON SchuelerLeistungsdaten")) {
				logger.logLn(2, "Fehler beim Entfernen des Index quartals_modus_11");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("DROP INDEX quartals_modus_12 ON SchuelerLeistungsdaten")) {
				logger.logLn(2, "Fehler beim Entfernen des Index quartals_modus_12");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("DROP INDEX quartals_modus_13 ON Kurse")) {
				logger.logLn(2, "Fehler beim Entfernen des Index quartals_modus_13");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("DROP INDEX quartals_modus_14 ON Kurse")) {
				logger.logLn(2, "Fehler beim Entfernen des Index quartals_modus_14");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("DROP INDEX quartals_modus_15 ON Kurse")) {
				logger.logLn(2, "Fehler beim Entfernen des Index quartals_modus_15");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("DROP INDEX quartals_modus_16 ON Kurse")) {
				logger.logLn(2, "Fehler beim Entfernen des Index quartals_modus_16");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("DROP INDEX quartals_modus_17 ON Kurse")) {
				logger.logLn(2, "Fehler beim Entfernen des Index quartals_modus_17");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("DROP INDEX quartals_modus_18 ON Kurse")) {
				logger.logLn(2, "Fehler beim Entfernen des Index quartals_modus_18");
				return false;
			}
			if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("DROP INDEX quartals_modus_19 ON SchuelerLeistungsdaten")) {
				logger.logLn(2, "Fehler beim Entfernen des Index quartals_modus_19");
				return false;
			}
		}
		return true;
	}


	private static boolean bereiteSprachenfolgeFuerUniqueConstraintVor(final DBEntityManager conn, final Logger logger) {
		logger.logLn("Fasse ggf. Einträge bei der Sprachenfolge zusammen, damit eine Unique-Constraint auf Schüler-ID und Sprache eingerichtet werden kann:");
		logger.modifyIndent(2);

		boolean resultVorbereitung = true;

		// Liste erstellen mit Schüler-IDs, in deren Sprachenfolge eine Sprache zweimal vorkommt.
		final List<Object[]> listAlleSprachenfolgeDuplikate =
				conn.queryNative("SELECT Schueler_ID, Sprache FROM SchuelerSprachenfolge GROUP BY Schueler_ID, Sprache HAVING COUNT(*) > 1");

		// Für besser lesbaren Zugriff auf Arrays die entsprechenden Indexwerte der Einträge benennen.
		final int iID = 0;
		final int iASDVon = 1;
		final int iAbsVon = 2;
		final int iASDBis = 3;
		final int iAbsBis = 4;
		final int iReihenfolge = 5;
		final int iNiveau = 6;
		final int iKlLat = 7;
		final int iLat = 8;
		final int iGrae = 9;
		final int iHeb = 10;

		for (final Object[] schuelerSprachenfolgeDuplikat : listAlleSprachenfolgeDuplikate) {
			final long schuelerID = (Long) schuelerSprachenfolgeDuplikat[0];
			final String sprache = (String) schuelerSprachenfolgeDuplikat[1];

			logger.logLn("... Duplikate für Schüler mit der ID %d in der Sprache %s gefunden.".formatted(schuelerID, sprache));
			logger.modifyIndent(2);

			// Einlesen der doppelten Einträge in der Sprachenfolge in zwei DISJUNKTE Listen, einmal mit Sprachbeginn und einmal ohne Beginn.
			List<Object[]> listDuplikateMITBeginn = conn.queryNative(
					"SELECT ID, ASDJahrgangVon, AbschnittVon, ASDJahrgangBis, AbschnittBis, ReihenfolgeNr, Referenzniveau, KleinesLatinumErreicht, LatinumErreicht, GraecumErreicht, HebraicumErreicht FROM SchuelerSprachenfolge WHERE Schueler_ID = "
							+ schuelerID + " AND Sprache = '" + sprache
							+ "' AND ASDJahrgangVon IS NOT NULL AND ASDJahrgangVon != '' ORDER BY ASDJahrgangVon, AbschnittVon, ASDJahrgangBis, AbschnittBis");
			final List<Object[]> listDuplikateOHNEBeginn = conn.queryNative(
					"SELECT ID, ASDJahrgangVon, AbschnittVon, ASDJahrgangBis, AbschnittBis, ReihenfolgeNr, Referenzniveau, KleinesLatinumErreicht, LatinumErreicht, GraecumErreicht, HebraicumErreicht FROM SchuelerSprachenfolge WHERE Schueler_ID = "
							+ schuelerID + " AND Sprache = '" + sprache
							+ "' AND (ASDJahrgangVon IS NULL OR ASDJahrgangVon = '') ORDER BY ASDJahrgangVon, AbschnittVon, ASDJahrgangBis, AbschnittBis");

			// Werte für die späteren Vergleiche normalisieren.
			werteNormalisieren(listDuplikateMITBeginn, false);
			werteNormalisieren(listDuplikateOHNEBeginn, true);

			// Sortiere die doppelten Einträge aus der Sprachenfolge nach ihrem Beginn, sofern vorhanden.
			listDuplikateMITBeginn = listDuplikateMITBeginn.stream().filter(s -> (s[iASDVon] != null)).sorted((a, b) -> {
				final Jahrgaenge aVon = Jahrgaenge.data().getWertBySchluessel((String) a[iASDVon]);
				final Jahrgaenge bVon = Jahrgaenge.data().getWertBySchluessel((String) b[iASDVon]);
				return aVon.compareTo(bVon);
			}).toList();

			// Initialisiere die Variablen für den späteren, finalen DB-Eintrag der Sprache mit Standardwerten
			Long finalID = null;
			String finalASDVon = null;
			Integer finalAbsVon = null;
			String finalASDBis = null;
			Integer finalAbsBis = null;
			Integer finalReihenfolge = null;
			String finalNiveau = null;
			Integer finalKlLat = 0;
			Integer finalLat = 0;
			Integer finalGrae = 0;
			Integer finalHeb = 0;

			/*
				Anmerkungen zum folgenden Vorgehen beim Zusammenfassen:
				#######################################################
				Die Duplikate sollen zu einem Eintrag verschmolzen werden. Dabei werden überlappende Zeitbereiche zusammengefasst.
				Sollten sich zwei disjunkte Bereiche ergeben, so wird der jüngste Bereich übernommen.
				Vorrang haben immer die Einträge mit Sprachbeginn, da darüber später die Sprachbelegung definiert wird.
				Ist ein Sprachanfang aber kein Sprachende angegeben, so wird das leere Sprachende übernommen, da dies bisher in Schild eine nicht abgeschlossene Sprache darstellte.
				Sind weder Sprachanfang noch Sprachende vorhanden, so werden nur übrigen Felder ausgewertet.
				Bei der Reihenfolge wird der kleinste Eintrag übernommen, beim Referenzniveau der größte.
				Die Nachweise wie das Latinum werden gesetzt, wenn sie zumindest einmal vorhanden.
			*/

			// 1. Schritt: Es werden die Einträge zusammengefasst, die einen Sprachbeginn haben, sofern es damit mehrere Duplikate einer Sprache gibt.
			if (!listDuplikateMITBeginn.isEmpty()) {
				// Es gibt mindestens ein Duplikat mit Sprachbeginn. Wähle davon den ersten Eintrag, denn der hat den kleinsten Jahrgang im Beginn, wähle dies zur Initialisierung.
				// Wenn es mehr als ein Duplikat zum Sprachbeginn gibt, fasse die Einträge entsprechend dem Kommentar oben zusammen.
				// Dabei ist zu beachten, dass die Einträge nach Jahrgang des Sprachbeginns bereits aufsteigend sortiert sind.
				for (final Object[] eintragSprachenfolge : listDuplikateMITBeginn) {
					if (finalID == null) {
						finalID = (Long) eintragSprachenfolge[iID];
						finalASDVon = (String) eintragSprachenfolge[iASDVon];
						finalAbsVon = (Integer) eintragSprachenfolge[iAbsVon];
						finalASDBis = (String) eintragSprachenfolge[iASDBis];
						finalAbsBis = (Integer) eintragSprachenfolge[iAbsBis];
						finalReihenfolge = (Integer) eintragSprachenfolge[iReihenfolge];
						finalNiveau = (String) eintragSprachenfolge[iNiveau];
						finalKlLat = (Integer) eintragSprachenfolge[iKlLat];
						finalLat = (Integer) eintragSprachenfolge[iLat];
						finalGrae = (Integer) eintragSprachenfolge[iGrae];
						finalHeb = (Integer) eintragSprachenfolge[iHeb];
						continue;
					}

					final Jahrgaenge jgFinalBeginn = Jahrgaenge.data().getWertBySchluessel(finalASDVon);
					final Jahrgaenge jgFinalEnde = Jahrgaenge.data().getWertBySchluessel(finalASDBis);
					final Jahrgaenge jgAktuellBeginn = Jahrgaenge.data().getWertBySchluessel((String) eintragSprachenfolge[iASDVon]);
					final Jahrgaenge jgAktuellEnde = Jahrgaenge.data().getWertBySchluessel((String) eintragSprachenfolge[iASDBis]);

					// Der gesetzte finale Eintrag hat einen definierten Sprachbeginn und ein definiertes Ende.
					// Wenn der aktuell betrachtet Jahrgang nicht innerhalb des finalen beginnt oder lückenlos daran anschließt, dann behalte nur den jüngsten Eintrag.
					// Ansonsten werden beide Einträge verschmolzen (ELSE-Teil)
					if ((jgFinalEnde != null) && (jgAktuellBeginn.compareTo(jgFinalEnde) > 0)
							&& (!jgAktuellBeginn.isMoeglicherNachfolgerVon(jgFinalEnde)
									|| (jgAktuellBeginn.isMoeglicherNachfolgerVon(jgFinalEnde)
											//	(finalAbsBis == 1) || (eintragSprachenfolge[iAbsVon] == 2), dann grenzen die Einträge nicht aneinander.
											&& (((finalAbsBis != null) && (finalAbsBis != 2))
													|| ((eintragSprachenfolge[iAbsVon] != null) && ((Integer) eintragSprachenfolge[iAbsVon] != 1)))))) {
						finalASDVon = (String) eintragSprachenfolge[iASDVon];
						finalAbsVon = (Integer) eintragSprachenfolge[iAbsVon];
						finalASDBis = (String) eintragSprachenfolge[iASDBis];
						finalAbsBis = (Integer) eintragSprachenfolge[iAbsBis];
						finalReihenfolge = (Integer) eintragSprachenfolge[iReihenfolge];
						if ((finalNiveau == null)
								|| (((eintragSprachenfolge[iNiveau]) != null) && ((finalNiveau.compareTo((String) eintragSprachenfolge[iNiveau])) < 0)))
							finalNiveau = (String) eintragSprachenfolge[iNiveau];
						if (finalKlLat < ((Integer) eintragSprachenfolge[iKlLat]))
							finalKlLat = (Integer) eintragSprachenfolge[iKlLat];
						if (finalLat < ((Integer) eintragSprachenfolge[iLat]))
							finalLat = (Integer) eintragSprachenfolge[iLat];
						if (finalGrae < ((Integer) eintragSprachenfolge[iGrae]))
							finalGrae = (Integer) eintragSprachenfolge[iGrae];
						if (finalHeb < ((Integer) eintragSprachenfolge[iHeb]))
							finalHeb = (Integer) eintragSprachenfolge[iHeb];
					} else {
						// Der aktuell betrachtete Eintrag beginnt vor bzw. mit dem bisher als final gesetzen Sprachbelegungsende. Verschmelze daher beide Einträge.
						if ((jgAktuellBeginn.compareTo(jgFinalBeginn) == 0)
								&& (((finalAbsVon == null) && (eintragSprachenfolge[iAbsVon] != null)) || ((finalAbsVon != null)
										&& (eintragSprachenfolge[iAbsVon] != null) && (((Integer) eintragSprachenfolge[iAbsVon]) < finalAbsVon))))
							finalAbsVon = (Integer) eintragSprachenfolge[iAbsVon];
						if ((jgFinalEnde != null) && ((jgAktuellEnde == null) || (jgAktuellEnde.compareTo(jgFinalEnde) > 0))) {
							finalASDBis = (String) eintragSprachenfolge[iASDBis];
							if (finalASDBis == null)
								finalAbsBis = null;
							else
								finalAbsBis = (Integer) eintragSprachenfolge[iAbsBis];
						}
						if (((finalReihenfolge == null) && ((eintragSprachenfolge[iReihenfolge]) != null)) || ((finalReihenfolge != null)
								&& ((eintragSprachenfolge[iReihenfolge]) != null) && (((Integer) eintragSprachenfolge[iReihenfolge]) < finalReihenfolge)))
							finalReihenfolge = (Integer) eintragSprachenfolge[iReihenfolge];
						if ((finalNiveau == null)
								|| (((eintragSprachenfolge[iNiveau]) != null) && ((finalNiveau.compareTo((String) eintragSprachenfolge[iNiveau])) < 0)))
							finalNiveau = (String) eintragSprachenfolge[iNiveau];
						if (finalKlLat < ((Integer) eintragSprachenfolge[iKlLat]))
							finalKlLat = (Integer) eintragSprachenfolge[iKlLat];
						if (finalLat < ((Integer) eintragSprachenfolge[iLat]))
							finalLat = (Integer) eintragSprachenfolge[iLat];
						if (finalGrae < ((Integer) eintragSprachenfolge[iGrae]))
							finalGrae = (Integer) eintragSprachenfolge[iGrae];
						if (finalHeb < ((Integer) eintragSprachenfolge[iHeb]))
							finalHeb = (Integer) eintragSprachenfolge[iHeb];
					}
				}
			}

			// 2. Schritt: Informationen ohne Sprachbeginn mit den bisherigen Einträgen zusammenführen.
			if (!listDuplikateOHNEBeginn.isEmpty()) {
				for (final Object[] eintragSprachenfolge : listDuplikateOHNEBeginn) {
					if (finalID == null) {
						// Wenn bisher kein Eintrag mit Sprachbeginn genutzt wurde, nehme den ersten Eintrag ohne Beginn.
						finalID = (Long) eintragSprachenfolge[iID];
						finalASDVon = null;
						finalAbsVon = null;
						finalASDBis = (String) eintragSprachenfolge[iASDBis];
						finalAbsBis = (Integer) eintragSprachenfolge[iAbsBis];
						finalReihenfolge = (Integer) eintragSprachenfolge[iReihenfolge];
						finalNiveau = (String) eintragSprachenfolge[iNiveau];
						finalKlLat = (Integer) eintragSprachenfolge[iKlLat];
						finalLat = (Integer) eintragSprachenfolge[iLat];
						finalGrae = (Integer) eintragSprachenfolge[iGrae];
						finalHeb = (Integer) eintragSprachenfolge[iHeb];
						continue;
					}
					final Jahrgaenge jgFinalEnde = Jahrgaenge.data().getWertBySchluessel(finalASDBis);
					final Jahrgaenge jgAktuellEnde = Jahrgaenge.data().getWertBySchluessel((String) eintragSprachenfolge[iASDBis]);

					if ((jgFinalEnde != null) && ((jgAktuellEnde == null) || (jgAktuellEnde.compareTo(jgFinalEnde) > 0))) {
						finalASDBis = (String) eintragSprachenfolge[iASDBis];
						if (finalASDBis == null)
							finalAbsBis = null;
						else
							finalAbsBis = (Integer) eintragSprachenfolge[iAbsBis];
					}
					if (((finalReihenfolge == null) && ((eintragSprachenfolge[iReihenfolge]) != null)) || ((finalReihenfolge != null)
							&& ((eintragSprachenfolge[iReihenfolge]) != null) && (((Integer) eintragSprachenfolge[iReihenfolge]) < finalReihenfolge)))
						finalReihenfolge = (Integer) eintragSprachenfolge[iReihenfolge];
					if ((finalNiveau == null)
							|| (((eintragSprachenfolge[iNiveau]) != null) && ((finalNiveau.compareTo((String) eintragSprachenfolge[iNiveau])) < 0)))
						finalNiveau = (String) eintragSprachenfolge[iNiveau];
					if (finalKlLat < ((Integer) eintragSprachenfolge[iKlLat]))
						finalKlLat = (Integer) eintragSprachenfolge[iKlLat];
					if (finalLat < ((Integer) eintragSprachenfolge[iLat]))
						finalLat = (Integer) eintragSprachenfolge[iLat];
					if (finalGrae < ((Integer) eintragSprachenfolge[iGrae]))
						finalGrae = (Integer) eintragSprachenfolge[iGrae];
					if (finalHeb < ((Integer) eintragSprachenfolge[iHeb]))
						finalHeb = (Integer) eintragSprachenfolge[iHeb];
				}
			}

			// Falls kein Eintrag übernommen wurde, ist ein unerwarteter Fehler aufgetreten.
			if (finalID == null)
				resultVorbereitung = false;

			// Nur wenn bisher kein Fehler aufgetreten ist, aktualisiere die Datenbank.
			if (resultVorbereitung) {
				// Alle Einträge entfernen, die nicht der ID des finalen Eintrags entsprechen:
				for (final Object[] objects : listDuplikateMITBeginn) {
					final Long idToDelete = (Long) objects[iID];
					if (!idToDelete.equals(finalID)) {
						final int removed = conn.transactionNativeDeleteAndFlush("DELETE FROM SchuelerSprachenfolge WHERE ID = %d".formatted(idToDelete));
						if (removed == 1)
							logger.logLn("Datensatz mit der ID %d in SchuelerSprachenfolge wurde entfernt.".formatted(idToDelete));
						else {
							logger.logLn("[Fehler] Datensatz mit der ID %d in SchuelerSprachenfolge konnte nicht entfernt werden.".formatted(idToDelete));
							resultVorbereitung = false;
						}
					}
				}
				for (final Object[] objects : listDuplikateOHNEBeginn) {
					final Long idToDelete = (Long) objects[iID];
					if (!idToDelete.equals(finalID)) {
						final int removed = conn.transactionNativeDeleteAndFlush("DELETE FROM SchuelerSprachenfolge WHERE ID = %d".formatted(idToDelete));
						if (removed == 1)
							logger.logLn("Datensatz mit der ID %d in SchuelerSprachenfolge wurde entfernt.".formatted(idToDelete));
						else {
							logger.logLn("[Fehler] Datensatz mit der ID %d in SchuelerSprachenfolge konnte nicht entfernt werden.".formatted(idToDelete));
							resultVorbereitung = false;
						}
					}
				}

				// Den verbleibenden Eintrag mit den gewonnenen Werten aktualisieren.
				// SQL-UPDATE-Command erzeugen. Dafür Werte SQL-konform umwandeln.
				final String updateCommand = """
									UPDATE SchuelerSprachenfolge
									SET Schueler_ID=%d, ASDJahrgangVon=%s, AbschnittVon=%s, ASDJahrgangBis=%s, AbschnittBis=%s, Sprache=%s, ReihenfolgeNr=%s, Referenzniveau=%s, KleinesLatinumErreicht=%s, LatinumErreicht=%s, GraecumErreicht=%s, HebraicumErreicht=%s
									WHERE ID = %d"""
						.formatted(schuelerID, convertToSQL(finalASDVon), convertToSQL(finalAbsVon), convertToSQL(finalASDBis), convertToSQL(finalAbsBis),
								convertToSQL(sprache), convertToSQL(finalReihenfolge), convertToSQL(finalNiveau),
								convertToSQL(finalKlLat), convertToSQL(finalLat), convertToSQL(finalGrae), convertToSQL(finalHeb), finalID);

				final int updated = conn.transactionNativeUpdateAndFlush(updateCommand);
				if (updated == 1)
					logger.logLn("Datensatz mit der ID %d in SchuelerSprachenfolge wurde aktualisiert.".formatted(finalID));
				else {
					logger.logLn("[Fehler] Datensatz mit der ID  %d in SchuelerSprachenfolge konnte nicht aktualisiert werden.".formatted(finalID));
					resultVorbereitung = false;
				}
			}

			logger.modifyIndent(-2);
		}

		logger.modifyIndent(-2);

		return resultVorbereitung;
	}

	private static void werteNormalisieren(final List<Object[]> listDuplikateSprachenfolge, final boolean setBeginnNull) {
		// Für besser lesbaren Zugriff auf Arrays die entsprechenden Indexwerte der Einträge benennen.
		final int iASDVon = 1;
		final int iAbsVon = 2;
		final int iASDBis = 3;
		final int iAbsBis = 4;
		final int iReihenfolge = 5;
		final int iNiveau = 6;
		final int iKlLat = 7;
		final int iLat = 8;
		final int iGrae = 9;
		final int iHeb = 10;

		// Werte normalisieren
		for (final Object[] eintragSprachenfolge : listDuplikateSprachenfolge) {
			if (setBeginnNull)
				eintragSprachenfolge[iASDVon] = null;

			if ((eintragSprachenfolge[iASDVon] != null) && eintragSprachenfolge[iASDVon].toString().isEmpty())
				eintragSprachenfolge[iASDVon] = null;
			if (eintragSprachenfolge[iASDVon] == null)
				eintragSprachenfolge[iAbsVon] = null;
			if (eintragSprachenfolge[iASDVon] != null) {
				if ((eintragSprachenfolge[iAbsVon] != null) && (((Short) eintragSprachenfolge[iAbsVon]) <= 1))
					eintragSprachenfolge[iAbsVon] = 1;
				else if ((eintragSprachenfolge[iAbsVon] != null) && (((Short) eintragSprachenfolge[iAbsVon]) >= 2))
					eintragSprachenfolge[iAbsVon] = 2;
				else
					eintragSprachenfolge[iAbsVon] = null;
			}

			if ((eintragSprachenfolge[iASDBis] != null) && eintragSprachenfolge[iASDBis].toString().isEmpty())
				eintragSprachenfolge[iASDBis] = null;
			if (eintragSprachenfolge[iASDBis] == null)
				eintragSprachenfolge[iAbsBis] = null;
			if (eintragSprachenfolge[iASDBis] != null) {
				if ((eintragSprachenfolge[iAbsBis] != null) && (((Short) eintragSprachenfolge[iAbsBis]) <= 1))
					eintragSprachenfolge[iAbsBis] = 1;
				else if ((eintragSprachenfolge[iAbsBis] != null) && (((Short) eintragSprachenfolge[iAbsBis]) >= 2))
					eintragSprachenfolge[iAbsBis] = 2;
				else
					eintragSprachenfolge[iAbsBis] = null;
			}

			if ((eintragSprachenfolge[iReihenfolge] != null) && (((Integer) eintragSprachenfolge[iReihenfolge]) <= 0))
				eintragSprachenfolge[iReihenfolge] = null;
			if ((eintragSprachenfolge[iNiveau] != null) && eintragSprachenfolge[iNiveau].toString().isEmpty())
				eintragSprachenfolge[iNiveau] = null;
			if ((eintragSprachenfolge[iKlLat] == null) || ((((Integer) eintragSprachenfolge[iKlLat]) != 0) && (((Integer) eintragSprachenfolge[iKlLat]) != 1)))
				eintragSprachenfolge[iKlLat] = 0;
			if ((eintragSprachenfolge[iLat] == null) || ((((Integer) eintragSprachenfolge[iLat]) != 0) && (((Integer) eintragSprachenfolge[iLat]) != 1)))
				eintragSprachenfolge[iLat] = 0;
			if ((eintragSprachenfolge[iGrae] == null) || ((((Integer) eintragSprachenfolge[iGrae]) != 0) && (((Integer) eintragSprachenfolge[iGrae]) != 1)))
				eintragSprachenfolge[iGrae] = 0;
			if ((eintragSprachenfolge[iHeb] == null) || ((((Integer) eintragSprachenfolge[iHeb]) != 0) && (((Integer) eintragSprachenfolge[iHeb]) != 1)))
				eintragSprachenfolge[iHeb] = 0;
		}
	}

	private static String convertToSQL(final String value) {
		return (value == null) ? "NULL" : ("'" + value + "'");
	}

	private static String convertToSQL(final Number value) {
		return (value == null) ? "NULL" : value.toString();
	}


	@Override
	public boolean runLast(final DBEntityManager conn, final Logger logger) {
		if (conn.getDBDriver() != DBDriver.MARIA_DB) {
			logger.logLn("DBMS wird für dieses Datenbank Revisions-Update nicht unterstützt.");
			return false;
		}

		if (!entferneQuartalsmodus(conn, logger))
			return false;

		return bereiteSprachenfolgeFuerUniqueConstraintVor(conn, logger);
	}

}
