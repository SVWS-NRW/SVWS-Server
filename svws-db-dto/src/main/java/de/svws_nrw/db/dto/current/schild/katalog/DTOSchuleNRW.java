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
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Schule.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Schule")
@JsonPropertyOrder({"ID", "SchulNr", "Name", "SchulformNr", "SchulformKrz", "SchulformBez", "Strassenname", "HausNr", "HausNrZusatz", "PLZ", "Ort", "Telefon", "Fax", "Email", "Schulleiter", "Sortierung", "Sichtbar", "Aenderbar", "SchulNr_SIM", "Kuerzel", "KurzBez"})
public final class DTOSchuleNRW {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuleNRW e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuleNRW e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchuleNRW e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuleNRW e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchuleNRW e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchuleNRW e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulNr */
	public static final String QUERY_BY_SCHULNR = "SELECT e FROM DTOSchuleNRW e WHERE e.SchulNr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulNr */
	public static final String QUERY_LIST_BY_SCHULNR = "SELECT e FROM DTOSchuleNRW e WHERE e.SchulNr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Name */
	public static final String QUERY_BY_NAME = "SELECT e FROM DTOSchuleNRW e WHERE e.Name = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Name */
	public static final String QUERY_LIST_BY_NAME = "SELECT e FROM DTOSchuleNRW e WHERE e.Name IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulformNr */
	public static final String QUERY_BY_SCHULFORMNR = "SELECT e FROM DTOSchuleNRW e WHERE e.SchulformNr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulformNr */
	public static final String QUERY_LIST_BY_SCHULFORMNR = "SELECT e FROM DTOSchuleNRW e WHERE e.SchulformNr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulformKrz */
	public static final String QUERY_BY_SCHULFORMKRZ = "SELECT e FROM DTOSchuleNRW e WHERE e.SchulformKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulformKrz */
	public static final String QUERY_LIST_BY_SCHULFORMKRZ = "SELECT e FROM DTOSchuleNRW e WHERE e.SchulformKrz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulformBez */
	public static final String QUERY_BY_SCHULFORMBEZ = "SELECT e FROM DTOSchuleNRW e WHERE e.SchulformBez = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulformBez */
	public static final String QUERY_LIST_BY_SCHULFORMBEZ = "SELECT e FROM DTOSchuleNRW e WHERE e.SchulformBez IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Strassenname */
	public static final String QUERY_BY_STRASSENNAME = "SELECT e FROM DTOSchuleNRW e WHERE e.Strassenname = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Strassenname */
	public static final String QUERY_LIST_BY_STRASSENNAME = "SELECT e FROM DTOSchuleNRW e WHERE e.Strassenname IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes HausNr */
	public static final String QUERY_BY_HAUSNR = "SELECT e FROM DTOSchuleNRW e WHERE e.HausNr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes HausNr */
	public static final String QUERY_LIST_BY_HAUSNR = "SELECT e FROM DTOSchuleNRW e WHERE e.HausNr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes HausNrZusatz */
	public static final String QUERY_BY_HAUSNRZUSATZ = "SELECT e FROM DTOSchuleNRW e WHERE e.HausNrZusatz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes HausNrZusatz */
	public static final String QUERY_LIST_BY_HAUSNRZUSATZ = "SELECT e FROM DTOSchuleNRW e WHERE e.HausNrZusatz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PLZ */
	public static final String QUERY_BY_PLZ = "SELECT e FROM DTOSchuleNRW e WHERE e.PLZ = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PLZ */
	public static final String QUERY_LIST_BY_PLZ = "SELECT e FROM DTOSchuleNRW e WHERE e.PLZ IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Ort */
	public static final String QUERY_BY_ORT = "SELECT e FROM DTOSchuleNRW e WHERE e.Ort = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Ort */
	public static final String QUERY_LIST_BY_ORT = "SELECT e FROM DTOSchuleNRW e WHERE e.Ort IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Telefon */
	public static final String QUERY_BY_TELEFON = "SELECT e FROM DTOSchuleNRW e WHERE e.Telefon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Telefon */
	public static final String QUERY_LIST_BY_TELEFON = "SELECT e FROM DTOSchuleNRW e WHERE e.Telefon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fax */
	public static final String QUERY_BY_FAX = "SELECT e FROM DTOSchuleNRW e WHERE e.Fax = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fax */
	public static final String QUERY_LIST_BY_FAX = "SELECT e FROM DTOSchuleNRW e WHERE e.Fax IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Email */
	public static final String QUERY_BY_EMAIL = "SELECT e FROM DTOSchuleNRW e WHERE e.Email = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Email */
	public static final String QUERY_LIST_BY_EMAIL = "SELECT e FROM DTOSchuleNRW e WHERE e.Email IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schulleiter */
	public static final String QUERY_BY_SCHULLEITER = "SELECT e FROM DTOSchuleNRW e WHERE e.Schulleiter = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schulleiter */
	public static final String QUERY_LIST_BY_SCHULLEITER = "SELECT e FROM DTOSchuleNRW e WHERE e.Schulleiter IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM DTOSchuleNRW e WHERE e.Sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM DTOSchuleNRW e WHERE e.Sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sichtbar */
	public static final String QUERY_BY_SICHTBAR = "SELECT e FROM DTOSchuleNRW e WHERE e.Sichtbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sichtbar */
	public static final String QUERY_LIST_BY_SICHTBAR = "SELECT e FROM DTOSchuleNRW e WHERE e.Sichtbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Aenderbar */
	public static final String QUERY_BY_AENDERBAR = "SELECT e FROM DTOSchuleNRW e WHERE e.Aenderbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Aenderbar */
	public static final String QUERY_LIST_BY_AENDERBAR = "SELECT e FROM DTOSchuleNRW e WHERE e.Aenderbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulNr_SIM */
	public static final String QUERY_BY_SCHULNR_SIM = "SELECT e FROM DTOSchuleNRW e WHERE e.SchulNr_SIM = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulNr_SIM */
	public static final String QUERY_LIST_BY_SCHULNR_SIM = "SELECT e FROM DTOSchuleNRW e WHERE e.SchulNr_SIM IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kuerzel */
	public static final String QUERY_BY_KUERZEL = "SELECT e FROM DTOSchuleNRW e WHERE e.Kuerzel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kuerzel */
	public static final String QUERY_LIST_BY_KUERZEL = "SELECT e FROM DTOSchuleNRW e WHERE e.Kuerzel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KurzBez */
	public static final String QUERY_BY_KURZBEZ = "SELECT e FROM DTOSchuleNRW e WHERE e.KurzBez = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KurzBez */
	public static final String QUERY_LIST_BY_KURZBEZ = "SELECT e FROM DTOSchuleNRW e WHERE e.KurzBez IN ?1";

	/** ID des Eintrags der Schulen */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

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
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Änderbarkeit des Eintrags der Schulen */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
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
	 * Erstellt ein neues Objekt der Klasse DTOSchuleNRW ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuleNRW() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuleNRW ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param SchulNr   der Wert für das Attribut SchulNr
	 */
	public DTOSchuleNRW(final long ID, final String SchulNr) {
		this.ID = ID;
		if (SchulNr == null) {
			throw new NullPointerException("SchulNr must not be null");
		}
		this.SchulNr = SchulNr;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchuleNRW other = (DTOSchuleNRW) obj;
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
		return "DTOSchuleNRW(ID=" + this.ID + ", SchulNr=" + this.SchulNr + ", Name=" + this.Name + ", SchulformNr=" + this.SchulformNr + ", SchulformKrz=" + this.SchulformKrz + ", SchulformBez=" + this.SchulformBez + ", Strassenname=" + this.Strassenname + ", HausNr=" + this.HausNr + ", HausNrZusatz=" + this.HausNrZusatz + ", PLZ=" + this.PLZ + ", Ort=" + this.Ort + ", Telefon=" + this.Telefon + ", Fax=" + this.Fax + ", Email=" + this.Email + ", Schulleiter=" + this.Schulleiter + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ", SchulNr_SIM=" + this.SchulNr_SIM + ", Kuerzel=" + this.Kuerzel + ", KurzBez=" + this.KurzBez + ")";
	}

}
