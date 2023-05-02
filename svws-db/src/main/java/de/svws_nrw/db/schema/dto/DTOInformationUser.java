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
import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse definiert ein DTO für Informationen des DBMS zu den Datenbank-Benutzern. <br>
 * Wichtig: Dies entspricht nicht den SVWS-Benutzern!
 */
@Entity
@Table(name = "INFORMATION_SCHEMA.TABLES")
@Cacheable(DBEntityManager.use_db_caching)
@NamedNativeQuery(name = "DTOInformationUser.mysql", query = "SELECT DISTINCT User AS USER_NAME FROM mysql.user")
@NamedNativeQuery(name = "DTOInformationUser.mssql", query = "SELECT DISTINCT name AS USER_NAME FROM sys.sql_logins WHERE type='S'")
public final class DTOInformationUser {

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
	public static Map<String, DTOInformationUser> query(final @NotNull DBEntityManager conn) {
		final List<DTOInformationUser> results = switch (conn.getDBDriver()) {
			case MARIA_DB, MYSQL -> conn.queryNamed("DTOInformationUser.mysql", DTOInformationUser.class).getResultList();
			case MSSQL -> conn.queryNamed("DTOInformationUser.mssql", DTOInformationUser.class).getResultList();
			case MDB, SQLITE -> Collections.emptyList();
			default -> Collections.emptyList();
		};
		return results.stream().collect(Collectors.toMap(e -> e.Name, e -> e));
	}

	private static final Set<String> setSystemUserMariaDB = Stream.of("root").collect(Collectors.toCollection(HashSet::new));
	private static final Set<String> setSystemUserMySQL = Stream.of("root", "mysql.infoschema", "mysql.session", "mysql.sys").collect(Collectors.toCollection(HashSet::new));
	private static final Set<String> setSystemUserMSSQL = Stream.of("sa", "##MS_PolicyTsqlExecutionLogin##", "##MS_PolicyEventProcessingLogin##").collect(Collectors.toCollection(HashSet::new));

	/**
	 * Stellt eine Anfrage nach den Namen aller Benutzer der Datenbank. Hierbei werden alle Benutzer ignoriert, die vom DBMS
	 * vorgegeben sind.
	 *
	 * @param conn   die Datenbankverbindung
	 *
	 * @return die Liste mit den Benutzernamen
	 */
	public static List<String> queryNames(final DBEntityManager conn) {
		return switch (conn.getDBDriver()) {
			case MARIA_DB -> conn.queryNamed("DTOInformationUser.mysql", String.class).getResultList().stream().filter(setSystemUserMariaDB::contains).toList();
			case MYSQL -> conn.queryNamed("DTOInformationUser.mysql", String.class).getResultList().stream().filter(setSystemUserMySQL::contains).toList();
			case MSSQL -> conn.queryNamed("DTOInformationUser.mssql", String.class).getResultList().stream().filter(setSystemUserMSSQL::contains).toList();
			case MDB, SQLITE -> Collections.emptyList();
			default -> Collections.emptyList();
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
		final DTOInformationUser other = (DTOInformationUser) obj;
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
