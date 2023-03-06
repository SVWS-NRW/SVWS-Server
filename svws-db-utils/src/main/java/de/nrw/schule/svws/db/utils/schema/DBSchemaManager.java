package de.nrw.schule.svws.db.utils.schema;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import de.nrw.schule.svws.core.logger.Logger;
import de.nrw.schule.svws.db.Benutzer;
import de.nrw.schule.svws.db.DBDriver;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.svws.db.DTODBVersion;
import de.nrw.schule.svws.db.schema.DBSchemaViews;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaRevisionUpdateSQL;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleIndex;
import de.nrw.schule.svws.db.schema.SchemaTabelleTrigger;
import de.nrw.schule.svws.db.schema.View;

/**
 * Diese Klasse stellt Hilfs-Funktionen zur Verfügung, um auf ein SVWS-Datenbank-Schema zuzugreifen und dieses zu bearbeiten.
 */
public class DBSchemaManager {
	
	/// Der Datenbank-Benutzer
	private final Benutzer user;
	
	/// Der Status des Datenbank-Schema
	private final DBSchemaStatus status;

	/// Ein Logger, um die Abläufe in dem Schema-Manager zu loggen 
	private final Logger logger;
	
	/// Gibt an, ob die Ausführung von Operationen bei einzelnen Fehlern abgebrochen werden sollen.
	private final boolean returnOnError;
	
	/// Enthält ggf. einen Fehler-String für einen zuletzt aufgetretenen Fehler 
	private String lastError;

	/** Der Updater, um Datenbank-Updates durchzuführen */
	public final DBUpdater updater;
	
	/** Der Backup-Manager, für den Import von und den Export nach SQLite */
	public final DBBackupManager backup;
	
	
	/**
	 * Erstellt einen neuen DBSchema-Manager, der Schema-Operationen mithilfe des angegebenen Datenbank-Benutzer ermöglicht.
	 * 
	 * @param user             der Datenbak-Benutzer
	 * @param returnOnError    gibt an, ob Operatioen bei Einzelfehlern abgebrochen werden sollen 
	 * @param logger           ein Logger, um die Abläufe in dem Schema-Manager zu loggen
	 */
	private DBSchemaManager(final Benutzer user, final boolean returnOnError, final Logger logger) {
		this.user = user;
		this.status = DBSchemaStatus.read(user);
		this.returnOnError = returnOnError;
		this.logger = logger;
		this.updater = new DBUpdater(this, returnOnError);
		this.backup = new DBBackupManager(this);
	}
	
	
	
	/**
	 * Versucht einen neuen DB-Schema-Manager zu erstellen.
	 * 
	 * @param user             der Datenbank-Benutzer
	 * @param returnOnError    gibt an, ob Operatioen bei Einzelfehlern abgebrochen werden sollen 
	 * @param logger           ein Logger, um die Abläufe in dem Schema-Manager zu loggen
	 * 
	 * @return der DB-Schema-Manager bei Erfolg
	 */
	public static DBSchemaManager create(final Benutzer user, final boolean returnOnError, Logger logger) {
		return new DBSchemaManager(user, returnOnError, (logger == null) ? new Logger() : logger);		
	}
	
	
	
	/**
	 * Gibt den Datenbank-Benutzer des Managers zurück.
	 * 
	 * @return der Datenbank-Benutzer
	 */
	public Benutzer getUser() {
		return this.user;
	}

	
	/**
	 * Gibt den Schema-Status  (see {@link DBSchemaStatus} zurück.
	 * 
	 * @return der Schema-Status von diesem Schema-Manager 
	 */
	public DBSchemaStatus getSchemaStatus() {
		return this.status;
	}

	
	/**
	 * Gibt den vom Schema-Manager verwendeten Logger zurück.
	 * 
	 * @return der Logger
	 */
	public Logger getLogger() {
		return this.logger;
	}


	/**
	 * Gibt den zuletzt gesetzten Fehler einer Operation des Schema-Managers zurück.
	 * 
	 * @return der zuletzt gesetzte Fehler
	 */
	public String getLastError() {
		return lastError;
	}

	
	
	/**
	 * Führt die SQL-Skripte zum Erstellen aller Datenbank-Tabellen der angegebenen Schema-Revision
	 * aus.
	 *   
	 * @param revision   die Revision des Datenbank-Schemas
	 * 
	 * @return true, falls alle Tabellen erfolgreich erstellt wurden
	 */
	private boolean createAllTables(long revision) {
		try (DBEntityManager conn = user.getEntityManager()) {
			boolean result = true;
			var dbms = conn.getDBDriver();
			for (SchemaTabelle tab : Schema.getTabellen(revision)) {
				logger.logLn(tab.name());
				String script = tab.getSQL(dbms, revision);
				if (conn.executeNativeUpdate(script) == Integer.MIN_VALUE) {
					result = false;
					if (returnOnError)
						break;
				} else {
					List<String> pkTrigger = tab.getPrimaerschluesselTriggerSQLList(dbms, revision, true);
					if (pkTrigger.size() > 0) {
						logger.logLn("  -> Erstelle Trigger für Auto-Inkremente");
						for (String scriptTrigger : pkTrigger) {
							if (conn.executeNativeUpdate(scriptTrigger) == Integer.MIN_VALUE) {
								result = false;
								if (returnOnError)
									break;
							}					
						}
					}
				}
			}
			return result;
		}
	}
	
	
	
	/**
	 * Führt die SQL-Skripte zum Erstellen aller Datenbank-Indizes der angegebenen Schema-Revision
	 * aus.
	 *   
	 * @param revision   die Revision des Datenbank-Schemas
	 * 
	 * @return true, falls alle Indizes erfolgreich erstellt wurden
	 */
	private boolean createAllIndizes(long revision) {
		try (DBEntityManager conn = user.getEntityManager()) {
			boolean result = true;
			for (SchemaTabelle tab : Schema.getTabellen(revision)) {
				for (SchemaTabelleIndex idx : tab.indizes()) {
					logger.logLn(idx.name());
					String script = idx.getSQL();
					if (conn.executeNativeUpdate(script) == Integer.MIN_VALUE) {
						result = false;
						if (returnOnError)
							break;
					}
				}
			}
			return result;
		}
	}
	
	
	
	/**
	 * Führt die SQL-Skripte zum Erstellen aller Datenbank-Trigger der angegebenen Schema-Revision
	 * aus, welche nicht zu den Auto-Inkrementen bei Primärschlüsseln gehören.
	 *   
	 * @param revision   die Revision des Datenbank-Schemas
	 * 
	 * @return true, falls alle Trigger erfolgreich erstellt wurden
	 */
	private boolean createAllTrigger(long revision) {
		try (DBEntityManager conn = user.getEntityManager()) {
			boolean result = true;
			var dbms = conn.getDBDriver();
			for (SchemaTabelle tab : Schema.getTabellen(revision)) {
				for (SchemaTabelleTrigger trig : tab.trigger()) {
					if (!dbms.equals(trig.dbms()))
						continue;
					if (revision < trig.revision().revision)
						continue;
					if ((trig.veraltet().revision >= 0) && (revision > trig.veraltet().revision))
						continue;
					logger.logLn(trig.name());
					String script = trig.getSQL(conn.getDBDriver(), true);
					if (conn.executeWithJDBCConnection(script) == Integer.MIN_VALUE) {
						result = false;
						if (returnOnError)
							break;
					}				
				}
			}
			return result;
		}
	}
	
	
	
	/**
	 * Führt die manuellen SQL-Skripte zum Erstellen eines Schemas der angegebenen Schema-Revision
	 * aus.
	 *   
	 * @param revision   die Revision des Datenbank-Schemas
	 * 
	 * @return true, falls alle Skripte erfolgreich ausgeführt wurden
	 */
	private boolean executeManualSQLOnCreate(long revision) {
		try (DBEntityManager conn = user.getEntityManager()) {
			var dbms = conn.getDBDriver();
			for (long r = 0; r <= revision; r++) {
				SchemaRevisionUpdateSQL msqlAll = SchemaRevisionen.get(revision).getUpdater();
				if ((!msqlAll.runFirst(conn, logger)) && returnOnError)
					return false;
				for (int i = 0; i < msqlAll.size(); i++) {
					String script = msqlAll.getSQL(dbms, i);
					if ((script == null) || "".equals(script))
						continue; // should not happen
					logger.logLn(msqlAll.getKommentar(i));
					if (conn.executeNativeUpdate(script) == Integer.MIN_VALUE) {
						if (returnOnError)
							return false;
					}
				}
				if ((!msqlAll.runLast(conn, logger)) && returnOnError)
					return false;
			}
			return true;
		}
	}



	/**
	 * Erstellt die Views für das Schemas der angegebenen Schema-Revision.
	 *   
	 * @param revision   die Revision des Datenbank-Schemas
	 * 
	 * @return true, falls alle Skripte erfolgreich ausgeführt wurden
	 */
	private boolean executeSQLCreateViews(long revision) {
		try (DBEntityManager conn = user.getEntityManager()) {
			boolean result = true;
			List<View> views = DBSchemaViews.getInstance().getViewsActive(revision);
			for (View view : views) {
				logger.logLn(view.name);
				if (conn.executeNativeUpdate(view.getSQLCreate(conn.getDBDriver())) == Integer.MIN_VALUE) {
					result = false;
					if (returnOnError)
						break;
				}
			}
			return result;
		}
	}



	/**
	 * Führt die SQL-Finalisierungs-Skripte beim Erstellen eines Schemas der angegebenen Schema-Revision
	 * aus.
	 *
	 * @param revision   die Revision des Datenbank-Schemas
	 * 
	 * @return true, falls alle Skripte erfolgreich ausgeführt wurden
	 */
	private boolean createDefaultSVWSBenutzer(long revision) {
		try (DBEntityManager conn = user.getEntityManager()) {
			boolean result = true;
			List<String> sqlList = Schema.getCreateBenutzerSQL(revision);
			for (String sql : sqlList) {
				logger.logLn(sql);
				if (conn.executeNativeUpdate(sql) == Integer.MIN_VALUE) {
					result = false;
					if (returnOnError)
						break;
				}
			}
			return result;
		}
	}
	

	/**
	 * Setzt die Datenbank-Revision auf die angegebene Revision
	 * 
	 * @param revision   die zu setzende Revision, bei -1 wird die neueste Revision gesetzt
	 * 
	 * @return true, falls die Revision erfolgreich gesetzt wurde, sonst false
	 */
	public boolean setDBRevision(long revision) {
		try (DBEntityManager conn = user.getEntityManager()) {
			long rev = (revision == -1) ? SchemaRevisionen.maxRevision.revision : revision;
			if (rev == -1)
				return false;
			DTODBVersion oldObj = conn.querySingle(DTODBVersion.class);
			DTODBVersion newObj = new DTODBVersion(rev, (rev > SchemaRevisionen.maxRevision.revision) || ((oldObj != null) && (oldObj.IsTainted)));
			if (oldObj == null) {
				conn.persist(newObj);
			} else {
				conn.replace(oldObj, newObj);
			}
			return true;
		}
	}


	/**
	 * Erstellt ein SVWS-Datenbank-Schema der angegebenen Revision 
	 * 
	 * @param revision    die Revision für das SVWS-DB-Schema
	 * @param createUser  gibt an, ob Default-SVWS-Benutzer angelegt werden sollen
	 * 
	 * @return true, wenn das Schema erfolgreich erstellt wurde, sonst false
	 */
	public boolean createSVWSSchema(long revision, boolean createUser) {
		logger.logLn("- Erstelle Tabellen für die aktuelle DB-Revision... ");
		logger.modifyIndent(2);
		boolean success = createAllTables(revision);
		logger.modifyIndent(-2);
		if (!success) {
			logger.logLn("[Fehler]");
			if (returnOnError) return false;
		}
		logger.logLn("[OK]");
		
		logger.logLn("- Erstelle Indizes für die aktuelle DB-Revision... ");
		logger.modifyIndent(2);
		success = createAllIndizes(revision);
		logger.modifyIndent(-2);
		if (!success) {
			logger.logLn("[Fehler]");
			if (returnOnError) return false;
		}
		logger.logLn("[OK]");
		
		logger.logLn("- Erstelle Trigger für die aktuelle DB-Revision... ");
		logger.modifyIndent(2);
		success = createAllTrigger(revision);
		logger.modifyIndent(-2);
		if (!success) {
			logger.logLn("[Fehler]");
			if (returnOnError) return false;
		}
		logger.logLn("[OK]");

		logger.logLn("- Schreibe die Daten der Core-Types");
		logger.modifyIndent(2);
		success = updater.coreTypes.update(false, revision);
		logger.modifyIndent(-2);
		if (!success) {
			logger.logLn("[Fehler]");
			if (returnOnError) return false;
		}
		logger.logLn("[OK]");
		
		logger.logLn("- Führe manuelle SQL-Befehle aus: ");
		logger.modifyIndent(2);
		success = executeManualSQLOnCreate(revision);
		logger.modifyIndent(-2);
		if (!success) {
			logger.logLn("[Fehler]");
			if (returnOnError) return false;
		}
		logger.logLn("[OK]");
		
		logger.logLn("- Erstelle Views: ");
		logger.modifyIndent(2);
		success = executeSQLCreateViews(revision);
		logger.modifyIndent(-2);
		if (!success) {
			logger.logLn("[Fehler]");
			if (returnOnError) return false;
		}
		logger.logLn("[OK]");
		
		if (createUser) {
			logger.logLn("- Lege Default-Benutzer an: ");
			logger.modifyIndent(2);
			success = createDefaultSVWSBenutzer(revision);
			logger.modifyIndent(-2);
			if (!success) {
				logger.logLn("[Fehler]");
				if (returnOnError) return false;
			}
			logger.logLn("[OK]");
		}
		
		logger.logLn("- Setze die DB-Revision in der neu erzeugten Datenbank: ");
		logger.modifyIndent(2);
		success = setDBRevision(revision);
		logger.modifyIndent(-2);
		if (!success) {
			logger.logLn("[Fehler]");
			if (returnOnError) return false;
		}
		logger.logLn("[OK]");
		return true;
	}
	
	
	
	/**
	 * Verwirft das SVWS-Datenbank-Schema. 
	 * 
	 * @return true, wenn das Schema erfolgreich verworfen wurde, 
	 *         false wenn dabei Fehler aufgetreten sind.
	 */
	public boolean dropSVWSSchema() {
		try (DBEntityManager conn = user.getEntityManager()) {
			boolean success = true;
			logger.logLn("- Verwerfe Tabellen...");
			logger.modifyIndent(2);
			
			DBDriver driver = conn.getDBDriver();
			if ((driver == DBDriver.MDB) || (driver == DBDriver.SQLITE)) {
				// no support to drop multiple tables in one drop statement - drop table order is important due to foreign key constraints
				// Bestimme die aktuelle Revision der Datenbank
				DBSchemaVersion version = status.getVersion(); 
				long revision = version.getRevisionOrDefault(0);
				List<SchemaTabelle> tabellen = Schema.getTabellen(revision);
				Collections.reverse(tabellen);
				for (SchemaTabelle tab : tabellen) {
					// Prüfe bei einer Lecagy-Schild-DB, ob eine Tabelle für die Migration vorliegt - nur diese sollen verworfen werden
					if (!version.isValid() && !tab.migrate())
						continue;
					if (!status.hasTable(tab.name()))
						continue;				
					logger.log(tab.name() + "... ");
					String sql = "DROP TABLE " + ((driver == DBDriver.SQLITE) ? "IF EXISTS " : "") + tab.name() + ";";
					int result = conn.executeWithJDBCConnection(sql);
					if (result == Integer.MIN_VALUE) {
						logger.logLn(0, " [Fehler]");
						success = false;
					} else {
						logger.logLn(0, " [OK]");					
					}
				}
	
				// berücksichtige auch Tabellen, die bei der Revision eigentlich nicht definiert oder nicht für die Migration vorgemerkt sind 
				status.update();
				for (String tabname : status.getTabellen()) {
					logger.log(tabname + "... ");
					String sql = "DROP TABLE " + ((driver == DBDriver.SQLITE) ? "IF EXISTS " : "") + tabname + ";";
					int result = conn.executeWithJDBCConnection(sql);
					if (result == Integer.MIN_VALUE) {
						logger.logLn(0, " [Fehler]");
						success = false;
					} else {
						logger.logLn(0, " [OK]");					
					}
				}
			} else if ((driver == DBDriver.MARIA_DB) || (driver == DBDriver.MYSQL) || (driver == DBDriver.MSSQL)) {
				// drop all existing in one statement - we do not need to cope with foreign key constraints
				List<String> tableNames = status.getTabellen();
				if (tableNames.size() > 0) {
					logger.log("alle auf einmal... ");
					String sql = status.getTabellen().stream().collect(Collectors.joining(",", "DROP TABLE IF EXISTS ", ";"));
					int result = conn.executeWithJDBCConnection(sql);
					if (result == Integer.MIN_VALUE) {
						logger.logLn(0, " [Fehler]");
						success = false;
					} else {
						logger.logLn(0, " [OK]");					
					}
				}
			}
			
			logger.modifyIndent(-2);
			status.update();
			return success;
		}
	}
	
	
	
	/**
	 * Versucht Repariert eine alte Schild2-Datenbank zu reparieren. Dieser Schritt ist
	 * empfehlenswert vor der Migration zur SVWS-DB. 
	 */
	@Deprecated
	public void repairLegacyDB() {
		try (DBEntityManager conn = user.getEntityManager()) {
			int count = 0;
			// Entferne Schulen, denen keine Schulnummer zugewiesen ist.
			logger.log("- Entferne Datensätze aus DTOSchuleNRW bei denen keine Schulnummer zugewiesen ist: ");
			count = conn.executeDelete("DELETE FROM DTOSchuleNRW WHERE SchulNr IS NULL");
			logger.logLn(0, count + " Datensätze");
			
			// Entferne Schüler, bei denen die ID null ist
			logger.log("- Entferne Datensätze aus Schueler, welche keine ID (null oder < 0) haben: ");
			count = conn.executeNativeDelete("DELETE FROM SchuelerLernabschnittsdaten WHERE ID IS NULL OR ID < 0");
			logger.logLn(0, count + " Datensätze");		
			
			// Entferne Schüler Lernabschnitte, bei denen die ID null ist
			logger.log("- Entferne Datensätze aus SchuelerLernabschnittsdaten, welche keine ID (null oder < 0) oder keine gültige Schüler-ID oder kein gültiges Jahr bzw. Abschnitt haben: ");
			count = conn.executeNativeDelete("DELETE FROM SchuelerLernabschnittsdaten WHERE ID IS NULL OR ID < 0 OR Schueler_ID IS NULL OR Schueler_ID < 0 OR Jahr IS NULL OR JAHR < 1960 OR Jahr > 2500 OR Abschnitt IS NULL OR Abschnitt < 1 OR Abschnitt > 4");
			logger.logLn(0, count + " Datensätze");		
			
			// Entferne Schüler Leistungsdaten, bei denen die ID null ist
			logger.log("- Entferne Datensätze aus SchuelerLeistungsdaten, welche keine ID (null oder < 0) haben oder welche keinem Abschnitt, Fach oder Kursart zugeordnet sind: ");
			count = conn.executeNativeDelete("DELETE FROM SchuelerLeistungsdaten WHERE ID IS NULL OR ID < 0 OR Abschnitt_ID IS NULL OR Abschnitt_ID < 0 OR Fach_ID IS NULL OR Fach_ID < 0 OR Kursart IS NULL");
			logger.logLn(0, count + " Datensätze");
			
			// Entferne Schüler-Leistungsdaten, welche eine doppelte ID aufweisen
			logger.log("- Entferne Datensätze aus SchuelerLeistungsdaten bei denen eine ID doppelt vergeben wurde (Primary Key Constraint verletzt): ");
			count = conn.executeNativeDelete("DELETE FROM SchuelerLeistungsdaten WHERE ID IN (SELECT ID FROM SchuelerLeistungsdaten GROUP BY ID HAVING count(*) > 1)");
			logger.logLn(0, count + " Datensätze");
			
			// Entferne SchülerTelefone, bei denen die ID null oder negativ ist
			logger.log("- Entferne Datensätze aus SchuelerTelefone, welche keine ID (null oder < 0) haben: ");
			count = conn.executeNativeDelete("DELETE FROM SchuelerTelefone WHERE ID IS NULL OR ID < 0 OR Schueler_ID IS NULL OR Schueler_ID < 0 OR TelefonArt_ID IS NULL OR TelefonArt_ID < 0");
			logger.logLn(0, count + " Datensätze");		
			
			// Entferne Kurse, bei denen die ID null ist
			logger.log("- Entferne Datensätze aus Kurse, welche keine ID (null) haben: ");
			count = conn.executeNativeDelete("DELETE FROM Kurse WHERE ID IS null");
			logger.logLn(0, count + " Datensätze");		
			
			// Entferne SchuelerLernabschnittsdaten, welche keinen gültigen Schueler zugeordnet haben
			logger.log("- Entferne SchuelerLernabschnittsdaten, welche keinen gültigen Schueler zugeordnet haben: ");
			count = conn.executeNativeDelete("DELETE FROM SchuelerLernabschnittsdaten WHERE Schueler_ID NOT IN (SELECT ID FROM Schueler)");
			logger.logLn(0, count + " Datensätze");		
			
			// Entferne SchuelerLeistungsdaten, welche keine gültigen SchuelerLernabschnittsdaten zugeordnet haben
			logger.log("- Entferne SchuelerLeistungsdaten, welche keine gültigen SchuelerLernabschnittsdaten zugeordnet haben: ");
			count = conn.executeNativeDelete("DELETE FROM SchuelerLeistungsdaten WHERE Abschnitt_ID NOT IN (SELECT ID FROM SchuelerLernabschnittsdaten)");
			logger.logLn(0, count + " Datensätze");		
			
			// Setze die PLZ beim Schüler auf NULL, falls diese nicht in DTOOrt vorhanden ist 
			logger.log("- Passe Schülerdatensätze an - Setze PLZ auf NULL, falls diese nicht bei den Orten gefunden wird: ");
			count = conn.executeUpdate("UPDATE DTOSchueler s SET s.PLZ = NULL WHERE s.PLZ NOT IN (SELECT o.PLZ FROM DTOOrt o)"); 
			logger.logLn(0, count + " Datensätze");
	
			// Setze die PLZ beim Erzieher auf NULL, falls diese nicht in DTOOrt vorhanden ist
			logger.log("- Passe Erzieheradressen an - Setze PLZ auf NULL, falls diese nicht bei den Orten gefunden wird: ");
			count = conn.executeUpdate("UPDATE DTOSchuelerErzieherAdresse e SET e.ErzPLZ = NULL WHERE e.ErzPLZ NOT IN (SELECT o.PLZ FROM DTOOrt o)"); 
			logger.logLn(0, count + " Datensätze");
			
			// Entferne Datensätze aus DTOSchuelerAllgemeineAdresse, wenn die Address-ID NULL oder ungültig ist
			logger.log("- Entferne Datensätze aus Schueler_AllgAdr, bei denen keine gültige Adress-ID gesetzt ist: ");
			count = conn.executeDelete("DELETE FROM DTOSchuelerAllgemeineAdresse a WHERE a.Adresse_ID IS NULL OR a.Adresse_ID NOT IN (SELECT k.ID FROM DTOKatalogAllgemeineAdresse k)");
			logger.logLn(0, count + " Datensätze");
	
			// Entferne Datensätze aus der Tabelle Schueler_AllgAdr, welche in der Tabelle K_AllgAdresse keine gültige Address-Art besitzen.
			logger.log("- Entferne Datensätze aus Schueler_AllgAdr, welche in der Tabelle K_AllgAdresse keine gültige Address-Art besitzen: ");
			count = conn.executeNativeDelete("DELETE FROM Schueler_AllgAdr WHERE Adresse_ID IN ( SELECT ID FROM K_AllgAdresse WHERE AllgAdrAdressArt NOT IN (SELECT DISTINCT Bezeichnung FROM K_AdressArt WHERE Bezeichnung IS NOT NULL))");
			logger.logLn(0, count + " Datensätze");
			
			// Entferne Datensätze aus der Tabelle AllgAdrAnsprechpartner, welche in der Tabelle K_AllgAdresse keine gültige Address-Art besitzen.
			logger.log("- Entferne Datensätze aus AllgAdrAnsprechpartner, welche in der Tabelle K_AllgAdresse keine gültige Address-Art besitzen: ");
			count = conn.executeNativeDelete("DELETE FROM AllgAdrAnsprechpartner WHERE Adresse_ID IN ( SELECT ID FROM K_AllgAdresse WHERE AllgAdrAdressArt NOT IN (SELECT DISTINCT Bezeichnung FROM K_AdressArt WHERE Bezeichnung IS NOT NULL))");
			logger.logLn(0, count + " Datensätze");
			
			// Entferne Datensätze aus der Tabelle K_AllgAdresse, welche die Foreign-Key-Constraint zu K_AdressArt nicht erfüllt.
			logger.log("- Entferne Datensätze aus K_AllgAdresse, welche keine gültige Address-Art besitzen: ");
			count = conn.executeNativeDelete("DELETE FROM K_AllgAdresse WHERE AllgAdrAdressArt NOT IN (SELECT DISTINCT Bezeichnung FROM K_AdressArt WHERE Bezeichnung IS NOT NULL)");
			logger.logLn(0, count + " Datensätze");
			
			// Setze das Feld FachLehrer aus NULL, falls der entsprechende Eintrag nicht in K_Lehrer auftaucht.
			logger.log("- Setze den FachLehrer bei den Leistungsdaten auf NULL, falls dieser nicht in K_Lehrer vorkommt: ");
			count = conn.executeNativeUpdate("UPDATE SchuelerLeistungsdaten SET FachLehrer = NULL WHERE FachLehrer IS NOT NULL AND FachLehrer NOT IN (SELECT DISTINCT Kuerzel FROM K_Lehrer)");
			logger.logLn(0, count + " Datensätze");
			
			// Setze den Kurs auf Null bei Datensätzen aus den Leistungsdaten, wo die Fach_ID des Kurses nicht der Fach_ID in den Leistungsdaten entspricht 
			logger.log("- Passe SchuelerLeistungsdaten an : Setze den Kurs auf Null, fallse das Fach des Kurses nicht dem Fach in den Leistungsdaten entspricht: ");
			count = conn.executeNativeUpdate("UPDATE SchuelerLeistungsdaten ld SET ld.Kurs_ID = NULL WHERE ld.ID IN " +  
										   "(SELECT l.ID FROM SchuelerLeistungsdaten l JOIN Kurse k ON l.Kurs_ID = k.ID " +
										   "WHERE l.Fach_ID <> k.Fach_ID OR l.KursartAllg <> k.KursartAllg)");
			logger.logLn(0, count + " Datensätze");
	
			// Beuge beim folgenden Update einem Fehler vor...
			logger.log("- Entferne Schülerleistungsdaten, falls kein Kurs gesetzt ist, es aber in dem gleichen Lernabschnitt entsprechende Leistungdaten mit gesetztem Kurseintrag gibt, welcher ggf. entfernt werden muss: ");
			count = conn.executeNativeDelete("DELETE FROM SchuelerLeistungsdaten WHERE Kurs_ID IS NULL AND (Abschnitt_ID, Fach_ID, Kursart) IN " +   
			                               "(SELECT l.Abschnitt_ID, l.Fach_ID, l.Kursart FROM SchuelerLeistungsdaten l JOIN Kurse k ON l.Kurs_ID = k.ID " +
							               "JOIN SchuelerLernabschnittsdaten a ON l.Abschnitt_ID = a.ID " +
							               "WHERE (a.Jahr <> k.Jahr) OR (a.Abschnitt <> k.Abschnitt))");
			logger.logLn(0, count + " Datensätze");
			
			// s.o. / Setze den Kurs auf Null bei Datensätzen aus den Leistungsdaten, wo der Abschnitt beim Lernabschnitt und beim Kurs nicht zusammenpassen  
			logger.log("- Passe SchuelerLeistungsdaten an : Setze den Kurs auf Null, falls der Lernabschnitte nicht zum Lernabschnitt des Kurses passt: ");
			count = conn.executeNativeUpdate("UPDATE SchuelerLeistungsdaten ld SET ld.Kurs_ID = NULL WHERE ld.ID IN " +  
					   "(SELECT l.ID FROM SchuelerLeistungsdaten l JOIN Kurse k ON l.Kurs_ID = k.ID " +
					   "JOIN SchuelerLernabschnittsdaten a ON l.Abschnitt_ID = a.ID " +
					   "WHERE (a.Jahr <> k.Jahr) OR (a.Abschnitt <> k.Abschnitt))");
			logger.logLn(0, count + " Datensätze");
	
			// Setze den Kurs auf Null bei Datensätzen aus den Leistungsdaten, wo ein Kurs im gleichen Abschnitt mehrfach vorkommt  
			logger.log("- Passe SchuelerLeistungsdaten an : Setze den Kurs auf Null, falls ein Kurs im gleichen Lernabschnitte mehrfach vorkommt: ");
			count = conn.executeNativeUpdate("UPDATE SchuelerLeistungsdaten l SET l.Kurs_ID = NULL WHERE l.ID IN " +  
					   "(SELECT min(ID) AS ID FROM SchuelerLeistungsdaten GROUP BY Abschnitt_ID, Kurs_ID HAVING Kurs_ID IS NOT NULL AND count(*) > 1)");
			logger.logLn(0, count + " Datensätze");
			
			// Entferne doppelte Datensätze in Bezug auf die Unique-Constraint für Abschnitt_ID, Fach_ID, Kursart und Kurs_ID aus den Leistungsdaten
			logger.log("- Entferne Leistungsdaten, false die Unique-Constraint für Abschnitt_ID, Fach_ID, Kursart und Kurs_ID verletzt wird: ");
			count = conn.executeNativeDelete("DELETE FROM SchuelerLeistungsdaten l WHERE l.ID IN " +
									 "(SELECT a.ID FROM SchuelerLeistungsdaten a JOIN " + 
									 "(SELECT max(ID) AS maxID, Abschnitt_ID, Fach_ID, Kurs_ID, Kursart FROM SchuelerLeistungsdaten" +
									 "  GROUP BY Abschnitt_ID, Fach_ID, Kurs_ID, Kursart HAVING count(*) > 1) b " +
									 " ON a.Abschnitt_ID = b.Abschnitt_ID AND ((a.Kurs_ID = b.Kurs_ID) OR (a.Kurs_ID IS NULL AND b.Kurs_ID IS NULL))" +
									 " AND ((a.Kursart = b.Kursart) OR (a.Kursart IS NULL AND b.Kursart IS NULL)) AND a.ID <> b.maxID " +
									 ")");
			logger.logLn(0, count + " Datensätze");
			
			// Entferne Datensätze aus der Tabelle für Erzieher mit schulinternen Funktionen, falls diese nicht (mehr) in der DB vorhanden sind.
			logger.log("- Entferne Datensätze aus SchuelerErzFunktion, falls die Erzieher nicht mehr in der Datenbank vorhanden sind: ");
			count = conn.executeNativeDelete("DELETE FROM SchuelerErzFunktion ef WHERE ef.Erzieher_ID NOT IN (SELECT ea.ID FROM SchuelerErzAdr ea)");
			logger.logLn(0, count + " Datensätze");
			
			// Entferne Datensätze aus den Schülerleistungsdaten, wo Schüler einem Kurs mehrfach zugeordnet sind und einem der Einträge keine Note zugeordnet ist
			logger.log("- Entferne Leistungsdaten, wo Schüler einem Kurs mehrfach zugeordnet sind und einem der Einträge keine Note zugeordnet ist: ");
			count = conn.executeNativeDelete("DELETE FROM SchuelerLeistungsdaten WHERE ID IN"
					+ "(SELECT SchuelerLeistungsdaten.ID FROM SchuelerLeistungsdaten JOIN SchuelerLernabschnittsdaten ON "
					+ "SchuelerLeistungsdaten.Abschnitt_ID = SchuelerLernabschnittsdaten.ID AND SchuelerLeistungsdaten.NotenKrz IS NULL AND "
					+ "(SchuelerLernabschnittsdaten.Schueler_ID, SchuelerLeistungsdaten.Kurs_ID) IN "
					+ "(SELECT SchuelerLernabschnittsdaten.Schueler_ID, SchuelerLeistungsdaten.Kurs_ID FROM SchuelerLeistungsdaten JOIN SchuelerLernabschnittsdaten ON "
					+ " SchuelerLeistungsdaten.Abschnitt_ID = SchuelerLernabschnittsdaten.ID AND SchuelerLeistungsdaten.Kurs_ID IS NOT NULL"
					+ " GROUP BY SchuelerLernabschnittsdaten.Schueler_ID, SchuelerLeistungsdaten.Kurs_ID HAVING count(*) > 1))");
			logger.logLn(0, count + " Datensätze");
	
			// Entferne Datensätze bei den SchuelerLernabschnittsdaten bei denen der Foreign-Key-Constraint zu EigeneSchuele_Jahrgaenge verletzt ist.
			logger.log("- Entferne Schüler-Lernabschnitte, die keinem existierenden Jahrgang zugeordnet sind: ");
			count = conn.executeNativeDelete("DELETE FROM SchuelerLernabschnittsdaten WHERE Jahrgang_ID NOT IN (SELECT ID FROM EigeneSchule_Jahrgaenge)");
			logger.logLn(0, count + " Datensätze");
	
			// Entferne Datensätze aus der Tabelle SchuelerLD_PSFachBem, deren Abschnitt_ID nicht in der Tabelle SchuelerLernabschnittsdaten vorhanden ist 
			logger.log("- Entferne Datensätze aus SchuelerLD_PSFachBem, die keinem korrekten Schüler-Lernabschnitt zugeordnet sind: ");
			count = conn.executeNativeDelete("DELETE FROM SchuelerLD_PSFachBem WHERE Abschnitt_ID NOT IN (SELECT ID FROM SchuelerLernabschnittsdaten)");
			logger.logLn(0, count + " Datensätze");
			
			// Entferne doppelte Datensätze bezüglich der Abschnitts-ID in der Tabelle SchuelerLD_PSFachBem, sofern diese nur mit NULL-Werten belegt sind.
			logger.log("- Entferne Datensätze aus SchuelerLD_PSFachBem, die doppelt vorkommen und nur mit NULL-Werten belegt sind: ");
			count = conn.executeNativeDelete("DELETE FROM SchuelerLD_PSFachBem WHERE Abschnitt_ID IN (SELECT Abschnitt_ID FROM SchuelerLD_PSFachBem GROUP BY Abschnitt_ID HAVING count(*) = 2) AND (ASV IS NULL) AND (LELS IS NULL) AND (ESF IS NULL)");
			logger.logLn(0, count + " Datensätze");
			
			// Entferne Datensätze aus der Tabelle K_Ankreuzdaten bei denen außer SchulNrEigner aller Felder mit NULL belegt sind.
			logger.log("- Entferne Datensätze aus K_Ankreuzdaten, bei denen außer SchulNrEigner aller Felder mit NULL belegt sind: ");
			count = conn.executeNativeDelete("DELETE FROM K_Ankreuzdaten WHERE TextStufe1 IS NULL AND TextStufe2 IS NULL AND TextStufe3 IS NULL AND TextStufe4 IS NULL AND TextStufe5 IS NULL AND BezeichnungSONST IS NULL");
			logger.logLn(0, count + " Datensätze");
	
			// Entferne Zuordnungen aus vergangenen Abschnitten aus der Tabelle Kurslehrer, wo leere Kurse aus der Tabelle Kurse vorkommen, d.h. Kurse, welche nicht in den SchuelerLeistungsdaten vorkommen
			logger.log("- Entferne Datensätze aus KursLehrer, bei denen in vergangenen Abschnitten die zugehörigen Kurse leer waren: ");
			count = conn.executeNativeDelete("DELETE FROM KursLehrer WHERE Kurs_ID IN (SELECT ID FROM Kurse WHERE ID NOT IN (SELECT DISTINCT Kurs_ID FROM SchuelerLeistungsdaten WHERE Kurs_ID IS NOT NULL) AND (Kurse.Jahr, Kurse.Abschnitt) NOT IN (SELECT Schuljahr, SchuljahrAbschnitt FROM EigeneSchule))");
			logger.logLn(0, count + " Datensätze");
			
			// Entferne leere Kurse aus vergangenen Abschnitten aus der Tabelle Kurse, d.h. alle Kurse, welche nicht in SchuelerLeistungsdaten vorkommen
			logger.log("- Entferne Datensätze aus Kurse, bei denen diese in vergangenen Abschnitten leer waren: ");
			count = conn.executeNativeDelete("DELETE FROM Kurse WHERE ID NOT IN (SELECT DISTINCT Kurs_ID FROM SchuelerLeistungsdaten WHERE Kurs_ID IS NOT NULL) AND (Kurse.Jahr, Kurse.Abschnitt) NOT IN (SELECT Schuljahr, SchuljahrAbschnitt FROM EigeneSchule)");
			logger.logLn(0, count + " Datensätze");
			
			// Fasse Kurse in den Leistungsdaten zusammen, die laut der Unique-Constraint identisch sind
			logger.log("- Fasse Kurse in den Leistungsdaten zusammen, die laut der Unique-Constraint der Kurs-Tabelle identisch sind: ");
			count = conn.executeNativeUpdate("UPDATE SchuelerLeistungsdaten l " + 
					"SET l.Kurs_ID = (SELECT max(ID) FROM Kurse GROUP BY Jahr, Abschnitt, KurzBez, ASDJahrgang, Fach_ID, KursartAllg, Wochenstd, LehrerKrz, Jahrgaenge HAVING " + 
					"  (Jahr, Abschnitt, KurzBez, ASDJahrgang, Fach_ID, KursartAllg, Wochenstd, LehrerKrz, Jahrgaenge) IN " + 
					"  (SELECT Jahr, Abschnitt, KurzBez, ASDJahrgang, Fach_ID, KursartAllg, Wochenstd, LehrerKrz, Jahrgaenge FROM Kurse WHERE ID = l.Kurs_ID)) " + 
					"WHERE Kurs_ID IN (SELECT a.ID FROM Kurse a JOIN " + 
					"  (SELECT max(ID) AS preserveID, Jahr, Abschnitt, KurzBez, ASDJahrgang, Fach_ID, KursartAllg, Wochenstd, LehrerKrz, Jahrgaenge " + 
					"   FROM Kurse GROUP BY Jahr, Abschnitt, KurzBez, ASDJahrgang, Fach_ID, KursartAllg, Wochenstd, LehrerKrz, Jahrgaenge HAVING count(*) > 1 ) b " + 
					"  ON " + 
					"    ((a.Jahr IS NULL AND b.Jahr IS NULL) OR (a.Jahr = b.Jahr)) AND " + 
					"    ((a.Abschnitt IS NULL AND b.Abschnitt IS NULL) OR (a.Abschnitt = b.Abschnitt)) AND " + 
					"    ((a.KurzBez IS NULL AND b.KurzBez IS NULL) OR (a.KurzBez = b.KurzBez)) AND " + 
					"    ((a.ASDJahrgang IS NULL AND b.ASDJahrgang IS NULL) OR (a.ASDJahrgang = b.ASDJahrgang)) AND " + 
					"    ((a.Fach_ID IS NULL AND b.Fach_ID IS NULL) OR (a.Fach_ID = b.Fach_ID)) AND " + 
					"    ((a.KursartAllg IS NULL AND b.KursartAllg IS NULL) OR (a.KursartAllg = b.KursartAllg)) AND " + 
					"    ((a.Wochenstd IS NULL AND b.Wochenstd IS NULL) OR (a.Wochenstd = b.Wochenstd)) AND " + 
					"    ((a.LehrerKrz IS NULL AND b.LehrerKrz IS NULL) OR (a.LehrerKrz = b.LehrerKrz)) AND " + 
					"    ((a.Jahrgaenge IS NULL AND b.Jahrgaenge IS NULL) OR (a.Jahrgaenge = b.Jahrgaenge)) AND " + 
					"    a.ID <> b.preserveID)");
			logger.logLn(0, count + " Datensätze");
			
			// Entferne alle Kurse bis auf einen Kurs, welche laut der Unique-Constraint identisch sind und zuvor zusammengefasst wurden
			logger.log("- Entferne alle Kurse bis auf einen Kurs, welche laut der Unique-Constraint identisch sind und zuvor zusammengefasst wurden: ");
			count = conn.executeNativeDelete("DELETE FROM Kurse WHERE ID IN (SELECT a.ID FROM Kurse a JOIN " + 
					"  (SELECT max(ID) AS preserveID, Jahr, Abschnitt, KurzBez, ASDJahrgang, Fach_ID, KursartAllg, Wochenstd, LehrerKrz, Jahrgaenge " + 
					"   FROM Kurse GROUP BY Jahr, Abschnitt, KurzBez, ASDJahrgang, Fach_ID, KursartAllg, Wochenstd, LehrerKrz, Jahrgaenge HAVING count(*) > 1 ) b " + 
					"  ON " + 
					"    ((a.Jahr IS NULL AND b.Jahr IS NULL) OR (a.Jahr = b.Jahr)) AND " + 
					"    ((a.Abschnitt IS NULL AND b.Abschnitt IS NULL) OR (a.Abschnitt = b.Abschnitt)) AND " + 
					"    ((a.KurzBez IS NULL AND b.KurzBez IS NULL) OR (a.KurzBez = b.KurzBez)) AND " + 
					"    ((a.ASDJahrgang IS NULL AND b.ASDJahrgang IS NULL) OR (a.ASDJahrgang = b.ASDJahrgang)) AND " + 
					"    ((a.Fach_ID IS NULL AND b.Fach_ID IS NULL) OR (a.Fach_ID = b.Fach_ID)) AND " + 
					"    ((a.KursartAllg IS NULL AND b.KursartAllg IS NULL) OR (a.KursartAllg = b.KursartAllg)) AND " + 
					"    ((a.Wochenstd IS NULL AND b.Wochenstd IS NULL) OR (a.Wochenstd = b.Wochenstd)) AND " + 
					"    ((a.LehrerKrz IS NULL AND b.LehrerKrz IS NULL) OR (a.LehrerKrz = b.LehrerKrz)) AND " + 
					"    ((a.Jahrgaenge IS NULL AND b.Jahrgaenge IS NULL) OR (a.Jahrgaenge = b.Jahrgaenge)) AND " + 
					"    a.ID <> b.preserveID)");
			logger.logLn(0, count + " Datensätze");
			
			// Aktualisiere den Wohnort des Schülers anhand der Tabelle K_Ort
			logger.log("- Aktualisiere den Wohnort des Schülers anhand der Tabelle K_Ort (neue Foreign-Key Constraint): ");
			count = conn.executeNativeUpdate("UPDATE Schueler SET Schueler.OrtAbk = (SELECT Bezeichnung FROM K_Ort WHERE PLZ = Schueler.PLZ) WHERE " + 
					"  ID IN (SELECT s.ID FROM Schueler s JOIN K_Ort o ON (s.PLZ = o.PLZ) AND ((s.OrtAbk IS NULL AND o.Bezeichnung IS NOT NULL) OR (s.OrtAbk IS NOT NULL AND o.Bezeichnung IS NULL) OR (s.OrtAbk <> o.Bezeichnung)))");
			logger.logLn(0, count + " Datensätze");
		}
	}

}
