package de.svws_nrw.db.dto.current.gost.klausurplanung;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.svws_nrw.csv.converter.current.Boolean01ConverterSerializer;
import de.svws_nrw.csv.converter.current.Boolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Klausuren_Termine_Jahrgaenge.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOGostKlausurenTermineJahrgaengePK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Klausuren_Termine_Jahrgaenge")
@JsonPropertyOrder({"Termin_ID", "Abi_Jahrgang", "Quartal", "Bezeichnung", "Bemerkungen", "IstHaupttermin", "NachschreiberZugelassen"})
public final class DTOGostKlausurenTermineJahrgaenge {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.Termin_ID = ?1 AND e.Abi_Jahrgang = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.Termin_ID IS NOT NULL AND e.Abi_Jahrgang IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Termin_ID */
	public static final String QUERY_BY_TERMIN_ID = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.Termin_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Termin_ID */
	public static final String QUERY_LIST_BY_TERMIN_ID = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.Termin_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abi_Jahrgang */
	public static final String QUERY_BY_ABI_JAHRGANG = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.Abi_Jahrgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abi_Jahrgang */
	public static final String QUERY_LIST_BY_ABI_JAHRGANG = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.Abi_Jahrgang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Quartal */
	public static final String QUERY_BY_QUARTAL = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.Quartal = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Quartal */
	public static final String QUERY_LIST_BY_QUARTAL = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.Quartal IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.Bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.Bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bemerkungen */
	public static final String QUERY_BY_BEMERKUNGEN = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.Bemerkungen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bemerkungen */
	public static final String QUERY_LIST_BY_BEMERKUNGEN = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.Bemerkungen IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IstHaupttermin */
	public static final String QUERY_BY_ISTHAUPTTERMIN = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.IstHaupttermin = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IstHaupttermin */
	public static final String QUERY_LIST_BY_ISTHAUPTTERMIN = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.IstHaupttermin IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NachschreiberZugelassen */
	public static final String QUERY_BY_NACHSCHREIBERZUGELASSEN = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.NachschreiberZugelassen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NachschreiberZugelassen */
	public static final String QUERY_LIST_BY_NACHSCHREIBERZUGELASSEN = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.NachschreiberZugelassen IN ?1";

	/** Termin_ID des Klausurtermins */
	@Id
	@Column(name = "Termin_ID")
	@JsonProperty
	public long Termin_ID;

	/** Der Abiturjahrgang, der zum Klausurtermin zugelassen werden soll. */
	@Id
	@Column(name = "Abi_Jahrgang")
	@JsonProperty
	public int Abi_Jahrgang;

	/** Das Quartal, in dem die Klausur geschrieben wird. */
	@Column(name = "Quartal")
	@JsonProperty
	public int Quartal;

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
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenTermineJahrgaenge ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostKlausurenTermineJahrgaenge() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenTermineJahrgaenge ohne eine Initialisierung der Attribute.
	 * @param Termin_ID   der Wert für das Attribut Termin_ID
	 * @param Abi_Jahrgang   der Wert für das Attribut Abi_Jahrgang
	 * @param Quartal   der Wert für das Attribut Quartal
	 * @param IstHaupttermin   der Wert für das Attribut IstHaupttermin
	 * @param NachschreiberZugelassen   der Wert für das Attribut NachschreiberZugelassen
	 */
	public DTOGostKlausurenTermineJahrgaenge(final long Termin_ID, final int Abi_Jahrgang, final int Quartal, final Boolean IstHaupttermin, final Boolean NachschreiberZugelassen) {
		this.Termin_ID = Termin_ID;
		this.Abi_Jahrgang = Abi_Jahrgang;
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
		DTOGostKlausurenTermineJahrgaenge other = (DTOGostKlausurenTermineJahrgaenge) obj;
		if (Termin_ID != other.Termin_ID)
			return false;
		return Abi_Jahrgang == other.Abi_Jahrgang;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Termin_ID);

		result = prime * result + Integer.hashCode(Abi_Jahrgang);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOGostKlausurenTermineJahrgaenge(Termin_ID=" + this.Termin_ID + ", Abi_Jahrgang=" + this.Abi_Jahrgang + ", Quartal=" + this.Quartal + ", Bezeichnung=" + this.Bezeichnung + ", Bemerkungen=" + this.Bemerkungen + ", IstHaupttermin=" + this.IstHaupttermin + ", NachschreiberZugelassen=" + this.NachschreiberZugelassen + ")";
	}

}
