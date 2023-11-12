package de.svws_nrw.db.dto.current.schild.berufskolleg;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle K_BeschaeftigungsArt.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_BeschaeftigungsArt")
@NamedQuery(name = "DTOBeschaeftigungsart.all", query = "SELECT e FROM DTOBeschaeftigungsart e")
@NamedQuery(name = "DTOBeschaeftigungsart.id", query = "SELECT e FROM DTOBeschaeftigungsart e WHERE e.ID = :value")
@NamedQuery(name = "DTOBeschaeftigungsart.id.multiple", query = "SELECT e FROM DTOBeschaeftigungsart e WHERE e.ID IN :value")
@NamedQuery(name = "DTOBeschaeftigungsart.bezeichnung", query = "SELECT e FROM DTOBeschaeftigungsart e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "DTOBeschaeftigungsart.bezeichnung.multiple", query = "SELECT e FROM DTOBeschaeftigungsart e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "DTOBeschaeftigungsart.sortierung", query = "SELECT e FROM DTOBeschaeftigungsart e WHERE e.Sortierung = :value")
@NamedQuery(name = "DTOBeschaeftigungsart.sortierung.multiple", query = "SELECT e FROM DTOBeschaeftigungsart e WHERE e.Sortierung IN :value")
@NamedQuery(name = "DTOBeschaeftigungsart.sichtbar", query = "SELECT e FROM DTOBeschaeftigungsart e WHERE e.Sichtbar = :value")
@NamedQuery(name = "DTOBeschaeftigungsart.sichtbar.multiple", query = "SELECT e FROM DTOBeschaeftigungsart e WHERE e.Sichtbar IN :value")
@NamedQuery(name = "DTOBeschaeftigungsart.aenderbar", query = "SELECT e FROM DTOBeschaeftigungsart e WHERE e.Aenderbar = :value")
@NamedQuery(name = "DTOBeschaeftigungsart.aenderbar.multiple", query = "SELECT e FROM DTOBeschaeftigungsart e WHERE e.Aenderbar IN :value")
@NamedQuery(name = "DTOBeschaeftigungsart.primaryKeyQuery", query = "SELECT e FROM DTOBeschaeftigungsart e WHERE e.ID = ?1")
@NamedQuery(name = "DTOBeschaeftigungsart.primaryKeyQuery.multiple", query = "SELECT e FROM DTOBeschaeftigungsart e WHERE e.ID IN ?1")
@NamedQuery(name = "DTOBeschaeftigungsart.all.migration", query = "SELECT e FROM DTOBeschaeftigungsart e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Bezeichnung", "Sortierung", "Sichtbar", "Aenderbar"})
public final class DTOBeschaeftigungsart {

	/** ID der Beschäftigungsart unter weitere Adressen */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Bezeichnung der Beschäftigungsart */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Sortierung der Beschäftigungsart */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichtbarkeit der Beschäftigungsart */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Beschäftigungsart ist änderbar Ja Nein */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOBeschaeftigungsart ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOBeschaeftigungsart() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOBeschaeftigungsart ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public DTOBeschaeftigungsart(final long ID, final String Bezeichnung) {
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
		DTOBeschaeftigungsart other = (DTOBeschaeftigungsart) obj;
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
		return "DTOBeschaeftigungsart(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ")";
	}

}
