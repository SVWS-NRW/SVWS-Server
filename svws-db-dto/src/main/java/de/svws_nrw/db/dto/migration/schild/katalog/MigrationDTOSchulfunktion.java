package de.svws_nrw.db.dto.migration.schild.katalog;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Schulfunktionen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Schulfunktionen")
@NamedQuery(name="MigrationDTOSchulfunktion.all", query="SELECT e FROM MigrationDTOSchulfunktion e")
@NamedQuery(name="MigrationDTOSchulfunktion.id", query="SELECT e FROM MigrationDTOSchulfunktion e WHERE e.ID = :value")
@NamedQuery(name="MigrationDTOSchulfunktion.id.multiple", query="SELECT e FROM MigrationDTOSchulfunktion e WHERE e.ID IN :value")
@NamedQuery(name="MigrationDTOSchulfunktion.bezeichnung", query="SELECT e FROM MigrationDTOSchulfunktion e WHERE e.Bezeichnung = :value")
@NamedQuery(name="MigrationDTOSchulfunktion.bezeichnung.multiple", query="SELECT e FROM MigrationDTOSchulfunktion e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="MigrationDTOSchulfunktion.sortierung", query="SELECT e FROM MigrationDTOSchulfunktion e WHERE e.Sortierung = :value")
@NamedQuery(name="MigrationDTOSchulfunktion.sortierung.multiple", query="SELECT e FROM MigrationDTOSchulfunktion e WHERE e.Sortierung IN :value")
@NamedQuery(name="MigrationDTOSchulfunktion.sichtbar", query="SELECT e FROM MigrationDTOSchulfunktion e WHERE e.Sichtbar = :value")
@NamedQuery(name="MigrationDTOSchulfunktion.sichtbar.multiple", query="SELECT e FROM MigrationDTOSchulfunktion e WHERE e.Sichtbar IN :value")
@NamedQuery(name="MigrationDTOSchulfunktion.schulnreigner", query="SELECT e FROM MigrationDTOSchulfunktion e WHERE e.SchulnrEigner = :value")
@NamedQuery(name="MigrationDTOSchulfunktion.schulnreigner.multiple", query="SELECT e FROM MigrationDTOSchulfunktion e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name="MigrationDTOSchulfunktion.primaryKeyQuery", query="SELECT e FROM MigrationDTOSchulfunktion e WHERE e.ID = ?1")
@NamedQuery(name="MigrationDTOSchulfunktion.all.migration", query="SELECT e FROM MigrationDTOSchulfunktion e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Bezeichnung","Sortierung","Sichtbar","SchulnrEigner"})
public class MigrationDTOSchulfunktion {

	/** ID der schulinternen Funktion */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Bezeichnung der schulinternen Funktion */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Sortierung der schulinternen Funktion */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichbarkeit der schulinternen Funktion */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchulfunktion ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchulfunktion() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchulfunktion ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public MigrationDTOSchulfunktion(final Long ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOSchulfunktion other = (MigrationDTOSchulfunktion) obj;
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
		return "MigrationDTOSchulfunktion(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}