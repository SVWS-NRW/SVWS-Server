package de.nrw.schule.svws.db.dto.migration.schild.katalog;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverter;


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
import de.nrw.schule.svws.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterSerializer;
import de.nrw.schule.svws.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Haltestelle.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Haltestelle")
@NamedQuery(name="MigrationDTOHaltestellen.all", query="SELECT e FROM MigrationDTOHaltestellen e")
@NamedQuery(name="MigrationDTOHaltestellen.id", query="SELECT e FROM MigrationDTOHaltestellen e WHERE e.ID = :value")
@NamedQuery(name="MigrationDTOHaltestellen.id.multiple", query="SELECT e FROM MigrationDTOHaltestellen e WHERE e.ID IN :value")
@NamedQuery(name="MigrationDTOHaltestellen.bezeichnung", query="SELECT e FROM MigrationDTOHaltestellen e WHERE e.Bezeichnung = :value")
@NamedQuery(name="MigrationDTOHaltestellen.bezeichnung.multiple", query="SELECT e FROM MigrationDTOHaltestellen e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="MigrationDTOHaltestellen.sortierung", query="SELECT e FROM MigrationDTOHaltestellen e WHERE e.Sortierung = :value")
@NamedQuery(name="MigrationDTOHaltestellen.sortierung.multiple", query="SELECT e FROM MigrationDTOHaltestellen e WHERE e.Sortierung IN :value")
@NamedQuery(name="MigrationDTOHaltestellen.sichtbar", query="SELECT e FROM MigrationDTOHaltestellen e WHERE e.Sichtbar = :value")
@NamedQuery(name="MigrationDTOHaltestellen.sichtbar.multiple", query="SELECT e FROM MigrationDTOHaltestellen e WHERE e.Sichtbar IN :value")
@NamedQuery(name="MigrationDTOHaltestellen.aenderbar", query="SELECT e FROM MigrationDTOHaltestellen e WHERE e.Aenderbar = :value")
@NamedQuery(name="MigrationDTOHaltestellen.aenderbar.multiple", query="SELECT e FROM MigrationDTOHaltestellen e WHERE e.Aenderbar IN :value")
@NamedQuery(name="MigrationDTOHaltestellen.entfernungschule", query="SELECT e FROM MigrationDTOHaltestellen e WHERE e.EntfernungSchule = :value")
@NamedQuery(name="MigrationDTOHaltestellen.entfernungschule.multiple", query="SELECT e FROM MigrationDTOHaltestellen e WHERE e.EntfernungSchule IN :value")
@NamedQuery(name="MigrationDTOHaltestellen.schulnreigner", query="SELECT e FROM MigrationDTOHaltestellen e WHERE e.SchulnrEigner = :value")
@NamedQuery(name="MigrationDTOHaltestellen.schulnreigner.multiple", query="SELECT e FROM MigrationDTOHaltestellen e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name="MigrationDTOHaltestellen.primaryKeyQuery", query="SELECT e FROM MigrationDTOHaltestellen e WHERE e.ID = ?1")
@NamedQuery(name="MigrationDTOHaltestellen.all.migration", query="SELECT e FROM MigrationDTOHaltestellen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Bezeichnung","Sortierung","Sichtbar","Aenderbar","EntfernungSchule","SchulnrEigner"})
public class MigrationDTOHaltestellen {

	/** ID der Haltestelle */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Bezeichnung der Haltestelle */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Sortierung der Haltestelle */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichbarkeit der Haltestelle */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Änderbarkeit der Haltestelle */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/** Enterfung zur Schule von der Haltestelle */
	@Column(name = "EntfernungSchule")
	@JsonProperty
	public Double EntfernungSchule;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOHaltestellen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOHaltestellen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOHaltestellen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public MigrationDTOHaltestellen(final Long ID, final String Bezeichnung) {
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
		MigrationDTOHaltestellen other = (MigrationDTOHaltestellen) obj;
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
		return "MigrationDTOHaltestellen(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ", EntfernungSchule=" + this.EntfernungSchule + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}