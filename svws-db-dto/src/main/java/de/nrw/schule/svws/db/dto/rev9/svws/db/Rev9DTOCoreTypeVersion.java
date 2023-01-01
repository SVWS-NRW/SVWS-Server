package de.nrw.schule.svws.db.dto.rev9.svws.db;

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
@NamedQuery(name="Rev9DTOCoreTypeVersion.all", query="SELECT e FROM Rev9DTOCoreTypeVersion e")
@NamedQuery(name="Rev9DTOCoreTypeVersion.nametabelle", query="SELECT e FROM Rev9DTOCoreTypeVersion e WHERE e.NameTabelle = :value")
@NamedQuery(name="Rev9DTOCoreTypeVersion.nametabelle.multiple", query="SELECT e FROM Rev9DTOCoreTypeVersion e WHERE e.NameTabelle IN :value")
@NamedQuery(name="Rev9DTOCoreTypeVersion.name", query="SELECT e FROM Rev9DTOCoreTypeVersion e WHERE e.Name = :value")
@NamedQuery(name="Rev9DTOCoreTypeVersion.name.multiple", query="SELECT e FROM Rev9DTOCoreTypeVersion e WHERE e.Name IN :value")
@NamedQuery(name="Rev9DTOCoreTypeVersion.version", query="SELECT e FROM Rev9DTOCoreTypeVersion e WHERE e.Version = :value")
@NamedQuery(name="Rev9DTOCoreTypeVersion.version.multiple", query="SELECT e FROM Rev9DTOCoreTypeVersion e WHERE e.Version IN :value")
@NamedQuery(name="Rev9DTOCoreTypeVersion.primaryKeyQuery", query="SELECT e FROM Rev9DTOCoreTypeVersion e WHERE e.NameTabelle = ?1")
@NamedQuery(name="Rev9DTOCoreTypeVersion.all.migration", query="SELECT e FROM Rev9DTOCoreTypeVersion e WHERE e.NameTabelle IS NOT NULL")
@JsonPropertyOrder({"NameTabelle","Name","Version"})
public class Rev9DTOCoreTypeVersion {

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
	 * Erstellt ein neues Objekt der Klasse Rev9DTOCoreTypeVersion ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev9DTOCoreTypeVersion() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOCoreTypeVersion ohne eine Initialisierung der Attribute.
	 * @param NameTabelle   der Wert für das Attribut NameTabelle
	 * @param Name   der Wert für das Attribut Name
	 * @param Version   der Wert für das Attribut Version
	 */
	public Rev9DTOCoreTypeVersion(final String NameTabelle, final String Name, final Long Version) {
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
		Rev9DTOCoreTypeVersion other = (Rev9DTOCoreTypeVersion) obj;
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
		return "Rev9DTOCoreTypeVersion(NameTabelle=" + this.NameTabelle + ", Name=" + this.Name + ", Version=" + this.Version + ")";
	}

}