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
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Datenschutz.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Datenschutz")
@NamedQuery(name = "DTOKatalogDatenschutz.all", query = "SELECT e FROM DTOKatalogDatenschutz e")
@NamedQuery(name = "DTOKatalogDatenschutz.id", query = "SELECT e FROM DTOKatalogDatenschutz e WHERE e.ID = :value")
@NamedQuery(name = "DTOKatalogDatenschutz.id.multiple", query = "SELECT e FROM DTOKatalogDatenschutz e WHERE e.ID IN :value")
@NamedQuery(name = "DTOKatalogDatenschutz.bezeichnung", query = "SELECT e FROM DTOKatalogDatenschutz e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "DTOKatalogDatenschutz.bezeichnung.multiple", query = "SELECT e FROM DTOKatalogDatenschutz e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "DTOKatalogDatenschutz.sichtbar", query = "SELECT e FROM DTOKatalogDatenschutz e WHERE e.Sichtbar = :value")
@NamedQuery(name = "DTOKatalogDatenschutz.sichtbar.multiple", query = "SELECT e FROM DTOKatalogDatenschutz e WHERE e.Sichtbar IN :value")
@NamedQuery(name = "DTOKatalogDatenschutz.schluessel", query = "SELECT e FROM DTOKatalogDatenschutz e WHERE e.Schluessel = :value")
@NamedQuery(name = "DTOKatalogDatenschutz.schluessel.multiple", query = "SELECT e FROM DTOKatalogDatenschutz e WHERE e.Schluessel IN :value")
@NamedQuery(name = "DTOKatalogDatenschutz.sortierung", query = "SELECT e FROM DTOKatalogDatenschutz e WHERE e.Sortierung = :value")
@NamedQuery(name = "DTOKatalogDatenschutz.sortierung.multiple", query = "SELECT e FROM DTOKatalogDatenschutz e WHERE e.Sortierung IN :value")
@NamedQuery(name = "DTOKatalogDatenschutz.beschreibung", query = "SELECT e FROM DTOKatalogDatenschutz e WHERE e.Beschreibung = :value")
@NamedQuery(name = "DTOKatalogDatenschutz.beschreibung.multiple", query = "SELECT e FROM DTOKatalogDatenschutz e WHERE e.Beschreibung IN :value")
@NamedQuery(name = "DTOKatalogDatenschutz.personart", query = "SELECT e FROM DTOKatalogDatenschutz e WHERE e.PersonArt = :value")
@NamedQuery(name = "DTOKatalogDatenschutz.personart.multiple", query = "SELECT e FROM DTOKatalogDatenschutz e WHERE e.PersonArt IN :value")
@NamedQuery(name = "DTOKatalogDatenschutz.primaryKeyQuery", query = "SELECT e FROM DTOKatalogDatenschutz e WHERE e.ID = ?1")
@NamedQuery(name = "DTOKatalogDatenschutz.all.migration", query = "SELECT e FROM DTOKatalogDatenschutz e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Bezeichnung", "Sichtbar", "Schluessel", "Sortierung", "Beschreibung", "PersonArt"})
public final class DTOKatalogDatenschutz {

	/** Eindeutige ID für den Datensatz */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Eine kurze Bezeichnung des DSGVO-Merkmals */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Regelt die Sichtbarkeit des Merkmals bei der Ansicht der Schülertabelle  */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Fest vorgegebene Werte, die es in Schild-NRW später ermöglichen, die DSGVO-Merkmale zu erkennen */
	@Column(name = "Schluessel")
	@JsonProperty
	public String Schluessel;

	/** Gibt die Reihenfolge der Merkmale bei der Darstellung an. */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Eine ausführliche Beschreibung des DSGCO-Merkmals */
	@Column(name = "Beschreibung")
	@JsonProperty
	public String Beschreibung;

	/** PersonenArt des Datenschutz-Eintrags (S=Schueler L=Lehrer) */
	@Column(name = "PersonArt")
	@JsonProperty
	public String PersonArt;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKatalogDatenschutz ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOKatalogDatenschutz() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKatalogDatenschutz ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 * @param Sichtbar   der Wert für das Attribut Sichtbar
	 * @param Sortierung   der Wert für das Attribut Sortierung
	 */
	public DTOKatalogDatenschutz(final Long ID, final String Bezeichnung, final Boolean Sichtbar, final Integer Sortierung) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Bezeichnung == null) {
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
		if (Sichtbar == null) {
			throw new NullPointerException("Sichtbar must not be null");
		}
		this.Sichtbar = Sichtbar;
		if (Sortierung == null) {
			throw new NullPointerException("Sortierung must not be null");
		}
		this.Sortierung = Sortierung;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOKatalogDatenschutz other = (DTOKatalogDatenschutz) obj;
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
		return "DTOKatalogDatenschutz(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Sichtbar=" + this.Sichtbar + ", Schluessel=" + this.Schluessel + ", Sortierung=" + this.Sortierung + ", Beschreibung=" + this.Beschreibung + ", PersonArt=" + this.PersonArt + ")";
	}

}
