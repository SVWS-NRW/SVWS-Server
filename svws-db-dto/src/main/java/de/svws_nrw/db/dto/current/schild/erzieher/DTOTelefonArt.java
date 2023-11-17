package de.svws_nrw.db.dto.current.schild.erzieher;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle K_TelefonArt.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_TelefonArt")
@NamedQuery(name = "DTOTelefonArt.all", query = "SELECT e FROM DTOTelefonArt e")
@NamedQuery(name = "DTOTelefonArt.id", query = "SELECT e FROM DTOTelefonArt e WHERE e.ID = :value")
@NamedQuery(name = "DTOTelefonArt.id.multiple", query = "SELECT e FROM DTOTelefonArt e WHERE e.ID IN :value")
@NamedQuery(name = "DTOTelefonArt.bezeichnung", query = "SELECT e FROM DTOTelefonArt e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "DTOTelefonArt.bezeichnung.multiple", query = "SELECT e FROM DTOTelefonArt e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "DTOTelefonArt.sortierung", query = "SELECT e FROM DTOTelefonArt e WHERE e.Sortierung = :value")
@NamedQuery(name = "DTOTelefonArt.sortierung.multiple", query = "SELECT e FROM DTOTelefonArt e WHERE e.Sortierung IN :value")
@NamedQuery(name = "DTOTelefonArt.sichtbar", query = "SELECT e FROM DTOTelefonArt e WHERE e.Sichtbar = :value")
@NamedQuery(name = "DTOTelefonArt.sichtbar.multiple", query = "SELECT e FROM DTOTelefonArt e WHERE e.Sichtbar IN :value")
@NamedQuery(name = "DTOTelefonArt.aenderbar", query = "SELECT e FROM DTOTelefonArt e WHERE e.Aenderbar = :value")
@NamedQuery(name = "DTOTelefonArt.aenderbar.multiple", query = "SELECT e FROM DTOTelefonArt e WHERE e.Aenderbar IN :value")
@NamedQuery(name = "DTOTelefonArt.primaryKeyQuery", query = "SELECT e FROM DTOTelefonArt e WHERE e.ID = ?1")
@NamedQuery(name = "DTOTelefonArt.primaryKeyQuery.multiple", query = "SELECT e FROM DTOTelefonArt e WHERE e.ID IN :value")
@NamedQuery(name = "DTOTelefonArt.all.migration", query = "SELECT e FROM DTOTelefonArt e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Bezeichnung", "Sortierung", "Sichtbar", "Aenderbar"})
public final class DTOTelefonArt {

	/** ID der Telefonart */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Bezeichnung der Telefonart */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Sortierung der Telefonart */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichbarkeit der Telefonart */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Änderbarkeit der Telefonart */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOTelefonArt ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOTelefonArt() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOTelefonArt ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public DTOTelefonArt(final long ID, final String Bezeichnung) {
		this.ID = ID;
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
		DTOTelefonArt other = (DTOTelefonArt) obj;
		return ID == other.ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOTelefonArt(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ")";
	}

}
