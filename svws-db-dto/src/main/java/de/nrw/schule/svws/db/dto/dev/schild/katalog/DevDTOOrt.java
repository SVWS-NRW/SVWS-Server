package de.nrw.schule.svws.db.dto.dev.schild.katalog;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultPlusConverter;


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
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Ort.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Ort")
@NamedQuery(name="DevDTOOrt.all", query="SELECT e FROM DevDTOOrt e")
@NamedQuery(name="DevDTOOrt.id", query="SELECT e FROM DevDTOOrt e WHERE e.ID = :value")
@NamedQuery(name="DevDTOOrt.id.multiple", query="SELECT e FROM DevDTOOrt e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOOrt.plz", query="SELECT e FROM DevDTOOrt e WHERE e.PLZ = :value")
@NamedQuery(name="DevDTOOrt.plz.multiple", query="SELECT e FROM DevDTOOrt e WHERE e.PLZ IN :value")
@NamedQuery(name="DevDTOOrt.bezeichnung", query="SELECT e FROM DevDTOOrt e WHERE e.Bezeichnung = :value")
@NamedQuery(name="DevDTOOrt.bezeichnung.multiple", query="SELECT e FROM DevDTOOrt e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="DevDTOOrt.kreis", query="SELECT e FROM DevDTOOrt e WHERE e.Kreis = :value")
@NamedQuery(name="DevDTOOrt.kreis.multiple", query="SELECT e FROM DevDTOOrt e WHERE e.Kreis IN :value")
@NamedQuery(name="DevDTOOrt.sortierung", query="SELECT e FROM DevDTOOrt e WHERE e.Sortierung = :value")
@NamedQuery(name="DevDTOOrt.sortierung.multiple", query="SELECT e FROM DevDTOOrt e WHERE e.Sortierung IN :value")
@NamedQuery(name="DevDTOOrt.sichtbar", query="SELECT e FROM DevDTOOrt e WHERE e.Sichtbar = :value")
@NamedQuery(name="DevDTOOrt.sichtbar.multiple", query="SELECT e FROM DevDTOOrt e WHERE e.Sichtbar IN :value")
@NamedQuery(name="DevDTOOrt.aenderbar", query="SELECT e FROM DevDTOOrt e WHERE e.Aenderbar = :value")
@NamedQuery(name="DevDTOOrt.aenderbar.multiple", query="SELECT e FROM DevDTOOrt e WHERE e.Aenderbar IN :value")
@NamedQuery(name="DevDTOOrt.land", query="SELECT e FROM DevDTOOrt e WHERE e.Land = :value")
@NamedQuery(name="DevDTOOrt.land.multiple", query="SELECT e FROM DevDTOOrt e WHERE e.Land IN :value")
@NamedQuery(name="DevDTOOrt.primaryKeyQuery", query="SELECT e FROM DevDTOOrt e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOOrt.all.migration", query="SELECT e FROM DevDTOOrt e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","PLZ","Bezeichnung","Kreis","Sortierung","Sichtbar","Aenderbar","Land"})
public class DevDTOOrt {

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
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Änderbarkeit des Ortes */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/** Land des Ortes */
	@Column(name = "Land")
	@JsonProperty
	public String Land;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOOrt ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOOrt() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOOrt ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param PLZ   der Wert für das Attribut PLZ
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public DevDTOOrt(final Long ID, final String PLZ, final String Bezeichnung) {
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevDTOOrt other = (DevDTOOrt) obj;
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
		return "DevDTOOrt(ID=" + this.ID + ", PLZ=" + this.PLZ + ", Bezeichnung=" + this.Bezeichnung + ", Kreis=" + this.Kreis + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ", Land=" + this.Land + ")";
	}

}