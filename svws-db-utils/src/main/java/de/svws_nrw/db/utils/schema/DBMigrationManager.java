package de.svws_nrw.db.utils.schema;

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
import java.util.ArrayList;
import java.util.stream.Collectors;

import de.svws_nrw.base.CsvReader;
import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.data.schule.SchulenKatalogEintrag;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.KursFortschreibungsart;
import de.svws_nrw.core.types.PersonalTyp;
import de.svws_nrw.core.types.SchuelerStatus;
import de.svws_nrw.core.types.schueler.Herkunftsarten;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.core.types.schule.Schulgliederung;
import de.svws_nrw.core.utils.AdressenUtils;
import de.svws_nrw.data.schule.SchulUtils;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBConfig;
import de.svws_nrw.db.DBConnectionException;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.DBException;
import de.svws_nrw.db.dto.MigrationDTOs;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.migration.schild.MigrationDTOSchuelerIndividuelleGruppe;
import de.svws_nrw.db.dto.migration.schild.MigrationDTOSchuelerIndividuelleGruppeSchueler;
import de.svws_nrw.db.dto.migration.schild.benutzer.MigrationDTOProtokollLogin;
import de.svws_nrw.db.dto.migration.schild.benutzer.MigrationDTOUsers;
import de.svws_nrw.db.dto.migration.schild.berufskolleg.MigrationDTOFachklassen;
import de.svws_nrw.db.dto.migration.schild.berufskolleg.MigrationDTOSchuelerBKFach;
import de.svws_nrw.db.dto.migration.schild.erzieher.MigrationDTOErzieherLernplattform;
import de.svws_nrw.db.dto.migration.schild.erzieher.MigrationDTOSchuelerErzieherAdresse;
import de.svws_nrw.db.dto.migration.schild.faecher.MigrationDTOFach;
import de.svws_nrw.db.dto.migration.schild.grundschule.MigrationDTOAnkreuzdaten;
import de.svws_nrw.db.dto.migration.schild.grundschule.MigrationDTOKindergarten;
import de.svws_nrw.db.dto.migration.schild.katalog.MigrationDTOKatalogAdressart;
import de.svws_nrw.db.dto.migration.schild.katalog.MigrationDTOKatalogAllgemeineAdresse;
import de.svws_nrw.db.dto.migration.schild.katalog.MigrationDTOKursarten;
import de.svws_nrw.db.dto.migration.schild.katalog.MigrationDTOOrt;
import de.svws_nrw.db.dto.migration.schild.katalog.MigrationDTOSchuleNRW;
import de.svws_nrw.db.dto.migration.schild.kurse.MigrationDTOKurs;
import de.svws_nrw.db.dto.migration.schild.lehrer.MigrationDTOLehrer;
import de.svws_nrw.db.dto.migration.schild.lehrer.MigrationDTOLehrerAbschnittsdaten;
import de.svws_nrw.db.dto.migration.schild.lehrer.MigrationDTOLehrerAnrechnungsstunde;
import de.svws_nrw.db.dto.migration.schild.lehrer.MigrationDTOLehrerDatenschutz;
import de.svws_nrw.db.dto.migration.schild.lehrer.MigrationDTOLehrerEntlastungsstunde;
import de.svws_nrw.db.dto.migration.schild.lehrer.MigrationDTOLehrerFoto;
import de.svws_nrw.db.dto.migration.schild.lehrer.MigrationDTOLehrerFunktion;
import de.svws_nrw.db.dto.migration.schild.lehrer.MigrationDTOLehrerLehramt;
import de.svws_nrw.db.dto.migration.schild.lehrer.MigrationDTOLehrerLehramtBefaehigung;
import de.svws_nrw.db.dto.migration.schild.lehrer.MigrationDTOLehrerLehramtFachrichtung;
import de.svws_nrw.db.dto.migration.schild.lehrer.MigrationDTOLehrerLernplattform;
import de.svws_nrw.db.dto.migration.schild.lehrer.MigrationDTOLehrerMehrleistung;
import de.svws_nrw.db.dto.migration.schild.personengruppen.MigrationDTOPersonengruppen;
import de.svws_nrw.db.dto.migration.schild.personengruppen.MigrationDTOPersonengruppenPersonen;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOSchueler;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOSchuelerAllgemeineAdresse;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOSchuelerDatenschutz;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOSchuelerFoerderempfehlung;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOSchuelerFoto;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOSchuelerGrundschuldaten;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOSchuelerKAoADaten;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOSchuelerLernplattform;
import de.svws_nrw.db.dto.migration.schild.schueler.MigrationDTOSchuelerPSFachBemerkungen;
import de.svws_nrw.db.dto.migration.schild.schueler.abitur.MigrationDTOSchuelerAbiturFach;
import de.svws_nrw.db.dto.migration.schild.schule.MigrationDTOEigeneSchule;
import de.svws_nrw.db.dto.migration.schild.schule.MigrationDTOTeilstandorte;
import de.svws_nrw.db.dto.migration.svws.auth.MigrationDTOCredentials;
import de.svws_nrw.db.dto.migration.svws.auth.MigrationDTOCredentialsLernplattformen;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import jakarta.persistence.Column;
import jakarta.persistence.PersistenceException;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt Methoden zur Verfügung, um ein Schild2-Datenbankschema in
 * ein SVWS-Datenbank-Schema zu übertragen.
 */
public final class DBMigrationManager {

	private final DBConfig srcConfig;
	private final DBConfig tgtConfig;
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
	private final HashSet<Long> schuelerIDs = new HashSet<>();

	// Eine Liste zum Zwischenspeichern der User-IDs, um Datensätze direkt entfernen zu können, wenn sie nicht in der Datenbank vorhanden sind.
	private final HashSet<Long> userIDs = new HashSet<>();

	// Eine Liste zum Zwischenspeichern der Credential-IDs, um Datensätze direkt entfernen zu können, wenn sie nicht in der Datenbank vorhanden sind.
	private final HashSet<Long> credentialsIDs = new HashSet<>();

	// Eine Liste zum Zwischenspeichern der Credential-IDs bei Lernplattformen, um Datensätze direkt entfernen zu können, wenn sie nicht in der Datenbank vorhanden sind.
	private final HashSet<Long> credentialsLernplattformenIDs = new HashSet<>();

	// Eine Liste zum Zwischenspeichern der Schüler-Lernabschnitts-IDs, um Datensätze direkt entfernen zu können, wenn sie nicht in der Datenbank vorhanden sind.
	private final HashSet<Long> schuelerLernabschnittsIDs = new HashSet<>();

	// Eine Liste zum Zwischenspeichern der Fächer-IDs, um Datensätze direkt entfernen zu können, wenn sie nicht in der Datenbank vorhanden sind.
	private final HashSet<Long> faecherIDs = new HashSet<>();

	// Eine Liste zum Zwischenspeichern der Schülerleistungsdaten-IDs, um Datensätze direkt entfernen zu können, wenn sie nicht in der Datenbank vorhanden sind.
	private final HashSet<Long> schuelerLeistungsdatenIDs = new HashSet<>();

	// Eine Liste zum Zwischenspeichern der Kurs-IDs, um Datensätze direkt entfernen zu können, wenn sie nicht in der Datenbank vorhanden sind.
	private final HashSet<Long> kursIDs = new HashSet<>();

	// Eine Liste zum Zwischenspeichern der Lehrer-IDs, um Datensätze direkt entfernen zu können, wenn sie nicht in der Datenbank vorhanden sind.
	private final HashSet<Long> lehrerIDs = new HashSet<>();

	// Eine Liste zum Zwischenspeichern der Lehrer-Abschnitts-IDs, um Datensätze direkt entfernen zu können, wenn sie nicht in der Datenbank vorhanden sind.
	private final HashSet<Long> lehrerAbschnittsIDs = new HashSet<>();

	// Eine Liste der Abschnitt, die in den Abschnittsdaten angelegt wurden als String (z.B. 1905.1)
	private final HashSet<String> lehrerAbschnitte = new HashSet<>();

	// Eine Liste zum Zwischenspeichern der Adress-IDs, um Datensätze direkten entfernen zu können, wenn sie nicht in der Datenbank vorhanden sind.
	private final HashSet<Long> adressIDs = new HashSet<>();

	// Eine Liste zum Zwischenspeichern der Personengruppen-IDs, um Datensätze direkt entfernen zu können, wenn sie nicht in der Datenbank vorhanden sind.
	private final HashSet<Long> personengruppenIDs = new HashSet<>();

	// Eine Liste zum Zwischenspeichern der Schülerlisten-IDs, um Datensätze direkt entfernen zu können, wenn sie nicht in der Datenbank vorhanden sind.
	private final HashSet<Long> schuelerListenIDs = new HashSet<>();

	// Eine Liste zum Zwischenspeichern der Fachklassen-IDs, um Datensätze direkt entfernen zu können, wenn sie nicht in der Datenbank vorhanden sind.
	private final HashSet<Long> fachklassenIDs = new HashSet<>();

	// Eine Liste zum Zwischenspeichern der Erzieher-IDs, um Datensätze direkt entfernen zu können, wenn sie nicht in der Datenbank vorhanden sind.
	private final HashSet<Long> erzieherIDs = new HashSet<>();


	private static final String strOK = "[OK]";
	private static final String strFehler = "[Fehler]";

	private static final String strFehlerKeinLernabschnitt = "Entferne ungültigen Datensatz: Es gibt keinen Lernabschnitt mit der angebenen ID in der Datenbank.";
	private static final String strFehlerLernabschnittUngueltigNull = "Entferne ungültigen Datensatz: Abschnittsdaten müssen einen gültigen Lernabschnitt haben - null ist unzulässig.";
	private static final String strFehlerLernabschnittUngueltig = "Entferne ungültigen Datensatz: Abschnittsdaten müssen einen gültigen Lernabschnitt haben.";

	private static final String strFehlerKeinLehrer = "Entferne ungültigen Datensatz: Es gibt keinen Lehrer mit der angebenen ID in der Datenbank.";

	private static final String strFehlerKeineCredentials = "Korrigiere fehlerhaften Datensatz: Entferne den Bezug zu den nicht vorhandenen Credentials.";


	/**
	 * Erzuegt eine neue Instanz des DBMigrationManager mit den übergebenen Attributen.
	 *
	 * @param srcConfig            die Datenbank-Konfiguration für den Zugriff auf die Schild2-Datenbank
	 * @param tgtConfig            die Datenbank-Konfiguration für den Zugriff auf die SVWS-Server-Datenbank
	 * @param maxUpdateRevision    die Revision, bis zu welcher die Zieldatenbank aktualisiert wird
	 * @param devMode              gibt an, ob auch Schema-Revision erlaubt werden, die nur für Entwickler zur Verfügung stehen
	 * @param schulNr              die Schulnummer, für welche die Daten migriert werden sollen (null, wenn alle Daten gelesen werden sollen).
	 * @param logger               ein Logger, welcher die Migration loggt.
	 */
	private DBMigrationManager(final DBConfig srcConfig, final DBConfig tgtConfig, final int maxUpdateRevision, final boolean devMode, final Integer schulNr, final Logger logger) {
		this.srcConfig = srcConfig;
		this.tgtConfig = tgtConfig;
		this.maxUpdateRevision = maxUpdateRevision;
		this.devMode = devMode;
		this.filterSchulNummer = schulNr;
		this.logger = logger;
	}


	/**
	 * Erstellt ein neues Schema für die Migration. Für das Erstellen werden root-Rechte benötigt.
	 *
	 * @param tgtConfig            die Datenbank-Konfiguration für den Zugriff auf die SVWS-Server-Datenbank
	 * @param tgtRootUser          der Benutzername eines Benutzers, der mit den Rechten zum Verwalten der Datenbankschemata ausgestattet ist.
	 * @param tgtRootPW            das root-Kennwort für den Zugriff auf die Zieldatenbank
	 * @param logger               ein Logger, welcher das Erstellen loggt.
	 *
	 * @return true, falls das Erstellen erfolgreich durchgeführt wurde.
	 */
	public static boolean createNewTargetSchema(final DBConfig tgtConfig, final String tgtRootUser, final String tgtRootPW, final Logger logger) {
		boolean success = true;
		final String tgtSchema = tgtConfig.getDBSchema();
		logger.logLn("Erstelle Ziel-Schema für " + tgtConfig.getDBDriver() + " (" + tgtConfig.getDBLocation() + "/" + tgtSchema + ")");
		logger.modifyIndent(2);
		try {
			if ((tgtSchema == null) || "".equals(tgtSchema.trim()))
				throw new DBException("Ziel-Schemaname darf nicht null oder leer sein");

			if (!SVWSKonfiguration.get().lockSchema(tgtSchema))
				throw new DBException("Ziel-Schema ist aktuell gesperrt und kann daher nicht überschrieben werden");

			if (((tgtConfig.getDBDriver() == DBDriver.MARIA_DB) || (tgtConfig.getDBDriver() == DBDriver.MYSQL)) && ("root".equals(tgtConfig.getUsername())))
				throw new DBException("Der Benutzer \"root\" ist kein zulässiger SVWS-Admin-Benutzer für MYSQL / MARIA_DB");

			if ((tgtConfig.getDBDriver() == DBDriver.MSSQL) && ("sa".equals(tgtConfig.getUsername())))
				throw new DBException("Der Benutzer \"sa\" ist kein zulässiger SVWS-Admin-Benutzer für MS SQL Server");

			if (!DBRootManager.recreateDB(tgtConfig, tgtRootUser, tgtRootPW, logger))
				throw new DBException("Fehler beim Anlegen des Schemas und des Admin-Benutzers");

		} catch (final DBException e) {
			logger.logLn("-> Erstellen fehlgeschlagen! (%s)".formatted(e.getMessage()));
			success = false;
		} finally {
			if (!SVWSKonfiguration.get().unlockSchema(tgtSchema)) {
				logger.logLn("-> Schema ist nicht gesperrt und konnte daher nicht freigegeben werden.");
				success = false;
			}
		}
		logger.modifyIndent(-2);
		return success;
	}


	/**
	 * Diese Methode führt eine Migration von der durch srcConfig beschriebene Schild2-Datenbank in die durch tgtConfig beschriebene
	 * SVWS-Server-Datenbank durch. Das Ziel-Schema muss dabei bereits exitistieren.
	 *
	 * @param srcConfig            die Datenbank-Konfiguration für den Zugriff auf die Schild2-Datenbank
	 * @param tgtConfig            die Datenbank-Konfiguration für den Zugriff auf die SVWS-Server-Datenbank
	 * @param maxUpdateRevision    die Revision, bis zu welcher die Zieldatenbank aktualisiert wird
	 * @param devMode              gibt an, ob auch Schema-Revision erlaubt werden, die nur für Entwickler zur Verfügung stehen
	 * @param schulNr              die Schulnummer, für welche die Daten migriert werden sollen (null, wenn alle Daten gelesen werden sollen).
	 * @param logger               ein Logger, welcher die Migration loggt.
	 *
	 * @return true, falls die Migration erfolgreich durchgeführt wurde.
	 */
	public static boolean migrateInto(final DBConfig srcConfig, final DBConfig tgtConfig, final int maxUpdateRevision, final boolean devMode, final Integer schulNr, final Logger logger) {
		final Benutzer tgtUser = Benutzer.create(tgtConfig);
		final DBSchemaManager tgtManager = DBSchemaManager.create(tgtUser, true, logger);
		if (!tgtManager.dropSVWSSchema())
			return false;
		final DBMigrationManager migrationManager = new DBMigrationManager(srcConfig, tgtConfig, maxUpdateRevision, devMode, schulNr, logger);
		return migrationManager.doMigrate();
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
	public static boolean migrate(final DBConfig srcConfig, final DBConfig tgtConfig, final String tgtRootUser, final String tgtRootPW, final int maxUpdateRevision, final boolean devMode, final Integer schulNr, final Logger logger) {
		if (!createNewTargetSchema(tgtConfig, tgtRootUser, tgtRootPW, logger))
			return false;
		final DBMigrationManager migrationManager = new DBMigrationManager(srcConfig, tgtConfig, maxUpdateRevision, devMode, schulNr, logger);
		return migrationManager.doMigrate();
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
	private DBSchemaManager getSchemaManager(final DBConfig cfg, final Benutzer user, final boolean isSrc) throws DBException {
		try (DBEntityManager conn = user.getEntityManager()) {
			if (conn == null) {
				logger.logLn(0, " " + strFehler);
				logger.log(LogLevel.ERROR, "Fehler bei der Erstellung der Datenbank-Verbindung (driver='" + cfg.getDBDriver() + "', location='" + cfg.getDBLocation() + "', user='" + cfg.getUsername() + "')" + System.lineSeparator());
				throw new DBException("Fehler beim Verbinden zur " + (isSrc ? "Quelldatenbank" : "Zieldatenbank"));
			}
			logger.logLn(0, " " + strOK);
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
		final long timeStart = System.currentTimeMillis();
		logger.logLn("Migriere von " + srcConfig.getDBDriver() + " (" + srcConfig.getDBLocation() + "/" + srcConfig.getDBSchema() + ") nach "
	                                 + tgtConfig.getDBDriver() + " (" + tgtConfig.getDBLocation() + "/" + tgtConfig.getDBSchema() + ")");
		logger.modifyIndent(2);

		final String tgtSchema = tgtConfig.getDBSchema();
		if ((tgtSchema == null) || "".equals(tgtSchema.trim())) {
			logger.logLn("-> Migration fehlgeschlagen! (Ziel-Schemaname darf nicht null oder leer sein)");
			logger.modifyIndent(-2);
			return false;
		}
		if (!SVWSKonfiguration.get().lockSchema(tgtSchema)) {
			logger.logLn("-> Migration fehlgeschlagen! (Ziel-Schema ist aktuell gesperrt und daher kann nicht migriert werden)");
			logger.modifyIndent(-2);
			return false;
		}

		try {
			logger.log("-> Verbinde zur Quell-Datenbank... ");
			final Benutzer srcUser = Benutzer.create(srcConfig);
			try (DBEntityManager srcConn = srcUser.getEntityManager()) {
				srcManager = getSchemaManager(srcConfig, srcUser, true);

				logger.log("-> Verbinde zum Ziel-Schema...");
				final Benutzer tgtUser = Benutzer.create(tgtConfig);
				try (DBEntityManager tgtConn = tgtUser.getEntityManager()) {
					tgtManager = getSchemaManager(tgtConfig, tgtUser, false);

					logger.logLn("-> Erstelle für die Migration in die Ziel-DB ein SVWS-Schema der Revision 0");
					logger.modifyIndent(2);
					boolean result = tgtManager.createSVWSSchema(0, false);
					logger.modifyIndent(-2);
					if (!result) {
						logger.logLn(" " + strFehler);
						throw new DBException("Fehler beim Erstelen des Schemas mit der Revision 0");
					}
					logger.logLn(strOK);

					try {
						tgtConn.reconnect();
					} catch (@SuppressWarnings("unused") final DBConnectionException e) {
						logger.logLn(" [Fehler] Erneuter Verbindungsaufbau zur Zieldatenbank fehlgeschlagen!");
					}

					logger.logLn("-> Kopiere die Daten aus der Quell-DB in die Ziel-DB...");
					logger.modifyIndent(2);
					result = copy();
					logger.modifyIndent(-2);
					if (!result) {
						logger.logLn(" " + strFehler);
						throw new DBException("Fehler beim Kopieren der zu migrierenden Daten");
					}
					logger.logLn(strOK);

					try {
						tgtConn.reconnect();
					} catch (@SuppressWarnings("unused") final DBConnectionException e) {
						logger.logLn(" [Fehler] Erneuter Verbindungsaufbau zur Zieldatenbank fehlgeschlagen!");
					}

					logger.logLn("-> Überprüfe die in der DB eingetragene Schulform anhand der Statistik-Vorgaben und korrigiere diese ggf. ...");
					logger.modifyIndent(2);
					result = fixSchulform();
					logger.modifyIndent(-2);
					logger.logLn(result ? strOK : strFehler);

					logger.logLn("-> Konvertiere die Bilder als Base64-kodiertes Text-Format...");
					logger.modifyIndent(2);
					convertImages();
					logger.modifyIndent(-2);
					logger.logLn(result ? strOK : strFehler);

					if (maxUpdateRevision != 0) {
						logger.logLn("-> Aktualisiere die Ziel-DB auf die " + ((maxUpdateRevision < 0) ? "neueste " : "") + "DB-Revision" + ((maxUpdateRevision > 0) ? " " + maxUpdateRevision : "") + "...");
						logger.modifyIndent(2);
						result = tgtManager.updater.update(maxUpdateRevision < 0 ? -1 : maxUpdateRevision, devMode, false);
						logger.modifyIndent(-2);
						if (!result) {
							logger.logLn(strFehler);
							throw new DBException("Fehler beim Aktualsieren der Ziel-DB");
						}
						logger.logLn(strOK);
					}

					logger.logLn("-> Speicherbelegung (frei/verfügbar/gesamt): " + (Math.round(Runtime.getRuntime().freeMemory() / 10000000.0) / 100.0) + "G/" + (Math.round(Runtime.getRuntime().totalMemory() / 10000000.0) / 100.0) + "G/" + (Math.round(Runtime.getRuntime().maxMemory() / 10000000.0) / 100.0) + "G");
					logger.logLn("-> Migration erfolgreich in " + ((System.currentTimeMillis() - timeStart) / 1000.0) + " Sekunden abgeschlossen.");
				} finally {
					tgtManager = null;
				}
			} finally {
				srcManager = null;
			}
		} catch (final DBException e) {
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
			final @NotNull DTOEigeneSchule schule = SchulUtils.getDTOSchule(conn);
			logger.logLn("- Schulnummer: " + schule.SchulNr);
			logger.logLn("- Schulform: " + schule.Schulform.daten.kuerzel);
			final List<SchulenKatalogEintrag> katalogSchulen = CsvReader.fromResource("daten/csv/schulver/Schulen.csv", SchulenKatalogEintrag.class);
			final SchulenKatalogEintrag dtoSchulver = katalogSchulen.stream().filter(s -> s.SchulNr.equals("" + schule.SchulNr)).findFirst().orElse(null);
			if (dtoSchulver == null) {
				logger.logLn("- Fehler: Schule konnte für die Schul-Nummer " + schule.SchulNr + " nicht im Verzeichnis der Schulen gefunden werden!");
				return false;
			}

			final Schulform statSchulform = Schulform.getByNummer(dtoSchulver.SF);
			if (statSchulform != schule.Schulform) {
				logger.logLn("- Fehler: Schulform laut Schulverzeichnis: " + statSchulform.daten.kuerzel);
				logger.logLn("- Korrigiere die Schulform in der SVWS-DB...");
				schule.Schulform = statSchulform;
				conn.persist(schule);
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
	private List<?> readAllData(final SchemaTabelle tab) {
		final Class<?> dtoClass = MigrationDTOs.getFromTableName(tab.name());
		lastError = null;

		// Prüfe, ob eine Java-DTO-Klasse definiert wurde. Dies sollte eigentlich der Fall sein...
		final String dtoName = tab.getJavaKlasse(0);
		if (dtoName == null) {
			lastError = "Keine Java-DTO-Klasse definiert.";
			return null;
		}

		// Prüfe, ob die Tabelle im Schema überhaupt definiert wurde
		final DBSchemaStatus status = srcManager.getSchemaStatus();
		if (!status.hasTable(tab.name())) {
			lastError = "Die Tabelle ist im Quell-Schema nicht definiert.";
			return new ArrayList<>();
		}

		// Prüfe, ob alle Spalten auch wirklich vorhanden sind...
		final List<String> spaltenSoll = tab.getSpalten(0).stream().map(col -> col.name()).toList();
		final List<String> spaltenIst = status.filterColumns(tab.name(), spaltenSoll);
		// Falls ja, dann kopiere direkt, sofern keine Schulnummer angegeben ist.
		if ((filterSchulNummer == null) && (spaltenSoll.size() == spaltenIst.size())) {
			// Lese alle Daten aus der Tabelle
			try (DBEntityManager srcConn = srcManager.getUser().getEntityManager()) {
				return srcConn.queryNamed("" + dtoName + ".all" + ((!tab.pkSpalten().isEmpty()) ? ".migration" : ""), dtoClass).getResultList();
			} catch (final PersistenceException e) {
				lastError = e.getMessage();
				return null;
			}
		}

		// Ansonsten muss ein angepasster SQL-String zusammengesetzt werden und die Daten müssen manuell in das DTO-Objekt übertragen werden...
		try {
			final List<Field> fields = Arrays.asList(dtoClass.getDeclaredFields())
					.stream().filter(f -> {
						final var col_annotation = f.getAnnotation(Column.class);
						if (col_annotation == null)
							return false;
						return spaltenIst.contains(col_annotation.name()) || spaltenIst.contains(col_annotation.name().toUpperCase());
					})
					.toList();
			final List<Field> missing_fields = Arrays.asList(dtoClass.getDeclaredFields())
					.stream().filter(f ->  {
						final var col_annotation = f.getAnnotation(Column.class);
						if (col_annotation == null)
							return false;
					    return !spaltenIst.contains(col_annotation.name()) && !spaltenIst.contains(col_annotation.name().toUpperCase());
					})
					.toList();
			fields.stream().forEach(f -> f.setAccessible(true));
			missing_fields.stream().forEach(f -> f.setAccessible(true));
			String jpql = "SELECT " + fields.stream().map(f -> "e." + f.getName()).collect(Collectors.joining(",")) + " FROM " + dtoClass.getSimpleName() + " e";
			if (!tab.pkSpalten().isEmpty()) {
				final List<SchemaTabelleSpalte> pkSpalten = tab.pkSpalten().stream().filter(col -> spaltenIst.contains(col.name())).toList();
				if (!pkSpalten.isEmpty()) {
					jpql += " WHERE " + pkSpalten.stream()
							.map(col -> "e." + col.javaAttributName() + " IS NOT NULL")
							.collect(Collectors.joining(" AND "));
				}
				if ((filterSchulNummer != null) && spaltenIst.contains("SchulnrEigner")) {
					jpql += ((!pkSpalten.isEmpty()) ? " AND " : " WHERE ") +  "(e.SchulnrEigner = " + filterSchulNummer + "";
					if (!"Users".equals(tab.name()) && (!"Logins".equals(tab.name())))
						jpql += " OR e.SchulnrEigner = 0";
					jpql += ")";
				}
			}
			try (DBEntityManager srcConn = srcManager.getUser().getEntityManager()) {
				final List<Object[]> entities = srcConn.query(jpql, Object[].class).getResultList();
				final Constructor<?> constructor = dtoClass.getDeclaredConstructor();
				constructor.setAccessible(true);
				final ArrayList<Object> list = new ArrayList<>();
				for (final Object[] obj : entities) {
					final Object entity = constructor.newInstance();
					int i = 0;
					for (final Field f : fields)
						f.set(entity, obj[i++]);
					for (final Field f : missing_fields) {
						final SchemaTabelleSpalte column = tab.getSpalten(0).stream().filter(col -> col.javaAttributName().equals(f.getName())).findFirst().orElse(null);
						f.set(entity, column == null ? null : column.getDefaultWertConverted());
					}
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
	private void write(final List<?> entities) {
		try (DBEntityManager tgtConn = tgtManager.getUser().getEntityManager()) {
			logger.logLn("- Schreibe " + entities.size() + " Datensätze: ");
			logger.modifyIndent(2);
			// Versuche zunächst in Blöcken von 10000 Datensätzen zu schreiben, diese werden je nach Erfolg später noch unterteilt...
			int write_errors = 0;
			final LinkedList<Map.Entry<Integer, Integer>> ranges = new LinkedList<>();
			for (int i = 0; i <= ((entities.size() - 1) / 10000); i++) {
				final int first = i * 10000;
				int last = (i + 1) * 10000 - 1;
				if (last >= entities.size())
					last = entities.size() - 1;
				ranges.add(Map.entry(first, last));
			}
			while (!ranges.isEmpty()) {
				final Map.Entry<Integer, Integer> range = ranges.removeFirst();
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
							final int first = last - step + 1;
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
		final List<?> tmpSchulen = readAllData(Schema.tab_EigeneSchule);
		if ((tmpSchulen == null) || (tmpSchulen.isEmpty())) {
			logger.logLn("Kein Eintrag in der Tabelle EigeneSchule gefunden. Datenbank kann nicht migriert werden.");
			logger.modifyIndent(-2);
			return false;
		}
		if (tmpSchulen.get(0) instanceof final MigrationDTOEigeneSchule schule) {
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
	private boolean checkEigeneSchule(final List<MigrationDTOEigeneSchule> entities) {
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
				final MigrationDTOEigeneSchule daten = entities.get(i);
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
		final MigrationDTOEigeneSchule daten = entities.get(0);
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
			final String[] aufgeteilt = AdressenUtils.splitStrasse(daten.Strasse);
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
	private boolean checkEigeneSchuleTeilstandorte(final List<MigrationDTOTeilstandorte> entities) {
		if (entities.size() > 1) {
			for (int i = entities.size() - 1; i >= 0; i--) {
				final MigrationDTOTeilstandorte daten = entities.get(i);
				final boolean isNotSchule = (daten.SchulnrEigner == null) || (schulNummer == null) || (Integer.compare(daten.SchulnrEigner, schulNummer) != 0);
				if (isNotSchule) {
					logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz, da die Schulnummer des Teilstandorts nicht mit der Schulnummer aus EigeneSchule übereinstimmt. Die Quell-Datenbank sollte überprüft werden.");
					entities.remove(i);
				}
			}
		}
		for (int i = 0; i < entities.size(); i++) {
			final MigrationDTOTeilstandorte daten = entities.get(i);
			// Splitte die Strasseninformation in Name, Hausnummer und Zusatz
			if (daten.Strasse != null) {
				final String[] aufgeteilt = AdressenUtils.splitStrasse(daten.Strasse);
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
	private boolean checkEigeneSchuleKursarten(final List<MigrationDTOKursarten> entities) {
		if (entities.size() > 1) {
			for (int i = entities.size() - 1; i >= 0; i--) {
				final MigrationDTOKursarten daten = entities.get(i);
				final boolean isNotSchule = (daten.SchulnrEigner == null) || (schulNummer == null) || (Integer.compare(daten.SchulnrEigner, schulNummer) != 0);
				if (isNotSchule) {
					logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz, da die Schulnummer des Kursarten nicht mit der Schulnummer aus EigeneSchule übereinstimmt. Die Quell-Datenbank sollte überprüft werden.");
					entities.remove(i);
				}
			}
		}
		for (int i = 0; i < entities.size(); i++) {
			final MigrationDTOKursarten daten = entities.get(i);
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
	private boolean checkKatalogSchule(final List<MigrationDTOSchuleNRW> entities) {
		if (entities.size() > 1) {
			for (int i = entities.size() - 1; i >= 0; i--) {
				final MigrationDTOSchuleNRW daten = entities.get(i);
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
			final MigrationDTOSchuleNRW daten = entities.get(i);
			// Splitte die Strasseninformation in Name, Hausnummer und Zusatz
			if (daten.Strasse != null) {
				final String[] aufgeteilt = AdressenUtils.splitStrasse(daten.Strasse);
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
	private boolean checkSchuelerListe(final List<MigrationDTOSchuelerIndividuelleGruppe> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOSchuelerIndividuelleGruppe daten = entities.get(i);
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
	private boolean checkSchuelerListeInhalt(final List<MigrationDTOSchuelerIndividuelleGruppeSchueler> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOSchuelerIndividuelleGruppeSchueler daten = entities.get(i);
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
	private boolean checkCredentials(final List<MigrationDTOCredentials> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOCredentials daten = entities.get(i);
			if ((daten.Benutzername == null) || ("".equals(daten.Benutzername.trim()))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Benutzername darf nicht leer sein.");
				entities.remove(i);
			} else
				credentialsIDs.add(daten.ID);
		}
		return true;
	}


	/**
	 * Prüft die Entitäten der Tabelle "CredentialsLernplattformen".
	 * Hierbei wird überprüft, ob die Kombination von Lernplattform-ID und Benutzername eindeutig ist.
	 * Doppelte Vorkommen werden entfernt.
	 *
	 * @param entities   die Entitäten
	 *
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkCredentialsLernplattformen(final List<MigrationDTOCredentialsLernplattformen> entities) {
		final HashSet<String> credsUC1 = new HashSet<>();
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOCredentialsLernplattformen daten = entities.get(i);
			if ((daten.Benutzername == null) || ("".equals(daten.Benutzername.trim()))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Benutzername darf nicht leer sein.");
				entities.remove(i);
				continue;
			}
			final String uc1 = daten.LernplattformID + "-" + daten.Benutzername;
			if (credsUC1.contains(uc1)) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Die Kombination auf Lernplattform und Benutzername ist nicht eindeutig (ID " + daten.ID + ").");
				entities.remove(i);
				continue;
			}
			credsUC1.add(uc1);
			credentialsLernplattformenIDs.add(daten.ID);
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
	private boolean checkEigeneSchuleFachklassen(final List<MigrationDTOFachklassen> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOFachklassen daten = entities.get(i);
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
	private boolean checkUsers(final List<MigrationDTOUsers> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOUsers daten = entities.get(i);
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
	private boolean checkLogins(final List<MigrationDTOProtokollLogin> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOProtokollLogin daten = entities.get(i);
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
	private boolean checkPersonengruppen(final List<MigrationDTOPersonengruppen> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOPersonengruppen daten = entities.get(i);
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
	private boolean checkPersonengruppenPersonen(final List<MigrationDTOPersonengruppenPersonen> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOPersonengruppenPersonen daten = entities.get(i);
			if ((daten.Gruppe_ID == null) || (!personengruppenIDs.contains(daten.Gruppe_ID))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz in PersonengruppenPersonen: Es gibt keine Personengruppe mit der angebenen ID in der Datenbank.");
				entities.remove(i);
			}
			// Splitte die Strasseninformation in Name, Hausnummer und Zusatz
			if (daten.PersonStrasse != null) {
				final String[] aufgeteilt = AdressenUtils.splitStrasse(daten.PersonStrasse);
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
	private boolean checkSchuelerLernabschnittsdaten(final List<MigrationDTOSchuelerLernabschnittsdaten> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOSchuelerLernabschnittsdaten daten = entities.get(i);
			if ((daten.Schueler_ID == null) || (!schuelerIDs.contains(daten.Schueler_ID))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz (ID %d): Es gibt keinen Schüler mit der angebenen ID in der Datenbank.".formatted(daten.ID));
				entities.remove(i);
				continue;
			}
			if ((daten.Jahr == null) || (daten.Abschnitt == null)) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz (ID %d): Lernabschnittsdaten müssen einen gültigen Lernabschnitt haben - null ist unzulässig.".formatted(daten.ID));
				entities.remove(i);
				continue;
			}
			if (daten.Jahr < 1990) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz (ID %d): Lernabschnittsdaten müssen einen gültigen Lernabschnitt haben - Schuljahre vor 1990 werden nicht übernommen.".formatted(daten.ID));
				entities.remove(i);
				continue;
			}
			if ((daten.Abschnitt < 1) || (daten.Abschnitt > 4)) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz (ID %d): Lernabschnittsdaten müssen einen gültigen Lernabschnitt haben - Abschnitte müssen zwischen 1 und 4 liegen.".formatted(daten.ID));
				entities.remove(i);
				continue;
			}
			// Prüfe die Fachklasse im Lernabschnitt und setze diese ggf. auf NULL
			if ((daten.Fachklasse_ID != null) && (!fachklassenIDs.contains(daten.Fachklasse_ID))) {
				logger.logLn(LogLevel.ERROR, "Anpassung eines fehlerhaften Datensatzes(ID: %d): Die Lernabschnittsdaten haben eine ungültige Fachklassen-ID. Diese wird auf null gesetzt.".formatted(daten.ID));
				daten.Fachklasse_ID = null;
			}
			// Prüfe die Schulgliederung im Lernabschnitt und setze diese ggf. auf NULL
			if (daten.Schulgliederung != null) {
				if ("".equals(daten.Schulgliederung)) {
					logger.logLn(LogLevel.ERROR, "Anpassung eines fehlerhaften Datensatzes(ID: %d): Die Lernabschnittsdaten haben einen leeren Schulgliederungs-Eintrag. Dieser wird auf null gesetzt.".formatted(daten.ID));
					daten.Schulgliederung = null;
				} else {
					final Schulgliederung sgl = Schulgliederung.getByKuerzel(daten.Schulgliederung);
					if ((sgl == null) || (!sgl.hasSchulform(this.schulform))) {
						logger.logLn(LogLevel.ERROR, "Anpassung eines fehlerhaften Datensatzes(ID: %d): Die Lernabschnittsdaten haben einen ungültigen Schulgliederungs-Eintrag. Dieser wird auf null gesetzt.".formatted(daten.ID));
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
	private boolean checkSchuelerLeistungsdaten(final List<MigrationDTOSchuelerLeistungsdaten> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOSchuelerLeistungsdaten daten = entities.get(i);
			if ((daten.Abschnitt_ID == null) || (!schuelerLernabschnittsIDs.contains(daten.Abschnitt_ID))) {
				logger.logLn(LogLevel.ERROR, strFehlerKeinLernabschnitt);
				entities.remove(i);
				continue;
			}
			if (!faecherIDs.contains(daten.Fach_ID)) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz (ID %d): Fächer-ID (hier %d) muss in der Tabelle EigeneSchule_Faecher definiert sein.".formatted(daten.ID, daten.Fach_ID));
				entities.remove(i);
				continue;
			}
			schuelerLeistungsdatenIDs.add(daten.ID);
			if (daten.Kursart != null)
				daten.Kursart = mapKursart(daten.Kursart);
			if (daten.KursartAllg != null)
				daten.KursartAllg = mapKursart(daten.KursartAllg);
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
	private boolean checkSchuelerLD_PSFachBem(final List<MigrationDTOSchuelerPSFachBemerkungen> entities) {
		final HashSet<Long> localSchuelerLernabschnittsIDs = new HashSet<>();
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOSchuelerPSFachBemerkungen daten = entities.get(i);
			if ((daten.Abschnitt_ID == null) || (!schuelerLernabschnittsIDs.contains(daten.Abschnitt_ID))) {
				logger.logLn(LogLevel.ERROR, strFehlerKeinLernabschnitt);
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
	 * Prüft die Entitäten der Tabelle "SchuelerBKFaecher".
	 * Hierbei wird geprüft, ob Abschnitt existiert.
	 *
	 * @param entities   die Entitäten
	 *
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkSchuelerBKFaecher(final List<MigrationDTOSchuelerBKFach> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOSchuelerBKFach daten = entities.get(i);
			if ((daten.Schueler_ID == null) || (!schuelerIDs.contains(daten.Schueler_ID))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz (ID %d): Es gibt keinen Schüler mit der angebenen ID in der Datenbank.".formatted(daten.ID));
				entities.remove(i);
				continue;
			}
			if (!faecherIDs.contains(daten.Fach_ID)) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Fächer-ID muss in der Tabelle EigeneSchule_Faecher definiert sein.");
				entities.remove(i);
				continue;
			}
			if (daten.ID < 0) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Die ID darf nicht kleiner als 0 sein.");
				entities.remove(i);
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
	private boolean checkSchuelerFoerderempfehlungen(final List<MigrationDTOSchuelerFoerderempfehlung> entities) {
		final HashSet<Long> localSchuelerLernabschnittsIDs = new HashSet<>();
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOSchuelerFoerderempfehlung daten = entities.get(i);
			if ((daten.Schueler_ID == null) || (!schuelerIDs.contains(daten.Schueler_ID))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Es gibt keinen Schüler mit der angebenen ID in der Datenbank.");
				entities.remove(i);
			} else if ((daten.Abschnitt_ID == null) || (!schuelerLernabschnittsIDs.contains(daten.Abschnitt_ID))) {
				logger.logLn(LogLevel.ERROR, strFehlerKeinLernabschnitt);
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
	 * @param entities   die zu prüfenden DTOs
	 *
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkSchuelerAbiFaecher(final List<MigrationDTOSchuelerAbiturFach> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOSchuelerAbiturFach daten = entities.get(i);
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
	private String mapKursart(final String kursart) {
		if (kursart == null)
			return null;
		final String result = switch (kursart) {
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
	private boolean checkKurse(final List<MigrationDTOKurs> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOKurs daten = entities.get(i);
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
	private boolean checkLehrer(final List<MigrationDTOLehrer> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOLehrer daten = entities.get(i);
			if ((daten.PersonTyp == null) || ("".equals(daten.PersonTyp.trim())))
				daten.PersonTyp = PersonalTyp.LEHRKRAFT.kuerzel;
			// Splitte die Strasseninformation in Name, Hausnummer und Zusatz
			if (daten.Strasse != null) {
				final String[] aufgeteilt = AdressenUtils.splitStrasse(daten.Strasse);
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
	private boolean checkLehrerAbschnittsdaten(final List<MigrationDTOLehrerAbschnittsdaten> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOLehrerAbschnittsdaten daten = entities.get(i);
			if ((daten.Lehrer_ID == null) || (!lehrerIDs.contains(daten.Lehrer_ID))) {
				logger.logLn(LogLevel.ERROR, strFehlerKeinLehrer);
				entities.remove(i);
			} else if ((daten.Jahr == null) || (daten.Abschnitt == null)) {
				logger.logLn(LogLevel.ERROR, strFehlerLernabschnittUngueltigNull);
				entities.remove(i);
			} else {
				lehrerAbschnittsIDs.add(daten.ID);
				lehrerAbschnitte.add("" + daten.Lehrer_ID + "." + daten.Jahr + "." + daten.Abschnitt);
			}
		}
		// Füge eine ID als Primärschlüssel hinzu.
		for (int i = 0; i < entities.size(); i++) {
			final MigrationDTOLehrerAbschnittsdaten daten = entities.get(i);
			daten.ID = (long) (i + 1);
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
	private boolean checkLehrerAnrechnung(final List<MigrationDTOLehrerAnrechnungsstunde> entities) {
		final HashMap<String, MigrationDTOLehrerAnrechnungsstunde> map = new HashMap<>();
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOLehrerAnrechnungsstunde daten = entities.get(i);
			if ((daten.Lehrer_ID == null) || (!lehrerIDs.contains(daten.Lehrer_ID))) {
				logger.logLn(LogLevel.ERROR, strFehlerKeinLehrer);
				entities.remove(i);
			} else if ((daten.Jahr == null) || (daten.Abschnitt == null)) {
				logger.logLn(LogLevel.ERROR, strFehlerLernabschnittUngueltigNull);
				entities.remove(i);
			} else if (!lehrerAbschnitte.contains("" + daten.Lehrer_ID + "." + daten.Jahr + "." + daten.Abschnitt)) {
				logger.logLn(LogLevel.ERROR, strFehlerLernabschnittUngueltig);
				entities.remove(i);
			} else {
				// Entferne ggf. Duplikate in Bezug auf die Anrechnungsgründe bei den gleichen Lehrerabschnittsdaten
				final String key = "" + daten.Lehrer_ID + "." + daten.Jahr + "." + daten.Abschnitt + "." + daten.AnrechnungsgrundKrz;
				final MigrationDTOLehrerAnrechnungsstunde other = map.get(key);
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
			final MigrationDTOLehrerAnrechnungsstunde daten = entities.get(i);
			daten.ID = (long) (i + 1);
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
	private boolean checkLehrerEntlastung(final List<MigrationDTOLehrerEntlastungsstunde> entities) {
		final HashMap<String, MigrationDTOLehrerEntlastungsstunde> map = new HashMap<>();
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOLehrerEntlastungsstunde daten = entities.get(i);
			if ((daten.Lehrer_ID == null) || (!lehrerIDs.contains(daten.Lehrer_ID))) {
				logger.logLn(LogLevel.ERROR, strFehlerKeinLehrer);
				entities.remove(i);
			} else if ((daten.Jahr == null) || (daten.Abschnitt == null)) {
				logger.logLn(LogLevel.ERROR, strFehlerLernabschnittUngueltigNull);
				entities.remove(i);
			} else if (!lehrerAbschnitte.contains("" + daten.Lehrer_ID + "." + daten.Jahr + "." + daten.Abschnitt)) {
				logger.logLn(LogLevel.ERROR, strFehlerLernabschnittUngueltig);
				entities.remove(i);
			} else {
				// Entferne ggf. Duplikate in Bezug auf die Entlastungsgründe bei den gleichen Lehrerabschnittsdaten
				final String key = "" + daten.Lehrer_ID + "." + daten.Jahr + "." + daten.Abschnitt + "." + daten.EntlastungsgrundKrz;
				final MigrationDTOLehrerEntlastungsstunde other = map.get(key);
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
			final MigrationDTOLehrerEntlastungsstunde daten = entities.get(i);
			daten.ID = (long) (i + 1);
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
	private boolean checkLehrerMehrleistung(final List<MigrationDTOLehrerMehrleistung> entities) {
		final HashMap<String, MigrationDTOLehrerMehrleistung> map = new HashMap<>();
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOLehrerMehrleistung daten = entities.get(i);
			if ((daten.Lehrer_ID == null) || (!lehrerIDs.contains(daten.Lehrer_ID))) {
				logger.logLn(LogLevel.ERROR, strFehlerKeinLehrer);
				entities.remove(i);
			} else if ((daten.Jahr == null) || (daten.Abschnitt == null)) {
				logger.logLn(LogLevel.ERROR, strFehlerLernabschnittUngueltigNull);
				entities.remove(i);
			} else if (!lehrerAbschnitte.contains("" + daten.Lehrer_ID + "." + daten.Jahr + "." + daten.Abschnitt)) {
				logger.logLn(LogLevel.ERROR, strFehlerLernabschnittUngueltig);
				entities.remove(i);
			} else {
				// Entferne ggf. Duplikate in Bezug auf die Mehrleistungsgründe bei den gleichen Lehrerabschnittsdaten
				final String key = "" + daten.Lehrer_ID + "." + daten.Jahr + "." + daten.Abschnitt + "." + daten.MehrleistungsgrundKrz;
				final MigrationDTOLehrerMehrleistung other = map.get(key);
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
			final MigrationDTOLehrerMehrleistung daten = entities.get(i);
			daten.ID = (long) (i + 1);
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
	private boolean checkLehrerFoto(final List<MigrationDTOLehrerFoto> entities) {
		final HashMap<Long, MigrationDTOLehrerFoto> map = new HashMap<>();
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOLehrerFoto daten = entities.get(i);
			if ((daten.Lehrer_ID == null) || (!lehrerIDs.contains(daten.Lehrer_ID))) {
				logger.logLn(LogLevel.ERROR, strFehlerKeinLehrer);
				entities.remove(i);
			} else {
				// Entferne ggf. Duplikate mit gleicher ID
				final MigrationDTOLehrerFoto other = map.get(daten.Lehrer_ID);
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
	private boolean checkLehrerFunktionen(final List<MigrationDTOLehrerFunktion> entities) {
		final HashMap<String, MigrationDTOLehrerFunktion> map = new HashMap<>();
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOLehrerFunktion daten = entities.get(i);
			if ((daten.Lehrer_ID == null) || (!lehrerIDs.contains(daten.Lehrer_ID))) {
				logger.logLn(LogLevel.ERROR, strFehlerKeinLehrer);
				entities.remove(i);
			} else if ((daten.Jahr == null) || (daten.Abschnitt == null)) {
				logger.logLn(LogLevel.ERROR, strFehlerLernabschnittUngueltigNull);
				entities.remove(i);
			} else if (!lehrerAbschnitte.contains("" + daten.Lehrer_ID + "." + daten.Jahr + "." + daten.Abschnitt)) {
				logger.logLn(LogLevel.ERROR, strFehlerLernabschnittUngueltig);
				entities.remove(i);
			} else {
				// Entferne ggf. Duplikate in Bezug auf die Lehrerfunktion bei den gleichen Lehrerabschnittsdaten
				final String key = "" + daten.Lehrer_ID + "." + daten.Jahr + "." + daten.Abschnitt + "." + daten.Funktion_ID;
				final MigrationDTOLehrerFunktion other = map.get(key);
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
			final MigrationDTOLehrerFunktion daten = entities.get(i);
			daten.ID = (long) (i + 1);
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
	private boolean checkLehrerLehramt(final List<MigrationDTOLehrerLehramt> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOLehrerLehramt daten = entities.get(i);
			if ((daten.Lehrer_ID == null) || (!lehrerIDs.contains(daten.Lehrer_ID))) {
				logger.logLn(LogLevel.ERROR, strFehlerKeinLehrer);
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
	private boolean checkLehrerLehramtFachrichtung(final List<MigrationDTOLehrerLehramtFachrichtung> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOLehrerLehramtFachrichtung daten = entities.get(i);
			if ((daten.Lehrer_ID == null) || (!lehrerIDs.contains(daten.Lehrer_ID))) {
				logger.logLn(LogLevel.ERROR, strFehlerKeinLehrer);
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
	private boolean checkLehrerLehramtBefaehigung(final List<MigrationDTOLehrerLehramtBefaehigung> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOLehrerLehramtBefaehigung daten = entities.get(i);
			if ((daten.Lehrer_ID == null) || (!lehrerIDs.contains(daten.Lehrer_ID))) {
				logger.logLn(LogLevel.ERROR, strFehlerKeinLehrer);
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
	private boolean checkLehrerDatenschutz(final List<MigrationDTOLehrerDatenschutz> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOLehrerDatenschutz daten = entities.get(i);
			if ((daten.LehrerID == null) || (!lehrerIDs.contains(daten.LehrerID))) {
				logger.logLn(LogLevel.ERROR, strFehlerKeinLehrer);
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
	private boolean checkLehrerLernplattform(final List<MigrationDTOLehrerLernplattform> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOLehrerLernplattform daten = entities.get(i);
			if ((daten.LehrerID == null) || (!lehrerIDs.contains(daten.LehrerID))) {
				logger.logLn(LogLevel.ERROR, strFehlerKeinLehrer);
				entities.remove(i);
				continue;
			}
			if ((daten.CredentialID != null) && (!credentialsLernplattformenIDs.contains(daten.CredentialID))) {
				logger.logLn(LogLevel.ERROR, strFehlerKeineCredentials);
				daten.CredentialID = null;
			}
		}
		return true;
	}



	/**
	 * Prüft die Entitäten der Tabelle "SchuelerLernplattform".
	 * Hierbei wird geprüft, ob der zugeordnete Schüler mit den angegebenen ID existiert.
	 *
	 * @param entities   die Entitäten
	 *
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkSchuelerLernplattform(final List<MigrationDTOSchuelerLernplattform> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOSchuelerLernplattform daten = entities.get(i);
			if ((daten.SchuelerID == null) || (!schuelerIDs.contains(daten.SchuelerID))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Es gibt keinen Schüler mit der angebenen ID in der Datenbank.");
				entities.remove(i);
				continue;
			}
			if ((daten.CredentialID != null) && (!credentialsLernplattformenIDs.contains(daten.CredentialID))) {
				logger.logLn(LogLevel.ERROR, strFehlerKeineCredentials);
				daten.CredentialID = null;
			}
		}
		return true;
	}


	/**
	 * Prüft die Entitäten der Tabelle "ErzieherLernplattform".
	 * Hierbei wird geprüft, ob der zugeordnete Erzieher mit der angegebenen ID existiert.
	 *
	 * @param entities   die Entitäten
	 *
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkErzieherLernplattform(final List<MigrationDTOErzieherLernplattform> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOErzieherLernplattform daten = entities.get(i);
			if ((daten.ErzieherID == null) || (!erzieherIDs.contains(daten.ErzieherID))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz: Es gibt keinen Erzieher mit der angebenen ID in der Datenbank.");
				entities.remove(i);
				continue;
			}
			if ((daten.CredentialID != null) && (!credentialsLernplattformenIDs.contains(daten.CredentialID))) {
				logger.logLn(LogLevel.ERROR, strFehlerKeineCredentials);
				daten.CredentialID = null;
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
	private boolean checkSchueler(final List<MigrationDTOSchueler> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOSchueler daten = entities.get(i);
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
				final String[] aufgeteilt = AdressenUtils.splitStrasse(daten.Strasse);
				daten.Strassenname = aufgeteilt[0];
				daten.HausNr = aufgeteilt[1];
				daten.HausNrZusatz = aufgeteilt[2];
			}
			// Prüfe das Feld LSSchulform
			if ((daten.LSSchulform != null) && (daten.LSSchulform.length() > 2))
				daten.LSSchulform = daten.LSSchulform.substring(0, 2);
			// Passe das Feld LSVersetzung an und verwende die ID statt des Statistik-Kürzels in der DB
			if ((daten.LSVersetzung != null)) {
				final Herkunftsarten art = switch (daten.LSVersetzung) {
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
	private boolean checkSchuelerErzieherAdresse(final List<MigrationDTOSchuelerErzieherAdresse> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOSchuelerErzieherAdresse daten = entities.get(i);
			// Splitte die Strasseninformation in Name, Hausnummer und Zusatz
			if (daten.ErzStrasse != null) {
				final String[] aufgeteilt = AdressenUtils.splitStrasse(daten.ErzStrasse);
				daten.ErzStrassenname = aufgeteilt[0];
				daten.ErzHausNr = aufgeteilt[1];
				daten.ErzHausNrZusatz = aufgeteilt[2];
			}
			erzieherIDs.add(daten.ID);
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
	private boolean checkSchuelerDatenschutz(final List<MigrationDTOSchuelerDatenschutz> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOSchuelerDatenschutz daten = entities.get(i);
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
	private boolean checkSchuelerGSDaten(final List<MigrationDTOSchuelerGrundschuldaten> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOSchuelerGrundschuldaten daten = entities.get(i);
			if ((daten.Schueler_ID == null) || (!schuelerIDs.contains(daten.Schueler_ID))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz in SchuelerGSDaten: Es gibt keinen Schüler mit der angebenen ID (%d) in der Datenbank.".formatted(daten.Schueler_ID));
				entities.remove(i);
			}
		}
		return true;
	}


	/**
	 * Prüft die Entitäten der Tabelle "SchuelerKAoADaten".
	 * Hierbei wird geprüft, ob der Schüler in der DB existiert.
	 *
	 * @param entities   die Entitäten
	 *
	 * @return true, falls die Daten ohne schwerwiegenden Fehler geprüft wurden
	 */
	private boolean checkSchuelerKAoADaten(final List<MigrationDTOSchuelerKAoADaten> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOSchuelerKAoADaten daten = entities.get(i);
			if ((daten.Schueler_ID == null) || (!schuelerIDs.contains(daten.Schueler_ID))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz in SchuelerKAoADaten: Es gibt keinen Schüler mit der angebenen ID (%d) in der Datenbank.".formatted(daten.Schueler_ID));
				entities.remove(i);
				continue;
			}
			if ((daten.Abschnitt_ID == null) || (!schuelerLeistungsdatenIDs.contains(daten.Abschnitt_ID))) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz in SchuelerKAoADaten: Es gibt keinen Lernabschnitt mit der angebenen ID (" + daten.Abschnitt_ID + ") in der Datenbank.");
				entities.remove(i);
				continue;
			}
			if (daten.KategorieID == null) {
				logger.logLn(LogLevel.ERROR, "Entferne ungültigen Datensatz in SchuelerKAoADaten: Kategorie muss zugeordnet sein.");
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
	private boolean checkKatalogAllgAdresse(final List<MigrationDTOKatalogAllgemeineAdresse> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOKatalogAllgemeineAdresse daten = entities.get(i);
			if (daten.strasse != null) {
				final String[] aufgeteilt = AdressenUtils.splitStrasse(daten.strasse);
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
	private boolean checkSchuelerAllgAdr(final List<MigrationDTOSchuelerAllgemeineAdresse> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOSchuelerAllgemeineAdresse daten = entities.get(i);
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
	private boolean checkKatalogOrt(final List<MigrationDTOOrt> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOOrt daten = entities.get(i);
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
	private static boolean checkKatalogAnkreuzdaten(final List<MigrationDTOAnkreuzdaten> entities) {
		for (int i = 0; i < entities.size(); i++) {
			final MigrationDTOAnkreuzdaten daten = entities.get(i);
			daten.ID = (long) (i + 1);
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
	private boolean checkFaecher(final List<MigrationDTOFach> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOFach daten = entities.get(i);
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
			final MigrationDTOFach daten = entities.get(i);
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
	private boolean checkKatalogAdressart(final List<MigrationDTOKatalogAdressart> entities) {
		for (int i = entities.size() - 1; i >= 0; i--) {
			final MigrationDTOKatalogAdressart daten = entities.get(i);
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
	private static boolean checkKatalogKindergarten(final List<MigrationDTOKindergarten> entities) {
		for (int i = 0; i < entities.size(); i++) {
			final MigrationDTOKindergarten daten = entities.get(i);
			// Splitte die Strasseninformation in Name, Hausnummer und Zusatz
			if (daten.Strasse != null) {
				final String[] aufgeteilt = AdressenUtils.splitStrasse(daten.Strasse);
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
	private boolean checkData(final List<?> entities) {
		final Object firstObject = entities.get(0);
		if (firstObject instanceof MigrationDTOEigeneSchule)
			return checkEigeneSchule((List<MigrationDTOEigeneSchule>) entities);
		if (firstObject instanceof MigrationDTOTeilstandorte)
			return checkEigeneSchuleTeilstandorte((List<MigrationDTOTeilstandorte>) entities);
		if (firstObject instanceof MigrationDTOKursarten)
			return checkEigeneSchuleKursarten((List<MigrationDTOKursarten>) entities);
		if (firstObject instanceof MigrationDTOSchuleNRW)
			return checkKatalogSchule((List<MigrationDTOSchuleNRW>) entities);
		if (firstObject instanceof MigrationDTOSchuelerIndividuelleGruppe)
			return checkSchuelerListe((List<MigrationDTOSchuelerIndividuelleGruppe>) entities);
		if (firstObject instanceof MigrationDTOSchuelerIndividuelleGruppeSchueler)
			return checkSchuelerListeInhalt((List<MigrationDTOSchuelerIndividuelleGruppeSchueler>) entities);
		if (firstObject instanceof MigrationDTOCredentials)
			return checkCredentials((List<MigrationDTOCredentials>) entities);
		if (firstObject instanceof MigrationDTOCredentialsLernplattformen)
			return checkCredentialsLernplattformen((List<MigrationDTOCredentialsLernplattformen>) entities);
		if (firstObject instanceof MigrationDTOFachklassen)
			return checkEigeneSchuleFachklassen((List<MigrationDTOFachklassen>) entities);
		if (firstObject instanceof MigrationDTOUsers)
			return checkUsers((List<MigrationDTOUsers>) entities);
		if (firstObject instanceof MigrationDTOProtokollLogin)
			return checkLogins((List<MigrationDTOProtokollLogin>) entities);
		if (firstObject instanceof MigrationDTOPersonengruppen)
			return checkPersonengruppen((List<MigrationDTOPersonengruppen>) entities);
		if (firstObject instanceof MigrationDTOPersonengruppenPersonen)
			return checkPersonengruppenPersonen((List<MigrationDTOPersonengruppenPersonen>) entities);
		if (firstObject instanceof MigrationDTOSchuelerLernabschnittsdaten)
			return checkSchuelerLernabschnittsdaten((List<MigrationDTOSchuelerLernabschnittsdaten>) entities);
		if (firstObject instanceof MigrationDTOSchuelerLeistungsdaten)
			return checkSchuelerLeistungsdaten((List<MigrationDTOSchuelerLeistungsdaten>) entities);
		if (firstObject instanceof MigrationDTOSchuelerPSFachBemerkungen)
			return checkSchuelerLD_PSFachBem((List<MigrationDTOSchuelerPSFachBemerkungen>) entities);
		if (firstObject instanceof MigrationDTOSchuelerFoerderempfehlung)
			return checkSchuelerFoerderempfehlungen((List<MigrationDTOSchuelerFoerderempfehlung>) entities);
		if (firstObject instanceof MigrationDTOSchuelerAbiturFach)
			return checkSchuelerAbiFaecher((List<MigrationDTOSchuelerAbiturFach>) entities);
		if (firstObject instanceof MigrationDTOKurs)
			return checkKurse((List<MigrationDTOKurs>) entities);
		if (firstObject instanceof MigrationDTOLehrer)
			return checkLehrer((List<MigrationDTOLehrer>) entities);
		if (firstObject instanceof MigrationDTOLehrerAbschnittsdaten)
			return checkLehrerAbschnittsdaten((List<MigrationDTOLehrerAbschnittsdaten>) entities);
		if (firstObject instanceof MigrationDTOLehrerAnrechnungsstunde)
			return checkLehrerAnrechnung((List<MigrationDTOLehrerAnrechnungsstunde>) entities);
		if (firstObject instanceof MigrationDTOLehrerEntlastungsstunde)
			return checkLehrerEntlastung((List<MigrationDTOLehrerEntlastungsstunde>) entities);
		if (firstObject instanceof MigrationDTOLehrerMehrleistung)
			return checkLehrerMehrleistung((List<MigrationDTOLehrerMehrleistung>) entities);
		if (firstObject instanceof MigrationDTOLehrerLehramt)
			return checkLehrerLehramt((List<MigrationDTOLehrerLehramt>) entities);
		if (firstObject instanceof MigrationDTOLehrerLehramtBefaehigung)
			return checkLehrerLehramtBefaehigung((List<MigrationDTOLehrerLehramtBefaehigung>) entities);
		if (firstObject instanceof MigrationDTOLehrerLehramtFachrichtung)
			return checkLehrerLehramtFachrichtung((List<MigrationDTOLehrerLehramtFachrichtung>) entities);
		if (firstObject instanceof MigrationDTOLehrerDatenschutz)
			return checkLehrerDatenschutz((List<MigrationDTOLehrerDatenschutz>) entities);
		if (firstObject instanceof MigrationDTOLehrerLernplattform)
			return checkLehrerLernplattform((List<MigrationDTOLehrerLernplattform>) entities);
		if (firstObject instanceof MigrationDTOSchuelerLernplattform)
			return checkSchuelerLernplattform((List<MigrationDTOSchuelerLernplattform>) entities);
		if (firstObject instanceof MigrationDTOErzieherLernplattform)
			return checkErzieherLernplattform((List<MigrationDTOErzieherLernplattform>) entities);
		if (firstObject instanceof MigrationDTOLehrerFoto)
			return checkLehrerFoto((List<MigrationDTOLehrerFoto>) entities);
		if (firstObject instanceof MigrationDTOLehrerFunktion)
			return checkLehrerFunktionen((List<MigrationDTOLehrerFunktion>) entities);
		if (firstObject instanceof MigrationDTOSchueler)
			return checkSchueler((List<MigrationDTOSchueler>) entities);
		if (firstObject instanceof MigrationDTOSchuelerErzieherAdresse)
			return checkSchuelerErzieherAdresse((List<MigrationDTOSchuelerErzieherAdresse>) entities);
		if (firstObject instanceof MigrationDTOSchuelerDatenschutz)
			return checkSchuelerDatenschutz((List<MigrationDTOSchuelerDatenschutz>) entities);
		if (firstObject instanceof MigrationDTOSchuelerGrundschuldaten)
			return checkSchuelerGSDaten((List<MigrationDTOSchuelerGrundschuldaten>) entities);
		if (firstObject instanceof MigrationDTOSchuelerKAoADaten)
			return checkSchuelerKAoADaten((List<MigrationDTOSchuelerKAoADaten>) entities);
		if (firstObject instanceof MigrationDTOSchuelerBKFach)
			return checkSchuelerBKFaecher((List<MigrationDTOSchuelerBKFach>) entities);
		if (firstObject instanceof MigrationDTOKatalogAllgemeineAdresse)
			return checkKatalogAllgAdresse((List<MigrationDTOKatalogAllgemeineAdresse>) entities);
		if (firstObject instanceof MigrationDTOSchuelerAllgemeineAdresse)
			return checkSchuelerAllgAdr((List<MigrationDTOSchuelerAllgemeineAdresse>) entities);
		if (firstObject instanceof MigrationDTOOrt)
			return checkKatalogOrt((List<MigrationDTOOrt>) entities);
		if (firstObject instanceof MigrationDTOAnkreuzdaten)
			return checkKatalogAnkreuzdaten((List<MigrationDTOAnkreuzdaten>) entities);
		if (firstObject instanceof MigrationDTOFach)
			return checkFaecher((List<MigrationDTOFach>) entities);
		if (firstObject instanceof MigrationDTOKatalogAdressart)
			return checkKatalogAdressart((List<MigrationDTOKatalogAdressart>) entities);
		if (firstObject instanceof MigrationDTOKindergarten)
			return checkKatalogKindergarten((List<MigrationDTOKindergarten>) entities);
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
		for (final SchemaTabelle tab : Schema.getTabellen(0)) {
			// Prüfe, ob die Tabelle bei der Migration beachtet werden soll, wenn nicht dann überspringe sie
			if (!tab.migrate())
				continue;

			logger.logLn("Tabelle " + tab.name() + ":");
			logger.modifyIndent(2);

			// Lese alle Datensätze aus der Quell-Tabelle
			logger.log("- Lese Datensätze: ");
			final List<?> entities = readAllData(tab);
			if (entities == null) {
				logger.logLn(LogLevel.ERROR, 0, "[FEHLER] - Kann die Datensätze nicht einlesen - Überspringe die Tabelle");
				logger.logLn(LogLevel.ERROR, srcManager.getLastError());
				continue;
			}
			logger.logLn(0, entities.size() + " Datensätze eingelesen (Freier Speicher: " + (Math.round(Runtime.getRuntime().freeMemory() / 10000000.0) / 100.0) + "G/" + (Math.round(Runtime.getRuntime().totalMemory() / 10000000.0) / 100.0) + "G/" + (Math.round(Runtime.getRuntime().maxMemory() / 10000000.0) / 100.0) + "G)");

			// Wenn keine Daten vorhanden sind, dann brauchen auch keine geschrieben zu werden...
			if (entities.isEmpty()) {
				if ((lastError != null) && (!"".equals(lastError)))
					logger.logLn("  Fehler: " + lastError);
				logger.modifyIndent(-2);
				continue;
			}

			// Prüfe die Entitäten auf fehlerhafte Daten, welche dann gefiltert werden, und ergänze ggf. zusätzliche Informationen während der Migration
			if (!checkData(entities))
				return false;
			if (entities.isEmpty()) {
				logger.modifyIndent(-2);
				continue;
			}

			// Schreibe die Datensätze in die Zieltabelle
			write(entities);
		}
		return true;
	}


	/**
	 * Konvertiert am Ende der Migration die Bilder in der Zieldatenbank in die Base64-Kodierung.
	 */
	private void convertImages() {
		try (DBEntityManager conn = tgtManager.getUser().getEntityManager()) {
			final String strLogBilderAnzahl = "%d Bilder";
			logger.log("* Tabelle EigeneSchule...");
			final List<MigrationDTOEigeneSchule> es_in = conn.queryAll(MigrationDTOEigeneSchule.class);
			final List<MigrationDTOEigeneSchule> es_out = es_in.stream()
					.filter(es -> {
						if (es.SchulLogo == null)
							return false;
						es.SchulLogoBase64 = Base64.getEncoder().encodeToString(es.SchulLogo);
						return true;
					}).toList();
			conn.persistAll(es_out);
			logger.logLn(strLogBilderAnzahl.formatted(es_out.size()));

			logger.log("* Tabelle LehrerFoto...");
			final List<MigrationDTOLehrerFoto> lf_in = conn.queryAll(MigrationDTOLehrerFoto.class);
			final List<MigrationDTOLehrerFoto> lf_out = lf_in.stream()
					.filter(lf -> {
						if (lf.Foto == null)
							return false;
						lf.FotoBase64 = Base64.getEncoder().encodeToString(lf.Foto);
						return true;
					}).toList();
			conn.persistAll(lf_out);
			logger.logLn(strLogBilderAnzahl.formatted(lf_out.size()));

			logger.log("* Tabelle SchuelerFoto...");
			final List<MigrationDTOSchuelerFoto> sf_in = conn.queryAll(MigrationDTOSchuelerFoto.class);
			final List<MigrationDTOSchuelerFoto> sf_out = sf_in.stream()
					.filter(sf -> {
						if (sf.Foto == null)
							return false;
						sf.FotoBase64 = Base64.getEncoder().encodeToString(sf.Foto);
						return true;
					}).toList();
			conn.persistAll(sf_out);
			logger.logLn(strLogBilderAnzahl.formatted(sf_out.size()));
		}
	}

}
