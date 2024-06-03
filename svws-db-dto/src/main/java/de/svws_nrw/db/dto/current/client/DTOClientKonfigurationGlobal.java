package de.svws_nrw.db.dto.current.client;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Client_Konfiguration_Global.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOClientKonfigurationGlobalPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Client_Konfiguration_Global")
@JsonPropertyOrder({"AppName", "Schluessel", "Wert"})
public final class DTOClientKonfigurationGlobal {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOClientKonfigurationGlobal e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOClientKonfigurationGlobal e WHERE e.AppName = ?1 AND e.Schluessel = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOClientKonfigurationGlobal e WHERE e.AppName IS NOT NULL AND e.Schluessel IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AppName */
	public static final String QUERY_BY_APPNAME = "SELECT e FROM DTOClientKonfigurationGlobal e WHERE e.AppName = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AppName */
	public static final String QUERY_LIST_BY_APPNAME = "SELECT e FROM DTOClientKonfigurationGlobal e WHERE e.AppName IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schluessel */
	public static final String QUERY_BY_SCHLUESSEL = "SELECT e FROM DTOClientKonfigurationGlobal e WHERE e.Schluessel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schluessel */
	public static final String QUERY_LIST_BY_SCHLUESSEL = "SELECT e FROM DTOClientKonfigurationGlobal e WHERE e.Schluessel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Wert */
	public static final String QUERY_BY_WERT = "SELECT e FROM DTOClientKonfigurationGlobal e WHERE e.Wert = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Wert */
	public static final String QUERY_LIST_BY_WERT = "SELECT e FROM DTOClientKonfigurationGlobal e WHERE e.Wert IN ?1";

	/** Der Name der Client-Anwendung, für die der Konfigurationsdatensatz gespeichert ist */
	@Id
	@Column(name = "AppName")
	@JsonProperty
	public String AppName;

	/** Der Schlüsselname des Konfigurationsdatensatzes */
	@Id
	@Column(name = "Schluessel")
	@JsonProperty
	public String Schluessel;

	/** Der Wert des Konfigurationsdatensatzes */
	@Column(name = "Wert")
	@JsonProperty
	public String Wert;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOClientKonfigurationGlobal ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOClientKonfigurationGlobal() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOClientKonfigurationGlobal ohne eine Initialisierung der Attribute.
	 * @param AppName   der Wert für das Attribut AppName
	 * @param Schluessel   der Wert für das Attribut Schluessel
	 * @param Wert   der Wert für das Attribut Wert
	 */
	public DTOClientKonfigurationGlobal(final String AppName, final String Schluessel, final String Wert) {
		if (AppName == null) {
			throw new NullPointerException("AppName must not be null");
		}
		this.AppName = AppName;
		if (Schluessel == null) {
			throw new NullPointerException("Schluessel must not be null");
		}
		this.Schluessel = Schluessel;
		if (Wert == null) {
			throw new NullPointerException("Wert must not be null");
		}
		this.Wert = Wert;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOClientKonfigurationGlobal other = (DTOClientKonfigurationGlobal) obj;
		if (AppName == null) {
			if (other.AppName != null)
				return false;
		} else if (!AppName.equals(other.AppName))
			return false;
		if (Schluessel == null) {
			if (other.Schluessel != null)
				return false;
		} else if (!Schluessel.equals(other.Schluessel))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((AppName == null) ? 0 : AppName.hashCode());

		result = prime * result + ((Schluessel == null) ? 0 : Schluessel.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOClientKonfigurationGlobal(AppName=" + this.AppName + ", Schluessel=" + this.Schluessel + ", Wert=" + this.Wert + ")";
	}

}
