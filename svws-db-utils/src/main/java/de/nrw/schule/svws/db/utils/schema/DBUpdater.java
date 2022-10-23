package de.nrw.schule.svws.db.utils.schema;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import de.nrw.schule.svws.config.SVWSKonfiguration;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.DBException;
import de.nrw.schule.svws.db.schema.DBSchemaViews;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.schema.SchemaRevisionen;
import de.nrw.schule.svws.db.schema.SchemaTabelle;
import de.nrw.schule.svws.db.schema.SchemaTabelleFremdschluessel;
import de.nrw.schule.svws.db.schema.SchemaTabelleIndex;
import de.nrw.schule.svws.db.schema.SchemaTabelleSpalte;
import de.nrw.schule.svws.db.schema.SchemaTabelleTrigger;
import de.nrw.schule.svws.db.schema.SchemaTabelleUniqueIndex;
import de.nrw.schule.svws.db.schema.View;
import de.nrw.schule.svws.logger.Logger;

/**
 * Diese Klasse enthält die Methode zur Durchführung von Datenbank-Updates 
 */
public class DBUpdater {

	/** Der Schema-Manager, welcher für die Updates verwendet wird */
	private final DBSchemaManager schemaManager;
	
	/** Ein Logger, um die Abläufe bei dem Update-Prozess zu loggen */ 
	private final Logger logger;	

	/** Der Status des Datenbank-Schema */
	private final DBSchemaStatus status;
	
	/** Gibt an, ob die Ausführung von Operationen bei einzelnen Fehlern abgebrochen werden sollen. */
	private final boolean returnOnError;
	
	/** Der Updater für die Core-Types */
	public final DBCoreTypeUpdater coreTypes;
	

	/**
	 * Erzeugt einen neuen {@link DBUpdater}.
	 * 
	 * @param schemaManager   der Schema-Manager, welcher verwendet wird
	 * @param returnOnError   gibt an, ob Operatioen bei Einzelfehlern abgebrochen werden sollen
	 */
	DBUpdater(DBSchemaManager schemaManager, boolean returnOnError) {
		this.schemaManager = schemaManager;
		this.logger = schemaManager.getLogger();
		this.status = schemaManager.getSchemaStatus();
		this.returnOnError = returnOnError;
		this.coreTypes = new DBCoreTypeUpdater(schemaManager, returnOnError);
	}
	

	/**
	 * Führt die Update-Schritte für die angegebene nächste Revision durch. Hierbei muss 
	 * beim Aufruf vorab sichergestellt sein, dass es sich wirklich um die nächste Revision 
	 * handelt!
	 * 
	 * @param neue_revision   die nächste (!) Revision, auf die aktualisiert wird. 
	 * 
	 * @return true, falls ein update erfolgreich durchgeführt wurde, sonst false - d.h. 
	 *         auch dann false, falls kein Update notwendig ist.
	 */
	private boolean performUpdate(long neue_revision) {
		boolean success = true;
		try {
			// 1. Update-Schritt: DROP_TRIGGER
			if (!dropTrigger(neue_revision))
				throw new DBException("Fehler beim Verwerfen der Trigger");

			// 2. Update-Schritt: DROP_INDICES
			if (!dropIndices(neue_revision))
				throw new DBException("Fehler beim Verwerfen der Indizes");
			
			// 3. Update-Schritt: DROP_UNIQUE_CONSTRAINTS
			if (!dropUniqueConstraints(neue_revision))
				throw new DBException("Fehler beim Verwerfen der Unique-Constraints");
			
			// 4. Update-Schritt: DROP_FOREIGN_KEYS 
			if (!dropForeignKeys(neue_revision))
				throw new DBException("Fehler beim Verwerfen der Fremdschlüssel");

			// 5. Update-Schritt: CREATE_TABLES
			if (!createNewTables(neue_revision))
				throw new DBException("Fehler beim Erstellen der neuen Tabellen");
			
			// 6. Update-Schritt: ADD_COLUMNS
			if (!addNewColumns(neue_revision))
				throw new DBException("Fehler beim Hinzufügen der neuen Tabellenspalten");
			
			// 7. Update-Schritt: Core-Type-Updates
			if (!coreTypes.update(false, neue_revision))
				throw new DBException("Fehler beim Aktualisieren der Core-Types");				
			
			// 8. Update-Schritt: ManualSQL
			if (!executeManualSQLCommands(neue_revision))
				throw new DBException("Fehler beim Ausführen der manuellen SQL-Befehle");
			
			// 9. Update-Schritt: ADD_FOREIGN_KEYS
			if (!addNewForeignKeys(neue_revision))
				throw new DBException("Fehler beim Hinzufügen der neuen Fremdschlüssel");
			
			// 10. Update-Schritt: ADD_UNIQUE_CONSTRAINTS
			if (!addNewUniqueConstraints(neue_revision))
				throw new DBException("Fehler beim Hinzufügen der neuen Unique-Constraints");
			
			// 11. Update-Schritt: ADD_INDICES
			if (!addNewIndices(neue_revision))
				throw new DBException("Fehler beim Hinzufügen der neuen Indizes");
			
			// 12. Update-Schritt: ADD_TRIGGER
			if (!createNewTrigger(neue_revision))
				throw new DBException("Fehler beim Erstellen der neuen Trigger");
			
			// 13. Update-Schritt: ADD VIEWS
			if (!createNewViews(neue_revision))
				throw new DBException("Fehler beim Erstellen der neuen Views");
			
			// 14. Update-Schritt: DROP VIEWS
			if (!dropViews(neue_revision))
				throw new DBException("Fehler beim Verwerfen der Views");
			
			// 15. Update-Schritt: DROP_COLUMNS
			if (!dropColumns(neue_revision))
				throw new DBException("Fehler beim Verwerfen veralteter Tabellenspalten");
			
			// 16. Update-Schritt: DROP_TABLES
			if (!dropTables(neue_revision))
				throw new DBException("Fehler beim Verwerfen veralteter Tabellen");
			
			// 17. Update-Schritt: Setze ggf. die Default_Daten neu
			if (!copyDefaultDataUpdates(neue_revision))
				throw new DBException("Fehler beim Aktualisieren der Default-Daten");
			
			// 18. Update-Schritt: Tabelle SVWS_DB_Version aktualisieren
			logger.logLn("- Setze die DB-Revision auf " + neue_revision);
			if (!schemaManager.setDBRevision(neue_revision))
				throw new DBException("Fehler beim Setzen der SVWS-DB-Revision");
		} catch (DBException e) {
			success = false;
		} finally {
			System.gc();
		}
		return success;
	}
	
	
	
	/**
	 * Prüft, ob das Schema aktuell ist, d.h. entweder die neueste bekannte Revision hat oder sogar neuer ist.
	 * 
	 * @param maxUpdateRevision   die Revision auf die geprüft werden soll
	 * @param devMode             gibt an, ob auch Schema-Revision erlaubt werden, die nur für Entwickler zur Verfügung stehen
	 * 
	 * @return true, falls das schema aktuell ist, sonst false
	 */
	public boolean isUptodate(long maxUpdateRevision, boolean devMode) {
		status.update();
		DBSchemaVersion currentVersion = status.getVersion();
		if (currentVersion == null) {
			logger.logLn("Fehler: Aktuelle Revision des Schemas konnte nicht ermittelt werden.");
			return false;
		}
		long max_revision = devMode ? SchemaRevisionen.maxDeveloperRevision.revision : SchemaRevisionen.maxRevision.revision;
		if (max_revision < 0) {
			logger.logLn("Interner Fehler: Es ist keine gültige Datenbank-Revision definiert");
			return true;
		}
		if ((maxUpdateRevision >= 0) && (maxUpdateRevision < max_revision))
			max_revision = maxUpdateRevision;
		try {
			// Prüfe, ob das Schema aktuell ist oder sogar neuer als in der schema-Beschreibung bekannt.
			long revision = currentVersion.getRevision();
			if (revision == max_revision) {
				logger.logLn("Das Schema ist bereits in der angegebenen Revision.");
				return true;
			} else if (revision > max_revision) {
				logger.logLn("Warnung: Das Schema ist bereits aktueller, als in der Datenbank eingetragen. Es sollte eine aktuellere Version des SVWS-Server-Projektes verwendet werden!");
				return true;
			}
			return false;
		} catch (Exception e) {
			logger.logLn("Fehler: Aktuelle Revision des Schemas konnte nicht ermittelt werden.");
			return false;
		}
	}
	
	
	
	/**
	 * Prüft, ob eine Aktualisierung auf die angegebene Revision möglich ist
	 * 
	 * @param maxUpdateRevision   die maximale Revision auf die aktualisiert werden soll, -1 für die neueste Revision
	 * @param devMode             gibt an, ob auch Schema-Revision erlaubt werden, die nur für Entwickler zur Verfügung stehen
	 * 
	 * @return true, falls eine Aktualisierung möglich ist, sonst false
	 */
	public boolean isUpdatable(long maxUpdateRevision, boolean devMode) {
		// Prüfe zunächst, ob ein Update möglich ist
		status.update();
		DBSchemaVersion currentVersion = status.getVersion();
		long max_revision = devMode ? SchemaRevisionen.maxDeveloperRevision.revision : SchemaRevisionen.maxRevision.revision;
		if ((currentVersion == null) || (max_revision < 0))
			return false;
		if ((maxUpdateRevision >= 0) && (maxUpdateRevision < max_revision))
			max_revision = maxUpdateRevision;
		try {
			// Ist eine Aktualisierung überhaupt nötig, oder ist das Schema schon aktuell oder sogar aktueller?
			long revision = currentVersion.getRevision();
			if (revision >= max_revision)
				return false;
		} catch (Exception e) {
			// Das Schema hat keine gültige Revision und kann daher nicht aktualisiert werden - Migration nötig?
			return false;
		}
		return true;
	}
	
	
	/**
	 * Aktualisiert das Schema schrittweise auf die angegebene Revision
	 * 
	 * @param maxUpdateRevision   die maximale Revision auf die aktualisiert wird, -1 für die neueste Revision
	 * @param devMode             gibt an, ob auch Schema-Revision erlaubt werden, die nur für Entwickler zur Verfügung stehen
	 * @param lockSchema          gibt an, on das Schema für den Update-Prozess gesperrt werden soll. Dies ist z.B. nicht 
	 *                            notwendig, wenn der Update-Prozess am Ende einer Migration gestartet wird.  
	 * 
	 * @return true im Erfolgsfall, sonst false
	 */
	public boolean update(long maxUpdateRevision, boolean devMode, boolean lockSchema) {
		// Sperre ggf. das Datenbankschema
		if ((lockSchema) && (!SVWSKonfiguration.get().lockSchema(schemaManager.getSchemaStatus().schemaName))) {
			logger.logLn("-> Update fehlgeschlagen! (Schema ist aktuell gesperrt und kann daher nicht aktualisiert werden)");
			return false;
		}
		
		// Prüfe zunächst, ob ein Update möglich ist
		status.update();
		DBSchemaVersion currentVersion = status.getVersion();
		long max_revision = devMode ? SchemaRevisionen.maxDeveloperRevision.revision : SchemaRevisionen.maxRevision.revision;
		if ((currentVersion == null) || (max_revision < 0))
			return false;
		if ((maxUpdateRevision >= 0) && (maxUpdateRevision < max_revision))
			max_revision = maxUpdateRevision;
		
		// Ist kein Update nötig, so war die Aktualisierung erfolgreich
		if (max_revision <= currentVersion.getRevisionOrDefault(0))
			return true;
		
		// Ermittle die nächste Revision, auf die aktualisiert werden soll
		boolean success = true;
		for (long neue_revision = currentVersion.getRevisionOrDefault(0) + 1; neue_revision <= max_revision; neue_revision++) {
			logger.logLn("* Aktualisiere auf Revision " + neue_revision);
			logger.modifyIndent(2);
			success = performUpdate(neue_revision);
			logger.modifyIndent(-2);
			if (!success)
				break;
		}
		
		// Entsperre ggf. das Datenbankschema
		if ((lockSchema) && (!SVWSKonfiguration.get().unlockSchema(schemaManager.getSchemaStatus().schemaName))) {
			logger.logLn("-> Update evtl. fehlgeschlagen! (Fehler beim Freigeben des Datenbank-Schemas. Schema ist nicht gesperrt - dies wird an dieser Stelle nicht erwartet!)");
			return false;
		}		
		return success;
	}

	
	
	/**
	 * Kopiert die Default-Daten für alle Tabellen, welche bei der angegebenen Revision 
	 * Änderungen bei den Default-Daten haben.
	 * 
	 * @param rev   die Revision, bei der Änderungen bei den Default-Daten vorliegen sollen
	 * 
	 * @return true, falls die Daten erfolgreich kopiert wurden, sonst false.
	 */
	private boolean copyDefaultDataUpdates(long rev) {
		logger.log("- Aktualisiere Daten: ");
		// TODO Diese Methode der Aktualisierung ist veraltet... Sie ist nur für eine Übergangszeit zur Kompatibilität vorhanden
		if (rev != 0) {
			logger.logLn(0, "0 Tabellen");
			return true;
		}
		List<SchemaTabelle> tabs = Schema.getTabellenDefaultDaten(rev);
		if ((tabs == null) || (tabs.size() <= 0)) {
			logger.logLn(0, "0 Tabellen");
			return true;
		}
		boolean result = true;
		logger.logLn(0, tabs.size() + " Tabellen...");
		logger.modifyIndent(2);
		for (SchemaTabelle tab : tabs) {
			logger.logLn(tab.name());
			result = schemaManager.copyDefaultData(tab, rev);
			System.gc();
			if (!result && returnOnError)
				break;
		}		
		logger.modifyIndent(-2);
		return result;
	}	

	
	/**
	 * Verwirft die Trigger, die mit der angegebenen Revision veraltet sind.
	 * 
	 * @param veraltet   die Revision, mit der die Trigger veraltet sein müssen.
	 *   
	 * @return true, false alle entsprechenden Trigger erfolgreich verworfen wurden 
	 *         oder keine verworfen werden müssen, sonst false
	 */
	private boolean dropTrigger(long veraltet) {
		DBEntityManager conn = schemaManager.getEntityManager();
		logger.log("- Verwerfe: ");
		var dbms = conn.getDBDriver();
		List<SchemaTabelleTrigger> trigger = Schema.tabellen.values().stream()
				.flatMap(tab -> tab.trigger().stream())
				.filter(trig -> trig.veraltet().revision == veraltet)
				.collect(Collectors.toList());
		if ((trigger == null) || (trigger.size() <= 0)) {
			logger.logLn(0, "0 Trigger");
			return true;
		}
		boolean result = true;
		logger.logLn(0, trigger.size() + " Trigger...");
		logger.modifyIndent(2);
		for (SchemaTabelleTrigger trig : trigger) {
			var sql = trig.getSQL(dbms, false); 
			if ((sql == null) || ("".equals(sql)))
				continue;
			logger.logLn(trig.name());
			if (conn.executeNativeUpdate(sql) == Integer.MIN_VALUE) {
				result = false;
				if (returnOnError)
					break;
			}
		}
		logger.modifyIndent(-2);
		return result;
	}
	
	
	/**
	 * Verwirft die Indizes, die mit der angegebenen Revision veraltet sind.
	 * 
	 * @param veraltet   die Revision, mit der die Indizes veraltet sind.
	 *   
	 * @return true, false alle entsprechenden Indizes erfolgreich verworfen wurden 
	 *         oder keine verworfen werden müssen, sonst false
	 */
	private boolean dropIndices(long veraltet) {
		DBEntityManager conn = schemaManager.getEntityManager();
		logger.log("- Verwerfe: ");
		List<SchemaTabelleIndex> indizesVeraltet = Schema.tabellen.values().stream()
				.flatMap(tab -> tab.indizes().stream())
				.filter(idx -> idx.veraltet().revision == veraltet)
				.collect(Collectors.toList());
		if ((indizesVeraltet == null) || (indizesVeraltet.size() <= 0)) {
			logger.logLn(0, "0 Indizes");
			return true;
		}
		boolean result = true;
		logger.logLn(0, indizesVeraltet.size() + " Indizes...");
		logger.modifyIndent(2);
		var dbms = conn.getDBDriver();
		for (SchemaTabelleIndex idx : indizesVeraltet) {
			String sql = idx.getSQLDrop(dbms);
			if ((sql == null) || ("".equals(sql))) {
				logger.logLn("Kann " + idx.name() + "nicht entfernen!");
				continue;
			}
			logger.logLn(idx.name());
			if (conn.executeNativeUpdate(sql) == Integer.MIN_VALUE) {
				result = false;
				if (returnOnError)
					break;
			}
		}
		logger.modifyIndent(-2);
		return result;
	}
	
	
	/**
	 * Verwirft die Unique-Constraints, die mit der angegebenen Revision veraltet sind.
	 * 
	 * @param veraltet   die Revision, mit der die Unique-Constraints veraltet sind.
	 *   
	 * @return true, false alle entsprechenden Unique-Constraints erfolgreich verworfen wurden 
	 *         oder keine verworfen werden müssen, sonst false
	 */
	private boolean dropUniqueConstraints(long veraltet) {
		DBEntityManager conn = schemaManager.getEntityManager();
		logger.log("- Verwerfe: ");
		List<SchemaTabelleUniqueIndex> ucs = Schema.tabellen.values().stream()
				.flatMap(tab -> tab.unique().stream())
				.filter(uc -> uc.veraltet().revision == veraltet)
				.collect(Collectors.toList());
		if ((ucs == null) || (ucs.size() <= 0)) {
			logger.logLn(0, "0 Unique-Constraints");
			return true;
		}
		boolean result = true;
		logger.logLn(0, ucs.size() + " Unique-Constraints...");
		logger.modifyIndent(2);
		var dbms = conn.getDBDriver();
		for (SchemaTabelleUniqueIndex uc : ucs) {
			String sql = uc.getSQLDrop(dbms);
			if ((sql == null) || ("".equals(sql))) {
				logger.logLn("Kann " + uc.name() + "nicht entfernen!");
				continue;
			}
			logger.logLn(uc.name());
			if (conn.executeNativeUpdate(sql) == Integer.MIN_VALUE) {
				result = false;
				if (returnOnError)
					break;
			}
		}
		logger.modifyIndent(-2);
		return result;
	}
	
	
	/**
	 * Verwirft die Fremdschlüssel, die mit der angegebenen Revision veraltet sind.
	 * 
	 * @param veraltet   die Revision, mit der die Fremdschlüssel veraltet sind.
	 *   
	 * @return true, false alle entsprechenden Fremdschlüssel erfolgreich verworfen wurden 
	 *         oder keine verworfen werden müssen, sonst false
	 */
	private boolean dropForeignKeys(long veraltet) {
		DBEntityManager conn = schemaManager.getEntityManager();
		logger.log("- Verwerfe: ");
		List<SchemaTabelleFremdschluessel> fks = Schema.tabellen.values().stream()
				.flatMap(tab -> tab.fremdschluessel().stream())
				.filter(fk -> fk.veraltet().revision == veraltet)
				.collect(Collectors.toList());
		if ((fks == null) || (fks.size() <= 0)) {
			logger.logLn(0, "0 Fremdschlüssel");
			return true;
		}
		boolean result = true;
		logger.logLn(0, fks.size() + " Fremdschlüssel...");
		logger.modifyIndent(2);
		var dbms = conn.getDBDriver();
		for (SchemaTabelleFremdschluessel fk : fks) {
			String sql = fk.getSQLDrop(dbms);
			if ((sql == null) || ("".equals(sql))) {
				logger.logLn("Kann " + fk.name() + "nicht entfernen!");
				continue;
			}
			logger.logLn(fk.name());
			if (conn.executeNativeUpdate(sql) == Integer.MIN_VALUE) {
				result = false;
				if (returnOnError)
					break;
			}
		}
		logger.modifyIndent(-2);
		return result;
	}
	
	
	/**
	 * Verwirft die Tabellenspalten, die mit der angegebenen Revision veraltet sind.
	 * 
	 * @param veraltet   die Revision, mit der die Tabellenspalten veraltet sind.
	 *   
	 * @return true, false alle entsprechenden Tabellenspalten erfolgreich verworfen wurden 
	 *         oder keine verworfen werden müssen, sonst false
	 */
	private boolean dropColumns(long veraltet) {
		DBEntityManager conn = schemaManager.getEntityManager();
		logger.log("- Verwerfe: ");
		List<SchemaTabelle> tabs = Schema.getTabellen(veraltet-1);
		Collections.reverse(tabs);
		List<SchemaTabelleSpalte> cols = tabs.stream()
				.filter(tab -> tab.veraltet().revision != veraltet)
				.flatMap(tab -> tab.getSpalten().stream())
				.filter(col -> col.veraltet().revision == veraltet)
				.collect(Collectors.toList());
		if ((cols == null) || (cols.size() <= 0)) {
			logger.logLn(0, "0 Spalten");
			return true;
		}
		boolean result = true;
		logger.logLn(0, cols.size() + " Spalten...");
		logger.modifyIndent(2);
		var dbms = conn.getDBDriver();
		for (SchemaTabelleSpalte col : cols) {
			String sql = col.getSQLDrop(dbms);
			if ((sql == null) || ("".equals(sql))) {
				logger.logLn("Kann Spalte " + col.tabelle().name() + "." + col.name() + "nicht entfernen!");
				continue;
			}
			logger.logLn(col.tabelle().name() + "." + col.name());
			if (conn.executeNativeUpdate(sql) == Integer.MIN_VALUE) {
				result = false;
				if (returnOnError)
					break;
			}
		}
		logger.modifyIndent(-2);
		return result;
	}
	
	
	/**
	 * Verwirft die Tabellen, die mit der angegebenen Revision veraltet sind.
	 * 
	 * @param veraltet   die Revision, mit der die Tabellen veraltet ist.
	 *   
	 * @return true, false alle entsprechenden Tabellen erfolgreich verworfen wurden 
	 *         oder keine verworfen werden müssen, sonst false
	 */
	private boolean dropTables(long veraltet) {
		DBEntityManager conn = schemaManager.getEntityManager();
		logger.log("- Verwerfe: ");
		List<SchemaTabelle> tabs = Schema.getTabellen(veraltet-1).stream()
				.filter(tab -> tab.veraltet().revision == veraltet)
				.collect(Collectors.toList());
		Collections.reverse(tabs);
		if ((tabs == null) || (tabs.size() <= 0)) {
			logger.logLn(0, "0 Tabellen");
			return true;
		}
		boolean result = true;
		logger.logLn(0, tabs.size() + " Tabellen...");
		logger.modifyIndent(2);
		var dbms = conn.getDBDriver();
		for (SchemaTabelle tab : tabs) {
			String sql = tab.getSQLDrop(dbms);
			logger.logLn(tab.name());
			if (conn.executeNativeUpdate(sql) == Integer.MIN_VALUE) {
				result = false;
				if (returnOnError)
					break;
			}			
		}
		logger.modifyIndent(-2);
		return result;
	}
	
	
	/**
	 * Verwirft die Views, die mit der angegebenen Revision veraltet sind.
	 * 
	 * @param veraltet   die Views, mit der die Tabellen veraltet sind.
	 *   
	 * @return true, false alle entsprechenden Views erfolgreich verworfen wurden 
	 *         oder keine verworfen werden müssen, sonst false
	 */
	private boolean dropViews(long veraltet) {
		DBEntityManager conn = schemaManager.getEntityManager();
		logger.log("- Verwerfe: ");
		List<View> views = DBSchemaViews.getInstance().getViewsDeprecated(veraltet); 
		if ((views == null) || (views.size() <= 0)) {
			logger.logLn(0, "0 Views");
			return true;
		}
		boolean result = true;
		logger.logLn(0, views.size() + " Views...");
		logger.modifyIndent(2);
		for (int i = views.size() - 1; i >= 0; i--) {
			View view = views.get(i);
			String sql = view.getSQLDrop();
			logger.logLn(view.name);
			if (conn.executeNativeUpdate(sql) == Integer.MIN_VALUE) {
				result = false;
				if (returnOnError)
					break;
			}			
		}
		logger.modifyIndent(-2);
		return result;
	}
	
	
	/**
	 * Erstellt die neuen Tabellen, die mit der angegebenen Revision hinzugekommen sind.
	 * 
	 * @param revision   die Revision, mit die neuen Tabellen hinzugekommen sind.
	 *   
	 * @return true, false alle entsprechenden Tabellen erfolgreich angelegt wurden 
	 *         oder keine angelegt werden müssen, sonst false
	 */
	private boolean createNewTables(long revision) {
		DBEntityManager conn = schemaManager.getEntityManager();
		logger.log("- Erstelle: ");
		if (revision == 0) {
			logger.logLn(0, "Fehler: Eine Aktualisierung auf Revision 0 ergibt keinen Sinn, weshalb keine Tabellen erstellt werden.");
			return false;
		}
		List<SchemaTabelle> tabs = Schema.getTabellen(revision).stream().filter(tab -> tab.revision().revision == revision).collect(Collectors.toList());
		if ((tabs == null) || (tabs.size() <= 0)) {
			logger.logLn(0, "0 Tabellen");
			return true;
		}
		boolean result = true;
		logger.logLn(0, tabs.size() + " Tabellen...");
		logger.modifyIndent(2);
		
		var dbms = conn.getDBDriver();
		for (SchemaTabelle tab : tabs) {
			String sql = tab.getSQL(dbms, revision);
			if ((sql == null) || "".equals(sql))
				continue;
			logger.logLn(tab.name());
			if (conn.executeNativeUpdate(sql) == Integer.MIN_VALUE) {
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
		logger.modifyIndent(-2);
		return result;
	}
	
	
	/**
	 * Erstellt die neuen Views, die mit der angegebenen Revision hinzugekommen sind.
	 * 
	 * @param revision   die Revision, mit der die neuen Views hinzugekommen sind.
	 *   
	 * @return true, false alle entsprechenden Views erfolgreich angelegt wurden 
	 *         oder keine angelegt werden müssen, sonst false
	 */
	private boolean createNewViews(long revision) {
		DBEntityManager conn = schemaManager.getEntityManager();
		logger.log("- Erstelle: ");
		if (revision == 0) {
			logger.logLn(0, "Fehler: Eine Aktualisierung auf Revision 0 ergibt keinen Sinn, weshalb keine Views erstellt werden.");
			return false;
		}
		List<View> views = DBSchemaViews.getInstance().getViewsCreated(revision);
		if ((views == null) || (views.size() <= 0)) {
			logger.logLn(0, "0 Tabellen");
			return true;
		}
		boolean result = true;
		logger.logLn(0, views.size() + " Tabellen...");
		logger.modifyIndent(2);
		for (View view : views) {
			String sql = view.getSQLCreate(conn.getDBDriver());
			if ((sql == null) || "".equals(sql))
				continue;
			logger.logLn(view.name);
			if (conn.executeNativeUpdate(sql) == Integer.MIN_VALUE) {
				result = false;
				if (returnOnError)
					break;
			}
		}
		logger.modifyIndent(-2);
		return result;
	}
	
	
	/**
	 * Erstellt die neuen Tabellenspalten, die mit der angegebenen Revision hinzugekommen sind.
	 * 
	 * @param revision   die Revision, mit der die neuen Tabellenspalten hinzugekommen sind.
	 *   
	 * @return true, false alle entsprechenden Tabellenspalten erfolgreich angelegt wurden 
	 *         oder keine angelegt werden müssen, sonst false
	 */
	private boolean addNewColumns(long revision) {
		DBEntityManager conn = schemaManager.getEntityManager();
		logger.log("- Hinzufügen: ");
		List<SchemaTabelleSpalte> cols = Schema.getTabellen(revision).stream()
				.filter(tab -> tab.revision().revision < revision)
				.flatMap(tab -> tab.getSpalten().stream())
				.filter(col -> col.revision().revision == revision)
				.collect(Collectors.toList());
		if ((cols == null) || (cols.size() <= 0)) {
			logger.logLn(0, "0 Spalten");
			return true;
		}
		boolean result = true;
		logger.logLn(0, cols.size() + " Spalten...");
		logger.modifyIndent(2);
		var dbms = conn.getDBDriver();
		for (SchemaTabelleSpalte col : cols) {
			String sql = col.getSQLCreate(dbms);
			if ((sql == null) || ("".equals(sql))) {
				logger.logLn("Kann Spalte " + col.tabelle().name() + "." + col.name() + " nicht hinzufügen!");
				continue;
			}
			logger.logLn(col.tabelle().name() + "." + col.name());
			if (conn.executeNativeUpdate(sql) == Integer.MIN_VALUE) {
				result = false;
				if (returnOnError)
					break;
			}
		}
		logger.modifyIndent(-2);
		return result;
	}
	
	
	/**
	 * Erstellet die neuen Fremdschlüssel, die mit der angegebenen Revision hinzugekommen sind.
	 * 
	 * @param revision   die Revision, mit der die neuen Fremdschlüssel hinzugekommen sind.
	 *   
	 * @return true, false alle entsprechenden Fremdschlüssel erfolgreich angelegt wurden 
	 *         oder keine angelegt werden müssen, sonst false
	 */
	private boolean addNewForeignKeys(long revision) {
		DBEntityManager conn = schemaManager.getEntityManager();
		logger.log("- Hinzufügen: ");
		List<SchemaTabelleFremdschluessel> fks = Schema.getTabellen(revision).stream()
				.filter(tab -> tab.revision().revision < revision)
				.flatMap(tab -> tab.fremdschluessel().stream())
				.filter(fk -> fk.revision().revision == revision)
				.collect(Collectors.toList());
		if ((fks == null) || (fks.size() <= 0)) {
			logger.logLn(0, "0 Fremdschlüssel");
			return true;
		}
		boolean result = true;
		logger.logLn(0, fks.size() + " Fremdschlüssel...");
		logger.modifyIndent(2);
		var dbms = conn.getDBDriver();
		for (SchemaTabelleFremdschluessel fk : fks) {
			String sql = fk.getSQLCreate(dbms);
			if ((sql == null) || ("".equals(sql))) {
				logger.logLn("Kann Fremdschlüssel " + fk.name() + " nicht hinzufügen!");
				continue;
			}
			logger.logLn(fk.name());
			if (conn.executeNativeUpdate(sql) == Integer.MIN_VALUE) {
				result = false;
				if (returnOnError)
					break;
			}
		}
		logger.modifyIndent(-2);
		return result;
	}
	
	
	/**
	 * Erstellet die neuen Unique-Constraints, die mit der angegebenen Revision hinzugekommen sind.
	 * 
	 * @param revision   die Revision, mit der die neuen Unique-Constraints hinzugekommen sind.
	 *   
	 * @return true, false alle entsprechenden Unique-Constraints erfolgreich angelegt wurden 
	 *         oder keine angelegt werden müssen, sonst false
	 */
	private boolean addNewUniqueConstraints(long revision) {
		DBEntityManager conn = schemaManager.getEntityManager();
		logger.log("- Hinzufügen: ");
		List<SchemaTabelleUniqueIndex> ucs = Schema.getTabellen(revision).stream()
				.filter(tab -> tab.revision().revision < revision)
				.flatMap(tab -> tab.unique().stream())
				.filter(uc -> uc.revision().revision == revision)
				.collect(Collectors.toList());
		if ((ucs == null) || (ucs.size() <= 0)) {
			logger.logLn(0, "0 Unique-Constraints");
			return true;
		}
		boolean result = true;
		logger.logLn(0, ucs.size() + " Unique-Constraints...");
		logger.modifyIndent(2);
		var dbms = conn.getDBDriver();
		for (SchemaTabelleUniqueIndex uc : ucs) {
			String sql = uc.getSQLCreate(dbms);
			if ((sql == null) || ("".equals(sql))) {
				logger.logLn("Kann Unique-Constraint " + uc.name() + " nicht hinzufügen!");
				continue;
			}
			logger.logLn(uc.name());
			if (conn.executeNativeUpdate(sql) == Integer.MIN_VALUE) {
				result = false;
				if (returnOnError)
					break;
			}
		}
		logger.modifyIndent(-2);
		return result;
	}
	
	
	/**
	 * Erstellet die neuen Indizes, die mit der angegebenen Revision hinzugekommen sind.
	 * 
	 * @param revision   die Revision, mit der die neuen Indizes hinzugekommen sind.
	 *   
	 * @return true, false alle entsprechenden Indizes erfolgreich angelegt wurden 
	 *         oder keine angelegt werden müssen, sonst false
	 */
	private boolean addNewIndices(long revision) {
		DBEntityManager conn = schemaManager.getEntityManager();
		logger.log("- Hinzufügen: ");
		List<SchemaTabelleIndex> indizes = Schema.getTabellen(revision).stream()
				.filter(tab -> tab.revision().revision < revision)
				.flatMap(tab -> tab.indizes().stream())
				.filter(idx -> idx.revision().revision == revision)
				.collect(Collectors.toList());
		if ((indizes == null) || (indizes.size() <= 0)) {
			logger.logLn(0, "0 Indizes");
			return true;
		}
		boolean result = true;
		logger.logLn(0, indizes.size() + " Indizes...");
		logger.modifyIndent(2);
		for (SchemaTabelleIndex idx : indizes) {
			String sql = idx.getSQL();
			if ((sql == null) || ("".equals(sql))) {
				logger.logLn("Kann Index " + idx.name() + " nicht hinzufügen!");
				continue;
			}
			logger.logLn(idx.name());
			if (conn.executeNativeUpdate(sql) == Integer.MIN_VALUE) {
				result = false;
				if (returnOnError)
					break;
			}
		}
		logger.modifyIndent(-2);
		return result;
	}
	
	
	/**
	 * Führt die manuellen SQL-Befehle für die angegebene Revision aus.
	 * 
	 * @param revision   die Revision der manuellen SQL-Befehle, die ausgeführt werden sollen
	 *   
	 * @return true, false alle manuelle SQL-Befehle erfolgreich ausgeführt wurden 
	 *         oder keine ausgeführt werden müssen, sonst false
	 */
	private boolean executeManualSQLCommands(long revision) {
		DBEntityManager conn = schemaManager.getEntityManager();
		SchemaRevisionen rev = SchemaRevisionen.get(revision);
		if (rev == null) {
			logger.log("- Fehler beim Ermitteln der Schema-Revision " + revision);
			return false;
		}
		var sqlBefehle = rev.getUpdater();
		if (sqlBefehle == null) {
			logger.log("- Fehler beim Ermitteln der SQL-Befehle für die Revision " + revision);
			return false;
		}
		logger.log("- Ausführen: ");
		if (sqlBefehle.size() == 0) {
			logger.logLn(0, "0 Befehle");
			return true;
		}
		
		boolean result = true;
		logger.logLn(0, sqlBefehle.size() + " Befehle...");
		logger.modifyIndent(2);
		for (int i = 0; i < sqlBefehle.size(); i++) {
			String comment = sqlBefehle.getKommentar(i);
			String sql = sqlBefehle.getSQL(conn.getDBDriver(), i); 
			if ((comment == null) || (sql == null) || ("".equals(sql))) {
				logger.logLn("Kann Befehl nicht ausführen!");
				continue;
			}
			logger.log((i+1) + " - " + comment);
			int success = conn.executeNativeUpdate(sql);
			if (success == Integer.MIN_VALUE) {
				logger.logLn(0, "ERROR");
				result = false;
				if (returnOnError)
					break;
			} else {
				logger.logLn(0, " (" + success + ")");
			}
		}
		logger.modifyIndent(-2);
		return result;
	}
	
	
	/**
	 * Erstellet die neuen Trigger, die mit der angegebenen Revision hinzugekommen sind.
	 * 
	 * @param revision   die Revision, mit der die neuen Trigger hinzugekommen sind.
	 *   
	 * @return true, false alle entsprechenden Trigger erfolgreich angelegt wurden 
	 *         oder keine angelegt werden müssen, sonst false
	 */
	private boolean createNewTrigger(long revision) {
		DBEntityManager conn = schemaManager.getEntityManager();
		logger.log("- Erstelle: ");
		var dbms = conn.getDBDriver();
		List<SchemaTabelleTrigger> trigger = Schema.tabellen.values().stream()
				.flatMap(tab -> tab.trigger().stream())
				.filter(trig -> trig.dbms() == dbms)
				.filter(trig -> trig.revision().revision == revision)
				.collect(Collectors.toList());
		if ((trigger == null) || (trigger.size() <= 0)) {
			logger.logLn(0, "0 Trigger");
			return true;
		}
		boolean result = true;
		logger.logLn(0, trigger.size() + " Trigger...");
		logger.modifyIndent(2);
		for (SchemaTabelleTrigger trig : trigger) {
			String sql = trig.getSQL(conn.getDBDriver(), true);
			if ((sql == null) || ("".equals(sql))) {
				logger.logLn("Kann Trigger " + trig.name() + " nicht erstellen!");
				continue;
			}
			logger.logLn(trig.name());
			if (conn.executeWithJDBCConnection(sql) == Integer.MIN_VALUE) {
				result = false;
				if (returnOnError)
					break;
			}
		}
		logger.modifyIndent(-2);
		return result;
	}

	
}
