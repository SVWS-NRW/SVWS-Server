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
 * Diese Klasse definiert ein DTO für Informationen des DBMS zu den Datenbank-Benutzern. <br>
 * Wichtig: Dies entspricht nicht den SVWS-Benutzern!
 */
@Entity
@Table(name = "INFORMATION_SCHEMA.TABLES")
@Cacheable(DBEntityManager.use_db_caching)
@NamedNativeQuery(name="DTOInformationUser.mysql", query="SELECT DISTINCT User AS USER_NAME FROM mysql.user")
@NamedNativeQuery(name="DTOInformationUser.mssql", query="SELECT DISTINCT name AS USER_NAME FROM sys.sql_logins WHERE type='S'")
public class DTOInformationUser {
	
	/** Der Name Datenbank des Datenbank-Benutzers */
	@Id
	@Column(name = "USER_NAME")
	private String Name;	

	
	/**
	 * Default-Konstruktor für das Erzeugen dieser DBEntity 
	 */
	private DTOInformationUser() {
	}
	
	
	/**
	 * Gibt den Benutzernamen zurück.
	 * 
	 * @return   der Benutzername
	 */
	public String getName() {
		return Name;
	}


	/**
	 * Stellt eine Anfrage nach allen Benutzer der Datenbank.
	 *
	 * @param conn   die Datenbankverbindung
	 *
	 * @return die Map mit den Benutzer-DTOs, welche den Benutzer-Namen zugeordnet sind.
	 */
	public static Map<String, DTOInformationUser> query(DBEntityManager conn) {
		List<DTOInformationUser> results = null;
		switch (conn.getDBDriver()) {
			case MARIA_DB:
			case MYSQL:
				results = conn.queryNamed("DTOInformationUser.mysql", DTOInformationUser.class).getResultList();
				break;
			case MSSQL:
				results = conn.queryNamed("DTOInformationUser.mssql", DTOInformationUser.class).getResultList();
				break;
			case MDB:
			case SQLITE:
				results = Collections.emptyList();
				break;
		}
		if (results == null)
			return null;
		return results.stream().collect(Collectors.toMap(e -> e.Name, e -> e));
	}


	/**
	 * Stellt eine Anfrage nach den Namen nach aller Benutzer der Datenbank. Hierbei werden alle Benutzer ignoriert, die vom DBMS
	 * vorgegeben sind.
	 * 
	 * @param conn   die Datenbankverbindung
	 * 
	 * @return die Liste mit den Benutzernamen
	 */
	public static List<String> queryNames(DBEntityManager conn) {
		switch (conn.getDBDriver()) {
			case MARIA_DB:
				return conn.queryNamed("DTOInformationUser.mysql", String.class).getResultList().stream().filter( name -> {
					switch (name) {
						case "root":
							return false;
					}
					return true;
				}).collect(Collectors.toList());
			case MYSQL:
				return conn.queryNamed("DTOInformationUser.mysql", String.class).getResultList().stream().filter( name -> {
					switch (name) {
						case "root":
						case "mysql.infoschema":
						case "mysql.session":
						case "mysql.sys":
							return false;
					}
					return true;
				}).collect(Collectors.toList());
			case MSSQL:
				return conn.queryNamed("DTOInformationUser.mssql", String.class).getResultList().stream().filter( name -> {
						switch (name) {
						case "sa":
						case "##MS_PolicyTsqlExecutionLogin##":
						case "##MS_PolicyEventProcessingLogin##":
							return false;
					}
					return true;
				}).collect(Collectors.toList());
			case MDB:
			case SQLITE:
				return Collections.emptyList();
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
		DTOInformationUser other = (DTOInformationUser) obj;
		if (Name == null) {
			if (other.Name != null)
				return false;
		} else if (!Name.equals(other.Name))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "DTOInformationUser [Name=" + Name + "]";
	}
	
	
}
