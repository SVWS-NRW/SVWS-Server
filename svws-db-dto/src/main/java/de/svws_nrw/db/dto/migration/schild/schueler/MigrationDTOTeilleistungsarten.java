package de.svws_nrw.db.dto.migration.schild.schueler;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Einzelleistungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Einzelleistungen")
@NamedQuery(name = "MigrationDTOTeilleistungsarten.all", query = "SELECT e FROM MigrationDTOTeilleistungsarten e")
@NamedQuery(name = "MigrationDTOTeilleistungsarten.id", query = "SELECT e FROM MigrationDTOTeilleistungsarten e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOTeilleistungsarten.id.multiple", query = "SELECT e FROM MigrationDTOTeilleistungsarten e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOTeilleistungsarten.schulnreigner", query = "SELECT e FROM MigrationDTOTeilleistungsarten e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOTeilleistungsarten.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOTeilleistungsarten e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOTeilleistungsarten.bezeichnung", query = "SELECT e FROM MigrationDTOTeilleistungsarten e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "MigrationDTOTeilleistungsarten.bezeichnung.multiple", query = "SELECT e FROM MigrationDTOTeilleistungsarten e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "MigrationDTOTeilleistungsarten.sortierung", query = "SELECT e FROM MigrationDTOTeilleistungsarten e WHERE e.Sortierung = :value")
@NamedQuery(name = "MigrationDTOTeilleistungsarten.sortierung.multiple", query = "SELECT e FROM MigrationDTOTeilleistungsarten e WHERE e.Sortierung IN :value")
@NamedQuery(name = "MigrationDTOTeilleistungsarten.sichtbar", query = "SELECT e FROM MigrationDTOTeilleistungsarten e WHERE e.Sichtbar = :value")
@NamedQuery(name = "MigrationDTOTeilleistungsarten.sichtbar.multiple", query = "SELECT e FROM MigrationDTOTeilleistungsarten e WHERE e.Sichtbar IN :value")
@NamedQuery(name = "MigrationDTOTeilleistungsarten.gewichtung", query = "SELECT e FROM MigrationDTOTeilleistungsarten e WHERE e.Gewichtung = :value")
@NamedQuery(name = "MigrationDTOTeilleistungsarten.gewichtung.multiple", query = "SELECT e FROM MigrationDTOTeilleistungsarten e WHERE e.Gewichtung IN :value")
@NamedQuery(name = "MigrationDTOTeilleistungsarten.primaryKeyQuery", query = "SELECT e FROM MigrationDTOTeilleistungsarten e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOTeilleistungsarten.all.migration", query = "SELECT e FROM MigrationDTOTeilleistungsarten e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "SchulnrEigner", "Bezeichnung", "Sortierung", "Sichtbar", "Gewichtung"})
public final class MigrationDTOTeilleistungsarten {

	/** ID der Teilleistung zu den Leistungsdaten */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Bezeichnung der Teilleistung zu den Leistungsdaten */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Sortierung der Teilleistung zu den Leistungsdaten */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichbarkeit der Teilleistung zu den Leistungsdaten */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Gewichtung der Teilleistung zu den Leistungsdaten */
	@Column(name = "Gewichtung")
	@JsonProperty
	public Double Gewichtung;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOTeilleistungsarten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOTeilleistungsarten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOTeilleistungsarten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param SchulnrEigner   der Wert für das Attribut SchulnrEigner
	 */
	public MigrationDTOTeilleistungsarten(final Long ID, final Integer SchulnrEigner) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (SchulnrEigner == null) {
			throw new NullPointerException("SchulnrEigner must not be null");
		}
		this.SchulnrEigner = SchulnrEigner;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOTeilleistungsarten other = (MigrationDTOTeilleistungsarten) obj;
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
		return "MigrationDTOTeilleistungsarten(ID=" + this.ID + ", SchulnrEigner=" + this.SchulnrEigner + ", Bezeichnung=" + this.Bezeichnung + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Gewichtung=" + this.Gewichtung + ")";
	}

}
