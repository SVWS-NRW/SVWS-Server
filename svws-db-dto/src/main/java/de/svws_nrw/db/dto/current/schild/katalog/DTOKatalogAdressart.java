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
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Adressart.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Adressart")
@NamedQuery(name = "DTOKatalogAdressart.all", query = "SELECT e FROM DTOKatalogAdressart e")
@NamedQuery(name = "DTOKatalogAdressart.id", query = "SELECT e FROM DTOKatalogAdressart e WHERE e.ID = :value")
@NamedQuery(name = "DTOKatalogAdressart.id.multiple", query = "SELECT e FROM DTOKatalogAdressart e WHERE e.ID IN :value")
@NamedQuery(name = "DTOKatalogAdressart.bezeichnung", query = "SELECT e FROM DTOKatalogAdressart e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "DTOKatalogAdressart.bezeichnung.multiple", query = "SELECT e FROM DTOKatalogAdressart e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "DTOKatalogAdressart.sortierung", query = "SELECT e FROM DTOKatalogAdressart e WHERE e.Sortierung = :value")
@NamedQuery(name = "DTOKatalogAdressart.sortierung.multiple", query = "SELECT e FROM DTOKatalogAdressart e WHERE e.Sortierung IN :value")
@NamedQuery(name = "DTOKatalogAdressart.sichtbar", query = "SELECT e FROM DTOKatalogAdressart e WHERE e.Sichtbar = :value")
@NamedQuery(name = "DTOKatalogAdressart.sichtbar.multiple", query = "SELECT e FROM DTOKatalogAdressart e WHERE e.Sichtbar IN :value")
@NamedQuery(name = "DTOKatalogAdressart.aenderbar", query = "SELECT e FROM DTOKatalogAdressart e WHERE e.Aenderbar = :value")
@NamedQuery(name = "DTOKatalogAdressart.aenderbar.multiple", query = "SELECT e FROM DTOKatalogAdressart e WHERE e.Aenderbar IN :value")
@NamedQuery(name = "DTOKatalogAdressart.primaryKeyQuery", query = "SELECT e FROM DTOKatalogAdressart e WHERE e.ID = ?1")
@NamedQuery(name = "DTOKatalogAdressart.primaryKeyQuery.multiple", query = "SELECT e FROM DTOKatalogAdressart e WHERE e.ID IN ?1")
@NamedQuery(name = "DTOKatalogAdressart.all.migration", query = "SELECT e FROM DTOKatalogAdressart e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Bezeichnung", "Sortierung", "Sichtbar", "Aenderbar"})
public final class DTOKatalogAdressart {

	/** ID der Adressart */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Bezeichnung der Adressart (Betrieb Kammer usw) */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Sortierung der Adressart */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichtbarkeit der Adressart */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Adressart ist änderbar Ja Nein */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKatalogAdressart ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOKatalogAdressart() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKatalogAdressart ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public DTOKatalogAdressart(final long ID, final String Bezeichnung) {
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
		DTOKatalogAdressart other = (DTOKatalogAdressart) obj;
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
		return "DTOKatalogAdressart(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ")";
	}

}
