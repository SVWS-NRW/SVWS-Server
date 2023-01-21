package de.nrw.schule.svws.db.dto.dev.gost.klausurplanung;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.Boolean01Converter;


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
import de.nrw.schule.svws.csv.converter.current.Boolean01ConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.Boolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Klausuren_Kalenderinformationen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Klausuren_Kalenderinformationen")
@NamedQuery(name="DevDTOGostKlausurenKalenderinformationen.all", query="SELECT e FROM DevDTOGostKlausurenKalenderinformationen e")
@NamedQuery(name="DevDTOGostKlausurenKalenderinformationen.id", query="SELECT e FROM DevDTOGostKlausurenKalenderinformationen e WHERE e.ID = :value")
@NamedQuery(name="DevDTOGostKlausurenKalenderinformationen.id.multiple", query="SELECT e FROM DevDTOGostKlausurenKalenderinformationen e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOGostKlausurenKalenderinformationen.bezeichnung", query="SELECT e FROM DevDTOGostKlausurenKalenderinformationen e WHERE e.Bezeichnung = :value")
@NamedQuery(name="DevDTOGostKlausurenKalenderinformationen.bezeichnung.multiple", query="SELECT e FROM DevDTOGostKlausurenKalenderinformationen e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="DevDTOGostKlausurenKalenderinformationen.startdatum", query="SELECT e FROM DevDTOGostKlausurenKalenderinformationen e WHERE e.Startdatum = :value")
@NamedQuery(name="DevDTOGostKlausurenKalenderinformationen.startdatum.multiple", query="SELECT e FROM DevDTOGostKlausurenKalenderinformationen e WHERE e.Startdatum IN :value")
@NamedQuery(name="DevDTOGostKlausurenKalenderinformationen.startzeit", query="SELECT e FROM DevDTOGostKlausurenKalenderinformationen e WHERE e.Startzeit = :value")
@NamedQuery(name="DevDTOGostKlausurenKalenderinformationen.startzeit.multiple", query="SELECT e FROM DevDTOGostKlausurenKalenderinformationen e WHERE e.Startzeit IN :value")
@NamedQuery(name="DevDTOGostKlausurenKalenderinformationen.enddatum", query="SELECT e FROM DevDTOGostKlausurenKalenderinformationen e WHERE e.Enddatum = :value")
@NamedQuery(name="DevDTOGostKlausurenKalenderinformationen.enddatum.multiple", query="SELECT e FROM DevDTOGostKlausurenKalenderinformationen e WHERE e.Enddatum IN :value")
@NamedQuery(name="DevDTOGostKlausurenKalenderinformationen.endzeit", query="SELECT e FROM DevDTOGostKlausurenKalenderinformationen e WHERE e.Endzeit = :value")
@NamedQuery(name="DevDTOGostKlausurenKalenderinformationen.endzeit.multiple", query="SELECT e FROM DevDTOGostKlausurenKalenderinformationen e WHERE e.Endzeit IN :value")
@NamedQuery(name="DevDTOGostKlausurenKalenderinformationen.istsperrtermin", query="SELECT e FROM DevDTOGostKlausurenKalenderinformationen e WHERE e.IstSperrtermin = :value")
@NamedQuery(name="DevDTOGostKlausurenKalenderinformationen.istsperrtermin.multiple", query="SELECT e FROM DevDTOGostKlausurenKalenderinformationen e WHERE e.IstSperrtermin IN :value")
@NamedQuery(name="DevDTOGostKlausurenKalenderinformationen.bemerkungen", query="SELECT e FROM DevDTOGostKlausurenKalenderinformationen e WHERE e.Bemerkungen = :value")
@NamedQuery(name="DevDTOGostKlausurenKalenderinformationen.bemerkungen.multiple", query="SELECT e FROM DevDTOGostKlausurenKalenderinformationen e WHERE e.Bemerkungen IN :value")
@NamedQuery(name="DevDTOGostKlausurenKalenderinformationen.primaryKeyQuery", query="SELECT e FROM DevDTOGostKlausurenKalenderinformationen e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOGostKlausurenKalenderinformationen.all.migration", query="SELECT e FROM DevDTOGostKlausurenKalenderinformationen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Bezeichnung","Startdatum","Startzeit","Enddatum","Endzeit","IstSperrtermin","Bemerkungen"})
public class DevDTOGostKlausurenKalenderinformationen {

	/** ID der Kalenderinformation (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Text für Bezeichnung der Kalenderinformation */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Startdatum für den Kalendereintrag */
	@Column(name = "Startdatum")
	@JsonProperty
	public String Startdatum;

	/** Startzeit für den Kalendereintrag */
	@Column(name = "Startzeit")
	@JsonProperty
	public String Startzeit;

	/** Enddatum für den Kalendereintrag */
	@Column(name = "Enddatum")
	@JsonProperty
	public String Enddatum;

	/** Endzeit für den Kalendereintrag */
	@Column(name = "Endzeit")
	@JsonProperty
	public String Endzeit;

	/** Gibt an, ob es sich um einen Sperrtermin handelt oder nicht: 1 - true, 0 - false. */
	@Column(name = "IstSperrtermin")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean IstSperrtermin;

	/** Text für Bemerkungen zur Kalenderinformation */
	@Column(name = "Bemerkungen")
	@JsonProperty
	public String Bemerkungen;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOGostKlausurenKalenderinformationen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOGostKlausurenKalenderinformationen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOGostKlausurenKalenderinformationen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param IstSperrtermin   der Wert für das Attribut IstSperrtermin
	 */
	public DevDTOGostKlausurenKalenderinformationen(final Long ID, final Boolean IstSperrtermin) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (IstSperrtermin == null) { 
			throw new NullPointerException("IstSperrtermin must not be null");
		}
		this.IstSperrtermin = IstSperrtermin;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevDTOGostKlausurenKalenderinformationen other = (DevDTOGostKlausurenKalenderinformationen) obj;
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
		return "DevDTOGostKlausurenKalenderinformationen(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Startdatum=" + this.Startdatum + ", Startzeit=" + this.Startzeit + ", Enddatum=" + this.Enddatum + ", Endzeit=" + this.Endzeit + ", IstSperrtermin=" + this.IstSperrtermin + ", Bemerkungen=" + this.Bemerkungen + ")";
	}

}