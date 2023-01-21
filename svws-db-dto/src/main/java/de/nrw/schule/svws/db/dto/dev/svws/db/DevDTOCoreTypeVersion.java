package de.nrw.schule.svws.db.dto.dev.svws.db;

import de.nrw.schule.svws.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SVWS_Core_Type_Versionen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SVWS_Core_Type_Versionen")
@NamedQuery(name="DevDTOCoreTypeVersion.all", query="SELECT e FROM DevDTOCoreTypeVersion e")
@NamedQuery(name="DevDTOCoreTypeVersion.nametabelle", query="SELECT e FROM DevDTOCoreTypeVersion e WHERE e.NameTabelle = :value")
@NamedQuery(name="DevDTOCoreTypeVersion.nametabelle.multiple", query="SELECT e FROM DevDTOCoreTypeVersion e WHERE e.NameTabelle IN :value")
@NamedQuery(name="DevDTOCoreTypeVersion.name", query="SELECT e FROM DevDTOCoreTypeVersion e WHERE e.Name = :value")
@NamedQuery(name="DevDTOCoreTypeVersion.name.multiple", query="SELECT e FROM DevDTOCoreTypeVersion e WHERE e.Name IN :value")
@NamedQuery(name="DevDTOCoreTypeVersion.version", query="SELECT e FROM DevDTOCoreTypeVersion e WHERE e.Version = :value")
@NamedQuery(name="DevDTOCoreTypeVersion.version.multiple", query="SELECT e FROM DevDTOCoreTypeVersion e WHERE e.Version IN :value")
@NamedQuery(name="DevDTOCoreTypeVersion.primaryKeyQuery", query="SELECT e FROM DevDTOCoreTypeVersion e WHERE e.NameTabelle = ?1")
@NamedQuery(name="DevDTOCoreTypeVersion.all.migration", query="SELECT e FROM DevDTOCoreTypeVersion e WHERE e.NameTabelle IS NOT NULL")
@JsonPropertyOrder({"NameTabelle","Name","Version"})
public class DevDTOCoreTypeVersion {

	/** Gibt den Namen der Tabelle an, wo die Daten des Core-Types hinterlegt werden. */
	@Id
	@Column(name = "NameTabelle")
	@JsonProperty
	public String NameTabelle;

	/** Gibt den Namen des Core-Types an. */
	@Column(name = "Name")
	@JsonProperty
	public String Name;

	/** Die Version, in welcher der Core-Type in der DB vorliegt */
	@Column(name = "Version")
	@JsonProperty
	public Long Version;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOCoreTypeVersion ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOCoreTypeVersion() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOCoreTypeVersion ohne eine Initialisierung der Attribute.
	 * @param NameTabelle   der Wert für das Attribut NameTabelle
	 * @param Name   der Wert für das Attribut Name
	 * @param Version   der Wert für das Attribut Version
	 */
	public DevDTOCoreTypeVersion(final String NameTabelle, final String Name, final Long Version) {
		if (NameTabelle == null) { 
			throw new NullPointerException("NameTabelle must not be null");
		}
		this.NameTabelle = NameTabelle;
		if (Name == null) { 
			throw new NullPointerException("Name must not be null");
		}
		this.Name = Name;
		if (Version == null) { 
			throw new NullPointerException("Version must not be null");
		}
		this.Version = Version;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevDTOCoreTypeVersion other = (DevDTOCoreTypeVersion) obj;
		if (NameTabelle == null) {
			if (other.NameTabelle != null)
				return false;
		} else if (!NameTabelle.equals(other.NameTabelle))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((NameTabelle == null) ? 0 : NameTabelle.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DevDTOCoreTypeVersion(NameTabelle=" + this.NameTabelle + ", Name=" + this.Name + ", Version=" + this.Version + ")";
	}

}