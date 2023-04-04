package de.svws_nrw.db.dto.migration.schild.personengruppen;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Personengruppen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Personengruppen")
@NamedQuery(name = "MigrationDTOPersonengruppen.all", query = "SELECT e FROM MigrationDTOPersonengruppen e")
@NamedQuery(name = "MigrationDTOPersonengruppen.id", query = "SELECT e FROM MigrationDTOPersonengruppen e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOPersonengruppen.id.multiple", query = "SELECT e FROM MigrationDTOPersonengruppen e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOPersonengruppen.schulnreigner", query = "SELECT e FROM MigrationDTOPersonengruppen e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOPersonengruppen.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOPersonengruppen e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOPersonengruppen.gruppenname", query = "SELECT e FROM MigrationDTOPersonengruppen e WHERE e.Gruppenname = :value")
@NamedQuery(name = "MigrationDTOPersonengruppen.gruppenname.multiple", query = "SELECT e FROM MigrationDTOPersonengruppen e WHERE e.Gruppenname IN :value")
@NamedQuery(name = "MigrationDTOPersonengruppen.zusatzinfo", query = "SELECT e FROM MigrationDTOPersonengruppen e WHERE e.Zusatzinfo = :value")
@NamedQuery(name = "MigrationDTOPersonengruppen.zusatzinfo.multiple", query = "SELECT e FROM MigrationDTOPersonengruppen e WHERE e.Zusatzinfo IN :value")
@NamedQuery(name = "MigrationDTOPersonengruppen.sammelemail", query = "SELECT e FROM MigrationDTOPersonengruppen e WHERE e.SammelEmail = :value")
@NamedQuery(name = "MigrationDTOPersonengruppen.sammelemail.multiple", query = "SELECT e FROM MigrationDTOPersonengruppen e WHERE e.SammelEmail IN :value")
@NamedQuery(name = "MigrationDTOPersonengruppen.gruppenart", query = "SELECT e FROM MigrationDTOPersonengruppen e WHERE e.GruppenArt = :value")
@NamedQuery(name = "MigrationDTOPersonengruppen.gruppenart.multiple", query = "SELECT e FROM MigrationDTOPersonengruppen e WHERE e.GruppenArt IN :value")
@NamedQuery(name = "MigrationDTOPersonengruppen.xmlexport", query = "SELECT e FROM MigrationDTOPersonengruppen e WHERE e.XMLExport = :value")
@NamedQuery(name = "MigrationDTOPersonengruppen.xmlexport.multiple", query = "SELECT e FROM MigrationDTOPersonengruppen e WHERE e.XMLExport IN :value")
@NamedQuery(name = "MigrationDTOPersonengruppen.sortierung", query = "SELECT e FROM MigrationDTOPersonengruppen e WHERE e.Sortierung = :value")
@NamedQuery(name = "MigrationDTOPersonengruppen.sortierung.multiple", query = "SELECT e FROM MigrationDTOPersonengruppen e WHERE e.Sortierung IN :value")
@NamedQuery(name = "MigrationDTOPersonengruppen.sichtbar", query = "SELECT e FROM MigrationDTOPersonengruppen e WHERE e.Sichtbar = :value")
@NamedQuery(name = "MigrationDTOPersonengruppen.sichtbar.multiple", query = "SELECT e FROM MigrationDTOPersonengruppen e WHERE e.Sichtbar IN :value")
@NamedQuery(name = "MigrationDTOPersonengruppen.primaryKeyQuery", query = "SELECT e FROM MigrationDTOPersonengruppen e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOPersonengruppen.all.migration", query = "SELECT e FROM MigrationDTOPersonengruppen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "SchulnrEigner", "Gruppenname", "Zusatzinfo", "SammelEmail", "GruppenArt", "XMLExport", "Sortierung", "Sichtbar"})
public final class MigrationDTOPersonengruppen {

	/** ID der Personengruppe */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Gruppenname der Personengruppe */
	@Column(name = "Gruppenname")
	@JsonProperty
	public String Gruppenname;

	/** Zusatzinfo der Personengruppe */
	@Column(name = "Zusatzinfo")
	@JsonProperty
	public String Zusatzinfo;

	/** Sammel-E-Mail-Adresse der Personengruppe */
	@Column(name = "SammelEmail")
	@JsonProperty
	public String SammelEmail;

	/** Gruppenart  der Personengruppe */
	@Column(name = "GruppenArt")
	@JsonProperty
	public String GruppenArt;

	/** Steuert den LogineoXML-Export */
	@Column(name = "XMLExport")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean XMLExport;

	/** Sortierung der Personengruppe */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichtbarkeit der Personengruppe */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOPersonengruppen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOPersonengruppen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOPersonengruppen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param SchulnrEigner   der Wert für das Attribut SchulnrEigner
	 * @param Gruppenname   der Wert für das Attribut Gruppenname
	 */
	public MigrationDTOPersonengruppen(final Long ID, final Integer SchulnrEigner, final String Gruppenname) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (SchulnrEigner == null) {
			throw new NullPointerException("SchulnrEigner must not be null");
		}
		this.SchulnrEigner = SchulnrEigner;
		if (Gruppenname == null) {
			throw new NullPointerException("Gruppenname must not be null");
		}
		this.Gruppenname = Gruppenname;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOPersonengruppen other = (MigrationDTOPersonengruppen) obj;
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
		return "MigrationDTOPersonengruppen(ID=" + this.ID + ", SchulnrEigner=" + this.SchulnrEigner + ", Gruppenname=" + this.Gruppenname + ", Zusatzinfo=" + this.Zusatzinfo + ", SammelEmail=" + this.SammelEmail + ", GruppenArt=" + this.GruppenArt + ", XMLExport=" + this.XMLExport + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ")";
	}

}
