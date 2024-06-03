package de.svws_nrw.db.dto.current.schild.katalog;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@JsonPropertyOrder({"ID", "Bezeichnung", "InternBez", "Kursart", "KursartAllg", "Sortierung", "Sichtbar", "Aenderbar"})
public final class DTOKursarten {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOKursarten e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOKursarten e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOKursarten e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOKursarten e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOKursarten e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOKursarten e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM DTOKursarten e WHERE e.Bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM DTOKursarten e WHERE e.Bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes InternBez */
	public static final String QUERY_BY_INTERNBEZ = "SELECT e FROM DTOKursarten e WHERE e.InternBez = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes InternBez */
	public static final String QUERY_LIST_BY_INTERNBEZ = "SELECT e FROM DTOKursarten e WHERE e.InternBez IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kursart */
	public static final String QUERY_BY_KURSART = "SELECT e FROM DTOKursarten e WHERE e.Kursart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kursart */
	public static final String QUERY_LIST_BY_KURSART = "SELECT e FROM DTOKursarten e WHERE e.Kursart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KursartAllg */
	public static final String QUERY_BY_KURSARTALLG = "SELECT e FROM DTOKursarten e WHERE e.KursartAllg = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KursartAllg */
	public static final String QUERY_LIST_BY_KURSARTALLG = "SELECT e FROM DTOKursarten e WHERE e.KursartAllg IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM DTOKursarten e WHERE e.Sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM DTOKursarten e WHERE e.Sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sichtbar */
	public static final String QUERY_BY_SICHTBAR = "SELECT e FROM DTOKursarten e WHERE e.Sichtbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sichtbar */
	public static final String QUERY_LIST_BY_SICHTBAR = "SELECT e FROM DTOKursarten e WHERE e.Sichtbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Aenderbar */
	public static final String QUERY_BY_AENDERBAR = "SELECT e FROM DTOKursarten e WHERE e.Aenderbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Aenderbar */
	public static final String QUERY_LIST_BY_AENDERBAR = "SELECT e FROM DTOKursarten e WHERE e.Aenderbar IN ?1";

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
