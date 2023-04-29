package de.svws_nrw.db.utils.schema;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.svws.db.DTOCoreTypeVersion;
import de.svws_nrw.db.dto.current.svws.db.DTODBVersion;
import de.svws_nrw.db.schema.dto.DTOInformationSchemaTableColumn;
import de.svws_nrw.db.schema.dto.DTOInformationSchemaTables;

/**
 * Diese Klasse stellt Methode für den Zugriff auf den aktuellen Status einen Schema zur Verfügung.
 *
 */
public final class DBSchemaStatus {

	/** Der Name des Schemas */
	final String schemaName;

	/** Der Datenbank-Benutzer, der für das Auslesen des Schema-Status verwendet wird. */
	final Benutzer user;

	/** Die Version des SVWS-Datenbank-Schemas */
	DBSchemaVersion version;

	/** Eine Liste der Tabellen in dem Datenbank-Schema */
	List<String> tabellen;

	/** Eine Liste mit den aktuellen Versionen der Core-Types in der Datenbank */
	Map<String, DTOCoreTypeVersion> coreTypeVersionen = null;


	/**
	 * Erzeugt ein neues Schema-Status-Objekt, indem mithilfe des übergebenen
	 * DB-Benutzers z.B. das Information-Schema der Datenbank abgefragt wird.
	 *
	 * @param user         der Datenbank-Benutzer für den Zugriff auf die Tabellen
	 * @param schemaName   der Name des Schemas, dessen Status abgefragt werden soll
	 */
	private DBSchemaStatus(final Benutzer user, final String schemaName) {
		this.user = user;
		this.schemaName = schemaName;
		update();
	}


	/**
	 * Liest den Schema-Status mithilfe des übergebenen DB-Benutzers aus. Dabei wird das
	 * Schema abgefragt, welches dem Benutzer zugeordnet ist
	 *
	 * @param user   der Datenbank-Benutzer für den Zugriff auf die Schema-Informationen
	 *
	 * @return der Schema-Status
	 */
	public static DBSchemaStatus read(final Benutzer user) {
		return new DBSchemaStatus(user, user.connectionManager.getConfig().getDBSchema());
	}


	/**
	 * Liest den Schema-Status mithilfe des übergebenen DB-Benutzers aus. Dabei wird das
	 * Schema mit dem übergebenen Namen abgefragt.
	 *
	 * @param user         der Datenbank-Benutzer für den Zugriff auf die Schema-Informationen
	 * @param schemaName   der Name des Schemas, dessen Status abgefragt werden soll
	 *
	 * @return der Schema-Status
	 */
	public static DBSchemaStatus read(final Benutzer user, final String schemaName) {
		return new DBSchemaStatus(user, schemaName);
	}


	/**
	 * Gibt die aktuelle Revision des Datenbankschemas zurück.
	 *
	 * @return die aktuelle Revision des Datenbankschemas
	 */
	public DBSchemaVersion getVersion() {
		return version;
	}


	/**
	 * Gibt die vorhandenen Tabellen im Datenbankschema zurück.
	 *
	 * @return die im Datenbankschema vorhandenen Tabellen
	 */
	public List<String> getTabellen() {
		return tabellen;
	}


	/**
	 * Aktualisiert den Schema-Status
	 */
	public void update() {
		try (DBEntityManager conn = user.getEntityManager()) {
			tabellen = DTOInformationSchemaTables.queryNames(conn, schemaName);
			version = leseDBSchemaVersion(conn);
			coreTypeVersionen = null; // Muss neu eingelesen werden... darf aber z.B. wegen Migrationen initial eingelesen werden
			// TODO
		}
	}


	/**
	 * Hilfsmethode zum Einlesen der Schema-Version aus der Datenbank. Dabei wird zunächst geprüft, ob überhaupt eine
	 * Tabelle mit den Versionsinformationen vorhanden ist und es sich somit überhaupt um eine SVWS-Server-Datenbank
	 * handelt.
	 *
	 * @param conn   die Datenbank-Verbindung zum Lesen der Schema-Version
	 *
	 * @return die Datenbank-Version
	 */
	private DBSchemaVersion leseDBSchemaVersion(final DBEntityManager conn) {
		if (tabellen.stream().filter(tabname -> "SVWS_DB_Version".equalsIgnoreCase(tabname)).findFirst().orElse(null) == null)
			return null;
		DTODBVersion dto;
		final DBDriver dbms = conn.getDBDriver();
		if ((!dbms.hasMultiSchemaSupport()) || (schemaName == null) || schemaName.equals(conn.getDBSchema())) {
			dto = conn.querySingle(DTODBVersion.class);
		} else {
			// Hole die Versions-Informationen aus einem fremden Schema. Hier wird natives SQL benötigt
			String sql;
			if ((dbms == DBDriver.MARIA_DB) || (dbms == DBDriver.MYSQL)) {
				sql = "SELECT * FROM `" + schemaName + "`.SVWS_DB_Version";
			} else if (dbms == DBDriver.MSSQL) {
				sql = "SELECT * FROM [" + schemaName + "].SVWS_DB_Version";
			} else {
				return null;
			}
			dto = conn.queryNative(sql, DTODBVersion.class).stream().findFirst().orElse(null);
		}
		Long revision = null;
		if ((dto != null) && (dto.Revision >= 0))
			revision = dto.Revision;
		final boolean isTainted = (dto == null) || dto.IsTainted;
		return new DBSchemaVersion(revision, isTainted);
	}


	/**
	 * Prüft, ob der angegebene Tabellenname in der Datenbank vorhanden ist.
	 * Dabei ist die Groß- und Klein-Schreibung nicht relevant.
	 *
	 * @param tabname   der zu prüfende Tabellenname
	 *
	 * @return true, falls die Tabelle vorhanden ist und ansonsten false
	 */
	public boolean hasTable(final String tabname) {
		for (final String nameTabelle : tabellen)
			if (nameTabelle.toLowerCase().equals(tabname.toLowerCase()))
				return true;
		return false;
	}


	/**
	 * Liefert die Version des Core-Types zurück,
	 * welcher in der übergebenen Tabelle gespeichert wird.
	 *
	 * @param tabname   der Name der Tabelle
	 *
	 * @return die Version des Core-Types oder null, wenn aktuell
	 *         keine Version in der Tabelle gespeichert ist
	 */
	public DTOCoreTypeVersion getCoreTypeVersion(final String tabname) {
		if (coreTypeVersionen == null) {
			try (DBEntityManager conn = user.getEntityManager()) {
				coreTypeVersionen = conn.queryAll(DTOCoreTypeVersion.class).stream()
					.collect(Collectors.toMap(dto -> dto.NameTabelle, dto -> dto));
			}
		}
		return coreTypeVersionen.get(tabname);
	}


	/**
	 * Diese Methode prüft, ob die angegebene Spealte bei der angebenen Tabelle vorhanden ist.
	 *
	 * @param tabname    der zu prüfende Tabellenname
	 * @param colname    der zu prüfende Spaltenname
	 *
	 * @return true, falls die Spalte bei der Tabelle vorhanden ist und ansonsten false
	 */
	public boolean hasColumn(final String tabname, final String colname) {
		if (!hasTable(tabname))
			return false;
		try (DBEntityManager conn = user.getEntityManager()) {
			final Map<String, DTOInformationSchemaTableColumn> spalten = DTOInformationSchemaTableColumn.query(conn, tabname);
			if (spalten == null)
				return false;
			return spalten.containsKey(colname.toLowerCase());
		}
	}


	/**
	 * Filtert die übergebene Liste der Spaltennamen der angegebenen Tabelle anhand der im Schema
	 * existierenden Spalten.
	 *
	 * @param tabname   der Name der Tabelle
	 * @param cols      eine Liste mit Spaltennamen, die gefiltert werden soll
	 *
	 * @return die gefilterte Liste von Spaltennamen
	 */
	public List<String> filterColumns(final String tabname, final List<String> cols) {
		if (!hasTable(tabname))
			return null;
		try (DBEntityManager conn = user.getEntityManager()) {
			final Map<String, DTOInformationSchemaTableColumn> spalten = DTOInformationSchemaTableColumn.query(conn, tabname);
			if (spalten == null)
				return null;
			return cols.stream().filter(col -> (spalten.containsKey(col.toLowerCase()))).collect(Collectors.toList());
		}
	}


}
