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
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Ortsteil.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Ortsteil")
@NamedQuery(name="MigrationDTOOrtsteil.all", query="SELECT e FROM MigrationDTOOrtsteil e")
@NamedQuery(name="MigrationDTOOrtsteil.id", query="SELECT e FROM MigrationDTOOrtsteil e WHERE e.ID = :value")
@NamedQuery(name="MigrationDTOOrtsteil.id.multiple", query="SELECT e FROM MigrationDTOOrtsteil e WHERE e.ID IN :value")
@NamedQuery(name="MigrationDTOOrtsteil.bezeichnung", query="SELECT e FROM MigrationDTOOrtsteil e WHERE e.Bezeichnung = :value")
@NamedQuery(name="MigrationDTOOrtsteil.bezeichnung.multiple", query="SELECT e FROM MigrationDTOOrtsteil e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="MigrationDTOOrtsteil.ort_id", query="SELECT e FROM MigrationDTOOrtsteil e WHERE e.Ort_ID = :value")
@NamedQuery(name="MigrationDTOOrtsteil.ort_id.multiple", query="SELECT e FROM MigrationDTOOrtsteil e WHERE e.Ort_ID IN :value")
@NamedQuery(name="MigrationDTOOrtsteil.plz", query="SELECT e FROM MigrationDTOOrtsteil e WHERE e.PLZ = :value")
@NamedQuery(name="MigrationDTOOrtsteil.plz.multiple", query="SELECT e FROM MigrationDTOOrtsteil e WHERE e.PLZ IN :value")
@NamedQuery(name="MigrationDTOOrtsteil.sortierung", query="SELECT e FROM MigrationDTOOrtsteil e WHERE e.Sortierung = :value")
@NamedQuery(name="MigrationDTOOrtsteil.sortierung.multiple", query="SELECT e FROM MigrationDTOOrtsteil e WHERE e.Sortierung IN :value")
@NamedQuery(name="MigrationDTOOrtsteil.sichtbar", query="SELECT e FROM MigrationDTOOrtsteil e WHERE e.Sichtbar = :value")
@NamedQuery(name="MigrationDTOOrtsteil.sichtbar.multiple", query="SELECT e FROM MigrationDTOOrtsteil e WHERE e.Sichtbar IN :value")
@NamedQuery(name="MigrationDTOOrtsteil.aenderbar", query="SELECT e FROM MigrationDTOOrtsteil e WHERE e.Aenderbar = :value")
@NamedQuery(name="MigrationDTOOrtsteil.aenderbar.multiple", query="SELECT e FROM MigrationDTOOrtsteil e WHERE e.Aenderbar IN :value")
@NamedQuery(name="MigrationDTOOrtsteil.schulnreigner", query="SELECT e FROM MigrationDTOOrtsteil e WHERE e.SchulnrEigner = :value")
@NamedQuery(name="MigrationDTOOrtsteil.schulnreigner.multiple", query="SELECT e FROM MigrationDTOOrtsteil e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name="MigrationDTOOrtsteil.ortsteilschluessel", query="SELECT e FROM MigrationDTOOrtsteil e WHERE e.OrtsteilSchluessel = :value")
@NamedQuery(name="MigrationDTOOrtsteil.ortsteilschluessel.multiple", query="SELECT e FROM MigrationDTOOrtsteil e WHERE e.OrtsteilSchluessel IN :value")
@NamedQuery(name="MigrationDTOOrtsteil.primaryKeyQuery", query="SELECT e FROM MigrationDTOOrtsteil e WHERE e.ID = ?1")
@NamedQuery(name="MigrationDTOOrtsteil.all.migration", query="SELECT e FROM MigrationDTOOrtsteil e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Bezeichnung","Ort_ID","PLZ","Sortierung","Sichtbar","Aenderbar","SchulnrEigner","OrtsteilSchluessel"})
public class MigrationDTOOrtsteil {

	/** ID des Ortsteils */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Bezeichnung des Ortsteils */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Fremdschlüssel auf den Ort, dem der Ortsteil zugeordnet ist */
	@Column(name = "Ort_ID")
	@JsonProperty
	public Long Ort_ID;

	/** PLZ des Ortsteils */
	@Column(name = "PLZ")
	@JsonProperty
	public String PLZ;

	/** Sortierung des Ortsteils */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichbarkeit des Ortsteils */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Änderbarkeit des Ortsteils */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Schlüssel des Ortsteils (Regional?) */
	@Column(name = "OrtsteilSchluessel")
	@JsonProperty
	public String OrtsteilSchluessel;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOOrtsteil ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOOrtsteil() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOOrtsteil ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public MigrationDTOOrtsteil(final Long ID, final String Bezeichnung) {
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
		MigrationDTOOrtsteil other = (MigrationDTOOrtsteil) obj;
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
		return "MigrationDTOOrtsteil(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Ort_ID=" + this.Ort_ID + ", PLZ=" + this.PLZ + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ", SchulnrEigner=" + this.SchulnrEigner + ", OrtsteilSchluessel=" + this.OrtsteilSchluessel + ")";
	}

}