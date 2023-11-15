package de.svws_nrw.db.schema.dto;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.svws_nrw.db.DBEntityManager;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.Table;


/**
 * Diese Klasse definiert ein DTO für Informationen des DBMS zu den Schemata der Datenbank.
 */
@Entity
@Table(name = "INFORMATION_SCHEMA.TABLES")
@Cacheable(DBEntityManager.use_db_caching)
@NamedNativeQuery(name = "DTOInformationSchema.mysql", query = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA")
@NamedNativeQuery(name = "DTOInformationSchema.mdb", query = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA")
@NamedNativeQuery(name = "DTOInformationSchema.mssql", query = "SELECT name AS SCHEMA_NAME FROM master.sys.databases")
@NamedNativeQuery(name = "DTOInformationSchema.sqlite", query = "SELECT 'master' AS SCHEMA_NAME")
public final class DTOInformationSchema {

	/** Der Name des Datenbank-Schemas */
	@Id
	@Column(name = "SCHEMA_NAME")
	private String Name;

	/**
	 * Default-Konstruktor für das Erzeugen dieser DBEntity
	 */
	private DTOInformationSchema() {
	}


	/**
	 * Gibt den Namen des Schemas zurück.
	 *
	 * @return der Name des Schemas
	 */
	public String getName() {
		return Name;
	}


	/**
	 * Stellt eine Anfrage nach allen Schemata.
	 *
	 * @param conn   die Datenbankverbindung
	 *
	 * @return die Map mit den Schemata-DTOs, welche den Schema-Namen in Kleinschreibung zugeordnet sind.
	 */
	public static Map<String, DTOInformationSchema> query(final DBEntityManager conn) {
		final List<DTOInformationSchema> results = switch (conn.getDBDriver()) {
			case MARIA_DB, MYSQL -> conn.queryNamed("DTOInformationSchema.mysql", DTOInformationSchema.class).getResultList();
			case MDB -> conn.queryNamed("DTOInformationSchema.mdb", DTOInformationSchema.class).getResultList();
			case MSSQL -> conn.queryNamed("DTOInformationSchema.mssql", DTOInformationSchema.class).getResultList();
			case SQLITE -> conn.queryNamed("DTOInformationSchema.sqlite", DTOInformationSchema.class).getResultList();
			default -> Collections.emptyList();
		};
		return results.stream().collect(Collectors.toMap(e -> e.Name.toLowerCase(), e -> e));
	}


	private static final Set<String> setSystemSchemaMariaDB = Stream.of("information_schema", "mysql", "performance_schema", "sys").collect(Collectors.toCollection(HashSet::new));
	private static final Set<String> setSystemSchemaMySQL = Stream.of("information_schema", "mysql", "performance_schema", "sys").collect(Collectors.toCollection(HashSet::new));
	private static final Set<String> setSystemSchemaMSSQL = Stream.of("master", "tempdb",  "model", "msdb").collect(Collectors.toCollection(HashSet::new));


	/**
	 * Stellt eine Anfrage nach den Namen in Kleinschreibung (!) nach aller Schemata. Hierbei werden alle
	 * Schemata ignoriert, die vom DBMS vorgegeben sind.
	 *
	 * @param conn   die Datenbankverbindung
	 *
	 * @return die Liste mit den Schemanamen in Kleinschreibung
	 */
	public static List<String> queryNames(final DBEntityManager conn) {
		return switch (conn.getDBDriver()) {
			case MARIA_DB -> conn.queryNamed("DTOInformationSchema.mysql", String.class).getResultList().stream()
				.filter(name -> !setSystemSchemaMariaDB.contains(name.toLowerCase())).toList();
			case MYSQL -> conn.queryNamed("DTOInformationSchema.mysql", String.class).getResultList().stream()
				.filter(name -> !setSystemSchemaMySQL.contains(name.toLowerCase())).toList();
			case MSSQL -> conn.queryNamed("DTOInformationSchema.mssql", String.class).getResultList().stream()
				.filter(name -> !setSystemSchemaMSSQL.contains(name.toLowerCase())).toList();
			case SQLITE, MDB -> Collections.emptyList();
			default -> Collections.emptyList();
		};
	}


	/**
	 * Prüft, ob das angegenene Schema in der Datenbank existiert oder nicht.
	 *
	 * @param conn     die Datenbank-Verbindung
	 * @param schema   der Name des Schemas, welches geprüft wird
	 *
	 * @return true, falls ein Schema mit diesem Namen existiert, und ansonsten false
	 */
	public static boolean hasSchemaIgnoreCase(final DBEntityManager conn, final String schema) {
		final List<String> schemata = queryNames(conn);
		for (final String s : schemata)
			if (s.equalsIgnoreCase(schema))
				return true;
		return false;
	}


	/**
	 * Gibt für das angegebene Schema den Namen mit dem korrekten case zurück. Existiert das Schema nicht,
	 * so wird null zurückgegeben.
	 *
	 * @param conn     die Datenbank-Verbindung
	 * @param schema   der Name des Schemas, welches geprüft wird
	 *
	 * @return Der Schemaname mit dem korrekten case, falls ein Schema mit diesem Namen existiert, und ansonsten null
	 */
	public static String getSchemanameCaseDB(final DBEntityManager conn, final String schema) {
		final List<String> schemata = queryNames(conn);
		for (final String s : schemata)
			if (s.equalsIgnoreCase(schema))
				return s;
		return null;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Name == null) ? 0 : Name.hashCode());
		return result;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DTOInformationSchema other = (DTOInformationSchema) obj;
		if (Name == null) {
			if (other.Name != null)
				return false;
		} else if (!Name.equals(other.Name))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "DTOInformationSchema [Name=" + Name + "]";
	}

}
