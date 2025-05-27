package de.svws_nrw.db.dto.current.gost.klausurplanung;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.converter.current.DatumConverter;
import de.svws_nrw.db.converter.current.UhrzeitConverter;
import de.svws_nrw.db.converter.current.gost.GOStHalbjahrConverter;

import de.svws_nrw.core.types.gost.GostHalbjahr;


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
import de.svws_nrw.csv.converter.current.gost.GOStHalbjahrConverterSerializer;
import de.svws_nrw.csv.converter.current.gost.GOStHalbjahrConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Klausuren_Termine.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Klausuren_Termine")
@JsonPropertyOrder({"ID", "Schuljahresabschnitt_ID", "Abi_Jahrgang", "Halbjahr", "Quartal", "Datum", "Startzeit", "Bezeichnung", "Bemerkungen", "IstHaupttermin", "NachschreiberZugelassen"})
public final class DTOGostKlausurenTermine {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOGostKlausurenTermine e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schuljahresabschnitt_ID */
	public static final String QUERY_BY_SCHULJAHRESABSCHNITT_ID = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.Schuljahresabschnitt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schuljahresabschnitt_ID */
	public static final String QUERY_LIST_BY_SCHULJAHRESABSCHNITT_ID = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.Schuljahresabschnitt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abi_Jahrgang */
	public static final String QUERY_BY_ABI_JAHRGANG = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.Abi_Jahrgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abi_Jahrgang */
	public static final String QUERY_LIST_BY_ABI_JAHRGANG = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.Abi_Jahrgang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Halbjahr */
	public static final String QUERY_BY_HALBJAHR = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.Halbjahr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Halbjahr */
	public static final String QUERY_LIST_BY_HALBJAHR = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.Halbjahr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Quartal */
	public static final String QUERY_BY_QUARTAL = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.Quartal = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Quartal */
	public static final String QUERY_LIST_BY_QUARTAL = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.Quartal IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Datum */
	public static final String QUERY_BY_DATUM = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.Datum = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Datum */
	public static final String QUERY_LIST_BY_DATUM = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.Datum IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Startzeit */
	public static final String QUERY_BY_STARTZEIT = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.Startzeit = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Startzeit */
	public static final String QUERY_LIST_BY_STARTZEIT = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.Startzeit IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.Bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.Bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bemerkungen */
	public static final String QUERY_BY_BEMERKUNGEN = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.Bemerkungen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bemerkungen */
	public static final String QUERY_LIST_BY_BEMERKUNGEN = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.Bemerkungen IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IstHaupttermin */
	public static final String QUERY_BY_ISTHAUPTTERMIN = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.IstHaupttermin = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IstHaupttermin */
	public static final String QUERY_LIST_BY_ISTHAUPTTERMIN = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.IstHaupttermin IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NachschreiberZugelassen */
	public static final String QUERY_BY_NACHSCHREIBERZUGELASSEN = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.NachschreiberZugelassen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NachschreiberZugelassen */
	public static final String QUERY_LIST_BY_NACHSCHREIBERZUGELASSEN = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.NachschreiberZugelassen IN ?1";

	/** ID des Klausurtermins (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** ID des Schuljahresabschnittes */
	@Column(name = "Schuljahresabschnitt_ID")
	@JsonProperty
	public long Schuljahresabschnitt_ID;

	/** Der Abiturjahrgang, dem die Klausurvorgabe zugeordnet ist */
	@Column(name = "Abi_Jahrgang")
	@JsonProperty
	public int Abi_Jahrgang;

	/** Das Halbjahr, welchem die Klausurvorgabe zugeordnet ist (0=EF.1, 1=EF.2, 2=Q1.1, 3=Q1.2, 4=Q2.1, 5=Q2.2) */
	@Column(name = "Halbjahr")
	@JsonProperty
	@Convert(converter = GOStHalbjahrConverter.class)
	@JsonSerialize(using = GOStHalbjahrConverterSerializer.class)
	@JsonDeserialize(using = GOStHalbjahrConverterDeserializer.class)
	public GostHalbjahr Halbjahr;

	/** Das Quartal, in dem die Klausur geschrieben wird. */
	@Column(name = "Quartal")
	@JsonProperty
	public int Quartal;

	/** Das Datum des Klausurtermins */
	@Column(name = "Datum")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String Datum;

	/** Die Startzeit des Klausurtermins */
	@Column(name = "Startzeit")
	@JsonProperty
	@Convert(converter = UhrzeitConverter.class)
	@JsonSerialize(using = UhrzeitConverterSerializer.class)
	@JsonDeserialize(using = UhrzeitConverterDeserializer.class)
	public Integer Startzeit;

	/** Text für Bezeichnung des Klausurtermins */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Text für Bemerkungen des Klausurtermins */
	@Column(name = "Bemerkungen")
	@JsonProperty
	public String Bemerkungen;

	/** Gibt an, ob es sich bei dem Termin um den Haupttermin (1) handelt oder einen Nachschreibtermin (0). */
	@Column(name = "IstHaupttermin")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean IstHaupttermin;

	/** Gibt an, ob bei einem Haupttermin Nachschreibklausuren zugelassen sind (1) oder nicht (0). */
	@Column(name = "NachschreiberZugelassen")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean NachschreiberZugelassen;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenTermine ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostKlausurenTermine() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenTermine ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schuljahresabschnitt_ID   der Wert für das Attribut Schuljahresabschnitt_ID
	 * @param Abi_Jahrgang   der Wert für das Attribut Abi_Jahrgang
	 * @param Halbjahr   der Wert für das Attribut Halbjahr
	 * @param Quartal   der Wert für das Attribut Quartal
	 * @param IstHaupttermin   der Wert für das Attribut IstHaupttermin
	 * @param NachschreiberZugelassen   der Wert für das Attribut NachschreiberZugelassen
	 */
	public DTOGostKlausurenTermine(final long ID, final long Schuljahresabschnitt_ID, final int Abi_Jahrgang, final GostHalbjahr Halbjahr, final int Quartal, final Boolean IstHaupttermin, final Boolean NachschreiberZugelassen) {
		this.ID = ID;
		this.Schuljahresabschnitt_ID = Schuljahresabschnitt_ID;
		this.Abi_Jahrgang = Abi_Jahrgang;
		this.Halbjahr = Halbjahr;
		this.Quartal = Quartal;
		this.IstHaupttermin = IstHaupttermin;
		this.NachschreiberZugelassen = NachschreiberZugelassen;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostKlausurenTermine other = (DTOGostKlausurenTermine) obj;
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
		return "DTOGostKlausurenTermine(ID=" + this.ID + ", Schuljahresabschnitt_ID=" + this.Schuljahresabschnitt_ID + ", Abi_Jahrgang=" + this.Abi_Jahrgang + ", Halbjahr=" + this.Halbjahr + ", Quartal=" + this.Quartal + ", Datum=" + this.Datum + ", Startzeit=" + this.Startzeit + ", Bezeichnung=" + this.Bezeichnung + ", Bemerkungen=" + this.Bemerkungen + ", IstHaupttermin=" + this.IstHaupttermin + ", NachschreiberZugelassen=" + this.NachschreiberZugelassen + ")";
	}

}
