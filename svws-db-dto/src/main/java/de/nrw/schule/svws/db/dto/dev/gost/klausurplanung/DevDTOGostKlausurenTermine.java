package de.nrw.schule.svws.db.dto.dev.gost.klausurplanung;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.DatumConverter;
import de.nrw.schule.svws.db.converter.current.UhrzeitConverter;
import de.nrw.schule.svws.db.converter.current.gost.GOStHalbjahrConverter;

import de.nrw.schule.svws.core.types.gost.GostHalbjahr;


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
import de.nrw.schule.svws.csv.converter.current.DatumConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.DatumConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.UhrzeitConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.UhrzeitConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.gost.GOStHalbjahrConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.gost.GOStHalbjahrConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Klausuren_Termine.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Klausuren_Termine")
@NamedQuery(name="DevDTOGostKlausurenTermine.all", query="SELECT e FROM DevDTOGostKlausurenTermine e")
@NamedQuery(name="DevDTOGostKlausurenTermine.id", query="SELECT e FROM DevDTOGostKlausurenTermine e WHERE e.ID = :value")
@NamedQuery(name="DevDTOGostKlausurenTermine.id.multiple", query="SELECT e FROM DevDTOGostKlausurenTermine e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOGostKlausurenTermine.abi_jahrgang", query="SELECT e FROM DevDTOGostKlausurenTermine e WHERE e.Abi_Jahrgang = :value")
@NamedQuery(name="DevDTOGostKlausurenTermine.abi_jahrgang.multiple", query="SELECT e FROM DevDTOGostKlausurenTermine e WHERE e.Abi_Jahrgang IN :value")
@NamedQuery(name="DevDTOGostKlausurenTermine.halbjahr", query="SELECT e FROM DevDTOGostKlausurenTermine e WHERE e.Halbjahr = :value")
@NamedQuery(name="DevDTOGostKlausurenTermine.halbjahr.multiple", query="SELECT e FROM DevDTOGostKlausurenTermine e WHERE e.Halbjahr IN :value")
@NamedQuery(name="DevDTOGostKlausurenTermine.quartal", query="SELECT e FROM DevDTOGostKlausurenTermine e WHERE e.Quartal = :value")
@NamedQuery(name="DevDTOGostKlausurenTermine.quartal.multiple", query="SELECT e FROM DevDTOGostKlausurenTermine e WHERE e.Quartal IN :value")
@NamedQuery(name="DevDTOGostKlausurenTermine.datum", query="SELECT e FROM DevDTOGostKlausurenTermine e WHERE e.Datum = :value")
@NamedQuery(name="DevDTOGostKlausurenTermine.datum.multiple", query="SELECT e FROM DevDTOGostKlausurenTermine e WHERE e.Datum IN :value")
@NamedQuery(name="DevDTOGostKlausurenTermine.startzeit", query="SELECT e FROM DevDTOGostKlausurenTermine e WHERE e.Startzeit = :value")
@NamedQuery(name="DevDTOGostKlausurenTermine.startzeit.multiple", query="SELECT e FROM DevDTOGostKlausurenTermine e WHERE e.Startzeit IN :value")
@NamedQuery(name="DevDTOGostKlausurenTermine.bemerkungen", query="SELECT e FROM DevDTOGostKlausurenTermine e WHERE e.Bemerkungen = :value")
@NamedQuery(name="DevDTOGostKlausurenTermine.bemerkungen.multiple", query="SELECT e FROM DevDTOGostKlausurenTermine e WHERE e.Bemerkungen IN :value")
@NamedQuery(name="DevDTOGostKlausurenTermine.primaryKeyQuery", query="SELECT e FROM DevDTOGostKlausurenTermine e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOGostKlausurenTermine.all.migration", query="SELECT e FROM DevDTOGostKlausurenTermine e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Abi_Jahrgang","Halbjahr","Quartal","Datum","Startzeit","Bemerkungen"})
public class DevDTOGostKlausurenTermine {

	/** ID des Klausurtermins (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Der Abiturjahrgang, dem die Klausurvorgabe zugeordnet ist */
	@Column(name = "Abi_Jahrgang")
	@JsonProperty
	public Integer Abi_Jahrgang;

	/** Das Halbjahr, welchem die Klausurvorgabe zugeordnet ist (0=EF.1, 1=EF.2, 2=Q1.1, 3=Q1.2, 4=Q2.1, 5=Q2.2) */
	@Column(name = "Halbjahr")
	@JsonProperty
	@Convert(converter=GOStHalbjahrConverter.class)
	@JsonSerialize(using=GOStHalbjahrConverterSerializer.class)
	@JsonDeserialize(using=GOStHalbjahrConverterDeserializer.class)
	public GostHalbjahr Halbjahr;

	/** Das Quartal, in dem die Klausur geschrieben wird. */
	@Column(name = "Quartal")
	@JsonProperty
	public Integer Quartal;

	/** Das Datum des Klausurtermins */
	@Column(name = "Datum")
	@JsonProperty
	@Convert(converter=DatumConverter.class)
	@JsonSerialize(using=DatumConverterSerializer.class)
	@JsonDeserialize(using=DatumConverterDeserializer.class)
	public String Datum;

	/** Die Startzeit des Klausurtermins */
	@Column(name = "Startzeit")
	@JsonProperty
	@Convert(converter=UhrzeitConverter.class)
	@JsonSerialize(using=UhrzeitConverterSerializer.class)
	@JsonDeserialize(using=UhrzeitConverterDeserializer.class)
	public String Startzeit;

	/** Text für Bemerkungen zur Klausurvorlage */
	@Column(name = "Bemerkungen")
	@JsonProperty
	public String Bemerkungen;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOGostKlausurenTermine ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOGostKlausurenTermine() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOGostKlausurenTermine ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Abi_Jahrgang   der Wert für das Attribut Abi_Jahrgang
	 * @param Halbjahr   der Wert für das Attribut Halbjahr
	 * @param Quartal   der Wert für das Attribut Quartal
	 */
	public DevDTOGostKlausurenTermine(final Long ID, final Integer Abi_Jahrgang, final GostHalbjahr Halbjahr, final Integer Quartal) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Abi_Jahrgang == null) { 
			throw new NullPointerException("Abi_Jahrgang must not be null");
		}
		this.Abi_Jahrgang = Abi_Jahrgang;
		if (Halbjahr == null) { 
			throw new NullPointerException("Halbjahr must not be null");
		}
		this.Halbjahr = Halbjahr;
		if (Quartal == null) { 
			throw new NullPointerException("Quartal must not be null");
		}
		this.Quartal = Quartal;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevDTOGostKlausurenTermine other = (DevDTOGostKlausurenTermine) obj;
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
		return "DevDTOGostKlausurenTermine(ID=" + this.ID + ", Abi_Jahrgang=" + this.Abi_Jahrgang + ", Halbjahr=" + this.Halbjahr + ", Quartal=" + this.Quartal + ", Datum=" + this.Datum + ", Startzeit=" + this.Startzeit + ", Bemerkungen=" + this.Bemerkungen + ")";
	}

}