package de.nrw.schule.svws.db.dto.dev.svws.auth;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Lernplattformen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Lernplattformen")
@NamedQuery(name="DevDTOLernplattformen.all", query="SELECT e FROM DevDTOLernplattformen e")
@NamedQuery(name="DevDTOLernplattformen.id", query="SELECT e FROM DevDTOLernplattformen e WHERE e.ID = :value")
@NamedQuery(name="DevDTOLernplattformen.id.multiple", query="SELECT e FROM DevDTOLernplattformen e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOLernplattformen.bezeichnung", query="SELECT e FROM DevDTOLernplattformen e WHERE e.Bezeichnung = :value")
@NamedQuery(name="DevDTOLernplattformen.bezeichnung.multiple", query="SELECT e FROM DevDTOLernplattformen e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="DevDTOLernplattformen.benutzernamesuffixlehrer", query="SELECT e FROM DevDTOLernplattformen e WHERE e.BenutzernameSuffixLehrer = :value")
@NamedQuery(name="DevDTOLernplattformen.benutzernamesuffixlehrer.multiple", query="SELECT e FROM DevDTOLernplattformen e WHERE e.BenutzernameSuffixLehrer IN :value")
@NamedQuery(name="DevDTOLernplattformen.benutzernamesuffixerzieher", query="SELECT e FROM DevDTOLernplattformen e WHERE e.BenutzernameSuffixErzieher = :value")
@NamedQuery(name="DevDTOLernplattformen.benutzernamesuffixerzieher.multiple", query="SELECT e FROM DevDTOLernplattformen e WHERE e.BenutzernameSuffixErzieher IN :value")
@NamedQuery(name="DevDTOLernplattformen.benutzernamesuffixschueler", query="SELECT e FROM DevDTOLernplattformen e WHERE e.BenutzernameSuffixSchueler = :value")
@NamedQuery(name="DevDTOLernplattformen.benutzernamesuffixschueler.multiple", query="SELECT e FROM DevDTOLernplattformen e WHERE e.BenutzernameSuffixSchueler IN :value")
@NamedQuery(name="DevDTOLernplattformen.konfiguration", query="SELECT e FROM DevDTOLernplattformen e WHERE e.Konfiguration = :value")
@NamedQuery(name="DevDTOLernplattformen.konfiguration.multiple", query="SELECT e FROM DevDTOLernplattformen e WHERE e.Konfiguration IN :value")
@NamedQuery(name="DevDTOLernplattformen.primaryKeyQuery", query="SELECT e FROM DevDTOLernplattformen e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOLernplattformen.all.migration", query="SELECT e FROM DevDTOLernplattformen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Bezeichnung","BenutzernameSuffixLehrer","BenutzernameSuffixErzieher","BenutzernameSuffixSchueler","Konfiguration"})
public class DevDTOLernplattformen {

	/** ID des Datensatzes für die verwendete Lernplattform */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Bezeichnung der Lernplattform */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Suffix für den Benutzernamen bei den Lehrern */
	@Column(name = "BenutzernameSuffixLehrer")
	@JsonProperty
	public String BenutzernameSuffixLehrer;

	/** Suffix für den Benutzernamen bei den Erziehern */
	@Column(name = "BenutzernameSuffixErzieher")
	@JsonProperty
	public String BenutzernameSuffixErzieher;

	/** Suffix für den Benutzernamen bei den Schülern */
	@Column(name = "BenutzernameSuffixSchueler")
	@JsonProperty
	public String BenutzernameSuffixSchueler;

	/** Json-Objekt mit den Konfigurationseinstellungen der Accounterstellung für die Lernplattform */
	@Column(name = "Konfiguration")
	@JsonProperty
	public String Konfiguration;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOLernplattformen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOLernplattformen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOLernplattformen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public DevDTOLernplattformen(final Long ID, final String Bezeichnung) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Bezeichnung == null) { 
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevDTOLernplattformen other = (DevDTOLernplattformen) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DevDTOLernplattformen(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", BenutzernameSuffixLehrer=" + this.BenutzernameSuffixLehrer + ", BenutzernameSuffixErzieher=" + this.BenutzernameSuffixErzieher + ", BenutzernameSuffixSchueler=" + this.BenutzernameSuffixSchueler + ", Konfiguration=" + this.Konfiguration + ")";
	}

}