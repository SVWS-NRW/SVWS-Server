package de.nrw.schule.svws.db.dto.dev.schild.schueler;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle K_EntlassGrund.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_EntlassGrund")
@NamedQuery(name="DevDTOEntlassarten.all", query="SELECT e FROM DevDTOEntlassarten e")
@NamedQuery(name="DevDTOEntlassarten.id", query="SELECT e FROM DevDTOEntlassarten e WHERE e.ID = :value")
@NamedQuery(name="DevDTOEntlassarten.id.multiple", query="SELECT e FROM DevDTOEntlassarten e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOEntlassarten.bezeichnung", query="SELECT e FROM DevDTOEntlassarten e WHERE e.Bezeichnung = :value")
@NamedQuery(name="DevDTOEntlassarten.bezeichnung.multiple", query="SELECT e FROM DevDTOEntlassarten e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="DevDTOEntlassarten.sortierung", query="SELECT e FROM DevDTOEntlassarten e WHERE e.Sortierung = :value")
@NamedQuery(name="DevDTOEntlassarten.sortierung.multiple", query="SELECT e FROM DevDTOEntlassarten e WHERE e.Sortierung IN :value")
@NamedQuery(name="DevDTOEntlassarten.sichtbar", query="SELECT e FROM DevDTOEntlassarten e WHERE e.Sichtbar = :value")
@NamedQuery(name="DevDTOEntlassarten.sichtbar.multiple", query="SELECT e FROM DevDTOEntlassarten e WHERE e.Sichtbar IN :value")
@NamedQuery(name="DevDTOEntlassarten.aenderbar", query="SELECT e FROM DevDTOEntlassarten e WHERE e.Aenderbar = :value")
@NamedQuery(name="DevDTOEntlassarten.aenderbar.multiple", query="SELECT e FROM DevDTOEntlassarten e WHERE e.Aenderbar IN :value")
@NamedQuery(name="DevDTOEntlassarten.primaryKeyQuery", query="SELECT e FROM DevDTOEntlassarten e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOEntlassarten.all.migration", query="SELECT e FROM DevDTOEntlassarten e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Bezeichnung","Sortierung","Sichtbar","Aenderbar"})
public class DevDTOEntlassarten {

	/** ID des Entlassgrundes */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Bezeichnung des Entlassgrundes */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Sortierung des Entlassgrundes */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichbarkeit des Entlassgrundes */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Änderbarkeit des Entlassgrundes */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOEntlassarten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOEntlassarten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOEntlassarten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public DevDTOEntlassarten(final Long ID, final String Bezeichnung) {
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
		DevDTOEntlassarten other = (DevDTOEntlassarten) obj;
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
		return "DevDTOEntlassarten(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ")";
	}

}