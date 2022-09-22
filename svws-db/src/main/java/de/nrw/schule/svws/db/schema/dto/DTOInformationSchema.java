package de.nrw.schule.svws.db.schema.dto;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.nrw.schule.svws.db.DBEntityManager;
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
@NamedNativeQuery(name="DTOInformationSchema.mysql", query="SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA")
@NamedNativeQuery(name="DTOInformationSchema.mdb", query="SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA")
@NamedNativeQuery(name="DTOInformationSchema.mssql", query="SELECT name AS SCHEMA_NAME FROM master.sys.databases")
@NamedNativeQuery(name="DTOInformationSchema.sqlite", query="SELECT 'master' AS SCHEMA_NAME")
public class DTOInformationSchema {
	
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
	public static Map<String, DTOInformationSchema> query(DBEntityManager conn) {
		List<DTOInformationSchema> results = null;
		switch (conn.getDBDriver()) {
			case MARIA_DB:
			case MYSQL:
				results = conn.queryNamed("DTOInformationSchema.mysql", DTOInformationSchema.class).getResultList();
				break;
			case MDB:
				results = conn.queryNamed("DTOInformationSchema.mdb", DTOInformationSchema.class).getResultList();
				break;
			case MSSQL:
				results = conn.queryNamed("DTOInformationSchema.mssql", DTOInformationSchema.class).getResultList();
				break;
			case SQLITE:
				results = conn.queryNamed("DTOInformationSchema.sqlite", DTOInformationSchema.class).getResultList();
				break;
		}
		if (results == null)
			return null;
		return results.stream().collect(Collectors.toMap(e -> e.Name.toLowerCase(), e -> e));
	}


	/**
	 * Stellt eine Anfrage nach den Namen in Kleinschreibung (!) nach aller Schemata. Hierbei werden alle 
	 * Schemata ignoriert, die vom DBMS vorgegeben sind.
	 * 
	 * @param conn   die Datenbankverbindung
	 * 
	 * @return die Liste mit den Schemanamen in Kleinschreibung
	 */
	@SuppressWarnings("unchecked")
	public static List<String> queryNames(DBEntityManager conn) {
		switch (conn.getDBDriver()) {
			case MARIA_DB:
				return conn.queryNamed("DTOInformationSchema.mysql", String.class).getResultList().stream().filter( name -> {
					switch (name.toLowerCase()) {
						case "information_schema":
						case "mysql":
						case "performance_schema":
							return false;
					}
					return true;
				}).map(name -> name.toLowerCase()).collect(Collectors.toList());
			case MYSQL:
				return conn.queryNamed("DTOInformationSchema.mysql", String.class).getResultList().stream().filter( name -> {
					switch (name.toLowerCase()) {
						case "information_schema":
						case "mysql":
						case "performance_schema":
						case "sys":
							return false;
					}
					return true;
				}).map(name -> name.toLowerCase()).collect(Collectors.toList());
			case MDB:
				return Collections.EMPTY_LIST;
			case MSSQL:
				return conn.queryNamed("DTOInformationSchema.mssql", String.class).getResultList().stream().filter( name -> {
					switch (name) {
						case "master":
						case "tempdb":
						case "model":
						case "msdb":
							return false;
					}
					return true;
				}).map(name -> name.toLowerCase()).collect(Collectors.toList());
			case SQLITE:
				return Collections.EMPTY_LIST;
		}
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOInformationSchema other = (DTOInformationSchema) obj;
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
