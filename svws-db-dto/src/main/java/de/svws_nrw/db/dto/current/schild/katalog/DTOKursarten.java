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
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Kursart.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Kursart")
@NamedQuery(name = "DTOKursarten.all", query = "SELECT e FROM DTOKursarten e")
@NamedQuery(name = "DTOKursarten.id", query = "SELECT e FROM DTOKursarten e WHERE e.ID = :value")
@NamedQuery(name = "DTOKursarten.id.multiple", query = "SELECT e FROM DTOKursarten e WHERE e.ID IN :value")
@NamedQuery(name = "DTOKursarten.bezeichnung", query = "SELECT e FROM DTOKursarten e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "DTOKursarten.bezeichnung.multiple", query = "SELECT e FROM DTOKursarten e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "DTOKursarten.internbez", query = "SELECT e FROM DTOKursarten e WHERE e.InternBez = :value")
@NamedQuery(name = "DTOKursarten.internbez.multiple", query = "SELECT e FROM DTOKursarten e WHERE e.InternBez IN :value")
@NamedQuery(name = "DTOKursarten.kursart", query = "SELECT e FROM DTOKursarten e WHERE e.Kursart = :value")
@NamedQuery(name = "DTOKursarten.kursart.multiple", query = "SELECT e FROM DTOKursarten e WHERE e.Kursart IN :value")
@NamedQuery(name = "DTOKursarten.kursartallg", query = "SELECT e FROM DTOKursarten e WHERE e.KursartAllg = :value")
@NamedQuery(name = "DTOKursarten.kursartallg.multiple", query = "SELECT e FROM DTOKursarten e WHERE e.KursartAllg IN :value")
@NamedQuery(name = "DTOKursarten.sortierung", query = "SELECT e FROM DTOKursarten e WHERE e.Sortierung = :value")
@NamedQuery(name = "DTOKursarten.sortierung.multiple", query = "SELECT e FROM DTOKursarten e WHERE e.Sortierung IN :value")
@NamedQuery(name = "DTOKursarten.sichtbar", query = "SELECT e FROM DTOKursarten e WHERE e.Sichtbar = :value")
@NamedQuery(name = "DTOKursarten.sichtbar.multiple", query = "SELECT e FROM DTOKursarten e WHERE e.Sichtbar IN :value")
@NamedQuery(name = "DTOKursarten.aenderbar", query = "SELECT e FROM DTOKursarten e WHERE e.Aenderbar = :value")
@NamedQuery(name = "DTOKursarten.aenderbar.multiple", query = "SELECT e FROM DTOKursarten e WHERE e.Aenderbar IN :value")
@NamedQuery(name = "DTOKursarten.primaryKeyQuery", query = "SELECT e FROM DTOKursarten e WHERE e.ID = ?1")
@NamedQuery(name = "DTOKursarten.primaryKeyQuery.multiple", query = "SELECT e FROM DTOKursarten e WHERE e.ID IN :value")
@NamedQuery(name = "DTOKursarten.all.migration", query = "SELECT e FROM DTOKursarten e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Bezeichnung", "InternBez", "Kursart", "KursartAllg", "Sortierung", "Sichtbar", "Aenderbar"})
public final class DTOKursarten {

	/** ID des Kursarteneintrag */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Bezeichnung des Kursarteneintrags IT.NRW */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Interne Bezeichnung Kursarteneintrag */
	@Column(name = "InternBez")
	@JsonProperty
	public String InternBez;

	/** Kürzel Kursart */
	@Column(name = "Kursart")
	@JsonProperty
	public String Kursart;

	/** Allgemeine Bezeichnung Kursart (zB GK bei GKM) */
	@Column(name = "KursartAllg")
	@JsonProperty
	public String KursartAllg;

	/** Sortierung der Kursart */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichtbarkeit der Kursart */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Kursart änderbar Ja Nein */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKursarten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOKursarten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKursarten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public DTOKursarten(final long ID) {
		this.ID = ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOKursarten other = (DTOKursarten) obj;
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
		return "DTOKursarten(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", InternBez=" + this.InternBez + ", Kursart=" + this.Kursart + ", KursartAllg=" + this.KursartAllg + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ")";
	}

}
