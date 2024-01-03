package de.svws_nrw.db.dto.current.gost.klausurplanung;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.NamedQuery;
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
@NamedQuery(name = "DTOGostKlausurenTermineJahrgaenge.all", query = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e")
@NamedQuery(name = "DTOGostKlausurenTermineJahrgaenge.termin_id", query = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.Termin_ID = :value")
@NamedQuery(name = "DTOGostKlausurenTermineJahrgaenge.termin_id.multiple", query = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.Termin_ID IN :value")
@NamedQuery(name = "DTOGostKlausurenTermineJahrgaenge.abi_jahrgang", query = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.Abi_Jahrgang = :value")
@NamedQuery(name = "DTOGostKlausurenTermineJahrgaenge.abi_jahrgang.multiple", query = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.Abi_Jahrgang IN :value")
@NamedQuery(name = "DTOGostKlausurenTermineJahrgaenge.quartal", query = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.Quartal = :value")
@NamedQuery(name = "DTOGostKlausurenTermineJahrgaenge.quartal.multiple", query = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.Quartal IN :value")
@NamedQuery(name = "DTOGostKlausurenTermineJahrgaenge.bezeichnung", query = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "DTOGostKlausurenTermineJahrgaenge.bezeichnung.multiple", query = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "DTOGostKlausurenTermineJahrgaenge.bemerkungen", query = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.Bemerkungen = :value")
@NamedQuery(name = "DTOGostKlausurenTermineJahrgaenge.bemerkungen.multiple", query = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.Bemerkungen IN :value")
@NamedQuery(name = "DTOGostKlausurenTermineJahrgaenge.isthaupttermin", query = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.IstHaupttermin = :value")
@NamedQuery(name = "DTOGostKlausurenTermineJahrgaenge.isthaupttermin.multiple", query = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.IstHaupttermin IN :value")
@NamedQuery(name = "DTOGostKlausurenTermineJahrgaenge.nachschreiberzugelassen", query = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.NachschreiberZugelassen = :value")
@NamedQuery(name = "DTOGostKlausurenTermineJahrgaenge.nachschreiberzugelassen.multiple", query = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.NachschreiberZugelassen IN :value")
@NamedQuery(name = "DTOGostKlausurenTermineJahrgaenge.primaryKeyQuery", query = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.Termin_ID = ?1 AND e.Abi_Jahrgang = ?2")
@NamedQuery(name = "DTOGostKlausurenTermineJahrgaenge.all.migration", query = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.Termin_ID IS NOT NULL AND e.Abi_Jahrgang IS NOT NULL")
@JsonPropertyOrder({"Termin_ID", "Abi_Jahrgang", "Quartal", "Bezeichnung", "Bemerkungen", "IstHaupttermin", "NachschreiberZugelassen"})
public final class DTOGostKlausurenTermineJahrgaenge {

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
