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
 * Diese Klasse dient als DTO für die Datenbanktabelle Client_Konfiguration_Benutzer.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOClientKonfigurationBenutzerPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Client_Konfiguration_Benutzer")
@JsonPropertyOrder({"Benutzer_ID", "AppName", "Schluessel", "Wert"})
public final class DTOClientKonfigurationBenutzer {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOClientKonfigurationBenutzer e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOClientKonfigurationBenutzer e WHERE e.Benutzer_ID = ?1 AND e.AppName = ?2 AND e.Schluessel = ?3";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOClientKonfigurationBenutzer e WHERE e.Benutzer_ID IS NOT NULL AND e.AppName IS NOT NULL AND e.Schluessel IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Benutzer_ID */
	public static final String QUERY_BY_BENUTZER_ID = "SELECT e FROM DTOClientKonfigurationBenutzer e WHERE e.Benutzer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Benutzer_ID */
	public static final String QUERY_LIST_BY_BENUTZER_ID = "SELECT e FROM DTOClientKonfigurationBenutzer e WHERE e.Benutzer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AppName */
	public static final String QUERY_BY_APPNAME = "SELECT e FROM DTOClientKonfigurationBenutzer e WHERE e.AppName = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AppName */
	public static final String QUERY_LIST_BY_APPNAME = "SELECT e FROM DTOClientKonfigurationBenutzer e WHERE e.AppName IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schluessel */
	public static final String QUERY_BY_SCHLUESSEL = "SELECT e FROM DTOClientKonfigurationBenutzer e WHERE e.Schluessel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schluessel */
	public static final String QUERY_LIST_BY_SCHLUESSEL = "SELECT e FROM DTOClientKonfigurationBenutzer e WHERE e.Schluessel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Wert */
	public static final String QUERY_BY_WERT = "SELECT e FROM DTOClientKonfigurationBenutzer e WHERE e.Wert = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Wert */
	public static final String QUERY_LIST_BY_WERT = "SELECT e FROM DTOClientKonfigurationBenutzer e WHERE e.Wert IN ?1";

	/** Die ID des Datenbankbenutzers, für den der Client-Konfigurationsdatensatz gespeichert ist */
	@Id
	@Column(name = "Benutzer_ID")
	@JsonProperty
	public long Benutzer_ID;

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
	 * Erstellt ein neues Objekt der Klasse DTOClientKonfigurationBenutzer ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOClientKonfigurationBenutzer() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOClientKonfigurationBenutzer ohne eine Initialisierung der Attribute.
	 * @param Benutzer_ID   der Wert für das Attribut Benutzer_ID
	 * @param AppName   der Wert für das Attribut AppName
	 * @param Schluessel   der Wert für das Attribut Schluessel
	 * @param Wert   der Wert für das Attribut Wert
	 */
	public DTOClientKonfigurationBenutzer(final long Benutzer_ID, final String AppName, final String Schluessel, final String Wert) {
		this.Benutzer_ID = Benutzer_ID;
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
		DTOClientKonfigurationBenutzer other = (DTOClientKonfigurationBenutzer) obj;
		if (Benutzer_ID != other.Benutzer_ID)
			return false;
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
		result = prime * result + Long.hashCode(Benutzer_ID);

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
		return "DTOClientKonfigurationBenutzer(Benutzer_ID=" + this.Benutzer_ID + ", AppName=" + this.AppName + ", Schluessel=" + this.Schluessel + ", Wert=" + this.Wert + ")";
	}

}
