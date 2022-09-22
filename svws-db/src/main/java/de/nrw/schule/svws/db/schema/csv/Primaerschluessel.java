package de.nrw.schule.svws.db.schema.csv;

import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.nrw.schule.svws.db.DBDriver;


/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Primaerschluessel.
 */
public class Primaerschluessel {
	
	/** Die Tabelle zu der der Primärschlüssel gehört */
	@JsonIgnore public Tabelle tabelle;
	
	/** Gibt an, ob der Primärschlüssel ein Autoinkrement hat oder nicht */
	@JsonIgnore public boolean hatAutoIncrement;
	
	/** Die Spalten, die zu dem Primärschlüssel gehören */ 
	@JsonIgnore public Vector<TabelleSpalte> spalten = new Vector<>();
	
	
	/**
	 * Erstellt einen SQL-String für das Erstellen einen Primärschlüssel als SQL-CONSTRAINT 
	 * 
	 * @return der SQL-String für das Erstellen des Primärschlüssels
	 */
	@JsonIgnore
	public String getSQL() {
		if (spalten.size() <= 0)
			return "";
		return spalten.stream().map(spalte -> spalte.NameSpalte).collect(Collectors.joining(", ", "CONSTRAINT PK_" + tabelle.Name + " PRIMARY KEY (", ")"));
	}


	
	/**
	 * Erstellt die SQL-Skripte zum Erstellen oder Entfernen von Triggern für das Auto-Inkrement
	 * 
	 * @param dbms     das DBMS für welches das Skript angefragt wird
     * @param rev      die Revision, für welche die Trigger der Tabelle erzeugt oder entfernt werden sollen
	 * @param create   gibt an, ob das CREATE-Skript oder das Drop-Skript angefragt wird.
	 * 
	 * @return das SQL-Skript zum Erstellen oder Entfernen von Triggern für das Auto-Inkrement
	 */
	@JsonIgnore 
	public String getTriggerSQL(DBDriver dbms, int rev, boolean create) {
    	var triggerList = getTriggerSQLList(dbms, rev, create);
    	if (triggerList.size() <= 0)
    		return "";
    	var newline = System.lineSeparator();
    	if (DBDriver.MARIA_DB.equals(dbms) || DBDriver.MYSQL.equals(dbms)) {
    		return triggerList.stream().map(sql -> "delimiter $" + newline + sql + newline + "$" + newline + "delimiter ;" + newline)
    				.collect(Collectors.joining(newline + newline));
    	} else if (DBDriver.MSSQL.equals(dbms)) {
    		return triggerList.stream().map(sql -> sql + newline + "GO" + newline)
    				.collect(Collectors.joining(newline + newline));    		
    	} 
    	// DBDriver.SQLITE.equals(dbms))
		return triggerList.stream().collect(Collectors.joining(newline + newline));    		
	}
	
	
	
	/**
	 * Erstellt die SQL-Skripte zum Erstellen oder Entfernen von Triggern für das Auto-Inkrement
	 * 
	 * @param dbms     das DBMS für welches das Skript angefragt wird
     * @param rev      die Revision, für welche die Trigger der Tabelle erzeugt oder entfernt werden sollen
	 * @param create   gibt an, ob das CREATE-Skript oder das Drop-Skript angefragt wird.
	 * 
	 * @return das SQL-Skript zum Erstellen oder Entfernen von Triggern für das Auto-Inkrement
	 */
	@JsonIgnore 
	public List<String> getTriggerSQLList(DBDriver dbms, int rev, boolean create) {
		Vector<String> result = new Vector<>();
		if ((!this.hatAutoIncrement) || (this.spalten.size() != 1))
			return result;
		// DBDriver.MDB wird hier nicht unterstützt !!!
		String tab = this.tabelle.Name;
		String spalte = this.spalten.get(0).NameSpalte;
		String newline = System.lineSeparator();
		if (create) {
			if (!(((rev == -1) && (this.tabelle.dbRevisionVeraltet.Revision == -1))
					|| ((rev != -1) && (rev >= this.tabelle.dbRevision.Revision) && ((this.tabelle.dbRevisionVeraltet.Revision == -1) || (rev < this.tabelle.dbRevisionVeraltet.Revision)))))
				return result;
			if (DBDriver.MARIA_DB.equals(dbms) || DBDriver.MYSQL.equals(dbms)) {
				result.add( 
						"CREATE TRIGGER t_AutoIncrement_INSERT_" + tab + newline +
						"BEFORE INSERT" + newline +
						"  ON " + tab + " FOR EACH ROW" + newline +
						"BEGIN" + newline +
						"  DECLARE tmpID bigint;" + newline +
						"  SELECT MaxID INTO tmpID FROM SVWS_DB_AutoInkremente WHERE NameTabelle='" + tab + "';" + newline +
						"  IF tmpID IS NULL THEN" + newline +
						"    SELECT max(" + spalte + ") INTO tmpID FROM " + tab + ";" + newline +
						"    IF tmpID IS NULL THEN" + newline +
						"      SET tmpID = 0;" + newline +
						"    END IF;" + newline +
						"    INSERT INTO SVWS_DB_AutoInkremente(NameTabelle, MaxID) VALUES ('" + tab + "', tmpID);" + newline +
						"  END IF;" + newline +
						"  IF NEW." + spalte + " < 0 THEN" + newline +
						"    SET NEW." + spalte + " = tmpID + 1;" + newline +
						"  END IF;" + newline +
						"  IF NEW." + spalte + " > tmpID THEN" + newline +
						"    UPDATE SVWS_DB_AutoInkremente SET MaxID = NEW." + spalte + " WHERE NameTabelle='" + tab + "';" + newline +
						"  END IF;" + newline +
						"END" + newline
				);
				result.add( 
						"CREATE TRIGGER t_AutoIncrement_UPDATE_" + tab + newline + 
						"BEFORE UPDATE" + newline + 
						"  ON " + tab + " FOR EACH ROW" + newline + 
						"BEGIN" + newline +
						"  DECLARE tmpID bigint;" + newline + 
						"  IF (OLD." + spalte + " <> NEW." + spalte + ") THEN" + newline + 
						"    SELECT MaxID INTO tmpID FROM SVWS_DB_AutoInkremente WHERE NameTabelle='" + tab + "';" + newline +
						"    IF tmpID IS NULL THEN" + newline +
						"      SELECT max(" + spalte + ") INTO tmpID FROM " + tab + ";" + newline + 
						"      IF tmpID IS NULL THEN" + newline +
						"        SET tmpID = 0;" + newline +
						"      END IF;" + newline +
						"      INSERT INTO SVWS_DB_AutoInkremente(NameTabelle, MaxID) VALUES ('" + tab + "', tmpID);" + newline + 
						"    END IF;" + newline +
						"    IF NEW." + spalte + " < 0 THEN" + newline + 
						"      SET NEW." + spalte + " = tmpID + 1;" + newline + 
						"    END IF;" + newline +
						"    IF NEW." + spalte + " > tmpID THEN" + newline + 
						"      UPDATE SVWS_DB_AutoInkremente SET MaxID = NEW." + spalte + " WHERE NameTabelle='" + tab + "';" + newline + 
						"    END IF;" + newline + 
						"  END IF;" + newline +
						"END" + newline
				);
			} else if (DBDriver.MSSQL.equals(dbms)) {
				result.add(
						"exec('" + newline + 
						"CREATE TRIGGER t_AutoIncrement_INSERT_" + tab + " ON " + tab + " INSTEAD OF INSERT AS" + newline + 
						"BEGIN" + newline + 
						"  DECLARE @tmpID bigint" + newline + 
						"  DECLARE @maxInsertedID bigint" + newline + 
						newline + 
						"  SET @maxInsertedID = (SELECT max(" + spalte + ") FROM inserted WHERE " + spalte + " >= 0)" + newline + 
						"  INSERT INTO " + tab + newline + 
						"    SELECT * FROM inserted WHERE " + spalte + " >= 0" + newline + 
						"    " + newline + 
						"  SET @tmpID = (SELECT MaxID FROM SVWS_DB_AutoInkremente WHERE NameTabelle = ''" + tab + "'')" + newline + 
						"  IF (@tmpID IS NULL)" + newline + 
						"    BEGIN" + newline + 
						"      SET @tmpID = (SELECT max(" + spalte + ") FROM " + tab + ")" + newline + 
						"      IF (@tmpID IS NULL)" + newline + 
						"        BEGIN" + newline + 
						"          SET @tmpID = 0" + newline + 
						"        END" + newline + 
						"      SET NOCOUNT ON" + newline + 
						"      INSERT INTO SVWS_DB_AutoInkremente(NameTabelle, MaxID) VALUES (''" + tab + "'', @tmpID)" + newline + 
						"      SET NOCOUNT OFF" + newline + 
						"    END" + newline + 
						"  " + newline + 
						"  IF ((SELECT count(*) FROM inserted WHERE " + spalte + " < 0) > 0)" + newline + 
						"    BEGIN  " + newline + 
						"      SELECT * INTO #tmp FROM inserted WHERE " + spalte + " < 0" + newline + 
						"      UPDATE #tmp SET " + spalte + " = @tmpID, @tmpID = @tmpID + 1" + newline + 
						"      INSERT INTO " + tab + newline + 
						"        SELECT * FROM #tmp" + newline + 
						"      DROP TABLE #tmp" + newline + 
						"    END" + newline + 
						"  " + newline + 
						"  SET NOCOUNT ON" + newline + 
						"  IF (@maxInsertedID > @tmpID)" + newline + 
						"    BEGIN" + newline + 
						"      SET @tmpID = @maxInsertedID" + newline + 
						"	 END" + newline + 
						"  UPDATE SVWS_DB_AutoInkremente SET MaxID = @tmpID WHERE NameTabelle = ''" + tab + "''" + newline + 
						"  SET NOCOUNT OFF" + newline + 
						"END;" + newline + 
						"')" + newline
				);
				result.add(
						"exec('" + newline +
						"CREATE TRIGGER t_AutoIncrement_UPDATE_" + tab + " ON " + tab + " AFTER UPDATE AS" + newline + 
						"BEGIN" + newline + 
						"  if (UPDATE(" + spalte + "))" + newline + 
						"    BEGIN" + newline + 
						"      DECLARE @tmpID bigint" + newline + 
						"      DECLARE @maxInsertedID bigint" + newline + 
						newline + 
						"      SET @maxInsertedID = (SELECT max(" + spalte + ") FROM inserted WHERE " + spalte + " >= 0)" + newline + 
						"      SET @tmpID = (SELECT MaxID FROM SVWS_DB_AutoInkremente WHERE NameTabelle = ''" + tab + "'')" + newline + 
						"      IF (@tmpID IS NULL)" + newline + 
						"        BEGIN" + newline + 
						"          SET @tmpID = (SELECT max(" + spalte + ") FROM " + tab + ")" + newline + 
						"          IF (@tmpID IS NULL)" + newline + 
						"            BEGIN" + newline + 
						"              SET @tmpID = 0" + newline + 
						"            END" + newline + 
						"          INSERT INTO SVWS_DB_AutoInkremente(NameTabelle, MaxID) VALUES (''" + tab + "'', @tmpID)" + newline + 
						"        END    " + newline + 
						"      IF (@maxInsertedID > @tmpID)" + newline + 
						"        BEGIN" + newline + 
						"          SET @tmpID = @maxInsertedID" + newline + 
						"	      END" + newline + 
						"      UPDATE SVWS_DB_AutoInkremente SET MaxID = @tmpID WHERE NameTabelle = ''" + tab + "''" + newline + 
						"    END" + newline + 
						"END;" + newline +
						"')" + newline
				);
			} else if (DBDriver.SQLITE.equals(dbms)) {
				result.add( 
						"CREATE TRIGGER t_AutoIncrement_INSERT_" + tab + "_1 AFTER INSERT ON " + tab + " FOR EACH ROW" + newline + 
						"	WHEN NEW." + spalte + " >= 0 AND " + newline + 
						"	  (SELECT MaxID FROM SVWS_DB_AutoInkremente WHERE NameTabelle='" + tab + "') IS NOT NULL AND " + newline + 
						"	  NEW." + spalte + " > (SELECT max(MaxID) FROM SVWS_DB_AutoInkremente WHERE NameTabelle='" + tab + "')" + newline + 
						"BEGIN" + newline + 
						"  UPDATE SVWS_DB_AutoInkremente SET MaxID = NEW." + spalte + " WHERE NameTabelle = '" + tab + "';" + newline + 
						"END;\r\n"
				);
				result.add( 
						"CREATE TRIGGER t_AutoIncrement_INSERT_" + tab + "_2 AFTER INSERT ON " + tab + " FOR EACH ROW" + newline + 
						"	WHEN NEW." + spalte + " < 0 AND" + newline + 
						"	  (SELECT MaxID FROM SVWS_DB_AutoInkremente WHERE NameTabelle='" + tab + "') IS NOT NULL" + newline + 
						"BEGIN" + newline + 
						"  UPDATE " + tab + " SET " + spalte + " = (SELECT MaxID FROM SVWS_DB_AutoInkremente WHERE NameTabelle='" + tab + "') + 1 WHERE " + spalte + " = NEW." + spalte + ";" + newline + 
						"  UPDATE SVWS_DB_AutoInkremente SET MaxID = MaxID + 1 WHERE NameTabelle = '" + tab + "';" + newline + 
						"END;\r\n"
				);
				result.add(
						"CREATE TRIGGER t_AutoIncrement_INSERT_" + tab + "_3 AFTER INSERT ON " + tab + " FOR EACH ROW" + newline + 
						"	WHEN NEW." + spalte + " >= 0 AND " + newline + 
						"	  (SELECT MaxID FROM SVWS_DB_AutoInkremente WHERE NameTabelle='" + tab + "') IS NULL AND" + newline + 
						"	  NEW." + spalte + " < coalesce((SELECT max(" + spalte + ") FROM " + tab + "), 0)" + newline + 
						"BEGIN" + newline + 
						"  INSERT INTO SVWS_DB_AutoInkremente(NameTabelle, MaxID) VALUES ('" + tab + "', coalesce((SELECT max(" + spalte + ") FROM " + tab + "), 0));" + newline + 
						"END;\r\n"
				);
				result.add(
						"CREATE TRIGGER t_AutoIncrement_INSERT_" + tab + "_4 AFTER INSERT ON " + tab + " FOR EACH ROW" + newline + 
						"	WHEN NEW." + spalte + " >= 0 AND " + newline + 
						"	  (SELECT MaxID FROM SVWS_DB_AutoInkremente WHERE NameTabelle='" + tab + "') IS NULL AND" + newline + 
						"	  NEW." + spalte + " >= coalesce((SELECT max(" + spalte + ") FROM " + tab + "), 0)" + newline + 
						"BEGIN" + newline + 
						"  INSERT INTO SVWS_DB_AutoInkremente(NameTabelle, MaxID) VALUES ('" + tab + "',  NEW." + spalte + ");" + newline + 
						"END;\r\n"
				);
				result.add(
						"CREATE TRIGGER t_AutoIncrement_INSERT_" + tab + "_5 AFTER INSERT ON " + tab + " FOR EACH ROW" + newline + 
						"	WHEN NEW." + spalte + " < 0 AND" + newline + 
						"	  (SELECT MaxID FROM SVWS_DB_AutoInkremente WHERE NameTabelle='" + tab + "') IS NULL" + newline + 
						"BEGIN" + newline + 
						"  UPDATE " + tab + " SET " + spalte + " = coalesce((SELECT max(" + spalte + ") FROM " + tab + "), 0) + 1 WHERE " + spalte + " = NEW." + spalte + ";" + newline + 
						"  INSERT INTO SVWS_DB_AutoInkremente(NameTabelle, MaxID) VALUES ('" + tab + "',  coalesce((SELECT max(" + spalte + ") FROM " + tab + "), 0) + 1);" + newline + 
						"END;\r\n"
				);
				result.add(
						"CREATE TRIGGER t_AutoIncrement_UPDATE_" + tab + "_1 AFTER UPDATE ON " + tab + " FOR EACH ROW" + newline + 
						"	WHEN NEW." + spalte + " >= 0 AND " + newline + 
						"	  (SELECT max(MaxID) FROM SVWS_DB_AutoInkremente WHERE NameTabelle='" + tab + "') IS NOT NULL AND " + newline + 
						"	  NEW." + spalte + " > (SELECT max(MaxID) FROM SVWS_DB_AutoInkremente WHERE NameTabelle='" + tab + "')" + newline + 
						"BEGIN" + newline + 
						"  UPDATE SVWS_DB_AutoInkremente SET MaxID = NEW." + spalte + " WHERE NameTabelle = '" + tab + "';" + newline + 
						"END;\r\n"
				);
				result.add(
						"CREATE TRIGGER t_AutoIncrement_UPDATE_" + tab + "_2 AFTER UPDATE ON " + tab + " FOR EACH ROW" + newline + 
						"	WHEN NEW." + spalte + " < 0 AND" + newline + 
						"	  (SELECT max(MaxID) FROM SVWS_DB_AutoInkremente WHERE NameTabelle='" + tab + "') IS NOT NULL" + newline + 
						"BEGIN" + newline + 
						"  UPDATE " + tab + " SET " + spalte + " = (SELECT MaxID FROM SVWS_DB_AutoInkremente WHERE NameTabelle='" + tab + "') + 1 WHERE " + spalte + " = NEW." + spalte + ";" + newline + 
						"  UPDATE SVWS_DB_AutoInkremente SET MaxID = MaxID + 1 WHERE NameTabelle = '" + tab + "';" + newline + 
						"END;\r\n"
				);
				result.add(
						"CREATE TRIGGER t_AutoIncrement_UPDATE_" + tab + "_3 AFTER UPDATE ON " + tab + " FOR EACH ROW" + newline + 
						"	WHEN NEW." + spalte + " >= 0 AND " + newline + 
						"	  (SELECT max(MaxID) FROM SVWS_DB_AutoInkremente WHERE NameTabelle='" + tab + "') IS NULL AND" + newline + 
						"	  NEW." + spalte + " < coalesce((SELECT max(" + spalte + ") FROM " + tab + "), 0)" + newline + 
						"BEGIN" + newline + 
						"  INSERT INTO SVWS_DB_AutoInkremente(NameTabelle, MaxID) VALUES ('" + tab + "', coalesce((SELECT max(" + spalte + ") FROM " + tab + "), 0));" + newline + 
						"END;\r\n"
				);
				result.add(
						"CREATE TRIGGER t_AutoIncrement_UPDATE_" + tab + "_4 AFTER UPDATE ON " + tab + " FOR EACH ROW" + newline + 
						"	WHEN NEW." + spalte + " >= 0 AND " + newline + 
						"	  (SELECT max(MaxID) FROM SVWS_DB_AutoInkremente WHERE NameTabelle='" + tab + "') IS NULL AND" + newline + 
						"	  NEW." + spalte + " >= coalesce((SELECT max(" + spalte + ") FROM " + tab + "), 0)" + newline + 
						"BEGIN" + newline + 
						"  INSERT INTO SVWS_DB_AutoInkremente(NameTabelle, MaxID) VALUES ('" + tab + "',  NEW." + spalte + ");" + newline + 
						"END;\r\n"
				);
				result.add(
						"CREATE TRIGGER t_AutoIncrement_UPDATE_" + tab + "_5 AFTER UPDATE ON " + tab + " FOR EACH ROW" + newline + 
						"	WHEN NEW." + spalte + " < 0 AND" + newline + 
						"	  (SELECT max(MaxID) FROM SVWS_DB_AutoInkremente WHERE NameTabelle='" + tab + "') IS NULL" + newline + 
						"BEGIN" + newline + 
						"  -- Update der " + spalte + " in der Tabelle " + tab + " erfolgt durch den Autoinkrement-Trigger 2, daher hier auch kein +1, sondern nur den Max-Wert schreiben" + newline + 
						"  INSERT INTO SVWS_DB_AutoInkremente(NameTabelle, MaxID) VALUES ('" + tab + "',  coalesce((SELECT max(" + spalte + ") FROM " + tab + "), 0));" + newline + 
						"END;\r\n"
				);
			}
		} else {
			if ((this.tabelle.dbRevisionVeraltet.Revision < 0) || (rev < this.tabelle.dbRevisionVeraltet.Revision))
				return result;
			if (DBDriver.MARIA_DB.equals(dbms) || DBDriver.MYSQL.equals(dbms)) {
				result.add("DROP TRIGGER IF EXISTS t_AutoIncrement_INSERT_" + tab + ";");
				result.add("DROP TRIGGER IF EXISTS t_AutoIncrement_UPDATE_" + tab + ";");
			} else if (DBDriver.MSSQL.equals(dbms)) {
				result.add("DROP TRIGGER t_AutoIncrement_INSERT_" + tab + ";");
				result.add("DROP TRIGGER t_AutoIncrement_UPDATE_" + tab + ";"); 
			} else if (DBDriver.SQLITE.equals(dbms)) {
				result.add("DROP TRIGGER IF EXISTS t_AutoIncrement_INSERT_" + tab + "_1;");
				result.add("DROP TRIGGER IF EXISTS t_AutoIncrement_INSERT_" + tab + "_2;");
				result.add("DROP TRIGGER IF EXISTS t_AutoIncrement_INSERT_" + tab + "_3;");
				result.add("DROP TRIGGER IF EXISTS t_AutoIncrement_INSERT_" + tab + "_4;");
				result.add("DROP TRIGGER IF EXISTS t_AutoIncrement_INSERT_" + tab + "_5;");
				result.add("DROP TRIGGER IF EXISTS t_AutoIncrement_UPDATE_" + tab + "_1;");
				result.add("DROP TRIGGER IF EXISTS t_AutoIncrement_UPDATE_" + tab + "_2;");
				result.add("DROP TRIGGER IF EXISTS t_AutoIncrement_UPDATE_" + tab + "_3;");
				result.add("DROP TRIGGER IF EXISTS t_AutoIncrement_UPDATE_" + tab + "_4;");
				result.add("DROP TRIGGER IF EXISTS t_AutoIncrement_UPDATE_" + tab + "_5;");
			}
		}
		return result;
	}
	
	
}
