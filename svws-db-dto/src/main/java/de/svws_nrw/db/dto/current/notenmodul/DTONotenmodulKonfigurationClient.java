package de.svws_nrw.db.dto.current.notenmodul;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Notenmodul_Konfiguration_Client.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Notenmodul_Konfiguration_Client")
@JsonPropertyOrder({"schluessel", "wert"})
public final class DTONotenmodulKonfigurationClient {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTONotenmodulKonfigurationClient e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTONotenmodulKonfigurationClient e WHERE e.schluessel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTONotenmodulKonfigurationClient e WHERE e.schluessel IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTONotenmodulKonfigurationClient e WHERE e.schluessel IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes schluessel */
	public static final String QUERY_BY_SCHLUESSEL = "SELECT e FROM DTONotenmodulKonfigurationClient e WHERE e.schluessel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes schluessel */
	public static final String QUERY_LIST_BY_SCHLUESSEL = "SELECT e FROM DTONotenmodulKonfigurationClient e WHERE e.schluessel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes wert */
	public static final String QUERY_BY_WERT = "SELECT e FROM DTONotenmodulKonfigurationClient e WHERE e.wert = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes wert */
	public static final String QUERY_LIST_BY_WERT = "SELECT e FROM DTONotenmodulKonfigurationClient e WHERE e.wert IN ?1";

	/** Der Schlüssel des Konfigurationseintrags */
	@Id
	@Column(name = "schluessel")
	@JsonProperty
	public String schluessel;

	/** Der Wert des Konfigurationseintrags */
	@Column(name = "wert")
	@JsonProperty
	public String wert;

	/**
	 * Erstellt ein neues Objekt der Klasse DTONotenmodulKonfigurationClient ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTONotenmodulKonfigurationClient() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTONotenmodulKonfigurationClient ohne eine Initialisierung der Attribute.
	 * @param schluessel   der Wert für das Attribut schluessel
	 * @param wert   der Wert für das Attribut wert
	 */
	public DTONotenmodulKonfigurationClient(final String schluessel, final String wert) {
		if (schluessel == null) {
			throw new NullPointerException("schluessel must not be null");
		}
		this.schluessel = schluessel;
		if (wert == null) {
			throw new NullPointerException("wert must not be null");
		}
		this.wert = wert;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTONotenmodulKonfigurationClient other = (DTONotenmodulKonfigurationClient) obj;
		if (schluessel == null) {
			if (other.schluessel != null)
				return false;
		} else if (!schluessel.equals(other.schluessel))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((schluessel == null) ? 0 : schluessel.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTONotenmodulKonfigurationClient(schluessel=" + this.schluessel + ", wert=" + this.wert + ")";
	}

}
