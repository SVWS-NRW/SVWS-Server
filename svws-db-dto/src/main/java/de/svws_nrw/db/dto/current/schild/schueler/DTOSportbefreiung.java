package de.svws_nrw.db.dto.current.schild.schueler;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Sportbefreiung.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Sportbefreiung")
@NamedQuery(name="DTOSportbefreiung.all", query="SELECT e FROM DTOSportbefreiung e")
@NamedQuery(name="DTOSportbefreiung.id", query="SELECT e FROM DTOSportbefreiung e WHERE e.ID = :value")
@NamedQuery(name="DTOSportbefreiung.id.multiple", query="SELECT e FROM DTOSportbefreiung e WHERE e.ID IN :value")
@NamedQuery(name="DTOSportbefreiung.bezeichnung", query="SELECT e FROM DTOSportbefreiung e WHERE e.Bezeichnung = :value")
@NamedQuery(name="DTOSportbefreiung.bezeichnung.multiple", query="SELECT e FROM DTOSportbefreiung e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="DTOSportbefreiung.sortierung", query="SELECT e FROM DTOSportbefreiung e WHERE e.Sortierung = :value")
@NamedQuery(name="DTOSportbefreiung.sortierung.multiple", query="SELECT e FROM DTOSportbefreiung e WHERE e.Sortierung IN :value")
@NamedQuery(name="DTOSportbefreiung.sichtbar", query="SELECT e FROM DTOSportbefreiung e WHERE e.Sichtbar = :value")
@NamedQuery(name="DTOSportbefreiung.sichtbar.multiple", query="SELECT e FROM DTOSportbefreiung e WHERE e.Sichtbar IN :value")
@NamedQuery(name="DTOSportbefreiung.aenderbar", query="SELECT e FROM DTOSportbefreiung e WHERE e.Aenderbar = :value")
@NamedQuery(name="DTOSportbefreiung.aenderbar.multiple", query="SELECT e FROM DTOSportbefreiung e WHERE e.Aenderbar IN :value")
@NamedQuery(name="DTOSportbefreiung.primaryKeyQuery", query="SELECT e FROM DTOSportbefreiung e WHERE e.ID = ?1")
@NamedQuery(name="DTOSportbefreiung.all.migration", query="SELECT e FROM DTOSportbefreiung e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Bezeichnung","Sortierung","Sichtbar","Aenderbar"})
public class DTOSportbefreiung {

	/** ID der Sportbefreiung */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Bezeichnung der Sportbefreiung */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Sortierung der Sportbefreiung */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichbarkeit der Sportbefreiung */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Änderbarkeit der Sportbefreiung */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSportbefreiung ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSportbefreiung() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSportbefreiung ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public DTOSportbefreiung(final Long ID, final String Bezeichnung) {
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
		DTOSportbefreiung other = (DTOSportbefreiung) obj;
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
		return "DTOSportbefreiung(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ")";
	}

}