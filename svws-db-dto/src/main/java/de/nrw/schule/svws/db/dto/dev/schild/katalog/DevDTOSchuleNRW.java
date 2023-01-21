package de.nrw.schule.svws.db.dto.dev.schild.katalog;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultPlusConverter;


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
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Schule.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Schule")
@NamedQuery(name="DevDTOSchuleNRW.all", query="SELECT e FROM DevDTOSchuleNRW e")
@NamedQuery(name="DevDTOSchuleNRW.id", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.ID = :value")
@NamedQuery(name="DevDTOSchuleNRW.id.multiple", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOSchuleNRW.schulnr", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.SchulNr = :value")
@NamedQuery(name="DevDTOSchuleNRW.schulnr.multiple", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.SchulNr IN :value")
@NamedQuery(name="DevDTOSchuleNRW.name", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.Name = :value")
@NamedQuery(name="DevDTOSchuleNRW.name.multiple", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.Name IN :value")
@NamedQuery(name="DevDTOSchuleNRW.schulformnr", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.SchulformNr = :value")
@NamedQuery(name="DevDTOSchuleNRW.schulformnr.multiple", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.SchulformNr IN :value")
@NamedQuery(name="DevDTOSchuleNRW.schulformkrz", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.SchulformKrz = :value")
@NamedQuery(name="DevDTOSchuleNRW.schulformkrz.multiple", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.SchulformKrz IN :value")
@NamedQuery(name="DevDTOSchuleNRW.schulformbez", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.SchulformBez = :value")
@NamedQuery(name="DevDTOSchuleNRW.schulformbez.multiple", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.SchulformBez IN :value")
@NamedQuery(name="DevDTOSchuleNRW.strassenname", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.Strassenname = :value")
@NamedQuery(name="DevDTOSchuleNRW.strassenname.multiple", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.Strassenname IN :value")
@NamedQuery(name="DevDTOSchuleNRW.hausnr", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.HausNr = :value")
@NamedQuery(name="DevDTOSchuleNRW.hausnr.multiple", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.HausNr IN :value")
@NamedQuery(name="DevDTOSchuleNRW.hausnrzusatz", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.HausNrZusatz = :value")
@NamedQuery(name="DevDTOSchuleNRW.hausnrzusatz.multiple", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.HausNrZusatz IN :value")
@NamedQuery(name="DevDTOSchuleNRW.plz", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.PLZ = :value")
@NamedQuery(name="DevDTOSchuleNRW.plz.multiple", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.PLZ IN :value")
@NamedQuery(name="DevDTOSchuleNRW.ort", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.Ort = :value")
@NamedQuery(name="DevDTOSchuleNRW.ort.multiple", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.Ort IN :value")
@NamedQuery(name="DevDTOSchuleNRW.telefon", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.Telefon = :value")
@NamedQuery(name="DevDTOSchuleNRW.telefon.multiple", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.Telefon IN :value")
@NamedQuery(name="DevDTOSchuleNRW.fax", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.Fax = :value")
@NamedQuery(name="DevDTOSchuleNRW.fax.multiple", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.Fax IN :value")
@NamedQuery(name="DevDTOSchuleNRW.email", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.Email = :value")
@NamedQuery(name="DevDTOSchuleNRW.email.multiple", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.Email IN :value")
@NamedQuery(name="DevDTOSchuleNRW.schulleiter", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.Schulleiter = :value")
@NamedQuery(name="DevDTOSchuleNRW.schulleiter.multiple", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.Schulleiter IN :value")
@NamedQuery(name="DevDTOSchuleNRW.sortierung", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.Sortierung = :value")
@NamedQuery(name="DevDTOSchuleNRW.sortierung.multiple", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.Sortierung IN :value")
@NamedQuery(name="DevDTOSchuleNRW.sichtbar", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.Sichtbar = :value")
@NamedQuery(name="DevDTOSchuleNRW.sichtbar.multiple", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.Sichtbar IN :value")
@NamedQuery(name="DevDTOSchuleNRW.aenderbar", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.Aenderbar = :value")
@NamedQuery(name="DevDTOSchuleNRW.aenderbar.multiple", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.Aenderbar IN :value")
@NamedQuery(name="DevDTOSchuleNRW.schulnr_sim", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.SchulNr_SIM = :value")
@NamedQuery(name="DevDTOSchuleNRW.schulnr_sim.multiple", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.SchulNr_SIM IN :value")
@NamedQuery(name="DevDTOSchuleNRW.kuerzel", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.Kuerzel = :value")
@NamedQuery(name="DevDTOSchuleNRW.kuerzel.multiple", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.Kuerzel IN :value")
@NamedQuery(name="DevDTOSchuleNRW.kurzbez", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.KurzBez = :value")
@NamedQuery(name="DevDTOSchuleNRW.kurzbez.multiple", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.KurzBez IN :value")
@NamedQuery(name="DevDTOSchuleNRW.primaryKeyQuery", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOSchuleNRW.all.migration", query="SELECT e FROM DevDTOSchuleNRW e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","SchulNr","Name","SchulformNr","SchulformKrz","SchulformBez","Strassenname","HausNr","HausNrZusatz","PLZ","Ort","Telefon","Fax","Email","Schulleiter","Sortierung","Sichtbar","Aenderbar","SchulNr_SIM","Kuerzel","KurzBez"})
public class DevDTOSchuleNRW {

	/** ID des Eintrags der Schulen */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Schulnummer des Eintrags der Schulen */
	@Column(name = "SchulNr")
	@JsonProperty
	public String SchulNr;

	/** Nachname der Lehrkraft PAuswG vom 21.6.2019 §5 Abs. 2 */
	@Column(name = "Name")
	@JsonProperty
	public String Name;

	/** Schulformnummer des Eintrags der Schulen */
	@Column(name = "SchulformNr")
	@JsonProperty
	public String SchulformNr;

	/** Schulformkürzel des Eintrags der Schulen */
	@Column(name = "SchulformKrz")
	@JsonProperty
	public String SchulformKrz;

	/** Schulformbezeichnung des Eintrags der Schulen */
	@Column(name = "SchulformBez")
	@JsonProperty
	public String SchulformBez;

	/** Straßenname der Schule */
	@Column(name = "Strassenname")
	@JsonProperty
	public String Strassenname;

	/** Hausnummer wenn getrennt gespeichert */
	@Column(name = "HausNr")
	@JsonProperty
	public String HausNr;

	/** Hausnummerzusatz wenn getrennt gespeichert */
	@Column(name = "HausNrZusatz")
	@JsonProperty
	public String HausNrZusatz;

	/** PLZ des Eintrags der Schulen */
	@Column(name = "PLZ")
	@JsonProperty
	public String PLZ;

	/** Ort des Eintrags der Schulen */
	@Column(name = "Ort")
	@JsonProperty
	public String Ort;

	/** Telefonnummer des Eintrags der Schulen */
	@Column(name = "Telefon")
	@JsonProperty
	public String Telefon;

	/** Faxnummer des Eintrags der Schulen */
	@Column(name = "Fax")
	@JsonProperty
	public String Fax;

	/** E-MailAdresse des Eintrags der Schulen */
	@Column(name = "Email")
	@JsonProperty
	public String Email;

	/** Schulleitername des Eintrags der Schulen */
	@Column(name = "Schulleiter")
	@JsonProperty
	public String Schulleiter;

	/** Sortierung des Eintrags der Schulen */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichbarkeit des Eintrags der Schulen */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Änderbarkeit des Eintrags der Schulen */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/** Statistiklürzel Schulnummer des Eintrags der Schulen */
	@Column(name = "SchulNr_SIM")
	@JsonProperty
	public String SchulNr_SIM;

	/** Kürzel des Eintrags der Schulen */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Kutbezeichnung des Eintrags der Schulen */
	@Column(name = "KurzBez")
	@JsonProperty
	public String KurzBez;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOSchuleNRW ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOSchuleNRW() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOSchuleNRW ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param SchulNr   der Wert für das Attribut SchulNr
	 */
	public DevDTOSchuleNRW(final Long ID, final String SchulNr) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (SchulNr == null) { 
			throw new NullPointerException("SchulNr must not be null");
		}
		this.SchulNr = SchulNr;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevDTOSchuleNRW other = (DevDTOSchuleNRW) obj;
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
		return "DevDTOSchuleNRW(ID=" + this.ID + ", SchulNr=" + this.SchulNr + ", Name=" + this.Name + ", SchulformNr=" + this.SchulformNr + ", SchulformKrz=" + this.SchulformKrz + ", SchulformBez=" + this.SchulformBez + ", Strassenname=" + this.Strassenname + ", HausNr=" + this.HausNr + ", HausNrZusatz=" + this.HausNrZusatz + ", PLZ=" + this.PLZ + ", Ort=" + this.Ort + ", Telefon=" + this.Telefon + ", Fax=" + this.Fax + ", Email=" + this.Email + ", Schulleiter=" + this.Schulleiter + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ", SchulNr_SIM=" + this.SchulNr_SIM + ", Kuerzel=" + this.Kuerzel + ", KurzBez=" + this.KurzBez + ")";
	}

}