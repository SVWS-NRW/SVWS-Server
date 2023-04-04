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
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Ort.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Ort")
@NamedQuery(name = "MigrationDTOOrt.all", query = "SELECT e FROM MigrationDTOOrt e")
@NamedQuery(name = "MigrationDTOOrt.id", query = "SELECT e FROM MigrationDTOOrt e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOOrt.id.multiple", query = "SELECT e FROM MigrationDTOOrt e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOOrt.plz", query = "SELECT e FROM MigrationDTOOrt e WHERE e.PLZ = :value")
@NamedQuery(name = "MigrationDTOOrt.plz.multiple", query = "SELECT e FROM MigrationDTOOrt e WHERE e.PLZ IN :value")
@NamedQuery(name = "MigrationDTOOrt.bezeichnung", query = "SELECT e FROM MigrationDTOOrt e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "MigrationDTOOrt.bezeichnung.multiple", query = "SELECT e FROM MigrationDTOOrt e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "MigrationDTOOrt.kreis", query = "SELECT e FROM MigrationDTOOrt e WHERE e.Kreis = :value")
@NamedQuery(name = "MigrationDTOOrt.kreis.multiple", query = "SELECT e FROM MigrationDTOOrt e WHERE e.Kreis IN :value")
@NamedQuery(name = "MigrationDTOOrt.sortierung", query = "SELECT e FROM MigrationDTOOrt e WHERE e.Sortierung = :value")
@NamedQuery(name = "MigrationDTOOrt.sortierung.multiple", query = "SELECT e FROM MigrationDTOOrt e WHERE e.Sortierung IN :value")
@NamedQuery(name = "MigrationDTOOrt.sichtbar", query = "SELECT e FROM MigrationDTOOrt e WHERE e.Sichtbar = :value")
@NamedQuery(name = "MigrationDTOOrt.sichtbar.multiple", query = "SELECT e FROM MigrationDTOOrt e WHERE e.Sichtbar IN :value")
@NamedQuery(name = "MigrationDTOOrt.aenderbar", query = "SELECT e FROM MigrationDTOOrt e WHERE e.Aenderbar = :value")
@NamedQuery(name = "MigrationDTOOrt.aenderbar.multiple", query = "SELECT e FROM MigrationDTOOrt e WHERE e.Aenderbar IN :value")
@NamedQuery(name = "MigrationDTOOrt.land", query = "SELECT e FROM MigrationDTOOrt e WHERE e.Land = :value")
@NamedQuery(name = "MigrationDTOOrt.land.multiple", query = "SELECT e FROM MigrationDTOOrt e WHERE e.Land IN :value")
@NamedQuery(name = "MigrationDTOOrt.schulnreigner", query = "SELECT e FROM MigrationDTOOrt e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOOrt.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOOrt e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOOrt.primaryKeyQuery", query = "SELECT e FROM MigrationDTOOrt e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOOrt.all.migration", query = "SELECT e FROM MigrationDTOOrt e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "PLZ", "Bezeichnung", "Kreis", "Sortierung", "Sichtbar", "Aenderbar", "Land", "SchulnrEigner"})
public final class MigrationDTOOrt {

	/** ID des Ortes */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** PLZ des Ortes */
	@Column(name = "PLZ")
	@JsonProperty
	public String PLZ;

	/** Bezeichnung des Ortes */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Kreis des Ortes */
	@Column(name = "Kreis")
	@JsonProperty
	public String Kreis;

	/** Sortierung des Ortes */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichbarkeit des Ortes */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Änderbarkeit des Ortes */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/** Land des Ortes */
	@Column(name = "Land")
	@JsonProperty
	public String Land;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOOrt ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOOrt() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOOrt ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param PLZ   der Wert für das Attribut PLZ
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public MigrationDTOOrt(final Long ID, final String PLZ, final String Bezeichnung) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (PLZ == null) {
			throw new NullPointerException("PLZ must not be null");
		}
		this.PLZ = PLZ;
		if (Bezeichnung == null) {
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOOrt other = (MigrationDTOOrt) obj;
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
		return "MigrationDTOOrt(ID=" + this.ID + ", PLZ=" + this.PLZ + ", Bezeichnung=" + this.Bezeichnung + ", Kreis=" + this.Kreis + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ", Land=" + this.Land + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}
