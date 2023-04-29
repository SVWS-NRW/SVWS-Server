package de.svws_nrw.db.dto.current.gost.klausurplanung;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.DatumConverter;
import de.svws_nrw.db.converter.current.UhrzeitConverter;
import de.svws_nrw.db.converter.current.gost.GOStHalbjahrConverter;

import de.svws_nrw.core.types.gost.GostHalbjahr;


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
@NamedQuery(name = "DTOGostKlausurenTermine.all", query = "SELECT e FROM DTOGostKlausurenTermine e")
@NamedQuery(name = "DTOGostKlausurenTermine.id", query = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.ID = :value")
@NamedQuery(name = "DTOGostKlausurenTermine.id.multiple", query = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.ID IN :value")
@NamedQuery(name = "DTOGostKlausurenTermine.abi_jahrgang", query = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.Abi_Jahrgang = :value")
@NamedQuery(name = "DTOGostKlausurenTermine.abi_jahrgang.multiple", query = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.Abi_Jahrgang IN :value")
@NamedQuery(name = "DTOGostKlausurenTermine.halbjahr", query = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.Halbjahr = :value")
@NamedQuery(name = "DTOGostKlausurenTermine.halbjahr.multiple", query = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.Halbjahr IN :value")
@NamedQuery(name = "DTOGostKlausurenTermine.quartal", query = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.Quartal = :value")
@NamedQuery(name = "DTOGostKlausurenTermine.quartal.multiple", query = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.Quartal IN :value")
@NamedQuery(name = "DTOGostKlausurenTermine.datum", query = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.Datum = :value")
@NamedQuery(name = "DTOGostKlausurenTermine.datum.multiple", query = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.Datum IN :value")
@NamedQuery(name = "DTOGostKlausurenTermine.startzeit", query = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.Startzeit = :value")
@NamedQuery(name = "DTOGostKlausurenTermine.startzeit.multiple", query = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.Startzeit IN :value")
@NamedQuery(name = "DTOGostKlausurenTermine.bezeichnung", query = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "DTOGostKlausurenTermine.bezeichnung.multiple", query = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "DTOGostKlausurenTermine.bemerkungen", query = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.Bemerkungen = :value")
@NamedQuery(name = "DTOGostKlausurenTermine.bemerkungen.multiple", query = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.Bemerkungen IN :value")
@NamedQuery(name = "DTOGostKlausurenTermine.primaryKeyQuery", query = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.ID = ?1")
@NamedQuery(name = "DTOGostKlausurenTermine.all.migration", query = "SELECT e FROM DTOGostKlausurenTermine e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Abi_Jahrgang", "Halbjahr", "Quartal", "Datum", "Startzeit", "Bezeichnung", "Bemerkungen"})
public final class DTOGostKlausurenTermine {

	/** ID des Klausurtermins (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

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
	public String Startzeit;

	/** Text für Bezeichnung des Klausurtermins */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Text für Bemerkungen des Klausurtermins */
	@Column(name = "Bemerkungen")
	@JsonProperty
	public String Bemerkungen;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenTermine ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostKlausurenTermine() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenTermine ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Abi_Jahrgang   der Wert für das Attribut Abi_Jahrgang
	 * @param Halbjahr   der Wert für das Attribut Halbjahr
	 * @param Quartal   der Wert für das Attribut Quartal
	 */
	public DTOGostKlausurenTermine(final long ID, final int Abi_Jahrgang, final GostHalbjahr Halbjahr, final int Quartal) {
		this.ID = ID;
		this.Abi_Jahrgang = Abi_Jahrgang;
		this.Halbjahr = Halbjahr;
		this.Quartal = Quartal;
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
		if (ID != other.ID)
			return false;
		return true;
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
		return "DTOGostKlausurenTermine(ID=" + this.ID + ", Abi_Jahrgang=" + this.Abi_Jahrgang + ", Halbjahr=" + this.Halbjahr + ", Quartal=" + this.Quartal + ", Datum=" + this.Datum + ", Startzeit=" + this.Startzeit + ", Bezeichnung=" + this.Bezeichnung + ", Bemerkungen=" + this.Bemerkungen + ")";
	}

}
