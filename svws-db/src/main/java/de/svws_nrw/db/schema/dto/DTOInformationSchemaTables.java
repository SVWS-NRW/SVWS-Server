package de.svws_nrw.db.schema.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.db.DBEntityManager;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.Table;


/**
 * Diese Klasse definiert ein DTO für Informationen des DBMS zu den Tabellen.
 */
@Entity
@Table(name = "INFORMATION_SCHEMA.TABLES")
@Cacheable(DBEntityManager.use_db_caching)
@NamedNativeQuery(name = "DTOInformationSchemaTables.mysql", query = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA=? AND TABLE_TYPE='BASE TABLE'")
@NamedNativeQuery(name = "DTOInformationSchemaTables.mdb", query = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_CATALOG='PUBLIC' AND TABLE_SCHEMA='PUBLIC'")
@NamedNativeQuery(name = "DTOInformationSchemaTables.mssql", query = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_CATALOG=? AND TABLE_SCHEMA='dbo' AND TABLE_TYPE='BASE TABLE'")
@NamedNativeQuery(name = "DTOInformationSchemaTables.sqlite", query = "SELECT name AS TABLE_NAME FROM sqlite_master WHERE type='table'")
public final class DTOInformationSchemaTables {

	/** Der Name der Datenbank-Tabelle */
	@Id
	@Column(name = "TABLE_NAME")
	private String Name;


	/**
	 * Default-Konstruktor für das Erzeugen dieser DBEntity
	 */
	private DTOInformationSchemaTables() {
	}


	/**
	 * Gibt den Tabellennamen zurück.
	 *
	 * @return der Tabellenname
	 */
	public String getName() {
		return Name;
	}


	/**
	 * Stellt eine Anfrage nach allen Datenbank-Tabellen des angebenenen Datenbank-Schemas.
	 *
	 * @param conn   die Datenbankverbindung
	 *
	 * @return die Map mit den Datenbank-Tabellen-DTOs, welche den Tabellen-Namen zugeordnet sind.
	 */
	public static Map<String, DTOInformationSchemaTables> query(final DBEntityManager conn) {
		final List<DTOInformationSchemaTables> results = switch (conn.getDBDriver()) {
			case MARIA_DB, MYSQL -> conn.queryNamed("DTOInformationSchemaTables.mysql", DTOInformationSchemaTables.class)
					.setParameter(1, conn.getDBSchema())
					.getResultList();
			case MDB -> conn.queryNamed("DTOInformationSchemaTables.mdb", DTOInformationSchemaTables.class).getResultList();
			case MSSQL -> conn.queryNamed("DTOInformationSchemaTables.mssql", DTOInformationSchemaTables.class)
					.setParameter(1, conn.getDBSchema())
					.getResultList();
			case SQLITE -> conn.queryNamed("DTOInformationSchemaTables.sqlite", DTOInformationSchemaTables.class).getResultList();
			default -> new ArrayList<>();
		};
		return results.stream().collect(Collectors.toMap(e -> e.Name, e -> e));
	}


	/**
	 * Stellt eine Anfrage nach den Namen aller Tabellen des angebenenen Datenbank-Schemas.
	 *
	 * @param conn   die Datenbankverbindung
	 *
	 * @return die Liste mit den Tabellennamen
	 */
	public static List<String> queryNames(final DBEntityManager conn) {
		return switch (conn.getDBDriver()) {
			case MARIA_DB, MYSQL -> conn.queryNamed("DTOInformationSchemaTables.mysql", String.class)
					.setParameter(1, conn.getDBSchema())
					.getResultList();
			case MDB -> conn.queryNamed("DTOInformationSchemaTables.mdb", String.class).getResultList();
			case MSSQL -> conn.queryNamed("DTOInformationSchemaTables.mssql", String.class)
					.setParameter(1, conn.getDBSchema())
					.getResultList();
			case SQLITE -> conn.queryNamed("DTOInformationSchemaTables.sqlite", String.class).getResultList();
			default -> new ArrayList<>();
		};
	}



	/**
	 * Stellt eine Anfrage nach den Namen aller Tabellen des angebenenen Datenbank-Schemas.
	 *
	 * @param conn   die Datenbankverbindung
	 * @param schemaName   der Name des zu prüfenden Schemas
	 *
	 * @return die Liste mit den Tabellennamen
	 */
	public static List<String> queryNames(final DBEntityManager conn, final String schemaName) {
		return switch (conn.getDBDriver()) {
			case MARIA_DB, MYSQL -> conn.queryNamed("DTOInformationSchemaTables.mysql", String.class)
					.setParameter(1, schemaName)
					.getResultList();
			case MDB -> (!"PUBLIC".equalsIgnoreCase(schemaName))
					? Collections.emptyList()
					: conn.queryNamed("DTOInformationSchemaTables.mdb", String.class).getResultList();
			case MSSQL -> conn.queryNamed("DTOInformationSchemaTables.mssql", String.class)
					.setParameter(1, schemaName)
					.getResultList();
			case SQLITE -> ((schemaName == null) || (!"".equalsIgnoreCase(schemaName)) || (!"master".equalsIgnoreCase(schemaName)))
					? Collections.emptyList()
					: conn.queryNamed("DTOInformationSchemaTables.sqlite", String.class).getResultList();
			default -> new ArrayList<>();
		};
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
		final DTOInformationSchemaTables other = (DTOInformationSchemaTables) obj;
		if (Name == null) {
			if (other.Name != null)
				return false;
		} else if (!Name.equals(other.Name))
			return false;
		return true;
	}



	@Override
	public String toString() {
		return "DTOInformationSchemaTables [Name=" + Name + "]";
	}



}
