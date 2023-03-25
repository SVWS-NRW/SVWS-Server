package de.svws_nrw.db.dto.current.schild.katalog;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;


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
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Ortsteil.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Ortsteil")
@NamedQuery(name="DTOOrtsteil.all", query="SELECT e FROM DTOOrtsteil e")
@NamedQuery(name="DTOOrtsteil.id", query="SELECT e FROM DTOOrtsteil e WHERE e.ID = :value")
@NamedQuery(name="DTOOrtsteil.id.multiple", query="SELECT e FROM DTOOrtsteil e WHERE e.ID IN :value")
@NamedQuery(name="DTOOrtsteil.bezeichnung", query="SELECT e FROM DTOOrtsteil e WHERE e.Bezeichnung = :value")
@NamedQuery(name="DTOOrtsteil.bezeichnung.multiple", query="SELECT e FROM DTOOrtsteil e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="DTOOrtsteil.ort_id", query="SELECT e FROM DTOOrtsteil e WHERE e.Ort_ID = :value")
@NamedQuery(name="DTOOrtsteil.ort_id.multiple", query="SELECT e FROM DTOOrtsteil e WHERE e.Ort_ID IN :value")
@NamedQuery(name="DTOOrtsteil.sortierung", query="SELECT e FROM DTOOrtsteil e WHERE e.Sortierung = :value")
@NamedQuery(name="DTOOrtsteil.sortierung.multiple", query="SELECT e FROM DTOOrtsteil e WHERE e.Sortierung IN :value")
@NamedQuery(name="DTOOrtsteil.sichtbar", query="SELECT e FROM DTOOrtsteil e WHERE e.Sichtbar = :value")
@NamedQuery(name="DTOOrtsteil.sichtbar.multiple", query="SELECT e FROM DTOOrtsteil e WHERE e.Sichtbar IN :value")
@NamedQuery(name="DTOOrtsteil.aenderbar", query="SELECT e FROM DTOOrtsteil e WHERE e.Aenderbar = :value")
@NamedQuery(name="DTOOrtsteil.aenderbar.multiple", query="SELECT e FROM DTOOrtsteil e WHERE e.Aenderbar IN :value")
@NamedQuery(name="DTOOrtsteil.ortsteilschluessel", query="SELECT e FROM DTOOrtsteil e WHERE e.OrtsteilSchluessel = :value")
@NamedQuery(name="DTOOrtsteil.ortsteilschluessel.multiple", query="SELECT e FROM DTOOrtsteil e WHERE e.OrtsteilSchluessel IN :value")
@NamedQuery(name="DTOOrtsteil.primaryKeyQuery", query="SELECT e FROM DTOOrtsteil e WHERE e.ID = ?1")
@NamedQuery(name="DTOOrtsteil.all.migration", query="SELECT e FROM DTOOrtsteil e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Bezeichnung","Ort_ID","Sortierung","Sichtbar","Aenderbar","OrtsteilSchluessel"})
public class DTOOrtsteil {

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

	/** Sortierung des Ortsteils */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichbarkeit des Ortsteils */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Änderbarkeit des Ortsteils */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/** Schlüssel des Ortsteils (Regional?) */
	@Column(name = "OrtsteilSchluessel")
	@JsonProperty
	public String OrtsteilSchluessel;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOOrtsteil ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOOrtsteil() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOOrtsteil ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public DTOOrtsteil(final Long ID, final String Bezeichnung) {
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
		DTOOrtsteil other = (DTOOrtsteil) obj;
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
		return "DTOOrtsteil(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Ort_ID=" + this.Ort_ID + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ", OrtsteilSchluessel=" + this.OrtsteilSchluessel + ")";
	}

}