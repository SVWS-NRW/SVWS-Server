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
@JsonPropertyOrder({"ID", "Bezeichnung", "Startdatum", "Startzeit", "Enddatum", "Endzeit", "IstSperrtermin", "Bemerkungen"})
public final class DTOGostKlausurenKalenderinformationen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOGostKlausurenKalenderinformationen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.Bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.Bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Startdatum */
	public static final String QUERY_BY_STARTDATUM = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.Startdatum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Startdatum */
	public static final String QUERY_LIST_BY_STARTDATUM = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.Startdatum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Startzeit */
	public static final String QUERY_BY_STARTZEIT = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.Startzeit = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Startzeit */
	public static final String QUERY_LIST_BY_STARTZEIT = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.Startzeit IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Enddatum */
	public static final String QUERY_BY_ENDDATUM = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.Enddatum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Enddatum */
	public static final String QUERY_LIST_BY_ENDDATUM = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.Enddatum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Endzeit */
	public static final String QUERY_BY_ENDZEIT = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.Endzeit = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Endzeit */
	public static final String QUERY_LIST_BY_ENDZEIT = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.Endzeit IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IstSperrtermin */
	public static final String QUERY_BY_ISTSPERRTERMIN = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.IstSperrtermin = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IstSperrtermin */
	public static final String QUERY_LIST_BY_ISTSPERRTERMIN = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.IstSperrtermin IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bemerkungen */
	public static final String QUERY_BY_BEMERKUNGEN = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.Bemerkungen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bemerkungen */
	public static final String QUERY_LIST_BY_BEMERKUNGEN = "SELECT e FROM DTOGostKlausurenKalenderinformationen e WHERE e.Bemerkungen IN ?1";

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
