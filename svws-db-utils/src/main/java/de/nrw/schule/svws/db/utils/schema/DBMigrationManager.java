package de.nrw.schule.svws.db.utils.schema;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;
import java.util.stream.Collectors;

import de.nrw.schule.svws.base.CsvReader;
import de.nrw.schule.svws.config.SVWSKonfiguration;
import de.nrw.schule.svws.core.data.schule.SchulenKatalogEintrag;
import de.nrw.schule.svws.core.logger.LogLevel;
import de.nrw.schule.svws.core.logger.Logger;
import de.nrw.schule.svws.core.types.KursFortschreibungsart;
import de.nrw.schule.svws.core.types.PersonalTyp;
import de.nrw.schule.svws.core.types.SchuelerStatus;
import de.nrw.schule.svws.core.types.schueler.Herkunftsarten;
import de.nrw.schule.svws.core.types.schule.Schulform;
import de.nrw.schule.svws.core.types.schule.Schulgliederung;
import de.nrw.schule.svws.core.utils.AdressenUtils;
import de.nrw.schule.svws.db.Benutzer;
import de.nrw.schule.svws.db.DBConfig;
import de.nrw.schule.svws.db.DBConnectionException;
import de.nrw.schule.svws.db.DBDriver;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.DBException;
import de.nrw.schule.svws.db.dto.MigrationDTOs;
import de.nrw.schule.svws.db.dto.migration.schild.MigrationDTOSchuelerIndividuelleGruppe;
import de.nrw.schule.svws.db.dto.migration.schild.MigrationDTOSchuelerIndividuelleGruppeSchueler;
import de.nrw.schule.svws.db.dto.migration.schild.benutzer.MigrationDTOProtokollLogin;
import de.nrw.schule.svws.db.dto.migration.schild.benutzer.MigrationDTOUsers;
import de.nrw.schule.svws.db.dto.migration.schild.berufskolleg.MigrationDTOFachklassen;
import de.nrw.schule.svws.db.dto.migration.schild.erzieher.MigrationDTOSchuelerErzieherAdresse;
import de.nrw.schule.svws.db.dto.migration.schild.faecher.MigrationDTOFach;
import de.nrw.schule.svws.db.dto.migration.schild.grundschule.MigrationDTOAnkreuzdaten;
import de.nrw.schule.svws.db.dto.migration.schild.grundschule.MigrationDTOKindergarten;
import de.nrw.schule.svws.db.dto.migration.schild.katalog.MigrationDTOKatalogAdressart;
import de.nrw.schule.svws.db.dto.migration.schild.katalog.MigrationDTOKatalogAllgemeineAdresse;
import de.nrw.schule.svws.db.dto.migration.schild.katalog.MigrationDTOKursarten;
import de.nrw.schule.svws.db.dto.migration.schild.katalog.MigrationDTOOrt;
import de.nrw.schule.svws.db.dto.migration.schild.katalog.MigrationDTOSchuleNRW;
import de.nrw.schule.svws.db.dto.migration.schild.kurse.MigrationDTOKurs;
import de.nrw.schule.svws.db.dto.migration.schild.lehrer.MigrationDTOLehrer;
import de.nrw.schule.svws.db.dto.migration.schild.lehrer.MigrationDTOLehrerAbschnittsdaten;
import de.nrw.schule.svws.db.dto.migration.schild.lehrer.MigrationDTOLehrerAnrechnungsstunde;
import de.nrw.schule.svws.db.dto.migration.schild.lehrer.MigrationDTOLehrerDatenschutz;
import de.nrw.schule.svws.db.dto.migration.schild.lehrer.MigrationDTOLehrerEntlastungsstunde;
import de.nrw.schule.svws.db.dto.migration.schild.lehrer.MigrationDTOLehrerFoto;
import de.nrw.schule.svws.db.dto.migration.schild.lehrer.MigrationDTOLehrerFunktion;
import de.nrw.schule.svws.db.dto.migration.schild.lehrer.MigrationDTOLehrerLehramt;
import de.nrw.schule.svws.db.dto.migration.schild.lehrer.MigrationDTOLehrerLehramtBefaehigung;
import de.nrw.schule.svws.db.dto.migration.schild.lehrer.MigrationDTOLehrerLehramtFachrichtung;
import de.nrw.schule.svws.db.dto.migration.schild.lehrer.MigrationDTOLehrerLernplattform;
import de.nrw.schule.svws.db.dto.migration.schild.lehrer.MigrationDTOLehrerMehrleistung;
import de.nrw.schule.svws.db.dto.migration.schild.personengruppen.MigrationDTOPersonengruppen;
import de.nrw.schule.svws.db.dto.migration.schild.personengruppen.MigrationDTOPersonengruppenPersonen;
import de.nrw.schule.svws.db.dto.migration.schild.schueler.MigrationDTOSchueler;
import de.nrw.schule.svws.db.dto.migration.schild.schueler.MigrationDTOSchuelerAllgemeineAdresse;
import de.nrw.schule.svws.db.dto.migration.schild.schueler.MigrationDTOSchuelerDatenschutz;
import de.nrw.schule.svws.db.dto.migration.schild.schueler.MigrationDTOSchuelerFoerderempfehlung;
import de.nrw.schule.svws.db.dto.migration.schild.schueler.MigrationDTOSchuelerFoto;
import de.nrw.schule.svws.db.dto.migration.schild.schueler.MigrationDTOSchuelerGrundschuldaten;
import de.nrw.schule.svws.db.dto.migration.schild.schueler.MigrationDTOSchuelerLeistungsdaten;
import de.nrw.schule.svws.db.dto.migration.schild.schueler.MigrationDTOSchuelerLernabschnittsdaten;
import de.nrw.schule.svws.db.dto.migration.schild.schueler.MigrationDTOSchuelerPSFachBemerkungen;
import de.nrw.schule.svws.db.dto.migration.schild.schueler.abitur.MigrationDTOSchuelerAbiturFach;
import de.nrw.schule.svws.db.dto.migration.schild.schule.MigrationDTOEigeneSchule;
import de.nrw.schule.svws.db.dto.migration.schild.schule.MigrationDTOTeilstandorte;
import de.nrw.schule.svws.db.dto.migration.svws.auth.MigrationDTOCredentials;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;
import de.nrw.schule.svws.db.utils.data.Schule;
import jakarta.persistence.Column;
import jakarta.persistence.PersistenceException;

/**
 * Diese Klasse stellt Methoden zur Verfügung, um ein Schild2-Datenbankschema in  
 * ein SVWS-Datenbank-Schema zu übertragen.
 */
public class DBMigrationManager {
	
	private final DBConfig srcConfig;
	private final DBConfig tgtConfig;
	private final String tgtRootUser;
	private final String tgtRootPW;
	private final int maxUpdateRevision;
	private final boolean devMode;
	private final Integer filterSchulNummer;
	private final Logger logger;

	/// Enthält ggf. einen Fehler-String für einen zuletzt aufgetretenen Fehler 
	private String lastError;

	// Der Source-DB-Schema-Manager, sofern gerade eine Datenbank-Verbindung besteht 
	private DBSchemaManager srcManager = null;
	
	// Der Target-DB-Schema-Manager, sofern gerade eine Datenbank-Verbindung besteht 
	private DBSchemaManager tgtManager = null;
	
	
	// Die Schulnummer der Quelldatenbank, sofern sie schon eingelesen wurde
	private Integer schulNummer = null;
	
	// Die Schulform der Quelldatenbank, sofern sie schon eingelesen wurde
	private Schulform schulform = null;

	// Eine Liste zum Zwischenspeichern der Schüler-IDs, um Datensätze direkt entfernen zu können, wenn sie nicht in der Datenbank vorhanden sind. 
	private HashSet<Long> schuelerIDs = new HashSet<>();

	// Eine Liste zum Zwischenspeichern der User-IDs, um Datensätze direkt entfernen zu können, wenn sie nicht in der Datenbank vorhanden sind. 
	private HashSet<Long> userIDs = new HashSet<>();
	
	// Eine Liste zum Zwischenspeichern der Credetial-IDs, um Datensätze direkt entfernen zu können, wenn sie nicht in der Datenbank vorhanden sind. 
	private HashSet<Long> credentialsIDs = new HashSet<>();

	// Eine Liste zum Zwischenspeichern der Schüler-Lernabschnitts-IDs, um Datensätze direkt entfernen zu können, wenn sie nicht in der Datenbank vorhanden sind. 
	private HashSet<Long> schuelerLernabschnittsIDs = new HashSet<>();
	
	// Eine Liste zum Zwischenspeichern der Fächer-IDs, um Datensätze direkt entfernen zu können, wenn sie nicht in der Datenbank vorhanden sind. 
	private HashSet<Long> faecherIDs = new HashSet<>();
	
	// Eine Liste zum Zwischenspeichern der Schülerleistungsdaten-IDs, um Datensätze direkt entfernen zu können, wenn sie nicht in der Datenbank vorhanden sind. 
	private HashSet<Long> schuelerLeistungsdatenIDs = new HashSet<>();
	
	// Eine Liste zum Zwischenspeichern der Kurs-IDs, um Datensätze direkt entfernen zu können, wenn sie nicht in der Datenbank vorhanden sind. 
	private HashSet<Long> kursIDs = new HashSet<>();

	// Eine Liste zum Zwischenspeichern der Lehrer-IDs, um Datensätze direkt entfernen zu können, wenn sie nicht in der Datenbank vorhanden sind. 
	private HashSet<Long> lehrerIDs = new HashSet<>();

	// Eine Liste zum Zwischenspeichern der Lehrer-Abschnitts-IDs, um Datensätze direkt entfernen zu können, wenn sie nicht in der Datenbank vorhanden sind. 
	private HashSet<Long> lehrerAbschnittsIDs = new HashSet<>();
	
	// Eine Liste der Abschnitt, die in den Abschnittsdaten angelegt wurden als String (z.B. 1905.1)
	private HashSet<String> lehrerAbschnitte = new HashSet<>();
	
	// Eine Liste zum Zwischenspeichern der Adress-IDs, um Datensätze direkten entfernen zu können, wenn sie nicht in der Datenbank vorhanden sind. 
	private HashSet<Long> adressIDs = new HashSet<>();
	
	// Eine Liste zum Zwischenspeichern der Personengruppen-IDs, um Datensätze direkt entfernen zu können, wenn sie nicht in der Datenbank vorhanden sind.
	private HashSet<Long> personengruppenIDs = new HashSet<>();

	// Eine Liste zum Zwischenspeichern der Schülerlisten-IDs, um Datensätze direkt entfernen zu können, wenn sie nicht in der Datenbank vorhanden sind.
	private HashSet<Long> schuelerListenIDs = new HashSet<>();
	
	// Eine Liste zum Zwischenspeichern der Fachklassen-IDs, um Datensätze direkt entfernen zu können, wenn sie nicht in der Datenbank vorhanden sind.
	private HashSet<Long> fachklassenIDs = new HashSet<>();
		
	
	
	/**
	 * Erzuegt eine neue Instanz des DBMigrationManager mit den übergebenen Attributen.  
	 * 
	 * @param srcConfig            die Datenbank-Konfiguration für den Zugriff auf die Schild2-Datenbank
	 * @param tgtConfig            die Datenbank-Konfiguration für den Zugriff auf die SVWS-Server-Datenbank
	 * @param tgtRootUser          der Benutzername eines "root"-Benutzers, der mit den Rechten zur Schemaverwaltung ausgestattet ist
	 * @param tgtRootPW            das root-Kennwort für den Zugriff auf die Zieldatenbank
	 * @param maxUpdateRevision    die Revision, bis zu welcher die Zieldatenbank aktualisiert wird
	 * @param devMode              gibt an, ob auch Schema-Revision erlaubt werden, die nur für Entwickler zur Verfügung stehen
	 * @param schulNr              die Schulnummer, für welche die Daten migriert werden sollen (null, wenn alle Daten gelesen werden sollen). 
	 * @param logger               ein Logger, welcher die Migration loggt.
	 */
	private DBMigrationManager(DBConfig srcConfig, DBConfig tgtConfig, String tgtRootUser, String tgtRootPW, int maxUpdateRevision, boolean devMode, Integer schulNr, Logger logger) {
		this.srcConfig = srcConfig;
		this.tgtConfig = tgtConfig;
		this.tgtRootUser = tgtRootUser;
		this.tgtRootPW = tgtRootPW;
		this.maxUpdateRevision = maxUpdateRevision;
		this.devMode = devMode;
		this.filterSchulNummer = schulNr;
		this.logger = logger;
	}
	

	/**
	 * Diese Methode führt eine Migration von der durch srcConfig beschriebene Schild2-Datenbank in die durch tgtConfig beschriebene SVWS-Server-Datenbank
	 * durch.  
	 * 
	 * @param srcConfig            die Datenbank-Konfiguration für den Zugriff auf die Schild2-Datenbank
	 * @param tgtConfig            die Datenbank-Konfiguration für den Zugriff auf die SVWS-Server-Datenbank
	 * @param tgtRootUser          der Benutzername eines Benutzers, der mit den Rechten zum Verwalten der Datenbankschemata ausgestattet ist.
	 * @param tgtRootPW            das root-Kennwort für den Zugriff auf die Zieldatenbank
	 * @param maxUpdateRevision    die Revision, bis zu welcher die Zieldatenbank aktualisiert wird
	 * @param devMode              gibt an, ob auch Schema-Revision erlaubt werden, die nur für Entwickler zur Verfügung stehen
	 * @param schulNr              die Schulnummer, für welche die Daten migriert werden sollen (null, wenn alle Daten gelesen werden sollen). 
	 * @param logger               ein Logger, welcher die Migration loggt.
	 * 
	 * @return true, falls die Migration erfolgreich durchgeführt wurde.
	 */
	public static boolean migrate(DBConfig srcConfig, DBConfig tgtConfig, String tgtRootUser, String tgtRootPW, int maxUpdateRevision, boolean devMode, Integer schulNr, Logger logger) {
		DBMigrationManager migrationManager = new DBMigrationManager(srcConfig, tgtConfig, tgtRootUser, tgtRootPW, maxUpdateRevision, devMode, schulNr, logger);
		return migrationManager.doMigrate();
	}


	/**
	 * Prüft die Konfiguration für die Ziel-Datenbank und erstellt das Datenbank-Schema.
	 */
	private boolean createTargetSchema() {
		try {
			if (((tgtConfig.getDBDriver() == DBDriver.MARIA_DB) || (tgtConfig.getDBDriver() == DBDriver.MYSQL)) && ("root".equals(tgtConfig.getUsername())))
				throw new DBException("Der Benutzer \"root\" ist kein zulässiger SVWS-Admin-Benutzer für MYSQL / MARIA_DB");
			
			if ((tgtConfig.getDBDriver() == DBDriver.MSSQL) && ("sa".equals(tgtConfig.getUsername())))
				throw new DBException("Der Benutzer \"sa\" ist kein zulässiger SVWS-Admin-Benutzer für MS SQL Server");
			
			if (!DBRootManager.recreateDB(tgtConfig, tgtRootUser, tgtRootPW, logger))
				throw new DBException("Fehler beim Anlegen des Schemas und des Admin-Benutzers");
			return true;
		} catch (DBException e) {
			logger.logLn("-> Migration fehlgeschlagen! (" + e.getMessage() + ")");
			return false;
		}
	}
	
	
	/**
	 * Erstellt den Schema-Manager für die angegebene Datenbank-Konfiguration und -Verbindung.
	 * 
	 * @param cfg     die Datenbank-Konfiguration
	 * @param user    der Datenbank-Benutzer
	 * @param isSrc   gibt für evtl. Fehlermeldungen an, ob es sich um die Verbidung zur Quell- oder Zieldatenbank handelt.
	 * 
	 * @return der Schema-Manager
	 * 
	 * @throws DBException falls ein Fehler beim Erstellen des Schema-Managers auftritt
	 */
	private DBSchemaManager getSchemaManager(DBConfig cfg, Benutzer user, boolean isSrc) throws DBException {
		try (DBEntityManager conn = user.getEntityManager()) {
			if (conn == null) {
				logger.logLn(0, " [Fehler]");
				logger.log(LogLevel.ERROR, "Fehler bei der Erstellung der Datenbank-Verbindung (driver='" + cfg.getDBDriver() + "', location='" + cfg.getDBLocation() + "', user='" + cfg.getUsername() + "')" + System.lineSeparator());
				throw new DBException("Fehler beim Verbinden zur " + (isSrc ? "Quelldatenbank" : "Zieldatenbank"));
			}
			logger.logLn(0, " [OK]");
			logger.log(LogLevel.INFO, "Datenbank-Verbindung erfolgreich aufgebaut (driver='" + cfg.getDBDriver() + "', location='" + cfg.getDBLocation() + "', user='" + cfg.getUsername() + "')" + System.lineSeparator());
		}
		return DBSchemaManager.create(user, true, logger);		
	}
	
	
	/**
	 * Führt die Migration, für welche dieser Migration-Manager konfiguriert wurde durch.
	 * 
	 * @return true, falls die Migration erfolgreich durchgeführt wurde.
	 */
	private boolean doMigrate() {
		boolean success = true;
		long timeStart = System.currentTimeMillis();
		logger.logLn("Migriere von " + srcConfig.getDBDriver() + " (" + srcConfig.getDBLocation() + "/" + srcConfig.getDBSchema() + ") nach " 
	                                 + tgtConfig.getDBDriver() + " (" + tgtConfig.getDBLocation() + "/" + tgtConfig.getDBSchema() + ")");
		logger.modifyIndent(2);
		
		String tgtSchema = tgtConfig.getDBSchema();
		if ((tgtSchema == null) || "".equals(tgtSchema.trim())) {
			logger.logLn("-> Migration fehlgeschlagen! (Ziel-Schemaname darf nicht null oder leer sein)");
			logger.modifyIndent(-2);
			return false;
		}
		if (!SVWSKonfiguration.get().lockSchema(tgtSchema)) {
			logger.logLn("-> Migration fehlgeschlagen! (Ziel-Schema ist aktuell gesperrt und kann daher nicht überschrieben werden)");
			logger.modifyIndent(-2);
			return false;
		}
		
		try {
			success = createTargetSchema();
			if (!success) {
				logger.modifyIndent(-2);
				return false;
			}
			
			logger.log("-> Verbinde zur Quell-Datenbank... ");
			Benutzer srcUser = Benutzer.create(srcConfig); 
			try (DBEntityManager srcConn = srcUser.getEntityManager()) {
				srcManager = getSchemaManager(srcConfig, srcUser, true); 

				logger.log("-> Verbinde zum Ziel-Schema mit dem Datenbank-Test-Benutzer...");
				Benutzer tgtUser = Benutzer.create(tgtConfig);
				try (DBEntityManager tgtConn = tgtUser.getEntityManager()) {
					tgtManager = getSchemaManager(tgtConfig, tgtUser, false);
					
					logger.logLn("-> Erstelle für die Migration in die Ziel-DB ein SVWS-Schema der Revision 0");
					logger.modifyIndent(2);
					boolean result = tgtManager.createSVWSSchema(0, false);
					logger.modifyIndent(-2);
					if (!result) {
						logger.logLn(" [Fehler]");
						throw new DBException("Fehler beim Erstelen des Schemas mit der Revision 0");				
					}
					logger.logLn("[OK]");
					
					try {
						tgtConn.reconnect();
					} catch (@SuppressWarnings("unused") DBConnectionException e) {
						logger.logLn(" [Fehler] Erneuter Verbindungsaufbau zur Zieldatenbank fehlgeschlagen!");
					}
					
					logger.logLn("-> Kopiere die Daten aus der Quell-DB in die Ziel-DB...");
					logger.modifyIndent(2);
					result = copy();
					logger.modifyIndent(-2);
					if (!result) {
						logger.logLn(" [Fehler]");
						throw new DBException("Fehler beim Kopieren der zu migrierenden Daten");				
					}
					logger.logLn("[OK]");
					
					try {
						tgtConn.reconnect();
					} catch (@SuppressWarnings("unused") DBConnectionException e) {
						logger.logLn(" [Fehler] Erneuter Verbindungsaufbau zur Zieldatenbank fehlgeschlagen!");
					}
								
					logger.logLn("-> Überprüfe die in der DB eingetragene Schulform anhand der Statistik-Vorgaben und korrigiere diese ggf. ...");
					logger.modifyIndent(2);
					result = fixSchulform();
					logger.modifyIndent(-2);
					logger.logLn(result ? "[OK]" : "[Fehler]");
					
					logger.logLn("-> Konvertiere die Bilder als Base64-kodiertes Text-Format...");
					logger.modifyIndent(2);
					convertImages();
					logger.modifyIndent(-2);
					logger.logLn(result ? "[OK]" : "[Fehler]");
		
					if (maxUpdateRevision != 0) {
						logger.logLn("-> Aktualisiere die Ziel-DB auf die " + ((maxUpdateRevision < 0) ? "neueste " : "") + "DB-Revision" + ((maxUpdateRevision > 0) ? " " + maxUpdateRevision : "") + "...");
						logger.modifyIndent(2);
						result = tgtManager.updater.update(maxUpdateRevision < 0 ? -1 : maxUpdateRevision, devMode, false);
						logger.modifyIndent(-2);
						if (!result) {
							logger.logLn("[Fehler]");
							throw new DBException("Fehler beim Aktualsieren der Ziel-DB");				
						}			
						logger.logLn("[OK]");
					}
			
					logger.logLn("-> Speicherbelegung (frei/verfügbar/gesamt): " + (Math.round(Runtime.getRuntime().freeMemory() / 10000000.0) / 100.0) + "G/" + (Math.round(Runtime.getRuntime().totalMemory() / 10000000.0) / 100.0) + "G/" + (Math.round(Runtime.getRuntime().maxMemory() / 10000000.0) / 100.0) + "G");
					logger.logLn("-> Migration erfolgreich in " + ((System.currentTimeMillis() - timeStart) / 1000.0) + " Sekunden abgeschlossen.");
				} finally {
					tgtManager = null;
				}
			} finally {
				srcManager = null;
			}
		} catch (DBException e) {
			logger.logLn("-> Migration fehlgeschlagen! (" + e.getMessage() + ")");
			success = false;
		} finally {
			System.gc();
			
			if (!SVWSKonfiguration.get().unlockSchema(tgtSchema)) {
				logger.logLn("-> Migration evtl. fehlgeschlagen! (Fehler beim Freigeben des Datenbank-Schemas. Schema ist nicht gesperrt - dies wird an dieser Stelle nicht erwartet!)");
				success = false;
			}		
		}
		logger.modifyIndent(-2);
		return success;
	}
	

	/**
	 * Überprüft die Schulform im Rahmen der Migration und korrigiert diese gegebenenfalls.
	 * 
	 * @return true, falls die Schulform korrekt war oder korrigiert werden konnte, ansonsten false
	 */
	private boolean fixSchulform() {
		try (DBEntityManager conn = tgtManager.getUser().getEntityManager()) {
			Schule schule = Schule.query(conn);
			logger.logLn("- Schulnummer: " + schule.dto.SchulNr);
			logger.logLn("- Schulform: " + schule.getSchulform().daten.kuerzel);
			List<SchulenKatalogEintrag> katalogSchulen = CsvReader.fromResource("daten/csv/schulver/Schulen.csv", SchulenKatalogEintrag.class);
			SchulenKatalogEintrag dtoSchulver = katalogSchulen.stream().filter(s -> s.SchulNr.equals("" + schule.dto.SchulNr)).findFirst().orElse(null);
			if (dtoSchulver == null) {
				logger.logLn("- Fehler: Schule konnte für die Schul-Nummer " + schule.dto.SchulNr + " nicht im Verzeichnis der Schulen gefunden werden!");
				return false;
			}

			Schulform statSchulform = Schulform.getByNummer(dtoSchulver.SF);
			if (statSchulform != schule.getSchulform()) {
				logger.logLn("- Fehler: Schulform laut Schulverzeichnis: " + statSchulform.daten.kuerzel);
				logger.logLn("- Korrigiere die Schulform in der SVWS-DB...");
				schule.dto.Schulform = statSchulform;
				conn.persist(schule.dto);
			}
			return true;
		}
	}
	
	
	/**
	 * Liest alle Daten aus der Tabelle aus. Die Daten werden anhand
	 * der Schulnummer aus dem Attribut {@link DBMigrationManager.schulNr}
	 * eingeschränkt, sofern dieses nicht null ist.
	 *  
	 * @param tab   die einzulesende Tabelle
	 * 
	 * @return eine Liste mit allen Entitäten der Tabelle
	 */
	private List<?> readAllData(SchemaTabelle tab) {
		Class<?> dtoClass = MigrationDTOs.getFromTableName(tab.name());
		lastError = null;
		
		// Prüfe, ob eine Java-DTO-Klasse definiert wurde. Dies sollte eigentlich der Fall sein...
		String dtoName = tab.getJavaKlasse(0);
		if (dtoName == null) {
			lastError = "Keine Java-DTO-Klasse definiert.";
			return null;
		}
		
		// Prüfe, ob die Tabelle im Schema überhaupt definiert wurde
		DBSchemaStatus status = srcManager.getSchemaStatus();
		if (!status.hasTable(tab.name())) {
			lastError = "Die Tabelle ist im Quell-Schema nicht definiert.";
			return new Vector<>();
		}
		
		// Prüfe, ob alle Spalten auch wirklich vorhanden sind...
		List<String> spaltenSoll = tab.getSpalten(0).stream().map(col -> col.name()).collect(Collectors.toList());
		List<String> spaltenIst = status.filterColumns(tab.name(), spaltenSoll);
		// Falls ja, dann kopiere direkt, sofern keine Schulnummer angegeben ist.
		if ((filterSchulNummer == null) && (spaltenSoll.size() == spaltenIst.size())) {
			// Lese alle Daten aus der Tabelle
			try (DBEntityManager srcConn = srcManager.getUser().getEntityManager()) {
				List<?> entities = srcConn.queryNamed("" + dtoName + ".all" + ((tab.pkSpalten().size() > 0) ? ".migration" : ""), dtoClass).getResultList();
				return entities;
			} catch (PersistenceException e) {
				lastError = e.getMessage(); 
				return null;
			}
		}
	
		// Ansonsten muss ein angepasster SQL-String zusammengesetzt werden und die Daten müssen manuell in das DTO-Objekt übertragen werden...
		try {
			List<Field> fields = Arrays.asList(dtoClass.getDeclaredFields())
					.stream().filter(f -> {
						var col_annotation = f.getAnnotation(Column.class);
						return col_annotation == null ? false : spaltenIst.contains(col_annotation.name()) || spaltenIst.contains(col_annotation.name().toUpperCase());
					})
					.collect(Collectors.toList());
			List<Field> missing_fields = Arrays.asList(dtoClass.getDeclaredFields())
					.stream().filter(f ->  { 
						var col_annotation = f.getAnnotation(Column.class);
					    return col_annotation == null ? false : !spaltenIst.contains(col_annotation.name()) && !spaltenIst.contains(col_annotation.name().toUpperCase());
					})
					.collect(Collectors.toList());
			fields.stream().forEach(f -> f.setAccessible(true));
			missing_fields.stream().forEach(f -> f.setAccessible(true));
			String jpql = "SELECT " + fields.stream().map(f -> "e."+f.getName()).collect(Collectors.joining(",")) + " FROM " + dtoClass.getSimpleName() + " e";
			if (tab.pkSpalten().size() > 0) {
				List<SchemaTabelleSpalte> pkSpalten = tab.pkSpalten().stream().filter(col -> spaltenIst.contains(col.name())).collect(Collectors.toList());
				if (pkSpalten.size() > 0) {
					jpql += " WHERE " + pkSpalten.stream()
							.map(col -> "e." + col.javaAttributName() + " IS NOT NULL")
							.collect(Collectors.joining(" AND "));
				}
				if ((filterSchulNummer != null) && spaltenIst.contains("SchulnrEigner")) {
					jpql += ((pkSpalten.size() > 0) ? " AND " : " WHERE ") +  "(e.SchulnrEigner = " + filterSchulNummer + "";
					if (!"Users".equals(tab.name()) && (!"Logins".equals(tab.name())))
						jpql += " OR e.SchulnrEigner = 0";
					jpql += ")";
				}
			}
			try (DBEntityManager srcConn = srcManager.getUser().getEntityManager()) {
				List<Object[]> entities = srcConn.query(jpql, Object[].class).getResultList();
				Constructor<?> constructor = dtoClass.getDeclaredConstructor();
				constructor.setAccessible(true);
				Vector<Object> list = new Vector<>();
				for (Object[] obj : entities) {
					Object entity = constructor.newInstance();
					int i=0;
					for (Field f : fields)
						f.set(entity, obj[i++]);
					for (Field f : missing_fields)
						f.set(entity, tab.getSpalten(0).stream().filter(col -> col.javaAttributName().equals(f.getName())).findFirst().orElse(null).getDefaultWertConverted());
					list.add(entity);
				}
				return list;
			}
		} catch (PersistenceException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException  e) {
			lastError = e.getMessage();
			return null;
		}
	}

	
	/**
	 * Schreibe die übergebenen Entitäten in die Zieldatenbank. Diese werden
	 * in Bereichen von 10000 Entitäten persistiert. Im Fehlerfall werden
	 * diese Bereiche schrittweise zerlegt und das Schreiben der Entitäten wird
	 * so lange versucht, bis einzelnen Entitäten nicht geschrieben werde können. 
	 * 
	 * @param entities   die zu schreibenden Entitäten
	 */
	private void write(List<?> entities) {
		try (DBEntityManager tgtConn = tgtManager.getUser().getEntityManager()) {
			logger.logLn("- Schreibe " + entities.size() + " Datensätze: ");
			logger.modifyIndent(2);
			// Versuche zunächst in Blöcken von 10000 Datensätzen zu schreiben, diese werden je nach Erfolg später noch unterteilt...
			int write_errors = 0;
			LinkedList<Map.Entry<Integer, Integer>> ranges = new LinkedList<>(); 
			for (int i = 0; i <= ((entities.size() - 1) / 10000); i++) {
				int first = i * 10000;
				int last = (i+1)*10000 - 1;
				if (last >= entities.size())
					last = entities.size() - 1;
				ranges.add(Map.entry(first, last));
			}
			while (!ranges.isEmpty()) {
				Map.Entry<Integer, Integer> range = ranges.removeFirst();
				if (tgtConn.persistRange(entities, range.getKey(), range.getValue())) {
					if (range.getKey().equals(range.getValue())) 
						logger.logLn("Datensatz " + range.getKey() + " erfolgreich geschrieben. (Freier Speicher: " + (Math.round(Runtime.getRuntime().freeMemory() / 10000000.0) / 100.0) + "G/" + (Math.round(Runtime.getRuntime().totalMemory() / 10000000.0) / 100.0) + "G/" + (Math.round(Runtime.getRuntime().maxMemory() / 10000000.0) / 100.0) +  "G)");						
					else 
						logger.logLn("Datensätze " + range.getKey() + "-" + range.getValue() + " erfolgreich geschrieben. (Freier Speicher: " + (Math.round(Runtime.getRuntime().freeMemory() / 10000000.0) / 100.0) + "G/" + (Math.round(Runtime.getRuntime().totalMemory() / 10000000.0) / 100.0) + "G/" + (Math.round(Runtime.getRuntime().maxMemory() / 10000000.0) / 100.0) +  "G)");
				} else {
					if (range.getKey().equals(range.getValue())) { 
						logger.logLn(LogLevel.ERROR, "Datensatz " + range.getKey() + " konnte nicht geschrieben werden - Datensatz wird übersprungen.");
						logger.logLn(LogLevel.ERROR, "[FEHLER] " + entities.get(range.getKey()));
						write_errors++;
					} else {
						logger.logLn("Datensätze " + range.getKey() + "-" + range.getValue() + " konnten nicht geschrieben werden geschrieben - Teile den Block auf und versuche die Teilblöcke zu schreiben.");
						// Teile den Block auf
						int step = (range.getValue() - range.getKey() + 1) / 10;
						if (step < 1)
							step = 1;
						for (int last = range.getValue(); last >= range.getKey(); last -= step) {
							int first = last - step + 1;
							ranges.addFirst(Map.entry(first >= range.getKey() ? first : range.getKey(), last));
						}
					}
				}
			}
			logger.modifyIndent(-2);
			logger.logLn("" + (entities.size() - write_errors) + " Datensätze geschrieben, " + write_errors + " fehlerhafte Datensätze übersprungen.");
			logger.modifyIndent(-2);
		}
	}

	
	/**
	 * Liest die Schulnummer aus der Quelldatenbank ein.
	 * 
	 * @return true, falls die Schulnummer erfolgreich bestimmt wurde
	 */
	private boolean readSchulnummer() {
		logger.logLn("Bestimme die Schulnummer aus EigeneSchule:");
		logger.modifyIndent(2);
		List<?> tmpSchulen = readAllData(Schema.tab_EigeneSchule);
		if ((tmpSchulen == null) || (tmpSchulen.size() <= 0)) {
			logger.logLn("Kein Eintrag in der Tabelle EigeneSchule gefunden. Datenbank kann nicht migriert werden.");
			logger.modifyIndent(-2);
			return false;
		}
		if (tmpSchulen.get(0) instanceof MigrationDTOEigeneSchule schule) {
			schulNummer = (filterSchulNummer == null) ? schule.SchulNr : filterSchulNummer;
		} else {
			logger.logLn("Programmfehler: Datentyp für EigeneSchule fehlerhaft.");
			logger.modifyIndent(-2);
			return false;			
		}
		logger.logLn("Schulnummer: " + schulNummer);
		logger.modifyIndent(-2);
		return true;
	}


	/**
	 * Prüft, ob die Tabelle "EigeneSchule" nur einen gültige Datensatz einer Schule beinhaltet.
	 * Ist dies nicht der Fall, wo wird diese Tabelle ggf. aufgeräumt. 
	 * Bei unterschiedlichen Schulnummern wird die Migration mit einer Fehlermeldung beendet.
	 * 
	 * @param entities   die Entitäten der Tabelle EigeneSchule
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkEigeneSchule(List<MigrationDTOEigeneSchule> entities) {
		if (entities.size() > 1) {
			int pos = 0;
			for (pos = 0; pos < entities.size(); pos++) {
				schulNummer = entities.get(pos).SchulNr;
				if (schulNummer != null)
					break;
			}
			if (schulNummer == null) {
				logger.logLn(LogLevel.ERROR, "Die Quelldatenbank ist fehlerhaft und enthält nur Datensätze mit leerer Schulnummer.");
				return false;
			}
			for (int i = entities.size() - 1; i >= 0; i--) {
				if (i == pos)  // Der erste gültige Datensatz sollte nicht entfernt werden...
					continue;
				MigrationDTOEigeneSchule daten = entities.get(i);
				if (daten.SchulNr == null) {
					logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz mit leerer Schulnummer. Die Quell-Datenbank war fehlerhaft. Das Ergebnis der Migration sollte ausführlich geprüft werden.");
					entities.remove(i);
				} else if (daten.SchulNr.equals(schulNummer)) {
					logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz mit gleicher Schulnummer, wie der erste Datensatz. Die Quell-Datenbank war fehlerhaft. Das Ergebnis der Migration sollte ausführlich geprüft werden.");
					entities.remove(i);							
				} else {
					logger.logLn(LogLevel.ERROR, "Es existieren mehrere Schul-Datensätze mit unterschiedlichen Schulnummern in einem Datenbankschema. Solche Datenbanken werden von der SVWS-DB nicht mehr unterstützt.");
					return false;
				}
			}
		}
		MigrationDTOEigeneSchule daten = entities.get(0);
		// Passe ggf. das die Schulform an
		if (daten.SchulformNr.equals(Schulform.PS.daten.nummer)) {
			daten.Schulform = Schulform.PS.daten.kuerzel;
			daten.SchulformBez = Schulform.PS.daten.bezeichnung;
		} else if (daten.SchulformNr.equals(Schulform.SK.daten.nummer)) {
			daten.Schulform = Schulform.SK.daten.kuerzel;
			daten.SchulformBez = Schulform.SK.daten.bezeichnung;
		} else if (daten.SchulformNr.equals(Schulform.GM.daten.nummer)) {
			daten.Schulform = Schulform.GM.daten.kuerzel;
			daten.SchulformBez = Schulform.GM.daten.bezeichnung;
		} else if (daten.SchulformNr.equals(Schulform.HI.daten.nummer)) {
			daten.Schulform = Schulform.HI.daten.kuerzel;
			daten.SchulformBez = Schulform.HI.daten.bezeichnung;
		} else if (daten.SchulformNr.equals(Schulform.WF.daten.nummer)) {
			daten.Schulform = Schulform.WF.daten.kuerzel;
			daten.SchulformBez = Schulform.WF.daten.bezeichnung;
		}
		// Splitte die Strasseninformation in Name, Hausnummer und Zusatz
		if (daten.Strasse != null) {
			String[] aufgeteilt = AdressenUtils.splitStrasse(daten.Strasse);
			daten.Strassenname = aufgeteilt[0];
			daten.HausNr = aufgeteilt[1];
			daten.HausNrZusatz = aufgeteilt[2];
		}
		if (daten.Schulform != null) {
			schulform = Schulform.getByKuerzel(daten.Schulform);
		}
		return true;
	}

	
	/**
	 * Prüft die Entitäten der Tabelle "EigeneSchule_Teilstandorte".
	 * Hierbei wird sichergestellt, dass die Datensätze zur Schulnummer passen. 
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkEigeneSchuleTeilstandorte(List<MigrationDTOTeilstandorte> entities) {
		if (entities.size() > 1) {
			for (int i = entities.size() - 1; i >= 0; i--) {
				MigrationDTOTeilstandorte daten = entities.get(i);
				boolean isNotSchule = (daten.SchulnrEigner == null) || (schulNummer == null) || (Integer.compare(daten.SchulnrEigner, schulNummer) != 0);
				if (isNotSchule) {
					logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz, da die Schulnummer des Teilstandorts nicht mit der Schulnummer aus EigeneSchule übereinstimmt. Die Quell-Datenbank sollte überprüft werden.");
					entities.remove(i);							
				}
			}
		}
		for (int i = 0; i < entities.size(); i++) {
			MigrationDTOTeilstandorte daten = entities.get(i);
			// Splitte die Strasseninformation in Name, Hausnummer und Zusatz
			if (daten.Strasse != null) {
				String[] aufgeteilt = AdressenUtils.splitStrasse(daten.Strasse);
				daten.Strassenname = aufgeteilt[0];
				daten.HausNr = aufgeteilt[1];
				daten.HausNrZusatz = aufgeteilt[2];
			}
		}
		return true;
	}
	
	
	/**
	 * Prüft die Entitäten der Tabelle "EigeneSchule_Kursarten".
	 * Hierbei wird sichergestellt, dass die Datensätze zur Schulnummer passen. 
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkEigeneSchuleKursarten(List<MigrationDTOKursarten> entities) {
		if (entities.size() > 1) {
			for (int i = entities.size() - 1; i >= 0; i--) {
				MigrationDTOKursarten daten = entities.get(i);
				boolean isNotSchule = (daten.SchulnrEigner == null) || (schulNummer == null) || (Integer.compare(daten.SchulnrEigner, schulNummer) != 0);
				if (isNotSchule) {
					logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz, da die Schulnummer des Kursarten nicht mit der Schulnummer aus EigeneSchule übereinstimmt. Die Quell-Datenbank sollte überprüft werden.");
					entities.remove(i);							
				}
			}
		}
		for (int i = 0; i < entities.size(); i++) {
			MigrationDTOKursarten daten = entities.get(i);
			if (daten.Kursart != null)
				daten.Kursart = mapKursart(daten.Kursart);
			if (daten.KursartAllg != null)
				daten.KursartAllg = mapKursart(daten.KursartAllg);
			if (daten.Kursart != null)
				daten.InternBez = mapKursart(daten.InternBez);
		}
		return true;
	}
	
	
	/**
	 * Prüft die Entitäten der Tabelle "K_Schule".
	 * Hierbei wird überprüft, ob eine negative ID in der DB existiert. 
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkKatalogSchule(List<MigrationDTOSchuleNRW> entities) {
		if (entities.size() > 1) {
			for (int i = entities.size() - 1; i >= 0; i--) {
				MigrationDTOSchuleNRW daten = entities.get(i);
				if (daten.ID < 0) {
					logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz, da die ID negativ ist.");
					entities.remove(i);
				} else if (daten.SchulNr == null) {
					logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz, da die Schulnummer null ist.");
					entities.remove(i);
				}
			}
		}
		for (int i = 0; i < entities.size(); i++) {
			MigrationDTOSchuleNRW daten = entities.get(i);
			// Splitte die Strasseninformation in Name, Hausnummer und Zusatz
			if (daten.Strasse != null) {
				String[] aufgeteilt = AdressenUtils.splitStrasse(daten.Strasse);
				daten.Strassenname = aufgeteilt[0];
				daten.HausNr = aufgeteilt[1];
				daten.HausNrZusatz = aufgeteilt[2];
			}
		}
		return true;
	}

	
	/**
	 * Prüft die Entitäten der Tabelle "SchuelerListe".
	 * Hierbei wird überprüft, ob die Bezeichnung gültig gesetzt ist. 
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkSchuelerListe(List<MigrationDTOSchuelerIndividuelleGruppe> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOSchuelerIndividuelleGruppe daten = entities.get(i);
			if ((daten.Bezeichnung == null) || ("".equals(daten.Bezeichnung.trim()))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Schülerlisten müssen eine Bezeichnung haben.");
				entities.remove(i);						
			} else {
				schuelerListenIDs.add(daten.ID);
			}
		}
		return true;
	}
	
	/**
	 * Prüft die Entitäten der Tabelle "SchuelerListe_Inhalt".
	 * Hierbei wird überprüft, ob der Schülerlisten-Eintrag auch zu einer gültigen Liste
	 * gehört. 
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkSchuelerListeInhalt(List<MigrationDTOSchuelerIndividuelleGruppeSchueler> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOSchuelerIndividuelleGruppeSchueler daten = entities.get(i);
			if ((daten.Liste_ID == null) || (!schuelerListenIDs.contains(daten.Liste_ID))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Es gibt keine Schülerliste mit der angebenen ID in der Datenbank.");
				entities.remove(i);
			}
		}
		return true;
	}


	/**
	 * Prüft die Entitäten der Tabelle "Credentials".
	 * Hierbei wird überprüft, ob ein Benutzername vorhanden ist.
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkCredentials(List<MigrationDTOCredentials> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOCredentials daten = entities.get(i); 
			if ((daten.Benutzername == null) || ("".equals(daten.Benutzername.trim()))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Benutzername darf nicht leer sein.");
				entities.remove(i);
			} else
				credentialsIDs.add(daten.ID);
		}
		return true;
	}			

	
	/**
	 * Prüft die Entitäten der Tabelle "EigeneSchule_Fachklassen".
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkEigeneSchuleFachklassen(List<MigrationDTOFachklassen> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOFachklassen daten = entities.get(i);
			fachklassenIDs.add(daten.ID);
		}
		return true;
	}
	
	/**
	 * Prüft die Entitäten der Tabelle "Users".
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkUsers(List<MigrationDTOUsers> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOUsers daten = entities.get(i); 
			userIDs.add(daten.ID);
		}
		return true;
	}

	/**
	 * Prüft die Entitäten der Tabelle "Logins".
	 * Hierbei wird geprüft, ob der User mit der ID in der DB existiert.
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkLogins(List<MigrationDTOProtokollLogin> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOProtokollLogin daten = entities.get(i);
			if ((daten.LI_UserID == null) || (!userIDs.contains(daten.LI_UserID))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Es gibt keinen User mit der angebenen ID in der Datenbank.");
				entities.remove(i);
			}
		}
		return true;
	}
	
	/**
	 * Prüft die Entitäten der Tabelle "Personengruppen".
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkPersonengruppen(List<MigrationDTOPersonengruppen> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOPersonengruppen daten = entities.get(i); 
			personengruppenIDs.add(daten.ID);
		}
		return true;
	}
	
	/**
	 * Prüft die Entitäten der Tabelle "Personengruppen_Personen".
	 * Hierbei wird geprüft, ob die Personengruppe exisitiert.
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkPersonengruppenPersonen(List<MigrationDTOPersonengruppenPersonen> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOPersonengruppenPersonen daten = entities.get(i);
			if ((daten.Gruppe_ID == null) || (!personengruppenIDs.contains(daten.Gruppe_ID))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz in PersonengruppenPersonen: Es gibt keine Personengruppe mit der angebenen ID in der Datenbank.");
				entities.remove(i);
			}
			// Splitte die Strasseninformation in Name, Hausnummer und Zusatz
			if (daten.PersonStrasse != null) {
				String[] aufgeteilt = AdressenUtils.splitStrasse(daten.PersonStrasse);
				daten.PersonStrassenname = aufgeteilt[0];
				daten.PersonHausNr = aufgeteilt[1];
				daten.PersonHausNrZusatz = aufgeteilt[2];
			}		
		}
		return true;
	}
	
	
	/**
	 * Prüft die Entitäten der Tabelle "SchuelerLernabschnittsdaten".
	 * Hierbei wird geprüft, ob der Schüler exisitiert, der Abschnitt nicht null ist
	 * und es werden ggf. Fachklassen-IDs angepasst. 
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkSchuelerLernabschnittsdaten(List<MigrationDTOSchuelerLernabschnittsdaten> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOSchuelerLernabschnittsdaten daten = entities.get(i);
			if ((daten.Schueler_ID == null) || (!schuelerIDs.contains(daten.Schueler_ID))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz (ID " + daten.ID + "): Es gibt keinen Schüler mit der angebenen ID in der Datenbank.");
				entities.remove(i);
				continue;
			}
			if ((daten.Jahr == null) || (daten.Abschnitt == null)) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz (ID " + daten.ID + "): Lernabschnittsdaten müssen einen gültigen Lernabschnitt haben - null ist unzulässig.");
				entities.remove(i);
				continue;
			}
			if (daten.Jahr < 1990) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz (ID " + daten.ID + "): Lernabschnittsdaten müssen einen gültigen Lernabschnitt haben - Schuljahre vor 1990 werden nicht übernommen.");
				entities.remove(i);
				continue;
			}
			if ((daten.Abschnitt < 1) || (daten.Abschnitt > 4)) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz (ID " + daten.ID + "): Lernabschnittsdaten müssen einen gültigen Lernabschnitt haben - Abschnitte müssen zwischen 1 und 4 liegen.");
				entities.remove(i);
				continue;
			}
			// Prüfe die Fachklasse im Lernabschnitt und setze diese ggf. auf NULL
			if ((daten.Fachklasse_ID != null) && (!fachklassenIDs.contains(daten.Fachklasse_ID))) {
				logger.logLn(LogLevel.ERROR, "Anpassung eines fehlerhaften Datensatzes(ID: " + daten.ID + "): Die Lernabschnittsdaten haben eine ungültige Fachklassen-ID. Diese wird auf null gesetzt.");
				daten.Fachklasse_ID = null;
			}
			// Prüfe die Schulgliederung im Lernabschnitt und setze diese ggf. auf NULL
			if (daten.Schulgliederung != null) {
				if ("".equals(daten.Schulgliederung)) {
					logger.logLn(LogLevel.ERROR, "Anpassung eines fehlerhaften Datensatzes(ID: " + daten.ID + "): Die Lernabschnittsdaten haben einen leeren Schulgliederungs-Eintrag. Dieser wird auf null gesetzt.");
					daten.Schulgliederung = null;
				} else {
					Schulgliederung sgl = Schulgliederung.getByKuerzel(daten.Schulgliederung);
					if ((sgl == null) || (!sgl.hasSchulform(this.schulform))) {
						logger.logLn(LogLevel.ERROR, "Anpassung eines fehlerhaften Datensatzes(ID: " + daten.ID + "): Die Lernabschnittsdaten haben einen ungültigen Schulgliederungs-Eintrag. Dieser wird auf null gesetzt.");
						daten.Schulgliederung = null;
					}
				}
			}
			schuelerLernabschnittsIDs.add(daten.ID);
		}
		return true;
	}

	
	/**
	 * Prüft die Entitäten der Tabelle "SchuelerLeistungsdaten".
	 * Hierbei wird geprüft, ob Abschnitt existiert.
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkSchuelerLeistungsdaten(List<MigrationDTOSchuelerLeistungsdaten> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOSchuelerLeistungsdaten daten = entities.get(i);
			if ((daten.Abschnitt_ID == null) || (!schuelerLernabschnittsIDs.contains(daten.Abschnitt_ID))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Es gibt keinen Lernabschnitt mit der angebenen ID in der Datenbank.");
				entities.remove(i);
			} else {
				schuelerLeistungsdatenIDs.add(daten.ID);
				if (daten.Kursart != null)
					daten.Kursart = mapKursart(daten.Kursart);
				if (daten.KursartAllg != null)
					daten.KursartAllg = mapKursart(daten.KursartAllg);
			}
		}
		return true;
	}
	
	
	/**
	 * Prüft die Entitäten der Tabelle "SchuelerLD_PSFachBem".
	 * Hierbei wird geprüft, ob Abschnitt existiert. Doppelte Einträge für den gleichen Abschnitt werden auch entfernt.
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkSchuelerLD_PSFachBem(List<MigrationDTOSchuelerPSFachBemerkungen> entities) {
		HashSet<Long> localSchuelerLernabschnittsIDs = new HashSet<>();
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOSchuelerPSFachBemerkungen daten = entities.get(i);
			if ((daten.Abschnitt_ID == null) || (!schuelerLernabschnittsIDs.contains(daten.Abschnitt_ID))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Es gibt keinen Lernabschnitt mit der angebenen ID in der Datenbank.");
				entities.remove(i);
			} else if (localSchuelerLernabschnittsIDs.contains(daten.Abschnitt_ID)) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Doppelte Lernabschnitt-IDs sind unzulässig.");
				entities.remove(i);
			} else {
				localSchuelerLernabschnittsIDs.add(daten.Abschnitt_ID);
			}
		}
		return true;
	}
	
	
	/**
	 * Prüft die Entitäten der Tabelle "SchuelerFoerderempfehlungen".
	 * Hierbei wird geprüft, ob Abschnitt existiert. Doppelte Einträge für den gleichen Abschnitt werden auch entfernt.
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkSchuelerFoerderempfehlungen(List<MigrationDTOSchuelerFoerderempfehlung> entities) {
		HashSet<Long> localSchuelerLernabschnittsIDs = new HashSet<>();
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOSchuelerFoerderempfehlung daten = entities.get(i);
			if ((daten.Schueler_ID == null) || (!schuelerIDs.contains(daten.Schueler_ID))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Es gibt keinen Schüler mit der angebenen ID in der Datenbank.");
				entities.remove(i);
			} else if ((daten.Abschnitt_ID == null) || (!schuelerLernabschnittsIDs.contains(daten.Abschnitt_ID))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Es gibt keinen Lernabschnitt mit der angebenen ID in der Datenbank.");
				entities.remove(i);
			} else if (localSchuelerLernabschnittsIDs.contains(daten.Abschnitt_ID)) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Doppelte Lernabschnitt-IDs sind unzulässig.");
				entities.remove(i);
			} else {
				localSchuelerLernabschnittsIDs.add(daten.Abschnitt_ID);
			}
		}
		return true;
	}
	
	
	/**
	 * Prüft die Entitäten der Tabelle "SchuelerAbiFaecher".
	 * Hierbei wird geprüft, ob die Fremdschlüssel auf den Schüler und das Fach gültig ist.
	 *  
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkSchuelerAbiFaecher(List<MigrationDTOSchuelerAbiturFach> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOSchuelerAbiturFach daten = entities.get(i);
			if (daten.Schueler_ID == null) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Schüler-ID darf nicht null sein.");
				entities.remove(i);
			} else if (!schuelerIDs.contains(daten.Schueler_ID)) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Schüler-ID muss in der Tabelle Schueler definiert sein.");
				entities.remove(i);
			} else if (daten.Fach_ID == null) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Fach-ID darf nicht null sein.");
				entities.remove(i);
			} else if (!faecherIDs.contains(daten.Fach_ID)) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Fächer-ID muss in der Tabelle EigeneSchule_Faecher definiert sein.");
				entities.remove(i);
			}
		}
		return true;
	}

	
	/**
	 * Schlüsselt die Kursart um
	 * 
	 * @param kursart   die ursprüngliche Kursart
	 * 
	 * @return die angepasste Kursart
	 */
	private String mapKursart(String kursart) {
		if (kursart == null)
			return null;
		String result = switch (kursart) {
			case "FS" -> "DFG";
			case "FSD" -> "DFK";
			case "FÜK" -> "FUEK";
			case "KMFÖ" -> "KMFOE";
			case "SPFÖ" -> "SPF";
			case "AGK" -> (schulform == Schulform.WB) ? "AGKWB" : "AGK";
			case "WPI" -> (schulform == Schulform.GY) ? "WPI_GY" : "WPI";
			case "FU" -> (schulform == Schulform.S) ? "FU_AUS" : "FU";
			case "E" -> (schulform == Schulform.H) || (schulform == Schulform.R) || (schulform == Schulform.S) || (schulform == Schulform.V) ? "E_H" : "E";
			case "G" -> (schulform == Schulform.H) || (schulform == Schulform.R) || (schulform == Schulform.S) || (schulform == Schulform.V) ? "G_H" : "G";
			default -> kursart;
		};
		if (!kursart.equals(result)) 
			logger.logLn(LogLevel.ERROR, "Korrigiere Datensatz: Die Kursart " + kursart + " wurde auf " + result + " angepasst.");
		return result;
	}
	

	/**
	 * Prüft die Entitäten der Tabelle "Kurse".
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkKurse(List<MigrationDTOKurs> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOKurs daten = entities.get(i); 
			if ((daten.Fortschreibungsart == null) || (!KursFortschreibungsart.isValidKuerzel(daten.Fortschreibungsart))) {
				logger.logLn(LogLevel.ERROR, "Korrigiere Datensatz: Die Fortschreibungsart muss gesetzt sein.");
				daten.Fortschreibungsart = KursFortschreibungsart.KEINE.kuerzel;
			}
			if (daten.KursartAllg != null)
				daten.KursartAllg = mapKursart(daten.KursartAllg);
			kursIDs.add(daten.ID);
		}
		return true;
	}


	/**
	 * Prüft die Entitäten der Tabelle "K_Lehrer".
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkLehrer(List<MigrationDTOLehrer> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOLehrer daten = entities.get(i); 
			if ((daten.PersonTyp == null) || ("".equals(daten.PersonTyp.trim())))
				daten.PersonTyp = PersonalTyp.LEHRKRAFT.kuerzel;
			// Splitte die Strasseninformation in Name, Hausnummer und Zusatz
			if (daten.Strasse != null) {
				String[] aufgeteilt = AdressenUtils.splitStrasse(daten.Strasse);
				daten.Strassenname = aufgeteilt[0];
				daten.HausNr = aufgeteilt[1];
				daten.HausNrZusatz = aufgeteilt[2];
			}
			lehrerIDs.add(daten.ID);
		}
		return true;
	}
				
	/**
	 * Prüft die Entitäten der Tabelle "LehrerAbschnittsdaten".
	 * Hierbei wird geprüft, ob der Lehrer existiert und ob null-Werte beim Abschnitt vorkommen.
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkLehrerAbschnittsdaten(List<MigrationDTOLehrerAbschnittsdaten> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOLehrerAbschnittsdaten daten = entities.get(i);
			if ((daten.Lehrer_ID == null) || (!lehrerIDs.contains(daten.Lehrer_ID))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Es gibt keinen Lehrer mit der angebenen ID in der Datenbank.");
				entities.remove(i);
			} else if ((daten.Jahr == null) || (daten.Abschnitt == null)) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Abschnittsdaten müssen einen gültigen Lernabschnitt haben - null ist unzulässig.");
				entities.remove(i);
			} else {
				lehrerAbschnittsIDs.add(daten.ID);
				lehrerAbschnitte.add("" + daten.Lehrer_ID + "." + daten.Jahr + "." + daten.Abschnitt);
			}
		}
		// Füge eine ID als Primärschlüssel hinzu.
		for (int i = 0; i < entities.size(); i++) {
			MigrationDTOLehrerAbschnittsdaten daten = entities.get(i);
			daten.ID = (long) (i+1);
		}
		return true;
	}

	/**
	 * Prüft die Entitäten der Tabelle "LehrerAnrechnung".
	 * Hierbei wird geprüft, ob der Lehrer existiert, null-Werte beim Abschnitt vorkommen oder 
	 * es für die Kombination Lehrer/Abschnitt keine Abschnittsdaten gibt.
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkLehrerAnrechnung(List<MigrationDTOLehrerAnrechnungsstunde> entities) {
		HashMap<String, MigrationDTOLehrerAnrechnungsstunde> map = new HashMap<>();
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOLehrerAnrechnungsstunde daten = entities.get(i);
			if ((daten.Lehrer_ID == null) || (!lehrerIDs.contains(daten.Lehrer_ID))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Es gibt keinen Lehrer mit der angebenen ID in der Datenbank.");
				entities.remove(i);
			} else if ((daten.Jahr == null) || (daten.Abschnitt == null)) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Abschnittsdaten müssen einen gültigen Lernabschnitt haben - null ist unzulässig.");
				entities.remove(i);
			} else if (!lehrerAbschnitte.contains("" + daten.Lehrer_ID + "." + daten.Jahr + "." + daten.Abschnitt)) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Abschnittsdaten müssen einen gültigen Lernabschnitt haben.");
				entities.remove(i);
			} else {
				// Entferne ggf. Duplikate in Bezug auf die Anrechnungsgründe bei den gleichen Lehrerabschnittsdaten
				String key = "" + daten.Lehrer_ID + "." + daten.Jahr + "." + daten.Abschnitt + "." + daten.AnrechnungsgrundKrz;
				MigrationDTOLehrerAnrechnungsstunde other = map.get(key);
				if (other == null) {
					map.put(key, daten);
				} else {
					other.AnrechnungStd += daten.AnrechnungStd;
					logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Ein Anrechungsgrund darf nur einmal in den Abschnittsdaten vorkommen. Addiere die Anrechnungsstunden auf den vorigen Eintrag.");
					entities.remove(i);
				}
			}
		}
		
		// Füge eine ID als Primärschlüssel hinzu.
		for (int i = 0; i < entities.size(); i++) {
			MigrationDTOLehrerAnrechnungsstunde daten = entities.get(i);
			daten.ID = (long) (i+1);
		}
		return true;
	}
	
	/**
	 * Prüft die Entitäten der Tabelle "LehrerEntlastung".
	 * Hierbei wird geprüft, ob der Lehrer existiert, null-Werte beim Abschnitt vorkommen oder 
	 * es für die Kombination Lehrer/Abschnitt keine Abschnittsdaten gibt.
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkLehrerEntlastung(List<MigrationDTOLehrerEntlastungsstunde> entities) {
		HashMap<String, MigrationDTOLehrerEntlastungsstunde> map = new HashMap<>();
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOLehrerEntlastungsstunde daten = entities.get(i);
			if ((daten.Lehrer_ID == null) || (!lehrerIDs.contains(daten.Lehrer_ID))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Es gibt keinen Lehrer mit der angebenen ID in der Datenbank.");
				entities.remove(i);
			} else if ((daten.Jahr == null) || (daten.Abschnitt == null)) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Abschnittsdaten müssen einen gültigen Lernabschnitt haben - null ist unzulässig.");
				entities.remove(i);
			} else if (!lehrerAbschnitte.contains("" + daten.Lehrer_ID + "." + daten.Jahr + "." + daten.Abschnitt)) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Abschnittsdaten müssen einen gültigen Lernabschnitt haben.");
				entities.remove(i);
			} else {
				// Entferne ggf. Duplikate in Bezug auf die Entlastungsgründe bei den gleichen Lehrerabschnittsdaten
				String key = "" + daten.Lehrer_ID + "." + daten.Jahr + "." + daten.Abschnitt + "." + daten.EntlastungsgrundKrz;
				MigrationDTOLehrerEntlastungsstunde other = map.get(key);
				if (other == null) {
					map.put(key, daten);
				} else {
					other.EntlastungStd += daten.EntlastungStd;
					logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Ein Entlastungsgrund darf nur einmal in den Abschnittsdaten vorkommen. Addiere die Entlastungsstunden auf den vorigen Eintrag.");
					entities.remove(i);
				}
			}
		}
		// Füge eine ID als Primärschlüssel hinzu.
		for (int i = 0; i < entities.size(); i++) {
			MigrationDTOLehrerEntlastungsstunde daten = entities.get(i);
			daten.ID = (long) (i+1);
		}
		return true;
	}
		
	/**
	 * Prüft die Entitäten der Tabelle "LehrerMehrleistung".
	 * Hierbei wird geprüft, ob der Lehrer existiert, null-Werte beim Abschnitt vorkommen oder 
	 * es für die Kombination Lehrer/Abschnitt keine Abschnittsdaten gibt.
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkLehrerMehrleistung(List<MigrationDTOLehrerMehrleistung> entities) {
		HashMap<String, MigrationDTOLehrerMehrleistung> map = new HashMap<>();
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOLehrerMehrleistung daten = entities.get(i);
			if ((daten.Lehrer_ID == null) || (!lehrerIDs.contains(daten.Lehrer_ID))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Es gibt keinen Lehrer mit der angebenen ID in der Datenbank.");
				entities.remove(i);
			} else if ((daten.Jahr == null) || (daten.Abschnitt == null)) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Abschnittsdaten müssen einen gültigen Lernabschnitt haben - null ist unzulässig.");
				entities.remove(i);
			} else if (!lehrerAbschnitte.contains("" + daten.Lehrer_ID + "." + daten.Jahr + "." + daten.Abschnitt)) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Abschnittsdaten müssen einen gültigen Lernabschnitt haben.");
				entities.remove(i);
			} else {
				// Entferne ggf. Duplikate in Bezug auf die Mehrleistungsgründe bei den gleichen Lehrerabschnittsdaten
				String key = "" + daten.Lehrer_ID + "." + daten.Jahr + "." + daten.Abschnitt + "." + daten.MehrleistungsgrundKrz;
				MigrationDTOLehrerMehrleistung other = map.get(key);
				if (other == null) {
					map.put(key, daten);
				} else {
					other.MehrleistungStd += daten.MehrleistungStd;
					logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Ein Mehrleistungsgrund darf nur einmal in den Abschnittsdaten vorkommen. Addiere die Mehrleistungsstunden auf den vorigen Eintrag.");
					entities.remove(i);
				}
			}
		}
		// Füge eine ID als Primärschlüssel hinzu.
		for (int i = 0; i < entities.size(); i++) {
			MigrationDTOLehrerMehrleistung daten = entities.get(i);
			daten.ID = (long) (i+1);
		}
		return true;
	}


	/**
	 * Prüft die Entitäten der Tabelle "LehrerFotos".
	 * Hierbei wird geprüft, ob der zugeordnete Lehrer mit den angegebenen ID existiert.
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkLehrerFoto(List<MigrationDTOLehrerFoto> entities) {
		HashMap<Long, MigrationDTOLehrerFoto> map = new HashMap<>();
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOLehrerFoto daten = entities.get(i);
			if ((daten.Lehrer_ID == null) || (!lehrerIDs.contains(daten.Lehrer_ID))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Es gibt keinen Lehrer mit der angebenen ID in der Datenbank.");
				entities.remove(i);
			} else {
				// Entferne ggf. Duplikate mit gleicher ID
				MigrationDTOLehrerFoto other = map.get(daten.Lehrer_ID);
				if (other == null) {
					map.put(daten.Lehrer_ID, daten);
				} else {
					logger.logLn(LogLevel.ERROR, "Entferne Datensatz: Es ist nur die Speicherung von einem Foto für einen Lehrer in der DB vorgesehen.");
					entities.remove(i);
				}
			}
		}
		return true;
	}
	

	/**
	 * Prüft die Entitäten der Tabelle "LehrerFunktionen".
	 * Hierbei wird eine ID als Primärschlüssel ergänzt.
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkLehrerFunktionen(List<MigrationDTOLehrerFunktion> entities) {
		HashMap<String, MigrationDTOLehrerFunktion> map = new HashMap<>();
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOLehrerFunktion daten = entities.get(i);
			if ((daten.Lehrer_ID == null) || (!lehrerIDs.contains(daten.Lehrer_ID))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Es gibt keinen Lehrer mit der angebenen ID in der Datenbank.");
				entities.remove(i);
			} else if ((daten.Jahr == null) || (daten.Abschnitt == null)) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Abschnittsdaten müssen einen gültigen Lernabschnitt haben - null ist unzulässig.");
				entities.remove(i);
			} else if (!lehrerAbschnitte.contains("" + daten.Lehrer_ID + "." + daten.Jahr + "." + daten.Abschnitt)) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Abschnittsdaten müssen einen gültigen Lernabschnitt haben.");
				entities.remove(i);
			} else {
				// Entferne ggf. Duplikate in Bezug auf die Lehrerfunktion bei den gleichen Lehrerabschnittsdaten
				String key = "" + daten.Lehrer_ID + "." + daten.Jahr + "." + daten.Abschnitt + "." + daten.Funktion_ID;
				MigrationDTOLehrerFunktion other = map.get(key);
				if (other == null) {
					map.put(key, daten);
				} else {
					logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Eine Lehrerfunktion darf nur einmal in den Abschnittsdaten vorkommen.");
					entities.remove(i);
				}
			}
		}
		// Füge eine ID als Primärschlüssel hinzu.
		for (int i = 0; i < entities.size(); i++) {
			MigrationDTOLehrerFunktion daten = entities.get(i);
			daten.ID = (long) (i+1);
		}
		return true;
	}
	

	/**
	 * Prüft die Entitäten der Tabelle "LehrerLehramt".
	 * Hierbei wird geprüft, ob der zugeordnete Lehrer mit den angegebenen ID existiert.
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkLehrerLehramt(List<MigrationDTOLehrerLehramt> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOLehrerLehramt daten = entities.get(i);
			if ((daten.Lehrer_ID == null) || (!lehrerIDs.contains(daten.Lehrer_ID))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Es gibt keinen Lehrer mit der angebenen ID in der Datenbank.");
				entities.remove(i);
			}
		}
		return true;
	}
	

	/**
	 * Prüft die Entitäten der Tabelle "LehrerLehramtFachr".
	 * Hierbei wird geprüft, ob der zugeordnete Lehrer mit den angegebenen ID existiert.
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkLehrerLehramtFachrichtung(List<MigrationDTOLehrerLehramtFachrichtung> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOLehrerLehramtFachrichtung daten = entities.get(i);
			if ((daten.Lehrer_ID == null) || (!lehrerIDs.contains(daten.Lehrer_ID))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Es gibt keinen Lehrer mit der angebenen ID in der Datenbank.");
				entities.remove(i);
			}
		}
		return true;
	}
	

	/**
	 * Prüft die Entitäten der Tabelle "LehrerLehramtLehrbef".
	 * Hierbei wird geprüft, ob der zugeordnete Lehrer mit den angegebenen ID existiert.
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkLehrerLehramtBefaehigung(List<MigrationDTOLehrerLehramtBefaehigung> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOLehrerLehramtBefaehigung daten = entities.get(i);
			if ((daten.Lehrer_ID == null) || (!lehrerIDs.contains(daten.Lehrer_ID))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Es gibt keinen Lehrer mit der angebenen ID in der Datenbank.");
				entities.remove(i);
			}
		}
		return true;
	}
	

	/**
	 * Prüft die Entitäten der Tabelle "LehrerDatenschutz".
	 * Hierbei wird geprüft, ob der zugeordnete Lehrer mit den angegebenen ID existiert.
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkLehrerDatenschutz(List<MigrationDTOLehrerDatenschutz> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOLehrerDatenschutz daten = entities.get(i);
			if ((daten.LehrerID == null) || (!lehrerIDs.contains(daten.LehrerID))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Es gibt keinen Lehrer mit der angebenen ID in der Datenbank.");
				entities.remove(i);
			}
		}
		return true;
	}
	

	/**
	 * Prüft die Entitäten der Tabelle "LehrerLernplattform".
	 * Hierbei wird geprüft, ob der zugeordnete Lehrer mit den angegebenen ID existiert.
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkLehrerLernplattform(List<MigrationDTOLehrerLernplattform> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOLehrerLernplattform daten = entities.get(i);
			if ((daten.LehrerID == null) || (!lehrerIDs.contains(daten.LehrerID))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Es gibt keinen Lehrer mit der angebenen ID in der Datenbank.");
				entities.remove(i);
			}
		}
		return true;
	}
	

	
	
	/**
	 * Prüft die Entitäten der Tabelle "Schueler". 
	 * Fehlerhafte Fachklassen-Einträge werden dabei korrigiert.
	 * Außerdem werden ggf. fehlende GU_IDs generiert.
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkSchueler(List<MigrationDTOSchueler> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOSchueler daten = entities.get(i); 
			// Füge GU_IDs zu der Tabelle Schueler hinzu falls diese NULL sind.
			if ((daten.GU_ID == null) || ("".equals(daten.GU_ID)))
				daten.GU_ID = "{" + UUID.randomUUID().toString() + "}";
			if ((daten.Fachklasse_ID != null) && (!fachklassenIDs.contains(daten.Fachklasse_ID))) {
				logger.logLn(LogLevel.ERROR, "Korrigiere Datensatz: Der Schüler hat eine ungültige Fachklassen-ID. Diese wird auf null gesetzt.");
				daten.Fachklasse_ID = null;
			}
			if (!SchuelerStatus.isValidID(daten.Status)) {
				logger.logLn(LogLevel.ERROR, "Korrigiere Datensatz: Der Schüler-Status is ungültig. Dieser wird auf Neuaufnahme gesetzt. Bitte überprüfen Sie die Neuaufnahmen nach der Migration.");
				daten.Status = SchuelerStatus.NEUAUFNAHME.id;
			}
			// Splitte die Strasseninformation in Name, Hausnummer und Zusatz
			if (daten.Strasse != null) {
				String[] aufgeteilt = AdressenUtils.splitStrasse(daten.Strasse);
				daten.Strassenname = aufgeteilt[0];
				daten.HausNr = aufgeteilt[1];
				daten.HausNrZusatz = aufgeteilt[2];
			}
			// Prüfe das Feld LSSchulform 
			if ((daten.LSSchulform != null) && (daten.LSSchulform.length() > 2))
				daten.LSSchulform = daten.LSSchulform.substring(0, 2);
			// Passe das Feld LSVersetzung an und verwende die ID statt des Statistik-Kürzels in der DB
			if ((daten.LSVersetzung != null)) {
				Herkunftsarten art = switch (daten.LSVersetzung) {
					case "0", "3", "4" -> Herkunftsarten.getByKuerzel("0" + daten.LSVersetzung);
					default -> Herkunftsarten.getByKuerzel(daten.LSVersetzung); 
				};
				daten.LSVersetzung = (art == null) ? null : "" + art.daten.id;
			}
			schuelerIDs.add(daten.ID);
		}
		return true;
	}
	
	/**
	 * Prüft die Entitäten der Tabelle "SchuelerErzAdr". 
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkSchuelerErzieherAdresse(List<MigrationDTOSchuelerErzieherAdresse> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOSchuelerErzieherAdresse daten = entities.get(i); 
			// Splitte die Strasseninformation in Name, Hausnummer und Zusatz
			if (daten.ErzStrasse != null) {
				String[] aufgeteilt = AdressenUtils.splitStrasse(daten.ErzStrasse);
				daten.ErzStrassenname = aufgeteilt[0];
				daten.ErzHausNr = aufgeteilt[1];
				daten.ErzHausNrZusatz = aufgeteilt[2];
			}
			schuelerIDs.add(daten.ID);
		}
		return true;
	}
	
	/**
	 * Prüft die Entitäten der Tabelle "SchuelerDatenschutz". 
	 * Hierbei wird geprüft, ob der Schüler in der DB existiert.
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkSchuelerDatenschutz(List<MigrationDTOSchuelerDatenschutz> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOSchuelerDatenschutz daten = entities.get(i);
			if ((daten.Schueler_ID == null) || (!schuelerIDs.contains(daten.Schueler_ID))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz in SchuelerDatenschutz: Es gibt keinen Schüler mit der angebenen ID in der Datenbank.");
				entities.remove(i);
			}
		}
		return true;
	}
	
	/**
	 * Prüft die Entitäten der Tabelle "SchuelerGSDaten". 
	 * Hierbei wird geprüft, ob der Schüler in der DB existiert.
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkSchuelerGSDaten(List<MigrationDTOSchuelerGrundschuldaten> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOSchuelerGrundschuldaten daten = entities.get(i);
			if ((daten.Schueler_ID == null) || (!schuelerIDs.contains(daten.Schueler_ID))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz in SchuelerGSDaten: Es gibt keinen Schüler mit der angebenen ID in der Datenbank.");
				entities.remove(i);
			}
		}
		return true;
	}
	
	
	/**
	 * Prüft die Entitäten der Tabelle "K_AllgAdresse". 
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkKatalogAllgAdresse(List<MigrationDTOKatalogAllgemeineAdresse> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOKatalogAllgemeineAdresse daten = entities.get(i);
			if (daten.strasse != null) {
				String[] aufgeteilt = AdressenUtils.splitStrasse(daten.strasse);
				daten.strassenname = aufgeteilt[0];
				daten.hausnr = aufgeteilt[1];
				daten.hausnrzusatz = aufgeteilt[2];
			}		
			adressIDs.add(daten.ID);
		}
		return true;
	}
	
	
	/**
	 * Prüft die Entitäten der Tabelle "Schueler_AllgAdr". 
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkSchuelerAllgAdr(List<MigrationDTOSchuelerAllgemeineAdresse> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOSchuelerAllgemeineAdresse daten = entities.get(i);
			if ((daten.Adresse_ID == null) || (!adressIDs.contains(daten.Adresse_ID))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz in Schueler_AllgAdr: Es gibt keine Adresse mit der angebenen ID in der Datenbank.");
				entities.remove(i);
			}
		}
		return true;
	}
	
	/**
	 * Prüft die Entitäten der Tabelle "K_Ort". 
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkKatalogOrt(List<MigrationDTOOrt> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOOrt daten = entities.get(i);
			if ((daten.Bezeichnung == null) || ("".equals(daten.Bezeichnung.trim()))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Eine Bezeichnung fehlt.");
				entities.remove(i);
			}
		}
		return true;
	}
	
	/**
	 * Prüft die Entitäten der Tabelle "K_Ankreuzdaten". 
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private static boolean checkKatalogAnkreuzdaten(List<MigrationDTOAnkreuzdaten> entities) {
		for (int i = 0; i < entities.size(); i++) {
			MigrationDTOAnkreuzdaten daten = entities.get(i);
			daten.ID = (long) (i+1);
		}
		return true;
	}
	
	/**
	 * Prüft die Entitäten der Tabelle "EigeneSchule_Faecher".
	 * Dabei werden alle Fächer mit negativer ID entfernt, z.B. das Pseudofach der Besonderen Lernleistung 
	 * Doppelte Einträge bei der ID werden entfernt.
	 * Fehlerhafte Einträge bei den Leitfächern werden außerdem auf null gesetzt.
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkFaecher(List<MigrationDTOFach> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOFach daten = entities.get(i);
			if (daten.ID == null) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: ID darf nicht null sein.");
				entities.remove(i);
			} else if (daten.ID < 0) {
				logger.logLn("  Entferne Pseudo-Fach mit der Fach-ID " + daten.ID);
				entities.remove(i);
			} else if (faecherIDs.contains(daten.ID)) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Doppelte Fächer-IDs sind unzulässig.");
				entities.remove(i);
			} else {
				faecherIDs.add(daten.ID);
			}
		}
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOFach daten = entities.get(i);
			if ((daten.ProjektKursLeitfach1_ID != null) && (!faecherIDs.contains(daten.ProjektKursLeitfach1_ID))) {
				logger.logLn(LogLevel.ERROR, "Korrigiere fehlerhaften Datensatz: Leitfach1-ID muss eine gültige Fächer-ID sein.");
				daten.ProjektKursLeitfach1_ID = null;
			}
			if ((daten.ProjektKursLeitfach2_ID != null) && (!faecherIDs.contains(daten.ProjektKursLeitfach2_ID))) {
				logger.logLn(LogLevel.ERROR, "Korrigiere fehlerhaften Datensatz: Leitfach2-ID muss eine gültige Fächer-ID sein.");
				daten.ProjektKursLeitfach2_ID = null;
			}
		}
		return true;
	}
	
	
	/**
	 * Prüft die Entitäten der Tabelle "K_Adressart". 
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkKatalogAdressart(List<MigrationDTOKatalogAdressart> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			MigrationDTOKatalogAdressart daten = entities.get(i);
			if ((daten.Bezeichnung == null) || ("".equals(daten.Bezeichnung.trim()))) {
				logger.logLn("  Entferne Katalog-Eintrag mit ID " + daten.ID + ", da dieser keine Bezeichnung hat.");
				entities.remove(i);
			}
		}
		return true;
	}

	
	/**
	 * Prüft die Entitäten der Tabelle "K_Kindergarten". 
	 * 
	 * @param entities   die Entitäten
	 * 
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private static boolean checkKatalogKindergarten(List<MigrationDTOKindergarten> entities) {
		for (int i = 0; i < entities.size(); i++) {
			MigrationDTOKindergarten daten = entities.get(i);
			// Splitte die Strasseninformation in Name, Hausnummer und Zusatz
			if (daten.Strasse != null) {
				String[] aufgeteilt = AdressenUtils.splitStrasse(daten.Strasse);
				daten.Strassenname = aufgeteilt[0];
				daten.HausNr = aufgeteilt[1];
				daten.HausNrZusatz = aufgeteilt[2];
			}
		}
		return true;
	}
	
	
	/**
	 * Prüft die Entitäten auf fehlerhafte Daten, welche dann gefiltert werden. Außerdem 
	 * werden ggf. zusätzliche Informationen ergänzt, welche in der Quelldatenbank so noch
	 * nicht vorhanden sind.
	 * 
	 * @param entities   die anzupassenden Entitäten
	 * 
	 * @return true, falls die Überprüfung erfolgreich war
	 */
	@SuppressWarnings("unchecked")
	private boolean checkData(List<?> entities) {
		Object firstObject = entities.get(0);
		if (firstObject instanceof MigrationDTOEigeneSchule) 	
			return checkEigeneSchule((List<MigrationDTOEigeneSchule>)entities);
		if (firstObject instanceof MigrationDTOTeilstandorte)	
			return checkEigeneSchuleTeilstandorte((List<MigrationDTOTeilstandorte>)entities);
		if (firstObject instanceof MigrationDTOKursarten)
			return checkEigeneSchuleKursarten((List<MigrationDTOKursarten>)entities);
		if (firstObject instanceof MigrationDTOSchuleNRW)		
			return checkKatalogSchule((List<MigrationDTOSchuleNRW>)entities);
		if (firstObject instanceof MigrationDTOSchuelerIndividuelleGruppe) 
			return checkSchuelerListe((List<MigrationDTOSchuelerIndividuelleGruppe>)entities); 
		if (firstObject instanceof MigrationDTOSchuelerIndividuelleGruppeSchueler) 
			return checkSchuelerListeInhalt((List<MigrationDTOSchuelerIndividuelleGruppeSchueler>)entities);
		if (firstObject instanceof MigrationDTOCredentials)
			return checkCredentials((List<MigrationDTOCredentials>)entities);
		if (firstObject instanceof MigrationDTOFachklassen)
			return checkEigeneSchuleFachklassen((List<MigrationDTOFachklassen>)entities);
		if (firstObject instanceof MigrationDTOUsers)
			return checkUsers((List<MigrationDTOUsers>)entities);
		if (firstObject instanceof MigrationDTOProtokollLogin)
			return checkLogins((List<MigrationDTOProtokollLogin>)entities);
		if (firstObject instanceof MigrationDTOPersonengruppen)
			return checkPersonengruppen((List<MigrationDTOPersonengruppen>)entities);
		if (firstObject instanceof MigrationDTOPersonengruppenPersonen)
			return checkPersonengruppenPersonen((List<MigrationDTOPersonengruppenPersonen>)entities);
		if (firstObject instanceof MigrationDTOSchuelerLernabschnittsdaten)
			return checkSchuelerLernabschnittsdaten((List<MigrationDTOSchuelerLernabschnittsdaten>)entities);
		if (firstObject instanceof MigrationDTOSchuelerLeistungsdaten)
			return checkSchuelerLeistungsdaten((List<MigrationDTOSchuelerLeistungsdaten>)entities);
		if (firstObject instanceof MigrationDTOSchuelerPSFachBemerkungen)
			return checkSchuelerLD_PSFachBem((List<MigrationDTOSchuelerPSFachBemerkungen>)entities);
		if (firstObject instanceof MigrationDTOSchuelerFoerderempfehlung)
			return checkSchuelerFoerderempfehlungen((List<MigrationDTOSchuelerFoerderempfehlung>)entities);
		if (firstObject instanceof MigrationDTOSchuelerAbiturFach)
			return checkSchuelerAbiFaecher((List<MigrationDTOSchuelerAbiturFach>)entities);
		if (firstObject instanceof MigrationDTOKurs)
			return checkKurse((List<MigrationDTOKurs>) entities);
		if (firstObject instanceof MigrationDTOLehrer)
			return checkLehrer((List<MigrationDTOLehrer>)entities);
		if (firstObject instanceof MigrationDTOLehrerAbschnittsdaten)
			return checkLehrerAbschnittsdaten((List<MigrationDTOLehrerAbschnittsdaten>)entities);
		if (firstObject instanceof MigrationDTOLehrerAnrechnungsstunde)
			return checkLehrerAnrechnung((List<MigrationDTOLehrerAnrechnungsstunde>)entities);
		if (firstObject instanceof MigrationDTOLehrerEntlastungsstunde)
			return checkLehrerEntlastung((List<MigrationDTOLehrerEntlastungsstunde>)entities);
		if (firstObject instanceof MigrationDTOLehrerMehrleistung)
			return checkLehrerMehrleistung((List<MigrationDTOLehrerMehrleistung>)entities);
		if (firstObject instanceof MigrationDTOLehrerLehramt)
			return checkLehrerLehramt((List<MigrationDTOLehrerLehramt>)entities);
		if (firstObject instanceof MigrationDTOLehrerLehramtBefaehigung)
			return checkLehrerLehramtBefaehigung((List<MigrationDTOLehrerLehramtBefaehigung>)entities);
		if (firstObject instanceof MigrationDTOLehrerLehramtFachrichtung)
			return checkLehrerLehramtFachrichtung((List<MigrationDTOLehrerLehramtFachrichtung>)entities);
		if (firstObject instanceof MigrationDTOLehrerDatenschutz)
			return checkLehrerDatenschutz((List<MigrationDTOLehrerDatenschutz>)entities);
		if (firstObject instanceof MigrationDTOLehrerLernplattform)
			return checkLehrerLernplattform((List<MigrationDTOLehrerLernplattform>)entities);
		if (firstObject instanceof MigrationDTOLehrerFoto)
			return checkLehrerFoto((List<MigrationDTOLehrerFoto>)entities);
		if (firstObject instanceof MigrationDTOLehrerFunktion)
			return checkLehrerFunktionen((List<MigrationDTOLehrerFunktion>)entities);
		if (firstObject instanceof MigrationDTOSchueler)
			return checkSchueler((List<MigrationDTOSchueler>)entities);
		if (firstObject instanceof MigrationDTOSchuelerErzieherAdresse)
			return checkSchuelerErzieherAdresse((List<MigrationDTOSchuelerErzieherAdresse>)entities);
		if (firstObject instanceof MigrationDTOSchuelerDatenschutz)
			return checkSchuelerDatenschutz((List<MigrationDTOSchuelerDatenschutz>)entities);
		if (firstObject instanceof MigrationDTOSchuelerGrundschuldaten)
			return checkSchuelerGSDaten((List<MigrationDTOSchuelerGrundschuldaten>)entities);
		if (firstObject instanceof MigrationDTOKatalogAllgemeineAdresse)
			return checkKatalogAllgAdresse((List<MigrationDTOKatalogAllgemeineAdresse>)entities);
		if (firstObject instanceof MigrationDTOSchuelerAllgemeineAdresse)
			return checkSchuelerAllgAdr((List<MigrationDTOSchuelerAllgemeineAdresse>)entities);
		if (firstObject instanceof MigrationDTOOrt)
			return checkKatalogOrt((List<MigrationDTOOrt>)entities);
		if (firstObject instanceof MigrationDTOAnkreuzdaten)
			return checkKatalogAnkreuzdaten((List<MigrationDTOAnkreuzdaten>)entities);
		if (firstObject instanceof MigrationDTOFach)
			return checkFaecher((List<MigrationDTOFach>)entities);
		if (firstObject instanceof MigrationDTOKatalogAdressart)
			return checkKatalogAdressart((List<MigrationDTOKatalogAdressart>)entities);
		if (firstObject instanceof MigrationDTOKindergarten)
			return checkKatalogKindergarten((List<MigrationDTOKindergarten>)entities);
		return true;
	}
	

	/**
	 * Kopiert die Daten aus dem Quell-Schema in das Ziel-Schema, welches in Revision sein muss.
	 * Dies wird innerhalb dieser Klasse auch so angelegt.
	 * 
	 * @return true, falls die Daten erfolgreich kopiert wurden und sonst false
	 */
	private boolean copy() {
		// Lese die Schulnummer aus
		if (!readSchulnummer())
			return false;
		
		// Durchwandere alle Tabellen in der geeigneten Reihenfolge, so dass Foreign-Key-Constraints erfüllt werden
		for (SchemaTabelle tab : Schema.getTabellen(0)) {
			// Prüfe, ob die Tabelle bei der Migration beachtet werden soll, wenn nicht dann überspringe sie
			if (!tab.migrate())
				continue;

			logger.logLn("Tabelle " + tab.name() + ":");
			logger.modifyIndent(2);
			
			// Lese alle Datensätze aus der Quell-Tabelle
			logger.log("- Lese Datensätze: ");			
			List<?> entities = readAllData(tab);
			if (entities == null) {
				logger.logLn(LogLevel.ERROR, 0, "[FEHLER] - Kann die Datensätze nicht einlesen - Überspringe die Tabelle");
				logger.logLn(LogLevel.ERROR, srcManager.getLastError());
				continue;
			}
			logger.logLn(0, entities.size() + " Datensätze eingelesen (Freier Speicher: " + (Math.round(Runtime.getRuntime().freeMemory() / 10000000.0) / 100.0) + "G/" + (Math.round(Runtime.getRuntime().totalMemory() / 10000000.0) / 100.0) + "G/" + (Math.round(Runtime.getRuntime().maxMemory() / 10000000.0) / 100.0) + "G)");
			
			// Wenn keine Daten vorhanden sind, dann brauchen auch keine geschrieben zu werden...
			if (entities.size() == 0) {
				if ((lastError != null) && (!"".equals(lastError)))
					logger.logLn("  Fehler: " + lastError);
				logger.modifyIndent(-2);				
				continue;
			}

			// Prüfe die Entitäten auf fehlerhafte Daten, welche dann gefiltert werden, und ergänze ggf. zusätzliche Informationen während der Migration
			if (!checkData(entities))
				return false;
			if (entities.size() == 0) {
				logger.modifyIndent(-2);				
				continue;
			}
			
			// Schreibe die Datensätze in die Zieltabelle
			write(entities);
			entities = null;
		}
		return true;
	}


	/**
	 * Konvertiert am Ende der Migration die Bilder in der Zieldatenbank in die Base64-Kodierung.
	 */
	private void convertImages() {
		try (DBEntityManager conn = tgtManager.getUser().getEntityManager()) {
			logger.log("* Tabelle EigeneSchule...");
			List<MigrationDTOEigeneSchule> es_in = conn.queryAll(MigrationDTOEigeneSchule.class);
			List<MigrationDTOEigeneSchule> es_out = es_in.stream()
					.filter(es -> {
						if (es.SchulLogo == null)
							return false;
						es.SchulLogoBase64 = Base64.getEncoder().encodeToString(es.SchulLogo);
						return true;
					}).collect(Collectors.toList());
			conn.persistAll(es_out);
			logger.logLn("" + es_out.size() + " Bilder");
	
			logger.log("* Tabelle LehrerFoto...");
			List<MigrationDTOLehrerFoto> lf_in = conn.queryAll(MigrationDTOLehrerFoto.class);
			List<MigrationDTOLehrerFoto> lf_out = lf_in.stream()
					.filter(lf -> {
						if (lf.Foto == null)
							return false;
						lf.FotoBase64 = Base64.getEncoder().encodeToString(lf.Foto);
						return true;
					}).collect(Collectors.toList());
			conn.persistAll(lf_out);
			logger.logLn("" + lf_out.size() + " Bilder");
			
			logger.log("* Tabelle SchuelerFoto...");
			List<MigrationDTOSchuelerFoto> sf_in = conn.queryAll(MigrationDTOSchuelerFoto.class);
			List<MigrationDTOSchuelerFoto> sf_out = sf_in.stream()
					.filter(sf -> {
						if (sf.Foto == null)
							return false;
						sf.FotoBase64 = Base64.getEncoder().encodeToString(sf.Foto);
						return true;
					}).collect(Collectors.toList());
			conn.persistAll(sf_out);
			logger.logLn("" + sf_out.size() + " Bilder");
		}
	}

}
