package de.svws_nrw.db.dto.current.gost.klausurplanung;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.converter.current.DatumConverter;
import de.svws_nrw.db.converter.current.UhrzeitConverter;


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
import de.svws_nrw.csv.converter.current.Boolean01ConverterSerializer;
import de.svws_nrw.csv.converter.current.Boolean01ConverterDeserializer;
import de.svws_nrw.csv.converter.current.DatumConverterSerializer;
import de.svws_nrw.csv.converter.current.DatumConverterDeserializer;
import de.svws_nrw.csv.converter.current.UhrzeitConverterSerializer;
import de.svws_nrw.csv.converter.current.UhrzeitConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Klausuren_Kalenderinformationen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Klausuren_Kalenderinformationen")
@NamedQuery(name = "DTOGostKlausurenKalenderinformationen.all", query = "SELECT e FROM DTOGostKlausurenKalenderinformationen e")
@NamedQuery(name = "DTOGostKlausurenKalenderinformationen.id", query = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.ID = :value")
@NamedQuery(name = "DTOGostKlausurenKalenderinformationen.id.multiple", query = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.ID IN :value")
@NamedQuery(name = "DTOGostKlausurenKalenderinformationen.bezeichnung", query = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "DTOGostKlausurenKalenderinformationen.bezeichnung.multiple", query = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "DTOGostKlausurenKalenderinformationen.startdatum", query = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.Startdatum = :value")
@NamedQuery(name = "DTOGostKlausurenKalenderinformationen.startdatum.multiple", query = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.Startdatum IN :value")
@NamedQuery(name = "DTOGostKlausurenKalenderinformationen.startzeit", query = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.Startzeit = :value")
@NamedQuery(name = "DTOGostKlausurenKalenderinformationen.startzeit.multiple", query = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.Startzeit IN :value")
@NamedQuery(name = "DTOGostKlausurenKalenderinformationen.enddatum", query = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.Enddatum = :value")
@NamedQuery(name = "DTOGostKlausurenKalenderinformationen.enddatum.multiple", query = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.Enddatum IN :value")
@NamedQuery(name = "DTOGostKlausurenKalenderinformationen.endzeit", query = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.Endzeit = :value")
@NamedQuery(name = "DTOGostKlausurenKalenderinformationen.endzeit.multiple", query = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.Endzeit IN :value")
@NamedQuery(name = "DTOGostKlausurenKalenderinformationen.istsperrtermin", query = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.IstSperrtermin = :value")
@NamedQuery(name = "DTOGostKlausurenKalenderinformationen.istsperrtermin.multiple", query = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.IstSperrtermin IN :value")
@NamedQuery(name = "DTOGostKlausurenKalenderinformationen.bemerkungen", query = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.Bemerkungen = :value")
@NamedQuery(name = "DTOGostKlausurenKalenderinformationen.bemerkungen.multiple", query = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.Bemerkungen IN :value")
@NamedQuery(name = "DTOGostKlausurenKalenderinformationen.primaryKeyQuery", query = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.ID = ?1")
@NamedQuery(name = "DTOGostKlausurenKalenderinformationen.primaryKeyQuery.multiple", query = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.ID IN ?1")
@NamedQuery(name = "DTOGostKlausurenKalenderinformationen.all.migration", query = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Bezeichnung", "Startdatum", "Startzeit", "Enddatum", "Endzeit", "IstSperrtermin", "Bemerkungen"})
public final class DTOGostKlausurenKalenderinformationen {

	/** ID der Kalenderinformation (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Text für Bezeichnung der Kalenderinformation */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Startdatum für den Kalendereintrag */
	@Column(name = "Startdatum")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String Startdatum;

	/** Startzeit für den Kalendereintrag */
	@Column(name = "Startzeit")
	@JsonProperty
	@Convert(converter = UhrzeitConverter.class)
	@JsonSerialize(using = UhrzeitConverterSerializer.class)
	@JsonDeserialize(using = UhrzeitConverterDeserializer.class)
	public Integer Startzeit;

	/** Enddatum für den Kalendereintrag */
	@Column(name = "Enddatum")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String Enddatum;

	/** Endzeit für den Kalendereintrag */
	@Column(name = "Endzeit")
	@JsonProperty
	@Convert(converter = UhrzeitConverter.class)
	@JsonSerialize(using = UhrzeitConverterSerializer.class)
	@JsonDeserialize(using = UhrzeitConverterDeserializer.class)
	public Integer Endzeit;

	/** Gibt an, ob es sich um einen Sperrtermin handelt oder nicht: 1 - true, 0 - false. */
	@Column(name = "IstSperrtermin")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean IstSperrtermin;

	/** Text für Bemerkungen zur Kalenderinformation */
	@Column(name = "Bemerkungen")
	@JsonProperty
	public String Bemerkungen;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenKalenderinformationen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostKlausurenKalenderinformationen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenKalenderinformationen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param IstSperrtermin   der Wert für das Attribut IstSperrtermin
	 */
	public DTOGostKlausurenKalenderinformationen(final long ID, final Boolean IstSperrtermin) {
		this.ID = ID;
		this.IstSperrtermin = IstSperrtermin;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostKlausurenKalenderinformationen other = (DTOGostKlausurenKalenderinformationen) obj;
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
		return "DTOGostKlausurenKalenderinformationen(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Startdatum=" + this.Startdatum + ", Startzeit=" + this.Startzeit + ", Enddatum=" + this.Enddatum + ", Endzeit=" + this.Endzeit + ", IstSperrtermin=" + this.IstSperrtermin + ", Bemerkungen=" + this.Bemerkungen + ")";
	}

}
