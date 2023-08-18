package de.svws_nrw.db.utils.schema;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.DBException;
import de.svws_nrw.db.schema.DBSchemaViews;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;
import de.svws_nrw.db.schema.SchemaTabelle;
import de.svws_nrw.db.schema.SchemaTabelleFremdschluessel;
import de.svws_nrw.db.schema.SchemaTabelleIndex;
import de.svws_nrw.db.schema.SchemaTabelleSpalte;
import de.svws_nrw.db.schema.SchemaTabelleTrigger;
import de.svws_nrw.db.schema.SchemaTabelleUniqueIndex;
import de.svws_nrw.db.schema.View;

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
	DBUpdater(final DBSchemaManager schemaManager, final boolean returnOnError) {
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
	 * @param conn            die Datenbankverbindung
	 * @param neue_revision   die nächste (!) Revision, auf die aktualisiert wird.
	 *
	 * @return true, falls ein update erfolgreich durchgeführt wurde, sonst false - d.h.
	 *         auch dann false, falls kein Update notwendig ist.
	 */
	private boolean performUpdate(final DBEntityManager conn, final long neue_revision) {
		boolean success = true;
		try {
			// 1. Update-Schritt: DROP_TRIGGER
			if (!dropTrigger(conn, neue_revision))
				throw new DBException("Fehler beim Verwerfen der Trigger");

			// 2. Update-Schritt: DROP_INDICES
			if (!dropIndices(conn, neue_revision))
				throw new DBException("Fehler beim Verwerfen der Indizes");

			// 3. Update-Schritt: DROP_UNIQUE_CONSTRAINTS
			if (!dropUniqueConstraints(conn, neue_revision))
				throw new DBException("Fehler beim Verwerfen der Unique-Constraints");

			// 4. Update-Schritt: DROP_FOREIGN_KEYS
			if (!dropForeignKeys(conn, neue_revision))
				throw new DBException("Fehler beim Verwerfen der Fremdschlüssel");

			// 5. Update-Schritt: CREATE_TABLES
			if (!createNewTables(conn, neue_revision))
				throw new DBException("Fehler beim Erstellen der neuen Tabellen");

			// 6. Update-Schritt: ADD_COLUMNS
			if (!addNewColumns(conn, neue_revision))
				throw new DBException("Fehler beim Hinzufügen der neuen Tabellenspalten");

			// 7. Update-Schritt: Core-Type-Updates
			if (!coreTypes.update(conn, false, neue_revision))
				throw new DBException("Fehler beim Aktualisieren der Core-Types");

			// 8. Update-Schritt: ManualSQL
			if (!executeManualSQLCommands(conn, neue_revision))
				throw new DBException("Fehler beim Ausführen der manuellen SQL-Befehle");

			// 9. Update-Schritt: ADD_FOREIGN_KEYS
			if (!addNewForeignKeys(conn, neue_revision))
				throw new DBException("Fehler beim Hinzufügen der neuen Fremdschlüssel");

			// 10. Update-Schritt: ADD_UNIQUE_CONSTRAINTS
			if (!addNewUniqueConstraints(conn, neue_revision))
				throw new DBException("Fehler beim Hinzufügen der neuen Unique-Constraints");

			// 11. Update-Schritt: ADD_INDICES
			if (!addNewIndices(conn, neue_revision))
				throw new DBException("Fehler beim Hinzufügen der neuen Indizes");

			// 12. Update-Schritt: ADD_TRIGGER
			if (!createNewTrigger(conn, neue_revision))
				throw new DBException("Fehler beim Erstellen der neuen Trigger");

			// 13. Update-Schritt: ADD VIEWS
			if (!createNewViews(conn, neue_revision))
				throw new DBException("Fehler beim Erstellen der neuen Views");

			// 14. Update-Schritt: DROP VIEWS
			if (!dropViews(conn, neue_revision))
				throw new DBException("Fehler beim Verwerfen der Views");

			// 15. Update-Schritt: DROP_COLUMNS
			if (!dropColumns(conn, neue_revision))
				throw new DBException("Fehler beim Verwerfen veralteter Tabellenspalten");

			// 16. Update-Schritt: DROP_TABLES
			if (!dropTables(conn, neue_revision))
				throw new DBException("Fehler beim Verwerfen veralteter Tabellen");

			// 17. Update-Schritt: Tabelle Schema_Version aktualisieren
			logger.logLn("- Setze die DB-Revision auf " + neue_revision);
			if (!DBSchemaManager.transactionSetDBRevision(conn, neue_revision))
				throw new DBException("Fehler beim Setzen der SVWS-DB-Revision");
		} catch (@SuppressWarnings("unused") final DBException e) {
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
	public boolean isUptodate(final long maxUpdateRevision, final boolean devMode) {
		status.update();
		final DBSchemaVersion currentVersion = status.getVersion();
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
			final long revision = currentVersion.getRevision();
			if (revision == max_revision) {
				logger.logLn("Das Schema ist bereits in der angegebenen Revision.");
				return true;
			} else if (revision > max_revision) {
				logger.logLn("Warnung: Das Schema ist bereits aktueller, als in der Datenbank eingetragen. Es sollte eine aktuellere Version des SVWS-Server-Projektes verwendet werden!");
				return true;
			}
			return false;
		} catch (@SuppressWarnings("unused") final Exception e) {
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
	public boolean isUpdatable(final long maxUpdateRevision, final boolean devMode) {
		// Prüfe zunächst, ob ein Update möglich ist
		status.update();
		final DBSchemaVersion currentVersion = status.getVersion();
		long max_revision = devMode ? SchemaRevisionen.maxDeveloperRevision.revision : SchemaRevisionen.maxRevision.revision;
		if ((currentVersion == null) || (max_revision < 0))
			return false;
		if ((maxUpdateRevision >= 0) && (maxUpdateRevision < max_revision))
			max_revision = maxUpdateRevision;
		try {
			// Ist eine Aktualisierung überhaupt nötig, oder ist das Schema schon aktuell oder sogar aktueller?
			final long revision = currentVersion.getRevision();
			if (revision >= max_revision)
				return false;
		} catch (@SuppressWarnings("unused") final Exception e) {
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
	public boolean update(final long maxUpdateRevision, final boolean devMode, final boolean lockSchema) {
		// Sperre ggf. das Datenbankschema
		if ((lockSchema) && (!SVWSKonfiguration.get().lockSchema(schemaManager.getSchemaStatus().schemaName))) {
			logger.logLn("-> Update fehlgeschlagen! (Schema ist aktuell gesperrt und kann daher nicht aktualisiert werden)");
			return false;
		}

		boolean success = true;
		try (DBEntityManager conn = schemaManager.getUser().getEntityManager()) {
			try {
				conn.transactionBegin();
				// Prüfe zunächst, ob ein Update möglich ist
				status.update(conn);
				final DBSchemaVersion currentVersion = status.getVersion();
				long max_revision = devMode ? SchemaRevisionen.maxDeveloperRevision.revision : SchemaRevisionen.maxRevision.revision;
				if ((currentVersion == null) || (max_revision < 0))
					return false;
				if ((maxUpdateRevision >= 0) && (maxUpdateRevision < max_revision))
					max_revision = maxUpdateRevision;

				// Ist kein Update nötig, so war die Aktualisierung erfolgreich
				if (max_revision <= currentVersion.getRevisionOrDefault(0))
					return true;

				// Ermittle die nächste Revision, auf die aktualisiert werden soll
				for (long neue_revision = currentVersion.getRevisionOrDefault(0) + 1; neue_revision <= max_revision; neue_revision++) {
					logger.logLn("* Aktualisiere auf Revision " + neue_revision);
					logger.modifyIndent(2);
					success = performUpdate(conn, neue_revision);
					logger.modifyIndent(-2);
					if (!success)
						break;
				}

				if (success && (!conn.transactionCommit()))
					success = false;
			} catch (final Exception e) {
				e.printStackTrace();
				success = false;
			} finally {
				// Perform a rollback if necessary
				conn.transactionRollback();
			}
		}

		// Entsperre ggf. das Datenbankschema
		if ((lockSchema) && (!SVWSKonfiguration.get().unlockSchema(schemaManager.getSchemaStatus().schemaName))) {
			logger.logLn("-> Update evtl. fehlgeschlagen! (Fehler beim Freigeben des Datenbank-Schemas. Schema ist nicht gesperrt - dies wird an dieser Stelle nicht erwartet!)");
			return false;
		}
		return success;
	}


	/**
	 * Verwirft die Trigger, die mit der angegebenen Revision veraltet sind.
	 *
	 * @param conn       die Datenbankverbindung
	 * @param veraltet   die Revision, mit der die Trigger veraltet sein müssen.
	 *
	 * @return true, false alle entsprechenden Trigger erfolgreich verworfen wurden
	 *         oder keine verworfen werden müssen, sonst false
	 */
	private boolean dropTrigger(final DBEntityManager conn, final long veraltet) {
		logger.log("- Verwerfe: ");
		final var dbms = conn.getDBDriver();
		final List<SchemaTabelleTrigger> trigger = Schema.tabellen.values().stream()
				.flatMap(tab -> tab.trigger().stream())
				.filter(trig -> trig.veraltet().revision == veraltet)
				.toList();
		if ((trigger == null) || (trigger.isEmpty())) {
			logger.logLn(0, "0 Trigger");
			return true;
		}
		boolean result = true;
		logger.logLn(0, trigger.size() + " Trigger...");
		logger.modifyIndent(2);
		for (final SchemaTabelleTrigger trig : trigger) {
			final var sql = trig.getSQL(dbms, false);
			if ((sql == null) || ("".equals(sql)))
				continue;
			logger.logLn(trig.name());
			if (conn.transactionNativeUpdateAndFlush(sql) == Integer.MIN_VALUE) {
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
	 * @param conn       die Datenbankverbindung
	 * @param veraltet   die Revision, mit der die Indizes veraltet sind.
	 *
	 * @return true, false alle entsprechenden Indizes erfolgreich verworfen wurden
	 *         oder keine verworfen werden müssen, sonst false
	 */
	private boolean dropIndices(final DBEntityManager conn, final long veraltet) {
		logger.log("- Verwerfe: ");
		final List<SchemaTabelleIndex> indizesVeraltet = Schema.tabellen.values().stream()
				.flatMap(tab -> tab.indizes().stream())
				.filter(idx -> idx.veraltet().revision == veraltet)
				.toList();
		if ((indizesVeraltet == null) || (indizesVeraltet.isEmpty())) {
			logger.logLn(0, "0 Indizes");
			return true;
		}
		boolean result = true;
		logger.logLn(0, indizesVeraltet.size() + " Indizes...");
		logger.modifyIndent(2);
		final var dbms = conn.getDBDriver();
		for (final SchemaTabelleIndex idx : indizesVeraltet) {
			final String sql = idx.getSQLDrop(dbms);
			if ((sql == null) || ("".equals(sql))) {
				logger.logLn("Kann " + idx.name() + "nicht entfernen!");
				continue;
			}
			logger.logLn(idx.name());
			if (conn.transactionNativeUpdateAndFlush(sql) == Integer.MIN_VALUE) {
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
	 * @param conn       die Datenbankverbindung
	 * @param veraltet   die Revision, mit der die Unique-Constraints veraltet sind.
	 *
	 * @return true, false alle entsprechenden Unique-Constraints erfolgreich verworfen wurden
	 *         oder keine verworfen werden müssen, sonst false
	 */
	private boolean dropUniqueConstraints(final DBEntityManager conn, final long veraltet) {
		logger.log("- Verwerfe: ");
		final List<SchemaTabelleUniqueIndex> ucs = Schema.tabellen.values().stream()
				.flatMap(tab -> tab.unique().stream())
				.filter(uc -> uc.veraltet().revision == veraltet)
				.toList();
		if ((ucs == null) || (ucs.isEmpty())) {
			logger.logLn(0, "0 Unique-Constraints");
			return true;
		}
		boolean result = true;
		logger.logLn(0, ucs.size() + " Unique-Constraints...");
		logger.modifyIndent(2);
		final var dbms = conn.getDBDriver();
		for (final SchemaTabelleUniqueIndex uc : ucs) {
			final String sql = uc.getSQLDrop(dbms);
			if ((sql == null) || ("".equals(sql))) {
				logger.logLn("Kann " + uc.name() + "nicht entfernen!");
				continue;
			}
			logger.logLn(uc.name());
			if (conn.transactionNativeUpdateAndFlush(sql) == Integer.MIN_VALUE) {
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
	 * @param conn       die Datenbankverbindung
	 * @param veraltet   die Revision, mit der die Fremdschlüssel veraltet sind.
	 *
	 * @return true, false alle entsprechenden Fremdschlüssel erfolgreich verworfen wurden
	 *         oder keine verworfen werden müssen, sonst false
	 */
	private boolean dropForeignKeys(final DBEntityManager conn, final long veraltet) {
		logger.log("- Verwerfe: ");
		final List<SchemaTabelleFremdschluessel> fks = Schema.tabellen.values().stream()
				.flatMap(tab -> tab.fremdschluessel().stream())
				.filter(fk -> fk.veraltet().revision == veraltet)
				.toList();
		if ((fks == null) || (fks.isEmpty())) {
			logger.logLn(0, "0 Fremdschlüssel");
			return true;
		}
		boolean result = true;
		logger.logLn(0, fks.size() + " Fremdschlüssel...");
		logger.modifyIndent(2);
		final var dbms = conn.getDBDriver();
		for (final SchemaTabelleFremdschluessel fk : fks) {
			final String sql = fk.getSQLDrop(dbms);
			if ((sql == null) || ("".equals(sql))) {
				logger.logLn("Kann " + fk.name() + "nicht entfernen!");
				continue;
			}
			logger.logLn(fk.name());
			if (conn.transactionNativeUpdateAndFlush(sql) == Integer.MIN_VALUE) {
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
	 * @param conn       die Datenbankverbindung
	 * @param veraltet   die Revision, mit der die Tabellenspalten veraltet sind.
	 *
	 * @return true, false alle entsprechenden Tabellenspalten erfolgreich verworfen wurden
	 *         oder keine verworfen werden müssen, sonst false
	 */
	private boolean dropColumns(final DBEntityManager conn, final long veraltet) {
		logger.log("- Verwerfe: ");
		final List<SchemaTabelle> tabs = new ArrayList<>(Schema.getTabellen(veraltet - 1));
		Collections.reverse(tabs);
		final List<SchemaTabelleSpalte> cols = tabs.stream()
				.filter(tab -> tab.veraltet().revision != veraltet)
				.flatMap(tab -> tab.getSpalten().stream())
				.filter(col -> col.veraltet().revision == veraltet)
				.toList();
		if ((cols == null) || (cols.isEmpty())) {
			logger.logLn(0, "0 Spalten");
			return true;
		}
		boolean result = true;
		logger.logLn(0, cols.size() + " Spalten...");
		logger.modifyIndent(2);
		final var dbms = conn.getDBDriver();
		for (final SchemaTabelleSpalte col : cols) {
			final String sql = col.getSQLDrop(dbms);
			if ((sql == null) || ("".equals(sql))) {
				logger.logLn("Kann Spalte " + col.tabelle().name() + "." + col.name() + "nicht entfernen!");
				continue;
			}
			logger.logLn(col.tabelle().name() + "." + col.name());
			if (conn.transactionNativeUpdateAndFlush(sql) == Integer.MIN_VALUE) {
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
	 * @param conn       die Datenbankverbindung
	 * @param veraltet   die Revision, mit der die Tabellen veraltet ist.
	 *
	 * @return true, false alle entsprechenden Tabellen erfolgreich verworfen wurden
	 *         oder keine verworfen werden müssen, sonst false
	 */
	private boolean dropTables(final DBEntityManager conn, final long veraltet) {
		logger.log("- Verwerfe: ");
		final List<SchemaTabelle> tabs = Schema.getTabellen(veraltet - 1).stream()
				.filter(tab -> tab.veraltet().revision == veraltet)
				.toList();
		if ((tabs == null) || (tabs.isEmpty())) {
			logger.logLn(0, "0 Tabellen");
			return true;
		}
		boolean result = true;
		logger.logLn(0, tabs.size() + " Tabellen...");
		logger.modifyIndent(2);
		final var dbms = conn.getDBDriver();
		for (int i = tabs.size() - 1; i >= 0; i--) {
			final SchemaTabelle tab = tabs.get(i);
			final String sql = tab.getSQLDrop(dbms);
			logger.logLn(tab.name());
			if (conn.transactionNativeUpdateAndFlush(sql) == Integer.MIN_VALUE) {
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
	 * @param conn       die Datenbankverbindung
	 * @param veraltet   die Views, mit der die Tabellen veraltet sind.
	 *
	 * @return true, false alle entsprechenden Views erfolgreich verworfen wurden
	 *         oder keine verworfen werden müssen, sonst false
	 */
	private boolean dropViews(final DBEntityManager conn, final long veraltet) {
		logger.log("- Verwerfe: ");
		final List<View> views = DBSchemaViews.getInstance().getViewsDeprecated(veraltet);
		if ((views == null) || (views.isEmpty())) {
			logger.logLn(0, "0 Views");
			return true;
		}
		boolean result = true;
		logger.logLn(0, views.size() + " Views...");
		logger.modifyIndent(2);
		for (int i = views.size() - 1; i >= 0; i--) {
			final View view = views.get(i);
			final String sql = view.getSQLDrop();
			logger.logLn(view.name);
			if (conn.transactionNativeUpdateAndFlush(sql) == Integer.MIN_VALUE) {
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
	 * @param conn       die Datenbankverbindung
	 * @param revision   die Revision, mit die neuen Tabellen hinzugekommen sind.
	 *
	 * @return true, false alle entsprechenden Tabellen erfolgreich angelegt wurden
	 *         oder keine angelegt werden müssen, sonst false
	 */
	private boolean createNewTables(final DBEntityManager conn, final long revision) {
		logger.log("- Erstelle: ");
		if (revision == 0) {
			logger.logLn(0, "Fehler: Eine Aktualisierung auf Revision 0 ergibt keinen Sinn, weshalb keine Tabellen erstellt werden.");
			return false;
		}
		final List<SchemaTabelle> tabs = Schema.getTabellen(revision).stream().filter(tab -> tab.revision().revision == revision).toList();
		if ((tabs == null) || (tabs.isEmpty())) {
			logger.logLn(0, "0 Tabellen");
			return true;
		}
		boolean result = true;
		logger.logLn(0, tabs.size() + " Tabellen...");
		logger.modifyIndent(2);

		final var dbms = conn.getDBDriver();
		for (final SchemaTabelle tab : tabs) {
			final String sql = tab.getSQL(dbms, revision);
			if ((sql == null) || "".equals(sql))
				continue;
			logger.logLn(tab.name());
			if (conn.transactionNativeUpdateAndFlush(sql) == Integer.MIN_VALUE) {
				result = false;
				if (returnOnError)
					break;
				continue;
			}
			final List<String> pkTrigger = tab.getPrimaerschluesselTriggerSQLList(dbms, revision, true);
			if (!pkTrigger.isEmpty()) {
				logger.logLn("  -> Erstelle Trigger für Auto-Inkremente");
				for (final String scriptTrigger : pkTrigger) {
					if (conn.transactionNativeUpdateAndFlush(scriptTrigger) == Integer.MIN_VALUE) {
						result = false;
						if (returnOnError)
							break;
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
	 * @param conn       die Datenbankverbindung
	 * @param revision   die Revision, mit der die neuen Views hinzugekommen sind.
	 *
	 * @return true, false alle entsprechenden Views erfolgreich angelegt wurden
	 *         oder keine angelegt werden müssen, sonst false
	 */
	private boolean createNewViews(final DBEntityManager conn, final long revision) {
		logger.log("- Erstelle: ");
		if (revision == 0) {
			logger.logLn(0, "Fehler: Eine Aktualisierung auf Revision 0 ergibt keinen Sinn, weshalb keine Views erstellt werden.");
			return false;
		}
		final List<View> views = DBSchemaViews.getInstance().getViewsCreated(revision);
		if ((views == null) || (views.isEmpty())) {
			logger.logLn(0, "0 Tabellen");
			return true;
		}
		boolean result = true;
		logger.logLn(0, views.size() + " Tabellen...");
		logger.modifyIndent(2);
		for (final View view : views) {
			final String sql = view.getSQLCreate(conn.getDBDriver());
			if ((sql == null) || "".equals(sql))
				continue;
			logger.logLn(view.name);
			if (conn.transactionNativeUpdateAndFlush(sql) == Integer.MIN_VALUE) {
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
	 * @param conn       die Datenbankverbindung
	 * @param revision   die Revision, mit der die neuen Tabellenspalten hinzugekommen sind.
	 *
	 * @return true, false alle entsprechenden Tabellenspalten erfolgreich angelegt wurden
	 *         oder keine angelegt werden müssen, sonst false
	 */
	private boolean addNewColumns(final DBEntityManager conn, final long revision) {
		logger.log("- Hinzufügen: ");
		final List<SchemaTabelleSpalte> cols = Schema.getTabellen(revision).stream()
				.filter(tab -> tab.revision().revision < revision)
				.flatMap(tab -> tab.getSpalten().stream())
				.filter(col -> col.revision().revision == revision)
				.toList();
		if ((cols == null) || (cols.isEmpty())) {
			logger.logLn(0, "0 Spalten");
			return true;
		}
		boolean result = true;
		logger.logLn(0, cols.size() + " Spalten...");
		logger.modifyIndent(2);
		final var dbms = conn.getDBDriver();
		for (final SchemaTabelleSpalte col : cols) {
			final String sql = col.getSQLCreate(dbms);
			if ((sql == null) || ("".equals(sql))) {
				logger.logLn("Kann Spalte " + col.tabelle().name() + "." + col.name() + " nicht hinzufügen!");
				continue;
			}
			logger.logLn(col.tabelle().name() + "." + col.name());
			if (conn.transactionNativeUpdateAndFlush(sql) == Integer.MIN_VALUE) {
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
	 * @param conn       die Datenbankverbindung
	 * @param revision   die Revision, mit der die neuen Fremdschlüssel hinzugekommen sind.
	 *
	 * @return true, false alle entsprechenden Fremdschlüssel erfolgreich angelegt wurden
	 *         oder keine angelegt werden müssen, sonst false
	 */
	private boolean addNewForeignKeys(final DBEntityManager conn, final long revision) {
		logger.log("- Hinzufügen: ");
		final List<SchemaTabelleFremdschluessel> fks = Schema.getTabellen(revision).stream()
				.filter(tab -> tab.revision().revision < revision)
				.flatMap(tab -> tab.fremdschluessel().stream())
				.filter(fk -> fk.revision().revision == revision)
				.toList();
		if ((fks == null) || (fks.isEmpty())) {
			logger.logLn(0, "0 Fremdschlüssel");
			return true;
		}
		boolean result = true;
		logger.logLn(0, fks.size() + " Fremdschlüssel...");
		logger.modifyIndent(2);
		final var dbms = conn.getDBDriver();
		for (final SchemaTabelleFremdschluessel fk : fks) {
			final String sql = fk.getSQLCreate(dbms);
			if ((sql == null) || ("".equals(sql))) {
				logger.logLn("Kann Fremdschlüssel " + fk.name() + " nicht hinzufügen!");
				continue;
			}
			logger.logLn(fk.name());
			if (conn.transactionNativeUpdateAndFlush(sql) == Integer.MIN_VALUE) {
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
	 * @param conn       die Datenbankverbindung
	 * @param revision   die Revision, mit der die neuen Unique-Constraints hinzugekommen sind.
	 *
	 * @return true, false alle entsprechenden Unique-Constraints erfolgreich angelegt wurden
	 *         oder keine angelegt werden müssen, sonst false
	 */
	private boolean addNewUniqueConstraints(final DBEntityManager conn, final long revision) {
		logger.log("- Hinzufügen: ");
		final List<SchemaTabelleUniqueIndex> ucs = Schema.getTabellen(revision).stream()
				.filter(tab -> tab.revision().revision < revision)
				.flatMap(tab -> tab.unique().stream())
				.filter(uc -> uc.revision().revision == revision)
				.toList();
		if ((ucs == null) || (ucs.isEmpty())) {
			logger.logLn(0, "0 Unique-Constraints");
			return true;
		}
		boolean result = true;
		logger.logLn(0, ucs.size() + " Unique-Constraints...");
		logger.modifyIndent(2);
		final var dbms = conn.getDBDriver();
		for (final SchemaTabelleUniqueIndex uc : ucs) {
			final String sql = uc.getSQLCreate(dbms);
			if ((sql == null) || ("".equals(sql))) {
				logger.logLn("Kann Unique-Constraint " + uc.name() + " nicht hinzufügen!");
				continue;
			}
			logger.logLn(uc.name());
			if (conn.transactionNativeUpdateAndFlush(sql) == Integer.MIN_VALUE) {
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
	 * @param conn       die Datenbankverbindung
	 * @param revision   die Revision, mit der die neuen Indizes hinzugekommen sind.
	 *
	 * @return true, false alle entsprechenden Indizes erfolgreich angelegt wurden
	 *         oder keine angelegt werden müssen, sonst false
	 */
	private boolean addNewIndices(final DBEntityManager conn, final long revision) {
		logger.log("- Hinzufügen: ");
		final List<SchemaTabelleIndex> indizes = Schema.getTabellen(revision).stream()
				.filter(tab -> tab.revision().revision < revision)
				.flatMap(tab -> tab.indizes().stream())
				.filter(idx -> idx.revision().revision == revision)
				.toList();
		if ((indizes == null) || (indizes.isEmpty())) {
			logger.logLn(0, "0 Indizes");
			return true;
		}
		boolean result = true;
		logger.logLn(0, indizes.size() + " Indizes...");
		logger.modifyIndent(2);
		for (final SchemaTabelleIndex idx : indizes) {
			final String sql = idx.getSQL();
			if ((sql == null) || ("".equals(sql))) {
				logger.logLn("Kann Index " + idx.name() + " nicht hinzufügen!");
				continue;
			}
			logger.logLn(idx.name());
			if (conn.transactionNativeUpdateAndFlush(sql) == Integer.MIN_VALUE) {
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
	 * @param conn       die Datenbankverbindung
	 * @param revision   die Revision der manuellen SQL-Befehle, die ausgeführt werden sollen
	 *
	 * @return true, false alle manuelle SQL-Befehle erfolgreich ausgeführt wurden
	 *         oder keine ausgeführt werden müssen, sonst false
	 */
	private boolean executeManualSQLCommands(final DBEntityManager conn, final long revision) {
		final SchemaRevisionen rev = SchemaRevisionen.get(revision);
		if (rev == null) {
			logger.log("- Fehler beim Ermitteln der Schema-Revision " + revision);
			return false;
		}
		final SchemaRevisionUpdateSQL sqlBefehle = rev.getUpdater();
		if (sqlBefehle == null) {
			logger.log("- Fehler beim Ermitteln der SQL-Befehle für die Revision " + revision);
			return false;
		}
		logger.log("- Ausführen: ");
		logger.logLn(0, sqlBefehle.size() + " Befehle" + ((sqlBefehle.size() > 0) ? "..." : ""));
		logger.modifyIndent(2);
		if (!sqlBefehle.runFirst(conn, logger) && returnOnError) {
			logger.modifyIndent(-2);
			return false;
		}
		for (int i = 0; i < sqlBefehle.size(); i++) {
			final String comment = sqlBefehle.getKommentar(i);
			final String sql = sqlBefehle.getSQL(conn.getDBDriver(), i);
			if ((comment == null) || (sql == null) || ("".equals(sql))) {
				logger.logLn("Kann Befehl nicht ausführen!");
				continue;
			}
			logger.log((i + 1) + " - " + comment);
			final int success = conn.transactionNativeUpdateAndFlush(sql);
			if (success == Integer.MIN_VALUE) {
				logger.logLn(0, "ERROR");
				if (returnOnError) {
					logger.modifyIndent(-2);
					return false;
				}
			} else {
				logger.logLn(0, " (" + success + ")");
			}
		}
		if (!sqlBefehle.runLast(conn, logger) && returnOnError) {
			logger.modifyIndent(-2);
			return false;
		}
		logger.modifyIndent(-2);
		return true;
	}


	/**
	 * Erstellet die neuen Trigger, die mit der angegebenen Revision hinzugekommen sind.
	 *
	 * @param conn       die Datenbankverbindung
	 * @param revision   die Revision, mit der die neuen Trigger hinzugekommen sind.
	 *
	 * @return true, false alle entsprechenden Trigger erfolgreich angelegt wurden
	 *         oder keine angelegt werden müssen, sonst false
	 */
	private boolean createNewTrigger(final DBEntityManager conn, final long revision) {
		logger.log("- Erstelle: ");
		final var dbms = conn.getDBDriver();
		final List<SchemaTabelleTrigger> trigger = Schema.tabellen.values().stream()
				.flatMap(tab -> tab.trigger().stream())
				.filter(trig -> trig.dbms() == dbms)
				.filter(trig -> trig.revision().revision == revision)
				.toList();
		if ((trigger == null) || (trigger.isEmpty())) {
			logger.logLn(0, "0 Trigger");
			return true;
		}
		boolean result = true;
		logger.logLn(0, trigger.size() + " Trigger...");
		logger.modifyIndent(2);
		for (final SchemaTabelleTrigger trig : trigger) {
			final String sql = trig.getSQL(conn.getDBDriver(), true);
			if ((sql == null) || ("".equals(sql))) {
				logger.logLn("Kann Trigger " + trig.name() + " nicht erstellen!");
				continue;
			}
			logger.logLn(trig.name());
			if (conn.transactionExecuteWithJDBCConnection(sql) == Integer.MIN_VALUE) {
				result = false;
				if (returnOnError)
					break;
			}
			conn.transactionFlush();
		}
		logger.modifyIndent(-2);
		return result;
	}


}
