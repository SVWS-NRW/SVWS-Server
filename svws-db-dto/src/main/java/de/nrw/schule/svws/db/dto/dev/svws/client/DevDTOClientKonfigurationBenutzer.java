package de.nrw.schule.svws.db.dto.dev.svws.client;

import de.nrw.schule.svws.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SVWS_Client_Konfiguration_Benutzer.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DevDTOClientKonfigurationBenutzerPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SVWS_Client_Konfiguration_Benutzer")
@NamedQuery(name="DevDTOClientKonfigurationBenutzer.all", query="SELECT e FROM DevDTOClientKonfigurationBenutzer e")
@NamedQuery(name="DevDTOClientKonfigurationBenutzer.benutzer_id", query="SELECT e FROM DevDTOClientKonfigurationBenutzer e WHERE e.Benutzer_ID = :value")
@NamedQuery(name="DevDTOClientKonfigurationBenutzer.benutzer_id.multiple", query="SELECT e FROM DevDTOClientKonfigurationBenutzer e WHERE e.Benutzer_ID IN :value")
@NamedQuery(name="DevDTOClientKonfigurationBenutzer.appname", query="SELECT e FROM DevDTOClientKonfigurationBenutzer e WHERE e.AppName = :value")
@NamedQuery(name="DevDTOClientKonfigurationBenutzer.appname.multiple", query="SELECT e FROM DevDTOClientKonfigurationBenutzer e WHERE e.AppName IN :value")
@NamedQuery(name="DevDTOClientKonfigurationBenutzer.schluessel", query="SELECT e FROM DevDTOClientKonfigurationBenutzer e WHERE e.Schluessel = :value")
@NamedQuery(name="DevDTOClientKonfigurationBenutzer.schluessel.multiple", query="SELECT e FROM DevDTOClientKonfigurationBenutzer e WHERE e.Schluessel IN :value")
@NamedQuery(name="DevDTOClientKonfigurationBenutzer.wert", query="SELECT e FROM DevDTOClientKonfigurationBenutzer e WHERE e.Wert = :value")
@NamedQuery(name="DevDTOClientKonfigurationBenutzer.wert.multiple", query="SELECT e FROM DevDTOClientKonfigurationBenutzer e WHERE e.Wert IN :value")
@NamedQuery(name="DevDTOClientKonfigurationBenutzer.primaryKeyQuery", query="SELECT e FROM DevDTOClientKonfigurationBenutzer e WHERE e.Benutzer_ID = ?1 AND e.AppName = ?2 AND e.Schluessel = ?3")
@NamedQuery(name="DevDTOClientKonfigurationBenutzer.all.migration", query="SELECT e FROM DevDTOClientKonfigurationBenutzer e WHERE e.Benutzer_ID IS NOT NULL AND e.AppName IS NOT NULL AND e.Schluessel IS NOT NULL")
@JsonPropertyOrder({"Benutzer_ID","AppName","Schluessel","Wert"})
public class DevDTOClientKonfigurationBenutzer {

	/** Die ID des Datenbankbenutzers, für den der Client-Konfigurationsdatensatz gespeichert ist */
	@Id
	@Column(name = "Benutzer_ID")
	@JsonProperty
	public Long Benutzer_ID;

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
	 * Erstellt ein neues Objekt der Klasse DevDTOClientKonfigurationBenutzer ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOClientKonfigurationBenutzer() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOClientKonfigurationBenutzer ohne eine Initialisierung der Attribute.
	 * @param Benutzer_ID   der Wert für das Attribut Benutzer_ID
	 * @param AppName   der Wert für das Attribut AppName
	 * @param Schluessel   der Wert für das Attribut Schluessel
	 * @param Wert   der Wert für das Attribut Wert
	 */
	public DevDTOClientKonfigurationBenutzer(final Long Benutzer_ID, final String AppName, final String Schluessel, final String Wert) {
		if (Benutzer_ID == null) { 
			throw new NullPointerException("Benutzer_ID must not be null");
		}
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevDTOClientKonfigurationBenutzer other = (DevDTOClientKonfigurationBenutzer) obj;
		if (Benutzer_ID == null) {
			if (other.Benutzer_ID != null)
				return false;
		} else if (!Benutzer_ID.equals(other.Benutzer_ID))
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
		result = prime * result + ((Benutzer_ID == null) ? 0 : Benutzer_ID.hashCode());

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
		return "DevDTOClientKonfigurationBenutzer(Benutzer_ID=" + this.Benutzer_ID + ", AppName=" + this.AppName + ", Schluessel=" + this.Schluessel + ", Wert=" + this.Wert + ")";
	}

}